package com.argentumjk.client;

import com.argentumjk.client.general.EmptyMidiPlayer;
import com.argentumjk.client.general.IMidiPlayer;
import com.argentumjk.server.GameServer;
import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.I18NBundle;
import com.badlogic.gdx.utils.async.AsyncExecutor;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.kotcrab.vis.ui.VisUI;
import com.argentumjk.client.connection.Connection;
import com.argentumjk.client.connection.GnConnection;
import com.argentumjk.client.containers.Assets;
import com.argentumjk.client.containers.GameData;
import com.argentumjk.client.general.Config;
import com.argentumjk.client.utils.Dialogs;
import com.argentumjk.client.views.screens.CargaView;
import com.argentumjk.client.views.screens.MenuView;
import com.argentumjk.client.views.screens.View;
import com.kotcrab.vis.ui.util.async.AsyncTask;

import static com.argentumjk.client.general.FileNames.*;
import static com.badlogic.gdx.Application.ApplicationType.*;

/**
 * Clase principal del juego
 * <p>
 * rebootable: contiene un Runnable (código para ejecutar) que se encarga de reiniciar el juego.
 * bundle: maneja los textos según el idioma.
 * config: ajustes iniciales (tamaño de pantalla, del world, etc).
 * assets: manejador de recursos
 * connection: permite la conexión con el servidor y el envío y recepción de paquetes.
 * gameData: contiene toda estructura y estado del juego
 */
public class Game extends com.badlogic.gdx.Game {

    /**
     * Constructor general
     */
    public Game() {
    }

    /**
     * Constructor usado en Desktop
     *
     * @param rebootable trozo de código que reinicia el juego
     */
    public Game(Runnable rebootable, IMidiPlayer midiPlayer, boolean fullscreen) {
        this.rebootable = rebootable;
        this.midiPlayer = midiPlayer;
        this.fullscreen = fullscreen;
    }


    private Runnable rebootable;
    private boolean fullscreen;

    private I18NBundle bundle;
    private Batch batch;

    private Config config;
    private Assets assets;
    private Connection connection;
    private GameData gameData;
    private IMidiPlayer midiPlayer;

    /**
     * Sale del juego
     */
    public void salir() {
        Gdx.app.exit();
    }

    /**
     * Reinicia el juego
     */
    public void reiniciar() {
        // Inserta y ejecuta el runnable en la aplicación
        Gdx.app.postRunnable(rebootable);
    }

    /**
     * Inicialización del juego
     */
    @Override
    public void create() {
        // Config global
        Gdx.graphics.setTitle("Argentum Online");
        Gdx.app.setLogLevel(Application.LOG_DEBUG);
        Gdx.input.setCatchBackKey(true);

        if (fullscreen && Gdx.graphics.supportsDisplayModeChange()) {
            Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode());
        }

        // TODO: ver el tema del cursor
		/*Pixmap pm = new Pixmap(Gdx.files.internal(getCursorDir()));
		Gdx.graphics.setCursor(Gdx.graphics.newCursor(pm, 0, 0));
		pm.dispose();*/

        if (midiPlayer == null) {
            midiPlayer = new EmptyMidiPlayer();
        }
        midiPlayer.initialize();


        // Config propia del juego
        bundle = I18NBundle.createBundle(Gdx.files.internal(getBundleDir()));
        batch = new SpriteBatch();
        config = new Config();
        assets = new Assets();
        VisUI.load(assets.getGDXAssets().get(getSkinDir(), Skin.class));
        gameData = new GameData();

        // Conexión
        connection = new GnConnection();

        setScreen(new CargaView());
    }

    @Override
    public void render() {

        if (connection.getSvPack().isLostConnection()) {
            setScreen(new MenuView());
            Dialogs.showOKDialog(Game.getInstance().getBundle().get("error"), "Conexión perdida");
        }

        // Procesamos los paquetes recibidos al socket
        connection.getSvPack().doActions();

        // Uso esta llamada para que se siga renderizando la pantalla actual normalmente
        super.render();

        // Escribe agrega los bytes pendientes a la cola.
        connection.getClPack().write();

        // Si es Web, le avisa al socket que envíe las acciones registradas anteriormente
        // (para las demás plataformas no es necesario, ya que tienen un thread que se encarga de esto)
        if (Gdx.app.getType() == WebGL)
            connection.write();
    }

    /**
     * Liberar recursos
     */
    @Override
    public void dispose() {
        VisUI.dispose();
        screen.dispose();
        assets.dispose();
        connection.dispose();
        batch.dispose();
        midiPlayer.stop();
        midiPlayer.release();
    }

    /**
     * Obtiene la instancia del juego
     */
    public static Game getInstance() {
        return (Game) Gdx.app.getApplicationListener();
    }

    /**
     * Devuelve un nuevo escenario usando el mismo Batch.
     */
    public static Stage newStage() {
        Config c = Game.getInstance().getConfig();
        return new Stage(new FitViewport(c.getVirtualWidth(), c.getVirtualHeight()), Game.getInstance().getBatch());
    }

    /**
     * Cambia de Screen. (desecha la anterior y usa la nueva)
     *
     * @param screen
     */
    @Override
    public void setScreen(Screen screen) {
        if (this.screen != null) this.screen.dispose();
        this.screen = screen;
        if (this.screen != null) {
            this.screen.show();
            this.screen.resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
            Action a = Actions.sequence(Actions.alpha(0f), Actions.fadeIn(0.3f, Interpolation.fade));
            ((View) this.screen).getStage().addAction(a);
        }
    }

    public I18NBundle getBundle() {
        return bundle;
    }

    public Batch getBatch() {
        return batch;
    }

    public Config getConfig() {
        return config;
    }

    public Assets getAssets() {
        return assets;
    }

    public Connection getConnection() {
        return connection;
    }

    public GameData getGameData() {
        return gameData;
    }

    public IMidiPlayer getMidiPlayer() {
        return midiPlayer;
    }
}
