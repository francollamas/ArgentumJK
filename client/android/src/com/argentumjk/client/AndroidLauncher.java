package com.argentumjk.client;

import android.os.Bundle;

import com.argentumjk.client.implementations.AndroidMidiPlayer;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;

public class AndroidLauncher extends AndroidApplication {
	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		initialize(new Game(null, new AndroidMidiPlayer(getContext()), false), config);
	}
}
