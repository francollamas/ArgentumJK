package com.argentumjk.client.desktop;

import static com.argentumjk.client.general.DtConfig.decorated;
import static com.argentumjk.client.general.DtConfig.fullscreeen;
import static com.argentumjk.client.general.DtConfig.height;
import static com.argentumjk.client.general.DtConfig.resizable;
import static com.argentumjk.client.general.DtConfig.vSync;
import static com.argentumjk.client.general.DtConfig.width;

import com.argentumjk.client.Game;
import com.argentumjk.client.GameParameters;
import com.argentumjk.client.desktop.implementations.DesktopLoader;
import com.argentumjk.client.general.DtConfig;
import com.argentumjk.client.desktop.implementations.DesktopMidiPlayer;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;

import java.io.File;
import java.io.IOException;
import java.lang.management.ManagementFactory;

/** Launches the desktop (LWJGL3) application. */
public class DesktopLauncher {
    public static void main(String[] args) {
        if (StartupHelper.startNewJvmIfRequired()) return; // This handles macOS support and helps on Windows.
        createApplication();
    }

    private static Lwjgl3Application createApplication() {
        // Se define un trozo de código encargado de reiniciar el juego
        final Runnable rebootable = () -> {
                if (Gdx.app != null) {
                    Gdx.app.exit();
                }
                start();
        };
        DtConfig.loadConfig();

        // Usar este booleano para indicar si debe iniciar o no el servidor!
        boolean shouldLoadServer = false;

        GameParameters gameParameters = new GameParameters();
        gameParameters.rebootable = rebootable;
        gameParameters.midiPlayer = new DesktopMidiPlayer();
        gameParameters.loader = new DesktopLoader();
        gameParameters.fullscreen = fullscreeen;

        return new Lwjgl3Application(new Game(gameParameters), getDefaultConfiguration());
    }

    private static Lwjgl3ApplicationConfiguration getDefaultConfiguration() {

        Lwjgl3ApplicationConfiguration configuration = new Lwjgl3ApplicationConfiguration();
        configuration.setTitle("YourProjectName");
        //// Vsync limits the frames per second to what your hardware can display, and helps eliminate
        //// screen tearing. This setting doesn't always work on Linux, so the line after is a safeguard.
        configuration.useVsync(vSync);
        //// Limits FPS to the refresh rate of the currently active monitor, plus 1 to try to match fractional
        //// refresh rates. The Vsync setting above should limit the actual FPS to match the monitor.
        configuration.setForegroundFPS(Lwjgl3ApplicationConfiguration.getDisplayMode().refreshRate + 1);
        //// If you remove the above line and set Vsync to false, you can get unlimited FPS, which can be
        //// useful for testing performance, but can also be very stressful to some hardware.
        //// You may also need to configure GPU drivers to fully disable Vsync; this can cause screen tearing.
        configuration.setWindowedMode(width, height);
        configuration.setDecorated(decorated);
        configuration.setResizable(resizable);
        //// You can change these files; they are in lwjgl3/src/main/resources/ .
        configuration.setWindowIcon("icons/icon128.png", "icons/icon64.png", "icons/icon32.png", "icons/icon16.png");
        return configuration;
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
