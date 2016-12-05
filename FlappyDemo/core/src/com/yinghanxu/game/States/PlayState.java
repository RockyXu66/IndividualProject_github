package com.yinghanxu.game.States;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.yinghanxu.game.FlappyDemo;
import com.yinghanxu.game.sprites.Ally;
import com.yinghanxu.game.sprites.Bird;
import com.yinghanxu.game.sprites.Ground;

import java.util.Random;

/**
 * Created by 英瀚 on 2016/11/8.
 */

public class PlayState extends State{
    private static final int TUBE_SPACING = 125;    //the spacing from one tube to another tube
    private static final int TUBE_COUNT = 4;        //the tubes' number
    private static final int GROUND_COUNT = 5;
    //public static final int GROUND_Y_OFFSET = -50; //to make sure the ground is not too high
    private Random rand;

    public Bird bird;

    private Array<Ally> allies;
    private Ground ground1, ground2, ground3, ground4, ground5, ground6;
    private Array<Ground> grounds;

    private int allyCount = 0;
    private float allyPosition = 100;
    private int currentGroundNum = 1;
    private Ground nextGround;
    private Vector3 groundPos1, groundPos2, groundPos5;

    private float groundLength = 0;
    private boolean gameover;
    private Texture gameoverImg;
    private int playerFrameNum = 2;

    Vector3 birdPosition;

    protected PlayState(GameStateManager gsm) {
        super(gsm);
        cam.setToOrtho(false, FlappyDemo.WIDTH, FlappyDemo.HEIGHT); //zoomed up the screen view in order to see the bird clearly
        //bg = new Texture("bgWhite.png");
        rand = new Random();

        ground1 = new Ground(cam.position.x - (cam.viewportWidth / 2) - 50, 0);
        ground1.setLength(1500);
        ground1.setGroundGap(400);
        ground2 = new Ground(cam.position.x - (cam.viewportWidth / 2) - 50 + ground1.getLength() + ground1.getGroundGap(), 0);
        ground3 = new Ground(cam.position.x - (cam.viewportWidth / 2) - 50 + ground1.getLength()
                + ground2.getLength() + ground1.getGroundGap() + ground2.getGroundGap(), 0);
        ground4 = new Ground(cam.position.x - (cam.viewportWidth / 2) - 50 + ground1.getLength()
                + ground2.getLength() + ground3.getLength() + ground1.getGroundGap() + ground2.getGroundGap()
                + ground3.getGroundGap(), 0);
        ground5 = new Ground(cam.position.x - (cam.viewportWidth / 2) - 50 + ground1.getLength()
                + ground2.getLength() + ground3.getLength() + ground4.getLength() + ground1.getGroundGap()
                + ground2.getGroundGap() + ground3.getGroundGap() + ground4.getGroundGap(), 0);

        ground1.setTexture("groundBlue.png");
        ground2.setTexture("groundPink.png");
        ground3.setTexture("groundGreen.png");
        ground4.setTexture("groundPurple.png");
        ground5.setTexture("groundGrey.png");

        grounds = new Array<Ground>();
        grounds.add(ground1);
        grounds.add(ground2);
        grounds.add(ground3);
        grounds.add(ground4);
        grounds.add(ground5);

        nextGround = ground1;

        System.out.println("groundLength = " + groundLength);
        groundLength = ground1.getLength() + ground2.getLength() + ground3.getLength() + ground4.getLength() + ground5.getLength() - 50;
        System.out.println(ground1.getLength());
        bird = new Bird(0, ground1.getHeight());
        birdPosition = new Vector3(bird.getPosition().x, ground1.getHeight(), 0);
        gameover = false;
        gameoverImg = new Texture("gameover.png");
//      birdPosition.x = bird.getPosition().x;
//      birdPosition.y = ground1.getHeight();
//      birdPosition.z = 0;

        allies = new Array<Ally>();

//        for (int i = 1; i <= TUBE_COUNT; i++) {
//            allies.add(new Tube(i * (TUBE_SPACING + Tube.TUBE_WIDTH)));
//        }
        allies.add(new Ally(600 + rand.nextInt(400)));
        allies.add(new Ally(ground2.getPosition().x + rand.nextInt(ground2.getLength())));

    }

    @Override
    protected void handleInput() {
        if (Gdx.input.justTouched()) {
            if (gameover) {
                gsm.set(new PlayState(gsm));
            }
            //touch left screen to jump & touch right screen to wave the sword
            else{
                if (Gdx.input.getX() < Gdx.graphics.getWidth() / 2 && bird.getTouchCount() ==1) {//capture the screen touch's X position
                    bird.jump();
                    bird.setTouchCount(2);
                }
                else if(Gdx.input.getX() < Gdx.graphics.getWidth() / 2 && bird.getTouchCount() ==2){
                    bird.jump();
                    bird.setTouchCount(3);
                }
            }
        }

    }

