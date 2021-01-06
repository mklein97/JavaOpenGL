package javaapplication1;

import static assimp.AI.aiAttachLogStream;
import static assimp.AI.aiDefaultLogStream_STDOUT;
import static assimp.AI.aiGetPredefinedLogStream;
import static assimp.AI.aiImportFile;
import assimp.aiScene;
import java.io.File;
import javax.swing.JFileChooser;
import static assimp.AI.*;
import assimp.*;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;

/**
 *
 * @author jhudson
 */
public class Converter {
    public static void main(String[] args){
        System.out.println(aiGetVersionMajor()+"."+aiGetVersionMinor()+"."+aiGetVersionRevision());
        JFileChooser jf = new JFileChooser("assets");
        jf.setMultiSelectionEnabled(true);
        //jf.setFileFilter( new FileNameExtensionFilter("obj"));
        aiAttachLogStream( aiGetPredefinedLogStream(aiDefaultLogStream_STDOUT,""));
        if( JFileChooser.APPROVE_OPTION == jf.showOpenDialog(null) ){
            for( File f : jf.getSelectedFiles()){
                System.out.println(f);
                String abspath = f.getAbsolutePath();
                aiScene scene = aiImportFile( 
                        abspath , 
                        aiProcess_Triangulate               //quads -> triangles
                        | aiProcess_JoinIdenticalVertices   //collapse identical vertices to save memory
                        | aiProcess_SortByPType             //
                        | aiProcess_GenSmoothNormals        //for lighting
                        | aiProcess_FindInvalidData
                        | aiProcess_PreTransformVertices
                        | aiProcess_CalcTangentSpace
                        );
                if( scene == null ){
                    System.out.println("Something went wrong!");
                    System.exit(1);
                }
                aiMesh[] MS = scene.getMeshes();
                int numv=0,numi=0;
                for(aiMesh M : MS ){
                    if( M.mPrimitiveTypes != aiPrimitiveType_TRIANGLE)
                        continue;
                    numv += M.mNumVertices;
                    numi += M.mNumFaces*3;
                }
                
                ByteBuffer positionsAsBytes = ByteBuffer.allocate( 4*numv*3);
                positionsAsBytes.order(ByteOrder.nativeOrder());
                FloatBuffer positionsAsFloats = positionsAsBytes.asFloatBuffer();

                ByteBuffer vertexMaterialsAsBytes = ByteBuffer.allocate( 4*numv);
                vertexMaterialsAsBytes.order(ByteOrder.nativeOrder());
                FloatBuffer vertexMaterialsAsFloats = vertexMaterialsAsBytes.asFloatBuffer();

                ByteBuffer texCAsBytes = ByteBuffer.allocate( 4*numv*2);
                texCAsBytes.order(ByteOrder.nativeOrder());
                FloatBuffer texCAsFloats = texCAsBytes.asFloatBuffer();
                
                ByteBuffer indicesAsBytes = ByteBuffer.allocate( 4*numi );
                indicesAsBytes.order(ByteOrder.nativeOrder());
                IntBuffer indicesAsInts = indicesAsBytes.asIntBuffer();
                
                ByteBuffer normalsAsBytes = ByteBuffer.allocate(4*numv*3);
                normalsAsBytes.order(ByteOrder.nativeOrder());
                FloatBuffer normalsAsFloats = normalsAsBytes.asFloatBuffer();
                
                ByteBuffer tangentsAsBytes = ByteBuffer.allocate(4*numv*3);
                tangentsAsBytes.order(ByteOrder.nativeOrder());
                FloatBuffer tangentsAsFloats = tangentsAsBytes.asFloatBuffer();
                
                int delta=0;
                for(aiMesh M : MS ){
                    if( M.mPrimitiveTypes != aiPrimitiveType_TRIANGLE)
                        continue;
                    for( aiVector3D V : M.getVertices()){
                        positionsAsFloats.put(V.x);
                        positionsAsFloats.put(V.y);
                        positionsAsFloats.put(V.z);
                        vertexMaterialsAsFloats.put(M.mMaterialIndex);
                    }
                    for( aiFace F : M.getFaces()){
                        int[] I = F.getIndices();
                        indicesAsInts.put(I[0]+delta);
                        indicesAsInts.put(I[1]+delta);
                        indicesAsInts.put(I[2]+delta);
                    }
                    for(aiVector3D T : M.getTextureCoords(0)){
                        texCAsFloats.put(T.x);
                        texCAsFloats.put(T.y);
                    }
                    for (aiVector3D N : M.getNormals()){
                        normalsAsFloats.put(N.x);
                        normalsAsFloats.put(N.y);
                        normalsAsFloats.put(N.z);
                    }
                    for( aiVector3D T : M.getTangents() ){
                        tangentsAsFloats.put(T.x);
                        tangentsAsFloats.put(T.y);
                        tangentsAsFloats.put(T.z);
                    }
                    delta += M.mNumVertices;
                }
                
                System.out.println(f+": "+numv+" vertices; "+numi+" indices");
                try{
                    RandomAccessFile out = new RandomAccessFile(abspath+".mesh","rw");
                    out.setLength(0);
                    out.writeUTF("mesh3");          //magic number
                    out.writeUTF("numv");
                    out.writeInt(numv);
                    out.writeUTF("numi");
                    out.writeInt(numi);
                    out.writeUTF("positions");
                    out.write(positionsAsBytes.array());
                    out.writeUTF("normals");
                    out.write(normalsAsBytes.array());
                    out.writeUTF("texcoords");
                    out.write(texCAsBytes.array());
                    out.writeUTF("tangents");
                    out.write(tangentsAsBytes.array());
                    out.writeUTF("vertexMaterials");
                    out.write(vertexMaterialsAsBytes.array());
                    out.writeUTF("indices");
                    out.write(indicesAsBytes.array());
                    out.writeUTF("materials");
                    out.writeInt(scene.mNumMaterials);
                    aiMaterial[] mtls = scene.getMaterials();
                    ArrayList<String> teximages = new ArrayList<>();
                    for(aiMaterial M : mtls ){
                        int numtex = aiGetMaterialTextureCount(M, aiTextureType_DIFFUSE);
                        if( numtex > 0 ){
                            aiString tmp = new aiString();
                            aiGetMaterialTexture(M, aiTextureType_DIFFUSE, 
                                    0, tmp, null,null,null,null,null,null);
                            String tmpS = tmp.toString();
                            if( !teximages.contains(tmpS))
                                teximages.add(tmpS);
                        }
                    }
                    
                    for(aiMaterial M : mtls ){
                        String mname = aiGetMaterialString(M, AI_MATKEY_NAME);
                        aiColor4D col = new aiColor4D();
                        aiColor4D spec = new aiColor4D();
                        float[] shi = new float[1];
                        aiGetMaterialColor(M, AI_MATKEY_COLOR_DIFFUSE,0,0,col);
                        aiGetMaterialColor(M, AI_MATKEY_COLOR_SPECULAR,0,0,spec);
                        aiGetMaterialFloat(M, AI_MATKEY_SHININESS,0,0,shi);
                        out.writeUTF("name");
                        out.writeUTF(mname);
                        out.writeUTF("diffuse");
                        out.writeFloat(col.r);
                        out.writeFloat(col.g);
                        out.writeFloat(col.b);
                        out.writeFloat(col.a);
                        out.writeUTF("specular");
                        out.writeFloat(spec.r);
                        out.writeFloat(spec.g);
                        out.writeFloat(spec.b);
                        out.writeFloat(spec.a);
                        out.writeUTF("shininess");
                        if (shi[0] == 0)
                            shi[0] = 1;
                        out.writeFloat(shi[0]);
                       int ti = getTexture(M, aiTextureType_DIFFUSE, teximages);
                        out.writeUTF("diffuseTextureIndex");
                        out.writeInt(ti);
                        ti = getTexture(M, aiTextureType_EMISSIVE, teximages);
                        out.writeUTF("emissiveTextureIndex");
                        out.writeInt(ti);
                        ti = getTexture(M, aiTextureType_SPECULAR, teximages);
                        out.writeUTF("specularTextureIndex");
                        out.writeInt(ti);
                        ti = getTexture(M, aiTextureType_HEIGHT, teximages);
                        out.writeUTF("bumpTextureIndex");
                        out.writeInt(ti);
                        out.writeUTF("endMaterial");
                    }
                    out.writeUTF("teximages");
                    out.writeInt(teximages.size());
                    for(String t : teximages ){
                        out.writeUTF(t);
                    }
                    out.writeUTF("end");
                    out.close();
                 }
                catch(IOException e){
                    System.out.println("AAAAGH!!!!!");
                    System.out.println(e);
                    System.exit(1);
                }
            }
        }
        
        System.exit(0);
    }
    
    static int getTexture(aiMaterial M, int textureType, ArrayList<String> teximages) {
        int numtex = aiGetMaterialTextureCount(M, textureType);
        if (numtex == 0) {
            return -1;
        }
        
        aiString tmp = new aiString();
        aiGetMaterialTexture(M, textureType,
                0, tmp, null, null, null, null, null, null);
        String tmpS = tmp.toString();
        if (!teximages.contains(tmpS)) {
            teximages.add(tmpS);
        }
        return teximages.indexOf(tmpS);
    }
    
}
