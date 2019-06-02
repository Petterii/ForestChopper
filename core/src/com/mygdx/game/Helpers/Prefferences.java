package com.mygdx.game.Helpers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class Prefferences {

    public static void savePreffs(List<HighScore> highScores){
        Preferences prefs = Gdx.app.getPreferences("PREFFS_HIGHSCORES");
        for (int i = 0; i < highScores.size(); i++) {
            prefs.putString("name"+i, highScores.get(i).getName());
            prefs.putInteger("score"+i, highScores.get(i).getScore());
            prefs.putFloat("time"+i, highScores.get(i).getTime());
        }
    }

    public static List<HighScore> getPreffsHighScore(){
        List<HighScore> highScores = new ArrayList<HighScore>();

        Preferences prefs = Gdx.app.getPreferences("PREFFS_HIGHSCORES");
        for (int i = 0; i < 3; i++) {
            HighScore hs = new HighScore(prefs.getInteger("score"+i),
                                        (int)prefs.getFloat("time"+i));
            hs.setName(prefs.getString("name"+i));

            highScores.add(hs);
        }
        if (highScores.size() == 0)
            highScores = dummyData();
        return highScores;
    }

    private static List<HighScore> dummyData(){
        List<HighScore> highScores = new ArrayList<HighScore>();
        HighScore n = new HighScore(999,100);
        n.setName("Fil");
        highScores.add(n);
        n = new HighScore(99,140);
        n.setName("Dril");
        highScores.add(n);
        n = new HighScore(4999,200);
        n.setName("Pete");
        highScores.add(n);

        Collections.sort(highScores, new CompareHighScores());

        return highScores;
    }
}
