package com.mygdx.game.Items;

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
import com.mygdx.game.Screens.PlayScreen;

import java.util.Random;

import static com.mygdx.game.ForestChopper.ENEMYWALLS_BIT;
import static com.mygdx.game.ForestChopper.GROUND_BIT;
import static com.mygdx.game.ForestChopper.ITEM_BIT;
import static com.mygdx.game.ForestChopper.OBJECT_BIT;
import static com.mygdx.game.ForestChopper.PLAYER_BIT;
import static com.mygdx.game.ForestChopper.PPM;
import static com.mygdx.game.ForestChopper.WALL_BIT;
import static com.mygdx.game.Screens.PlayScreen.SOUND_COINAPEAR;
import static com.mygdx.game.Screens.PlayScreen.SOUND_COINPICKUPP;
import static com.mygdx.game.Screens.PlayScreen.TEXTURE_COINSHEET;

public class Coin extends Items {


    private Animation<TextureRegion> rotationAnimation;
    private Array<TextureRegion> frames;
    private final int oWsize =40;
    private final int oHsize =42;


    private float stateTime;

    public Coin(PlayScreen screen, float x, float y) {
        super(screen, x, y);
        ((Sound)screen.getManager().get(SOUND_COINAPEAR)).play();
        create = true;
        frames = new Array<TextureRegion>();

        stateTime = 0;

        for (int i = 0; i < 3; i++) {
            frames.add(new TextureRegion((Texture)screen.getManager().get(TEXTURE_COINSHEET),0,i*42,oWsize,oHsize));
        }
        rotationAnimation = new Animation(0.5f,frames);
        frames.clear();

        setBounds(getX(),getY(),oWsize/3/PPM,oHsize/3/PPM);
        //setRegion(rotationAnimation.getKeyFrame(stateTime, true));
    }

    @Override
    protected void createBox2dItem() {
        BodyDef bdef = new BodyDef();
        bdef.position.set(getX(),getY());
        bdef.type = BodyDef.BodyType.DynamicBody;
        body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        //fdef.friction = 0f;
        fdef.density = 0f;
        fdef.restitution = 0f;
        CircleShape shape = new CircleShape();
        shape.setRadius(3/PPM);
        fdef.filter.categoryBits = ITEM_BIT;
        fdef.filter.maskBits = GROUND_BIT | PLAYER_BIT | OBJECT_BIT | WALL_BIT | ENEMYWALLS_BIT;

        fdef.shape = shape;

        body.createFixture(fdef).setUserData(this);
        Random r = new Random();
        float x = (r.nextInt(200)-100)*0.01f;
        body.applyLinearImpulse(new Vector2(x,3f),body.getWorldCenter(),true);
        shape.dispose();
    }

    @Override
    public void draw(Batch batch) {
        super.draw(batch);

    }

    @Override
    public Items collectItem() {
        ((Sound)screen.getManager().get(SOUND_COINPICKUPP)).play();
        setTodestroy = true;
        return this;
    }
    private boolean create;
    @Override
    public void update(float dt) {
        if (create){
            createBox2dItem();
            body.setActive(true);
            create = false;
        }
        if (setTodestroy){
            setTodestroy = false;
            destroyed = true;
            world.destroyBody(body);

        }else{
        stateTime += dt;
        setPosition(body.getPosition().x - getWidth() / 2, body.getPosition().y - getHeight() /2);
        setRegion(getFrame(dt));
        }
    }



    private TextureRegion getFrame(float dt) {

        stateTime = stateTime + dt;


        return rotationAnimation.getKeyFrame(stateTime, true);
    }

}
