package cn.bforce.common.persistence;

import java.util.Map;

/**
 * 命名的查询方案
 * @author LTJ
 *
 */
public class NamedQuery {

	private String where;
	
	private String name;
	
	private String label;
	
	private boolean isAppend;
	
	private Object[] paramValues;
	
	private String[] paramNames;
	
	private boolean isInvokeMethod;
	
	
	public NamedQuery(String name){
		this.name = name;
	}
	public NamedQuery(String name,String where){
		this.name = name;
		this.where = where;
	}
	
	public NamedQuery(String name, String label, String where){
		this.name = name;
		this.label = label;
		this.where = where;
	}
	
	public NamedQuery(String name, boolean isInvokeMethod){
		this.name = name;
		this.isInvokeMethod = isInvokeMethod;
	}
	
	public String getWhere() {
		return where;
	}

	public void setWhere(String where) {
		this.where = where;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	public String getLabel() {
		return label;
	}
	
	public void setLabel(String label) {
		this.label = label;
	}
	
	public boolean isAppend() {
		return isAppend;
	}
	
	public void setAppend(boolean isAppend) {
		this.isAppend = isAppend;
	}
	
	public Object[] buildParamValues(Map paramMap) {
		if (paramNames != null){
			paramValues = new Object[paramNames.length];
			for(int i = 0; i < paramNames.length; i++){
				paramValues[i] = paramMap.get(paramNames[i]);
			}
		}
		return paramValues;
	}
	public Object[] getParamValues() {
		return paramValues;
	}
	public void setParamValues(Object[] paramValues) {
		this.paramValues = paramValues;
	}
	
	public String[] getParamNames() {
		return paramNames;
	}
	
	public void setParamNames(String[] paramNames) {
		this.paramNames = paramNames;
	}
	
	
	public boolean isInvokeMethod() {
		return isInvokeMethod;
	}
	public void setInvokeMethod(boolean isInvokeMethod) {
		this.isInvokeMethod = isInvokeMethod;
	}
	
	
	
}
