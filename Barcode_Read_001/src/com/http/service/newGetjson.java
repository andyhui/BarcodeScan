package com.http.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import com.google.gson.Gson;

public class newGetjson {
	private String strResult;
	
	public String getResult(){
		return strResult;
	}
	public boolean GetData(double lat, double lng) {
		String path = "http://maps.googleapis.com/maps/api/geocode/json";
		Map<String, Double> params = new HashMap<String, Double>();
		params.put("lat",lng );
		params.put("lng",lat);
		return sendGETRequest(path, params);

	}

	private boolean sendGETRequest(String path, Map<String, Double> params) {
		// TODO Auto-generated method stub
		// http://maps.googleapis.com/maps/api/geocode/json?latlng=40.714224£¬-73.961452&sensor=false
		StringBuilder url = new StringBuilder(path);
		url.append("?").append("latlng").append("=");
		for (Map.Entry<String, Double> entry : params.entrySet()) {
			url.append(entry.getValue()).append(",");
		}
		url.deleteCharAt(url.length() - 1);
		url.append("&").append("sensor=false");
		String Url = url.toString();
		System.out.println("Url=" + Url);

		HttpGet httpRequest = new HttpGet(Url);
		HttpClient httpClient = new DefaultHttpClient();
		String responseData = "";
		try {

			HttpResponse httpResponse = httpClient.execute(httpRequest);
			if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				HttpEntity httpEntity = httpResponse.getEntity();
				//String strResult = EntityUtils.toString(httpEntity);
				BufferedReader bufferedReader = new BufferedReader(
						new InputStreamReader(httpEntity.getContent()));
				String line = "";
				while ((line = bufferedReader.readLine()) != null) {
					responseData = responseData + line;
				}
				Gson gson = new Gson();
				TestResult testResult = gson.fromJson(responseData, TestResult.class);
				System.out.println(testResult);
				if(testResult.getStatus().equals("OK")){
					List list = testResult.getResults();
					Iterator<Result> it = list.iterator();
					strResult = it.next().getFormatted_address();
					System.out.println(strResult);
					return true;
				} 			
			}
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
}
