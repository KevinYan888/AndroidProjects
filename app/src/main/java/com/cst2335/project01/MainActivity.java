package com.cst2335.project01;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
        setContentView(R.layout.activity_main);

        Button btnTrivia;
        Button btnSongster;
        Button btnCar;
        Button btnSoccer;
        btnTrivia = findViewById(R.id.btnTrivia);
//        btnTrivia.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View v) {
////            Intent goToChat = new Intent();
////            goToChat.setClass(ProfileActivity.this, ChatRoomActivity.class);
//                Intent goToChat = new Intent(MainActivity.this, TriviaActivity.class);
//                startActivity(goToChat);
//            }
//        });
        btnSongster = findViewById(R.id.btnSongster);
       btnSongster.setOnClickListener(new View.OnClickListener(){
            @Override
           public void onClick(View v) {
     //       Intent goToChat = new Intent();
     //       goToChat.setClass(MainActivity.this, SongActivity.class);
                Intent goToSong = new Intent(MainActivity.this, SongActivity.class);
                startActivity(goToSong);
            }
        });
        btnCar = findViewById(R.id.btnCar);
//        Intent goToCar = new Intent(MainActivity.this, CarActivity.class);
//        btnCar.setOnClickListener( bt->{
//            startActivity(goToCar);
//        });

        btnCar.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
//            Intent goToChat = new Intent();
//            goToChat.setClass(ProfileActivity.this, ChatRoomActivity.class);
                Intent goToCar = new Intent(MainActivity.this, CarActivity.class);
                startActivity(goToCar);
            }
        });

//        btnSoccer = findViewById(R.id.btnSoccer);
//        btnSoccer.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View v) {
////            Intent goToChat = new Intent();
////            goToChat.setClass(ProfileActivity.this, ChatRoomActivity.class);
//                Intent goToChat = new Intent(MainActivity.this, SoccerActivity.class);
//                startActivity(goToChat);
//            }
//        });

    }
}