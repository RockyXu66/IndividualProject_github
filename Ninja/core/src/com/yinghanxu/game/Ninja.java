package com.yinghanxu.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.yinghanxu.game.States.GameStateManager;
import com.yinghanxu.game.States.MenuState;

public class Ninja extends ApplicationAdapter {
	public static final int WIDTH = 1080;
	public static final int HEIGHT = 1920;

	public static final String TITLE = "Ninja";

	private GameStateManager gsm;
	private SpriteBatch batch;

	private Music music;

	@Override
	public void create () {
		batch = new SpriteBatch();
		gsm = new GameStateManager();
		music = Gdx.audio.newMusic(Gdx.files.internal("music.mp3"));
		music.setLooping(true);
		music.setVolume(0.1f);	// 1 would be 100% volume
		//music.play();
		Gdx.gl.glClearColor(1, 1, 1, 1);	//set the background color to be white
		gsm.push(new MenuState(gsm));
	}

	@Override
	public void render () {
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		gsm.update(Gdx.graphics.getDeltaTime());
		gsm.render(batch);
	}

	@Override
	public void dispose() {
		super.dispose();
		music.dispose();
	}
}
