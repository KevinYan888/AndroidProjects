package com.cst2335.project01;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

public class CarActivity extends AppCompatActivity {
//    at least 1 Toast, Snackbar, and AlertDialog notification.

    EditText companyName;



    public static final int RESULT_CHAT =100;
    public static final int RESULT_WEATHER =200;
    public static final int RESULT_LOGIN =500;
    Button searchBtn;
    String saveString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_database);

        companyName = findViewById(R.id.companyName);

        SharedPreferences prefs = getSharedPreferences("manufacturerName", Context.MODE_PRIVATE);
        saveString = prefs.getString("ReserveName","");
        companyName.setText(saveString);

        Intent goToSearch = new Intent(CarActivity.this, SearchActivity.class);

        searchBtn = findViewById(R.id.searchBtn);
        searchBtn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Snackbar skbar= Snackbar.make(searchBtn,"It is manufacturerName",Snackbar.LENGTH_LONG);
                skbar.show();
                String savedString = companyName.getText().toString();
                goToSearch.putExtra("manufacturerName",savedString);
                startActivity(goToSearch);

            }


        });

        Toast.makeText(this, "manufacturerName: " + "input your favorite company", Toast.LENGTH_LONG).show();

        //This gets the toolbar from the layout:
        Toolbar tBar = (Toolbar)findViewById(R.id.toolbar);

        //This loads the toolbar, which calls onCreateOptionsMenu below:
        setSupportActionBar(tBar);



    }

    private void saveSharedPrefs(String stringToSave) {
        SharedPreferences prefs = getSharedPreferences("manufacturerName", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("ReserveName", stringToSave);
        editor.commit();


    }

    public void onPause() {
        super.onPause();  // Always call the superclass method first
        saveSharedPrefs(companyName.getText().toString());
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.project_menu, menu);

//        MenuInflater inflater0 = getMenuInflater();
//        inflater0.inflate(R.menu.lab08_nmenu, menu);

	    /* slide 15 material:
	    MenuItem searchItem = menu.findItem(R.id.search_item);
        SearchView sView = (SearchView)searchItem.getActionView();
        sView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }  });

	    */
        Toast.makeText(this, "manufacturerName: " + companyName, Toast.LENGTH_LONG).show();
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