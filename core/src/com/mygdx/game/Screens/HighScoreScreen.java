package com.mygdx.game.Screens;

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
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.ForestChopper;
import com.mygdx.game.Helpers.HighScore;

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

    private Array<HighScore> highScores;

    private AssetManager manager;
    public static final String bg = "pictures/environment_forestbackground_scaled.png";

    public HighScoreScreen(Game game, int score){
        this(game);
        myScore1 = score;
        firstScorepoints.setText(""+score);
        Gdx.input.getTextInput(this,"Score "+score+". Enter Name:", "","name. ex Kalle");

    }


    public HighScoreScreen(Game game) {
        //Texture bground = new Texture(bg);
        this.game = game;
        viewport = new FitViewport(ForestChopper.V_WIDTH,ForestChopper.V_HEIGHT,new OrthographicCamera());
        stage = new Stage(viewport,((ForestChopper) game).batch);
        Label.LabelStyle font = new Label.LabelStyle(new BitmapFont(), Color.WHITE);

        Table table = new Table();
        Table tablescore = new Table();
        //table.center();
        table.setFillParent(true);

        Label highscoreLabel = new Label("HIGHSCORE", font);
        Label highscoreLabelLine = new Label("-----------------------------------------------------------------", font);

        Label scoreTitleLabel = new Label("Score:", font);
        Label nameLabel = new Label("Name:", font);

        int myScore1 = 100;
        int myScore2 = 50;

        emptyLabel = new Label("",font);
        firstScore = new Label("abc", font);
        secondScore= new Label("edf", font);
        firstScorepoints= new Label("" + myScore1, font);
        secondScorepoints= new Label("" + myScore2, font);


        table.add().expandX();
        table.add(highscoreLabel).expandX();
        table.add().expandX();
        table.row();
        table.add(nameLabel).expandX();
        table.add().expandX();
        table.add(scoreTitleLabel).expandX();
        table.row();
        table.add(firstScore).expandX();
        table.add().expandX();
        table.add(firstScorepoints).expandX();
        table.row();
        table.add(secondScore).expandX();
        table.add().expandX();
        table.add(secondScorepoints).expandX();


        stage.addActor(table);


       // stage.addActor(tablescore);
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
        this.name = text;
    }

    @Override
    public void canceled() {

    }
}
