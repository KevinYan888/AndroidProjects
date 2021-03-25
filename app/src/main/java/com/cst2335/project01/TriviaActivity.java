package com.cst2335.project01;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

//https://opentdb.com/api.php?amount=10&difficulty=easy&type=multiple
//https://opentdb.com/api.php?amount=10&difficulty=medium&type=boolean
public class TriviaActivity extends AppCompatActivity {

    private String strPlayerName;
    private int intAmountOfQuestion;
    private String strTypeOfQuestion;
    private String strDifficultyOfQuestion;

    public String getStrPlayerName() {
        return strPlayerName;
    }

    public int getIntAmountOfQuestion() {
        return intAmountOfQuestion;
    }

    public String getStrTypeOfQuestion() {
        return strTypeOfQuestion;
    }

    public String getStrDifficultyOfQuestion() {
        return strDifficultyOfQuestion;
    }
//  ArrayList<TriviaQuestion> arrayListTriviaQuestions;
//    ArrayList<String> arrayListTemp;
//    //initialize newRound
//    TriviaGameRound newRound = new TriviaGameRound();
//    private MyListAdapter myAdapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trivia);



        //Number of questions
        EditText textEditNumberQuestion = findViewById(R.id.textEditAmountOfQuestion);
        //Number of questions
        EditText textEditNameOfPlayer = findViewById(R.id.textEditPlayerName);


        //Game Type(True or false/Multiple choice/Both)
        RadioGroup radioGroupType = findViewById(R.id.radioGroupType);
//        RadioButton rbtnTypeTrue = findViewById(R.id.rbtnTypeTrue);
//        RadioButton rbtnTypeMultiple = findViewById(R.id.rbtnTypeMultiple);
//        RadioButton rbtnTypeBoth = findViewById(R.id.rbtnTypeBoth);

        //Game level(Easy/Medium/Hard)
        RadioGroup radioGroupDifficulty = findViewById(R.id.radioGroupDifficulty);
//        RadioButton rbtnDifficultyEasy = findViewById(R.id.rbtnDifficultyEasy);
//        RadioButton rbtnDifficultyMedium = findViewById(R.id.rbtnDifficultyMedium);
//        RadioButton rbtnDifficultyHard = findViewById(R.id.rbtnDifficultyHard);

        //Buttons
        Button btnPlayNow = findViewById(R.id.btnPlayNow);
        Button btnGoBack = findViewById(R.id.btnGoBack);

        radioGroupType.setOnCheckedChangeListener((rg1,i)->{
            RadioButton rb1 = findViewById(radioGroupType.getCheckedRadioButtonId());
            if(String.valueOf(rb1.getText()).contains("true")){
                strTypeOfQuestion = "boolean";
            }
            else if(String.valueOf(rb1.getText()).contains("multiple")){
                strTypeOfQuestion ="multiple";
            }
            else{
                strTypeOfQuestion = String.valueOf(rb1.getText());

            }
        });

        radioGroupDifficulty.setOnCheckedChangeListener((rg2,i)->{
            RadioButton rb2 = findViewById(radioGroupDifficulty.getCheckedRadioButtonId());
            strDifficultyOfQuestion= String.valueOf(rb2.getText());

        });

        btnPlayNow.setOnClickListener(play->{


          intAmountOfQuestion =  Integer.parseInt(String.valueOf(textEditNumberQuestion.getText()));
          strPlayerName = String.valueOf(textEditNameOfPlayer.getText());

          //pass info to next activity
          Bundle bundle = new Bundle();
          bundle.putString("strPlayerName",strPlayerName);
            bundle.putInt("intAmountOfQuestion",intAmountOfQuestion);
            bundle.putString("strTypeOfQuestion",strTypeOfQuestion);
            bundle.putString("strDifficultyOfQuestion",strDifficultyOfQuestion);
            bundle.putString("strPlayerName",strPlayerName);
            Intent goToLoadQuestions = new Intent();
            goToLoadQuestions.putExtras(bundle);
            goToLoadQuestions.setClass(TriviaActivity.this,TriviaLoadQuestions.class);
            startActivity(goToLoadQuestions);




         //open the next Layout
  //       setContentView(R.layout.activity_trivia_listview);
//            ListView myList = findViewById(R.id.theListView);
//            myList.setAdapter( myAdapter = new MyListAdapter());
        });
    }


//    private class MyListAdapter extends BaseAdapter {
//
//        public int getCount() { return newRound.arrayListTriviaQuestions.size();}
//
//        public Object getItem(int position) { return "This is row " + position; }
//
//        public long getItemId(int position) { return (long) position; }
//
//        public View getView(int position, View old, ViewGroup parent)
//        {
//            LayoutInflater inflater = getLayoutInflater();
//
//            //make a new row:
//            View newView = inflater.inflate(R.layout.activity_trivia_boolean_question, parent, false);
//
//            //set what the text should be for this row:
////            TextView tView = newView.findViewById(R.id.textGoesHere);
////            tView.setText( getItem(position).toString() );
////            RadioGroup radioGroupBoolean = newView.findViewById(R.id.radioGroupBoolean);
////           TextView textNameOfQuestion = newView.findViewById(R.id.textNameOfQuestion);
////           textNameOfQuestion.setText(newRound.arrayListTriviaQuestions.get(0).strQuestion);
//            //return it to be put in the table
//            return newView;
//        }
//    }

}

