package com.cst2335.project01;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

public class TriviaSettingActivity extends AppCompatActivity {

    private int intAmountOfQuestion;
    private String strTypeOfQuestion;
    private String strDifficultyOfQuestion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_trivia);

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

        radioGroupType.setOnCheckedChangeListener((RadioGroup group1,int checkedId)->{
            switch (checkedId){
                case R.id.rbtnTypeTrue:
                    strTypeOfQuestion = "boolean";
                    break;
                case R.id.rbtnTypeMultiple:
                    strTypeOfQuestion = "multiple";
                    break;
                case R.id.rbtnTypeBoth:
                    strTypeOfQuestion = "both";
                    break;
            }


        });

        radioGroupDifficulty.setOnCheckedChangeListener((RadioGroup group2,int checkedId)->{
            String oldDifficulty = strDifficultyOfQuestion;
            RadioButton rb2 = findViewById(radioGroupDifficulty.getCheckedRadioButtonId());
                        switch (checkedId){
                            case R.id.rbtnDifficultyEasy:
                                strDifficultyOfQuestion = "Easy";
                                break;
                            case R.id.rbtnDifficultyMedium:
                                strDifficultyOfQuestion = "Medium";
                                break;
                            case R.id.rbtnDifficultyHard:
                                strDifficultyOfQuestion = "Hard";
                                Snackbar snackbar = Snackbar
                                        .make(rb2, "You select hard level !", Snackbar.LENGTH_LONG)
                                        .setAction("UNDO", new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                Snackbar snackbar1 = Snackbar.make(rb2, "You cancel hard level !", Snackbar.LENGTH_SHORT);
                                                snackbar1.show();
                                                //redo
                                                strDifficultyOfQuestion = oldDifficulty;
                                                rb2.setChecked(false);
                                            }

                                        });

                                snackbar.show();
                                break;
                        }

        });
        //btnGoBack: goToMainActivity
        btnGoBack.setOnClickListener(back->{
//            Intent goToMainActivity = new Intent(TriviaActivity.this, MainActivity.class);
//            startActivity(goToMainActivity);

//            Intent goToToolbar = new Intent(TriviaSettingActivity.this, TriviaActivity.class);
//            startActivity(goToToolbar);
            Intent goToRankList = new Intent(TriviaSettingActivity.this, TriviaRankItemsActivity.class);
            startActivity(goToRankList);

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
                bundle.putString("strTypeOfQuestion",strTypeOfQuestion.toLowerCase());
                bundle.putString("strDifficultyOfQuestion",strDifficultyOfQuestion.toLowerCase());
                Intent goToLoadQuestions = new Intent();
                goToLoadQuestions.putExtras(bundle);
                goToLoadQuestions.setClass(TriviaSettingActivity.this, TriviaQuestionItemsActivity.class);
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

