package cn.bforce.common.persistence;

public class TableColumn {

	private String columnName;
	
	private String aliasName;
	
	private String columnLabel;
	
	private String columnType;
	
	private int columnDisplaySize;
	
	public TableColumn(){
		
	}

	public TableColumn(String columnLabel, String columnName, String aliasName){
		this.columnLabel = columnLabel;
		this.columnName = columnName;
		this.aliasName = aliasName;
	}
	
	public String getColumnName() {
		return columnName;
	}

	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}

	public String getColumnLabel() {
		return columnLabel;
	}

	public void setColumnLabel(String columnLabel) {
		this.columnLabel = columnLabel;
	}

	public String getColumnType() {
		return columnType;
	}

	public void setColumnType(String columnType) {
		this.columnType = columnType;
	}

	public int getColumnDisplaySize() {
		return columnDisplaySize;
	}

	public void setColumnDisplaySize(int columnDisplaySize) {
		this.columnDisplaySize = columnDisplaySize;
	}

	public String getAliasName() {
		return aliasName;
	}

	public void setAliasName(String aliasName) {
		this.aliasName = aliasName;
	}
	
	
	
}
