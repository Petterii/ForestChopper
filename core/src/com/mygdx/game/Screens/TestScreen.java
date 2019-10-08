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
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJoint;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJointDef;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.ContactListener.WorldContactListener;
import com.mygdx.game.ForestChopper;
import com.mygdx.game.Helpers.B2dPolygon;
import com.mygdx.game.Helpers.CreateWorldFromTiled;
import com.mygdx.game.Helpers.CustomBody;
import com.mygdx.game.Items.Items;
import com.mygdx.game.Parallax.ParallaxBackground;
import com.mygdx.game.Units.Enemy;
import com.mygdx.game.Units.Player;

import static com.mygdx.game.ForestChopper.ENEMY_BIT;
import static com.mygdx.game.ForestChopper.PLAYERSWORD_BIT;
import static com.mygdx.game.ForestChopper.PPM;
import static com.mygdx.game.ForestChopper.V_HEIGHT;
import static com.mygdx.game.ForestChopper.V_WIDTH;

public class TestScreen implements Screen {
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





    public TestScreen(ForestChopper game) {
        manager = game.getManager(); // where textures and sounds are
        hudBatch = new SpriteBatch();
        this.game = game;

        hudCam = new OrthographicCamera();
        gamecam = new OrthographicCamera();
        gamePort = new FitViewport(V_WIDTH/ PPM,V_HEIGHT/ PPM,gamecam);
        gamePort.apply();
        world = new World(new Vector2(0,-10), true); // -10 is gravity
        b2dr = new Box2DDebugRenderer();


        // Tiled init. where level file is loaded.
        float hightX =10;
        float widthX = 120;
        swordBody = new B2dPolygon(world,220/PPM,120/PPM, CustomBody.BodyType.DYNAMICBODY,widthX,hightX,false);

        swordBody.Collision((short)ENEMY_BIT,(short)PLAYERSWORD_BIT);
        swordBody.Finalize(this);

        hightX = 5;
        widthX = 4;
        box1 = new B2dPolygon(world,150/PPM,(150-hightX)/PPM, CustomBody.BodyType.DYNAMICBODY,widthX,hightX,false);

        // swordBody.Collision((short)ENEMY_BIT,(short)PLAYERSWORD_BIT);
        box1.Finalize(this);
        hightX = 30;
        widthX = 10;
        swordancher = new B2dPolygon(world,220/PPM,(120-hightX)/PPM, CustomBody.BodyType.STATICBODY,widthX,hightX,false);

        // swordBody.Collision((short)ENEMY_BIT,(short)PLAYERSWORD_BIT);
        swordancher.Finalize(this);

        RevoluteJointDef rj = new RevoluteJointDef();

        rj.initialize(swordBody.getBody(), swordancher.getBody() , swordancher.getBody().getWorldCenter());

        rj.motorSpeed = 2f;
        rj.maxMotorTorque = 10000f;
        rj.enableMotor = true;

        RevoluteJoint rjj = (RevoluteJoint) world.createJoint(rj);

        gamecam.position.set(gamePort.getWorldWidth()/2, gamePort.getWorldHeight()/2,0);

    }

    private B2dPolygon swordBody;
    private B2dPolygon swordancher;
    private B2dPolygon box1;

    private ChatBubble leftChat;
    private ChatBubble rightChat;


    @Override
    public void show() {

    }



    private void update(float delta) {
        world.step(1/60f,6,2);

        gamecam.update();

    }

    @Override
    public void render(float delta) {

        // if player is dieing then you cant move.
        handleInputs();

        Gdx.gl.glClearColor(0,0,0,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);


        update(delta);

        // box2d debug lines.
        b2dr.render(world,gamecam.combined);

    }


    private void handleInputs() {

            handleTouchInputs();

    }

    private void handleTouchInputs() {

        // for loop because it can have multiple touches
        for (int i = 0; i < 20 ; i++) {
            if (Gdx.input.isTouched(i)){


            }
        }

    }



    // needed when useing Tiled. else Blackscreen on android.
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

        world.dispose();
        b2dr.dispose();



    }



    public World getWorld(){
        return world;
    }

    public AssetManager getManager() {
        return manager;
    }

}
