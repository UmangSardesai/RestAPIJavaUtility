package com.BS;

import java.io.IOException;
import java.util.ArrayList;

import org.apache.commons.codec.binary.Base64;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

class Util 
{
	static HttpClient client = new DefaultHttpClient();
	static HttpResponse response;
	static JSONParser parser; 
	
	protected static HttpResponse makeGetRequest(String url,String key, String type) throws IOException, ParseException
	{
		if(response!=null)
			EntityUtils.consume(response.getEntity());
		
		HttpGet getRequest = new HttpGet(url);
		getRequest.setHeader("Accept",type);
		getRequest.setHeader("Content-type",type);

		String encoding = Base64.encodeBase64String(key.getBytes());
		getRequest.setHeader("Authorization", "Basic " + encoding);

		// Get the responses
		response = client.execute(getRequest);
		return response;
	}
	
	protected static HttpResponse makePostRequest(String url, String url2, String key, JSONObject obj) throws IOException
	{
		if(response!=null)
			EntityUtils.consume(response.getEntity());
		
		HttpPost postRequest = new HttpPost(url);

//		Content-type and Accept header not required
		String encoding = Base64.encodeBase64String(key.getBytes());
		postRequest.setHeader("Authorization", "Basic " + encoding);

		ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		
		Object[] names=obj.keySet().toArray();
		for (int i=0;i<names.length;i++)
		{
			String a = names[i]+"";
			nameValuePairs.add(new BasicNameValuePair(a, obj.get(a)+""));
		}
		
		nameValuePairs.add(new BasicNameValuePair("url", url2));
		postRequest.setEntity(new UrlEncodedFormEntity(nameValuePairs));
		
		// Get the responses
		response = client.execute(postRequest);
		return response;

	}
	
	protected static HttpResponse makeDeleteRequest(String url, String key, String type) throws IOException, InterruptedException, ParseException
	{
		if(response!=null)
			EntityUtils.consume(response.getEntity());
		
		HttpDelete deleteRequest = new HttpDelete(url);
		
		deleteRequest.setHeader("Accept",type);
		deleteRequest.setHeader("Content-type",type);

		String encoding = Base64.encodeBase64String(key.getBytes());
		deleteRequest.setHeader("Authorization", "Basic " + encoding);

		// Get the responses
		response = client.execute(deleteRequest);
		return response;
	}
	
	protected static HttpResponse makePutRequest(String url, String key, JSONObject obj) throws IOException
	{
		if(response!=null)
			EntityUtils.consume(response.getEntity());
		
		HttpPut putRequest = new HttpPut(url);

//		Content-type and Accept header not required
		String encoding = Base64.encodeBase64String(key.getBytes());
		putRequest.setHeader("Authorization", "Basic " + encoding);

		ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
 
		Object[] names=obj.keySet().toArray();
		for (int i=0;i<names.length;i++)
		{
			String a = names[i]+"";
			nameValuePairs.add(new BasicNameValuePair(a, obj.get(a)+""));
		}
		
		putRequest.setEntity(new UrlEncodedFormEntity(nameValuePairs));

		// Get the responses
		response = client.execute(putRequest);
		return response;
	}
}
