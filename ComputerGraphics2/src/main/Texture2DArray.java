/*
 */
package main;

import static etgg.GL.GL_TEXTURE_2D_ARRAY;

/**
 *
 * @author jhudson
 */
public class Texture2DArray extends Texture{
    int w,h,slices;
    Texture2DArray(){
        super(GL_TEXTURE_2D_ARRAY);
    }
}
