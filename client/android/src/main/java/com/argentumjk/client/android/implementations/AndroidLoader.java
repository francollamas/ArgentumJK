package com.argentumjk.client.android.implementations;

import com.argentumjk.client.Game;
import com.argentumjk.client.general.Loader;
import com.argentumjk.server.GameServer;

/**
 * Clase para el control de la carga de recursos del juego
 * <p>
 * thread: hilo de ejecucion aparte para cargar los recursos.
 * cargado: indica si se cargaron los recursos
 */
public class AndroidLoader implements Loader {
    private final Thread loadThread;
    private Thread serverThread;
    private boolean cargado;
    private GameServer server;

    public AndroidLoader(boolean shouldLoadServer) {
        // Defino las acciones del thread de carga.
        loadThread = new Thread(new Runnable() {
            @Override
            public void run() {
                Game.getInstance().getAssets().loadRemaining();
                if (shouldLoadServer) {
                    server = GameServer.instance();
                }
                cargado = true;
            }
        });

        if (shouldLoadServer) {
            serverThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    while (server == null) {
                    }
                    server.runGameLoop();
                }
            });
        }
    }

    @Override
    public void load() {
        // Activo el thread
        loadThread.start();
        if (serverThread != null) {
            serverThread.start();
        }
    }

    @Override
    public boolean isLoading() {
        return loadThread.isAlive();
    }

    @Override
    public boolean isLoaded() {
        return cargado;
    }

    @Override
    public void dispose() {
        if (server != null) {
            server.shutdown();
        }
    }
}
