package com.cst2335.project01;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class TriviaLoadQuestions extends AppCompatActivity {
//    TriviaActivity.MyHTTPRequest req = new TriviaActivity.MyHTTPRequest();
//            req.execute("https://opentdb.com/api.php?amount="+newRound.getIntNumberOfQuestions()+
//                    "&difficulty="+newRound.getStrDifficultyOfGame()+"&type="+newRound.getStrTypeOfGame());  //Type 1
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load_questions);
    }
//    private class MyHTTPRequest extends AsyncTask< String, Integer, String>
//    {
//
//
//        String result;
//        JSONObject jsObjRoot;
//        JSONObject jsObjOneQuestion;
//        JSONArray jsArrQuestionsAndResults;
//        JSONArray jsArrIncorrectAnswers;
//        //Type3                Type1
//        public String doInBackground(String ... args)
//        {
//            try {
//
//                //create a URL object of what server to contact:
//                URL url = new URL(args[0]);
//
//                //open the connection
//                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
//
//                //wait for data:
//                InputStream response = urlConnection.getInputStream();
//
//                //JSON reading:   Look at slide 26
//                //Build the entire string response:
//                BufferedReader reader = new BufferedReader(new InputStreamReader(response, "UTF-8"), 8);
//                StringBuilder sb = new StringBuilder();
//
//                String line = null;
//                while ((line = reader.readLine()) != null)
//                {
//                    sb.append(line + "\n");
//                }
//                result = sb.toString(); //result is the whole string
//                Log.i("llll",result.toString());
//
//
//                // convert string to JSON
//                jsObjRoot = new JSONObject(result);
//                Log.i("jsObjRoot",jsObjRoot.toString());
//                jsArrQuestionsAndResults = jsObjRoot.getJSONArray("results");
//                Log.i("Results",jsArrQuestionsAndResults.getString(0));
//                for(int i = 0; i < jsArrQuestionsAndResults.length(); i++){
//                    jsObjOneQuestion = jsArrQuestionsAndResults.getJSONObject(i);
//                    Log.i("jsObjOneQuestion",jsObjOneQuestion.toString());
//                    jsArrIncorrectAnswers = jsObjOneQuestion.getJSONArray("incorrect_answers");
//                    Log.i("jsArrIncorrectAnswers",jsArrIncorrectAnswers.toString());
//                    arrayListTemp = new ArrayList<>();
//                    arrayListTemp.clear();
//                    if(jsArrIncorrectAnswers !=null){
//                        for(int j=0; j<jsArrIncorrectAnswers.length();j++){
//                            arrayListTemp.add(jsArrIncorrectAnswers.getString(j));
//                        }
//                    }
//                    // Log.i("arrayListTemp",arrayListTemp.toString());
//
//
//                    Log.i("jsObType",jsObjOneQuestion.getString("type"));
//                    Log.i("difficulty",jsObjOneQuestion.getString("difficulty"));
//                    newRound.arrayListTriviaQuestions = new ArrayList<>();
//                    newRound.arrayListTriviaQuestions.add(new TriviaQuestion(
//                            jsObjOneQuestion.getString("type"),
//                            jsObjOneQuestion.getString("difficulty"),
//                            jsObjOneQuestion.getString("question"),
//                            jsObjOneQuestion.getString("correct_answer"),
//                            arrayListTemp,
//                            "unanswered",
//                            ""));
//                    Log.i("yyy","yyy");
//                    Log.i("listQuestions",newRound.arrayListTriviaQuestions.get(0).getStrCorrectAnswer());
//                    Log.i("listQuestions",newRound.arrayListTriviaQuestions.get(0).getStrDifficultyOfQuestion());
//                    Log.i("listQuestions",newRound.arrayListTriviaQuestions.get(0).strIncorrectAnswers.toString());
//
//                }
//                Log.i("name of player",newRound.getNameOfPlayer());
//                Log.i("difficulty",newRound.strDifficultyOfGame);
//                Log.i("difficulty",newRound.strDifficultyOfGame);
//                Log.i("Round info",newRound.getNameOfPlayer());
//                Log.i("Round info",newRound.getStrDifficultyOfGame());
//
//
////                }
//
////                //get the double associated with "value"
////                String uvRating = jsObjRoot.getString("correct_answer");
////
////                Log.i("MainActivity", "The uv is now: " + uvRating) ;
//
//            }
//            catch (Exception e)
//            {
//
//            }
//
//            return "Done";
//        }
//
//        //Type 2
//        public void onProgressUpdate(Integer ... args)
//        {
//
//        }
//        //Type3
//        public void onPostExecute(String fromDoInBackground)
//        {
//            Log.i("HTTP", fromDoInBackground);
//        }
//    }
}