package main;

import static etgg.GL.GL_CLAMP_TO_EDGE;
import static etgg.GL.GL_COLOR_ATTACHMENT0;
import static etgg.GL.GL_DEPTH24_STENCIL8;
import static etgg.GL.GL_DEPTH_STENCIL;
import static etgg.GL.GL_DEPTH_STENCIL_ATTACHMENT;
import static etgg.GL.GL_FLOAT;
import static etgg.GL.GL_FRAMEBUFFER;
import static etgg.GL.GL_FRAMEBUFFER_BINDING;
import static etgg.GL.GL_FRAMEBUFFER_COMPLETE;
import static etgg.GL.GL_LINEAR;
import static etgg.GL.GL_NEAREST;
import static etgg.GL.GL_RGBA;
import static etgg.GL.GL_TEXTURE_2D_ARRAY;
import static etgg.GL.GL_TEXTURE_BINDING_CUBE_MAP;
import static etgg.GL.GL_TEXTURE_CUBE_MAP;
import static etgg.GL.GL_TEXTURE_CUBE_MAP_POSITIVE_X;
import static etgg.GL.GL_TEXTURE_MAG_FILTER;
import static etgg.GL.GL_TEXTURE_MIN_FILTER;
import static etgg.GL.GL_TEXTURE_WRAP_R;
import static etgg.GL.GL_TEXTURE_WRAP_S;
import static etgg.GL.GL_TEXTURE_WRAP_T;
import static etgg.GL.GL_UNSIGNED_BYTE;
import static etgg.GL.GL_UNSIGNED_INT_24_8;
import static etgg.GL.glBindFramebuffer;
import static etgg.GL.glBindTexture;
import static etgg.GL.glCheckFramebufferStatus;
import static etgg.GL.glFramebufferTextureLayer;
import static etgg.GL.glGenFramebuffers;
import static etgg.GL.glGenTextures;
import static etgg.GL.glGetIntegerv;
import static etgg.GL.glGetTexImage;
import static etgg.GL.glTexImage2D;
import static etgg.GL.glTexParameteri;
import static etgg.GL.glTexStorage2D;
import static etgg.GL.glTextureView;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import javax.imageio.ImageIO;
import math3d.vec2;


public class FramebufferCube extends Framebuffer{
   
    //texture which belongs to this fbo
    TextureCube texture;
    
    //z buffer + stencil
    TextureCube depthtexture;

    //used for blurring. Requires GL 4.3+
    Texture2DArray textureView;
    Framebuffer2D viewFBO;
    
    static Program cubeBlurProgram;

