package com.mygdx.game.Helpers;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

import static com.mygdx.game.ForestChopper.PPM;

public class B2dCircle {

    public B2dCircle(World world, float x, float y, CustomBody.BodyType bodyType, float radius, boolean trigger) {
        this.posX = x;
        this.posY = y;
        this.radius = radius;
        this.bodyType = bodyType;
        this.world = world;
        this.trigger = trigger;
        // body = new Body();
        init();
    }



    public void isTrigger(boolean b) {
        // this.trigger = true;
        fdef.isSensor = b;
    }

    public Fixture getFixture() {
        return fixture;
    }

    public void setFixture(Fixture fixture) {
        this.fixture = fixture;
    }

    private float posX,posY,radius;
    private Body body;
    private Boolean trigger;
    private BodyDef bdef;
    private Fixture fixture;
    private FixtureDef fdef;
    private CustomBody.BodyType bodyType;
    private World world;
    private Body mainBody;

    private void init(){
        bdef = new BodyDef();
        bdef.position.set(posX,posY);
        switch (bodyType){
            case DYNAMICBODY: bdef.type = BodyDef.BodyType.DynamicBody; break;
            case STATICBODY: bdef.type = BodyDef.BodyType.StaticBody; break;
        }
        body = world.createBody(bdef);

        fdef = new FixtureDef();
        if(trigger)
            fdef.isSensor = true;

        CircleShape shape2 = new CircleShape();
        shape2.setRadius(radius/PPM);
        fdef.shape = shape2;
        shape2.dispose();

    }


    public void Collision(short i, short x){
        fdef.filter.categoryBits = x; // defines this.
        fdef.filter.maskBits = i; // defines who it collides with.
    }

    public void Finalize(Object object) {
        fixture = body.createFixture(fdef);
        fixture.setUserData(object);
      }

    public float getPosX() {
        return posX;
    }

    public float getPosY() {
        return posY;
    }

    public CustomBody.BodyType getBodyType() {
        return bodyType;
    }

    public Body getBody() {
        return body;
    }


}