    @Override
    public void update(float dt) {
        handleInput();
        update_Ground_Bird();

        bird.update(dt);
        //System.out.println(bird.getPosition().x);

        cam.position.x = bird.getPosition().x + (cam.viewportWidth / 2) - 50;     //set our camera's position with the flying bird
        for (int i = 0; i < allies.size; i++) {
            Ally ally = allies.get(i);
            ally.update(dt);
            //if the ally runs out of the screen then we're going to execute this
            if (bird.getPosition().x >=  ally.getPosTopTube().x + ally.getTopTube().getWidth()) {
                switch (currentGroundNum) {
                    case 1:
                        nextGround = ground3;
                        break;
                    case 2:
                        nextGround = ground4;
                        break;
                    case 3:
                        nextGround = ground5;
                        break;
                    case 4:
                        nextGround = ground1;
                        break;
                    case 5:
                        nextGround = ground2;
                        break;
                }
                switch (rand.nextInt(3)) {
                    case 0:

                    case 1:
                        //Make sure the new ally is not relocated in a short ground
                        if (nextGround.getLength() > 500) {
                            ally.reposition(nextGround.getPosition().x + 100 + rand.nextInt(nextGround.getLength() - 100 - ally.getTopTube().getWidth()/20));
                        }
                        break;
                }
            }
            //Check the collision between the blue ally with the player
            if (ally.collides(bird.getBounds())) {
//                gsm.set(new PlayState(gsm));
                bird.status = 3;
                gameover = true;
                bird.colliding = true;
            }
        }
        cam.update();   //this will tell lib gdx the camera has been repositioned
    }

    @Override
    public void render(SpriteBatch sb) {
        sb.setProjectionMatrix(cam.combined); //put the camera in the world view
        sb.begin(); //open rendering box off

        sb.draw(bird.getTexture(), bird.getPosition().x, bird.getPosition().y);

        for (Ally ally : allies) {
            sb.draw(ally.getTexture(), ally.getPosTopTube().x, ally.getPosTopTube().y);
//        sb.draw(tube.getBottomTube(), tube.getPosBottomTube().x, tube.getPosBottomTube().y);
        }

        //draw the gorunds in the screen
        for(Ground ground : grounds) {
            sb.draw(ground.getTexture(), ground.getPosition().x, ground.getPosition().y, ground.getLength(), ground.getHeight());
        }
        if (gameover) {
            sb.draw(gameoverImg, cam.position.x - gameoverImg.getWidth() / 2, cam.position.y);

        }
        sb.end();
    }

    @Override
    public void dispose() {
        //Dispose the grounds
        for (Ground ground : grounds) {
            ground.dispose();
        }
        bird.dispose();
        for (Ally ally : allies) {
            ally.dispose();
        }
        System.out.println("Play State Disposed");
    }

    private void update_Ground_Bird() {

        //birdPositionX is the right border's position of the player
        float birdPositionX = bird.getPosition().x + (bird.getTexture().getRegionWidth()/playerFrameNum);

        //When the player stand on the new ground, setting the new player's ground height
        if (birdPositionX >= ground1.getPosition().x) {
            bird.setGroundHeight(ground1.getHeight());
            //Record which ground the player is staying on.
            currentGroundNum = 1;
        }
        if (birdPositionX >= ground2.getPosition().x) {
            System.out.print("birdPositionX = " + birdPositionX);
            System.out.print("ground2.getPosition().x = " + ground2.getPosition().x);
            bird.setGroundHeight(ground2.getHeight());
            currentGroundNum = 2;
        }
        if (birdPositionX >= ground3.getPosition().x) {
            bird.setGroundHeight(ground3.getHeight());
            currentGroundNum = 3;
        }
        if (birdPositionX >= ground4.getPosition().x) {
            bird.setGroundHeight(ground4.getHeight());
            currentGroundNum = 4;
        }
        if (birdPositionX >= ground5.getPosition().x) {
            bird.setGroundHeight(ground5.getHeight());
            currentGroundNum = 5;
        }

        //Making sure the player would not fall down on the specific ground by setting the player's height
        for(Ground ground: grounds){
            if (birdPositionX > ground.getPosition().x + ground.getLength()) {
                //bird.setPositionY(0);
                bird.setGroundHeight(0);
                if (bird.getPosition().y < ground.getHeight()) {
                    gameover = true;
                    bird.colliding = true;
                    bird.status = 3;    //3 means collision status
                    //bird.setGroundHeight(0);    //if the player fall down, set the player's ground height 0
                }
            }
        }

        //If the player has pass the current ground, we create the new ground (by computing the whole ground length which includes the ground's gap)
        if (bird.getPosition().x - (bird.getTexture().getRegionWidth()/playerFrameNum) > ground1.getPosition().x + ground1.getLength()) {
            ground1.setPosition(ground5.getPosition().x - ground1.getPosition().x + ground5.getLength() + ground5.getGroundGap(), 0, 0);
            ground1.reposition();
        }
        if (bird.getPosition().x - (bird.getTexture().getRegionWidth()/playerFrameNum) > ground2.getPosition().x + ground2.getLength()) {
            ground2.setPosition(ground1.getPosition().x - ground2.getPosition().x + ground1.getLength() + ground1.getGroundGap(), 0, 0);
            ground2.reposition();
        }
        if (bird.getPosition().x - (bird.getTexture().getRegionWidth()/playerFrameNum) > ground3.getPosition().x + ground3.getLength()) {
            ground3.setPosition(ground2.getPosition().x - ground3.getPosition().x + ground2.getLength() + ground2.getGroundGap(), 0, 0);
            ground3.reposition();
        }
        if (bird.getPosition().x - (bird.getTexture().getRegionWidth()/playerFrameNum) > ground4.getPosition().x + ground4.getLength()) {
            ground4.setPosition(ground3.getPosition().x - ground4.getPosition().x + ground3.getLength() + ground3.getGroundGap(), 0, 0);
            ground4.reposition();
        }
        if (bird.getPosition().x - (bird.getTexture().getRegionWidth()/playerFrameNum) > ground5.getPosition().x + ground5.getLength()) {
            ground5.setPosition(ground4.getPosition().x - ground5.getPosition().x + ground4.getLength() + ground4.getGroundGap(), 0, 0);
            ground5.reposition();
        }

    }

}

