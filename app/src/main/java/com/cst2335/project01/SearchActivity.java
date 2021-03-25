package com.cst2335.project01;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
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
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

public class SearchActivity extends AppCompatActivity {
//    ListView modelID;
    TextView modelID;
    TextView modelName;
    ProgressBar progressBar;
    MyListAdapter myAdapter;

    public  JSONObject object;
    public 	ListView lv;
    public ArrayList<Map<String, Object>> list = new ArrayList<Map<String,Object>>();


    private ArrayList<String> searchResult = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        Intent fromCarData = getIntent();
//        fromMain.getStringExtra("typeEmail");
        EditText searchEdit = findViewById(R.id.searchEdit);
        searchEdit.setText(fromCarData.getStringExtra("manufacturerName"));

        progressBar = findViewById(R.id.progressBar);
//        progressBar.setVisibility(View.VISIBLE );

        SearchCar sCar = new SearchCar();//obj
        sCar.execute("https://vpic.nhtsa.dot.gov/api/vehicles/GetModelsForMake/"+fromCarData.getStringExtra("manufacturerName")+"?format=JSON");//Type 1

        myAdapter = new MyListAdapter();
        ListView myList = (ListView) findViewById(R.id.sListView);

        myList.setAdapter(myAdapter = new MyListAdapter());

        myList.setOnItemLongClickListener((parent, view, pos, id) -> {
            showContact( pos );
            return true;
        });
    }


    protected void showContact(int position)
    {
        Object selectedContact = list.get(position);
        View extraStuff = getLayoutInflater().inflate(R.layout.search_list, null);
        //get the TextViews
        TextView modelName = extraStuff.findViewById(R.id.modelName);

        TextView modelId = extraStuff.findViewById(R.id.modelID);

        modelName.setText(selectedContact.toString());
        modelId.setText(selectedContact.toString());

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
//                    selectedContact.update(rowMsg.getText().toString());
//                    updateContact(selectedContact);
                    myAdapter.notifyDataSetChanged();
                })
                //What the delete button does:
                .setNegativeButton("Delete", (click, arg) -> {
//                    deleteContact(selectedContact); //remove the contact from database
                    list.remove(position); //remove the contact from contact list
                    myAdapter.notifyDataSetChanged(); //there is one less item so update the list
                })
