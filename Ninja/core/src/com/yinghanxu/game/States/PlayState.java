package com.yinghanxu.game.States;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.yinghanxu.game.Ninja;
import com.yinghanxu.game.sprites.Ally;
import com.yinghanxu.game.sprites.Enemy;
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
    private Array<Enemy> enemies;
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
    private int waveTime = 0;
    private boolean waveStatus = false;

    Vector3 birdPosition;

    //load the font
    BitmapFont font1, fontDistance, font2, fontScore;
    String myText1, myDistance, myText2, myScore;
    float font1PosX = 0,fontDistancePosX = 0, font2PosX = 0, fontScorePosX = 0;
    int score = 0; //The score is used to calculate the number that the player kills the enmey

    protected PlayState(GameStateManager gsm) {
        super(gsm);
        cam.setToOrtho(false, Ninja.WIDTH, Ninja.HEIGHT); //zoomed up the screen view in order to see the player clearly
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
        enemies = new Array<Enemy>();

        allies.add(new Ally(600 + rand.nextInt(400)));
        allies.add(new Ally(ground2.getPosition().x + rand.nextInt(ground2.getLength())));
        enemies.add(new Enemy(700 + rand.nextInt()));
        enemies.add(new Enemy(ground2.getPosition().x + rand.nextInt(ground2.getLength())));

        //load the font
        font1 = new BitmapFont(Gdx.files.internal("whitefont.fnt"));
        font1.getData().setScale(2, 2);
        font1.setColor(Color.BLACK);
        myText1 = "Distance:";
        fontDistance = new BitmapFont(Gdx.files.internal("whitefont.fnt"));
        fontDistance.getData().setScale(4,3);
        fontDistance.setColor(Color.BLACK);
        myDistance = "0";
        font2 = new BitmapFont(Gdx.files.internal("whitefont.fnt"));
        font2.getData().setScale(2, 2);
        font2.setColor(Color.BLACK);
        myText2 = "Score:";
        fontScore = new BitmapFont(Gdx.files.internal("whitefont.fnt"));
        fontScore.getData().setScale(4, 3);
        fontScore.setColor(Color.BLACK);
        myScore = "0";
    }

    @Override
    protected void handleInput() {
        if (Gdx.input.justTouched()) {
            if (gameover) {
                gsm.set(new PlayState(gsm));
            }
            //touch left screen to jump & touch right screen to wave the sword
            else{
//                if (player.getWaveStatus() == 0) {
                    if (Gdx.input.getX() < Gdx.graphics.getWidth() / 2 && player.getTouchCount() == 1) {//capture the screen touch's X position
                        player.jump();
                        player.setTouchCount(2);
                    } else if (Gdx.input.getX() < Gdx.graphics.getWidth() / 2 && player.getTouchCount() == 2) {
                        player.jump();
                        player.setTouchCount(3);
                    }
//                }
                if (Gdx.input.getX() > Gdx.graphics.getWidth() / 2) {
                    player.wave();
                }
            }
        }

    }

@Override
public void update(float dt) {
        handleInput();
        update_Ground();

        player.update(dt);
        //System.out.println(player.getPosition().x);

        //set the font x position in the screen
        font1PosX = player.getPosition().x + cam.viewportWidth - 600;
        fontDistancePosX = player.getPosition().x + cam.viewportWidth -260;
        font2PosX = player.getPosition().x + cam.viewportWidth - 600;
        fontScorePosX = player.getPosition().x + cam.viewportWidth -260;

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
            if (ally.collides(player.getBounds())) {
//                gsm.set(new PlayState(gsm));
                player.status = 3;
                ally.status = 1;
                gameover = true;
                player.colliding = true;
            }

        }


        for (int i = 0; i < enemies.size; i++) {
            Enemy enemy = enemies.get(i);
            enemy.update(dt);
            //if the enemy runs out of the screen then we're going to execute this
            if (player.getPosition().x >=  enemy.getPosEnemy().x + enemy.getEnemy().getWidth()) {
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
                        //Make sure the new enemy is not relocated in a short ground
                        if (nextGround.getLength() > 500) {
                            float X = nextGround.getPosition().x + 100 +
                                    rand.nextInt(nextGround.getLength() - 100 - enemy.getEnemy().getWidth()/20);
                            for (Ally ally : allies) {
                                if (ally.getPosAlly().x - 200 > X || ally.getPosAlly().x +
                                        ally.getTexture().getRegionWidth() / playerFrameNum + 200 < X) {
                                    enemy.reposition(X);
                                }
                            }
                        }
                        break;
                }
            }
            //Check the collision between the blue ally with the player

            //Check whether the enemy have waved the sword and killed the player
