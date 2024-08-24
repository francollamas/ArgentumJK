package com.argentumjk.client.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.argentumjk.client.general.Config;
import com.argentumjk.client.Game;
import com.argentumjk.client.graphics.Grh;
import com.argentumjk.client.utils.*;


import static com.argentumjk.client.general.FileNames.*;
import static com.argentumjk.client.containers.GameData.*;

/**
 * Es el mapa actual del juego
 *
 * tiles: matriz de tiles, cada tile con sus correspondientes componentes (objetos, capas, luces, partículas, etc)
 * número: número de mapa
 * nombre: nombre del mapa
 * size: rectángulo que representa el tamaño del mapa
 */
public class Map {

    private static final int X_MIN_SIZE = 1;
    private static final int X_MAX_SIZE = 100;
    private static final int Y_MIN_SIZE = 1;
    private static final int Y_MAX_SIZE = 100;

    private MapTile[][] tiles;
    private int numero;
    private String nombre;
    private Rect size;

    public Map(int numero) {
        this.numero = numero;

        try {
            load();
        }
        catch(NotEnoughDataException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Carga un mapa
     */
    private void load() throws NotEnoughDataException {
        FileHandle fh = Gdx.files.internal(getMapDir(numero));
        if (!fh.exists()) {
            // Si no existe, intento desconectar el usuario y aviso...
            Game.getInstance().getGameData().disconnect();
            // TODO: como desconecta el socket, debería desconetar el usuario del servidor, pero hay que chequearlo
            Dialogs.showOKDialog(Game.getInstance().getBundle().get("error"), "Falta el archivo '" + getMapDir(numero) + ".");
            return;
        }
        BytesReader r = new BytesReader(fh.readBytes(), true);

        r.skipBytes(273);

        // Setea el tamaño del mapa
        size = new Rect();
        size.setX1(X_MIN_SIZE);
        size.setY1(Y_MIN_SIZE);
        size.setX2(X_MAX_SIZE);
        size.setY2(Y_MAX_SIZE);

        int cantXTiles = Math.abs(X_MAX_SIZE - X_MIN_SIZE) + 1;
        int cantYTiles = Math.abs(Y_MAX_SIZE - Y_MIN_SIZE) + 1;

        tiles = new MapTile[cantXTiles][cantYTiles];
        for (int j = 0; j < tiles.length; j++) {
            for (int k = 0; k < tiles[j].length; k++) {
                tiles[j][k] = new MapTile();
            }
        }

        for (int y = 0; y < cantYTiles; y++) {
            for (int x = 0; x < cantXTiles; x++) {
                byte flags = (byte)r.readByte();

                // Bloqueos
                tiles[x][y].setBlocked((flags & 1) > 0);

                // Capa 1
                tiles[x][y].setCapa(0, new Grh(r.readShort()));

                // Capa 2
                if ((flags & 2) > 0) {
                    tiles[x][y].setCapa(1, new Grh(r.readShort()));
                }

                // Capa 3
                if ((flags & 4) > 0) {
                    tiles[x][y].setCapa(2, new Grh(r.readShort()));
                }

                // Capa 4
                if ((flags & 8) > 0) {
                    tiles[x][y].setCapa(3, new Grh(r.readShort()));
                }

                // Trigger
                if ((flags & 16) > 0) {
                    tiles[x][y].setTrigger(r.readShort());
                }

                // TODO falta:
                // setLight
                // setParticula
            }
        }

    }

    public MapTile getTile(Position pos) {
        return getTile((int)pos.getX(), (int)pos.getY());
    }

    public MapTile getTile(int x, int y) {
        if (x >= size.getX1() && x <= size.getX2() && y >= size.getY1() && y <= size.getY2())
            return tiles[x - 1][y - 1];
        return null;
    }

    public int getNumero() {
        return numero;
    }

    public Rect getSize() {
        return size;
    }

    public String getNombre() {
        return nombre;
    }

    /**
     * Obtiene un rectángulo interno al mapa, que corresponden a los límites de posición del usuario.
     * Esto es para que lo último que se vea del mapa sean los límites, y no bordes negros.
     */
    public Rect getBorderRect() {
        int halfWindowsTileWidth = Game.getInstance().getConfig().getWindowsTileWidth() / 2;
        int halfWindowsTileHeight = Game.getInstance().getConfig().getWindowsTileHeight() / 2;

        Rect r = new Rect();
        r.setX1(size.getX1() + halfWindowsTileWidth);
        r.setY1(size.getY1() + halfWindowsTileHeight);
        r.setX2(size.getX2() - halfWindowsTileWidth);
        r.setY2(size.getY2() - halfWindowsTileHeight);

        return r;
    }


    /**
     * Indica si es posible moverse hacia una dirección estando en una posición determinada.
     */
    public boolean isLegalPos(Position pos, Config.Direccion dir) {
        return isLegalPos(pos.getSuma(Position.dirToPos(dir)));
    }

    public boolean isLegalPos(float x, float y) {
        return isLegalPos(new Position(x, y));
    }

    /**
     * Indica si es posible moverse a un tile final (pos)
     */
    public boolean isLegalPos(Position pos) {
        MapTile tile = getTile(pos);
        MapTile wTile = getTile(Game.getInstance().getGameData().getWorld().getPos());

        if (!getBorderRect().isPointIn(pos)) return false;
        if (tile.isBlocked()) return false;

        User u = Game.getInstance().getGameData().getCurrentUser();
        Char wC = Game.getInstance().getGameData().getChars().getChar(u.getIndexInServer());

        // Si hay un char
        if (tile.getCharIndex() > 0) {

            if (wTile.isBlocked()) return false;

            Char c = Game.getInstance().getGameData().getChars().getChar(tile.getCharIndex());
            // Si el char está muerto
            if (c.getHeadIndex() == MUERTO_HEAD || c.getBodyIndex() == MUERTO_NAV_BODY) {
                // Si el muerto esta en el agua y yo estoy en tierra (o viceversa), no lo dejo intercambiar.
                if (wTile.hayAgua() != tile.hayAgua()) return false;

                // Si soy admin y estoy invisible, no puedo intercambiar con el muerto
                if (wC.getPriv() > 0 && wC.getPriv() < 6 && wC.isInvisible()) return false;
            }
            // Si no está muerto
            else return false;
        }

        // Si quiere entrar al agua y no está navegando, o si quiere entrar a tierra y está navegando, no lo deja caminar
        if (u.isNavegando() != tile.hayAgua()) return false;

        return true;
    }



}
