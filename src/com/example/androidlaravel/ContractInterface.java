package com.example.androidlaravel;

public interface ContractInterface {
	/** Returns database name */
	public String getDbName();
	
	/** Returns database version */
	public int getDbVersion();
	
	/** Returns table name */
	public String getTableName();
	
	/** Returns sql query to create database table */
	public String getSqlQuery();
	
}
