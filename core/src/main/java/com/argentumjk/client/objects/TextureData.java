package com.argentumjk.client.objects;

import com.argentumjk.client.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.TimeUtils;

import static com.argentumjk.client.general.FileNames.*;

/**
 * Información de una textua
 *
 * tex: textura
 * num: número de gráfico que representa a la textura
 * lastAccess: ultimo momento en que se solicitó esta textura (en milisegundos)
 */
public class TextureData {
    private int num;
    private long lastAccess;
    private TextureRegion textureRegion;

    public TextureData(int num) {
        TextureAtlas atlas = Game.getInstance().getAssets().getTextureAtlas();

        this.num = num;
        try {
            textureRegion = atlas.findRegion(num + "");
        }
        catch (Exception e) {
            // TODO: no crear una textura aleatoria sino que manejar el error mas arriba!
            textureRegion = atlas.findRegion("510");
        }
    }

    public int getNum() {
        return num;
    }

    public long getLastAccess() {
        return lastAccess;
    }

    public TextureRegion getTextureRegion() {
        lastAccess = TimeUtils.millis();
        return textureRegion;
    }

    public void dispose() {
        textureRegion.getTexture().dispose();
    }
}
