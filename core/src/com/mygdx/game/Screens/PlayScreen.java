package com.mygdx.game.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.ContactListener.WorldContactListener;
import com.mygdx.game.CreateWorldFromTiled;
import com.mygdx.game.ForestChopper;
import com.mygdx.game.Items.Coin;
import com.mygdx.game.Items.Items;
import com.mygdx.game.Parallax.ParallaxBackground;
import com.mygdx.game.TiledHelpers.B2TiledWorldCreater;
import com.mygdx.game.Units.Enemies.Screecher;
import com.mygdx.game.Units.Enemy;
import com.mygdx.game.Units.Player;

import static com.mygdx.game.ForestChopper.PPM;
import static com.mygdx.game.ForestChopper.V_HEIGHT;
import static com.mygdx.game.ForestChopper.V_WIDTH;


public class PlayScreen implements Screen {

    // assets manager files to load
    public static final String SOUND_COINAPEAR = "sound/coin_apear.mp3";
    public static final String SOUND_DAMAGE2 = "sound/damage2.ogg";
    public static final String SOUND_SWORDSWING = "sound/swordswing.wav";
    public static final String SOUND_JUMP = "sound/jump1.wav";
    public static final String SOUND_WALK = "sound/walk.wav";
    public static final String SOUND_PLAYERHURT = "sound/player_hurt.mp3";
    public static final String SOUND_PLAYERDIEING = "sound/player_dieing.wav";
    public static final String SOUND_COINPICKUPP = "sound/coin_apear.mp3";

    public static final String TEXTURE_COINSPRITESHEET = "chars/coinspritesheet.png";
    public static final String TEXTURE_DEADPLAYER = "chars/deadPlayer.png";
    public static final String TEXTURE_ORKHURT = "chars/hurt_ork_64x56.png";
    public static final String TEXTURE_PLAYERHURT = "chars/hurtPlayer.png";
    public static final String TEXTURE_MINITOUR4STANCES = "chars/minitour_four_stances.png";
    public static final String TEXTURE_ORKWALK = "chars/ork_walk.png";
    public static final String TEXTURE_ORKDIEINGSPRITESHEET = "chars/orkdieingSpritsheet.png";
    public static final String TEXTURE_ORKSPRITESHEET = "chars/orkspritesheet.png";
    public static final String TEXTURE_COINSHEET = "chars/coinspritesheet.png";

    public static final String UI_BUTTONS = "ui/leftandright.png";
    public static final String UI_ATTACKICON = "ui/Icon_attack.png";

    public static final String TILE_SMALLMAP = "tile/smaller_map_60.tmx";


    private ForestChopper game;
    private Viewport gamePort;

    private World world;
    private Box2DDebugRenderer b2dr;
    private B2TiledWorldCreater tiledCreate;

    private OrthographicCamera gamecam;

    private TmxMapLoader mapLoad;
    private TiledMap map;
    private OrthogonalTiledMapRenderer tileRenderer;

    private Player player;
    private SpriteBatch hudBatch;

    // Hud stuff
    private Sprite leftButton;
    private Sprite rightButton;

    private OrthographicCamera hudCam;
   // private Texture attackTextureIcon;
    private Sprite attackIcon;
    private Hud topHud;

    private Array<Enemy> enemies;
    private Array<Items> items;
    private boolean createCoin;

    private AssetManager manager;
    private ParallaxBackground parallaxBackground;

    private Stage stage;

    public ParallaxBackground getParallaxBackground() {
        return parallaxBackground;
    }

