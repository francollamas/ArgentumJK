package com.argentumjk.client.managers.screens;

import com.argentumjk.client.general.IMidiPlayer;
import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.argentumjk.client.Game;
import com.argentumjk.client.containers.Audio;
import com.argentumjk.client.general.Config;
import com.argentumjk.client.general.DtConfig;
import com.argentumjk.client.views.screens.MenuView;

public class ConfigManager extends ViewManager {

    public ConfigManager() {

    }

    @Override
    public void back() {
        setScreen(new MenuView());
    }

    public void guardarVideo(boolean vSync, boolean fullscreen, int res, boolean titleBar, boolean resizable) {

        // OnlyDesktop
        if (Gdx.app.getType() == Application.ApplicationType.Desktop) {
            // Guarda los valores actuales para luego verificar si se hizo un cambio en al menos una característica
            boolean oldVSync = DtConfig.vSync;
            boolean oldFullScreen = DtConfig.fullscreeen;
            int oldWidth = DtConfig.width;
            int oldHeight = DtConfig.height;
            boolean oldDecorated = DtConfig.decorated;
            boolean oldResizable = DtConfig.resizable;

            DtConfig.vSync = vSync;

            DtConfig.fullscreeen = fullscreen;
            if (DtConfig.fullscreeen) {
                DtConfig.width = 1024; DtConfig.height = 768;
            }
            else {
                switch (res) {
                    case 0:
                        DtConfig.width = 800; DtConfig.height = 600;
                        break;
                    case 1:
                        DtConfig.width = 1024; DtConfig.height = 768;
                        break;
                    case 2:
                        DtConfig.width = 1200; DtConfig.height = 900;
                        break;
                }
            }

            DtConfig.decorated = titleBar;
            DtConfig.resizable = resizable;

            DtConfig.saveConfig();

            // Si se hicieron cambios de video, se reinicia el cliente.
            if (oldVSync != DtConfig.vSync || oldFullScreen != DtConfig.fullscreeen
                    || oldWidth != DtConfig.width || oldHeight != DtConfig.height
                    || oldDecorated != DtConfig.decorated || oldResizable != DtConfig.resizable) {

                restartGame();
            }

        }
    }

    public void guardarAudio(boolean musica, float volMusica, boolean sonido, float volSonido) {
        // Configuraciones generales
        Config c = getConfig();
        c.setMusicActive(musica);
        c.setMusicVol(volMusica);
        c.setSoundActive(sonido);
        c.setSoundVol(volSonido);

        // Actualizo música actual...
        Audio a = Game.getInstance().getAssets().getAudio();
        a.setMusicVolume(volMusica);
        a.setSoundVolume(volSonido);

        IMidiPlayer m = Game.getInstance().getMidiPlayer();
        m.setVolume(volMusica);

        if (a.getMusic() != null) a.getMusic().setVolume(volMusica);

        a.setMuteMusic(!musica);
        m.setMuteMidi(!musica);
        a.setMuteSound(!sonido);
    }

    public Config getConfig() { return Game.getInstance().getConfig(); }
}
