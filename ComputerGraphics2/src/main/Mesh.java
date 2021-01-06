/*
 */
package main;

import static etgg.GL.*;
import java.io.*;
import static math3d.functions.add;
import static math3d.functions.length;
import static math3d.functions.mul;
import static math3d.functions.sub;
import math3d.vec3;

/**
 *
 * @author jhudson
 */
public class Mesh {

    String filename;
    
    int numv;
    int numi;
    int vao;

    static final int MAX_MATERIALS = 16;
    static final int MAX_TEXTURES = 5;
    vec3[] diffuseColors = new vec3[MAX_MATERIALS];
    vec3[] specularColors = new vec3[MAX_MATERIALS];
    float[] shininess = new float[MAX_MATERIALS];
    Texture2DArray[] textures = new Texture2DArray[MAX_TEXTURES];
    int[] materialDiffuseTextureIndex = new int[MAX_MATERIALS];
    int[] materialEmissiveTextureIndex = new int[MAX_MATERIALS];
    int[] materialSpecularTextureIndex = new int[MAX_MATERIALS];
    int[] materialBumpTextureIndex = new int[MAX_MATERIALS];
    vec3 bbMin = new vec3(0,0,0);
    vec3 bbMax = new vec3(0,0,0);
    Sphere boundingSphere;
    
    final void expect(RandomAccessFile raf, String what) throws IOException {
        String x = raf.readUTF();
        if (!x.equals(what)) {
            throw new RuntimeException(filename+": Expected " + what + " got " + x);
        }
    }

