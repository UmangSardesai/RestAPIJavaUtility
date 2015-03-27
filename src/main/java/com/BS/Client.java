package com.BS;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.Scanner;

import org.apache.http.HttpResponse;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;


class Client
{
	JSONParser parser;
	private String key = "";
	private final String init_url = "https://www.browserstack.com/automate/";
	String type = "application/json";
	
	Client(String username, String accesskey)
	{
		key=username+":"+accesskey;
	}
	
	Client()
	{
		Scanner sc=new Scanner (System.in);
		System.out.println("Enter username");
		String us=sc.next();
		System.out.println("Enter accesskey");
		String ak=sc.next();
		key=us+":"+ak;
	}
	
	void ChangeStatus(String sessionID, String status, String reason) throws IOException, ParseException
	{
		JSONObject obj = new JSONObject();
		obj.put("status", status);
		obj.put("reason", reason);
		String url = init_url + "sessions/" + sessionID + ".json";
		HttpResponse hrep = Util.makePutRequest(url, key, obj);
		String ans = convertToString(hrep) +"";
		System.out.println(ans);
	}
	
	JSONObject getAPIStatus() throws IOException, ParseException
	{
		String url = init_url + "plan.json";
		HttpResponse hrep = Util.makeGetRequest(url, key, type);
		return convertToJSON(hrep);
	}
	
	String getPlan() throws IOException, ParseException
	{
		JSONObject obj = getAPIStatus();
		return obj.get("automate_plan")+"";
	}
	
	String getMaxParallelSessions() throws IOException, ParseException
	{
		JSONObject obj = getAPIStatus();
		return obj.get("parallel_sessions_max_allowed")+"";

	}
	
	String getParallelSessionsRunning() throws IOException, ParseException
	{
		JSONObject obj = getAPIStatus();
		return obj.get("parallel_sessions_running")+"";
	}
	
	JSONArray getBuilds() throws IOException, ParseException
	{
		String url = init_url + "builds.json";
		HttpResponse hrep = Util.makeGetRequest(url, key, type);
		return convertToJSONArray(hrep);
	}
	
	JSONObject getLatestBuild() throws IOException, ParseException
	{
		String url = init_url + "builds.json?limit=1";
		HttpResponse hrep = Util.makeGetRequest(url, key, type);
		JSONArray JSarr = convertToJSONArray(hrep);
		return (JSONObject)((JSONObject)JSarr.get(0)).get("automation_build");
	}
	
	JSONArray getBuilds(int limit) throws IOException, ParseException
	{
		String url = init_url + "builds.json?limit="+limit;
		HttpResponse hrep = Util.makeGetRequest(url, key, type);
		return convertToJSONArray(hrep);
	}
	
	String getLatestBuildID() throws IOException, ParseException
	{
		JSONObject obj = getLatestBuild();
		return obj.get("hashed_id")+"";
	}
	
	String getLatestBuildName() throws IOException, ParseException
	{
		JSONObject obj = getLatestBuild();
		return obj.get("name")+"";
	}
	
	JSONArray getSessions(String BuildID) throws IOException, ParseException
	{
		String url = init_url + "builds/"+BuildID+"/sessions.json";
		HttpResponse hrep = Util.makeGetRequest(url, key, type);
		return convertToJSONArray(hrep);
	}
	
	JSONArray getSessions(String BuildID, int limit) throws IOException, ParseException
	{
		String url = init_url + "builds/"+BuildID+"/sessions.json?limit="+limit;
		HttpResponse hrep = Util.makeGetRequest(url, key, type);
		return convertToJSONArray(hrep);
	}
	
	String getLatestSessionID() throws IOException, ParseException
	{
		String BuildID = getLatestBuildID();
		String url = init_url + "builds/"+BuildID+"/sessions.json?limit=1";
		HttpResponse hrep = Util.makeGetRequest(url, key, type);
		JSONArray JSarr = convertToJSONArray(hrep);
		JSONObject obj = (JSONObject)((JSONObject)JSarr.get(0)).get("automation_session");
		return obj.get("hashed_id")+"";
	}
	
	JSONObject getSessionDetails(String SessionID) throws IOException, ParseException
	{
		String url = init_url + "sessions/"+SessionID+".json";
		HttpResponse hrep = Util.makeGetRequest(url, key, type);
		return convertToJSON(hrep);
	}
	
	JSONObject getLatestSessionDetails() throws IOException, ParseException
	{
		String SessionID = getLatestSessionID();
		return getSessionDetails(SessionID);
	}
	
