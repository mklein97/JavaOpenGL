/*
 */
package main;

import static etgg.GL.GL_CLAMP_TO_EDGE;
import static etgg.GL.GL_LINEAR;
import static etgg.GL.GL_LINEAR_MIPMAP_LINEAR;
import static etgg.GL.GL_REPEAT;
import static etgg.GL.GL_RGBA;
import static etgg.GL.GL_TEXTURE_2D_ARRAY;
import static etgg.GL.GL_TEXTURE_MAG_FILTER;
import static etgg.GL.GL_TEXTURE_MIN_FILTER;
import static etgg.GL.GL_TEXTURE_WRAP_S;
import static etgg.GL.GL_TEXTURE_WRAP_T;
import static etgg.GL.GL_UNSIGNED_BYTE;
import static etgg.GL.glGenTextures;
import static etgg.GL.glGenerateMipmap;
import static etgg.GL.glTexImage3D;
import static etgg.GL.glTexParameteri;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import javax.imageio.ImageIO;

/**
 *
 * @author jhudson
 */
public class ImageTexture2DArray extends Texture2DArray {
    String filename;
    
//    static String[] glob(String pattern){
//        ArrayList<String> A = new ArrayList<>();
//        for(int i=0; ; i++){
//            String fname = String.format(pattern,i);
//            File f = new File(fname);
//            if( f.exists() )
//                A.add(fname);
//            else
//                break;
//        }
//        String[] ret = new String[A.size()];
//        for(int i=0;i<A.size();++i)
//            ret[i] = A.get(i);
//        return ret;
//    }
    
    ImageTexture2DArray(String filename){
        
        try{
            //for debugging, etc.
            this.filename = filename;

            int[] tmp = new int[1];
            glGenTextures(1,tmp);
            tex = tmp[0];

            ByteArrayOutputStream A = new ByteArrayOutputStream();
            ArrayList<BufferedImage> frames = new ArrayList<>();
            if( filename.endsWith(".zip") || filename.endsWith(".ora")){
                ZipFile zf = new ZipFile(filename);
                Object[] o = zf.stream().toArray();
                ZipEntry[] ze = new ZipEntry[o.length];
                for(int i=0;i<o.length;++i)
                    ze[i] = (ZipEntry)o[i];
                Arrays.sort(ze, (ZipEntry a, ZipEntry b) -> {
                    return a.getName().compareTo(b.getName());
                });
                for( ZipEntry x : ze ){
                    if( x.getName().contains("Thumbnails/"))
                        continue;   //skip ORA thumbnail
                    if( x.getName().endsWith(".png") || x.getName().endsWith(".jpg")){
                        //System.out.println("File:"+x.getName());
                        frames.add(ImageIO.read(zf.getInputStream(x)));
                    }
                }
            }
            else{
                frames.add(ImageIO.read(new File(filename)));
            }

            this.slices = frames.size();
            
            for(BufferedImage img : frames ){
                if( this.w == 0 ){
                    this.w = img.getWidth();
                    this.h = img.getHeight();
                }
                else{
                    if( img.getWidth() != this.w || img.getHeight() != this.h )
                        throw new RuntimeException("Image size mismatch: "+filename+": "+w+"x"+h+" "+img.getWidth()+"x"+img.getHeight());
                }

                int[] idata = img.getRGB(0,0,this.w,this.h,null,0,this.w);
                for(int y=0;y<this.h;++y){
                    int idx = (this.h-1-y)*this.w;
                    for(int x=0;x<this.w;++x,++idx){
                        int q = idata[idx];
                        A.write( (q>>16)&0xff );//r
                        A.write( (q>>8 )&0xff );//g
                        A.write( (q    )&0xff );//b
                        A.write( (q>>24)&0xff );//a
                    }
                }
            }

            bind(0);
            glTexImage3D(GL_TEXTURE_2D_ARRAY, 0, GL_RGBA, this.w, this.h, slices, 0,
                    GL_RGBA, GL_UNSIGNED_BYTE, A.toByteArray() );
            if( isPowerOf2(this.w) && isPowerOf2(this.h) ){
                glGenerateMipmap(GL_TEXTURE_2D_ARRAY);
                glTexParameteri(GL_TEXTURE_2D_ARRAY, GL_TEXTURE_WRAP_S, GL_REPEAT);
                glTexParameteri(GL_TEXTURE_2D_ARRAY, GL_TEXTURE_WRAP_T, GL_REPEAT);
                glTexParameteri(GL_TEXTURE_2D_ARRAY, GL_TEXTURE_MIN_FILTER, GL_LINEAR_MIPMAP_LINEAR);
                glTexParameteri(GL_TEXTURE_2D_ARRAY, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
            }
            else{
                glTexParameteri(GL_TEXTURE_2D_ARRAY, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
                glTexParameteri(GL_TEXTURE_2D_ARRAY, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
                glTexParameteri(GL_TEXTURE_2D_ARRAY, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
                glTexParameteri(GL_TEXTURE_2D_ARRAY, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
            }
            unbind(0);
        } catch(IOException e){
            System.out.println("Cannot read file "+filename);
            throw new RuntimeException(e);
        }
    }
}

