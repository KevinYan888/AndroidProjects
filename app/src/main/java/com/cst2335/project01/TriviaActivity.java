package com.cst2335.project01;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

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
    //initialize newRound
    TriviaGameRound newRound = new TriviaGameRound();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trivia);




        //Number of questions
        EditText textEditNumberQuestion = findViewById(R.id.textEditNumberQuestion);

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
                newRound.setStrTypeOfGame("boolean");
            }
            else if(String.valueOf(rb1.getText()).contains("multiple")){
                newRound.setStrTypeOfGame("boolean");
            }
            else{
                newRound.setStrTypeOfGame(String.valueOf(rb1.getText()));

            }
        });

        radioGroupDifficulty.setOnCheckedChangeListener((rg2,i)->{
            RadioButton rb2 = findViewById(radioGroupDifficulty.getCheckedRadioButtonId());
            newRound.setStrDifficultyOfGame(String.valueOf(rb2.getText()));

        });

        btnPlayNow.setOnClickListener(play->{


            newRound.setIntNumberOfQuestions(Integer.parseInt(String.valueOf(textEditNumberQuestion.getText())));

            MyHTTPRequest req = new MyHTTPRequest();
            req.execute("https://opentdb.com/api.php?amount="+newRound.getIntNumberOfQuestions()+"&difficulty="+newRound.getStrDifficultyOfGame()+"&type="+newRound.getStrTypeOfGame());  //Type 1


        });
    }
    private class MyHTTPRequest extends AsyncTask< String, Integer, String>
    {
        JSONObject jsObjRoot;
        JSONObject jsObjOneQuestion;
        JSONArray jsArrQuestionsAndResults;
        JSONArray jsArrIncorrectAnswers;
        //Type3                Type1
        public String doInBackground(String ... args)
        {
            try {

                //create a URL object of what server to contact:
                URL url = new URL(args[0]);

                //open the connection
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

                //wait for data:
                InputStream response = urlConnection.getInputStream();

                //JSON reading:   Look at slide 26
                //Build the entire string response:
                BufferedReader reader = new BufferedReader(new InputStreamReader(response, "UTF-8"), 8);
                StringBuilder sb = new StringBuilder();

                String line = null;
                while ((line = reader.readLine()) != null)
                {
                    sb.append(line + "\n");
                }
                String result = sb.toString(); //result is the whole string


                // convert string to JSON
                jsObjRoot = new JSONObject(result);
                jsArrQuestionsAndResults = jsObjRoot.getJSONArray("results");
                for(int i = 0; i < jsArrQuestionsAndResults.length(); i++){
                    jsObjOneQuestion = jsArrQuestionsAndResults.getJSONObject(i);
                    jsArrIncorrectAnswers = jsObjOneQuestion.getJSONArray("incorrect_answers");

                    ArrayList<String> arrayListTemp = new ArrayList<>();
                    if(jsArrIncorrectAnswers !=null){
                        for(int j=0; j<jsArrIncorrectAnswers.length();j++){
                            arrayListTemp.add(jsArrIncorrectAnswers.getString(j));
                        }
                    }

                    newRound.arrayListTriviaQuestions.add(new TriviaQuestion(
                            jsObjOneQuestion.getString("type"),
                            jsObjOneQuestion.getString("difficulty"),
                            jsObjOneQuestion.getString("question"),
                            jsObjOneQuestion.getString("correct_answer"),
                            arrayListTemp,
                            "unanswered",
                            ""));



                }
//    public TriviaQuestion(String type,String diff,String ques,String correct, String[] incorrect,String state,String answerOfPlayer){
//                    this.strTypeOfQuestion = type;
//                    this.strDifficultyOfQuestion = diff;
//                    this.strQuestion = ques;
//                    this.strCorrectAnswer = correct;
//                    this.strIncorrectAnswers = incorrect;
//                    this.strStateOfQuestion = state;
//                    this.strAnswerOfPlayer = answerOfPlayer;
//                }

//                //get the double associated with "value"
//                String uvRating = jsObjRoot.getString("correct_answer");
//
//                Log.i("MainActivity", "The uv is now: " + uvRating) ;

            }
            catch (Exception e)
            {

            }

            return "Done";
        }

        //Type 2
        public void onProgressUpdate(Integer ... args)
        {

        }
        //Type3
        public void onPostExecute(String fromDoInBackground)
        {
            Log.i("HTTP", fromDoInBackground);
        }
    }
}

