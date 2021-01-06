/*
 */
package main;

import static etgg.GL.GL_CLAMP_TO_EDGE;
import static etgg.GL.GL_LINEAR;
import static etgg.GL.GL_LINEAR_MIPMAP_LINEAR;
import static etgg.GL.GL_REPEAT;
import static etgg.GL.GL_RGBA;
import static etgg.GL.GL_TEXTURE_2D;
import static etgg.GL.GL_TEXTURE_MAG_FILTER;
import static etgg.GL.GL_TEXTURE_MIN_FILTER;
import static etgg.GL.GL_TEXTURE_WRAP_S;
import static etgg.GL.GL_TEXTURE_WRAP_T;
import static etgg.GL.GL_UNSIGNED_BYTE;
import static etgg.GL.glGenTextures;
import static etgg.GL.glGenerateMipmap;
import static etgg.GL.glTexImage2D;
import static etgg.GL.glTexParameteri;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import static math3d.functions.flipImage;

/**
 *
 * @author jhudson
 */
public class ImageTexture2D extends Texture2D {
    String filename;
    public ImageTexture2D(String filename){
        
         //for debugging, etc.
         this.filename = filename;

        int[] tmp = new int[1];
        glGenTextures(1,tmp);
        tex = tmp[0];

        ByteArrayOutputStream A = new ByteArrayOutputStream();
        BufferedImage img;
        try {
            img = flipImage(ImageIO.read(new File(filename)));
        } catch (IOException ex) {
            throw new RuntimeException("Cannot read image "+filename);
        }
        w = img.getWidth();
        h = img.getHeight();
            
        int[] idata = img.getRGB(0,0,w,h,null,0,w);
        for(int q : idata ){
            A.write( (q>>16)&0xff );//r
            A.write( (q>>8 )&0xff );//g
            A.write( (q    )&0xff );//b
            A.write( (q>>24)&0xff );//a
        }
        
        bind(0);
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, w,h, 0,
                GL_RGBA, GL_UNSIGNED_BYTE, A.toByteArray() );
        if( isPowerOf2(w) && isPowerOf2(h) ){
            glGenerateMipmap(GL_TEXTURE_2D);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR_MIPMAP_LINEAR);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
        }
        else{
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
        }
        unbind(0);
    }
}

