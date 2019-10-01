package com.mygdx.game.Units.Enemies;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.Helpers.CustomBody;
import com.mygdx.game.Items.Coin;
import com.mygdx.game.Screens.Hud;
import com.mygdx.game.Screens.PlayScreen;
import com.mygdx.game.Units.Enemy;


import static com.mygdx.game.ForestChopper.ENEMYWALLS_BIT;
import static com.mygdx.game.ForestChopper.ENEMY_BIT;
import static com.mygdx.game.ForestChopper.GROUND_BIT;

import static com.mygdx.game.ForestChopper.OBJECT_BIT;
import static com.mygdx.game.ForestChopper.PLAYERSWORD_BIT;
import static com.mygdx.game.ForestChopper.PLAYER_BIT;
import static com.mygdx.game.ForestChopper.PPM;
import static com.mygdx.game.ForestChopper.WALL_BIT;
import static com.mygdx.game.Screens.PlayScreen.SOUND_DAMAGE2;
import static com.mygdx.game.Screens.PlayScreen.TEXTURE_ENEMYPLANTSPRITESHEET;
import static com.mygdx.game.Screens.PlayScreen.TEXTURE_ORKDIEINGSPRITESHEET;
import static com.mygdx.game.Screens.PlayScreen.TEXTURE_ORKHURT;
import static com.mygdx.game.Screens.PlayScreen.TEXTURE_ORKSPRITESHEET;


public class Screecher extends Enemy {


    private State currentState;
    private State previousState;

    private final int DMG_AMOUNT = 30;
    private int hp;

    private boolean walkingRight;
    private float stateTime;
    private Animation<TextureRegion> walkAnimation;
    private Animation<TextureRegion> hurtAnimation;
    private Animation<TextureRegion> dieingAnimation;


    private Array<TextureRegion> frames;


    private Animation<TextureRegion> bloodSplatterAnimation;
    private boolean animateBlood;


    // size of each of our ork's in our sprite sheet texture's
    private final int oWsize = 64;
    private final int oHsize = 56;

    public Screecher(PlayScreen screen, float x, float y) {
        super(screen, x, y);
        hp = 100;
        setToDestroy = false;
        isDestroyed = false;
        currentState = State.WALKING;
        previousState = State.WALKING;
        animateBlood = false;

        frames = new Array<TextureRegion>();

        // loop our frames and store in Animation with a frameduration.
        for (int i = 0; i < 7; i++) {
            frames.add(new TextureRegion((Texture) screen.getManager().get(TEXTURE_ORKSPRITESHEET),i*oWsize,0,oWsize,oHsize));
        }
        walkAnimation = new Animation(0.2f,frames);
        frames.clear();
        for (int i = 0; i < 5; i++) {
            frames.add(new TextureRegion((Texture) screen.getManager().get(TEXTURE_ORKSPRITESHEET),i*50,0,50,35));
        }
        bloodSplatterAnimation = new Animation(0.4f,frames);
        frames.clear();

        for (int i = 0; i < 7; i++) {
            frames.add(new TextureRegion((Texture) screen.getManager().get(TEXTURE_ORKDIEINGSPRITESHEET),i*oWsize,0,oWsize,oHsize));
        }
        dieingAnimation = new Animation(0.2f,frames);
        frames.clear();


        hurtAnimation = new Animation(0.2f,new TextureRegion((Texture) screen.getManager().get(TEXTURE_ORKHURT)));


        stateTime = 0;

        setBounds(getX(),getY(),oWsize/2/PPM,oHsize/2/PPM);
    }

    @Override
    public int dmgAmount() {
        return DMG_AMOUNT;
    }

    // private CustomBody mainBody;
    @Override
    protected void create2dBoxEnemy() {
        int x,y,radius;
        radius = 15;
        mainBody = new CustomBody(world,getX(),getY(), CustomBody.BodyType.DYNAMICBODY,radius);
        mainBody.finilizeCollision((short)(GROUND_BIT | PLAYER_BIT | OBJECT_BIT | PLAYERSWORD_BIT | WALL_BIT | ENEMYWALLS_BIT),ENEMY_BIT);
        mainBody.finalize(this);

        /*
        BodyDef bdef = new BodyDef();
        bdef.position.set(getX(),getY());
        bdef.type = BodyDef.BodyType.DynamicBody;
        body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(15/PPM);
        fdef.filter.categoryBits = ENEMY_BIT;
        fdef.filter.maskBits = GROUND_BIT | PLAYER_BIT | OBJECT_BIT | PLAYERSWORD_BIT | WALL_BIT | ENEMYWALLS_BIT;

        fdef.shape = shape;
        body.createFixture(fdef).setUserData(this);

        shape.dispose();
        */
    }

