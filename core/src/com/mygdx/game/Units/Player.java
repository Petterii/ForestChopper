package com.mygdx.game.Units;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;

import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.Helpers.CustomBody;
import com.mygdx.game.Items.Coin;
import com.mygdx.game.Items.Items;
import com.mygdx.game.Screens.Hud;
import com.mygdx.game.Screens.PlayScreen;

import static com.mygdx.game.ForestChopper.ENDTREE_BIT;
import static com.mygdx.game.ForestChopper.ENEMY_BIT;
import static com.mygdx.game.ForestChopper.GROUND_BIT;
import static com.mygdx.game.ForestChopper.ITEM_BIT;
import static com.mygdx.game.ForestChopper.OBJECT_BIT;
import static com.mygdx.game.ForestChopper.PLAYERSWORD_BIT;
import static com.mygdx.game.ForestChopper.PLAYER_BIT;
import static com.mygdx.game.ForestChopper.PPM;
import static com.mygdx.game.ForestChopper.WALL_BIT;
import static com.mygdx.game.Screens.PlayScreen.SOUND_PLAYERDIEING;
import static com.mygdx.game.Screens.PlayScreen.SOUND_PLAYERHURT;
import static com.mygdx.game.Screens.PlayScreen.SOUND_SWORDSWING;
import static com.mygdx.game.Screens.PlayScreen.SOUND_WALK;
import static com.mygdx.game.Screens.PlayScreen.TEXTURE_DEADPLAYER;
import static com.mygdx.game.Screens.PlayScreen.TEXTURE_MINITOUR4STANCES;
import static com.mygdx.game.Screens.PlayScreen.TEXTURE_PLAYERHURT;


public class Player extends Sprite {

    public enum State {RUNNING, JUMPING , IDLE, FALLING, ATTACKING, DIEING, HURT, WINNING}

   // public Body b2body;
    private World world;

    public State currentState;
    public State previousState;
    private Animation<TextureRegion> playerIdle;
    private Animation<TextureRegion> playerRun;
    private Animation<TextureRegion> playerAttack;
    private Animation<TextureRegion> playerJump;
    private Animation<TextureRegion> playerDieing;
    private Animation<TextureRegion> playerHurt;

    private float stateTimer;
    private boolean runningRight;

    private final int pSize = 96; // size of the frames of the player in spritesheet

    private PlayScreen screen;

    private Body sword;
    private Fixture swordfixture;

    public boolean destroyed;
    public boolean setToDestroy;

    public Player(PlayScreen screen, Texture texture,Texture dead) {
        world = screen.getWorld();
        this.screen = screen;
        definePlayer();
        playingWalk = false;
        currentState = State.IDLE;
        previousState = State.IDLE;
        stateTimer = 0;
        runningRight = true;
        setToDestroy = false;
        destroyed = false;

       initialiseAnimations();

        setBounds(0,0,50/PPM,50/PPM);
        setRegion(playerIdle.getKeyFrame(stateTimer));
    }

    private void initialiseAnimations() {
        Array<TextureRegion> frames = new Array<TextureRegion>();
        for (int i = 0; i < 4; i++) {
            frames.add(new TextureRegion((Texture)screen.getManager().get(TEXTURE_MINITOUR4STANCES),i*pSize,0,pSize,pSize));
        }
        playerIdle = new Animation(0.1f,frames);
        frames.clear();

        for (int i = 0; i < 8; i++) {
            frames.add(new TextureRegion((Texture)screen.getManager().get(TEXTURE_MINITOUR4STANCES),i*pSize,pSize*1,pSize,pSize));
        }
        playerRun = new Animation(0.1f,frames);
        frames.clear();
        for (int i = 0; i < 7; i++) {
            frames.add(new TextureRegion((Texture)screen.getManager().get(TEXTURE_MINITOUR4STANCES),i*pSize,pSize*2,pSize,pSize));
        }
        playerAttack = new Animation(0.1f,frames);
        frames.clear();

        for (int i = 0; i < 6; i++) {
            frames.add(new TextureRegion((Texture)screen.getManager().get(TEXTURE_MINITOUR4STANCES),i*pSize,pSize*3,pSize,pSize));
        }
        playerJump = new Animation(0.1f,frames);
        frames.clear();

        for (int i = 0; i < 1; i++) {
            frames.add(new TextureRegion((Texture)screen.getManager().get(TEXTURE_PLAYERHURT),i*96,0,96,96));
        }
        playerHurt = new Animation(1f,frames);
        frames.clear();

        for (int i = 0; i < 5; i++) {
            frames.add(new TextureRegion((Texture)screen.getManager().get(TEXTURE_DEADPLAYER),i*96,0,96,96));
        }
        playerDieing = new Animation(0.2f,frames);
        frames.clear();
    }

    private boolean playingWalk;
    public void update(float dt) {
        if (getState() == State.ATTACKING && stateTimer > 0.5f){
            destroySword();
            currentState = State.IDLE;
        }
        Sound s = screen.getManager().get(SOUND_WALK);
        if (currentState != State.DIEING) {
            if (currentState == State.RUNNING && !playingWalk) {
                playingWalk = true;
                long id = s.play(0.8f);
                s.setLooping(id,true);
            } else if (currentState != State.RUNNING) {
                s.stop();
                playingWalk = false;
            }

            setPosition(mainBody.getBody().getPosition().x - getWidth() / 2, mainBody.getBody().getPosition().y - getHeight() / 2);
            setRegion(getFrame(dt));
        } else{
            if (!destroyed){
                world.destroyBody(mainBody.getBody());
                s.stop();
            }
            destroyed = true;
            setRegion(getFrame(dt));

        }


    }

