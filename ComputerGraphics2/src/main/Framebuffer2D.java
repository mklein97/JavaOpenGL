package main;

import static etgg.GL.GL_CLAMP_TO_EDGE;
import static etgg.GL.GL_COLOR_ATTACHMENT0;
import static etgg.GL.GL_COLOR_BUFFER_BIT;
import static etgg.GL.GL_DEPTH24_STENCIL8;
import static etgg.GL.GL_DEPTH_BUFFER_BIT;
import static etgg.GL.GL_DEPTH_STENCIL;
import static etgg.GL.GL_DEPTH_STENCIL_ATTACHMENT;
import static etgg.GL.GL_FRAMEBUFFER;
import static etgg.GL.GL_FRAMEBUFFER_BINDING;
import static etgg.GL.GL_FRAMEBUFFER_COMPLETE;
import static etgg.GL.GL_LINEAR;
import static etgg.GL.GL_NEAREST;
import static etgg.GL.GL_RGBA;
import static etgg.GL.GL_TEXTURE_2D_ARRAY;
import static etgg.GL.GL_TEXTURE_BINDING_2D_ARRAY;
import static etgg.GL.GL_TEXTURE_MAG_FILTER;
import static etgg.GL.GL_TEXTURE_MIN_FILTER;
import static etgg.GL.GL_TEXTURE_WRAP_S;
import static etgg.GL.GL_TEXTURE_WRAP_T;
import static etgg.GL.GL_UNSIGNED_BYTE;
import static etgg.GL.GL_UNSIGNED_INT_24_8;
import static etgg.GL.glBindFramebuffer;
import static etgg.GL.glBindTexture;
import static etgg.GL.glCheckFramebufferStatus;
import static etgg.GL.glClear;
import static etgg.GL.glDrawBuffers;
import static etgg.GL.glFramebufferTextureLayer;
import static etgg.GL.glGenFramebuffers;
import static etgg.GL.glGenTextures;
import static etgg.GL.glGetIntegerv;
import static etgg.GL.glGetTexImage;
import static etgg.GL.glTexImage3D;
import static etgg.GL.glTexParameteri;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import math3d.vec2;

public class Framebuffer2D extends Framebuffer {

    //texture which belongs to this fbo
    Texture2DArray texture;

    //z buffer + stencil
    Texture2DArray depthtexture;

    //for blurring
    private static Program blurProgram;

    public Framebuffer2D(Texture2DArray tex, int format) {
        super(format);
        texture = tex;
        depthtexture = makeTexture(GL_DEPTH24_STENCIL8, tex.w, tex.h, 1, GL_NEAREST);
        init();
    }

    public Framebuffer2D(int width, int height, int format, int slices) {
        super(format);
        texture = makeTexture(format, width, height, slices, GL_LINEAR);
        depthtexture = makeTexture(GL_DEPTH24_STENCIL8, width, height, 1, GL_NEAREST);
        init();
    }

