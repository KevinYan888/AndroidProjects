package com.cst2335.project01;

import androidx.appcompat.app.AlertDialog;
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
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;

public class TriviaLoadQuestions extends AppCompatActivity {
    //arrListRandomQuestions
    ArrayList<TriviaRandomQuestions> arrListRandomQuestions = new ArrayList<>();
    ProgressBar proBar;

    private int numOfCorrectPlayer;
    private int numOfIncorrectPlayer;
    private int numOfUnansweredPlayer;

    public double scoreOfPlayer;
    public String namePlayer;
    public String strDifficultyOfQuestion;

    private MyListAdapter myAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load_questions);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        JsonQuery jq = new JsonQuery();
        jq.execute("https://opentdb.com/api.php?amount=" + bundle.getInt("intAmountOfQuestion") +
                "&difficulty=" + bundle.getString("strDifficultyOfQuestion") + "&type=" + bundle.getString("strTypeOfQuestion"));  //Type 1
        strDifficultyOfQuestion = bundle.getString("strDifficultyOfQuestion");

        ListView myList = (ListView) findViewById(R.id.listView);
        myList.setAdapter(myAdapter = new MyListAdapter()); //populates the list

        Button btnSubmit = findViewById(R.id.btnSubmit);
        Button btnGoBack = findViewById(R.id.btnGoBack);
        TextView titleQuestion = findViewById(R.id.titleQuestion);
        proBar = findViewById(R.id.proBar);
