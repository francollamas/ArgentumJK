package com.argentumjk.client.web;

import com.argentumjk.client.Game;
import com.argentumjk.client.GameParameters;
import com.argentumjk.client.web.implementations.WebConnection;
import com.argentumjk.client.web.implementations.WebLoader;
import com.github.xpenatan.gdx.backends.teavm.TeaApplicationConfiguration;
import com.github.xpenatan.gdx.backends.teavm.TeaApplication;

/**
 * Launches the TeaVM/HTML application.
 */
public class WebLauncher {
    public static void main(String[] args) {
        TeaApplicationConfiguration config = new TeaApplicationConfiguration("canvas");
        //// If width and height are each greater than 0, then the app will use a fixed size.
        //config.width = 640;
        //config.height = 480;
        //// If width and height are both 0, then the app will use all available space.
        //config.width = 0;
        //config.height = 0;
        //// If width and height are both -1, then the app will fill the canvas size.
        config.width = -1;
        config.height = -1;
        GameParameters gameParameters = new GameParameters();
        gameParameters.connection = new WebConnection();
        gameParameters.loader = new WebLoader();
        new TeaApplication(new Game(gameParameters), config);
    }
}
