package com.argentumjk.server;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;

public class Game extends ApplicationAdapter {

    public Game() {
    }

    @Override
    public void create() {
        GameServer.instance().runGameLoop();
    }
}
