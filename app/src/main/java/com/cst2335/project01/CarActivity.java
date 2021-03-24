package com.cst2335.project01;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class CarActivity extends AppCompatActivity {
//    at least 1 Toast, Snackbar, and AlertDialog notification.

    EditText companyName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_database);

        companyName = findViewById(R.id.companyName);

        SharedPreferences prefs = getSharedPreferences("manufacturerName", Context.MODE_PRIVATE);
        String saveString = prefs.getString("ReserveName","");
        companyName.setText(saveString);

        Intent goToSearch = new Intent(CarActivity.this, SearchActivity.class);

        Button searchBtn = findViewById(R.id.searchBtn);
        searchBtn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                String savedString = companyName.getText().toString();
                goToSearch.putExtra("typeEmails",savedString);
                startActivity(goToSearch);
            }
        });
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

}