package cn.bforce.common.persistence.repository;


import java.io.Serializable;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.commons.collections.map.CaseInsensitiveMap;
import org.apache.commons.lang.reflect.MethodUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import com.alibaba.fastjson.JSON;

import cn.bforce.common.persistence.DataObject;
import cn.bforce.common.persistence.NamedQuery;
import cn.bforce.common.persistence.OperationException;
import cn.bforce.common.persistence.TableColumn;
import cn.bforce.common.utils.ExArrayList;
import cn.bforce.common.utils.ExDateUtils;
import cn.bforce.common.utils.ExStringUtils;
import cn.bforce.common.utils.web.ExSQLUtils;


public class DataRepositoryJDBCBf implements DataRepository
{

    protected final Logger log = LogManager.getLogger(getClass());

    @Autowired
    @Qualifier("bfJdbcTemplate")
    protected JdbcTemplate jdbcTemp;

    @Autowired
    @Qualifier("bfDataSource")
    protected DataSource dataSource;

    // 值列表
    protected Map columnValueListMap = new CaseInsensitiveMap();

    protected String masterTable;

    protected String masterTablePK;

    protected String loginUserId;

    protected String orderBy;

    protected String databaseType;

    protected String valueListName;

    protected boolean isAutoKey = true;

    private List<NamedQuery> namedQuerys = new ArrayList();

    public DataRepositoryJDBCBf()
    {

    }

    public DataRepositoryJDBCBf(int clientId, String loginUserId)
    {
        this.loginUserId = loginUserId;
    }

    public DataSource getDataSource()
    {
        return this.dataSource;
    }

    public void setDataSource(DataSource dataSource)
    {
        this.dataSource = dataSource;
        this.getJdbcTemplate().setDataSource(dataSource);
    }

    public JdbcTemplate getJdbcTemplate()
    {
        return this.jdbcTemp;
    }

    public void setJdbcTemplate(JdbcTemplate jdbcTemp)
    {
        this.jdbcTemp = jdbcTemp;
    }

    /**
     * 获取表的字段信息
     * 
     * @return
     */
    protected String[] getTableColumnNames()
    {
        List<TableColumn> columnList = loadTableColumns();
        String[] columnNames = new String[columnList.size()];

        for (int i = 0; i < columnList.size(); i++ )
        {
            columnNames[i] = columnList.get(i).getColumnName();
        }

        return columnNames;
    }

    protected List<TableColumn> loadTableColumns()
    {

        String sql = " select * from " + this.masterTable + " where 1 = 0";

        List<TableColumn> columnList = getJdbcTemplate().query(sql, new ResultSetExtractor<List>()
        {
            @Override
            public List extractData(ResultSet rs)
                throws SQLException, DataAccessException
            {
                int count = rs.getMetaData().getColumnCount();
                final List<TableColumn> columnList = new ArrayList();
                for (int i = 0; i < count; i++ )
                {
                    TableColumn tableColumn = new TableColumn();
                    tableColumn.setColumnDisplaySize(rs.getMetaData().getColumnDisplaySize(i + 1));
                    tableColumn.setColumnLabel(rs.getMetaData().getColumnLabel(i + 1));
                    tableColumn.setColumnName(rs.getMetaData().getColumnName(i + 1));
                    tableColumn.setColumnType(rs.getMetaData().getColumnTypeName(i + 1));
                    columnList.add(tableColumn);
                }
                return columnList;
            }

        });

        return columnList;

    }

