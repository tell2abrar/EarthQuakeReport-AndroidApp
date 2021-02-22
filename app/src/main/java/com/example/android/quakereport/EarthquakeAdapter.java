package com.example.android.quakereport;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.text.SimpleDateFormat;
import java.util.Date;
import android.graphics.drawable.GradientDrawable;

public class EarthquakeAdapter extends ArrayAdapter<Earthquake> {
        private Context mContext;
        private ArrayList<Earthquake> mEarthquakesInfo;
        private Earthquake mCurrentEarthquake;

        EarthquakeAdapter(Context context , ArrayList<Earthquake> earthquakesInfo){
        super(context,0,earthquakesInfo);
        mContext = context;
        mEarthquakesInfo = earthquakesInfo;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItemView = convertView;

        if(listItemView == null)
        {
              // we get a reference to the activity
            listItemView = LayoutInflater.from(mContext).inflate(R.layout.list_item_layout,parent,false);
        }

        TextView magnitudeTextView = (TextView) listItemView.findViewById(R.id.magnitude_text_view);
        TextView primaryLocationTextView =(TextView) listItemView.findViewById(R.id.primary_location_text_view);
        TextView offsetLocationTextView =(TextView) listItemView.findViewById(R.id.offset_location_text_view);
        TextView dateTextView = (TextView) listItemView.findViewById(R.id.date_text_view);
        TextView timeTextView = (TextView) listItemView.findViewById(R.id.time_text_view);


        mCurrentEarthquake = (Earthquake) mEarthquakesInfo.get(position);

        //Extracting Magnitude from earthQuake object and formating it so that it shows only one decimal point
        double magnitude = mCurrentEarthquake.getmMagnitude();
        DecimalFormat myFormatter = new DecimalFormat("0.0");
        magnitudeTextView.setText(myFormatter.format(magnitude));

        //Set appropriate backround color to Magnitude TextView
        GradientDrawable colorMagnitude = (GradientDrawable) magnitudeTextView.getBackground();
        int color = getMagnitudeColor(mCurrentEarthquake.getmMagnitude());
        colorMagnitude.setColor(color);


        // 1 Extracting string of Location from earthQuake object and split them into offset location and primary location
        // 2 Set offset location and primary location into offset_location_TextView and primary_location_TextView

        String actuallLocation = mCurrentEarthquake.getmLocation();
        String primaryLocation;
        String offsetLocation;
        int index=actuallLocation.indexOf("of");
        if(index!=-1){
            primaryLocation = actuallLocation.substring(index+3);
            primaryLocation.trim();
            offsetLocation = actuallLocation.substring(0,index+3);
        }else{
            offsetLocation = "Near the";
            primaryLocation = actuallLocation;
        }
        offsetLocationTextView.setText(offsetLocation);
        primaryLocationTextView.setText(primaryLocation);

        //Extracting time and convert into Readable format,sperate date and time and add into their respective TextView
        long date = mCurrentEarthquake.getmDate();
        Date myDate = new Date(date);
        SimpleDateFormat dateSimpleDateFormat = new SimpleDateFormat("MMM DD, yyyy");
        SimpleDateFormat timeSimpleDateFormat = new SimpleDateFormat("h:mm a");
        String actuallDate = dateSimpleDateFormat.format(myDate);
        String actuallTime = timeSimpleDateFormat.format(myDate);
        dateTextView.setText(actuallDate);
        timeTextView.setText(actuallTime);

        return listItemView;

    }

    // return appropriate background color
    private int getMagnitudeColor(double magnitude){
            int color;
            if(magnitude>0 && magnitude <=2){
                color = ContextCompat.getColor(getContext(), R.color.magnitude1);
                return color;
            }else if(magnitude > 2 && magnitude <=3){
                color = ContextCompat.getColor(getContext(), R.color.magnitude2);
                return color;
            }else if(magnitude > 3 && magnitude <=4) {
                color = ContextCompat.getColor(getContext(), R.color.magnitude3);
                return color;
            }else if(magnitude > 4 && magnitude <=5){
                color = ContextCompat.getColor(getContext(), R.color.magnitude4);
                return color;
            }else if(magnitude > 5 && magnitude <=6) {
                color = ContextCompat.getColor(getContext(), R.color.magnitude5);
                return color;
            }else if(magnitude > 6 && magnitude <=7) {
                color = ContextCompat.getColor(getContext(), R.color.magnitude6);
                return color;
            }else if(magnitude > 7 && magnitude <=8) {
                color = ContextCompat.getColor(getContext(), R.color.magnitude7);
                return color;
            }else if(magnitude > 8 && magnitude <=9) {
                color = ContextCompat.getColor(getContext(), R.color.magnitude8);
                return color;
            }else if(magnitude > 9 && magnitude <10) {
                color = ContextCompat.getColor(getContext(), R.color.magnitude9);
                return color;
            }else{
                color = ContextCompat.getColor(getContext(), R.color.magnitude10plus);
                return color;
            }
    }
}
