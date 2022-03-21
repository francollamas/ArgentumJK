package com.argentumjk.client.general;

import com.argentumjk.client.Game;
import com.argentumjk.server.GameServer;

/**
 * Clase para el control de la carga de recursos del juego (en Desktop, Android e iOS)
 *
 * thread: hilo de ejecucion aparte para cargar los recursos.
 * cargado: indica si se cargaron los recursos
 */
public class GnLoader implements Loader {
    private Thread thread;
    private Thread thread2;
    private boolean cargado;
    private GameServer server;

    public GnLoader() {
        // Defino las acciones del thread de carga.
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                Game.getInstance().getAssets().loadRemaining();
                server = GameServer.instance();
                cargado = true;
            }
        });

        // TODO: reubicar... es para arrancar el server aqui mismo...
        thread2 = new Thread(new Runnable() {
            @Override
            public void run() {
                while (server == null) {
                }
                server.runGameLoop();
            }
        });
    }

    @Override
    public void load() {
        // Activo el thread
        thread.start();
        thread2.start();
    }

    @Override
    public boolean isLoading() {
        return thread.isAlive();
    }

    @Override
    public boolean isLoaded() {
        return cargado;
    }
}
