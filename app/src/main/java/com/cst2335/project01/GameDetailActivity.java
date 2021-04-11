package com.cst2335.project01;


import androidx.appcompat.app.AppCompatActivity;


import android.database.sqlite.SQLiteDatabase;

import android.os.Bundle;

import android.widget.Button;
import android.widget.ImageButton;

import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;


/**
 *
 *
 * class Game detail activity.
 *
 */
public class GameDetailActivity extends AppCompatActivity {

//    private Button saveBtn, goToFavBtn;
//    private ProgressBar pb2;
//    private TextView teamName;
//    private TextView gameDate;
//    private TextView gameVedioUrl;
//    private ImageView image;
//    private String imageUrl;
//    private Button goWacthBtn;
//    private SQLiteDatabase db;

    /**
     * when user click the yes button to check the game detail,
     * if is on a phone , the compiler is going to look at this method.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_detail);
        Bundle dataToPass = getIntent().getExtras();

        SoccerDetailsFragment dFragment = new SoccerDetailsFragment();
        dFragment.setArguments(dataToPass); //pass data to the the fragment
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.soc_fragmentLocation, dFragment)
                .commit();
    }
}
