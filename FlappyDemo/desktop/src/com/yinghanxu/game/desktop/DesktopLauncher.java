package com.yinghanxu.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.yinghanxu.game.FlappyDemo;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = FlappyDemo.WIDTH;  //set some basic setting in this place
		config.height = FlappyDemo.HEIGHT;		//set some basic setting in this place
		config.title = FlappyDemo.TITLE;	//set some basic setting in this place
		new LwjglApplication(new FlappyDemo(), config);
	}
}
