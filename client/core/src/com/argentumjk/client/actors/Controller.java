package com.argentumjk.client.actors;

import com.badlogic.gdx.scenes.scene2d.ui.Touchpad;
import com.kotcrab.vis.ui.VisUI;
import com.argentumjk.client.Game;
import com.argentumjk.client.general.Config;

import static com.argentumjk.client.general.Config.Direccion.*;

/**
 * Touchpad que controla el movimiento del personaje (se usa en dispositivos móviles)
 *
 * tpSkin y tpStyle: necesarias para darle un estilo al controlador.
 * world: referencia al mundo para poder moverse en él.
 */
public class Controller extends Touchpad {
    public Controller() {
        super(15, VisUI.getSkin());
    }

    /**
     * Comportamiento del controlador, se ejecuta constantemente
     */
    @Override
    public void act(float delta) {
        super.act(delta);

        World w = Game.getInstance().getGameData().getWorld();
        if (isTouched())
            if (!w.isMoving()) {
                float despX = getKnobPercentX();
                float despY = getKnobPercentY();
                Config.Direccion dir = null;
                if (despX != 0 || despY != 0)
                    if (Math.abs(despX) > Math.abs(despY))
                        if (despX > 0)
                            dir = ESTE;
                        else
                            dir = OESTE;
                    else
                    if (despY > 0)
                        dir = NORTE;
                    else
                        dir = SUR;

                if (dir != null)
                    w.moveChar(dir);
            }
    }
}


