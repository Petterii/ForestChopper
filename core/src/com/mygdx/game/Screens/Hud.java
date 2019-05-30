package com.mygdx.game.Screens;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.ForestChopper;

import static com.mygdx.game.ForestChopper.PPM;

public class Hud implements Disposable {
    public Stage stage;
    private Viewport viewPort;
    private Integer worldTimer;
    private float timeCount;
    private static Integer score;
    private static Integer health;

    Label healthTitle;
    static Label healthLabel;
    Label countdownLabel;
    static Label scoreLabel;
    Label timeLabel;
    Label playerLabel;

    public Hud(SpriteBatch sb){
        worldTimer = 300;
        timeCount = 0;
        score = 0;
        health = 100;
        viewPort = new FitViewport(ForestChopper.V_WIDTH,ForestChopper.V_HEIGHT, new OrthographicCamera());
        stage = new Stage(viewPort, sb);

        Table table = new Table();
        table.top();
        table.setFillParent(true);

        healthLabel = new Label(String.format("%03d", health), new Label.LabelStyle(new BitmapFont(), Color.RED));
        healthLabel.setFontScale(0.8f);

        countdownLabel = new Label(String.format("%03d", worldTimer), new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        countdownLabel.setFontScale(0.8f);
        scoreLabel = new Label(String.format("%06d", score), new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        scoreLabel.setFontScale(0.8f);
        timeLabel = new Label("TIME", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        timeLabel.setFontScale(.8f);
        playerLabel = new Label("SCORE", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        playerLabel.setFontScale(0.8f);
        healthTitle= new Label("HP", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        healthTitle.setFontScale(0.8f);

        table.add(playerLabel).expandX().padTop(2);
        table.add(healthTitle).expandX().padTop(2);
        table.add(timeLabel).expandX().padTop(2);
        table.row();
        table.add(scoreLabel).expandX();
        table.add(healthLabel).expandX();
        table.add(countdownLabel).expandX();

        stage.addActor(table);

    }

    public static void reduceHealth(int dmgAmount) {

        health -= dmgAmount;
        if (health < 0)
            health = 0;
        healthLabel.setText(String.format("%03d", health));
    }

    public static int getScore() {
        return score;
    }

    public void update(float dt) {
        timeCount += dt;
        if (timeCount >= 1){
            worldTimer--;
            countdownLabel.setText(String.format("%03d", worldTimer));
            timeCount = 0;
        }
    }

    public static void addScore(int value){
        score += value;
        scoreLabel.setText(String.format("%06d", score));
    }

    public static Integer getHealth() {
        return health;
    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}
