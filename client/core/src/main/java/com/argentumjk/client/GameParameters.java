package com.argentumjk.client;

import com.argentumjk.client.connection.Connection;
import com.argentumjk.client.general.IMidiPlayer;
import com.argentumjk.client.general.Loader;

public class GameParameters {
    public Runnable rebootable;
    public Loader loader;
    public IMidiPlayer midiPlayer;
    public Connection connection;
    public boolean fullscreen;
}
