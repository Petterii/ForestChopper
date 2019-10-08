package com.mygdx.game.Helpers;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

import static com.mygdx.game.ForestChopper.PPM;

public class B2dPolygon {


    public B2dPolygon(World world, float x, float y, CustomBody.BodyType bodyType, float radius, boolean trigger) {
        this.posX = x;
        this.posY = y;
      //  this.radius = radius;
        this.bodyType = bodyType;
        this.world = world;
        this.trigger = trigger;
        // body = new Body();
        init();
    }
    public B2dPolygon(World world, float x, float y, CustomBody.BodyType bodyType, float widthX,float hightY, boolean trigger) {
        this.posX = x;
        this.posY = y;
        this.widthX = widthX;
        this.hightY = hightY;
        this.bodyType = bodyType;
        this.world = world;
        this.trigger = trigger;
        this.mainBody = mainBody;
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

    private float posX,posY,widthX,hightY;
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
            case KINEMATIC: bdef.type = BodyDef.BodyType.KinematicBody; break;
        }
        body = world.createBody(bdef);



        PolygonShape shape = new PolygonShape();
        shape.setAsBox(widthX/PPM, hightY/PPM);
        fdef = new FixtureDef();
        fdef.shape = shape;
        fdef.restitution = 0f;
        fdef.density = 1f;
        fdef.friction = 0f;
        fixture = body.createFixture(fdef);
        //fixture.setSensor(true);

        shape.dispose();


    }

    public void Collision(short i, short x){

        fdef.filter.categoryBits = x; // defines this.
        fdef.filter.maskBits = i; // defines who it collides with.
    }

    public void Finalize(Object object) {
        fixture.setFilterData(fdef.filter);
        fixture.setUserData(object);
    }

    public float getPosX() {
        return body.getPosition().x;
    }

    public float getPosY() {
        return body.getPosition().y;
    }

    public CustomBody.BodyType getBodyType() {
        return bodyType;
    }

    public Body getBody() {
        return body;
    }

}
