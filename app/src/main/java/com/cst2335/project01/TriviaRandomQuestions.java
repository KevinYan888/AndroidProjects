package com.cst2335.project01;

import java.util.ArrayList;

//each question create an object of TriviaQuestion
public class TriviaRandomQuestions {
    protected String strTypeOfQuestion;
    protected String strDifficultyOfQuestion;
    protected String strQuestion;
    protected String strCorrectAnswer;
    protected ArrayList<String> randomAnswers;
//    protected String strStateOfQuestion;
//    protected String strAnswerOfPlayer;

    public TriviaRandomQuestions(String ty,String di,String sq, String sc,ArrayList<String> ras){
        this.strTypeOfQuestion = ty;
        this.strDifficultyOfQuestion = di;
        this.strQuestion = sq;
        this.strCorrectAnswer = sc;
        this.randomAnswers = ras;
    }
    public String getStrTypeOfQuestion() {
        return strTypeOfQuestion;
    }

    public String getStrDifficultyOfQuestion() {
        return strDifficultyOfQuestion;
    }

    public String getStrQuestion() {
        return strQuestion;
    }

    public String getStrCorrectAnswer() {
        return strCorrectAnswer;
    }


    public ArrayList<String> getRamdomAnswers() {
        return randomAnswers;
    }

//    public String getStrStateOfQuestion() {
//        return strStateOfQuestion;
//    }
//
//    public String getStrAnswerOfPlayer() {
//        return strAnswerOfPlayer;
//    }

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


    public void setRamdomAnswers(ArrayList<String> ramdomAnswers) {
        this.randomAnswers = ramdomAnswers;
    }

//    public void setStrStateOfQuestion(String strStateOfQuestion) {
//        this.strStateOfQuestion = strStateOfQuestion;
//    }
//
//    public void setStrAnswerOfPlayer(String strAnswerOfPlayer) {
//        this.strAnswerOfPlayer = strAnswerOfPlayer;
//    }
}
