package com.yinghanxu.game.sprites;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

import java.util.Random;

/**
 * Created by 英瀚 on 2016/11/15.
 */

public class Ground {
    private Vector3 position;
    private Texture texture;

    private Random rand;
    private int GROUND_MAX_GAP = 200;
    private int GROUND_MAX_LENGTH = 1000;
    private int GROUND_MAX_HEIGHT = 30;

    public static final int GROUND_HEIGHT = 700;

    private Texture ground1, ground2, ground3, ground4;
    //ground1 is the first ground texture, ground2 is the second ground texture
    //otherwise, there would just be half of the background has the ground
    private Vector2 groundPos1, groundPos2, groundPos3, groundPos4;

    private int length = 0, height = 0, groundGap = 100;

    public Ground(float x, float y) {
        rand = new Random();
        position = new Vector3(x, y, 0);
        length = 400 + rand.nextInt(GROUND_MAX_LENGTH);
        height = 700;
        groundGap = 200 + rand.nextInt(GROUND_MAX_GAP);
    }

    public Vector3 getPosition(){ return position;}

    public void setPosition(float x, float y, float z) {
        position.x += x;
        position.y += y;
        position.z += z;
    }

    public void reposition(){
        length = 700 + rand.nextInt(800);
        groundGap = 100 + rand.nextInt(GROUND_MAX_GAP);
        switch (rand.nextInt(6)) {
            case 1:
                texture = new Texture("groundBlue.png");
                break;
            case 2:
                texture = new Texture("groundPink.png");
                break;
            case 3:
                texture = new Texture("groundGrey.png");
                break;
            case 4:
                texture = new Texture("groundPurple.png");
                break;
            case 5:
                texture = new Texture("groundGreen.png");
                break;
        }

    }

    public Texture getTexture(){ return texture;}

    public int getLength(){ return length;}

    public int getGroundGap() { return groundGap;}

    public void setGroundGap(int groundGap) { this.groundGap = groundGap;}

    public void setLength(int length){ this.length = length;}

    public int getHeight(){ return height;}

    public void setTexture(String image){ texture = new Texture(image);}

    public void dispose(){
        texture.dispose();
    }
}