	String getLatestSessionDuration() throws IOException, ParseException
	{
		JSONObject obj = getLatestSessionDetails();
		return ((JSONObject)obj.get("automation_session")).get("duration")+"";
	}
	
	String getSessionName(String SessionID) throws IOException, ParseException
	{
		JSONObject obj = getSessionDetails(SessionID);
		return ((JSONObject)obj.get("automation_session")).get("name")+"";
	}
	
	String getSessionStatus(String SessionID) throws IOException, ParseException
	{
		JSONObject obj = getSessionDetails(SessionID);
		return ((JSONObject)obj.get("automation_session")).get("status")+"";
	}
	
	String getSessionURL(String SessionID) throws IOException, ParseException
	{
		JSONObject obj = getSessionDetails(SessionID);
		return ((JSONObject)obj.get("automation_session")).get("browser_url")+"";
	}
	
	String getSessionDuration(String SessionID) throws IOException, ParseException
	{
		JSONObject obj = getSessionDetails(SessionID);
		return ((JSONObject)obj.get("automation_session")).get("duration")+"";
	}
	
	void getSessionLogs(String SessionID, String filepath) throws IOException, ParseException
	{
		JSONObject obj = getSessionDetails(SessionID);
		String logsurl = ((JSONObject)obj.get("automation_session")).get("logs")+"";
		HttpResponse hrep = Util.makeGetRequest(logsurl, key, type);
		String logs = convertToString(hrep);
		File f = new File(filepath);
		PrintWriter pw = new PrintWriter(f); 
		pw.write(logs);
		pw.close();
	}
	
	String getLatestSessionName() throws IOException, ParseException
	{
		JSONObject obj = getLatestSessionDetails();
		return ((JSONObject)obj.get("automation_session")).get("name")+"";
	}
	
	String getLatestSessionStatus() throws IOException, ParseException
	{
		JSONObject obj = getLatestSessionDetails();
		return ((JSONObject)obj.get("automation_session")).get("status")+"";
	}
	
	String getLatestSessionURL() throws IOException, ParseException
	{
		JSONObject obj = getLatestSessionDetails();
		return ((JSONObject)obj.get("automation_session")).get("browser_url")+"";
	}
	
	void getLatestSessionLogs(String filepath) throws IOException, ParseException
	{
		JSONObject obj = getLatestSessionDetails();
		String logsurl = ((JSONObject)obj.get("automation_session")).get("logs")+"";
		HttpResponse hrep = Util.makeGetRequest(logsurl, key, type);
		String logs = convertToString(hrep);
		File f = new File(filepath);
		PrintWriter pw = new PrintWriter(f); 
		pw.write(logs);
		pw.close();
	}
	
	JSONArray getListOfBrowsers() throws IOException, ParseException
	{
		String url = init_url + "browsers.json";
		HttpResponse hrep = Util.makeGetRequest(url, key, type);
		return convertToJSONArray(hrep);
	}
	
	String getLatestBrowserVersion (String browser, String os, String os_version) throws IOException, ParseException
	{
		JSONArray jsArray = getListOfBrowsers();
		Object obj[] = jsArray.toArray();
		String latest = "";
		for(int i=0; i<obj.length; i++)
		{
			JSONObject jObj = (JSONObject)obj[i];
			if(jObj.containsValue(os_version) && jObj.containsValue(browser) && jObj.containsValue(os))
			latest = jObj.get("browser_version")+"";
		}
		return latest;
	}
	
	JSONObject convertToJSON(HttpResponse response) throws ParseException, IOException
	{
		if (response!=null)
		{
			parser = new JSONParser();
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String line = "";
			line = rd.readLine();
			JSONObject JSobj = (JSONObject) parser.parse(line);
			return JSobj;
		}
		return null;
	}

	String convertToString(HttpResponse response) throws ParseException, IOException
	{
		if(response!=null)
		{
			parser = new JSONParser();
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String line = "";
			String line2 = "";
			while ((line = rd.readLine()) != null) 
			{
				line2+=line+"\n";
			}
			return line2;
		}
		return "";
	}
	
	JSONArray convertToJSONArray(HttpResponse response) throws ParseException, IOException
	{
		if (response!=null)
		{
			parser = new JSONParser();
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String line = "";
			line = rd.readLine();
			JSONArray JSarr = (JSONArray) parser.parse(line);
			return JSarr;
		}
		return null;
	}
	
}