    private Texture2DArray makeTexture(int format, int width, int height, int slices, int filter) {
        Texture2DArray texture = new Texture2DArray();
        int[] tmp = new int[1];
        glGenTextures(1, tmp);
        texture.tex = tmp[0];
        texture.bind(0);
        texture.w = width;
        texture.h = height;
        texture.slices = slices;
        texture.owning_fbo = this;
        glTexImage3D(GL_TEXTURE_2D_ARRAY, 0, format, width, height, slices, 0,
                (format == GL_DEPTH24_STENCIL8) ? GL_DEPTH_STENCIL : GL_RGBA,
                (format == GL_DEPTH24_STENCIL8) ? GL_UNSIGNED_INT_24_8 : GL_UNSIGNED_BYTE,
                (byte[]) null);
        glTexParameteri(GL_TEXTURE_2D_ARRAY, GL_TEXTURE_MIN_FILTER, filter);
        glTexParameteri(GL_TEXTURE_2D_ARRAY, GL_TEXTURE_MAG_FILTER, filter);
        glTexParameteri(GL_TEXTURE_2D_ARRAY, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
        glTexParameteri(GL_TEXTURE_2D_ARRAY, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
        texture.unbind(0);
        return texture;
    }

    private void init() {
        int[] tmp = new int[1];
        glGenFramebuffers(1, tmp);
        fbo = tmp[0];
        glBindFramebuffer(GL_FRAMEBUFFER, fbo);

        for (int i = 0; i < texture.slices; ++i) {
            //target, attachment, texture, mip level, layer
            glFramebufferTextureLayer(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0 + i, texture.tex, 0, i);
        }
        glFramebufferTextureLayer(GL_FRAMEBUFFER, GL_DEPTH_STENCIL_ATTACHMENT, depthtexture.tex, 0, 0);

        int complete = glCheckFramebufferStatus(GL_FRAMEBUFFER);
        if (complete != GL_FRAMEBUFFER_COMPLETE) {
            throw new RuntimeException("Framebuffer is not complete: " + complete);
        }

        drawbuffers = new int[texture.slices];
        for (int i = 0; i < texture.slices; ++i) {
            drawbuffers[i] = GL_COLOR_ATTACHMENT0 + i;
        }

        glBindFramebuffer(GL_FRAMEBUFFER, 0);

    }

    @Override
    int getWidth() {
        return texture.w;
    }

    @Override
    int getHeight() {
        return texture.h;
    }

    @Override
    Texture getTexture() {
        return texture;
    }

    @Override
    Texture getDepthTexture() {
        return depthtexture;
    }

   /**
     * Blur this FBO.
     *
     * @param radius The blur radius
     * @param inputLayer Which layer (slice) to read from
     * @param outputLayer Which layer (slice) to write to
     * @param colorScale How much to scale the colors. Useful for emission maps.
     */
    protected void blur(int radius, int inputLayer, int outputLayer, float colorScale) {
        if (radius <= 0) {
            return;
        }

        computeBlurWeights(radius);

        Triple tr = new Triple(getWidth(), getHeight(), format);
        if (!blurHelpers.containsKey(tr)) {
            blurHelpers.put(tr,
                    new Framebuffer2D(getWidth(), getHeight(), this.format, 1));
        }

        if (blurProgram == null) {
            blurProgram = new Program("blurprog.vs", null, "blurprog.fs");
        }

        if (blurUsq == null) {
            blurUsq = new UnitSquare();
        }

        Framebuffer2D helper = blurHelpers.get(tr);

        Program oldProg = Program.current;
        Framebuffer oldfbo = active_target;

        blurProgram.use();
        blurProgram.setUniform("blurWeights", blurWeights.get(radius));
        blurProgram.setUniform("blurRadius", radius);

        Texture inputTexture = getTexture();
        helper.bind(true);
        blurProgram.setUniform("blurReadLayer", inputLayer);
        blurProgram.setUniform("tex", inputTexture);
        blurProgram.setUniform("blurDeltas", new vec2(1.0 / getWidth(), 0));
        blurProgram.setUniform("blurColorScale", colorScale);
        blurUsq.draw();
        inputTexture.unbind();
        helper.unbind();

        this.bind(false);
        int[] db = new int[]{GL_COLOR_ATTACHMENT0 + outputLayer};
        glDrawBuffers(db.length, db);
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        blurProgram.setUniform("blurReadLayer", 0);
        blurProgram.setUniform("tex", helper.texture);
        blurProgram.setUniform("blurDeltas", new vec2(0, 1.0 / getHeight()));
        blurProgram.setUniform("blurColorScale", 1);
        blurUsq.draw();
        this.unbind();
        helper.texture.unbind();

        oldProg.use();
        if (oldfbo != null) {
            oldfbo.bind(false);
        }
    }

    /**
     * Debugging. Dump the contents of this FBO's layers to a series of PNG files.
     */
    @Override
    void dump(String filename) {
        int[] tmp = new int[1];
        glGetIntegerv(GL_FRAMEBUFFER_BINDING, tmp);
        //System.out.println("DEBUG: Current framebuffer is "+tmp[0]);
        glBindFramebuffer(GL_FRAMEBUFFER, 0);
        byte[] B = new byte[texture.w * texture.h * 4 * texture.slices];
        int[] tmp2 = new int[1];
        glGetIntegerv(GL_TEXTURE_BINDING_2D_ARRAY, tmp2);
        glBindTexture(GL_TEXTURE_2D_ARRAY, this.texture.tex);
        glGetTexImage(GL_TEXTURE_2D_ARRAY, 0, GL_RGBA, GL_UNSIGNED_BYTE, B);
        glBindTexture(GL_TEXTURE_2D_ARRAY, tmp2[0]);
        glBindFramebuffer(GL_FRAMEBUFFER, tmp[0]);

        int[] rgb = new int[texture.w * texture.h];
        int k = 0;
        for (int i = 0; i < texture.slices; ++i) {
            BufferedImage img = new BufferedImage(texture.w, texture.h, BufferedImage.TYPE_INT_ARGB);
            for (int y = 0; y < texture.h; ++y) {
                int m = (texture.h - 1 - y) * texture.w;
                for (int x = 0; x < texture.w; ++x) {
                    int r = B[k++];
                    r &= 0xff;
                    int g = B[k++];
                    g &= 0xff;
                    int b = B[k++];
                    b &= 0xff;
                    int a = B[k++];
                    a &= 0xff;
                    rgb[m++] = (a << 24) | (r << 16) | (g << 8) | b;
                }
            }
            img.setRGB(0, 0, img.getWidth(), img.getHeight(), rgb, 0, img.getWidth());
            try {
                String fn = filename + "-layer" + i + ".png";
                ImageIO.write(img, "png", new File(fn));
                System.out.println("Wrote " + fn);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

    }
};