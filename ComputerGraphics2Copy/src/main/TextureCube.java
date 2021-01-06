package main;

import static etgg.GL.GL_TEXTURE_CUBE_MAP;

public class TextureCube extends Texture{
    int size;
    TextureCube(){
        super(GL_TEXTURE_CUBE_MAP);
    }
}
