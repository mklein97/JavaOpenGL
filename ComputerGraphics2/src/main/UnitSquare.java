/*
 */
package main;
import static etgg.GL.*;

/**
 *
 * @author jhudson
 */
public class UnitSquare{
    int vao;
    public UnitSquare(){
        float[] vdata = new float[]{
            -1,1,-1,
            -1,-1,-1,
            1,-1,-1,
            1,1,-1
        };
        float[] tdata = new float[]{
            0,1,
            0,0,
            1,0,
            1,1
        };
        
        int[] idata = new int[]{
            0,1,2,  0,2,3
        };
        int[] tmp = new int[1];
        glGenVertexArrays(1,tmp);
        this.vao = tmp[0];
        glBindVertexArray(vao);
        glGenBuffers(1,tmp);
        int vbuff = tmp[0];
        glBindBuffer(GL_ARRAY_BUFFER,vbuff);
        glBufferData(GL_ARRAY_BUFFER, vdata.length, vdata, GL_STATIC_DRAW);
        glVertexAttribPointer(Program.POSITION_INDEX, 3, GL_FLOAT, false, 3*4, 0);
        glEnableVertexAttribArray(Program.POSITION_INDEX);
        
        glGenBuffers(1,tmp);
        int tbuff = tmp[0];
        glBindBuffer(GL_ARRAY_BUFFER,tbuff);
        glBufferData(GL_ARRAY_BUFFER, tdata.length, tdata, GL_STATIC_DRAW);
        glVertexAttribPointer(Program.TEXCOORD_INDEX, 2, GL_FLOAT, false, 2*4, 0);
        glEnableVertexAttribArray(Program.TEXCOORD_INDEX);
        
        glGenBuffers(1,tmp);
        int ibuff = tmp[0];
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER,ibuff);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, idata.length, idata, GL_STATIC_DRAW);
        glBindVertexArray(0);
    }
    
    public void draw(){
        glBindVertexArray(vao);
        Program.pushUniforms();
        glDrawElements(GL_TRIANGLES, 6, GL_UNSIGNED_INT, 0);
    }
}