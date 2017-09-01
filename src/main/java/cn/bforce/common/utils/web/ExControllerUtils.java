package cn.bforce.common.utils.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import cn.bforce.common.utils.ExArrayList;
import cn.bforce.common.utils.ExStringUtils;


public class ExControllerUtils extends ExRequestUtils {
	
	protected static final Logger log = LogManager.getLogger(ExControllerUtils.class);
	
	// TODO
	/*public static Map checkLoginUser(HttpServletRequest request){
		if (ExStringUtils.isBlank(getLoginId(request))){
 			return getLoginUserNullError();
 		}
		return null;
	}*/
	
	public static Map getLoginUserNullError(){
		return ExStringUtils.buildResultMessage(16001, "当前登录用户ID为空,不能继续。");
	}
	
	/*public static Map findOneRow(DataRepository bizRepository, Map paramMap){
		
		Map rowMap = bizRepository.findOne(paramMap);
		if (rowMap == null){
			rowMap = new HashMap();
		}
		return rowMap;
	}
		
	public static Map getDataList(DataRepository dataRepository, String filter, Map paramMap){
		
		if (paramMap == null){
			paramMap = new HashMap();
		}
		
		int pageSize = getPageSize(paramMap);
		int pageIndex = getPageIndex(paramMap);
		
		List resultList = dataRepository.queryData(filter, paramMap, pageIndex, pageSize);
		// 初始化VALUELIST
		dataRepository.linkColumnToValueList();
		
		// 输出
		return buildTableData(resultList, dataRepository.getColumnValueListMap(), pageIndex, pageSize);

	}*/
	
	public static int getPageIndex(Map paramMap){
		int pageIndex = 1;
		if (paramMap.get("pageIndex") != null){
			pageIndex = ExStringUtils.parseInt((String)paramMap.get("pageIndex"),1);
		}else if (paramMap.get("page") != null){
			pageIndex = ExStringUtils.parseInt((String)paramMap.get("page"),1);
		}
		return pageIndex;
	}
	
	public static int getPageSize(Map paramMap){
		int pageSize = 100;
		if (paramMap.get("pageSize") != null){
			pageSize = ExStringUtils.parseInt((String)paramMap.get("pageSize"),20);
		}else if (paramMap.get("rows") != null){
			pageSize = ExStringUtils.parseInt((String)paramMap.get("rows"),20);
		}
		return pageSize;
	}
	
	public static Object[] buildSearchWhere(StringBuilder whereClause, Map paramMap){
		List paramValueList = new ArrayList();
		String key, value;
		String[] ignoreWords = new String[]{"userToken","page","filter","rows","sort","order","loginUserId","command","search","pageIndex","pageSize"};
		for(Iterator<String> iter = paramMap.keySet().iterator(); iter.hasNext();){
			key = iter.next();
			if (ExStringUtils.isInclude(key, ignoreWords)){
				continue;
			}
			value = (String)paramMap.get(key);
			if (value != null && value.length() > 0){
				if (whereClause.length() > 0){
					whereClause.append(" and ");
				}
				if (value.endsWith("%")){
					whereClause.append(key + " like ?");
				}else{
					whereClause.append(key + " = ?");
				}
				paramValueList.add(paramMap.get(key));
			}
		}// end for

		return paramValueList.toArray();
	}
	
	private static void formatDataList(List resultList, Map refKeyMap){
		
		Object[] columnNames = refKeyMap.keySet().toArray();
		if (columnNames.length == 0){
			return;
		}
		
		for(Object item : resultList){
			Map row = (Map)item;
			for(Object column:columnNames){
				String refKey = (String)refKeyMap.get(column);
				if (refKey != null){
					row.put(column +"Label", getRefValue(refKey, row.get((String)column)));
				}
			}
			
		}
	}
	
	public static Object getRefValue(String refKey, Object value){
		String cacheName = "DataDictCache";
		/**
		 * TODO 从缓存中获取字典表数据
		 */
		/*Map dataMap = (Map)ExCacheUtils.getData(cacheName, refKey);*/
		Map dataMap = new HashMap();
		if (value != null  && dataMap != null && dataMap.get(value) != null){
			return String.valueOf(dataMap.get(value));
		}
		return value;
	}
	
	public static Map buildTableData(List resultList, int pageIndex, int pageSize){
		return buildTableData(resultList, null, pageIndex, pageSize);
	}

	public static Map buildTableData(List resultList, Map refKeyMap, int pageIndex, int pageSize){
				
		Map pageInfo = new HashMap();
		int total = resultList.size();
		if (resultList instanceof ExArrayList){
			total = ((ExArrayList) resultList).getTotal();
		}
		
		if (pageSize == 0){
			pageSize = 20;
		}
		int pageCount = total/pageSize;
		if (total%pageSize > 0){
			pageCount++;
		}
		if (pageIndex <= 0){
			pageIndex = 1;
		}
		pageInfo.put("total", total);
		pageInfo.put("page", pageIndex);
		pageInfo.put("pageSize", pageSize);
		pageInfo.put("pageCount", pageCount);
		
		int fromIndex = (pageIndex - 1)*pageSize;
		int endIndex =  fromIndex + pageSize;
		
		
		if (refKeyMap != null){
			formatDataList(resultList, refKeyMap);
		}
		pageInfo.put("fromIndex", fromIndex);
		pageInfo.put("endIndex", endIndex);
		Map resultMap = new HashMap();
		
		resultMap.put("rows", resultList);			
		//resultMap.put("data", resultList);	
		resultMap.put("pagination", pageInfo);
		resultMap.put("total", total);
		resultMap.put("resultCode", 0);
		return resultMap;
	}
	
	public static String getLoginId(Map paramMap){
		return (String)paramMap.get("loginUserId");
	}

	public static String getClientId(HttpServletRequest request){
		return (String)request.getParameter("clientId");
	}

}
