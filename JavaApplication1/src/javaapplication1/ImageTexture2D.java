
package javaapplication1;

import etgg.GL;
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
import static etgg.GL.glGenTextures;
import static etgg.GL.glGenerateMipmap;
import static etgg.GL.glTexImage2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

/**
 *
 * @author jhudson
 */
public class ImageTexture2D extends Texture2D {
    String filename;
    
    ImageTexture2D(String filename){
        this.filename = filename;
        try {
            BufferedImage img = ImageIO.read(new File(filename));
            
            int[] tmp = new int[1];
            glGenTextures(1, tmp);
            this.tex = tmp[0];
            
            w=img.getWidth();
            h=img.getHeight();

            int[] pix = img.getRGB(0,0, w,h, null, 0, w);
            byte[] bpix = new byte[w*h*4];
            
            for(int y=h-1,j=0;y>=0;--y){
                //want i to refer to index in pix that corresponds to
                //row i of image
                int i = w*y;
                for(int x=0;x<w;++x,++i){
                    byte b = (byte)( (pix[i]   ) & 0xff);
                    byte g = (byte)( (pix[i]>>8) & 0xff);
                    byte r = (byte)( (pix[i]>>16)& 0xff);
                    byte a = (byte)( (pix[i]>>24)& 0xff);
                    bpix[j++] = r;
                    bpix[j++] = g;
                    bpix[j++] = b;
                    bpix[j++] = a;
                }
            }
            
            bind(0);
            glTexImage2D(GL_TEXTURE_2D, 
                    0, //mipmap level 
                    GL_RGBA,    //how the gpu should store internally
                    w,h,
                    0,  //border
                    GL_RGBA, GL.GL_UNSIGNED_BYTE,   //format of data being passed in
                    bpix //byte array of image data
                    );
            
            if( isPowerOf2(w) && isPowerOf2(h)){
                glGenerateMipmap(GL_TEXTURE_2D);
                setParameter(GL_TEXTURE_WRAP_S, GL_REPEAT);
                setParameter(GL_TEXTURE_WRAP_T, GL_REPEAT);
                setParameter(GL_TEXTURE_MIN_FILTER, GL_LINEAR_MIPMAP_LINEAR);
                setParameter(GL_TEXTURE_MAG_FILTER,GL_LINEAR);
            }else{
                setParameter(GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
                setParameter(GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
                setParameter(GL_TEXTURE_MIN_FILTER, GL_LINEAR);
                setParameter(GL_TEXTURE_MAG_FILTER,GL_LINEAR);
            }
            unbind(0);
            
        } catch (IOException ex) {
            throw new RuntimeException("Cannot load image "+filename);
        }
    }
}
