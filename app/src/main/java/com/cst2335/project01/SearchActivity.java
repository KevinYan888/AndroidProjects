package com.cst2335.project01;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class SearchActivity extends AppCompatActivity {
//    ListView modelID;
    TextView modelID;
    TextView modelName;
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        Intent fromCarData = getIntent();
//        fromMain.getStringExtra("typeEmail");
        EditText searchEdit = findViewById(R.id.searchEdit);
        searchEdit.setText(fromCarData.getStringExtra("manufacturerName"));

        modelID = findViewById(R.id.modelID);
//        modelName = findViewById(R.id.modelName);

        progressBar = findViewById(R.id.progressBar);
//        progressBar.setVisibility(View.VISIBLE );

        SearchCar sCar = new SearchCar();//obj
        sCar.execute("https://vpic.nhtsa.dot.gov/api/vehicles/GetModelsForMake/Honda?format=JSON");  //Type 1

    }

    private class SearchCar extends AsyncTask< String, Integer, String>
    {

        String id, name;

        //Type3                Type1
        protected String doInBackground(String ... args)
        {

            try {
                //create a URL object of what server to contact:
                URL url2 = new URL(args[0]);

                //open the connection
                HttpURLConnection urlConnection = (HttpURLConnection) url2.openConnection();

                //wait for data:
                InputStream response = urlConnection.getInputStream();

                //JSON reading:   Look at slide 26
                //Build the entire string response:
                BufferedReader reader = new BufferedReader(new InputStreamReader(response, "UTF-8"), 8);
                StringBuilder sb = new StringBuilder();

                String line = null;
                while ((line = reader.readLine()) != null)
                {
                    sb.append(line + "\n");
                }
                String result = sb.toString(); //result is the whole string

                // convert string to JSON: Look at slide 27:
                JSONObject uvReport = new JSONObject(result);

                //get the double associated with "value"
                id= String.valueOf(uvReport.getDouble("Model_ID"));
//                name = String.valueOf(uvReport.getDouble("Model_Name"));
//                toString()
                Log.i("MainActivity", "The uv is now: " +  modelID) ;

            }
            catch (Exception e)
            {
            }
            Log.i("HTTP", "I want look for image,current temperature and icon");
            return "Done";
        }
        public boolean fileExistance(String fname){
            File file = getBaseContext().getFileStreamPath(fname);
            return file.exists();   }
        //Type 2
        public void onProgressUpdate(Integer ... args)
        {
//            progressBar();
            Log.i("HTTP", "in onProgressUpdate");
        }
        //Type3
        public void onPostExecute(String fromDoInBackground)
        {
            modelID.setText(id);
//            modelName.setText(name);
            Log.i("HTTP", fromDoInBackground);
            progressBar.setVisibility(View.INVISIBLE );
        }
    }
}