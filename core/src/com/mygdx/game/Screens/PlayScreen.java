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

import com.mygdx.game.Items.Items;
import com.mygdx.game.Parallax.ParallaxBackground;

import com.mygdx.game.Units.Enemy;
import com.mygdx.game.Units.Player;

import static com.mygdx.game.ForestChopper.PPM;
import static com.mygdx.game.ForestChopper.V_HEIGHT;
import static com.mygdx.game.ForestChopper.V_WIDTH;


public class PlayScreen implements Screen{

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
    public static final String TEXTURE_HIGHSCOREBACKGROUND = "pictures/highscorebackground400x208.jpg";
    public static final String TEXTURE_LEVELPICKERBACKGROUND = "pictures/levelpicker_background400x200.jpg";
    public static final String TEXTURE_MAINMENU = "pictures/mainmenu_screen.png";
    public static final String TEXTURE_LEVEL3 = "tile/cleanforest_half.png";
    public static final String TEXTURE_ENEMYPLANTSPRITESHEET = "chars/enemyplant_spritesheet.png";
    public static final String TEXTURE_ENEMYPLANTHURT = "chars/plant_hurt_96x128.png";
    public static final String TEXTURE_ENEMYPLANTDIEINGSPRITESHEET = "chars/plant_dieing_spritesheet.png";




    public static final String UI_BUTTONS = "ui/leftandright.png";
    public static final String UI_ATTACKICON = "ui/Icon_attack.png";

    public static final String TILE_SMALLMAP = "tile/smaller_map_60.tmx";


    private ForestChopper game;
    private Viewport gamePort;

    private World world;
    private Box2DDebugRenderer b2dr;
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
    private Sprite attackIcon;
    private Hud topHud;

    // store our enemies,items and assets files
    private Array<Enemy> enemies;
    private Array<Items> items;
    private AssetManager manager;
    private ParallaxBackground parallaxBackground;

    // some chatbubble handling variables
    private static int leftDialog;
    private boolean leftChatisActive;
    private boolean noChat;

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





    public PlayScreen(ForestChopper game, String level) {
        enemies = new Array<Enemy>();
        items = new Array<Items>();
        initParallax();
        manager = game.getManager();
        leftDialog = 0;
        leftChatisActive = true;

        topHud = new Hud(game.batch);
        hudBatch = new SpriteBatch();

        this.game = game;

        hudCam = new OrthographicCamera();
        gamecam = new OrthographicCamera();
        gamePort = new FitViewport(V_WIDTH/ PPM,V_HEIGHT/ PPM,gamecam);
        gamePort.apply();

        mapLoad = new TmxMapLoader();
        map = mapLoad.load(level);
        tileRenderer = new OrthogonalTiledMapRenderer(map,1/PPM);

        gamecam.position.set(gamePort.getWorldWidth()/2, gamePort.getWorldHeight()/2,0);
        world = new World(new Vector2(0,-10), true);
        b2dr = new Box2DDebugRenderer();


        new CreateWorldFromTiled(this);
        player = new Player(this, new Texture(TEXTURE_MINITOUR4STANCES), new Texture(TEXTURE_DEADPLAYER));

        initButtons();

        world.setContactListener(new WorldContactListener());

        rightChat = new ChatBubble();
        leftChat = new ChatBubble();
        //rightChat.show("Puny Creature",4f);
        leftChat.show("Hello Sir", 2f);
        NextDialog();
    }

    private ChatBubble leftChat;
    private ChatBubble rightChat;