    protected String getDatabaseType()
    {
        if (this.databaseType != null)
        {
            return this.databaseType;
        }

        Connection conn = null;
        try
        {
            conn = getJdbcTemplate().getDataSource().getConnection();
            String driverName = conn.getMetaData().getDriverName();
            if (driverName != null)
            {
                if (driverName.toLowerCase().indexOf("oracle") >= 0)
                {
                    databaseType = "oracle";
                }
                else if (driverName.toLowerCase().indexOf("mysql") >= 0)
                {
                    databaseType = "mysql";
                }
                else if (driverName.toLowerCase().indexOf("sqlserver") >= 0
                         || driverName.toLowerCase().indexOf("microsoft") >= 0)
                {
                    databaseType = "mssql";
                }
                else if (driverName.toLowerCase().indexOf("postgresql") >= 0)
                {
                    databaseType = "postgresql";
                }
                else
                {
                    databaseType = "mysql";
                }

            }

        }
        catch (SQLException e)
        {
            log.error(e.getMessage(), e);
        }
        finally
        {
            if (conn != null)
            {
                try
                {
                    conn.close();
                }
                catch (SQLException e)
                {
                    throw new OperationException(e);
                }
            }
        }

        return this.databaseType;
    }

    @Override
    public String getMasterTablePK()
    {
        return this.masterTablePK;
    }

    @Override
    public void setMasterTablePK(String masterTablePK)
    {
        this.masterTablePK = masterTablePK;
    }

    @Override
    public String getMasterTable()
    {
        return this.masterTable;
    }

    @Override
    public void setMasterTable(String masterTable)
    {
        this.masterTable = masterTable;

    }

    @Override
    public void setCaller(String setCaller)
    {
        this.loginUserId = setCaller;
    }

    @Override
    public void setLoginUserId(String loginUserId)
    {
        this.loginUserId = loginUserId;
    }

    public String getLoginUserId()
    {
        return loginUserId;
    }

    @Override
    public String getOrderBy()
    {
        return this.orderBy;
    }

    @Override
    public void setOrderBy(String orderBy)
    {
        this.orderBy = orderBy;
    }

    @Override
    public String getSelectSQL()
    {
        String sql = "select * from " + this.masterTable + " where 1=1 ";
        return sql;
    }

    @Override
    public List queryDataBySQL(String sql, Object[] parameters)
    {

        log.debug("查询SQL=" + sql);
        List dataList = getJdbcTemplate().queryForList(sql, parameters);

        List resultList = new ArrayList();
        for (Object item : dataList)
        {
            Map itemMap = (Map)item;
            Map rowMap = new CaseInsensitiveMap();
            rowMap.putAll(itemMap);
            resultList.add(rowMap);
        }

        return resultList;
    }

    @Override
    public List queryData(String filter, Map paramMap, int pageIndex, int pageSize)
    {

        StringBuilder whereClause = new StringBuilder();

        Object[] paramObjs = null;
        NamedQuery namedQuery = this.findNamedQuery(filter);
        if (namedQuery != null)
        {
            if (namedQuery.isInvokeMethod())
            {
                if ("all".equalsIgnoreCase(filter))
                {
                    filter = "queryAll";
                }
                return (List)this.invokeCommandMethod(filter, paramMap);
            }
            else
            {
                whereClause.append(namedQuery.getWhere());
                paramObjs = namedQuery.buildParamValues(paramMap);
            }
        }
        else if ("none".equalsIgnoreCase(filter))
        {
            whereClause.append("1 = 0");
        }
        else if (filter != null && filter.startsWith("{") && filter.endsWith("}"))
        {
            filter = filter.replace("{", "");
            filter = filter.replace("}", "");
            String[] tmpValues = ExStringUtils.split(filter, "|");
            whereClause.append(tmpValues[0] + " = '" + tmpValues[1] + "'");
        }

        String command = (String)paramMap.get("command");
        if ("search".equalsIgnoreCase(command) && namedQuery == null)
        {
            paramObjs = ExSQLUtils.buildWhere(whereClause, paramMap);
        }

        String aOrderBy = this.getOrderBy();
        if (paramMap.containsKey("sort"))
        {
            aOrderBy = paramMap.get("sort") + " " + paramMap.get("order");
        }

        List resultList = this.queryDataByPage(whereClause.toString(), aOrderBy, pageIndex,
            pageSize, paramObjs);

        return resultList;
    }