    public Mesh(String filename) {
        try {
            this.filename=filename;
            RandomAccessFile raf = new RandomAccessFile(filename, "r");
            File tmpf = new File(filename);
            String basedir = tmpf.getParent();

            expect(raf, "mesh20171209");

            int[] tmp = new int[1];
            glGenVertexArrays(1, tmp);
            vao = tmp[0];
            glBindVertexArray(vao);

            expect(raf, "num_vertices");
            numv = raf.readInt();
            expect(raf, "num_indices");
            numi = raf.readInt();
            expect(raf, "positions");
            byte[] data = new byte[numv * 3 * 4];
            raf.readFully(data);
            glGenBuffers(1, tmp);
            glBindBuffer(GL_ARRAY_BUFFER, tmp[0]);
            glBufferData(GL_ARRAY_BUFFER, data.length, data, GL_STATIC_DRAW);
            glVertexAttribPointer(Program.POSITION_INDEX, 3, GL_FLOAT, false, 3 * 4, 0);
            glEnableVertexAttribArray(Program.POSITION_INDEX);
            expect(raf, "normals");
            data = new byte[numv * 3 * 4];
            raf.readFully(data);
            glGenBuffers(1, tmp);
            glBindBuffer(GL_ARRAY_BUFFER, tmp[0]);
            glBufferData(GL_ARRAY_BUFFER, data.length, data, GL_STATIC_DRAW);
            glVertexAttribPointer(Program.NORMAL_INDEX, 3, GL_FLOAT, false, 3 * 4, 0);
            glEnableVertexAttribArray(Program.NORMAL_INDEX);
            expect(raf, "texCoords");
            data = new byte[numv * 2 * 4];
            raf.readFully(data);
            glGenBuffers(1, tmp);
            glBindBuffer(GL_ARRAY_BUFFER, tmp[0]);
            glBufferData(GL_ARRAY_BUFFER, data.length, data, GL_STATIC_DRAW);
            glVertexAttribPointer(Program.TEXCOORD_INDEX, 2, GL_FLOAT, false, 2 * 4, 0);
            glEnableVertexAttribArray(Program.TEXCOORD_INDEX);
            expect(raf, "tangents");
            data = new byte[numv * 3 * 4];
            raf.readFully(data);
            glGenBuffers(1, tmp);
            glBindBuffer(GL_ARRAY_BUFFER, tmp[0]);
            glBufferData(GL_ARRAY_BUFFER, data.length, data, GL_STATIC_DRAW);
            glVertexAttribPointer(Program.TANGENT_INDEX, 3, GL_FLOAT, false, 3 * 4, 0);
            glEnableVertexAttribArray(Program.TANGENT_INDEX);
            expect(raf, "indices");
            data = new byte[numi * 4];
            raf.readFully(data);
            glGenBuffers(1, tmp);
            glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, tmp[0]);
            glBufferData(GL_ELEMENT_ARRAY_BUFFER, data.length, data, GL_STATIC_DRAW);
            expect(raf, "vertexMaterials");
            data = new byte[numv * 4 * 1];
            raf.readFully(data);
            
            glGenBuffers(1, tmp);
            glBindBuffer(GL_ARRAY_BUFFER, tmp[0]);
            glBufferData(GL_ARRAY_BUFFER, data.length, data, GL_STATIC_DRAW);
            glVertexAttribPointer(Program.MATERIAL_INDEX, 1, GL_FLOAT, false, 1 * 4, 0);
            glEnableVertexAttribArray(Program.MATERIAL_INDEX);
            
            expect(raf, "num_materials");
            int numm = raf.readInt();
            if (numm > MAX_MATERIALS) {
                System.out.println("Too many materials!");
                System.exit(1);
            }
            for (int i = 0; i < numm; ++i) {
                expect(raf, "material");

                int mi = raf.readInt();
                String name = raf.readUTF();
                expect(raf, "diffuse");
                diffuseColors[mi] = new vec3(
                        raf.readFloat(),
                        raf.readFloat(),
                        raf.readFloat()
                );
                expect(raf, "specular");
                specularColors[mi] = new vec3(
                        raf.readFloat(),
                        raf.readFloat(),
                        raf.readFloat()
                );
                expect(raf, "shininess");
                shininess[mi] = raf.readFloat();
                expect(raf, "diffuseTextureIndex");
                int tnum = raf.readInt();
                materialDiffuseTextureIndex[mi] = tnum;
                expect(raf, "emissiveTextureIndex");
                tnum = raf.readInt();
                materialEmissiveTextureIndex[mi] = tnum;
                expect(raf, "specularTextureIndex");
                tnum = raf.readInt();
                materialSpecularTextureIndex[mi] = tnum;
                expect(raf, "bumpmapTextureIndex");
                tnum = raf.readInt();
                materialBumpTextureIndex[mi]=tnum;
            }
            expect(raf, "numTextures");
            int numT = raf.readInt();
            for (int i = 0; i < numT; ++i) {
                expect(raf, "texfile");
                raf.readInt();  //should always equal i...
                String texfile = raf.readUTF();
                textures[i] = new ImageTexture2DArray(basedir + "/" + texfile);
            }
            expect(raf,"endMaterials");
                    
            expect(raf,"boundingBox");
            bbMin.x = raf.readFloat();
            bbMin.y = raf.readFloat();
            bbMin.z = raf.readFloat();
            bbMax.x = raf.readFloat();
            bbMax.y = raf.readFloat();
            bbMax.z = raf.readFloat();
                
            vec3 c = mul(0.5, add(bbMin,bbMax) );
            boundingSphere = new Sphere(c, length(sub(c,bbMin) ) );
            
            expect(raf,"end");
            glBindVertexArray(0);

        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
        for (int i = 0; i < diffuseColors.length; ++i) {
            if (diffuseColors[i] == null) {
                diffuseColors[i] = new vec3(0, 0, 0);
            }
        }
        for (int i = 0; i < specularColors.length; ++i) {
            if (specularColors[i] == null) {
                specularColors[i] = new vec3(1, 1, 1);
            }
        }

    }

    public void draw() {
        glBindVertexArray(vao);
        Program.current.setUniform("diffuse[0]", diffuseColors);
        Program.current.setUniform("specular[0]", specularColors);
        Program.current.setUniform("shininess[0]", shininess);
        Program.current.setUniform("textures[0]", textures);
        Program.current.setUniform("materialDiffuseTextureIndex[0]", materialDiffuseTextureIndex);
        Program.current.setUniform("materialEmissiveTextureIndex[0]", materialEmissiveTextureIndex);
        Program.current.setUniform("materialSpecularTextureIndex[0]", materialSpecularTextureIndex);
        Program.current.setUniform("materialBumpTextureIndex[0]", materialBumpTextureIndex);
        Program.pushUniforms();
        glDrawElements(GL_TRIANGLES, numi, GL_UNSIGNED_INT, 0);
    }
}
