package com.yinghanxu.game.sprites;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import java.util.Random;

/**
 * Created by 英瀚 on 2016/11/9.
 */

public class Tube {
    public static final int TUBE_WIDTH = 52;

    private static final int FLUCTUATION = 130;
    private static final int TUBE_GAP = 100;    //tube gap (so that the bird can fly through it)
    private static final int LOWEST_OPENING = 120;
    private Texture topTube, bottomTube;
    private Vector2 posTopTube, posBottomTube;
    private Rectangle boundsTop, boundsBot;
    private Random rand;

    private Bird bird;

    public Tube(float x) {

        topTube = new Texture("toptube.png");
        bottomTube = new Texture("obstacle.png");
        rand = new Random();

        posTopTube = new Vector2(x, Ground.GROUND_HEIGHT);
        posBottomTube = new Vector2(x, Ground.GROUND_HEIGHT);//-bird.GROUND_Y_OFFSET + (bottomTube.getHeight() / 2));//bottomTube.getHeight() + 10); //posTopTube.y - TUBE_GAP - bottomTube.getHeight());

        boundsTop = new Rectangle(posTopTube.x, posTopTube.y, topTube.getWidth(), topTube.getHeight()); //use the 4 parameters' method, x, y, width, height
        boundsBot = new Rectangle(posBottomTube.x, posBottomTube.y, bottomTube.getWidth(), bottomTube.getHeight());

    }

    public Texture getTopTube() {
        return topTube;
    }

    public Texture getBottomTube() {
        return bottomTube;
    }

    public Vector2 getPosTopTube() {
        return posTopTube;
    }

    public Vector2 getPosBottomTube() {
        return posBottomTube;
    }

    public void reposition(float x) {
        posTopTube.set(x, Ground.GROUND_HEIGHT);
        posBottomTube.set(x, Ground.GROUND_HEIGHT);
        boundsTop.setPosition(posTopTube.x, posTopTube.y);
        boundsBot.setPosition(posBottomTube.x, posBottomTube.y);
    }

    public boolean collides(Rectangle player) {
        return player.overlaps(boundsTop) || player.overlaps(boundsBot);
    }

    public void dispose(){
        topTube.dispose();
        bottomTube.dispose();
    }
}
