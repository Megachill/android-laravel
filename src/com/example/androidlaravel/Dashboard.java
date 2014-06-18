package com.example.androidlaravel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.app.ListActivity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class Dashboard extends ListActivity {

	List<HashMap<String, String>> list;
	private static final String TAG = Dashboard.class.getSimpleName();
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle("Dashboard");
		setContentView(R.layout.dashboard);
		
		ListView listView = getListView();
		
		DbHelper dbHelper = new DbHelper(this, new StudentContract());
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		
		Cursor cursor = db.rawQuery("SELECT * FROM " + StudentContract.TABLE_NAME, null);
		list = new ArrayList<HashMap<String, String>>();
		
		if (cursor.moveToFirst()) {
			do {
				HashMap<String, String> data = new HashMap<String, String>();
				
				data.put(StudentContract.Columns.ID, cursor.getString(0));
				data.put(StudentContract.Columns.FIRST_NAME, cursor.getString(1));
				data.put(StudentContract.Columns.LAST_NAME, cursor.getString(2));
				data.put(StudentContract.Columns.GENDER, cursor.getString(3));
				data.put(StudentContract.Columns.DATE_OF_BIRTH, cursor.getString(4));
				data.put(StudentContract.Columns.ADDRESS, cursor.getString(5));
				data.put(StudentContract.Columns.CITY, cursor.getString(6));
				data.put(StudentContract.Columns.ZIP_CODE, cursor.getString(7));
				data.put(StudentContract.Columns.CREATED_AT, cursor.getString(8));
				data.put(StudentContract.Columns.UPDATED_AT, cursor.getString(9));
				
				Log.d(TAG, data.toString());
				
				list.add(data);
			}while(cursor.moveToNext());
		}
		ListViewAdapter adapter = new ListViewAdapter(this, list);
		
		// @TODO: load more data when reached at the end of the list view
		listView.setAdapter(adapter);
		
	}

	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.dashboard, menu);
		return true;
	}

	static class ViewHolder {
		public TextView name, address;
	}
	
	public class ListViewAdapter extends ArrayAdapter<HashMap<String, String>> {
		
		private List<HashMap<String, String>> list;
		private Activity context;
		
		public ListViewAdapter(Activity context, List<HashMap<String, String>> list) {
			super(context, R.layout.row, list);
			this.list = list;
			this.context = context;
		}
		
		public View getView(int position, View convertView, ViewGroup parent) {
			View rowView = convertView;
			
			if (rowView == null) {
				LayoutInflater inflater = context.getLayoutInflater();
				rowView = inflater.inflate(R.layout.row, null);
				
				ViewHolder viewHolder = new ViewHolder();
				viewHolder.name = (TextView) rowView.findViewById(R.id.name);
				viewHolder.address = (TextView) rowView.findViewById(R.id.address);
				
				rowView.setTag(viewHolder);
			}
			
			ViewHolder holder = (ViewHolder) rowView.getTag();
			holder.name.setText(list.get(position).get("first_name") + " " + list.get(position).get("last_name"));
			holder.address.setText(list.get(position).get("address"));
			
			return rowView;
		}
		
	}
	
}
