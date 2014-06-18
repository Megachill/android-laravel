/**
 * Taken from various sources on the internet. 
 * Modified them to suit my needs :p  
 */

package com.example.androidlaravel;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Base64;
import android.util.Log;

public class JsonClient {
	private static final String BASE_URL = "http://10.0.2.2/laravelandroid/api/v1/androidAuth/";
	private HttpClient httpClient;
	
	private String username;
	private String password;
	
	private static final String TAG = JsonClient.class.getSimpleName();

	public JsonClient() {
	}
	
	public JsonClient(String username, String password){
		this.httpClient = this.getHttpClient();
		
		this.username = username;
		this.password = password;
	}

	/**
	 * Post request
	 * 
	 * @param uri - Uri to make request
	 * @param params - Post data
	 * @return JSONObject
	 */
	public JSONObject httpPost(String uri, List<NameValuePair> params) {
		JSONObject jsonObject = null;
		try {
			HttpPost httpPost = new HttpPost(this.getUrl(uri));
			httpPost.addHeader("Authorization", this.getAuthCredentials());
			httpPost.setEntity(new UrlEncodedFormEntity(params));
			
			HttpResponse httpResponse = httpClient.execute(httpPost);
			jsonObject = this.parseJson(this.getContent(httpResponse));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			httpClient.getConnectionManager().shutdown();
		}

		return jsonObject;
	}

	/**
	 * Get request
	 * 
	 * @param uri - Uri to maker equest
	 * @param params - Post data
	 * @return JSONObject
	 */
	public JSONObject httpGet(String uri, List<NameValuePair> params) {
		JSONObject jsonObject = null;

		String url = this.getUrl(uri);
		if (params != null) {
			String paramString = URLEncodedUtils.format(params, "utf-8");
			url += "?" + paramString;
		}

		Log.d(TAG, url);
		HttpGet httpGet = new HttpGet(url);

		try {
			httpGet.addHeader("Authorization", this.getAuthCredentials());
			HttpResponse httpResponse = httpClient.execute(httpGet);
			
			jsonObject = this.parseJson(this.getContent(httpResponse));
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			httpClient.getConnectionManager().shutdown();
		}

		return jsonObject;
	}
	
	/**
	 * Returns authentication credentials
	 * 
	 * @return credentials
	 */
	private String getAuthCredentials(){
		return "Basic " + Base64.encodeToString((this.username + ":" + this.password).getBytes(), Base64.NO_WRAP);
	}

	/**
	 * Returns httpClient. Separated the HttpClient 
	 * just in case we might send extra headers in the future
	 * 
	 * @return httpClient object
	 */
	private HttpClient getHttpClient() {
		DefaultHttpClient httpClient = new DefaultHttpClient();
		return httpClient;
	}

	/**
	 * Returns content from response
	 * 
	 * @param httpResponse - Either get or post
	 * @return inputStream object
	 */
	private InputStream getContent(HttpResponse httpResponse) {
		InputStream inputStream = null;
		HttpEntity httpEntity = httpResponse.getEntity();

		try {
			inputStream = httpEntity.getContent();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return inputStream;
	}

	/**
	 * Parse json from request, either get or post
	 * 
	 * @param inputStream - Content from returned request
	 * @return JSONObject
	 */
	private JSONObject parseJson(InputStream inputStream) {
		JSONObject jsonObject = null;

		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					inputStream, "iso-8859-1"), 8);
			StringBuilder stringBuilder = new StringBuilder();
			String line = null;

			while ((line = reader.readLine()) != null) {
				stringBuilder.append(line + "\n");
			}
			
			inputStream.close();
			jsonObject = new JSONObject(stringBuilder.toString());
			
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return jsonObject;
	}

	/**
	 * Build url based from base url
	 * 
	 * @param uri - Uri to make request
	 * @return url string
	 */
	private String getUrl(String uri) {
		return BASE_URL + uri;
	}
}
