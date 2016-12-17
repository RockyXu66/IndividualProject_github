package com.yinghanxu.game.States;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.yinghanxu.game.Ninja;

/**
 * Created by 英瀚 on 2016/11/8.
 */

public class MenuState extends State {
    private Texture background;
    private Texture playBtn;
    public MenuState(GameStateManager gsm) {
        super(gsm);
        cam.setToOrtho(false, Ninja.WIDTH / 2, Ninja.HEIGHT / 2);
        //background = new Texture("bg.png");
        playBtn = new Texture("start.png");
    }

    @Override
    public void handleInput() {
        //If there is any finger touch or such kind of thing
        if (Gdx.input.justTouched()) {
            gsm.set(new PlayState(gsm));
        }
    }

    @Override
    public void update(float dt) {
        handleInput();  //handleInput would always stay in here (the first place in update) to check if the user do anything new
    }

    @Override
    public void render(SpriteBatch sb) {
        sb.setProjectionMatrix(cam.combined);
        sb.begin();
        //sb.draw(background, 0, 0  ); //background, x-axes-start, y-axes-start, width, height
        //sb.draw(playBtn, cam.position.x - playBtn.getWidth() / 2, cam.position.y);
        sb.draw(playBtn, 0, 0, 500, 900);
        sb.end();
    }

    @Override
    public void dispose() {
        //background.dispose();
        playBtn.dispose();
        System.out.println("Menu State Disposed");
    }
}
