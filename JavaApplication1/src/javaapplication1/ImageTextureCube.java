package javaapplication1;

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
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import static javaapplication1.Texture.isPowerOf2;

public class ImageTextureCube extends TextureCube {

    //+x, -x, +y, -y, +z, -z
    ImageTextureCube(String... filenames) {
        if (filenames.length != 6) {
            throw new RuntimeException("Naughty, naughty!");
        }

        int[] tmp = new int[1];
        glGenTextures(1, tmp);
        this.tex = tmp[0];
        bind(0);

        try {
            for (int k = 0; k < 6; ++k) {
                BufferedImage img = ImageIO.read(new File(filenames[k]));

                int w = img.getWidth();
                int h = img.getHeight();
                if (w != h) {
                    throw new RuntimeException("Sizes don't match");
                }
                if (k == 0) {
                    this.size = w;
                } else if (w != this.size) {
                    throw new RuntimeException("Sizes don't match #2");
                }

                int[] pix = img.getRGB(0, 0, w, h, null, 0, w);
                byte[] bpix = new byte[w * h * 4];

                for (int y = 0, j = 0; y < h; y++) {
                    //want i to refer to index in pix that corresponds to
                    //row i of image
                    int i = w * y;
                    for (int x = 0; x < w; ++x, ++i) {
                        byte b = (byte) ((pix[i]) & 0xff);
                        byte g = (byte) ((pix[i] >> 8) & 0xff);
                        byte r = (byte) ((pix[i] >> 16) & 0xff);
                        byte a = (byte) ((pix[i] >> 24) & 0xff);
                        bpix[j++] = r;
                        bpix[j++] = g;
                        bpix[j++] = b;
                        bpix[j++] = a;
                    }
                }
                glTexImage2D(
                        GL_TEXTURE_CUBE_MAP_POSITIVE_X + k,
                        0, //mipmap level 
                        GL_RGBA, //how the gpu should store internally
                        size, size,
                        0, //border
                        GL_RGBA, GL_UNSIGNED_BYTE, //format of data being passed in
                        bpix //byte array of image data
                );
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

      
            setParameter(GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
            setParameter(GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
            setParameter(GL_TEXTURE_MIN_FILTER, GL_LINEAR);
            setParameter(GL_TEXTURE_MAG_FILTER, GL_LINEAR);
        
        unbind(0);

    }
}
