package javaapplication1;

import etgg.GL;
import static etgg.GL.GL_CLAMP_TO_EDGE;
import static etgg.GL.GL_LINEAR;
import static etgg.GL.GL_LINEAR_MIPMAP_LINEAR;
import static etgg.GL.GL_REPEAT;
import static etgg.GL.GL_RGBA;
import static etgg.GL.GL_TEXTURE_2D;
import static etgg.GL.GL_TEXTURE_2D_ARRAY;
import static etgg.GL.GL_TEXTURE_MAG_FILTER;
import static etgg.GL.GL_TEXTURE_MIN_FILTER;
import static etgg.GL.GL_TEXTURE_WRAP_S;
import static etgg.GL.GL_TEXTURE_WRAP_T;
import static etgg.GL.glGenTextures;
import static etgg.GL.glGenerateMipmap;
import static etgg.GL.glTexImage3D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import javax.imageio.ImageIO;
import static javaapplication1.Texture.isPowerOf2;

/**
 *
 * @author jhudson
 */
public class ImageTexture2DArray extends Texture2DArray {

    String filename;

    ImageTexture2DArray(String filename) {
        try {
            this.filename = filename;

            int[] tmp = new int[1];
            glGenTextures(1, tmp);
            this.tex = tmp[0];

            ArrayList<BufferedImage> images = new ArrayList<>();

            if (filename.endsWith(".zip") || filename.endsWith(".ora")) {
                ZipFile zf = new ZipFile(filename);
                Object[] o = zf.stream().toArray();
                ZipEntry[] ze = new ZipEntry[o.length];
                for (int i = 0; i < o.length; ++i) {
                    ze[i] = (ZipEntry) o[i];
                }
                Arrays.sort(ze, (ZipEntry a, ZipEntry b) -> {
                    return a.getName().compareTo(b.getName());
                });
                for (ZipEntry zz : ze) {
                    if (zz.getName().contains("Thumbnails/")) {
                        continue;
                    } else if (zz.getName().endsWith(".png")
                            || zz.getName().endsWith(".jpg")) {
                        images.add(ImageIO.read(zf.getInputStream(zz)));
                    }
                }
            } else {
                images.add(ImageIO.read(new File(filename)));
            }

            this.sloices = images.size();

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            for (BufferedImage img : images) {

                if (this.w == 0) {
                    this.w = img.getWidth();
                    this.h = img.getHeight();
                } else if (img.getWidth() != this.w || img.getHeight() != this.h) {
                    throw new RuntimeException("Get some artists who know how to draw!");
                }

                int[] pix = img.getRGB(0, 0, w, h, null, 0, w);

                for (int y = h - 1, j = 0; y >= 0; --y) {
                    //want i to refer to index in pix that corresponds to
                    //row i of image
                    int i = w * y;
                    for (int x = 0; x < w; ++x, ++i) {
                        byte b = (byte) ((pix[i]) & 0xff);
                        byte g = (byte) ((pix[i] >> 8) & 0xff);
                        byte r = (byte) ((pix[i] >> 16) & 0xff);
                        byte a = (byte) ((pix[i] >> 24) & 0xff);
                        baos.write(r);
                        baos.write(g);
                        baos.write(b);
                        baos.write(a);
                    }
                }

            }

            bind(0);
            glTexImage3D(GL_TEXTURE_2D_ARRAY,
                    0, //mipmap level 
                    GL_RGBA, //how the gpu should store internally
                    w, h, sloices,
                    0, //border
                    GL_RGBA, GL.GL_UNSIGNED_BYTE, baos.toByteArray() //byte array of image data
            );

            if (isPowerOf2(w) && isPowerOf2(h)) {
                glGenerateMipmap(GL_TEXTURE_2D_ARRAY);
                setParameter(GL_TEXTURE_WRAP_S, GL_REPEAT);
                setParameter(GL_TEXTURE_WRAP_T, GL_REPEAT);
                setParameter(GL_TEXTURE_MIN_FILTER, GL_LINEAR_MIPMAP_LINEAR);
                setParameter(GL_TEXTURE_MAG_FILTER, GL_LINEAR);
            } else {
                setParameter(GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
                setParameter(GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
                setParameter(GL_TEXTURE_MIN_FILTER, GL_LINEAR);
                setParameter(GL_TEXTURE_MAG_FILTER, GL_LINEAR);
            }
            unbind(0);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
