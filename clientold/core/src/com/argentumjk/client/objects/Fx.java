package com.argentumjk.client.objects;

import com.argentumjk.client.graphics.Grh;
import com.argentumjk.client.utils.Position;

/**
 * Representa a un Fx
 *
 * grhIndex: índex del grh del fx
 * offset: distancia desde donde se comienza a dibujar
 */
public class Fx {
    private int grhIndex;
    private Position offset;

    public Fx() {
        offset = new Position();
    }

    public int getGrhIndex() {
        return grhIndex;
    }

    public void setGrhIndex(int grhIndex) {
        this.grhIndex = grhIndex;
    }

    /**
     * Obtiene un nuevo Grh según el grhIndex del Fx
     */
    public Grh getGrh() {
        return new Grh(grhIndex);
    }

    /**
     * Obtiene la posición exacta del offset
     */
    public Position getOffset() {
        return offset;
    }

    /**
     * Setea la posición exacta del offset
     */
    public void setOffset(Position offset) {
        this.offset = offset;
    }

    public void setOffsetX(int x) {
        offset.setX(x);
    }

    public void setOffsetY(int y) {
        offset.setY(y);
    }

}
