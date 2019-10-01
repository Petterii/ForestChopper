package com.mygdx.game.Helpers;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.game.Units.Enemies.Screecher;
import com.mygdx.game.Units.Enemy;
import com.mygdx.game.Units.Player;

import static com.mygdx.game.ForestChopper.ENDTREE_BIT;
import static com.mygdx.game.ForestChopper.ENEMY_BIT;
import static com.mygdx.game.ForestChopper.GROUND_BIT;
import static com.mygdx.game.ForestChopper.ITEM_BIT;
import static com.mygdx.game.ForestChopper.OBJECT_BIT;
import static com.mygdx.game.ForestChopper.PLAYER_BIT;
import static com.mygdx.game.ForestChopper.PPM;
import static com.mygdx.game.ForestChopper.WALL_BIT;

public class CustomBody {
    public void isTrigger(boolean b) {
       // this.trigger = true;
        fdef.isSensor = b;
    }



    public enum BodyType {DYNAMICBODY,STATICBODY}


    private float posX,posY,radius;
    private Body body;
    private Boolean trigger;
    private BodyDef bdef;
    private Fixture fixture;
    private FixtureDef fdef;
    private CircleShape shape;
    private BodyType bodyType;
    private World world;

    public CustomBody(World world, float x, float y, BodyType bodyType, float radius) {
        this.posX = x;
        this.posY = y;
        this.bodyType = bodyType;
        this.world = world;
        this.trigger = false;
       // body = new Body();
        init();
    }

    private Body mainBody;
    public CustomBody(Body mainBody, float x, float y, BodyType bodyType, float radius, boolean trigger) {
        this.posX = x;
        this.posY = y;
        this.bodyType = bodyType;
        this.mainBody = mainBody;
        this.trigger = trigger;
        // body = new Body();
        init();
    }
    private void init(){
        bdef = new BodyDef();
        bdef.position.set(posX,posY);
        switch (bodyType){
            case DYNAMICBODY: bdef.type = BodyDef.BodyType.DynamicBody; break;
            case STATICBODY: bdef.type = BodyDef.BodyType.StaticBody; break;
        }
        if (mainBody == null)
            body = world.createBody(bdef);


        fdef = new FixtureDef();
        if(trigger)
            fdef.isSensor = true;
        shape = new CircleShape();
        shape.setRadius(5/PPM);
        fdef.shape = shape;
        shape.dispose();
    }

    public void finilizeCollision(short i, short x){
        fdef.filter.categoryBits = x; // defines this.
        fdef.filter.maskBits = i; // defines who it collides with.
    }

    public void finalize(Object object) {
        if (mainBody != null)
        {
            fixture = mainBody.createFixture(fdef);
            fixture.setUserData(object);
        }
        else
            body.createFixture(fdef).setUserData(object);
    }

    public float getPosX() {
        return posX;
    }

    public float getPosY() {
        return posY;
    }

    public BodyType getBodyType() {
        return bodyType;
    }

    public Body getBody() {
        return body;
    }
}
