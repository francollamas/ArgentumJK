package com.argentumjk.client.android.implementations;

import com.argentumjk.client.Game;
import com.argentumjk.client.general.Loader;

/**
 * Clase para el control de la carga de recursos del juego
 * <p>
 * thread: hilo de ejecucion aparte para cargar los recursos.
 * cargado: indica si se cargaron los recursos
 */
public class AndroidLoader implements Loader {
    private final Thread loadThread;
    private boolean cargado;

    public AndroidLoader() {
        // Defino las acciones del thread de carga.
        loadThread = new Thread(new Runnable() {
            @Override
            public void run() {
                Game.getInstance().getAssets().loadRemaining();
                cargado = true;
            }
        });
    }

    @Override
    public void load() {
        // Activo el thread
        loadThread.start();
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
    }
}
