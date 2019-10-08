package com.mygdx.game.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net;
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
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
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
    private Label playButton;
    private Label weatherLabel;

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
        playButton = new Label("Click to Play", font);
        weatherLabel = new Label("Getting Weather Today ", font);

        table.add(gameOverLabel).expandX().padTop(40f);
        table.row();
        table.add(playButton).expandX().padTop(10f);
        table.row();
        table.add(weatherLabel).expandX().padTop(30f);

        stage.addActor(table);

        httpRequestTest();

    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {


        Gdx.gl.glClearColor(0,0,0,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.getBatch().begin();
        stage.getBatch().draw(bground,0,0,ForestChopper.V_WIDTH,ForestChopper.V_HEIGHT);
        stage.getBatch().end();

        stage.draw();

        handleInput();
    }

    private void handleInput() {
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

                //game.setScreen(new TestScreen((ForestChopper) game));
                game.setScreen(new PlayScreen((ForestChopper) game,"tile/smaller_map_60.tmx"));
                //game.setScreen(new LevelPickerScreen((ForestChopper) game));
                dispose();
            }
        }
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

    // get json data
    private void rjsonread(String jsondata){
        JsonReader json = new JsonReader();
        JsonValue base = json.parse(jsondata);

        //array objects in json if you would have more components
        for (JsonValue component1 : base.get("weather"))
        {
            changeWeatherText(component1.getString("description"));
        }
    }

    private void changeWeatherText(String text){
        weatherLabel.setText("Weather in London : "+text);
    }

    // GET weather data from URL
    private void httpRequestTest(){
        Gdx.app.log("startShit:","god dammit");
        Net.HttpRequest request = new Net.HttpRequest(Net.HttpMethods.GET);
        request.setUrl("http://api.openweathermap.org/data/2.5/weather?q=London&APPID=0b097aa7c90c1fe75898c100483bfa96");
        request.setHeader("Content-Type", "application/json");
        request.setHeader("Accept", "application/json");

        Gdx.net.sendHttpRequest(request, new Net.HttpResponseListener() {
            @Override
            public void handleHttpResponse (Net.HttpResponse httpResponse) {
                String testFetch = httpResponse.getResultAsString();
                rjsonread(testFetch);
            }

            @Override
            public void failed (Throwable t) {
                Gdx.app.error("HttpRequestExample", "something went wrong", t);
                changeWeatherText("WEATHER FETCH FAILED :(");
            }

            @Override
            public void cancelled () {
                Gdx.app.log("HttpRequestExample", "cancelled");
            }
        });

    }

}
