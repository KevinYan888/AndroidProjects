package com.cst2335.project01;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class CarEmptyActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.car_activity_empty);

        Bundle dataToPass = getIntent().getExtras(); //get the data that was passed from FragmentExample

        //This is copied directly from FragmentExample.java lines 47-54
        CardataFragment carFragment = new CardataFragment();
        carFragment.setArguments( dataToPass ); //pass data to the the fragment
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fLayout, carFragment)
                .commit();
    }
}