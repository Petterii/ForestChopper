package com.mygdx.game.Items;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.Helpers.B2dPolygon;
import com.mygdx.game.Helpers.CustomBody;

import static com.mygdx.game.ForestChopper.ENEMY_BIT;
import static com.mygdx.game.ForestChopper.PLAYERSWORD_BIT;
import static com.mygdx.game.ForestChopper.PPM;
import static com.mygdx.game.Screens.PlayScreen.TEXTURE_MINITOUR4STANCES;

public class Sword extends Sprite {

    private B2dPolygon body;
    private Animation<TextureRegion> swordAnim;

    public Sword(Object object,World world, Texture texture,  float x, float y, CustomBody.BodyType bodyType,float widthX, float hightY, boolean trigger) {

        body = new B2dPolygon(world,(x-widthX)/PPM,y/PPM, CustomBody.BodyType.DYNAMICBODY,widthX,hightY,trigger);

        body.Collision((short)ENEMY_BIT,(short)PLAYERSWORD_BIT);
        body.Finalize(object);

        Array<TextureRegion> frames = new Array<TextureRegion>();

        frames.add(new TextureRegion(texture,61,16));

        swordAnim = new Animation(0.1f,frames);
        frames.clear();

        setOrigin(-widthX/PPM,-hightY/PPM);
        //flip(true,true);
        setBounds(0,0,-widthX*2/PPM,-hightY*2/PPM);
       // setRotation(90);
        setRegion(swordAnim.getKeyFrame(0));


    }

    private float toDegrees(){
        return (float)(body.getBody().getAngle()*(180/Math.PI));
    }

    public void update(float dt){
       // Gdx.app.log("Attack:", ""+body.getBody().getAngle()+ " toDegrees : "+toDegrees());
        setPosition(body.getBody().getPosition().x+(30/PPM), body.getBody().getPosition().y+5/PPM);
        setRotation(toDegrees());


        //setRegion(sw(dt));
    }

    public Body getBody() {
        return body.getBody();
    }
}
