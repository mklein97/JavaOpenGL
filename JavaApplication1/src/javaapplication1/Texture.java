
package javaapplication1;

import etgg.GL;
import static etgg.GL.GL_TEXTURE0;
import static etgg.GL.glActiveTexture;
import static etgg.GL.glBindTexture;
import static etgg.GL.glTexParameteri;

/**
 *
 * @author jhudson
 */
public class Texture {
    int tex;        //"name" of texture as GL sees it
    int type;       //for now, always GL_TEXTURE_2D
    
    Texture(int type){
        this.type = type;
    }
    
    void bind(int unit){
        if( tex == 0 )
            throw new RuntimeException("Uninitialized texture!");
        glActiveTexture(GL_TEXTURE0 + unit);
        glBindTexture(type, tex);
    }
    void unbind(int unit){
        glActiveTexture(GL_TEXTURE0 + unit);
        glBindTexture(type, 0);
    }
    
    void setParameter(int pname, int pvalue){
        bind(0);
        glTexParameteri(type,pname,pvalue);
    }
    
    static boolean isPowerOf2(int x){
        return ( (x-1) & x ) == 0;
    }
}