    @Override
    public void show() {

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
        //b2dr.render(world,gamecam.combined);

        game.batch.setProjectionMatrix(gamecam.combined);
        game.batch.begin();
        player.draw(game.batch);
        for (Enemy enemy : enemies) {
            if (!enemy.isToBedeleted()) enemy.draw(game.batch);
        }
        for (Items item : items) {
            if (!item.destroyed) item.draw(game.batch);
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

        drawChat();
        handleDialogs(); // new potential dialog

        // if player failed and goes to a gameover/newgame screen
        if (GameOver()){
            game.setScreen(new HighScoreScreen(game, Hud.getScore(),Hud.getTime()));
            dispose();
        }
    }

    private void drawChat() {
        if (leftChatisActive && !noChat)
            leftChat.draw(game.batch,8 , ForestChopper.V_HEIGHT/2);
        else if (!noChat)
            rightChat.draw(game.batch,(ForestChopper.V_WIDTH/3)*2, ForestChopper.V_HEIGHT/2);
    }

    private void handleDialogs() {
        if (!leftChat.isVisible() && !rightChat.isVisible()) {
            switch (leftDialog){
                case 1: leftDialog =2;
                        leftChat.show("Chop That Big BOY Tree", 2f);
                        noChat = false;
                        break;
                case 2: leftDialog = 3;
                        rightChat.show("Puny Creature.",2f);
                        leftChatisActive = false;
                        noChat = false;
                        break;
                case 3: leftDialog = -1;
                        rightChat.show("ORKS KILLLLLL!!",2f);
                        leftChatisActive = false;
                        noChat = false;
                        break;
                case 4: leftDialog = -1;
                        leftChat.show("Well Done",2f);
                        leftChatisActive = true;
                        noChat = false;
                        break;

                default: noChat = true;

            }
        }


    }



    public static void NextDialog() {
        leftDialog++;
    }

    private boolean GameOver(){
        if (player.currentState == Player.State.WINNING && player.getStateTimer() > 2)
            return true;
        if (Hud.getHealth() <= 0 && player.getStateTimer() > 3)
            return true;
        return false;
    }

    private boolean paused;

    private void update(float delta) {
        if (!paused)
         world.step(1/60f,6,2);
        else
            world.step(0, 6,2);
        parallaxBackground.setSpeed(player.b2body.getLinearVelocity().x*-1);
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


        // camera follows player until player dies
        if (player.getState() != Player.State.DIEING) {
            gamecam.position.x = player.b2body.getPosition().x;
        }
        gamecam.update();
        tileRenderer.setView(gamecam);
    }

    private void handleInputs() {
        if (player.getState() != Player.State.DIEING) {
            handleKeyboardInputs();
            handleTouchInputs();
        }
    }

    private void handleTouchInputs() {

        // for loop because it can have multiple touches
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

                }else if (attackIcon.getBoundingRectangle().contains(xKord,yKord) && player.getState() != Player.State.ATTACKING){
                 //   paused = !paused;
                    player.attack();
                }
                else if (!leftButton.getBoundingRectangle().contains( xKord , yKord ) && !rightButton.getBoundingRectangle().contains(xKord, yKord) && player.getState() != Player.State.ATTACKING ){
                    if (player.b2body.getLinearVelocity().y == 0){
                        Sound s = manager.get(SOUND_JUMP);
                        s.play();
                        player.b2body.applyLinearImpulse(new Vector2(0,4f),player.b2body.getWorldCenter(),true);
                    }
                }


            }
        }

    }

    private void handleKeyboardInputs() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.UP)){
            Sound s = manager.get(SOUND_JUMP);
            s.play();
            player.b2body.applyLinearImpulse(new Vector2(0,4f),player.b2body.getWorldCenter(),true);
        }

        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) && player.b2body.getLinearVelocity().x <= 2){

            player.b2body.applyLinearImpulse(new Vector2(0.1f,0f),player.b2body.getWorldCenter(),true);

        }
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT) && player.b2body.getLinearVelocity().x >= -2){
            player.b2body.applyLinearImpulse(new Vector2(-0.1f,0f),player.b2body.getWorldCenter(),true);

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

    public Array<Items> getItems() {
        return items;
    }

    public void setDialog(int i) {
        leftDialog = i;
    }
}
