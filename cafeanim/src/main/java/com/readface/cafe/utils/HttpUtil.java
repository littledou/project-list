package com.readface.cafe.utils;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * @author dou
 * 网络通信管理
 */
public class HttpUtil {

	public static String doGet(String url){
		return doGet(url,null);
	}

	public static String doGet(String url,Header[] headers){
		StringBuilder builder = new StringBuilder();
		HttpClient client = new DefaultHttpClient();
		HttpGet httpGet = new HttpGet(url);
		try {
			if(headers!=null)httpGet.setHeaders(headers);
			HttpResponse response = client.execute(httpGet);
			if(response.getStatusLine().getStatusCode()==200){
				HttpEntity entity = response.getEntity();
				InputStream content = entity.getContent();
				BufferedReader reader = new BufferedReader(new InputStreamReader(content));
				String line;
				while((line=reader.readLine())!=null){
					builder.append(line);
				}
				return builder.toString();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}
	public static String doPost(String url,HttpEntity mEntity,Header[] headers){
		StringBuilder builder = new StringBuilder();
		HttpClient client = new DefaultHttpClient();
		HttpPost httpPost = new HttpPost(url);
		try {
			if(headers!=null)httpPost.setHeaders(headers);
			if(mEntity!=null)httpPost.setEntity(mEntity);
			HttpResponse response = client.execute(httpPost);
			if(response.getStatusLine().getStatusCode()==200){
				HttpEntity entity = response.getEntity();
				InputStream content = entity.getContent();
				BufferedReader reader = new BufferedReader(new InputStreamReader(content));
				String line;
				while((line=reader.readLine())!=null){
					builder.append(line);
				}
				return builder.toString();
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}
