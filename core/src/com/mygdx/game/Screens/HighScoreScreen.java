package com.mygdx.game.Screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.ForestChopper;
import com.mygdx.game.Helpers.CompareHighScores;
import com.mygdx.game.Helpers.HighScore;
import com.mygdx.game.Helpers.Prefferences;

import java.util.Collections;
import java.util.List;

import static com.mygdx.game.Helpers.Prefferences.getPreffsHighScore;
import static com.mygdx.game.Screens.PlayScreen.TEXTURE_HIGHSCOREBACKGROUND;

public class HighScoreScreen implements Screen,  Input.TextInputListener{

    private Viewport viewport;
    private Stage stage;
    private ForestChopper game;

    Label firstScore;
    Label firstScorepoints;

    private Texture background;
    private List<HighScore> highScores;


    public HighScoreScreen(ForestChopper screen, int score,float time){
        this(screen);
        //score = (int)(score*time);
        Gdx.input.getTextInput(this,"Score "+score+". Enter Name:", "","name. ex Kalle");
        newHScore = new HighScore(score,(int)time);
        hasTyped = false;

    }
    HighScore newHScore;



    public HighScoreScreen(ForestChopper game) {
        background = game.getManager().get(TEXTURE_HIGHSCOREBACKGROUND);
        hasTyped = true;
        highScores = getPreffsHighScore();

        this.game = game;
        viewport = new FitViewport(ForestChopper.V_WIDTH,ForestChopper.V_HEIGHT,new OrthographicCamera());
        stage = new Stage(viewport,((ForestChopper) game).batch);
        Label.LabelStyle font = new Label.LabelStyle(new BitmapFont(), Color.WHITE);

        Table table = doTable();
        Prefferences.savePreffs(highScores);

        stage.addActor(table);

    }
    Label.LabelStyle font = new Label.LabelStyle(new BitmapFont(), Color.WHITE);

    private Table doTable() {

        Table table = new Table();
        table.setFillParent(true);
        Label highscoreLabel = new Label("HIGHSCORE", font);
        Label scoreTitleLabel = new Label("Score:", font);
        Label nameLabel = new Label("Name:", font);

        // adding to table in right order.
        table.add().expandX();
        table.add(highscoreLabel).expandX();
        table.add().expandX();
        table.row();
        table.add(nameLabel).expandX();
        table.add().expandX();
        table.add(scoreTitleLabel).expandX();
        table.row();

        for (int i = 0; i < 3; i++) {
            firstScore = new Label(highScores.get(i).getName(), font);
            firstScorepoints= new Label(""+highScores.get(i).getScore(), font);
            table.add(firstScore).expandX();
            table.add().expandX();
            table.add(firstScorepoints).expandX();
            table.row();
        }
        return table;
    }


    @Override
    public void show() {

    }

    boolean hasTyped;

    @Override
    public void render(float delta) {

        if (Gdx.input.justTouched() && hasTyped){
            game.setScreen(new NewGameScreen((ForestChopper) game));
            dispose();
        }
        Gdx.gl.glClearColor(0,0,0,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.getBatch().begin();
        stage.getBatch().draw(background,0,0,ForestChopper.V_WIDTH,ForestChopper.V_HEIGHT);
        stage.getBatch().end();

        stage.draw();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        stage.dispose();
    }

    private String name;

    @Override
    public void input(String text) {
        newHScore.setName(text);

        highScores.add(newHScore);

        Collections.sort(highScores,new CompareHighScores());

        Prefferences.savePreffs(highScores);

        stage.clear();
        Table table = doTable();
        stage.addActor(table);
        hasTyped = true;
    }

    @Override
    public void canceled() {

    }
}
