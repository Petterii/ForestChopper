package com.mygdx.game.ContactListener;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.mygdx.game.Items.Items;
import com.mygdx.game.Units.Enemy;
import com.mygdx.game.Units.Player;

import static com.mygdx.game.ForestChopper.ENDTREE_BIT;
import static com.mygdx.game.ForestChopper.ENEMYWALLS_BIT;
import static com.mygdx.game.ForestChopper.ENEMY_BIT;
import static com.mygdx.game.ForestChopper.ITEM_BIT;
import static com.mygdx.game.ForestChopper.OBJECT_BIT;
import static com.mygdx.game.ForestChopper.PLAYERSWORD_BIT;
import static com.mygdx.game.ForestChopper.PLAYER_BIT;
import static com.mygdx.game.ForestChopper.WALL_BIT;

public class WorldContactListener implements ContactListener {

    @Override
    public void beginContact(Contact contact) {
        Fixture fixA = contact.getFixtureA();
        Fixture fixB = contact.getFixtureB();

        int cDef = fixA.getFilterData().categoryBits | fixB.getFilterData().categoryBits;

        switch (cDef){
            case ITEM_BIT | PLAYER_BIT:
                if (fixA.getFilterData().categoryBits == PLAYER_BIT) {

                    ((Player) fixA.getUserData()).collectItem(((Items) fixB.getUserData()).collectItem());
                }
                else
                    ((Player) fixB.getUserData()).collectItem(((Items) fixA.getUserData()).collectItem());
                break;
            case ENEMY_BIT | PLAYERSWORD_BIT:
                if (fixA.getFilterData().categoryBits == ENEMY_BIT)
                    ((Enemy) fixA.getUserData()).gotHit();
                else
                    ((Enemy) fixB.getUserData()).gotHit();
                break;
            case ENEMY_BIT | WALL_BIT:
                if (fixA.getFilterData().categoryBits == ENEMY_BIT)
                    ((Enemy) fixA.getUserData()).reverseVelocity(true,false);
                else
                    ((Enemy) fixB.getUserData()).reverseVelocity(true,false);
                break;
            case ENEMY_BIT | OBJECT_BIT:
                if (fixA.getFilterData().categoryBits == ENEMY_BIT)
                    ((Enemy) fixA.getUserData()).reverseVelocity(true,false);
                else
                    ((Enemy) fixB.getUserData()).reverseVelocity(true,false);
                break;
            case ENEMY_BIT | ENEMYWALLS_BIT:
                if (fixA.getFilterData().categoryBits == ENEMY_BIT)
                    ((Enemy) fixA.getUserData()).reverseVelocity(true,false);
                else
                    ((Enemy) fixB.getUserData()).reverseVelocity(true,false);
                break;
            case PLAYER_BIT | ENDTREE_BIT:
                if (fixA.getFilterData().categoryBits == PLAYER_BIT) {
                    ((Player) fixA.getUserData()).chopThatTree();
                }else {
                    ((Player) fixB.getUserData()).chopThatTree();
                }
                break;
            case PLAYER_BIT | ENEMY_BIT:
                if (fixA.getFilterData().categoryBits == PLAYER_BIT) {
                    ((Enemy) fixB.getUserData()).reverseVelocity(true,false);
                    if (((Enemy) fixB.getUserData()).getState() != Enemy.State.HURT)
                        ((Player) fixA.getUserData()).gotHit(((Enemy) fixB.getUserData()).dmgAmount());

                }else {
                    ((Enemy) fixA.getUserData()).reverseVelocity(true,false);
                    if (((Enemy) fixA.getUserData()).getState() != Enemy.State.HURT)
                        ((Player) fixB.getUserData()).gotHit(((Enemy) fixA.getUserData()).dmgAmount());
                }
                break;
        }
    }

    @Override
    public void endContact(Contact contact) {

    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }





}
