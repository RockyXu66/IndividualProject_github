package com.yinghanxu.game.States;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.yinghanxu.game.FlappyDemo;
import com.yinghanxu.game.sprites.Ally;
import com.yinghanxu.game.sprites.Player;
import com.yinghanxu.game.sprites.Ground;

import java.util.Random;

/**
 * Created by 英瀚 on 2016/11/8.
 */

public class PlayState extends State{
    private Random rand;

    public Player player;

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

    //load the font
    BitmapFont font;
    String myText;
    float fontPosWidth = 0;

    protected PlayState(GameStateManager gsm) {
        super(gsm);
        cam.setToOrtho(false, FlappyDemo.WIDTH, FlappyDemo.HEIGHT); //zoomed up the screen view in order to see the player clearly
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
        player = new Player(0, ground1.getHeight());
        birdPosition = new Vector3(player.getPosition().x, ground1.getHeight(), 0);
        gameover = false;
        gameoverImg = new Texture("gameover.png");

        allies = new Array<Ally>();

        allies.add(new Ally(600 + rand.nextInt(400)));
        allies.add(new Ally(ground2.getPosition().x + rand.nextInt(ground2.getLength())));

        //load the font
        font = new BitmapFont(Gdx.files.internal("whitefont.fnt"));
        font.getData().setScale(4,3);
        font.setColor(Color.BLACK);
        myText = "0";
    }

    @Override
    protected void handleInput() {
        if (Gdx.input.justTouched()) {
            if (gameover) {
                gsm.set(new PlayState(gsm));
            }
            //touch left screen to jump & touch right screen to wave the sword
            else{
                if (Gdx.input.getX() < Gdx.graphics.getWidth() / 2 && player.getTouchCount() ==1) {//capture the screen touch's X position
                    player.jump();
                    player.setTouchCount(2);
                }
                else if(Gdx.input.getX() < Gdx.graphics.getWidth() / 2 && player.getTouchCount() ==2){
                    player.jump();
                    player.setTouchCount(3);
                }
            }
        }

    }

