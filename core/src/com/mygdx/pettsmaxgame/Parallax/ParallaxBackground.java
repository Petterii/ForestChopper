package com.mygdx.pettsmaxgame.Parallax;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Array;

public class ParallaxBackground extends Actor {

    private int scroll;
    private Array<Texture> layers;
    private final float LAYER_SPEED_DIFFERENCE = 1f;

    float x,y,width,heigth,scaleX,scaleY;
    float originX, originY,rotation,srcX,srcY;
    boolean flipX,flipY;

    private float speed;

    public ParallaxBackground(Array<Texture> textures){
        layers = textures;
        for(int i = 0; i <textures.size;i++){
            layers.get(i).setWrap(Texture.TextureWrap.MirroredRepeat, Texture.TextureWrap.MirroredRepeat);
        }
        scroll = 0;
        speed = 0;

        x = y = originX = originY = rotation = srcY = 0;
        width =  Gdx.graphics.getWidth();
        heigth = Gdx.graphics.getHeight();
        scaleX = scaleY = 1;
        flipX = flipY = false;
    }

    public void setSpeed(float newSpeed){
        if (newSpeed == 0)
            this.speed = 0;
        else if (newSpeed < 0)
            this.speed = 1f;
        else
            this.speed = -1f;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.setColor(getColor().r, getColor().g, getColor().b, getColor().a * parentAlpha);

        scroll+=speed;
        for(int i = 0;i<layers.size;i++) {
            srcX = scroll + i*this.LAYER_SPEED_DIFFERENCE *scroll;
            batch.draw(layers.get(i), x, y, originX, originY, width, heigth,scaleX,scaleY,rotation,(int)srcX,(int)srcY,layers.get(i).getWidth(),layers.get(i).getHeight(),flipX,flipY);
        }
    }
}