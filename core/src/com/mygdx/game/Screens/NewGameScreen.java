package com.mygdx.game.Screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
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



import static com.mygdx.game.ForestChopper.V_HEIGHT;
import static com.mygdx.game.ForestChopper.V_WIDTH;
import static com.mygdx.game.Screens.PlayScreen.TEXTURE_MAINMENU;

public class NewGameScreen implements Screen {

    private Viewport viewport;
    private Stage stage;
    private ForestChopper game;


    private AssetManager manager;
    public static final String bg = "pictures/environment_forestbackground_scaled.png";
    Texture bground;
    public NewGameScreen(ForestChopper game) {
        bground = game.getManager().get(TEXTURE_MAINMENU);
        this.game = game;
        viewport = new FitViewport(ForestChopper.V_WIDTH,ForestChopper.V_HEIGHT,new OrthographicCamera());
        stage = new Stage(viewport,((ForestChopper) game).batch);
        Label.LabelStyle font = new Label.LabelStyle(new BitmapFont(), Color.WHITE);

        Table table = new Table();
        table.center();
        table.setFillParent(true);

        Label gameOverLabel = new Label("FOREST CHOPPER", font);
        Label playButton = new Label("Click to Play", font);

        table.add(gameOverLabel).expandX();
        table.row();
        table.add(playButton).expandX().padTop(10f);

        stage.addActor(table);
    }


    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        if (Gdx.input.justTouched()) {

            float dingosX = Gdx.input.getX();
            float dingosY = Gdx.input.getY();
            // just debugging stuff

            float xKord = ((float) V_WIDTH / Gdx.graphics.getWidth()) * dingosX;
            float yKord = ((float) V_HEIGHT / Gdx.graphics.getHeight()) * (Gdx.graphics.getHeight() - dingosY);


            if (ForestChopper.V_WIDTH / 2 < xKord && ForestChopper.V_HEIGHT / 2 < yKord) {
                game.setScreen(new HighScoreScreen((ForestChopper) game));
                dispose();
            }
            else {
                game.setScreen(new LevelPickerScreen((ForestChopper) game));
                dispose();
            }
        }

        Gdx.gl.glClearColor(0,0,0,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.getBatch().begin();
        stage.getBatch().draw(bground,0,0,ForestChopper.V_WIDTH,ForestChopper.V_HEIGHT);
        stage.getBatch().end();

        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width,height,false);
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



}
