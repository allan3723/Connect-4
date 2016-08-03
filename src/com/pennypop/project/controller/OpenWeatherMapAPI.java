package com.pennypop.project.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/*
 * Class to establish and obtain information from the open weather API.
 * This uses JSON to get the information and passes it back to MainScreen
 */

public class OpenWeatherMapAPI {
	private static final String API_URL = "http://api.openweathermap.org/data/2.5/weather?";
	private static final String API_KEY = "&appid=2e32d2b4b825464ec8c677a49531e9ae";
	
	private HttpURLConnection httpConnection;
	private String weatherDesc;
	private int windDeg;
	private double windSpeed, temperature;
	
	//Constructor
	public OpenWeatherMapAPI(String city, String country) {
		try {
			establishConnection(city, country);
			getWeatherData();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.err.println("Cannot establish connection to API");
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	//Establishes a connection to the API
	private void establishConnection(String city, String country) throws IOException {
		String cityUrl = "q="+city.replaceAll(" ", "%20")+","+country;
		URL url = new URL(API_URL+cityUrl+API_KEY);
		httpConnection = (HttpURLConnection) url.openConnection();
	}
	
	//Reads the API and writes it into a JSON Object
	private JSONObject getJSONObject() throws IOException, JSONException {
		BufferedReader buf = new BufferedReader(
				new InputStreamReader(httpConnection.getInputStream()));
        String jsonString = "";
        String read;
        while ((read = buf.readLine()) != null) {
            jsonString += read;
        }
        return new JSONObject(jsonString);   
	}
	
	//Parses the JSON data into the information needed
	private void getWeatherData() throws IOException, JSONException {
		//Get the JSON objects
		JSONObject weatherObj = getJSONObject();
		JSONArray weatherData = weatherObj.getJSONArray("weather");
		JSONObject windData = weatherObj.getJSONObject("wind");
		JSONObject mainData = weatherObj.getJSONObject("main");
		
		//Get the strings from the JSON objects
		weatherDesc = weatherData.getJSONObject(0).getString("description");
		
		temperature = mainData.getDouble("temp");
		
		windDeg = windData.getInt("deg");
		windSpeed = windData.getDouble("speed");
	}

	//Generated getters
	public String getWeatherDesc() {
		return weatherDesc;
	}
	
	public double getTemperature() {
		return temperature;
	}

	public int getWindDeg() {
		return windDeg;
	}

	public double getWindSpeed() {
		return windSpeed;
	}
}

