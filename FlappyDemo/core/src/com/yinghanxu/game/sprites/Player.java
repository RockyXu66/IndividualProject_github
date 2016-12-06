package com.yinghanxu.game.sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.yinghanxu.game.States.PlayState;

/**
 * Created by 英瀚 on 2016/11/9.
 */

public class Player {
    private static final int GRAVITY = -40;
    private static final int MOVEMENT = 500;
    public static final int GROUND_Y_OFFSET = -100;
    private Vector3 position;
    private Vector3 velocity;
    private Rectangle bounds;
    private Animation playerAnimationRun;
    private Animation playerAnimationJump;
    private Animation playerAnimationCollide;
    public Texture texture, textureJump, textureCollide;
    private int jumpFrameNum = 20, runFrameNum = 20;
    private Sound flap;
    private int groundHeight = 700;
    public int touchCount = 1;

    private Texture player;
    private PlayState playState;
    public int status = 1; //1 means runing; 2 means jumping; 3 means colliding;
    private Ground ground;
    public boolean colliding;
    public Vector3 getPosition() {
        return position;
    }

    public void setPositionY(float y) {
        //position.x = positionXYZ.x;
        position.y = y;
        //positionXYZ.z = positionXYZ.z;
    }

    public TextureRegion getTexture() {
        if (status == 1) {
            touchCount = 1;
            return playerAnimationRun.getFrame();
        } else if (status == 2) {
            return playerAnimationJump.getFrame();
        }
//        } else if (status == 3) {
//            return playerAnimationCollide.getFrame();
//        }
        return playerAnimationRun.getFrame();
    }

    public Texture getCollideTexture(){
        return textureCollide;
    }

    public Player(int x, int y) {
        position = new Vector3(x, y, 0);
        velocity = new Vector3(0, 0, 0);
        //player = new Texture("player.png");
        texture = new Texture("player.png");
        textureJump = new Texture("1130jump.png");
        textureCollide = new Texture("die.png");
        playerAnimationRun = new Animation(new TextureRegion(texture), runFrameNum, 0.5f);
        playerAnimationJump = new Animation(new TextureRegion(textureJump), jumpFrameNum, 0.8f);
        playerAnimationCollide = new Animation(new TextureRegion(texture), runFrameNum, 0.1f);
        bounds = new Rectangle(x, y, texture.getWidth() / runFrameNum , texture.getHeight());
        flap = Gdx.audio.newSound(Gdx.files.internal("sfx_wing.ogg"));
        colliding = false;
    }


    public void update(float dt) {
        if (status == 1) {
            playerAnimationRun.update(dt);
        } else if (status == 2) {
            playerAnimationJump.update(dt);
        } else if (status ==3) {
            playerAnimationCollide.update(dt);
        }

        velocity.add(0, GRAVITY, 0);

        velocity.scl(dt);   //multiply everything by delta time---scale. Otherwise the player will move too fast to see.
        if (!colliding) {
            position.add(MOVEMENT * dt, velocity.y, 0);
        }

        if (position.y < 0) {
            position.y = 0;
        }
        velocity.scl(1 / dt);   //reverse the velocity
        bounds.setPosition(position.x, position.y);

        if (position.y < groundHeight ) {   // + (texture.getHeight()/2)
            position.y = 700;
        }

        if (position.y == groundHeight) {
            status = 1;
        }
    }

    public void jump() {
        //flap.play();    //set the 0.5 volumme
        velocity.y = 1200;
        status = 2;
        playerAnimationJump = new Animation(new TextureRegion(textureJump), jumpFrameNum, 0.8f);
        //velocity.x = 20; //we can change the x axes so the player would fly ahead
    }

    public int getStatus(){ return status;}

    public Rectangle getBounds() {
        return bounds;
    }

    public void setGroundHeight(int groundHeight) { this.groundHeight = groundHeight; }

    public float getGroundHeight() { return groundHeight;}

    public void setTouchCount(int i) {this.touchCount = i; }

    public int getTouchCount() { return touchCount; }

    public void dispose(){
        texture.dispose();
        textureJump.dispose();
        textureCollide.dispose();
        flap.dispose();
    }
}
