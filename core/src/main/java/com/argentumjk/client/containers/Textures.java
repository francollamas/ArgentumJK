package com.argentumjk.client.containers;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.argentumjk.client.objects.TextureData;

/**
 * Manejador de las texturas del juego
 * TODO: revisar si tiene sentido el funcionamiento de este componente, ya que ahora que ahora se usa un atlas.
 * TODO: usar la estructura Hash de Java!!!
 * TODO: PONER LÍMITE DE MEMORIA Y BORRAR LAS TEXTURAS MÁS VIEJAS!!!
 *
 * hash: array de array de TextureData. Se usa para agiliar la búsqueda de texturas.
 */
public class Textures {

    private Array<TextureData>[] hash;

    public Textures() {
        hash = new Array[350];
    }

    /**
     * Obtiene un textureRegion a partir de su número de gráfico (si no existe la crea)
     */
    public TextureRegion getTextureRegion(int num) {
        // Determino en que posición del Hash va la textura solicitada
        int hashIndex = num % hash.length;

        // Si es la primera textura en esa posición del Hash, creo el array y la textura solicitada
        if (hash[hashIndex] == null) {
            hash[hashIndex] = new Array<>();
            return createTextureRegion(num);
        }

        // De lo contrario, la busco...
        else {
            for (int i = 0; i < hash[hashIndex].size; i++) {
                TextureData td = hash[hashIndex].get(i);
                if (td.getNum() == num)
                    return td.getTextureRegion();
            }
        }

        // Si no la encontró, la crea...
        return createTextureRegion(num);
    }

    /**
     * Crea un texture region
     */
    private TextureRegion createTextureRegion(int num) {
        // Creo un nuevo texture region y lo agrego en el array de la posición correspondiente del Hash
        TextureData td = new TextureData(num);
        hash[num % hash.length].add(td);
        return td.getTextureRegion();
    }

    /**
     * Elimina todas las texturas de la placa de video
     */
    public void dispose() {
        for (Array<TextureData> h : hash)
            if (h != null)
                for (int i = 0; i < h.size; i++)
                    h.get(i).dispose();
    }
}
