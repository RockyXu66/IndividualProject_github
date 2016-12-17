package com.yinghanxu.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.yinghanxu.game.Ninja;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = Ninja.WIDTH;  //set some basic setting in this place
		config.height = Ninja.HEIGHT;		//set some basic setting in this place
		config.title = Ninja.TITLE;	//set some basic setting in this place
		new LwjglApplication(new Ninja(), config);
	}
}
