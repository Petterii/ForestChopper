package com.mygdx.pettsmaxgame.Helpers;

import java.util.Comparator;

public class CompareHighScores implements Comparator<HighScore> {
    @Override
    public int compare(HighScore highScore, HighScore t1) {

        return t1.getScore() - highScore.getScore();
    }
}
