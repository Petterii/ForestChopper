package com.mygdx.game.Helpers;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.game.Items.Coin;
import com.mygdx.game.Screens.PlayScreen;
import com.mygdx.game.Units.Enemies.Plant;
import com.mygdx.game.Units.Enemies.Screecher;

import static com.mygdx.game.ForestChopper.ENDTREE_BIT;
import static com.mygdx.game.ForestChopper.ENEMYWALLS_BIT;
import static com.mygdx.game.ForestChopper.GROUND_BIT;
import static com.mygdx.game.ForestChopper.OBJECT_BIT;
import static com.mygdx.game.ForestChopper.PPM;
import static com.mygdx.game.ForestChopper.WALL_BIT;


public class CreateWorldFromTiled {




    public CreateWorldFromTiled(PlayScreen screen) {
        TiledMap map = screen.getMap();
        World world = screen.getWorld();

        BodyDef bdef = new BodyDef();
        PolygonShape shape = new PolygonShape();
        FixtureDef fdef = new FixtureDef();
        Body body;


        // ground objects from Tiled
        for (MapObject object :
                map.getLayers().get("ground").getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();

            bdef.type = BodyDef.BodyType.StaticBody;
            bdef.position.set((rect.getX() + rect.getWidth() / 2) /PPM,(rect.getY() + rect.getHeight()/2) /PPM);

            body = world.createBody(bdef);

            shape.setAsBox((rect.getWidth()/2)/PPM,(rect.getHeight()/2)/PPM);
            fdef.shape = shape;
            fdef.filter.categoryBits = GROUND_BIT;
            body.createFixture(fdef);

        }
        // objects in world
        for (MapObject object :
                map.getLayers().get("objects").getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();

            bdef.type = BodyDef.BodyType.StaticBody;
            bdef.position.set((rect.getX() + rect.getWidth() / 2) /PPM,(rect.getY() + rect.getHeight()/2) /PPM);

            body = world.createBody(bdef);

            shape.setAsBox((rect.getWidth()/2)/PPM,(rect.getHeight()/2)/PPM);
            fdef.shape = shape;
            fdef.filter.categoryBits = OBJECT_BIT;
            body.createFixture(fdef);

        }

        // enemies created
        for (MapObject object :
                map.getLayers().get("enemies").getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();

            screen.addEnemy(new Screecher(screen,rect.getX()/PPM,rect.getY()/PPM));

        }

        // enemyplants created.
        for (MapObject object :
                map.getLayers().get("enemiesplants").getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();

            screen.addEnemy(new Plant(screen,rect.getX()/PPM,rect.getY()/PPM));

        }


        for (MapObject object :
                map.getLayers().get("walls").getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();

            bdef.type = BodyDef.BodyType.StaticBody;
            bdef.position.set((rect.getX() + rect.getWidth() / 2) /PPM,(rect.getY() + rect.getHeight()/2) /PPM);

            body = world.createBody(bdef);

            shape.setAsBox((rect.getWidth()/2)/PPM,(rect.getHeight()/2)/PPM);
            fdef.shape = shape;
            fdef.filter.categoryBits = WALL_BIT;
            body.createFixture(fdef);

        }

        // enemy walls created where only enemies react. So they dont fall down from platforms.
        for (MapObject object :
                map.getLayers().get("enemywalls").getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();

            bdef.type = BodyDef.BodyType.StaticBody;
            bdef.position.set((rect.getX() + rect.getWidth() / 2) /PPM,(rect.getY() + rect.getHeight()/2) /PPM);

            body = world.createBody(bdef);

            shape.setAsBox((rect.getWidth()/2)/PPM,(rect.getHeight()/2)/PPM);
            fdef.shape = shape;
            fdef.filter.categoryBits = ENEMYWALLS_BIT;
            body.createFixture(fdef);

        }

        // The tree at the end. AKA finish line.
        for (MapObject object :
                map.getLayers().get("treeend").getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();

            bdef.type = BodyDef.BodyType.StaticBody;
            bdef.position.set((rect.getX() + rect.getWidth() / 2) /PPM,(rect.getY() + rect.getHeight()/2) /PPM);

            body = world.createBody(bdef);

            shape.setAsBox((rect.getWidth()/2)/PPM,(rect.getHeight()/2)/PPM);
            fdef.shape = shape;
            fdef.filter.categoryBits = ENDTREE_BIT;
            body.createFixture(fdef);

        }

        shape.dispose();
    }

}
