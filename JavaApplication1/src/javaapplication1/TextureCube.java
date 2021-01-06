package javaapplication1;

import static etgg.GL.GL_TEXTURE_CUBE_MAP;

/**
 *
 * @author jhudson
 */
public class TextureCube extends Texture {
    int size;
    TextureCube(){
        super(GL_TEXTURE_CUBE_MAP);
    }
}
