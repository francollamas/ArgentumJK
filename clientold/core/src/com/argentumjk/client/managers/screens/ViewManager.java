package com.argentumjk.client.managers.screens;

import com.badlogic.gdx.Screen;
import com.argentumjk.client.Game;
import com.argentumjk.client.connection.ClientPackages;
import com.argentumjk.client.connection.Connection;
import com.argentumjk.client.connection.ServerPackages;
import com.argentumjk.client.containers.GameData;
import com.argentumjk.client.views.screens.View;

public abstract class ViewManager {

    public ViewManager() {

    }

    /**
     * Realiza actualizaciones constantemente
     *
     * No debe ser llamado.. solo hacer Override.. ya es llamado desde la clase abstracta {@link View}
     */
    public void update() {

    }

    public void playMusic(int num) {
        Game.getInstance().getMidiPlayer().play(num);
    }

    public void exitGame() {
        Game.getInstance().salir();
    }

    public void restartGame() {
        Game.getInstance().reiniciar();
    }

    public void back() {

    }

    protected void setScreen(Screen scr) { Game.getInstance().setScreen(scr); }
    protected ClientPackages getClPack() { return Game.getInstance().getConnection().getClPack(); }
    protected ServerPackages getSvPack() { return Game.getInstance().getConnection().getSvPack(); }
    protected Connection getConnection() { return Game.getInstance().getConnection(); }
    protected GameData getGD() { return Game.getInstance().getGameData(); }
    protected String bu(String key) { return Game.getInstance().getBundle().get(key); }
}
