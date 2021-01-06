package javaapplication1;

import etgg.GL;
import static etgg.GL.GL_ARRAY_BUFFER;
import static etgg.GL.GL_ELEMENT_ARRAY_BUFFER;
import static etgg.GL.GL_FLOAT;
import static etgg.GL.GL_STATIC_DRAW;
import static etgg.GL.GL_TRIANGLES;
import static etgg.GL.GL_UNSIGNED_INT;
import static etgg.GL.GL_VERTEX_ARRAY;
import static etgg.GL.glBindBuffer;
import static etgg.GL.glBindVertexArray;
import static etgg.GL.glBufferData;
import static etgg.GL.glDrawElements;
import static etgg.GL.glEnableVertexAttribArray;
import static etgg.GL.glGenBuffers;
import static etgg.GL.glGenVertexArrays;
import static etgg.GL.glVertexAttribPointer;

/**
 *
 * @author jhudson
 */
public class Bullet {

    private int vao;

    public Bullet() {
        float[] vdata;
        vdata = new float[]{
            // front
            -0.01f, -0.01f, 0.01f,
            0.01f, -0.01f, 0.01f,
            0.01f, 0.01f, 0.01f,
            -0.01f, 0.01f, 0.01f,
            // back
            -0.01f, -0.01f, -0.01f,
            0.01f, -0.01f, -0.01f,
            0.01f, 0.01f, -0.01f,
            -0.01f, 0.01f, -0.01f
        };
        
        int[] idata = new int[]{
            // front
            0, 1, 2,
            2, 3, 0,
            // top
            1, 5, 6,
            6, 2, 1,
            // back
            7, 6, 5,
            5, 4, 7,
            // bottom
            4, 0, 3,
            3, 7, 4,
            // left
            4, 5, 1,
            1, 0, 4,
            // right
            3, 2, 6,
            6, 7, 3
        };

        int[] tmp = new int[1];

        glGenVertexArrays(1, tmp);
        this.vao = tmp[0];
        glBindVertexArray(vao);

        glGenBuffers(1, tmp);
        int vbuff = tmp[0];
        glBindBuffer(GL_ARRAY_BUFFER, vbuff);
        glBufferData(GL_ARRAY_BUFFER, vdata.length, vdata, GL_STATIC_DRAW);
        glVertexAttribPointer(Program.POSITION_INDEX, 3, GL_FLOAT,
                false, 3 * 4, 0);
        glEnableVertexAttribArray(Program.POSITION_INDEX);

        glGenBuffers(1, tmp);
        int ibuff = tmp[0];
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ibuff);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, idata.length, idata, GL_STATIC_DRAW);

        glBindVertexArray(0);
    }

    public void draw() {
        glBindVertexArray(this.vao);
        glDrawElements(GL_TRIANGLES, 12, GL_UNSIGNED_INT, 0);
    }
}
