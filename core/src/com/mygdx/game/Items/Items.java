package com.mygdx.game.Items;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Disposable;
import com.mygdx.game.Screens.PlayScreen;

public abstract class Items extends Sprite {
    public boolean destroyed;
    public boolean setTodestroy;


    protected PlayScreen screen;
    protected World world;
    public Body body;

    public Items(PlayScreen screen,float x,float y) {
        this.screen = screen;
        this.world = screen.getWorld();
        destroyed = false;
        // cant create item becaus it is created during a collsion in the world most often.
        // will be created during an update
        //createBox2dItem();
        setPosition(x,y);

       // body.setActive(true);
    }

    protected abstract void createBox2dItem();
    public abstract Items collectItem();
    public abstract void update(float dt);

}
