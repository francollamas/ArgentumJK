package com.argentumjk.client.containers;


import com.badlogic.gdx.graphics.Color;

/**
 * Contiene los colores de los nicks de los PJs.
 * <p>
 * colors: array de colores
 */
public class Colors {
    private Color[] colors;

    public Colors() {
        colors = new Color[51];

        colors[1] = newColor(30, 150, 30); // consejeros
        colors[2] = newColor(30, 255, 30); // semidioses
        colors[3] = newColor(250, 250, 150); // dioses
        colors[4] = newColor(255, 255, 255); // admins
        colors[5] = newColor(180, 180, 180); // rolmasters
        colors[6] = newColor(255, 50, 0); // caos
        colors[7] = newColor(0, 195, 255); // consejo de bander
        colors[49] = newColor(0, 128, 255); // ciudadanos
        colors[50] = newColor(255, 0, 0); // criminales
    }

    public static Color newColor(int r, int g, int b) {
        return newColor(r, g, b, 255);
    }

    public static Color newColor(int r, int g, int b, int a) {
        return new Color(r / 255.0f, g / 255.0f, b / 255.0f, a / 255.0f);
    }

    /**
     * Obtiene el color correspondiente según privilegios y bando
     */
    public Color getColor(int priv, int bando) {
        if (priv == 0) {
            if (bando == 1) {
                return colors[50];
            }
            return colors[49];
        } else {
            return colors[priv + 3];
        }
    }
}