    private void initParallax(){
        stage = new Stage(new ScreenViewport());
        Array<Texture> textures = new Array<Texture>();
        for(int i = 1; i <=6;i++){
            textures.add(new Texture(Gdx.files.internal("parallax/img"+i+".png")));
            textures.get(textures.size-1).setWrap(Texture.TextureWrap.MirroredRepeat, Texture.TextureWrap.MirroredRepeat);
        }

        parallaxBackground = new ParallaxBackground(textures);
        parallaxBackground.setSize(Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
        parallaxBackground.setSpeed(0);
        stage.addActor(parallaxBackground);
    }





    public PlayScreen(ForestChopper game) {
        enemies = new Array<Enemy>();
        items = new Array<Items>();
        createCoin = false;
        initParallax();
        manager = game.getManager();

        topHud = new Hud(game.batch);
        hudBatch = new SpriteBatch();

        this.game = game;

        hudCam = new OrthographicCamera();
        gamecam = new OrthographicCamera();
        gamePort = new FitViewport(V_WIDTH/ PPM,V_HEIGHT/ PPM,gamecam);
        gamePort.apply();

        mapLoad = new TmxMapLoader();
        map = mapLoad.load(TILE_SMALLMAP);
        tileRenderer = new OrthogonalTiledMapRenderer(map,1/PPM);

        gamecam.position.set(gamePort.getWorldWidth()/2, gamePort.getWorldHeight()/2,0);
        world = new World(new Vector2(0,-10), true);
        b2dr = new Box2DDebugRenderer();

        tiledCreate = new B2TiledWorldCreater(this);
        new CreateWorldFromTiled(this);
        player = new Player(this, new Texture(TEXTURE_MINITOUR4STANCES), new Texture(TEXTURE_DEADPLAYER));

        initButtons();

        world.setContactListener(new WorldContactListener());


    }

    @Override
    public void show() {

    }

    public Hud getTopHud() {
        return topHud;
    }

    private void initButtons() {
        hudCam.setToOrtho(false, V_WIDTH,V_HEIGHT);

        attackIcon = new Sprite(((Texture)manager.get(UI_ATTACKICON)));

        leftButton = new Sprite(((Texture)manager.get(UI_BUTTONS)),0,0,((Texture)manager.get(UI_BUTTONS)).getWidth()/2,((Texture)manager.get(UI_BUTTONS)).getHeight());

        leftButton.setSize(leftButton.getWidth() ,leftButton.getHeight());
        attackIcon.setSize(attackIcon.getWidth(),attackIcon.getHeight());
        attackIcon.setPosition(V_WIDTH - attackIcon.getWidth()*2, attackIcon.getHeight());

        rightButton = new Sprite(((Texture)manager.get(UI_BUTTONS)),((Texture)manager.get(UI_BUTTONS)).getWidth()/2,0,((Texture)manager.get(UI_BUTTONS)).getWidth()/2,((Texture)manager.get(UI_BUTTONS)).getHeight());

        rightButton.setSize(rightButton.getWidth(),rightButton.getHeight() );
        rightButton.setPosition(rightButton.getWidth()*2,0);
    }

    @Override
    public void render(float delta) {


        if (player.getState() != Player.State.DIEING)
            if (!player.destroyed)
                handleInputs();

        Gdx.gl.glClearColor(0,0,0,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);


        // draw parallax background
        stage.act();
        stage.draw();

        update(delta);
        tileRenderer.render();
        b2dr.render(world,gamecam.combined);

        game.batch.setProjectionMatrix(gamecam.combined);
        game.batch.begin();
        player.draw(game.batch);
        for (Enemy enemy : enemies) {
            if (!enemy.isToBedeleted())
                enemy.draw(game.batch);
        }
        for (Items item : items) {
            if (!item.destroyed)
                item.draw(game.batch);
        }
       game.batch.end();


       // drawing the UI buttons
        hudBatch.setProjectionMatrix(hudCam.combined);
        hudBatch.begin();
        hudBatch.draw(leftButton,0,0);
        hudBatch.draw(rightButton,rightButton.getWidth()*2,0);
        hudBatch.draw(attackIcon,V_WIDTH - (attackIcon.getWidth()*2),attackIcon.getHeight());

        hudBatch.end();

        topHud.stage.draw();

        if (GameOver()){
            game.setScreen(new NewGameScreen(game));
            dispose();
        }
    }

    private boolean GameOver(){
        if (player.currentState == Player.State.DIEING && player.getStateTimer() > 3)
            return true;
        return false;
    }

    private void update(float delta) {
        world.step(1/60f,6,2);

        parallaxBackground.setSpeed(player.b2body.getLinearVelocity().x);
        for (Enemy enemy : enemies) {
            if (!enemy.isToBedeleted())
                enemy.update(delta);
        }
        for (Items item: items) {
            if (!item.destroyed)
                item.update(delta);
        }


        topHud.update(delta);
        player.update(delta);


        if (player.getState() != Player.State.DIEING) {
            gamecam.position.x = player.b2body.getPosition().x;
        }
        gamecam.update();
        tileRenderer.setView(gamecam);
    }

    public Array<Items> getItems() {
        return items;
    }

    private void handleInputs() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.UP)){
            player.b2body.applyLinearImpulse(new Vector2(0,4f),player.b2body.getWorldCenter(),true);
        }

        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) && player.b2body.getLinearVelocity().x <= 2){

            player.b2body.applyLinearImpulse(new Vector2(0.1f,0f),player.b2body.getWorldCenter(),true);
            player.destroySword();

        }
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT) && player.b2body.getLinearVelocity().x >= -2){
            player.b2body.applyLinearImpulse(new Vector2(-0.1f,0f),player.b2body.getWorldCenter(),true);

        }




        for (int i = 0; i < 20 ; i++) {
            if (Gdx.input.isTouched(i)){

                float dingosX = Gdx.input.getX(i);
                float dingosY = Gdx.input.getY(i);
                float rectbutton = leftButton.getBoundingRectangle().width; // just debugging stuff

                float xKord = ((float)V_WIDTH/Gdx.graphics.getWidth()) * dingosX;
                float yKord = ((float)V_HEIGHT/Gdx.graphics.getHeight()) * (Gdx.graphics.getHeight()-dingosY);

                if (leftButton.getBoundingRectangle().contains( xKord , yKord ) && player.b2body.getLinearVelocity().x >= -2){
                    player.b2body.applyLinearImpulse(new Vector2(-0.1f,0f),player.b2body.getWorldCenter(),true);
                }
                else if (rightButton.getBoundingRectangle().contains(xKord, yKord) && player.b2body.getLinearVelocity().x <= 2){
                    player.b2body.applyLinearImpulse(new Vector2(0.1f,0f),player.b2body.getWorldCenter(),true);

                }else if (attackIcon.getBoundingRectangle().contains(xKord,yKord)){
                    player.attack();
                }
                else if (!leftButton.getBoundingRectangle().contains( xKord , yKord ) && !rightButton.getBoundingRectangle().contains(xKord, yKord)){
                    if (player.b2body.getLinearVelocity().y == 0){
                        Sound s = manager.get(SOUND_JUMP);
                        s.play();
                        player.b2body.applyLinearImpulse(new Vector2(0,4f),player.b2body.getWorldCenter(),true);
                    }
                }


            }
        }




    }

    @Override
    public void resize(int width, int height) {
        gamePort.update(width,height,false);
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
        map.dispose();
        tileRenderer.dispose();
        world.dispose();
        b2dr.dispose();
        hudBatch.dispose();

        topHud.dispose();
        enemies.clear();
        items.clear();

       stage.dispose();


     }

    public TiledMap getMap() {
        return map;
    }

    public World getWorld(){
        return world;
    }


    public Player getPlayer(){
        return player;
    }

    public void addEnemy(Enemy enemy){
        enemies.add(enemy);
    }

    public Array<Enemy> getEnemies(){
        return enemies;
    }

    public AssetManager getManager() {
        return manager;
    }

}