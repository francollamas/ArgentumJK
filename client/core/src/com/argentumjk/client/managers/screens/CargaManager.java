package com.argentumjk.client.managers.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.TimeUtils;
import com.argentumjk.client.Game;
import com.argentumjk.client.general.GnLoader;
import com.argentumjk.client.general.Loader;
import com.argentumjk.client.general.WebLoader;
import com.argentumjk.client.views.screens.MenuView;

import static com.badlogic.gdx.Application.ApplicationType.WebGL;

public class CargaManager extends ViewManager {

    private Loader loader;
    private long tiempoInicio;
    private boolean solicitaSalir;

    public CargaManager() {
        // Crea el Loader
        if (Gdx.app.getType() == WebGL)
            loader = new WebLoader();
        else
            loader = new GnLoader();

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
