package com.cst2335.project01;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity {
//    ListView modelID;
//    TextView modelID;
//    TextView modelName;
    SQLiteDatabase db;
    ProgressBar progressBar;
    MyListAdapter myAdapter;
    CardataFragment carFragment;
    public static final String MAKE_NAME = "Make_Name";
    public static final String MODEL_NAME = "Model_Name";
    public static final String MODEL_ID = "Model_ID";

    Intent intent = new Intent();

    public static final int RESULT_CHAT =100;
    public static final int RESULT_WEATHER =200;
    public static final int RESULT_LOGIN =500;

    public  JSONObject object;
    public 	ListView lv;
    public ArrayList<CarListItem> list = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        Intent fromCarData = getIntent();
//        fromMain.getStringExtra("typeEmail");
        EditText searchEdit = findViewById(R.id.searchEdit);
        searchEdit.setText(fromCarData.getStringExtra("manufacturerName"));

        Button searchBtn = findViewById(R.id.searchBtn1);

        progressBar = findViewById(R.id.progressBar);
//        progressBar.setVisibility(View.VISIBLE );

        SearchCar sCar = new SearchCar();//obj
        sCar.execute("https://vpic.nhtsa.dot.gov/api/vehicles/GetModelsForMake/"+fromCarData.getStringExtra("manufacturerName")+"?format=JSON");//Type 1

        myAdapter = new MyListAdapter();
        ListView myList = (ListView) findViewById(R.id.sListView);

        myList.setAdapter(myAdapter = new MyListAdapter());



        //If it returns null then you are on a phone, otherwise it’s on a tablet. Store this in result in a Boolean variable.
        FrameLayout fLayout = findViewById(R.id.fLayout);
        boolean isTablet = fLayout != null; //check if the FrameLayout is loaded

        myList.setOnItemClickListener((adapter, view, pos, id) -> {
            //Create a bundle to pass data to the new fragment

//            Message newMsg = new Message(searchEdit.getText().toString(),false,id);
            Bundle dataToPass = new Bundle();
            String source = "search";
            dataToPass.putString("sourcePage", source);
            dataToPass.putString("make", list.get(pos).getMake());
            dataToPass.putString("name",list.get(pos).getName());
            dataToPass.putInt("position",pos);
            dataToPass.putInt("modelId", list.get(pos).getModelId());

            if(isTablet)
            {
                carFragment = new CardataFragment(); //add a DetailFragment
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


        myList.setOnItemLongClickListener((parent, view, position, id) -> {
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
//                    .setView(extraStuff)
                    //what the update button does:
                    .setPositiveButton("Yes", (click, arg) -> {
                        myAdapter.notifyDataSetChanged();
                        if(carFragment!=null){
                            getSupportFragmentManager().beginTransaction().remove(carFragment).commit();
                            Snackbar skbar= Snackbar.make(view,"Remove"+list.get(position).getName(),Snackbar.LENGTH_LONG);
                            skbar.show();
                        }
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
            return true;
        });



        searchBtn.setOnClickListener(e->{
            String  searchE = searchEdit.getText().toString();
            if(!searchE.isEmpty()){
                InputMethodManager inputManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
//                inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);
                Toast.makeText(this,"search for"+searchEdit.getText().toString()+"...",Toast.LENGTH_LONG);
                list.clear();
            }
        });

//        //This gets the toolbar from the layout:
//        Toolbar tBar = (Toolbar)findViewById(R.id.toolbar);
//
//        //This loads the toolbar, which calls onCreateOptionsMenu below:
//        setSupportActionBar(tBar);


    }



    private class SearchCar extends AsyncTask< String, Integer, String>
    {
        String make, name;
        int modelId;
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
                    modelId = object.getJSONObject(i).getInt("Model_ID");
                    make = object.getJSONObject(i).getString("Make_Name");
                    name = object.getJSONObject(i).getString("Model_Name");

                    list.add(new CarListItem(name, make, modelId));
                }
                publishProgress(100);
                Log.e("MainActivity", list.get(object.length()-2).getName()) ;
                Log.e("MainActivity", "list contain: "+object.length()+" object") ;

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

            int modelID = listCar.getModelId();
            String make = listCar.getMake();
            String name = listCar.getName();

            //finding what in the screen and set message into the new row
            TextView modelIDV = newRow.findViewById(R.id.modelID);
            modelIDV.setText( "modelID: " + modelID);
            TextView modelName = (TextView)newRow.findViewById(R.id.modelName);
            modelName.setText("Model_Name: "+name);
            TextView makeName = (TextView)newRow.findViewById(R.id.makeName);
            makeName.setText("Make_Name: "+ make);

            return newRow;
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.project_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        String message = null;
        //Look at your menu XML file. Put a case for every id in that file:
        switch(item.getItemId())
        {
            //what to do when the menu item is selected:
            case R.id.item1:
                message = "You clicked item 1";
                break;
            case R.id.search_item:
                message = "You clicked on the search";
                break;
            case R.id.help_item:
                message = "You clicked on help";
                break;
            case R.id.mail:
                message = "You clicked on mail";
                break;
        }
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
        return true;
    }



}