package main;

import static etgg.GL.GL_CLAMP_TO_EDGE;
import static etgg.GL.GL_FLOAT;
import static etgg.GL.GL_NEAREST;
import static etgg.GL.GL_R32F;
import static etgg.GL.GL_RED;
import static etgg.GL.GL_RG;
import static etgg.GL.GL_RG32F;
import static etgg.GL.GL_RGB;
import static etgg.GL.GL_RGB32F;
import static etgg.GL.GL_RGBA;
import static etgg.GL.GL_RGBA32F;
import static etgg.GL.GL_TEXTURE_2D_ARRAY;
import static etgg.GL.GL_TEXTURE_MAG_FILTER;
import static etgg.GL.GL_TEXTURE_MIN_FILTER;
import static etgg.GL.GL_TEXTURE_WRAP_S;
import static etgg.GL.GL_TEXTURE_WRAP_T;
import static etgg.GL.GL_UNSIGNED_BYTE;
import static etgg.GL.glGenTextures;
import static etgg.GL.glTexImage3D;
import static etgg.GL.glTexParameteri;
import static etgg.GL.glTexSubImage3D;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import math3d.mat4;
import math3d.vec2;
import math3d.vec3;
import math3d.vec4;

/**
 *
 * @author jhudson
 */
public class DataTexture2DArray extends Texture2DArray{
    int channels;
    int updateFmt;
    int bytesPerChannel;
    
    /**Create the data texture.
     * 
     * @param width The width of the texture.
     * @param height  The height of the texture.
     * @param slices The number of slices in the texture.
     * @param iformat  The storage format. Should be GL_R32F, GL_RG32F, GL_RGB32F, or GL_RGBA32F
     */
    DataTexture2DArray(int width, int height, int slices, int iformat){
        init(width,height,slices,iformat);
    }
    
     /**Create the data texture.
     * 
     * @param width The width of the texture.
     * @param height  The height of the texture.
     * @param slices The number of slices in the texture.
     * @param data The initial data
     */
    DataTexture2DArray(int width, int height, int slices, float[] data){
        init(width,height,slices,GL_R32F);
        update(data);
    }
    DataTexture2DArray(int width, int height, int slices, vec2[] data){
        init(width,height,slices,GL_RG32F);
        update(data);
    }
    DataTexture2DArray(int width, int height, int slices, vec3[] data){
        init(width,height,slices,GL_RGB32F);
        update(data);
    }
    DataTexture2DArray(int width, int height, int slices, vec4[] data){
        init(width,height,slices,GL_RGBA32F);
        update(data);
    }
    DataTexture2DArray(int width, int height, int slices, mat4[] data){
        init(width,height,slices,GL_RGBA32F);
        update(data);
    }
    
    private void init(int width, int height, int slices, int iformat){
        this.channels = channels;
        this.updateFmt = updateFmt;
        switch(iformat){
            case GL_R32F:
                channels=1; 
                updateFmt = GL_RED;
                bytesPerChannel=4;
                break;
            case GL_RG32F:
                channels=2; 
                updateFmt = GL_RG;
                bytesPerChannel=4;
                break;
            case GL_RGB32F:
                channels=3; 
                updateFmt = GL_RGB;
                bytesPerChannel=4;
                break;
            case GL_RGBA32F:
                channels=4; 
                updateFmt = GL_RGBA;
                bytesPerChannel=4;
                break;
            default:
                throw new RuntimeException("Unknown internal format");
        }
        
        this.w=width;
        this.h=height;
        this.slices=slices;
        int[] tmp = new int[1];
        glGenTextures(1,tmp);
        this.tex = tmp[0];
        bind(0);
        glTexImage3D(GL_TEXTURE_2D_ARRAY, 0, iformat, 
                this.w, this.h, this.slices, 0,
            GL_RGBA, GL_UNSIGNED_BYTE, (byte[]) null );
        glTexParameteri(GL_TEXTURE_2D_ARRAY, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
        glTexParameteri(GL_TEXTURE_2D_ARRAY, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
        glTexParameteri(GL_TEXTURE_2D_ARRAY, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D_ARRAY, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
        unbind(0);
    }
    
    private void update(byte[] data){
        int expected = w*h*slices*channels*bytesPerChannel;
        if( data.length != expected )
            throw new RuntimeException("Data length mismatch: Expecting "+expected+" bytes got "+data.length+" bytes");
        bind(0);
        //mip level, {x,y,z} offsets, w,h,depth, format, type, data
        glTexSubImage3D(GL_TEXTURE_2D_ARRAY, 0, 
            0,0,0,
            w,h,slices,
            updateFmt, GL_FLOAT, data);
        
        unbind(0);
    }
    
    void update(float[] data){
        ByteBuffer tmp = ByteBuffer.allocate(data.length*4);
        tmp.order(ByteOrder.nativeOrder());
        FloatBuffer tmpf = tmp.asFloatBuffer();
        tmpf.put(data);
        update(tmp.array());
    }
                
    void update(mat4[] data){
        //x = matrix number
        //y = row
        //rgba = column
        if( channels != 4 )
            throw new RuntimeException("Oops.");
        float[] tmp = new float[data.length*4*4];
        int k=0;
        for(mat4 M : data ){
            for(int i=0;i<4;++i){
                for(int j=0;j<4;++j){
                    tmp[k++] = M._M[i*4+j];
                }
            }
        }
        update(tmp);
    }
    void update(vec2[] data){
        if( channels != 2 )
            throw new RuntimeException("Oops.");
        float[] tmp = new float[data.length*2];
        int k=0;
        for(vec2 v : data ){
            tmp[k++] = v.x;
            tmp[k++] = v.y;
        }
        update(tmp);
    }
    void update(vec3[] data){
        if( channels != 3 )
            throw new RuntimeException("Oops.");
        float[] tmp = new float[data.length*3];
        int k=0;
        for(vec3 v : data ){
            tmp[k++] = v.x;
            tmp[k++] = v.y;
            tmp[k++] = v.z;
        }
        update(tmp);
    }
    void update(vec4[] data){
        if( channels != 4 )
            throw new RuntimeException("Oops.");
        float[] tmp = new float[data.length*4];
        int k=0;
        for(vec4 v : data ){
            tmp[k++] = v.x;
            tmp[k++] = v.y;
            tmp[k++] = v.z;
            tmp[k++] = v.w;
        }
        update(tmp);
    }
}