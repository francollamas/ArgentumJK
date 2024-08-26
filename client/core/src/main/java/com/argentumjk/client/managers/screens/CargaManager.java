package com.argentumjk.client.managers.screens;

import com.badlogic.gdx.utils.TimeUtils;
import com.argentumjk.client.Game;
import com.argentumjk.client.general.Loader;
import com.argentumjk.client.views.screens.MenuView;

public class CargaManager extends ViewManager {

    private Loader loader;
    private long tiempoInicio;
    private boolean solicitaSalir;

    public CargaManager() {
        loader = Game.getInstance().getLoader();
        tiempoInicio = TimeUtils.millis();
    }

    public void salir() {
        solicitaSalir = true;
    }

    public void update() {
        float value;
        value = Game.getInstance().getAssets().loadNextAsset();

        if (value == 1 && !loader.isLoading() && !loader.isLoaded())
            loader.load();

        if (loader.isLoaded() && (TimeUtils.millis() - tiempoInicio > 6000 || solicitaSalir)) {
            setScreen(new MenuView());
        }
    }
}
