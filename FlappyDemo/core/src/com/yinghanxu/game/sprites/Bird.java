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

public class Bird {
    private static final int GRAVITY = -30;
    private static final int MOVEMENT = 100;
    public static final int GROUND_Y_OFFSET = -100;
    private Vector3 position;
    private Vector3 velocity;
    private Rectangle bounds;
    private Animation birdAnimation;
    public Texture texture;
    private Sound flap;

    private Texture bird;
    private PlayState playState;

    public Vector3 getPosition() {
        return position;
    }

    public TextureRegion getTexture() {
        return birdAnimation.getFrame();
    }

    public Bird(int x, int y) {
        position = new Vector3(x, y, 0);
        velocity = new Vector3(0, 0, 0);
        //bird = new Texture("bird.png");
        texture = new Texture("player.png");
        birdAnimation = new Animation(new TextureRegion(texture), 3, 0.5f);
        bounds = new Rectangle(x, y, texture.getWidth() / 3, texture.getHeight());
        flap = Gdx.audio.newSound(Gdx.files.internal("sfx_wing.ogg"));
    }


    public void update(float dt) {
        birdAnimation.update(dt);
        if (position.y >= -GROUND_Y_OFFSET + (texture.getHeight()/2) + 40) {
            velocity.add(0, GRAVITY, 0);
            //position.y = -GROUND_Y_OFFSET + 10;
        }

        velocity.scl(dt);   //multiply everything by delta time---scale. Otherwise the bird will move too fast to see.
        position.add(MOVEMENT * dt, velocity.y, 0);
        if (position.y < 0) {
            position.y = 0;
        }
        velocity.scl(1 / dt);   //reverse the velocity
        bounds.setPosition(position.x, position.y);

        if (position.y < 256 ) {   // + (texture.getHeight()/2)
            position.y = 256;
            //velocity.add(0, -GRAVITY, 0);
            //position.y = -GROUND_Y_OFFSET;// + (texture.getHeight()/2);
        }
    }

    public void jump() {
        flap.play();    //set the 0.5 volumme
        velocity.y = 250;
        //velocity.x = 20; //we can c hange the x axes so the bird would fly ahead
    }

    public Rectangle getBounds() {
        return bounds;
    }

    public void dispose(){
        texture.dispose();
        flap.dispose();
    }
}
