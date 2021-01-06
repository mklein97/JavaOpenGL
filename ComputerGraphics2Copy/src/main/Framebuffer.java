package main;

import static etgg.GL.GL_COLOR_ATTACHMENT0;
import static etgg.GL.GL_COLOR_BUFFER_BIT;
import static etgg.GL.GL_DEPTH_BUFFER_BIT;
import static etgg.GL.GL_DEPTH_STENCIL_ATTACHMENT;
import static etgg.GL.GL_FRAMEBUFFER;
import static etgg.GL.GL_VIEWPORT;
import static etgg.GL.glBindFramebuffer;
import static etgg.GL.glClear;
import static etgg.GL.glDrawBuffers;
import static etgg.GL.glFramebufferTextureLayer;
import static etgg.GL.glGetIntegerv;
import static etgg.GL.glViewport;
import java.util.TreeMap;
import static main.Framebuffer2D.active_target;

/**
 *
 * @author jhudson
 */
public abstract class Framebuffer {

    //tells which FBO is currently active, or null if none
    static Framebuffer active_target;

    //saved viewport (the one that was active before we switched
    //to this RT
    static int[] saved_viewport = new int[4];

    int[] drawbuffers;

    //GL identifier
    int fbo;

    //internal format (ex; GL_RGBA8)
    protected int format;

    class Triple implements Comparable<Triple> {
        int w, h, fmt;
        Triple(int w, int h, int fmt) {
            this.w = w;
            this.h = h;
            this.fmt = fmt;
        }
        @Override
        public int compareTo(Triple t) {
            if (w != t.w) {
                return w - t.w;
            }
            if (h != t.h) {
                return h - t.h;
            }
            return fmt - t.fmt;
        }
    }
    static TreeMap<Triple, Framebuffer2D> blurHelpers = new TreeMap<>();
    static TreeMap<Integer, float[]> blurWeights = new TreeMap<>();
    static final int MAX_BLUR_RADIUS = 30;
    static UnitSquare blurUsq;

    Framebuffer(int fmt) {
        format = fmt;
    }

    //Determine if it's ok to bind this fbo for writing
    private void checkOkToBind() {
        if (active_target != null) {
            throw new RuntimeException("Another FBO is already bound");
        }
        if (!getTexture().on_units.isEmpty()) {
            throw new RuntimeException("This FBO has textures that are active");
        }
    }

    abstract Texture getTexture();

    abstract Texture getDepthTexture();

    /**
     * Bind all layers of this FBO as the render target.
     *
     * @param clearIt If true, the FBO is cleared after binding.
     */
    void bind(boolean clearIt) {
        bindLayer(clearIt, -1);
    }

    /**
     * Bind one layer of this FBO as render target attachment zero.
     *
     * @param clearIt If true, the layer is cleared after binding.
     * @param layer The layer to bind. -1 = Leave the layer binding alone.
     */
    void bindLayer(boolean clearIt, int layer) {
        if (active_target != null) {
            active_target.unbind();
        }
        checkOkToBind();
        active_target = this;
        glBindFramebuffer(GL_FRAMEBUFFER, fbo);

        if (layer != -1) {
            glFramebufferTextureLayer(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0,
                    getTexture().tex, 0, layer);
            glFramebufferTextureLayer(GL_FRAMEBUFFER, GL_DEPTH_STENCIL_ATTACHMENT,
                    getDepthTexture().tex, 0, layer);
        }

        glDrawBuffers(drawbuffers.length, drawbuffers);

        glGetIntegerv(GL_VIEWPORT, saved_viewport);

        setViewport();

        if (clearIt) {
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        }
    }

    abstract void dump(String filename);

    abstract int getWidth();

    abstract int getHeight();

    void setViewport() {
        glViewport(0, 0, getWidth(), getHeight());
    }

    /**
     * Unbind this FBO: It will no longer be a render target.
     *
     */
    void unbind() {
        if (active_target != this) {
            throw new RuntimeException("This FBO is not bound");
        }
        active_target = null;
        glBindFramebuffer(GL_FRAMEBUFFER, 0);
        glViewport(
                saved_viewport[0], saved_viewport[1],
                saved_viewport[2], saved_viewport[3]);
    }

    /** Common to 2d and cube framebuffers, so we put it here*/
    protected float[] computeBlurWeights(int radius) {
        if (blurWeights.containsKey(radius)) {
            return blurWeights.get(radius);
        }

        float[] F = new float[MAX_BLUR_RADIUS];
        float sigma = radius / 3.0f;
        float sum = 0.0f;
        for (int i = 0; i <= radius; ++i) {
            //box blur
            //float tmp = 1.0f/(radius+radius+1);
            //F[i] = tmp;
            //Gaussian blur
            float Q = i * i / (-2 * sigma * sigma);
            F[i] = (float) (Math.exp(Q) / (sigma * Math.sqrt(2 * Math.PI)));
            if (i == 0) {
                sum += F[i];
            } else {
                sum += 2.0 * F[i];
            }
        }
        for (int i = 0; i <= radius; ++i) {
            F[i] /= sum;
        }
        blurWeights.put(radius, F);
        return F;
    }
}