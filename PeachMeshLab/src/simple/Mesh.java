/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simple;

import static assimp.AI.AI_MATKEY_COLOR_DIFFUSE;
import static assimp.AI.AI_MATKEY_NAME;
import static assimp.AI.aiGetMaterialColor;
import static assimp.AI.aiGetMaterialString;
import assimp.aiColor4D;
import assimp.aiMaterial;
import static etgg.GL.GL_ARRAY_BUFFER;
import static etgg.GL.GL_ELEMENT_ARRAY_BUFFER;
import static etgg.GL.GL_FLOAT;
import static etgg.GL.GL_STATIC_DRAW;
import static etgg.GL.GL_TRIANGLES;
import static etgg.GL.GL_UNSIGNED_INT;
import static etgg.GL.glBindBuffer;
import static etgg.GL.glBindVertexArray;
import static etgg.GL.glBufferData;
import static etgg.GL.glDrawElements;
import static etgg.GL.glEnableVertexAttribArray;
import static etgg.GL.glGenBuffers;
import static etgg.GL.glGenVertexArrays;
import static etgg.GL.glVertexAttribPointer;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Path;
import java.util.logging.Level;
import java.util.logging.Logger;
import math3d.vec3;
import math3d.vec4;

/**
 *
 * @author jhudson
 */
public class Mesh {
    int vao;
    int numi;
    
    static final int MAX_MATERIALS = 16;
    vec4[] diffuseColors = new vec4[MAX_MATERIALS];
    
    Mesh(String filename){
        //delegate operations to common function
        init(filename);
    }
    Mesh(Path filename){
        init(filename.toString());
    }
    void init(String path){
        try {
            RandomAccessFile raf = new RandomAccessFile(path,"r");
            
            int[] tmp = new int[1];
            glGenVertexArrays(1, tmp);
            this.vao = tmp[0];
            glBindVertexArray(vao);
        
            String s;
            s = raf.readUTF();
            if( !"mesh1".equals(s)){ //  !s.equals("mesh0"))
                throw new RuntimeException("Bad mesh format");
            }
            s = raf.readUTF();
            if(!"numv".equals(s))
                throw new RuntimeException("Bad");
            int numv = raf.readInt();
            s = raf.readUTF();
            if(!"numi".equals(s))
                throw new RuntimeException("Bad");
            numi = raf.readInt();
            s = raf.readUTF();
            if(!"positions".equals(s))
                throw new RuntimeException("Bad");
                    
            //4 bytes per float, numv vertices, 3 floats per vertex (x,y,z)
            byte[] vdata = new byte[  4 * numv * 3 ];
            //vdata gets filled with informatin from the file
            raf.readFully(vdata);
            
            glGenBuffers(1,tmp);
            int vbuff = tmp[0];
            glBindBuffer(GL_ARRAY_BUFFER,vbuff);
            glBufferData(GL_ARRAY_BUFFER,vdata.length,vdata,GL_STATIC_DRAW);
            glVertexAttribPointer(Program.POSITION_INDEX, 3, GL_FLOAT,
                    false, 3*4, 0);
            glEnableVertexAttribArray(Program.POSITION_INDEX);

            if( !raf.readUTF().equals("vertexMaterials"))
                throw new RuntimeException("Mismatch");
            
            
             //4 bytes per float, numv vertices, one float per vertex
            byte[] mtldata = new byte[  4 * numv  ];
            raf.readFully(mtldata);
            
            glGenBuffers(1,tmp);
            int mtlbuff = tmp[0];
            glBindBuffer(GL_ARRAY_BUFFER,mtlbuff);
            glBufferData(GL_ARRAY_BUFFER,mtldata.length,mtldata,GL_STATIC_DRAW);
            glVertexAttribPointer(Program.MATERIAL_INDEX, 1, GL_FLOAT,
                    false, 4, 0);
            glEnableVertexAttribArray(Program.MATERIAL_INDEX);

            
            
            
            
            s = raf.readUTF();
            if( !s.equals("indices"))
                throw new RuntimeException("Bad");
            
            //4 bytes per index
            byte[] idata = new byte[numi * 4];
            raf.readFully(idata);
            glGenBuffers(1,tmp);
            int ibuff = tmp[0];
            glBindBuffer(GL_ELEMENT_ARRAY_BUFFER,ibuff);
            glBufferData(GL_ELEMENT_ARRAY_BUFFER,idata.length,idata,GL_STATIC_DRAW);
        
            if( !raf.readUTF().equals("materials"))
                throw new RuntimeException("Bad");
            
            int numMaterials = raf.readInt();
            for(int i=0;i<numMaterials;i++){
                if( !raf.readUTF().equals("name"))
                    throw new RuntimeException("Bad");
                String mname = raf.readUTF();
                float r,g,b,a;
                if( !raf.readUTF().equals("diffuse"))
                    throw new RuntimeException("What's wrong with you, man?");
                
                r = raf.readFloat();
                g = raf.readFloat();
                b = raf.readFloat();
                a = raf.readFloat();

                diffuseColors[i] = new vec4(r,g,b,a);
                
                if( !raf.readUTF().equals("endMaterial"))
                    throw new RuntimeException("Cut down on the alcohol");
            }
            for(int i = numMaterials; i<MAX_MATERIALS;i++){
                diffuseColors[i] = new vec4(1,0,1,1);
            }
            s = raf.readUTF();
            if( !s.equals("end"))
                throw new RuntimeException("Extra junk at end of file");
            
            
            glBindVertexArray(0);
            raf.close();
        } catch (FileNotFoundException ex) {
            throw new RuntimeException("Cannot load mesh "+path);
        } catch (IOException ex) {
            throw new RuntimeException("IO exception: "+path);
        }
        
    }
    void draw(){
        glBindVertexArray(this.vao);
        Program.current.setUniform("diffuseColors[0]", diffuseColors);
        glDrawElements(GL_TRIANGLES, numi, GL_UNSIGNED_INT, 0);    
    }
}
