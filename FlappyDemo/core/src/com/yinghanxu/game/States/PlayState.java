package com.yinghanxu.game.States;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.yinghanxu.game.FlappyDemo;
import com.yinghanxu.game.sprites.Bird;
import com.yinghanxu.game.sprites.Tube;

import java.util.Random;

/**
 * Created by 英瀚 on 2016/11/8.
 */

public class PlayState extends State {
    private static final int TUBE_SPACING = 125;    //the spacing from one tube to another tube
    private static final int TUBE_COUNT = 4;        //the tubes' number
    //public static final int GROUND_Y_OFFSET = -50; //to make sure the ground is not too high
    private Random rand;
    private int GROUND_MAX_GAP = 100;

    public Bird bird;
    //private Texture bg;
    private Texture ground1, ground2, ground3, ground4;
    //ground1 is the first ground texture, ground2 is the second ground texture
    //otherwise, there would just be half of the background has the ground
    private Vector2 groundPos1, groundPos2, groundPos3, groundPos4;

    private Array<Tube> tubes;

    protected PlayState(GameStateManager gsm) {
        super(gsm);
        cam.setToOrtho(false, FlappyDemo.WIDTH , FlappyDemo.HEIGHT ); //zoomed up the screen view in order to see the bird clearly
        //bg = new Texture("bgWhite.png");
        rand = new Random();
        ground1 = new Texture("groundBlue.png");
        ground2 = new Texture("groundPink.png");
        ground3 = null;
        groundPos1 = new Vector2(cam.position.x - (cam.viewportWidth/2) - 50 , 0);
        groundPos2 = new Vector2(cam.position.x - (cam.viewportWidth/2) -50  + ground1.getWidth() + rand.nextInt(GROUND_MAX_GAP), 0);
        bird = new Bird( 0, ground2.getHeight() );

        tubes = new Array<Tube>();

        for (int i = 1; i <= TUBE_COUNT; i++) {
            tubes.add(new Tube(i * (TUBE_SPACING + Tube.TUBE_WIDTH)));
        }
    }

    @Override
    protected void handleInput() {
        if (Gdx.input.justTouched()) {
            bird.jump();
        }
    }

    @Override
    public void update(float dt) {
        handleInput();
        updateGround();
        bird.update(dt);
        //System.out.println(bird.getPosition().x);

        cam.position.x = bird.getPosition().x + (cam.viewportWidth / 2) - 50;     //set our camera's position with the flying bird
        for (int i = 0; i < tubes.size; i++) {
            Tube tube = tubes.get(i);
            //if the tube is left out of the screen then we're going to execute this
            if (cam.position.x - cam.viewportWidth  > tube.getPosTopTube().x + tube.getTopTube().getWidth()) {
                tube.reposition(tube.getPosTopTube().x + ((Tube.TUBE_WIDTH + TUBE_SPACING) * TUBE_COUNT));
            }
            if (tube.collides(bird.getBounds())) {
                gsm.set(new PlayState(gsm));
            }
        }
//        if (bird.getPosition().y <= ground.getHeight() + GROUND_Y_OFFSET) {
//            gsm.set(new PlayState(gsm));
//        }
        cam.update();   //this will tell lib gdx the camera has been repositioned
    }

    @Override
    public void render(SpriteBatch sb) {
        sb.setProjectionMatrix(cam.combined); //put the camera in the world view
        sb.begin(); //open rendering box off
        //sb.draw(bg, cam.position.x - (cam.viewportWidth / 2), 0);
        sb.draw(bird.getTexture(), bird.getPosition().x, bird.getPosition().y);
        for (Tube tube : tubes) {
            sb.draw(tube.getTopTube(), tube.getPosTopTube().x, tube.getPosTopTube().y);
            sb.draw(tube.getBottomTube(), tube.getPosBottomTube().x, tube.getPosBottomTube().y);
        }
        sb.draw(ground1, groundPos1.x, groundPos1.y);
        sb.draw(ground2, groundPos2.x, groundPos2.y);
        sb.end();
    }

    @Override
    public void dispose() {
        ground1.dispose();
        ground2.dispose();
        ground3.dispose();
        //bg.dispose();
        bird.dispose();
        for (Tube tube : tubes) {
            tube.dispose();
        }
        System.out.println("Play State Disposed");
    }

    private void updateGround(){
        //System.out.println("cam.x = " + cam.position.x);
        //System.out.println("groundPos1.x = " + groundPos1.x);
        if (ground3 == null) {
            if (cam.position.x - (cam.viewportWidth / 2) > groundPos1.x + (ground1.getWidth()*2)-cam.viewportWidth) {
                ground3 = ground1;
                groundPos3 = groundPos1;
                System.out.println("ground3.x = " + groundPos3.x);
                groundPos3.add(ground1.getWidth() * 2, 0);
            }
        }

        if (cam.position.x - (cam.viewportWidth / 2) > groundPos2.x + ground2.getWidth()) {
            groundPos2.add(ground2.getWidth() * 2, 0);
        }
    }
}
