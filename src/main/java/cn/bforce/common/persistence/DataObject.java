package cn.bforce.common.persistence;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import cn.bforce.common.utils.ExDateUtils;

/**
 * 数据对象，代表数据表中的一条记录
 * @author LTJ
 *
 */
public class DataObject extends HashMap {
	
	private static final long serialVersionUID = 4336838378135065157L;
		
	private boolean isModified = false;
		
	private Map oldValues = new HashMap(100);
	
	private Map metaData = new HashMap(100);
	
	private final Map<String,String> lowerCaseMap = new HashMap<String,String>();
		
	public DataObject(){
		super(100);
	}

	public DataObject(Map dataMap){
		this.putAll(dataMap);
	}
	
	/**
	 * @param tableName 表名
	 * @param tablePK  主键
	 */
	public DataObject(String tableName, String _tablePK){
		super(100);
				
		put("rowStatus", "new");
		String key1 = "pk_" + tableName ;
		put(key1, _tablePK);
		
		String key2 = key1 + "_" + _tablePK;
		put(key2,null);		
		setModified(false);

	}
	
	public DataObject(String tableName, String _tablePK, Map rowData){
		
		putAll(rowData);
		
		oldValues.putAll(rowData);
				
		put("rowStatus", "edit");
		String key1 = "pk_" + tableName ;
		put(key1, _tablePK);
		String key2 = key1 + "_" + _tablePK;
		put(key2, String.valueOf(rowData.get(_tablePK)));
		setModified(false);
		
	}
	
	/**
	 * 检查数据对象是否有修改
	 */
	public void checkDataModified(){
		
		if (oldValues.isEmpty()){
			String status = (String)this.get("rowStatus");
			if ("new".equalsIgnoreCase(status)){
				this.setModified(true);
				return;
			}			
		}
		
		for(Iterator iter = oldValues.keySet().iterator(); iter.hasNext();){
			String key = (String)iter.next();
			Object oldValue = oldValues.get(key);
			Object newValue = this.get(key);
			
			if (newValue != null && newValue instanceof List){
				List temp = (List)newValue;
				for(int i = 0; i < temp.size();i++){
					if (temp.get(i) instanceof DataObject){
						((DataObject)temp.get(i)).checkDataModified();
					}					
				}
				continue;
			}
			if ("created".equalsIgnoreCase(key) || "updated".equalsIgnoreCase(key) ){
				this.remove(key);
				continue;
			}
			
			if (oldValue == null && newValue == null){
				this.remove(key);
				continue;
			}
			if (oldValue != null && oldValue.equals(newValue)){
				this.remove(key);
				continue;
			}
			
			if (oldValue == newValue){
				this.remove(key);
				continue;
			}
			
			if (oldValue != null && oldValue instanceof Date && newValue != null){
				if (newValue instanceof String){
					newValue = ExDateUtils.parseDate((String)newValue);
				}
				if (((Date)oldValue).getTime() == ((Date)newValue).getTime()){
					this.remove(key);
					continue;
				}
			}
			
			if (oldValue != null && oldValue instanceof Long 
					&& newValue != null && newValue instanceof String){
				long a = (Long)oldValue;
				//if ("null".equalsIgnoreCase((String)newValue)){
				//	continue;
				//}
				long b = Long.parseLong((String)newValue);
				if (a == b){
					this.remove(key);
					continue;
				}
			}
			if (oldValue != null && oldValue instanceof Integer 
					&& newValue != null && newValue instanceof String){
				int a = (Integer)oldValue;
				int b = Integer.parseInt((String)newValue);
				if (a == b){
					this.remove(key);
					continue;
				}
			}
			this.setModified(true);
			
		}
	}
	
	/**
	 * 是否有修改
	 * @return
	 */
	public boolean isModified() {
		return isModified;
	}

	public void setModified(boolean isModified) {
		
		this.isModified = isModified;
		if (isModified){
			put("rowModified", "true");
		}else{
			put("rowModified", "false");
		}
		
	}
	/**
	 * @param key  字段名
	 * @param value  字段的值
	 */
	public Object put(Object key , Object value){
		
		Object oldKey = lowerCaseMap.put(key.toString().toLowerCase(), key.toString());
        Object oldValue = super.remove(oldKey);

		String _key = key.toString();
		
		if (value instanceof List){
			List newList = (List)value;
			List oldList = (List)get(_key);
			if (oldList != null){
				if (oldList.size() == newList.size()){
					for(int i = 0; i < newList.size(); i++){
						Map newRow = (Map)newList.get(i);
						if (oldList != null && i < oldList.size()){
							Map oldRow = (Map)oldList.get(i);
							oldRow.putAll(newRow);
						}else{
							 //super.put(_key, value);
						}										
					}
				}else{
					super.put(_key, value);
				}
				
			}else{
				super.put(_key, value);
			}
			
		}else{
			super.put(_key, value);
		}
		
		return oldValue;
	}
	
	/**
	 * 
	 * @param key
	 * @param value
	 */
	public void setRelationData(Object key , Object value){
		put(key, value);
		oldValues.put(key, value);		
	}
	
	
	
