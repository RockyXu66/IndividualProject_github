package com.yinghanxu.game.sprites;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.yinghanxu.game.FlappyDemo;

import java.util.Random;

/**
 * Created by 英瀚 on 2016/11/15.
 */

public class Ground {
    private Vector3 position;
    private Texture texture;

    private Random rand;
    private int GROUND_MAX_GAP = 30;
    private int GROUND_MAX_LENGTH = 1000;
    private int GROUND_MAX_HEIGHT = 30;

    private Texture ground1, ground2, ground3, ground4;
    //ground1 is the first ground texture, ground2 is the second ground texture
    //otherwise, there would just be half of the background has the ground
    private Vector2 groundPos1, groundPos2, groundPos3, groundPos4;

    private int length = 0, ground_length1 = 0, ground_length2 = 0, height = 0, ground_height2 = 0;

    public Ground(float x, float y) {
        rand = new Random();
        position = new Vector3(x, y, 0);
        //texture = new Texture("groundBlue.png");
        length = 1000;// + rand.nextInt(GROUND_MAX_LENGTH);
        height = 500;
        texture = new Texture("groundBlue.png");
    }

    public Vector3 getPosition(){ return position;}

    public void setPosition(Vector3 positionXYZ) {
        position.x += positionXYZ.x;
        positionXYZ.y += positionXYZ.y;
        positionXYZ.z += positionXYZ.z;
    }

    public void reposition(){

    }

    public Texture getTexture(){ return texture;}

    public int getLength(){ return length;}

    public void setLength(int length){ this.length = length;}

    public int getHeight(){ return height;}

    public void setTexture(String image){ texture = new Texture(image);}

    public void dispose(){
        texture.dispose();
    }
}
