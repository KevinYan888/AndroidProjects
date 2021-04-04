package com.cst2335.project01;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import static com.cst2335.project01.CarActivity.RESULT_CHAT;
import static com.cst2335.project01.CarActivity.RESULT_LOGIN;

public class SavingActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, BottomNavigationView.OnNavigationItemSelectedListener{

    private static final int RESULT_WEATHER = 100;
    Intent intent = new Intent();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saving);


        //This gets the toolbar from the layout:
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
            case R.id.chat:
                message = "You clicked on Chat Page";

                SavingActivity.this.setResult(RESULT_CHAT,intent);
                Intent goToChat = new Intent(SavingActivity.this, MainActivity.class);
                startActivity(goToChat);
                break;
            case R.id.search_item:
                message = "You clicked on the search";

                break;
            case R.id.weather:
                message = "You clicked on Weather Forecast";
                SavingActivity.this.setResult(RESULT_WEATHER,intent);
                Intent goToWeather = new Intent(SavingActivity.this,CarListItem.class);
                startActivity(goToWeather);
                break;
            case R.id.login:
                message = "You clicked on login page";
                SavingActivity.this.setResult(RESULT_LOGIN,intent);
                SavingActivity.this.finish();
                break;
        }

        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        drawerLayout.closeDrawer(GravityCompat.START);

        Toast.makeText(this, "NavigationDrawer: " + message, Toast.LENGTH_LONG).show();
        return false;
    }
}