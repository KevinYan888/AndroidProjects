package com.cst2335.project01;

import java.util.ArrayList;

//each question create an object of TriviaQuestion
public class TriviaQuestionItemsClass {
    protected  int id;
    protected String strTypeOfQuestion;
    protected String strDifficultyOfQuestion;
    protected String strQuestion;
    protected String strCorrectAnswer;
    protected ArrayList<String> randomAnswers;
    protected String strStateOfQuestion ="unanswered" ;//unanswered; ture; false
    protected String strAnswerOfPlayer;
    protected String strNameOfPlayer = "Unknown";

    public String getStrNameOfPlayer() {
        return strNameOfPlayer;
    }

    public void setStrNameOfPlayer(String strNameOfPlayer) {
        this.strNameOfPlayer = strNameOfPlayer;
    }

    public TriviaQuestionItemsClass(int id, String ty, String di, String sq, String sc, ArrayList<String> ras){
        this.id = id;
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

    public int getId() {
        return id;
    }

    public ArrayList<String> getRandomAnswers() {
        return randomAnswers;
    }
    public String getIntStateOfQuestion() {
        return strStateOfQuestion;
    }

    public String getStrAnswerOfPlayer() {
        return strAnswerOfPlayer;
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


    public void setRamdomAnswers(ArrayList<String> ramdomAnswers) {
        this.randomAnswers = ramdomAnswers;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setRandomAnswers(ArrayList<String> randomAnswers) {
        this.randomAnswers = randomAnswers;
    }
    public void setStrStateOfQuestion(String strStateOfQuestion) {
        this.strStateOfQuestion = strStateOfQuestion;
    }

    public void setStrAnswerOfPlayer(String strAnswerOfPlayer) {
        this.strAnswerOfPlayer = strAnswerOfPlayer;
    }
}
