package com.cst2335.project01;

import java.util.ArrayList;

//each question create an object of TriviaQuestion
public class TriviaQuestion {
    protected String strTypeOfQuestion = "";
    protected String strDifficultyOfQuestion = "";
    protected String strQuestion = "";
    protected String strCorrectAnswer = "";
    protected ArrayList<String> strIncorrectAnswers = new ArrayList<>();
    protected String strStateOfQuestion = "";
    protected String strAnswerOfPlayer = "";

    public TriviaQuestion(String type,String diff,String ques,String correct, ArrayList<String> incorrect,String state,String answerOfPlayer){
        this.strTypeOfQuestion = type;
        this.strDifficultyOfQuestion = diff;
        this.strQuestion = ques;
        this.strCorrectAnswer = correct;
        this.strIncorrectAnswers = incorrect;
        this.strStateOfQuestion = state;
        this.strAnswerOfPlayer = answerOfPlayer;
    }

    public String getStrQuestion() {
        return strQuestion;
    }

    public String getStrCorrectAnswer() {
        return strCorrectAnswer;
    }

    public ArrayList<String> getStrIncorrectAnswers() {
        return strIncorrectAnswers;
    }

    public String getStrStateOfQuestion() {
        return strStateOfQuestion;
    }

    public String getStrAnswerOfPlayer() {
        return strAnswerOfPlayer;
    }

    public String getStrTypeOfQuestion() {
        return strTypeOfQuestion;
    }

    public String getStrDifficultyOfQuestion() {
        return strDifficultyOfQuestion;
    }

    public void setStrTypeOfQuestion(String strTypeOfQuestion) {
        this.strTypeOfQuestion = strTypeOfQuestion;
    }

    public void setStrDifficultyOfQuestion(String strDifficultyOfQuestion) {
        this.strDifficultyOfQuestion = strDifficultyOfQuestion;
    }

    public void setStrQuestion(String strQuestion) {
        this.strQuestion = strQuestion;
    }

    public void setStrCorrectAnswer(String strCorrectAnswer) {
        this.strCorrectAnswer = strCorrectAnswer;
    }

    public void setStrIncorrectAnswers(ArrayList<String> strIncorrectAnswers) {
        this.strIncorrectAnswers = strIncorrectAnswers;
    }

    public void setStrStateOfQuestion(String strStateOfQuestion) {
        this.strStateOfQuestion = strStateOfQuestion;
    }

    public void setStrAnswerOfPlayer(String strAnswerOfPlayer) {
        this.strAnswerOfPlayer = strAnswerOfPlayer;
    }
}