    @Override
    public List queryDataByPage(String whereClause, String aOrderBy, int pageIndex, int pageSize,
                                Object[] parameters)
    {

        String sql = getSelectSQL();
        if (ExStringUtils.isNotBlank(whereClause))
        {
            sql = sql + " and " + whereClause;
        }

        if (ExStringUtils.isBlank(aOrderBy))
        {
            sql = sql + " order by " + this.masterTablePK + " desc ";
        }
        else
        {
            sql = sql + " order by " + aOrderBy;
        }

        return queryDataByPage(sql, pageIndex, pageSize, parameters);

    }

    @Override
    public List queryDataByPage(String sql, int pageIndex, int pageSize, Object[] parameters)
    {

        String rowCountSQL = ExSQLUtils.buildRowCountSQL(this.getDatabaseType(), sql);
        if (pageSize > 0)
        {
            // 行统计SQL
            sql = ExSQLUtils.buildPageableSQL(this.getDatabaseType(), sql, pageSize, pageIndex);

        }

        log.debug("查询SQL=" + sql);
        List dataList = getJdbcTemplate().queryForList(sql, parameters);

        ExArrayList resultList = new ExArrayList();
        for (Object item : dataList)
        {
            Map itemMap = (Map)item;
            Map rowMap = new CaseInsensitiveMap();
            rowMap.putAll(itemMap);
            resultList.add(rowMap);
        }

        resultList.setTotal(getRowCount(rowCountSQL, parameters));
        return resultList;

    }

    @Override
    public List queryData(String whereClause, String aOrderBy, Object[] parameters)
    {
        return this.queryDataByPage(whereClause, aOrderBy, 0, 0, parameters);
    }

    private int getRowCount(String sql, Object[] parameters)
    {
        if (sql == null)
        {
            log.error("SQL IS NULL");
            return 0;
        }
        Map rowData = getJdbcTemplate().queryForMap(sql, parameters);
        Object value = rowData.get("COUNT(*)");
        if (value == null)
        {
            value = rowData.get("COUNT(1)");
        }
        if (value == null)
        {
            value = rowData.get("");
        }
        if (value == null)
        {
            return 0;
        }
        return ExStringUtils.getIntValue(value);
    }

    @Override
    public Map findOneBySQL(String sql, Object[] parameters)
    {

        log.debug("查询SQL=" + sql);

        try
        {
            Map dataMap = getJdbcTemplate().queryForMap(sql, parameters);
            return new CaseInsensitiveMap(dataMap);
        }
        catch (EmptyResultDataAccessException ex)
        {

        }

        return new HashMap();
    }

    @Override
    public Map findOne(String whereClause, Object[] parameters)
    {

        String sql = this.getSelectSQL();

        if (ExStringUtils.isNotBlank(whereClause))
        {
            sql = sql + " and " + whereClause;
        }
        return findOneBySQL(sql, parameters);
    }

    @Override
    public Map findOne(Map paramMap)
    {
        StringBuilder whereClause = new StringBuilder();
        Object[] paramObjs = ExSQLUtils.buildWhere(whereClause, paramMap);
        return this.findOne(whereClause.toString(), paramObjs);
    }

    @Override
    public void clearRowMetaData(Map dataMap)
    {

    }

    @Override
    public DataObject doLoad(Serializable rowId)
    {
        String sql = "select * from " + this.masterTable + " where  " + this.masterTablePK
                     + " = ? ";
        log.debug("查询SQL=" + sql);

        Map dataMap = new HashMap();
        try
        {
            dataMap = getJdbcTemplate().queryForMap(sql, rowId);
            dataMap = new CaseInsensitiveMap(dataMap);
        }
        catch (EmptyResultDataAccessException ex)
        {

        }
        return new DataObject(dataMap);
    }