//        titleQuestion.setText("Enjoy your game");


        btnSubmit.setOnClickListener(clk->{
           if(resultOfGameCalculate()>0){//calculate results and return numOfUnansweredPlayer
               Toast.makeText(this, numOfUnansweredPlayer+
                       " question(s) no answer.You need finished all questions!  ", Toast.LENGTH_LONG).show();
           }else {

               //show the result on a dialog
               View dialogResultView = getLayoutInflater().inflate(R.layout.trivia_dialog_result, null);

               TextView textCorrect = dialogResultView.findViewById(R.id.textCorrect);
               TextView textIncorrect = dialogResultView.findViewById(R.id.textIncorrect);
               textCorrect.setText("Correct : " + numOfCorrectPlayer);
               textIncorrect.setText("Incorrect: " + numOfIncorrectPlayer);
               EditText editTextPlayerName = dialogResultView.findViewById(R.id.textEditPlayerName);

               AlertDialog.Builder builder = new AlertDialog.Builder(this);
               builder.setTitle("Good job! Your score:  " + String.format("%.1f", scoreOfPlayer) + "(*%)")
                       .setMessage("Enter your name and save result: ")
                       .setView(dialogResultView) //add texts showing the contact information
                       .setPositiveButton("Return ", (click, b) -> {

                       })
                       .setNegativeButton("Exit", (click, b) -> {
                           Intent goToMainActivity = new Intent(TriviaLoadQuestions.this, MainActivity.class);
                           startActivity(goToMainActivity);
                       })
                       .setNeutralButton("Save result", (click, b) -> {
                           namePlayer = editTextPlayerName.getText().toString();
                           strDifficultyOfQuestion = bundle.getString("strDifficultyOfQuestion");

                           //pass info to next activity
                           Bundle bundleToLeaderBoard = new Bundle();

                           bundleToLeaderBoard.putString("namePlayer",namePlayer);
                           bundleToLeaderBoard.putString("strDifficultyOfQuestion",strDifficultyOfQuestion);
                           bundleToLeaderBoard.putDouble("scoreOfPlayer",scoreOfPlayer);

                           Intent goToHighScoreLeaderBoard = new Intent();
                           goToHighScoreLeaderBoard.putExtras(bundleToLeaderBoard);
                           goToHighScoreLeaderBoard.setClass(TriviaLoadQuestions.this,TriviaHighScoreLeaderboard.class);
                           startActivity(goToHighScoreLeaderBoard);
                       })
                       .create().show();
           }
        });
        btnGoBack.setOnClickListener(cancel->{
            Intent goToTriviaActivity = new Intent();
            goToTriviaActivity.setClass(TriviaLoadQuestions.this,TriviaActivity.class);
            startActivity(goToTriviaActivity);
        });

    }
    public int resultOfGameCalculate(){//calculate results and return numOfUnansweredPlayer
        numOfCorrectPlayer = 0;
        numOfIncorrectPlayer = 0;
        numOfUnansweredPlayer = 0;
        for(int i=0;i<arrListRandomQuestions.size();i++){

            if(arrListRandomQuestions.get(i).getStrCorrectAnswer().equals(arrListRandomQuestions.get(i).getStrAnswerOfPlayer())){
                numOfCorrectPlayer++;
            }
            else if (arrListRandomQuestions.get(i).getStrStateOfQuestion().equals("Unanswered")){
                numOfUnansweredPlayer++;
            }
            else {
                numOfIncorrectPlayer++;
            }
            //numOfUnansweredPlayer--;
        }
        scoreOfPlayer = numOfCorrectPlayer/((numOfCorrectPlayer+numOfUnansweredPlayer+numOfIncorrectPlayer)*1.0)*100;
        return numOfUnansweredPlayer;
    }

    private class JsonQuery extends AsyncTask<String, Integer, String> {
        String result;
        JSONObject jsObjRoot;
        JSONObject jsObjOneQuestion;
        JSONArray jsArrQuestionsAndAnswers;
        JSONArray jsArrIncorrectAnswers;

        //Type3                Type1
        public String doInBackground(String... args) {
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
                publishProgress(25);
                String line = null;
                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                }
                result = sb.toString(); //result is the whole string
                Log.i("llll", result.toString());


                // convert string to JSON
                jsObjRoot = new JSONObject(result);

                jsArrQuestionsAndAnswers = jsObjRoot.getJSONArray("results");


                for (int i = 0; i < jsArrQuestionsAndAnswers.length(); i++) {
                    jsObjOneQuestion = jsArrQuestionsAndAnswers.getJSONObject(i);


                    jsArrIncorrectAnswers = jsObjOneQuestion.getJSONArray("incorrect_answers");
                  //  TriviaRandomQuestions objRandomQuestions = new TriviaRandomQuestions();
                    ArrayList<String>tempArrListRandomQues = new ArrayList<>();
                    if (jsArrIncorrectAnswers != null) {
                        for (int j = 0; j < jsArrIncorrectAnswers.length(); j++) {
                            tempArrListRandomQues.add(jsArrIncorrectAnswers.getString(j));
                        }
                    }
                    publishProgress(50);
                    tempArrListRandomQues.add(jsObjOneQuestion.getString("correct_answer"));
                    Collections.shuffle(tempArrListRandomQues);//Random answers

                    arrListRandomQuestions.add(i,new TriviaRandomQuestions(i,
                            jsObjOneQuestion.getString("type"),
                            jsObjOneQuestion.getString("difficulty"),
                            jsObjOneQuestion.getString("question"),
                            jsObjOneQuestion.getString("correct_answer"),
                            tempArrListRandomQues
                            ));
                    publishProgress(75);
                }
                publishProgress(100);

            } catch (Exception e) {

            }

            return "Done";
        }

        //Type 2
        public void onProgressUpdate(Integer... args) {
            proBar.setVisibility(View.VISIBLE);
            proBar.setProgress(args[0]);
        }

        //Type3
        public void onPostExecute(String fromDoInBackground) {
            proBar.setVisibility(View.INVISIBLE);
        myAdapter.notifyDataSetChanged();

        }
    }
    //an inner class
    class MyListAdapter extends BaseAdapter {
        @Override //number of items in the list
        public int getCount() {
            return arrListRandomQuestions.size();
        }

        public TriviaRandomQuestions getItem(int position) {
            return arrListRandomQuestions.get(position);
        }

        @Override   //how to show it: button, texView, checkbox?
        public View getView(int position, View view, ViewGroup viewGroup) {
            LayoutInflater inflater = getLayoutInflater();
            View newView;
            TriviaRandomQuestions thisRow = getItem(position);
            if(thisRow.getStrTypeOfQuestion().equals("boolean") ){
                newView = inflater.inflate(R.layout.activity_trivia_boolean_question, viewGroup, false);

                TextView nameQuestion = newView.findViewById(R.id.textNameOfQuestion);

                nameQuestion.setText(position+1+". "+thisRow.getStrQuestion());


                RadioButton rbtnTrue = newView.findViewById(R.id.rbtnTrue);
                rbtnTrue.setText(thisRow.getRamdomAnswers().get(0));
                RadioButton rbtnfalse = newView.findViewById(R.id.rbtnfalse);
                rbtnfalse.setText(thisRow.getRamdomAnswers().get(1));

                TextView textStateOfQuestion = newView.findViewById(R.id.textStateOfQuestion);
                RadioGroup radioGroupBoolean = newView.findViewById(R.id.radioGroupBoolean);

                radioGroupBoolean.setOnCheckedChangeListener((rgb,i)->{
                    RadioButton isSelected = findViewById(radioGroupBoolean.getCheckedRadioButtonId());
                    //setStrAnswerOfPlayer
                    thisRow.setStrAnswerOfPlayer(isSelected.getText().toString());

                    if(thisRow.getStrCorrectAnswer().equals(isSelected.getText().toString()))
                    {
                        //Unanswered/True/False
                        textStateOfQuestion.setText("True");
                        thisRow.setStrStateOfQuestion("True");//setStrStateOfQuestion

                    }
                    else {
                        textStateOfQuestion.setText("False");
                        thisRow.setStrStateOfQuestion("False");//setStrStateOfQuestion
                    }
                    // myAdapter.notifyDataSetChanged();
                });

            }
            else {
                newView = inflater.inflate(R.layout.activity_trivia_multiple_question, viewGroup, false);

                TextView nameQuestion = newView.findViewById(R.id.textNameOfQuestion);

                nameQuestion.setText(position+1+". "+thisRow.getStrQuestion());
//                RadioGroup radioGroupMulti = newView.findViewById(R.id.radioGroupMulti);

                RadioButton rbtnMult1 = newView.findViewById(R.id.rbtnMult1);
                rbtnMult1.setText(thisRow.getRamdomAnswers().get(0));
                RadioButton rbtnMult2 = newView.findViewById(R.id.rbtnMult2);
                rbtnMult2.setText(thisRow.getRamdomAnswers().get(1));
                RadioButton rbtnMult3 = newView.findViewById(R.id.rbtnMult3);
                rbtnMult3.setText(thisRow.getRamdomAnswers().get(2));
                RadioButton rbtnMult4 = newView.findViewById(R.id.rbtnMult4);
                rbtnMult4.setText(thisRow.getRamdomAnswers().get(3));


                TextView textStateOfQuestion = newView.findViewById(R.id.textStateOfQuestion);
                RadioGroup radioGroupMulti = newView.findViewById(R.id.radioGroupMulti);

                radioGroupMulti.setOnCheckedChangeListener((RadioGroup rgb,int CheckedId)->{

 //                  RadioButton isSelected = findViewById(radioGroupMulti.getCheckedRadioButtonId());
                    RadioButton isSelected = newView.findViewById(CheckedId);
                    //setStrAnswerOfPlayer
                    thisRow.setStrAnswerOfPlayer(isSelected.getText().toString());

                    if(thisRow.getStrCorrectAnswer().equals(isSelected.getText().toString()))
                    {
                        //Unanswered/True/False
                        textStateOfQuestion.setText("True");
                        thisRow.setStrStateOfQuestion("True");//setStrStateOfQuestion

                    }
                    else {
                        textStateOfQuestion.setText("False");
                        thisRow.setStrStateOfQuestion("False");//setStrStateOfQuestion
                    }
                    Log.e("stateOfQuestionInList",arrListRandomQuestions.get(position).getStrAnswerOfPlayer().toString());
                    Log.e("stateOfQuestionInList",arrListRandomQuestions.get(position).getStrStateOfQuestion().toString());
                    Log.e("player answer",isSelected.getText().toString());
                    Log.e("correct answer",thisRow.getStrCorrectAnswer());
                    Log.i("checkedId",Integer.toString(CheckedId));
                    Log.i("position",Integer.toString(position));
                    Log.i("RadioGroup Id",Integer.toString(rgb.getId()));

                });

            }
            myAdapter.notifyDataSetChanged();


            return newView;
        }
        @Override //returns the database id of item i
        public long getItemId(int position) {
            return getItem(position).getId();
        }
    }
}