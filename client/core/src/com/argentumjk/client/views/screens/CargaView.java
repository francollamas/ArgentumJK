package com.argentumjk.client.views.screens;

import com.argentumjk.client.Game;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.argentumjk.client.managers.screens.CargaManager;

import static com.argentumjk.client.utils.Actors.*;

/**
 * Pantalla de carga
 */
public class CargaView extends View {

    public CargaView() {
        super(new CargaManager());
    }
    public CargaManager getGestor() { return (CargaManager)gestor; }

    @Override
    public void show() {
        super.show();

        // Definición de los elementos de la pantalla
        newFirstTable(getDrawable("carga"), true);

        // Eventos generales de la pantalla
        stage.addListener(new InputListener() {
            @Override
            public boolean keyUp(InputEvent event, int keycode) {
                if (keycode == Input.Keys.ESCAPE)
                    getGestor().salir();
                return super.keyUp(event, keycode);
            }

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                getGestor().salir();
                return super.touchDown(event, x, y, pointer, button);
            }
        });

        Game.getInstance().getMidiPlayer().play(6);
    }
}
