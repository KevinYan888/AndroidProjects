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
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;

public class TriviaLoadQuestions extends AppCompatActivity {
ArrayList<TriviaRandomQuestions> arrListRandomQuestions = new ArrayList<>();
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
        ListView myList = (ListView) findViewById(R.id.listView);
        myList.setAdapter(myAdapter = new MyListAdapter()); //populates the list

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
                    tempArrListRandomQues.add(jsObjOneQuestion.getString("correct_answer"));
                    Collections.shuffle(tempArrListRandomQues);//Random answers
//                    protected String strTypeOfQuestion;
//                    protected String strDifficultyOfQuestion;
//                    protected String strQuestion;
//                    protected String strCorrectAnswer;
//                    protected ArrayList<String> strIncorrectAnswers;
//                    protected ArrayList<String> randomAnswers;
                    arrListRandomQuestions.add(i,new TriviaRandomQuestions(i,
                            jsObjOneQuestion.getString("type"),
                            jsObjOneQuestion.getString("difficulty"),
                            jsObjOneQuestion.getString("question"),
                            jsObjOneQuestion.getString("correct_answer"),
                            tempArrListRandomQues
                            ));
                    Log.i("diffi", arrListRandomQuestions.get(i).getStrCorrectAnswer());
                    Log.i("diffi", arrListRandomQuestions.get(i).getStrDifficultyOfQuestion());
                    Log.i("diffi", arrListRandomQuestions.get(i).getStrQuestion());
                    Log.i("diffi", arrListRandomQuestions.get(i).getStrTypeOfQuestion());
                    Log.i("diffi", arrListRandomQuestions.get(i).getRamdomAnswers().toString());

                }

            } catch (Exception e) {

            }

            return "Done";
        }

        //Type 2
        public void onProgressUpdate(Integer... args) {

        }

        //Type3
        public void onPostExecute(String fromDoInBackground) {

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

                nameQuestion.setText(thisRow.getStrQuestion());

                RadioButton rbtnTrue = newView.findViewById(R.id.rbtnTrue);
                rbtnTrue.setText(thisRow.getRamdomAnswers().get(0));
                RadioButton rbtnfalse = newView.findViewById(R.id.rbtnfalse);
                rbtnfalse.setText(thisRow.getRamdomAnswers().get(1));

                TextView textIsAnswered = newView.findViewById(R.id.textIsAnswered);
                textIsAnswered.setText("111");

                TextView textWrong = newView.findViewById(R.id.textWrong);
                textWrong.setText("222");

                TextView textRight = newView.findViewById(R.id.textRight);
                textRight.setText("333");


            }
            else {
                newView = inflater.inflate(R.layout.activity_trivia_boolean_question, viewGroup, false);

                TextView nameQuestion = newView.findViewById(R.id.textNameOfQuestion);

                nameQuestion.setText(thisRow.getStrQuestion());

                RadioButton rbtnTrue = newView.findViewById(R.id.rbtnTrue);
                rbtnTrue.setText(thisRow.getRamdomAnswers().get(0));
                RadioButton rbtnfalse = newView.findViewById(R.id.rbtnfalse);
                rbtnfalse.setText(thisRow.getRamdomAnswers().get(1));

                TextView textIsAnswered = newView.findViewById(R.id.textIsAnswered);
                textIsAnswered.setText("111");

                TextView textWrong = newView.findViewById(R.id.textWrong);
                textWrong.setText("222");

                TextView textRight = newView.findViewById(R.id.textRight);
                textRight.setText("333");
            }


//            //finding what's in layout file
//            TextView thisRowMessage = (TextView)newView.findViewById(R.id.textGoesHere);
//            thisRowMessage.setText(thisRow.getOneOfChatText());

            return newView;
        }
        @Override //returns the database id of item i
        public long getItemId(int position) {
            return getItem(position).getId();
        }
    }
}