	  /**
     * @see java.util.Map#containsKey(java.lang.Object)
     */
    public boolean containsKey(Object key) {
    	Object realKey = lowerCaseMap.get(key.toString().toLowerCase());
        return super.containsKey(realKey);
    }

	/**
	 * @param key  字段名
	 */
    public Object get(Object key) {
    	Object realKey = lowerCaseMap.get(key.toString().toLowerCase());
    	return super.get(realKey);
    }


    /**
     * @see java.util.Map#putAll(java.util.Map)
     */
    public void putAll(Map m) {
        Iterator iter = m.keySet().iterator();
        while (iter.hasNext()) {
            Object key = iter.next();
            Object value = m.get(key);
            this.put(key, value);
        }
    }

    /**
     * @see java.util.Map#remove(java.lang.Object)
     */
    public Object remove(Object key) {
    	Object realKey = lowerCaseMap.remove(key.toString().toLowerCase());
        return super.remove(realKey);      
    }

	@Override
	public Object clone() {
		DataObject target = new DataObject();
		
		try {

			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(baos);
			oos.writeObject(this);
			oos.close();

			ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
			ObjectInputStream bis = new ObjectInputStream(bais);
			target = (DataObject) bis.readObject();

		} catch (Exception e) {
			e.printStackTrace();
		} 
		
//	   for (Iterator keyIt = this.keySet().iterator(); keyIt.hasNext();) {
//			Object key = keyIt.next();
//			if (this.get(key) != null && this.get(key) instanceof List) {
//				List srcList = (List) this.get(key);
//				List destList = new ArrayList<Object>(Arrays.asList(new Object[srcList.size()]));  
//				
//				for (Iterator iter = srcList.iterator(); iter.hasNext();) {
//					destList.add( iter.next());
//				}				
//				target.put(key, destList);
//			} else {
//				target.put(key, this.get(key));
//			}
//		}
		
		target.setOldValues(this.oldValues);
		return target;

	}


    public String getString(Object key) {
        Object value = this.get(key);
        if (value instanceof String){
        	return (String)value;
        }else if (value == null){
        	return null;
        }
        return String.valueOf(value);
    }

    public Integer getInt(Object key) {
        Object value = this.get(key);
        if (value instanceof Integer){
        	return (Integer)value;
        }else if (value == null){
        	return null;
        }
        return Integer.parseInt(String.valueOf(value));
    }
    
    public Long getLong(Object key) {
        Object value = this.get(key);
        if (value instanceof Integer){
        	return new Long((Integer)value);
        }else if (value == null){
        	return null;
        }
        return Long.parseLong(String.valueOf(value));
    }
    
    public Date getDate(Object key) {
        Object value = this.get(key);
        if (value instanceof Date){
        	return (Date)value;
        }else if (value == null){
        	return null;
        }else if (value instanceof String){
        	if ("".equalsIgnoreCase(value.toString())){
        		return null;        		
        	}else{
        		return ExDateUtils.parseDate(value.toString());
        	}
        }
        return null;
    }
    
    
    
    public List getList(Object key) {
        Object value = this.get(key);
        if (value instanceof List){
        	return (List)value;
        }else if (value == null){
        	return null;
        }
        return null;
    }
       
    public boolean getBoolean(Object key) {
        Object value = this.get(key);
        if (value instanceof Boolean){
        	return (Boolean)value;
        }else if (value == null){
        	return false;
        }
        return false;
    }
    
    public void setReadOnly(boolean readOnly){
    	this.put("readOnly", readOnly);
    }
        
    public boolean isReadOnly(){
    	if (this.get("readOnly") != null){
    		return (Boolean)this.get("readOnly");
    	}
    	return false;
    }
    
    /**
     * 设置当前数据的存储状态
     * @param rowStatus 值可为：New、Edit
     */
    public void setRowStatus(String rowStatus){
    	this.put("rowStatus", rowStatus);
    }
    
    /**
     * 当前数据的存储状态
     * @return  值可为：New、Edit
     */
    public String getRowStatus(){
    	return (String)this.get("rowStatus");
    }

    /**
     * 是否新记录
     * @return
     */
    public boolean isNewRow(){
    	if (this.getRowStatus() != null && "New".equalsIgnoreCase(this.getRowStatus())){
    		return true;
    	}
    	return false;
    }
    
    public void loadExtendData(DataObject extendDO){
    	this.putAll(extendDO);
    	this.oldValues.putAll(extendDO.oldValues);
    }

	protected void setOldValues(Map oldValues) {
		this.oldValues = oldValues;
	}
     
	public void setPkValue(String tablePK, Object pkValue){
		this.put(tablePK, pkValue);
	}
    
	/**
	 * 
	 * @param tableName
	 * @return
	 */
	public Long getRowID(String tableName){
		
		String key1 = "pk_" +  tableName ;
		String _tablePK = this.getString(key1);
		if (this.get(_tablePK) != null){
			return this.getLong(_tablePK);
		}else{
			String key2 = key1 + "_" + _tablePK;
			return this.getLong(key2);
		}
		
	}
}
