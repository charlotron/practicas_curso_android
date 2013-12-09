package com.worldpcs.tiendecitas3.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import android.util.Log;
 
public class JSONParser {
	
    public static JSONObject getJSONObjectFromURL(String url){
        InputStream is = null;
        String result = "";
        JSONObject jobj = null;
        // Making HTTP request
        try {
            DefaultHttpClient httpclient = new DefaultHttpClient();
            HttpGet httpget = new HttpGet(url);
            HttpResponse response = httpclient.execute(httpget);
            HttpEntity entity = response.getEntity();
            is = entity.getContent();

    		BufferedReader reader = new BufferedReader(new InputStreamReader(is,"iso-8859-1"),8);
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
            }
            is.close();
            result=sb.toString();

            jobj = new JSONObject(result);   
        }
        catch (Exception e) {
            Log.e("JSON Parser", "Error: " + e.toString());
		}        	
        return jobj;
    }
    
    public static JSONArray getJSONArrayFromURL(String url){
        InputStream is = null;
        String result = "";
        JSONArray jArray = null;
        // Making HTTP request
        try {
            DefaultHttpClient httpclient = new DefaultHttpClient();
            HttpGet httpget = new HttpGet(url);
            HttpResponse response = httpclient.execute(httpget);
            HttpEntity entity = response.getEntity();
            is = entity.getContent();

    		BufferedReader reader = new BufferedReader(new InputStreamReader(is,"iso-8859-1"),8);
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
            }
            is.close();
            result=sb.toString();

            jArray = new JSONArray(result);     
        }
        catch (Exception e) {
            Log.e("JSON Parser", "Error: " + e.toString());
		}        	
        return jArray;
    }
}