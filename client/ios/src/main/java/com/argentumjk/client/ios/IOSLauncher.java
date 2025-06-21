package com.argentumjk.client.ios;

import org.robovm.apple.foundation.NSAutoreleasePool;
import org.robovm.apple.uikit.UIApplication;

import com.argentumjk.client.Game;
import com.argentumjk.client.GameParameters;
import com.argentumjk.client.ios.implementations.IOSLoader;
import com.badlogic.gdx.backends.iosrobovm.IOSApplication;
import com.badlogic.gdx.backends.iosrobovm.IOSApplicationConfiguration;

/**
 * Launches the iOS (RoboVM) application.
 */
public class IOSLauncher extends IOSApplication.Delegate {
    @Override
    protected IOSApplication createApplication() {
        IOSApplicationConfiguration configuration = new IOSApplicationConfiguration();

        // Usar este booleano para indicar si debe iniciar o no el servidor!
        boolean shouldLoadServer = true;

        GameParameters gameParameters = new GameParameters();
        gameParameters.loader = new IOSLoader();

        return new IOSApplication(new Game(), configuration);
    }

    public static void main(String[] argv) {
        NSAutoreleasePool pool = new NSAutoreleasePool();
        UIApplication.main(argv, null, IOSLauncher.class);
        pool.close();
    }
}
