package com.argentumjk.client.android;

import android.os.Bundle;

import com.argentumjk.client.Game;
import com.argentumjk.client.GameParameters;
import com.argentumjk.client.android.implementations.AndroidLoader;
import com.argentumjk.client.android.implementations.AndroidMidiPlayer;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;

/**
 * Launches the Android application.
 */
public class AndroidLauncher extends AndroidApplication {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AndroidApplicationConfiguration configuration = new AndroidApplicationConfiguration();
        configuration.useImmersiveMode = true; // Recommended, but not required.

        // Usar este booleano para indicar si debe iniciar o no el servidor!
        boolean shouldLoadServer = true;

        GameParameters gameParameters = new GameParameters();
        gameParameters.midiPlayer = new AndroidMidiPlayer(getContext());
        gameParameters.loader = new AndroidLoader();
        initialize(new Game(gameParameters), configuration);
    }
}
