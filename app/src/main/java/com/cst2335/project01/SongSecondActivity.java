package com.cst2335.project01;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class SongSecondActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_song_second );

        Bundle dataToPass = getIntent().getExtras(); //get the data that was passed from FragmentExample

        //This is copied directly from FragmentExample.java lines 47-54
        SongDetailFragment songDFragment = new SongDetailFragment();
        songDFragment.setArguments( dataToPass ); //pass data to the the fragment
        getSupportFragmentManager()
                .beginTransaction()
                .replace( R.id.fragmentLocation, songDFragment)
                .commit();
    }
}