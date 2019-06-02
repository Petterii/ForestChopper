package com.mygdx.game.Screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;

public class GameOverScreen implements Screen, Input.TextInputListener {

    private String text;

    public GameOverScreen(Game game) {

       // Gdx.input.getTextInput(this,"Your Name:", "","your name");
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0,0,0,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

     //   Gdx.app.log("Text:",text);
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

    }



    @Override
    public void input(String text) {
        this.text = text;
    }

    @Override
    public void canceled() {
        text = "can";
    }
}