    @Override
    public int doDelete(Serializable rowId)
    {
        String sql = "delete from " + this.masterTable + " where  " + this.masterTablePK + " = ? ";

        int result = getJdbcTemplate().update(sql, rowId);
        log.debug("删除SQL=" + sql + ", 执行结果=" + result);
        return result;
    }

    @Override
    public Map createNewRow()
    {
        Map dataMap = new DataObject();
        dataMap.put("created", new Date());
        return dataMap;
    }

    @Override
    public int[] doBatchInsert(List<Map> dataList)
    {

        List<TableColumn> tableColumns = loadTableColumns();
        String[] tableColumnNames = getTableColumnNames();

        String insertSQL = null;
        int index = 0;

        SqlParameterSource[] batchArgs = new SqlParameterSource[dataList.size()];

        for (Map rowData : dataList)
        {

            Object createdTime = rowData.get("reg_time");
            if (createdTime == null || "".equalsIgnoreCase(createdTime.toString()))
            {
                rowData.put("reg_time", new Date());
            }
            rowData.put("create_user", this.loginUserId);

            for (TableColumn tableColumn : tableColumns)
            {
                Object value = rowData.get(tableColumn.getColumnName());
                // 转换日期字段
                String columnType = tableColumn.getColumnType();
                if (value != null && ("Date".equalsIgnoreCase(columnType)
                                      || "DateTime".equalsIgnoreCase(columnType)
                                      || "Timestamp".equalsIgnoreCase(columnType)))
                {
                    Date date = null;
                    if (value instanceof String)
                    {
                        date = ExDateUtils.parseDate((String)value);
                    }
                    else if (value instanceof Long)
                    {
                        date = ExDateUtils.parseDate((Long)value);
                    }
                    if (date != null)
                    {
                        rowData.put(tableColumn.getColumnName(), date);
                    }
                }
            }

            log.debug("SQL:" + insertSQL + ", params = " + rowData);

            batchArgs[index++ ] = new MapSqlParameterSource(new CaseInsensitiveMap(rowData));
            if (insertSQL == null)
            {
                insertSQL = ExSQLUtils.buildInsertSQL(this.masterTable, tableColumnNames, rowData);
            }
        }

        // :后面的名称必须和Bean属性名称一样
        NamedParameterJdbcTemplate namedJdbcTemp = new NamedParameterJdbcTemplate(getDataSource());
        int[] results = namedJdbcTemp.batchUpdate(insertSQL, batchArgs);

        return results;

    }

    @Override
    public Serializable doInsert(Map dataMap)
    {
        List<TableColumn> tableColumns = loadTableColumns();
        String[] tableColumnNames = getTableColumnNames();

        Object createdTime = dataMap.get("reg_time");
        if (createdTime == null || "".equalsIgnoreCase(createdTime.toString()))
        {
            dataMap.put("reg_time", new Date());
        }
        dataMap.put("create_user", this.loginUserId);

        String insertSQL = ExSQLUtils.buildInsertSQL(this.masterTable, tableColumnNames, dataMap);

        for (TableColumn tableColumn : tableColumns)
        {
            Object value = dataMap.get(tableColumn.getColumnName());
            // 转换日期字段
            String columnType = tableColumn.getColumnType();
            if (value != null
                && ("Date".equalsIgnoreCase(columnType) || "DateTime".equalsIgnoreCase(columnType)
                    || "Timestamp".equalsIgnoreCase(columnType)))
            {
                Date date = null;
                if (value instanceof String)
                {
                    date = ExDateUtils.parseDate((String)value);
                }
                else if (value instanceof Long)
                {
                    date = ExDateUtils.parseDate((Long)value);
                }
                if (date != null)
                {
                    dataMap.put(tableColumn.getColumnName(), date);
                }
            }
        }

        log.debug("SQL:" + insertSQL + ", params = " + dataMap);
        // :后面的名称必须和Bean属性名称一样
        NamedParameterJdbcTemplate namedJdbcTemp = new NamedParameterJdbcTemplate(getDataSource());

        SqlParameterSource sqlParameterSource = new MapSqlParameterSource(
            new CaseInsensitiveMap(dataMap));
        KeyHolder keyHolder = new GeneratedKeyHolder();

        // 加上KeyHolder这个参数可以得到添加后主键的值
        try
        {

            int result = -1;
            if (this.isAutoKey && ExStringUtils.isNotBlank(this.masterTablePK))
            {
                result = namedJdbcTemp.update(insertSQL, sqlParameterSource, keyHolder,
                    new String[] {this.masterTablePK});
                Number rowKey = keyHolder.getKey();
                if (rowKey != null && rowKey instanceof Number)
                {
                    return rowKey.longValue();
                }
            }
            else
            {
                result = namedJdbcTemp.update(insertSQL, sqlParameterSource);
            }

            return Long.valueOf(result);

        }
        catch (DataRetrievalFailureException ex)
        {
            log.error(ex.getMessage());
        }
        catch (Exception ex)
        {
            throw new OperationException(ex);
        }

        return null;

    }

