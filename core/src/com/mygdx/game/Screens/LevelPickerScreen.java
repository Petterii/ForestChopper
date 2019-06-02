package com.mygdx.game.Screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Button.ButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.ForestChopper;
import com.mygdx.game.Helpers.PagedScrollPane;
import com.mygdx.game.Screens.PlayScreen;

import static com.mygdx.game.Screens.PlayScreen.TEXTURE_LEVELPICKERBACKGROUND;


public class LevelPickerScreen implements Screen {

    private Skin skin;
    private Stage stage;
    private Table container;
    private ForestChopper game;
    private OrthographicCamera cam;
    private Viewport viewport;

    private Texture background;

    public LevelPickerScreen(ForestChopper game){
        this.game = game;
        create();
    }

    public void create () {

        background = game.getManager().get(TEXTURE_LEVELPICKERBACKGROUND);
       // TextureRegion region = new TextureRegion(background, 0, 0, 2048, 1238);




        cam = new OrthographicCamera(ForestChopper.V_WIDTH,ForestChopper.V_HEIGHT);
        viewport = new FitViewport(ForestChopper.V_WIDTH*2,ForestChopper.V_HEIGHT*2, cam);

        stage = new Stage(viewport);
        skin = new Skin(Gdx.files.internal("data/uiskin.json"));
        skin.add("top", skin.newDrawable("default-round", Color.RED), Drawable.class);
        skin.add("star-filled", skin.newDrawable("white", Color.YELLOW), Drawable.class);
        skin.add("star-unfilled", skin.newDrawable("white", Color.GRAY), Drawable.class);

        Gdx.input.setInputProcessor(stage);

        container = new Table();
        stage.addActor(container);
        container.setFillParent(true);

        PagedScrollPane scroll = new PagedScrollPane();
        scroll.setFlingTime(0.1f);
        scroll.setPageSpacing(20);
        int c = 1;
        for (int l = 0; l < 10; l++) {
            Table levels = new Table().pad(50);
            levels.defaults().pad(20, 40, 20, 40);
            for (int y = 0; y < 3; y++) {
                levels.row();
                for (int x = 0; x < 4; x++) {
                    levels.add(getLevelButton(c++)).expand().fill();
                }
            }
            scroll.addPage(levels);
        }
        container.add(scroll).expand().fill();
    }

    public void render () {

        //Table.drawDebug(stage);
    }


    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(Gdx.graphics.getDeltaTime());
        stage.getBatch().begin();
        stage.getBatch().draw(background,0,0,Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
        stage.getBatch().end();

        stage.draw();
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

    public void dispose () {
        stage.dispose();
        skin.dispose();
    }

    public boolean needsGL20 () {
        return false;
    }


    /**
     * Creates a button to represent the level
     *
     * @param level
     * @return The button to use for the level
     */
    public Button getLevelButton(int level) {
        Button button = new Button(skin);
        ButtonStyle style = button.getStyle();
        style.up = 	style.down = null;

        // Create the label to show the level number
        Label label = new Label(Integer.toString(level), skin);
        label.setFontScale(2f);
        label.setAlignment(Align.center);

        // Stack the image and the label at the top of our button
        button.stack(new Image(skin.getDrawable("top")), label).expand().fill();

        // Randomize the number of stars earned for demonstration purposes
      //  int stars = MathUtils.random(-1, +3);
       int stars = 0;
        Table starTable = new Table();
        starTable.defaults().pad(5);
        if (stars >= 0) {
            for (int star = 0; star < 3; star++) {
                if (stars > star) {
                    starTable.add(new Image(skin.getDrawable("star-filled"))).width(20).height(20);
                } else {
                    starTable.add(new Image(skin.getDrawable("star-unfilled"))).width(20).height(20);
                }
            }
        }

        button.row();
        button.add(starTable).height(30);

        button.setName("Level" + Integer.toString(level));
        button.addListener(levelClickListener);
        return button;
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }
    /**
     * Handle the click - in real life, we'd go to the level
     */
    public ClickListener levelClickListener = new ClickListener() {
        @Override
        public void clicked (InputEvent event, float x, float y) {
            System.out.println("Click: " + event.getListenerActor().getName());
            if (event.getListenerActor().getName().equals("Level1"))
                game.setScreen(new PlayScreen((ForestChopper) game,"tile/smaller_map_60.tmx"));
            else
                game.setScreen(new PlayScreen((ForestChopper) game,"tile/level2.tmx"));
            dispose();
            // TODO start picked level
        }
    };

}