    // returns what is player doing right now.
    public State getState() {
        if (Hud.getHealth() <= 0)
            return State.DIEING;
        else if (currentState == State.WINNING)
            return State.WINNING;
        else if (swordfixture != null)
            return State.ATTACKING;
        else if (currentState == State.HURT && stateTimer <0.2f)
            return State.HURT;
        else if (mainBody.getBody().getLinearVelocity().y > 0 || (mainBody.getBody().getLinearVelocity().y < 0 && previousState == State.JUMPING))
            return State.JUMPING;
        else if (mainBody.getBody().getLinearVelocity().y < 0)
            return State.IDLE;
        else if (mainBody.getBody().getLinearVelocity().x != 0)
            return State.RUNNING;
        else
            return State.IDLE;


    }

    public float getStateTimer() {
        return stateTimer;
    }

    //
    private TextureRegion getFrame(float dt) {
        if (currentState != State.DIEING && currentState != State.WINNING && (currentState != State.ATTACKING || stateTimer > 3)) {
            currentState = getState();
        }else{previousState = currentState;}

        // got state now gets the animation frame.
        TextureRegion region = new TextureRegion();
        switch (currentState){
            case JUMPING:
                region = playerJump.getKeyFrame(stateTimer);
                break;
            case RUNNING:
                region = playerRun.getKeyFrame(stateTimer, true);
                break;
            case ATTACKING:
                region = playerAttack.getKeyFrame(stateTimer, true);
                break;
            case DIEING:
                region = playerDieing.getKeyFrame(stateTimer);
                break;
            case HURT:
                region = playerHurt.getKeyFrame(stateTimer);
                break;
            case FALLING:
            case IDLE:
            default:
                region = playerIdle.getKeyFrame(stateTimer,true);
                break;
        }

        // which way is player walking/facing.
        if ((mainBody.getBody().getLinearVelocity().x < 0 || !runningRight) && !region.isFlipX())
        {
            region.flip(true,false);
            runningRight = false;
        }
        else if ((mainBody.getBody().getLinearVelocity().x > 0 || runningRight) && region.isFlipX()){
            region.flip(true,false);
            runningRight = true;
        }

        // if in new STATE reset timer
        stateTimer = currentState == previousState ? stateTimer + dt : 0;
        previousState = currentState;

        return region;
    }

    private CustomBody mainBody;
    private int positionX,positionY,radius;

    // create the body i box2d and insert into world
    private void definePlayer() {
        positionX = 132;
        positionY = 132;
        radius = 5;

        mainBody = new CustomBody(world,positionX/PPM,positionY/PPM, CustomBody.BodyType.DYNAMICBODY,radius);
        mainBody.finilizeCollision((short)(GROUND_BIT | OBJECT_BIT | ENEMY_BIT | WALL_BIT | ITEM_BIT | ENDTREE_BIT),(short)PLAYER_BIT);
        mainBody.finalize(this);

    }



    // create sword box2d sensor.
    private void createBodySwordSwingArea(){

        BodyDef bdef2word = new BodyDef();
        bdef2word.type = BodyDef.BodyType.StaticBody;
        FixtureDef fdef = new FixtureDef();
        PolygonShape shape = new PolygonShape();
        shape.setAsBox((200/9)/PPM, (100/9)/PPM);

        fdef.filter.categoryBits = PLAYERSWORD_BIT; // this is Player sword
        fdef.filter.maskBits = ENEMY_BIT; // can collide with Enemy
        fdef.shape = shape;
        fdef.isSensor = true;
        swordfixture = mainBody.getBody().createFixture(fdef);

        shape.dispose();
    }

    public void destroySword(){
        if(swordfixture != null){
            mainBody.getBody().destroyFixture(swordfixture);
            swordfixture = null;
            sword = null;
        }
    }

    private void attackStance(){
        Sound getHurt = screen.getManager().get(SOUND_SWORDSWING);
        getHurt.play(0.3f);
        createBodySwordSwingArea();
        stateTimer = 0;
    }

    public void gotHit(int dmgAmount){
       ((Sound) screen.getManager().get(SOUND_PLAYERHURT)).play();
        currentState = State.HURT;
        stateTimer = 0;
        Hud.reduceHealth(dmgAmount);
        if (Hud.getHealth() <= 0){
            playerDied();
        }else{
            mainBody.getBody().applyLinearImpulse(new Vector2(0,2f),mainBody.getBody().getWorldCenter(),true);
        }
    }

    private void playerDied() {
        if (currentState != State.DIEING) {
           ((Sound) screen.getManager().get(SOUND_PLAYERDIEING)).play();
            currentState = State.DIEING;
            stateTimer = 0;

            setToDestroy = true;
        }
    }

    public void attack() {
        if (getState() != State.ATTACKING) {
            stateTimer = 0;
            attackStance();
            currentState = State.ATTACKING;
        }
    }

    public void collectItem(Items itemCollected) {
        // To check what kind of item it is.
        if (itemCollected instanceof Coin){
            Hud.addScore(1000);
        }
    }

    public void chopThatTree() {
        screen.setDialog(4);
        currentState = State.WINNING ;
    }

    public Body getMainBody(){
        return mainBody.getBody();
    }

}
