package com.argentumjk.client.desktop;

import com.argentumjk.client.Game;
import com.argentumjk.client.desktop.implementations.DesktopMidiPlayer;
import com.argentumjk.client.general.DtConfig;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;

import java.io.File;
import java.io.IOException;
import java.lang.management.ManagementFactory;

import static com.argentumjk.client.general.DtConfig.*;

public class DesktopLauncher {
    public static void main(String[] arg) {
        // Se define un trozo de código encargado de reiniciar el juego
        final Runnable rebootable = new Runnable() {
            @Override
            public void run() {
                if (Gdx.app != null) {
                    Gdx.app.exit();
                }
                start();
            }
        };

        DtConfig.loadConfig();
        createLwjgl3Application(new Game(rebootable, new DesktopMidiPlayer(), fullscreeen));
    }

    /**
     * Devuelve la aplicación ya lista, con todas sus configuraciones
     */
    private static Lwjgl3Application createLwjgl3Application(Game game) {

        Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();

        config.useVsync(vSync);
        config.setDecorated(decorated);
        config.setResizable(resizable);
        config.setWindowedMode(width, height);

        config.setWindowIcon("icons/icon128.png", "icons/icon64.png", "icons/icon32.png", "icons/icon16.png");

        return new Lwjgl3Application(game, config);
    }

    /**
     * Inicia la aplicación nuevamente
     * <p>
     * (inserta un nuevo comando, indicando de abrir el juego)
     */
    public static void start() {
        final StringBuilder cmd = new StringBuilder();
        cmd.append(System.getProperty("java.home") + File.separator + "bin" + File.separator + "java ");
        for (final String jvmArg : ManagementFactory.getRuntimeMXBean().getInputArguments()) {
            cmd.append(jvmArg + " ");
        }
        cmd.append("-cp \"").append(ManagementFactory.getRuntimeMXBean().getClassPath()).append("\" ");
        cmd.append(DesktopLauncher.class.getName()).append(" ");

        try {
            System.out.println(cmd.toString());
            Runtime.getRuntime().exec(cmd.toString());
        } catch (final IOException e) {
            e.printStackTrace();
        }
    }
}