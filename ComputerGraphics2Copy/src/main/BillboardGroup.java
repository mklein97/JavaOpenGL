package main;
import static etgg.GL.*;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import math3d.vec2;
import math3d.vec3;

/**
 *
 * @author Matt
 */
public class BillboardGroup {
    int vao;
    int vbuff;
    int max;
    int num = 0;
    ByteBuffer B;
    FloatBuffer F;
    boolean dirty;
    Texture2DArray tex;
    Program bbprog;
    vec2 size;
    
    public BillboardGroup(Texture2DArray tex, int cap) {
        max = cap;
        this.tex = tex;
        int[] tmp = new int[1];
        B = ByteBuffer.allocate(cap*4*3);
        B.order(ByteOrder.nativeOrder());
        F = B.asFloatBuffer();
        glGenVertexArrays(1, tmp);
        vao = tmp[0];
        glBindVertexArray(vao);
        glGenBuffers(1, tmp);
        vbuff = tmp[0];
        glBindBuffer(GL_ARRAY_BUFFER, vbuff);
        glBufferData(GL_ARRAY_BUFFER, B.capacity(), (byte[]) null, GL_DYNAMIC_DRAW);
        glEnableVertexAttribArray(Program.POSITION_INDEX);
        glVertexAttribPointer(Program.POSITION_INDEX, 3, GL_FLOAT, false, 3 * 4, 0);
    }
    
    int addBillboard(vec3 p, vec2 size) {
        dirty = true;
        this.size = size;
        if (num == max)
            return -1;
        int index = num++;
        F.put(3 * index, p.x);
        F.put(3 * index + 1, p.y);
        F.put(3 * index + 2, p.z);
        return index;
    }
    
    void updateBillboard(int index, vec3 p) {
        F.put(3 * index, p.x);
        F.put(3 * index + 1, p.y);
        F.put(3 * index + 2, p.z);
        dirty = true;
    }
    
    void draw() {
        glBindVertexArray(vao);
        Program curr = Program.current;
        if (bbprog == null)
            bbprog = new Program("bb.vs", "bb.gs", "bb.fs");
        bbprog.use();
        bbprog.setUniform("textures", tex);
        bbprog.setUniform("bbSize", size);
        Program.pushUniforms();
        if (dirty) {
            glBindBuffer(GL_ARRAY_BUFFER, vbuff);
            glBufferSubData(GL_ARRAY_BUFFER, 0, B.capacity(), B.array());
            dirty = false;
        }
        glDrawArrays(GL_POINTS, 0, num);
        curr.use();
    }
}