    @Override
    public int doUpdate(String sql, Object... args)
    {
        return this.getJdbcTemplate().update(sql, args);
    }

    @Override
    public int doUpdate(Serializable rowId, Map dataMap)
    {

        List<TableColumn> tableColumns = loadTableColumns();
        String[] tableColumnNames = getTableColumnNames();

        dataMap.put(this.masterTablePK, rowId);
        dataMap.remove("reg_time"); // 去掉输入时间的字段

        Map oldDataMap = this.doLoad(rowId);
        String updateSQL = ExSQLUtils.buildUpdateSQL(this.masterTable, this.masterTablePK,
            tableColumnNames, dataMap, oldDataMap);
        log.debug("SQL:" + updateSQL + ", params = " + dataMap);
        if (ExStringUtils.isBlank(updateSQL))
        {
            return 0;
        }

        for (TableColumn tableColumn : tableColumns)
        {
            Object value = dataMap.get(tableColumn.getColumnName());
            // 转换日期字段
            String columnType = tableColumn.getColumnType();
            if (value != null
                && ("Date".equalsIgnoreCase(columnType) || "DateTime".equalsIgnoreCase(columnType)
                    || "Timestamp".equalsIgnoreCase(columnType)))
            {
                Date date = null;
                if (value instanceof String)
                {
                    date = ExDateUtils.parseDate((String)value);
                }
                else if (value instanceof Long)
                {
                    date = ExDateUtils.parseDate((Long)value);
                }
                if (date != null)
                {
                    dataMap.put(tableColumn.getColumnName(), date);
                }
            }
        }

        // :后面的名称必须和Bean属性名称一样
        NamedParameterJdbcTemplate namedJdbcTemp = new NamedParameterJdbcTemplate(getDataSource());
        SqlParameterSource sqlParameterSource = new MapSqlParameterSource(
            new CaseInsensitiveMap(dataMap));

        int result = namedJdbcTemp.update(updateSQL, sqlParameterSource);
        return result;

    }

    @Override
    public String doAction(String oId, Map parameters)
        throws OperationException
    {

        String command = (String)parameters.get("command");
        if ("batchDelete".equalsIgnoreCase(command))
        {
            String rowIds = (String)parameters.get("rowIds");
            String[] rowIdArray = ExStringUtils.split(rowIds, ",");

            int deleteRows = 0;
            for (String rowId : rowIdArray)
            {
                int result = this.doDelete(rowId);
                deleteRows += result;
            }
            return "删除成功";
        }
        else if (ExStringUtils.isNotBlank(command))
        {
            String methodName = "do" + command;
            Object cmdResult = this.invokeCommandMethod(methodName, parameters);
            if (cmdResult instanceof String)
            {
                return (String)cmdResult;
            }
            return JSON.toJSONString(cmdResult);
        }

        return "未实现的指令";
    }

