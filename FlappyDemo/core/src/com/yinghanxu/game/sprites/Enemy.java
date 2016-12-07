package com.yinghanxu.game.sprites;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by 英瀚 on 2016/11/9.
 */

public class Enemy {
    private Vector2 enemy;
    private Rectangle bounds;
    private Texture texture,textureCollide;
    private Animation enemyAnimationRun;
    private int runFrameNum = 3;
    public int status = 1;

    public Enemy(float x) {

        enemy = new Vector2(x, Ground.GROUND_HEIGHT);
        texture = new Texture("bird.png");
        textureCollide = new Texture("gameover.png");
        enemyAnimationRun = new Animation(new TextureRegion(texture), runFrameNum, 0.5f);

        bounds = new Rectangle(enemy.x, enemy.y, texture.getWidth() / runFrameNum , texture.getHeight()); //use the 4 parameters' method, x, y, width, height

    }

    public void update(float dt) {
        if (status == 1) {
            enemyAnimationRun.update(dt);
        } else {

        }


    }
    public Texture getEnemy() {
        return texture;
    }

    public TextureRegion getTexture() {
        return enemyAnimationRun.getFrame();
    }

    public Vector2 getPosEnemy() {
        return enemy;
    }

    public void reposition(float x) {
        enemy.set(x, Ground.GROUND_HEIGHT);
        bounds.setPosition(enemy.x, enemy.y);
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
