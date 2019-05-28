package com.mygdx.game;

import com.badlogic.gdx.Game;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.mygdx.game.Screens.NewGameScreen;
import com.mygdx.game.Screens.PlayScreen;

import static com.mygdx.game.Screens.PlayScreen.SOUND_COINAPEAR;
import static com.mygdx.game.Screens.PlayScreen.SOUND_COINPICKUPP;
import static com.mygdx.game.Screens.PlayScreen.SOUND_DAMAGE2;
import static com.mygdx.game.Screens.PlayScreen.SOUND_JUMP;
import static com.mygdx.game.Screens.PlayScreen.SOUND_PLAYERDIEING;
import static com.mygdx.game.Screens.PlayScreen.SOUND_PLAYERHURT;
import static com.mygdx.game.Screens.PlayScreen.SOUND_SWORDSWING;
import static com.mygdx.game.Screens.PlayScreen.SOUND_WALK;
import static com.mygdx.game.Screens.PlayScreen.TEXTURE_COINSHEET;
import static com.mygdx.game.Screens.PlayScreen.TEXTURE_DEADPLAYER;
import static com.mygdx.game.Screens.PlayScreen.TEXTURE_MINITOUR4STANCES;
import static com.mygdx.game.Screens.PlayScreen.TEXTURE_ORKDIEINGSPRITESHEET;
import static com.mygdx.game.Screens.PlayScreen.TEXTURE_ORKHURT;
import static com.mygdx.game.Screens.PlayScreen.TEXTURE_ORKSPRITESHEET;
import static com.mygdx.game.Screens.PlayScreen.TEXTURE_ORKWALK;
import static com.mygdx.game.Screens.PlayScreen.TEXTURE_PLAYERHURT;
import static com.mygdx.game.Screens.PlayScreen.UI_ATTACKICON;
import static com.mygdx.game.Screens.PlayScreen.UI_BUTTONS;

public class ForestChopper extends Game {


	public static final int V_WIDTH = 400;
	public static final int V_HEIGHT = 208;

	// scaleing of Box2d Stuff. from 1 pixel = 1 meter. to 1 pixel = 1cm
	public static final float PPM = 100;

	public static final short ENEMYWALLS_BIT = 128;
	public static final short PLAYER_BIT = 2;
	public static final short GROUND_BIT = 4;
	public static final short ITEM_BIT = 8;
	public static final short ENEMY_BIT = 16;
	public static final short WALL_BIT = 32;
	public static final short OBJECT_BIT = 64;
	public static final short PLAYERSWORD_BIT = 256;

	private AssetManager manager;

	public SpriteBatch batch;
	@Override
	public void create () {
		batch = new SpriteBatch();
		manager = new AssetManager();
		manager.load(SOUND_DAMAGE2, Sound.class);
		manager.load(SOUND_SWORDSWING, Sound.class);
		manager.load(SOUND_JUMP, Sound.class);
		manager.load(SOUND_WALK, Sound.class);
		manager.load(SOUND_PLAYERHURT, Sound.class);
		manager.load(SOUND_PLAYERDIEING, Sound.class);
		manager.load(SOUND_COINPICKUPP, Sound.class);
		manager.load(SOUND_COINAPEAR, Sound.class);


		manager.load(TEXTURE_DEADPLAYER, Texture.class);
		manager.load(TEXTURE_ORKHURT, Texture.class);
		manager.load(TEXTURE_PLAYERHURT, Texture.class);
		manager.load(TEXTURE_MINITOUR4STANCES, Texture.class);
		manager.load(TEXTURE_ORKWALK, Texture.class);
		manager.load(TEXTURE_ORKDIEINGSPRITESHEET, Texture.class);
		manager.load(TEXTURE_ORKSPRITESHEET, Texture.class);
		manager.load(TEXTURE_COINSHEET, Texture.class);

		manager.load(UI_BUTTONS, Texture.class);
		manager.load(UI_ATTACKICON, Texture.class);
		manager.finishLoading();
		setScreen(new NewGameScreen(this));
	}

	@Override
	public void render () {
		super.render();


	}

	public AssetManager getManager(){
		return manager;
	}

	@Override
	public void dispose () {
		super.dispose();
		batch.dispose();
		manager.dispose();
	}


}
