/*
 */
package main;

import static etgg.GL.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import static java.nio.file.Files.size;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import math3d.mat4;
import math3d.vec2;
import math3d.vec3;
import math3d.vec4;

/**
 *
 * @author jhudson
 */
public class Program {

    int prog;
    String identifier;
    static Program current;
    static int POSITION_INDEX = 0;
    static int MATERIAL_INDEX = 1;
    static int NORMAL_INDEX = 2;
    static int TEXCOORD_INDEX = 3;
    static int TANGENT_INDEX = 4;

    Map<String, Setter> uniforms = new TreeMap<>();
    static int ubo=0;
    static ByteBuffer uboByteBuffer;
    static FloatBuffer uboFloatBuffer;
    static IntBuffer uboIntBuffer;
    
    static void pushUniforms(){
        //push uniforms from CPU to GPU
        byte[] tmp = Program.uboByteBuffer.array();
        //float[] F = new float[uboFloatBuffer.limit()];
        //uboFloatBuffer.get(F);
        glBufferSubData(GL_UNIFORM_BUFFER,0,tmp.length,tmp);
    }
    abstract class Setter {

        String name;
        int loc;
        int size;
        Setter(String n, int sz, int l) {
            name = n;
            loc = l;
            size=sz;
        }

        abstract void set(Object o);
    }

    class vec4Setter extends Setter {
        vec4Setter(String n, int size, int loc) {
            super(n, size, loc);
        }

        @Override
        void set(Object o) {
            vec4[] v;
            if( o instanceof vec4)
                v = new vec4[]{(vec4)o};
            else
                v = (vec4[]) o;
            if( v.length != size )
                throw new RuntimeException("Mismatch in array length: got " + v.length+" expected "+size);
            int j=loc/4;
            for(int i=0;i<v.length;++i){
                uboFloatBuffer.put(j++,v[i].x);
                uboFloatBuffer.put(j++,v[i].y);
                uboFloatBuffer.put(j++,v[i].z);
                uboFloatBuffer.put(j++,v[i].w);
            }
        }
    }

    class vec3Setter extends Setter {
        vec3Setter(String n, int size, int loc) {
            super(n, size, loc);
        }

        @Override
        void set(Object o) {
            vec3[] v;
            if( o instanceof vec3)
                v = new vec3[]{(vec3)o};
            else
                v = (vec3[]) o;
            if( v.length != size )
                throw new RuntimeException("Mismatch in array length: got " + v.length+" expected "+size);
            int j=loc/4;
            for(int i=0;i<v.length;++i){
                uboFloatBuffer.put(j++,v[i].x);
                uboFloatBuffer.put(j++,v[i].y);
                uboFloatBuffer.put(j++,v[i].z);
                ++j;
            }
        }
    }
    class vec2Setter extends Setter {
        vec2Setter(String n, int size, int loc) {
            super(n, size, loc);
        }

        @Override
        void set(Object o) {
            vec2[] v;
            if( o instanceof vec2)
                v = new vec2[]{(vec2)o};
            else
                v = (vec2[]) o;
            if( v.length != size )
                throw new RuntimeException("Mismatch in array length: got " + v.length+" expected "+size);
            int j=loc/4;
            for(int i=0;i<v.length;++i){
                uboFloatBuffer.put(j++,v[i].x);
                uboFloatBuffer.put(j++,v[i].y);
                j+=2;
            }
        }
    }
    class floatSetter extends Setter {
        floatSetter(String n, int size, int loc) {
            super(n, size, loc);
        }

        @Override
        void set(Object o) {
            float[] v;
            if( o instanceof Number)
                v = new float[]{ ((Number)o).floatValue()};
            else
                v = (float[]) o;
            if( v.length != size )
                throw new RuntimeException("Mismatch in array length: got " + v.length+" expected "+size);
            int j=loc/4;
            for(int i=0;i<v.length;++i){
                uboFloatBuffer.put(j,v[i]);
                j+=4;
            }
        }
    }
     class intSetter extends Setter {
        intSetter(String n, int size, int loc) {
            super(n, size, loc);
        }

        @Override
        void set(Object o) {
            int[] v;
            if( o instanceof Number)
                v = new int[]{ ((Number)o).intValue()};
            else
                v = (int[]) o;
            if( v.length != size )
                throw new RuntimeException("Mismatch in array length: got " + v.length+" expected "+size);
            int j=loc/4;
            for(int i=0;i<v.length;++i){
                uboIntBuffer.put(j,v[i]);
                j+=4;
            }
        }
    }
    
    class mat4Setter extends Setter {
        mat4Setter(String n, int size, int loc) {
            super(n, size, loc);
        }

