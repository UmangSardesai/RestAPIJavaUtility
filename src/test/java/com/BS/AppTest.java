package com.BS;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.json.simple.parser.ParseException;
import org.xml.sax.SAXException;

import org.junit.Test;


public class AppTest 
{
    static String Username = "";
    static String AutomateKey = "";

    @Test
    public void SampleTest() throws IOException, InterruptedException, SAXException, ParserConfigurationException, ParseException
    {
        Client cl=new Client(Username,AutomateKey);
        
        String sessionID = ""; //You can directly fetch this in your Selenium test using --> driver.getSessionId()  
        
        System.out.println(cl.getAPIStatus());
        System.out.println(cl.getPlan());
        System.out.println(cl.getMaxParallelSessions());
        System.out.println(cl.getParallelSessionsRunning());
        System.out.println(cl.getBuilds());
        System.out.println(cl.getBuilds(3));
        System.out.println(cl.getLatestBuild());
        System.out.println(cl.getLatestBuildName());
        String BuildID = cl.getLatestBuildID();
        System.out.println(BuildID);
        System.out.println(cl.getSessions(BuildID));
        System.out.println(cl.getSessions(BuildID, 3));
        System.out.println(cl.getLatestSessionDetails());
        System.out.println(cl.getLatestSessionDuration());
        System.out.println(cl.getLatestSessionName());
        System.out.println(cl.getLatestSessionURL());
        System.out.println(cl.getLatestSessionStatus());
        System.out.println(cl.getSessionDetails(sessionID));
        System.out.println(cl.getSessionDuration(sessionID));
        System.out.println(cl.getSessionName(sessionID));
        System.out.println(cl.getSessionURL(sessionID));
        System.out.println(cl.getSessionStatus(sessionID));
        System.out.println(cl.getListOfBrowsers()+"");
        System.out.println(cl.getLatestBrowserVersion("opera", "Windows","XP"));
        cl.getSessionLogs(sessionID, "<file-path>"+sessionID+".txt");
        cl.getLatestSessionLogs("<file-path>\\logs-latestSession.txt");
        cl.ChangeStatus(sessionID, "Error", "Enter reason");
    }
}
