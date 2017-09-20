package cn.bforce.common.utils.web;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.map.CaseInsensitiveMap;

import cn.bforce.common.utils.ExDateUtils;
import cn.bforce.common.utils.ExStringUtils;

public class ExSQLUtils {

	public static String buildInsertSQL(String tableName, String[] tableColumns, Map newDataMap){
		StringBuffer columnsBuffer = new StringBuffer();
		StringBuffer valuesBuffer = new StringBuffer();
		Map newRowMap = new CaseInsensitiveMap(newDataMap);
		columnsBuffer.append("Insert into " + tableName + "(");
		
		for(String tableColumn:tableColumns){
			if (newRowMap.containsKey(tableColumn)){
				if (valuesBuffer.length() > 0){
					valuesBuffer.append(",");
					columnsBuffer.append(",");
				}
				columnsBuffer.append(tableColumn.toLowerCase());
				valuesBuffer.append(":" + tableColumn.toLowerCase());
			}
		}
		
		columnsBuffer.append(") Values(" + valuesBuffer + ")");
		return columnsBuffer.toString();
	}
	
	public static String buildUpdateSQL(String tableName, String tablePK,  String[] tableColumns, Map newDataMap, Map oldDataMap){
		
		StringBuffer valuesBuffer = new StringBuffer();
		
		Map newRowMap = new CaseInsensitiveMap(newDataMap);
		
		for(String tableColumn:tableColumns){
			if (newRowMap.containsKey(tableColumn)){
				Object newVal = newRowMap.get(tableColumn);
				Object oldVal = oldDataMap.get(tableColumn);
				if (isEquals(newVal,oldVal)){
					continue;
				}
				if (valuesBuffer.length() > 0){
					valuesBuffer.append(",");
				}
				valuesBuffer.append( tableColumn.toLowerCase() + " = :" + tableColumn.toLowerCase());
			}
			
		}
		if (valuesBuffer.length() == 0){
			return null;
		}
		valuesBuffer.insert(0,  "Update " + tableName + " set ");
		valuesBuffer.append(" where " + tablePK.toLowerCase() + " =  :" + tablePK.toLowerCase());
		
		return valuesBuffer.toString();
	}
	
	private static boolean isEquals(Object newValue, Object oldValue){
		
		if (oldValue == null && newValue == null){
			return true;
		}
		
		if (oldValue != null && newValue == null){
			return false;
		}
		
		if (oldValue != null && oldValue.equals(newValue)){
			return true;
		}
		
		if (oldValue == newValue){
			return true;
		}
		
		if (oldValue != null && oldValue instanceof BigDecimal 
				&& newValue != null && newValue instanceof Number){
			String a = oldValue.toString();
			String b = newValue.toString();
			if (a.equals(b)){
				return true;
			}
		}
		
		if (oldValue != null && oldValue instanceof Long 
				&& newValue != null && newValue instanceof String){
			Long a = (Long)oldValue;
			Long b = Long.parseLong((String)newValue);
			if (a.equals(b)){
				return true;
			}
		}
		
		if (oldValue != null && oldValue instanceof Integer 
				&& newValue != null && newValue instanceof String){
			int a = (Integer)oldValue;
			int b = Integer.parseInt((String)newValue);
			if (a == b){
				return true;
			}
		}
		
		if (oldValue != null && oldValue instanceof Date && newValue != null){
			if (newValue instanceof String){
				newValue = ExDateUtils.parseDate((String)newValue);
			}
			if (oldValue != null  && newValue == null){
				return false;
			}
			if (((Date)oldValue).getTime() == ((Date)newValue).getTime()){
				return true;
			}
		}
		
		
		return false;
	}
	
