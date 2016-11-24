package com.yinghanxu.game.States;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.yinghanxu.game.FlappyDemo;
import com.yinghanxu.game.sprites.Bird;
import com.yinghanxu.game.sprites.Ground;
import com.yinghanxu.game.sprites.Tube;

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

    private Array<Tube> tubes;
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

        tubes = new Array<Tube>();

//        for (int i = 1; i <= TUBE_COUNT; i++) {
//            tubes.add(new Tube(i * (TUBE_SPACING + Tube.TUBE_WIDTH)));
//        }
        tubes.add(new Tube(600 + rand.nextInt(400)));
        tubes.add(new Tube(ground2.getPosition().x + rand.nextInt(ground2.getLength())));

    }

    @Override
    protected void handleInput() {
        if (Gdx.input.isTouched(0)) {
            if (gameover)
                gsm.set(new PlayState(gsm));
            //touch left screen to jump & touch right screen to wave the sword
            else if(Gdx.input.getX() < Gdx.graphics.getWidth() / 2) //capture the screen touch's X position
                bird.jump();
        }
        //System.out.println("The coordinate you point = " + Gdx.input.getX());

    }
//    public boolean onTouchEvent()

    @Override
    public void update(float dt) {
        handleInput();
        update_Ground_Bird();

        bird.update(dt);
        //System.out.println(bird.getPosition().x);

        cam.position.x = bird.getPosition().x + (cam.viewportWidth / 2) - 50;     //set our camera's position with the flying bird
        for (int i = 0; i < tubes.size; i++) {
            Tube tube = tubes.get(i);
            //if the tube is left out of the screen then we're going to execute this
//            if (cam.position.x - cam.viewportWidth > tube.getPosTopTube().x + tube.getTopTube().getWidth()) {
////                System.out.println("cam = " + (cam.position.x - (cam.viewportWidth / 2)) + 50);
////                System.out.println("bird = " + bird.getPosition().x);
//                tube.reposition(tube.getPosTopTube().x + ((Tube.TUBE_WIDTH + TUBE_SPACING) * TUBE_COUNT));
//            }
            if (bird.getPosition().x >=  tube.getPosTopTube().x + tube.getTopTube().getWidth()) {
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
                        break;
                    case 1:
                        //Make sure the new ally is not relocated in a short ground
                        if (nextGround.getLength() > 500) {
                            tube.reposition(nextGround.getPosition().x + 100 + rand.nextInt(nextGround.getLength() - 100 - tube.getTopTube().getWidth()));
                        }
                        break;
                }
            }
            if (tube.collides(bird.getBounds())) {
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

        for (Tube tube : tubes) {
            sb.draw(tube.getTopTube(), tube.getPosTopTube().x, tube.getPosTopTube().y);
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
//        ground1.dispose();
//        ground2.dispose();
//        ground3.dispose();
//        ground4.dispose();
//        ground5.dispose();
        bird.dispose();
        for (Tube tube : tubes) {
            tube.dispose();
        }
        System.out.println("Play State Disposed");
    }

    private void update_Ground_Bird() {

//        if (bird.getPosition().y == 500) {
//            bird.run();
//        }

        float birdPositionX = bird.getPosition().x + (bird.getTexture().getRegionWidth()/14);
        //Making sure the player would not fall down on the specific ground by setting the player's height
//        for (Ground ground : grounds) {
//            if (birdPositionX > ground.getPosition().x) {
//                bird.setGroundHeight(ground.getHeight());
//
//                currentGroundNum =
//            }
//        }
        if (birdPositionX> ground1.getPosition().x) {
            bird.setGroundHeight(ground1.getHeight());
            //Record which ground the player is staying on.
            currentGroundNum = 1;
        }
        if (birdPositionX > ground2.getPosition().x) {
            bird.setGroundHeight(ground2.getHeight());
            currentGroundNum = 2;
        }
        if (bird.getPosition().x + (bird.getTexture().getRegionWidth()/14) > ground3.getPosition().x) {
            bird.setGroundHeight(ground3.getHeight());
            currentGroundNum = 3;
        }
        if (bird.getPosition().x + (bird.getTexture().getRegionWidth()/14) > ground4.getPosition().x) {
            bird.setGroundHeight(ground4.getHeight());
            currentGroundNum = 4;
        }
        if (bird.getPosition().x + (bird.getTexture().getRegionWidth()/14) > ground5.getPosition().x) {
            bird.setGroundHeight(ground5.getHeight());
            currentGroundNum = 5;
        }

        //If the player has pass the current ground, we create the new ground (by computing the whole ground length which includes the ground's gap)
        if (bird.getPosition().x + (bird.getTexture().getRegionWidth()/14) > ground1.getPosition().x + ground1.getLength()) {
            System.out.println("=========  create new ground1  ============");
            System.out.println("cam = " + (cam.position.x - (cam.viewportWidth / 2)));
            System.out.println("bird = " + bird.getPosition().x);
            if (bird.getPosition().y < ground1.getHeight()) {
                gameover = true;
                bird.colliding = true;
                bird.status = 3;
            }
            bird.setGroundHeight(0);
            System.out.println("new length of ground1 = " + ground1.getLength());
        }
        if (bird.getPosition().x - (bird.getTexture().getRegionWidth()/14) > ground1.getPosition().x + ground1.getLength()) {
            ground1.setPosition(ground5.getPosition().x - ground1.getPosition().x + ground5.getLength() + ground5.getGroundGap(), 0, 0);
            ground1.reposition();
        }

        if (bird.getPosition().x + (bird.getTexture().getRegionWidth()/14) > ground2.getPosition().x + ground2.getLength()) {
            System.out.println("=========  create new ground2  ============");
            if (bird.getPosition().y < ground1.getHeight()) {
                gameover = true;
                bird.colliding = true;
                bird.status = 3;
            }
            bird.setGroundHeight(0);
            System.out.println("new length of ground2 = " + ground2.getLength());
            //if (cam.position.x - (cam.viewportWidth / 2) > groundPos2.x + ground2.getWidth()) {
            //    groundPos2.add(ground2.getWidth() * 2, 0);
            //}
        }
        if (bird.getPosition().x - (bird.getTexture().getRegionWidth()/14) > ground2.getPosition().x + ground2.getLength()) {
            ground2.setPosition(ground1.getPosition().x - ground2.getPosition().x + ground1.getLength() + ground1.getGroundGap(), 0, 0);
            ground2.reposition();
        }

        if (bird.getPosition().x + (bird.getTexture().getRegionWidth()/14) > ground3.getPosition().x + ground3.getLength()) {
            System.out.println("=========  create new ground3  ============");
            if (bird.getPosition().y < ground1.getHeight()) {
                gameover = true;
                bird.colliding = true;
                bird.status = 3;
            }
            bird.setGroundHeight(0);
            System.out.println("new length of ground1 = " + ground3.getLength());
        }
        if (bird.getPosition().x - (bird.getTexture().getRegionWidth()/14) > ground3.getPosition().x + ground3.getLength()) {
            ground3.setPosition(ground2.getPosition().x - ground3.getPosition().x + ground2.getLength() + ground2.getGroundGap(), 0, 0);
            ground3.reposition();
        }

        if (bird.getPosition().x + (bird.getTexture().getRegionWidth()/14) > ground4.getPosition().x + ground4.getLength()) {
            System.out.println("=========  create new ground4  ============");
            if (bird.getPosition().y < ground1.getHeight()) {
                gameover = true;
                bird.colliding = true;
                bird.status = 3;
            }
            bird.setGroundHeight(0);
            System.out.println("new length of ground4 = " + ground4.getLength());
        }
        if (bird.getPosition().x - (bird.getTexture().getRegionWidth()/14) > ground4.getPosition().x + ground4.getLength()) {
            ground4.setPosition(ground3.getPosition().x - ground4.getPosition().x + ground3.getLength() + ground3.getGroundGap(), 0, 0);
            ground4.reposition();
        }

        if (bird.getPosition().x + (bird.getTexture().getRegionWidth()/14) > ground5.getPosition().x + ground5.getLength()) {
            System.out.println("=========  create new ground5  ============");
            if (bird.getPosition().y < ground1.getHeight()) {
                gameover = true;
                bird.colliding = true;
                bird.status = 3;
            }
            bird.setGroundHeight(0);
            System.out.println("new length of ground1 = " + ground5.getLength());
        }
        if (bird.getPosition().x - (bird.getTexture().getRegionWidth()/14) > ground5.getPosition().x + ground5.getLength()) {
            ground5.setPosition(ground4.getPosition().x - ground5.getPosition().x + ground4.getLength() + ground4.getGroundGap(), 0, 0);
            ground5.reposition();
        }
    }

}

