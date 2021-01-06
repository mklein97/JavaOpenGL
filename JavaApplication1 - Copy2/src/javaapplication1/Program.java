package javaapplication1;

import static etgg.GL.GL_ACTIVE_UNIFORMS;
import static etgg.GL.GL_COMPILE_STATUS;
import static etgg.GL.GL_FLOAT;
import static etgg.GL.GL_FLOAT_MAT4;
import static etgg.GL.GL_FLOAT_VEC3;
import static etgg.GL.GL_FLOAT_VEC4;
import static etgg.GL.GL_FRAGMENT_SHADER;
import static etgg.GL.GL_INT;
import static etgg.GL.GL_LINK_STATUS;
import static etgg.GL.GL_SAMPLER_2D;
import static etgg.GL.GL_SAMPLER_2D_ARRAY;
import static etgg.GL.GL_UNIFORM_SIZE;
import static etgg.GL.GL_UNIFORM_TYPE;
import static etgg.GL.GL_VERTEX_SHADER;
import static etgg.GL.glAttachShader;
import static etgg.GL.glBindAttribLocation;
import static etgg.GL.glCompileShader;
import static etgg.GL.glCreateProgram;
import static etgg.GL.glCreateShader;
import static etgg.GL.glGetActiveUniformName;
import static etgg.GL.glGetActiveUniformsiv;
import static etgg.GL.glGetProgramInfoLog;
import static etgg.GL.glGetProgramiv;
import static etgg.GL.glGetShaderInfoLog;
import static etgg.GL.glGetShaderiv;
import static etgg.GL.glGetUniformLocation;
import static etgg.GL.glLinkProgram;
import static etgg.GL.glShaderSource;
import static etgg.GL.glUniform1f;
import static etgg.GL.glUniform1iv;
import static etgg.GL.glUniform3f;
import static etgg.GL.glUniform4fv;
import static etgg.GL.glUniformMatrix4fv;
import static etgg.GL.glUseProgram;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import math3d.mat4;
import math3d.vec3;
import math3d.vec4;

public class Program {
    int prog;
    String identifier;
    static Program current;
    static final int POSITION_INDEX = 0;
    static final int MATERIAL_INDEX = 1;
    static final int TEXCOORD_INDEX = 3;
    static final int NORMAL_INDEX = 2;
    
    abstract class Setter{
        String name;        //uniform name, in the shader
        int loc;            //uniform slot number, for the shader
        Setter(String n, int i){
            name=n;
            loc=i;
        }
        abstract void set(Object o);
    }

    class mat4Setter extends Setter{
        mat4Setter(String n, int i){
            super(n,i);
        }
        void set(Object o){
            mat4 M = (mat4) o;
            glUniformMatrix4fv(loc,1,true,M._M);
        }
    }
    
    class vec3Setter extends Setter{
        vec3Setter(String n, int i){
            super(n,i);
        }
        void set(Object o){
            vec3 V = (vec3) o;
            glUniform3f(loc,V.x, V.y, V.z );
        }
    }
    
    class floatSetter extends Setter {
        floatSetter(String n, int i){
            super(n,i);
        }
        @Override
        void set(Object o){
            Number v = (Number) o;
            glUniform1f(loc,v.floatValue());
        }
    }
    
    class vec4ArraySetter extends Setter{
        int size;
        vec4ArraySetter(String n, int i, int size){
            super(n,i);
            this.size=size;
        }
        void set(Object o){
            vec4[] V = (vec4[]) o;
            if( V.length != size )
                throw new RuntimeException(name+": Expected "+size+" elements, but got "+V.length);
            float[] tmp = new float[V.length * 4];
            for(int i=0,j=0; i<V.length;i++){
                tmp[j++] = V[i].x;
                tmp[j++] = V[i].y;
                tmp[j++] = V[i].z;
                tmp[j++] = V[i].w;
            }
            glUniform4fv(loc,size,tmp);
        }
    }
     
     class intArraySetter extends Setter{
        int size;
        intArraySetter(String n, int i, int size){
            super(n,i);
            this.size=size;
        }
        void set(Object o){
            int[] V = (int[]) o;
            if( V.length != size )
                throw new RuntimeException(name+": Expected "+size+" elements, but got "+V.length);
            glUniform1iv(loc,size,V);
        }
    }
     class sampler2DArraySetter extends Setter{
        int size;
        int texunit;
        sampler2DArraySetter(String n, int i, int size, int texunit){
            super(n,i);
            this.size=size;
            this.texunit = texunit;
        }
        
        
        void set(Object o){
            Texture2D[] V = (Texture2D[]) o;
            if( V.length != size )
                throw new RuntimeException(name+": Expected "+size+" elements, but got "+V.length);
            int[] tmp = new int[size];
            for(int i=0;i<size;i++){
                tmp[i] = texunit+i;
                if( V[i] != null )
                    V[i].bind(texunit+i);
            }
            glUniform1iv(loc,size,tmp);
        }
    }
     class sampler2DArrayArraySetter extends Setter {

        int size;
        int texunit;

        sampler2DArrayArraySetter(String n, int i, int size, int texunit) {
            super(n, i);
            this.size = size;
            this.texunit = texunit;
        }

        void set(Object o) {
            
            Texture2DArray[] V;
            if( o instanceof Texture2DArray )
                V = new Texture2DArray[]{(Texture2DArray)o};
            else
                V = (Texture2DArray[]) o;
            
            if (V.length != size) {
                throw new RuntimeException(name + ": Expected " + size + " elements, but got " + V.length);
            }
            int[] tmp = new int[size];
            for (int i = 0; i < size; i++) {
                tmp[i] = texunit + i;
                if (V[i] != null) {
                    V[i].bind(texunit + i);
                }
            }
            glUniform1iv(loc, size, tmp);
        }
    }
        
