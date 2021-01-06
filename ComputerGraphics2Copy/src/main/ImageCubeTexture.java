package main;

import static etgg.GL.GL_CLAMP_TO_EDGE;
import static etgg.GL.GL_LINEAR;
import static etgg.GL.GL_LINEAR_MIPMAP_LINEAR;
import static etgg.GL.GL_REPEAT;
import static etgg.GL.GL_RGBA;
import static etgg.GL.GL_TEXTURE_CUBE_MAP;
import static etgg.GL.GL_TEXTURE_CUBE_MAP_POSITIVE_X;
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


class ImageCubeTexture extends TextureCube {

    //+-X, +-Y, +-Z
    ImageCubeTexture(String... filenames){
        if( filenames.length != 6 )
            throw new RuntimeException("Bad size for filenames");
        
        int[] tmp = new int[1];
        glGenTextures(1,tmp);
        tex = tmp[0];
        
        bind(0);
        size=-1;
        
        for(int m=0;m<6;++m){
            ByteArrayOutputStream A = new ByteArrayOutputStream();
            BufferedImage img;
            try {
                img = ImageIO.read(new File(filenames[m]));
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
            
            int w = img.getWidth();
            int h = img.getHeight();
            if(size == -1 )
                size=w;
            
            if(w!=h || w!=size)
                throw new RuntimeException("Image size mismatch");

            int[] idata = img.getRGB(0,0,w,h,null,0,w);
            //not flipped vertically!
            for(int q : idata ){
                A.write( (q>>16)&0xff );//r
                A.write( (q>>8 )&0xff );//g
                A.write( (q    )&0xff );//b
                A.write( (q>>24)&0xff );//a
            }
            glTexImage2D(GL_TEXTURE_CUBE_MAP_POSITIVE_X+m,
                0,GL_RGBA,size,size,0,GL_RGBA,GL_UNSIGNED_BYTE,
                A.toByteArray());
        }

        
        if(isPowerOf2(size) ){
            glGenerateMipmap(GL_TEXTURE_CUBE_MAP);
            glTexParameteri(GL_TEXTURE_CUBE_MAP,GL_TEXTURE_WRAP_S,GL_CLAMP_TO_EDGE);
            glTexParameteri(GL_TEXTURE_CUBE_MAP,GL_TEXTURE_WRAP_T,GL_CLAMP_TO_EDGE);
            glTexParameteri(GL_TEXTURE_CUBE_MAP,GL_TEXTURE_MIN_FILTER,GL_LINEAR_MIPMAP_LINEAR);
            glTexParameteri(GL_TEXTURE_CUBE_MAP,GL_TEXTURE_MAG_FILTER,GL_LINEAR);
        }
        else{
            glTexParameteri(GL_TEXTURE_CUBE_MAP,GL_TEXTURE_WRAP_S,GL_CLAMP_TO_EDGE);
            glTexParameteri(GL_TEXTURE_CUBE_MAP,GL_TEXTURE_WRAP_T,GL_CLAMP_TO_EDGE);
            glTexParameteri(GL_TEXTURE_CUBE_MAP,GL_TEXTURE_MIN_FILTER,GL_LINEAR);
            glTexParameteri(GL_TEXTURE_CUBE_MAP,GL_TEXTURE_MAG_FILTER,GL_LINEAR);
        }
        
        unbind(0);
    }
};
        