    @Override
    public void update(float dt) {
        handleInput();
        update_Ground_Bird();

        player.update(dt);
        //System.out.println(player.getPosition().x);

        fontPosWidth = cam.position.x + cam.viewportWidth - 50;

        cam.position.x = player.getPosition().x + (cam.viewportWidth / 2) - 50;     //set our camera's position with the flying player
        for (int i = 0; i < allies.size; i++) {
            Ally ally = allies.get(i);
            ally.update(dt);
            //if the ally runs out of the screen then we're going to execute this
            if (player.getPosition().x >=  ally.getPosAlly().x + ally.getAlly().getWidth()) {
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
                            ally.reposition(nextGround.getPosition().x + 100 + rand.nextInt(nextGround.getLength() - 100 - ally.getAlly().getWidth()/20));
                        }
                        break;
                }
            }
            //Check the collision between the blue ally with the player
            if (ally.collides(player.getBounds())) {
//                gsm.set(new PlayState(gsm));
                player.status = 3;
                gameover = true;
                player.colliding = true;
            }
        }
        cam.update();   //this will tell lib gdx the camera has been repositioned
        myText = String.valueOf((int)(player.getPosition().x/50));
    }

    @Override
    public void render(SpriteBatch sb) {
        sb.setProjectionMatrix(cam.combined); //put the camera in the world view
        sb.begin(); //open rendering box off

        sb.draw(player.getTexture(), player.getPosition().x, player.getPosition().y);

        for (Ally ally : allies) {
            sb.draw(ally.getTexture(), ally.getPosAlly().x, ally.getPosAlly().y);
        }

        //draw the grounds in the screen
        for(Ground ground : grounds) {
            sb.draw(ground.getTexture(), ground.getPosition().x, ground.getPosition().y, ground.getLength(), ground.getHeight());
        }
        if (gameover) {
            sb.draw(gameoverImg, cam.position.x - gameoverImg.getWidth() / 2, cam.position.y);

        }
        //draw the score in the screen
        font.draw(sb, myText, fontPosWidth , cam.viewportHeight - 50);
        sb.end();
    }

    @Override
    public void dispose() {
        //Dispose the grounds
        for (Ground ground : grounds) {
            ground.dispose();
        }
        player.dispose();
        for (Ally ally : allies) {
            ally.dispose();
        }
        System.out.println("Play State Disposed");
    }

    private void update_Ground_Bird() {

        //birdPositionX is the right border's position of the player
        float birdPositionX = player.getPosition().x + (player.getTexture().getRegionWidth()/playerFrameNum);

        //When the player stand on the new ground, setting the new player's ground height
        if (birdPositionX >= ground1.getPosition().x) {
            player.setGroundHeight(ground1.getHeight());
            //Record which ground the player is staying on.
            currentGroundNum = 1;
        }
        if (birdPositionX >= ground2.getPosition().x) {
            System.out.print("birdPositionX = " + birdPositionX);
            System.out.print("ground2.getPosition().x = " + ground2.getPosition().x);
            player.setGroundHeight(ground2.getHeight());
            currentGroundNum = 2;
        }
        if (birdPositionX >= ground3.getPosition().x) {
            player.setGroundHeight(ground3.getHeight());
            currentGroundNum = 3;
        }
        if (birdPositionX >= ground4.getPosition().x) {
            player.setGroundHeight(ground4.getHeight());
            currentGroundNum = 4;
        }
        if (birdPositionX >= ground5.getPosition().x) {
            player.setGroundHeight(ground5.getHeight());
            currentGroundNum = 5;
        }

        //Making sure the player would not fall down on the specific ground by setting the player's height
        for(Ground ground: grounds){
            if (birdPositionX > ground.getPosition().x + ground.getLength()) {
                //player.setPositionY(0);
                player.setGroundHeight(0);
                if (player.getPosition().y < ground.getHeight()) {
                    gameover = true;
                    player.colliding = true;
                    player.status = 3;    //3 means collision status
                    //player.setGroundHeight(0);    //if the player fall down, set the player's ground height 0
                }
            }
        }

        //If the player has pass the current ground, we create the new ground (by computing the whole ground length which includes the ground's gap)
        if (player.getPosition().x - (player.getTexture().getRegionWidth()/playerFrameNum) > ground1.getPosition().x + ground1.getLength()) {
            ground1.setPosition(ground5.getPosition().x - ground1.getPosition().x + ground5.getLength() + ground5.getGroundGap(), 0, 0);
            ground1.reposition();
        }
        if (player.getPosition().x - (player.getTexture().getRegionWidth()/playerFrameNum) > ground2.getPosition().x + ground2.getLength()) {
            ground2.setPosition(ground1.getPosition().x - ground2.getPosition().x + ground1.getLength() + ground1.getGroundGap(), 0, 0);
            ground2.reposition();
        }
        if (player.getPosition().x - (player.getTexture().getRegionWidth()/playerFrameNum) > ground3.getPosition().x + ground3.getLength()) {
            ground3.setPosition(ground2.getPosition().x - ground3.getPosition().x + ground2.getLength() + ground2.getGroundGap(), 0, 0);
            ground3.reposition();
        }
        if (player.getPosition().x - (player.getTexture().getRegionWidth()/playerFrameNum) > ground4.getPosition().x + ground4.getLength()) {
            ground4.setPosition(ground3.getPosition().x - ground4.getPosition().x + ground3.getLength() + ground3.getGroundGap(), 0, 0);
            ground4.reposition();
        }
        if (player.getPosition().x - (player.getTexture().getRegionWidth()/playerFrameNum) > ground5.getPosition().x + ground5.getLength()) {
            ground5.setPosition(ground4.getPosition().x - ground5.getPosition().x + ground4.getLength() + ground4.getGroundGap(), 0, 0);
            ground5.reposition();
        }

    }

}