    @Override
    public NamedQuery findNamedQuery(String name)
    {
        NamedQuery query = null;
        for (int i = 0; i < this.namedQuerys.size(); i++ )
        {
            query = (NamedQuery)this.namedQuerys.get(i);
            if (name != null && name.equalsIgnoreCase(query.getName()))
            {
                return query;
            }
        }
        return null;
    }

    @Override
    public Map getColumnValueListMap()
    {
        return columnValueListMap;
    }

    protected void setUpTableColumns()
    {

    }

    public void addNamedQuery(NamedQuery namedQuery)
    {
        this.namedQuerys.add(namedQuery);
    }

    public String reName(String table)
    {
        return table;
    }

    @Override
    public int doSave(Map dataMap)
    {
        return 0;
    }

    @Override
    public String getValueListName()
    {
        return this.valueListName;
    }

    /*
     * @Override public void linkColumnToValueList(){ String cacheName = "DataDictCache"; String
     * valuelistTable = GlobalConfig.getConfigValue("sysconfig.valuelistTable", "b_valuelist");
     * String sql = "select * from b_valuelisttable where valueListName = ?"; for(Iterator<String>
     * iter = this.columnValueListMap.keySet().iterator(); iter.hasNext();){ String value =
     * (String)this.columnValueListMap.get(iter.next()); TODO Map valueListMap =
     * (Map)ExCacheUtils.getData(cacheName, value); Map valueListMap = new HashMap(); if
     * (valueListMap == null || valueListMap.isEmpty()){ List dataList = null; if (value != null &&
     * value.startsWith("TableValueList.")){ Map rowMap = this.getJdbcTemplate().queryForMap(sql,
     * value); String valueSql = (String)rowMap.get("SQLTPL"); dataList =
     * this.getJdbcTemplate().queryForList(valueSql); }else if (value != null &&
     * value.startsWith("ValueList.")){ dataList = this.getJdbcTemplate().
     * queryForList("select keyName as optValue, valueText as optLabel from " + valuelistTable
     * +" where typename = ?", value.substring(10)); } valueListMap = new DataObject(); if
     * (dataList != null){ for(Object item : dataList){ Map row = (Map)item;
     * valueListMap.put(String.valueOf(row.get("optValue")),String.valueOf(row.get("optLabel"))); }
     * } TODO ExCacheUtils.cacheData(cacheName, value, valueListMap); } } }
     */

    private Object invokeCommandMethod(String methodName, Map paramMap)
    {

        try
        {
            Object invokeResult = MethodUtils.invokeExactMethod(this, methodName,
                new Object[] {paramMap}, new Class[] {Map.class});
            if (invokeResult != null)
            {
                return invokeResult;
            }
        }
        catch (NoSuchMethodException e1)
        {
            log.error(e1.getMessage(), e1);
        }
        catch (Exception e)
        {
            log.error(e.getMessage(), e);
        }

        return null;
    }

    /**
     * 从缓存中获取当前登录用户信息, 如所在姓名、部门、手机、电邮等 TODO
     * 
     * @return
     */
    protected Map getLoginUserData()
    {

        Map userData = null;
        /*
         * String token = (String)ExCacheUtils.getData("UserTokenCache", "Session-" +
         * this.loginUserId); Map tokenData = (Map)ExCacheUtils.getData("UserTokenCache", token);
         * if (tokenData != null){ userData =(Map)tokenData.get("extraData"); } if (userData ==
         * null){ userData = new HashMap(); }
         */
        return userData;
    }

    /**
     * <p class="detail">
     * 功能：查询记录数
     * </p>
     * @author Zerlinda
     * @date 2017年9月14日 
     * @param whereParams
     * @return
     */
    public int queryRowCount(StringBuilder whereClause, Map whereParams)
    {
        Object[] paramObjs = ExSQLUtils.buildWhere(whereClause, whereParams);
        String sql = "select COUNT(1) from " + this.masterTable + whereClause;
        return this.getRowCount(sql, paramObjs);

    }
}
