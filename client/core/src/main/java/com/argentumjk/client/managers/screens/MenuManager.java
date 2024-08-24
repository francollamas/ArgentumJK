package com.argentumjk.client.managers.screens;

import com.argentumjk.client.utils.Dialogs;
import com.argentumjk.client.views.screens.CrearPjView;

public class MenuManager extends ViewManager {

    public MenuManager() {

    }

    public void conectar(String nombre, String contraseña) {
        if (nombre.equals(""))
            Dialogs.showOKDialog("Error", "El nombre no puede estar vacío.");
        else if (contraseña.equals(""))
            Dialogs.showOKDialog("Error", "La contraseña no puede estar vacía");
        else {
            if (getConnection().connect())
                getClPack().writeLoginExistingChar(nombre, contraseña);
        }
    }

    public void crearPj() {
        if (getConnection().connect()) {
            setScreen(new CrearPjView());
            getClPack().writeThrowDices();
        }
    }
}
