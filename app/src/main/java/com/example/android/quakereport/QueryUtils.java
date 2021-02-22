package com.example.android.quakereport;

import android.util.Log;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;


public final class QueryUtils {

    private QueryUtils() {

        // making private constructor so just that class can't be instaniated
    }

    // Static function to Fetch the List of earth Quake over the network
    public static ArrayList<Earthquake> extractEarthquakes(URL url)  {

        String responceJSON = makeHttpRequest(url);

        if(responceJSON!=null){
            return parseJSON(responceJSON);
        }

        return null;
    }


    // Helper Function to make Http Request to server
    private static String makeHttpRequest(URL url){

        HttpURLConnection httpURLConnection = null;
        StringBuilder jsonString = new StringBuilder();

        try {
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.connect();
            if(httpURLConnection.getResponseCode() == 200){
                InputStream inputStream = httpURLConnection.getInputStream();
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String line = bufferedReader.readLine();
                while (line!=null){
                    jsonString.append(line);
                    line = bufferedReader.readLine();
                }
                return jsonString.toString();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }



    // Helper Function to parse the JSON respone
    private static ArrayList<Earthquake> parseJSON(String jsonString){


        // Create an empty ArrayList that we can start adding earthquakes to
        ArrayList<Earthquake> earthquakes = new ArrayList<>();
        JSONObject root = null;
        try {
            // TODO: Parse the response given by the SAMPLE_JSON_RESPONSE string and
            // build up a list of Earthquake objects with the corresponding data.

            root = new JSONObject(jsonString);
            JSONArray featuresArray = root.getJSONArray("features");
            double magnitude;
            String place;
            long time;
            String urlOfWebPage;
            JSONObject featureObject;
            JSONObject properties;

            for (int i=0;i<featuresArray.length();++i){
                featureObject= featuresArray.getJSONObject(i);
                properties = featureObject.getJSONObject("properties");
                magnitude = properties.getDouble("mag");
                place = properties.getString("place");
                time = properties.getLong("time");
                urlOfWebPage = properties.getString("url");
                earthquakes.add(new Earthquake(magnitude,place,time,urlOfWebPage));
            }

        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e("QueryUtils", "Problem parsing the earthquake JSON results", e);
            return  null;
        }
        // Return the list of earthquakes
        return earthquakes;

    }

}