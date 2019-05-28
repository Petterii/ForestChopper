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
import com.mygdx.game.Items.Coin;
import com.mygdx.game.Items.Items;
import com.mygdx.game.Screens.Hud;
import com.mygdx.game.Screens.PlayScreen;

import java.awt.Polygon;
import java.awt.Rectangle;

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
import static com.mygdx.game.Screens.PlayScreen.TEXTURE_PLAYERHURT;


public class Player extends Sprite {


    public void attack() {

        if (!swinging) {
            swinging= true;
            attackStance();
            currentState = State.ATTACKING;
        }
    }

    public void collectItem(Items itemCollected) {
        if (itemCollected instanceof Coin){
            Hud.addScore(1000);
        }
    }

    public enum State {RUNNING, JUMPING , IDLE, FALLING, ATTACKING, DIEING, HURT}


    public Body b2body;
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

    private final int pSize = 96;

    private PlayScreen screen;
    private Texture texture;
    private Texture dead;
    private Texture hurtP;

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

        hurtP = new Texture(TEXTURE_PLAYERHURT);

       this.texture = texture;
       this.dead = dead;
       initialiseAnimations();

        setBounds(0,0,50/PPM,50/PPM);
        setRegion(playerIdle.getKeyFrame(stateTimer));
    }

    private void initialiseAnimations() {
        Array<TextureRegion> frames = new Array<TextureRegion>();
        for (int i = 0; i < 4; i++) {
            frames.add(new TextureRegion(getTexture(),i*pSize,0,pSize,pSize));
        }
        playerIdle = new Animation(0.1f,frames);
        frames.clear();

        for (int i = 0; i < 8; i++) {
            frames.add(new TextureRegion(getTexture(),i*pSize,pSize*1,pSize,pSize));
        }
        playerRun = new Animation(0.1f,frames);
        frames.clear();
        for (int i = 0; i < 7; i++) {
            frames.add(new TextureRegion(getTexture(),i*pSize,pSize*2,pSize,pSize));
        }
        playerAttack = new Animation(0.1f,frames);
        frames.clear();

        for (int i = 0; i < 6; i++) {
            frames.add(new TextureRegion(getTexture(),i*pSize,pSize*3,pSize,pSize));
        }
        playerJump = new Animation(0.1f,frames);
        frames.clear();

        for (int i = 0; i < 1; i++) {
            frames.add(new TextureRegion(hurtP,i*96,0,96,96));
        }
        playerHurt = new Animation(1f,frames);
        frames.clear();

        for (int i = 0; i < 5; i++) {
            frames.add(new TextureRegion(dead,i*96,0,96,96));
        }
        playerDieing = new Animation(0.2f,frames);
        frames.clear();
    }

    @Override
    public Texture getTexture() {
        return texture;
    }

    private boolean playingWalk;
    public void update(float dt) {


        if (currentState != State.DIEING) {
            Sound s = screen.getManager().get(SOUND_WALK);
            if (currentState == State.RUNNING && !playingWalk) {
                playingWalk = true;
                long id = s.play(0.3f);
                s.setLooping(id,true);
            } else if (currentState != State.RUNNING) {
                s.stop();
                playingWalk = false;
            }

            setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2);
            setRegion(getFrame(dt));
        } else{
            if (!destroyed)
                world.destroyBody(b2body);
            destroyed = true;
            setRegion(getFrame(dt));

        }


    }

    public State getState() {
        if (currentState == State.ATTACKING)
            return State.ATTACKING;
        else if (currentState == State.HURT && stateTimer <0.2f)
            return State.HURT;
        else if (b2body.getLinearVelocity().y > 0 || (b2body.getLinearVelocity().y < 0 && previousState == State.JUMPING))
            return State.JUMPING;
        else if (b2body.getLinearVelocity().y < 0)
            return State.IDLE;
        else if (b2body.getLinearVelocity().x != 0)
            return State.RUNNING;
        else
            return State.IDLE;


    }

    public float getStateTimer() {
        return stateTimer;
    }

    private TextureRegion getFrame(float dt) {
        if (currentState != State.DIEING) {
            currentState = getState();
        }else{previousState = currentState;}

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

        if ((b2body.getLinearVelocity().x < 0 || !runningRight) && !region.isFlipX())
        {
            region.flip(true,false);
            runningRight = false;
        }
        else if ((b2body.getLinearVelocity().x > 0 || runningRight) && region.isFlipX()){
            region.flip(true,false);
            runningRight = true;
        }

        // if in new STATE reset timer
        stateTimer = currentState == previousState ? stateTimer + dt : 0;
        previousState = currentState;

        return region;
    }

    private void definePlayer() {
        BodyDef bdef = new BodyDef();
        bdef.position.set(32/PPM,132/PPM);
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(5/PPM);
        fdef.filter.categoryBits = PLAYER_BIT;
        fdef.filter.maskBits = GROUND_BIT | OBJECT_BIT | ENEMY_BIT | WALL_BIT | ITEM_BIT;

        fdef.shape = shape;
        b2body.createFixture(fdef).setUserData(this);

        shape.dispose();
    }




    private void createBodySwordSwingArea(){
        BodyDef bdef2word = new BodyDef();
        bdef2word.type = BodyDef.BodyType.StaticBody;
        FixtureDef fdef = new FixtureDef();
        PolygonShape shape = new PolygonShape();
        shape.setAsBox((200/9)/PPM, (100/9)/PPM);

        fdef.filter.categoryBits = PLAYERSWORD_BIT;
        fdef.filter.maskBits = ENEMY_BIT;

        fdef.shape = shape;
        fdef.isSensor = true;
        swordfixture = b2body.createFixture(fdef);

        shape.dispose();



    }

    public void destroySword(){
        if(swordfixture != null){
            b2body.destroyFixture(swordfixture);
            swordfixture = null;
            sword = null;
        }
    }

    private boolean swinging;

    private void attackStance(){
        Sound getHurt = screen.getManager().get(SOUND_SWORDSWING);
        getHurt.play(0.1f);
        createBodySwordSwingArea();
        new Thread(new Runnable() {
            @Override
            public void run() {
                long time = System.currentTimeMillis();
                while (System.currentTimeMillis() < time + 200){}
                Gdx.app.postRunnable(new Runnable() {
                    @Override
                    public void run() {
                        destroySword();
                        swinging = false;
                        currentState = State.IDLE;
                    }
                });
            }
        }).start();
    }

    public void gotHit(int dmgAmount){
       ((Sound) screen.getManager().get(SOUND_PLAYERHURT)).play();
        currentState = State.HURT;
        stateTimer = 0;
        Hud.reduceHealth(dmgAmount);
        if (Hud.getHealth() <= 0){
            playerDied();
        }else{
        b2body.applyLinearImpulse(new Vector2(0,2f),b2body.getWorldCenter(),true);
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


}