        @Override
        void set(Object o) {
            mat4[] v;
            if( o instanceof mat4)
                v = new mat4[]{(mat4)o};
            else
                v = (mat4[]) o;
            if( v.length != size )
                throw new RuntimeException("Mismatch in array length: got " + v.length+" expected "+size);
            int j=loc/4;
            //System.out.println("Set mat4 "+name+" loc="+loc+" j="+j);
            for(int i=0;i<v.length;++i){
                for(int k=0;k<16;++k){
                    uboFloatBuffer.put(j++,v[i]._M[k]);
                }
            }
        }
    }
    
    class samplerCubeSetter extends Setter {
        int firstunit;

        samplerCubeSetter(String n, int loc, int size, int unit) {
            super(n, size, loc);
            this.firstunit = unit;
        }

        @Override
        void set(Object o) {
            TextureCube[] v;
            if( o instanceof TextureCube){
                v = new TextureCube[]{(TextureCube)o};
            } else{
                v = (TextureCube[]) o;
            }
            if (v.length != size) {
                throw new RuntimeException("Array length mismatch");
            }
            int[] tmp = new int[v.length];
            for (int i = 0; i < v.length; ++i) {
                tmp[i] = firstunit + i;
                if (v[i] != null) {
                    v[i].bind(firstunit + i);
                }
                //else, we leave existing binding alone
            }
            glUniform1iv(loc, size, tmp);
        }
    }

    class sampler2DArraySetter extends Setter {

        int firstunit;

        sampler2DArraySetter(String n, int loc, int size, int firstunit) {
            super(n, size, loc);
            this.size = size;
            this.firstunit = firstunit;
        }

        @Override
        void set(Object o) {
            Texture2DArray[] v;
            if( o instanceof Texture2DArray){
                v = new Texture2DArray[]{(Texture2DArray)o};
            } else{
                v = (Texture2DArray[]) o;
            }
            if (v.length != size) {
                throw new RuntimeException("Array length mismatch");
            }
            int[] tmp = new int[v.length];
            for (int i = 0; i < v.length; ++i) {
                tmp[i] = firstunit + i;
                if (v[i] != null) {
                    v[i].bind(firstunit + i);
                }
                //else, we leave existing binding alone
            }
            glUniform1iv(loc, size, tmp);
        }
    }


    public Program(String vsfname, String gsfname, String fsfname) {
        identifier = vsfname + "+" + fsfname;
        int vs = compile(vsfname, GL_VERTEX_SHADER);
        int gs = -1;
        if (gsfname != null)
            gs = compile(gsfname, GL_GEOMETRY_SHADER);
        int fs = compile(fsfname, GL_FRAGMENT_SHADER);
        prog = glCreateProgram();
        glAttachShader(prog, vs);
        if (gsfname != null)
            glAttachShader(prog, gs);
        glAttachShader(prog, fs);

        bindAttributeLocations();
        link();
        setupUniformSetters();
    }

    void bindAttributeLocations() {
        glBindAttribLocation(prog, POSITION_INDEX, "position");
        glBindAttribLocation(prog, MATERIAL_INDEX, "materialIndex");
        glBindAttribLocation(prog, NORMAL_INDEX, "normal");
        glBindAttribLocation(prog, TEXCOORD_INDEX, "texCoord");
        glBindAttribLocation(prog, TANGENT_INDEX, "tangent");
    }

    void link() {
        glLinkProgram(prog);
        int[] length = new int[1];
        byte[] infolog = new byte[4096];
        glGetProgramInfoLog(prog, infolog.length, length, infolog);
        if (length[0] > 0) {
            String tmp = new String(infolog, 0, length[0]);
            System.out.println("When compiling " + identifier + ":");
            System.out.println(tmp);
        }
        int[] status = new int[1];
        glGetProgramiv(prog, GL_LINK_STATUS, status);
        if (status[0] == 0) {
            throw new RuntimeException("Could not link " + identifier);
        }
    }