    @Override
    public void getHit() {

    }

    @Override
    public void draw(Batch batch) {
        super.draw(batch);

    }

    public State getState() {
        if (currentState == State.DIEING)
            return State.DIEING;
        if (animateBlood) {
            if (hurtAnimation.isAnimationFinished(stateTime))
                animateBlood = false;
            return State.HURT;
        }

        if (currentState == State.ATTACKING)
            return State.ATTACKING;
        else if (mainBody.getBody().getLinearVelocity().y > 0 || (mainBody.getBody().getLinearVelocity().y < 0 && previousState == State.JUMPING))
            return State.JUMPING;
        else if (mainBody.getBody().getLinearVelocity().y < 0)
            return State.WALKING;
        else if (mainBody.getBody().getLinearVelocity().x != 0)
            return State.WALKING;


        return State.WALKING;
    }

    private TextureRegion getFrame(float dt) {
        currentState = getState();

        TextureRegion region = new TextureRegion();
        switch (currentState){
            case JUMPING:
                region = walkAnimation.getKeyFrame(stateTime);
                break;
            case WALKING:
                region = walkAnimation.getKeyFrame(stateTime, true);
                break;
            case ATTACKING:
                region = walkAnimation.getKeyFrame(stateTime, true);
                break;
            case HURT:
                region = hurtAnimation.getKeyFrame(stateTime, true);
                break;
            case DIEING:
                region = dieingAnimation.getKeyFrame(stateTime);
                break;
            default:
                region = walkAnimation.getKeyFrame(stateTime,true);
                break;
        }

        if ((mainBody.getBody().getLinearVelocity().x < 0 || !walkingRight) && !region.isFlipX())
        {
            region.flip(true,false);
            walkingRight = false;
        }
        else if ((mainBody.getBody().getLinearVelocity().x > 0 || walkingRight) && region.isFlipX()){
            region.flip(true,false);
            walkingRight = true;
        }

        stateTime = currentState == previousState ? stateTime + dt : 0;
        previousState = currentState;

        return region;
    }

    @Override
    public void update(float dt) {
        stateTime += dt;
        if (currentState == State.DIEING) {
            // need to have it like this so death animation is shown.
            if (!isDestroyed){
                Hud.addScore(20);
                world.destroyBody(mainBody.getBody());
                isDestroyed = true;
            }

            // deathAnimation show
            if (stateTime < 2f)
                setRegion(getFrame(dt));
            else
                toBedeleted = true;

        }else if(!isDestroyed){
            mainBody.getBody().setLinearVelocity(velocity);
            setPosition(mainBody.getBody().getPosition().x - getWidth() / 2, mainBody.getBody().getPosition().y - getHeight() /2);
            setRegion(getFrame(dt));
        }

        if (velocity.x == 0 && stateTime >= 1)
            velocity.x =1;

    }

    public boolean toBedeleted() {
        return toBedeleted;
    }

    @Override
    public void gotHit() {
        Sound sound = screen.getManager().get(SOUND_DAMAGE2);
        sound.play();
        velocity.x = 0;
        stateTime = 0;
        hp -= 30;
        float xForce = screen.getPlayer().getMainBody().getPosition().x < mainBody.getBody().getPosition().x ? 2 : -2;
        mainBody.getBody().applyLinearImpulse(new Vector2(xForce,2f),mainBody.getBody().getWorldCenter(),true);
        stateTime = 0;
        animateBlood = true;


        if (hp <= 0){

            screen.getItems().add(new Coin(screen,getX(),getY()));
            screen.getItems().add(new Coin(screen,getX(),getY()));
            screen.getItems().add(new Coin(screen,getX(),getY()));
            screen.getItems().add(new Coin(screen,getX(),getY()));
            screen.getItems().add(new Coin(screen,getX(),getY()));
            screen.getItems().add(new Coin(screen,getX(),getY()));
            screen.getItems().add(new Coin(screen,getX(),getY()));


            currentState = State.DIEING;
            setToDestroy = true;
        }
    }
}