    TreeMap<String,Setter> uniforms = new TreeMap<>();
    
    public Program(String vsfname, String fsfname){
        identifier = vsfname+"+"+fsfname;
        int vs = compile(vsfname, GL_VERTEX_SHADER);
        int fs = compile(fsfname, GL_FRAGMENT_SHADER);
        prog = glCreateProgram();
        glAttachShader(prog,vs);
        glAttachShader(prog,fs);
        bindAttributeLocations();
        link();
        setupUniformSetters();
    }
    
    void setupUniformSetters(){
        int texcount=0;
        
        int[] tmp = new int[1];
        glGetProgramiv(prog,GL_ACTIVE_UNIFORMS,tmp);
        int numuniforms = tmp[0];
        for(int i=0;i<numuniforms;++i){
            int[] idx = new int[1];
            idx[0] = i;
            glGetActiveUniformsiv(prog,1,idx,GL_UNIFORM_TYPE,tmp);
            int type = tmp[0];
            glGetActiveUniformsiv(prog,1,idx,GL_UNIFORM_SIZE,tmp);
            int size = tmp[0];
            byte[] namea = new byte[256];
            glGetActiveUniformName(prog,i,namea.length,tmp,namea);
            String name = new String(namea,0,tmp[0]);
            int loc = glGetUniformLocation(prog,name);
            System.out.println("We have a uniform: " + name + " and it is at "+loc);
            if( type == GL_FLOAT_MAT4 && size == 1 )
                uniforms.put(name,new mat4Setter(name,loc));
            else if( type == GL_FLOAT_VEC3 && size == 1 )
                uniforms.put(name,new vec3Setter(name,loc));
            else if( type == GL_FLOAT_VEC4 && size > 1 )
                uniforms.put(name,new vec4ArraySetter(name,loc,size));
            else if( type == GL_INT && size > 1 )
                uniforms.put(name,new intArraySetter(name,loc,size));
            else if( type == GL_SAMPLER_2D && size > 1 ){
                uniforms.put(name,new sampler2DArraySetter(name,loc,size,texcount));
                texcount += size;
            }
            else if (type == GL_SAMPLER_2D_ARRAY && size >= 1) {
                uniforms.put(name, new sampler2DArrayArraySetter(name, loc, size, texcount));
                texcount += size;
            }
            else if( type == GL_FLOAT && size == 1 ){
                uniforms.put(name,new floatSetter(name, loc));
            }
            else{
                System.out.println("I can't handle a uniform of this type: Variable name: "+name);
                System.exit(1);
            }
        }
    }
    
    
    void use(){
        glUseProgram(prog);
        current = this;
    }

    TreeSet<String> warned = new TreeSet<>();
    void setUniform(String name, Object o){
        if(current != this ){
            System.out.println("Naughty naughty!");
            System.exit(1);
        }
        if( uniforms.containsKey(name)){
            uniforms.get(name).set(o);
        }
        else{
            if( uniforms.containsKey(name+"[0]")){
                throw new RuntimeException("Setting uniform "+name+": Use the [0] for an array");
            }
            if( !warned.contains(name)){
                System.out.println("Warning: No such uniform "+name);
                warned.add(name);
            }
        }
        
    }
    
    void link(){
        glLinkProgram(prog);
        
        byte[] infolog = new byte[4000];
        int[] length = new int[1];
        glGetProgramInfoLog(prog,infolog.length, length, infolog);
        if( length[0] > 0 ){
            String tmp2 = new String(infolog,0,length[0]);
            System.out.println("When linking "+identifier+":");
            System.out.println(tmp2);
        }
        
        int[] status = new int[1];
        glGetProgramiv(prog,GL_LINK_STATUS,status);
        if( status[0] == 0 ){
            System.out.println("Link of program "+identifier+" failed");
            System.exit(1);
        }
    }
    
    void bindAttributeLocations(){
        glBindAttribLocation(prog,POSITION_INDEX, "position");
        glBindAttribLocation(prog,MATERIAL_INDEX, "materialIndex");
        glBindAttribLocation(prog,TEXCOORD_INDEX, "texCoord");
        glBindAttribLocation(prog, NORMAL_INDEX, "normal");
    }
    
    int compile(String fname, int type){
        int s = glCreateShader(type);
        String sdata;
        try {
            sdata = new String(Files.readAllBytes(FileSystems.getDefault().getPath(fname)));
        } catch (IOException ex) {
            System.out.println("Can't load shader "+fname);
            System.exit(0);
            return -1;      //never executes!
        }
        
        String[] tmp = new String[1];
        tmp[0]=sdata;
        int[] length = new int[1];
        length[0] = sdata.length();
        glShaderSource(s,1,tmp,length);
        glCompileShader(s);
        
        byte[] infolog = new byte[4000];
        glGetShaderInfoLog(s,infolog.length, length, infolog);
        if( length[0] > 0 ){
            System.out.println("When compiling "+fname+":");
            String tmp2 = new String(infolog,0,length[0]);
            System.out.println(tmp2);
        }
        
        int[] status = new int[1];
        glGetShaderiv(s,GL_COMPILE_STATUS,status);
        if( status[0] == 0 ){
            System.out.println("Compile of shader "+fname+" failed");
            System.exit(1);
            return -1;
        }
        return s;
    }
}
