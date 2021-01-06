/*
 */
package main;

import assimp.*;
import static assimp.AI.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;
import math3d.vec3;

public class Converter {

    public static void main(String[] args) throws FileNotFoundException, IOException {
        System.out.println(aiGetVersionMajor()+"."+aiGetVersionMinor()+"."+aiGetVersionRevision());
        
        JFileChooser jf = new JFileChooser("assets");
        jf.setMultiSelectionEnabled(true);
        jf.setAcceptAllFileFilterUsed(true);
        jf.setFileFilter(new FileFilter() {
            @Override
            public boolean accept(File f) {
                if (f.getName().endsWith(".obj")) {
                    return true;
                } else {
                    return false;
                }
            }

            @Override
            public String getDescription() {
                return "OBJ files";
            }
        });
        aiAttachLogStream(
                aiGetPredefinedLogStream(aiDefaultLogStream_STDOUT, ""));
        if (jf.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            File[] selected = jf.getSelectedFiles();
            for (File f : selected) {
                String fullpath = f.getAbsolutePath();
                aiScene scene = aiImportFile(fullpath,
                        aiProcess_CalcTangentSpace
                        | aiProcess_Triangulate
                        | aiProcess_JoinIdenticalVertices
                        | aiProcess_SortByPType
                        | aiProcess_GenSmoothNormals
                        | aiProcess_LimitBoneWeights
                        | aiProcess_OptimizeMeshes
                        | aiProcess_FindInvalidData
                        | aiProcess_PreTransformVertices
                );
                if (scene == null) {
                    System.out.println("Not imported");
                    System.exit(1);
                }

                aiMesh[] MS = scene.getMeshes();
                int numv = 0;
                int numi = 0;
                for (aiMesh M : MS) {
                    if (M.mPrimitiveTypes != aiPrimitiveType_TRIANGLE) {
                        continue;
                    }
                    numv += M.mNumVertices;
                    numi += M.mNumFaces * 3;
                }

                ArrayList<String> usedTextures = new ArrayList<>();

                ByteBuffer positionsAsBytes = ByteBuffer.allocate(4 * numv * 3);
                positionsAsBytes.order(ByteOrder.nativeOrder());
                FloatBuffer positionsAsFloats = positionsAsBytes.asFloatBuffer();

                ByteBuffer normalsAsBytes = ByteBuffer.allocate(4 * numv * 3);
                normalsAsBytes.order(ByteOrder.nativeOrder());
                FloatBuffer normalsAsFloats = normalsAsBytes.asFloatBuffer();

                ByteBuffer texCAsBytes = ByteBuffer.allocate(4 * numv * 2);
                texCAsBytes.order(ByteOrder.nativeOrder());
                FloatBuffer texCAsFloats = texCAsBytes.asFloatBuffer();

                ByteBuffer tangentsAsBytes = ByteBuffer.allocate(4 * numv * 3);
                tangentsAsBytes.order(ByteOrder.nativeOrder());
                FloatBuffer tangentsAsFloats = tangentsAsBytes.asFloatBuffer();
                
                ByteBuffer indicesAsBytes = ByteBuffer.allocate(numi * 4);
                indicesAsBytes.order(ByteOrder.nativeOrder());
                IntBuffer indicesAsInts = indicesAsBytes.asIntBuffer();

                //material info: One material index per vertex.
                ByteBuffer vertexMaterialsAsBytes = ByteBuffer.allocate(numv * 4);
                vertexMaterialsAsBytes.order(ByteOrder.nativeOrder());
                FloatBuffer vertexMaterialsAsFloats = vertexMaterialsAsBytes.asFloatBuffer();

                int delta = 0;

                vec3 bbMin = new vec3(Float.MAX_VALUE,Float.MAX_VALUE,Float.MAX_VALUE);
                vec3 bbMax = new vec3(Float.MIN_VALUE,Float.MIN_VALUE,Float.MIN_VALUE);
 
                for (aiMesh M : MS) {
                    if (M.mPrimitiveTypes != aiPrimitiveType_TRIANGLE) {
                        continue;
                    }

                    aiVector3D[] V = M.getVertices();
                    aiVector3D[] N = M.getNormals();
                    aiVector3D[] T = M.getTextureCoords(0);
                    aiVector3D[] TT = M.getTangents();
                    
                    for (int i = 0; i < V.length; ++i) {
                        positionsAsFloats.put(V[i].x);
                        positionsAsFloats.put(V[i].y);
                        positionsAsFloats.put(V[i].z);
                        
                        bbMin.x = Math.min(V[i].x,bbMin.x);
                        bbMin.y = Math.min(V[i].y,bbMin.y);
                        bbMin.z = Math.min(V[i].z,bbMin.z);
                        bbMax.x = Math.max(V[i].x,bbMax.x);
                        bbMax.y = Math.max(V[i].y,bbMax.y);
                        bbMax.z = Math.max(V[i].z,bbMax.z);
                        
 
                        
                        vertexMaterialsAsFloats.put(M.mMaterialIndex);
                        
                        normalsAsFloats.put(N[i].x);
                        normalsAsFloats.put(N[i].y);
                        normalsAsFloats.put(N[i].z);
                        if( TT == null ){
                            tangentsAsFloats.put(1);
                            tangentsAsFloats.put(0);
                            tangentsAsFloats.put(0);
                        } else {
                            tangentsAsFloats.put(TT[i].x);
                            tangentsAsFloats.put(TT[i].y);
                            tangentsAsFloats.put(TT[i].z);
                        }
                        if( T == null ){
                            texCAsFloats.put(0);
                            texCAsFloats.put(0);
                        }
                        else {
                            texCAsFloats.put(T[i].x);
                            texCAsFloats.put(T[i].y);
                        }
                    }
                    for (aiFace F : M.getFaces()) {
                        int[] I = F.getIndices();
                        indicesAsInts.put(I[0] + delta);
                        indicesAsInts.put(I[1] + delta);
                        indicesAsInts.put(I[2] + delta);
                    }
                    delta += M.mNumVertices;
                }

                System.out.println("numv: " + numv);
                System.out.println("numi: " + numi);

                String outfilename = fullpath + ".mesh";
                RandomAccessFile out = new RandomAccessFile(outfilename, "rw");
                out.setLength(0);
                out.writeUTF("mesh20171209");
                out.writeUTF("num_vertices");
                out.writeInt(numv);
                out.writeUTF("num_indices");
                out.writeInt(numi);
                out.writeUTF("positions");
                out.write(positionsAsBytes.array());
                out.writeUTF("normals");
                out.write(normalsAsBytes.array());
                out.writeUTF("texCoords");
                out.write(texCAsBytes.array());
                out.writeUTF("tangents");
                out.write(tangentsAsBytes.array());
                out.writeUTF("indices");
                out.write(indicesAsBytes.array());
                out.writeUTF("vertexMaterials");
                out.write(vertexMaterialsAsBytes.array());

                out.writeUTF("num_materials");
                out.writeInt(scene.mNumMaterials);
                aiMaterial[] ML = scene.getMaterials();
                for (int i = 0; i < ML.length; ++i) {
                    aiMaterial mtl = ML[i];
                    aiColor4D col = new aiColor4D();
                    out.writeUTF("material");
                    out.writeInt(i);
                    out.writeUTF(aiGetMaterialString(mtl, AI_MATKEY_NAME));
                    
                    //System.out.println("Considering material "+aiGetMaterialString(mtl, AI_MATKEY_NAME));
                    
                    aiGetMaterialColor(mtl, AI_MATKEY_COLOR_DIFFUSE, 0, 0, col);
                    out.writeUTF("diffuse");
                    out.writeFloat(col.r);
                    out.writeFloat(col.g);
                    out.writeFloat(col.b);
                    aiGetMaterialColor(mtl, AI_MATKEY_COLOR_SPECULAR, 0, 0, col);
                    out.writeUTF("specular");
                    out.writeFloat(col.r);
                    out.writeFloat(col.g);
                    out.writeFloat(col.b);
                    float[] ff = new float[1];
                    aiGetMaterialFloat(mtl, AI_MATKEY_SHININESS, 0, 0, ff);
                    out.writeUTF("shininess");
                    out.writeFloat(ff[0]);

                    int tidx = getTextureIndex(mtl, aiTextureType_DIFFUSE, usedTextures);
                    out.writeUTF("diffuseTextureIndex");
                    out.writeInt(tidx);
                    tidx = getTextureIndex(mtl, aiTextureType_EMISSIVE, usedTextures);
                    out.writeUTF("emissiveTextureIndex");
                    out.writeInt(tidx);
                    tidx = getTextureIndex(mtl, aiTextureType_SPECULAR, usedTextures);
                    out.writeUTF("specularTextureIndex");
                    out.writeInt(tidx);
                    tidx = getTextureIndex(mtl, aiTextureType_HEIGHT, usedTextures);
                    out.writeUTF("bumpmapTextureIndex");
                    out.writeInt(tidx);
                }

                out.writeUTF("numTextures");
                out.writeInt(usedTextures.size());
                for (int i = 0; i < usedTextures.size(); ++i) {
                    out.writeUTF("texfile");
                    out.writeInt(i);
                    out.writeUTF(usedTextures.get(i));
                }

                out.writeUTF("endMaterials");

                out.writeUTF("boundingBox");
                out.writeFloat(bbMin.x);
                out.writeFloat(bbMin.y);
                out.writeFloat(bbMin.z);
                out.writeFloat(bbMax.x);
                out.writeFloat(bbMax.y);
                out.writeFloat(bbMax.z);
                
                out.writeUTF("end");

                out.close();
                System.out.println("Wrote " + outfilename);
            }

        }
        System.exit(0);
    }

    static int getTextureIndex(aiMaterial mtl, int textype, ArrayList<String> usedTextures) throws IOException {
        int numtex = aiGetMaterialTextureCount(mtl, textype);
        if( numtex == 0 ){
            return -1;
        }
        else{
            aiString str = new aiString();
            aiGetMaterialTexture(mtl, textype,0,str,null, null, null, null, null, null); 
            String s = str.toString();
            int texidx = usedTextures.indexOf(s);
            if( texidx == -1 ){
                texidx = usedTextures.size();
                usedTextures.add(s);
            }
            return texidx;
        }
    }
}
