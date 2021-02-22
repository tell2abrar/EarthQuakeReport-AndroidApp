/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.android.quakereport;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.LoaderManager;
import android.content.Loader;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.w3c.dom.Text;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class EarthquakeActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<ArrayList<Earthquake>> {

    public static final String LOG_TAG = EarthquakeActivity.class.getName();
    private static final String USGS_REQUEST_URL = "https://earthquake.usgs.gov/fdsnws/event/1/query";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.earthquake_activity);


        /*
        *   Very important note related to using Loaders in your your app.
        *   Since Loaders are depreciated in new versions of android
        *   so you have to use support version of packages related to
        *   use these classes.
        *   But support version give you many errors if you use these classes as below
        *   So bottom line is,never use support version of these classes
        *
        * */


        ConnectivityManager cm = (ConnectivityManager)getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo nInfo = cm.getActiveNetworkInfo();
        boolean connected = nInfo != null && nInfo.isAvailable() && nInfo.isConnected();

        if(connected){
            //Get LoaderManager and instiate Loader object
            getLoaderManager().initLoader(0,null,this).forceLoad();
        }
        else
        {
            findViewById(R.id.progress_bar_view).setVisibility(View.GONE);
            TextView emptyStateView = (TextView) findViewById(R.id.emptyElement);
            emptyStateView.setText("No Internet Connection");
            emptyStateView.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public Loader<ArrayList<Earthquake>> onCreateLoader(int id, Bundle args) {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        String minMagnitude = sharedPrefs.getString(
                getString(R.string.settings_min_magnitude_key),
                getString(R.string.settings_min_magnitude_default));

        String orderBy = sharedPrefs.getString(
                getString(R.string.settings_order_by_key),
                getString(R.string.settings_order_by_default)
        );

        Uri baseUri = Uri.parse(USGS_REQUEST_URL);
        Uri.Builder uriBuilder = baseUri.buildUpon();

        uriBuilder.appendQueryParameter("format", "geojson");
        uriBuilder.appendQueryParameter("limit", "10");
        uriBuilder.appendQueryParameter("minmag", minMagnitude);
        uriBuilder.appendQueryParameter("orderby", orderBy);

        return new Asyncearthquakeloader(this, uriBuilder.toString());
        //return new Asyncearthquakeloader(this,EarthquakeActivity.mUrl);
    }


    @Override
    public void onLoadFinished(android.content.Loader<ArrayList<Earthquake>> loader, ArrayList<Earthquake> data) {

        updateListViewUI(data);
    }

    @Override
    public void onLoaderReset(android.content.Loader<ArrayList<Earthquake>> loader) {
        //Do nothing
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_main_activity,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent settingsIntent = new Intent(this, SettingsActivity.class);
            startActivity(settingsIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //Helper function to update the ListView in UI thread
    private void updateListViewUI (ArrayList < Earthquake > earthquakes) {

        ArrayList<Earthquake> myEarthQuakes = null;
        ListView earthquakeListView = null;

        if(earthquakes != null){
            findViewById(R.id.progress_bar_view).setVisibility(View.GONE);
            final ArrayList<Earthquake> myEarthQuake = earthquakes;
            earthquakeListView = (ListView) findViewById(R.id.list);
            EarthquakeAdapter myAdapter = new EarthquakeAdapter(getApplicationContext(), myEarthQuake);
            earthquakeListView.setAdapter(myAdapter);
            earthquakeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Earthquake currentEarthQuake = myEarthQuake.get(position);
                    String url = currentEarthQuake.getmUrl();
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(url));
                    startActivity(i);
                }
            });
        }else{
            findViewById(R.id.progress_bar_view).setVisibility(View.GONE);
            myEarthQuakes = new ArrayList<Earthquake>();
            earthquakeListView = (ListView) findViewById(R.id.list);
            EarthquakeAdapter myAdapter = new EarthquakeAdapter(getApplicationContext(), myEarthQuakes);
            earthquakeListView.setAdapter(myAdapter);
            earthquakeListView.setEmptyView(findViewById(R.id.emptyElement));
        }



    }


}

