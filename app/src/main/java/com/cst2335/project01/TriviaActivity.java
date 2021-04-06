package com.cst2335.project01;


        import android.content.Context;
        import android.content.Intent;
        import android.os.Bundle;
        import android.view.Menu;
        import android.view.MenuInflater;
        import android.view.MenuItem;
        import android.view.View;
        import android.widget.Button;
        import android.widget.TextView;
        import android.widget.Toast;

        import androidx.appcompat.app.ActionBarDrawerToggle;
        import androidx.appcompat.app.AlertDialog;
        import androidx.appcompat.app.AppCompatActivity;
        import androidx.appcompat.widget.Toolbar;
        import androidx.drawerlayout.widget.DrawerLayout;

        import com.google.android.material.bottomnavigation.BottomNavigationView;
        import com.google.android.material.navigation.NavigationView;

public class TriviaActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, BottomNavigationView.OnNavigationItemSelectedListener  {
    Intent intentGoToLogin = new Intent();
    public static final int RESULT_BACK = 500;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trivia_toolbar);

        //This gets the toolbar from the layout:
        Toolbar myToolBar = (Toolbar)findViewById(R.id.toolbar);

        //This loads the toolbar, which calls onCreateOptionsMenu below:
        setSupportActionBar(myToolBar);

        //For NavigationDrawer:
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,
                drawer, myToolBar, R.string.open,R.string.close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        //For bottomNavigationBar
        BottomNavigationView bnv = findViewById(R.id.bnv);
        bnv.setOnNavigationItemSelectedListener(this);

        Button btnPlayNow = findViewById(R.id.btnPlayNow);
        btnPlayNow.setOnClickListener(b->{
            Intent goToActivity = new Intent(TriviaActivity.this, TriviaSettingActivity.class);
            startActivity(goToActivity);
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.trivia_toolbar, menu);

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
                message = "View Rank";

                                           //pass info to next activity
                Bundle bundleToRankList = new Bundle();

                bundleToRankList.putString("NO_INSERT","NO_INSERT");
                Intent goToRankList = new Intent();
                goToRankList.putExtras(bundleToRankList);
                goToRankList.setClass(TriviaActivity.this, TriviaRankItemsActivity.class);
                startActivity(goToRankList);

                break;
            case R.id.item2:
                message = "Go to play";
                Intent goToActivity = new Intent(TriviaActivity.this, TriviaSettingActivity.class);
                startActivity(goToActivity);
                break;
            case R.id.item3:
                message = "Help";
                //show the interface instruction on a dialog
                View dialogHelpView = getLayoutInflater().inflate(R.layout.trivia_help_dialog, null);

                TextView textHelp1 = dialogHelpView.findViewById(R.id.textHelp1);

                textHelp1.setText("The API documentation is located here: https://opentdb.com/api_config.php. The URL pattern is: " +
                        "https://opentdb.com/api.php?amount=XXX&type=YYY&difficulty=ZZZ, XXX is an integer for " +
                        "the number of questions. YYY is either “Boolean” or “Multiple”, and ZZZ is either “EASY”, “MEDIUM”, or “HARD”");

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("How to use the interface?")
                        .setMessage("Easy to use if you follow this instruction")
                        .setView(dialogHelpView) //add texts showing the contact information
                        .setPositiveButton("Go to Main Menu ", (click, b) -> {
                            Intent goToMainActivity = new Intent(TriviaActivity.this, MainActivity.class);
                            startActivity(goToMainActivity);
                        })
                        .setNegativeButton("Go Back", (click, b) -> {
                        })
                        .setNeutralButton("Play Now", (click, b) -> {
                            Intent goToQuestionActivity = new Intent(TriviaActivity.this, TriviaSettingActivity.class);
                            startActivity(goToQuestionActivity);
                        })
                        .create().show();
                break;
        }
        Toast.makeText(this, "Toolbar: " + message, Toast.LENGTH_LONG).show();


        return true;
    }

    // Needed for the OnNavigationItemSelected interface:
    @Override
    public boolean onNavigationItemSelected( MenuItem item) {

        String message = null;

        switch(item.getItemId())
        {
            case R.id.item1:
                message = "View Rank";
//                Intent goToRankList = new Intent(TriviaActivity.this,TriviaRankListActivity.class);
//                startActivity(goToRankList);
                Bundle bundleToRankList = new Bundle();

                bundleToRankList.putString("NO_INSERT","NO_INSERT");
                Intent goToRankList = new Intent();
                goToRankList.putExtras(bundleToRankList);
                goToRankList.setClass(TriviaActivity.this, TriviaRankItemsActivity.class);
                startActivity(goToRankList);

                break;
            case R.id.item2:
                message = "Go to play";
                Intent goToActivity = new Intent(TriviaActivity.this, TriviaSettingActivity.class);
                startActivity(goToActivity);
                break;
            case R.id.item3:
                message = "Help";
                //show the interface instruction on a dialog
                View dialogHelpView = getLayoutInflater().inflate(R.layout.trivia_help_dialog, null);

                TextView textHelp1 = dialogHelpView.findViewById(R.id.textHelp1);

                textHelp1.setText("The API documentation is located here: https://opentdb.com/api_config.php. The URL pattern is: " +
                        "https://opentdb.com/api.php?amount=XXX&type=YYY&difficulty=ZZZ, XXX is an integer for " +
                        "the number of questions. YYY is either “Boolean” or “Multiple”, and ZZZ is either “EASY”, “MEDIUM”, or “HARD”");

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("How to use the interface?")
                        .setMessage("Easy to use if you follow this instruction")
                        .setView(dialogHelpView) //add texts showing the contact information
                        .setPositiveButton("Go to Main Menu ", (click, b) -> {
                            Intent goToMainActivity = new Intent(TriviaActivity.this, MainActivity.class);
                            startActivity(goToMainActivity);
                        })
                        .setNegativeButton("Go Back", (click, b) -> {
                        })
                        .setNeutralButton("Play Now", (click, b) -> {
                            Intent goToQuestionActivity = new Intent(TriviaActivity.this, TriviaSettingActivity.class);
                            startActivity(goToQuestionActivity);
                        })
                        .create().show();
                break;
        }
        return false;
        }



}