    /**Create a cube map framebuffer object.
     * 
     * @param size The size of the cube, in pixels
     * @param format The format. Ex: GL_RGBA8 or GL_RG32F
     * @param blurrable True if you need to call blur() on this FBO. This requires
     *                  OpenGL 4.3 or later.
     */
    public FramebufferCube(int size, int format, boolean blurrable){
        super(format);
        
        int[] tmp = new int[1];

        texture = new TextureCube();
        glGenTextures(1,tmp);
        texture.tex = tmp[0];
        texture.bind(0);
        texture.size = size;
        texture.owning_fbo = this;
        
        if( blurrable ){
            //int numLevels = (int)(Math.floor(Math.log(size)/Math.log(2))+1);
            glTexStorage2D(GL_TEXTURE_CUBE_MAP, 1,format,size,size);
        } else { 
            for(int i=0;i<6;++i){
                //face, mip level, internal format, w,h, border, external data format
                glTexImage2D(GL_TEXTURE_CUBE_MAP_POSITIVE_X+i, 0, format, size,size,0, 
                        GL_RGBA, GL_UNSIGNED_BYTE, (byte[])null );
            }
        }
        glTexParameteri(GL_TEXTURE_CUBE_MAP , GL_TEXTURE_MIN_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_CUBE_MAP , GL_TEXTURE_MAG_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_CUBE_MAP , GL_TEXTURE_WRAP_R, GL_CLAMP_TO_EDGE);
        glTexParameteri(GL_TEXTURE_CUBE_MAP , GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
        glTexParameteri(GL_TEXTURE_CUBE_MAP , GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
        texture.unbind(0);
        
        depthtexture = new TextureCube();
        glGenTextures(1,tmp);
        depthtexture.tex = tmp[0];
        depthtexture.bind(0);
        depthtexture.size = size;
        depthtexture.owning_fbo = this;
        for(int i=0;i<6;++i){
            glTexImage2D(GL_TEXTURE_CUBE_MAP_POSITIVE_X+i , 0, GL_DEPTH24_STENCIL8, 
                size,size,0,GL_DEPTH_STENCIL, GL_UNSIGNED_INT_24_8, (byte[])null );
        }
        glTexParameteri(GL_TEXTURE_CUBE_MAP , GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_CUBE_MAP , GL_TEXTURE_MAG_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_CUBE_MAP , GL_TEXTURE_WRAP_R, GL_CLAMP_TO_EDGE);
        glTexParameteri(GL_TEXTURE_CUBE_MAP , GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
        glTexParameteri(GL_TEXTURE_CUBE_MAP , GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
        depthtexture.unbind(0);
        
        glGenFramebuffers(1,tmp);
        fbo = tmp[0];
        glBindFramebuffer(GL_FRAMEBUFFER, fbo );
        
        //dummy attachment for validation
        glFramebufferTextureLayer(GL_FRAMEBUFFER,GL_COLOR_ATTACHMENT0, texture.tex, 0, 0);
        glFramebufferTextureLayer(GL_FRAMEBUFFER,GL_DEPTH_STENCIL_ATTACHMENT, depthtexture.tex, 0, 0);

        int complete = glCheckFramebufferStatus(GL_FRAMEBUFFER);
        if( complete != GL_FRAMEBUFFER_COMPLETE ){
            throw new RuntimeException(String.format(
                    "Framebuffer is not complete: %d 0x%x",complete,complete));
        }
        
        drawbuffers = new int[1];
        for(int i=0;i<drawbuffers.length;++i)
            drawbuffers[i] = GL_COLOR_ATTACHMENT0+i;
        
        glBindFramebuffer(GL_FRAMEBUFFER,0);
        
        if( blurrable ){
            textureView = new Texture2DArray();
            glGenTextures(1,tmp);
            textureView.tex = tmp[0];
            glTextureView(textureView.tex,GL_TEXTURE_2D_ARRAY,texture.tex,format,0,1,0,6);
            textureView.w=size;
            textureView.h=size;
            textureView.slices=6;
            textureView.owning_fbo=this;
            textureView.bind(0);
            glTexParameteri(GL_TEXTURE_2D_ARRAY , GL_TEXTURE_MIN_FILTER, GL_LINEAR);
            glTexParameteri(GL_TEXTURE_2D_ARRAY , GL_TEXTURE_MAG_FILTER, GL_LINEAR);
            glTexParameteri(GL_TEXTURE_2D_ARRAY , GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
            glTexParameteri(GL_TEXTURE_2D_ARRAY , GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
            textureView.unbind();
            viewFBO = new Framebuffer2D(textureView,format);
        }
    }
    @Override
    int getWidth(){
        return texture.size;
    }
    @Override
    int getHeight(){
        return texture.size;
    }
    @Override
    Texture getTexture(){
        return texture;
    }
    @Override
    Texture getDepthTexture(){
        return depthtexture;
    }
    @Override
    void dump(String filename){
        int[] oldFBinding = new int[1];
        glGetIntegerv(GL_FRAMEBUFFER_BINDING, oldFBinding);
        //System.out.println("DEBUG: Current framebuffer is "+tmp[0]);
        glBindFramebuffer(GL_FRAMEBUFFER,0);
        int[] oldCBinding = new int[1];
        byte[][] B = new byte[6][];
        float[][] F = new float[6][texture.size*texture.size*4];
        ByteBuffer xBuff = ByteBuffer.allocate(texture.size*texture.size*4*4);
        xBuff.order(ByteOrder.nativeOrder());
        FloatBuffer fBuff = xBuff.asFloatBuffer();
        
        glGetIntegerv(GL_TEXTURE_BINDING_CUBE_MAP, oldCBinding);
        glBindTexture(GL_TEXTURE_CUBE_MAP, this.texture.tex);

        for(int i=0;i<6;++i){
            byte[] BB = new byte[texture.size*texture.size*4];
            B[i] = BB;
            glGetTexImage(GL_TEXTURE_CUBE_MAP_POSITIVE_X+i, 0, GL_RGBA, GL_UNSIGNED_BYTE, BB);
            byte[] XX = new byte[texture.size*texture.size*4*4];
            glGetTexImage(GL_TEXTURE_CUBE_MAP_POSITIVE_X+i, 0, GL_RGBA, GL_FLOAT, XX);
            xBuff.rewind();
            xBuff.put(XX);
            fBuff.rewind();
            fBuff.get(F[i]);
        }
        glBindTexture(GL_TEXTURE_CUBE_MAP,oldCBinding[0]);
        glBindFramebuffer(GL_FRAMEBUFFER,oldFBinding[0]);
        
        int[] rgb = new int[texture.size*texture.size];
        for(int i=0;i<6;++i){
            BufferedImage img = new BufferedImage(texture.size,texture.size,BufferedImage.TYPE_INT_ARGB);
            for(int k=0,y=0;y<texture.size;++y){
                // (h-1-y) * w
                int m = (texture.size-1-y)*texture.size;
                for(int x=0;x<texture.size;++x){
                    int r = B[i][k++];
                    r &= 0xff;
                    int g = B[i][k++];
                    g &= 0xff;
                    int b = B[i][k++];
                    b &= 0xff;
                    int a = B[i][k++];
                    a &= 0xff;
                    rgb[m++] = (a<<24) | (r<<16) | (g<<8) | b;
                }
            }
            img.setRGB(0,0,img.getWidth(),img.getHeight(),rgb,0,img.getWidth());
            try{
                String fn = filename+"-cubeside"+i+".png";
                ImageIO.write(img, "png", new File(fn) );
                System.out.println("Wrote "+fn);
            }catch(IOException e){
                throw new RuntimeException(e);
            }
        }
    }
    
    
     protected void blur(int radius,  float colorScale) {
        if (radius <= 0) {
            return;
        }

        computeBlurWeights(radius);

        Triple tr = new Triple(-getWidth(), getHeight(), format);
        if (!blurHelpers.containsKey(tr)) {
            blurHelpers.put(tr,
                    new Framebuffer2D(getWidth(), getHeight(), this.format, 6));
        }

        if (cubeBlurProgram == null) {
            cubeBlurProgram = new Program("blurcubeprog.vs", null, "blurcubeprog.fs");
        }

        if (blurUsq == null) {
            blurUsq = new UnitSquare();
        }

        Framebuffer2D helper = blurHelpers.get(tr);

        Program oldProg = Program.current;
        Framebuffer oldfbo = active_target;

        cubeBlurProgram.use();
        Program.current.setUniform("blurWeights", blurWeights.get(radius));
        Program.current.setUniform("blurRadius", radius);

        helper.bind(true);
        Program.current.setUniform("tex", textureView);
        Program.current.setUniform("blurDeltas", new vec2(1.0 / getWidth(), 0));
        Program.current.setUniform("blurColorScale", colorScale);
        blurUsq.draw();
        textureView.unbind();
        helper.unbind();

        viewFBO.bind(true);
        Program.current.setUniform("tex", helper.texture);
        Program.current.setUniform("blurDeltas", new vec2(0, 1.0 / getHeight()));
        Program.current.setUniform("blurColorScale", 1);
        blurUsq.draw();
        viewFBO.unbind();
        helper.texture.unbind();

        if( oldProg != null )
            oldProg.use();
        if (oldfbo != null) {
            oldfbo.bind(false);
        }
    }
};
