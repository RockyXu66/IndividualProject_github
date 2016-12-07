package com.yinghanxu.game.sprites;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import java.util.Random;

/**
 * Created by 英瀚 on 2016/11/9.
 */

public class Ally {
    private Vector2 ally;
    private Rectangle bounds;
    private Texture texture, textureCollide;
    private AllyAnimation allyAnimationRun;
    private int runFrameNum = 20;
    public int status = 0;

    public Ally(float x) {

        ally = new Vector2(x, Ground.GROUND_HEIGHT);
        texture = new Texture("ally.png");
        textureCollide = new Texture("allyDie.png");
        allyAnimationRun = new AllyAnimation(new TextureRegion(texture), runFrameNum, 0.5f);

        bounds = new Rectangle(ally.x, ally.y, texture.getWidth() / runFrameNum , texture.getHeight()); //use the 4 parameters' method, x, y, width, height

    }

    public void update(float dt) {
        allyAnimationRun.update(dt);


    }
    public Texture getAlly() {
        return texture;
    }

    public TextureRegion getTexture() {
        return allyAnimationRun.getFrame();
    }

    public Vector2 getPosAlly() {
        return ally;
    }

    public void reposition(float x) {
        ally.set(x, Ground.GROUND_HEIGHT);
        bounds.setPosition(ally.x, ally.y);
    }

    public boolean collides(Rectangle player) {
        return player.overlaps(bounds);
    }

    public void dispose(){
        texture.dispose();
    }

    public Texture getCollideTexture() {
        return textureCollide;
    }
}
