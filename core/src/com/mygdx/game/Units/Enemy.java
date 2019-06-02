package com.mygdx.game.Units;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.game.Screens.PlayScreen;

public abstract class Enemy extends Sprite {

    protected World world;
    protected PlayScreen screen;
    public Body body;
    public Vector2 velocity;

    protected boolean setToDestroy;
    public boolean isDestroyed;
    protected boolean toBedeleted;

    public Enemy(PlayScreen screen, float x, float y) {
        this.screen = screen;
        this.world = screen.getWorld();
        setPosition(x,y);
        create2dBoxEnemy();
        velocity = new Vector2(1f,0);
        body.setActive(true);
    }

    public abstract int dmgAmount();
    protected abstract void create2dBoxEnemy();
    public abstract void getHit();
    public abstract void update(float dt);

    public void reverseVelocity(boolean x, boolean y){
        if (x)
            velocity.x = -velocity.x;
        if (y)
            velocity.y = -velocity.y;
    }

    public boolean isToBedeleted() {
        return toBedeleted;
    }

    public abstract void gotHit();
}