//            if (enemy.collides((player.getBounds()))) {
//                if(enemy.status!= 3 ) {
//                    //enemy.status = 2;   //waving the sword;
//                    player.status = 3;
//                    enemy.status = 1;   //Aftering killing the player, the enemy changes the animation to running
//                    gameover = true;
//                    player.colliding = true;
//                }
//
//            }
            //System.out.println("enemyX = " + enemy.getPosEnemy().x + "; playerX = " + cam.position.x);
            //This is the distance which is used to determine whether the player kills the enemy;
            if(player.status == 4){ // status 4 means the player is waving the sword
                if (java.lang.Math.abs(enemy.getPosEnemy().x -
                        (player.getPosition().x + (player.getTexture().getRegionWidth() / 7))) < 150 && enemy.status != 3) {
            //        if (player.status == 4) {
                        enemy.status = 3; // status 3 means the enemy is dead.
                        score++;
            //        }
                }
            }

            //This is the distance which is used to determine whether the enemy should wave the sword and kill the player;
            if(!waveStatus){
                if (java.lang.Math.abs(enemy.getPosEnemy().x -
                        (player.getPosition().x + (player.getTexture().getRegionWidth() / 20))) < 140) {
                    if(enemy.status!= 3 ) {
                        player.status = 3; //status 3 means the player is dead
                        waveTime++;
                        if(waveTime % 15 ==0 ){
                            enemy.status = 1;
                            waveStatus = true;
                        }else {
                            enemy.status = 2; // the enemy wave the sword
                        }
                        //System.out.println("enemy status: " + enemy.status);
                        gameover = true;
                        player.colliding = true;

                    }
                }
            }else {
                player.status = 3;
            }


        }


        cam.update();   //this will tell lib gdx the camera has been repositioned

        //set the score value
        myDistance = String.valueOf((int)(player.getPosition().x/50));
        myScore = String.valueOf(score);
    }

    @Override
    public void render(SpriteBatch sb) {
        sb.setProjectionMatrix(cam.combined); //put the camera in the world view
        sb.begin(); //open rendering box off

        //draw the grounds in the screen
        for(Ground ground : grounds) {
            sb.draw(ground.getTexture(), ground.getPosition().x, ground.getPosition().y, ground.getLength(), ground.getHeight());
        }
        if (player.getStatus() == 1 || player.getStatus() == 2 || player.getStatus() == 4) {
            sb.draw(player.getTexture(), player.getPosition().x, player.getPosition().y);
        } else {
            sb.draw(player.getCollideTexture(), player.getPosition().x - 50, player.getGroundHeight() - 25);
        }

        for (Ally ally : allies) {
            if (ally.status == 1) {
                sb.draw(ally.getCollideTexture(), ally.getPosAlly().x + 50, ally.getPosAlly().y - 25);
            } else {
                sb.draw(ally.getTexture(), ally.getPosAlly().x, ally.getPosAlly().y);
            }
        }
        for (Enemy enemy : enemies) {
            if(enemy.status!=3) {
                sb.draw(enemy.getTexture(), enemy.getPosEnemy().x, enemy.getPosEnemy().y);
            }else {
                sb.draw(enemy.getCollideTexture(),enemy.getPosEnemy().x + 50, enemy.getPosEnemy().y - 25);
            }
        }

        if (gameover) {
            sb.draw(gameoverImg, cam.position.x - gameoverImg.getWidth() / 2, cam.position.y);

        }
        //render the score font and distance in the screen
        font1.draw(sb, myText1, font1PosX, cam.viewportHeight - 70);
        fontDistance.draw(sb, myDistance, fontDistancePosX , cam.viewportHeight - 50);
        font1.draw(sb, myText2, font1PosX, cam.viewportHeight - 170);
        fontDistance.draw(sb, myScore, fontScorePosX , cam.viewportHeight - 150);

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
        for (Enemy enemy : enemies) {
            enemy.dispose();
        }
        System.out.println("Play State Disposed");
    }

    private void update_Ground() {

        //birdPositionX is the right border's position of the player
        float birdPositionX = player.getPosition().x + (player.getTexture().getRegionWidth()/playerFrameNum);

        //When the player stand on the new ground, setting the new player's ground height
        int groundNum = 0;
        for (Ground ground : grounds) {
            groundNum ++;
            if (birdPositionX >= ground.getPosition().x) {
                player.setGroundHeight(ground.getHeight());
                currentGroundNum = groundNum;
            }
        }

        //Make sure the player would not fall down on the specific ground by setting the player's height
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

