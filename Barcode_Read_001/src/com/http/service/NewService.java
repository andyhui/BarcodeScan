package com.http.service;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

public class NewService {



	public static boolean save(String gps, String content) {
		// TODO Auto-generated method stub
		ServerUrl serverURL = new ServerUrl();
		String path = serverURL.URL;
		Map<String,String> params = new HashMap<String, String>();
		params.put("gps",gps);
		params.put("content",content);
		//return sendGETRequest(path);
		return sendPOSTRequest(path,params,"gb2312");
	}

	private static boolean sendPOSTRequest(String path,
			Map<String, String> params,String encoding) {
		// TODO Auto-generated method stub
		List<NameValuePair> pairs = new ArrayList<NameValuePair>();
		if(params != null&& !params.isEmpty()){
			for(Map.Entry<String, String> entry:params.entrySet()){
				pairs.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
			}
		}
		HttpPost httpost = new HttpPost(path);
		try {
			HttpEntity httpentity = new UrlEncodedFormEntity(pairs,encoding);
			httpost.setEntity(httpentity);
			HttpClient httpclient = new DefaultHttpClient();
			HttpResponse httpReponse = httpclient.execute(httpost);
			if(httpReponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
				String ss = EntityUtils.toString(httpReponse.getEntity());
				System.out.println(ss);
				return true;
			}			
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch(ClientProtocolException e){
			e.printStackTrace();
		}catch(IOException e){
			e.printStackTrace();
		}catch(Exception e){
			e.printStackTrace();
		}		
		return false;
	}

	private static boolean sendGETRequest(String path) {
		// TODO Auto-generated method stub
		HttpPost httpost = new HttpPost(path);
		try {
			HttpClient httpclient = new DefaultHttpClient();
			HttpResponse httpReponse = httpclient.execute(httpost);
			if(httpReponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
				String strResult = EntityUtils.toString(httpReponse.getEntity());
				return true;
			}			
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch(ClientProtocolException e){
			e.printStackTrace();
		}catch(IOException e){
			e.printStackTrace();
		}catch(Exception e){
			e.printStackTrace();
		}
		return false;
	}

}
