package com.mygdx.game.Units.Enemies;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
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
import static com.mygdx.game.Screens.PlayScreen.TEXTURE_ENEMYPLANTDIEINGSPRITESHEET;
import static com.mygdx.game.Screens.PlayScreen.TEXTURE_ENEMYPLANTHURT;
import static com.mygdx.game.Screens.PlayScreen.TEXTURE_ENEMYPLANTSPRITESHEET;

public class Plant extends Enemy {
    private State currentState;
    private State previousState;

    private final int DMG_AMOUNT = 50;
    private int hp;

    private boolean walkingRight;
    private float stateTime;
    private Animation<TextureRegion> walkAnimation;
    private Animation<TextureRegion> hurtAnimation;
    private Animation<TextureRegion> dieingAnimation;


    private Array<TextureRegion> frames;
    private boolean animateBlood;


    // size of each of our ork's in our sprite sheet texture's
    private final int oWsize = 96;
    private final int oHsize = 128;

    public Plant(PlayScreen screen, float x, float y) {
        super(screen, x, y);
        hp = 10;
        setToDestroy = false;
        isDestroyed = false;
        currentState = State.WALKING;
        previousState = State.WALKING;
        animateBlood = false;

        frames = new Array<TextureRegion>();

        // loop our frames and store in Animation with a frameduration.
        for (int i = 0; i < 2; i++) {
            frames.add(new TextureRegion((Texture) screen.getManager().get(TEXTURE_ENEMYPLANTSPRITESHEET),i*oWsize,0,oWsize,oHsize));
        }
        walkAnimation = new Animation(0.2f,frames);
        frames.clear();

        for (int i = 0; i < 3; i++) {
            frames.add(new TextureRegion((Texture) screen.getManager().get(TEXTURE_ENEMYPLANTDIEINGSPRITESHEET),i*oWsize,0,oWsize,oHsize));
        }
        dieingAnimation = new Animation(0.2f,frames);
        frames.clear();


        hurtAnimation = new Animation(0.2f,new TextureRegion((Texture) screen.getManager().get(TEXTURE_ENEMYPLANTHURT)));


        stateTime = 0;

        setBounds(getX(),getY(),oWsize/4/PPM,oHsize/4/PPM);
    }

    @Override
    public int dmgAmount() {
        return DMG_AMOUNT;
    }

    @Override
    protected void create2dBoxEnemy() {
        int x,y,radius;
        radius = 15;
        mainBody = new CustomBody(world,getX(),getY(), CustomBody.BodyType.DYNAMICBODY,radius);
        mainBody.Collision((short)(GROUND_BIT | PLAYER_BIT | OBJECT_BIT | PLAYERSWORD_BIT | WALL_BIT | ENEMYWALLS_BIT),ENEMY_BIT);
        mainBody.Finalize(this);
        /*
        BodyDef bdef = new BodyDef();
        bdef.position.set(getX(),getY());
        bdef.type = BodyDef.BodyType.DynamicBody;
        body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(15/PPM);
        fdef.filter.categoryBits = ENEMY_BIT;
        fdef.filter.maskBits = GROUND_BIT | PLAYER_BIT | OBJECT_BIT | PLAYERSWORD_BIT;

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

        return State.WALKING;
    }

    private TextureRegion getFrame(float dt) {
        currentState = getState();

        TextureRegion region = new TextureRegion();
        switch (currentState){
            case WALKING:
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
            setPosition(mainBody.getBody().getPosition().x - getWidth() / 2, mainBody.getBody().getPosition().y - getHeight() /2);
            setRegion(getFrame(dt));
        }

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
