package com.cst2335.project01;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

import static com.cst2335.project01.CarActivity.RESULT_CHAT;
import static com.cst2335.project01.CarActivity.RESULT_LOGIN;

public class SavingActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, BottomNavigationView.OnNavigationItemSelectedListener{

    private static final String NULL = null;
    SQLiteDatabase db;
    private Bundle dataFromActivity;
    private long id;
    private String  modelName;
    private String  make;
    private AppCompatActivity parentActivity;
    CardataFragment carFragment;
    public ArrayList<CarListItem> list = new ArrayList<>();

    MyListAdapter myAdapter;
    private static final int RESULT_WEATHER = 100;
    Intent intent = new Intent();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saving);

//        TextView id = findViewById(R.id.modelID);
//        TextView name = findViewById(R.id.modelName);
//        TextView make = findViewById(R.id.makeName);

        myAdapter = new MyListAdapter();
        ListView myList = (ListView) findViewById(R.id.sListView);
        myList.setAdapter(myAdapter = new MyListAdapter());

        loadDataFromDatabase();

        //If it returns null then you are on a phone, otherwise it’s on a tablet. Store this in result in a Boolean variable.
        FrameLayout fLayout = findViewById(R.id.fLayout);
        boolean isTablet = fLayout != null; //check if the FrameLayout is loaded

        myList.setOnItemClickListener((adapter, view, pos, id) -> {
            //Create a bundle to pass data to the new fragment

//            Message newMsg = new Message(searchEdit.getText().toString(),false,id);
            Bundle dataToPass = new Bundle();
            String source = "move";
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
                Intent nextActivity = new Intent(SavingActivity.this, EmptyActivity.class);
                nextActivity.putExtras(dataToPass); //send data to next activity
//look at the startActivity() call from step 7 and change the Intent object so that it will transition to EmptyActivity.class.
                startActivity(nextActivity); //make the transition
            }
        });



        myList.setOnItemLongClickListener((parent, view, position, id) -> {
            Object selectedContact = list.get(position);
            View extraStuff = getLayoutInflater().inflate(R.layout.save_list, null);
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
//                        if(carFragment!=null){
//                            getSupportFragmentManager().beginTransaction().remove(carFragment).commit();
//                            Snackbar skbar= Snackbar.make(view,"Remove"+list.get(position).getName(),Snackbar.LENGTH_LONG);
//                            skbar.show();
//                        }
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
//        This gets the toolbar from the layout:
        Toolbar tBar = (Toolbar)findViewById(R.id.toolbar);

        //This loads the toolbar, which calls onCreateOptionsMenu below:
        setSupportActionBar(tBar);

//For NavigationDrawer:
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,
                drawer, tBar, R.string.open, R.string.close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        //For bottomNavigationBar
        BottomNavigationView bnv = findViewById(R.id.bnv);
        bnv.setOnNavigationItemSelectedListener(this);
    }

    private void loadDataFromDatabase()
    {
        //get a database connection:
//        MyOpener dbOpener = new MyOpener(cf.parentActivity);
        MyOpener dbOpener = new MyOpener(this);
        Log.e("saving","2222222222222222");
        db = dbOpener.getWritableDatabase(); //This calls onCreate() if you've never built the table before, or onUpgrade if the version here is newer


        // We want to get all of the columns. Look at MyOpener.java for the definitions:
        String [] columns = {MyOpener.COL_ID, MyOpener.COL_MODELID, MyOpener.COL_MAKE, MyOpener.COL_NAME};
        //query all the results from the database:
        Cursor results = db.query(false, MyOpener.TABLE_NAME, columns, null, null, null, null, null, null);

        //Now the results object has rows of results that match the query.
        //find the column indices:
        int nameColIndex = results.getColumnIndex(MyOpener.COL_NAME);
        int makeColumnIndex = results.getColumnIndex(MyOpener.COL_MAKE);
        int idColIndex = results.getColumnIndex(MyOpener.COL_MODELID);
        int idIndex = results.getColumnIndex(MyOpener.COL_ID);
        //iterate over the results, return true if there is a next item:
        while(results.moveToNext())
        {
            String name = results.getString(nameColIndex);
            String make = results.getString(makeColumnIndex);
            int idmodel = (int) results.getLong(idColIndex);
            int id = (int) results.getLong(idColIndex+1);
            //add the new Contact to the array list:
            list.add(new CarListItem(name, make, idmodel));
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

            newRow = inflater.inflate(R.layout.save_list, parent, false);

            CarListItem listCar = (CarListItem) getItem(position);

            int modelID = listCar.getModelId();
            String make = listCar.getMake();
            String name = listCar.getName();

            //finding what in the screen and set message into the new row
            TextView modelIDV = newRow.findViewById(R.id.modelID);
            modelIDV.setText( "modelID: " + modelID);
            Log.i("11111" ,String.valueOf(modelID));
            TextView modelName = (TextView)newRow.findViewById(R.id.modelName);
            modelName.setText("Model_Name: "+name);
            TextView makeName = (TextView)newRow.findViewById(R.id.makeName);
            makeName.setText("Make_Name: "+ make);

            return newRow;
        }
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

    // Needed for the OnNavigationItemSelected interface:
    @Override
    public boolean onNavigationItemSelected( MenuItem item) {

        String message = null;

        switch(item.getItemId())
        {
            case R.id.main:
                message = "You clicked on Main Page";

                SavingActivity.this.setResult(RESULT_CHAT,intent);
                Intent goToMain = new Intent(SavingActivity.this, MainActivity.class);
                startActivity(goToMain);
                break;
            case R.id.searchCar:
                message = "You clicked on the search ";
                SavingActivity.this.setResult(RESULT_WEATHER,intent);
                Intent goToWeather = new Intent(SavingActivity.this,CarActivity.class);
                startActivity(goToWeather);
                break;
        }

        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        drawerLayout.closeDrawer(GravityCompat.START);

        Toast.makeText(this, "NavigationDrawer: " + message, Toast.LENGTH_LONG).show();
        return false;
    }
}