package com.example.androidlaravel;

import android.provider.BaseColumns;

public class StudentContract implements ContractInterface {
	private static final String DB_NAME = "androidLaravel.db";
	private static final int DB_VERSION = 1;
	public static final String TABLE_NAME = "students";

	public static final String DEFAULT_SORT = Columns.CREATED_AT + "DESC";
	public static final String[] COLUMN_LIST = { Columns.ID,
			Columns.FIRST_NAME, Columns.LAST_NAME, Columns.GENDER,
			Columns.DATE_OF_BIRTH, Columns.ADDRESS, Columns.CITY,
			Columns.ZIP_CODE, Columns.CREATED_AT, Columns.UPDATED_AT };

	public class Columns {
		public static final String ID = BaseColumns._ID;
		public static final String FIRST_NAME = "first_name";
		public static final String LAST_NAME = "last_name";
		public static final String GENDER = "gender";
		public static final String DATE_OF_BIRTH = "date_of_birth";
		public static final String ADDRESS = "address";
		public static final String CITY = "city";
		public static final String ZIP_CODE = "zipcode";
		public static final String CREATED_AT = "created_at";
		public static final String UPDATED_AT = "updated_at";
	}

	public String getDbName() {
		return DB_NAME;
	}

	public int getDbVersion() {
		return DB_VERSION;
	}

	public String getTableName() {
		return TABLE_NAME;
	}

	public String getSqlQuery() {
		return String
				.format("CREATE TABLE %s (%s INT PRIMARY KEY, %s text, %s text, "
						+ "%s text, %s text, %s text, %s text, %s text, %s text, %s text)",
						TABLE_NAME, Columns.ID, Columns.FIRST_NAME,
						Columns.LAST_NAME, Columns.GENDER,
						Columns.DATE_OF_BIRTH, Columns.ADDRESS, Columns.CITY,
						Columns.ZIP_CODE, Columns.CREATED_AT,
						Columns.UPDATED_AT);
	}
}