//                    An optional third button:
                .setNeutralButton("Dissmiss", (click, arg) -> {  })
                //Show the dialog
                .create().show();
    }


    private class SearchCar extends AsyncTask< String, Integer, String>
    {
        String id, name;
//        int id, name;
        //Type3                Type1
        protected String doInBackground(String ... args)
        {
            try {

                //create a URL object of what server to contact:
                URL url = new URL(args[0]);

                //open the connection
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

                //wait for data:
                InputStream response = urlConnection.getInputStream();

                //JSON reading:   Look at slide 26
                //Build the entire string response:
                BufferedReader reader = new BufferedReader(new InputStreamReader(response, "UTF-8"), 8);
                StringBuilder sb = new StringBuilder();

                String line = null;
                if((line = reader.readLine()) != null){

                    //将字符串转换成jsonObject对象
                    JSONObject jsonObject = new JSONObject(String.valueOf(sb.append(line )));
//获取到json数据中里的Results数组内容
                    JSONArray resultJsonArray = jsonObject.getJSONArray("Results");
                    Log.e("MainActivity", String.valueOf(resultJsonArray) ) ;
//                    bianli
                    Map<String, Object> map=new HashMap<String, Object>();
                    for(int i=0;i<resultJsonArray.length();i++){
                        object = resultJsonArray.getJSONObject(i);
                        try {
                            //获取到json数据中的results数组里的内容Model_ID
                            String id = object.getString("Model_ID");
                            //获取到json数据中的results数组里的内容Model_Name
                            String name=object.getString("Model_Name");
                            //存入map
                            map.put("Model_ID", id);
                            map.put("Model_Name", name);
                            //add to ArrayList集合
                            list.add(map);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    Log.e("MainActivity", String.valueOf(list) ) ;
                }
            }
            catch (Exception e)
            {
            }
            return "Done";
        }

        //Type 2
        public void onProgressUpdate(Integer ... args)
        {
            Log.i("HTTP", "in onProgressUpdate");
        }
        //Type3
        public void onPostExecute(String fromDoInBackground)
        {
            Log.i("HTTP", fromDoInBackground);
            myAdapter.notifyDataSetChanged();
            progressBar.setVisibility(View.INVISIBLE );
//          Toast.makeText(this, "Inserted item id:"+ Integer.valueOf(modelID), Toast.LENGTH_LONG).show();
        }
    }

    //MyListAdapter is an inner class
    class MyListAdapter extends BaseAdapter {
        //implement by writing 4 public functions
        @Override
        public int getCount() {
            return list.size();
        }

        @Override//what to show at row
        public Object getItem(int row) {
//            public Object getItem(int row) {
            {
//                "This is row" + eText.getText().toString();
                return list.get(row);
//                return eText.getText().toString();
            }
        }

        @Override//return the database id of item i
        public long getItemId(int id) {
            return id;
        }

        @Override//how to show it:button, textView, editText, checkbox
        public View getView(int position, View convertView, ViewGroup parent) {

            LayoutInflater inflater = getLayoutInflater();
            Object msg = getItem(position);
            Log.i("MainActivity", "999999999999999999" ) ;
//        make a new row in choose gender or send receive button
            View newRow = convertView;//msg.getGender()==0)

            newRow = inflater.inflate(R.layout.search_list, parent, false);

            //finding what in the screen and set message into the new row
            TextView modelID = newRow.findViewById(R.id.modelID);
            modelID.setText( list.get(position).get("Model_ID").toString());
            TextView modelName = (TextView)newRow.findViewById(R.id.modelName);
            modelName.setText(list.get(position).get("Model_Name").toString());
            Log.i("MainActivity", "88888888888888888888888" ) ;

            return newRow;
        }
    }


//    protected void showContact(int position)
//    {
//        Message selectedContact = elements.get(position);
//        View extraStuff = getLayoutInflater().inflate(R.layout.empyty, null);
//        //get the TextViews
//        EditText rowMsg = extraStuff.findViewById(R.id.eText);
////        EditText rowSend = extraStuff.findViewById(R.id.sBtn);
//        TextView rowId = extraStuff.findViewById(R.id.row_id);
//
//        //set the fields for the alert dialog
////eText.setText(selectedContact.getMessage());
////        rowSend.setText(selectedContact.getIsSend());
//        rowMsg.setText(selectedContact.getMessage());
//        rowId.setText("id:" + selectedContact.getId());
//
//        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
//        alertDialogBuilder.setTitle("You clicked on item #" + position)
//
//                //What is the message:
//                .setMessage("The selected row is: " + (position + 1) +
//                        "\n" +
//                        "You can update the fields and then click update to save in the database")
//                // add extra layout elements:showing the contact information
//                .setView(extraStuff)
//                //what the update button does:
//                .setPositiveButton("Update", (click, arg) -> {
////                        elements.add("HELLO");
//                    selectedContact.update(rowMsg.getText().toString());
//                    updateContact(selectedContact);
//                    myAdapter.notifyDataSetChanged();
//                })
//                //What the delete button does:
//                .setNegativeButton("Delete", (click, arg) -> {
//                    deleteContact(selectedContact); //remove the contact from database
//                    elements.remove(position); //remove the contact from contact list
//                    myAdapter.notifyDataSetChanged(); //there is one less item so update the list
//
//                })
//
////                    An optional third button:
//                .setNeutralButton("Dissmiss", (click, arg) -> {  })
//
//                //Show the dialog
//                .create().show();
//    }


}