/*
 */
package main;

import static etgg.GL.GL_TEXTURE0;
import static etgg.GL.glActiveTexture;
import static etgg.GL.glBindTexture;
import static etgg.GL.glTexParameteri;
import java.util.Set;
import java.util.TreeSet;

/**
 *
 * @author jhudson
 */
public class Texture{
    int tex;
    int type;
    //keeps of track of which texture units this texture is attached to
    Set<Integer> on_units = new TreeSet<>();
    
    //identity of the fbo that owns this texture or null if none
    Framebuffer owning_fbo;
    
    Texture(int type){
        this.type=type;
    }
    public void bind(int unit){
        if( tex == 0 )
            throw new RuntimeException("Cannot bind: Not yet initialized");
        if( this.owning_fbo != null && this.owning_fbo == Framebuffer2D.active_target )
            throw new RuntimeException("Cannot bind: The FBO that owns this texture is active");
        
        glActiveTexture(GL_TEXTURE0+unit);
        glBindTexture(type,tex);
        on_units.add(unit);
        //FIXME: Need to deal with this...
    }
    public void unbind(int unit){
        glActiveTexture(GL_TEXTURE0+unit);
        glBindTexture(type,0);
        on_units.remove(unit);
    }
    public void unbind(){
        for( int x : on_units )
            unbind(x);
    }
    static boolean isPowerOf2(int x){
        return ((x-1)&x)==0;
    }
    public void setParameter(int pname, int pvalue ){
        bind(0);
        glTexParameteri(type,pname,pvalue);
    }
}