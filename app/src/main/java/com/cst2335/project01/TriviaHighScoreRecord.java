package com.cst2335.project01;

public class TriviaHighScoreRecord {

    //Android Studio hint: to create getter and setter, put mouse on variable and
    // click "alt+insert" in Windows, "control+return" on Macintosh
    protected String name, difficulty;
    protected double score;
    protected long id;

    /**Constructor:*/
    public TriviaHighScoreRecord(String n, double s,String d, long i)
    {
        name =n;
        score = s;
        difficulty = d;
        id = i;
    }

//    public void update(String n, String e)
//    {
//        name = n;
//        email = e;
//    }

    /**Chaining constructor: */
    public TriviaHighScoreRecord(String n, double s,String d) { this(n, s, d, 0);}

    public String getName() {
        return name;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public double getScore() {
        return score;
    }

    public long getId() {
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public void setScore(double score) {
        this.score = score;
    }
}