	public static String buildPageableSQL(String databaseType, String selectSQL, int pageSize, int pageIndex){
		
		if (ExStringUtils.isBlank(selectSQL)){
			return selectSQL;
		}
		
		StringBuffer sqlBuffer = new StringBuffer();
		
		int  fromIndex  = pageSize * (pageIndex -1);
		if (ExStringUtils.isBlank(databaseType)){
			databaseType = "Oracle";
		}
		if ("MySQL".equalsIgnoreCase(databaseType)){
			sqlBuffer.append(selectSQL);
			sqlBuffer.append(" limit " + fromIndex + ", " + pageSize);
		}else if ("Oracle".equalsIgnoreCase(databaseType)){
			sqlBuffer.append("select * from (select a.*,rownum row_num from");
			sqlBuffer.append("  (" + selectSQL + ") a ");
			fromIndex = fromIndex +1;
			sqlBuffer.append("   ) b where b.row_num >= " + (fromIndex)+" and   b.row_num <  " + (fromIndex + pageSize));
		}else if ("Postgresql".equalsIgnoreCase(databaseType)){
			sqlBuffer.append(selectSQL);
			sqlBuffer.append(" limit " + fromIndex + " offset " + pageSize);
		}else if ("MsSQL".equalsIgnoreCase(databaseType)){
			int begin = selectSQL.toLowerCase().indexOf("select ");
			int end = selectSQL.toLowerCase().indexOf("order by ");
			if (end < 1){
				end = selectSQL.length();
			}
			String orderBy = selectSQL.substring(end + 9);
			sqlBuffer.append("SELECT TOP " + pageSize + " *  FROM  ");
			sqlBuffer.append(" (select  ROW_NUMBER() OVER (ORDER BY " + orderBy +") AS RowNumber, ");
			sqlBuffer.append(selectSQL.substring(7, end));
			sqlBuffer.append(")As B WHERE RowNumber > " + fromIndex);
		}else{
			sqlBuffer.append(selectSQL);
		}
		
		return sqlBuffer.toString();
	}
	
	public static String buildRowCountSQL(String databaseType, String selectSQL){
		
		if (ExStringUtils.isBlank(selectSQL)){
			return selectSQL;
		}
		
		StringBuffer sqlBuffer = new StringBuffer();
		sqlBuffer.append("select count(*) from ");
		 if ("MsSQL".equalsIgnoreCase(databaseType)){
				int end = selectSQL.toLowerCase().indexOf("order by ");
				if (end < 1){
					end = selectSQL.length();
				}
				 sqlBuffer.append("  (" + selectSQL.substring(0, end) + ") a ");
		 }else{
			 sqlBuffer.append("  (" + selectSQL + ") a ");
		 }
		
		

		return sqlBuffer.toString();
	}
	
	
	public static Object[] buildWhere(StringBuilder whereClause, Map paramMap){
		List paramValueList = new ArrayList();
		String key, value;
		String[] ignoreWords = new String[]{"userToken","page","filter","rows","sort","order","loginUserId","command","search","pageIndex","pageSize"};
		for(Iterator<String> iter = paramMap.keySet().iterator(); iter.hasNext();){
			key = iter.next();
			if (ExStringUtils.isInclude(key, ignoreWords)  || key.endsWith("Operator")){
				continue;
			}
			value = String.valueOf(paramMap.get(key));
			if (value != null && value.length() > 0){
				if (whereClause.length() > 0){
					whereClause.append(" and ");
				}
				if (paramMap.get(key + "Operator") != null){
					whereClause.append(paramMap.get(key + "Operator"));
				}else if (value.endsWith("%")){
					whereClause.append(key + " like ?");
				}else if (value.indexOf(">") > 0 || value.indexOf(">=") > 0 ||value.indexOf("<") > 0 || value.indexOf("<=") > 0){
                    whereClause.append(key + "?");
                }else{
					whereClause.append(key + " = ?");
				}
				paramValueList.add(paramMap.get(key));
			}
		}// end for

		return paramValueList.toArray();
	}

	/**
	 * <p class="detail">
	 * 功能：重构like,<,>等参数
	 * </p>
	 * @author Zerlinda
	 * @date 2017年9月15日 
	 * @param keyArr 需要构建的key
	 * @param filterArr key对应的filter类型
	 * @param paramMap
	 * @return
	 */
	public static Map buildFilterParams(String[] keyArr, String[] filterArr, Map paramMap){
        for(int i = 0; i < keyArr.length; i++){
           String key = keyArr[i];
           if(paramMap.containsKey(key)){
               paramMap.put(key, buildFilterParam(paramMap.get(key).toString(),filterArr[i]));
           }
        }
        return paramMap;
    }
	
	public static String buildFilterParam(String value, String filter){
        switch(filter){
            case "%" : value = filter+value+filter; break;
            case "_%" : value = value+"%"; break; 
            case "%_" : value = "%"+value;  break; 
            case ">" : value = filter + value; break; 
            case "<" : value = filter + value; break; 
            case ">=" : value = filter + value; break; 
            case "<=" : value = filter + value; break; 
            default : break;
        }
        return value;
    }
	
	
	
	public static void main(String[] args) {
		
		
		String selectSQL = "select * from b_user where 1=1";
		
		System.out.println(ExSQLUtils.buildRowCountSQL("MsSQL", selectSQL));
		
		selectSQL = "select * from b_user where 1=1 order by userId  ";
		
		System.out.println(ExSQLUtils.buildRowCountSQL("MsSQL", selectSQL));
		
		
	}
}
