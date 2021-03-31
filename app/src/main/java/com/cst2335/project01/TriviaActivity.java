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
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class TriviaActivity extends AppCompatActivity {

    private int intAmountOfQuestion;
    private String strTypeOfQuestion;
    private String strDifficultyOfQuestion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trivia);

        //Number of questions
        EditText textEditNumberQuestion = findViewById(R.id.textEditAmountOfQuestion);

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
                strTypeOfQuestion = "String.valueOf(rb1.getText())";

            }
        });

        radioGroupDifficulty.setOnCheckedChangeListener((rg2,i)->{
            String oldDifficulty = strDifficultyOfQuestion;
            RadioButton rb2 = findViewById(radioGroupDifficulty.getCheckedRadioButtonId());

            strDifficultyOfQuestion= String.valueOf(rb2.getText());
            //Add a Snack bar
            if(strDifficultyOfQuestion.equals("hard")){

                        Snackbar snackbar = Snackbar
                                .make(rg2, "You select hard level !", Snackbar.LENGTH_LONG)
                                .setAction("UNDO", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        Snackbar snackbar1 = Snackbar.make(rg2, "You cancel hard level !", Snackbar.LENGTH_SHORT);
                                        snackbar1.show();
                                        //redo
                                        strDifficultyOfQuestion = oldDifficulty;
                                        rb2.setChecked(false);
                                    }

                                });

                        snackbar.show();

           }


        });
        //btnGoBack: goToMainActivity
        btnGoBack.setOnClickListener(back->{
            Intent goToMainActivity = new Intent(TriviaActivity.this, MainActivity.class);
            startActivity(goToMainActivity);
        });

        //btnPlayNow: goToLoadQuestions
        btnPlayNow.setOnClickListener(play->{

          String numQuestions = String.valueOf(textEditNumberQuestion.getText());

            if(isInteger(numQuestions) &&
                    (Integer.parseInt(numQuestions) > 0) &&
                    (Integer.parseInt(numQuestions) < 1000) &&
                    !((strTypeOfQuestion == null) || "".equals(strTypeOfQuestion)) &&
                    !((strDifficultyOfQuestion == null) || "".equals(strDifficultyOfQuestion)))
            {//All information has been filled in and is correct

                intAmountOfQuestion = Integer.parseInt(numQuestions);
                //pass info to next activity
                Bundle bundle = new Bundle();
                bundle.putInt("intAmountOfQuestion",intAmountOfQuestion);
                bundle.putString("strTypeOfQuestion",strTypeOfQuestion);
                bundle.putString("strDifficultyOfQuestion",strDifficultyOfQuestion);
                Intent goToLoadQuestions = new Intent();
                goToLoadQuestions.putExtras(bundle);
                goToLoadQuestions.setClass(TriviaActivity.this,TriviaLoadQuestions.class);
                startActivity(goToLoadQuestions);
            }
            else{
                Toast.makeText(this,
                        " Something is incomplete or incorrect. Please try again!  ", Toast.LENGTH_LONG).show();
            }
        });
    }
    //isInteger？
    public static boolean isInteger(String value){
        try {
            Integer.parseInt(value);
            return true;
        }catch(NumberFormatException e){
            return false;
        }
    }
}

