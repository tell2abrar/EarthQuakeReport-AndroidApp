package com.example.android.quakereport;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class Asyncearthquakeloader extends AsyncTaskLoader<ArrayList<Earthquake>> {

    private String mUrlString;


    public Asyncearthquakeloader(Context context,String urlString) {
        super(context);
        mUrlString = urlString;
    }


    // This function runs in worker thread to fetch the EarthQuake data over the network
    @Override
    public ArrayList<Earthquake> loadInBackground() {

//        try {
//            Thread.sleep(2000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }

        URL url = createURL(mUrlString);

            if (url != null) {
                ArrayList<Earthquake> earthquakes = QueryUtils.extractEarthquakes(url);
                return earthquakes;
            }

        return null;
    }

    // Helper function to create URL object by passing String
    private URL createURL (String murl){

        URL url = null;
        try {
            url = new URL(murl);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

}
