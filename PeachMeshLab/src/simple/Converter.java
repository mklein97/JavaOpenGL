package simple;

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

/**
 *
 * @author jhudson
 */
public class Converter {
    public static void main(String[] args){
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

                
                ByteBuffer indicesAsBytes = ByteBuffer.allocate( 4*numi );
                indicesAsBytes.order(ByteOrder.nativeOrder());
                IntBuffer indicesAsInts = indicesAsBytes.asIntBuffer();
                
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
                    delta += M.mNumVertices;
                }
                
                System.out.println(f+": "+numv+" vertices; "+numi+" indices");
                try{
                    RandomAccessFile out = new RandomAccessFile(abspath+".mesh","rw");
                    out.setLength(0);
                    out.writeUTF("mesh1");          //magic number
                    out.writeUTF("numv");
                    out.writeInt(numv);
                    out.writeUTF("numi");
                    out.writeInt(numi);
                    out.writeUTF("positions");
                    out.write(positionsAsBytes.array());
                    out.writeUTF("vertexMaterials");
                    out.write(vertexMaterialsAsBytes.array());
                    out.writeUTF("indices");
                    out.write(indicesAsBytes.array());
                    out.writeUTF("materials");
                    out.writeInt(scene.mNumMaterials);
                    aiMaterial[] mtls = scene.getMaterials();
                    for(aiMaterial M : mtls ){
                        String mname = aiGetMaterialString(M, AI_MATKEY_NAME);
                        aiColor4D col = new aiColor4D();
                        aiGetMaterialColor(M, AI_MATKEY_COLOR_DIFFUSE,0,0,col);
                        out.writeUTF("name");
                        out.writeUTF(mname);
                        out.writeUTF("diffuse");
                        out.writeFloat(col.r);
                        out.writeFloat(col.g);
                        out.writeFloat(col.b);
                        out.writeFloat(col.a);
                        out.writeUTF("endMaterial");
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
}
