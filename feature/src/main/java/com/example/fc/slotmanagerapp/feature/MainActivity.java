package com.example.fc.slotmanagerapp.feature;

import android.app.ActionBar;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.app.FragmentTabHost;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;
import org.json.JSONArray;
import org.json.JSONStringer;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.os.AsyncTask;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {




        public String readJSONFeed(String URL) {
            StringBuilder stringBuilder = new StringBuilder();
            HttpClient httpClient = new DefaultHttpClient();
            HttpGet httpGet = new HttpGet(URL);
            String line = null;
            try {
                HttpResponse response = httpClient.execute(httpGet);
                StatusLine statusLine = response.getStatusLine();
                int statusCode = statusLine.getStatusCode();
                if (statusCode == 200) {
                    HttpEntity entity = response.getEntity();
                    InputStream inputStream = entity.getContent();
                    BufferedReader reader = new BufferedReader(
                            new InputStreamReader(inputStream));
                    while ((line = reader.readLine()) == null || reader.readLine() != null) {
                        stringBuilder.append(line);
                    }

                    inputStream.close();
                } else {
                    Log.d("JSON", "Failed to download file");
                }
            } catch (Exception e) {
                Log.d("readJSONFeed", e.getLocalizedMessage());
            }
            return String.valueOf(stringBuilder.append(line));


        }

    private class ReadWeatherJSONFeedTask extends AsyncTask
            <String, Void, String> {
        protected String doInBackground(String... urls) {
            return readJSONFeed(urls[0]);
        }

        protected void onPostExecute(String result) {
            try {
                JSONObject jsonObject = new JSONObject(result);
                JSONObject weatherObservationItems = new JSONObject(jsonObject.getString("weatherObservation"));

                Toast.makeText(getBaseContext(),
                        weatherObservationItems.getString("clouds") +
                                " - " + weatherObservationItems.getString("stationName"),
                        Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                Log.d("ReadWeatherJSONFeedTask", e.getLocalizedMessage());
            }
        }
    }

    private class ReadPlacesFeedTask extends AsyncTask
            <String, Void, String> {
        protected String doInBackground(String... urls) {
            return readJSONFeed(urls[0]);
        }

        protected void onPostExecute(String result) {
            try {
                TextView textView = (TextView) findViewById(R.id.simpleTextView);
                textView.setText(result.replaceAll(",","\n"));

                JSONObject jsonObject = new JSONObject();
                jsonObject.put("result", result);

                JSONArray postalCodesItems = new JSONArray();
                postalCodesItems.put(jsonObject);

                //---print out the content of the json feed---
                //for (int i = 0; i < postalCodesItems.length(); i++) {
                   // JSONObject postalCodesItem =
                         //   postalCodesItems.getJSONObject(i);

                 //   Toast.makeText(getBaseContext(), postalCodesItem.getString("result"), Toast.LENGTH_LONG).show();

            //    }

            } catch (Exception e) {
                Log.d("ReadPlacesFeedTask", e.getLocalizedMessage());
            }
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnGetPlaces();
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayUseLogoEnabled(true);
        actionBar.setLogo(R.drawable.ic_plane);




       // ActionBar bar = getActionBar();
       // bar.setBackgroundDrawable(new ColorDrawable(Color.CYAN));

    }

    public void btnGetPlaces() {
        Button button  = findViewById(R.id.get_places);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(getBaseContext(),
                        "peter",
                        Toast.LENGTH_SHORT).show();

                new ReadPlacesFeedTask().execute(
                        "http://192.168.1.62:8080/airlines");


            }
        });



    }














}


