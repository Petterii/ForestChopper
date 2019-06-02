package com.mygdx.game.Screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.viewport.StretchViewport;

public class LeaderboardScreen implements Screen {
    ScrollPane scrollPane;
    List<String> list;
    Skin skin;

    float gameWidth, gameHeight;
    TextureAtlas atlas;
    private Stage stage;
    private Game game;


    public LeaderboardScreen(Game game) {
        this.game = game;
        show();
    }

    @Override
    public void show() {
        gameWidth = Gdx.graphics.getWidth();
        gameHeight = Gdx.graphics.getHeight();
        //atlas = new TextureAtlas(Gdx.files.internal("ui/spritesheet.atlas"));
        skin = new Skin(Gdx.files.internal("json/list_skin.json"));


        list = new List<String>(skin);
        String[] strings = new String[20];
        for (int i = 0, k = 0; i < 20; i++) {
            strings[k++] = "String: " + i;

        }
        list.setItems(strings);
        scrollPane = new ScrollPane(list);
        scrollPane.setBounds(0, 0, gameWidth, gameHeight + 100);
        scrollPane.setSmoothScrolling(false);
        scrollPane.setPosition(gameWidth / 2 - scrollPane.getWidth() / 4,
                gameHeight / 2 - scrollPane.getHeight() / 4);
        scrollPane.setTransform(true);
        scrollPane.setScale(0.5f);
        stage = new Stage(new StretchViewport(gameWidth, gameHeight));
        stage.addActor(scrollPane);
        Gdx.input.setInputProcessor(stage);

    }

    float backgroundX = 0;

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act(delta);
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
        skin.dispose();
        atlas.dispose();
    }
}
