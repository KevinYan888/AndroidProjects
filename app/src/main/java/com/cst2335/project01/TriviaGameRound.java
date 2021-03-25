package com.cst2335.project01;

import java.util.ArrayList;

public class TriviaGameRound {
    protected String nameOfPlayer = "unknown";
    protected  int intNumberOfQuestions = 5;
    protected String strTypeOfGame;
    protected String strDifficultyOfGame;
//    protected ArrayList<TriviaQuestion> arrayListTriviaQuestions;
    protected double dblScore;
//
//    public TriviaGameRound(){
//        new TriviaQuestion();
//
//        ArrayList<TriviaQuestion> arrayListTriviaQuestions = new ArrayList<TriviaQuestion>();
//    }


    public String getNameOfPlayer() {
        return nameOfPlayer;
    }

    public int getIntNumberOfQuestions() {
        return intNumberOfQuestions;
    }

    public String getStrTypeOfGame() {
        return strTypeOfGame;
    }

    public String getStrDifficultyOfGame() {
        return strDifficultyOfGame;
    }

//    public ArrayList<TriviaQuestion> getArrayListTriviaQuestions() {
//        return arrayListTriviaQuestions;
//    }

    public double getDblScore() {
        return dblScore;
    }

    public void setNameOfPlayer(String nameOfPlayer) {
        this.nameOfPlayer = nameOfPlayer;
    }

    public void setIntNumberOfQuestions(int intNumberOfQuestions) {
        this.intNumberOfQuestions = intNumberOfQuestions;
    }

    public void setStrTypeOfGame(String strTypeOfGame) {
        this.strTypeOfGame = strTypeOfGame;
    }

    public void setStrDifficultyOfGame(String strDifficultyOfGame) {
        this.strDifficultyOfGame = strDifficultyOfGame;
    }

//    public void setArrayListTriviaQuestions(ArrayList<TriviaQuestion> arrayListTriviaQuestions) {
//        this.arrayListTriviaQuestions = arrayListTriviaQuestions;
//    }

    public void setDblScore(double dblScore) {
        this.dblScore = dblScore;
    }
}
