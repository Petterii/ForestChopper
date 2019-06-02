package com.mygdx.pettsmaxgame.Screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.pettsmaxgame.ForestChopper;
import com.mygdx.pettsmaxgame.Helpers.CompareHighScores;
import com.mygdx.pettsmaxgame.Helpers.HighScore;
import com.mygdx.pettsmaxgame.Helpers.Prefferences;

import java.util.Collections;
import java.util.List;

import static com.mygdx.pettsmaxgame.Helpers.Prefferences.getPreffsHighScore;

public class HighScoreScreen implements Screen,  Input.TextInputListener{

    private Viewport viewport;
    private Stage stage;
    private Game game;

    private int newScore;

    int myScore1 = 100;
    int myScore2 = 50;

    Label emptyLabel;
    Label firstScore;
    Label secondScore;
    Label firstScorepoints;
    Label secondScorepoints;

    private List<HighScore> highScores;

    private AssetManager manager;
    public static final String bg = "pictures/environment_forestbackground_scaled.png";

    public HighScoreScreen(Game game, int score,float time){
        this(game);
        Gdx.input.getTextInput(this,"Score "+score+". Enter Name:", "","name. ex Kalle");
        newHScore = new HighScore(score,(int)time);

    }
    HighScore newHScore;



    public HighScoreScreen(Game game) {
        //Texture bground = new Texture(bg);

        highScores = getPreffsHighScore();

        this.game = game;
        viewport = new FitViewport(ForestChopper.V_WIDTH,ForestChopper.V_HEIGHT,new OrthographicCamera());
        stage = new Stage(viewport,((ForestChopper) game).batch);
        Label.LabelStyle font = new Label.LabelStyle(new BitmapFont(), Color.WHITE);

        Table table = doTable();
        Prefferences.savePreffs(highScores);
        /*
        table.add(firstScore).expandX();
        table.add().expandX();
        table.add(firstScorepoints).expandX();
        table.row();
        table.add(secondScore).expandX();
        table.add().expandX();
        table.add(secondScorepoints).expandX();
*/

        stage.addActor(table);


       // stage.addActor(tablescore);
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

    @Override
    public void render(float delta) {
        if (Gdx.input.justTouched()){
            game.setScreen(new NewGameScreen((ForestChopper) game));
            dispose();
        }
        Gdx.gl.glClearColor(0,0,0,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);



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
    }

    @Override
    public void canceled() {

    }
}
