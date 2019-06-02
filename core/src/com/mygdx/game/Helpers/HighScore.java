package com.mygdx.game.Helpers;

public class HighScore {

    private int score;
    private String name;
    private float time;

    public HighScore(int score, int time) {
        this.score = score;
        this.time = time;
    }

    public int getScore() {
        return score;
    }



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getTime() {
        return time;
    }

}
