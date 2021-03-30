package com.cst2335.project01;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
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
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

public class SearchActivity extends AppCompatActivity {
//    ListView modelID;
//    TextView modelID;
//    TextView modelName;
    SQLiteDatabase db;
    ProgressBar progressBar;
    MyListAdapter myAdapter;
    public static final String MAKE_NAME = "Make_Name";
    public static final String MODEL_NAME = "Model_Name";
    public static final String MODEL_ID = "Model_ID";

    public  JSONObject object;
    public 	ListView lv;
    public ArrayList<CarListItem> list = new ArrayList<>();

    class  CarListItem {
        private String make, name;
        private Long id;
        public CarListItem(String n, String m, Long i)
        {
            name =n;
            make = m;
            id = i;
        }

        public String getMake() {
            return  make;
        }

        public String getName() {
            return name;
        }

        public Long getId() {
            return id;
        }

    }



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

        //If it returns null then you are on a phone, otherwise it’s on a tablet. Store this in result in a Boolean variable.
        FrameLayout fLayout = findViewById(R.id.fLayout);
        boolean isTablet = fLayout != null; //check if the FrameLayout is loaded

        myList.setOnItemClickListener((adapter, view, pos, id) -> {
            //Create a bundle to pass data to the new fragment
            Log.e("1111111111","2222222222222222");
//            Message newMsg = new Message(searchEdit.getText().toString(),false,id);
            Bundle dataToPass = new Bundle();
            dataToPass.putString("make", list.get(pos).getMake());
            dataToPass.putString("name",list.get(pos).getName());
            dataToPass.putInt("position",pos);
            dataToPass.putLong("id", list.get(pos).getId());

            if(isTablet)
            {
                CardataFragment carFragment = new CardataFragment(); //add a DetailFragment
                carFragment.setArguments( dataToPass ); //pass it a bundle for information
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fLayout, carFragment) //Add the fragment in FrameLayout
                        .commit(); //actually load the fragment. Calls onCreate() in DetailsFragment
            }
            else //isPhone
            {
                Intent nextActivity = new Intent(SearchActivity.this, EmptyActivity.class);
                nextActivity.putExtras(dataToPass); //send data to next activity
//look at the startActivity() call from step 7 and change the Intent object so that it will transition to EmptyActivity.class.
                startActivity(nextActivity); //make the transition
            }
        });

        loadDataFromDatabase();
    }
    private void loadDataFromDatabase()
    {
        //get a database connection:
        MyOpener dbOpener = new MyOpener(this);
        db = dbOpener.getWritableDatabase(); //This calls onCreate() if you've never built the table before, or onUpgrade if the version here is newer


        // We want to get all of the columns. Look at MyOpener.java for the definitions:
        String [] columns = {MyOpener.COL_ID, MyOpener.COL_MAKE, MyOpener.COL_NAME};
        //query all the results from the database:
        Cursor results = db.query(false, MyOpener.TABLE_NAME, columns, null, null, null, null, null, null);

        //Now the results object has rows of results that match the query.
        //find the column indices:
        int nameColIndex = results.getColumnIndex(MyOpener.COL_NAME);
        int makeColumnIndex = results.getColumnIndex(MyOpener.COL_MAKE);
        int idColIndex = results.getColumnIndex(MyOpener.COL_ID);

        //iterate over the results, return true if there is a next item:
        while(results.moveToNext())
        {
            String name = results.getString(nameColIndex);
            String make = results.getString(makeColumnIndex);
            long id = results.getLong(idColIndex);

            //add the new Contact to the array list:
            list.add(new CarListItem(name, make, id));
        }

        //At this point, the contactsList array has loaded every row from the cursor.
    }

    protected void showContact(int position)
    {
        Object selectedContact = list.get(position);
        View extraStuff = getLayoutInflater().inflate(R.layout.search_list, null);
        //get the TextViews
        TextView modelName = extraStuff.findViewById(R.id.modelName);
        TextView makeName = extraStuff.findViewById(R.id.makeName);
        TextView modelId = extraStuff.findViewById(R.id.modelID);

        modelName.setText(selectedContact.toString());
        modelId.setText(selectedContact.toString());
        makeName.setText(selectedContact.toString());

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
        String make, name;
        long id;
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
                publishProgress(0);
                //JSON reading:   Look at slide 26
                //Build the entire string response:
                BufferedReader reader = new BufferedReader(new InputStreamReader(response, "UTF-8"), 8);
                StringBuilder sb = new StringBuilder();
                publishProgress(25);
                String line = null;
                while ((line = reader.readLine()) != null)
                {
                    sb.append(line + "\n");
                }
                String result = sb.toString(); //result is the whole string
                publishProgress(50);
                // convert string to JSON: Look at slide 27:
                JSONObject results = new JSONObject(result);
                JSONArray object = results.getJSONArray("Results");
                publishProgress(75);
                for(int i=0;i<object.length();i++) {
                    id = object.getJSONObject(i).getLong("Model_ID");
                    make = object.getJSONObject(i).getString("Make_Name");
                    name = object.getJSONObject(i).getString("Model_Name");

                    list.add(new CarListItem(name, make, id));
                }
                publishProgress(100);
                Log.e("MainActivity", list.get(object.length()-2).getName()) ;
                Log.e("MainActivity", "list contain: "+object.length()+" object") ;
//                if((line = reader.readLine()) != null){
//
//                    //将字符串转换成jsonObject对象
//                    JSONObject jsonObject = new JSONObject(String.valueOf(sb.append(line )));
////获取到json数据中里的Results数组内容
//                    JSONArray resultJsonArray = jsonObject.getJSONArray("Results");
//                    Log.e("MainActivity", String.valueOf(resultJsonArray) ) ;
////                    bianli
//                    Map<String, Object> map=new HashMap<String, Object>();
//                    for(int i=0;i<resultJsonArray.length();i++){
//                        object = resultJsonArray.getJSONObject(i);
//                        try {
//                            //获取到json数据中的results数组里的内容Model_ID
//                            Long id = object.getLong("Model_ID");
//                            String make = object.getString("Make_Name");
//                            //获取到json数据中的results数组里的内容Model_Name
//                            String name = object.getString("Model_Name");
//                            Log.e("123123132323232333",name) ;
////                            //存入map
////                            map.put("Model_ID", id);
////                            Log.e("MainActivity", String.valueOf(id) ) ;
////                            map.put("Model_Name", name);
////                            Log.e("MainActivity", String.valueOf(name) ) ;
//                            //add to ArrayList集合
//
//                            list.add(new CarListItem(name,make,id));
////                            list.addAll((Collection<? extends Map<String, Object>>) map);
//                            Log.e("MainActivity", list.get(1).getMake()) ;
//                            Log.e("MainActivity", list.get(1).getName()) ;
//                            Log.e("123123132323232333","gsdafuigfifgsuidf") ;
//                            Log.e("MainActivity", String.valueOf(list.get(1).getId()));
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                    }
//
//                    Log.e("MainActivity", String.valueOf(list) ) ;
//                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
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

//        make a new row in choose gender or send receive button
            View newRow = convertView;//msg.getGender()==0)

            newRow = inflater.inflate(R.layout.search_list, parent, false);

            CarListItem listCar = (CarListItem) getItem(position);

            long modelID = listCar.getId();
            String make = listCar.getMake();
            String name = listCar.getName();

            //finding what in the screen and set message into the new row
            TextView modelIDV = newRow.findViewById(R.id.modelID);
            modelIDV.setText( "ID: " + modelID);
            TextView modelName = (TextView)newRow.findViewById(R.id.modelName);
            modelName.setText("Model_Name: "+name);
            TextView makeName = (TextView)newRow.findViewById(R.id.makeName);
            makeName.setText("Make_Name: "+ make);

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