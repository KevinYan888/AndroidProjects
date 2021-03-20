package com.cst2335.project01;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;

public class CarActivity extends AppCompatActivity {
    ListView listView;
    Button btnTrivia;
    Button btnSongster;
    Button btnCardata;
    Button btnSoccer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_database);

        listView = findViewById(R.id.listView);
        btnCardata = findViewById(R.id.btnCardata);

        btnCardata.setOnClickListener( bt->{
//            startActivity(goToCar);
        });
    }
}