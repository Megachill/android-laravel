package com.example.androidlaravel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Login extends Activity {
	EditText etUsername, etPassword;
	String username, password;
	Button bLogin;
	Toast toast;
	
	private static final String TAG_SUCCESS = "success";
	private static final String TAG_MESSAGE = "message";
	private static final String TAG_RESULTS = "results";
	private static final String TAG = Login.class.getSimpleName();
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle("Login");
		setContentView(R.layout.login);

		etUsername = (EditText) findViewById(R.id.editTextUsername);
		etPassword = (EditText) findViewById(R.id.editTextPassword);
		bLogin = (Button) findViewById(R.id.buttonLogin);
		
		bLogin.setOnClickListener(new OnClickListener(){
			public void onClick(View view) {
				
				username = etUsername.getText().toString();
				password = etPassword.getText().toString();
				
				if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {
					toast = Toast.makeText(Login.this, "Invalid credentials", Toast.LENGTH_LONG);
					toast.setGravity(Gravity.CENTER, 0, 0);
					toast.show();
				} else {
					new LoginAsyncTask().execute("student");
				}
			}
		});
		
	}
	
	class LoginAsyncTask extends AsyncTask<String, Void, String> {
		ProgressDialog progressDialog;
		
		protected String doInBackground(String... string) {
			try {
				JsonClient jsonClient = new JsonClient(username, password);
				JSONObject jsonObject = jsonClient.httpGet(string[0], null);
				
				Log.d(TAG, jsonObject.toString());
				if (jsonObject.getInt(TAG_SUCCESS) != 1) {
					return jsonObject.getString(TAG_MESSAGE);
				}
				
				SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(Login.this);
				Editor editor = sharedPreferences.edit();
				
				editor.putString("username", username);
				editor.putString("password", password);
				editor.commit();
				
				DbHelper dbHelper = new DbHelper(getApplicationContext(), new StudentContract());
				dbHelper.deleteAll();
				
				ContentValues values = new ContentValues();
				
				JSONArray jsonArray = jsonObject.getJSONArray(TAG_RESULTS);
				for(int i = 0; i < jsonArray.length(); i++) {
					JSONObject data = jsonArray.getJSONObject(i);
					
					values.clear();
					
					values.put(StudentContract.Columns.ID, data.getString("id"));
					values.put(StudentContract.Columns.FIRST_NAME, data.getString(StudentContract.Columns.FIRST_NAME));
					values.put(StudentContract.Columns.LAST_NAME, data.getString(StudentContract.Columns.LAST_NAME));
					values.put(StudentContract.Columns.GENDER, data.getString(StudentContract.Columns.GENDER));
					values.put(StudentContract.Columns.DATE_OF_BIRTH, data.getString(StudentContract.Columns.DATE_OF_BIRTH));
					values.put(StudentContract.Columns.ADDRESS, data.getString(StudentContract.Columns.ADDRESS));
					values.put(StudentContract.Columns.CITY, data.getString(StudentContract.Columns.CITY));
					values.put(StudentContract.Columns.ZIP_CODE, data.getString(StudentContract.Columns.ZIP_CODE));
					values.put(StudentContract.Columns.CREATED_AT, data.getString(StudentContract.Columns.CREATED_AT));
					values.put(StudentContract.Columns.UPDATED_AT, data.getString(StudentContract.Columns.UPDATED_AT));
					
					dbHelper.insert(values);	
				}
				
				dbHelper.close();
				
				Intent dashboard = new Intent(getApplicationContext(), Dashboard.class);
				startActivity(dashboard);
				finish();
				
			} catch (JSONException e) {
				e.printStackTrace();
			}
			
			return null;
		}

		protected void onPreExecute() {
			super.onPreExecute();
			progressDialog = new ProgressDialog(Login.this);
			progressDialog.setMessage("Please wait...");
			progressDialog.setIndeterminate(false);
			progressDialog.setCancelable(false);
			progressDialog.show();
		}
		
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			progressDialog.dismiss();
			
			if (result != null) {
				toast = Toast.makeText(Login.this, result, Toast.LENGTH_LONG);
				toast.setGravity(Gravity.CENTER, 0, 0);
				toast.show();
			}
		}
		
	}

}
