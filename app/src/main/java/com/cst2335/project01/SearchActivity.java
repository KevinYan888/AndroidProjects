package com.cst2335.project01;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
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
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;

public class SearchActivity extends AppCompatActivity {
//    ListView modelID;
    TextView modelID;
    TextView modelName;
    ProgressBar progressBar;
    MyListAdapter myAdapter;

    private ArrayList<Message> elements = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        Intent fromCarData = getIntent();
//        fromMain.getStringExtra("typeEmail");
        EditText searchEdit = findViewById(R.id.searchEdit);
        searchEdit.setText(fromCarData.getStringExtra("manufacturerName"));

        myAdapter = new MyListAdapter();
        ListView myList = (ListView) findViewById(R.id.sListView);
        myList.setAdapter(myAdapter = new MyListAdapter());
        searchEdit = findViewById(R.id.searchEdit);

        myList.setAdapter(myAdapter);



//        modelID = findViewById(R.id.modelID);
        modelName = findViewById(R.id.modelName);
        progressBar = findViewById(R.id.progressBar);
//        progressBar.setVisibility(View.VISIBLE );

        SearchCar sCar = new SearchCar();//obj
        sCar.execute("https://vpic.nhtsa.dot.gov/api/vehicles/GetModelsForMake/Honda?format=JSON",
                "https://api.openweathermap.org/data/2.5/uvi?appid=7e943c97096a9784391a981c4d878b22&lat=45.348945&lon=-75.759389");
    }//Type 1

    private class SearchCar extends AsyncTask< String, Integer, String>
    {
        String id, name;
//        int id, name;
        //Type3                Type1
        protected String doInBackground(String ... args)
        {
            try {

                //create a URL object of what server to contact:
                URL url2 = new URL(args[0]);

                //open the connection
                HttpURLConnection urlConnection = (HttpURLConnection) url2.openConnection();
                Log.i("MainActivity", "000000000000000000" ) ;
                //wait for data:
                InputStream response = urlConnection.getInputStream();
                Log.i("MainActivity", "111111111111111111111" ) ;
                //JSON reading:   Look at slide 26
                //Build the entire string response:
                BufferedReader reader = new BufferedReader(new InputStreamReader(response, "UTF-8"), 8);
                StringBuilder sb = new StringBuilder();

                String line = null;
//                line = String.valueOf(reader.readLine());
//                sb.append(line + "\n");
                sb.append(reader.readLine());
                while ((line = reader.readLine()) != null)
                {
                    Log.i("MainActivity", "22222222222222222" ) ;
                    sb.append(line + "\n");

                }
                String result = sb.toString(); //result is the whole string
                Log.i("MainActivity", "33333333333333333333" ) ;
                // convert string to JSON: Look at slide 27:
                JSONObject uvReport = new JSONObject(result);
                //get the double associated with "value"
//                id= Integer.valueOf(uvReport.getInt("Model_ID"));
                name = String.valueOf(uvReport.getString("Model_Name"));
//                name = String.valueOf(uvReport.getDouble("value"));
//                name = uvReport.getString("Model_Name");
//                toString()
                Log.i("MainActivity", "4444444444444444444" ) ;
                Log.i("MainActivity", "The uv is now: " +  name) ;
            }
            catch (Exception e)
            {
            }
            Log.i("HTTP", "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
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
//            modelID.setText(id);
            modelName.setText(name);
            Log.i("HTTP", fromDoInBackground);
            progressBar.setVisibility(View.INVISIBLE );
        }
    }



    protected void showContact(int position)
    {
        Message selectedContact = elements.get(position);
        View extraStuff = getLayoutInflater().inflate(R.layout.empyty, null);
        //get the TextViews
        EditText rowMsg = extraStuff.findViewById(R.id.eText);
//        EditText rowSend = extraStuff.findViewById(R.id.sBtn);
        TextView rowId = extraStuff.findViewById(R.id.row_id);

        //set the fields for the alert dialog
//eText.setText(selectedContact.getMessage());
//        rowSend.setText(selectedContact.getIsSend());
        rowMsg.setText(selectedContact.getMessage());
        rowId.setText("id:" + selectedContact.getId());

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("You clicked on item #" + position)

                //What is the message:
                .setMessage("The selected row is: " + (position + 1) +
                        "\n" +
                        "You can update the fields and then click update to save in the database")
                // add extra layout elements:showing the contact information
                .setView(extraStuff)
                //what the update button does:
                .setPositiveButton("Update", (click, arg) -> {
//                        elements.add("HELLO");
                    selectedContact.update(rowMsg.getText().toString());
                    updateContact(selectedContact);
                    myAdapter.notifyDataSetChanged();
                })
                //What the delete button does:
                .setNegativeButton("Delete", (click, arg) -> {
                    deleteContact(selectedContact); //remove the contact from database
                    elements.remove(position); //remove the contact from contact list
                    myAdapter.notifyDataSetChanged(); //there is one less item so update the list

                })

//                    An optional third button:
                .setNeutralButton("Dissmiss", (click, arg) -> {  })

                //Show the dialog
                .create().show();
    }

    //MyListAdapter is an inner class
    class MyListAdapter extends BaseAdapter {
        //implement by writing 4 public functions
        @Override
        public int getCount() {
            return elements.size();
        }

        @Override//what to show at row
        public Message getItem(int row) {
//            public Object getItem(int row) {
            {
//                "This is row" + eText.getText().toString();
                return elements.get(row);
//                return eText.getText().toString();
            }
        }

        @Override//return the database id of item i
        public long getItemId(int id) {
            return (long) (getItem(id).getId());
        }

        @Override//how to show it:button, textView, editText, checkbox
        public View getView(int position, View convertView, ViewGroup parent) {

            LayoutInflater inflater = getLayoutInflater();
            Message msg = (Message) getItem(position);

//        make a new row in choose gender or send receive button
            View newRow = convertView;//msg.getGender()==0)
            if(msg.getIsSend()) {

                newRow = inflater.inflate(R.layout.send, parent, false);
            }else{
//            if(!msg.getIsSend() ){
                newRow = inflater.inflate(R.layout.receive, parent, false);
            }


            //finding what in the screen and set message into the new row
            TextView eText = newRow.findViewById(R.id.eText);
            eText.setText( msg.getMessage());
            TextView rowId = (TextView)newRow.findViewById(R.id.row_id);
            rowId.setText("id:" + msg.getId());
//
            return newRow;
        }
    }
}