package com.example.androidlaravel;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DbHelper extends SQLiteOpenHelper{
	
	/** Contract */
	protected ContractInterface contract;
	
	/**
	 * Constructor
	 * 
	 * @param context - Application context
	 * @param contract - ContractInterface Instance
	 */
	public DbHelper(Context context, ContractInterface contract) {
		super(context, contract.getDbName(), null, contract.getDbVersion());
		this.contract = contract;
	}
	
	/**
	 * Insert data
	 * @param values - Content values
	 */
	public void insert(ContentValues values) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.insert(contract.getTableName(), null, values);
		db.close();
	}
	
	/**
	 * Delete all data
	 */
	public void deleteAll() {
		SQLiteDatabase db = this.getWritableDatabase();
		db.execSQL("DELETE FROM " + contract.getTableName());
		db.close();
	}

	/**
	 * Create database table
	 */
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(contract.getSqlQuery());
	}

	/**
	 * Drop table
	 */
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + contract.getTableName());
		onCreate(db);
	}

}