    void setupUniformSetters() {
        
        if( ubo == 0 ){
            //this is the first program, so we need to setup
            //our uniform buffer
            int[] tmp = new int[1];
            glGenBuffers(1,tmp);
            ubo = tmp[0];
            glBindBuffer(GL_UNIFORM_BUFFER,ubo);
            
            //Piece of @!#$%@
            glBindBufferBase(GL_UNIFORM_BUFFER,0,ubo);
            
            glGetActiveUniformBlockiv(prog, 0, GL_UNIFORM_BLOCK_DATA_SIZE, tmp);
            int size = tmp[0];
            glBufferData(GL_UNIFORM_BUFFER, size, (byte[]) null, GL_DYNAMIC_DRAW);
            uboByteBuffer = ByteBuffer.allocate(size);
            uboByteBuffer.order(ByteOrder.nativeOrder());
            uboFloatBuffer = uboByteBuffer.asFloatBuffer();
            uboIntBuffer = uboByteBuffer.asIntBuffer();
        }
        
        
        int[] tmp = new int[1];
        glGetProgramiv(prog, GL_ACTIVE_UNIFORMS, tmp);
        int numuniforms = tmp[0];
        int unit = 0;
        for (int i = 0; i < numuniforms; ++i) {
            int[] idx = new int[]{i};
            glGetActiveUniformsiv(prog, 1, idx, GL_UNIFORM_TYPE, tmp);
            int type = tmp[0];
            glGetActiveUniformsiv(prog, 1, idx, GL_UNIFORM_SIZE, tmp);
            int size = tmp[0];
            byte[] namea = new byte[256];
            glGetActiveUniformName(prog, i, namea.length, tmp, namea);
            int len = tmp[0];
            String name = new String(namea, 0, len);
            int loc = glGetUniformLocation(prog, name);
            
            int offset;
            glGetActiveUniformsiv(prog,1,idx,GL_UNIFORM_OFFSET,tmp);
            offset = tmp[0];
            
            //offset will be -1 if the uniform is not inside of a block
            //offset will be >=0 if the uniform is inside of a block
            if( offset == -1 ){
                //textures
                switch(type){
                    case GL_SAMPLER_CUBE:
                        uniforms.put(name, new samplerCubeSetter(name, loc, size, unit));
                        unit += size;
                        break;
                    case GL_SAMPLER_2D_ARRAY :
                        uniforms.put(name, new sampler2DArraySetter(name, loc, size, unit));
                        unit += size;
                        break;
                    default:
                        throw new RuntimeException("Cannot have non-block uniform "+name+" in "+identifier);
                }
            }
            else{
                switch(type){
                    case GL_FLOAT_VEC4:
                        uniforms.put(name, new vec4Setter(name, size, offset));
                        break;
                    case GL_FLOAT_MAT4:
                        uniforms.put(name, new mat4Setter(name, size, offset));
                        break;
                    case GL_FLOAT:
                        uniforms.put(name, new floatSetter(name, size, offset));
                        break;
                    case GL_INT:
                        uniforms.put(name, new intSetter(name, size, offset));
                        break;
                    case GL_FLOAT_VEC3:
                        uniforms.put(name, new vec3Setter(name, size, offset));
                        break;
                    case GL_FLOAT_VEC2:
                        uniforms.put(name, new vec2Setter(name, size, offset));
                        break;
                    default:
                        throw new RuntimeException("Bad uniform type: "+ name+" in "+identifier);
                }
            }
        }
    }

    public void use() {
        glUseProgram(prog);
        current = this;
    }
    Set<String> warned = new TreeSet<>();

    public void setUniform(String name, Object value) {
        if (current != this) {
            throw new RuntimeException("Cannot set uniform on non-active program");
        }
        
        if(!uniforms.containsKey(name) && uniforms.containsKey(name+"[0]"))
            name = name+"[0]";

        if (!uniforms.containsKey(name)) {
            if (!warned.contains(name)) {
                warned.add(name);
                System.out.println("Warning: No such uniform " + name + " in " + identifier);
            }
            return;
        }
        uniforms.get(name).set(value);
    }

    int compile(String fname, int type) {
        int s = glCreateShader(type);
        try {
            FileInputStream fin = new FileInputStream(fname);
            Scanner sc = new Scanner(fin);
            sc.useDelimiter("<END OF FILE>");
            String shaderData = sc.next();
            fin = new FileInputStream("uniforms.txt");
            sc = new Scanner(fin);
            sc.useDelimiter("<END OF FILE>");
            String uniformData = sc.next();

            String mondoString = uniformData + "\n#line 1 0\n" +shaderData;
            String[] fdata = new String[]{mondoString};
            int[] length = new int[]{mondoString.length()};
            glShaderSource(s, 1, fdata, length);
            glCompileShader(s);
            byte[] infolog = new byte[4096];
            glGetShaderInfoLog(s, infolog.length, length, infolog);
            if (length[0] > 0) {
                String tmp = new String(infolog, 0, length[0]);
                System.out.println("When compiling " + fname + ":");
                System.out.println(tmp);
            }
            int[] status = new int[1];
            glGetShaderiv(s, GL_COMPILE_STATUS, status);
            if (status[0] == 0) {
                throw new RuntimeException("Could not compile " + fname);
            }
            return s;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
