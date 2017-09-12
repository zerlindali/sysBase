package cn.bforce.common.persistence.repository;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import cn.bforce.common.persistence.NamedQuery;
import cn.bforce.common.persistence.OperationException;

public interface DataRepository {
    
    /**
	 * 数据表的主键字段名
	 * @return
	 */
	public String getMasterTablePK();

	public void setMasterTablePK(String masterTablePK);
	
	/**
	 * 数据表
	 * @return
	 */
	public String getMasterTable();
	/**
	 * 设置数据表
	 * @param masterTable
	 */
	public void setMasterTable(String masterTable);
	
	
	/**
	 * 设置当前的登录用户
	 * @param caller
	 */
	public void setCaller(String caller);
	
	/**
	 * 设置当前的登录用户
	 * @param loginUserId
	 * @deprecated
	 */
	public void setLoginUserId(String loginUserId);
	
	
	/**
	 * 查询的SQL
	 * @return
	 */
	public String getSelectSQL();
	
	/**
	 * 排序的字段
	 * @return
	 */
	public String getOrderBy();

	public void setOrderBy(String orderBy);
	
	/**
	 * 查询数据
	 * @param filter 预定义的查询方案
	 * @param paramMap 查询参数
	 * @param pageIndex 页码
	 * @param pageSize  页大小
	 * @return
	 */
	public List queryData(String filter, Map paramMap, int pageIndex, int pageSize);
	
	/**
	 * 查询数据,不分页
	 * @param whereClause
	 * @param aOrderBy
	 * @param parameters
	 * @return
	 */
	public List queryData(String whereClause, String aOrderBy, Object[] parameters);
	/**
	 * 分页查询数据
	 * @param whereClause
	 * @param aOrderBy
	 * @param pageIndex
	 * @param pageSize
	 * @param parameters
	 * @return
	 */
	public List queryDataByPage(String whereClause, String aOrderBy, int pageIndex, int pageSize, Object[] parameters);
	/**
	 * 分页查询数据
	 * @param sql
	 * @param pageIndex
	 * @param pageSize
	 * @param parameters
	 * @return
	 */
	public List queryDataByPage(String sql, int pageIndex, int pageSize, Object[] parameters);
	
	public List queryDataBySQL(String sql, Object[] parameters);
	/**
	 * 查询单条数据
	 * @param paramMap
	 * @return
	 */
	public  Map findOne(Map paramMap);
	
	/**
	 * 查询单条数据
	 * @param whereClause
	 * @param parameters
	 * @return
	 */
	public  Map findOne(String whereClause, Object[] parameters);
	
	/**
	 * 
	 * @param sql
	 * @param parameters
	 * @return
	 */
	public  Map findOneBySQL(String sql, Object[] parameters);
 
	/**
	 * 根据rowId获取数据
	 * @param rowId
	 * @return
	 */
	public Map doLoad(Serializable rowId);
	
	/**
	 * 根据rowId删除数据
	 * @param rowId
	 * @return
	 */
	public int doDelete(Serializable rowId);
	
	/**
	 * 创建数据对象，并填充字段的缺省值
	 * @return
	 */
	public Map createNewRow();
	
	/**
	 * 插入数据行
	 * @param dataMap
	 * @return
	 */
	public Serializable doInsert(Map dataMap);
	
	/**
	 * 批量插入数据
	 * @param dataList
	 * @return
	 */
	public  int[] doBatchInsert(List<Map> dataList);
	
	/**
	 * 修改数据行
	 * @param rowId
	 * @param dataMap
	 * @return
	 */
	public int doUpdate(Serializable rowId, Map dataMap);
	
	/*
	 * SQL Update 操作
	 */
	public int doUpdate(String sql, Object... args);
	
	/**
	 * 保存数据行，根据rowId判定是执行插入或者修改数据行方法
	 * @param dataMap
	 * @return
	 */
	public int doSave(Map dataMap);
	
	/**
	 * 根据action指令完成数据行的修改
	 */
	public String doAction(String oId, Map parameters) throws OperationException;
	
	
	/**
	 * 获取值列表的集合，用于数据显示时的值转换
	 * @return
	 */
	public Map getColumnValueListMap();
	
//	public void linkColumnToValueList();
	
	/**
	 * 值列表的名称
	 * @return
	 */
	public String getValueListName();
	
	/**
	 * 查找预定义的查询方案
	 * @param queryName
	 * @return
	 */
	public NamedQuery findNamedQuery(String queryName);
	
	/**
	 * 清除数据行中的MetaData信息
	 * @param dataMap
	 */
	public void clearRowMetaData(Map dataMap);
	
	
}
