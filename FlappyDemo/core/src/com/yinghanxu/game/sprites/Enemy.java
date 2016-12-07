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
    private Texture texture,textureWave, textureCollide;
    private Animation enemyAnimationRun, enemyAnimationWave;
    private int runFrameNum = 20, waveFrameNum = 20;
    public int status = 1; //1 means runing; 2 means waving the sword; 3 means die;

    public Enemy(float x) {

        enemy = new Vector2(x, Ground.GROUND_HEIGHT);
        texture = new Texture("ally.png");
        textureWave = new Texture("birdanimation.png");
        textureCollide = new Texture("allyDie.png");
        enemyAnimationRun = new Animation(new TextureRegion(texture), runFrameNum, 0.5f);
        enemyAnimationWave = new Animation(new TextureRegion(texture), waveFrameNum, 0.1f);

        bounds = new Rectangle(enemy.x, enemy.y, texture.getWidth() / runFrameNum , texture.getHeight()); //use the 4 parameters' method, x, y, width, height

    }

    public void update(float dt) {
        if (status == 1 || status ==3) {
            enemyAnimationRun.update(dt);
        } else {
            enemyAnimationWave.update(dt);
        }


    }
    public Texture getEnemy() {
        return texture;
    }

    public TextureRegion getTexture() {
        if (status == 1 || status ==3 ) {
            return enemyAnimationRun.getFrame();
        } else {
            return enemyAnimationWave.getFrame();
        }
        //return enemyAnimationRun.getFrame();
    }

    public Vector2 getPosEnemy() {
        return enemy;
    }

    public void reposition(float x) {
        enemy.set(x, Ground.GROUND_HEIGHT);
        bounds.setPosition(enemy.x, enemy.y);
        status = 1;
    }

    public boolean collides(Rectangle player) {
        return player.overlaps(bounds);
    }

    public void dispose(){
        texture.dispose();
        textureWave.dispose();
    }

    public Texture getCollideTexture() {
        return textureCollide;
    }
}
