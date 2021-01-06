/*
 */
package main;
import static etgg.GL.*;

/**
 *
 * @author jhudson
 */
public class Square{
    int vao;
    public Square(){
        float[] vdata = new float[]{
            0,1,2,3
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
        glVertexAttribPointer(Program.POSITION_INDEX, 1, GL_FLOAT, false, 1*4, 0);
        glEnableVertexAttribArray(Program.POSITION_INDEX);
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