package etgg;
import com.sun.jna.*;
/*Data from gl.xml, which has this copyright:

#Copyright (c) 2013-2016 The Khronos Group Inc.
#
#Permission is hereby granted, free of charge, to any person obtaining a
#copy of this software and/or associated documentation files (the
#"Materials"), to deal in the Materials without restriction, including
#without limitation the rights to use, copy, modify, merge, publish,
#distribute, sublicense, and/or sell copies of the Materials, and to
#permit persons to whom the Materials are furnished to do so, subject to
#the following conditions:
#
#The above copyright notice and this permission notice shall be included
#in all copies or substantial portions of the Materials.
#
#THE MATERIALS ARE PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
#EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
#MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
#IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY
#CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT,
#TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
#MATERIALS OR THE USE OR OTHER DEALINGS IN THE MATERIALS.
#
#------------------------------------------------------------------------
#
#This file, gl.xml, is the OpenGL and OpenGL API Registry. The older
#".spec" file format has been retired and will no longer be updated with
#new extensions and API versions. The canonical version of the registry,
#together with documentation, schema, and Python generator scripts used
#to generate C header files for OpenGL and OpenGL ES, can always be found
#in the Khronos Registry at
#        http://www.opengl.org/registry/
#    
*/

import java.nio.*;

public class GL {
    private static int callConvention;
    
    
    static {
        String os = System.getProperty("os.name");
        if( os.indexOf("Windows") != -1 )
            callConvention = Function.ALT_CONVENTION;
        else
            callConvention = Function.C_CONVENTION;
    }
    
    /*
    public static class GLXLib{
        static boolean initialized=false;
        private static void doInit(){
            if(!initialized){
                Native.register("GL");
                initialized=true;
            }
        }
        public static Pointer _glXGetProcAddress(String pname){
            if(!initialized)
                doInit();
            return glXGetProcAddress(pname);
        }
        public static native Pointer glXGetProcAddress(String procname);
    }
    */
    
    private static Function loadFunction(String fname){
        Pointer p = SDL.SDL_GL_GetProcAddress(fname);
        //Pointer p = GLXLib._glXGetProcAddress(fname);
        if( p == null )
            throw new RuntimeException("No such function "+fname);
        Function f = Function.getFunction(p, callConvention);
        return f;
    }
    
    public static void glShaderSource(int shader_, int count_, String[] string_, int[] length_){
        if( _glShaderSource == null )
            _glShaderSource = loadFunction("glShaderSource");
        //jna converts array of String to array of char*
        _glShaderSource.invoke(Void.class, new Object[]{
            shader_,count_,string_,length_});
    }
    
    public static void glVertexAttribPointer(int index, int size, int type, boolean normalized, int stride, int offset){
        if( _glVertexAttribPointer == null )
            _glVertexAttribPointer = loadFunction("glVertexAttribPointer");
        _glVertexAttribPointer.invoke( Void.class, new Object[]{
            index,size,type,normalized,stride,Pointer.createConstant(offset)});
    }
    
    private static Function _glGetString;
    public static String glGetString(int name_){
        if( _glGetString == null )
            _glGetString = loadFunction("glGetString");
        String s = (String) _glGetString.invoke( String.class, new Object[]{name_});
        return s;
    }
    
    private static Function _glGetStringi;
    public static String glGetStringi(int name_, int index_){
        if( _glGetStringi == null )
            _glGetStringi = loadFunction("glGetStringi");
        String s = (String) _glGetStringi.invoke( String.class, new Object[]{name_,index_});
        return s;
    }
    
    private static Function _glGetBooleani_v;
    public static void glGetBooleani_v(int target_, int index_, boolean[] data_){
        byte[] tmp = new byte[data_.length];
        if( _glGetBooleani_v == null )
            _glGetBooleani_v = loadFunction("glGetBooleani_v");
        _glGetBooleani_v.invoke(Void.class, new Object[]{target_,index_,tmp});
        for(int i=0;i<data_.length;++i)
            data_[i] = ((tmp[i]==0) ? false:true);
    }
    private static Function _glGetBooleanv;
    public static void glGetBooleanv(int target_, int index_, boolean[] data_){
        byte[] tmp = new byte[data_.length];
        if( _glGetBooleanv == null )
            _glGetBooleanv = loadFunction("glGetBooleanv");
        _glGetBooleanv.invoke(Void.class, new Object[]{target_,index_,tmp});
        for(int i=0;i<data_.length;++i)
            data_[i] = ((tmp[i]==0) ? false:true);
    }

    private static Function _glDebugMessageCallback;
    public interface DebugCallback{
        public void callback( int source, int type, int id, int severity,
            int length, String message, Object userparam );
    }
    private static class proxy implements Callback {
        DebugCallback cb;
        Object uparam;
        public void callback( int source, int type, int id, int severity,
            int length, String message, Pointer userparam ){
            if( cb != null )
                cb.callback(source,type,id,severity,length,message,uparam);
        }
    }
    //we keep the proxy so we can ensure it never becomes null
    private static proxy prx = new proxy();
    public static void glDebugMessageCallback( DebugCallback c, Object uparam_){
        prx.uparam=uparam_;
        prx.cb = c;
        if( _glDebugMessageCallback == null )
            _glDebugMessageCallback = loadFunction("glDebugMessageCallback");
        _glDebugMessageCallback.invoke(Void.class, new Object[]{prx,new Pointer(0)});
    }
        
    public static void glTexImage2D(int target_, int level_, int internalformat_,
        int width_, int height_, int border_, int format_, int type_, int[] data_){
        if( _glTexImage2D == null )
            _glTexImage2D = loadFunction("glTexImage2D");
        _glTexImage2D.invoke(Void.class,new Object[]{
            target_,level_,internalformat_,width_,height_,border_,format_,type_,data_});
    }
    
    public static void glDrawElements(int mode_, int count_, int type_, int offset_ ){
        if( _glDrawElements == null )
            _glDrawElements = loadFunction("glDrawElements");
        _glDrawElements.invoke( Void.class, new Object[] {
            mode_,count_,type_, Pointer.createConstant(offset_) });
    }
    
    public static void glDrawElementsInstanced(int mode_, int count_, int type_, int offset_ , int primcount_){
        if( _glDrawElementsInstanced == null )
            _glDrawElementsInstanced = loadFunction("glDrawElementsInstanced");
        _glDrawElements.invoke( Void.class, new Object[] {
            mode_,count_,type_, Pointer.createConstant(offset_), primcount_ });
    }
    
    /** Store data.
    @param size_ Size of the array, in floats (not bytes)
    */
    public static void glBufferData(int target_, int size_, float[] data, int usage_){
        ByteBuffer bb = ByteBuffer.allocate(4*size_);
        bb.order(ByteOrder.nativeOrder());
        bb.asFloatBuffer().put(data,0,size_);
        byte[] a = bb.array();
        glBufferData(target_,a.length,a,usage_);
    }
    
    public static void glBufferData(int target_, int size_, int[] data, int usage_){
        ByteBuffer bb = ByteBuffer.allocate(4*size_);
        bb.order(ByteOrder.nativeOrder());
        bb.asIntBuffer().put(data,0,size_);
        byte[] a = bb.array();
        glBufferData(target_,a.length,a,usage_);
    }
    
    public static void glBufferData(int target_, int size_, short[] data, int usage_){
        ByteBuffer bb = ByteBuffer.allocate(2*size_);
        bb.order(ByteOrder.nativeOrder());
        bb.asShortBuffer().put(data,0,size_);
        byte[] a = bb.array();
        glBufferData(target_,a.length,a,usage_);
    }
    
    public static void glBufferData(int target_, int size_, double[] data, int usage_){
        ByteBuffer bb = ByteBuffer.allocate(8*size_);
        bb.order(ByteOrder.nativeOrder());
        bb.asDoubleBuffer().put(data,0,size_);
        byte[] a = bb.array();
        glBufferData(target_,a.length,a,usage_);
    }
    public static final int GL_CURRENT_BIT = 0x00000001;
    public static final int GL_POINT_BIT = 0x00000002;
    public static final int GL_LINE_BIT = 0x00000004;
    public static final int GL_POLYGON_BIT = 0x00000008;
    public static final int GL_POLYGON_STIPPLE_BIT = 0x00000010;
    public static final int GL_PIXEL_MODE_BIT = 0x00000020;
    public static final int GL_LIGHTING_BIT = 0x00000040;
    public static final int GL_FOG_BIT = 0x00000080;
    public static final int GL_DEPTH_BUFFER_BIT = 0x00000100;
    public static final int GL_ACCUM_BUFFER_BIT = 0x00000200;
    public static final int GL_STENCIL_BUFFER_BIT = 0x00000400;
    public static final int GL_VIEWPORT_BIT = 0x00000800;
    public static final int GL_TRANSFORM_BIT = 0x00001000;
    public static final int GL_ENABLE_BIT = 0x00002000;
    public static final int GL_COLOR_BUFFER_BIT = 0x00004000;
    public static final int GL_HINT_BIT = 0x00008000;
    public static final int GL_EVAL_BIT = 0x00010000;
    public static final int GL_LIST_BIT = 0x00020000;
    public static final int GL_TEXTURE_BIT = 0x00040000;
    public static final int GL_SCISSOR_BIT = 0x00080000;
    public static final int GL_MULTISAMPLE_BIT = 0x20000000;
    public static final int GL_ALL_ATTRIB_BITS = 0xFFFFFFFF;
    public static final int GL_CLIENT_PIXEL_STORE_BIT = 0x00000001;
    public static final int GL_CLIENT_VERTEX_ARRAY_BIT = 0x00000002;
    public static final int GL_CLIENT_ALL_ATTRIB_BITS = 0xFFFFFFFF;
    public static final int GL_CONTEXT_FLAG_FORWARD_COMPATIBLE_BIT = 0x00000001;
    public static final int GL_CONTEXT_FLAG_DEBUG_BIT = 0x00000002;
    public static final int GL_CONTEXT_FLAG_ROBUST_ACCESS_BIT = 0x00000004;
    public static final int GL_CONTEXT_CORE_PROFILE_BIT = 0x00000001;
    public static final int GL_CONTEXT_COMPATIBILITY_PROFILE_BIT = 0x00000002;
    public static final int GL_MAP_READ_BIT = 0x0001;
    public static final int GL_MAP_WRITE_BIT = 0x0002;
    public static final int GL_MAP_INVALIDATE_RANGE_BIT = 0x0004;
    public static final int GL_MAP_INVALIDATE_BUFFER_BIT = 0x0008;
    public static final int GL_MAP_FLUSH_EXPLICIT_BIT = 0x0010;
    public static final int GL_MAP_UNSYNCHRONIZED_BIT = 0x0020;
    public static final int GL_MAP_PERSISTENT_BIT = 0x0040;
    public static final int GL_MAP_COHERENT_BIT = 0x0080;
    public static final int GL_DYNAMIC_STORAGE_BIT = 0x0100;
    public static final int GL_CLIENT_STORAGE_BIT = 0x0200;
    public static final int GL_VERTEX_ATTRIB_ARRAY_BARRIER_BIT = 0x00000001;
    public static final int GL_ELEMENT_ARRAY_BARRIER_BIT = 0x00000002;
    public static final int GL_UNIFORM_BARRIER_BIT = 0x00000004;
    public static final int GL_TEXTURE_FETCH_BARRIER_BIT = 0x00000008;
    public static final int GL_SHADER_IMAGE_ACCESS_BARRIER_BIT = 0x00000020;
    public static final int GL_COMMAND_BARRIER_BIT = 0x00000040;
    public static final int GL_PIXEL_BUFFER_BARRIER_BIT = 0x00000080;
    public static final int GL_TEXTURE_UPDATE_BARRIER_BIT = 0x00000100;
    public static final int GL_BUFFER_UPDATE_BARRIER_BIT = 0x00000200;
    public static final int GL_FRAMEBUFFER_BARRIER_BIT = 0x00000400;
    public static final int GL_TRANSFORM_FEEDBACK_BARRIER_BIT = 0x00000800;
    public static final int GL_ATOMIC_COUNTER_BARRIER_BIT = 0x00001000;
    public static final int GL_SHADER_STORAGE_BARRIER_BIT = 0x00002000;
    public static final int GL_CLIENT_MAPPED_BUFFER_BARRIER_BIT = 0x00004000;
    public static final int GL_QUERY_BUFFER_BARRIER_BIT = 0x00008000;
    public static final int GL_ALL_BARRIER_BITS = 0xFFFFFFFF;
    public static final int GL_SYNC_FLUSH_COMMANDS_BIT = 0x00000001;
    public static final int GL_VERTEX_SHADER_BIT = 0x00000001;
    public static final int GL_FRAGMENT_SHADER_BIT = 0x00000002;
    public static final int GL_GEOMETRY_SHADER_BIT = 0x00000004;
    public static final int GL_TESS_CONTROL_SHADER_BIT = 0x00000008;
    public static final int GL_TESS_EVALUATION_SHADER_BIT = 0x00000010;
    public static final int GL_COMPUTE_SHADER_BIT = 0x00000020;
    public static final int GL_ALL_SHADER_BITS = 0xFFFFFFFF;
    public static final int GL_FALSE = 0;
    public static final int GL_NO_ERROR = 0;
    public static final int GL_ZERO = 0;
    public static final int GL_NONE = 0;
    public static final int GL_TRUE = 1;
    public static final int GL_ONE = 1;
    public static final int GL_INVALID_INDEX = 0xFFFFFFFF;
    public static final int GL_TIMEOUT_IGNORED = -1;
    public static final int GL_VERSION_ES_CL_1_0 = 1;
    public static final int GL_VERSION_ES_CM_1_1 = 1;
    public static final int GL_VERSION_ES_CL_1_1 = 1;
    public static final int GL_POINTS = 0x0000;
    public static final int GL_LINES = 0x0001;
    public static final int GL_LINE_LOOP = 0x0002;
    public static final int GL_LINE_STRIP = 0x0003;
    public static final int GL_TRIANGLES = 0x0004;
    public static final int GL_TRIANGLE_STRIP = 0x0005;
    public static final int GL_TRIANGLE_FAN = 0x0006;
    public static final int GL_QUADS = 0x0007;
    public static final int GL_QUAD_STRIP = 0x0008;
    public static final int GL_POLYGON = 0x0009;
    public static final int GL_LINES_ADJACENCY = 0x000A;
    public static final int GL_LINE_STRIP_ADJACENCY = 0x000B;
    public static final int GL_TRIANGLES_ADJACENCY = 0x000C;
    public static final int GL_TRIANGLE_STRIP_ADJACENCY = 0x000D;
    public static final int GL_PATCHES = 0x000E;
    public static final int GL_ACCUM = 0x0100;
    public static final int GL_LOAD = 0x0101;
    public static final int GL_RETURN = 0x0102;
    public static final int GL_MULT = 0x0103;
    public static final int GL_ADD = 0x0104;
    public static final int GL_NEVER = 0x0200;
    public static final int GL_LESS = 0x0201;
    public static final int GL_EQUAL = 0x0202;
    public static final int GL_LEQUAL = 0x0203;
    public static final int GL_GREATER = 0x0204;
    public static final int GL_NOTEQUAL = 0x0205;
    public static final int GL_GEQUAL = 0x0206;
    public static final int GL_ALWAYS = 0x0207;
    public static final int GL_SRC_COLOR = 0x0300;
    public static final int GL_ONE_MINUS_SRC_COLOR = 0x0301;
    public static final int GL_SRC_ALPHA = 0x0302;
    public static final int GL_ONE_MINUS_SRC_ALPHA = 0x0303;
    public static final int GL_DST_ALPHA = 0x0304;
    public static final int GL_ONE_MINUS_DST_ALPHA = 0x0305;
    public static final int GL_DST_COLOR = 0x0306;
    public static final int GL_ONE_MINUS_DST_COLOR = 0x0307;
    public static final int GL_SRC_ALPHA_SATURATE = 0x0308;
    public static final int GL_FRONT_LEFT = 0x0400;
    public static final int GL_FRONT_RIGHT = 0x0401;
    public static final int GL_BACK_LEFT = 0x0402;
    public static final int GL_BACK_RIGHT = 0x0403;
    public static final int GL_FRONT = 0x0404;
    public static final int GL_BACK = 0x0405;
    public static final int GL_LEFT = 0x0406;
    public static final int GL_RIGHT = 0x0407;
    public static final int GL_FRONT_AND_BACK = 0x0408;
    public static final int GL_AUX0 = 0x0409;
    public static final int GL_AUX1 = 0x040A;
    public static final int GL_AUX2 = 0x040B;
    public static final int GL_AUX3 = 0x040C;
    public static final int GL_INVALID_ENUM = 0x0500;
    public static final int GL_INVALID_VALUE = 0x0501;
    public static final int GL_INVALID_OPERATION = 0x0502;
    public static final int GL_STACK_OVERFLOW = 0x0503;
    public static final int GL_STACK_UNDERFLOW = 0x0504;
    public static final int GL_OUT_OF_MEMORY = 0x0505;
    public static final int GL_INVALID_FRAMEBUFFER_OPERATION = 0x0506;
    public static final int GL_CONTEXT_LOST = 0x0507;
    public static final int GL_2D = 0x0600;
    public static final int GL_3D = 0x0601;
    public static final int GL_3D_COLOR = 0x0602;
    public static final int GL_3D_COLOR_TEXTURE = 0x0603;
    public static final int GL_4D_COLOR_TEXTURE = 0x0604;
    public static final int GL_PASS_THROUGH_TOKEN = 0x0700;
    public static final int GL_POINT_TOKEN = 0x0701;
    public static final int GL_LINE_TOKEN = 0x0702;
    public static final int GL_POLYGON_TOKEN = 0x0703;
    public static final int GL_BITMAP_TOKEN = 0x0704;
    public static final int GL_DRAW_PIXEL_TOKEN = 0x0705;
    public static final int GL_COPY_PIXEL_TOKEN = 0x0706;
    public static final int GL_LINE_RESET_TOKEN = 0x0707;
    public static final int GL_EXP = 0x0800;
    public static final int GL_EXP2 = 0x0801;
    public static final int GL_CW = 0x0900;
    public static final int GL_CCW = 0x0901;
    public static final int GL_COEFF = 0x0A00;
    public static final int GL_ORDER = 0x0A01;
    public static final int GL_DOMAIN = 0x0A02;
    public static final int GL_CURRENT_COLOR = 0x0B00;
    public static final int GL_CURRENT_INDEX = 0x0B01;
    public static final int GL_CURRENT_NORMAL = 0x0B02;
    public static final int GL_CURRENT_TEXTURE_COORDS = 0x0B03;
    public static final int GL_CURRENT_RASTER_COLOR = 0x0B04;
    public static final int GL_CURRENT_RASTER_INDEX = 0x0B05;
    public static final int GL_CURRENT_RASTER_TEXTURE_COORDS = 0x0B06;
    public static final int GL_CURRENT_RASTER_POSITION = 0x0B07;
    public static final int GL_CURRENT_RASTER_POSITION_VALID = 0x0B08;
    public static final int GL_CURRENT_RASTER_DISTANCE = 0x0B09;
    public static final int GL_POINT_SMOOTH = 0x0B10;
    public static final int GL_POINT_SIZE = 0x0B11;
    public static final int GL_POINT_SIZE_RANGE = 0x0B12;
    public static final int GL_SMOOTH_POINT_SIZE_RANGE = 0x0B12;
    public static final int GL_POINT_SIZE_GRANULARITY = 0x0B13;
    public static final int GL_SMOOTH_POINT_SIZE_GRANULARITY = 0x0B13;
    public static final int GL_LINE_SMOOTH = 0x0B20;
    public static final int GL_LINE_WIDTH = 0x0B21;
    public static final int GL_LINE_WIDTH_RANGE = 0x0B22;
    public static final int GL_SMOOTH_LINE_WIDTH_RANGE = 0x0B22;
    public static final int GL_LINE_WIDTH_GRANULARITY = 0x0B23;
    public static final int GL_SMOOTH_LINE_WIDTH_GRANULARITY = 0x0B23;
    public static final int GL_LINE_STIPPLE = 0x0B24;
    public static final int GL_LINE_STIPPLE_PATTERN = 0x0B25;
    public static final int GL_LINE_STIPPLE_REPEAT = 0x0B26;
    public static final int GL_LIST_MODE = 0x0B30;
    public static final int GL_MAX_LIST_NESTING = 0x0B31;
    public static final int GL_LIST_BASE = 0x0B32;
    public static final int GL_LIST_INDEX = 0x0B33;
    public static final int GL_POLYGON_MODE = 0x0B40;
    public static final int GL_POLYGON_SMOOTH = 0x0B41;
    public static final int GL_POLYGON_STIPPLE = 0x0B42;
    public static final int GL_EDGE_FLAG = 0x0B43;
    public static final int GL_CULL_FACE = 0x0B44;
    public static final int GL_CULL_FACE_MODE = 0x0B45;
    public static final int GL_FRONT_FACE = 0x0B46;
    public static final int GL_LIGHTING = 0x0B50;
    public static final int GL_LIGHT_MODEL_LOCAL_VIEWER = 0x0B51;
    public static final int GL_LIGHT_MODEL_TWO_SIDE = 0x0B52;
    public static final int GL_LIGHT_MODEL_AMBIENT = 0x0B53;
    public static final int GL_SHADE_MODEL = 0x0B54;
    public static final int GL_COLOR_MATERIAL_FACE = 0x0B55;
    public static final int GL_COLOR_MATERIAL_PARAMETER = 0x0B56;
    public static final int GL_COLOR_MATERIAL = 0x0B57;
    public static final int GL_FOG = 0x0B60;
    public static final int GL_FOG_INDEX = 0x0B61;
    public static final int GL_FOG_DENSITY = 0x0B62;
    public static final int GL_FOG_START = 0x0B63;
    public static final int GL_FOG_END = 0x0B64;
    public static final int GL_FOG_MODE = 0x0B65;
    public static final int GL_FOG_COLOR = 0x0B66;
    public static final int GL_DEPTH_RANGE = 0x0B70;
    public static final int GL_DEPTH_TEST = 0x0B71;
    public static final int GL_DEPTH_WRITEMASK = 0x0B72;
    public static final int GL_DEPTH_CLEAR_VALUE = 0x0B73;
    public static final int GL_DEPTH_FUNC = 0x0B74;
    public static final int GL_ACCUM_CLEAR_VALUE = 0x0B80;
    public static final int GL_STENCIL_TEST = 0x0B90;
    public static final int GL_STENCIL_CLEAR_VALUE = 0x0B91;
    public static final int GL_STENCIL_FUNC = 0x0B92;
    public static final int GL_STENCIL_VALUE_MASK = 0x0B93;
    public static final int GL_STENCIL_FAIL = 0x0B94;
    public static final int GL_STENCIL_PASS_DEPTH_FAIL = 0x0B95;
    public static final int GL_STENCIL_PASS_DEPTH_PASS = 0x0B96;
    public static final int GL_STENCIL_REF = 0x0B97;
    public static final int GL_STENCIL_WRITEMASK = 0x0B98;
    public static final int GL_MATRIX_MODE = 0x0BA0;
    public static final int GL_NORMALIZE = 0x0BA1;
    public static final int GL_VIEWPORT = 0x0BA2;
    public static final int GL_MODELVIEW_STACK_DEPTH = 0x0BA3;
    public static final int GL_PROJECTION_STACK_DEPTH = 0x0BA4;
    public static final int GL_TEXTURE_STACK_DEPTH = 0x0BA5;
    public static final int GL_MODELVIEW_MATRIX = 0x0BA6;
    public static final int GL_PROJECTION_MATRIX = 0x0BA7;
    public static final int GL_TEXTURE_MATRIX = 0x0BA8;
    public static final int GL_ATTRIB_STACK_DEPTH = 0x0BB0;
    public static final int GL_CLIENT_ATTRIB_STACK_DEPTH = 0x0BB1;
    public static final int GL_ALPHA_TEST = 0x0BC0;
    public static final int GL_ALPHA_TEST_FUNC = 0x0BC1;
    public static final int GL_ALPHA_TEST_REF = 0x0BC2;
    public static final int GL_DITHER = 0x0BD0;
    public static final int GL_BLEND_DST = 0x0BE0;
    public static final int GL_BLEND_SRC = 0x0BE1;
    public static final int GL_BLEND = 0x0BE2;
    public static final int GL_LOGIC_OP_MODE = 0x0BF0;
    public static final int GL_INDEX_LOGIC_OP = 0x0BF1;
    public static final int GL_LOGIC_OP = 0x0BF1;
    public static final int GL_COLOR_LOGIC_OP = 0x0BF2;
    public static final int GL_AUX_BUFFERS = 0x0C00;
    public static final int GL_DRAW_BUFFER = 0x0C01;
    public static final int GL_READ_BUFFER = 0x0C02;
    public static final int GL_SCISSOR_BOX = 0x0C10;
    public static final int GL_SCISSOR_TEST = 0x0C11;
    public static final int GL_INDEX_CLEAR_VALUE = 0x0C20;
    public static final int GL_INDEX_WRITEMASK = 0x0C21;
    public static final int GL_COLOR_CLEAR_VALUE = 0x0C22;
    public static final int GL_COLOR_WRITEMASK = 0x0C23;
    public static final int GL_INDEX_MODE = 0x0C30;
    public static final int GL_RGBA_MODE = 0x0C31;
    public static final int GL_DOUBLEBUFFER = 0x0C32;
    public static final int GL_STEREO = 0x0C33;
    public static final int GL_RENDER_MODE = 0x0C40;
    public static final int GL_PERSPECTIVE_CORRECTION_HINT = 0x0C50;
    public static final int GL_POINT_SMOOTH_HINT = 0x0C51;
    public static final int GL_LINE_SMOOTH_HINT = 0x0C52;
    public static final int GL_POLYGON_SMOOTH_HINT = 0x0C53;
    public static final int GL_FOG_HINT = 0x0C54;
    public static final int GL_TEXTURE_GEN_S = 0x0C60;
    public static final int GL_TEXTURE_GEN_T = 0x0C61;
    public static final int GL_TEXTURE_GEN_R = 0x0C62;
    public static final int GL_TEXTURE_GEN_Q = 0x0C63;
    public static final int GL_PIXEL_MAP_I_TO_I = 0x0C70;
    public static final int GL_PIXEL_MAP_S_TO_S = 0x0C71;
    public static final int GL_PIXEL_MAP_I_TO_R = 0x0C72;
    public static final int GL_PIXEL_MAP_I_TO_G = 0x0C73;
    public static final int GL_PIXEL_MAP_I_TO_B = 0x0C74;
    public static final int GL_PIXEL_MAP_I_TO_A = 0x0C75;
    public static final int GL_PIXEL_MAP_R_TO_R = 0x0C76;
    public static final int GL_PIXEL_MAP_G_TO_G = 0x0C77;
    public static final int GL_PIXEL_MAP_B_TO_B = 0x0C78;
    public static final int GL_PIXEL_MAP_A_TO_A = 0x0C79;
    public static final int GL_PIXEL_MAP_I_TO_I_SIZE = 0x0CB0;
    public static final int GL_PIXEL_MAP_S_TO_S_SIZE = 0x0CB1;
    public static final int GL_PIXEL_MAP_I_TO_R_SIZE = 0x0CB2;
    public static final int GL_PIXEL_MAP_I_TO_G_SIZE = 0x0CB3;
    public static final int GL_PIXEL_MAP_I_TO_B_SIZE = 0x0CB4;
    public static final int GL_PIXEL_MAP_I_TO_A_SIZE = 0x0CB5;
    public static final int GL_PIXEL_MAP_R_TO_R_SIZE = 0x0CB6;
    public static final int GL_PIXEL_MAP_G_TO_G_SIZE = 0x0CB7;
    public static final int GL_PIXEL_MAP_B_TO_B_SIZE = 0x0CB8;
    public static final int GL_PIXEL_MAP_A_TO_A_SIZE = 0x0CB9;
    public static final int GL_UNPACK_SWAP_BYTES = 0x0CF0;
    public static final int GL_UNPACK_LSB_FIRST = 0x0CF1;
    public static final int GL_UNPACK_ROW_LENGTH = 0x0CF2;
    public static final int GL_UNPACK_SKIP_ROWS = 0x0CF3;
    public static final int GL_UNPACK_SKIP_PIXELS = 0x0CF4;
    public static final int GL_UNPACK_ALIGNMENT = 0x0CF5;
    public static final int GL_PACK_SWAP_BYTES = 0x0D00;
    public static final int GL_PACK_LSB_FIRST = 0x0D01;
    public static final int GL_PACK_ROW_LENGTH = 0x0D02;
    public static final int GL_PACK_SKIP_ROWS = 0x0D03;
    public static final int GL_PACK_SKIP_PIXELS = 0x0D04;
    public static final int GL_PACK_ALIGNMENT = 0x0D05;
    public static final int GL_MAP_COLOR = 0x0D10;
    public static final int GL_MAP_STENCIL = 0x0D11;
    public static final int GL_INDEX_SHIFT = 0x0D12;
    public static final int GL_INDEX_OFFSET = 0x0D13;
    public static final int GL_RED_SCALE = 0x0D14;
    public static final int GL_RED_BIAS = 0x0D15;
    public static final int GL_ZOOM_X = 0x0D16;
    public static final int GL_ZOOM_Y = 0x0D17;
    public static final int GL_GREEN_SCALE = 0x0D18;
    public static final int GL_GREEN_BIAS = 0x0D19;
    public static final int GL_BLUE_SCALE = 0x0D1A;
    public static final int GL_BLUE_BIAS = 0x0D1B;
    public static final int GL_ALPHA_SCALE = 0x0D1C;
    public static final int GL_ALPHA_BIAS = 0x0D1D;
    public static final int GL_DEPTH_SCALE = 0x0D1E;
    public static final int GL_DEPTH_BIAS = 0x0D1F;
    public static final int GL_MAX_EVAL_ORDER = 0x0D30;
    public static final int GL_MAX_LIGHTS = 0x0D31;
    public static final int GL_MAX_CLIP_PLANES = 0x0D32;
    public static final int GL_MAX_CLIP_DISTANCES = 0x0D32;
    public static final int GL_MAX_TEXTURE_SIZE = 0x0D33;
    public static final int GL_MAX_PIXEL_MAP_TABLE = 0x0D34;
    public static final int GL_MAX_ATTRIB_STACK_DEPTH = 0x0D35;
    public static final int GL_MAX_MODELVIEW_STACK_DEPTH = 0x0D36;
    public static final int GL_MAX_NAME_STACK_DEPTH = 0x0D37;
    public static final int GL_MAX_PROJECTION_STACK_DEPTH = 0x0D38;
    public static final int GL_MAX_TEXTURE_STACK_DEPTH = 0x0D39;
    public static final int GL_MAX_VIEWPORT_DIMS = 0x0D3A;
    public static final int GL_MAX_CLIENT_ATTRIB_STACK_DEPTH = 0x0D3B;
    public static final int GL_SUBPIXEL_BITS = 0x0D50;
    public static final int GL_INDEX_BITS = 0x0D51;
    public static final int GL_RED_BITS = 0x0D52;
    public static final int GL_GREEN_BITS = 0x0D53;
    public static final int GL_BLUE_BITS = 0x0D54;
    public static final int GL_ALPHA_BITS = 0x0D55;
    public static final int GL_DEPTH_BITS = 0x0D56;
    public static final int GL_STENCIL_BITS = 0x0D57;
    public static final int GL_ACCUM_RED_BITS = 0x0D58;
    public static final int GL_ACCUM_GREEN_BITS = 0x0D59;
    public static final int GL_ACCUM_BLUE_BITS = 0x0D5A;
    public static final int GL_ACCUM_ALPHA_BITS = 0x0D5B;
    public static final int GL_NAME_STACK_DEPTH = 0x0D70;
    public static final int GL_AUTO_NORMAL = 0x0D80;
    public static final int GL_MAP1_COLOR_4 = 0x0D90;
    public static final int GL_MAP1_INDEX = 0x0D91;
    public static final int GL_MAP1_NORMAL = 0x0D92;
    public static final int GL_MAP1_TEXTURE_COORD_1 = 0x0D93;
    public static final int GL_MAP1_TEXTURE_COORD_2 = 0x0D94;
    public static final int GL_MAP1_TEXTURE_COORD_3 = 0x0D95;
    public static final int GL_MAP1_TEXTURE_COORD_4 = 0x0D96;
    public static final int GL_MAP1_VERTEX_3 = 0x0D97;
    public static final int GL_MAP1_VERTEX_4 = 0x0D98;
    public static final int GL_MAP2_COLOR_4 = 0x0DB0;
    public static final int GL_MAP2_INDEX = 0x0DB1;
    public static final int GL_MAP2_NORMAL = 0x0DB2;
    public static final int GL_MAP2_TEXTURE_COORD_1 = 0x0DB3;
    public static final int GL_MAP2_TEXTURE_COORD_2 = 0x0DB4;
    public static final int GL_MAP2_TEXTURE_COORD_3 = 0x0DB5;
    public static final int GL_MAP2_TEXTURE_COORD_4 = 0x0DB6;
    public static final int GL_MAP2_VERTEX_3 = 0x0DB7;
    public static final int GL_MAP2_VERTEX_4 = 0x0DB8;
    public static final int GL_MAP1_GRID_DOMAIN = 0x0DD0;
    public static final int GL_MAP1_GRID_SEGMENTS = 0x0DD1;
    public static final int GL_MAP2_GRID_DOMAIN = 0x0DD2;
    public static final int GL_MAP2_GRID_SEGMENTS = 0x0DD3;
    public static final int GL_TEXTURE_1D = 0x0DE0;
    public static final int GL_TEXTURE_2D = 0x0DE1;
    public static final int GL_FEEDBACK_BUFFER_POINTER = 0x0DF0;
    public static final int GL_FEEDBACK_BUFFER_SIZE = 0x0DF1;
    public static final int GL_FEEDBACK_BUFFER_TYPE = 0x0DF2;
    public static final int GL_SELECTION_BUFFER_POINTER = 0x0DF3;
    public static final int GL_SELECTION_BUFFER_SIZE = 0x0DF4;
    public static final int GL_TEXTURE_WIDTH = 0x1000;
    public static final int GL_TEXTURE_HEIGHT = 0x1001;
    public static final int GL_TEXTURE_INTERNAL_FORMAT = 0x1003;
    public static final int GL_TEXTURE_COMPONENTS = 0x1003;
    public static final int GL_TEXTURE_BORDER_COLOR = 0x1004;
    public static final int GL_TEXTURE_BORDER = 0x1005;
    public static final int GL_TEXTURE_TARGET = 0x1006;
    public static final int GL_DONT_CARE = 0x1100;
    public static final int GL_FASTEST = 0x1101;
    public static final int GL_NICEST = 0x1102;
    public static final int GL_AMBIENT = 0x1200;
    public static final int GL_DIFFUSE = 0x1201;
    public static final int GL_SPECULAR = 0x1202;
    public static final int GL_POSITION = 0x1203;
    public static final int GL_SPOT_DIRECTION = 0x1204;
    public static final int GL_SPOT_EXPONENT = 0x1205;
    public static final int GL_SPOT_CUTOFF = 0x1206;
    public static final int GL_CONSTANT_ATTENUATION = 0x1207;
    public static final int GL_LINEAR_ATTENUATION = 0x1208;
    public static final int GL_QUADRATIC_ATTENUATION = 0x1209;
    public static final int GL_COMPILE = 0x1300;
    public static final int GL_COMPILE_AND_EXECUTE = 0x1301;
    public static final int GL_BYTE = 0x1400;
    public static final int GL_UNSIGNED_BYTE = 0x1401;
    public static final int GL_SHORT = 0x1402;
    public static final int GL_UNSIGNED_SHORT = 0x1403;
    public static final int GL_INT = 0x1404;
    public static final int GL_UNSIGNED_INT = 0x1405;
    public static final int GL_FLOAT = 0x1406;
    public static final int GL_2_BYTES = 0x1407;
    public static final int GL_3_BYTES = 0x1408;
    public static final int GL_4_BYTES = 0x1409;
    public static final int GL_DOUBLE = 0x140A;
    public static final int GL_HALF_FLOAT = 0x140B;
    public static final int GL_FIXED = 0x140C;
    public static final int GL_CLEAR = 0x1500;
    public static final int GL_AND = 0x1501;
    public static final int GL_AND_REVERSE = 0x1502;
    public static final int GL_COPY = 0x1503;
    public static final int GL_AND_INVERTED = 0x1504;
    public static final int GL_NOOP = 0x1505;
    public static final int GL_XOR = 0x1506;
    public static final int GL_OR = 0x1507;
    public static final int GL_NOR = 0x1508;
    public static final int GL_EQUIV = 0x1509;
    public static final int GL_INVERT = 0x150A;
    public static final int GL_OR_REVERSE = 0x150B;
    public static final int GL_COPY_INVERTED = 0x150C;
    public static final int GL_OR_INVERTED = 0x150D;
    public static final int GL_NAND = 0x150E;
    public static final int GL_SET = 0x150F;
    public static final int GL_EMISSION = 0x1600;
    public static final int GL_SHININESS = 0x1601;
    public static final int GL_AMBIENT_AND_DIFFUSE = 0x1602;
    public static final int GL_COLOR_INDEXES = 0x1603;
    public static final int GL_MODELVIEW = 0x1700;
    public static final int GL_PROJECTION = 0x1701;
    public static final int GL_TEXTURE = 0x1702;
    public static final int GL_COLOR = 0x1800;
    public static final int GL_DEPTH = 0x1801;
    public static final int GL_STENCIL = 0x1802;
    public static final int GL_COLOR_INDEX = 0x1900;
    public static final int GL_STENCIL_INDEX = 0x1901;
    public static final int GL_DEPTH_COMPONENT = 0x1902;
    public static final int GL_RED = 0x1903;
    public static final int GL_GREEN = 0x1904;
    public static final int GL_BLUE = 0x1905;
    public static final int GL_ALPHA = 0x1906;
    public static final int GL_RGB = 0x1907;
    public static final int GL_RGBA = 0x1908;
    public static final int GL_LUMINANCE = 0x1909;
    public static final int GL_LUMINANCE_ALPHA = 0x190A;
    public static final int GL_BITMAP = 0x1A00;
    public static final int GL_POINT = 0x1B00;
    public static final int GL_LINE = 0x1B01;
    public static final int GL_FILL = 0x1B02;
    public static final int GL_RENDER = 0x1C00;
    public static final int GL_FEEDBACK = 0x1C01;
    public static final int GL_SELECT = 0x1C02;
    public static final int GL_FLAT = 0x1D00;
    public static final int GL_SMOOTH = 0x1D01;
    public static final int GL_KEEP = 0x1E00;
    public static final int GL_REPLACE = 0x1E01;
    public static final int GL_INCR = 0x1E02;
    public static final int GL_DECR = 0x1E03;
    public static final int GL_VENDOR = 0x1F00;
    public static final int GL_RENDERER = 0x1F01;
    public static final int GL_VERSION = 0x1F02;
    public static final int GL_EXTENSIONS = 0x1F03;
    public static final int GL_S = 0x2000;
    public static final int GL_T = 0x2001;
    public static final int GL_R = 0x2002;
    public static final int GL_Q = 0x2003;
    public static final int GL_MODULATE = 0x2100;
    public static final int GL_DECAL = 0x2101;
    public static final int GL_TEXTURE_ENV_MODE = 0x2200;
    public static final int GL_TEXTURE_ENV_COLOR = 0x2201;
    public static final int GL_TEXTURE_ENV = 0x2300;
    public static final int GL_EYE_LINEAR = 0x2400;
    public static final int GL_OBJECT_LINEAR = 0x2401;
    public static final int GL_SPHERE_MAP = 0x2402;
    public static final int GL_TEXTURE_GEN_MODE = 0x2500;
    public static final int GL_OBJECT_PLANE = 0x2501;
    public static final int GL_EYE_PLANE = 0x2502;
    public static final int GL_NEAREST = 0x2600;
    public static final int GL_LINEAR = 0x2601;
    public static final int GL_NEAREST_MIPMAP_NEAREST = 0x2700;
    public static final int GL_LINEAR_MIPMAP_NEAREST = 0x2701;
    public static final int GL_NEAREST_MIPMAP_LINEAR = 0x2702;
    public static final int GL_LINEAR_MIPMAP_LINEAR = 0x2703;
    public static final int GL_TEXTURE_MAG_FILTER = 0x2800;
    public static final int GL_TEXTURE_MIN_FILTER = 0x2801;
    public static final int GL_TEXTURE_WRAP_S = 0x2802;
    public static final int GL_TEXTURE_WRAP_T = 0x2803;
    public static final int GL_CLAMP = 0x2900;
    public static final int GL_REPEAT = 0x2901;
    public static final int GL_POLYGON_OFFSET_UNITS = 0x2A00;
    public static final int GL_POLYGON_OFFSET_POINT = 0x2A01;
    public static final int GL_POLYGON_OFFSET_LINE = 0x2A02;
    public static final int GL_R3_G3_B2 = 0x2A10;
    public static final int GL_V2F = 0x2A20;
    public static final int GL_V3F = 0x2A21;
    public static final int GL_C4UB_V2F = 0x2A22;
    public static final int GL_C4UB_V3F = 0x2A23;
    public static final int GL_C3F_V3F = 0x2A24;
    public static final int GL_N3F_V3F = 0x2A25;
    public static final int GL_C4F_N3F_V3F = 0x2A26;
    public static final int GL_T2F_V3F = 0x2A27;
    public static final int GL_T4F_V4F = 0x2A28;
    public static final int GL_T2F_C4UB_V3F = 0x2A29;
    public static final int GL_T2F_C3F_V3F = 0x2A2A;
    public static final int GL_T2F_N3F_V3F = 0x2A2B;
    public static final int GL_T2F_C4F_N3F_V3F = 0x2A2C;
    public static final int GL_T4F_C4F_N3F_V4F = 0x2A2D;
    public static final int GL_CLIP_PLANE0 = 0x3000;
    public static final int GL_CLIP_DISTANCE0 = 0x3000;
    public static final int GL_CLIP_PLANE1 = 0x3001;
    public static final int GL_CLIP_DISTANCE1 = 0x3001;
    public static final int GL_CLIP_PLANE2 = 0x3002;
    public static final int GL_CLIP_DISTANCE2 = 0x3002;
    public static final int GL_CLIP_PLANE3 = 0x3003;
    public static final int GL_CLIP_DISTANCE3 = 0x3003;
    public static final int GL_CLIP_PLANE4 = 0x3004;
    public static final int GL_CLIP_DISTANCE4 = 0x3004;
    public static final int GL_CLIP_PLANE5 = 0x3005;
    public static final int GL_CLIP_DISTANCE5 = 0x3005;
    public static final int GL_CLIP_DISTANCE6 = 0x3006;
    public static final int GL_CLIP_DISTANCE7 = 0x3007;
    public static final int GL_LIGHT0 = 0x4000;
    public static final int GL_LIGHT1 = 0x4001;
    public static final int GL_LIGHT2 = 0x4002;
    public static final int GL_LIGHT3 = 0x4003;
    public static final int GL_LIGHT4 = 0x4004;
    public static final int GL_LIGHT5 = 0x4005;
    public static final int GL_LIGHT6 = 0x4006;
    public static final int GL_LIGHT7 = 0x4007;
    public static final int GL_CONSTANT_COLOR = 0x8001;
    public static final int GL_ONE_MINUS_CONSTANT_COLOR = 0x8002;
    public static final int GL_CONSTANT_ALPHA = 0x8003;
    public static final int GL_ONE_MINUS_CONSTANT_ALPHA = 0x8004;
    public static final int GL_BLEND_COLOR = 0x8005;
    public static final int GL_FUNC_ADD = 0x8006;
    public static final int GL_MIN = 0x8007;
    public static final int GL_MAX = 0x8008;
    public static final int GL_BLEND_EQUATION = 0x8009;
    public static final int GL_BLEND_EQUATION_RGB = 0x8009;
    public static final int GL_FUNC_SUBTRACT = 0x800A;
    public static final int GL_FUNC_REVERSE_SUBTRACT = 0x800B;
    public static final int GL_CONVOLUTION_1D = 0x8010;
    public static final int GL_CONVOLUTION_2D = 0x8011;
    public static final int GL_SEPARABLE_2D = 0x8012;
    public static final int GL_CONVOLUTION_BORDER_MODE = 0x8013;
    public static final int GL_CONVOLUTION_FILTER_SCALE = 0x8014;
    public static final int GL_CONVOLUTION_FILTER_BIAS = 0x8015;
    public static final int GL_REDUCE = 0x8016;
    public static final int GL_CONVOLUTION_FORMAT = 0x8017;
    public static final int GL_CONVOLUTION_WIDTH = 0x8018;
    public static final int GL_CONVOLUTION_HEIGHT = 0x8019;
    public static final int GL_MAX_CONVOLUTION_WIDTH = 0x801A;
    public static final int GL_MAX_CONVOLUTION_HEIGHT = 0x801B;
    public static final int GL_POST_CONVOLUTION_RED_SCALE = 0x801C;
    public static final int GL_POST_CONVOLUTION_GREEN_SCALE = 0x801D;
    public static final int GL_POST_CONVOLUTION_BLUE_SCALE = 0x801E;
    public static final int GL_POST_CONVOLUTION_ALPHA_SCALE = 0x801F;
    public static final int GL_POST_CONVOLUTION_RED_BIAS = 0x8020;
    public static final int GL_POST_CONVOLUTION_GREEN_BIAS = 0x8021;
    public static final int GL_POST_CONVOLUTION_BLUE_BIAS = 0x8022;
    public static final int GL_POST_CONVOLUTION_ALPHA_BIAS = 0x8023;
    public static final int GL_HISTOGRAM = 0x8024;
    public static final int GL_PROXY_HISTOGRAM = 0x8025;
    public static final int GL_HISTOGRAM_WIDTH = 0x8026;
    public static final int GL_HISTOGRAM_FORMAT = 0x8027;
    public static final int GL_HISTOGRAM_RED_SIZE = 0x8028;
    public static final int GL_HISTOGRAM_GREEN_SIZE = 0x8029;
    public static final int GL_HISTOGRAM_BLUE_SIZE = 0x802A;
    public static final int GL_HISTOGRAM_ALPHA_SIZE = 0x802B;
    public static final int GL_HISTOGRAM_LUMINANCE_SIZE = 0x802C;
    public static final int GL_HISTOGRAM_SINK = 0x802D;
    public static final int GL_MINMAX = 0x802E;
    public static final int GL_MINMAX_FORMAT = 0x802F;
    public static final int GL_MINMAX_SINK = 0x8030;
    public static final int GL_TABLE_TOO_LARGE = 0x8031;
    public static final int GL_UNSIGNED_BYTE_3_3_2 = 0x8032;
    public static final int GL_UNSIGNED_SHORT_4_4_4_4 = 0x8033;
    public static final int GL_UNSIGNED_SHORT_5_5_5_1 = 0x8034;
    public static final int GL_UNSIGNED_INT_8_8_8_8 = 0x8035;
    public static final int GL_UNSIGNED_INT_10_10_10_2 = 0x8036;
    public static final int GL_POLYGON_OFFSET_FILL = 0x8037;
    public static final int GL_POLYGON_OFFSET_FACTOR = 0x8038;
    public static final int GL_RESCALE_NORMAL = 0x803A;
    public static final int GL_ALPHA4 = 0x803B;
    public static final int GL_ALPHA8 = 0x803C;
    public static final int GL_ALPHA12 = 0x803D;
    public static final int GL_ALPHA16 = 0x803E;
    public static final int GL_LUMINANCE4 = 0x803F;
    public static final int GL_LUMINANCE8 = 0x8040;
    public static final int GL_LUMINANCE12 = 0x8041;
    public static final int GL_LUMINANCE16 = 0x8042;
    public static final int GL_LUMINANCE4_ALPHA4 = 0x8043;
    public static final int GL_LUMINANCE6_ALPHA2 = 0x8044;
    public static final int GL_LUMINANCE8_ALPHA8 = 0x8045;
    public static final int GL_LUMINANCE12_ALPHA4 = 0x8046;
    public static final int GL_LUMINANCE12_ALPHA12 = 0x8047;
    public static final int GL_LUMINANCE16_ALPHA16 = 0x8048;
    public static final int GL_INTENSITY = 0x8049;
    public static final int GL_INTENSITY4 = 0x804A;
    public static final int GL_INTENSITY8 = 0x804B;
    public static final int GL_INTENSITY12 = 0x804C;
    public static final int GL_INTENSITY16 = 0x804D;
    public static final int GL_RGB4 = 0x804F;
    public static final int GL_RGB5 = 0x8050;
    public static final int GL_RGB8 = 0x8051;
    public static final int GL_RGB10 = 0x8052;
    public static final int GL_RGB12 = 0x8053;
    public static final int GL_RGB16 = 0x8054;
    public static final int GL_RGBA2 = 0x8055;
    public static final int GL_RGBA4 = 0x8056;
    public static final int GL_RGB5_A1 = 0x8057;
    public static final int GL_RGBA8 = 0x8058;
    public static final int GL_RGB10_A2 = 0x8059;
    public static final int GL_RGBA12 = 0x805A;
    public static final int GL_RGBA16 = 0x805B;
    public static final int GL_TEXTURE_RED_SIZE = 0x805C;
    public static final int GL_TEXTURE_GREEN_SIZE = 0x805D;
    public static final int GL_TEXTURE_BLUE_SIZE = 0x805E;
    public static final int GL_TEXTURE_ALPHA_SIZE = 0x805F;
    public static final int GL_TEXTURE_LUMINANCE_SIZE = 0x8060;
    public static final int GL_TEXTURE_INTENSITY_SIZE = 0x8061;
    public static final int GL_PROXY_TEXTURE_1D = 0x8063;
    public static final int GL_PROXY_TEXTURE_2D = 0x8064;
    public static final int GL_TEXTURE_PRIORITY = 0x8066;
    public static final int GL_TEXTURE_RESIDENT = 0x8067;
    public static final int GL_TEXTURE_BINDING_1D = 0x8068;
    public static final int GL_TEXTURE_BINDING_2D = 0x8069;
    public static final int GL_TEXTURE_BINDING_3D = 0x806A;
    public static final int GL_PACK_SKIP_IMAGES = 0x806B;
    public static final int GL_PACK_IMAGE_HEIGHT = 0x806C;
    public static final int GL_UNPACK_SKIP_IMAGES = 0x806D;
    public static final int GL_UNPACK_IMAGE_HEIGHT = 0x806E;
    public static final int GL_TEXTURE_3D = 0x806F;
    public static final int GL_PROXY_TEXTURE_3D = 0x8070;
    public static final int GL_TEXTURE_DEPTH = 0x8071;
    public static final int GL_TEXTURE_WRAP_R = 0x8072;
    public static final int GL_MAX_3D_TEXTURE_SIZE = 0x8073;
    public static final int GL_VERTEX_ARRAY = 0x8074;
    public static final int GL_NORMAL_ARRAY = 0x8075;
    public static final int GL_COLOR_ARRAY = 0x8076;
    public static final int GL_INDEX_ARRAY = 0x8077;
    public static final int GL_TEXTURE_COORD_ARRAY = 0x8078;
    public static final int GL_EDGE_FLAG_ARRAY = 0x8079;
    public static final int GL_VERTEX_ARRAY_SIZE = 0x807A;
    public static final int GL_VERTEX_ARRAY_TYPE = 0x807B;
    public static final int GL_VERTEX_ARRAY_STRIDE = 0x807C;
    public static final int GL_NORMAL_ARRAY_TYPE = 0x807E;
    public static final int GL_NORMAL_ARRAY_STRIDE = 0x807F;
    public static final int GL_COLOR_ARRAY_SIZE = 0x8081;
    public static final int GL_COLOR_ARRAY_TYPE = 0x8082;
    public static final int GL_COLOR_ARRAY_STRIDE = 0x8083;
    public static final int GL_INDEX_ARRAY_TYPE = 0x8085;
    public static final int GL_INDEX_ARRAY_STRIDE = 0x8086;
    public static final int GL_TEXTURE_COORD_ARRAY_SIZE = 0x8088;
    public static final int GL_TEXTURE_COORD_ARRAY_TYPE = 0x8089;
    public static final int GL_TEXTURE_COORD_ARRAY_STRIDE = 0x808A;
    public static final int GL_EDGE_FLAG_ARRAY_STRIDE = 0x808C;
    public static final int GL_VERTEX_ARRAY_POINTER = 0x808E;
    public static final int GL_NORMAL_ARRAY_POINTER = 0x808F;
    public static final int GL_COLOR_ARRAY_POINTER = 0x8090;
    public static final int GL_INDEX_ARRAY_POINTER = 0x8091;
    public static final int GL_TEXTURE_COORD_ARRAY_POINTER = 0x8092;
    public static final int GL_EDGE_FLAG_ARRAY_POINTER = 0x8093;
    public static final int GL_MULTISAMPLE = 0x809D;
    public static final int GL_SAMPLE_ALPHA_TO_COVERAGE = 0x809E;
    public static final int GL_SAMPLE_ALPHA_TO_ONE = 0x809F;
    public static final int GL_SAMPLE_COVERAGE = 0x80A0;
    public static final int GL_SAMPLE_BUFFERS = 0x80A8;
    public static final int GL_SAMPLES = 0x80A9;
    public static final int GL_SAMPLE_COVERAGE_VALUE = 0x80AA;
    public static final int GL_SAMPLE_COVERAGE_INVERT = 0x80AB;
    public static final int GL_COLOR_MATRIX = 0x80B1;
    public static final int GL_COLOR_MATRIX_STACK_DEPTH = 0x80B2;
    public static final int GL_MAX_COLOR_MATRIX_STACK_DEPTH = 0x80B3;
    public static final int GL_POST_COLOR_MATRIX_RED_SCALE = 0x80B4;
    public static final int GL_POST_COLOR_MATRIX_GREEN_SCALE = 0x80B5;
    public static final int GL_POST_COLOR_MATRIX_BLUE_SCALE = 0x80B6;
    public static final int GL_POST_COLOR_MATRIX_ALPHA_SCALE = 0x80B7;
    public static final int GL_POST_COLOR_MATRIX_RED_BIAS = 0x80B8;
    public static final int GL_POST_COLOR_MATRIX_GREEN_BIAS = 0x80B9;
    public static final int GL_POST_COLOR_MATRIX_BLUE_BIAS = 0x80BA;
    public static final int GL_POST_COLOR_MATRIX_ALPHA_BIAS = 0x80BB;
    public static final int GL_BLEND_DST_RGB = 0x80C8;
    public static final int GL_BLEND_SRC_RGB = 0x80C9;
    public static final int GL_BLEND_DST_ALPHA = 0x80CA;
    public static final int GL_BLEND_SRC_ALPHA = 0x80CB;
    public static final int GL_COLOR_TABLE = 0x80D0;
    public static final int GL_POST_CONVOLUTION_COLOR_TABLE = 0x80D1;
    public static final int GL_POST_COLOR_MATRIX_COLOR_TABLE = 0x80D2;
    public static final int GL_PROXY_COLOR_TABLE = 0x80D3;
    public static final int GL_PROXY_POST_CONVOLUTION_COLOR_TABLE = 0x80D4;
    public static final int GL_PROXY_POST_COLOR_MATRIX_COLOR_TABLE = 0x80D5;
    public static final int GL_COLOR_TABLE_SCALE = 0x80D6;
    public static final int GL_COLOR_TABLE_BIAS = 0x80D7;
    public static final int GL_COLOR_TABLE_FORMAT = 0x80D8;
    public static final int GL_COLOR_TABLE_WIDTH = 0x80D9;
    public static final int GL_COLOR_TABLE_RED_SIZE = 0x80DA;
    public static final int GL_COLOR_TABLE_GREEN_SIZE = 0x80DB;
    public static final int GL_COLOR_TABLE_BLUE_SIZE = 0x80DC;
    public static final int GL_COLOR_TABLE_ALPHA_SIZE = 0x80DD;
    public static final int GL_COLOR_TABLE_LUMINANCE_SIZE = 0x80DE;
    public static final int GL_COLOR_TABLE_INTENSITY_SIZE = 0x80DF;
    public static final int GL_BGR = 0x80E0;
    public static final int GL_BGRA = 0x80E1;
    public static final int GL_MAX_ELEMENTS_VERTICES = 0x80E8;
    public static final int GL_MAX_ELEMENTS_INDICES = 0x80E9;
    public static final int GL_PHONG_WIN = 0x80EA;
    public static final int GL_PHONG_HINT_WIN = 0x80EB;
    public static final int GL_FOG_SPECULAR_TEXTURE_WIN = 0x80EC;
    public static final int GL_POINT_SIZE_MIN = 0x8126;
    public static final int GL_POINT_SIZE_MAX = 0x8127;
    public static final int GL_POINT_FADE_THRESHOLD_SIZE = 0x8128;
    public static final int GL_POINT_DISTANCE_ATTENUATION = 0x8129;
    public static final int GL_CLAMP_TO_BORDER = 0x812D;
    public static final int GL_CLAMP_TO_EDGE = 0x812F;
    public static final int GL_TEXTURE_MIN_LOD = 0x813A;
    public static final int GL_TEXTURE_MAX_LOD = 0x813B;
    public static final int GL_TEXTURE_BASE_LEVEL = 0x813C;
    public static final int GL_TEXTURE_MAX_LEVEL = 0x813D;
    public static final int GL_CONSTANT_BORDER = 0x8151;
    public static final int GL_REPLICATE_BORDER = 0x8153;
    public static final int GL_CONVOLUTION_BORDER_COLOR = 0x8154;
    public static final int GL_GENERATE_MIPMAP = 0x8191;
    public static final int GL_GENERATE_MIPMAP_HINT = 0x8192;
    public static final int GL_DEPTH_COMPONENT16 = 0x81A5;
    public static final int GL_DEPTH_COMPONENT24 = 0x81A6;
    public static final int GL_DEPTH_COMPONENT32 = 0x81A7;
    public static final int GL_UNPACK_CONSTANT_DATA_SUNX = 0x81D5;
    public static final int GL_TEXTURE_CONSTANT_DATA_SUNX = 0x81D6;
    public static final int GL_LIGHT_MODEL_COLOR_CONTROL = 0x81F8;
    public static final int GL_SINGLE_COLOR = 0x81F9;
    public static final int GL_SEPARATE_SPECULAR_COLOR = 0x81FA;
    public static final int GL_FRAMEBUFFER_ATTACHMENT_COLOR_ENCODING = 0x8210;
    public static final int GL_FRAMEBUFFER_ATTACHMENT_COMPONENT_TYPE = 0x8211;
    public static final int GL_FRAMEBUFFER_ATTACHMENT_RED_SIZE = 0x8212;
    public static final int GL_FRAMEBUFFER_ATTACHMENT_GREEN_SIZE = 0x8213;
    public static final int GL_FRAMEBUFFER_ATTACHMENT_BLUE_SIZE = 0x8214;
    public static final int GL_FRAMEBUFFER_ATTACHMENT_ALPHA_SIZE = 0x8215;
    public static final int GL_FRAMEBUFFER_ATTACHMENT_DEPTH_SIZE = 0x8216;
    public static final int GL_FRAMEBUFFER_ATTACHMENT_STENCIL_SIZE = 0x8217;
    public static final int GL_FRAMEBUFFER_DEFAULT = 0x8218;
    public static final int GL_FRAMEBUFFER_UNDEFINED = 0x8219;
    public static final int GL_DEPTH_STENCIL_ATTACHMENT = 0x821A;
    public static final int GL_MAJOR_VERSION = 0x821B;
    public static final int GL_MINOR_VERSION = 0x821C;
    public static final int GL_NUM_EXTENSIONS = 0x821D;
    public static final int GL_CONTEXT_FLAGS = 0x821E;
    public static final int GL_BUFFER_IMMUTABLE_STORAGE = 0x821F;
    public static final int GL_BUFFER_STORAGE_FLAGS = 0x8220;
    public static final int GL_PRIMITIVE_RESTART_FOR_PATCHES_SUPPORTED = 0x8221;
    public static final int GL_INDEX = 0x8222;
    public static final int GL_COMPRESSED_RED = 0x8225;
    public static final int GL_COMPRESSED_RG = 0x8226;
    public static final int GL_RG = 0x8227;
    public static final int GL_RG_INTEGER = 0x8228;
    public static final int GL_R8 = 0x8229;
    public static final int GL_R16 = 0x822A;
    public static final int GL_RG8 = 0x822B;
    public static final int GL_RG16 = 0x822C;
    public static final int GL_R16F = 0x822D;
    public static final int GL_R32F = 0x822E;
    public static final int GL_RG16F = 0x822F;
    public static final int GL_RG32F = 0x8230;
    public static final int GL_R8I = 0x8231;
    public static final int GL_R8UI = 0x8232;
    public static final int GL_R16I = 0x8233;
    public static final int GL_R16UI = 0x8234;
    public static final int GL_R32I = 0x8235;
    public static final int GL_R32UI = 0x8236;
    public static final int GL_RG8I = 0x8237;
    public static final int GL_RG8UI = 0x8238;
    public static final int GL_RG16I = 0x8239;
    public static final int GL_RG16UI = 0x823A;
    public static final int GL_RG32I = 0x823B;
    public static final int GL_RG32UI = 0x823C;
    public static final int GL_DEBUG_OUTPUT_SYNCHRONOUS = 0x8242;
    public static final int GL_DEBUG_NEXT_LOGGED_MESSAGE_LENGTH = 0x8243;
    public static final int GL_DEBUG_CALLBACK_FUNCTION = 0x8244;
    public static final int GL_DEBUG_CALLBACK_USER_PARAM = 0x8245;
    public static final int GL_DEBUG_SOURCE_API = 0x8246;
    public static final int GL_DEBUG_SOURCE_WINDOW_SYSTEM = 0x8247;
    public static final int GL_DEBUG_SOURCE_SHADER_COMPILER = 0x8248;
    public static final int GL_DEBUG_SOURCE_THIRD_PARTY = 0x8249;
    public static final int GL_DEBUG_SOURCE_APPLICATION = 0x824A;
    public static final int GL_DEBUG_SOURCE_OTHER = 0x824B;
    public static final int GL_DEBUG_TYPE_ERROR = 0x824C;
    public static final int GL_DEBUG_TYPE_DEPRECATED_BEHAVIOR = 0x824D;
    public static final int GL_DEBUG_TYPE_UNDEFINED_BEHAVIOR = 0x824E;
    public static final int GL_DEBUG_TYPE_PORTABILITY = 0x824F;
    public static final int GL_DEBUG_TYPE_PERFORMANCE = 0x8250;
    public static final int GL_DEBUG_TYPE_OTHER = 0x8251;
    public static final int GL_LOSE_CONTEXT_ON_RESET = 0x8252;
    public static final int GL_GUILTY_CONTEXT_RESET = 0x8253;
    public static final int GL_INNOCENT_CONTEXT_RESET = 0x8254;
    public static final int GL_UNKNOWN_CONTEXT_RESET = 0x8255;
    public static final int GL_RESET_NOTIFICATION_STRATEGY = 0x8256;
    public static final int GL_PROGRAM_BINARY_RETRIEVABLE_HINT = 0x8257;
    public static final int GL_PROGRAM_SEPARABLE = 0x8258;
    public static final int GL_ACTIVE_PROGRAM = 0x8259;
    public static final int GL_PROGRAM_PIPELINE_BINDING = 0x825A;
    public static final int GL_MAX_VIEWPORTS = 0x825B;
    public static final int GL_VIEWPORT_SUBPIXEL_BITS = 0x825C;
    public static final int GL_VIEWPORT_BOUNDS_RANGE = 0x825D;
    public static final int GL_LAYER_PROVOKING_VERTEX = 0x825E;
    public static final int GL_VIEWPORT_INDEX_PROVOKING_VERTEX = 0x825F;
    public static final int GL_UNDEFINED_VERTEX = 0x8260;
    public static final int GL_NO_RESET_NOTIFICATION = 0x8261;
    public static final int GL_MAX_COMPUTE_SHARED_MEMORY_SIZE = 0x8262;
    public static final int GL_MAX_COMPUTE_UNIFORM_COMPONENTS = 0x8263;
    public static final int GL_MAX_COMPUTE_ATOMIC_COUNTER_BUFFERS = 0x8264;
    public static final int GL_MAX_COMPUTE_ATOMIC_COUNTERS = 0x8265;
    public static final int GL_MAX_COMBINED_COMPUTE_UNIFORM_COMPONENTS = 0x8266;
    public static final int GL_COMPUTE_WORK_GROUP_SIZE = 0x8267;
    public static final int GL_DEBUG_TYPE_MARKER = 0x8268;
    public static final int GL_DEBUG_TYPE_PUSH_GROUP = 0x8269;
    public static final int GL_DEBUG_TYPE_POP_GROUP = 0x826A;
    public static final int GL_DEBUG_SEVERITY_NOTIFICATION = 0x826B;
    public static final int GL_MAX_DEBUG_GROUP_STACK_DEPTH = 0x826C;
    public static final int GL_DEBUG_GROUP_STACK_DEPTH = 0x826D;
    public static final int GL_MAX_UNIFORM_LOCATIONS = 0x826E;
    public static final int GL_INTERNALFORMAT_SUPPORTED = 0x826F;
    public static final int GL_INTERNALFORMAT_PREFERRED = 0x8270;
    public static final int GL_INTERNALFORMAT_RED_SIZE = 0x8271;
    public static final int GL_INTERNALFORMAT_GREEN_SIZE = 0x8272;
    public static final int GL_INTERNALFORMAT_BLUE_SIZE = 0x8273;
    public static final int GL_INTERNALFORMAT_ALPHA_SIZE = 0x8274;
    public static final int GL_INTERNALFORMAT_DEPTH_SIZE = 0x8275;
    public static final int GL_INTERNALFORMAT_STENCIL_SIZE = 0x8276;
    public static final int GL_INTERNALFORMAT_SHARED_SIZE = 0x8277;
    public static final int GL_INTERNALFORMAT_RED_TYPE = 0x8278;
    public static final int GL_INTERNALFORMAT_GREEN_TYPE = 0x8279;
    public static final int GL_INTERNALFORMAT_BLUE_TYPE = 0x827A;
    public static final int GL_INTERNALFORMAT_ALPHA_TYPE = 0x827B;
    public static final int GL_INTERNALFORMAT_DEPTH_TYPE = 0x827C;
    public static final int GL_INTERNALFORMAT_STENCIL_TYPE = 0x827D;
    public static final int GL_MAX_WIDTH = 0x827E;
    public static final int GL_MAX_HEIGHT = 0x827F;
    public static final int GL_MAX_DEPTH = 0x8280;
    public static final int GL_MAX_LAYERS = 0x8281;
    public static final int GL_MAX_COMBINED_DIMENSIONS = 0x8282;
    public static final int GL_COLOR_COMPONENTS = 0x8283;
    public static final int GL_DEPTH_COMPONENTS = 0x8284;
    public static final int GL_STENCIL_COMPONENTS = 0x8285;
    public static final int GL_COLOR_RENDERABLE = 0x8286;
    public static final int GL_DEPTH_RENDERABLE = 0x8287;
    public static final int GL_STENCIL_RENDERABLE = 0x8288;
    public static final int GL_FRAMEBUFFER_RENDERABLE = 0x8289;
    public static final int GL_FRAMEBUFFER_RENDERABLE_LAYERED = 0x828A;
    public static final int GL_FRAMEBUFFER_BLEND = 0x828B;
    public static final int GL_READ_PIXELS = 0x828C;
    public static final int GL_READ_PIXELS_FORMAT = 0x828D;
    public static final int GL_READ_PIXELS_TYPE = 0x828E;
    public static final int GL_TEXTURE_IMAGE_FORMAT = 0x828F;
    public static final int GL_TEXTURE_IMAGE_TYPE = 0x8290;
    public static final int GL_GET_TEXTURE_IMAGE_FORMAT = 0x8291;
    public static final int GL_GET_TEXTURE_IMAGE_TYPE = 0x8292;
    public static final int GL_MIPMAP = 0x8293;
    public static final int GL_MANUAL_GENERATE_MIPMAP = 0x8294;
    public static final int GL_AUTO_GENERATE_MIPMAP = 0x8295;
    public static final int GL_COLOR_ENCODING = 0x8296;
    public static final int GL_SRGB_READ = 0x8297;
    public static final int GL_SRGB_WRITE = 0x8298;
    public static final int GL_FILTER = 0x829A;
    public static final int GL_VERTEX_TEXTURE = 0x829B;
    public static final int GL_TESS_CONTROL_TEXTURE = 0x829C;
    public static final int GL_TESS_EVALUATION_TEXTURE = 0x829D;
    public static final int GL_GEOMETRY_TEXTURE = 0x829E;
    public static final int GL_FRAGMENT_TEXTURE = 0x829F;
    public static final int GL_COMPUTE_TEXTURE = 0x82A0;
    public static final int GL_TEXTURE_SHADOW = 0x82A1;
    public static final int GL_TEXTURE_GATHER = 0x82A2;
    public static final int GL_TEXTURE_GATHER_SHADOW = 0x82A3;
    public static final int GL_SHADER_IMAGE_LOAD = 0x82A4;
    public static final int GL_SHADER_IMAGE_STORE = 0x82A5;
    public static final int GL_SHADER_IMAGE_ATOMIC = 0x82A6;
    public static final int GL_IMAGE_TEXEL_SIZE = 0x82A7;
    public static final int GL_IMAGE_COMPATIBILITY_CLASS = 0x82A8;
    public static final int GL_IMAGE_PIXEL_FORMAT = 0x82A9;
    public static final int GL_IMAGE_PIXEL_TYPE = 0x82AA;
    public static final int GL_SIMULTANEOUS_TEXTURE_AND_DEPTH_TEST = 0x82AC;
    public static final int GL_SIMULTANEOUS_TEXTURE_AND_STENCIL_TEST = 0x82AD;
    public static final int GL_SIMULTANEOUS_TEXTURE_AND_DEPTH_WRITE = 0x82AE;
    public static final int GL_SIMULTANEOUS_TEXTURE_AND_STENCIL_WRITE = 0x82AF;
    public static final int GL_TEXTURE_COMPRESSED_BLOCK_WIDTH = 0x82B1;
    public static final int GL_TEXTURE_COMPRESSED_BLOCK_HEIGHT = 0x82B2;
    public static final int GL_TEXTURE_COMPRESSED_BLOCK_SIZE = 0x82B3;
    public static final int GL_CLEAR_BUFFER = 0x82B4;
    public static final int GL_TEXTURE_VIEW = 0x82B5;
    public static final int GL_VIEW_COMPATIBILITY_CLASS = 0x82B6;
    public static final int GL_FULL_SUPPORT = 0x82B7;
    public static final int GL_CAVEAT_SUPPORT = 0x82B8;
    public static final int GL_IMAGE_CLASS_4_X_32 = 0x82B9;
    public static final int GL_IMAGE_CLASS_2_X_32 = 0x82BA;
    public static final int GL_IMAGE_CLASS_1_X_32 = 0x82BB;
    public static final int GL_IMAGE_CLASS_4_X_16 = 0x82BC;
    public static final int GL_IMAGE_CLASS_2_X_16 = 0x82BD;
    public static final int GL_IMAGE_CLASS_1_X_16 = 0x82BE;
    public static final int GL_IMAGE_CLASS_4_X_8 = 0x82BF;
    public static final int GL_IMAGE_CLASS_2_X_8 = 0x82C0;
    public static final int GL_IMAGE_CLASS_1_X_8 = 0x82C1;
    public static final int GL_IMAGE_CLASS_11_11_10 = 0x82C2;
    public static final int GL_IMAGE_CLASS_10_10_10_2 = 0x82C3;
    public static final int GL_VIEW_CLASS_128_BITS = 0x82C4;
    public static final int GL_VIEW_CLASS_96_BITS = 0x82C5;
    public static final int GL_VIEW_CLASS_64_BITS = 0x82C6;
    public static final int GL_VIEW_CLASS_48_BITS = 0x82C7;
    public static final int GL_VIEW_CLASS_32_BITS = 0x82C8;
    public static final int GL_VIEW_CLASS_24_BITS = 0x82C9;
    public static final int GL_VIEW_CLASS_16_BITS = 0x82CA;
    public static final int GL_VIEW_CLASS_8_BITS = 0x82CB;
    public static final int GL_VIEW_CLASS_S3TC_DXT1_RGB = 0x82CC;
    public static final int GL_VIEW_CLASS_S3TC_DXT1_RGBA = 0x82CD;
    public static final int GL_VIEW_CLASS_S3TC_DXT3_RGBA = 0x82CE;
    public static final int GL_VIEW_CLASS_S3TC_DXT5_RGBA = 0x82CF;
    public static final int GL_VIEW_CLASS_RGTC1_RED = 0x82D0;
    public static final int GL_VIEW_CLASS_RGTC2_RG = 0x82D1;
    public static final int GL_VIEW_CLASS_BPTC_UNORM = 0x82D2;
    public static final int GL_VIEW_CLASS_BPTC_FLOAT = 0x82D3;
    public static final int GL_VERTEX_ATTRIB_BINDING = 0x82D4;
    public static final int GL_VERTEX_ATTRIB_RELATIVE_OFFSET = 0x82D5;
    public static final int GL_VERTEX_BINDING_DIVISOR = 0x82D6;
    public static final int GL_VERTEX_BINDING_OFFSET = 0x82D7;
    public static final int GL_VERTEX_BINDING_STRIDE = 0x82D8;
    public static final int GL_MAX_VERTEX_ATTRIB_RELATIVE_OFFSET = 0x82D9;
    public static final int GL_MAX_VERTEX_ATTRIB_BINDINGS = 0x82DA;
    public static final int GL_TEXTURE_VIEW_MIN_LEVEL = 0x82DB;
    public static final int GL_TEXTURE_VIEW_NUM_LEVELS = 0x82DC;
    public static final int GL_TEXTURE_VIEW_MIN_LAYER = 0x82DD;
    public static final int GL_TEXTURE_VIEW_NUM_LAYERS = 0x82DE;
    public static final int GL_TEXTURE_IMMUTABLE_LEVELS = 0x82DF;
    public static final int GL_BUFFER = 0x82E0;
    public static final int GL_SHADER = 0x82E1;
    public static final int GL_PROGRAM = 0x82E2;
    public static final int GL_QUERY = 0x82E3;
    public static final int GL_PROGRAM_PIPELINE = 0x82E4;
    public static final int GL_MAX_VERTEX_ATTRIB_STRIDE = 0x82E5;
    public static final int GL_SAMPLER = 0x82E6;
    public static final int GL_DISPLAY_LIST = 0x82E7;
    public static final int GL_MAX_LABEL_LENGTH = 0x82E8;
    public static final int GL_NUM_SHADING_LANGUAGE_VERSIONS = 0x82E9;
    public static final int GL_QUERY_TARGET = 0x82EA;
    public static final int GL_MAX_CULL_DISTANCES = 0x82F9;
    public static final int GL_MAX_COMBINED_CLIP_AND_CULL_DISTANCES = 0x82FA;
    public static final int GL_CONTEXT_RELEASE_BEHAVIOR = 0x82FB;
    public static final int GL_CONTEXT_RELEASE_BEHAVIOR_FLUSH = 0x82FC;
    public static final int GL_UNSIGNED_BYTE_2_3_3_REV = 0x8362;
    public static final int GL_UNSIGNED_SHORT_5_6_5 = 0x8363;
    public static final int GL_UNSIGNED_SHORT_5_6_5_REV = 0x8364;
    public static final int GL_UNSIGNED_SHORT_4_4_4_4_REV = 0x8365;
    public static final int GL_UNSIGNED_SHORT_1_5_5_5_REV = 0x8366;
    public static final int GL_UNSIGNED_INT_8_8_8_8_REV = 0x8367;
    public static final int GL_UNSIGNED_INT_2_10_10_10_REV = 0x8368;
    public static final int GL_MIRRORED_REPEAT = 0x8370;
    public static final int GL_RGB_S3TC = 0x83A0;
    public static final int GL_RGB4_S3TC = 0x83A1;
    public static final int GL_RGBA_S3TC = 0x83A2;
    public static final int GL_RGBA4_S3TC = 0x83A3;
    public static final int GL_RGBA_DXT5_S3TC = 0x83A4;
    public static final int GL_RGBA4_DXT5_S3TC = 0x83A5;
    public static final int GL_FOG_COORDINATE_SOURCE = 0x8450;
    public static final int GL_FOG_COORD_SRC = 0x8450;
    public static final int GL_FOG_COORDINATE = 0x8451;
    public static final int GL_FOG_COORD = 0x8451;
    public static final int GL_FRAGMENT_DEPTH = 0x8452;
    public static final int GL_CURRENT_FOG_COORDINATE = 0x8453;
    public static final int GL_CURRENT_FOG_COORD = 0x8453;
    public static final int GL_FOG_COORDINATE_ARRAY_TYPE = 0x8454;
    public static final int GL_FOG_COORD_ARRAY_TYPE = 0x8454;
    public static final int GL_FOG_COORDINATE_ARRAY_STRIDE = 0x8455;
    public static final int GL_FOG_COORD_ARRAY_STRIDE = 0x8455;
    public static final int GL_FOG_COORDINATE_ARRAY_POINTER = 0x8456;
    public static final int GL_FOG_COORD_ARRAY_POINTER = 0x8456;
    public static final int GL_FOG_COORDINATE_ARRAY = 0x8457;
    public static final int GL_FOG_COORD_ARRAY = 0x8457;
    public static final int GL_COLOR_SUM = 0x8458;
    public static final int GL_CURRENT_SECONDARY_COLOR = 0x8459;
    public static final int GL_SECONDARY_COLOR_ARRAY_SIZE = 0x845A;
    public static final int GL_SECONDARY_COLOR_ARRAY_TYPE = 0x845B;
    public static final int GL_SECONDARY_COLOR_ARRAY_STRIDE = 0x845C;
    public static final int GL_SECONDARY_COLOR_ARRAY_POINTER = 0x845D;
    public static final int GL_SECONDARY_COLOR_ARRAY = 0x845E;
    public static final int GL_CURRENT_RASTER_SECONDARY_COLOR = 0x845F;
    public static final int GL_ALIASED_POINT_SIZE_RANGE = 0x846D;
    public static final int GL_ALIASED_LINE_WIDTH_RANGE = 0x846E;
    public static final int GL_SCREEN_COORDINATES_REND = 0x8490;
    public static final int GL_INVERTED_SCREEN_W_REND = 0x8491;
    public static final int GL_TEXTURE0 = 0x84C0;
    public static final int GL_TEXTURE1 = 0x84C1;
    public static final int GL_TEXTURE2 = 0x84C2;
    public static final int GL_TEXTURE3 = 0x84C3;
    public static final int GL_TEXTURE4 = 0x84C4;
    public static final int GL_TEXTURE5 = 0x84C5;
    public static final int GL_TEXTURE6 = 0x84C6;
    public static final int GL_TEXTURE7 = 0x84C7;
    public static final int GL_TEXTURE8 = 0x84C8;
    public static final int GL_TEXTURE9 = 0x84C9;
    public static final int GL_TEXTURE10 = 0x84CA;
    public static final int GL_TEXTURE11 = 0x84CB;
    public static final int GL_TEXTURE12 = 0x84CC;
    public static final int GL_TEXTURE13 = 0x84CD;
    public static final int GL_TEXTURE14 = 0x84CE;
    public static final int GL_TEXTURE15 = 0x84CF;
    public static final int GL_TEXTURE16 = 0x84D0;
    public static final int GL_TEXTURE17 = 0x84D1;
    public static final int GL_TEXTURE18 = 0x84D2;
    public static final int GL_TEXTURE19 = 0x84D3;
    public static final int GL_TEXTURE20 = 0x84D4;
    public static final int GL_TEXTURE21 = 0x84D5;
    public static final int GL_TEXTURE22 = 0x84D6;
    public static final int GL_TEXTURE23 = 0x84D7;
    public static final int GL_TEXTURE24 = 0x84D8;
    public static final int GL_TEXTURE25 = 0x84D9;
    public static final int GL_TEXTURE26 = 0x84DA;
    public static final int GL_TEXTURE27 = 0x84DB;
    public static final int GL_TEXTURE28 = 0x84DC;
    public static final int GL_TEXTURE29 = 0x84DD;
    public static final int GL_TEXTURE30 = 0x84DE;
    public static final int GL_TEXTURE31 = 0x84DF;
    public static final int GL_ACTIVE_TEXTURE = 0x84E0;
    public static final int GL_CLIENT_ACTIVE_TEXTURE = 0x84E1;
    public static final int GL_MAX_TEXTURE_UNITS = 0x84E2;
    public static final int GL_TRANSPOSE_MODELVIEW_MATRIX = 0x84E3;
    public static final int GL_TRANSPOSE_PROJECTION_MATRIX = 0x84E4;
    public static final int GL_TRANSPOSE_TEXTURE_MATRIX = 0x84E5;
    public static final int GL_TRANSPOSE_COLOR_MATRIX = 0x84E6;
    public static final int GL_SUBTRACT = 0x84E7;
    public static final int GL_MAX_RENDERBUFFER_SIZE = 0x84E8;
    public static final int GL_COMPRESSED_ALPHA = 0x84E9;
    public static final int GL_COMPRESSED_LUMINANCE = 0x84EA;
    public static final int GL_COMPRESSED_LUMINANCE_ALPHA = 0x84EB;
    public static final int GL_COMPRESSED_INTENSITY = 0x84EC;
    public static final int GL_COMPRESSED_RGB = 0x84ED;
    public static final int GL_COMPRESSED_RGBA = 0x84EE;
    public static final int GL_TEXTURE_COMPRESSION_HINT = 0x84EF;
    public static final int GL_UNIFORM_BLOCK_REFERENCED_BY_TESS_CONTROL_SHADER = 0x84F0;
    public static final int GL_UNIFORM_BLOCK_REFERENCED_BY_TESS_EVALUATION_SHADER = 0x84F1;
    public static final int GL_TEXTURE_RECTANGLE = 0x84F5;
    public static final int GL_TEXTURE_BINDING_RECTANGLE = 0x84F6;
    public static final int GL_PROXY_TEXTURE_RECTANGLE = 0x84F7;
    public static final int GL_MAX_RECTANGLE_TEXTURE_SIZE = 0x84F8;
    public static final int GL_DEPTH_STENCIL = 0x84F9;
    public static final int GL_UNSIGNED_INT_24_8 = 0x84FA;
    public static final int GL_MAX_TEXTURE_LOD_BIAS = 0x84FD;
    public static final int GL_TEXTURE_FILTER_CONTROL = 0x8500;
    public static final int GL_TEXTURE_LOD_BIAS = 0x8501;
    public static final int GL_INCR_WRAP = 0x8507;
    public static final int GL_DECR_WRAP = 0x8508;
    public static final int GL_NORMAL_MAP = 0x8511;
    public static final int GL_REFLECTION_MAP = 0x8512;
    public static final int GL_TEXTURE_CUBE_MAP = 0x8513;
    public static final int GL_TEXTURE_BINDING_CUBE_MAP = 0x8514;
    public static final int GL_TEXTURE_CUBE_MAP_POSITIVE_X = 0x8515;
    public static final int GL_TEXTURE_CUBE_MAP_NEGATIVE_X = 0x8516;
    public static final int GL_TEXTURE_CUBE_MAP_POSITIVE_Y = 0x8517;
    public static final int GL_TEXTURE_CUBE_MAP_NEGATIVE_Y = 0x8518;
    public static final int GL_TEXTURE_CUBE_MAP_POSITIVE_Z = 0x8519;
    public static final int GL_TEXTURE_CUBE_MAP_NEGATIVE_Z = 0x851A;
    public static final int GL_PROXY_TEXTURE_CUBE_MAP = 0x851B;
    public static final int GL_MAX_CUBE_MAP_TEXTURE_SIZE = 0x851C;
    public static final int GL_RED_MIN_CLAMP_INGR = 0x8560;
    public static final int GL_GREEN_MIN_CLAMP_INGR = 0x8561;
    public static final int GL_BLUE_MIN_CLAMP_INGR = 0x8562;
    public static final int GL_ALPHA_MIN_CLAMP_INGR = 0x8563;
    public static final int GL_RED_MAX_CLAMP_INGR = 0x8564;
    public static final int GL_GREEN_MAX_CLAMP_INGR = 0x8565;
    public static final int GL_BLUE_MAX_CLAMP_INGR = 0x8566;
    public static final int GL_ALPHA_MAX_CLAMP_INGR = 0x8567;
    public static final int GL_INTERLACE_READ_INGR = 0x8568;
    public static final int GL_COMBINE = 0x8570;
    public static final int GL_COMBINE_RGB = 0x8571;
    public static final int GL_COMBINE_ALPHA = 0x8572;
    public static final int GL_RGB_SCALE = 0x8573;
    public static final int GL_ADD_SIGNED = 0x8574;
    public static final int GL_INTERPOLATE = 0x8575;
    public static final int GL_CONSTANT = 0x8576;
    public static final int GL_PRIMARY_COLOR = 0x8577;
    public static final int GL_PREVIOUS = 0x8578;
    public static final int GL_SOURCE0_RGB = 0x8580;
    public static final int GL_SRC0_RGB = 0x8580;
    public static final int GL_SOURCE1_RGB = 0x8581;
    public static final int GL_SRC1_RGB = 0x8581;
    public static final int GL_SOURCE2_RGB = 0x8582;
    public static final int GL_SRC2_RGB = 0x8582;
    public static final int GL_SOURCE0_ALPHA = 0x8588;
    public static final int GL_SRC0_ALPHA = 0x8588;
    public static final int GL_SOURCE1_ALPHA = 0x8589;
    public static final int GL_SRC1_ALPHA = 0x8589;
    public static final int GL_SOURCE2_ALPHA = 0x858A;
    public static final int GL_SRC2_ALPHA = 0x858A;
    public static final int GL_OPERAND0_RGB = 0x8590;
    public static final int GL_OPERAND1_RGB = 0x8591;
    public static final int GL_OPERAND2_RGB = 0x8592;
    public static final int GL_OPERAND0_ALPHA = 0x8598;
    public static final int GL_OPERAND1_ALPHA = 0x8599;
    public static final int GL_OPERAND2_ALPHA = 0x859A;
    public static final int GL_VERTEX_ARRAY_BINDING = 0x85B5;
    public static final int GL_VERTEX_ATTRIB_ARRAY_ENABLED = 0x8622;
    public static final int GL_VERTEX_ATTRIB_ARRAY_SIZE = 0x8623;
    public static final int GL_VERTEX_ATTRIB_ARRAY_STRIDE = 0x8624;
    public static final int GL_VERTEX_ATTRIB_ARRAY_TYPE = 0x8625;
    public static final int GL_CURRENT_VERTEX_ATTRIB = 0x8626;
    public static final int GL_VERTEX_PROGRAM_POINT_SIZE = 0x8642;
    public static final int GL_PROGRAM_POINT_SIZE = 0x8642;
    public static final int GL_VERTEX_PROGRAM_TWO_SIDE = 0x8643;
    public static final int GL_VERTEX_ATTRIB_ARRAY_POINTER = 0x8645;
    public static final int GL_DEPTH_CLAMP = 0x864F;
    public static final int GL_TEXTURE_COMPRESSED_IMAGE_SIZE = 0x86A0;
    public static final int GL_TEXTURE_COMPRESSED = 0x86A1;
    public static final int GL_NUM_COMPRESSED_TEXTURE_FORMATS = 0x86A2;
    public static final int GL_COMPRESSED_TEXTURE_FORMATS = 0x86A3;
    public static final int GL_DOT3_RGB = 0x86AE;
    public static final int GL_DOT3_RGBA = 0x86AF;
    public static final int GL_PROGRAM_BINARY_LENGTH = 0x8741;
    public static final int GL_MIRROR_CLAMP_TO_EDGE = 0x8743;
    public static final int GL_VERTEX_ATTRIB_ARRAY_LONG = 0x874E;
    public static final int GL_TEXTURE_1D_STACK_MESAX = 0x8759;
    public static final int GL_TEXTURE_2D_STACK_MESAX = 0x875A;
    public static final int GL_PROXY_TEXTURE_1D_STACK_MESAX = 0x875B;
    public static final int GL_PROXY_TEXTURE_2D_STACK_MESAX = 0x875C;
    public static final int GL_TEXTURE_1D_STACK_BINDING_MESAX = 0x875D;
    public static final int GL_TEXTURE_2D_STACK_BINDING_MESAX = 0x875E;
    public static final int GL_BUFFER_SIZE = 0x8764;
    public static final int GL_BUFFER_USAGE = 0x8765;
    public static final int GL_NUM_PROGRAM_BINARY_FORMATS = 0x87FE;
    public static final int GL_PROGRAM_BINARY_FORMATS = 0x87FF;
    public static final int GL_STENCIL_BACK_FUNC = 0x8800;
    public static final int GL_STENCIL_BACK_FAIL = 0x8801;
    public static final int GL_STENCIL_BACK_PASS_DEPTH_FAIL = 0x8802;
    public static final int GL_STENCIL_BACK_PASS_DEPTH_PASS = 0x8803;
    public static final int GL_RGBA32F = 0x8814;
    public static final int GL_RGB32F = 0x8815;
    public static final int GL_RGBA16F = 0x881A;
    public static final int GL_RGB16F = 0x881B;
    public static final int GL_MAX_DRAW_BUFFERS = 0x8824;
    public static final int GL_DRAW_BUFFER0 = 0x8825;
    public static final int GL_DRAW_BUFFER1 = 0x8826;
    public static final int GL_DRAW_BUFFER2 = 0x8827;
    public static final int GL_DRAW_BUFFER3 = 0x8828;
    public static final int GL_DRAW_BUFFER4 = 0x8829;
    public static final int GL_DRAW_BUFFER5 = 0x882A;
    public static final int GL_DRAW_BUFFER6 = 0x882B;
    public static final int GL_DRAW_BUFFER7 = 0x882C;
    public static final int GL_DRAW_BUFFER8 = 0x882D;
    public static final int GL_DRAW_BUFFER9 = 0x882E;
    public static final int GL_DRAW_BUFFER10 = 0x882F;
    public static final int GL_DRAW_BUFFER11 = 0x8830;
    public static final int GL_DRAW_BUFFER12 = 0x8831;
    public static final int GL_DRAW_BUFFER13 = 0x8832;
    public static final int GL_DRAW_BUFFER14 = 0x8833;
    public static final int GL_DRAW_BUFFER15 = 0x8834;
    public static final int GL_BLEND_EQUATION_ALPHA = 0x883D;
    public static final int GL_TEXTURE_DEPTH_SIZE = 0x884A;
    public static final int GL_DEPTH_TEXTURE_MODE = 0x884B;
    public static final int GL_TEXTURE_COMPARE_MODE = 0x884C;
    public static final int GL_TEXTURE_COMPARE_FUNC = 0x884D;
    public static final int GL_COMPARE_R_TO_TEXTURE = 0x884E;
    public static final int GL_COMPARE_REF_TO_TEXTURE = 0x884E;
    public static final int GL_TEXTURE_CUBE_MAP_SEAMLESS = 0x884F;
    public static final int GL_POINT_SPRITE = 0x8861;
    public static final int GL_COORD_REPLACE = 0x8862;
    public static final int GL_QUERY_COUNTER_BITS = 0x8864;
    public static final int GL_CURRENT_QUERY = 0x8865;
    public static final int GL_QUERY_RESULT = 0x8866;
    public static final int GL_QUERY_RESULT_AVAILABLE = 0x8867;
    public static final int GL_MAX_VERTEX_ATTRIBS = 0x8869;
    public static final int GL_VERTEX_ATTRIB_ARRAY_NORMALIZED = 0x886A;
    public static final int GL_MAX_TESS_CONTROL_INPUT_COMPONENTS = 0x886C;
    public static final int GL_MAX_TESS_EVALUATION_INPUT_COMPONENTS = 0x886D;
    public static final int GL_MAX_TEXTURE_COORDS = 0x8871;
    public static final int GL_MAX_TEXTURE_IMAGE_UNITS = 0x8872;
    public static final int GL_GEOMETRY_SHADER_INVOCATIONS = 0x887F;
    public static final int GL_ARRAY_BUFFER = 0x8892;
    public static final int GL_ELEMENT_ARRAY_BUFFER = 0x8893;
    public static final int GL_ARRAY_BUFFER_BINDING = 0x8894;
    public static final int GL_ELEMENT_ARRAY_BUFFER_BINDING = 0x8895;
    public static final int GL_VERTEX_ARRAY_BUFFER_BINDING = 0x8896;
    public static final int GL_NORMAL_ARRAY_BUFFER_BINDING = 0x8897;
    public static final int GL_COLOR_ARRAY_BUFFER_BINDING = 0x8898;
    public static final int GL_INDEX_ARRAY_BUFFER_BINDING = 0x8899;
    public static final int GL_TEXTURE_COORD_ARRAY_BUFFER_BINDING = 0x889A;
    public static final int GL_EDGE_FLAG_ARRAY_BUFFER_BINDING = 0x889B;
    public static final int GL_SECONDARY_COLOR_ARRAY_BUFFER_BINDING = 0x889C;
    public static final int GL_FOG_COORDINATE_ARRAY_BUFFER_BINDING = 0x889D;
    public static final int GL_FOG_COORD_ARRAY_BUFFER_BINDING = 0x889D;
    public static final int GL_WEIGHT_ARRAY_BUFFER_BINDING = 0x889E;
    public static final int GL_VERTEX_ATTRIB_ARRAY_BUFFER_BINDING = 0x889F;
    public static final int GL_READ_ONLY = 0x88B8;
    public static final int GL_WRITE_ONLY = 0x88B9;
    public static final int GL_READ_WRITE = 0x88BA;
    public static final int GL_BUFFER_ACCESS = 0x88BB;
    public static final int GL_BUFFER_MAPPED = 0x88BC;
    public static final int GL_BUFFER_MAP_POINTER = 0x88BD;
    public static final int GL_TIME_ELAPSED = 0x88BF;
    public static final int GL_STREAM_DRAW = 0x88E0;
    public static final int GL_STREAM_READ = 0x88E1;
    public static final int GL_STREAM_COPY = 0x88E2;
    public static final int GL_STATIC_DRAW = 0x88E4;
    public static final int GL_STATIC_READ = 0x88E5;
    public static final int GL_STATIC_COPY = 0x88E6;
    public static final int GL_DYNAMIC_DRAW = 0x88E8;
    public static final int GL_DYNAMIC_READ = 0x88E9;
    public static final int GL_DYNAMIC_COPY = 0x88EA;
    public static final int GL_PIXEL_PACK_BUFFER = 0x88EB;
    public static final int GL_PIXEL_UNPACK_BUFFER = 0x88EC;
    public static final int GL_PIXEL_PACK_BUFFER_BINDING = 0x88ED;
    public static final int GL_PIXEL_UNPACK_BUFFER_BINDING = 0x88EF;
    public static final int GL_DEPTH24_STENCIL8 = 0x88F0;
    public static final int GL_TEXTURE_STENCIL_SIZE = 0x88F1;
    public static final int GL_SRC1_COLOR = 0x88F9;
    public static final int GL_ONE_MINUS_SRC1_COLOR = 0x88FA;
    public static final int GL_ONE_MINUS_SRC1_ALPHA = 0x88FB;
    public static final int GL_MAX_DUAL_SOURCE_DRAW_BUFFERS = 0x88FC;
    public static final int GL_VERTEX_ATTRIB_ARRAY_INTEGER = 0x88FD;
    public static final int GL_VERTEX_ATTRIB_ARRAY_DIVISOR = 0x88FE;
    public static final int GL_MAX_ARRAY_TEXTURE_LAYERS = 0x88FF;
    public static final int GL_MIN_PROGRAM_TEXEL_OFFSET = 0x8904;
    public static final int GL_MAX_PROGRAM_TEXEL_OFFSET = 0x8905;
    public static final int GL_SAMPLES_PASSED = 0x8914;
    public static final int GL_GEOMETRY_VERTICES_OUT = 0x8916;
    public static final int GL_GEOMETRY_INPUT_TYPE = 0x8917;
    public static final int GL_GEOMETRY_OUTPUT_TYPE = 0x8918;
    public static final int GL_SAMPLER_BINDING = 0x8919;
    public static final int GL_CLAMP_VERTEX_COLOR = 0x891A;
    public static final int GL_CLAMP_FRAGMENT_COLOR = 0x891B;
    public static final int GL_CLAMP_READ_COLOR = 0x891C;
    public static final int GL_FIXED_ONLY = 0x891D;
    public static final int GL_INTERLACE_OML = 0x8980;
    public static final int GL_INTERLACE_READ_OML = 0x8981;
    public static final int GL_FORMAT_SUBSAMPLE_24_24_OML = 0x8982;
    public static final int GL_FORMAT_SUBSAMPLE_244_244_OML = 0x8983;
    public static final int GL_PACK_RESAMPLE_OML = 0x8984;
    public static final int GL_UNPACK_RESAMPLE_OML = 0x8985;
    public static final int GL_RESAMPLE_REPLICATE_OML = 0x8986;
    public static final int GL_RESAMPLE_ZERO_FILL_OML = 0x8987;
    public static final int GL_RESAMPLE_AVERAGE_OML = 0x8988;
    public static final int GL_RESAMPLE_DECIMATE_OML = 0x8989;
    public static final int GL_UNIFORM_BUFFER = 0x8A11;
    public static final int GL_UNIFORM_BUFFER_BINDING = 0x8A28;
    public static final int GL_UNIFORM_BUFFER_START = 0x8A29;
    public static final int GL_UNIFORM_BUFFER_SIZE = 0x8A2A;
    public static final int GL_MAX_VERTEX_UNIFORM_BLOCKS = 0x8A2B;
    public static final int GL_MAX_GEOMETRY_UNIFORM_BLOCKS = 0x8A2C;
    public static final int GL_MAX_FRAGMENT_UNIFORM_BLOCKS = 0x8A2D;
    public static final int GL_MAX_COMBINED_UNIFORM_BLOCKS = 0x8A2E;
    public static final int GL_MAX_UNIFORM_BUFFER_BINDINGS = 0x8A2F;
    public static final int GL_MAX_UNIFORM_BLOCK_SIZE = 0x8A30;
    public static final int GL_MAX_COMBINED_VERTEX_UNIFORM_COMPONENTS = 0x8A31;
    public static final int GL_MAX_COMBINED_GEOMETRY_UNIFORM_COMPONENTS = 0x8A32;
    public static final int GL_MAX_COMBINED_FRAGMENT_UNIFORM_COMPONENTS = 0x8A33;
    public static final int GL_UNIFORM_BUFFER_OFFSET_ALIGNMENT = 0x8A34;
    public static final int GL_ACTIVE_UNIFORM_BLOCK_MAX_NAME_LENGTH = 0x8A35;
    public static final int GL_ACTIVE_UNIFORM_BLOCKS = 0x8A36;
    public static final int GL_UNIFORM_TYPE = 0x8A37;
    public static final int GL_UNIFORM_SIZE = 0x8A38;
    public static final int GL_UNIFORM_NAME_LENGTH = 0x8A39;
    public static final int GL_UNIFORM_BLOCK_INDEX = 0x8A3A;
    public static final int GL_UNIFORM_OFFSET = 0x8A3B;
    public static final int GL_UNIFORM_ARRAY_STRIDE = 0x8A3C;
    public static final int GL_UNIFORM_MATRIX_STRIDE = 0x8A3D;
    public static final int GL_UNIFORM_IS_ROW_MAJOR = 0x8A3E;
    public static final int GL_UNIFORM_BLOCK_BINDING = 0x8A3F;
    public static final int GL_UNIFORM_BLOCK_DATA_SIZE = 0x8A40;
    public static final int GL_UNIFORM_BLOCK_NAME_LENGTH = 0x8A41;
    public static final int GL_UNIFORM_BLOCK_ACTIVE_UNIFORMS = 0x8A42;
    public static final int GL_UNIFORM_BLOCK_ACTIVE_UNIFORM_INDICES = 0x8A43;
    public static final int GL_UNIFORM_BLOCK_REFERENCED_BY_VERTEX_SHADER = 0x8A44;
    public static final int GL_UNIFORM_BLOCK_REFERENCED_BY_GEOMETRY_SHADER = 0x8A45;
    public static final int GL_UNIFORM_BLOCK_REFERENCED_BY_FRAGMENT_SHADER = 0x8A46;
    public static final int GL_FRAGMENT_SHADER = 0x8B30;
    public static final int GL_VERTEX_SHADER = 0x8B31;
    public static final int GL_MAX_FRAGMENT_UNIFORM_COMPONENTS = 0x8B49;
    public static final int GL_MAX_VERTEX_UNIFORM_COMPONENTS = 0x8B4A;
    public static final int GL_MAX_VARYING_FLOATS = 0x8B4B;
    public static final int GL_MAX_VARYING_COMPONENTS = 0x8B4B;
    public static final int GL_MAX_VERTEX_TEXTURE_IMAGE_UNITS = 0x8B4C;
    public static final int GL_MAX_COMBINED_TEXTURE_IMAGE_UNITS = 0x8B4D;
    public static final int GL_SHADER_TYPE = 0x8B4F;
    public static final int GL_FLOAT_VEC2 = 0x8B50;
    public static final int GL_FLOAT_VEC3 = 0x8B51;
    public static final int GL_FLOAT_VEC4 = 0x8B52;
    public static final int GL_INT_VEC2 = 0x8B53;
    public static final int GL_INT_VEC3 = 0x8B54;
    public static final int GL_INT_VEC4 = 0x8B55;
    public static final int GL_BOOL = 0x8B56;
    public static final int GL_BOOL_VEC2 = 0x8B57;
    public static final int GL_BOOL_VEC3 = 0x8B58;
    public static final int GL_BOOL_VEC4 = 0x8B59;
    public static final int GL_FLOAT_MAT2 = 0x8B5A;
    public static final int GL_FLOAT_MAT3 = 0x8B5B;
    public static final int GL_FLOAT_MAT4 = 0x8B5C;
    public static final int GL_SAMPLER_1D = 0x8B5D;
    public static final int GL_SAMPLER_2D = 0x8B5E;
    public static final int GL_SAMPLER_3D = 0x8B5F;
    public static final int GL_SAMPLER_CUBE = 0x8B60;
    public static final int GL_SAMPLER_1D_SHADOW = 0x8B61;
    public static final int GL_SAMPLER_2D_SHADOW = 0x8B62;
    public static final int GL_SAMPLER_2D_RECT = 0x8B63;
    public static final int GL_SAMPLER_2D_RECT_SHADOW = 0x8B64;
    public static final int GL_FLOAT_MAT2x3 = 0x8B65;
    public static final int GL_FLOAT_MAT2x4 = 0x8B66;
    public static final int GL_FLOAT_MAT3x2 = 0x8B67;
    public static final int GL_FLOAT_MAT3x4 = 0x8B68;
    public static final int GL_FLOAT_MAT4x2 = 0x8B69;
    public static final int GL_FLOAT_MAT4x3 = 0x8B6A;
    public static final int GL_DELETE_STATUS = 0x8B80;
    public static final int GL_COMPILE_STATUS = 0x8B81;
    public static final int GL_LINK_STATUS = 0x8B82;
    public static final int GL_VALIDATE_STATUS = 0x8B83;
    public static final int GL_INFO_LOG_LENGTH = 0x8B84;
    public static final int GL_ATTACHED_SHADERS = 0x8B85;
    public static final int GL_ACTIVE_UNIFORMS = 0x8B86;
    public static final int GL_ACTIVE_UNIFORM_MAX_LENGTH = 0x8B87;
    public static final int GL_SHADER_SOURCE_LENGTH = 0x8B88;
    public static final int GL_ACTIVE_ATTRIBUTES = 0x8B89;
    public static final int GL_ACTIVE_ATTRIBUTE_MAX_LENGTH = 0x8B8A;
    public static final int GL_FRAGMENT_SHADER_DERIVATIVE_HINT = 0x8B8B;
    public static final int GL_SHADING_LANGUAGE_VERSION = 0x8B8C;
    public static final int GL_CURRENT_PROGRAM = 0x8B8D;
    public static final int GL_IMPLEMENTATION_COLOR_READ_TYPE = 0x8B9A;
    public static final int GL_IMPLEMENTATION_COLOR_READ_FORMAT = 0x8B9B;
    public static final int GL_STATE_RESTORE = 0x8BDC;
    public static final int GL_TEXTURE_RED_TYPE = 0x8C10;
    public static final int GL_TEXTURE_GREEN_TYPE = 0x8C11;
    public static final int GL_TEXTURE_BLUE_TYPE = 0x8C12;
    public static final int GL_TEXTURE_ALPHA_TYPE = 0x8C13;
    public static final int GL_TEXTURE_LUMINANCE_TYPE = 0x8C14;
    public static final int GL_TEXTURE_INTENSITY_TYPE = 0x8C15;
    public static final int GL_TEXTURE_DEPTH_TYPE = 0x8C16;
    public static final int GL_UNSIGNED_NORMALIZED = 0x8C17;
    public static final int GL_TEXTURE_1D_ARRAY = 0x8C18;
    public static final int GL_PROXY_TEXTURE_1D_ARRAY = 0x8C19;
    public static final int GL_TEXTURE_2D_ARRAY = 0x8C1A;
    public static final int GL_PROXY_TEXTURE_2D_ARRAY = 0x8C1B;
    public static final int GL_TEXTURE_BINDING_1D_ARRAY = 0x8C1C;
    public static final int GL_TEXTURE_BINDING_2D_ARRAY = 0x8C1D;
    public static final int GL_MAX_GEOMETRY_TEXTURE_IMAGE_UNITS = 0x8C29;
    public static final int GL_TEXTURE_BUFFER = 0x8C2A;
    public static final int GL_TEXTURE_BUFFER_BINDING = 0x8C2A;
    public static final int GL_MAX_TEXTURE_BUFFER_SIZE = 0x8C2B;
    public static final int GL_TEXTURE_BINDING_BUFFER = 0x8C2C;
    public static final int GL_TEXTURE_BUFFER_DATA_STORE_BINDING = 0x8C2D;
    public static final int GL_ANY_SAMPLES_PASSED = 0x8C2F;
    public static final int GL_SAMPLE_SHADING = 0x8C36;
    public static final int GL_MIN_SAMPLE_SHADING_VALUE = 0x8C37;
    public static final int GL_R11F_G11F_B10F = 0x8C3A;
    public static final int GL_UNSIGNED_INT_10F_11F_11F_REV = 0x8C3B;
    public static final int GL_RGB9_E5 = 0x8C3D;
    public static final int GL_UNSIGNED_INT_5_9_9_9_REV = 0x8C3E;
    public static final int GL_TEXTURE_SHARED_SIZE = 0x8C3F;
    public static final int GL_SRGB = 0x8C40;
    public static final int GL_SRGB8 = 0x8C41;
    public static final int GL_SRGB_ALPHA = 0x8C42;
    public static final int GL_SRGB8_ALPHA8 = 0x8C43;
    public static final int GL_SLUMINANCE_ALPHA = 0x8C44;
    public static final int GL_SLUMINANCE8_ALPHA8 = 0x8C45;
    public static final int GL_SLUMINANCE = 0x8C46;
    public static final int GL_SLUMINANCE8 = 0x8C47;
    public static final int GL_COMPRESSED_SRGB = 0x8C48;
    public static final int GL_COMPRESSED_SRGB_ALPHA = 0x8C49;
    public static final int GL_COMPRESSED_SLUMINANCE = 0x8C4A;
    public static final int GL_COMPRESSED_SLUMINANCE_ALPHA = 0x8C4B;
    public static final int GL_TRANSFORM_FEEDBACK_VARYING_MAX_LENGTH = 0x8C76;
    public static final int GL_TRANSFORM_FEEDBACK_BUFFER_MODE = 0x8C7F;
    public static final int GL_MAX_TRANSFORM_FEEDBACK_SEPARATE_COMPONENTS = 0x8C80;
    public static final int GL_TRANSFORM_FEEDBACK_VARYINGS = 0x8C83;
    public static final int GL_TRANSFORM_FEEDBACK_BUFFER_START = 0x8C84;
    public static final int GL_TRANSFORM_FEEDBACK_BUFFER_SIZE = 0x8C85;
    public static final int GL_PRIMITIVES_GENERATED = 0x8C87;
    public static final int GL_TRANSFORM_FEEDBACK_PRIMITIVES_WRITTEN = 0x8C88;
    public static final int GL_RASTERIZER_DISCARD = 0x8C89;
    public static final int GL_MAX_TRANSFORM_FEEDBACK_INTERLEAVED_COMPONENTS = 0x8C8A;
    public static final int GL_MAX_TRANSFORM_FEEDBACK_SEPARATE_ATTRIBS = 0x8C8B;
    public static final int GL_INTERLEAVED_ATTRIBS = 0x8C8C;
    public static final int GL_SEPARATE_ATTRIBS = 0x8C8D;
    public static final int GL_TRANSFORM_FEEDBACK_BUFFER = 0x8C8E;
    public static final int GL_TRANSFORM_FEEDBACK_BUFFER_BINDING = 0x8C8F;
    public static final int GL_POINT_SPRITE_COORD_ORIGIN = 0x8CA0;
    public static final int GL_LOWER_LEFT = 0x8CA1;
    public static final int GL_UPPER_LEFT = 0x8CA2;
    public static final int GL_STENCIL_BACK_REF = 0x8CA3;
    public static final int GL_STENCIL_BACK_VALUE_MASK = 0x8CA4;
    public static final int GL_STENCIL_BACK_WRITEMASK = 0x8CA5;
    public static final int GL_DRAW_FRAMEBUFFER_BINDING = 0x8CA6;
    public static final int GL_FRAMEBUFFER_BINDING = 0x8CA6;
    public static final int GL_RENDERBUFFER_BINDING = 0x8CA7;
    public static final int GL_READ_FRAMEBUFFER = 0x8CA8;
    public static final int GL_DRAW_FRAMEBUFFER = 0x8CA9;
    public static final int GL_READ_FRAMEBUFFER_BINDING = 0x8CAA;
    public static final int GL_RENDERBUFFER_SAMPLES = 0x8CAB;
    public static final int GL_DEPTH_COMPONENT32F = 0x8CAC;
    public static final int GL_DEPTH32F_STENCIL8 = 0x8CAD;
    public static final int GL_FRAMEBUFFER_ATTACHMENT_OBJECT_TYPE = 0x8CD0;
    public static final int GL_FRAMEBUFFER_ATTACHMENT_OBJECT_NAME = 0x8CD1;
    public static final int GL_FRAMEBUFFER_ATTACHMENT_TEXTURE_LEVEL = 0x8CD2;
    public static final int GL_FRAMEBUFFER_ATTACHMENT_TEXTURE_CUBE_MAP_FACE = 0x8CD3;
    public static final int GL_FRAMEBUFFER_ATTACHMENT_TEXTURE_LAYER = 0x8CD4;
    public static final int GL_FRAMEBUFFER_COMPLETE = 0x8CD5;
    public static final int GL_FRAMEBUFFER_INCOMPLETE_ATTACHMENT = 0x8CD6;
    public static final int GL_FRAMEBUFFER_INCOMPLETE_MISSING_ATTACHMENT = 0x8CD7;
    public static final int GL_FRAMEBUFFER_INCOMPLETE_DIMENSIONS = 0x8CD9;
    public static final int GL_FRAMEBUFFER_INCOMPLETE_DRAW_BUFFER = 0x8CDB;
    public static final int GL_FRAMEBUFFER_INCOMPLETE_READ_BUFFER = 0x8CDC;
    public static final int GL_FRAMEBUFFER_UNSUPPORTED = 0x8CDD;
    public static final int GL_MAX_COLOR_ATTACHMENTS = 0x8CDF;
    public static final int GL_COLOR_ATTACHMENT0 = 0x8CE0;
    public static final int GL_COLOR_ATTACHMENT1 = 0x8CE1;
    public static final int GL_COLOR_ATTACHMENT2 = 0x8CE2;
    public static final int GL_COLOR_ATTACHMENT3 = 0x8CE3;
    public static final int GL_COLOR_ATTACHMENT4 = 0x8CE4;
    public static final int GL_COLOR_ATTACHMENT5 = 0x8CE5;
    public static final int GL_COLOR_ATTACHMENT6 = 0x8CE6;
    public static final int GL_COLOR_ATTACHMENT7 = 0x8CE7;
    public static final int GL_COLOR_ATTACHMENT8 = 0x8CE8;
    public static final int GL_COLOR_ATTACHMENT9 = 0x8CE9;
    public static final int GL_COLOR_ATTACHMENT10 = 0x8CEA;
    public static final int GL_COLOR_ATTACHMENT11 = 0x8CEB;
    public static final int GL_COLOR_ATTACHMENT12 = 0x8CEC;
    public static final int GL_COLOR_ATTACHMENT13 = 0x8CED;
    public static final int GL_COLOR_ATTACHMENT14 = 0x8CEE;
    public static final int GL_COLOR_ATTACHMENT15 = 0x8CEF;
    public static final int GL_COLOR_ATTACHMENT16 = 0x8CF0;
    public static final int GL_COLOR_ATTACHMENT17 = 0x8CF1;
    public static final int GL_COLOR_ATTACHMENT18 = 0x8CF2;
    public static final int GL_COLOR_ATTACHMENT19 = 0x8CF3;
    public static final int GL_COLOR_ATTACHMENT20 = 0x8CF4;
    public static final int GL_COLOR_ATTACHMENT21 = 0x8CF5;
    public static final int GL_COLOR_ATTACHMENT22 = 0x8CF6;
    public static final int GL_COLOR_ATTACHMENT23 = 0x8CF7;
    public static final int GL_COLOR_ATTACHMENT24 = 0x8CF8;
    public static final int GL_COLOR_ATTACHMENT25 = 0x8CF9;
    public static final int GL_COLOR_ATTACHMENT26 = 0x8CFA;
    public static final int GL_COLOR_ATTACHMENT27 = 0x8CFB;
    public static final int GL_COLOR_ATTACHMENT28 = 0x8CFC;
    public static final int GL_COLOR_ATTACHMENT29 = 0x8CFD;
    public static final int GL_COLOR_ATTACHMENT30 = 0x8CFE;
    public static final int GL_COLOR_ATTACHMENT31 = 0x8CFF;
    public static final int GL_DEPTH_ATTACHMENT = 0x8D00;
    public static final int GL_STENCIL_ATTACHMENT = 0x8D20;
    public static final int GL_FRAMEBUFFER = 0x8D40;
    public static final int GL_RENDERBUFFER = 0x8D41;
    public static final int GL_RENDERBUFFER_WIDTH = 0x8D42;
    public static final int GL_RENDERBUFFER_HEIGHT = 0x8D43;
    public static final int GL_RENDERBUFFER_INTERNAL_FORMAT = 0x8D44;
    public static final int GL_STENCIL_INDEX1 = 0x8D46;
    public static final int GL_STENCIL_INDEX4 = 0x8D47;
    public static final int GL_STENCIL_INDEX8 = 0x8D48;
    public static final int GL_STENCIL_INDEX16 = 0x8D49;
    public static final int GL_RENDERBUFFER_RED_SIZE = 0x8D50;
    public static final int GL_RENDERBUFFER_GREEN_SIZE = 0x8D51;
    public static final int GL_RENDERBUFFER_BLUE_SIZE = 0x8D52;
    public static final int GL_RENDERBUFFER_ALPHA_SIZE = 0x8D53;
    public static final int GL_RENDERBUFFER_DEPTH_SIZE = 0x8D54;
    public static final int GL_RENDERBUFFER_STENCIL_SIZE = 0x8D55;
    public static final int GL_FRAMEBUFFER_INCOMPLETE_MULTISAMPLE = 0x8D56;
    public static final int GL_MAX_SAMPLES = 0x8D57;
    public static final int GL_RGB565 = 0x8D62;
    public static final int GL_PRIMITIVE_RESTART_FIXED_INDEX = 0x8D69;
    public static final int GL_ANY_SAMPLES_PASSED_CONSERVATIVE = 0x8D6A;
    public static final int GL_MAX_ELEMENT_INDEX = 0x8D6B;
    public static final int GL_RGBA32UI = 0x8D70;
    public static final int GL_RGB32UI = 0x8D71;
    public static final int GL_RGBA16UI = 0x8D76;
    public static final int GL_RGB16UI = 0x8D77;
    public static final int GL_RGBA8UI = 0x8D7C;
    public static final int GL_RGB8UI = 0x8D7D;
    public static final int GL_RGBA32I = 0x8D82;
    public static final int GL_RGB32I = 0x8D83;
    public static final int GL_RGBA16I = 0x8D88;
    public static final int GL_RGB16I = 0x8D89;
    public static final int GL_RGBA8I = 0x8D8E;
    public static final int GL_RGB8I = 0x8D8F;
    public static final int GL_RED_INTEGER = 0x8D94;
    public static final int GL_GREEN_INTEGER = 0x8D95;
    public static final int GL_BLUE_INTEGER = 0x8D96;
    public static final int GL_ALPHA_INTEGER = 0x8D97;
    public static final int GL_RGB_INTEGER = 0x8D98;
    public static final int GL_RGBA_INTEGER = 0x8D99;
    public static final int GL_BGR_INTEGER = 0x8D9A;
    public static final int GL_BGRA_INTEGER = 0x8D9B;
    public static final int GL_INT_2_10_10_10_REV = 0x8D9F;
    public static final int GL_FRAMEBUFFER_ATTACHMENT_LAYERED = 0x8DA7;
    public static final int GL_FRAMEBUFFER_INCOMPLETE_LAYER_TARGETS = 0x8DA8;
    public static final int GL_FLOAT_32_UNSIGNED_INT_24_8_REV = 0x8DAD;
    public static final int GL_FRAMEBUFFER_SRGB = 0x8DB9;
    public static final int GL_COMPRESSED_RED_RGTC1 = 0x8DBB;
    public static final int GL_COMPRESSED_SIGNED_RED_RGTC1 = 0x8DBC;
    public static final int GL_COMPRESSED_RG_RGTC2 = 0x8DBD;
    public static final int GL_COMPRESSED_SIGNED_RG_RGTC2 = 0x8DBE;
    public static final int GL_SAMPLER_1D_ARRAY = 0x8DC0;
    public static final int GL_SAMPLER_2D_ARRAY = 0x8DC1;
    public static final int GL_SAMPLER_BUFFER = 0x8DC2;
    public static final int GL_SAMPLER_1D_ARRAY_SHADOW = 0x8DC3;
    public static final int GL_SAMPLER_2D_ARRAY_SHADOW = 0x8DC4;
    public static final int GL_SAMPLER_CUBE_SHADOW = 0x8DC5;
    public static final int GL_UNSIGNED_INT_VEC2 = 0x8DC6;
    public static final int GL_UNSIGNED_INT_VEC3 = 0x8DC7;
    public static final int GL_UNSIGNED_INT_VEC4 = 0x8DC8;
    public static final int GL_INT_SAMPLER_1D = 0x8DC9;
    public static final int GL_INT_SAMPLER_2D = 0x8DCA;
    public static final int GL_INT_SAMPLER_3D = 0x8DCB;
    public static final int GL_INT_SAMPLER_CUBE = 0x8DCC;
    public static final int GL_INT_SAMPLER_2D_RECT = 0x8DCD;
    public static final int GL_INT_SAMPLER_1D_ARRAY = 0x8DCE;
    public static final int GL_INT_SAMPLER_2D_ARRAY = 0x8DCF;
    public static final int GL_INT_SAMPLER_BUFFER = 0x8DD0;
    public static final int GL_UNSIGNED_INT_SAMPLER_1D = 0x8DD1;
    public static final int GL_UNSIGNED_INT_SAMPLER_2D = 0x8DD2;
    public static final int GL_UNSIGNED_INT_SAMPLER_3D = 0x8DD3;
    public static final int GL_UNSIGNED_INT_SAMPLER_CUBE = 0x8DD4;
    public static final int GL_UNSIGNED_INT_SAMPLER_2D_RECT = 0x8DD5;
    public static final int GL_UNSIGNED_INT_SAMPLER_1D_ARRAY = 0x8DD6;
    public static final int GL_UNSIGNED_INT_SAMPLER_2D_ARRAY = 0x8DD7;
    public static final int GL_UNSIGNED_INT_SAMPLER_BUFFER = 0x8DD8;
    public static final int GL_GEOMETRY_SHADER = 0x8DD9;
    public static final int GL_MAX_GEOMETRY_UNIFORM_COMPONENTS = 0x8DDF;
    public static final int GL_MAX_GEOMETRY_OUTPUT_VERTICES = 0x8DE0;
    public static final int GL_MAX_GEOMETRY_TOTAL_OUTPUT_COMPONENTS = 0x8DE1;
    public static final int GL_ACTIVE_SUBROUTINES = 0x8DE5;
    public static final int GL_ACTIVE_SUBROUTINE_UNIFORMS = 0x8DE6;
    public static final int GL_MAX_SUBROUTINES = 0x8DE7;
    public static final int GL_MAX_SUBROUTINE_UNIFORM_LOCATIONS = 0x8DE8;
    public static final int GL_LOW_FLOAT = 0x8DF0;
    public static final int GL_MEDIUM_FLOAT = 0x8DF1;
    public static final int GL_HIGH_FLOAT = 0x8DF2;
    public static final int GL_LOW_INT = 0x8DF3;
    public static final int GL_MEDIUM_INT = 0x8DF4;
    public static final int GL_HIGH_INT = 0x8DF5;
    public static final int GL_SHADER_BINARY_FORMATS = 0x8DF8;
    public static final int GL_NUM_SHADER_BINARY_FORMATS = 0x8DF9;
    public static final int GL_SHADER_COMPILER = 0x8DFA;
    public static final int GL_MAX_VERTEX_UNIFORM_VECTORS = 0x8DFB;
    public static final int GL_MAX_VARYING_VECTORS = 0x8DFC;
    public static final int GL_MAX_FRAGMENT_UNIFORM_VECTORS = 0x8DFD;
    public static final int GL_QUERY_WAIT = 0x8E13;
    public static final int GL_QUERY_NO_WAIT = 0x8E14;
    public static final int GL_QUERY_BY_REGION_WAIT = 0x8E15;
    public static final int GL_QUERY_BY_REGION_NO_WAIT = 0x8E16;
    public static final int GL_QUERY_WAIT_INVERTED = 0x8E17;
    public static final int GL_QUERY_NO_WAIT_INVERTED = 0x8E18;
    public static final int GL_QUERY_BY_REGION_WAIT_INVERTED = 0x8E19;
    public static final int GL_QUERY_BY_REGION_NO_WAIT_INVERTED = 0x8E1A;
    public static final int GL_MAX_COMBINED_TESS_CONTROL_UNIFORM_COMPONENTS = 0x8E1E;
    public static final int GL_MAX_COMBINED_TESS_EVALUATION_UNIFORM_COMPONENTS = 0x8E1F;
    public static final int GL_TRANSFORM_FEEDBACK = 0x8E22;
    public static final int GL_TRANSFORM_FEEDBACK_BUFFER_PAUSED = 0x8E23;
    public static final int GL_TRANSFORM_FEEDBACK_PAUSED = 0x8E23;
    public static final int GL_TRANSFORM_FEEDBACK_BUFFER_ACTIVE = 0x8E24;
    public static final int GL_TRANSFORM_FEEDBACK_ACTIVE = 0x8E24;
    public static final int GL_TRANSFORM_FEEDBACK_BINDING = 0x8E25;
    public static final int GL_TIMESTAMP = 0x8E28;
    public static final int GL_TEXTURE_SWIZZLE_R = 0x8E42;
    public static final int GL_TEXTURE_SWIZZLE_G = 0x8E43;
    public static final int GL_TEXTURE_SWIZZLE_B = 0x8E44;
    public static final int GL_TEXTURE_SWIZZLE_A = 0x8E45;
    public static final int GL_TEXTURE_SWIZZLE_RGBA = 0x8E46;
    public static final int GL_ACTIVE_SUBROUTINE_UNIFORM_LOCATIONS = 0x8E47;
    public static final int GL_ACTIVE_SUBROUTINE_MAX_LENGTH = 0x8E48;
    public static final int GL_ACTIVE_SUBROUTINE_UNIFORM_MAX_LENGTH = 0x8E49;
    public static final int GL_NUM_COMPATIBLE_SUBROUTINES = 0x8E4A;
    public static final int GL_COMPATIBLE_SUBROUTINES = 0x8E4B;
    public static final int GL_QUADS_FOLLOW_PROVOKING_VERTEX_CONVENTION = 0x8E4C;
    public static final int GL_FIRST_VERTEX_CONVENTION = 0x8E4D;
    public static final int GL_LAST_VERTEX_CONVENTION = 0x8E4E;
    public static final int GL_PROVOKING_VERTEX = 0x8E4F;
    public static final int GL_SAMPLE_POSITION = 0x8E50;
    public static final int GL_SAMPLE_MASK = 0x8E51;
    public static final int GL_SAMPLE_MASK_VALUE = 0x8E52;
    public static final int GL_MAX_SAMPLE_MASK_WORDS = 0x8E59;
    public static final int GL_MAX_GEOMETRY_SHADER_INVOCATIONS = 0x8E5A;
    public static final int GL_MIN_FRAGMENT_INTERPOLATION_OFFSET = 0x8E5B;
    public static final int GL_MAX_FRAGMENT_INTERPOLATION_OFFSET = 0x8E5C;
    public static final int GL_FRAGMENT_INTERPOLATION_OFFSET_BITS = 0x8E5D;
    public static final int GL_MIN_PROGRAM_TEXTURE_GATHER_OFFSET = 0x8E5E;
    public static final int GL_MAX_PROGRAM_TEXTURE_GATHER_OFFSET = 0x8E5F;
    public static final int GL_MAX_TRANSFORM_FEEDBACK_BUFFERS = 0x8E70;
    public static final int GL_MAX_VERTEX_STREAMS = 0x8E71;
    public static final int GL_PATCH_VERTICES = 0x8E72;
    public static final int GL_PATCH_DEFAULT_INNER_LEVEL = 0x8E73;
    public static final int GL_PATCH_DEFAULT_OUTER_LEVEL = 0x8E74;
    public static final int GL_TESS_CONTROL_OUTPUT_VERTICES = 0x8E75;
    public static final int GL_TESS_GEN_MODE = 0x8E76;
    public static final int GL_TESS_GEN_SPACING = 0x8E77;
    public static final int GL_TESS_GEN_VERTEX_ORDER = 0x8E78;
    public static final int GL_TESS_GEN_POINT_MODE = 0x8E79;
    public static final int GL_ISOLINES = 0x8E7A;
    public static final int GL_FRACTIONAL_ODD = 0x8E7B;
    public static final int GL_FRACTIONAL_EVEN = 0x8E7C;
    public static final int GL_MAX_PATCH_VERTICES = 0x8E7D;
    public static final int GL_MAX_TESS_GEN_LEVEL = 0x8E7E;
    public static final int GL_MAX_TESS_CONTROL_UNIFORM_COMPONENTS = 0x8E7F;
    public static final int GL_MAX_TESS_EVALUATION_UNIFORM_COMPONENTS = 0x8E80;
    public static final int GL_MAX_TESS_CONTROL_TEXTURE_IMAGE_UNITS = 0x8E81;
    public static final int GL_MAX_TESS_EVALUATION_TEXTURE_IMAGE_UNITS = 0x8E82;
    public static final int GL_MAX_TESS_CONTROL_OUTPUT_COMPONENTS = 0x8E83;
    public static final int GL_MAX_TESS_PATCH_COMPONENTS = 0x8E84;
    public static final int GL_MAX_TESS_CONTROL_TOTAL_OUTPUT_COMPONENTS = 0x8E85;
    public static final int GL_MAX_TESS_EVALUATION_OUTPUT_COMPONENTS = 0x8E86;
    public static final int GL_TESS_EVALUATION_SHADER = 0x8E87;
    public static final int GL_TESS_CONTROL_SHADER = 0x8E88;
    public static final int GL_MAX_TESS_CONTROL_UNIFORM_BLOCKS = 0x8E89;
    public static final int GL_MAX_TESS_EVALUATION_UNIFORM_BLOCKS = 0x8E8A;
    public static final int GL_COMPRESSED_RGBA_BPTC_UNORM = 0x8E8C;
    public static final int GL_COMPRESSED_SRGB_ALPHA_BPTC_UNORM = 0x8E8D;
    public static final int GL_COMPRESSED_RGB_BPTC_SIGNED_FLOAT = 0x8E8E;
    public static final int GL_COMPRESSED_RGB_BPTC_UNSIGNED_FLOAT = 0x8E8F;
    public static final int GL_COPY_READ_BUFFER = 0x8F36;
    public static final int GL_COPY_READ_BUFFER_BINDING = 0x8F36;
    public static final int GL_COPY_WRITE_BUFFER = 0x8F37;
    public static final int GL_COPY_WRITE_BUFFER_BINDING = 0x8F37;
    public static final int GL_MAX_IMAGE_UNITS = 0x8F38;
    public static final int GL_MAX_COMBINED_IMAGE_UNITS_AND_FRAGMENT_OUTPUTS = 0x8F39;
    public static final int GL_MAX_COMBINED_SHADER_OUTPUT_RESOURCES = 0x8F39;
    public static final int GL_IMAGE_BINDING_NAME = 0x8F3A;
    public static final int GL_IMAGE_BINDING_LEVEL = 0x8F3B;
    public static final int GL_IMAGE_BINDING_LAYERED = 0x8F3C;
    public static final int GL_IMAGE_BINDING_LAYER = 0x8F3D;
    public static final int GL_IMAGE_BINDING_ACCESS = 0x8F3E;
    public static final int GL_DRAW_INDIRECT_BUFFER = 0x8F3F;
    public static final int GL_DRAW_INDIRECT_BUFFER_BINDING = 0x8F43;
    public static final int GL_DOUBLE_MAT2 = 0x8F46;
    public static final int GL_DOUBLE_MAT3 = 0x8F47;
    public static final int GL_DOUBLE_MAT4 = 0x8F48;
    public static final int GL_DOUBLE_MAT2x3 = 0x8F49;
    public static final int GL_DOUBLE_MAT2x4 = 0x8F4A;
    public static final int GL_DOUBLE_MAT3x2 = 0x8F4B;
    public static final int GL_DOUBLE_MAT3x4 = 0x8F4C;
    public static final int GL_DOUBLE_MAT4x2 = 0x8F4D;
    public static final int GL_DOUBLE_MAT4x3 = 0x8F4E;
    public static final int GL_VERTEX_BINDING_BUFFER = 0x8F4F;
    public static final int GL_RED_SNORM = 0x8F90;
    public static final int GL_RG_SNORM = 0x8F91;
    public static final int GL_RGB_SNORM = 0x8F92;
    public static final int GL_RGBA_SNORM = 0x8F93;
    public static final int GL_R8_SNORM = 0x8F94;
    public static final int GL_RG8_SNORM = 0x8F95;
    public static final int GL_RGB8_SNORM = 0x8F96;
    public static final int GL_RGBA8_SNORM = 0x8F97;
    public static final int GL_R16_SNORM = 0x8F98;
    public static final int GL_RG16_SNORM = 0x8F99;
    public static final int GL_RGB16_SNORM = 0x8F9A;
    public static final int GL_RGBA16_SNORM = 0x8F9B;
    public static final int GL_SIGNED_NORMALIZED = 0x8F9C;
    public static final int GL_PRIMITIVE_RESTART = 0x8F9D;
    public static final int GL_PRIMITIVE_RESTART_INDEX = 0x8F9E;
    public static final int GL_SHADER_BINARY_VIV = 0x8FC4;
    public static final int GL_DOUBLE_VEC2 = 0x8FFC;
    public static final int GL_DOUBLE_VEC3 = 0x8FFD;
    public static final int GL_DOUBLE_VEC4 = 0x8FFE;
    public static final int GL_TEXTURE_CUBE_MAP_ARRAY = 0x9009;
    public static final int GL_TEXTURE_BINDING_CUBE_MAP_ARRAY = 0x900A;
    public static final int GL_PROXY_TEXTURE_CUBE_MAP_ARRAY = 0x900B;
    public static final int GL_SAMPLER_CUBE_MAP_ARRAY = 0x900C;
    public static final int GL_SAMPLER_CUBE_MAP_ARRAY_SHADOW = 0x900D;
    public static final int GL_INT_SAMPLER_CUBE_MAP_ARRAY = 0x900E;
    public static final int GL_UNSIGNED_INT_SAMPLER_CUBE_MAP_ARRAY = 0x900F;
    public static final int GL_ALPHA_SNORM = 0x9010;
    public static final int GL_LUMINANCE_SNORM = 0x9011;
    public static final int GL_LUMINANCE_ALPHA_SNORM = 0x9012;
    public static final int GL_INTENSITY_SNORM = 0x9013;
    public static final int GL_ALPHA8_SNORM = 0x9014;
    public static final int GL_LUMINANCE8_SNORM = 0x9015;
    public static final int GL_LUMINANCE8_ALPHA8_SNORM = 0x9016;
    public static final int GL_INTENSITY8_SNORM = 0x9017;
    public static final int GL_ALPHA16_SNORM = 0x9018;
    public static final int GL_LUMINANCE16_SNORM = 0x9019;
    public static final int GL_LUMINANCE16_ALPHA16_SNORM = 0x901A;
    public static final int GL_INTENSITY16_SNORM = 0x901B;
    public static final int GL_GPU_MEMORY_INFO_DEDICATED_VIDMEM_NVX = 0x9047;
    public static final int GL_GPU_MEMORY_INFO_TOTAL_AVAILABLE_MEMORY_NVX = 0x9048;
    public static final int GL_GPU_MEMORY_INFO_CURRENT_AVAILABLE_VIDMEM_NVX = 0x9049;
    public static final int GL_GPU_MEMORY_INFO_EVICTION_COUNT_NVX = 0x904A;
    public static final int GL_GPU_MEMORY_INFO_EVICTED_MEMORY_NVX = 0x904B;
    public static final int GL_IMAGE_1D = 0x904C;
    public static final int GL_IMAGE_2D = 0x904D;
    public static final int GL_IMAGE_3D = 0x904E;
    public static final int GL_IMAGE_2D_RECT = 0x904F;
    public static final int GL_IMAGE_CUBE = 0x9050;
    public static final int GL_IMAGE_BUFFER = 0x9051;
    public static final int GL_IMAGE_1D_ARRAY = 0x9052;
    public static final int GL_IMAGE_2D_ARRAY = 0x9053;
    public static final int GL_IMAGE_CUBE_MAP_ARRAY = 0x9054;
    public static final int GL_IMAGE_2D_MULTISAMPLE = 0x9055;
    public static final int GL_IMAGE_2D_MULTISAMPLE_ARRAY = 0x9056;
    public static final int GL_INT_IMAGE_1D = 0x9057;
    public static final int GL_INT_IMAGE_2D = 0x9058;
    public static final int GL_INT_IMAGE_3D = 0x9059;
    public static final int GL_INT_IMAGE_2D_RECT = 0x905A;
    public static final int GL_INT_IMAGE_CUBE = 0x905B;
    public static final int GL_INT_IMAGE_BUFFER = 0x905C;
    public static final int GL_INT_IMAGE_1D_ARRAY = 0x905D;
    public static final int GL_INT_IMAGE_2D_ARRAY = 0x905E;
    public static final int GL_INT_IMAGE_CUBE_MAP_ARRAY = 0x905F;
    public static final int GL_INT_IMAGE_2D_MULTISAMPLE = 0x9060;
    public static final int GL_INT_IMAGE_2D_MULTISAMPLE_ARRAY = 0x9061;
    public static final int GL_UNSIGNED_INT_IMAGE_1D = 0x9062;
    public static final int GL_UNSIGNED_INT_IMAGE_2D = 0x9063;
    public static final int GL_UNSIGNED_INT_IMAGE_3D = 0x9064;
    public static final int GL_UNSIGNED_INT_IMAGE_2D_RECT = 0x9065;
    public static final int GL_UNSIGNED_INT_IMAGE_CUBE = 0x9066;
    public static final int GL_UNSIGNED_INT_IMAGE_BUFFER = 0x9067;
    public static final int GL_UNSIGNED_INT_IMAGE_1D_ARRAY = 0x9068;
    public static final int GL_UNSIGNED_INT_IMAGE_2D_ARRAY = 0x9069;
    public static final int GL_UNSIGNED_INT_IMAGE_CUBE_MAP_ARRAY = 0x906A;
    public static final int GL_UNSIGNED_INT_IMAGE_2D_MULTISAMPLE = 0x906B;
    public static final int GL_UNSIGNED_INT_IMAGE_2D_MULTISAMPLE_ARRAY = 0x906C;
    public static final int GL_MAX_IMAGE_SAMPLES = 0x906D;
    public static final int GL_IMAGE_BINDING_FORMAT = 0x906E;
    public static final int GL_RGB10_A2UI = 0x906F;
    public static final int GL_MIN_MAP_BUFFER_ALIGNMENT = 0x90BC;
    public static final int GL_IMAGE_FORMAT_COMPATIBILITY_TYPE = 0x90C7;
    public static final int GL_IMAGE_FORMAT_COMPATIBILITY_BY_SIZE = 0x90C8;
    public static final int GL_IMAGE_FORMAT_COMPATIBILITY_BY_CLASS = 0x90C9;
    public static final int GL_MAX_VERTEX_IMAGE_UNIFORMS = 0x90CA;
    public static final int GL_MAX_TESS_CONTROL_IMAGE_UNIFORMS = 0x90CB;
    public static final int GL_MAX_TESS_EVALUATION_IMAGE_UNIFORMS = 0x90CC;
    public static final int GL_MAX_GEOMETRY_IMAGE_UNIFORMS = 0x90CD;
    public static final int GL_MAX_FRAGMENT_IMAGE_UNIFORMS = 0x90CE;
    public static final int GL_MAX_COMBINED_IMAGE_UNIFORMS = 0x90CF;
    public static final int GL_SHADER_STORAGE_BUFFER = 0x90D2;
    public static final int GL_SHADER_STORAGE_BUFFER_BINDING = 0x90D3;
    public static final int GL_SHADER_STORAGE_BUFFER_START = 0x90D4;
    public static final int GL_SHADER_STORAGE_BUFFER_SIZE = 0x90D5;
    public static final int GL_MAX_VERTEX_SHADER_STORAGE_BLOCKS = 0x90D6;
    public static final int GL_MAX_GEOMETRY_SHADER_STORAGE_BLOCKS = 0x90D7;
    public static final int GL_MAX_TESS_CONTROL_SHADER_STORAGE_BLOCKS = 0x90D8;
    public static final int GL_MAX_TESS_EVALUATION_SHADER_STORAGE_BLOCKS = 0x90D9;
    public static final int GL_MAX_FRAGMENT_SHADER_STORAGE_BLOCKS = 0x90DA;
    public static final int GL_MAX_COMPUTE_SHADER_STORAGE_BLOCKS = 0x90DB;
    public static final int GL_MAX_COMBINED_SHADER_STORAGE_BLOCKS = 0x90DC;
    public static final int GL_MAX_SHADER_STORAGE_BUFFER_BINDINGS = 0x90DD;
    public static final int GL_MAX_SHADER_STORAGE_BLOCK_SIZE = 0x90DE;
    public static final int GL_SHADER_STORAGE_BUFFER_OFFSET_ALIGNMENT = 0x90DF;
    public static final int GL_DEPTH_STENCIL_TEXTURE_MODE = 0x90EA;
    public static final int GL_MAX_COMPUTE_WORK_GROUP_INVOCATIONS = 0x90EB;
    public static final int GL_UNIFORM_BLOCK_REFERENCED_BY_COMPUTE_SHADER = 0x90EC;
    public static final int GL_ATOMIC_COUNTER_BUFFER_REFERENCED_BY_COMPUTE_SHADER = 0x90ED;
    public static final int GL_DISPATCH_INDIRECT_BUFFER = 0x90EE;
    public static final int GL_DISPATCH_INDIRECT_BUFFER_BINDING = 0x90EF;
    public static final int GL_CONTEXT_ROBUST_ACCESS = 0x90F3;
    public static final int GL_TEXTURE_2D_MULTISAMPLE = 0x9100;
    public static final int GL_PROXY_TEXTURE_2D_MULTISAMPLE = 0x9101;
    public static final int GL_TEXTURE_2D_MULTISAMPLE_ARRAY = 0x9102;
    public static final int GL_PROXY_TEXTURE_2D_MULTISAMPLE_ARRAY = 0x9103;
    public static final int GL_TEXTURE_BINDING_2D_MULTISAMPLE = 0x9104;
    public static final int GL_TEXTURE_BINDING_2D_MULTISAMPLE_ARRAY = 0x9105;
    public static final int GL_TEXTURE_SAMPLES = 0x9106;
    public static final int GL_TEXTURE_FIXED_SAMPLE_LOCATIONS = 0x9107;
    public static final int GL_SAMPLER_2D_MULTISAMPLE = 0x9108;
    public static final int GL_INT_SAMPLER_2D_MULTISAMPLE = 0x9109;
    public static final int GL_UNSIGNED_INT_SAMPLER_2D_MULTISAMPLE = 0x910A;
    public static final int GL_SAMPLER_2D_MULTISAMPLE_ARRAY = 0x910B;
    public static final int GL_INT_SAMPLER_2D_MULTISAMPLE_ARRAY = 0x910C;
    public static final int GL_UNSIGNED_INT_SAMPLER_2D_MULTISAMPLE_ARRAY = 0x910D;
    public static final int GL_MAX_COLOR_TEXTURE_SAMPLES = 0x910E;
    public static final int GL_MAX_DEPTH_TEXTURE_SAMPLES = 0x910F;
    public static final int GL_MAX_INTEGER_SAMPLES = 0x9110;
    public static final int GL_MAX_SERVER_WAIT_TIMEOUT = 0x9111;
    public static final int GL_OBJECT_TYPE = 0x9112;
    public static final int GL_SYNC_CONDITION = 0x9113;
    public static final int GL_SYNC_STATUS = 0x9114;
    public static final int GL_SYNC_FLAGS = 0x9115;
    public static final int GL_SYNC_FENCE = 0x9116;
    public static final int GL_SYNC_GPU_COMMANDS_COMPLETE = 0x9117;
    public static final int GL_UNSIGNALED = 0x9118;
    public static final int GL_SIGNALED = 0x9119;
    public static final int GL_ALREADY_SIGNALED = 0x911A;
    public static final int GL_TIMEOUT_EXPIRED = 0x911B;
    public static final int GL_CONDITION_SATISFIED = 0x911C;
    public static final int GL_WAIT_FAILED = 0x911D;
    public static final int GL_BUFFER_ACCESS_FLAGS = 0x911F;
    public static final int GL_BUFFER_MAP_LENGTH = 0x9120;
    public static final int GL_BUFFER_MAP_OFFSET = 0x9121;
    public static final int GL_MAX_VERTEX_OUTPUT_COMPONENTS = 0x9122;
    public static final int GL_MAX_GEOMETRY_INPUT_COMPONENTS = 0x9123;
    public static final int GL_MAX_GEOMETRY_OUTPUT_COMPONENTS = 0x9124;
    public static final int GL_MAX_FRAGMENT_INPUT_COMPONENTS = 0x9125;
    public static final int GL_CONTEXT_PROFILE_MASK = 0x9126;
    public static final int GL_UNPACK_COMPRESSED_BLOCK_WIDTH = 0x9127;
    public static final int GL_UNPACK_COMPRESSED_BLOCK_HEIGHT = 0x9128;
    public static final int GL_UNPACK_COMPRESSED_BLOCK_DEPTH = 0x9129;
    public static final int GL_UNPACK_COMPRESSED_BLOCK_SIZE = 0x912A;
    public static final int GL_PACK_COMPRESSED_BLOCK_WIDTH = 0x912B;
    public static final int GL_PACK_COMPRESSED_BLOCK_HEIGHT = 0x912C;
    public static final int GL_PACK_COMPRESSED_BLOCK_DEPTH = 0x912D;
    public static final int GL_PACK_COMPRESSED_BLOCK_SIZE = 0x912E;
    public static final int GL_TEXTURE_IMMUTABLE_FORMAT = 0x912F;
    public static final int GL_MAX_DEBUG_MESSAGE_LENGTH = 0x9143;
    public static final int GL_MAX_DEBUG_LOGGED_MESSAGES = 0x9144;
    public static final int GL_DEBUG_LOGGED_MESSAGES = 0x9145;
    public static final int GL_DEBUG_SEVERITY_HIGH = 0x9146;
    public static final int GL_DEBUG_SEVERITY_MEDIUM = 0x9147;
    public static final int GL_DEBUG_SEVERITY_LOW = 0x9148;
    public static final int GL_QUERY_BUFFER = 0x9192;
    public static final int GL_QUERY_BUFFER_BINDING = 0x9193;
    public static final int GL_QUERY_RESULT_NO_WAIT = 0x9194;
    public static final int GL_MAX_SPARSE_ARRAY_TEXTURE_LAYERS = 0x919A;
    public static final int GL_TEXTURE_BUFFER_OFFSET = 0x919D;
    public static final int GL_TEXTURE_BUFFER_SIZE = 0x919E;
    public static final int GL_TEXTURE_BUFFER_OFFSET_ALIGNMENT = 0x919F;
    public static final int GL_COMPUTE_SHADER = 0x91B9;
    public static final int GL_MAX_COMPUTE_UNIFORM_BLOCKS = 0x91BB;
    public static final int GL_MAX_COMPUTE_TEXTURE_IMAGE_UNITS = 0x91BC;
    public static final int GL_MAX_COMPUTE_IMAGE_UNIFORMS = 0x91BD;
    public static final int GL_MAX_COMPUTE_WORK_GROUP_COUNT = 0x91BE;
    public static final int GL_MAX_COMPUTE_WORK_GROUP_SIZE = 0x91BF;
    public static final int GL_UNPACK_FLIP_Y_WEBGL = 0x9240;
    public static final int GL_UNPACK_PREMULTIPLY_ALPHA_WEBGL = 0x9241;
    public static final int GL_CONTEXT_LOST_WEBGL = 0x9242;
    public static final int GL_UNPACK_COLORSPACE_CONVERSION_WEBGL = 0x9243;
    public static final int GL_BROWSER_DEFAULT_WEBGL = 0x9244;
    public static final int GL_SHADER_BINARY_DMP = 0x9250;
    public static final int GL_SMAPHS30_PROGRAM_BINARY_DMP = 0x9251;
    public static final int GL_SMAPHS_PROGRAM_BINARY_DMP = 0x9252;
    public static final int GL_DMP_PROGRAM_BINARY_DMP = 0x9253;
    public static final int GL_GCCSO_SHADER_BINARY_FJ = 0x9260;
    public static final int GL_COMPRESSED_R11_EAC = 0x9270;
    public static final int GL_COMPRESSED_SIGNED_R11_EAC = 0x9271;
    public static final int GL_COMPRESSED_RG11_EAC = 0x9272;
    public static final int GL_COMPRESSED_SIGNED_RG11_EAC = 0x9273;
    public static final int GL_COMPRESSED_RGB8_ETC2 = 0x9274;
    public static final int GL_COMPRESSED_SRGB8_ETC2 = 0x9275;
    public static final int GL_COMPRESSED_RGB8_PUNCHTHROUGH_ALPHA1_ETC2 = 0x9276;
    public static final int GL_COMPRESSED_SRGB8_PUNCHTHROUGH_ALPHA1_ETC2 = 0x9277;
    public static final int GL_COMPRESSED_RGBA8_ETC2_EAC = 0x9278;
    public static final int GL_COMPRESSED_SRGB8_ALPHA8_ETC2_EAC = 0x9279;
    public static final int GL_MULTIPLY = 0x9294;
    public static final int GL_SCREEN = 0x9295;
    public static final int GL_OVERLAY = 0x9296;
    public static final int GL_DARKEN = 0x9297;
    public static final int GL_LIGHTEN = 0x9298;
    public static final int GL_COLORDODGE = 0x9299;
    public static final int GL_COLORBURN = 0x929A;
    public static final int GL_HARDLIGHT = 0x929B;
    public static final int GL_SOFTLIGHT = 0x929C;
    public static final int GL_DIFFERENCE = 0x929E;
    public static final int GL_EXCLUSION = 0x92A0;
    public static final int GL_HSL_HUE = 0x92AD;
    public static final int GL_HSL_SATURATION = 0x92AE;
    public static final int GL_HSL_COLOR = 0x92AF;
    public static final int GL_HSL_LUMINOSITY = 0x92B0;
    public static final int GL_PRIMITIVE_BOUNDING_BOX = 0x92BE;
    public static final int GL_ATOMIC_COUNTER_BUFFER = 0x92C0;
    public static final int GL_ATOMIC_COUNTER_BUFFER_BINDING = 0x92C1;
    public static final int GL_ATOMIC_COUNTER_BUFFER_START = 0x92C2;
    public static final int GL_ATOMIC_COUNTER_BUFFER_SIZE = 0x92C3;
    public static final int GL_ATOMIC_COUNTER_BUFFER_DATA_SIZE = 0x92C4;
    public static final int GL_ATOMIC_COUNTER_BUFFER_ACTIVE_ATOMIC_COUNTERS = 0x92C5;
    public static final int GL_ATOMIC_COUNTER_BUFFER_ACTIVE_ATOMIC_COUNTER_INDICES = 0x92C6;
    public static final int GL_ATOMIC_COUNTER_BUFFER_REFERENCED_BY_VERTEX_SHADER = 0x92C7;
    public static final int GL_ATOMIC_COUNTER_BUFFER_REFERENCED_BY_TESS_CONTROL_SHADER = 0x92C8;
    public static final int GL_ATOMIC_COUNTER_BUFFER_REFERENCED_BY_TESS_EVALUATION_SHADER = 0x92C9;
    public static final int GL_ATOMIC_COUNTER_BUFFER_REFERENCED_BY_GEOMETRY_SHADER = 0x92CA;
    public static final int GL_ATOMIC_COUNTER_BUFFER_REFERENCED_BY_FRAGMENT_SHADER = 0x92CB;
    public static final int GL_MAX_VERTEX_ATOMIC_COUNTER_BUFFERS = 0x92CC;
    public static final int GL_MAX_TESS_CONTROL_ATOMIC_COUNTER_BUFFERS = 0x92CD;
    public static final int GL_MAX_TESS_EVALUATION_ATOMIC_COUNTER_BUFFERS = 0x92CE;
    public static final int GL_MAX_GEOMETRY_ATOMIC_COUNTER_BUFFERS = 0x92CF;
    public static final int GL_MAX_FRAGMENT_ATOMIC_COUNTER_BUFFERS = 0x92D0;
    public static final int GL_MAX_COMBINED_ATOMIC_COUNTER_BUFFERS = 0x92D1;
    public static final int GL_MAX_VERTEX_ATOMIC_COUNTERS = 0x92D2;
    public static final int GL_MAX_TESS_CONTROL_ATOMIC_COUNTERS = 0x92D3;
    public static final int GL_MAX_TESS_EVALUATION_ATOMIC_COUNTERS = 0x92D4;
    public static final int GL_MAX_GEOMETRY_ATOMIC_COUNTERS = 0x92D5;
    public static final int GL_MAX_FRAGMENT_ATOMIC_COUNTERS = 0x92D6;
    public static final int GL_MAX_COMBINED_ATOMIC_COUNTERS = 0x92D7;
    public static final int GL_MAX_ATOMIC_COUNTER_BUFFER_SIZE = 0x92D8;
    public static final int GL_ACTIVE_ATOMIC_COUNTER_BUFFERS = 0x92D9;
    public static final int GL_UNIFORM_ATOMIC_COUNTER_BUFFER_INDEX = 0x92DA;
    public static final int GL_UNSIGNED_INT_ATOMIC_COUNTER = 0x92DB;
    public static final int GL_MAX_ATOMIC_COUNTER_BUFFER_BINDINGS = 0x92DC;
    public static final int GL_DEBUG_OUTPUT = 0x92E0;
    public static final int GL_UNIFORM = 0x92E1;
    public static final int GL_UNIFORM_BLOCK = 0x92E2;
    public static final int GL_PROGRAM_INPUT = 0x92E3;
    public static final int GL_PROGRAM_OUTPUT = 0x92E4;
    public static final int GL_BUFFER_VARIABLE = 0x92E5;
    public static final int GL_SHADER_STORAGE_BLOCK = 0x92E6;
    public static final int GL_IS_PER_PATCH = 0x92E7;
    public static final int GL_VERTEX_SUBROUTINE = 0x92E8;
    public static final int GL_TESS_CONTROL_SUBROUTINE = 0x92E9;
    public static final int GL_TESS_EVALUATION_SUBROUTINE = 0x92EA;
    public static final int GL_GEOMETRY_SUBROUTINE = 0x92EB;
    public static final int GL_FRAGMENT_SUBROUTINE = 0x92EC;
    public static final int GL_COMPUTE_SUBROUTINE = 0x92ED;
    public static final int GL_VERTEX_SUBROUTINE_UNIFORM = 0x92EE;
    public static final int GL_TESS_CONTROL_SUBROUTINE_UNIFORM = 0x92EF;
    public static final int GL_TESS_EVALUATION_SUBROUTINE_UNIFORM = 0x92F0;
    public static final int GL_GEOMETRY_SUBROUTINE_UNIFORM = 0x92F1;
    public static final int GL_FRAGMENT_SUBROUTINE_UNIFORM = 0x92F2;
    public static final int GL_COMPUTE_SUBROUTINE_UNIFORM = 0x92F3;
    public static final int GL_TRANSFORM_FEEDBACK_VARYING = 0x92F4;
    public static final int GL_ACTIVE_RESOURCES = 0x92F5;
    public static final int GL_MAX_NAME_LENGTH = 0x92F6;
    public static final int GL_MAX_NUM_ACTIVE_VARIABLES = 0x92F7;
    public static final int GL_MAX_NUM_COMPATIBLE_SUBROUTINES = 0x92F8;
    public static final int GL_NAME_LENGTH = 0x92F9;
    public static final int GL_TYPE = 0x92FA;
    public static final int GL_ARRAY_SIZE = 0x92FB;
    public static final int GL_OFFSET = 0x92FC;
    public static final int GL_BLOCK_INDEX = 0x92FD;
    public static final int GL_ARRAY_STRIDE = 0x92FE;
    public static final int GL_MATRIX_STRIDE = 0x92FF;
    public static final int GL_IS_ROW_MAJOR = 0x9300;
    public static final int GL_ATOMIC_COUNTER_BUFFER_INDEX = 0x9301;
    public static final int GL_BUFFER_BINDING = 0x9302;
    public static final int GL_BUFFER_DATA_SIZE = 0x9303;
    public static final int GL_NUM_ACTIVE_VARIABLES = 0x9304;
    public static final int GL_ACTIVE_VARIABLES = 0x9305;
    public static final int GL_REFERENCED_BY_VERTEX_SHADER = 0x9306;
    public static final int GL_REFERENCED_BY_TESS_CONTROL_SHADER = 0x9307;
    public static final int GL_REFERENCED_BY_TESS_EVALUATION_SHADER = 0x9308;
    public static final int GL_REFERENCED_BY_GEOMETRY_SHADER = 0x9309;
    public static final int GL_REFERENCED_BY_FRAGMENT_SHADER = 0x930A;
    public static final int GL_REFERENCED_BY_COMPUTE_SHADER = 0x930B;
    public static final int GL_TOP_LEVEL_ARRAY_SIZE = 0x930C;
    public static final int GL_TOP_LEVEL_ARRAY_STRIDE = 0x930D;
    public static final int GL_LOCATION = 0x930E;
    public static final int GL_LOCATION_INDEX = 0x930F;
    public static final int GL_FRAMEBUFFER_DEFAULT_WIDTH = 0x9310;
    public static final int GL_FRAMEBUFFER_DEFAULT_HEIGHT = 0x9311;
    public static final int GL_FRAMEBUFFER_DEFAULT_LAYERS = 0x9312;
    public static final int GL_FRAMEBUFFER_DEFAULT_SAMPLES = 0x9313;
    public static final int GL_FRAMEBUFFER_DEFAULT_FIXED_SAMPLE_LOCATIONS = 0x9314;
    public static final int GL_MAX_FRAMEBUFFER_WIDTH = 0x9315;
    public static final int GL_MAX_FRAMEBUFFER_HEIGHT = 0x9316;
    public static final int GL_MAX_FRAMEBUFFER_LAYERS = 0x9317;
    public static final int GL_MAX_FRAMEBUFFER_SAMPLES = 0x9318;
    public static final int GL_LOCATION_COMPONENT = 0x934A;
    public static final int GL_TRANSFORM_FEEDBACK_BUFFER_INDEX = 0x934B;
    public static final int GL_TRANSFORM_FEEDBACK_BUFFER_STRIDE = 0x934C;
    public static final int GL_CLIP_ORIGIN = 0x935C;
    public static final int GL_CLIP_DEPTH_MODE = 0x935D;
    public static final int GL_NEGATIVE_ONE_TO_ONE = 0x935E;
    public static final int GL_ZERO_TO_ONE = 0x935F;
    public static final int GL_CLEAR_TEXTURE = 0x9365;
    public static final int GL_NUM_SAMPLE_COUNTS = 0x9380;
    public static final int GL_MULTISAMPLE_LINE_WIDTH_RANGE = 0x9381;
    public static final int GL_MULTISAMPLE_LINE_WIDTH_GRANULARITY = 0x9382;
    public static final int GL_COMPRESSED_RGBA_ASTC_4x4 = 0x93B0;
    public static final int GL_COMPRESSED_RGBA_ASTC_5x4 = 0x93B1;
    public static final int GL_COMPRESSED_RGBA_ASTC_5x5 = 0x93B2;
    public static final int GL_COMPRESSED_RGBA_ASTC_6x5 = 0x93B3;
    public static final int GL_COMPRESSED_RGBA_ASTC_6x6 = 0x93B4;
    public static final int GL_COMPRESSED_RGBA_ASTC_8x5 = 0x93B5;
    public static final int GL_COMPRESSED_RGBA_ASTC_8x6 = 0x93B6;
    public static final int GL_COMPRESSED_RGBA_ASTC_8x8 = 0x93B7;
    public static final int GL_COMPRESSED_RGBA_ASTC_10x5 = 0x93B8;
    public static final int GL_COMPRESSED_RGBA_ASTC_10x6 = 0x93B9;
    public static final int GL_COMPRESSED_RGBA_ASTC_10x8 = 0x93BA;
    public static final int GL_COMPRESSED_RGBA_ASTC_10x10 = 0x93BB;
    public static final int GL_COMPRESSED_RGBA_ASTC_12x10 = 0x93BC;
    public static final int GL_COMPRESSED_RGBA_ASTC_12x12 = 0x93BD;
    public static final int GL_COMPRESSED_SRGB8_ALPHA8_ASTC_4x4 = 0x93D0;
    public static final int GL_COMPRESSED_SRGB8_ALPHA8_ASTC_5x4 = 0x93D1;
    public static final int GL_COMPRESSED_SRGB8_ALPHA8_ASTC_5x5 = 0x93D2;
    public static final int GL_COMPRESSED_SRGB8_ALPHA8_ASTC_6x5 = 0x93D3;
    public static final int GL_COMPRESSED_SRGB8_ALPHA8_ASTC_6x6 = 0x93D4;
    public static final int GL_COMPRESSED_SRGB8_ALPHA8_ASTC_8x5 = 0x93D5;
    public static final int GL_COMPRESSED_SRGB8_ALPHA8_ASTC_8x6 = 0x93D6;
    public static final int GL_COMPRESSED_SRGB8_ALPHA8_ASTC_8x8 = 0x93D7;
    public static final int GL_COMPRESSED_SRGB8_ALPHA8_ASTC_10x5 = 0x93D8;
    public static final int GL_COMPRESSED_SRGB8_ALPHA8_ASTC_10x6 = 0x93D9;
    public static final int GL_COMPRESSED_SRGB8_ALPHA8_ASTC_10x8 = 0x93DA;
    public static final int GL_COMPRESSED_SRGB8_ALPHA8_ASTC_10x10 = 0x93DB;
    public static final int GL_COMPRESSED_SRGB8_ALPHA8_ASTC_12x10 = 0x93DC;
    public static final int GL_COMPRESSED_SRGB8_ALPHA8_ASTC_12x12 = 0x93DD;
    public static final int GL_FRAMEBUFFER_ATTACHMENT_TEXTURE_NUM_VIEWS_OVR = 0x9630;
    public static final int GL_MAX_VIEWS_OVR = 0x9631;
    public static final int GL_FRAMEBUFFER_ATTACHMENT_TEXTURE_BASE_VIEW_INDEX_OVR = 0x9632;
    public static final int GL_GS_SHADER_BINARY_MTK = 0x9640;
    public static final int GL_GS_PROGRAM_BINARY_MTK = 0x9641;
    //functions
    /* <command>
            <proto>void <name>glActiveShaderProgram</name></proto>
            <param><ptype>GLuint</ptype> <name>pipeline</name></param>
            <param><ptype>GLuint</ptype> <name>program</name></param>
        </command>
         */
    private static Function _glActiveShaderProgram ;
    public static void glActiveShaderProgram (int pipeline_ , int program_ ) {
        if( _glActiveShaderProgram == null )
            _glActiveShaderProgram = loadFunction("glActiveShaderProgram");
        _glActiveShaderProgram.invoke(Void.class, new Object[]{pipeline_,program_ });
    }
    /* <command>
            <proto>void <name>glActiveTexture</name></proto>
            <param group="TextureUnit"><ptype>GLenum</ptype> <name>texture</name></param>
            <glx opcode="197" type="render" />
        </command>
         */
    private static Function _glActiveTexture ;
    public static void glActiveTexture (int texture_ ) {
        if( _glActiveTexture == null )
            _glActiveTexture = loadFunction("glActiveTexture");
        _glActiveTexture.invoke(Void.class, new Object[]{texture_ });
    }
    /* <command>
            <proto>void <name>glAttachShader</name></proto>
            <param><ptype>GLuint</ptype> <name>program</name></param>
            <param><ptype>GLuint</ptype> <name>shader</name></param>
        </command>
         */
    private static Function _glAttachShader ;
    public static void glAttachShader (int program_ , int shader_ ) {
        if( _glAttachShader == null )
            _glAttachShader = loadFunction("glAttachShader");
        _glAttachShader.invoke(Void.class, new Object[]{program_,shader_ });
    }
    /* <command>
            <proto>void <name>glBeginConditionalRender</name></proto>
            <param><ptype>GLuint</ptype> <name>id</name></param>
            <param group="TypeEnum"><ptype>GLenum</ptype> <name>mode</name></param>
        </command>
         */
    private static Function _glBeginConditionalRender ;
    public static void glBeginConditionalRender (int id_ , int mode_ ) {
        if( _glBeginConditionalRender == null )
            _glBeginConditionalRender = loadFunction("glBeginConditionalRender");
        _glBeginConditionalRender.invoke(Void.class, new Object[]{id_,mode_ });
    }
    /* <command>
            <proto>void <name>glBeginQuery</name></proto>
            <param><ptype>GLenum</ptype> <name>target</name></param>
            <param><ptype>GLuint</ptype> <name>id</name></param>
            <glx opcode="231" type="render" />
        </command>
         */
    private static Function _glBeginQuery ;
    public static void glBeginQuery (int target_ , int id_ ) {
        if( _glBeginQuery == null )
            _glBeginQuery = loadFunction("glBeginQuery");
        _glBeginQuery.invoke(Void.class, new Object[]{target_,id_ });
    }
    /* <command>
            <proto>void <name>glBeginQueryIndexed</name></proto>
            <param><ptype>GLenum</ptype> <name>target</name></param>
            <param><ptype>GLuint</ptype> <name>index</name></param>
            <param><ptype>GLuint</ptype> <name>id</name></param>
        </command>
         */
    private static Function _glBeginQueryIndexed ;
    public static void glBeginQueryIndexed (int target_ , int index_ , int id_ ) {
        if( _glBeginQueryIndexed == null )
            _glBeginQueryIndexed = loadFunction("glBeginQueryIndexed");
        _glBeginQueryIndexed.invoke(Void.class, new Object[]{target_,index_,id_ });
    }
    /* <command>
            <proto>void <name>glBeginTransformFeedback</name></proto>
            <param><ptype>GLenum</ptype> <name>primitiveMode</name></param>
        </command>
         */
    private static Function _glBeginTransformFeedback ;
    public static void glBeginTransformFeedback (int primitiveMode_ ) {
        if( _glBeginTransformFeedback == null )
            _glBeginTransformFeedback = loadFunction("glBeginTransformFeedback");
        _glBeginTransformFeedback.invoke(Void.class, new Object[]{primitiveMode_ });
    }
    /* <command>
            <proto>void <name>glBindAttribLocation</name></proto>
            <param><ptype>GLuint</ptype> <name>program</name></param>
            <param><ptype>GLuint</ptype> <name>index</name></param>
            <param>const <ptype>GLchar</ptype> *<name>name</name></param>
        </command>
         */
    private static Function _glBindAttribLocation ;
    public static void glBindAttribLocation (int program_ , int index_ , String name_ ) {
        if( _glBindAttribLocation == null )
            _glBindAttribLocation = loadFunction("glBindAttribLocation");
        _glBindAttribLocation.invoke(Void.class, new Object[]{program_,index_,name_ });
    }
    /* <command>
            <proto>void <name>glBindBuffer</name></proto>
            <param group="BufferTargetARB"><ptype>GLenum</ptype> <name>target</name></param>
            <param><ptype>GLuint</ptype> <name>buffer</name></param>
        </command>
         */
    private static Function _glBindBuffer ;
    public static void glBindBuffer (int target_ , int buffer_ ) {
        if( _glBindBuffer == null )
            _glBindBuffer = loadFunction("glBindBuffer");
        _glBindBuffer.invoke(Void.class, new Object[]{target_,buffer_ });
    }
    /* <command>
            <proto>void <name>glBindBufferBase</name></proto>
            <param><ptype>GLenum</ptype> <name>target</name></param>
            <param><ptype>GLuint</ptype> <name>index</name></param>
            <param><ptype>GLuint</ptype> <name>buffer</name></param>
        </command>
         */
    private static Function _glBindBufferBase ;
    public static void glBindBufferBase (int target_ , int index_ , int buffer_ ) {
        if( _glBindBufferBase == null )
            _glBindBufferBase = loadFunction("glBindBufferBase");
        _glBindBufferBase.invoke(Void.class, new Object[]{target_,index_,buffer_ });
    }
    /* <command>
            <proto>void <name>glBindBufferRange</name></proto>
            <param><ptype>GLenum</ptype> <name>target</name></param>
            <param><ptype>GLuint</ptype> <name>index</name></param>
            <param><ptype>GLuint</ptype> <name>buffer</name></param>
            <param group="BufferOffset"><ptype>GLintptr</ptype> <name>offset</name></param>
            <param group="BufferSize"><ptype>GLsizeiptr</ptype> <name>size</name></param>
        </command>
         */
    private static Function _glBindBufferRange ;
    public static void glBindBufferRange (int target_ , int index_ , int buffer_ , long offset_ , long size_ ) {
        if( _glBindBufferRange == null )
            _glBindBufferRange = loadFunction("glBindBufferRange");
        _glBindBufferRange.invoke(Void.class, new Object[]{target_,index_,buffer_,offset_,size_ });
    }
    /* <command>
            <proto>void <name>glBindBuffersBase</name></proto>
            <param><ptype>GLenum</ptype> <name>target</name></param>
            <param><ptype>GLuint</ptype> <name>first</name></param>
            <param><ptype>GLsizei</ptype> <name>count</name></param>
            <param len="count">const <ptype>GLuint</ptype> *<name>buffers</name></param>
        </command>
         */
    private static Function _glBindBuffersBase ;
    public static void glBindBuffersBase (int target_ , int first_ , int count_ , int[] buffers_ ) {
        if( _glBindBuffersBase == null )
            _glBindBuffersBase = loadFunction("glBindBuffersBase");
        _glBindBuffersBase.invoke(Void.class, new Object[]{target_,first_,count_,buffers_ });
    }
    /* <command>
            <proto>void <name>glBindBuffersRange</name></proto>
            <param><ptype>GLenum</ptype> <name>target</name></param>
            <param><ptype>GLuint</ptype> <name>first</name></param>
            <param><ptype>GLsizei</ptype> <name>count</name></param>
            <param len="count">const <ptype>GLuint</ptype> *<name>buffers</name></param>
            <param len="count">const <ptype>GLintptr</ptype> *<name>offsets</name></param>
            <param len="count">const <ptype>GLsizeiptr</ptype> *<name>sizes</name></param>
        </command>
         */
    private static Function _glBindBuffersRange ;
    public static void glBindBuffersRange (int target_ , int first_ , int count_ , int[] buffers_ , long[] offsets_ , long[] sizes_ ) {
        if( _glBindBuffersRange == null )
            _glBindBuffersRange = loadFunction("glBindBuffersRange");
        _glBindBuffersRange.invoke(Void.class, new Object[]{target_,first_,count_,buffers_,offsets_,sizes_ });
    }
    /* <command>
            <proto>void <name>glBindFragDataLocation</name></proto>
            <param><ptype>GLuint</ptype> <name>program</name></param>
            <param><ptype>GLuint</ptype> <name>color</name></param>
            <param len="COMPSIZE(name)">const <ptype>GLchar</ptype> *<name>name</name></param>
        </command>
         */
    private static Function _glBindFragDataLocation ;
    public static void glBindFragDataLocation (int program_ , int color_ , String name_ ) {
        if( _glBindFragDataLocation == null )
            _glBindFragDataLocation = loadFunction("glBindFragDataLocation");
        _glBindFragDataLocation.invoke(Void.class, new Object[]{program_,color_,name_ });
    }
    /* <command>
            <proto>void <name>glBindFragDataLocationIndexed</name></proto>
            <param><ptype>GLuint</ptype> <name>program</name></param>
            <param><ptype>GLuint</ptype> <name>colorNumber</name></param>
            <param><ptype>GLuint</ptype> <name>index</name></param>
            <param>const <ptype>GLchar</ptype> *<name>name</name></param>
        </command>
         */
    private static Function _glBindFragDataLocationIndexed ;
    public static void glBindFragDataLocationIndexed (int program_ , int colorNumber_ , int index_ , String name_ ) {
        if( _glBindFragDataLocationIndexed == null )
            _glBindFragDataLocationIndexed = loadFunction("glBindFragDataLocationIndexed");
        _glBindFragDataLocationIndexed.invoke(Void.class, new Object[]{program_,colorNumber_,index_,name_ });
    }
    /* <command>
            <proto>void <name>glBindFramebuffer</name></proto>
            <param group="FramebufferTarget"><ptype>GLenum</ptype> <name>target</name></param>
            <param><ptype>GLuint</ptype> <name>framebuffer</name></param>
            <glx opcode="236" type="render" />
        </command>
         */
    private static Function _glBindFramebuffer ;
    public static void glBindFramebuffer (int target_ , int framebuffer_ ) {
        if( _glBindFramebuffer == null )
            _glBindFramebuffer = loadFunction("glBindFramebuffer");
        _glBindFramebuffer.invoke(Void.class, new Object[]{target_,framebuffer_ });
    }
    /* <command>
            <proto>void <name>glBindImageTexture</name></proto>
            <param><ptype>GLuint</ptype> <name>unit</name></param>
            <param><ptype>GLuint</ptype> <name>texture</name></param>
            <param><ptype>GLint</ptype> <name>level</name></param>
            <param group="Boolean"><ptype>GLboolean</ptype> <name>layered</name></param>
            <param><ptype>GLint</ptype> <name>layer</name></param>
            <param><ptype>GLenum</ptype> <name>access</name></param>
            <param><ptype>GLenum</ptype> <name>format</name></param>
        </command>
         */
    private static Function _glBindImageTexture ;
    public static void glBindImageTexture (int unit_ , int texture_ , int level_ , boolean layered_ , int layer_ , int access_ , int format_ ) {
        if( _glBindImageTexture == null )
            _glBindImageTexture = loadFunction("glBindImageTexture");
        _glBindImageTexture.invoke(Void.class, new Object[]{unit_,texture_,level_,(layered_ ? (byte)1:(byte)0),layer_,access_,format_ });
    }
    /* <command>
            <proto>void <name>glBindImageTextures</name></proto>
            <param><ptype>GLuint</ptype> <name>first</name></param>
            <param><ptype>GLsizei</ptype> <name>count</name></param>
            <param len="count">const <ptype>GLuint</ptype> *<name>textures</name></param>
        </command>
         */
    private static Function _glBindImageTextures ;
    public static void glBindImageTextures (int first_ , int count_ , int[] textures_ ) {
        if( _glBindImageTextures == null )
            _glBindImageTextures = loadFunction("glBindImageTextures");
        _glBindImageTextures.invoke(Void.class, new Object[]{first_,count_,textures_ });
    }
    /* <command>
            <proto>void <name>glBindProgramPipeline</name></proto>
            <param><ptype>GLuint</ptype> <name>pipeline</name></param>
        </command>
         */
    private static Function _glBindProgramPipeline ;
    public static void glBindProgramPipeline (int pipeline_ ) {
        if( _glBindProgramPipeline == null )
            _glBindProgramPipeline = loadFunction("glBindProgramPipeline");
        _glBindProgramPipeline.invoke(Void.class, new Object[]{pipeline_ });
    }
    /* <command>
            <proto>void <name>glBindRenderbuffer</name></proto>
            <param group="RenderbufferTarget"><ptype>GLenum</ptype> <name>target</name></param>
            <param><ptype>GLuint</ptype> <name>renderbuffer</name></param>
            <glx opcode="235" type="render" />
        </command>
         */
    private static Function _glBindRenderbuffer ;
    public static void glBindRenderbuffer (int target_ , int renderbuffer_ ) {
        if( _glBindRenderbuffer == null )
            _glBindRenderbuffer = loadFunction("glBindRenderbuffer");
        _glBindRenderbuffer.invoke(Void.class, new Object[]{target_,renderbuffer_ });
    }
    /* <command>
            <proto>void <name>glBindSampler</name></proto>
            <param><ptype>GLuint</ptype> <name>unit</name></param>
            <param><ptype>GLuint</ptype> <name>sampler</name></param>
        </command>
         */
    private static Function _glBindSampler ;
    public static void glBindSampler (int unit_ , int sampler_ ) {
        if( _glBindSampler == null )
            _glBindSampler = loadFunction("glBindSampler");
        _glBindSampler.invoke(Void.class, new Object[]{unit_,sampler_ });
    }
    /* <command>
            <proto>void <name>glBindSamplers</name></proto>
            <param><ptype>GLuint</ptype> <name>first</name></param>
            <param><ptype>GLsizei</ptype> <name>count</name></param>
            <param len="count">const <ptype>GLuint</ptype> *<name>samplers</name></param>
        </command>
         */
    private static Function _glBindSamplers ;
    public static void glBindSamplers (int first_ , int count_ , int[] samplers_ ) {
        if( _glBindSamplers == null )
            _glBindSamplers = loadFunction("glBindSamplers");
        _glBindSamplers.invoke(Void.class, new Object[]{first_,count_,samplers_ });
    }
    /* <command>
            <proto>void <name>glBindTexture</name></proto>
            <param group="TextureTarget"><ptype>GLenum</ptype> <name>target</name></param>
            <param group="Texture"><ptype>GLuint</ptype> <name>texture</name></param>
            <glx opcode="4117" type="render" />
        </command>
         */
    private static Function _glBindTexture ;
    public static void glBindTexture (int target_ , int texture_ ) {
        if( _glBindTexture == null )
            _glBindTexture = loadFunction("glBindTexture");
        _glBindTexture.invoke(Void.class, new Object[]{target_,texture_ });
    }
    /* <command>
            <proto>void <name>glBindTextureUnit</name></proto>
            <param><ptype>GLuint</ptype> <name>unit</name></param>
            <param><ptype>GLuint</ptype> <name>texture</name></param>
        </command>
         */
    private static Function _glBindTextureUnit ;
    public static void glBindTextureUnit (int unit_ , int texture_ ) {
        if( _glBindTextureUnit == null )
            _glBindTextureUnit = loadFunction("glBindTextureUnit");
        _glBindTextureUnit.invoke(Void.class, new Object[]{unit_,texture_ });
    }
    /* <command>
            <proto>void <name>glBindTextures</name></proto>
            <param><ptype>GLuint</ptype> <name>first</name></param>
            <param><ptype>GLsizei</ptype> <name>count</name></param>
            <param len="count">const <ptype>GLuint</ptype> *<name>textures</name></param>
        </command>
         */
    private static Function _glBindTextures ;
    public static void glBindTextures (int first_ , int count_ , int[] textures_ ) {
        if( _glBindTextures == null )
            _glBindTextures = loadFunction("glBindTextures");
        _glBindTextures.invoke(Void.class, new Object[]{first_,count_,textures_ });
    }
    /* <command>
            <proto>void <name>glBindTransformFeedback</name></proto>
            <param><ptype>GLenum</ptype> <name>target</name></param>
            <param><ptype>GLuint</ptype> <name>id</name></param>
        </command>
         */
    private static Function _glBindTransformFeedback ;
    public static void glBindTransformFeedback (int target_ , int id_ ) {
        if( _glBindTransformFeedback == null )
            _glBindTransformFeedback = loadFunction("glBindTransformFeedback");
        _glBindTransformFeedback.invoke(Void.class, new Object[]{target_,id_ });
    }
    /* <command>
            <proto>void <name>glBindVertexArray</name></proto>
            <param><ptype>GLuint</ptype> <name>array</name></param>
            <glx opcode="350" type="render" />
        </command>
         */
    private static Function _glBindVertexArray ;
    public static void glBindVertexArray (int array_ ) {
        if( _glBindVertexArray == null )
            _glBindVertexArray = loadFunction("glBindVertexArray");
        _glBindVertexArray.invoke(Void.class, new Object[]{array_ });
    }
    /* <command>
            <proto>void <name>glBindVertexBuffer</name></proto>
            <param><ptype>GLuint</ptype> <name>bindingindex</name></param>
            <param><ptype>GLuint</ptype> <name>buffer</name></param>
            <param group="BufferOffset"><ptype>GLintptr</ptype> <name>offset</name></param>
            <param><ptype>GLsizei</ptype> <name>stride</name></param>
        </command>
         */
    private static Function _glBindVertexBuffer ;
    public static void glBindVertexBuffer (int bindingindex_ , int buffer_ , long offset_ , int stride_ ) {
        if( _glBindVertexBuffer == null )
            _glBindVertexBuffer = loadFunction("glBindVertexBuffer");
        _glBindVertexBuffer.invoke(Void.class, new Object[]{bindingindex_,buffer_,offset_,stride_ });
    }
    /* <command>
            <proto>void <name>glBindVertexBuffers</name></proto>
            <param><ptype>GLuint</ptype> <name>first</name></param>
            <param><ptype>GLsizei</ptype> <name>count</name></param>
            <param len="count">const <ptype>GLuint</ptype> *<name>buffers</name></param>
            <param len="count">const <ptype>GLintptr</ptype> *<name>offsets</name></param>
            <param len="count">const <ptype>GLsizei</ptype> *<name>strides</name></param>
        </command>
         */
    private static Function _glBindVertexBuffers ;
    public static void glBindVertexBuffers (int first_ , int count_ , int[] buffers_ , long[] offsets_ , int[] strides_ ) {
        if( _glBindVertexBuffers == null )
            _glBindVertexBuffers = loadFunction("glBindVertexBuffers");
        _glBindVertexBuffers.invoke(Void.class, new Object[]{first_,count_,buffers_,offsets_,strides_ });
    }
    /* <command>
            <proto>void <name>glBlendColor</name></proto>
            <param group="ColorF"><ptype>GLfloat</ptype> <name>red</name></param>
            <param group="ColorF"><ptype>GLfloat</ptype> <name>green</name></param>
            <param group="ColorF"><ptype>GLfloat</ptype> <name>blue</name></param>
            <param group="ColorF"><ptype>GLfloat</ptype> <name>alpha</name></param>
            <glx opcode="4096" type="render" />
        </command>
         */
    private static Function _glBlendColor ;
    public static void glBlendColor (float red_ , float green_ , float blue_ , float alpha_ ) {
        if( _glBlendColor == null )
            _glBlendColor = loadFunction("glBlendColor");
        _glBlendColor.invoke(Void.class, new Object[]{red_,green_,blue_,alpha_ });
    }
    /* <command>
            <proto>void <name>glBlendEquation</name></proto>
            <param group="BlendEquationMode"><ptype>GLenum</ptype> <name>mode</name></param>
            <glx opcode="4097" type="render" />
        </command>
         */
    private static Function _glBlendEquation ;
    public static void glBlendEquation (int mode_ ) {
        if( _glBlendEquation == null )
            _glBlendEquation = loadFunction("glBlendEquation");
        _glBlendEquation.invoke(Void.class, new Object[]{mode_ });
    }
    /* <command>
            <proto>void <name>glBlendEquationSeparate</name></proto>
            <param group="BlendEquationModeEXT"><ptype>GLenum</ptype> <name>modeRGB</name></param>
            <param group="BlendEquationModeEXT"><ptype>GLenum</ptype> <name>modeAlpha</name></param>
            <glx opcode="4228" type="render" />
        </command>
         */
    private static Function _glBlendEquationSeparate ;
    public static void glBlendEquationSeparate (int modeRGB_ , int modeAlpha_ ) {
        if( _glBlendEquationSeparate == null )
            _glBlendEquationSeparate = loadFunction("glBlendEquationSeparate");
        _glBlendEquationSeparate.invoke(Void.class, new Object[]{modeRGB_,modeAlpha_ });
    }
    /* <command>
            <proto>void <name>glBlendEquationSeparatei</name></proto>
            <param><ptype>GLuint</ptype> <name>buf</name></param>
            <param><ptype>GLenum</ptype> <name>modeRGB</name></param>
            <param><ptype>GLenum</ptype> <name>modeAlpha</name></param>
        </command>
         */
    private static Function _glBlendEquationSeparatei ;
    public static void glBlendEquationSeparatei (int buf_ , int modeRGB_ , int modeAlpha_ ) {
        if( _glBlendEquationSeparatei == null )
            _glBlendEquationSeparatei = loadFunction("glBlendEquationSeparatei");
        _glBlendEquationSeparatei.invoke(Void.class, new Object[]{buf_,modeRGB_,modeAlpha_ });
    }
    /* <command>
            <proto>void <name>glBlendEquationi</name></proto>
            <param><ptype>GLuint</ptype> <name>buf</name></param>
            <param><ptype>GLenum</ptype> <name>mode</name></param>
        </command>
         */
    private static Function _glBlendEquationi ;
    public static void glBlendEquationi (int buf_ , int mode_ ) {
        if( _glBlendEquationi == null )
            _glBlendEquationi = loadFunction("glBlendEquationi");
        _glBlendEquationi.invoke(Void.class, new Object[]{buf_,mode_ });
    }
    /* <command>
            <proto>void <name>glBlendFunc</name></proto>
            <param group="BlendingFactorSrc"><ptype>GLenum</ptype> <name>sfactor</name></param>
            <param group="BlendingFactorDest"><ptype>GLenum</ptype> <name>dfactor</name></param>
            <glx opcode="160" type="render" />
        </command>
         */
    private static Function _glBlendFunc ;
    public static void glBlendFunc (int sfactor_ , int dfactor_ ) {
        if( _glBlendFunc == null )
            _glBlendFunc = loadFunction("glBlendFunc");
        _glBlendFunc.invoke(Void.class, new Object[]{sfactor_,dfactor_ });
    }
    /* <command>
            <proto>void <name>glBlendFuncSeparate</name></proto>
            <param group="BlendFuncSeparateParameterEXT"><ptype>GLenum</ptype> <name>sfactorRGB</name></param>
            <param group="BlendFuncSeparateParameterEXT"><ptype>GLenum</ptype> <name>dfactorRGB</name></param>
            <param group="BlendFuncSeparateParameterEXT"><ptype>GLenum</ptype> <name>sfactorAlpha</name></param>
            <param group="BlendFuncSeparateParameterEXT"><ptype>GLenum</ptype> <name>dfactorAlpha</name></param>
            <glx opcode="4134" type="render" />
        </command>
         */
    private static Function _glBlendFuncSeparate ;
    public static void glBlendFuncSeparate (int sfactorRGB_ , int dfactorRGB_ , int sfactorAlpha_ , int dfactorAlpha_ ) {
        if( _glBlendFuncSeparate == null )
            _glBlendFuncSeparate = loadFunction("glBlendFuncSeparate");
        _glBlendFuncSeparate.invoke(Void.class, new Object[]{sfactorRGB_,dfactorRGB_,sfactorAlpha_,dfactorAlpha_ });
    }
    /* <command>
            <proto>void <name>glBlendFuncSeparatei</name></proto>
            <param><ptype>GLuint</ptype> <name>buf</name></param>
            <param><ptype>GLenum</ptype> <name>srcRGB</name></param>
            <param><ptype>GLenum</ptype> <name>dstRGB</name></param>
            <param><ptype>GLenum</ptype> <name>srcAlpha</name></param>
            <param><ptype>GLenum</ptype> <name>dstAlpha</name></param>
        </command>
         */
    private static Function _glBlendFuncSeparatei ;
    public static void glBlendFuncSeparatei (int buf_ , int srcRGB_ , int dstRGB_ , int srcAlpha_ , int dstAlpha_ ) {
        if( _glBlendFuncSeparatei == null )
            _glBlendFuncSeparatei = loadFunction("glBlendFuncSeparatei");
        _glBlendFuncSeparatei.invoke(Void.class, new Object[]{buf_,srcRGB_,dstRGB_,srcAlpha_,dstAlpha_ });
    }
    /* <command>
            <proto>void <name>glBlendFunci</name></proto>
            <param><ptype>GLuint</ptype> <name>buf</name></param>
            <param><ptype>GLenum</ptype> <name>src</name></param>
            <param><ptype>GLenum</ptype> <name>dst</name></param>
        </command>
         */
    private static Function _glBlendFunci ;
    public static void glBlendFunci (int buf_ , int src_ , int dst_ ) {
        if( _glBlendFunci == null )
            _glBlendFunci = loadFunction("glBlendFunci");
        _glBlendFunci.invoke(Void.class, new Object[]{buf_,src_,dst_ });
    }
    /* <command>
            <proto>void <name>glBlitFramebuffer</name></proto>
            <param><ptype>GLint</ptype> <name>srcX0</name></param>
            <param><ptype>GLint</ptype> <name>srcY0</name></param>
            <param><ptype>GLint</ptype> <name>srcX1</name></param>
            <param><ptype>GLint</ptype> <name>srcY1</name></param>
            <param><ptype>GLint</ptype> <name>dstX0</name></param>
            <param><ptype>GLint</ptype> <name>dstY0</name></param>
            <param><ptype>GLint</ptype> <name>dstX1</name></param>
            <param><ptype>GLint</ptype> <name>dstY1</name></param>
            <param group="ClearBufferMask"><ptype>GLbitfield</ptype> <name>mask</name></param>
            <param><ptype>GLenum</ptype> <name>filter</name></param>
            <glx opcode="4330" type="render" />
        </command>
         */
    private static Function _glBlitFramebuffer ;
    public static void glBlitFramebuffer (int srcX0_ , int srcY0_ , int srcX1_ , int srcY1_ , int dstX0_ , int dstY0_ , int dstX1_ , int dstY1_ , int mask_ , int filter_ ) {
        if( _glBlitFramebuffer == null )
            _glBlitFramebuffer = loadFunction("glBlitFramebuffer");
        _glBlitFramebuffer.invoke(Void.class, new Object[]{srcX0_,srcY0_,srcX1_,srcY1_,dstX0_,dstY0_,dstX1_,dstY1_,mask_,filter_ });
    }
    /* <command>
            <proto>void <name>glBlitNamedFramebuffer</name></proto>
            <param><ptype>GLuint</ptype> <name>readFramebuffer</name></param>
            <param><ptype>GLuint</ptype> <name>drawFramebuffer</name></param>
            <param><ptype>GLint</ptype> <name>srcX0</name></param>
            <param><ptype>GLint</ptype> <name>srcY0</name></param>
            <param><ptype>GLint</ptype> <name>srcX1</name></param>
            <param><ptype>GLint</ptype> <name>srcY1</name></param>
            <param><ptype>GLint</ptype> <name>dstX0</name></param>
            <param><ptype>GLint</ptype> <name>dstY0</name></param>
            <param><ptype>GLint</ptype> <name>dstX1</name></param>
            <param><ptype>GLint</ptype> <name>dstY1</name></param>
            <param><ptype>GLbitfield</ptype> <name>mask</name></param>
            <param><ptype>GLenum</ptype> <name>filter</name></param>
        </command>
         */
    private static Function _glBlitNamedFramebuffer ;
    public static void glBlitNamedFramebuffer (int readFramebuffer_ , int drawFramebuffer_ , int srcX0_ , int srcY0_ , int srcX1_ , int srcY1_ , int dstX0_ , int dstY0_ , int dstX1_ , int dstY1_ , int mask_ , int filter_ ) {
        if( _glBlitNamedFramebuffer == null )
            _glBlitNamedFramebuffer = loadFunction("glBlitNamedFramebuffer");
        _glBlitNamedFramebuffer.invoke(Void.class, new Object[]{readFramebuffer_,drawFramebuffer_,srcX0_,srcY0_,srcX1_,srcY1_,dstX0_,dstY0_,dstX1_,dstY1_,mask_,filter_ });
    }
    /* <command>
            <proto>void <name>glBufferData</name></proto>
            <param group="BufferTargetARB"><ptype>GLenum</ptype> <name>target</name></param>
            <param group="BufferSize"><ptype>GLsizeiptr</ptype> <name>size</name></param>
            <param len="size">const void *<name>data</name></param>
            <param group="BufferUsageARB"><ptype>GLenum</ptype> <name>usage</name></param>
        </command>
         */
    private static Function _glBufferData ;
    public static void glBufferData (int target_ , long size_ , byte[] data_ , int usage_ ) {
        if( _glBufferData == null )
            _glBufferData = loadFunction("glBufferData");
        _glBufferData.invoke(Void.class, new Object[]{target_,size_,data_,usage_ });
    }
    /* <command>
            <proto>void <name>glBufferStorage</name></proto>
            <param><ptype>GLenum</ptype> <name>target</name></param>
            <param><ptype>GLsizeiptr</ptype> <name>size</name></param>
            <param len="size">const void *<name>data</name></param>
            <param><ptype>GLbitfield</ptype> <name>flags</name></param>
        </command>
         */
    private static Function _glBufferStorage ;
    public static void glBufferStorage (int target_ , long size_ , byte[] data_ , int flags_ ) {
        if( _glBufferStorage == null )
            _glBufferStorage = loadFunction("glBufferStorage");
        _glBufferStorage.invoke(Void.class, new Object[]{target_,size_,data_,flags_ });
    }
    /* <command>
            <proto>void <name>glBufferSubData</name></proto>
            <param group="BufferTargetARB"><ptype>GLenum</ptype> <name>target</name></param>
            <param group="BufferOffset"><ptype>GLintptr</ptype> <name>offset</name></param>
            <param group="BufferSize"><ptype>GLsizeiptr</ptype> <name>size</name></param>
            <param len="size">const void *<name>data</name></param>
        </command>
         */
    private static Function _glBufferSubData ;
    public static void glBufferSubData (int target_ , long offset_ , long size_ , byte[] data_ ) {
        if( _glBufferSubData == null )
            _glBufferSubData = loadFunction("glBufferSubData");
        _glBufferSubData.invoke(Void.class, new Object[]{target_,offset_,size_,data_ });
    }
    /* <command>
            <proto><ptype>GLenum</ptype> <name>glCheckFramebufferStatus</name></proto>
            <param group="FramebufferTarget"><ptype>GLenum</ptype> <name>target</name></param>
            <glx opcode="1427" type="vendor" />
        </command>
         */
    private static Function _glCheckFramebufferStatus ;
    public static int glCheckFramebufferStatus (int target_ ) {
        if( _glCheckFramebufferStatus == null )
            _glCheckFramebufferStatus = loadFunction("glCheckFramebufferStatus");
        int rv= ( Integer )_glCheckFramebufferStatus.invoke(Integer.class, new Object[]{target_ });
        return rv;
    }
    /* <command>
            <proto><ptype>GLenum</ptype> <name>glCheckNamedFramebufferStatus</name></proto>
            <param><ptype>GLuint</ptype> <name>framebuffer</name></param>
            <param><ptype>GLenum</ptype> <name>target</name></param>
        </command>
         */
    private static Function _glCheckNamedFramebufferStatus ;
    public static int glCheckNamedFramebufferStatus (int framebuffer_ , int target_ ) {
        if( _glCheckNamedFramebufferStatus == null )
            _glCheckNamedFramebufferStatus = loadFunction("glCheckNamedFramebufferStatus");
        int rv= ( Integer )_glCheckNamedFramebufferStatus.invoke(Integer.class, new Object[]{framebuffer_,target_ });
        return rv;
    }
    /* <command>
            <proto>void <name>glClampColor</name></proto>
            <param group="ClampColorTargetARB"><ptype>GLenum</ptype> <name>target</name></param>
            <param group="ClampColorModeARB"><ptype>GLenum</ptype> <name>clamp</name></param>
            <glx opcode="234" type="render" />
        </command>
         */
    private static Function _glClampColor ;
    public static void glClampColor (int target_ , int clamp_ ) {
        if( _glClampColor == null )
            _glClampColor = loadFunction("glClampColor");
        _glClampColor.invoke(Void.class, new Object[]{target_,clamp_ });
    }
    /* <command>
            <proto>void <name>glClear</name></proto>
            <param group="ClearBufferMask"><ptype>GLbitfield</ptype> <name>mask</name></param>
            <glx opcode="127" type="render" />
        </command>
         */
    private static Function _glClear ;
    public static void glClear (int mask_ ) {
        if( _glClear == null )
            _glClear = loadFunction("glClear");
        _glClear.invoke(Void.class, new Object[]{mask_ });
    }
    /* <command>
            <proto>void <name>glClearBufferData</name></proto>
            <param><ptype>GLenum</ptype> <name>target</name></param>
            <param><ptype>GLenum</ptype> <name>internalformat</name></param>
            <param><ptype>GLenum</ptype> <name>format</name></param>
            <param><ptype>GLenum</ptype> <name>type</name></param>
            <param len="COMPSIZE(format,type)">const void *<name>data</name></param>
        </command>
         */
    private static Function _glClearBufferData ;
    public static void glClearBufferData (int target_ , int internalformat_ , int format_ , int type_ , byte[] data_ ) {
        if( _glClearBufferData == null )
            _glClearBufferData = loadFunction("glClearBufferData");
        _glClearBufferData.invoke(Void.class, new Object[]{target_,internalformat_,format_,type_,data_ });
    }
    /* <command>
            <proto>void <name>glClearBufferSubData</name></proto>
            <param><ptype>GLenum</ptype> <name>target</name></param>
            <param><ptype>GLenum</ptype> <name>internalformat</name></param>
            <param group="BufferOffset"><ptype>GLintptr</ptype> <name>offset</name></param>
            <param group="BufferSize"><ptype>GLsizeiptr</ptype> <name>size</name></param>
            <param><ptype>GLenum</ptype> <name>format</name></param>
            <param><ptype>GLenum</ptype> <name>type</name></param>
            <param len="COMPSIZE(format,type)">const void *<name>data</name></param>
        </command>
         */
    private static Function _glClearBufferSubData ;
    public static void glClearBufferSubData (int target_ , int internalformat_ , long offset_ , long size_ , int format_ , int type_ , byte[] data_ ) {
        if( _glClearBufferSubData == null )
            _glClearBufferSubData = loadFunction("glClearBufferSubData");
        _glClearBufferSubData.invoke(Void.class, new Object[]{target_,internalformat_,offset_,size_,format_,type_,data_ });
    }
    /* <command>
            <proto>void <name>glClearBufferfi</name></proto>
            <param><ptype>GLenum</ptype> <name>buffer</name></param>
            <param group="DrawBufferName"><ptype>GLint</ptype> <name>drawbuffer</name></param>
            <param><ptype>GLfloat</ptype> <name>depth</name></param>
            <param><ptype>GLint</ptype> <name>stencil</name></param>
        </command>
         */
    private static Function _glClearBufferfi ;
    public static void glClearBufferfi (int buffer_ , int drawbuffer_ , float depth_ , int stencil_ ) {
        if( _glClearBufferfi == null )
            _glClearBufferfi = loadFunction("glClearBufferfi");
        _glClearBufferfi.invoke(Void.class, new Object[]{buffer_,drawbuffer_,depth_,stencil_ });
    }
    /* <command>
            <proto>void <name>glClearBufferfv</name></proto>
            <param><ptype>GLenum</ptype> <name>buffer</name></param>
            <param group="DrawBufferName"><ptype>GLint</ptype> <name>drawbuffer</name></param>
            <param len="COMPSIZE(buffer)">const <ptype>GLfloat</ptype> *<name>value</name></param>
        </command>
         */
    private static Function _glClearBufferfv ;
    public static void glClearBufferfv (int buffer_ , int drawbuffer_ , float[] value_ ) {
        if( _glClearBufferfv == null )
            _glClearBufferfv = loadFunction("glClearBufferfv");
        _glClearBufferfv.invoke(Void.class, new Object[]{buffer_,drawbuffer_,value_ });
    }
    /* <command>
            <proto>void <name>glClearBufferiv</name></proto>
            <param><ptype>GLenum</ptype> <name>buffer</name></param>
            <param group="DrawBufferName"><ptype>GLint</ptype> <name>drawbuffer</name></param>
            <param len="COMPSIZE(buffer)">const <ptype>GLint</ptype> *<name>value</name></param>
        </command>
         */
    private static Function _glClearBufferiv ;
    public static void glClearBufferiv (int buffer_ , int drawbuffer_ , int[] value_ ) {
        if( _glClearBufferiv == null )
            _glClearBufferiv = loadFunction("glClearBufferiv");
        _glClearBufferiv.invoke(Void.class, new Object[]{buffer_,drawbuffer_,value_ });
    }
    /* <command>
            <proto>void <name>glClearBufferuiv</name></proto>
            <param><ptype>GLenum</ptype> <name>buffer</name></param>
            <param group="DrawBufferName"><ptype>GLint</ptype> <name>drawbuffer</name></param>
            <param len="COMPSIZE(buffer)">const <ptype>GLuint</ptype> *<name>value</name></param>
        </command>
         */
    private static Function _glClearBufferuiv ;
    public static void glClearBufferuiv (int buffer_ , int drawbuffer_ , int[] value_ ) {
        if( _glClearBufferuiv == null )
            _glClearBufferuiv = loadFunction("glClearBufferuiv");
        _glClearBufferuiv.invoke(Void.class, new Object[]{buffer_,drawbuffer_,value_ });
    }
    /* <command>
            <proto>void <name>glClearColor</name></proto>
            <param group="ColorF"><ptype>GLfloat</ptype> <name>red</name></param>
            <param group="ColorF"><ptype>GLfloat</ptype> <name>green</name></param>
            <param group="ColorF"><ptype>GLfloat</ptype> <name>blue</name></param>
            <param group="ColorF"><ptype>GLfloat</ptype> <name>alpha</name></param>
            <glx opcode="130" type="render" />
        </command>
         */
    private static Function _glClearColor ;
    public static void glClearColor (float red_ , float green_ , float blue_ , float alpha_ ) {
        if( _glClearColor == null )
            _glClearColor = loadFunction("glClearColor");
        _glClearColor.invoke(Void.class, new Object[]{red_,green_,blue_,alpha_ });
    }
    /* <command>
            <proto>void <name>glClearDepth</name></proto>
            <param><ptype>GLdouble</ptype> <name>depth</name></param>
            <glx opcode="132" type="render" />
        </command>
         */
    private static Function _glClearDepth ;
    public static void glClearDepth (double depth_ ) {
        if( _glClearDepth == null )
            _glClearDepth = loadFunction("glClearDepth");
        _glClearDepth.invoke(Void.class, new Object[]{depth_ });
    }
    /* <command>
            <proto>void <name>glClearDepthf</name></proto>
            <param><ptype>GLfloat</ptype> <name>d</name></param>
        </command>
         */
    private static Function _glClearDepthf ;
    public static void glClearDepthf (float d_ ) {
        if( _glClearDepthf == null )
            _glClearDepthf = loadFunction("glClearDepthf");
        _glClearDepthf.invoke(Void.class, new Object[]{d_ });
    }
    /* <command>
            <proto>void <name>glClearNamedBufferData</name></proto>
            <param><ptype>GLuint</ptype> <name>buffer</name></param>
            <param><ptype>GLenum</ptype> <name>internalformat</name></param>
            <param><ptype>GLenum</ptype> <name>format</name></param>
            <param><ptype>GLenum</ptype> <name>type</name></param>
            <param>const void *<name>data</name></param>
        </command>
         */
    private static Function _glClearNamedBufferData ;
    public static void glClearNamedBufferData (int buffer_ , int internalformat_ , int format_ , int type_ , byte[] data_ ) {
        if( _glClearNamedBufferData == null )
            _glClearNamedBufferData = loadFunction("glClearNamedBufferData");
        _glClearNamedBufferData.invoke(Void.class, new Object[]{buffer_,internalformat_,format_,type_,data_ });
    }
    /* <command>
            <proto>void <name>glClearNamedBufferSubData</name></proto>
            <param><ptype>GLuint</ptype> <name>buffer</name></param>
            <param><ptype>GLenum</ptype> <name>internalformat</name></param>
            <param><ptype>GLintptr</ptype> <name>offset</name></param>
            <param group="BufferSize"><ptype>GLsizeiptr</ptype> <name>size</name></param>
            <param><ptype>GLenum</ptype> <name>format</name></param>
            <param><ptype>GLenum</ptype> <name>type</name></param>
            <param>const void *<name>data</name></param>
        </command>
         */
    private static Function _glClearNamedBufferSubData ;
    public static void glClearNamedBufferSubData (int buffer_ , int internalformat_ , long offset_ , long size_ , int format_ , int type_ , byte[] data_ ) {
        if( _glClearNamedBufferSubData == null )
            _glClearNamedBufferSubData = loadFunction("glClearNamedBufferSubData");
        _glClearNamedBufferSubData.invoke(Void.class, new Object[]{buffer_,internalformat_,offset_,size_,format_,type_,data_ });
    }
    /* <command>
            <proto>void <name>glClearNamedFramebufferfi</name></proto>
            <param><ptype>GLuint</ptype> <name>framebuffer</name></param>
            <param><ptype>GLenum</ptype> <name>buffer</name></param>
            <param><ptype>GLint</ptype> <name>drawbuffer</name></param>
            <param><ptype>GLfloat</ptype> <name>depth</name></param>
            <param><ptype>GLint</ptype> <name>stencil</name></param>
        </command>
         */
    private static Function _glClearNamedFramebufferfi ;
    public static void glClearNamedFramebufferfi (int framebuffer_ , int buffer_ , int drawbuffer_ , float depth_ , int stencil_ ) {
        if( _glClearNamedFramebufferfi == null )
            _glClearNamedFramebufferfi = loadFunction("glClearNamedFramebufferfi");
        _glClearNamedFramebufferfi.invoke(Void.class, new Object[]{framebuffer_,buffer_,drawbuffer_,depth_,stencil_ });
    }
    /* <command>
            <proto>void <name>glClearNamedFramebufferfv</name></proto>
            <param><ptype>GLuint</ptype> <name>framebuffer</name></param>
            <param><ptype>GLenum</ptype> <name>buffer</name></param>
            <param><ptype>GLint</ptype> <name>drawbuffer</name></param>
            <param>const <ptype>GLfloat</ptype> *<name>value</name></param>
        </command>
         */
    private static Function _glClearNamedFramebufferfv ;
    public static void glClearNamedFramebufferfv (int framebuffer_ , int buffer_ , int drawbuffer_ , float[] value_ ) {
        if( _glClearNamedFramebufferfv == null )
            _glClearNamedFramebufferfv = loadFunction("glClearNamedFramebufferfv");
        _glClearNamedFramebufferfv.invoke(Void.class, new Object[]{framebuffer_,buffer_,drawbuffer_,value_ });
    }
    /* <command>
            <proto>void <name>glClearNamedFramebufferiv</name></proto>
            <param><ptype>GLuint</ptype> <name>framebuffer</name></param>
            <param><ptype>GLenum</ptype> <name>buffer</name></param>
            <param><ptype>GLint</ptype> <name>drawbuffer</name></param>
            <param>const <ptype>GLint</ptype> *<name>value</name></param>
        </command>
         */
    private static Function _glClearNamedFramebufferiv ;
    public static void glClearNamedFramebufferiv (int framebuffer_ , int buffer_ , int drawbuffer_ , int[] value_ ) {
        if( _glClearNamedFramebufferiv == null )
            _glClearNamedFramebufferiv = loadFunction("glClearNamedFramebufferiv");
        _glClearNamedFramebufferiv.invoke(Void.class, new Object[]{framebuffer_,buffer_,drawbuffer_,value_ });
    }
    /* <command>
            <proto>void <name>glClearNamedFramebufferuiv</name></proto>
            <param><ptype>GLuint</ptype> <name>framebuffer</name></param>
            <param><ptype>GLenum</ptype> <name>buffer</name></param>
            <param><ptype>GLint</ptype> <name>drawbuffer</name></param>
            <param>const <ptype>GLuint</ptype> *<name>value</name></param>
        </command>
         */
    private static Function _glClearNamedFramebufferuiv ;
    public static void glClearNamedFramebufferuiv (int framebuffer_ , int buffer_ , int drawbuffer_ , int[] value_ ) {
        if( _glClearNamedFramebufferuiv == null )
            _glClearNamedFramebufferuiv = loadFunction("glClearNamedFramebufferuiv");
        _glClearNamedFramebufferuiv.invoke(Void.class, new Object[]{framebuffer_,buffer_,drawbuffer_,value_ });
    }
    /* <command>
            <proto>void <name>glClearStencil</name></proto>
            <param group="StencilValue"><ptype>GLint</ptype> <name>s</name></param>
            <glx opcode="131" type="render" />
        </command>
         */
    private static Function _glClearStencil ;
    public static void glClearStencil (int s_ ) {
        if( _glClearStencil == null )
            _glClearStencil = loadFunction("glClearStencil");
        _glClearStencil.invoke(Void.class, new Object[]{s_ });
    }
    /* <command>
            <proto>void <name>glClearTexImage</name></proto>
            <param><ptype>GLuint</ptype> <name>texture</name></param>
            <param><ptype>GLint</ptype> <name>level</name></param>
            <param><ptype>GLenum</ptype> <name>format</name></param>
            <param><ptype>GLenum</ptype> <name>type</name></param>
            <param len="COMPSIZE(format,type)">const void *<name>data</name></param>
        </command>
         */
    private static Function _glClearTexImage ;
    public static void glClearTexImage (int texture_ , int level_ , int format_ , int type_ , byte[] data_ ) {
        if( _glClearTexImage == null )
            _glClearTexImage = loadFunction("glClearTexImage");
        _glClearTexImage.invoke(Void.class, new Object[]{texture_,level_,format_,type_,data_ });
    }
    /* <command>
            <proto>void <name>glClearTexSubImage</name></proto>
            <param><ptype>GLuint</ptype> <name>texture</name></param>
            <param><ptype>GLint</ptype> <name>level</name></param>
            <param><ptype>GLint</ptype> <name>xoffset</name></param>
            <param><ptype>GLint</ptype> <name>yoffset</name></param>
            <param><ptype>GLint</ptype> <name>zoffset</name></param>
            <param><ptype>GLsizei</ptype> <name>width</name></param>
            <param><ptype>GLsizei</ptype> <name>height</name></param>
            <param><ptype>GLsizei</ptype> <name>depth</name></param>
            <param><ptype>GLenum</ptype> <name>format</name></param>
            <param><ptype>GLenum</ptype> <name>type</name></param>
            <param len="COMPSIZE(format,type)">const void *<name>data</name></param>
        </command>
         */
    private static Function _glClearTexSubImage ;
    public static void glClearTexSubImage (int texture_ , int level_ , int xoffset_ , int yoffset_ , int zoffset_ , int width_ , int height_ , int depth_ , int format_ , int type_ , byte[] data_ ) {
        if( _glClearTexSubImage == null )
            _glClearTexSubImage = loadFunction("glClearTexSubImage");
        _glClearTexSubImage.invoke(Void.class, new Object[]{texture_,level_,xoffset_,yoffset_,zoffset_,width_,height_,depth_,format_,type_,data_ });
    }
    /* <command>
            <proto><ptype>GLenum</ptype> <name>glClientWaitSync</name></proto>
            <param group="sync"><ptype>GLsync</ptype> <name>sync</name></param>
            <param><ptype>GLbitfield</ptype> <name>flags</name></param>
            <param><ptype>GLuint64</ptype> <name>timeout</name></param>
        </command>
         */
    private static Function _glClientWaitSync ;
    public static int glClientWaitSync (Pointer sync_ , int flags_ , long timeout_ ) {
        if( _glClientWaitSync == null )
            _glClientWaitSync = loadFunction("glClientWaitSync");
        int rv= ( Integer )_glClientWaitSync.invoke(Integer.class, new Object[]{sync_,flags_,timeout_ });
        return rv;
    }
    /* <command>
            <proto>void <name>glClipControl</name></proto>
            <param><ptype>GLenum</ptype> <name>origin</name></param>
            <param><ptype>GLenum</ptype> <name>depth</name></param>
        </command>
         */
    private static Function _glClipControl ;
    public static void glClipControl (int origin_ , int depth_ ) {
        if( _glClipControl == null )
            _glClipControl = loadFunction("glClipControl");
        _glClipControl.invoke(Void.class, new Object[]{origin_,depth_ });
    }
    /* <command>
            <proto>void <name>glColorMask</name></proto>
            <param group="Boolean"><ptype>GLboolean</ptype> <name>red</name></param>
            <param group="Boolean"><ptype>GLboolean</ptype> <name>green</name></param>
            <param group="Boolean"><ptype>GLboolean</ptype> <name>blue</name></param>
            <param group="Boolean"><ptype>GLboolean</ptype> <name>alpha</name></param>
            <glx opcode="134" type="render" />
        </command>
         */
    private static Function _glColorMask ;
    public static void glColorMask (boolean red_ , boolean green_ , boolean blue_ , boolean alpha_ ) {
        if( _glColorMask == null )
            _glColorMask = loadFunction("glColorMask");
        _glColorMask.invoke(Void.class, new Object[]{(red_ ? (byte)1:(byte)0),(green_ ? (byte)1:(byte)0),(blue_ ? (byte)1:(byte)0),(alpha_ ? (byte)1:(byte)0) });
    }
    /* <command>
            <proto>void <name>glColorMaski</name></proto>
            <param><ptype>GLuint</ptype> <name>index</name></param>
            <param group="Boolean"><ptype>GLboolean</ptype> <name>r</name></param>
            <param group="Boolean"><ptype>GLboolean</ptype> <name>g</name></param>
            <param group="Boolean"><ptype>GLboolean</ptype> <name>b</name></param>
            <param group="Boolean"><ptype>GLboolean</ptype> <name>a</name></param>
        </command>
         */
    private static Function _glColorMaski ;
    public static void glColorMaski (int index_ , boolean r_ , boolean g_ , boolean b_ , boolean a_ ) {
        if( _glColorMaski == null )
            _glColorMaski = loadFunction("glColorMaski");
        _glColorMaski.invoke(Void.class, new Object[]{index_,(r_ ? (byte)1:(byte)0),(g_ ? (byte)1:(byte)0),(b_ ? (byte)1:(byte)0),(a_ ? (byte)1:(byte)0) });
    }
    /* <command>
            <proto>void <name>glCompileShader</name></proto>
            <param><ptype>GLuint</ptype> <name>shader</name></param>
        </command>
         */
    private static Function _glCompileShader ;
    public static void glCompileShader (int shader_ ) {
        if( _glCompileShader == null )
            _glCompileShader = loadFunction("glCompileShader");
        _glCompileShader.invoke(Void.class, new Object[]{shader_ });
    }
    /* <command>
            <proto>void <name>glCompressedTexImage1D</name></proto>
            <param group="TextureTarget"><ptype>GLenum</ptype> <name>target</name></param>
            <param group="CheckedInt32"><ptype>GLint</ptype> <name>level</name></param>
            <param group="PixelInternalFormat"><ptype>GLenum</ptype> <name>internalformat</name></param>
            <param><ptype>GLsizei</ptype> <name>width</name></param>
            <param group="CheckedInt32"><ptype>GLint</ptype> <name>border</name></param>
            <param><ptype>GLsizei</ptype> <name>imageSize</name></param>
            <param group="CompressedTextureARB" len="imageSize">const void *<name>data</name></param>
            <glx opcode="214" type="render" />
            <glx comment="PBO protocol" name="glCompressedTexImage1DPBO" opcode="314" type="render" />
        </command>
         */
    private static Function _glCompressedTexImage1D ;
    public static void glCompressedTexImage1D (int target_ , int level_ , int internalformat_ , int width_ , int border_ , int imageSize_ , byte[] data_ ) {
        if( _glCompressedTexImage1D == null )
            _glCompressedTexImage1D = loadFunction("glCompressedTexImage1D");
        _glCompressedTexImage1D.invoke(Void.class, new Object[]{target_,level_,internalformat_,width_,border_,imageSize_,data_ });
    }
    /* <command>
            <proto>void <name>glCompressedTexImage2D</name></proto>
            <param group="TextureTarget"><ptype>GLenum</ptype> <name>target</name></param>
            <param group="CheckedInt32"><ptype>GLint</ptype> <name>level</name></param>
            <param group="PixelInternalFormat"><ptype>GLenum</ptype> <name>internalformat</name></param>
            <param><ptype>GLsizei</ptype> <name>width</name></param>
            <param><ptype>GLsizei</ptype> <name>height</name></param>
            <param group="CheckedInt32"><ptype>GLint</ptype> <name>border</name></param>
            <param><ptype>GLsizei</ptype> <name>imageSize</name></param>
            <param group="CompressedTextureARB" len="imageSize">const void *<name>data</name></param>
            <glx opcode="215" type="render" />
            <glx comment="PBO protocol" name="glCompressedTexImage2DPBO" opcode="315" type="render" />
        </command>
         */
    private static Function _glCompressedTexImage2D ;
    public static void glCompressedTexImage2D (int target_ , int level_ , int internalformat_ , int width_ , int height_ , int border_ , int imageSize_ , byte[] data_ ) {
        if( _glCompressedTexImage2D == null )
            _glCompressedTexImage2D = loadFunction("glCompressedTexImage2D");
        _glCompressedTexImage2D.invoke(Void.class, new Object[]{target_,level_,internalformat_,width_,height_,border_,imageSize_,data_ });
    }
    /* <command>
            <proto>void <name>glCompressedTexImage3D</name></proto>
            <param group="TextureTarget"><ptype>GLenum</ptype> <name>target</name></param>
            <param group="CheckedInt32"><ptype>GLint</ptype> <name>level</name></param>
            <param group="PixelInternalFormat"><ptype>GLenum</ptype> <name>internalformat</name></param>
            <param><ptype>GLsizei</ptype> <name>width</name></param>
            <param><ptype>GLsizei</ptype> <name>height</name></param>
            <param><ptype>GLsizei</ptype> <name>depth</name></param>
            <param group="CheckedInt32"><ptype>GLint</ptype> <name>border</name></param>
            <param><ptype>GLsizei</ptype> <name>imageSize</name></param>
            <param group="CompressedTextureARB" len="imageSize">const void *<name>data</name></param>
            <glx opcode="216" type="render" />
            <glx comment="PBO protocol" name="glCompressedTexImage3DPBO" opcode="316" type="render" />
        </command>
         */
    private static Function _glCompressedTexImage3D ;
    public static void glCompressedTexImage3D (int target_ , int level_ , int internalformat_ , int width_ , int height_ , int depth_ , int border_ , int imageSize_ , byte[] data_ ) {
        if( _glCompressedTexImage3D == null )
            _glCompressedTexImage3D = loadFunction("glCompressedTexImage3D");
        _glCompressedTexImage3D.invoke(Void.class, new Object[]{target_,level_,internalformat_,width_,height_,depth_,border_,imageSize_,data_ });
    }
    /* <command>
            <proto>void <name>glCompressedTexSubImage1D</name></proto>
            <param group="TextureTarget"><ptype>GLenum</ptype> <name>target</name></param>
            <param group="CheckedInt32"><ptype>GLint</ptype> <name>level</name></param>
            <param group="CheckedInt32"><ptype>GLint</ptype> <name>xoffset</name></param>
            <param><ptype>GLsizei</ptype> <name>width</name></param>
            <param group="PixelFormat"><ptype>GLenum</ptype> <name>format</name></param>
            <param><ptype>GLsizei</ptype> <name>imageSize</name></param>
            <param group="CompressedTextureARB" len="imageSize">const void *<name>data</name></param>
            <glx opcode="217" type="render" />
            <glx comment="PBO protocol" name="glCompressedTexSubImage1DPBO" opcode="317" type="render" />
        </command>
         */
    private static Function _glCompressedTexSubImage1D ;
    public static void glCompressedTexSubImage1D (int target_ , int level_ , int xoffset_ , int width_ , int format_ , int imageSize_ , byte[] data_ ) {
        if( _glCompressedTexSubImage1D == null )
            _glCompressedTexSubImage1D = loadFunction("glCompressedTexSubImage1D");
        _glCompressedTexSubImage1D.invoke(Void.class, new Object[]{target_,level_,xoffset_,width_,format_,imageSize_,data_ });
    }
    /* <command>
            <proto>void <name>glCompressedTexSubImage2D</name></proto>
            <param group="TextureTarget"><ptype>GLenum</ptype> <name>target</name></param>
            <param group="CheckedInt32"><ptype>GLint</ptype> <name>level</name></param>
            <param group="CheckedInt32"><ptype>GLint</ptype> <name>xoffset</name></param>
            <param group="CheckedInt32"><ptype>GLint</ptype> <name>yoffset</name></param>
            <param><ptype>GLsizei</ptype> <name>width</name></param>
            <param><ptype>GLsizei</ptype> <name>height</name></param>
            <param group="PixelFormat"><ptype>GLenum</ptype> <name>format</name></param>
            <param><ptype>GLsizei</ptype> <name>imageSize</name></param>
            <param group="CompressedTextureARB" len="imageSize">const void *<name>data</name></param>
            <glx opcode="218" type="render" />
            <glx comment="PBO protocol" name="glCompressedTexSubImage2DPBO" opcode="318" type="render" />
        </command>
         */
    private static Function _glCompressedTexSubImage2D ;
    public static void glCompressedTexSubImage2D (int target_ , int level_ , int xoffset_ , int yoffset_ , int width_ , int height_ , int format_ , int imageSize_ , byte[] data_ ) {
        if( _glCompressedTexSubImage2D == null )
            _glCompressedTexSubImage2D = loadFunction("glCompressedTexSubImage2D");
        _glCompressedTexSubImage2D.invoke(Void.class, new Object[]{target_,level_,xoffset_,yoffset_,width_,height_,format_,imageSize_,data_ });
    }
    /* <command>
            <proto>void <name>glCompressedTexSubImage3D</name></proto>
            <param group="TextureTarget"><ptype>GLenum</ptype> <name>target</name></param>
            <param group="CheckedInt32"><ptype>GLint</ptype> <name>level</name></param>
            <param group="CheckedInt32"><ptype>GLint</ptype> <name>xoffset</name></param>
            <param group="CheckedInt32"><ptype>GLint</ptype> <name>yoffset</name></param>
            <param group="CheckedInt32"><ptype>GLint</ptype> <name>zoffset</name></param>
            <param><ptype>GLsizei</ptype> <name>width</name></param>
            <param><ptype>GLsizei</ptype> <name>height</name></param>
            <param><ptype>GLsizei</ptype> <name>depth</name></param>
            <param group="PixelFormat"><ptype>GLenum</ptype> <name>format</name></param>
            <param><ptype>GLsizei</ptype> <name>imageSize</name></param>
            <param group="CompressedTextureARB" len="imageSize">const void *<name>data</name></param>
            <glx opcode="219" type="render" />
            <glx comment="PBO protocol" name="glCompressedTexSubImage3DPBO" opcode="319" type="render" />
        </command>
         */
    private static Function _glCompressedTexSubImage3D ;
    public static void glCompressedTexSubImage3D (int target_ , int level_ , int xoffset_ , int yoffset_ , int zoffset_ , int width_ , int height_ , int depth_ , int format_ , int imageSize_ , byte[] data_ ) {
        if( _glCompressedTexSubImage3D == null )
            _glCompressedTexSubImage3D = loadFunction("glCompressedTexSubImage3D");
        _glCompressedTexSubImage3D.invoke(Void.class, new Object[]{target_,level_,xoffset_,yoffset_,zoffset_,width_,height_,depth_,format_,imageSize_,data_ });
    }
    /* <command>
            <proto>void <name>glCompressedTextureSubImage1D</name></proto>
            <param><ptype>GLuint</ptype> <name>texture</name></param>
            <param><ptype>GLint</ptype> <name>level</name></param>
            <param><ptype>GLint</ptype> <name>xoffset</name></param>
            <param><ptype>GLsizei</ptype> <name>width</name></param>
            <param><ptype>GLenum</ptype> <name>format</name></param>
            <param><ptype>GLsizei</ptype> <name>imageSize</name></param>
            <param>const void *<name>data</name></param>
        </command>
         */
    private static Function _glCompressedTextureSubImage1D ;
    public static void glCompressedTextureSubImage1D (int texture_ , int level_ , int xoffset_ , int width_ , int format_ , int imageSize_ , byte[] data_ ) {
        if( _glCompressedTextureSubImage1D == null )
            _glCompressedTextureSubImage1D = loadFunction("glCompressedTextureSubImage1D");
        _glCompressedTextureSubImage1D.invoke(Void.class, new Object[]{texture_,level_,xoffset_,width_,format_,imageSize_,data_ });
    }
    /* <command>
            <proto>void <name>glCompressedTextureSubImage2D</name></proto>
            <param><ptype>GLuint</ptype> <name>texture</name></param>
            <param><ptype>GLint</ptype> <name>level</name></param>
            <param><ptype>GLint</ptype> <name>xoffset</name></param>
            <param><ptype>GLint</ptype> <name>yoffset</name></param>
            <param><ptype>GLsizei</ptype> <name>width</name></param>
            <param><ptype>GLsizei</ptype> <name>height</name></param>
            <param><ptype>GLenum</ptype> <name>format</name></param>
            <param><ptype>GLsizei</ptype> <name>imageSize</name></param>
            <param>const void *<name>data</name></param>
        </command>
         */
    private static Function _glCompressedTextureSubImage2D ;
    public static void glCompressedTextureSubImage2D (int texture_ , int level_ , int xoffset_ , int yoffset_ , int width_ , int height_ , int format_ , int imageSize_ , byte[] data_ ) {
        if( _glCompressedTextureSubImage2D == null )
            _glCompressedTextureSubImage2D = loadFunction("glCompressedTextureSubImage2D");
        _glCompressedTextureSubImage2D.invoke(Void.class, new Object[]{texture_,level_,xoffset_,yoffset_,width_,height_,format_,imageSize_,data_ });
    }
    /* <command>
            <proto>void <name>glCompressedTextureSubImage3D</name></proto>
            <param><ptype>GLuint</ptype> <name>texture</name></param>
            <param><ptype>GLint</ptype> <name>level</name></param>
            <param><ptype>GLint</ptype> <name>xoffset</name></param>
            <param><ptype>GLint</ptype> <name>yoffset</name></param>
            <param><ptype>GLint</ptype> <name>zoffset</name></param>
            <param><ptype>GLsizei</ptype> <name>width</name></param>
            <param><ptype>GLsizei</ptype> <name>height</name></param>
            <param><ptype>GLsizei</ptype> <name>depth</name></param>
            <param><ptype>GLenum</ptype> <name>format</name></param>
            <param><ptype>GLsizei</ptype> <name>imageSize</name></param>
            <param>const void *<name>data</name></param>
        </command>
         */
    private static Function _glCompressedTextureSubImage3D ;
    public static void glCompressedTextureSubImage3D (int texture_ , int level_ , int xoffset_ , int yoffset_ , int zoffset_ , int width_ , int height_ , int depth_ , int format_ , int imageSize_ , byte[] data_ ) {
        if( _glCompressedTextureSubImage3D == null )
            _glCompressedTextureSubImage3D = loadFunction("glCompressedTextureSubImage3D");
        _glCompressedTextureSubImage3D.invoke(Void.class, new Object[]{texture_,level_,xoffset_,yoffset_,zoffset_,width_,height_,depth_,format_,imageSize_,data_ });
    }
    /* <command>
            <proto>void <name>glCopyBufferSubData</name></proto>
            <param><ptype>GLenum</ptype> <name>readTarget</name></param>
            <param><ptype>GLenum</ptype> <name>writeTarget</name></param>
            <param group="BufferOffset"><ptype>GLintptr</ptype> <name>readOffset</name></param>
            <param group="BufferOffset"><ptype>GLintptr</ptype> <name>writeOffset</name></param>
            <param group="BufferSize"><ptype>GLsizeiptr</ptype> <name>size</name></param>
        </command>
         */
    private static Function _glCopyBufferSubData ;
    public static void glCopyBufferSubData (int readTarget_ , int writeTarget_ , long readOffset_ , long writeOffset_ , long size_ ) {
        if( _glCopyBufferSubData == null )
            _glCopyBufferSubData = loadFunction("glCopyBufferSubData");
        _glCopyBufferSubData.invoke(Void.class, new Object[]{readTarget_,writeTarget_,readOffset_,writeOffset_,size_ });
    }
    /* <command>
            <proto>void <name>glCopyImageSubData</name></proto>
            <param><ptype>GLuint</ptype> <name>srcName</name></param>
            <param><ptype>GLenum</ptype> <name>srcTarget</name></param>
            <param><ptype>GLint</ptype> <name>srcLevel</name></param>
            <param><ptype>GLint</ptype> <name>srcX</name></param>
            <param><ptype>GLint</ptype> <name>srcY</name></param>
            <param><ptype>GLint</ptype> <name>srcZ</name></param>
            <param><ptype>GLuint</ptype> <name>dstName</name></param>
            <param><ptype>GLenum</ptype> <name>dstTarget</name></param>
            <param><ptype>GLint</ptype> <name>dstLevel</name></param>
            <param><ptype>GLint</ptype> <name>dstX</name></param>
            <param><ptype>GLint</ptype> <name>dstY</name></param>
            <param><ptype>GLint</ptype> <name>dstZ</name></param>
            <param><ptype>GLsizei</ptype> <name>srcWidth</name></param>
            <param><ptype>GLsizei</ptype> <name>srcHeight</name></param>
            <param><ptype>GLsizei</ptype> <name>srcDepth</name></param>
        </command>
         */
    private static Function _glCopyImageSubData ;
    public static void glCopyImageSubData (int srcName_ , int srcTarget_ , int srcLevel_ , int srcX_ , int srcY_ , int srcZ_ , int dstName_ , int dstTarget_ , int dstLevel_ , int dstX_ , int dstY_ , int dstZ_ , int srcWidth_ , int srcHeight_ , int srcDepth_ ) {
        if( _glCopyImageSubData == null )
            _glCopyImageSubData = loadFunction("glCopyImageSubData");
        _glCopyImageSubData.invoke(Void.class, new Object[]{srcName_,srcTarget_,srcLevel_,srcX_,srcY_,srcZ_,dstName_,dstTarget_,dstLevel_,dstX_,dstY_,dstZ_,srcWidth_,srcHeight_,srcDepth_ });
    }
    /* <command>
            <proto>void <name>glCopyNamedBufferSubData</name></proto>
            <param><ptype>GLuint</ptype> <name>readBuffer</name></param>
            <param><ptype>GLuint</ptype> <name>writeBuffer</name></param>
            <param><ptype>GLintptr</ptype> <name>readOffset</name></param>
            <param><ptype>GLintptr</ptype> <name>writeOffset</name></param>
            <param group="BufferSize"><ptype>GLsizeiptr</ptype> <name>size</name></param>
        </command>
         */
    private static Function _glCopyNamedBufferSubData ;
    public static void glCopyNamedBufferSubData (int readBuffer_ , int writeBuffer_ , long readOffset_ , long writeOffset_ , long size_ ) {
        if( _glCopyNamedBufferSubData == null )
            _glCopyNamedBufferSubData = loadFunction("glCopyNamedBufferSubData");
        _glCopyNamedBufferSubData.invoke(Void.class, new Object[]{readBuffer_,writeBuffer_,readOffset_,writeOffset_,size_ });
    }
    /* <command>
            <proto>void <name>glCopyTexImage1D</name></proto>
            <param group="TextureTarget"><ptype>GLenum</ptype> <name>target</name></param>
            <param group="CheckedInt32"><ptype>GLint</ptype> <name>level</name></param>
            <param group="PixelInternalFormat"><ptype>GLenum</ptype> <name>internalformat</name></param>
            <param group="WinCoord"><ptype>GLint</ptype> <name>x</name></param>
            <param group="WinCoord"><ptype>GLint</ptype> <name>y</name></param>
            <param><ptype>GLsizei</ptype> <name>width</name></param>
            <param group="CheckedInt32"><ptype>GLint</ptype> <name>border</name></param>
            <glx opcode="4119" type="render" />
        </command>
         */
    private static Function _glCopyTexImage1D ;
    public static void glCopyTexImage1D (int target_ , int level_ , int internalformat_ , int x_ , int y_ , int width_ , int border_ ) {
        if( _glCopyTexImage1D == null )
            _glCopyTexImage1D = loadFunction("glCopyTexImage1D");
        _glCopyTexImage1D.invoke(Void.class, new Object[]{target_,level_,internalformat_,x_,y_,width_,border_ });
    }
    /* <command>
            <proto>void <name>glCopyTexImage2D</name></proto>
            <param group="TextureTarget"><ptype>GLenum</ptype> <name>target</name></param>
            <param group="CheckedInt32"><ptype>GLint</ptype> <name>level</name></param>
            <param group="PixelInternalFormat"><ptype>GLenum</ptype> <name>internalformat</name></param>
            <param group="WinCoord"><ptype>GLint</ptype> <name>x</name></param>
            <param group="WinCoord"><ptype>GLint</ptype> <name>y</name></param>
            <param><ptype>GLsizei</ptype> <name>width</name></param>
            <param><ptype>GLsizei</ptype> <name>height</name></param>
            <param group="CheckedInt32"><ptype>GLint</ptype> <name>border</name></param>
            <glx opcode="4120" type="render" />
        </command>
         */
    private static Function _glCopyTexImage2D ;
    public static void glCopyTexImage2D (int target_ , int level_ , int internalformat_ , int x_ , int y_ , int width_ , int height_ , int border_ ) {
        if( _glCopyTexImage2D == null )
            _glCopyTexImage2D = loadFunction("glCopyTexImage2D");
        _glCopyTexImage2D.invoke(Void.class, new Object[]{target_,level_,internalformat_,x_,y_,width_,height_,border_ });
    }
    /* <command>
            <proto>void <name>glCopyTexSubImage1D</name></proto>
            <param group="TextureTarget"><ptype>GLenum</ptype> <name>target</name></param>
            <param group="CheckedInt32"><ptype>GLint</ptype> <name>level</name></param>
            <param group="CheckedInt32"><ptype>GLint</ptype> <name>xoffset</name></param>
            <param group="WinCoord"><ptype>GLint</ptype> <name>x</name></param>
            <param group="WinCoord"><ptype>GLint</ptype> <name>y</name></param>
            <param><ptype>GLsizei</ptype> <name>width</name></param>
            <glx opcode="4121" type="render" />
        </command>
         */
    private static Function _glCopyTexSubImage1D ;
    public static void glCopyTexSubImage1D (int target_ , int level_ , int xoffset_ , int x_ , int y_ , int width_ ) {
        if( _glCopyTexSubImage1D == null )
            _glCopyTexSubImage1D = loadFunction("glCopyTexSubImage1D");
        _glCopyTexSubImage1D.invoke(Void.class, new Object[]{target_,level_,xoffset_,x_,y_,width_ });
    }
    /* <command>
            <proto>void <name>glCopyTexSubImage2D</name></proto>
            <param group="TextureTarget"><ptype>GLenum</ptype> <name>target</name></param>
            <param group="CheckedInt32"><ptype>GLint</ptype> <name>level</name></param>
            <param group="CheckedInt32"><ptype>GLint</ptype> <name>xoffset</name></param>
            <param group="CheckedInt32"><ptype>GLint</ptype> <name>yoffset</name></param>
            <param group="WinCoord"><ptype>GLint</ptype> <name>x</name></param>
            <param group="WinCoord"><ptype>GLint</ptype> <name>y</name></param>
            <param><ptype>GLsizei</ptype> <name>width</name></param>
            <param><ptype>GLsizei</ptype> <name>height</name></param>
            <glx opcode="4122" type="render" />
        </command>
         */
    private static Function _glCopyTexSubImage2D ;
    public static void glCopyTexSubImage2D (int target_ , int level_ , int xoffset_ , int yoffset_ , int x_ , int y_ , int width_ , int height_ ) {
        if( _glCopyTexSubImage2D == null )
            _glCopyTexSubImage2D = loadFunction("glCopyTexSubImage2D");
        _glCopyTexSubImage2D.invoke(Void.class, new Object[]{target_,level_,xoffset_,yoffset_,x_,y_,width_,height_ });
    }
    /* <command>
            <proto>void <name>glCopyTexSubImage3D</name></proto>
            <param group="TextureTarget"><ptype>GLenum</ptype> <name>target</name></param>
            <param group="CheckedInt32"><ptype>GLint</ptype> <name>level</name></param>
            <param group="CheckedInt32"><ptype>GLint</ptype> <name>xoffset</name></param>
            <param group="CheckedInt32"><ptype>GLint</ptype> <name>yoffset</name></param>
            <param group="CheckedInt32"><ptype>GLint</ptype> <name>zoffset</name></param>
            <param group="WinCoord"><ptype>GLint</ptype> <name>x</name></param>
            <param group="WinCoord"><ptype>GLint</ptype> <name>y</name></param>
            <param><ptype>GLsizei</ptype> <name>width</name></param>
            <param><ptype>GLsizei</ptype> <name>height</name></param>
            <glx opcode="4123" type="render" />
        </command>
         */
    private static Function _glCopyTexSubImage3D ;
    public static void glCopyTexSubImage3D (int target_ , int level_ , int xoffset_ , int yoffset_ , int zoffset_ , int x_ , int y_ , int width_ , int height_ ) {
        if( _glCopyTexSubImage3D == null )
            _glCopyTexSubImage3D = loadFunction("glCopyTexSubImage3D");
        _glCopyTexSubImage3D.invoke(Void.class, new Object[]{target_,level_,xoffset_,yoffset_,zoffset_,x_,y_,width_,height_ });
    }
    /* <command>
            <proto>void <name>glCopyTextureSubImage1D</name></proto>
            <param><ptype>GLuint</ptype> <name>texture</name></param>
            <param><ptype>GLint</ptype> <name>level</name></param>
            <param><ptype>GLint</ptype> <name>xoffset</name></param>
            <param><ptype>GLint</ptype> <name>x</name></param>
            <param><ptype>GLint</ptype> <name>y</name></param>
            <param><ptype>GLsizei</ptype> <name>width</name></param>
        </command>
         */
    private static Function _glCopyTextureSubImage1D ;
    public static void glCopyTextureSubImage1D (int texture_ , int level_ , int xoffset_ , int x_ , int y_ , int width_ ) {
        if( _glCopyTextureSubImage1D == null )
            _glCopyTextureSubImage1D = loadFunction("glCopyTextureSubImage1D");
        _glCopyTextureSubImage1D.invoke(Void.class, new Object[]{texture_,level_,xoffset_,x_,y_,width_ });
    }
    /* <command>
            <proto>void <name>glCopyTextureSubImage2D</name></proto>
            <param><ptype>GLuint</ptype> <name>texture</name></param>
            <param><ptype>GLint</ptype> <name>level</name></param>
            <param><ptype>GLint</ptype> <name>xoffset</name></param>
            <param><ptype>GLint</ptype> <name>yoffset</name></param>
            <param><ptype>GLint</ptype> <name>x</name></param>
            <param><ptype>GLint</ptype> <name>y</name></param>
            <param><ptype>GLsizei</ptype> <name>width</name></param>
            <param><ptype>GLsizei</ptype> <name>height</name></param>
        </command>
         */
    private static Function _glCopyTextureSubImage2D ;
    public static void glCopyTextureSubImage2D (int texture_ , int level_ , int xoffset_ , int yoffset_ , int x_ , int y_ , int width_ , int height_ ) {
        if( _glCopyTextureSubImage2D == null )
            _glCopyTextureSubImage2D = loadFunction("glCopyTextureSubImage2D");
        _glCopyTextureSubImage2D.invoke(Void.class, new Object[]{texture_,level_,xoffset_,yoffset_,x_,y_,width_,height_ });
    }
    /* <command>
            <proto>void <name>glCopyTextureSubImage3D</name></proto>
            <param><ptype>GLuint</ptype> <name>texture</name></param>
            <param><ptype>GLint</ptype> <name>level</name></param>
            <param><ptype>GLint</ptype> <name>xoffset</name></param>
            <param><ptype>GLint</ptype> <name>yoffset</name></param>
            <param><ptype>GLint</ptype> <name>zoffset</name></param>
            <param><ptype>GLint</ptype> <name>x</name></param>
            <param><ptype>GLint</ptype> <name>y</name></param>
            <param><ptype>GLsizei</ptype> <name>width</name></param>
            <param><ptype>GLsizei</ptype> <name>height</name></param>
        </command>
         */
    private static Function _glCopyTextureSubImage3D ;
    public static void glCopyTextureSubImage3D (int texture_ , int level_ , int xoffset_ , int yoffset_ , int zoffset_ , int x_ , int y_ , int width_ , int height_ ) {
        if( _glCopyTextureSubImage3D == null )
            _glCopyTextureSubImage3D = loadFunction("glCopyTextureSubImage3D");
        _glCopyTextureSubImage3D.invoke(Void.class, new Object[]{texture_,level_,xoffset_,yoffset_,zoffset_,x_,y_,width_,height_ });
    }
    /* <command>
            <proto>void <name>glCreateBuffers</name></proto>
            <param><ptype>GLsizei</ptype> <name>n</name></param>
            <param><ptype>GLuint</ptype> *<name>buffers</name></param>
        </command>
         */
    private static Function _glCreateBuffers ;
    public static void glCreateBuffers (int n_ , int[] buffers_ ) {
        if( _glCreateBuffers == null )
            _glCreateBuffers = loadFunction("glCreateBuffers");
        _glCreateBuffers.invoke(Void.class, new Object[]{n_,buffers_ });
    }
    /* <command>
            <proto>void <name>glCreateFramebuffers</name></proto>
            <param><ptype>GLsizei</ptype> <name>n</name></param>
            <param><ptype>GLuint</ptype> *<name>framebuffers</name></param>
        </command>
         */
    private static Function _glCreateFramebuffers ;
    public static void glCreateFramebuffers (int n_ , int[] framebuffers_ ) {
        if( _glCreateFramebuffers == null )
            _glCreateFramebuffers = loadFunction("glCreateFramebuffers");
        _glCreateFramebuffers.invoke(Void.class, new Object[]{n_,framebuffers_ });
    }
    /* <command>
            <proto><ptype>GLuint</ptype> <name>glCreateProgram</name></proto>
        </command>
         */
    private static Function _glCreateProgram ;
    public static int glCreateProgram () {
        if( _glCreateProgram == null )
            _glCreateProgram = loadFunction("glCreateProgram");
        int rv= ( Integer )_glCreateProgram.invoke(Integer.class, new Object[]{ });
        return rv;
    }
    /* <command>
            <proto>void <name>glCreateProgramPipelines</name></proto>
            <param><ptype>GLsizei</ptype> <name>n</name></param>
            <param><ptype>GLuint</ptype> *<name>pipelines</name></param>
        </command>
         */
    private static Function _glCreateProgramPipelines ;
    public static void glCreateProgramPipelines (int n_ , int[] pipelines_ ) {
        if( _glCreateProgramPipelines == null )
            _glCreateProgramPipelines = loadFunction("glCreateProgramPipelines");
        _glCreateProgramPipelines.invoke(Void.class, new Object[]{n_,pipelines_ });
    }
    /* <command>
            <proto>void <name>glCreateQueries</name></proto>
            <param><ptype>GLenum</ptype> <name>target</name></param>
            <param><ptype>GLsizei</ptype> <name>n</name></param>
            <param><ptype>GLuint</ptype> *<name>ids</name></param>
        </command>
         */
    private static Function _glCreateQueries ;
    public static void glCreateQueries (int target_ , int n_ , int[] ids_ ) {
        if( _glCreateQueries == null )
            _glCreateQueries = loadFunction("glCreateQueries");
        _glCreateQueries.invoke(Void.class, new Object[]{target_,n_,ids_ });
    }
    /* <command>
            <proto>void <name>glCreateRenderbuffers</name></proto>
            <param><ptype>GLsizei</ptype> <name>n</name></param>
            <param><ptype>GLuint</ptype> *<name>renderbuffers</name></param>
        </command>
         */
    private static Function _glCreateRenderbuffers ;
    public static void glCreateRenderbuffers (int n_ , int[] renderbuffers_ ) {
        if( _glCreateRenderbuffers == null )
            _glCreateRenderbuffers = loadFunction("glCreateRenderbuffers");
        _glCreateRenderbuffers.invoke(Void.class, new Object[]{n_,renderbuffers_ });
    }
    /* <command>
            <proto>void <name>glCreateSamplers</name></proto>
            <param><ptype>GLsizei</ptype> <name>n</name></param>
            <param><ptype>GLuint</ptype> *<name>samplers</name></param>
        </command>
         */
    private static Function _glCreateSamplers ;
    public static void glCreateSamplers (int n_ , int[] samplers_ ) {
        if( _glCreateSamplers == null )
            _glCreateSamplers = loadFunction("glCreateSamplers");
        _glCreateSamplers.invoke(Void.class, new Object[]{n_,samplers_ });
    }
    /* <command>
            <proto><ptype>GLuint</ptype> <name>glCreateShader</name></proto>
            <param><ptype>GLenum</ptype> <name>type</name></param>
        </command>
         */
    private static Function _glCreateShader ;
    public static int glCreateShader (int type_ ) {
        if( _glCreateShader == null )
            _glCreateShader = loadFunction("glCreateShader");
        int rv= ( Integer )_glCreateShader.invoke(Integer.class, new Object[]{type_ });
        return rv;
    }
    /* <command>
            <proto><ptype>GLuint</ptype> <name>glCreateShaderProgramv</name></proto>
            <param><ptype>GLenum</ptype> <name>type</name></param>
            <param><ptype>GLsizei</ptype> <name>count</name></param>
            <param len="count">const <ptype>GLchar</ptype> *const*<name>strings</name></param>
        </command>
         */
    private static Function _glCreateShaderProgramv ;
    public static int glCreateShaderProgramv (int type_ , int count_ , byte[] strings_ ) {
        if( _glCreateShaderProgramv == null )
            _glCreateShaderProgramv = loadFunction("glCreateShaderProgramv");
        int rv= ( Integer )_glCreateShaderProgramv.invoke(Integer.class, new Object[]{type_,count_,strings_ });
        return rv;
    }
    /* <command>
            <proto>void <name>glCreateTextures</name></proto>
            <param><ptype>GLenum</ptype> <name>target</name></param>
            <param><ptype>GLsizei</ptype> <name>n</name></param>
            <param><ptype>GLuint</ptype> *<name>textures</name></param>
        </command>
         */
    private static Function _glCreateTextures ;
    public static void glCreateTextures (int target_ , int n_ , int[] textures_ ) {
        if( _glCreateTextures == null )
            _glCreateTextures = loadFunction("glCreateTextures");
        _glCreateTextures.invoke(Void.class, new Object[]{target_,n_,textures_ });
    }
    /* <command>
            <proto>void <name>glCreateTransformFeedbacks</name></proto>
            <param><ptype>GLsizei</ptype> <name>n</name></param>
            <param><ptype>GLuint</ptype> *<name>ids</name></param>
        </command>
         */
    private static Function _glCreateTransformFeedbacks ;
    public static void glCreateTransformFeedbacks (int n_ , int[] ids_ ) {
        if( _glCreateTransformFeedbacks == null )
            _glCreateTransformFeedbacks = loadFunction("glCreateTransformFeedbacks");
        _glCreateTransformFeedbacks.invoke(Void.class, new Object[]{n_,ids_ });
    }
    /* <command>
            <proto>void <name>glCreateVertexArrays</name></proto>
            <param><ptype>GLsizei</ptype> <name>n</name></param>
            <param><ptype>GLuint</ptype> *<name>arrays</name></param>
        </command>
         */
    private static Function _glCreateVertexArrays ;
    public static void glCreateVertexArrays (int n_ , int[] arrays_ ) {
        if( _glCreateVertexArrays == null )
            _glCreateVertexArrays = loadFunction("glCreateVertexArrays");
        _glCreateVertexArrays.invoke(Void.class, new Object[]{n_,arrays_ });
    }
    /* <command>
            <proto>void <name>glCullFace</name></proto>
            <param group="CullFaceMode"><ptype>GLenum</ptype> <name>mode</name></param>
            <glx opcode="79" type="render" />
        </command>
         */
    private static Function _glCullFace ;
    public static void glCullFace (int mode_ ) {
        if( _glCullFace == null )
            _glCullFace = loadFunction("glCullFace");
        _glCullFace.invoke(Void.class, new Object[]{mode_ });
    }
    /* <command>
            <proto>void <name>glDebugMessageControl</name></proto>
            <param><ptype>GLenum</ptype> <name>source</name></param>
            <param><ptype>GLenum</ptype> <name>type</name></param>
            <param><ptype>GLenum</ptype> <name>severity</name></param>
            <param><ptype>GLsizei</ptype> <name>count</name></param>
            <param len="count">const <ptype>GLuint</ptype> *<name>ids</name></param>
            <param group="Boolean"><ptype>GLboolean</ptype> <name>enabled</name></param>
        </command>
         */
    private static Function _glDebugMessageControl ;
    public static void glDebugMessageControl (int source_ , int type_ , int severity_ , int count_ , int[] ids_ , boolean enabled_ ) {
        if( _glDebugMessageControl == null )
            _glDebugMessageControl = loadFunction("glDebugMessageControl");
        _glDebugMessageControl.invoke(Void.class, new Object[]{source_,type_,severity_,count_,ids_,(enabled_ ? (byte)1:(byte)0) });
    }
    /* <command>
            <proto>void <name>glDebugMessageInsert</name></proto>
            <param><ptype>GLenum</ptype> <name>source</name></param>
            <param><ptype>GLenum</ptype> <name>type</name></param>
            <param><ptype>GLuint</ptype> <name>id</name></param>
            <param><ptype>GLenum</ptype> <name>severity</name></param>
            <param><ptype>GLsizei</ptype> <name>length</name></param>
            <param len="COMPSIZE(buf,length)">const <ptype>GLchar</ptype> *<name>buf</name></param>
        </command>
         */
    private static Function _glDebugMessageInsert ;
    public static void glDebugMessageInsert (int source_ , int type_ , int id_ , int severity_ , int length_ , String buf_ ) {
        if( _glDebugMessageInsert == null )
            _glDebugMessageInsert = loadFunction("glDebugMessageInsert");
        _glDebugMessageInsert.invoke(Void.class, new Object[]{source_,type_,id_,severity_,length_,buf_ });
    }
    /* <command>
            <proto>void <name>glDeleteBuffers</name></proto>
            <param><ptype>GLsizei</ptype> <name>n</name></param>
            <param len="n">const <ptype>GLuint</ptype> *<name>buffers</name></param>
        </command>
         */
    private static Function _glDeleteBuffers ;
    public static void glDeleteBuffers (int n_ , int[] buffers_ ) {
        if( _glDeleteBuffers == null )
            _glDeleteBuffers = loadFunction("glDeleteBuffers");
        _glDeleteBuffers.invoke(Void.class, new Object[]{n_,buffers_ });
    }
    /* <command>
            <proto>void <name>glDeleteFramebuffers</name></proto>
            <param><ptype>GLsizei</ptype> <name>n</name></param>
            <param len="n">const <ptype>GLuint</ptype> *<name>framebuffers</name></param>
            <glx opcode="4320" type="render" />
        </command>
         */
    private static Function _glDeleteFramebuffers ;
    public static void glDeleteFramebuffers (int n_ , int[] framebuffers_ ) {
        if( _glDeleteFramebuffers == null )
            _glDeleteFramebuffers = loadFunction("glDeleteFramebuffers");
        _glDeleteFramebuffers.invoke(Void.class, new Object[]{n_,framebuffers_ });
    }
    /* <command>
            <proto>void <name>glDeleteProgram</name></proto>
            <param><ptype>GLuint</ptype> <name>program</name></param>
            <glx opcode="202" type="single" />
        </command>
         */
    private static Function _glDeleteProgram ;
    public static void glDeleteProgram (int program_ ) {
        if( _glDeleteProgram == null )
            _glDeleteProgram = loadFunction("glDeleteProgram");
        _glDeleteProgram.invoke(Void.class, new Object[]{program_ });
    }
    /* <command>
            <proto>void <name>glDeleteProgramPipelines</name></proto>
            <param><ptype>GLsizei</ptype> <name>n</name></param>
            <param len="n">const <ptype>GLuint</ptype> *<name>pipelines</name></param>
        </command>
         */
    private static Function _glDeleteProgramPipelines ;
    public static void glDeleteProgramPipelines (int n_ , int[] pipelines_ ) {
        if( _glDeleteProgramPipelines == null )
            _glDeleteProgramPipelines = loadFunction("glDeleteProgramPipelines");
        _glDeleteProgramPipelines.invoke(Void.class, new Object[]{n_,pipelines_ });
    }
    /* <command>
            <proto>void <name>glDeleteQueries</name></proto>
            <param><ptype>GLsizei</ptype> <name>n</name></param>
            <param len="n">const <ptype>GLuint</ptype> *<name>ids</name></param>
            <glx opcode="161" type="single" />
        </command>
         */
    private static Function _glDeleteQueries ;
    public static void glDeleteQueries (int n_ , int[] ids_ ) {
        if( _glDeleteQueries == null )
            _glDeleteQueries = loadFunction("glDeleteQueries");
        _glDeleteQueries.invoke(Void.class, new Object[]{n_,ids_ });
    }
    /* <command>
            <proto>void <name>glDeleteRenderbuffers</name></proto>
            <param><ptype>GLsizei</ptype> <name>n</name></param>
            <param len="n">const <ptype>GLuint</ptype> *<name>renderbuffers</name></param>
            <glx opcode="4317" type="render" />
        </command>
         */
    private static Function _glDeleteRenderbuffers ;
    public static void glDeleteRenderbuffers (int n_ , int[] renderbuffers_ ) {
        if( _glDeleteRenderbuffers == null )
            _glDeleteRenderbuffers = loadFunction("glDeleteRenderbuffers");
        _glDeleteRenderbuffers.invoke(Void.class, new Object[]{n_,renderbuffers_ });
    }
    /* <command>
            <proto>void <name>glDeleteSamplers</name></proto>
            <param><ptype>GLsizei</ptype> <name>count</name></param>
            <param len="count">const <ptype>GLuint</ptype> *<name>samplers</name></param>
        </command>
         */
    private static Function _glDeleteSamplers ;
    public static void glDeleteSamplers (int count_ , int[] samplers_ ) {
        if( _glDeleteSamplers == null )
            _glDeleteSamplers = loadFunction("glDeleteSamplers");
        _glDeleteSamplers.invoke(Void.class, new Object[]{count_,samplers_ });
    }
    /* <command>
            <proto>void <name>glDeleteShader</name></proto>
            <param><ptype>GLuint</ptype> <name>shader</name></param>
            <glx opcode="195" type="single" />
        </command>
         */
    private static Function _glDeleteShader ;
    public static void glDeleteShader (int shader_ ) {
        if( _glDeleteShader == null )
            _glDeleteShader = loadFunction("glDeleteShader");
        _glDeleteShader.invoke(Void.class, new Object[]{shader_ });
    }
    /* <command>
            <proto>void <name>glDeleteSync</name></proto>
            <param group="sync"><ptype>GLsync</ptype> <name>sync</name></param>
        </command>
         */
    private static Function _glDeleteSync ;
    public static void glDeleteSync (Pointer sync_ ) {
        if( _glDeleteSync == null )
            _glDeleteSync = loadFunction("glDeleteSync");
        _glDeleteSync.invoke(Void.class, new Object[]{sync_ });
    }
    /* <command>
            <proto>void <name>glDeleteTextures</name></proto>
            <param><ptype>GLsizei</ptype> <name>n</name></param>
            <param group="Texture" len="n">const <ptype>GLuint</ptype> *<name>textures</name></param>
            <glx opcode="144" type="single" />
        </command>
         */
    private static Function _glDeleteTextures ;
    public static void glDeleteTextures (int n_ , int[] textures_ ) {
        if( _glDeleteTextures == null )
            _glDeleteTextures = loadFunction("glDeleteTextures");
        _glDeleteTextures.invoke(Void.class, new Object[]{n_,textures_ });
    }
    /* <command>
            <proto>void <name>glDeleteTransformFeedbacks</name></proto>
            <param><ptype>GLsizei</ptype> <name>n</name></param>
            <param len="n">const <ptype>GLuint</ptype> *<name>ids</name></param>
        </command>
         */
    private static Function _glDeleteTransformFeedbacks ;
    public static void glDeleteTransformFeedbacks (int n_ , int[] ids_ ) {
        if( _glDeleteTransformFeedbacks == null )
            _glDeleteTransformFeedbacks = loadFunction("glDeleteTransformFeedbacks");
        _glDeleteTransformFeedbacks.invoke(Void.class, new Object[]{n_,ids_ });
    }
    /* <command>
            <proto>void <name>glDeleteVertexArrays</name></proto>
            <param><ptype>GLsizei</ptype> <name>n</name></param>
            <param len="n">const <ptype>GLuint</ptype> *<name>arrays</name></param>
            <glx opcode="351" type="render" />
        </command>
         */
    private static Function _glDeleteVertexArrays ;
    public static void glDeleteVertexArrays (int n_ , int[] arrays_ ) {
        if( _glDeleteVertexArrays == null )
            _glDeleteVertexArrays = loadFunction("glDeleteVertexArrays");
        _glDeleteVertexArrays.invoke(Void.class, new Object[]{n_,arrays_ });
    }
    /* <command>
            <proto>void <name>glDepthFunc</name></proto>
            <param group="DepthFunction"><ptype>GLenum</ptype> <name>func</name></param>
            <glx opcode="164" type="render" />
        </command>
         */
    private static Function _glDepthFunc ;
    public static void glDepthFunc (int func_ ) {
        if( _glDepthFunc == null )
            _glDepthFunc = loadFunction("glDepthFunc");
        _glDepthFunc.invoke(Void.class, new Object[]{func_ });
    }
    /* <command>
            <proto>void <name>glDepthMask</name></proto>
            <param group="Boolean"><ptype>GLboolean</ptype> <name>flag</name></param>
            <glx opcode="135" type="render" />
        </command>
         */
    private static Function _glDepthMask ;
    public static void glDepthMask (boolean flag_ ) {
        if( _glDepthMask == null )
            _glDepthMask = loadFunction("glDepthMask");
        _glDepthMask.invoke(Void.class, new Object[]{(flag_ ? (byte)1:(byte)0) });
    }
    /* <command>
            <proto>void <name>glDepthRange</name></proto>
            <param><ptype>GLdouble</ptype> <name>near</name></param>
            <param><ptype>GLdouble</ptype> <name>far</name></param>
            <glx opcode="174" type="render" />
        </command>
         */
    private static Function _glDepthRange ;
    public static void glDepthRange (double near_ , double far_ ) {
        if( _glDepthRange == null )
            _glDepthRange = loadFunction("glDepthRange");
        _glDepthRange.invoke(Void.class, new Object[]{near_,far_ });
    }
    /* <command>
            <proto>void <name>glDepthRangeArrayv</name></proto>
            <param><ptype>GLuint</ptype> <name>first</name></param>
            <param><ptype>GLsizei</ptype> <name>count</name></param>
            <param len="COMPSIZE(count)">const <ptype>GLdouble</ptype> *<name>v</name></param>
        </command>
         */
    private static Function _glDepthRangeArrayv ;
    public static void glDepthRangeArrayv (int first_ , int count_ , double[] v_ ) {
        if( _glDepthRangeArrayv == null )
            _glDepthRangeArrayv = loadFunction("glDepthRangeArrayv");
        _glDepthRangeArrayv.invoke(Void.class, new Object[]{first_,count_,v_ });
    }
    /* <command>
            <proto>void <name>glDepthRangeIndexed</name></proto>
            <param><ptype>GLuint</ptype> <name>index</name></param>
            <param><ptype>GLdouble</ptype> <name>n</name></param>
            <param><ptype>GLdouble</ptype> <name>f</name></param>
        </command>
         */
    private static Function _glDepthRangeIndexed ;
    public static void glDepthRangeIndexed (int index_ , double n_ , double f_ ) {
        if( _glDepthRangeIndexed == null )
            _glDepthRangeIndexed = loadFunction("glDepthRangeIndexed");
        _glDepthRangeIndexed.invoke(Void.class, new Object[]{index_,n_,f_ });
    }
    /* <command>
            <proto>void <name>glDepthRangef</name></proto>
            <param><ptype>GLfloat</ptype> <name>n</name></param>
            <param><ptype>GLfloat</ptype> <name>f</name></param>
        </command>
         */
    private static Function _glDepthRangef ;
    public static void glDepthRangef (float n_ , float f_ ) {
        if( _glDepthRangef == null )
            _glDepthRangef = loadFunction("glDepthRangef");
        _glDepthRangef.invoke(Void.class, new Object[]{n_,f_ });
    }
    /* <command>
            <proto>void <name>glDetachShader</name></proto>
            <param><ptype>GLuint</ptype> <name>program</name></param>
            <param><ptype>GLuint</ptype> <name>shader</name></param>
        </command>
         */
    private static Function _glDetachShader ;
    public static void glDetachShader (int program_ , int shader_ ) {
        if( _glDetachShader == null )
            _glDetachShader = loadFunction("glDetachShader");
        _glDetachShader.invoke(Void.class, new Object[]{program_,shader_ });
    }
    /* <command>
            <proto>void <name>glDisable</name></proto>
            <param group="EnableCap"><ptype>GLenum</ptype> <name>cap</name></param>
            <glx opcode="138" type="render" />
        </command>
         */
    private static Function _glDisable ;
    public static void glDisable (int cap_ ) {
        if( _glDisable == null )
            _glDisable = loadFunction("glDisable");
        _glDisable.invoke(Void.class, new Object[]{cap_ });
    }
    /* <command>
            <proto>void <name>glDisableVertexArrayAttrib</name></proto>
            <param><ptype>GLuint</ptype> <name>vaobj</name></param>
            <param><ptype>GLuint</ptype> <name>index</name></param>
        </command>
         */
    private static Function _glDisableVertexArrayAttrib ;
    public static void glDisableVertexArrayAttrib (int vaobj_ , int index_ ) {
        if( _glDisableVertexArrayAttrib == null )
            _glDisableVertexArrayAttrib = loadFunction("glDisableVertexArrayAttrib");
        _glDisableVertexArrayAttrib.invoke(Void.class, new Object[]{vaobj_,index_ });
    }
    /* <command>
            <proto>void <name>glDisableVertexAttribArray</name></proto>
            <param><ptype>GLuint</ptype> <name>index</name></param>
        </command>
         */
    private static Function _glDisableVertexAttribArray ;
    public static void glDisableVertexAttribArray (int index_ ) {
        if( _glDisableVertexAttribArray == null )
            _glDisableVertexAttribArray = loadFunction("glDisableVertexAttribArray");
        _glDisableVertexAttribArray.invoke(Void.class, new Object[]{index_ });
    }
    /* <command>
            <proto>void <name>glDisablei</name></proto>
            <param><ptype>GLenum</ptype> <name>target</name></param>
            <param><ptype>GLuint</ptype> <name>index</name></param>
        </command>
         */
    private static Function _glDisablei ;
    public static void glDisablei (int target_ , int index_ ) {
        if( _glDisablei == null )
            _glDisablei = loadFunction("glDisablei");
        _glDisablei.invoke(Void.class, new Object[]{target_,index_ });
    }
    /* <command>
            <proto>void <name>glDispatchCompute</name></proto>
            <param><ptype>GLuint</ptype> <name>num_groups_x</name></param>
            <param><ptype>GLuint</ptype> <name>num_groups_y</name></param>
            <param><ptype>GLuint</ptype> <name>num_groups_z</name></param>
        </command>
         */
    private static Function _glDispatchCompute ;
    public static void glDispatchCompute (int num_groups_x_ , int num_groups_y_ , int num_groups_z_ ) {
        if( _glDispatchCompute == null )
            _glDispatchCompute = loadFunction("glDispatchCompute");
        _glDispatchCompute.invoke(Void.class, new Object[]{num_groups_x_,num_groups_y_,num_groups_z_ });
    }
    /* <command>
            <proto>void <name>glDispatchComputeIndirect</name></proto>
            <param group="BufferOffset"><ptype>GLintptr</ptype> <name>indirect</name></param>
        </command>
         */
    private static Function _glDispatchComputeIndirect ;
    public static void glDispatchComputeIndirect (long indirect_ ) {
        if( _glDispatchComputeIndirect == null )
            _glDispatchComputeIndirect = loadFunction("glDispatchComputeIndirect");
        _glDispatchComputeIndirect.invoke(Void.class, new Object[]{indirect_ });
    }
    /* <command>
            <proto>void <name>glDrawArrays</name></proto>
            <param group="PrimitiveType"><ptype>GLenum</ptype> <name>mode</name></param>
            <param><ptype>GLint</ptype> <name>first</name></param>
            <param><ptype>GLsizei</ptype> <name>count</name></param>
            <glx opcode="193" type="render" />
        </command>
         */
    private static Function _glDrawArrays ;
    public static void glDrawArrays (int mode_ , int first_ , int count_ ) {
        if( _glDrawArrays == null )
            _glDrawArrays = loadFunction("glDrawArrays");
        _glDrawArrays.invoke(Void.class, new Object[]{mode_,first_,count_ });
    }
    /* <command>
            <proto>void <name>glDrawArraysIndirect</name></proto>
            <param group="PrimitiveType"><ptype>GLenum</ptype> <name>mode</name></param>
            <param>const void *<name>indirect</name></param>
        </command>
         */
    private static Function _glDrawArraysIndirect ;
    public static void glDrawArraysIndirect (int mode_ , byte[] indirect_ ) {
        if( _glDrawArraysIndirect == null )
            _glDrawArraysIndirect = loadFunction("glDrawArraysIndirect");
        _glDrawArraysIndirect.invoke(Void.class, new Object[]{mode_,indirect_ });
    }
    /* <command>
            <proto>void <name>glDrawArraysInstanced</name></proto>
            <param group="PrimitiveType"><ptype>GLenum</ptype> <name>mode</name></param>
            <param><ptype>GLint</ptype> <name>first</name></param>
            <param><ptype>GLsizei</ptype> <name>count</name></param>
            <param><ptype>GLsizei</ptype> <name>instancecount</name></param>
        </command>
         */
    private static Function _glDrawArraysInstanced ;
    public static void glDrawArraysInstanced (int mode_ , int first_ , int count_ , int instancecount_ ) {
        if( _glDrawArraysInstanced == null )
            _glDrawArraysInstanced = loadFunction("glDrawArraysInstanced");
        _glDrawArraysInstanced.invoke(Void.class, new Object[]{mode_,first_,count_,instancecount_ });
    }
    /* <command>
            <proto>void <name>glDrawArraysInstancedBaseInstance</name></proto>
            <param group="PrimitiveType"><ptype>GLenum</ptype> <name>mode</name></param>
            <param><ptype>GLint</ptype> <name>first</name></param>
            <param><ptype>GLsizei</ptype> <name>count</name></param>
            <param><ptype>GLsizei</ptype> <name>instancecount</name></param>
            <param><ptype>GLuint</ptype> <name>baseinstance</name></param>
        </command>
         */
    private static Function _glDrawArraysInstancedBaseInstance ;
    public static void glDrawArraysInstancedBaseInstance (int mode_ , int first_ , int count_ , int instancecount_ , int baseinstance_ ) {
        if( _glDrawArraysInstancedBaseInstance == null )
            _glDrawArraysInstancedBaseInstance = loadFunction("glDrawArraysInstancedBaseInstance");
        _glDrawArraysInstancedBaseInstance.invoke(Void.class, new Object[]{mode_,first_,count_,instancecount_,baseinstance_ });
    }
    /* <command>
            <proto>void <name>glDrawBuffer</name></proto>
            <param group="DrawBufferMode"><ptype>GLenum</ptype> <name>buf</name></param>
            <glx opcode="126" type="render" />
        </command>
         */
    private static Function _glDrawBuffer ;
    public static void glDrawBuffer (int buf_ ) {
        if( _glDrawBuffer == null )
            _glDrawBuffer = loadFunction("glDrawBuffer");
        _glDrawBuffer.invoke(Void.class, new Object[]{buf_ });
    }
    /* <command>
            <proto>void <name>glDrawBuffers</name></proto>
            <param><ptype>GLsizei</ptype> <name>n</name></param>
            <param group="DrawBufferModeATI" len="n">const <ptype>GLenum</ptype> *<name>bufs</name></param>
            <glx opcode="233" type="render" />
        </command>
         */
    private static Function _glDrawBuffers ;
    public static void glDrawBuffers (int n_ , int[] bufs_ ) {
        if( _glDrawBuffers == null )
            _glDrawBuffers = loadFunction("glDrawBuffers");
        _glDrawBuffers.invoke(Void.class, new Object[]{n_,bufs_ });
    }
    /* <command>
            <proto>void <name>glDrawElements</name></proto>
            <param group="PrimitiveType"><ptype>GLenum</ptype> <name>mode</name></param>
            <param><ptype>GLsizei</ptype> <name>count</name></param>
            <param group="DrawElementsType"><ptype>GLenum</ptype> <name>type</name></param>
            <param len="COMPSIZE(count,type)">const void *<name>indices</name></param>
        </command>
         */
    private static Function _glDrawElements ;
    public static void glDrawElements (int mode_ , int count_ , int type_ , byte[] indices_ ) {
        if( _glDrawElements == null )
            _glDrawElements = loadFunction("glDrawElements");
        _glDrawElements.invoke(Void.class, new Object[]{mode_,count_,type_,indices_ });
    }
    /* <command>
            <proto>void <name>glDrawElementsBaseVertex</name></proto>
            <param group="PrimitiveType"><ptype>GLenum</ptype> <name>mode</name></param>
            <param><ptype>GLsizei</ptype> <name>count</name></param>
            <param group="DrawElementsType"><ptype>GLenum</ptype> <name>type</name></param>
            <param len="COMPSIZE(count,type)">const void *<name>indices</name></param>
            <param><ptype>GLint</ptype> <name>basevertex</name></param>
        </command>
         */
    private static Function _glDrawElementsBaseVertex ;
    public static void glDrawElementsBaseVertex (int mode_ , int count_ , int type_ , byte[] indices_ , int basevertex_ ) {
        if( _glDrawElementsBaseVertex == null )
            _glDrawElementsBaseVertex = loadFunction("glDrawElementsBaseVertex");
        _glDrawElementsBaseVertex.invoke(Void.class, new Object[]{mode_,count_,type_,indices_,basevertex_ });
    }
    /* <command>
            <proto>void <name>glDrawElementsIndirect</name></proto>
            <param group="PrimitiveType"><ptype>GLenum</ptype> <name>mode</name></param>
            <param><ptype>GLenum</ptype> <name>type</name></param>
            <param>const void *<name>indirect</name></param>
        </command>
         */
    private static Function _glDrawElementsIndirect ;
    public static void glDrawElementsIndirect (int mode_ , int type_ , byte[] indirect_ ) {
        if( _glDrawElementsIndirect == null )
            _glDrawElementsIndirect = loadFunction("glDrawElementsIndirect");
        _glDrawElementsIndirect.invoke(Void.class, new Object[]{mode_,type_,indirect_ });
    }
    /* <command>
            <proto>void <name>glDrawElementsInstanced</name></proto>
            <param group="PrimitiveType"><ptype>GLenum</ptype> <name>mode</name></param>
            <param><ptype>GLsizei</ptype> <name>count</name></param>
            <param group="DrawElementsType"><ptype>GLenum</ptype> <name>type</name></param>
            <param len="COMPSIZE(count,type)">const void *<name>indices</name></param>
            <param><ptype>GLsizei</ptype> <name>instancecount</name></param>
        </command>
         */
    private static Function _glDrawElementsInstanced ;
    public static void glDrawElementsInstanced (int mode_ , int count_ , int type_ , byte[] indices_ , int instancecount_ ) {
        if( _glDrawElementsInstanced == null )
            _glDrawElementsInstanced = loadFunction("glDrawElementsInstanced");
        _glDrawElementsInstanced.invoke(Void.class, new Object[]{mode_,count_,type_,indices_,instancecount_ });
    }
    /* <command>
            <proto>void <name>glDrawElementsInstancedBaseInstance</name></proto>
            <param group="PrimitiveType"><ptype>GLenum</ptype> <name>mode</name></param>
            <param><ptype>GLsizei</ptype> <name>count</name></param>
            <param><ptype>GLenum</ptype> <name>type</name></param>
            <param len="count">const void *<name>indices</name></param>
            <param><ptype>GLsizei</ptype> <name>instancecount</name></param>
            <param><ptype>GLuint</ptype> <name>baseinstance</name></param>
        </command>
         */
    private static Function _glDrawElementsInstancedBaseInstance ;
    public static void glDrawElementsInstancedBaseInstance (int mode_ , int count_ , int type_ , byte[] indices_ , int instancecount_ , int baseinstance_ ) {
        if( _glDrawElementsInstancedBaseInstance == null )
            _glDrawElementsInstancedBaseInstance = loadFunction("glDrawElementsInstancedBaseInstance");
        _glDrawElementsInstancedBaseInstance.invoke(Void.class, new Object[]{mode_,count_,type_,indices_,instancecount_,baseinstance_ });
    }
    /* <command>
            <proto>void <name>glDrawElementsInstancedBaseVertex</name></proto>
            <param group="PrimitiveType"><ptype>GLenum</ptype> <name>mode</name></param>
            <param><ptype>GLsizei</ptype> <name>count</name></param>
            <param group="DrawElementsType"><ptype>GLenum</ptype> <name>type</name></param>
            <param len="COMPSIZE(count,type)">const void *<name>indices</name></param>
            <param><ptype>GLsizei</ptype> <name>instancecount</name></param>
            <param><ptype>GLint</ptype> <name>basevertex</name></param>
        </command>
         */
    private static Function _glDrawElementsInstancedBaseVertex ;
    public static void glDrawElementsInstancedBaseVertex (int mode_ , int count_ , int type_ , byte[] indices_ , int instancecount_ , int basevertex_ ) {
        if( _glDrawElementsInstancedBaseVertex == null )
            _glDrawElementsInstancedBaseVertex = loadFunction("glDrawElementsInstancedBaseVertex");
        _glDrawElementsInstancedBaseVertex.invoke(Void.class, new Object[]{mode_,count_,type_,indices_,instancecount_,basevertex_ });
    }
    /* <command>
            <proto>void <name>glDrawElementsInstancedBaseVertexBaseInstance</name></proto>
            <param group="PrimitiveType"><ptype>GLenum</ptype> <name>mode</name></param>
            <param><ptype>GLsizei</ptype> <name>count</name></param>
            <param><ptype>GLenum</ptype> <name>type</name></param>
            <param len="count">const void *<name>indices</name></param>
            <param><ptype>GLsizei</ptype> <name>instancecount</name></param>
            <param><ptype>GLint</ptype> <name>basevertex</name></param>
            <param><ptype>GLuint</ptype> <name>baseinstance</name></param>
        </command>
         */
    private static Function _glDrawElementsInstancedBaseVertexBaseInstance ;
    public static void glDrawElementsInstancedBaseVertexBaseInstance (int mode_ , int count_ , int type_ , byte[] indices_ , int instancecount_ , int basevertex_ , int baseinstance_ ) {
        if( _glDrawElementsInstancedBaseVertexBaseInstance == null )
            _glDrawElementsInstancedBaseVertexBaseInstance = loadFunction("glDrawElementsInstancedBaseVertexBaseInstance");
        _glDrawElementsInstancedBaseVertexBaseInstance.invoke(Void.class, new Object[]{mode_,count_,type_,indices_,instancecount_,basevertex_,baseinstance_ });
    }
    /* <command>
            <proto>void <name>glDrawRangeElements</name></proto>
            <param group="PrimitiveType"><ptype>GLenum</ptype> <name>mode</name></param>
            <param><ptype>GLuint</ptype> <name>start</name></param>
            <param><ptype>GLuint</ptype> <name>end</name></param>
            <param><ptype>GLsizei</ptype> <name>count</name></param>
            <param group="DrawElementsType"><ptype>GLenum</ptype> <name>type</name></param>
            <param len="COMPSIZE(count,type)">const void *<name>indices</name></param>
        </command>
         */
    private static Function _glDrawRangeElements ;
    public static void glDrawRangeElements (int mode_ , int start_ , int end_ , int count_ , int type_ , byte[] indices_ ) {
        if( _glDrawRangeElements == null )
            _glDrawRangeElements = loadFunction("glDrawRangeElements");
        _glDrawRangeElements.invoke(Void.class, new Object[]{mode_,start_,end_,count_,type_,indices_ });
    }
    /* <command>
            <proto>void <name>glDrawRangeElementsBaseVertex</name></proto>
            <param group="PrimitiveType"><ptype>GLenum</ptype> <name>mode</name></param>
            <param><ptype>GLuint</ptype> <name>start</name></param>
            <param><ptype>GLuint</ptype> <name>end</name></param>
            <param><ptype>GLsizei</ptype> <name>count</name></param>
            <param group="DrawElementsType"><ptype>GLenum</ptype> <name>type</name></param>
            <param len="COMPSIZE(count,type)">const void *<name>indices</name></param>
            <param><ptype>GLint</ptype> <name>basevertex</name></param>
        </command>
         */
    private static Function _glDrawRangeElementsBaseVertex ;
    public static void glDrawRangeElementsBaseVertex (int mode_ , int start_ , int end_ , int count_ , int type_ , byte[] indices_ , int basevertex_ ) {
        if( _glDrawRangeElementsBaseVertex == null )
            _glDrawRangeElementsBaseVertex = loadFunction("glDrawRangeElementsBaseVertex");
        _glDrawRangeElementsBaseVertex.invoke(Void.class, new Object[]{mode_,start_,end_,count_,type_,indices_,basevertex_ });
    }
    /* <command>
            <proto>void <name>glDrawTransformFeedback</name></proto>
            <param group="PrimitiveType"><ptype>GLenum</ptype> <name>mode</name></param>
            <param><ptype>GLuint</ptype> <name>id</name></param>
        </command>
         */
    private static Function _glDrawTransformFeedback ;
    public static void glDrawTransformFeedback (int mode_ , int id_ ) {
        if( _glDrawTransformFeedback == null )
            _glDrawTransformFeedback = loadFunction("glDrawTransformFeedback");
        _glDrawTransformFeedback.invoke(Void.class, new Object[]{mode_,id_ });
    }
    /* <command>
            <proto>void <name>glDrawTransformFeedbackInstanced</name></proto>
            <param group="PrimitiveType"><ptype>GLenum</ptype> <name>mode</name></param>
            <param><ptype>GLuint</ptype> <name>id</name></param>
            <param><ptype>GLsizei</ptype> <name>instancecount</name></param>
        </command>
         */
    private static Function _glDrawTransformFeedbackInstanced ;
    public static void glDrawTransformFeedbackInstanced (int mode_ , int id_ , int instancecount_ ) {
        if( _glDrawTransformFeedbackInstanced == null )
            _glDrawTransformFeedbackInstanced = loadFunction("glDrawTransformFeedbackInstanced");
        _glDrawTransformFeedbackInstanced.invoke(Void.class, new Object[]{mode_,id_,instancecount_ });
    }
    /* <command>
            <proto>void <name>glDrawTransformFeedbackStream</name></proto>
            <param group="PrimitiveType"><ptype>GLenum</ptype> <name>mode</name></param>
            <param><ptype>GLuint</ptype> <name>id</name></param>
            <param><ptype>GLuint</ptype> <name>stream</name></param>
        </command>
         */
    private static Function _glDrawTransformFeedbackStream ;
    public static void glDrawTransformFeedbackStream (int mode_ , int id_ , int stream_ ) {
        if( _glDrawTransformFeedbackStream == null )
            _glDrawTransformFeedbackStream = loadFunction("glDrawTransformFeedbackStream");
        _glDrawTransformFeedbackStream.invoke(Void.class, new Object[]{mode_,id_,stream_ });
    }
    /* <command>
            <proto>void <name>glDrawTransformFeedbackStreamInstanced</name></proto>
            <param group="PrimitiveType"><ptype>GLenum</ptype> <name>mode</name></param>
            <param><ptype>GLuint</ptype> <name>id</name></param>
            <param><ptype>GLuint</ptype> <name>stream</name></param>
            <param><ptype>GLsizei</ptype> <name>instancecount</name></param>
        </command>
         */
    private static Function _glDrawTransformFeedbackStreamInstanced ;
    public static void glDrawTransformFeedbackStreamInstanced (int mode_ , int id_ , int stream_ , int instancecount_ ) {
        if( _glDrawTransformFeedbackStreamInstanced == null )
            _glDrawTransformFeedbackStreamInstanced = loadFunction("glDrawTransformFeedbackStreamInstanced");
        _glDrawTransformFeedbackStreamInstanced.invoke(Void.class, new Object[]{mode_,id_,stream_,instancecount_ });
    }
    /* <command>
            <proto>void <name>glEnable</name></proto>
            <param group="EnableCap"><ptype>GLenum</ptype> <name>cap</name></param>
            <glx opcode="139" type="render" />
        </command>
         */
    private static Function _glEnable ;
    public static void glEnable (int cap_ ) {
        if( _glEnable == null )
            _glEnable = loadFunction("glEnable");
        _glEnable.invoke(Void.class, new Object[]{cap_ });
    }
    /* <command>
            <proto>void <name>glEnableVertexArrayAttrib</name></proto>
            <param><ptype>GLuint</ptype> <name>vaobj</name></param>
            <param><ptype>GLuint</ptype> <name>index</name></param>
        </command>
         */
    private static Function _glEnableVertexArrayAttrib ;
    public static void glEnableVertexArrayAttrib (int vaobj_ , int index_ ) {
        if( _glEnableVertexArrayAttrib == null )
            _glEnableVertexArrayAttrib = loadFunction("glEnableVertexArrayAttrib");
        _glEnableVertexArrayAttrib.invoke(Void.class, new Object[]{vaobj_,index_ });
    }
    /* <command>
            <proto>void <name>glEnableVertexAttribArray</name></proto>
            <param><ptype>GLuint</ptype> <name>index</name></param>
        </command>
         */
    private static Function _glEnableVertexAttribArray ;
    public static void glEnableVertexAttribArray (int index_ ) {
        if( _glEnableVertexAttribArray == null )
            _glEnableVertexAttribArray = loadFunction("glEnableVertexAttribArray");
        _glEnableVertexAttribArray.invoke(Void.class, new Object[]{index_ });
    }
    /* <command>
            <proto>void <name>glEnablei</name></proto>
            <param><ptype>GLenum</ptype> <name>target</name></param>
            <param><ptype>GLuint</ptype> <name>index</name></param>
        </command>
         */
    private static Function _glEnablei ;
    public static void glEnablei (int target_ , int index_ ) {
        if( _glEnablei == null )
            _glEnablei = loadFunction("glEnablei");
        _glEnablei.invoke(Void.class, new Object[]{target_,index_ });
    }
    /* <command>
            <proto>void <name>glEndConditionalRender</name></proto>
            <glx opcode="349" type="render" />
        </command>
         */
    private static Function _glEndConditionalRender ;
    public static void glEndConditionalRender () {
        if( _glEndConditionalRender == null )
            _glEndConditionalRender = loadFunction("glEndConditionalRender");
        _glEndConditionalRender.invoke(Void.class, new Object[]{ });
    }
    /* <command>
            <proto>void <name>glEndQuery</name></proto>
            <param><ptype>GLenum</ptype> <name>target</name></param>
            <glx opcode="232" type="render" />
        </command>
         */
    private static Function _glEndQuery ;
    public static void glEndQuery (int target_ ) {
        if( _glEndQuery == null )
            _glEndQuery = loadFunction("glEndQuery");
        _glEndQuery.invoke(Void.class, new Object[]{target_ });
    }
    /* <command>
            <proto>void <name>glEndQueryIndexed</name></proto>
            <param><ptype>GLenum</ptype> <name>target</name></param>
            <param><ptype>GLuint</ptype> <name>index</name></param>
        </command>
         */
    private static Function _glEndQueryIndexed ;
    public static void glEndQueryIndexed (int target_ , int index_ ) {
        if( _glEndQueryIndexed == null )
            _glEndQueryIndexed = loadFunction("glEndQueryIndexed");
        _glEndQueryIndexed.invoke(Void.class, new Object[]{target_,index_ });
    }
    /* <command>
            <proto>void <name>glEndTransformFeedback</name></proto>
        </command>
         */
    private static Function _glEndTransformFeedback ;
    public static void glEndTransformFeedback () {
        if( _glEndTransformFeedback == null )
            _glEndTransformFeedback = loadFunction("glEndTransformFeedback");
        _glEndTransformFeedback.invoke(Void.class, new Object[]{ });
    }
    /* <command>
            <proto group="sync"><ptype>GLsync</ptype> <name>glFenceSync</name></proto>
            <param><ptype>GLenum</ptype> <name>condition</name></param>
            <param><ptype>GLbitfield</ptype> <name>flags</name></param>
        </command>
         */
    private static Function _glFenceSync ;
    public static Pointer glFenceSync (int condition_ , int flags_ ) {
        if( _glFenceSync == null )
            _glFenceSync = loadFunction("glFenceSync");
        Pointer rv= ( Pointer )_glFenceSync.invoke(Pointer.class, new Object[]{condition_,flags_ });
        return rv;
    }
    /* <command>
            <proto>void <name>glFinish</name></proto>
            <glx opcode="108" type="single" />
        </command>
         */
    private static Function _glFinish ;
    public static void glFinish () {
        if( _glFinish == null )
            _glFinish = loadFunction("glFinish");
        _glFinish.invoke(Void.class, new Object[]{ });
    }
    /* <command>
            <proto>void <name>glFlush</name></proto>
            <glx opcode="142" type="single" />
        </command>
         */
    private static Function _glFlush ;
    public static void glFlush () {
        if( _glFlush == null )
            _glFlush = loadFunction("glFlush");
        _glFlush.invoke(Void.class, new Object[]{ });
    }
    /* <command>
            <proto>void <name>glFlushMappedBufferRange</name></proto>
            <param group="BufferTargetARB"><ptype>GLenum</ptype> <name>target</name></param>
            <param group="BufferOffset"><ptype>GLintptr</ptype> <name>offset</name></param>
            <param group="BufferSize"><ptype>GLsizeiptr</ptype> <name>length</name></param>
        </command>
         */
    private static Function _glFlushMappedBufferRange ;
    public static void glFlushMappedBufferRange (int target_ , long offset_ , long length_ ) {
        if( _glFlushMappedBufferRange == null )
            _glFlushMappedBufferRange = loadFunction("glFlushMappedBufferRange");
        _glFlushMappedBufferRange.invoke(Void.class, new Object[]{target_,offset_,length_ });
    }
    /* <command>
            <proto>void <name>glFlushMappedNamedBufferRange</name></proto>
            <param><ptype>GLuint</ptype> <name>buffer</name></param>
            <param><ptype>GLintptr</ptype> <name>offset</name></param>
            <param group="BufferSize"><ptype>GLsizeiptr</ptype> <name>length</name></param>
        </command>
         */
    private static Function _glFlushMappedNamedBufferRange ;
    public static void glFlushMappedNamedBufferRange (int buffer_ , long offset_ , long length_ ) {
        if( _glFlushMappedNamedBufferRange == null )
            _glFlushMappedNamedBufferRange = loadFunction("glFlushMappedNamedBufferRange");
        _glFlushMappedNamedBufferRange.invoke(Void.class, new Object[]{buffer_,offset_,length_ });
    }
    /* <command>
            <proto>void <name>glFramebufferParameteri</name></proto>
            <param><ptype>GLenum</ptype> <name>target</name></param>
            <param><ptype>GLenum</ptype> <name>pname</name></param>
            <param><ptype>GLint</ptype> <name>param</name></param>
        </command>
         */
    private static Function _glFramebufferParameteri ;
    public static void glFramebufferParameteri (int target_ , int pname_ , int param_ ) {
        if( _glFramebufferParameteri == null )
            _glFramebufferParameteri = loadFunction("glFramebufferParameteri");
        _glFramebufferParameteri.invoke(Void.class, new Object[]{target_,pname_,param_ });
    }
    /* <command>
            <proto>void <name>glFramebufferRenderbuffer</name></proto>
            <param group="FramebufferTarget"><ptype>GLenum</ptype> <name>target</name></param>
            <param group="FramebufferAttachment"><ptype>GLenum</ptype> <name>attachment</name></param>
            <param group="RenderbufferTarget"><ptype>GLenum</ptype> <name>renderbuffertarget</name></param>
            <param><ptype>GLuint</ptype> <name>renderbuffer</name></param>
            <glx opcode="4324" type="render" />
        </command>
         */
    private static Function _glFramebufferRenderbuffer ;
    public static void glFramebufferRenderbuffer (int target_ , int attachment_ , int renderbuffertarget_ , int renderbuffer_ ) {
        if( _glFramebufferRenderbuffer == null )
            _glFramebufferRenderbuffer = loadFunction("glFramebufferRenderbuffer");
        _glFramebufferRenderbuffer.invoke(Void.class, new Object[]{target_,attachment_,renderbuffertarget_,renderbuffer_ });
    }
    /* <command>
            <proto>void <name>glFramebufferTexture</name></proto>
            <param><ptype>GLenum</ptype> <name>target</name></param>
            <param><ptype>GLenum</ptype> <name>attachment</name></param>
            <param><ptype>GLuint</ptype> <name>texture</name></param>
            <param><ptype>GLint</ptype> <name>level</name></param>
        </command>
         */
    private static Function _glFramebufferTexture ;
    public static void glFramebufferTexture (int target_ , int attachment_ , int texture_ , int level_ ) {
        if( _glFramebufferTexture == null )
            _glFramebufferTexture = loadFunction("glFramebufferTexture");
        _glFramebufferTexture.invoke(Void.class, new Object[]{target_,attachment_,texture_,level_ });
    }
    /* <command>
            <proto>void <name>glFramebufferTexture1D</name></proto>
            <param group="FramebufferTarget"><ptype>GLenum</ptype> <name>target</name></param>
            <param group="FramebufferAttachment"><ptype>GLenum</ptype> <name>attachment</name></param>
            <param><ptype>GLenum</ptype> <name>textarget</name></param>
            <param><ptype>GLuint</ptype> <name>texture</name></param>
            <param><ptype>GLint</ptype> <name>level</name></param>
            <glx opcode="4321" type="render" />
        </command>
         */
    private static Function _glFramebufferTexture1D ;
    public static void glFramebufferTexture1D (int target_ , int attachment_ , int textarget_ , int texture_ , int level_ ) {
        if( _glFramebufferTexture1D == null )
            _glFramebufferTexture1D = loadFunction("glFramebufferTexture1D");
        _glFramebufferTexture1D.invoke(Void.class, new Object[]{target_,attachment_,textarget_,texture_,level_ });
    }
    /* <command>
            <proto>void <name>glFramebufferTexture2D</name></proto>
            <param group="FramebufferTarget"><ptype>GLenum</ptype> <name>target</name></param>
            <param group="FramebufferAttachment"><ptype>GLenum</ptype> <name>attachment</name></param>
            <param><ptype>GLenum</ptype> <name>textarget</name></param>
            <param><ptype>GLuint</ptype> <name>texture</name></param>
            <param><ptype>GLint</ptype> <name>level</name></param>
            <glx opcode="4322" type="render" />
        </command>
         */
    private static Function _glFramebufferTexture2D ;
    public static void glFramebufferTexture2D (int target_ , int attachment_ , int textarget_ , int texture_ , int level_ ) {
        if( _glFramebufferTexture2D == null )
            _glFramebufferTexture2D = loadFunction("glFramebufferTexture2D");
        _glFramebufferTexture2D.invoke(Void.class, new Object[]{target_,attachment_,textarget_,texture_,level_ });
    }
    /* <command>
            <proto>void <name>glFramebufferTexture3D</name></proto>
            <param group="FramebufferTarget"><ptype>GLenum</ptype> <name>target</name></param>
            <param group="FramebufferAttachment"><ptype>GLenum</ptype> <name>attachment</name></param>
            <param><ptype>GLenum</ptype> <name>textarget</name></param>
            <param><ptype>GLuint</ptype> <name>texture</name></param>
            <param><ptype>GLint</ptype> <name>level</name></param>
            <param><ptype>GLint</ptype> <name>zoffset</name></param>
            <glx opcode="4323" type="render" />
        </command>
         */
    private static Function _glFramebufferTexture3D ;
    public static void glFramebufferTexture3D (int target_ , int attachment_ , int textarget_ , int texture_ , int level_ , int zoffset_ ) {
        if( _glFramebufferTexture3D == null )
            _glFramebufferTexture3D = loadFunction("glFramebufferTexture3D");
        _glFramebufferTexture3D.invoke(Void.class, new Object[]{target_,attachment_,textarget_,texture_,level_,zoffset_ });
    }
    /* <command>
            <proto>void <name>glFramebufferTextureLayer</name></proto>
            <param group="FramebufferTarget"><ptype>GLenum</ptype> <name>target</name></param>
            <param group="FramebufferAttachment"><ptype>GLenum</ptype> <name>attachment</name></param>
            <param group="Texture"><ptype>GLuint</ptype> <name>texture</name></param>
            <param group="CheckedInt32"><ptype>GLint</ptype> <name>level</name></param>
            <param group="CheckedInt32"><ptype>GLint</ptype> <name>layer</name></param>
            <glx opcode="237" type="render" />
        </command>
         */
    private static Function _glFramebufferTextureLayer ;
    public static void glFramebufferTextureLayer (int target_ , int attachment_ , int texture_ , int level_ , int layer_ ) {
        if( _glFramebufferTextureLayer == null )
            _glFramebufferTextureLayer = loadFunction("glFramebufferTextureLayer");
        _glFramebufferTextureLayer.invoke(Void.class, new Object[]{target_,attachment_,texture_,level_,layer_ });
    }
    /* <command>
            <proto>void <name>glFrontFace</name></proto>
            <param group="FrontFaceDirection"><ptype>GLenum</ptype> <name>mode</name></param>
            <glx opcode="84" type="render" />
        </command>
         */
    private static Function _glFrontFace ;
    public static void glFrontFace (int mode_ ) {
        if( _glFrontFace == null )
            _glFrontFace = loadFunction("glFrontFace");
        _glFrontFace.invoke(Void.class, new Object[]{mode_ });
    }
    /* <command>
            <proto>void <name>glGenBuffers</name></proto>
            <param><ptype>GLsizei</ptype> <name>n</name></param>
            <param len="n"><ptype>GLuint</ptype> *<name>buffers</name></param>
        </command>
         */
    private static Function _glGenBuffers ;
    public static void glGenBuffers (int n_ , int[] buffers_ ) {
        if( _glGenBuffers == null )
            _glGenBuffers = loadFunction("glGenBuffers");
        _glGenBuffers.invoke(Void.class, new Object[]{n_,buffers_ });
    }
    /* <command>
            <proto>void <name>glGenFramebuffers</name></proto>
            <param><ptype>GLsizei</ptype> <name>n</name></param>
            <param len="n"><ptype>GLuint</ptype> *<name>framebuffers</name></param>
            <glx opcode="1426" type="vendor" />
        </command>
         */
    private static Function _glGenFramebuffers ;
    public static void glGenFramebuffers (int n_ , int[] framebuffers_ ) {
        if( _glGenFramebuffers == null )
            _glGenFramebuffers = loadFunction("glGenFramebuffers");
        _glGenFramebuffers.invoke(Void.class, new Object[]{n_,framebuffers_ });
    }
    /* <command>
            <proto>void <name>glGenProgramPipelines</name></proto>
            <param><ptype>GLsizei</ptype> <name>n</name></param>
            <param len="n"><ptype>GLuint</ptype> *<name>pipelines</name></param>
        </command>
         */
    private static Function _glGenProgramPipelines ;
    public static void glGenProgramPipelines (int n_ , int[] pipelines_ ) {
        if( _glGenProgramPipelines == null )
            _glGenProgramPipelines = loadFunction("glGenProgramPipelines");
        _glGenProgramPipelines.invoke(Void.class, new Object[]{n_,pipelines_ });
    }
    /* <command>
            <proto>void <name>glGenQueries</name></proto>
            <param><ptype>GLsizei</ptype> <name>n</name></param>
            <param len="n"><ptype>GLuint</ptype> *<name>ids</name></param>
            <glx opcode="162" type="single" />
        </command>
         */
    private static Function _glGenQueries ;
    public static void glGenQueries (int n_ , int[] ids_ ) {
        if( _glGenQueries == null )
            _glGenQueries = loadFunction("glGenQueries");
        _glGenQueries.invoke(Void.class, new Object[]{n_,ids_ });
    }
    /* <command>
            <proto>void <name>glGenRenderbuffers</name></proto>
            <param><ptype>GLsizei</ptype> <name>n</name></param>
            <param len="n"><ptype>GLuint</ptype> *<name>renderbuffers</name></param>
            <glx opcode="1423" type="vendor" />
        </command>
         */
    private static Function _glGenRenderbuffers ;
    public static void glGenRenderbuffers (int n_ , int[] renderbuffers_ ) {
        if( _glGenRenderbuffers == null )
            _glGenRenderbuffers = loadFunction("glGenRenderbuffers");
        _glGenRenderbuffers.invoke(Void.class, new Object[]{n_,renderbuffers_ });
    }
    /* <command>
            <proto>void <name>glGenSamplers</name></proto>
            <param><ptype>GLsizei</ptype> <name>count</name></param>
            <param len="count"><ptype>GLuint</ptype> *<name>samplers</name></param>
        </command>
         */
    private static Function _glGenSamplers ;
    public static void glGenSamplers (int count_ , int[] samplers_ ) {
        if( _glGenSamplers == null )
            _glGenSamplers = loadFunction("glGenSamplers");
        _glGenSamplers.invoke(Void.class, new Object[]{count_,samplers_ });
    }
    /* <command>
            <proto>void <name>glGenTextures</name></proto>
            <param><ptype>GLsizei</ptype> <name>n</name></param>
            <param group="Texture" len="n"><ptype>GLuint</ptype> *<name>textures</name></param>
            <glx opcode="145" type="single" />
        </command>
         */
    private static Function _glGenTextures ;
    public static void glGenTextures (int n_ , int[] textures_ ) {
        if( _glGenTextures == null )
            _glGenTextures = loadFunction("glGenTextures");
        _glGenTextures.invoke(Void.class, new Object[]{n_,textures_ });
    }
    /* <command>
            <proto>void <name>glGenTransformFeedbacks</name></proto>
            <param><ptype>GLsizei</ptype> <name>n</name></param>
            <param len="n"><ptype>GLuint</ptype> *<name>ids</name></param>
        </command>
         */
    private static Function _glGenTransformFeedbacks ;
    public static void glGenTransformFeedbacks (int n_ , int[] ids_ ) {
        if( _glGenTransformFeedbacks == null )
            _glGenTransformFeedbacks = loadFunction("glGenTransformFeedbacks");
        _glGenTransformFeedbacks.invoke(Void.class, new Object[]{n_,ids_ });
    }
    /* <command>
            <proto>void <name>glGenVertexArrays</name></proto>
            <param><ptype>GLsizei</ptype> <name>n</name></param>
            <param len="n"><ptype>GLuint</ptype> *<name>arrays</name></param>
            <glx opcode="206" type="single" />
        </command>
         */
    private static Function _glGenVertexArrays ;
    public static void glGenVertexArrays (int n_ , int[] arrays_ ) {
        if( _glGenVertexArrays == null )
            _glGenVertexArrays = loadFunction("glGenVertexArrays");
        _glGenVertexArrays.invoke(Void.class, new Object[]{n_,arrays_ });
    }
    /* <command>
            <proto>void <name>glGenerateMipmap</name></proto>
            <param><ptype>GLenum</ptype> <name>target</name></param>
            <glx opcode="4325" type="render" />
        </command>
         */
    private static Function _glGenerateMipmap ;
    public static void glGenerateMipmap (int target_ ) {
        if( _glGenerateMipmap == null )
            _glGenerateMipmap = loadFunction("glGenerateMipmap");
        _glGenerateMipmap.invoke(Void.class, new Object[]{target_ });
    }
    /* <command>
            <proto>void <name>glGenerateTextureMipmap</name></proto>
            <param><ptype>GLuint</ptype> <name>texture</name></param>
        </command>
         */
    private static Function _glGenerateTextureMipmap ;
    public static void glGenerateTextureMipmap (int texture_ ) {
        if( _glGenerateTextureMipmap == null )
            _glGenerateTextureMipmap = loadFunction("glGenerateTextureMipmap");
        _glGenerateTextureMipmap.invoke(Void.class, new Object[]{texture_ });
    }
    /* <command>
            <proto>void <name>glGetActiveAtomicCounterBufferiv</name></proto>
            <param><ptype>GLuint</ptype> <name>program</name></param>
            <param><ptype>GLuint</ptype> <name>bufferIndex</name></param>
            <param><ptype>GLenum</ptype> <name>pname</name></param>
            <param len="COMPSIZE(pname)"><ptype>GLint</ptype> *<name>params</name></param>
        </command>
         */
    private static Function _glGetActiveAtomicCounterBufferiv ;
    public static void glGetActiveAtomicCounterBufferiv (int program_ , int bufferIndex_ , int pname_ , int[] params_ ) {
        if( _glGetActiveAtomicCounterBufferiv == null )
            _glGetActiveAtomicCounterBufferiv = loadFunction("glGetActiveAtomicCounterBufferiv");
        _glGetActiveAtomicCounterBufferiv.invoke(Void.class, new Object[]{program_,bufferIndex_,pname_,params_ });
    }
    /* <command>
            <proto>void <name>glGetActiveAttrib</name></proto>
            <param><ptype>GLuint</ptype> <name>program</name></param>
            <param><ptype>GLuint</ptype> <name>index</name></param>
            <param><ptype>GLsizei</ptype> <name>bufSize</name></param>
            <param len="1"><ptype>GLsizei</ptype> *<name>length</name></param>
            <param len="1"><ptype>GLint</ptype> *<name>size</name></param>
            <param len="1"><ptype>GLenum</ptype> *<name>type</name></param>
            <param len="bufSize"><ptype>GLchar</ptype> *<name>name</name></param>
        </command>
         */
    private static Function _glGetActiveAttrib ;
    public static void glGetActiveAttrib (int program_ , int index_ , int bufSize_ , int[] length_ , int[] size_ , int[] type_ , byte[] name_ ) {
        if( _glGetActiveAttrib == null )
            _glGetActiveAttrib = loadFunction("glGetActiveAttrib");
        _glGetActiveAttrib.invoke(Void.class, new Object[]{program_,index_,bufSize_,length_,size_,type_,name_ });
    }
    /* <command>
            <proto>void <name>glGetActiveSubroutineName</name></proto>
            <param><ptype>GLuint</ptype> <name>program</name></param>
            <param><ptype>GLenum</ptype> <name>shadertype</name></param>
            <param><ptype>GLuint</ptype> <name>index</name></param>
            <param><ptype>GLsizei</ptype> <name>bufsize</name></param>
            <param len="1"><ptype>GLsizei</ptype> *<name>length</name></param>
            <param len="bufsize"><ptype>GLchar</ptype> *<name>name</name></param>
        </command>
         */
    private static Function _glGetActiveSubroutineName ;
    public static void glGetActiveSubroutineName (int program_ , int shadertype_ , int index_ , int bufsize_ , int[] length_ , byte[] name_ ) {
        if( _glGetActiveSubroutineName == null )
            _glGetActiveSubroutineName = loadFunction("glGetActiveSubroutineName");
        _glGetActiveSubroutineName.invoke(Void.class, new Object[]{program_,shadertype_,index_,bufsize_,length_,name_ });
    }
    /* <command>
            <proto>void <name>glGetActiveSubroutineUniformName</name></proto>
            <param><ptype>GLuint</ptype> <name>program</name></param>
            <param><ptype>GLenum</ptype> <name>shadertype</name></param>
            <param><ptype>GLuint</ptype> <name>index</name></param>
            <param><ptype>GLsizei</ptype> <name>bufsize</name></param>
            <param len="1"><ptype>GLsizei</ptype> *<name>length</name></param>
            <param len="bufsize"><ptype>GLchar</ptype> *<name>name</name></param>
        </command>
         */
    private static Function _glGetActiveSubroutineUniformName ;
    public static void glGetActiveSubroutineUniformName (int program_ , int shadertype_ , int index_ , int bufsize_ , int[] length_ , byte[] name_ ) {
        if( _glGetActiveSubroutineUniformName == null )
            _glGetActiveSubroutineUniformName = loadFunction("glGetActiveSubroutineUniformName");
        _glGetActiveSubroutineUniformName.invoke(Void.class, new Object[]{program_,shadertype_,index_,bufsize_,length_,name_ });
    }
    /* <command>
            <proto>void <name>glGetActiveSubroutineUniformiv</name></proto>
            <param><ptype>GLuint</ptype> <name>program</name></param>
            <param><ptype>GLenum</ptype> <name>shadertype</name></param>
            <param><ptype>GLuint</ptype> <name>index</name></param>
            <param><ptype>GLenum</ptype> <name>pname</name></param>
            <param len="COMPSIZE(pname)"><ptype>GLint</ptype> *<name>values</name></param>
        </command>
         */
    private static Function _glGetActiveSubroutineUniformiv ;
    public static void glGetActiveSubroutineUniformiv (int program_ , int shadertype_ , int index_ , int pname_ , int[] values_ ) {
        if( _glGetActiveSubroutineUniformiv == null )
            _glGetActiveSubroutineUniformiv = loadFunction("glGetActiveSubroutineUniformiv");
        _glGetActiveSubroutineUniformiv.invoke(Void.class, new Object[]{program_,shadertype_,index_,pname_,values_ });
    }
    /* <command>
            <proto>void <name>glGetActiveUniform</name></proto>
            <param><ptype>GLuint</ptype> <name>program</name></param>
            <param><ptype>GLuint</ptype> <name>index</name></param>
            <param><ptype>GLsizei</ptype> <name>bufSize</name></param>
            <param len="1"><ptype>GLsizei</ptype> *<name>length</name></param>
            <param len="1"><ptype>GLint</ptype> *<name>size</name></param>
            <param len="1"><ptype>GLenum</ptype> *<name>type</name></param>
            <param len="bufSize"><ptype>GLchar</ptype> *<name>name</name></param>
        </command>
         */
    private static Function _glGetActiveUniform ;
    public static void glGetActiveUniform (int program_ , int index_ , int bufSize_ , int[] length_ , int[] size_ , int[] type_ , byte[] name_ ) {
        if( _glGetActiveUniform == null )
            _glGetActiveUniform = loadFunction("glGetActiveUniform");
        _glGetActiveUniform.invoke(Void.class, new Object[]{program_,index_,bufSize_,length_,size_,type_,name_ });
    }
    /* <command>
            <proto>void <name>glGetActiveUniformBlockName</name></proto>
            <param><ptype>GLuint</ptype> <name>program</name></param>
            <param><ptype>GLuint</ptype> <name>uniformBlockIndex</name></param>
            <param><ptype>GLsizei</ptype> <name>bufSize</name></param>
            <param len="1"><ptype>GLsizei</ptype> *<name>length</name></param>
            <param len="bufSize"><ptype>GLchar</ptype> *<name>uniformBlockName</name></param>
        </command>
         */
    private static Function _glGetActiveUniformBlockName ;
    public static void glGetActiveUniformBlockName (int program_ , int uniformBlockIndex_ , int bufSize_ , int[] length_ , byte[] uniformBlockName_ ) {
        if( _glGetActiveUniformBlockName == null )
            _glGetActiveUniformBlockName = loadFunction("glGetActiveUniformBlockName");
        _glGetActiveUniformBlockName.invoke(Void.class, new Object[]{program_,uniformBlockIndex_,bufSize_,length_,uniformBlockName_ });
    }
    /* <command>
            <proto>void <name>glGetActiveUniformBlockiv</name></proto>
            <param><ptype>GLuint</ptype> <name>program</name></param>
            <param><ptype>GLuint</ptype> <name>uniformBlockIndex</name></param>
            <param><ptype>GLenum</ptype> <name>pname</name></param>
            <param len="COMPSIZE(program,uniformBlockIndex,pname)"><ptype>GLint</ptype> *<name>params</name></param>
        </command>
         */
    private static Function _glGetActiveUniformBlockiv ;
    public static void glGetActiveUniformBlockiv (int program_ , int uniformBlockIndex_ , int pname_ , int[] params_ ) {
        if( _glGetActiveUniformBlockiv == null )
            _glGetActiveUniformBlockiv = loadFunction("glGetActiveUniformBlockiv");
        _glGetActiveUniformBlockiv.invoke(Void.class, new Object[]{program_,uniformBlockIndex_,pname_,params_ });
    }
    /* <command>
            <proto>void <name>glGetActiveUniformName</name></proto>
            <param><ptype>GLuint</ptype> <name>program</name></param>
            <param><ptype>GLuint</ptype> <name>uniformIndex</name></param>
            <param><ptype>GLsizei</ptype> <name>bufSize</name></param>
            <param len="1"><ptype>GLsizei</ptype> *<name>length</name></param>
            <param len="bufSize"><ptype>GLchar</ptype> *<name>uniformName</name></param>
        </command>
         */
    private static Function _glGetActiveUniformName ;
    public static void glGetActiveUniformName (int program_ , int uniformIndex_ , int bufSize_ , int[] length_ , byte[] uniformName_ ) {
        if( _glGetActiveUniformName == null )
            _glGetActiveUniformName = loadFunction("glGetActiveUniformName");
        _glGetActiveUniformName.invoke(Void.class, new Object[]{program_,uniformIndex_,bufSize_,length_,uniformName_ });
    }
    /* <command>
            <proto>void <name>glGetActiveUniformsiv</name></proto>
            <param><ptype>GLuint</ptype> <name>program</name></param>
            <param><ptype>GLsizei</ptype> <name>uniformCount</name></param>
            <param len="uniformCount">const <ptype>GLuint</ptype> *<name>uniformIndices</name></param>
            <param><ptype>GLenum</ptype> <name>pname</name></param>
            <param len="COMPSIZE(uniformCount,pname)"><ptype>GLint</ptype> *<name>params</name></param>
        </command>
         */
    private static Function _glGetActiveUniformsiv ;
    public static void glGetActiveUniformsiv (int program_ , int uniformCount_ , int[] uniformIndices_ , int pname_ , int[] params_ ) {
        if( _glGetActiveUniformsiv == null )
            _glGetActiveUniformsiv = loadFunction("glGetActiveUniformsiv");
        _glGetActiveUniformsiv.invoke(Void.class, new Object[]{program_,uniformCount_,uniformIndices_,pname_,params_ });
    }
    /* <command>
            <proto>void <name>glGetAttachedShaders</name></proto>
            <param><ptype>GLuint</ptype> <name>program</name></param>
            <param><ptype>GLsizei</ptype> <name>maxCount</name></param>
            <param len="1"><ptype>GLsizei</ptype> *<name>count</name></param>
            <param len="maxCount"><ptype>GLuint</ptype> *<name>shaders</name></param>
        </command>
         */
    private static Function _glGetAttachedShaders ;
    public static void glGetAttachedShaders (int program_ , int maxCount_ , int[] count_ , int[] shaders_ ) {
        if( _glGetAttachedShaders == null )
            _glGetAttachedShaders = loadFunction("glGetAttachedShaders");
        _glGetAttachedShaders.invoke(Void.class, new Object[]{program_,maxCount_,count_,shaders_ });
    }
    /* <command>
            <proto><ptype>GLint</ptype> <name>glGetAttribLocation</name></proto>
            <param><ptype>GLuint</ptype> <name>program</name></param>
            <param>const <ptype>GLchar</ptype> *<name>name</name></param>
        </command>
         */
    private static Function _glGetAttribLocation ;
    public static int glGetAttribLocation (int program_ , String name_ ) {
        if( _glGetAttribLocation == null )
            _glGetAttribLocation = loadFunction("glGetAttribLocation");
        int rv= ( Integer )_glGetAttribLocation.invoke(Integer.class, new Object[]{program_,name_ });
        return rv;
    }
    /* <command>
            <proto>void <name>glGetBufferParameteri64v</name></proto>
            <param group="BufferTargetARB"><ptype>GLenum</ptype> <name>target</name></param>
            <param group="BufferPNameARB"><ptype>GLenum</ptype> <name>pname</name></param>
            <param len="COMPSIZE(pname)"><ptype>GLint64</ptype> *<name>params</name></param>
        </command>
         */
    private static Function _glGetBufferParameteri64v ;
    public static void glGetBufferParameteri64v (int target_ , int pname_ , long[] params_ ) {
        if( _glGetBufferParameteri64v == null )
            _glGetBufferParameteri64v = loadFunction("glGetBufferParameteri64v");
        _glGetBufferParameteri64v.invoke(Void.class, new Object[]{target_,pname_,params_ });
    }
    /* <command>
            <proto>void <name>glGetBufferParameteriv</name></proto>
            <param group="BufferTargetARB"><ptype>GLenum</ptype> <name>target</name></param>
            <param group="BufferPNameARB"><ptype>GLenum</ptype> <name>pname</name></param>
            <param len="COMPSIZE(pname)"><ptype>GLint</ptype> *<name>params</name></param>
        </command>
         */
    private static Function _glGetBufferParameteriv ;
    public static void glGetBufferParameteriv (int target_ , int pname_ , int[] params_ ) {
        if( _glGetBufferParameteriv == null )
            _glGetBufferParameteriv = loadFunction("glGetBufferParameteriv");
        _glGetBufferParameteriv.invoke(Void.class, new Object[]{target_,pname_,params_ });
    }
    /* <command>
            <proto>void <name>glGetBufferPointerv</name></proto>
            <param group="BufferTargetARB"><ptype>GLenum</ptype> <name>target</name></param>
            <param group="BufferPointerNameARB"><ptype>GLenum</ptype> <name>pname</name></param>
            <param len="1">void **<name>params</name></param>
        </command>
         */
    private static Function _glGetBufferPointerv ;
    public static void glGetBufferPointerv (int target_ , int pname_ , byte[] params_ ) {
        if( _glGetBufferPointerv == null )
            _glGetBufferPointerv = loadFunction("glGetBufferPointerv");
        _glGetBufferPointerv.invoke(Void.class, new Object[]{target_,pname_,params_ });
    }
    /* <command>
            <proto>void <name>glGetBufferSubData</name></proto>
            <param group="BufferTargetARB"><ptype>GLenum</ptype> <name>target</name></param>
            <param group="BufferOffset"><ptype>GLintptr</ptype> <name>offset</name></param>
            <param group="BufferSize"><ptype>GLsizeiptr</ptype> <name>size</name></param>
            <param len="size">void *<name>data</name></param>
        </command>
         */
    private static Function _glGetBufferSubData ;
    public static void glGetBufferSubData (int target_ , long offset_ , long size_ , byte[] data_ ) {
        if( _glGetBufferSubData == null )
            _glGetBufferSubData = loadFunction("glGetBufferSubData");
        _glGetBufferSubData.invoke(Void.class, new Object[]{target_,offset_,size_,data_ });
    }
    /* <command>
            <proto>void <name>glGetCompressedTexImage</name></proto>
            <param group="TextureTarget"><ptype>GLenum</ptype> <name>target</name></param>
            <param group="CheckedInt32"><ptype>GLint</ptype> <name>level</name></param>
            <param group="CompressedTextureARB" len="COMPSIZE(target,level)">void *<name>img</name></param>
            <glx opcode="160" type="single" />
            <glx comment="PBO protocol" name="glGetCompressedTexImagePBO" opcode="335" type="render" />
        </command>
         */
    private static Function _glGetCompressedTexImage ;
    public static void glGetCompressedTexImage (int target_ , int level_ , byte[] img_ ) {
        if( _glGetCompressedTexImage == null )
            _glGetCompressedTexImage = loadFunction("glGetCompressedTexImage");
        _glGetCompressedTexImage.invoke(Void.class, new Object[]{target_,level_,img_ });
    }
    /* <command>
            <proto>void <name>glGetCompressedTextureImage</name></proto>
            <param><ptype>GLuint</ptype> <name>texture</name></param>
            <param><ptype>GLint</ptype> <name>level</name></param>
            <param><ptype>GLsizei</ptype> <name>bufSize</name></param>
            <param>void *<name>pixels</name></param>
        </command>
         */
    private static Function _glGetCompressedTextureImage ;
    public static void glGetCompressedTextureImage (int texture_ , int level_ , int bufSize_ , byte[] pixels_ ) {
        if( _glGetCompressedTextureImage == null )
            _glGetCompressedTextureImage = loadFunction("glGetCompressedTextureImage");
        _glGetCompressedTextureImage.invoke(Void.class, new Object[]{texture_,level_,bufSize_,pixels_ });
    }
    /* <command>
            <proto>void <name>glGetCompressedTextureSubImage</name></proto>
            <param><ptype>GLuint</ptype> <name>texture</name></param>
            <param><ptype>GLint</ptype> <name>level</name></param>
            <param><ptype>GLint</ptype> <name>xoffset</name></param>
            <param><ptype>GLint</ptype> <name>yoffset</name></param>
            <param><ptype>GLint</ptype> <name>zoffset</name></param>
            <param><ptype>GLsizei</ptype> <name>width</name></param>
            <param><ptype>GLsizei</ptype> <name>height</name></param>
            <param><ptype>GLsizei</ptype> <name>depth</name></param>
            <param><ptype>GLsizei</ptype> <name>bufSize</name></param>
            <param>void *<name>pixels</name></param>
        </command>
         */
    private static Function _glGetCompressedTextureSubImage ;
    public static void glGetCompressedTextureSubImage (int texture_ , int level_ , int xoffset_ , int yoffset_ , int zoffset_ , int width_ , int height_ , int depth_ , int bufSize_ , byte[] pixels_ ) {
        if( _glGetCompressedTextureSubImage == null )
            _glGetCompressedTextureSubImage = loadFunction("glGetCompressedTextureSubImage");
        _glGetCompressedTextureSubImage.invoke(Void.class, new Object[]{texture_,level_,xoffset_,yoffset_,zoffset_,width_,height_,depth_,bufSize_,pixels_ });
    }
    /* <command>
            <proto><ptype>GLuint</ptype> <name>glGetDebugMessageLog</name></proto>
            <param><ptype>GLuint</ptype> <name>count</name></param>
            <param><ptype>GLsizei</ptype> <name>bufSize</name></param>
            <param len="count"><ptype>GLenum</ptype> *<name>sources</name></param>
            <param len="count"><ptype>GLenum</ptype> *<name>types</name></param>
            <param len="count"><ptype>GLuint</ptype> *<name>ids</name></param>
            <param len="count"><ptype>GLenum</ptype> *<name>severities</name></param>
            <param len="count"><ptype>GLsizei</ptype> *<name>lengths</name></param>
            <param len="bufSize"><ptype>GLchar</ptype> *<name>messageLog</name></param>
        </command>
         */
    private static Function _glGetDebugMessageLog ;
    public static int glGetDebugMessageLog (int count_ , int bufSize_ , int[] sources_ , int[] types_ , int[] ids_ , int[] severities_ , int[] lengths_ , byte[] messageLog_ ) {
        if( _glGetDebugMessageLog == null )
            _glGetDebugMessageLog = loadFunction("glGetDebugMessageLog");
        int rv= ( Integer )_glGetDebugMessageLog.invoke(Integer.class, new Object[]{count_,bufSize_,sources_,types_,ids_,severities_,lengths_,messageLog_ });
        return rv;
    }
    /* <command>
            <proto>void <name>glGetDoublei_v</name></proto>
            <param><ptype>GLenum</ptype> <name>target</name></param>
            <param><ptype>GLuint</ptype> <name>index</name></param>
            <param len="COMPSIZE(target)"><ptype>GLdouble</ptype> *<name>data</name></param>
        </command>
         */
    private static Function _glGetDoublei_v ;
    public static void glGetDoublei_v (int target_ , int index_ , double[] data_ ) {
        if( _glGetDoublei_v == null )
            _glGetDoublei_v = loadFunction("glGetDoublei_v");
        _glGetDoublei_v.invoke(Void.class, new Object[]{target_,index_,data_ });
    }
    /* <command>
            <proto>void <name>glGetDoublev</name></proto>
            <param group="GetPName"><ptype>GLenum</ptype> <name>pname</name></param>
            <param len="COMPSIZE(pname)"><ptype>GLdouble</ptype> *<name>data</name></param>
            <glx opcode="114" type="single" />
        </command>
         */
    private static Function _glGetDoublev ;
    public static void glGetDoublev (int pname_ , double[] data_ ) {
        if( _glGetDoublev == null )
            _glGetDoublev = loadFunction("glGetDoublev");
        _glGetDoublev.invoke(Void.class, new Object[]{pname_,data_ });
    }
    /* <command>
            <proto group="ErrorCode"><ptype>GLenum</ptype> <name>glGetError</name></proto>
            <glx opcode="115" type="single" />
        </command>
         */
    private static Function _glGetError ;
    public static int glGetError () {
        if( _glGetError == null )
            _glGetError = loadFunction("glGetError");
        int rv= ( Integer )_glGetError.invoke(Integer.class, new Object[]{ });
        return rv;
    }
    /* <command>
            <proto>void <name>glGetFloati_v</name></proto>
            <param group="TypeEnum"><ptype>GLenum</ptype> <name>target</name></param>
            <param><ptype>GLuint</ptype> <name>index</name></param>
            <param len="COMPSIZE(target)"><ptype>GLfloat</ptype> *<name>data</name></param>
        </command>
         */
    private static Function _glGetFloati_v ;
    public static void glGetFloati_v (int target_ , int index_ , float[] data_ ) {
        if( _glGetFloati_v == null )
            _glGetFloati_v = loadFunction("glGetFloati_v");
        _glGetFloati_v.invoke(Void.class, new Object[]{target_,index_,data_ });
    }
    /* <command>
            <proto>void <name>glGetFloatv</name></proto>
            <param group="GetPName"><ptype>GLenum</ptype> <name>pname</name></param>
            <param len="COMPSIZE(pname)"><ptype>GLfloat</ptype> *<name>data</name></param>
            <glx opcode="116" type="single" />
        </command>
         */
    private static Function _glGetFloatv ;
    public static void glGetFloatv (int pname_ , float[] data_ ) {
        if( _glGetFloatv == null )
            _glGetFloatv = loadFunction("glGetFloatv");
        _glGetFloatv.invoke(Void.class, new Object[]{pname_,data_ });
    }
    /* <command>
            <proto><ptype>GLint</ptype> <name>glGetFragDataIndex</name></proto>
            <param><ptype>GLuint</ptype> <name>program</name></param>
            <param>const <ptype>GLchar</ptype> *<name>name</name></param>
        </command>
         */
    private static Function _glGetFragDataIndex ;
    public static int glGetFragDataIndex (int program_ , String name_ ) {
        if( _glGetFragDataIndex == null )
            _glGetFragDataIndex = loadFunction("glGetFragDataIndex");
        int rv= ( Integer )_glGetFragDataIndex.invoke(Integer.class, new Object[]{program_,name_ });
        return rv;
    }
    /* <command>
            <proto><ptype>GLint</ptype> <name>glGetFragDataLocation</name></proto>
            <param><ptype>GLuint</ptype> <name>program</name></param>
            <param len="COMPSIZE(name)">const <ptype>GLchar</ptype> *<name>name</name></param>
        </command>
         */
    private static Function _glGetFragDataLocation ;
    public static int glGetFragDataLocation (int program_ , String name_ ) {
        if( _glGetFragDataLocation == null )
            _glGetFragDataLocation = loadFunction("glGetFragDataLocation");
        int rv= ( Integer )_glGetFragDataLocation.invoke(Integer.class, new Object[]{program_,name_ });
        return rv;
    }
    /* <command>
            <proto>void <name>glGetFramebufferAttachmentParameteriv</name></proto>
            <param group="FramebufferTarget"><ptype>GLenum</ptype> <name>target</name></param>
            <param group="FramebufferAttachment"><ptype>GLenum</ptype> <name>attachment</name></param>
            <param><ptype>GLenum</ptype> <name>pname</name></param>
            <param len="COMPSIZE(pname)"><ptype>GLint</ptype> *<name>params</name></param>
            <glx opcode="1428" type="vendor" />
        </command>
         */
    private static Function _glGetFramebufferAttachmentParameteriv ;
    public static void glGetFramebufferAttachmentParameteriv (int target_ , int attachment_ , int pname_ , int[] params_ ) {
        if( _glGetFramebufferAttachmentParameteriv == null )
            _glGetFramebufferAttachmentParameteriv = loadFunction("glGetFramebufferAttachmentParameteriv");
        _glGetFramebufferAttachmentParameteriv.invoke(Void.class, new Object[]{target_,attachment_,pname_,params_ });
    }
    /* <command>
            <proto>void <name>glGetFramebufferParameteriv</name></proto>
            <param><ptype>GLenum</ptype> <name>target</name></param>
            <param><ptype>GLenum</ptype> <name>pname</name></param>
            <param len="COMPSIZE(pname)"><ptype>GLint</ptype> *<name>params</name></param>
        </command>
         */
    private static Function _glGetFramebufferParameteriv ;
    public static void glGetFramebufferParameteriv (int target_ , int pname_ , int[] params_ ) {
        if( _glGetFramebufferParameteriv == null )
            _glGetFramebufferParameteriv = loadFunction("glGetFramebufferParameteriv");
        _glGetFramebufferParameteriv.invoke(Void.class, new Object[]{target_,pname_,params_ });
    }
    /* <command>
            <proto><ptype>GLenum</ptype> <name>glGetGraphicsResetStatus</name></proto>
        </command>
         */
    private static Function _glGetGraphicsResetStatus ;
    public static int glGetGraphicsResetStatus () {
        if( _glGetGraphicsResetStatus == null )
            _glGetGraphicsResetStatus = loadFunction("glGetGraphicsResetStatus");
        int rv= ( Integer )_glGetGraphicsResetStatus.invoke(Integer.class, new Object[]{ });
        return rv;
    }
    /* <command>
            <proto>void <name>glGetInteger64i_v</name></proto>
            <param><ptype>GLenum</ptype> <name>target</name></param>
            <param><ptype>GLuint</ptype> <name>index</name></param>
            <param len="COMPSIZE(target)"><ptype>GLint64</ptype> *<name>data</name></param>
        </command>
         */
    private static Function _glGetInteger64i_v ;
    public static void glGetInteger64i_v (int target_ , int index_ , long[] data_ ) {
        if( _glGetInteger64i_v == null )
            _glGetInteger64i_v = loadFunction("glGetInteger64i_v");
        _glGetInteger64i_v.invoke(Void.class, new Object[]{target_,index_,data_ });
    }
    /* <command>
            <proto>void <name>glGetInteger64v</name></proto>
            <param><ptype>GLenum</ptype> <name>pname</name></param>
            <param len="COMPSIZE(pname)"><ptype>GLint64</ptype> *<name>data</name></param>
        </command>
         */
    private static Function _glGetInteger64v ;
    public static void glGetInteger64v (int pname_ , long[] data_ ) {
        if( _glGetInteger64v == null )
            _glGetInteger64v = loadFunction("glGetInteger64v");
        _glGetInteger64v.invoke(Void.class, new Object[]{pname_,data_ });
    }
    /* <command>
            <proto>void <name>glGetIntegeri_v</name></proto>
            <param><ptype>GLenum</ptype> <name>target</name></param>
            <param><ptype>GLuint</ptype> <name>index</name></param>
            <param len="COMPSIZE(target)"><ptype>GLint</ptype> *<name>data</name></param>
        </command>
         */
    private static Function _glGetIntegeri_v ;
    public static void glGetIntegeri_v (int target_ , int index_ , int[] data_ ) {
        if( _glGetIntegeri_v == null )
            _glGetIntegeri_v = loadFunction("glGetIntegeri_v");
        _glGetIntegeri_v.invoke(Void.class, new Object[]{target_,index_,data_ });
    }
    /* <command>
            <proto>void <name>glGetIntegerv</name></proto>
            <param group="GetPName"><ptype>GLenum</ptype> <name>pname</name></param>
            <param len="COMPSIZE(pname)"><ptype>GLint</ptype> *<name>data</name></param>
            <glx opcode="117" type="single" />
        </command>
         */
    private static Function _glGetIntegerv ;
    public static void glGetIntegerv (int pname_ , int[] data_ ) {
        if( _glGetIntegerv == null )
            _glGetIntegerv = loadFunction("glGetIntegerv");
        _glGetIntegerv.invoke(Void.class, new Object[]{pname_,data_ });
    }
    /* <command>
            <proto>void <name>glGetInternalformati64v</name></proto>
            <param><ptype>GLenum</ptype> <name>target</name></param>
            <param><ptype>GLenum</ptype> <name>internalformat</name></param>
            <param><ptype>GLenum</ptype> <name>pname</name></param>
            <param><ptype>GLsizei</ptype> <name>bufSize</name></param>
            <param len="bufSize"><ptype>GLint64</ptype> *<name>params</name></param>
        </command>
         */
    private static Function _glGetInternalformati64v ;
    public static void glGetInternalformati64v (int target_ , int internalformat_ , int pname_ , int bufSize_ , long[] params_ ) {
        if( _glGetInternalformati64v == null )
            _glGetInternalformati64v = loadFunction("glGetInternalformati64v");
        _glGetInternalformati64v.invoke(Void.class, new Object[]{target_,internalformat_,pname_,bufSize_,params_ });
    }
    /* <command>
            <proto>void <name>glGetInternalformativ</name></proto>
            <param><ptype>GLenum</ptype> <name>target</name></param>
            <param><ptype>GLenum</ptype> <name>internalformat</name></param>
            <param><ptype>GLenum</ptype> <name>pname</name></param>
            <param><ptype>GLsizei</ptype> <name>bufSize</name></param>
            <param len="bufSize"><ptype>GLint</ptype> *<name>params</name></param>
        </command>
         */
    private static Function _glGetInternalformativ ;
    public static void glGetInternalformativ (int target_ , int internalformat_ , int pname_ , int bufSize_ , int[] params_ ) {
        if( _glGetInternalformativ == null )
            _glGetInternalformativ = loadFunction("glGetInternalformativ");
        _glGetInternalformativ.invoke(Void.class, new Object[]{target_,internalformat_,pname_,bufSize_,params_ });
    }
    /* <command>
            <proto>void <name>glGetMultisamplefv</name></proto>
            <param><ptype>GLenum</ptype> <name>pname</name></param>
            <param><ptype>GLuint</ptype> <name>index</name></param>
            <param len="COMPSIZE(pname)"><ptype>GLfloat</ptype> *<name>val</name></param>
        </command>
         */
    private static Function _glGetMultisamplefv ;
    public static void glGetMultisamplefv (int pname_ , int index_ , float[] val_ ) {
        if( _glGetMultisamplefv == null )
            _glGetMultisamplefv = loadFunction("glGetMultisamplefv");
        _glGetMultisamplefv.invoke(Void.class, new Object[]{pname_,index_,val_ });
    }
    /* <command>
            <proto>void <name>glGetNamedBufferParameteri64v</name></proto>
            <param><ptype>GLuint</ptype> <name>buffer</name></param>
            <param><ptype>GLenum</ptype> <name>pname</name></param>
            <param><ptype>GLint64</ptype> *<name>params</name></param>
        </command>
         */
    private static Function _glGetNamedBufferParameteri64v ;
    public static void glGetNamedBufferParameteri64v (int buffer_ , int pname_ , long[] params_ ) {
        if( _glGetNamedBufferParameteri64v == null )
            _glGetNamedBufferParameteri64v = loadFunction("glGetNamedBufferParameteri64v");
        _glGetNamedBufferParameteri64v.invoke(Void.class, new Object[]{buffer_,pname_,params_ });
    }
    /* <command>
            <proto>void <name>glGetNamedBufferParameteriv</name></proto>
            <param><ptype>GLuint</ptype> <name>buffer</name></param>
            <param><ptype>GLenum</ptype> <name>pname</name></param>
            <param><ptype>GLint</ptype> *<name>params</name></param>
        </command>
         */
    private static Function _glGetNamedBufferParameteriv ;
    public static void glGetNamedBufferParameteriv (int buffer_ , int pname_ , int[] params_ ) {
        if( _glGetNamedBufferParameteriv == null )
            _glGetNamedBufferParameteriv = loadFunction("glGetNamedBufferParameteriv");
        _glGetNamedBufferParameteriv.invoke(Void.class, new Object[]{buffer_,pname_,params_ });
    }
    /* <command>
            <proto>void <name>glGetNamedBufferPointerv</name></proto>
            <param><ptype>GLuint</ptype> <name>buffer</name></param>
            <param><ptype>GLenum</ptype> <name>pname</name></param>
            <param>void **<name>params</name></param>
        </command>
         */
    private static Function _glGetNamedBufferPointerv ;
    public static void glGetNamedBufferPointerv (int buffer_ , int pname_ , byte[] params_ ) {
        if( _glGetNamedBufferPointerv == null )
            _glGetNamedBufferPointerv = loadFunction("glGetNamedBufferPointerv");
        _glGetNamedBufferPointerv.invoke(Void.class, new Object[]{buffer_,pname_,params_ });
    }
    /* <command>
            <proto>void <name>glGetNamedBufferSubData</name></proto>
            <param><ptype>GLuint</ptype> <name>buffer</name></param>
            <param><ptype>GLintptr</ptype> <name>offset</name></param>
            <param group="BufferSize"><ptype>GLsizeiptr</ptype> <name>size</name></param>
            <param>void *<name>data</name></param>
        </command>
         */
    private static Function _glGetNamedBufferSubData ;
    public static void glGetNamedBufferSubData (int buffer_ , long offset_ , long size_ , byte[] data_ ) {
        if( _glGetNamedBufferSubData == null )
            _glGetNamedBufferSubData = loadFunction("glGetNamedBufferSubData");
        _glGetNamedBufferSubData.invoke(Void.class, new Object[]{buffer_,offset_,size_,data_ });
    }
    /* <command>
            <proto>void <name>glGetNamedFramebufferAttachmentParameteriv</name></proto>
            <param><ptype>GLuint</ptype> <name>framebuffer</name></param>
            <param><ptype>GLenum</ptype> <name>attachment</name></param>
            <param><ptype>GLenum</ptype> <name>pname</name></param>
            <param><ptype>GLint</ptype> *<name>params</name></param>
        </command>
         */
    private static Function _glGetNamedFramebufferAttachmentParameteriv ;
    public static void glGetNamedFramebufferAttachmentParameteriv (int framebuffer_ , int attachment_ , int pname_ , int[] params_ ) {
        if( _glGetNamedFramebufferAttachmentParameteriv == null )
            _glGetNamedFramebufferAttachmentParameteriv = loadFunction("glGetNamedFramebufferAttachmentParameteriv");
        _glGetNamedFramebufferAttachmentParameteriv.invoke(Void.class, new Object[]{framebuffer_,attachment_,pname_,params_ });
    }
    /* <command>
            <proto>void <name>glGetNamedFramebufferParameteriv</name></proto>
            <param><ptype>GLuint</ptype> <name>framebuffer</name></param>
            <param><ptype>GLenum</ptype> <name>pname</name></param>
            <param><ptype>GLint</ptype> *<name>param</name></param>
        </command>
         */
    private static Function _glGetNamedFramebufferParameteriv ;
    public static void glGetNamedFramebufferParameteriv (int framebuffer_ , int pname_ , int[] param_ ) {
        if( _glGetNamedFramebufferParameteriv == null )
            _glGetNamedFramebufferParameteriv = loadFunction("glGetNamedFramebufferParameteriv");
        _glGetNamedFramebufferParameteriv.invoke(Void.class, new Object[]{framebuffer_,pname_,param_ });
    }
    /* <command>
            <proto>void <name>glGetNamedRenderbufferParameteriv</name></proto>
            <param><ptype>GLuint</ptype> <name>renderbuffer</name></param>
            <param><ptype>GLenum</ptype> <name>pname</name></param>
            <param><ptype>GLint</ptype> *<name>params</name></param>
        </command>
         */
    private static Function _glGetNamedRenderbufferParameteriv ;
    public static void glGetNamedRenderbufferParameteriv (int renderbuffer_ , int pname_ , int[] params_ ) {
        if( _glGetNamedRenderbufferParameteriv == null )
            _glGetNamedRenderbufferParameteriv = loadFunction("glGetNamedRenderbufferParameteriv");
        _glGetNamedRenderbufferParameteriv.invoke(Void.class, new Object[]{renderbuffer_,pname_,params_ });
    }
    /* <command>
            <proto>void <name>glGetObjectLabel</name></proto>
            <param><ptype>GLenum</ptype> <name>identifier</name></param>
            <param><ptype>GLuint</ptype> <name>name</name></param>
            <param><ptype>GLsizei</ptype> <name>bufSize</name></param>
            <param len="1"><ptype>GLsizei</ptype> *<name>length</name></param>
            <param len="bufSize"><ptype>GLchar</ptype> *<name>label</name></param>
        </command>
         */
    private static Function _glGetObjectLabel ;
    public static void glGetObjectLabel (int identifier_ , int name_ , int bufSize_ , int[] length_ , byte[] label_ ) {
        if( _glGetObjectLabel == null )
            _glGetObjectLabel = loadFunction("glGetObjectLabel");
        _glGetObjectLabel.invoke(Void.class, new Object[]{identifier_,name_,bufSize_,length_,label_ });
    }
    /* <command>
            <proto>void <name>glGetObjectPtrLabel</name></proto>
            <param>const void *<name>ptr</name></param>
            <param><ptype>GLsizei</ptype> <name>bufSize</name></param>
            <param len="1"><ptype>GLsizei</ptype> *<name>length</name></param>
            <param len="bufSize"><ptype>GLchar</ptype> *<name>label</name></param>
        </command>
         */
    private static Function _glGetObjectPtrLabel ;
    public static void glGetObjectPtrLabel (byte[] ptr_ , int bufSize_ , int[] length_ , byte[] label_ ) {
        if( _glGetObjectPtrLabel == null )
            _glGetObjectPtrLabel = loadFunction("glGetObjectPtrLabel");
        _glGetObjectPtrLabel.invoke(Void.class, new Object[]{ptr_,bufSize_,length_,label_ });
    }
    /* <command>
            <proto>void <name>glGetProgramBinary</name></proto>
            <param><ptype>GLuint</ptype> <name>program</name></param>
            <param><ptype>GLsizei</ptype> <name>bufSize</name></param>
            <param len="1"><ptype>GLsizei</ptype> *<name>length</name></param>
            <param len="1"><ptype>GLenum</ptype> *<name>binaryFormat</name></param>
            <param len="bufSize">void *<name>binary</name></param>
        </command>
         */
    private static Function _glGetProgramBinary ;
    public static void glGetProgramBinary (int program_ , int bufSize_ , int[] length_ , int[] binaryFormat_ , byte[] binary_ ) {
        if( _glGetProgramBinary == null )
            _glGetProgramBinary = loadFunction("glGetProgramBinary");
        _glGetProgramBinary.invoke(Void.class, new Object[]{program_,bufSize_,length_,binaryFormat_,binary_ });
    }
    /* <command>
            <proto>void <name>glGetProgramInfoLog</name></proto>
            <param><ptype>GLuint</ptype> <name>program</name></param>
            <param><ptype>GLsizei</ptype> <name>bufSize</name></param>
            <param len="1"><ptype>GLsizei</ptype> *<name>length</name></param>
            <param len="bufSize"><ptype>GLchar</ptype> *<name>infoLog</name></param>
            <glx opcode="201" type="single" />
        </command>
         */
    private static Function _glGetProgramInfoLog ;
    public static void glGetProgramInfoLog (int program_ , int bufSize_ , int[] length_ , byte[] infoLog_ ) {
        if( _glGetProgramInfoLog == null )
            _glGetProgramInfoLog = loadFunction("glGetProgramInfoLog");
        _glGetProgramInfoLog.invoke(Void.class, new Object[]{program_,bufSize_,length_,infoLog_ });
    }
    /* <command>
            <proto>void <name>glGetProgramInterfaceiv</name></proto>
            <param><ptype>GLuint</ptype> <name>program</name></param>
            <param><ptype>GLenum</ptype> <name>programInterface</name></param>
            <param><ptype>GLenum</ptype> <name>pname</name></param>
            <param len="COMPSIZE(pname)"><ptype>GLint</ptype> *<name>params</name></param>
        </command>
         */
    private static Function _glGetProgramInterfaceiv ;
    public static void glGetProgramInterfaceiv (int program_ , int programInterface_ , int pname_ , int[] params_ ) {
        if( _glGetProgramInterfaceiv == null )
            _glGetProgramInterfaceiv = loadFunction("glGetProgramInterfaceiv");
        _glGetProgramInterfaceiv.invoke(Void.class, new Object[]{program_,programInterface_,pname_,params_ });
    }
    /* <command>
            <proto>void <name>glGetProgramPipelineInfoLog</name></proto>
            <param><ptype>GLuint</ptype> <name>pipeline</name></param>
            <param><ptype>GLsizei</ptype> <name>bufSize</name></param>
            <param len="1"><ptype>GLsizei</ptype> *<name>length</name></param>
            <param len="bufSize"><ptype>GLchar</ptype> *<name>infoLog</name></param>
        </command>
         */
    private static Function _glGetProgramPipelineInfoLog ;
    public static void glGetProgramPipelineInfoLog (int pipeline_ , int bufSize_ , int[] length_ , byte[] infoLog_ ) {
        if( _glGetProgramPipelineInfoLog == null )
            _glGetProgramPipelineInfoLog = loadFunction("glGetProgramPipelineInfoLog");
        _glGetProgramPipelineInfoLog.invoke(Void.class, new Object[]{pipeline_,bufSize_,length_,infoLog_ });
    }
    /* <command>
            <proto>void <name>glGetProgramPipelineiv</name></proto>
            <param><ptype>GLuint</ptype> <name>pipeline</name></param>
            <param><ptype>GLenum</ptype> <name>pname</name></param>
            <param len="COMPSIZE(pname)"><ptype>GLint</ptype> *<name>params</name></param>
        </command>
         */
    private static Function _glGetProgramPipelineiv ;
    public static void glGetProgramPipelineiv (int pipeline_ , int pname_ , int[] params_ ) {
        if( _glGetProgramPipelineiv == null )
            _glGetProgramPipelineiv = loadFunction("glGetProgramPipelineiv");
        _glGetProgramPipelineiv.invoke(Void.class, new Object[]{pipeline_,pname_,params_ });
    }
    /* <command>
            <proto><ptype>GLuint</ptype> <name>glGetProgramResourceIndex</name></proto>
            <param><ptype>GLuint</ptype> <name>program</name></param>
            <param><ptype>GLenum</ptype> <name>programInterface</name></param>
            <param len="COMPSIZE(name)">const <ptype>GLchar</ptype> *<name>name</name></param>
        </command>
         */
    private static Function _glGetProgramResourceIndex ;
    public static int glGetProgramResourceIndex (int program_ , int programInterface_ , String name_ ) {
        if( _glGetProgramResourceIndex == null )
            _glGetProgramResourceIndex = loadFunction("glGetProgramResourceIndex");
        int rv= ( Integer )_glGetProgramResourceIndex.invoke(Integer.class, new Object[]{program_,programInterface_,name_ });
        return rv;
    }
    /* <command>
            <proto><ptype>GLint</ptype> <name>glGetProgramResourceLocation</name></proto>
            <param><ptype>GLuint</ptype> <name>program</name></param>
            <param><ptype>GLenum</ptype> <name>programInterface</name></param>
            <param len="COMPSIZE(name)">const <ptype>GLchar</ptype> *<name>name</name></param>
        </command>
         */
    private static Function _glGetProgramResourceLocation ;
    public static int glGetProgramResourceLocation (int program_ , int programInterface_ , String name_ ) {
        if( _glGetProgramResourceLocation == null )
            _glGetProgramResourceLocation = loadFunction("glGetProgramResourceLocation");
        int rv= ( Integer )_glGetProgramResourceLocation.invoke(Integer.class, new Object[]{program_,programInterface_,name_ });
        return rv;
    }
    /* <command>
            <proto><ptype>GLint</ptype> <name>glGetProgramResourceLocationIndex</name></proto>
            <param><ptype>GLuint</ptype> <name>program</name></param>
            <param><ptype>GLenum</ptype> <name>programInterface</name></param>
            <param len="COMPSIZE(name)">const <ptype>GLchar</ptype> *<name>name</name></param>
        </command>
         */
    private static Function _glGetProgramResourceLocationIndex ;
    public static int glGetProgramResourceLocationIndex (int program_ , int programInterface_ , String name_ ) {
        if( _glGetProgramResourceLocationIndex == null )
            _glGetProgramResourceLocationIndex = loadFunction("glGetProgramResourceLocationIndex");
        int rv= ( Integer )_glGetProgramResourceLocationIndex.invoke(Integer.class, new Object[]{program_,programInterface_,name_ });
        return rv;
    }
    /* <command>
            <proto>void <name>glGetProgramResourceName</name></proto>
            <param><ptype>GLuint</ptype> <name>program</name></param>
            <param><ptype>GLenum</ptype> <name>programInterface</name></param>
            <param><ptype>GLuint</ptype> <name>index</name></param>
            <param><ptype>GLsizei</ptype> <name>bufSize</name></param>
            <param len="1"><ptype>GLsizei</ptype> *<name>length</name></param>
            <param len="bufSize"><ptype>GLchar</ptype> *<name>name</name></param>
        </command>
         */
    private static Function _glGetProgramResourceName ;
    public static void glGetProgramResourceName (int program_ , int programInterface_ , int index_ , int bufSize_ , int[] length_ , byte[] name_ ) {
        if( _glGetProgramResourceName == null )
            _glGetProgramResourceName = loadFunction("glGetProgramResourceName");
        _glGetProgramResourceName.invoke(Void.class, new Object[]{program_,programInterface_,index_,bufSize_,length_,name_ });
    }
    /* <command>
            <proto>void <name>glGetProgramResourceiv</name></proto>
            <param><ptype>GLuint</ptype> <name>program</name></param>
            <param><ptype>GLenum</ptype> <name>programInterface</name></param>
            <param><ptype>GLuint</ptype> <name>index</name></param>
            <param><ptype>GLsizei</ptype> <name>propCount</name></param>
            <param len="propCount">const <ptype>GLenum</ptype> *<name>props</name></param>
            <param><ptype>GLsizei</ptype> <name>bufSize</name></param>
            <param len="1"><ptype>GLsizei</ptype> *<name>length</name></param>
            <param len="bufSize"><ptype>GLint</ptype> *<name>params</name></param>
        </command>
         */
    private static Function _glGetProgramResourceiv ;
    public static void glGetProgramResourceiv (int program_ , int programInterface_ , int index_ , int propCount_ , int[] props_ , int bufSize_ , int[] length_ , int[] params_ ) {
        if( _glGetProgramResourceiv == null )
            _glGetProgramResourceiv = loadFunction("glGetProgramResourceiv");
        _glGetProgramResourceiv.invoke(Void.class, new Object[]{program_,programInterface_,index_,propCount_,props_,bufSize_,length_,params_ });
    }
    /* <command>
            <proto>void <name>glGetProgramStageiv</name></proto>
            <param><ptype>GLuint</ptype> <name>program</name></param>
            <param><ptype>GLenum</ptype> <name>shadertype</name></param>
            <param><ptype>GLenum</ptype> <name>pname</name></param>
            <param len="1"><ptype>GLint</ptype> *<name>values</name></param>
        </command>
         */
    private static Function _glGetProgramStageiv ;
    public static void glGetProgramStageiv (int program_ , int shadertype_ , int pname_ , int[] values_ ) {
        if( _glGetProgramStageiv == null )
            _glGetProgramStageiv = loadFunction("glGetProgramStageiv");
        _glGetProgramStageiv.invoke(Void.class, new Object[]{program_,shadertype_,pname_,values_ });
    }
    /* <command>
            <proto>void <name>glGetProgramiv</name></proto>
            <param><ptype>GLuint</ptype> <name>program</name></param>
            <param><ptype>GLenum</ptype> <name>pname</name></param>
            <param len="COMPSIZE(pname)"><ptype>GLint</ptype> *<name>params</name></param>
            <glx opcode="199" type="single" />
        </command>
         */
    private static Function _glGetProgramiv ;
    public static void glGetProgramiv (int program_ , int pname_ , int[] params_ ) {
        if( _glGetProgramiv == null )
            _glGetProgramiv = loadFunction("glGetProgramiv");
        _glGetProgramiv.invoke(Void.class, new Object[]{program_,pname_,params_ });
    }
    /* <command>
            <proto>void <name>glGetQueryBufferObjecti64v</name></proto>
            <param><ptype>GLuint</ptype> <name>id</name></param>
            <param><ptype>GLuint</ptype> <name>buffer</name></param>
            <param><ptype>GLenum</ptype> <name>pname</name></param>
            <param><ptype>GLintptr</ptype> <name>offset</name></param>
        </command>
         */
    private static Function _glGetQueryBufferObjecti64v ;
    public static void glGetQueryBufferObjecti64v (int id_ , int buffer_ , int pname_ , long offset_ ) {
        if( _glGetQueryBufferObjecti64v == null )
            _glGetQueryBufferObjecti64v = loadFunction("glGetQueryBufferObjecti64v");
        _glGetQueryBufferObjecti64v.invoke(Void.class, new Object[]{id_,buffer_,pname_,offset_ });
    }
    /* <command>
            <proto>void <name>glGetQueryBufferObjectiv</name></proto>
            <param><ptype>GLuint</ptype> <name>id</name></param>
            <param><ptype>GLuint</ptype> <name>buffer</name></param>
            <param><ptype>GLenum</ptype> <name>pname</name></param>
            <param><ptype>GLintptr</ptype> <name>offset</name></param>
        </command>
         */
    private static Function _glGetQueryBufferObjectiv ;
    public static void glGetQueryBufferObjectiv (int id_ , int buffer_ , int pname_ , long offset_ ) {
        if( _glGetQueryBufferObjectiv == null )
            _glGetQueryBufferObjectiv = loadFunction("glGetQueryBufferObjectiv");
        _glGetQueryBufferObjectiv.invoke(Void.class, new Object[]{id_,buffer_,pname_,offset_ });
    }
    /* <command>
            <proto>void <name>glGetQueryBufferObjectui64v</name></proto>
            <param><ptype>GLuint</ptype> <name>id</name></param>
            <param><ptype>GLuint</ptype> <name>buffer</name></param>
            <param><ptype>GLenum</ptype> <name>pname</name></param>
            <param><ptype>GLintptr</ptype> <name>offset</name></param>
        </command>
         */
    private static Function _glGetQueryBufferObjectui64v ;
    public static void glGetQueryBufferObjectui64v (int id_ , int buffer_ , int pname_ , long offset_ ) {
        if( _glGetQueryBufferObjectui64v == null )
            _glGetQueryBufferObjectui64v = loadFunction("glGetQueryBufferObjectui64v");
        _glGetQueryBufferObjectui64v.invoke(Void.class, new Object[]{id_,buffer_,pname_,offset_ });
    }
    /* <command>
            <proto>void <name>glGetQueryBufferObjectuiv</name></proto>
            <param><ptype>GLuint</ptype> <name>id</name></param>
            <param><ptype>GLuint</ptype> <name>buffer</name></param>
            <param><ptype>GLenum</ptype> <name>pname</name></param>
            <param><ptype>GLintptr</ptype> <name>offset</name></param>
        </command>
         */
    private static Function _glGetQueryBufferObjectuiv ;
    public static void glGetQueryBufferObjectuiv (int id_ , int buffer_ , int pname_ , long offset_ ) {
        if( _glGetQueryBufferObjectuiv == null )
            _glGetQueryBufferObjectuiv = loadFunction("glGetQueryBufferObjectuiv");
        _glGetQueryBufferObjectuiv.invoke(Void.class, new Object[]{id_,buffer_,pname_,offset_ });
    }
    /* <command>
            <proto>void <name>glGetQueryIndexediv</name></proto>
            <param><ptype>GLenum</ptype> <name>target</name></param>
            <param><ptype>GLuint</ptype> <name>index</name></param>
            <param><ptype>GLenum</ptype> <name>pname</name></param>
            <param len="COMPSIZE(pname)"><ptype>GLint</ptype> *<name>params</name></param>
        </command>
         */
    private static Function _glGetQueryIndexediv ;
    public static void glGetQueryIndexediv (int target_ , int index_ , int pname_ , int[] params_ ) {
        if( _glGetQueryIndexediv == null )
            _glGetQueryIndexediv = loadFunction("glGetQueryIndexediv");
        _glGetQueryIndexediv.invoke(Void.class, new Object[]{target_,index_,pname_,params_ });
    }
    /* <command>
            <proto>void <name>glGetQueryObjecti64v</name></proto>
            <param><ptype>GLuint</ptype> <name>id</name></param>
            <param><ptype>GLenum</ptype> <name>pname</name></param>
            <param len="COMPSIZE(pname)"><ptype>GLint64</ptype> *<name>params</name></param>
        </command>
         */
    private static Function _glGetQueryObjecti64v ;
    public static void glGetQueryObjecti64v (int id_ , int pname_ , long[] params_ ) {
        if( _glGetQueryObjecti64v == null )
            _glGetQueryObjecti64v = loadFunction("glGetQueryObjecti64v");
        _glGetQueryObjecti64v.invoke(Void.class, new Object[]{id_,pname_,params_ });
    }
    /* <command>
            <proto>void <name>glGetQueryObjectiv</name></proto>
            <param><ptype>GLuint</ptype> <name>id</name></param>
            <param><ptype>GLenum</ptype> <name>pname</name></param>
            <param len="COMPSIZE(pname)"><ptype>GLint</ptype> *<name>params</name></param>
            <glx opcode="165" type="single" />
        </command>
         */
    private static Function _glGetQueryObjectiv ;
    public static void glGetQueryObjectiv (int id_ , int pname_ , int[] params_ ) {
        if( _glGetQueryObjectiv == null )
            _glGetQueryObjectiv = loadFunction("glGetQueryObjectiv");
        _glGetQueryObjectiv.invoke(Void.class, new Object[]{id_,pname_,params_ });
    }
    /* <command>
            <proto>void <name>glGetQueryObjectui64v</name></proto>
            <param><ptype>GLuint</ptype> <name>id</name></param>
            <param><ptype>GLenum</ptype> <name>pname</name></param>
            <param len="COMPSIZE(pname)"><ptype>GLuint64</ptype> *<name>params</name></param>
        </command>
         */
    private static Function _glGetQueryObjectui64v ;
    public static void glGetQueryObjectui64v (int id_ , int pname_ , long[] params_ ) {
        if( _glGetQueryObjectui64v == null )
            _glGetQueryObjectui64v = loadFunction("glGetQueryObjectui64v");
        _glGetQueryObjectui64v.invoke(Void.class, new Object[]{id_,pname_,params_ });
    }
    /* <command>
            <proto>void <name>glGetQueryObjectuiv</name></proto>
            <param><ptype>GLuint</ptype> <name>id</name></param>
            <param><ptype>GLenum</ptype> <name>pname</name></param>
            <param len="COMPSIZE(pname)"><ptype>GLuint</ptype> *<name>params</name></param>
            <glx opcode="166" type="single" />
        </command>
         */
    private static Function _glGetQueryObjectuiv ;
    public static void glGetQueryObjectuiv (int id_ , int pname_ , int[] params_ ) {
        if( _glGetQueryObjectuiv == null )
            _glGetQueryObjectuiv = loadFunction("glGetQueryObjectuiv");
        _glGetQueryObjectuiv.invoke(Void.class, new Object[]{id_,pname_,params_ });
    }
    /* <command>
            <proto>void <name>glGetQueryiv</name></proto>
            <param><ptype>GLenum</ptype> <name>target</name></param>
            <param><ptype>GLenum</ptype> <name>pname</name></param>
            <param len="COMPSIZE(pname)"><ptype>GLint</ptype> *<name>params</name></param>
            <glx opcode="164" type="single" />
        </command>
         */
    private static Function _glGetQueryiv ;
    public static void glGetQueryiv (int target_ , int pname_ , int[] params_ ) {
        if( _glGetQueryiv == null )
            _glGetQueryiv = loadFunction("glGetQueryiv");
        _glGetQueryiv.invoke(Void.class, new Object[]{target_,pname_,params_ });
    }
    /* <command>
            <proto>void <name>glGetRenderbufferParameteriv</name></proto>
            <param group="RenderbufferTarget"><ptype>GLenum</ptype> <name>target</name></param>
            <param><ptype>GLenum</ptype> <name>pname</name></param>
            <param len="COMPSIZE(pname)"><ptype>GLint</ptype> *<name>params</name></param>
            <glx opcode="1424" type="vendor" />
        </command>
         */
    private static Function _glGetRenderbufferParameteriv ;
    public static void glGetRenderbufferParameteriv (int target_ , int pname_ , int[] params_ ) {
        if( _glGetRenderbufferParameteriv == null )
            _glGetRenderbufferParameteriv = loadFunction("glGetRenderbufferParameteriv");
        _glGetRenderbufferParameteriv.invoke(Void.class, new Object[]{target_,pname_,params_ });
    }
    /* <command>
            <proto>void <name>glGetSamplerParameterIiv</name></proto>
            <param><ptype>GLuint</ptype> <name>sampler</name></param>
            <param><ptype>GLenum</ptype> <name>pname</name></param>
            <param len="COMPSIZE(pname)"><ptype>GLint</ptype> *<name>params</name></param>
        </command>
         */
    private static Function _glGetSamplerParameterIiv ;
    public static void glGetSamplerParameterIiv (int sampler_ , int pname_ , int[] params_ ) {
        if( _glGetSamplerParameterIiv == null )
            _glGetSamplerParameterIiv = loadFunction("glGetSamplerParameterIiv");
        _glGetSamplerParameterIiv.invoke(Void.class, new Object[]{sampler_,pname_,params_ });
    }
    /* <command>
            <proto>void <name>glGetSamplerParameterIuiv</name></proto>
            <param><ptype>GLuint</ptype> <name>sampler</name></param>
            <param><ptype>GLenum</ptype> <name>pname</name></param>
            <param len="COMPSIZE(pname)"><ptype>GLuint</ptype> *<name>params</name></param>
        </command>
         */
    private static Function _glGetSamplerParameterIuiv ;
    public static void glGetSamplerParameterIuiv (int sampler_ , int pname_ , int[] params_ ) {
        if( _glGetSamplerParameterIuiv == null )
            _glGetSamplerParameterIuiv = loadFunction("glGetSamplerParameterIuiv");
        _glGetSamplerParameterIuiv.invoke(Void.class, new Object[]{sampler_,pname_,params_ });
    }
    /* <command>
            <proto>void <name>glGetSamplerParameterfv</name></proto>
            <param><ptype>GLuint</ptype> <name>sampler</name></param>
            <param><ptype>GLenum</ptype> <name>pname</name></param>
            <param len="COMPSIZE(pname)"><ptype>GLfloat</ptype> *<name>params</name></param>
        </command>
         */
    private static Function _glGetSamplerParameterfv ;
    public static void glGetSamplerParameterfv (int sampler_ , int pname_ , float[] params_ ) {
        if( _glGetSamplerParameterfv == null )
            _glGetSamplerParameterfv = loadFunction("glGetSamplerParameterfv");
        _glGetSamplerParameterfv.invoke(Void.class, new Object[]{sampler_,pname_,params_ });
    }
    /* <command>
            <proto>void <name>glGetSamplerParameteriv</name></proto>
            <param><ptype>GLuint</ptype> <name>sampler</name></param>
            <param><ptype>GLenum</ptype> <name>pname</name></param>
            <param len="COMPSIZE(pname)"><ptype>GLint</ptype> *<name>params</name></param>
        </command>
         */
    private static Function _glGetSamplerParameteriv ;
    public static void glGetSamplerParameteriv (int sampler_ , int pname_ , int[] params_ ) {
        if( _glGetSamplerParameteriv == null )
            _glGetSamplerParameteriv = loadFunction("glGetSamplerParameteriv");
        _glGetSamplerParameteriv.invoke(Void.class, new Object[]{sampler_,pname_,params_ });
    }
    /* <command>
            <proto>void <name>glGetShaderInfoLog</name></proto>
            <param><ptype>GLuint</ptype> <name>shader</name></param>
            <param><ptype>GLsizei</ptype> <name>bufSize</name></param>
            <param len="1"><ptype>GLsizei</ptype> *<name>length</name></param>
            <param len="bufSize"><ptype>GLchar</ptype> *<name>infoLog</name></param>
            <glx opcode="200" type="single" />
        </command>
         */
    private static Function _glGetShaderInfoLog ;
    public static void glGetShaderInfoLog (int shader_ , int bufSize_ , int[] length_ , byte[] infoLog_ ) {
        if( _glGetShaderInfoLog == null )
            _glGetShaderInfoLog = loadFunction("glGetShaderInfoLog");
        _glGetShaderInfoLog.invoke(Void.class, new Object[]{shader_,bufSize_,length_,infoLog_ });
    }
    /* <command>
            <proto>void <name>glGetShaderPrecisionFormat</name></proto>
            <param><ptype>GLenum</ptype> <name>shadertype</name></param>
            <param><ptype>GLenum</ptype> <name>precisiontype</name></param>
            <param len="2"><ptype>GLint</ptype> *<name>range</name></param>
            <param len="2"><ptype>GLint</ptype> *<name>precision</name></param>
        </command>
         */
    private static Function _glGetShaderPrecisionFormat ;
    public static void glGetShaderPrecisionFormat (int shadertype_ , int precisiontype_ , int[] range_ , int[] precision_ ) {
        if( _glGetShaderPrecisionFormat == null )
            _glGetShaderPrecisionFormat = loadFunction("glGetShaderPrecisionFormat");
        _glGetShaderPrecisionFormat.invoke(Void.class, new Object[]{shadertype_,precisiontype_,range_,precision_ });
    }
    /* <command>
            <proto>void <name>glGetShaderSource</name></proto>
            <param><ptype>GLuint</ptype> <name>shader</name></param>
            <param><ptype>GLsizei</ptype> <name>bufSize</name></param>
            <param len="1"><ptype>GLsizei</ptype> *<name>length</name></param>
            <param len="bufSize"><ptype>GLchar</ptype> *<name>source</name></param>
        </command>
         */
    private static Function _glGetShaderSource ;
    public static void glGetShaderSource (int shader_ , int bufSize_ , int[] length_ , byte[] source_ ) {
        if( _glGetShaderSource == null )
            _glGetShaderSource = loadFunction("glGetShaderSource");
        _glGetShaderSource.invoke(Void.class, new Object[]{shader_,bufSize_,length_,source_ });
    }
    /* <command>
            <proto>void <name>glGetShaderiv</name></proto>
            <param><ptype>GLuint</ptype> <name>shader</name></param>
            <param><ptype>GLenum</ptype> <name>pname</name></param>
            <param len="COMPSIZE(pname)"><ptype>GLint</ptype> *<name>params</name></param>
            <glx opcode="198" type="single" />
        </command>
         */
    private static Function _glGetShaderiv ;
    public static void glGetShaderiv (int shader_ , int pname_ , int[] params_ ) {
        if( _glGetShaderiv == null )
            _glGetShaderiv = loadFunction("glGetShaderiv");
        _glGetShaderiv.invoke(Void.class, new Object[]{shader_,pname_,params_ });
    }
    /* <command>
            <proto><ptype>GLuint</ptype> <name>glGetSubroutineIndex</name></proto>
            <param><ptype>GLuint</ptype> <name>program</name></param>
            <param><ptype>GLenum</ptype> <name>shadertype</name></param>
            <param>const <ptype>GLchar</ptype> *<name>name</name></param>
        </command>
         */
    private static Function _glGetSubroutineIndex ;
    public static int glGetSubroutineIndex (int program_ , int shadertype_ , String name_ ) {
        if( _glGetSubroutineIndex == null )
            _glGetSubroutineIndex = loadFunction("glGetSubroutineIndex");
        int rv= ( Integer )_glGetSubroutineIndex.invoke(Integer.class, new Object[]{program_,shadertype_,name_ });
        return rv;
    }
    /* <command>
            <proto><ptype>GLint</ptype> <name>glGetSubroutineUniformLocation</name></proto>
            <param><ptype>GLuint</ptype> <name>program</name></param>
            <param><ptype>GLenum</ptype> <name>shadertype</name></param>
            <param>const <ptype>GLchar</ptype> *<name>name</name></param>
        </command>
         */
    private static Function _glGetSubroutineUniformLocation ;
    public static int glGetSubroutineUniformLocation (int program_ , int shadertype_ , String name_ ) {
        if( _glGetSubroutineUniformLocation == null )
            _glGetSubroutineUniformLocation = loadFunction("glGetSubroutineUniformLocation");
        int rv= ( Integer )_glGetSubroutineUniformLocation.invoke(Integer.class, new Object[]{program_,shadertype_,name_ });
        return rv;
    }
    /* <command>
            <proto>void <name>glGetSynciv</name></proto>
            <param group="sync"><ptype>GLsync</ptype> <name>sync</name></param>
            <param><ptype>GLenum</ptype> <name>pname</name></param>
            <param><ptype>GLsizei</ptype> <name>bufSize</name></param>
            <param len="1"><ptype>GLsizei</ptype> *<name>length</name></param>
            <param len="bufSize"><ptype>GLint</ptype> *<name>values</name></param>
        </command>
         */
    private static Function _glGetSynciv ;
    public static void glGetSynciv (Pointer sync_ , int pname_ , int bufSize_ , int[] length_ , int[] values_ ) {
        if( _glGetSynciv == null )
            _glGetSynciv = loadFunction("glGetSynciv");
        _glGetSynciv.invoke(Void.class, new Object[]{sync_,pname_,bufSize_,length_,values_ });
    }
    /* <command>
            <proto>void <name>glGetTexImage</name></proto>
            <param group="TextureTarget"><ptype>GLenum</ptype> <name>target</name></param>
            <param group="CheckedInt32"><ptype>GLint</ptype> <name>level</name></param>
            <param group="PixelFormat"><ptype>GLenum</ptype> <name>format</name></param>
            <param group="PixelType"><ptype>GLenum</ptype> <name>type</name></param>
            <param len="COMPSIZE(target,level,format,type)">void *<name>pixels</name></param>
            <glx opcode="135" type="single" />
            <glx comment="PBO protocol" name="glGetTexImagePBO" opcode="344" type="render" />
        </command>
         */
    private static Function _glGetTexImage ;
    public static void glGetTexImage (int target_ , int level_ , int format_ , int type_ , byte[] pixels_ ) {
        if( _glGetTexImage == null )
            _glGetTexImage = loadFunction("glGetTexImage");
        _glGetTexImage.invoke(Void.class, new Object[]{target_,level_,format_,type_,pixels_ });
    }
    /* <command>
            <proto>void <name>glGetTexLevelParameterfv</name></proto>
            <param group="TextureTarget"><ptype>GLenum</ptype> <name>target</name></param>
            <param group="CheckedInt32"><ptype>GLint</ptype> <name>level</name></param>
            <param group="GetTextureParameter"><ptype>GLenum</ptype> <name>pname</name></param>
            <param len="COMPSIZE(pname)"><ptype>GLfloat</ptype> *<name>params</name></param>
            <glx opcode="138" type="single" />
        </command>
         */
    private static Function _glGetTexLevelParameterfv ;
    public static void glGetTexLevelParameterfv (int target_ , int level_ , int pname_ , float[] params_ ) {
        if( _glGetTexLevelParameterfv == null )
            _glGetTexLevelParameterfv = loadFunction("glGetTexLevelParameterfv");
        _glGetTexLevelParameterfv.invoke(Void.class, new Object[]{target_,level_,pname_,params_ });
    }
    /* <command>
            <proto>void <name>glGetTexLevelParameteriv</name></proto>
            <param group="TextureTarget"><ptype>GLenum</ptype> <name>target</name></param>
            <param group="CheckedInt32"><ptype>GLint</ptype> <name>level</name></param>
            <param group="GetTextureParameter"><ptype>GLenum</ptype> <name>pname</name></param>
            <param len="COMPSIZE(pname)"><ptype>GLint</ptype> *<name>params</name></param>
            <glx opcode="139" type="single" />
        </command>
         */
    private static Function _glGetTexLevelParameteriv ;
    public static void glGetTexLevelParameteriv (int target_ , int level_ , int pname_ , int[] params_ ) {
        if( _glGetTexLevelParameteriv == null )
            _glGetTexLevelParameteriv = loadFunction("glGetTexLevelParameteriv");
        _glGetTexLevelParameteriv.invoke(Void.class, new Object[]{target_,level_,pname_,params_ });
    }
    /* <command>
            <proto>void <name>glGetTexParameterIiv</name></proto>
            <param group="TextureTarget"><ptype>GLenum</ptype> <name>target</name></param>
            <param group="GetTextureParameter"><ptype>GLenum</ptype> <name>pname</name></param>
            <param len="COMPSIZE(pname)"><ptype>GLint</ptype> *<name>params</name></param>
            <glx opcode="203" type="single" />
        </command>
         */
    private static Function _glGetTexParameterIiv ;
    public static void glGetTexParameterIiv (int target_ , int pname_ , int[] params_ ) {
        if( _glGetTexParameterIiv == null )
            _glGetTexParameterIiv = loadFunction("glGetTexParameterIiv");
        _glGetTexParameterIiv.invoke(Void.class, new Object[]{target_,pname_,params_ });
    }
    /* <command>
            <proto>void <name>glGetTexParameterIuiv</name></proto>
            <param group="TextureTarget"><ptype>GLenum</ptype> <name>target</name></param>
            <param group="GetTextureParameter"><ptype>GLenum</ptype> <name>pname</name></param>
            <param len="COMPSIZE(pname)"><ptype>GLuint</ptype> *<name>params</name></param>
            <glx opcode="204" type="single" />
        </command>
         */
    private static Function _glGetTexParameterIuiv ;
    public static void glGetTexParameterIuiv (int target_ , int pname_ , int[] params_ ) {
        if( _glGetTexParameterIuiv == null )
            _glGetTexParameterIuiv = loadFunction("glGetTexParameterIuiv");
        _glGetTexParameterIuiv.invoke(Void.class, new Object[]{target_,pname_,params_ });
    }
    /* <command>
            <proto>void <name>glGetTexParameterfv</name></proto>
            <param group="TextureTarget"><ptype>GLenum</ptype> <name>target</name></param>
            <param group="GetTextureParameter"><ptype>GLenum</ptype> <name>pname</name></param>
            <param len="COMPSIZE(pname)"><ptype>GLfloat</ptype> *<name>params</name></param>
            <glx opcode="136" type="single" />
        </command>
         */
    private static Function _glGetTexParameterfv ;
    public static void glGetTexParameterfv (int target_ , int pname_ , float[] params_ ) {
        if( _glGetTexParameterfv == null )
            _glGetTexParameterfv = loadFunction("glGetTexParameterfv");
        _glGetTexParameterfv.invoke(Void.class, new Object[]{target_,pname_,params_ });
    }
    /* <command>
            <proto>void <name>glGetTexParameteriv</name></proto>
            <param group="TextureTarget"><ptype>GLenum</ptype> <name>target</name></param>
            <param group="GetTextureParameter"><ptype>GLenum</ptype> <name>pname</name></param>
            <param len="COMPSIZE(pname)"><ptype>GLint</ptype> *<name>params</name></param>
            <glx opcode="137" type="single" />
        </command>
         */
    private static Function _glGetTexParameteriv ;
    public static void glGetTexParameteriv (int target_ , int pname_ , int[] params_ ) {
        if( _glGetTexParameteriv == null )
            _glGetTexParameteriv = loadFunction("glGetTexParameteriv");
        _glGetTexParameteriv.invoke(Void.class, new Object[]{target_,pname_,params_ });
    }
    /* <command>
            <proto>void <name>glGetTextureImage</name></proto>
            <param><ptype>GLuint</ptype> <name>texture</name></param>
            <param><ptype>GLint</ptype> <name>level</name></param>
            <param><ptype>GLenum</ptype> <name>format</name></param>
            <param><ptype>GLenum</ptype> <name>type</name></param>
            <param><ptype>GLsizei</ptype> <name>bufSize</name></param>
            <param>void *<name>pixels</name></param>
        </command>
         */
    private static Function _glGetTextureImage ;
    public static void glGetTextureImage (int texture_ , int level_ , int format_ , int type_ , int bufSize_ , byte[] pixels_ ) {
        if( _glGetTextureImage == null )
            _glGetTextureImage = loadFunction("glGetTextureImage");
        _glGetTextureImage.invoke(Void.class, new Object[]{texture_,level_,format_,type_,bufSize_,pixels_ });
    }
    /* <command>
            <proto>void <name>glGetTextureLevelParameterfv</name></proto>
            <param><ptype>GLuint</ptype> <name>texture</name></param>
            <param><ptype>GLint</ptype> <name>level</name></param>
            <param><ptype>GLenum</ptype> <name>pname</name></param>
            <param><ptype>GLfloat</ptype> *<name>params</name></param>
        </command>
         */
    private static Function _glGetTextureLevelParameterfv ;
    public static void glGetTextureLevelParameterfv (int texture_ , int level_ , int pname_ , float[] params_ ) {
        if( _glGetTextureLevelParameterfv == null )
            _glGetTextureLevelParameterfv = loadFunction("glGetTextureLevelParameterfv");
        _glGetTextureLevelParameterfv.invoke(Void.class, new Object[]{texture_,level_,pname_,params_ });
    }
    /* <command>
            <proto>void <name>glGetTextureLevelParameteriv</name></proto>
            <param><ptype>GLuint</ptype> <name>texture</name></param>
            <param><ptype>GLint</ptype> <name>level</name></param>
            <param><ptype>GLenum</ptype> <name>pname</name></param>
            <param><ptype>GLint</ptype> *<name>params</name></param>
        </command>
         */
    private static Function _glGetTextureLevelParameteriv ;
    public static void glGetTextureLevelParameteriv (int texture_ , int level_ , int pname_ , int[] params_ ) {
        if( _glGetTextureLevelParameteriv == null )
            _glGetTextureLevelParameteriv = loadFunction("glGetTextureLevelParameteriv");
        _glGetTextureLevelParameteriv.invoke(Void.class, new Object[]{texture_,level_,pname_,params_ });
    }
    /* <command>
            <proto>void <name>glGetTextureParameterIiv</name></proto>
            <param><ptype>GLuint</ptype> <name>texture</name></param>
            <param><ptype>GLenum</ptype> <name>pname</name></param>
            <param><ptype>GLint</ptype> *<name>params</name></param>
        </command>
         */
    private static Function _glGetTextureParameterIiv ;
    public static void glGetTextureParameterIiv (int texture_ , int pname_ , int[] params_ ) {
        if( _glGetTextureParameterIiv == null )
            _glGetTextureParameterIiv = loadFunction("glGetTextureParameterIiv");
        _glGetTextureParameterIiv.invoke(Void.class, new Object[]{texture_,pname_,params_ });
    }
    /* <command>
            <proto>void <name>glGetTextureParameterIuiv</name></proto>
            <param><ptype>GLuint</ptype> <name>texture</name></param>
            <param><ptype>GLenum</ptype> <name>pname</name></param>
            <param><ptype>GLuint</ptype> *<name>params</name></param>
        </command>
         */
    private static Function _glGetTextureParameterIuiv ;
    public static void glGetTextureParameterIuiv (int texture_ , int pname_ , int[] params_ ) {
        if( _glGetTextureParameterIuiv == null )
            _glGetTextureParameterIuiv = loadFunction("glGetTextureParameterIuiv");
        _glGetTextureParameterIuiv.invoke(Void.class, new Object[]{texture_,pname_,params_ });
    }
    /* <command>
            <proto>void <name>glGetTextureParameterfv</name></proto>
            <param><ptype>GLuint</ptype> <name>texture</name></param>
            <param><ptype>GLenum</ptype> <name>pname</name></param>
            <param><ptype>GLfloat</ptype> *<name>params</name></param>
        </command>
         */
    private static Function _glGetTextureParameterfv ;
    public static void glGetTextureParameterfv (int texture_ , int pname_ , float[] params_ ) {
        if( _glGetTextureParameterfv == null )
            _glGetTextureParameterfv = loadFunction("glGetTextureParameterfv");
        _glGetTextureParameterfv.invoke(Void.class, new Object[]{texture_,pname_,params_ });
    }
    /* <command>
            <proto>void <name>glGetTextureParameteriv</name></proto>
            <param><ptype>GLuint</ptype> <name>texture</name></param>
            <param><ptype>GLenum</ptype> <name>pname</name></param>
            <param><ptype>GLint</ptype> *<name>params</name></param>
        </command>
         */
    private static Function _glGetTextureParameteriv ;
    public static void glGetTextureParameteriv (int texture_ , int pname_ , int[] params_ ) {
        if( _glGetTextureParameteriv == null )
            _glGetTextureParameteriv = loadFunction("glGetTextureParameteriv");
        _glGetTextureParameteriv.invoke(Void.class, new Object[]{texture_,pname_,params_ });
    }
    /* <command>
            <proto>void <name>glGetTextureSubImage</name></proto>
            <param><ptype>GLuint</ptype> <name>texture</name></param>
            <param><ptype>GLint</ptype> <name>level</name></param>
            <param><ptype>GLint</ptype> <name>xoffset</name></param>
            <param><ptype>GLint</ptype> <name>yoffset</name></param>
            <param><ptype>GLint</ptype> <name>zoffset</name></param>
            <param><ptype>GLsizei</ptype> <name>width</name></param>
            <param><ptype>GLsizei</ptype> <name>height</name></param>
            <param><ptype>GLsizei</ptype> <name>depth</name></param>
            <param><ptype>GLenum</ptype> <name>format</name></param>
            <param><ptype>GLenum</ptype> <name>type</name></param>
            <param><ptype>GLsizei</ptype> <name>bufSize</name></param>
            <param>void *<name>pixels</name></param>
        </command>
         */
    private static Function _glGetTextureSubImage ;
    public static void glGetTextureSubImage (int texture_ , int level_ , int xoffset_ , int yoffset_ , int zoffset_ , int width_ , int height_ , int depth_ , int format_ , int type_ , int bufSize_ , byte[] pixels_ ) {
        if( _glGetTextureSubImage == null )
            _glGetTextureSubImage = loadFunction("glGetTextureSubImage");
        _glGetTextureSubImage.invoke(Void.class, new Object[]{texture_,level_,xoffset_,yoffset_,zoffset_,width_,height_,depth_,format_,type_,bufSize_,pixels_ });
    }
    /* <command>
            <proto>void <name>glGetTransformFeedbackVarying</name></proto>
            <param><ptype>GLuint</ptype> <name>program</name></param>
            <param><ptype>GLuint</ptype> <name>index</name></param>
            <param><ptype>GLsizei</ptype> <name>bufSize</name></param>
            <param len="1"><ptype>GLsizei</ptype> *<name>length</name></param>
            <param len="1"><ptype>GLsizei</ptype> *<name>size</name></param>
            <param len="1"><ptype>GLenum</ptype> *<name>type</name></param>
            <param len="bufSize"><ptype>GLchar</ptype> *<name>name</name></param>
        </command>
         */
    private static Function _glGetTransformFeedbackVarying ;
    public static void glGetTransformFeedbackVarying (int program_ , int index_ , int bufSize_ , int[] length_ , int[] size_ , int[] type_ , byte[] name_ ) {
        if( _glGetTransformFeedbackVarying == null )
            _glGetTransformFeedbackVarying = loadFunction("glGetTransformFeedbackVarying");
        _glGetTransformFeedbackVarying.invoke(Void.class, new Object[]{program_,index_,bufSize_,length_,size_,type_,name_ });
    }
    /* <command>
            <proto>void <name>glGetTransformFeedbacki64_v</name></proto>
            <param><ptype>GLuint</ptype> <name>xfb</name></param>
            <param><ptype>GLenum</ptype> <name>pname</name></param>
            <param><ptype>GLuint</ptype> <name>index</name></param>
            <param><ptype>GLint64</ptype> *<name>param</name></param>
        </command>
         */
    private static Function _glGetTransformFeedbacki64_v ;
    public static void glGetTransformFeedbacki64_v (int xfb_ , int pname_ , int index_ , long[] param_ ) {
        if( _glGetTransformFeedbacki64_v == null )
            _glGetTransformFeedbacki64_v = loadFunction("glGetTransformFeedbacki64_v");
        _glGetTransformFeedbacki64_v.invoke(Void.class, new Object[]{xfb_,pname_,index_,param_ });
    }
    /* <command>
            <proto>void <name>glGetTransformFeedbacki_v</name></proto>
            <param><ptype>GLuint</ptype> <name>xfb</name></param>
            <param><ptype>GLenum</ptype> <name>pname</name></param>
            <param><ptype>GLuint</ptype> <name>index</name></param>
            <param><ptype>GLint</ptype> *<name>param</name></param>
        </command>
         */
    private static Function _glGetTransformFeedbacki_v ;
    public static void glGetTransformFeedbacki_v (int xfb_ , int pname_ , int index_ , int[] param_ ) {
        if( _glGetTransformFeedbacki_v == null )
            _glGetTransformFeedbacki_v = loadFunction("glGetTransformFeedbacki_v");
        _glGetTransformFeedbacki_v.invoke(Void.class, new Object[]{xfb_,pname_,index_,param_ });
    }
    /* <command>
            <proto>void <name>glGetTransformFeedbackiv</name></proto>
            <param><ptype>GLuint</ptype> <name>xfb</name></param>
            <param><ptype>GLenum</ptype> <name>pname</name></param>
            <param><ptype>GLint</ptype> *<name>param</name></param>
        </command>
         */
    private static Function _glGetTransformFeedbackiv ;
    public static void glGetTransformFeedbackiv (int xfb_ , int pname_ , int[] param_ ) {
        if( _glGetTransformFeedbackiv == null )
            _glGetTransformFeedbackiv = loadFunction("glGetTransformFeedbackiv");
        _glGetTransformFeedbackiv.invoke(Void.class, new Object[]{xfb_,pname_,param_ });
    }
    /* <command>
            <proto><ptype>GLuint</ptype> <name>glGetUniformBlockIndex</name></proto>
            <param><ptype>GLuint</ptype> <name>program</name></param>
            <param len="COMPSIZE()">const <ptype>GLchar</ptype> *<name>uniformBlockName</name></param>
        </command>
         */
    private static Function _glGetUniformBlockIndex ;
    public static int glGetUniformBlockIndex (int program_ , String uniformBlockName_ ) {
        if( _glGetUniformBlockIndex == null )
            _glGetUniformBlockIndex = loadFunction("glGetUniformBlockIndex");
        int rv= ( Integer )_glGetUniformBlockIndex.invoke(Integer.class, new Object[]{program_,uniformBlockName_ });
        return rv;
    }
    /* <command>
            <proto>void <name>glGetUniformIndices</name></proto>
            <param><ptype>GLuint</ptype> <name>program</name></param>
            <param><ptype>GLsizei</ptype> <name>uniformCount</name></param>
            <param len="COMPSIZE(uniformCount)">const <ptype>GLchar</ptype> *const*<name>uniformNames</name></param>
            <param len="COMPSIZE(uniformCount)"><ptype>GLuint</ptype> *<name>uniformIndices</name></param>
        </command>
         */
    private static Function _glGetUniformIndices ;
    public static void glGetUniformIndices (int program_ , int uniformCount_ , byte[] uniformNames_ , int[] uniformIndices_ ) {
        if( _glGetUniformIndices == null )
            _glGetUniformIndices = loadFunction("glGetUniformIndices");
        _glGetUniformIndices.invoke(Void.class, new Object[]{program_,uniformCount_,uniformNames_,uniformIndices_ });
    }
    /* <command>
            <proto><ptype>GLint</ptype> <name>glGetUniformLocation</name></proto>
            <param><ptype>GLuint</ptype> <name>program</name></param>
            <param>const <ptype>GLchar</ptype> *<name>name</name></param>
        </command>
         */
    private static Function _glGetUniformLocation ;
    public static int glGetUniformLocation (int program_ , String name_ ) {
        if( _glGetUniformLocation == null )
            _glGetUniformLocation = loadFunction("glGetUniformLocation");
        int rv= ( Integer )_glGetUniformLocation.invoke(Integer.class, new Object[]{program_,name_ });
        return rv;
    }
    /* <command>
            <proto>void <name>glGetUniformSubroutineuiv</name></proto>
            <param><ptype>GLenum</ptype> <name>shadertype</name></param>
            <param><ptype>GLint</ptype> <name>location</name></param>
            <param len="1"><ptype>GLuint</ptype> *<name>params</name></param>
        </command>
         */
    private static Function _glGetUniformSubroutineuiv ;
    public static void glGetUniformSubroutineuiv (int shadertype_ , int location_ , int[] params_ ) {
        if( _glGetUniformSubroutineuiv == null )
            _glGetUniformSubroutineuiv = loadFunction("glGetUniformSubroutineuiv");
        _glGetUniformSubroutineuiv.invoke(Void.class, new Object[]{shadertype_,location_,params_ });
    }
    /* <command>
            <proto>void <name>glGetUniformdv</name></proto>
            <param><ptype>GLuint</ptype> <name>program</name></param>
            <param><ptype>GLint</ptype> <name>location</name></param>
            <param len="COMPSIZE(program,location)"><ptype>GLdouble</ptype> *<name>params</name></param>
        </command>
         */
    private static Function _glGetUniformdv ;
    public static void glGetUniformdv (int program_ , int location_ , double[] params_ ) {
        if( _glGetUniformdv == null )
            _glGetUniformdv = loadFunction("glGetUniformdv");
        _glGetUniformdv.invoke(Void.class, new Object[]{program_,location_,params_ });
    }
    /* <command>
            <proto>void <name>glGetUniformfv</name></proto>
            <param><ptype>GLuint</ptype> <name>program</name></param>
            <param><ptype>GLint</ptype> <name>location</name></param>
            <param len="COMPSIZE(program,location)"><ptype>GLfloat</ptype> *<name>params</name></param>
        </command>
         */
    private static Function _glGetUniformfv ;
    public static void glGetUniformfv (int program_ , int location_ , float[] params_ ) {
        if( _glGetUniformfv == null )
            _glGetUniformfv = loadFunction("glGetUniformfv");
        _glGetUniformfv.invoke(Void.class, new Object[]{program_,location_,params_ });
    }
    /* <command>
            <proto>void <name>glGetUniformiv</name></proto>
            <param><ptype>GLuint</ptype> <name>program</name></param>
            <param><ptype>GLint</ptype> <name>location</name></param>
            <param len="COMPSIZE(program,location)"><ptype>GLint</ptype> *<name>params</name></param>
        </command>
         */
    private static Function _glGetUniformiv ;
    public static void glGetUniformiv (int program_ , int location_ , int[] params_ ) {
        if( _glGetUniformiv == null )
            _glGetUniformiv = loadFunction("glGetUniformiv");
        _glGetUniformiv.invoke(Void.class, new Object[]{program_,location_,params_ });
    }
    /* <command>
            <proto>void <name>glGetUniformuiv</name></proto>
            <param><ptype>GLuint</ptype> <name>program</name></param>
            <param><ptype>GLint</ptype> <name>location</name></param>
            <param len="COMPSIZE(program,location)"><ptype>GLuint</ptype> *<name>params</name></param>
        </command>
         */
    private static Function _glGetUniformuiv ;
    public static void glGetUniformuiv (int program_ , int location_ , int[] params_ ) {
        if( _glGetUniformuiv == null )
            _glGetUniformuiv = loadFunction("glGetUniformuiv");
        _glGetUniformuiv.invoke(Void.class, new Object[]{program_,location_,params_ });
    }
    /* <command>
            <proto>void <name>glGetVertexArrayIndexed64iv</name></proto>
            <param><ptype>GLuint</ptype> <name>vaobj</name></param>
            <param><ptype>GLuint</ptype> <name>index</name></param>
            <param><ptype>GLenum</ptype> <name>pname</name></param>
            <param><ptype>GLint64</ptype> *<name>param</name></param>
        </command>
         */
    private static Function _glGetVertexArrayIndexed64iv ;
    public static void glGetVertexArrayIndexed64iv (int vaobj_ , int index_ , int pname_ , long[] param_ ) {
        if( _glGetVertexArrayIndexed64iv == null )
            _glGetVertexArrayIndexed64iv = loadFunction("glGetVertexArrayIndexed64iv");
        _glGetVertexArrayIndexed64iv.invoke(Void.class, new Object[]{vaobj_,index_,pname_,param_ });
    }
    /* <command>
            <proto>void <name>glGetVertexArrayIndexediv</name></proto>
            <param><ptype>GLuint</ptype> <name>vaobj</name></param>
            <param><ptype>GLuint</ptype> <name>index</name></param>
            <param><ptype>GLenum</ptype> <name>pname</name></param>
            <param><ptype>GLint</ptype> *<name>param</name></param>
        </command>
         */
    private static Function _glGetVertexArrayIndexediv ;
    public static void glGetVertexArrayIndexediv (int vaobj_ , int index_ , int pname_ , int[] param_ ) {
        if( _glGetVertexArrayIndexediv == null )
            _glGetVertexArrayIndexediv = loadFunction("glGetVertexArrayIndexediv");
        _glGetVertexArrayIndexediv.invoke(Void.class, new Object[]{vaobj_,index_,pname_,param_ });
    }
    /* <command>
            <proto>void <name>glGetVertexArrayiv</name></proto>
            <param><ptype>GLuint</ptype> <name>vaobj</name></param>
            <param><ptype>GLenum</ptype> <name>pname</name></param>
            <param><ptype>GLint</ptype> *<name>param</name></param>
        </command>
         */
    private static Function _glGetVertexArrayiv ;
    public static void glGetVertexArrayiv (int vaobj_ , int pname_ , int[] param_ ) {
        if( _glGetVertexArrayiv == null )
            _glGetVertexArrayiv = loadFunction("glGetVertexArrayiv");
        _glGetVertexArrayiv.invoke(Void.class, new Object[]{vaobj_,pname_,param_ });
    }
    /* <command>
            <proto>void <name>glGetVertexAttribIiv</name></proto>
            <param><ptype>GLuint</ptype> <name>index</name></param>
            <param group="VertexAttribEnum"><ptype>GLenum</ptype> <name>pname</name></param>
            <param len="1"><ptype>GLint</ptype> *<name>params</name></param>
        </command>
         */
    private static Function _glGetVertexAttribIiv ;
    public static void glGetVertexAttribIiv (int index_ , int pname_ , int[] params_ ) {
        if( _glGetVertexAttribIiv == null )
            _glGetVertexAttribIiv = loadFunction("glGetVertexAttribIiv");
        _glGetVertexAttribIiv.invoke(Void.class, new Object[]{index_,pname_,params_ });
    }
    /* <command>
            <proto>void <name>glGetVertexAttribIuiv</name></proto>
            <param><ptype>GLuint</ptype> <name>index</name></param>
            <param group="VertexAttribEnum"><ptype>GLenum</ptype> <name>pname</name></param>
            <param len="1"><ptype>GLuint</ptype> *<name>params</name></param>
        </command>
         */
    private static Function _glGetVertexAttribIuiv ;
    public static void glGetVertexAttribIuiv (int index_ , int pname_ , int[] params_ ) {
        if( _glGetVertexAttribIuiv == null )
            _glGetVertexAttribIuiv = loadFunction("glGetVertexAttribIuiv");
        _glGetVertexAttribIuiv.invoke(Void.class, new Object[]{index_,pname_,params_ });
    }
    /* <command>
            <proto>void <name>glGetVertexAttribLdv</name></proto>
            <param><ptype>GLuint</ptype> <name>index</name></param>
            <param><ptype>GLenum</ptype> <name>pname</name></param>
            <param len="COMPSIZE(pname)"><ptype>GLdouble</ptype> *<name>params</name></param>
        </command>
         */
    private static Function _glGetVertexAttribLdv ;
    public static void glGetVertexAttribLdv (int index_ , int pname_ , double[] params_ ) {
        if( _glGetVertexAttribLdv == null )
            _glGetVertexAttribLdv = loadFunction("glGetVertexAttribLdv");
        _glGetVertexAttribLdv.invoke(Void.class, new Object[]{index_,pname_,params_ });
    }
    /* <command>
            <proto>void <name>glGetVertexAttribPointerv</name></proto>
            <param><ptype>GLuint</ptype> <name>index</name></param>
            <param group="VertexAttribPointerPropertyARB"><ptype>GLenum</ptype> <name>pname</name></param>
            <param len="1">void **<name>pointer</name></param>
            <glx opcode="209" type="single" />
        </command>
         */
    private static Function _glGetVertexAttribPointerv ;
    public static void glGetVertexAttribPointerv (int index_ , int pname_ , byte[] pointer_ ) {
        if( _glGetVertexAttribPointerv == null )
            _glGetVertexAttribPointerv = loadFunction("glGetVertexAttribPointerv");
        _glGetVertexAttribPointerv.invoke(Void.class, new Object[]{index_,pname_,pointer_ });
    }
    /* <command>
            <proto>void <name>glGetVertexAttribdv</name></proto>
            <param><ptype>GLuint</ptype> <name>index</name></param>
            <param group="VertexAttribPropertyARB"><ptype>GLenum</ptype> <name>pname</name></param>
            <param len="4"><ptype>GLdouble</ptype> *<name>params</name></param>
            <glx opcode="1301" type="vendor" />
        </command>
         */
    private static Function _glGetVertexAttribdv ;
    public static void glGetVertexAttribdv (int index_ , int pname_ , double[] params_ ) {
        if( _glGetVertexAttribdv == null )
            _glGetVertexAttribdv = loadFunction("glGetVertexAttribdv");
        _glGetVertexAttribdv.invoke(Void.class, new Object[]{index_,pname_,params_ });
    }
    /* <command>
            <proto>void <name>glGetVertexAttribfv</name></proto>
            <param><ptype>GLuint</ptype> <name>index</name></param>
            <param group="VertexAttribPropertyARB"><ptype>GLenum</ptype> <name>pname</name></param>
            <param len="4"><ptype>GLfloat</ptype> *<name>params</name></param>
            <glx opcode="1302" type="vendor" />
        </command>
         */
    private static Function _glGetVertexAttribfv ;
    public static void glGetVertexAttribfv (int index_ , int pname_ , float[] params_ ) {
        if( _glGetVertexAttribfv == null )
            _glGetVertexAttribfv = loadFunction("glGetVertexAttribfv");
        _glGetVertexAttribfv.invoke(Void.class, new Object[]{index_,pname_,params_ });
    }
    /* <command>
            <proto>void <name>glGetVertexAttribiv</name></proto>
            <param><ptype>GLuint</ptype> <name>index</name></param>
            <param group="VertexAttribPropertyARB"><ptype>GLenum</ptype> <name>pname</name></param>
            <param len="4"><ptype>GLint</ptype> *<name>params</name></param>
            <glx opcode="1303" type="vendor" />
        </command>
         */
    private static Function _glGetVertexAttribiv ;
    public static void glGetVertexAttribiv (int index_ , int pname_ , int[] params_ ) {
        if( _glGetVertexAttribiv == null )
            _glGetVertexAttribiv = loadFunction("glGetVertexAttribiv");
        _glGetVertexAttribiv.invoke(Void.class, new Object[]{index_,pname_,params_ });
    }
    /* <command>
            <proto>void <name>glGetnCompressedTexImage</name></proto>
            <param><ptype>GLenum</ptype> <name>target</name></param>
            <param><ptype>GLint</ptype> <name>lod</name></param>
            <param><ptype>GLsizei</ptype> <name>bufSize</name></param>
            <param>void *<name>pixels</name></param>
        </command>
         */
    private static Function _glGetnCompressedTexImage ;
    public static void glGetnCompressedTexImage (int target_ , int lod_ , int bufSize_ , byte[] pixels_ ) {
        if( _glGetnCompressedTexImage == null )
            _glGetnCompressedTexImage = loadFunction("glGetnCompressedTexImage");
        _glGetnCompressedTexImage.invoke(Void.class, new Object[]{target_,lod_,bufSize_,pixels_ });
    }
    /* <command>
            <proto>void <name>glGetnTexImage</name></proto>
            <param><ptype>GLenum</ptype> <name>target</name></param>
            <param><ptype>GLint</ptype> <name>level</name></param>
            <param><ptype>GLenum</ptype> <name>format</name></param>
            <param><ptype>GLenum</ptype> <name>type</name></param>
            <param><ptype>GLsizei</ptype> <name>bufSize</name></param>
            <param>void *<name>pixels</name></param>
        </command>
         */
    private static Function _glGetnTexImage ;
    public static void glGetnTexImage (int target_ , int level_ , int format_ , int type_ , int bufSize_ , byte[] pixels_ ) {
        if( _glGetnTexImage == null )
            _glGetnTexImage = loadFunction("glGetnTexImage");
        _glGetnTexImage.invoke(Void.class, new Object[]{target_,level_,format_,type_,bufSize_,pixels_ });
    }
    /* <command>
            <proto>void <name>glGetnUniformdv</name></proto>
            <param><ptype>GLuint</ptype> <name>program</name></param>
            <param><ptype>GLint</ptype> <name>location</name></param>
            <param><ptype>GLsizei</ptype> <name>bufSize</name></param>
            <param><ptype>GLdouble</ptype> *<name>params</name></param>
        </command>
         */
    private static Function _glGetnUniformdv ;
    public static void glGetnUniformdv (int program_ , int location_ , int bufSize_ , double[] params_ ) {
        if( _glGetnUniformdv == null )
            _glGetnUniformdv = loadFunction("glGetnUniformdv");
        _glGetnUniformdv.invoke(Void.class, new Object[]{program_,location_,bufSize_,params_ });
    }
    /* <command>
            <proto>void <name>glGetnUniformfv</name></proto>
            <param><ptype>GLuint</ptype> <name>program</name></param>
            <param><ptype>GLint</ptype> <name>location</name></param>
            <param><ptype>GLsizei</ptype> <name>bufSize</name></param>
            <param><ptype>GLfloat</ptype> *<name>params</name></param>
        </command>
         */
    private static Function _glGetnUniformfv ;
    public static void glGetnUniformfv (int program_ , int location_ , int bufSize_ , float[] params_ ) {
        if( _glGetnUniformfv == null )
            _glGetnUniformfv = loadFunction("glGetnUniformfv");
        _glGetnUniformfv.invoke(Void.class, new Object[]{program_,location_,bufSize_,params_ });
    }
    /* <command>
            <proto>void <name>glGetnUniformiv</name></proto>
            <param><ptype>GLuint</ptype> <name>program</name></param>
            <param><ptype>GLint</ptype> <name>location</name></param>
            <param><ptype>GLsizei</ptype> <name>bufSize</name></param>
            <param><ptype>GLint</ptype> *<name>params</name></param>
        </command>
         */
    private static Function _glGetnUniformiv ;
    public static void glGetnUniformiv (int program_ , int location_ , int bufSize_ , int[] params_ ) {
        if( _glGetnUniformiv == null )
            _glGetnUniformiv = loadFunction("glGetnUniformiv");
        _glGetnUniformiv.invoke(Void.class, new Object[]{program_,location_,bufSize_,params_ });
    }
    /* <command>
            <proto>void <name>glGetnUniformuiv</name></proto>
            <param><ptype>GLuint</ptype> <name>program</name></param>
            <param><ptype>GLint</ptype> <name>location</name></param>
            <param><ptype>GLsizei</ptype> <name>bufSize</name></param>
            <param><ptype>GLuint</ptype> *<name>params</name></param>
        </command>
         */
    private static Function _glGetnUniformuiv ;
    public static void glGetnUniformuiv (int program_ , int location_ , int bufSize_ , int[] params_ ) {
        if( _glGetnUniformuiv == null )
            _glGetnUniformuiv = loadFunction("glGetnUniformuiv");
        _glGetnUniformuiv.invoke(Void.class, new Object[]{program_,location_,bufSize_,params_ });
    }
    /* <command>
            <proto>void <name>glHint</name></proto>
            <param group="HintTarget"><ptype>GLenum</ptype> <name>target</name></param>
            <param group="HintMode"><ptype>GLenum</ptype> <name>mode</name></param>
            <glx opcode="85" type="render" />
        </command>
         */
    private static Function _glHint ;
    public static void glHint (int target_ , int mode_ ) {
        if( _glHint == null )
            _glHint = loadFunction("glHint");
        _glHint.invoke(Void.class, new Object[]{target_,mode_ });
    }
    /* <command>
            <proto>void <name>glInvalidateBufferData</name></proto>
            <param><ptype>GLuint</ptype> <name>buffer</name></param>
        </command>
         */
    private static Function _glInvalidateBufferData ;
    public static void glInvalidateBufferData (int buffer_ ) {
        if( _glInvalidateBufferData == null )
            _glInvalidateBufferData = loadFunction("glInvalidateBufferData");
        _glInvalidateBufferData.invoke(Void.class, new Object[]{buffer_ });
    }
    /* <command>
            <proto>void <name>glInvalidateBufferSubData</name></proto>
            <param><ptype>GLuint</ptype> <name>buffer</name></param>
            <param group="BufferOffset"><ptype>GLintptr</ptype> <name>offset</name></param>
            <param group="BufferSize"><ptype>GLsizeiptr</ptype> <name>length</name></param>
        </command>
         */
    private static Function _glInvalidateBufferSubData ;
    public static void glInvalidateBufferSubData (int buffer_ , long offset_ , long length_ ) {
        if( _glInvalidateBufferSubData == null )
            _glInvalidateBufferSubData = loadFunction("glInvalidateBufferSubData");
        _glInvalidateBufferSubData.invoke(Void.class, new Object[]{buffer_,offset_,length_ });
    }
    /* <command>
            <proto>void <name>glInvalidateFramebuffer</name></proto>
            <param><ptype>GLenum</ptype> <name>target</name></param>
            <param><ptype>GLsizei</ptype> <name>numAttachments</name></param>
            <param len="numAttachments">const <ptype>GLenum</ptype> *<name>attachments</name></param>
        </command>
         */
    private static Function _glInvalidateFramebuffer ;
    public static void glInvalidateFramebuffer (int target_ , int numAttachments_ , int[] attachments_ ) {
        if( _glInvalidateFramebuffer == null )
            _glInvalidateFramebuffer = loadFunction("glInvalidateFramebuffer");
        _glInvalidateFramebuffer.invoke(Void.class, new Object[]{target_,numAttachments_,attachments_ });
    }
    /* <command>
            <proto>void <name>glInvalidateNamedFramebufferData</name></proto>
            <param><ptype>GLuint</ptype> <name>framebuffer</name></param>
            <param><ptype>GLsizei</ptype> <name>numAttachments</name></param>
            <param>const <ptype>GLenum</ptype> *<name>attachments</name></param>
        </command>
         */
    private static Function _glInvalidateNamedFramebufferData ;
    public static void glInvalidateNamedFramebufferData (int framebuffer_ , int numAttachments_ , int[] attachments_ ) {
        if( _glInvalidateNamedFramebufferData == null )
            _glInvalidateNamedFramebufferData = loadFunction("glInvalidateNamedFramebufferData");
        _glInvalidateNamedFramebufferData.invoke(Void.class, new Object[]{framebuffer_,numAttachments_,attachments_ });
    }
    /* <command>
            <proto>void <name>glInvalidateNamedFramebufferSubData</name></proto>
            <param><ptype>GLuint</ptype> <name>framebuffer</name></param>
            <param><ptype>GLsizei</ptype> <name>numAttachments</name></param>
            <param>const <ptype>GLenum</ptype> *<name>attachments</name></param>
            <param><ptype>GLint</ptype> <name>x</name></param>
            <param><ptype>GLint</ptype> <name>y</name></param>
            <param><ptype>GLsizei</ptype> <name>width</name></param>
            <param><ptype>GLsizei</ptype> <name>height</name></param>
        </command>
         */
    private static Function _glInvalidateNamedFramebufferSubData ;
    public static void glInvalidateNamedFramebufferSubData (int framebuffer_ , int numAttachments_ , int[] attachments_ , int x_ , int y_ , int width_ , int height_ ) {
        if( _glInvalidateNamedFramebufferSubData == null )
            _glInvalidateNamedFramebufferSubData = loadFunction("glInvalidateNamedFramebufferSubData");
        _glInvalidateNamedFramebufferSubData.invoke(Void.class, new Object[]{framebuffer_,numAttachments_,attachments_,x_,y_,width_,height_ });
    }
    /* <command>
            <proto>void <name>glInvalidateSubFramebuffer</name></proto>
            <param><ptype>GLenum</ptype> <name>target</name></param>
            <param><ptype>GLsizei</ptype> <name>numAttachments</name></param>
            <param len="numAttachments">const <ptype>GLenum</ptype> *<name>attachments</name></param>
            <param><ptype>GLint</ptype> <name>x</name></param>
            <param><ptype>GLint</ptype> <name>y</name></param>
            <param><ptype>GLsizei</ptype> <name>width</name></param>
            <param><ptype>GLsizei</ptype> <name>height</name></param>
        </command>
         */
    private static Function _glInvalidateSubFramebuffer ;
    public static void glInvalidateSubFramebuffer (int target_ , int numAttachments_ , int[] attachments_ , int x_ , int y_ , int width_ , int height_ ) {
        if( _glInvalidateSubFramebuffer == null )
            _glInvalidateSubFramebuffer = loadFunction("glInvalidateSubFramebuffer");
        _glInvalidateSubFramebuffer.invoke(Void.class, new Object[]{target_,numAttachments_,attachments_,x_,y_,width_,height_ });
    }
    /* <command>
            <proto>void <name>glInvalidateTexImage</name></proto>
            <param><ptype>GLuint</ptype> <name>texture</name></param>
            <param><ptype>GLint</ptype> <name>level</name></param>
        </command>
         */
    private static Function _glInvalidateTexImage ;
    public static void glInvalidateTexImage (int texture_ , int level_ ) {
        if( _glInvalidateTexImage == null )
            _glInvalidateTexImage = loadFunction("glInvalidateTexImage");
        _glInvalidateTexImage.invoke(Void.class, new Object[]{texture_,level_ });
    }
    /* <command>
            <proto>void <name>glInvalidateTexSubImage</name></proto>
            <param><ptype>GLuint</ptype> <name>texture</name></param>
            <param><ptype>GLint</ptype> <name>level</name></param>
            <param><ptype>GLint</ptype> <name>xoffset</name></param>
            <param><ptype>GLint</ptype> <name>yoffset</name></param>
            <param><ptype>GLint</ptype> <name>zoffset</name></param>
            <param><ptype>GLsizei</ptype> <name>width</name></param>
            <param><ptype>GLsizei</ptype> <name>height</name></param>
            <param><ptype>GLsizei</ptype> <name>depth</name></param>
        </command>
         */
    private static Function _glInvalidateTexSubImage ;
    public static void glInvalidateTexSubImage (int texture_ , int level_ , int xoffset_ , int yoffset_ , int zoffset_ , int width_ , int height_ , int depth_ ) {
        if( _glInvalidateTexSubImage == null )
            _glInvalidateTexSubImage = loadFunction("glInvalidateTexSubImage");
        _glInvalidateTexSubImage.invoke(Void.class, new Object[]{texture_,level_,xoffset_,yoffset_,zoffset_,width_,height_,depth_ });
    }
    /* <command>
            <proto group="Boolean"><ptype>GLboolean</ptype> <name>glIsBuffer</name></proto>
            <param><ptype>GLuint</ptype> <name>buffer</name></param>
        </command>
         */
    private static Function _glIsBuffer ;
    public static boolean glIsBuffer (int buffer_ ) {
        if( _glIsBuffer == null )
            _glIsBuffer = loadFunction("glIsBuffer");
        byte rv= ( Byte )_glIsBuffer.invoke(Byte.class, new Object[]{buffer_ });
        return (rv!=0);
    }
    /* <command>
            <proto group="Boolean"><ptype>GLboolean</ptype> <name>glIsEnabled</name></proto>
            <param group="EnableCap"><ptype>GLenum</ptype> <name>cap</name></param>
            <glx opcode="140" type="single" />
        </command>
         */
    private static Function _glIsEnabled ;
    public static boolean glIsEnabled (int cap_ ) {
        if( _glIsEnabled == null )
            _glIsEnabled = loadFunction("glIsEnabled");
        byte rv= ( Byte )_glIsEnabled.invoke(Byte.class, new Object[]{cap_ });
        return (rv!=0);
    }
    /* <command>
            <proto group="Boolean"><ptype>GLboolean</ptype> <name>glIsEnabledi</name></proto>
            <param><ptype>GLenum</ptype> <name>target</name></param>
            <param><ptype>GLuint</ptype> <name>index</name></param>
        </command>
         */
    private static Function _glIsEnabledi ;
    public static boolean glIsEnabledi (int target_ , int index_ ) {
        if( _glIsEnabledi == null )
            _glIsEnabledi = loadFunction("glIsEnabledi");
        byte rv= ( Byte )_glIsEnabledi.invoke(Byte.class, new Object[]{target_,index_ });
        return (rv!=0);
    }
    /* <command>
            <proto group="Boolean"><ptype>GLboolean</ptype> <name>glIsFramebuffer</name></proto>
            <param><ptype>GLuint</ptype> <name>framebuffer</name></param>
            <glx opcode="1425" type="vendor" />
        </command>
         */
    private static Function _glIsFramebuffer ;
    public static boolean glIsFramebuffer (int framebuffer_ ) {
        if( _glIsFramebuffer == null )
            _glIsFramebuffer = loadFunction("glIsFramebuffer");
        byte rv= ( Byte )_glIsFramebuffer.invoke(Byte.class, new Object[]{framebuffer_ });
        return (rv!=0);
    }
    /* <command>
            <proto group="Boolean"><ptype>GLboolean</ptype> <name>glIsProgram</name></proto>
            <param><ptype>GLuint</ptype> <name>program</name></param>
            <glx opcode="197" type="single" />
        </command>
         */
    private static Function _glIsProgram ;
    public static boolean glIsProgram (int program_ ) {
        if( _glIsProgram == null )
            _glIsProgram = loadFunction("glIsProgram");
        byte rv= ( Byte )_glIsProgram.invoke(Byte.class, new Object[]{program_ });
        return (rv!=0);
    }
    /* <command>
            <proto group="Boolean"><ptype>GLboolean</ptype> <name>glIsProgramPipeline</name></proto>
            <param><ptype>GLuint</ptype> <name>pipeline</name></param>
        </command>
         */
    private static Function _glIsProgramPipeline ;
    public static boolean glIsProgramPipeline (int pipeline_ ) {
        if( _glIsProgramPipeline == null )
            _glIsProgramPipeline = loadFunction("glIsProgramPipeline");
        byte rv= ( Byte )_glIsProgramPipeline.invoke(Byte.class, new Object[]{pipeline_ });
        return (rv!=0);
    }
    /* <command>
            <proto group="Boolean"><ptype>GLboolean</ptype> <name>glIsQuery</name></proto>
            <param><ptype>GLuint</ptype> <name>id</name></param>
            <glx opcode="163" type="single" />
        </command>
         */
    private static Function _glIsQuery ;
    public static boolean glIsQuery (int id_ ) {
        if( _glIsQuery == null )
            _glIsQuery = loadFunction("glIsQuery");
        byte rv= ( Byte )_glIsQuery.invoke(Byte.class, new Object[]{id_ });
        return (rv!=0);
    }
    /* <command>
            <proto group="Boolean"><ptype>GLboolean</ptype> <name>glIsRenderbuffer</name></proto>
            <param><ptype>GLuint</ptype> <name>renderbuffer</name></param>
            <glx opcode="1422" type="vendor" />
        </command>
         */
    private static Function _glIsRenderbuffer ;
    public static boolean glIsRenderbuffer (int renderbuffer_ ) {
        if( _glIsRenderbuffer == null )
            _glIsRenderbuffer = loadFunction("glIsRenderbuffer");
        byte rv= ( Byte )_glIsRenderbuffer.invoke(Byte.class, new Object[]{renderbuffer_ });
        return (rv!=0);
    }
    /* <command>
            <proto group="Boolean"><ptype>GLboolean</ptype> <name>glIsSampler</name></proto>
            <param><ptype>GLuint</ptype> <name>sampler</name></param>
        </command>
         */
    private static Function _glIsSampler ;
    public static boolean glIsSampler (int sampler_ ) {
        if( _glIsSampler == null )
            _glIsSampler = loadFunction("glIsSampler");
        byte rv= ( Byte )_glIsSampler.invoke(Byte.class, new Object[]{sampler_ });
        return (rv!=0);
    }
    /* <command>
            <proto group="Boolean"><ptype>GLboolean</ptype> <name>glIsShader</name></proto>
            <param><ptype>GLuint</ptype> <name>shader</name></param>
            <glx opcode="196" type="single" />
        </command>
         */
    private static Function _glIsShader ;
    public static boolean glIsShader (int shader_ ) {
        if( _glIsShader == null )
            _glIsShader = loadFunction("glIsShader");
        byte rv= ( Byte )_glIsShader.invoke(Byte.class, new Object[]{shader_ });
        return (rv!=0);
    }
    /* <command>
            <proto group="Boolean"><ptype>GLboolean</ptype> <name>glIsSync</name></proto>
            <param group="sync"><ptype>GLsync</ptype> <name>sync</name></param>
        </command>
         */
    private static Function _glIsSync ;
    public static boolean glIsSync (Pointer sync_ ) {
        if( _glIsSync == null )
            _glIsSync = loadFunction("glIsSync");
        byte rv= ( Byte )_glIsSync.invoke(Byte.class, new Object[]{sync_ });
        return (rv!=0);
    }
    /* <command>
            <proto group="Boolean"><ptype>GLboolean</ptype> <name>glIsTexture</name></proto>
            <param group="Texture"><ptype>GLuint</ptype> <name>texture</name></param>
            <glx opcode="146" type="single" />
        </command>
         */
    private static Function _glIsTexture ;
    public static boolean glIsTexture (int texture_ ) {
        if( _glIsTexture == null )
            _glIsTexture = loadFunction("glIsTexture");
        byte rv= ( Byte )_glIsTexture.invoke(Byte.class, new Object[]{texture_ });
        return (rv!=0);
    }
    /* <command>
            <proto group="Boolean"><ptype>GLboolean</ptype> <name>glIsTransformFeedback</name></proto>
            <param><ptype>GLuint</ptype> <name>id</name></param>
        </command>
         */
    private static Function _glIsTransformFeedback ;
    public static boolean glIsTransformFeedback (int id_ ) {
        if( _glIsTransformFeedback == null )
            _glIsTransformFeedback = loadFunction("glIsTransformFeedback");
        byte rv= ( Byte )_glIsTransformFeedback.invoke(Byte.class, new Object[]{id_ });
        return (rv!=0);
    }
    /* <command>
            <proto group="Boolean"><ptype>GLboolean</ptype> <name>glIsVertexArray</name></proto>
            <param><ptype>GLuint</ptype> <name>array</name></param>
            <glx opcode="207" type="single" />
        </command>
         */
    private static Function _glIsVertexArray ;
    public static boolean glIsVertexArray (int array_ ) {
        if( _glIsVertexArray == null )
            _glIsVertexArray = loadFunction("glIsVertexArray");
        byte rv= ( Byte )_glIsVertexArray.invoke(Byte.class, new Object[]{array_ });
        return (rv!=0);
    }
    /* <command>
            <proto>void <name>glLineWidth</name></proto>
            <param group="CheckedFloat32"><ptype>GLfloat</ptype> <name>width</name></param>
            <glx opcode="95" type="render" />
        </command>
         */
    private static Function _glLineWidth ;
    public static void glLineWidth (float width_ ) {
        if( _glLineWidth == null )
            _glLineWidth = loadFunction("glLineWidth");
        _glLineWidth.invoke(Void.class, new Object[]{width_ });
    }
    /* <command>
            <proto>void <name>glLinkProgram</name></proto>
            <param><ptype>GLuint</ptype> <name>program</name></param>
        </command>
         */
    private static Function _glLinkProgram ;
    public static void glLinkProgram (int program_ ) {
        if( _glLinkProgram == null )
            _glLinkProgram = loadFunction("glLinkProgram");
        _glLinkProgram.invoke(Void.class, new Object[]{program_ });
    }
    /* <command>
            <proto>void <name>glLogicOp</name></proto>
            <param group="LogicOp"><ptype>GLenum</ptype> <name>opcode</name></param>
            <glx opcode="161" type="render" />
        </command>
         */
    private static Function _glLogicOp ;
    public static void glLogicOp (int opcode_ ) {
        if( _glLogicOp == null )
            _glLogicOp = loadFunction("glLogicOp");
        _glLogicOp.invoke(Void.class, new Object[]{opcode_ });
    }
    /* <command>
            <proto>void *<name>glMapBuffer</name></proto>
            <param group="BufferTargetARB"><ptype>GLenum</ptype> <name>target</name></param>
            <param group="BufferAccessARB"><ptype>GLenum</ptype> <name>access</name></param>
        </command>
         */
    private static Function _glMapBuffer ;
    public static Pointer glMapBuffer (int target_ , int access_ ) {
        if( _glMapBuffer == null )
            _glMapBuffer = loadFunction("glMapBuffer");
        Pointer rv= ( Pointer )_glMapBuffer.invoke(Pointer.class, new Object[]{target_,access_ });
        return rv;
    }
    /* <command>
            <proto>void *<name>glMapBufferRange</name></proto>
            <param group="BufferTargetARB"><ptype>GLenum</ptype> <name>target</name></param>
            <param group="BufferOffset"><ptype>GLintptr</ptype> <name>offset</name></param>
            <param group="BufferSize"><ptype>GLsizeiptr</ptype> <name>length</name></param>
            <param group="BufferAccessMask"><ptype>GLbitfield</ptype> <name>access</name></param>
            <glx opcode="205" type="single" />
        </command>
         */
    private static Function _glMapBufferRange ;
    public static Pointer glMapBufferRange (int target_ , long offset_ , long length_ , int access_ ) {
        if( _glMapBufferRange == null )
            _glMapBufferRange = loadFunction("glMapBufferRange");
        Pointer rv= ( Pointer )_glMapBufferRange.invoke(Pointer.class, new Object[]{target_,offset_,length_,access_ });
        return rv;
    }
    /* <command>
            <proto>void *<name>glMapNamedBuffer</name></proto>
            <param><ptype>GLuint</ptype> <name>buffer</name></param>
            <param><ptype>GLenum</ptype> <name>access</name></param>
        </command>
         */
    private static Function _glMapNamedBuffer ;
    public static Pointer glMapNamedBuffer (int buffer_ , int access_ ) {
        if( _glMapNamedBuffer == null )
            _glMapNamedBuffer = loadFunction("glMapNamedBuffer");
        Pointer rv= ( Pointer )_glMapNamedBuffer.invoke(Pointer.class, new Object[]{buffer_,access_ });
        return rv;
    }
    /* <command>
            <proto>void *<name>glMapNamedBufferRange</name></proto>
            <param><ptype>GLuint</ptype> <name>buffer</name></param>
            <param><ptype>GLintptr</ptype> <name>offset</name></param>
            <param group="BufferSize"><ptype>GLsizeiptr</ptype> <name>length</name></param>
            <param><ptype>GLbitfield</ptype> <name>access</name></param>
        </command>
         */
    private static Function _glMapNamedBufferRange ;
    public static Pointer glMapNamedBufferRange (int buffer_ , long offset_ , long length_ , int access_ ) {
        if( _glMapNamedBufferRange == null )
            _glMapNamedBufferRange = loadFunction("glMapNamedBufferRange");
        Pointer rv= ( Pointer )_glMapNamedBufferRange.invoke(Pointer.class, new Object[]{buffer_,offset_,length_,access_ });
        return rv;
    }
    /* <command>
            <proto>void <name>glMemoryBarrier</name></proto>
            <param><ptype>GLbitfield</ptype> <name>barriers</name></param>
        </command>
         */
    private static Function _glMemoryBarrier ;
    public static void glMemoryBarrier (int barriers_ ) {
        if( _glMemoryBarrier == null )
            _glMemoryBarrier = loadFunction("glMemoryBarrier");
        _glMemoryBarrier.invoke(Void.class, new Object[]{barriers_ });
    }
    /* <command>
            <proto>void <name>glMemoryBarrierByRegion</name></proto>
            <param><ptype>GLbitfield</ptype> <name>barriers</name></param>
        </command>
         */
    private static Function _glMemoryBarrierByRegion ;
    public static void glMemoryBarrierByRegion (int barriers_ ) {
        if( _glMemoryBarrierByRegion == null )
            _glMemoryBarrierByRegion = loadFunction("glMemoryBarrierByRegion");
        _glMemoryBarrierByRegion.invoke(Void.class, new Object[]{barriers_ });
    }
    /* <command>
            <proto>void <name>glMinSampleShading</name></proto>
            <param group="ColorF"><ptype>GLfloat</ptype> <name>value</name></param>
        </command>
         */
    private static Function _glMinSampleShading ;
    public static void glMinSampleShading (float value_ ) {
        if( _glMinSampleShading == null )
            _glMinSampleShading = loadFunction("glMinSampleShading");
        _glMinSampleShading.invoke(Void.class, new Object[]{value_ });
    }
    /* <command>
            <proto>void <name>glMultiDrawArrays</name></proto>
            <param group="PrimitiveType"><ptype>GLenum</ptype> <name>mode</name></param>
            <param len="COMPSIZE(count)">const <ptype>GLint</ptype> *<name>first</name></param>
            <param len="COMPSIZE(drawcount)">const <ptype>GLsizei</ptype> *<name>count</name></param>
            <param><ptype>GLsizei</ptype> <name>drawcount</name></param>
        </command>
         */
    private static Function _glMultiDrawArrays ;
    public static void glMultiDrawArrays (int mode_ , int[] first_ , int[] count_ , int drawcount_ ) {
        if( _glMultiDrawArrays == null )
            _glMultiDrawArrays = loadFunction("glMultiDrawArrays");
        _glMultiDrawArrays.invoke(Void.class, new Object[]{mode_,first_,count_,drawcount_ });
    }
    /* <command>
            <proto>void <name>glMultiDrawArraysIndirect</name></proto>
            <param><ptype>GLenum</ptype> <name>mode</name></param>
            <param len="COMPSIZE(drawcount,stride)">const void *<name>indirect</name></param>
            <param><ptype>GLsizei</ptype> <name>drawcount</name></param>
            <param><ptype>GLsizei</ptype> <name>stride</name></param>
        </command>
         */
    private static Function _glMultiDrawArraysIndirect ;
    public static void glMultiDrawArraysIndirect (int mode_ , byte[] indirect_ , int drawcount_ , int stride_ ) {
        if( _glMultiDrawArraysIndirect == null )
            _glMultiDrawArraysIndirect = loadFunction("glMultiDrawArraysIndirect");
        _glMultiDrawArraysIndirect.invoke(Void.class, new Object[]{mode_,indirect_,drawcount_,stride_ });
    }
    /* <command>
            <proto>void <name>glMultiDrawElements</name></proto>
            <param group="PrimitiveType"><ptype>GLenum</ptype> <name>mode</name></param>
            <param len="COMPSIZE(drawcount)">const <ptype>GLsizei</ptype> *<name>count</name></param>
            <param group="DrawElementsType"><ptype>GLenum</ptype> <name>type</name></param>
            <param len="COMPSIZE(drawcount)">const void *const*<name>indices</name></param>
            <param><ptype>GLsizei</ptype> <name>drawcount</name></param>
        </command>
         */
    private static Function _glMultiDrawElements ;
    public static void glMultiDrawElements (int mode_ , int[] count_ , int type_ , byte[] indices_ , int drawcount_ ) {
        if( _glMultiDrawElements == null )
            _glMultiDrawElements = loadFunction("glMultiDrawElements");
        _glMultiDrawElements.invoke(Void.class, new Object[]{mode_,count_,type_,indices_,drawcount_ });
    }
    /* <command>
            <proto>void <name>glMultiDrawElementsBaseVertex</name></proto>
            <param><ptype>GLenum</ptype> <name>mode</name></param>
            <param len="COMPSIZE(drawcount)">const <ptype>GLsizei</ptype> *<name>count</name></param>
            <param group="DrawElementsType"><ptype>GLenum</ptype> <name>type</name></param>
            <param len="COMPSIZE(drawcount)">const void *const*<name>indices</name></param>
            <param><ptype>GLsizei</ptype> <name>drawcount</name></param>
            <param len="COMPSIZE(drawcount)">const <ptype>GLint</ptype> *<name>basevertex</name></param>
        </command>
         */
    private static Function _glMultiDrawElementsBaseVertex ;
    public static void glMultiDrawElementsBaseVertex (int mode_ , int[] count_ , int type_ , byte[] indices_ , int drawcount_ , int[] basevertex_ ) {
        if( _glMultiDrawElementsBaseVertex == null )
            _glMultiDrawElementsBaseVertex = loadFunction("glMultiDrawElementsBaseVertex");
        _glMultiDrawElementsBaseVertex.invoke(Void.class, new Object[]{mode_,count_,type_,indices_,drawcount_,basevertex_ });
    }
    /* <command>
            <proto>void <name>glMultiDrawElementsIndirect</name></proto>
            <param><ptype>GLenum</ptype> <name>mode</name></param>
            <param><ptype>GLenum</ptype> <name>type</name></param>
            <param len="COMPSIZE(drawcount,stride)">const void *<name>indirect</name></param>
            <param><ptype>GLsizei</ptype> <name>drawcount</name></param>
            <param><ptype>GLsizei</ptype> <name>stride</name></param>
        </command>
         */
    private static Function _glMultiDrawElementsIndirect ;
    public static void glMultiDrawElementsIndirect (int mode_ , int type_ , byte[] indirect_ , int drawcount_ , int stride_ ) {
        if( _glMultiDrawElementsIndirect == null )
            _glMultiDrawElementsIndirect = loadFunction("glMultiDrawElementsIndirect");
        _glMultiDrawElementsIndirect.invoke(Void.class, new Object[]{mode_,type_,indirect_,drawcount_,stride_ });
    }
    /* <command>
            <proto>void <name>glNamedBufferData</name></proto>
            <param><ptype>GLuint</ptype> <name>buffer</name></param>
            <param group="BufferSize"><ptype>GLsizeiptr</ptype> <name>size</name></param>
            <param>const void *<name>data</name></param>
            <param><ptype>GLenum</ptype> <name>usage</name></param>
        </command>
         */
    private static Function _glNamedBufferData ;
    public static void glNamedBufferData (int buffer_ , long size_ , byte[] data_ , int usage_ ) {
        if( _glNamedBufferData == null )
            _glNamedBufferData = loadFunction("glNamedBufferData");
        _glNamedBufferData.invoke(Void.class, new Object[]{buffer_,size_,data_,usage_ });
    }
    /* <command>
            <proto>void <name>glNamedBufferStorage</name></proto>
            <param><ptype>GLuint</ptype> <name>buffer</name></param>
            <param group="BufferSize"><ptype>GLsizeiptr</ptype> <name>size</name></param>
            <param len="size">const void *<name>data</name></param>
            <param><ptype>GLbitfield</ptype> <name>flags</name></param>
        </command>
         */
    private static Function _glNamedBufferStorage ;
    public static void glNamedBufferStorage (int buffer_ , long size_ , byte[] data_ , int flags_ ) {
        if( _glNamedBufferStorage == null )
            _glNamedBufferStorage = loadFunction("glNamedBufferStorage");
        _glNamedBufferStorage.invoke(Void.class, new Object[]{buffer_,size_,data_,flags_ });
    }
    /* <command>
            <proto>void <name>glNamedBufferSubData</name></proto>
            <param><ptype>GLuint</ptype> <name>buffer</name></param>
            <param><ptype>GLintptr</ptype> <name>offset</name></param>
            <param group="BufferSize"><ptype>GLsizeiptr</ptype> <name>size</name></param>
            <param len="COMPSIZE(size)">const void *<name>data</name></param>
        </command>
         */
    private static Function _glNamedBufferSubData ;
    public static void glNamedBufferSubData (int buffer_ , long offset_ , long size_ , byte[] data_ ) {
        if( _glNamedBufferSubData == null )
            _glNamedBufferSubData = loadFunction("glNamedBufferSubData");
        _glNamedBufferSubData.invoke(Void.class, new Object[]{buffer_,offset_,size_,data_ });
    }
    /* <command>
            <proto>void <name>glNamedFramebufferDrawBuffer</name></proto>
            <param><ptype>GLuint</ptype> <name>framebuffer</name></param>
            <param><ptype>GLenum</ptype> <name>buf</name></param>
        </command>
         */
    private static Function _glNamedFramebufferDrawBuffer ;
    public static void glNamedFramebufferDrawBuffer (int framebuffer_ , int buf_ ) {
        if( _glNamedFramebufferDrawBuffer == null )
            _glNamedFramebufferDrawBuffer = loadFunction("glNamedFramebufferDrawBuffer");
        _glNamedFramebufferDrawBuffer.invoke(Void.class, new Object[]{framebuffer_,buf_ });
    }
    /* <command>
            <proto>void <name>glNamedFramebufferDrawBuffers</name></proto>
            <param><ptype>GLuint</ptype> <name>framebuffer</name></param>
            <param><ptype>GLsizei</ptype> <name>n</name></param>
            <param>const <ptype>GLenum</ptype> *<name>bufs</name></param>
        </command>
         */
    private static Function _glNamedFramebufferDrawBuffers ;
    public static void glNamedFramebufferDrawBuffers (int framebuffer_ , int n_ , int[] bufs_ ) {
        if( _glNamedFramebufferDrawBuffers == null )
            _glNamedFramebufferDrawBuffers = loadFunction("glNamedFramebufferDrawBuffers");
        _glNamedFramebufferDrawBuffers.invoke(Void.class, new Object[]{framebuffer_,n_,bufs_ });
    }
    /* <command>
            <proto>void <name>glNamedFramebufferParameteri</name></proto>
            <param><ptype>GLuint</ptype> <name>framebuffer</name></param>
            <param><ptype>GLenum</ptype> <name>pname</name></param>
            <param><ptype>GLint</ptype> <name>param</name></param>
        </command>
         */
    private static Function _glNamedFramebufferParameteri ;
    public static void glNamedFramebufferParameteri (int framebuffer_ , int pname_ , int param_ ) {
        if( _glNamedFramebufferParameteri == null )
            _glNamedFramebufferParameteri = loadFunction("glNamedFramebufferParameteri");
        _glNamedFramebufferParameteri.invoke(Void.class, new Object[]{framebuffer_,pname_,param_ });
    }
    /* <command>
            <proto>void <name>glNamedFramebufferReadBuffer</name></proto>
            <param><ptype>GLuint</ptype> <name>framebuffer</name></param>
            <param><ptype>GLenum</ptype> <name>src</name></param>
        </command>
         */
    private static Function _glNamedFramebufferReadBuffer ;
    public static void glNamedFramebufferReadBuffer (int framebuffer_ , int src_ ) {
        if( _glNamedFramebufferReadBuffer == null )
            _glNamedFramebufferReadBuffer = loadFunction("glNamedFramebufferReadBuffer");
        _glNamedFramebufferReadBuffer.invoke(Void.class, new Object[]{framebuffer_,src_ });
    }
    /* <command>
            <proto>void <name>glNamedFramebufferRenderbuffer</name></proto>
            <param><ptype>GLuint</ptype> <name>framebuffer</name></param>
            <param><ptype>GLenum</ptype> <name>attachment</name></param>
            <param><ptype>GLenum</ptype> <name>renderbuffertarget</name></param>
            <param><ptype>GLuint</ptype> <name>renderbuffer</name></param>
        </command>
         */
    private static Function _glNamedFramebufferRenderbuffer ;
    public static void glNamedFramebufferRenderbuffer (int framebuffer_ , int attachment_ , int renderbuffertarget_ , int renderbuffer_ ) {
        if( _glNamedFramebufferRenderbuffer == null )
            _glNamedFramebufferRenderbuffer = loadFunction("glNamedFramebufferRenderbuffer");
        _glNamedFramebufferRenderbuffer.invoke(Void.class, new Object[]{framebuffer_,attachment_,renderbuffertarget_,renderbuffer_ });
    }
    /* <command>
            <proto>void <name>glNamedFramebufferTexture</name></proto>
            <param><ptype>GLuint</ptype> <name>framebuffer</name></param>
            <param><ptype>GLenum</ptype> <name>attachment</name></param>
            <param><ptype>GLuint</ptype> <name>texture</name></param>
            <param><ptype>GLint</ptype> <name>level</name></param>
        </command>
         */
    private static Function _glNamedFramebufferTexture ;
    public static void glNamedFramebufferTexture (int framebuffer_ , int attachment_ , int texture_ , int level_ ) {
        if( _glNamedFramebufferTexture == null )
            _glNamedFramebufferTexture = loadFunction("glNamedFramebufferTexture");
        _glNamedFramebufferTexture.invoke(Void.class, new Object[]{framebuffer_,attachment_,texture_,level_ });
    }
    /* <command>
            <proto>void <name>glNamedFramebufferTextureLayer</name></proto>
            <param><ptype>GLuint</ptype> <name>framebuffer</name></param>
            <param><ptype>GLenum</ptype> <name>attachment</name></param>
            <param><ptype>GLuint</ptype> <name>texture</name></param>
            <param><ptype>GLint</ptype> <name>level</name></param>
            <param><ptype>GLint</ptype> <name>layer</name></param>
        </command>
         */
    private static Function _glNamedFramebufferTextureLayer ;
    public static void glNamedFramebufferTextureLayer (int framebuffer_ , int attachment_ , int texture_ , int level_ , int layer_ ) {
        if( _glNamedFramebufferTextureLayer == null )
            _glNamedFramebufferTextureLayer = loadFunction("glNamedFramebufferTextureLayer");
        _glNamedFramebufferTextureLayer.invoke(Void.class, new Object[]{framebuffer_,attachment_,texture_,level_,layer_ });
    }
    /* <command>
            <proto>void <name>glNamedRenderbufferStorage</name></proto>
            <param><ptype>GLuint</ptype> <name>renderbuffer</name></param>
            <param><ptype>GLenum</ptype> <name>internalformat</name></param>
            <param><ptype>GLsizei</ptype> <name>width</name></param>
            <param><ptype>GLsizei</ptype> <name>height</name></param>
        </command>
         */
    private static Function _glNamedRenderbufferStorage ;
    public static void glNamedRenderbufferStorage (int renderbuffer_ , int internalformat_ , int width_ , int height_ ) {
        if( _glNamedRenderbufferStorage == null )
            _glNamedRenderbufferStorage = loadFunction("glNamedRenderbufferStorage");
        _glNamedRenderbufferStorage.invoke(Void.class, new Object[]{renderbuffer_,internalformat_,width_,height_ });
    }
    /* <command>
            <proto>void <name>glNamedRenderbufferStorageMultisample</name></proto>
            <param><ptype>GLuint</ptype> <name>renderbuffer</name></param>
            <param><ptype>GLsizei</ptype> <name>samples</name></param>
            <param><ptype>GLenum</ptype> <name>internalformat</name></param>
            <param><ptype>GLsizei</ptype> <name>width</name></param>
            <param><ptype>GLsizei</ptype> <name>height</name></param>
        </command>
         */
    private static Function _glNamedRenderbufferStorageMultisample ;
    public static void glNamedRenderbufferStorageMultisample (int renderbuffer_ , int samples_ , int internalformat_ , int width_ , int height_ ) {
        if( _glNamedRenderbufferStorageMultisample == null )
            _glNamedRenderbufferStorageMultisample = loadFunction("glNamedRenderbufferStorageMultisample");
        _glNamedRenderbufferStorageMultisample.invoke(Void.class, new Object[]{renderbuffer_,samples_,internalformat_,width_,height_ });
    }
    /* <command>
            <proto>void <name>glObjectLabel</name></proto>
            <param><ptype>GLenum</ptype> <name>identifier</name></param>
            <param><ptype>GLuint</ptype> <name>name</name></param>
            <param><ptype>GLsizei</ptype> <name>length</name></param>
            <param len="COMPSIZE(label,length)">const <ptype>GLchar</ptype> *<name>label</name></param>
        </command>
         */
    private static Function _glObjectLabel ;
    public static void glObjectLabel (int identifier_ , int name_ , int length_ , String label_ ) {
        if( _glObjectLabel == null )
            _glObjectLabel = loadFunction("glObjectLabel");
        _glObjectLabel.invoke(Void.class, new Object[]{identifier_,name_,length_,label_ });
    }
    /* <command>
            <proto>void <name>glObjectPtrLabel</name></proto>
            <param>const void *<name>ptr</name></param>
            <param><ptype>GLsizei</ptype> <name>length</name></param>
            <param len="COMPSIZE(label,length)">const <ptype>GLchar</ptype> *<name>label</name></param>
        </command>
         */
    private static Function _glObjectPtrLabel ;
    public static void glObjectPtrLabel (byte[] ptr_ , int length_ , String label_ ) {
        if( _glObjectPtrLabel == null )
            _glObjectPtrLabel = loadFunction("glObjectPtrLabel");
        _glObjectPtrLabel.invoke(Void.class, new Object[]{ptr_,length_,label_ });
    }
    /* <command>
            <proto>void <name>glPatchParameterfv</name></proto>
            <param><ptype>GLenum</ptype> <name>pname</name></param>
            <param len="COMPSIZE(pname)">const <ptype>GLfloat</ptype> *<name>values</name></param>
        </command>
         */
    private static Function _glPatchParameterfv ;
    public static void glPatchParameterfv (int pname_ , float[] values_ ) {
        if( _glPatchParameterfv == null )
            _glPatchParameterfv = loadFunction("glPatchParameterfv");
        _glPatchParameterfv.invoke(Void.class, new Object[]{pname_,values_ });
    }
    /* <command>
            <proto>void <name>glPatchParameteri</name></proto>
            <param><ptype>GLenum</ptype> <name>pname</name></param>
            <param><ptype>GLint</ptype> <name>value</name></param>
        </command>
         */
    private static Function _glPatchParameteri ;
    public static void glPatchParameteri (int pname_ , int value_ ) {
        if( _glPatchParameteri == null )
            _glPatchParameteri = loadFunction("glPatchParameteri");
        _glPatchParameteri.invoke(Void.class, new Object[]{pname_,value_ });
    }
    /* <command>
            <proto>void <name>glPauseTransformFeedback</name></proto>
        </command>
         */
    private static Function _glPauseTransformFeedback ;
    public static void glPauseTransformFeedback () {
        if( _glPauseTransformFeedback == null )
            _glPauseTransformFeedback = loadFunction("glPauseTransformFeedback");
        _glPauseTransformFeedback.invoke(Void.class, new Object[]{ });
    }
    /* <command>
            <proto>void <name>glPixelStoref</name></proto>
            <param group="PixelStoreParameter"><ptype>GLenum</ptype> <name>pname</name></param>
            <param group="CheckedFloat32"><ptype>GLfloat</ptype> <name>param</name></param>
            <glx opcode="109" type="single" />
        </command>
         */
    private static Function _glPixelStoref ;
    public static void glPixelStoref (int pname_ , float param_ ) {
        if( _glPixelStoref == null )
            _glPixelStoref = loadFunction("glPixelStoref");
        _glPixelStoref.invoke(Void.class, new Object[]{pname_,param_ });
    }
    /* <command>
            <proto>void <name>glPixelStorei</name></proto>
            <param group="PixelStoreParameter"><ptype>GLenum</ptype> <name>pname</name></param>
            <param group="CheckedInt32"><ptype>GLint</ptype> <name>param</name></param>
            <glx opcode="110" type="single" />
        </command>
         */
    private static Function _glPixelStorei ;
    public static void glPixelStorei (int pname_ , int param_ ) {
        if( _glPixelStorei == null )
            _glPixelStorei = loadFunction("glPixelStorei");
        _glPixelStorei.invoke(Void.class, new Object[]{pname_,param_ });
    }
    /* <command>
            <proto>void <name>glPointParameterf</name></proto>
            <param group="PointParameterNameARB"><ptype>GLenum</ptype> <name>pname</name></param>
            <param group="CheckedFloat32"><ptype>GLfloat</ptype> <name>param</name></param>
            <glx opcode="2065" type="render" />
        </command>
         */
    private static Function _glPointParameterf ;
    public static void glPointParameterf (int pname_ , float param_ ) {
        if( _glPointParameterf == null )
            _glPointParameterf = loadFunction("glPointParameterf");
        _glPointParameterf.invoke(Void.class, new Object[]{pname_,param_ });
    }
    /* <command>
            <proto>void <name>glPointParameterfv</name></proto>
            <param group="PointParameterNameARB"><ptype>GLenum</ptype> <name>pname</name></param>
            <param group="CheckedFloat32" len="COMPSIZE(pname)">const <ptype>GLfloat</ptype> *<name>params</name></param>
            <glx opcode="2066" type="render" />
        </command>
         */
    private static Function _glPointParameterfv ;
    public static void glPointParameterfv (int pname_ , float[] params_ ) {
        if( _glPointParameterfv == null )
            _glPointParameterfv = loadFunction("glPointParameterfv");
        _glPointParameterfv.invoke(Void.class, new Object[]{pname_,params_ });
    }
    /* <command>
            <proto>void <name>glPointParameteri</name></proto>
            <param group="PointParameterNameARB"><ptype>GLenum</ptype> <name>pname</name></param>
            <param><ptype>GLint</ptype> <name>param</name></param>
            <glx opcode="4221" type="render" />
        </command>
         */
    private static Function _glPointParameteri ;
    public static void glPointParameteri (int pname_ , int param_ ) {
        if( _glPointParameteri == null )
            _glPointParameteri = loadFunction("glPointParameteri");
        _glPointParameteri.invoke(Void.class, new Object[]{pname_,param_ });
    }
    /* <command>
            <proto>void <name>glPointParameteriv</name></proto>
            <param group="PointParameterNameARB"><ptype>GLenum</ptype> <name>pname</name></param>
            <param len="COMPSIZE(pname)">const <ptype>GLint</ptype> *<name>params</name></param>
            <glx opcode="4222" type="render" />
        </command>
         */
    private static Function _glPointParameteriv ;
    public static void glPointParameteriv (int pname_ , int[] params_ ) {
        if( _glPointParameteriv == null )
            _glPointParameteriv = loadFunction("glPointParameteriv");
        _glPointParameteriv.invoke(Void.class, new Object[]{pname_,params_ });
    }
    /* <command>
            <proto>void <name>glPointSize</name></proto>
            <param group="CheckedFloat32"><ptype>GLfloat</ptype> <name>size</name></param>
            <glx opcode="100" type="render" />
        </command>
         */
    private static Function _glPointSize ;
    public static void glPointSize (float size_ ) {
        if( _glPointSize == null )
            _glPointSize = loadFunction("glPointSize");
        _glPointSize.invoke(Void.class, new Object[]{size_ });
    }
    /* <command>
            <proto>void <name>glPolygonMode</name></proto>
            <param group="MaterialFace"><ptype>GLenum</ptype> <name>face</name></param>
            <param group="PolygonMode"><ptype>GLenum</ptype> <name>mode</name></param>
            <glx opcode="101" type="render" />
        </command>
         */
    private static Function _glPolygonMode ;
    public static void glPolygonMode (int face_ , int mode_ ) {
        if( _glPolygonMode == null )
            _glPolygonMode = loadFunction("glPolygonMode");
        _glPolygonMode.invoke(Void.class, new Object[]{face_,mode_ });
    }
    /* <command>
            <proto>void <name>glPolygonOffset</name></proto>
            <param><ptype>GLfloat</ptype> <name>factor</name></param>
            <param><ptype>GLfloat</ptype> <name>units</name></param>
            <glx opcode="192" type="render" />
        </command>
         */
    private static Function _glPolygonOffset ;
    public static void glPolygonOffset (float factor_ , float units_ ) {
        if( _glPolygonOffset == null )
            _glPolygonOffset = loadFunction("glPolygonOffset");
        _glPolygonOffset.invoke(Void.class, new Object[]{factor_,units_ });
    }
    /* <command>
            <proto>void <name>glPopDebugGroup</name></proto>
        </command>
         */
    private static Function _glPopDebugGroup ;
    public static void glPopDebugGroup () {
        if( _glPopDebugGroup == null )
            _glPopDebugGroup = loadFunction("glPopDebugGroup");
        _glPopDebugGroup.invoke(Void.class, new Object[]{ });
    }
    /* <command>
            <proto>void <name>glPrimitiveRestartIndex</name></proto>
            <param><ptype>GLuint</ptype> <name>index</name></param>
        </command>
         */
    private static Function _glPrimitiveRestartIndex ;
    public static void glPrimitiveRestartIndex (int index_ ) {
        if( _glPrimitiveRestartIndex == null )
            _glPrimitiveRestartIndex = loadFunction("glPrimitiveRestartIndex");
        _glPrimitiveRestartIndex.invoke(Void.class, new Object[]{index_ });
    }
    /* <command>
            <proto>void <name>glProgramBinary</name></proto>
            <param><ptype>GLuint</ptype> <name>program</name></param>
            <param><ptype>GLenum</ptype> <name>binaryFormat</name></param>
            <param len="length">const void *<name>binary</name></param>
            <param><ptype>GLsizei</ptype> <name>length</name></param>
        </command>
         */
    private static Function _glProgramBinary ;
    public static void glProgramBinary (int program_ , int binaryFormat_ , byte[] binary_ , int length_ ) {
        if( _glProgramBinary == null )
            _glProgramBinary = loadFunction("glProgramBinary");
        _glProgramBinary.invoke(Void.class, new Object[]{program_,binaryFormat_,binary_,length_ });
    }
    /* <command>
            <proto>void <name>glProgramParameteri</name></proto>
            <param><ptype>GLuint</ptype> <name>program</name></param>
            <param group="ProgramParameterPName"><ptype>GLenum</ptype> <name>pname</name></param>
            <param><ptype>GLint</ptype> <name>value</name></param>
        </command>
         */
    private static Function _glProgramParameteri ;
    public static void glProgramParameteri (int program_ , int pname_ , int value_ ) {
        if( _glProgramParameteri == null )
            _glProgramParameteri = loadFunction("glProgramParameteri");
        _glProgramParameteri.invoke(Void.class, new Object[]{program_,pname_,value_ });
    }
    /* <command>
            <proto>void <name>glProgramUniform1d</name></proto>
            <param><ptype>GLuint</ptype> <name>program</name></param>
            <param><ptype>GLint</ptype> <name>location</name></param>
            <param><ptype>GLdouble</ptype> <name>v0</name></param>
        </command>
         */
    private static Function _glProgramUniform1d ;
    public static void glProgramUniform1d (int program_ , int location_ , double v0_ ) {
        if( _glProgramUniform1d == null )
            _glProgramUniform1d = loadFunction("glProgramUniform1d");
        _glProgramUniform1d.invoke(Void.class, new Object[]{program_,location_,v0_ });
    }
    /* <command>
            <proto>void <name>glProgramUniform1dv</name></proto>
            <param><ptype>GLuint</ptype> <name>program</name></param>
            <param><ptype>GLint</ptype> <name>location</name></param>
            <param><ptype>GLsizei</ptype> <name>count</name></param>
            <param len="1">const <ptype>GLdouble</ptype> *<name>value</name></param>
        </command>
         */
    private static Function _glProgramUniform1dv ;
    public static void glProgramUniform1dv (int program_ , int location_ , int count_ , double[] value_ ) {
        if( _glProgramUniform1dv == null )
            _glProgramUniform1dv = loadFunction("glProgramUniform1dv");
        _glProgramUniform1dv.invoke(Void.class, new Object[]{program_,location_,count_,value_ });
    }
    /* <command>
            <proto>void <name>glProgramUniform1f</name></proto>
            <param><ptype>GLuint</ptype> <name>program</name></param>
            <param><ptype>GLint</ptype> <name>location</name></param>
            <param><ptype>GLfloat</ptype> <name>v0</name></param>
        </command>
         */
    private static Function _glProgramUniform1f ;
    public static void glProgramUniform1f (int program_ , int location_ , float v0_ ) {
        if( _glProgramUniform1f == null )
            _glProgramUniform1f = loadFunction("glProgramUniform1f");
        _glProgramUniform1f.invoke(Void.class, new Object[]{program_,location_,v0_ });
    }
    /* <command>
            <proto>void <name>glProgramUniform1fv</name></proto>
            <param><ptype>GLuint</ptype> <name>program</name></param>
            <param><ptype>GLint</ptype> <name>location</name></param>
            <param><ptype>GLsizei</ptype> <name>count</name></param>
            <param len="1">const <ptype>GLfloat</ptype> *<name>value</name></param>
        </command>
         */
    private static Function _glProgramUniform1fv ;
    public static void glProgramUniform1fv (int program_ , int location_ , int count_ , float[] value_ ) {
        if( _glProgramUniform1fv == null )
            _glProgramUniform1fv = loadFunction("glProgramUniform1fv");
        _glProgramUniform1fv.invoke(Void.class, new Object[]{program_,location_,count_,value_ });
    }
    /* <command>
            <proto>void <name>glProgramUniform1i</name></proto>
            <param><ptype>GLuint</ptype> <name>program</name></param>
            <param><ptype>GLint</ptype> <name>location</name></param>
            <param><ptype>GLint</ptype> <name>v0</name></param>
        </command>
         */
    private static Function _glProgramUniform1i ;
    public static void glProgramUniform1i (int program_ , int location_ , int v0_ ) {
        if( _glProgramUniform1i == null )
            _glProgramUniform1i = loadFunction("glProgramUniform1i");
        _glProgramUniform1i.invoke(Void.class, new Object[]{program_,location_,v0_ });
    }
    /* <command>
            <proto>void <name>glProgramUniform1iv</name></proto>
            <param><ptype>GLuint</ptype> <name>program</name></param>
            <param><ptype>GLint</ptype> <name>location</name></param>
            <param><ptype>GLsizei</ptype> <name>count</name></param>
            <param len="1">const <ptype>GLint</ptype> *<name>value</name></param>
        </command>
         */
    private static Function _glProgramUniform1iv ;
    public static void glProgramUniform1iv (int program_ , int location_ , int count_ , int[] value_ ) {
        if( _glProgramUniform1iv == null )
            _glProgramUniform1iv = loadFunction("glProgramUniform1iv");
        _glProgramUniform1iv.invoke(Void.class, new Object[]{program_,location_,count_,value_ });
    }
    /* <command>
            <proto>void <name>glProgramUniform1ui</name></proto>
            <param><ptype>GLuint</ptype> <name>program</name></param>
            <param><ptype>GLint</ptype> <name>location</name></param>
            <param><ptype>GLuint</ptype> <name>v0</name></param>
        </command>
         */
    private static Function _glProgramUniform1ui ;
    public static void glProgramUniform1ui (int program_ , int location_ , int v0_ ) {
        if( _glProgramUniform1ui == null )
            _glProgramUniform1ui = loadFunction("glProgramUniform1ui");
        _glProgramUniform1ui.invoke(Void.class, new Object[]{program_,location_,v0_ });
    }
    /* <command>
            <proto>void <name>glProgramUniform1uiv</name></proto>
            <param><ptype>GLuint</ptype> <name>program</name></param>
            <param><ptype>GLint</ptype> <name>location</name></param>
            <param><ptype>GLsizei</ptype> <name>count</name></param>
            <param len="1">const <ptype>GLuint</ptype> *<name>value</name></param>
        </command>
         */
    private static Function _glProgramUniform1uiv ;
    public static void glProgramUniform1uiv (int program_ , int location_ , int count_ , int[] value_ ) {
        if( _glProgramUniform1uiv == null )
            _glProgramUniform1uiv = loadFunction("glProgramUniform1uiv");
        _glProgramUniform1uiv.invoke(Void.class, new Object[]{program_,location_,count_,value_ });
    }
    /* <command>
            <proto>void <name>glProgramUniform2d</name></proto>
            <param><ptype>GLuint</ptype> <name>program</name></param>
            <param><ptype>GLint</ptype> <name>location</name></param>
            <param><ptype>GLdouble</ptype> <name>v0</name></param>
            <param><ptype>GLdouble</ptype> <name>v1</name></param>
        </command>
         */
    private static Function _glProgramUniform2d ;
    public static void glProgramUniform2d (int program_ , int location_ , double v0_ , double v1_ ) {
        if( _glProgramUniform2d == null )
            _glProgramUniform2d = loadFunction("glProgramUniform2d");
        _glProgramUniform2d.invoke(Void.class, new Object[]{program_,location_,v0_,v1_ });
    }
    /* <command>
            <proto>void <name>glProgramUniform2dv</name></proto>
            <param><ptype>GLuint</ptype> <name>program</name></param>
            <param><ptype>GLint</ptype> <name>location</name></param>
            <param><ptype>GLsizei</ptype> <name>count</name></param>
            <param len="2">const <ptype>GLdouble</ptype> *<name>value</name></param>
        </command>
         */
    private static Function _glProgramUniform2dv ;
    public static void glProgramUniform2dv (int program_ , int location_ , int count_ , double[] value_ ) {
        if( _glProgramUniform2dv == null )
            _glProgramUniform2dv = loadFunction("glProgramUniform2dv");
        _glProgramUniform2dv.invoke(Void.class, new Object[]{program_,location_,count_,value_ });
    }
    /* <command>
            <proto>void <name>glProgramUniform2f</name></proto>
            <param><ptype>GLuint</ptype> <name>program</name></param>
            <param><ptype>GLint</ptype> <name>location</name></param>
            <param><ptype>GLfloat</ptype> <name>v0</name></param>
            <param><ptype>GLfloat</ptype> <name>v1</name></param>
        </command>
         */
    private static Function _glProgramUniform2f ;
    public static void glProgramUniform2f (int program_ , int location_ , float v0_ , float v1_ ) {
        if( _glProgramUniform2f == null )
            _glProgramUniform2f = loadFunction("glProgramUniform2f");
        _glProgramUniform2f.invoke(Void.class, new Object[]{program_,location_,v0_,v1_ });
    }
    /* <command>
            <proto>void <name>glProgramUniform2fv</name></proto>
            <param><ptype>GLuint</ptype> <name>program</name></param>
            <param><ptype>GLint</ptype> <name>location</name></param>
            <param><ptype>GLsizei</ptype> <name>count</name></param>
            <param len="2">const <ptype>GLfloat</ptype> *<name>value</name></param>
        </command>
         */
    private static Function _glProgramUniform2fv ;
    public static void glProgramUniform2fv (int program_ , int location_ , int count_ , float[] value_ ) {
        if( _glProgramUniform2fv == null )
            _glProgramUniform2fv = loadFunction("glProgramUniform2fv");
        _glProgramUniform2fv.invoke(Void.class, new Object[]{program_,location_,count_,value_ });
    }
    /* <command>
            <proto>void <name>glProgramUniform2i</name></proto>
            <param><ptype>GLuint</ptype> <name>program</name></param>
            <param><ptype>GLint</ptype> <name>location</name></param>
            <param><ptype>GLint</ptype> <name>v0</name></param>
            <param><ptype>GLint</ptype> <name>v1</name></param>
        </command>
         */
    private static Function _glProgramUniform2i ;
    public static void glProgramUniform2i (int program_ , int location_ , int v0_ , int v1_ ) {
        if( _glProgramUniform2i == null )
            _glProgramUniform2i = loadFunction("glProgramUniform2i");
        _glProgramUniform2i.invoke(Void.class, new Object[]{program_,location_,v0_,v1_ });
    }
    /* <command>
            <proto>void <name>glProgramUniform2iv</name></proto>
            <param><ptype>GLuint</ptype> <name>program</name></param>
            <param><ptype>GLint</ptype> <name>location</name></param>
            <param><ptype>GLsizei</ptype> <name>count</name></param>
            <param len="2">const <ptype>GLint</ptype> *<name>value</name></param>
        </command>
         */
    private static Function _glProgramUniform2iv ;
    public static void glProgramUniform2iv (int program_ , int location_ , int count_ , int[] value_ ) {
        if( _glProgramUniform2iv == null )
            _glProgramUniform2iv = loadFunction("glProgramUniform2iv");
        _glProgramUniform2iv.invoke(Void.class, new Object[]{program_,location_,count_,value_ });
    }
    /* <command>
            <proto>void <name>glProgramUniform2ui</name></proto>
            <param><ptype>GLuint</ptype> <name>program</name></param>
            <param><ptype>GLint</ptype> <name>location</name></param>
            <param><ptype>GLuint</ptype> <name>v0</name></param>
            <param><ptype>GLuint</ptype> <name>v1</name></param>
        </command>
         */
    private static Function _glProgramUniform2ui ;
    public static void glProgramUniform2ui (int program_ , int location_ , int v0_ , int v1_ ) {
        if( _glProgramUniform2ui == null )
            _glProgramUniform2ui = loadFunction("glProgramUniform2ui");
        _glProgramUniform2ui.invoke(Void.class, new Object[]{program_,location_,v0_,v1_ });
    }
    /* <command>
            <proto>void <name>glProgramUniform2uiv</name></proto>
            <param><ptype>GLuint</ptype> <name>program</name></param>
            <param><ptype>GLint</ptype> <name>location</name></param>
            <param><ptype>GLsizei</ptype> <name>count</name></param>
            <param len="2">const <ptype>GLuint</ptype> *<name>value</name></param>
        </command>
         */
    private static Function _glProgramUniform2uiv ;
    public static void glProgramUniform2uiv (int program_ , int location_ , int count_ , int[] value_ ) {
        if( _glProgramUniform2uiv == null )
            _glProgramUniform2uiv = loadFunction("glProgramUniform2uiv");
        _glProgramUniform2uiv.invoke(Void.class, new Object[]{program_,location_,count_,value_ });
    }
    /* <command>
            <proto>void <name>glProgramUniform3d</name></proto>
            <param><ptype>GLuint</ptype> <name>program</name></param>
            <param><ptype>GLint</ptype> <name>location</name></param>
            <param><ptype>GLdouble</ptype> <name>v0</name></param>
            <param><ptype>GLdouble</ptype> <name>v1</name></param>
            <param><ptype>GLdouble</ptype> <name>v2</name></param>
        </command>
         */
    private static Function _glProgramUniform3d ;
    public static void glProgramUniform3d (int program_ , int location_ , double v0_ , double v1_ , double v2_ ) {
        if( _glProgramUniform3d == null )
            _glProgramUniform3d = loadFunction("glProgramUniform3d");
        _glProgramUniform3d.invoke(Void.class, new Object[]{program_,location_,v0_,v1_,v2_ });
    }
    /* <command>
            <proto>void <name>glProgramUniform3dv</name></proto>
            <param><ptype>GLuint</ptype> <name>program</name></param>
            <param><ptype>GLint</ptype> <name>location</name></param>
            <param><ptype>GLsizei</ptype> <name>count</name></param>
            <param len="3">const <ptype>GLdouble</ptype> *<name>value</name></param>
        </command>
         */
    private static Function _glProgramUniform3dv ;
    public static void glProgramUniform3dv (int program_ , int location_ , int count_ , double[] value_ ) {
        if( _glProgramUniform3dv == null )
            _glProgramUniform3dv = loadFunction("glProgramUniform3dv");
        _glProgramUniform3dv.invoke(Void.class, new Object[]{program_,location_,count_,value_ });
    }
    /* <command>
            <proto>void <name>glProgramUniform3f</name></proto>
            <param><ptype>GLuint</ptype> <name>program</name></param>
            <param><ptype>GLint</ptype> <name>location</name></param>
            <param><ptype>GLfloat</ptype> <name>v0</name></param>
            <param><ptype>GLfloat</ptype> <name>v1</name></param>
            <param><ptype>GLfloat</ptype> <name>v2</name></param>
        </command>
         */
    private static Function _glProgramUniform3f ;
    public static void glProgramUniform3f (int program_ , int location_ , float v0_ , float v1_ , float v2_ ) {
        if( _glProgramUniform3f == null )
            _glProgramUniform3f = loadFunction("glProgramUniform3f");
        _glProgramUniform3f.invoke(Void.class, new Object[]{program_,location_,v0_,v1_,v2_ });
    }
    /* <command>
            <proto>void <name>glProgramUniform3fv</name></proto>
            <param><ptype>GLuint</ptype> <name>program</name></param>
            <param><ptype>GLint</ptype> <name>location</name></param>
            <param><ptype>GLsizei</ptype> <name>count</name></param>
            <param len="3">const <ptype>GLfloat</ptype> *<name>value</name></param>
        </command>
         */
    private static Function _glProgramUniform3fv ;
    public static void glProgramUniform3fv (int program_ , int location_ , int count_ , float[] value_ ) {
        if( _glProgramUniform3fv == null )
            _glProgramUniform3fv = loadFunction("glProgramUniform3fv");
        _glProgramUniform3fv.invoke(Void.class, new Object[]{program_,location_,count_,value_ });
    }
    /* <command>
            <proto>void <name>glProgramUniform3i</name></proto>
            <param><ptype>GLuint</ptype> <name>program</name></param>
            <param><ptype>GLint</ptype> <name>location</name></param>
            <param><ptype>GLint</ptype> <name>v0</name></param>
            <param><ptype>GLint</ptype> <name>v1</name></param>
            <param><ptype>GLint</ptype> <name>v2</name></param>
        </command>
         */
    private static Function _glProgramUniform3i ;
    public static void glProgramUniform3i (int program_ , int location_ , int v0_ , int v1_ , int v2_ ) {
        if( _glProgramUniform3i == null )
            _glProgramUniform3i = loadFunction("glProgramUniform3i");
        _glProgramUniform3i.invoke(Void.class, new Object[]{program_,location_,v0_,v1_,v2_ });
    }
    /* <command>
            <proto>void <name>glProgramUniform3iv</name></proto>
            <param><ptype>GLuint</ptype> <name>program</name></param>
            <param><ptype>GLint</ptype> <name>location</name></param>
            <param><ptype>GLsizei</ptype> <name>count</name></param>
            <param len="3">const <ptype>GLint</ptype> *<name>value</name></param>
        </command>
         */
    private static Function _glProgramUniform3iv ;
    public static void glProgramUniform3iv (int program_ , int location_ , int count_ , int[] value_ ) {
        if( _glProgramUniform3iv == null )
            _glProgramUniform3iv = loadFunction("glProgramUniform3iv");
        _glProgramUniform3iv.invoke(Void.class, new Object[]{program_,location_,count_,value_ });
    }
    /* <command>
            <proto>void <name>glProgramUniform3ui</name></proto>
            <param><ptype>GLuint</ptype> <name>program</name></param>
            <param><ptype>GLint</ptype> <name>location</name></param>
            <param><ptype>GLuint</ptype> <name>v0</name></param>
            <param><ptype>GLuint</ptype> <name>v1</name></param>
            <param><ptype>GLuint</ptype> <name>v2</name></param>
        </command>
         */
    private static Function _glProgramUniform3ui ;
    public static void glProgramUniform3ui (int program_ , int location_ , int v0_ , int v1_ , int v2_ ) {
        if( _glProgramUniform3ui == null )
            _glProgramUniform3ui = loadFunction("glProgramUniform3ui");
        _glProgramUniform3ui.invoke(Void.class, new Object[]{program_,location_,v0_,v1_,v2_ });
    }
    /* <command>
            <proto>void <name>glProgramUniform3uiv</name></proto>
            <param><ptype>GLuint</ptype> <name>program</name></param>
            <param><ptype>GLint</ptype> <name>location</name></param>
            <param><ptype>GLsizei</ptype> <name>count</name></param>
            <param len="3">const <ptype>GLuint</ptype> *<name>value</name></param>
        </command>
         */
    private static Function _glProgramUniform3uiv ;
    public static void glProgramUniform3uiv (int program_ , int location_ , int count_ , int[] value_ ) {
        if( _glProgramUniform3uiv == null )
            _glProgramUniform3uiv = loadFunction("glProgramUniform3uiv");
        _glProgramUniform3uiv.invoke(Void.class, new Object[]{program_,location_,count_,value_ });
    }
    /* <command>
            <proto>void <name>glProgramUniform4d</name></proto>
            <param><ptype>GLuint</ptype> <name>program</name></param>
            <param><ptype>GLint</ptype> <name>location</name></param>
            <param><ptype>GLdouble</ptype> <name>v0</name></param>
            <param><ptype>GLdouble</ptype> <name>v1</name></param>
            <param><ptype>GLdouble</ptype> <name>v2</name></param>
            <param><ptype>GLdouble</ptype> <name>v3</name></param>
        </command>
         */
    private static Function _glProgramUniform4d ;
    public static void glProgramUniform4d (int program_ , int location_ , double v0_ , double v1_ , double v2_ , double v3_ ) {
        if( _glProgramUniform4d == null )
            _glProgramUniform4d = loadFunction("glProgramUniform4d");
        _glProgramUniform4d.invoke(Void.class, new Object[]{program_,location_,v0_,v1_,v2_,v3_ });
    }
    /* <command>
            <proto>void <name>glProgramUniform4dv</name></proto>
            <param><ptype>GLuint</ptype> <name>program</name></param>
            <param><ptype>GLint</ptype> <name>location</name></param>
            <param><ptype>GLsizei</ptype> <name>count</name></param>
            <param len="4">const <ptype>GLdouble</ptype> *<name>value</name></param>
        </command>
         */
    private static Function _glProgramUniform4dv ;
    public static void glProgramUniform4dv (int program_ , int location_ , int count_ , double[] value_ ) {
        if( _glProgramUniform4dv == null )
            _glProgramUniform4dv = loadFunction("glProgramUniform4dv");
        _glProgramUniform4dv.invoke(Void.class, new Object[]{program_,location_,count_,value_ });
    }
    /* <command>
            <proto>void <name>glProgramUniform4f</name></proto>
            <param><ptype>GLuint</ptype> <name>program</name></param>
            <param><ptype>GLint</ptype> <name>location</name></param>
            <param><ptype>GLfloat</ptype> <name>v0</name></param>
            <param><ptype>GLfloat</ptype> <name>v1</name></param>
            <param><ptype>GLfloat</ptype> <name>v2</name></param>
            <param><ptype>GLfloat</ptype> <name>v3</name></param>
        </command>
         */
    private static Function _glProgramUniform4f ;
    public static void glProgramUniform4f (int program_ , int location_ , float v0_ , float v1_ , float v2_ , float v3_ ) {
        if( _glProgramUniform4f == null )
            _glProgramUniform4f = loadFunction("glProgramUniform4f");
        _glProgramUniform4f.invoke(Void.class, new Object[]{program_,location_,v0_,v1_,v2_,v3_ });
    }
    /* <command>
            <proto>void <name>glProgramUniform4fv</name></proto>
            <param><ptype>GLuint</ptype> <name>program</name></param>
            <param><ptype>GLint</ptype> <name>location</name></param>
            <param><ptype>GLsizei</ptype> <name>count</name></param>
            <param len="4">const <ptype>GLfloat</ptype> *<name>value</name></param>
        </command>
         */
    private static Function _glProgramUniform4fv ;
    public static void glProgramUniform4fv (int program_ , int location_ , int count_ , float[] value_ ) {
        if( _glProgramUniform4fv == null )
            _glProgramUniform4fv = loadFunction("glProgramUniform4fv");
        _glProgramUniform4fv.invoke(Void.class, new Object[]{program_,location_,count_,value_ });
    }
    /* <command>
            <proto>void <name>glProgramUniform4i</name></proto>
            <param><ptype>GLuint</ptype> <name>program</name></param>
            <param><ptype>GLint</ptype> <name>location</name></param>
            <param><ptype>GLint</ptype> <name>v0</name></param>
            <param><ptype>GLint</ptype> <name>v1</name></param>
            <param><ptype>GLint</ptype> <name>v2</name></param>
            <param><ptype>GLint</ptype> <name>v3</name></param>
        </command>
         */
    private static Function _glProgramUniform4i ;
    public static void glProgramUniform4i (int program_ , int location_ , int v0_ , int v1_ , int v2_ , int v3_ ) {
        if( _glProgramUniform4i == null )
            _glProgramUniform4i = loadFunction("glProgramUniform4i");
        _glProgramUniform4i.invoke(Void.class, new Object[]{program_,location_,v0_,v1_,v2_,v3_ });
    }
    /* <command>
            <proto>void <name>glProgramUniform4iv</name></proto>
            <param><ptype>GLuint</ptype> <name>program</name></param>
            <param><ptype>GLint</ptype> <name>location</name></param>
            <param><ptype>GLsizei</ptype> <name>count</name></param>
            <param len="4">const <ptype>GLint</ptype> *<name>value</name></param>
        </command>
         */
    private static Function _glProgramUniform4iv ;
    public static void glProgramUniform4iv (int program_ , int location_ , int count_ , int[] value_ ) {
        if( _glProgramUniform4iv == null )
            _glProgramUniform4iv = loadFunction("glProgramUniform4iv");
        _glProgramUniform4iv.invoke(Void.class, new Object[]{program_,location_,count_,value_ });
    }
    /* <command>
            <proto>void <name>glProgramUniform4ui</name></proto>
            <param><ptype>GLuint</ptype> <name>program</name></param>
            <param><ptype>GLint</ptype> <name>location</name></param>
            <param><ptype>GLuint</ptype> <name>v0</name></param>
            <param><ptype>GLuint</ptype> <name>v1</name></param>
            <param><ptype>GLuint</ptype> <name>v2</name></param>
            <param><ptype>GLuint</ptype> <name>v3</name></param>
        </command>
         */
    private static Function _glProgramUniform4ui ;
    public static void glProgramUniform4ui (int program_ , int location_ , int v0_ , int v1_ , int v2_ , int v3_ ) {
        if( _glProgramUniform4ui == null )
            _glProgramUniform4ui = loadFunction("glProgramUniform4ui");
        _glProgramUniform4ui.invoke(Void.class, new Object[]{program_,location_,v0_,v1_,v2_,v3_ });
    }
    /* <command>
            <proto>void <name>glProgramUniform4uiv</name></proto>
            <param><ptype>GLuint</ptype> <name>program</name></param>
            <param><ptype>GLint</ptype> <name>location</name></param>
            <param><ptype>GLsizei</ptype> <name>count</name></param>
            <param len="4">const <ptype>GLuint</ptype> *<name>value</name></param>
        </command>
         */
    private static Function _glProgramUniform4uiv ;
    public static void glProgramUniform4uiv (int program_ , int location_ , int count_ , int[] value_ ) {
        if( _glProgramUniform4uiv == null )
            _glProgramUniform4uiv = loadFunction("glProgramUniform4uiv");
        _glProgramUniform4uiv.invoke(Void.class, new Object[]{program_,location_,count_,value_ });
    }
    /* <command>
            <proto>void <name>glProgramUniformMatrix2dv</name></proto>
            <param><ptype>GLuint</ptype> <name>program</name></param>
            <param><ptype>GLint</ptype> <name>location</name></param>
            <param><ptype>GLsizei</ptype> <name>count</name></param>
            <param group="Boolean"><ptype>GLboolean</ptype> <name>transpose</name></param>
            <param len="2">const <ptype>GLdouble</ptype> *<name>value</name></param>
        </command>
         */
    private static Function _glProgramUniformMatrix2dv ;
    public static void glProgramUniformMatrix2dv (int program_ , int location_ , int count_ , boolean transpose_ , double[] value_ ) {
        if( _glProgramUniformMatrix2dv == null )
            _glProgramUniformMatrix2dv = loadFunction("glProgramUniformMatrix2dv");
        _glProgramUniformMatrix2dv.invoke(Void.class, new Object[]{program_,location_,count_,(transpose_ ? (byte)1:(byte)0),value_ });
    }
    /* <command>
            <proto>void <name>glProgramUniformMatrix2fv</name></proto>
            <param><ptype>GLuint</ptype> <name>program</name></param>
            <param><ptype>GLint</ptype> <name>location</name></param>
            <param><ptype>GLsizei</ptype> <name>count</name></param>
            <param group="Boolean"><ptype>GLboolean</ptype> <name>transpose</name></param>
            <param len="2">const <ptype>GLfloat</ptype> *<name>value</name></param>
        </command>
         */
    private static Function _glProgramUniformMatrix2fv ;
    public static void glProgramUniformMatrix2fv (int program_ , int location_ , int count_ , boolean transpose_ , float[] value_ ) {
        if( _glProgramUniformMatrix2fv == null )
            _glProgramUniformMatrix2fv = loadFunction("glProgramUniformMatrix2fv");
        _glProgramUniformMatrix2fv.invoke(Void.class, new Object[]{program_,location_,count_,(transpose_ ? (byte)1:(byte)0),value_ });
    }
    /* <command>
            <proto>void <name>glProgramUniformMatrix2x3dv</name></proto>
            <param><ptype>GLuint</ptype> <name>program</name></param>
            <param><ptype>GLint</ptype> <name>location</name></param>
            <param><ptype>GLsizei</ptype> <name>count</name></param>
            <param group="Boolean"><ptype>GLboolean</ptype> <name>transpose</name></param>
            <param len="count">const <ptype>GLdouble</ptype> *<name>value</name></param>
        </command>
         */
    private static Function _glProgramUniformMatrix2x3dv ;
    public static void glProgramUniformMatrix2x3dv (int program_ , int location_ , int count_ , boolean transpose_ , double[] value_ ) {
        if( _glProgramUniformMatrix2x3dv == null )
            _glProgramUniformMatrix2x3dv = loadFunction("glProgramUniformMatrix2x3dv");
        _glProgramUniformMatrix2x3dv.invoke(Void.class, new Object[]{program_,location_,count_,(transpose_ ? (byte)1:(byte)0),value_ });
    }
    /* <command>
            <proto>void <name>glProgramUniformMatrix2x3fv</name></proto>
            <param><ptype>GLuint</ptype> <name>program</name></param>
            <param><ptype>GLint</ptype> <name>location</name></param>
            <param><ptype>GLsizei</ptype> <name>count</name></param>
            <param group="Boolean"><ptype>GLboolean</ptype> <name>transpose</name></param>
            <param len="count">const <ptype>GLfloat</ptype> *<name>value</name></param>
        </command>
         */
    private static Function _glProgramUniformMatrix2x3fv ;
    public static void glProgramUniformMatrix2x3fv (int program_ , int location_ , int count_ , boolean transpose_ , float[] value_ ) {
        if( _glProgramUniformMatrix2x3fv == null )
            _glProgramUniformMatrix2x3fv = loadFunction("glProgramUniformMatrix2x3fv");
        _glProgramUniformMatrix2x3fv.invoke(Void.class, new Object[]{program_,location_,count_,(transpose_ ? (byte)1:(byte)0),value_ });
    }
    /* <command>
            <proto>void <name>glProgramUniformMatrix2x4dv</name></proto>
            <param><ptype>GLuint</ptype> <name>program</name></param>
            <param><ptype>GLint</ptype> <name>location</name></param>
            <param><ptype>GLsizei</ptype> <name>count</name></param>
            <param group="Boolean"><ptype>GLboolean</ptype> <name>transpose</name></param>
            <param len="count">const <ptype>GLdouble</ptype> *<name>value</name></param>
        </command>
         */
    private static Function _glProgramUniformMatrix2x4dv ;
    public static void glProgramUniformMatrix2x4dv (int program_ , int location_ , int count_ , boolean transpose_ , double[] value_ ) {
        if( _glProgramUniformMatrix2x4dv == null )
            _glProgramUniformMatrix2x4dv = loadFunction("glProgramUniformMatrix2x4dv");
        _glProgramUniformMatrix2x4dv.invoke(Void.class, new Object[]{program_,location_,count_,(transpose_ ? (byte)1:(byte)0),value_ });
    }
    /* <command>
            <proto>void <name>glProgramUniformMatrix2x4fv</name></proto>
            <param><ptype>GLuint</ptype> <name>program</name></param>
            <param><ptype>GLint</ptype> <name>location</name></param>
            <param><ptype>GLsizei</ptype> <name>count</name></param>
            <param group="Boolean"><ptype>GLboolean</ptype> <name>transpose</name></param>
            <param len="count">const <ptype>GLfloat</ptype> *<name>value</name></param>
        </command>
         */
    private static Function _glProgramUniformMatrix2x4fv ;
    public static void glProgramUniformMatrix2x4fv (int program_ , int location_ , int count_ , boolean transpose_ , float[] value_ ) {
        if( _glProgramUniformMatrix2x4fv == null )
            _glProgramUniformMatrix2x4fv = loadFunction("glProgramUniformMatrix2x4fv");
        _glProgramUniformMatrix2x4fv.invoke(Void.class, new Object[]{program_,location_,count_,(transpose_ ? (byte)1:(byte)0),value_ });
    }
    /* <command>
            <proto>void <name>glProgramUniformMatrix3dv</name></proto>
            <param><ptype>GLuint</ptype> <name>program</name></param>
            <param><ptype>GLint</ptype> <name>location</name></param>
            <param><ptype>GLsizei</ptype> <name>count</name></param>
            <param group="Boolean"><ptype>GLboolean</ptype> <name>transpose</name></param>
            <param len="3">const <ptype>GLdouble</ptype> *<name>value</name></param>
        </command>
         */
    private static Function _glProgramUniformMatrix3dv ;
    public static void glProgramUniformMatrix3dv (int program_ , int location_ , int count_ , boolean transpose_ , double[] value_ ) {
        if( _glProgramUniformMatrix3dv == null )
            _glProgramUniformMatrix3dv = loadFunction("glProgramUniformMatrix3dv");
        _glProgramUniformMatrix3dv.invoke(Void.class, new Object[]{program_,location_,count_,(transpose_ ? (byte)1:(byte)0),value_ });
    }
    /* <command>
            <proto>void <name>glProgramUniformMatrix3fv</name></proto>
            <param><ptype>GLuint</ptype> <name>program</name></param>
            <param><ptype>GLint</ptype> <name>location</name></param>
            <param><ptype>GLsizei</ptype> <name>count</name></param>
            <param group="Boolean"><ptype>GLboolean</ptype> <name>transpose</name></param>
            <param len="3">const <ptype>GLfloat</ptype> *<name>value</name></param>
        </command>
         */
    private static Function _glProgramUniformMatrix3fv ;
    public static void glProgramUniformMatrix3fv (int program_ , int location_ , int count_ , boolean transpose_ , float[] value_ ) {
        if( _glProgramUniformMatrix3fv == null )
            _glProgramUniformMatrix3fv = loadFunction("glProgramUniformMatrix3fv");
        _glProgramUniformMatrix3fv.invoke(Void.class, new Object[]{program_,location_,count_,(transpose_ ? (byte)1:(byte)0),value_ });
    }
    /* <command>
            <proto>void <name>glProgramUniformMatrix3x2dv</name></proto>
            <param><ptype>GLuint</ptype> <name>program</name></param>
            <param><ptype>GLint</ptype> <name>location</name></param>
            <param><ptype>GLsizei</ptype> <name>count</name></param>
            <param group="Boolean"><ptype>GLboolean</ptype> <name>transpose</name></param>
            <param len="count">const <ptype>GLdouble</ptype> *<name>value</name></param>
        </command>
         */
    private static Function _glProgramUniformMatrix3x2dv ;
    public static void glProgramUniformMatrix3x2dv (int program_ , int location_ , int count_ , boolean transpose_ , double[] value_ ) {
        if( _glProgramUniformMatrix3x2dv == null )
            _glProgramUniformMatrix3x2dv = loadFunction("glProgramUniformMatrix3x2dv");
        _glProgramUniformMatrix3x2dv.invoke(Void.class, new Object[]{program_,location_,count_,(transpose_ ? (byte)1:(byte)0),value_ });
    }
    /* <command>
            <proto>void <name>glProgramUniformMatrix3x2fv</name></proto>
            <param><ptype>GLuint</ptype> <name>program</name></param>
            <param><ptype>GLint</ptype> <name>location</name></param>
            <param><ptype>GLsizei</ptype> <name>count</name></param>
            <param group="Boolean"><ptype>GLboolean</ptype> <name>transpose</name></param>
            <param len="count">const <ptype>GLfloat</ptype> *<name>value</name></param>
        </command>
         */
    private static Function _glProgramUniformMatrix3x2fv ;
    public static void glProgramUniformMatrix3x2fv (int program_ , int location_ , int count_ , boolean transpose_ , float[] value_ ) {
        if( _glProgramUniformMatrix3x2fv == null )
            _glProgramUniformMatrix3x2fv = loadFunction("glProgramUniformMatrix3x2fv");
        _glProgramUniformMatrix3x2fv.invoke(Void.class, new Object[]{program_,location_,count_,(transpose_ ? (byte)1:(byte)0),value_ });
    }
    /* <command>
            <proto>void <name>glProgramUniformMatrix3x4dv</name></proto>
            <param><ptype>GLuint</ptype> <name>program</name></param>
            <param><ptype>GLint</ptype> <name>location</name></param>
            <param><ptype>GLsizei</ptype> <name>count</name></param>
            <param group="Boolean"><ptype>GLboolean</ptype> <name>transpose</name></param>
            <param len="count">const <ptype>GLdouble</ptype> *<name>value</name></param>
        </command>
         */
    private static Function _glProgramUniformMatrix3x4dv ;
    public static void glProgramUniformMatrix3x4dv (int program_ , int location_ , int count_ , boolean transpose_ , double[] value_ ) {
        if( _glProgramUniformMatrix3x4dv == null )
            _glProgramUniformMatrix3x4dv = loadFunction("glProgramUniformMatrix3x4dv");
        _glProgramUniformMatrix3x4dv.invoke(Void.class, new Object[]{program_,location_,count_,(transpose_ ? (byte)1:(byte)0),value_ });
    }
    /* <command>
            <proto>void <name>glProgramUniformMatrix3x4fv</name></proto>
            <param><ptype>GLuint</ptype> <name>program</name></param>
            <param><ptype>GLint</ptype> <name>location</name></param>
            <param><ptype>GLsizei</ptype> <name>count</name></param>
            <param group="Boolean"><ptype>GLboolean</ptype> <name>transpose</name></param>
            <param len="count">const <ptype>GLfloat</ptype> *<name>value</name></param>
        </command>
         */
    private static Function _glProgramUniformMatrix3x4fv ;
    public static void glProgramUniformMatrix3x4fv (int program_ , int location_ , int count_ , boolean transpose_ , float[] value_ ) {
        if( _glProgramUniformMatrix3x4fv == null )
            _glProgramUniformMatrix3x4fv = loadFunction("glProgramUniformMatrix3x4fv");
        _glProgramUniformMatrix3x4fv.invoke(Void.class, new Object[]{program_,location_,count_,(transpose_ ? (byte)1:(byte)0),value_ });
    }
    /* <command>
            <proto>void <name>glProgramUniformMatrix4dv</name></proto>
            <param><ptype>GLuint</ptype> <name>program</name></param>
            <param><ptype>GLint</ptype> <name>location</name></param>
            <param><ptype>GLsizei</ptype> <name>count</name></param>
            <param group="Boolean"><ptype>GLboolean</ptype> <name>transpose</name></param>
            <param len="4">const <ptype>GLdouble</ptype> *<name>value</name></param>
        </command>
         */
    private static Function _glProgramUniformMatrix4dv ;
    public static void glProgramUniformMatrix4dv (int program_ , int location_ , int count_ , boolean transpose_ , double[] value_ ) {
        if( _glProgramUniformMatrix4dv == null )
            _glProgramUniformMatrix4dv = loadFunction("glProgramUniformMatrix4dv");
        _glProgramUniformMatrix4dv.invoke(Void.class, new Object[]{program_,location_,count_,(transpose_ ? (byte)1:(byte)0),value_ });
    }
    /* <command>
            <proto>void <name>glProgramUniformMatrix4fv</name></proto>
            <param><ptype>GLuint</ptype> <name>program</name></param>
            <param><ptype>GLint</ptype> <name>location</name></param>
            <param><ptype>GLsizei</ptype> <name>count</name></param>
            <param group="Boolean"><ptype>GLboolean</ptype> <name>transpose</name></param>
            <param len="4">const <ptype>GLfloat</ptype> *<name>value</name></param>
        </command>
         */
    private static Function _glProgramUniformMatrix4fv ;
    public static void glProgramUniformMatrix4fv (int program_ , int location_ , int count_ , boolean transpose_ , float[] value_ ) {
        if( _glProgramUniformMatrix4fv == null )
            _glProgramUniformMatrix4fv = loadFunction("glProgramUniformMatrix4fv");
        _glProgramUniformMatrix4fv.invoke(Void.class, new Object[]{program_,location_,count_,(transpose_ ? (byte)1:(byte)0),value_ });
    }
    /* <command>
            <proto>void <name>glProgramUniformMatrix4x2dv</name></proto>
            <param><ptype>GLuint</ptype> <name>program</name></param>
            <param><ptype>GLint</ptype> <name>location</name></param>
            <param><ptype>GLsizei</ptype> <name>count</name></param>
            <param group="Boolean"><ptype>GLboolean</ptype> <name>transpose</name></param>
            <param len="count">const <ptype>GLdouble</ptype> *<name>value</name></param>
        </command>
         */
    private static Function _glProgramUniformMatrix4x2dv ;
    public static void glProgramUniformMatrix4x2dv (int program_ , int location_ , int count_ , boolean transpose_ , double[] value_ ) {
        if( _glProgramUniformMatrix4x2dv == null )
            _glProgramUniformMatrix4x2dv = loadFunction("glProgramUniformMatrix4x2dv");
        _glProgramUniformMatrix4x2dv.invoke(Void.class, new Object[]{program_,location_,count_,(transpose_ ? (byte)1:(byte)0),value_ });
    }
    /* <command>
            <proto>void <name>glProgramUniformMatrix4x2fv</name></proto>
            <param><ptype>GLuint</ptype> <name>program</name></param>
            <param><ptype>GLint</ptype> <name>location</name></param>
            <param><ptype>GLsizei</ptype> <name>count</name></param>
            <param group="Boolean"><ptype>GLboolean</ptype> <name>transpose</name></param>
            <param len="count">const <ptype>GLfloat</ptype> *<name>value</name></param>
        </command>
         */
    private static Function _glProgramUniformMatrix4x2fv ;
    public static void glProgramUniformMatrix4x2fv (int program_ , int location_ , int count_ , boolean transpose_ , float[] value_ ) {
        if( _glProgramUniformMatrix4x2fv == null )
            _glProgramUniformMatrix4x2fv = loadFunction("glProgramUniformMatrix4x2fv");
        _glProgramUniformMatrix4x2fv.invoke(Void.class, new Object[]{program_,location_,count_,(transpose_ ? (byte)1:(byte)0),value_ });
    }
    /* <command>
            <proto>void <name>glProgramUniformMatrix4x3dv</name></proto>
            <param><ptype>GLuint</ptype> <name>program</name></param>
            <param><ptype>GLint</ptype> <name>location</name></param>
            <param><ptype>GLsizei</ptype> <name>count</name></param>
            <param group="Boolean"><ptype>GLboolean</ptype> <name>transpose</name></param>
            <param len="count">const <ptype>GLdouble</ptype> *<name>value</name></param>
        </command>
         */
    private static Function _glProgramUniformMatrix4x3dv ;
    public static void glProgramUniformMatrix4x3dv (int program_ , int location_ , int count_ , boolean transpose_ , double[] value_ ) {
        if( _glProgramUniformMatrix4x3dv == null )
            _glProgramUniformMatrix4x3dv = loadFunction("glProgramUniformMatrix4x3dv");
        _glProgramUniformMatrix4x3dv.invoke(Void.class, new Object[]{program_,location_,count_,(transpose_ ? (byte)1:(byte)0),value_ });
    }
    /* <command>
            <proto>void <name>glProgramUniformMatrix4x3fv</name></proto>
            <param><ptype>GLuint</ptype> <name>program</name></param>
            <param><ptype>GLint</ptype> <name>location</name></param>
            <param><ptype>GLsizei</ptype> <name>count</name></param>
            <param group="Boolean"><ptype>GLboolean</ptype> <name>transpose</name></param>
            <param len="count">const <ptype>GLfloat</ptype> *<name>value</name></param>
        </command>
         */
    private static Function _glProgramUniformMatrix4x3fv ;
    public static void glProgramUniformMatrix4x3fv (int program_ , int location_ , int count_ , boolean transpose_ , float[] value_ ) {
        if( _glProgramUniformMatrix4x3fv == null )
            _glProgramUniformMatrix4x3fv = loadFunction("glProgramUniformMatrix4x3fv");
        _glProgramUniformMatrix4x3fv.invoke(Void.class, new Object[]{program_,location_,count_,(transpose_ ? (byte)1:(byte)0),value_ });
    }
    /* <command>
            <proto>void <name>glProvokingVertex</name></proto>
            <param><ptype>GLenum</ptype> <name>mode</name></param>
        </command>
         */
    private static Function _glProvokingVertex ;
    public static void glProvokingVertex (int mode_ ) {
        if( _glProvokingVertex == null )
            _glProvokingVertex = loadFunction("glProvokingVertex");
        _glProvokingVertex.invoke(Void.class, new Object[]{mode_ });
    }
    /* <command>
            <proto>void <name>glPushDebugGroup</name></proto>
            <param><ptype>GLenum</ptype> <name>source</name></param>
            <param><ptype>GLuint</ptype> <name>id</name></param>
            <param><ptype>GLsizei</ptype> <name>length</name></param>
            <param len="COMPSIZE(message,length)">const <ptype>GLchar</ptype> *<name>message</name></param>
        </command>
         */
    private static Function _glPushDebugGroup ;
    public static void glPushDebugGroup (int source_ , int id_ , int length_ , String message_ ) {
        if( _glPushDebugGroup == null )
            _glPushDebugGroup = loadFunction("glPushDebugGroup");
        _glPushDebugGroup.invoke(Void.class, new Object[]{source_,id_,length_,message_ });
    }
    /* <command>
            <proto>void <name>glQueryCounter</name></proto>
            <param><ptype>GLuint</ptype> <name>id</name></param>
            <param><ptype>GLenum</ptype> <name>target</name></param>
        </command>
         */
    private static Function _glQueryCounter ;
    public static void glQueryCounter (int id_ , int target_ ) {
        if( _glQueryCounter == null )
            _glQueryCounter = loadFunction("glQueryCounter");
        _glQueryCounter.invoke(Void.class, new Object[]{id_,target_ });
    }
    /* <command>
            <proto>void <name>glReadBuffer</name></proto>
            <param group="ReadBufferMode"><ptype>GLenum</ptype> <name>src</name></param>
            <glx opcode="171" type="render" />
        </command>
         */
    private static Function _glReadBuffer ;
    public static void glReadBuffer (int src_ ) {
        if( _glReadBuffer == null )
            _glReadBuffer = loadFunction("glReadBuffer");
        _glReadBuffer.invoke(Void.class, new Object[]{src_ });
    }
    /* <command>
            <proto>void <name>glReadPixels</name></proto>
            <param group="WinCoord"><ptype>GLint</ptype> <name>x</name></param>
            <param group="WinCoord"><ptype>GLint</ptype> <name>y</name></param>
            <param><ptype>GLsizei</ptype> <name>width</name></param>
            <param><ptype>GLsizei</ptype> <name>height</name></param>
            <param group="PixelFormat"><ptype>GLenum</ptype> <name>format</name></param>
            <param group="PixelType"><ptype>GLenum</ptype> <name>type</name></param>
            <param len="COMPSIZE(format,type,width,height)">void *<name>pixels</name></param>
            <glx opcode="111" type="single" />
            <glx comment="PBO protocol" name="glReadPixelsPBO" opcode="345" type="render" />
        </command>
         */
    private static Function _glReadPixels ;
    public static void glReadPixels (int x_ , int y_ , int width_ , int height_ , int format_ , int type_ , byte[] pixels_ ) {
        if( _glReadPixels == null )
            _glReadPixels = loadFunction("glReadPixels");
        _glReadPixels.invoke(Void.class, new Object[]{x_,y_,width_,height_,format_,type_,pixels_ });
    }
    /* <command>
            <proto>void <name>glReadnPixels</name></proto>
            <param><ptype>GLint</ptype> <name>x</name></param>
            <param><ptype>GLint</ptype> <name>y</name></param>
            <param><ptype>GLsizei</ptype> <name>width</name></param>
            <param><ptype>GLsizei</ptype> <name>height</name></param>
            <param><ptype>GLenum</ptype> <name>format</name></param>
            <param><ptype>GLenum</ptype> <name>type</name></param>
            <param><ptype>GLsizei</ptype> <name>bufSize</name></param>
            <param>void *<name>data</name></param>
        </command>
         */
    private static Function _glReadnPixels ;
    public static void glReadnPixels (int x_ , int y_ , int width_ , int height_ , int format_ , int type_ , int bufSize_ , byte[] data_ ) {
        if( _glReadnPixels == null )
            _glReadnPixels = loadFunction("glReadnPixels");
        _glReadnPixels.invoke(Void.class, new Object[]{x_,y_,width_,height_,format_,type_,bufSize_,data_ });
    }
    /* <command>
            <proto>void <name>glReleaseShaderCompiler</name></proto>
        </command>
         */
    private static Function _glReleaseShaderCompiler ;
    public static void glReleaseShaderCompiler () {
        if( _glReleaseShaderCompiler == null )
            _glReleaseShaderCompiler = loadFunction("glReleaseShaderCompiler");
        _glReleaseShaderCompiler.invoke(Void.class, new Object[]{ });
    }
    /* <command>
            <proto>void <name>glRenderbufferStorage</name></proto>
            <param group="RenderbufferTarget"><ptype>GLenum</ptype> <name>target</name></param>
            <param><ptype>GLenum</ptype> <name>internalformat</name></param>
            <param><ptype>GLsizei</ptype> <name>width</name></param>
            <param><ptype>GLsizei</ptype> <name>height</name></param>
            <glx opcode="4318" type="render" />
        </command>
         */
    private static Function _glRenderbufferStorage ;
    public static void glRenderbufferStorage (int target_ , int internalformat_ , int width_ , int height_ ) {
        if( _glRenderbufferStorage == null )
            _glRenderbufferStorage = loadFunction("glRenderbufferStorage");
        _glRenderbufferStorage.invoke(Void.class, new Object[]{target_,internalformat_,width_,height_ });
    }
    /* <command>
            <proto>void <name>glRenderbufferStorageMultisample</name></proto>
            <param><ptype>GLenum</ptype> <name>target</name></param>
            <param><ptype>GLsizei</ptype> <name>samples</name></param>
            <param><ptype>GLenum</ptype> <name>internalformat</name></param>
            <param><ptype>GLsizei</ptype> <name>width</name></param>
            <param><ptype>GLsizei</ptype> <name>height</name></param>
            <glx opcode="4331" type="render" />
        </command>
         */
    private static Function _glRenderbufferStorageMultisample ;
    public static void glRenderbufferStorageMultisample (int target_ , int samples_ , int internalformat_ , int width_ , int height_ ) {
        if( _glRenderbufferStorageMultisample == null )
            _glRenderbufferStorageMultisample = loadFunction("glRenderbufferStorageMultisample");
        _glRenderbufferStorageMultisample.invoke(Void.class, new Object[]{target_,samples_,internalformat_,width_,height_ });
    }
    /* <command>
            <proto>void <name>glResumeTransformFeedback</name></proto>
        </command>
         */
    private static Function _glResumeTransformFeedback ;
    public static void glResumeTransformFeedback () {
        if( _glResumeTransformFeedback == null )
            _glResumeTransformFeedback = loadFunction("glResumeTransformFeedback");
        _glResumeTransformFeedback.invoke(Void.class, new Object[]{ });
    }
    /* <command>
            <proto>void <name>glSampleCoverage</name></proto>
            <param><ptype>GLfloat</ptype> <name>value</name></param>
            <param group="Boolean"><ptype>GLboolean</ptype> <name>invert</name></param>
            <glx opcode="229" type="render" />
        </command>
         */
    private static Function _glSampleCoverage ;
    public static void glSampleCoverage (float value_ , boolean invert_ ) {
        if( _glSampleCoverage == null )
            _glSampleCoverage = loadFunction("glSampleCoverage");
        _glSampleCoverage.invoke(Void.class, new Object[]{value_,(invert_ ? (byte)1:(byte)0) });
    }
    /* <command>
            <proto>void <name>glSampleMaski</name></proto>
            <param><ptype>GLuint</ptype> <name>maskNumber</name></param>
            <param><ptype>GLbitfield</ptype> <name>mask</name></param>
        </command>
         */
    private static Function _glSampleMaski ;
    public static void glSampleMaski (int maskNumber_ , int mask_ ) {
        if( _glSampleMaski == null )
            _glSampleMaski = loadFunction("glSampleMaski");
        _glSampleMaski.invoke(Void.class, new Object[]{maskNumber_,mask_ });
    }
    /* <command>
            <proto>void <name>glSamplerParameterIiv</name></proto>
            <param><ptype>GLuint</ptype> <name>sampler</name></param>
            <param><ptype>GLenum</ptype> <name>pname</name></param>
            <param len="COMPSIZE(pname)">const <ptype>GLint</ptype> *<name>param</name></param>
        </command>
         */
    private static Function _glSamplerParameterIiv ;
    public static void glSamplerParameterIiv (int sampler_ , int pname_ , int[] param_ ) {
        if( _glSamplerParameterIiv == null )
            _glSamplerParameterIiv = loadFunction("glSamplerParameterIiv");
        _glSamplerParameterIiv.invoke(Void.class, new Object[]{sampler_,pname_,param_ });
    }
    /* <command>
            <proto>void <name>glSamplerParameterIuiv</name></proto>
            <param><ptype>GLuint</ptype> <name>sampler</name></param>
            <param><ptype>GLenum</ptype> <name>pname</name></param>
            <param len="COMPSIZE(pname)">const <ptype>GLuint</ptype> *<name>param</name></param>
        </command>
         */
    private static Function _glSamplerParameterIuiv ;
    public static void glSamplerParameterIuiv (int sampler_ , int pname_ , int[] param_ ) {
        if( _glSamplerParameterIuiv == null )
            _glSamplerParameterIuiv = loadFunction("glSamplerParameterIuiv");
        _glSamplerParameterIuiv.invoke(Void.class, new Object[]{sampler_,pname_,param_ });
    }
    /* <command>
            <proto>void <name>glSamplerParameterf</name></proto>
            <param><ptype>GLuint</ptype> <name>sampler</name></param>
            <param><ptype>GLenum</ptype> <name>pname</name></param>
            <param><ptype>GLfloat</ptype> <name>param</name></param>
        </command>
         */
    private static Function _glSamplerParameterf ;
    public static void glSamplerParameterf (int sampler_ , int pname_ , float param_ ) {
        if( _glSamplerParameterf == null )
            _glSamplerParameterf = loadFunction("glSamplerParameterf");
        _glSamplerParameterf.invoke(Void.class, new Object[]{sampler_,pname_,param_ });
    }
    /* <command>
            <proto>void <name>glSamplerParameterfv</name></proto>
            <param><ptype>GLuint</ptype> <name>sampler</name></param>
            <param><ptype>GLenum</ptype> <name>pname</name></param>
            <param len="COMPSIZE(pname)">const <ptype>GLfloat</ptype> *<name>param</name></param>
        </command>
         */
    private static Function _glSamplerParameterfv ;
    public static void glSamplerParameterfv (int sampler_ , int pname_ , float[] param_ ) {
        if( _glSamplerParameterfv == null )
            _glSamplerParameterfv = loadFunction("glSamplerParameterfv");
        _glSamplerParameterfv.invoke(Void.class, new Object[]{sampler_,pname_,param_ });
    }
    /* <command>
            <proto>void <name>glSamplerParameteri</name></proto>
            <param><ptype>GLuint</ptype> <name>sampler</name></param>
            <param><ptype>GLenum</ptype> <name>pname</name></param>
            <param><ptype>GLint</ptype> <name>param</name></param>
        </command>
         */
    private static Function _glSamplerParameteri ;
    public static void glSamplerParameteri (int sampler_ , int pname_ , int param_ ) {
        if( _glSamplerParameteri == null )
            _glSamplerParameteri = loadFunction("glSamplerParameteri");
        _glSamplerParameteri.invoke(Void.class, new Object[]{sampler_,pname_,param_ });
    }
    /* <command>
            <proto>void <name>glSamplerParameteriv</name></proto>
            <param><ptype>GLuint</ptype> <name>sampler</name></param>
            <param><ptype>GLenum</ptype> <name>pname</name></param>
            <param len="COMPSIZE(pname)">const <ptype>GLint</ptype> *<name>param</name></param>
        </command>
         */
    private static Function _glSamplerParameteriv ;
    public static void glSamplerParameteriv (int sampler_ , int pname_ , int[] param_ ) {
        if( _glSamplerParameteriv == null )
            _glSamplerParameteriv = loadFunction("glSamplerParameteriv");
        _glSamplerParameteriv.invoke(Void.class, new Object[]{sampler_,pname_,param_ });
    }
    /* <command>
            <proto>void <name>glScissor</name></proto>
            <param group="WinCoord"><ptype>GLint</ptype> <name>x</name></param>
            <param group="WinCoord"><ptype>GLint</ptype> <name>y</name></param>
            <param><ptype>GLsizei</ptype> <name>width</name></param>
            <param><ptype>GLsizei</ptype> <name>height</name></param>
            <glx opcode="103" type="render" />
        </command>
         */
    private static Function _glScissor ;
    public static void glScissor (int x_ , int y_ , int width_ , int height_ ) {
        if( _glScissor == null )
            _glScissor = loadFunction("glScissor");
        _glScissor.invoke(Void.class, new Object[]{x_,y_,width_,height_ });
    }
    /* <command>
            <proto>void <name>glScissorArrayv</name></proto>
            <param><ptype>GLuint</ptype> <name>first</name></param>
            <param><ptype>GLsizei</ptype> <name>count</name></param>
            <param len="COMPSIZE(count)">const <ptype>GLint</ptype> *<name>v</name></param>
        </command>
         */
    private static Function _glScissorArrayv ;
    public static void glScissorArrayv (int first_ , int count_ , int[] v_ ) {
        if( _glScissorArrayv == null )
            _glScissorArrayv = loadFunction("glScissorArrayv");
        _glScissorArrayv.invoke(Void.class, new Object[]{first_,count_,v_ });
    }
    /* <command>
            <proto>void <name>glScissorIndexed</name></proto>
            <param><ptype>GLuint</ptype> <name>index</name></param>
            <param><ptype>GLint</ptype> <name>left</name></param>
            <param><ptype>GLint</ptype> <name>bottom</name></param>
            <param><ptype>GLsizei</ptype> <name>width</name></param>
            <param><ptype>GLsizei</ptype> <name>height</name></param>
        </command>
         */
    private static Function _glScissorIndexed ;
    public static void glScissorIndexed (int index_ , int left_ , int bottom_ , int width_ , int height_ ) {
        if( _glScissorIndexed == null )
            _glScissorIndexed = loadFunction("glScissorIndexed");
        _glScissorIndexed.invoke(Void.class, new Object[]{index_,left_,bottom_,width_,height_ });
    }
    /* <command>
            <proto>void <name>glScissorIndexedv</name></proto>
            <param><ptype>GLuint</ptype> <name>index</name></param>
            <param len="4">const <ptype>GLint</ptype> *<name>v</name></param>
        </command>
         */
    private static Function _glScissorIndexedv ;
    public static void glScissorIndexedv (int index_ , int[] v_ ) {
        if( _glScissorIndexedv == null )
            _glScissorIndexedv = loadFunction("glScissorIndexedv");
        _glScissorIndexedv.invoke(Void.class, new Object[]{index_,v_ });
    }
    /* <command>
            <proto>void <name>glShaderBinary</name></proto>
            <param><ptype>GLsizei</ptype> <name>count</name></param>
            <param len="count">const <ptype>GLuint</ptype> *<name>shaders</name></param>
            <param><ptype>GLenum</ptype> <name>binaryformat</name></param>
            <param len="length">const void *<name>binary</name></param>
            <param><ptype>GLsizei</ptype> <name>length</name></param>
        </command>
         */
    private static Function _glShaderBinary ;
    public static void glShaderBinary (int count_ , int[] shaders_ , int binaryformat_ , byte[] binary_ , int length_ ) {
        if( _glShaderBinary == null )
            _glShaderBinary = loadFunction("glShaderBinary");
        _glShaderBinary.invoke(Void.class, new Object[]{count_,shaders_,binaryformat_,binary_,length_ });
    }
    /* <command>
            <proto>void <name>glShaderSource</name></proto>
            <param><ptype>GLuint</ptype> <name>shader</name></param>
            <param><ptype>GLsizei</ptype> <name>count</name></param>
            <param len="count">const <ptype>GLchar</ptype> *const*<name>string</name></param>
            <param len="count">const <ptype>GLint</ptype> *<name>length</name></param>
        </command>
         */
    private static Function _glShaderSource ;
    public static void glShaderSource (int shader_ , int count_ , byte[] string_ , int[] length_ ) {
        if( _glShaderSource == null )
            _glShaderSource = loadFunction("glShaderSource");
        _glShaderSource.invoke(Void.class, new Object[]{shader_,count_,string_,length_ });
    }
    /* <command>
            <proto>void <name>glShaderStorageBlockBinding</name></proto>
            <param><ptype>GLuint</ptype> <name>program</name></param>
            <param><ptype>GLuint</ptype> <name>storageBlockIndex</name></param>
            <param><ptype>GLuint</ptype> <name>storageBlockBinding</name></param>
        </command>
         */
    private static Function _glShaderStorageBlockBinding ;
    public static void glShaderStorageBlockBinding (int program_ , int storageBlockIndex_ , int storageBlockBinding_ ) {
        if( _glShaderStorageBlockBinding == null )
            _glShaderStorageBlockBinding = loadFunction("glShaderStorageBlockBinding");
        _glShaderStorageBlockBinding.invoke(Void.class, new Object[]{program_,storageBlockIndex_,storageBlockBinding_ });
    }
    /* <command>
            <proto>void <name>glStencilFunc</name></proto>
            <param group="StencilFunction"><ptype>GLenum</ptype> <name>func</name></param>
            <param group="StencilValue"><ptype>GLint</ptype> <name>ref</name></param>
            <param group="MaskedStencilValue"><ptype>GLuint</ptype> <name>mask</name></param>
            <glx opcode="162" type="render" />
        </command>
         */
    private static Function _glStencilFunc ;
    public static void glStencilFunc (int func_ , int ref_ , int mask_ ) {
        if( _glStencilFunc == null )
            _glStencilFunc = loadFunction("glStencilFunc");
        _glStencilFunc.invoke(Void.class, new Object[]{func_,ref_,mask_ });
    }
    /* <command>
            <proto>void <name>glStencilFuncSeparate</name></proto>
            <param group="StencilFaceDirection"><ptype>GLenum</ptype> <name>face</name></param>
            <param group="StencilFunction"><ptype>GLenum</ptype> <name>func</name></param>
            <param group="StencilValue"><ptype>GLint</ptype> <name>ref</name></param>
            <param group="MaskedStencilValue"><ptype>GLuint</ptype> <name>mask</name></param>
        </command>
         */
    private static Function _glStencilFuncSeparate ;
    public static void glStencilFuncSeparate (int face_ , int func_ , int ref_ , int mask_ ) {
        if( _glStencilFuncSeparate == null )
            _glStencilFuncSeparate = loadFunction("glStencilFuncSeparate");
        _glStencilFuncSeparate.invoke(Void.class, new Object[]{face_,func_,ref_,mask_ });
    }
    /* <command>
            <proto>void <name>glStencilMask</name></proto>
            <param group="MaskedStencilValue"><ptype>GLuint</ptype> <name>mask</name></param>
            <glx opcode="133" type="render" />
        </command>
         */
    private static Function _glStencilMask ;
    public static void glStencilMask (int mask_ ) {
        if( _glStencilMask == null )
            _glStencilMask = loadFunction("glStencilMask");
        _glStencilMask.invoke(Void.class, new Object[]{mask_ });
    }
    /* <command>
            <proto>void <name>glStencilMaskSeparate</name></proto>
            <param group="StencilFaceDirection"><ptype>GLenum</ptype> <name>face</name></param>
            <param group="MaskedStencilValue"><ptype>GLuint</ptype> <name>mask</name></param>
        </command>
         */
    private static Function _glStencilMaskSeparate ;
    public static void glStencilMaskSeparate (int face_ , int mask_ ) {
        if( _glStencilMaskSeparate == null )
            _glStencilMaskSeparate = loadFunction("glStencilMaskSeparate");
        _glStencilMaskSeparate.invoke(Void.class, new Object[]{face_,mask_ });
    }
    /* <command>
            <proto>void <name>glStencilOp</name></proto>
            <param group="StencilOp"><ptype>GLenum</ptype> <name>fail</name></param>
            <param group="StencilOp"><ptype>GLenum</ptype> <name>zfail</name></param>
            <param group="StencilOp"><ptype>GLenum</ptype> <name>zpass</name></param>
            <glx opcode="163" type="render" />
        </command>
         */
    private static Function _glStencilOp ;
    public static void glStencilOp (int fail_ , int zfail_ , int zpass_ ) {
        if( _glStencilOp == null )
            _glStencilOp = loadFunction("glStencilOp");
        _glStencilOp.invoke(Void.class, new Object[]{fail_,zfail_,zpass_ });
    }
    /* <command>
            <proto>void <name>glStencilOpSeparate</name></proto>
            <param group="StencilFaceDirection"><ptype>GLenum</ptype> <name>face</name></param>
            <param group="StencilOp"><ptype>GLenum</ptype> <name>sfail</name></param>
            <param group="StencilOp"><ptype>GLenum</ptype> <name>dpfail</name></param>
            <param group="StencilOp"><ptype>GLenum</ptype> <name>dppass</name></param>
        </command>
         */
    private static Function _glStencilOpSeparate ;
    public static void glStencilOpSeparate (int face_ , int sfail_ , int dpfail_ , int dppass_ ) {
        if( _glStencilOpSeparate == null )
            _glStencilOpSeparate = loadFunction("glStencilOpSeparate");
        _glStencilOpSeparate.invoke(Void.class, new Object[]{face_,sfail_,dpfail_,dppass_ });
    }
    /* <command>
            <proto>void <name>glTexBuffer</name></proto>
            <param group="TextureTarget"><ptype>GLenum</ptype> <name>target</name></param>
            <param><ptype>GLenum</ptype> <name>internalformat</name></param>
            <param><ptype>GLuint</ptype> <name>buffer</name></param>
        </command>
         */
    private static Function _glTexBuffer ;
    public static void glTexBuffer (int target_ , int internalformat_ , int buffer_ ) {
        if( _glTexBuffer == null )
            _glTexBuffer = loadFunction("glTexBuffer");
        _glTexBuffer.invoke(Void.class, new Object[]{target_,internalformat_,buffer_ });
    }
    /* <command>
            <proto>void <name>glTexBufferRange</name></proto>
            <param><ptype>GLenum</ptype> <name>target</name></param>
            <param><ptype>GLenum</ptype> <name>internalformat</name></param>
            <param><ptype>GLuint</ptype> <name>buffer</name></param>
            <param group="BufferOffset"><ptype>GLintptr</ptype> <name>offset</name></param>
            <param group="BufferSize"><ptype>GLsizeiptr</ptype> <name>size</name></param>
        </command>
         */
    private static Function _glTexBufferRange ;
    public static void glTexBufferRange (int target_ , int internalformat_ , int buffer_ , long offset_ , long size_ ) {
        if( _glTexBufferRange == null )
            _glTexBufferRange = loadFunction("glTexBufferRange");
        _glTexBufferRange.invoke(Void.class, new Object[]{target_,internalformat_,buffer_,offset_,size_ });
    }
    /* <command>
            <proto>void <name>glTexImage1D</name></proto>
            <param group="TextureTarget"><ptype>GLenum</ptype> <name>target</name></param>
            <param group="CheckedInt32"><ptype>GLint</ptype> <name>level</name></param>
            <param group="TextureComponentCount"><ptype>GLint</ptype> <name>internalformat</name></param>
            <param><ptype>GLsizei</ptype> <name>width</name></param>
            <param group="CheckedInt32"><ptype>GLint</ptype> <name>border</name></param>
            <param group="PixelFormat"><ptype>GLenum</ptype> <name>format</name></param>
            <param group="PixelType"><ptype>GLenum</ptype> <name>type</name></param>
            <param len="COMPSIZE(format,type,width)">const void *<name>pixels</name></param>
            <glx opcode="109" type="render" />
            <glx comment="PBO protocol" name="glTexImage1DPBO" opcode="328" type="render" />
        </command>
         */
    private static Function _glTexImage1D ;
    public static void glTexImage1D (int target_ , int level_ , int internalformat_ , int width_ , int border_ , int format_ , int type_ , byte[] pixels_ ) {
        if( _glTexImage1D == null )
            _glTexImage1D = loadFunction("glTexImage1D");
        _glTexImage1D.invoke(Void.class, new Object[]{target_,level_,internalformat_,width_,border_,format_,type_,pixels_ });
    }
    /* <command>
            <proto>void <name>glTexImage2D</name></proto>
            <param group="TextureTarget"><ptype>GLenum</ptype> <name>target</name></param>
            <param group="CheckedInt32"><ptype>GLint</ptype> <name>level</name></param>
            <param group="TextureComponentCount"><ptype>GLint</ptype> <name>internalformat</name></param>
            <param><ptype>GLsizei</ptype> <name>width</name></param>
            <param><ptype>GLsizei</ptype> <name>height</name></param>
            <param group="CheckedInt32"><ptype>GLint</ptype> <name>border</name></param>
            <param group="PixelFormat"><ptype>GLenum</ptype> <name>format</name></param>
            <param group="PixelType"><ptype>GLenum</ptype> <name>type</name></param>
            <param len="COMPSIZE(format,type,width,height)">const void *<name>pixels</name></param>
            <glx opcode="110" type="render" />
            <glx comment="PBO protocol" name="glTexImage2DPBO" opcode="329" type="render" />
        </command>
         */
    private static Function _glTexImage2D ;
    public static void glTexImage2D (int target_ , int level_ , int internalformat_ , int width_ , int height_ , int border_ , int format_ , int type_ , byte[] pixels_ ) {
        if( _glTexImage2D == null )
            _glTexImage2D = loadFunction("glTexImage2D");
        _glTexImage2D.invoke(Void.class, new Object[]{target_,level_,internalformat_,width_,height_,border_,format_,type_,pixels_ });
    }
    /* <command>
            <proto>void <name>glTexImage2DMultisample</name></proto>
            <param><ptype>GLenum</ptype> <name>target</name></param>
            <param><ptype>GLsizei</ptype> <name>samples</name></param>
            <param><ptype>GLenum</ptype> <name>internalformat</name></param>
            <param><ptype>GLsizei</ptype> <name>width</name></param>
            <param><ptype>GLsizei</ptype> <name>height</name></param>
            <param group="Boolean"><ptype>GLboolean</ptype> <name>fixedsamplelocations</name></param>
        </command>
         */
    private static Function _glTexImage2DMultisample ;
    public static void glTexImage2DMultisample (int target_ , int samples_ , int internalformat_ , int width_ , int height_ , boolean fixedsamplelocations_ ) {
        if( _glTexImage2DMultisample == null )
            _glTexImage2DMultisample = loadFunction("glTexImage2DMultisample");
        _glTexImage2DMultisample.invoke(Void.class, new Object[]{target_,samples_,internalformat_,width_,height_,(fixedsamplelocations_ ? (byte)1:(byte)0) });
    }
    /* <command>
            <proto>void <name>glTexImage3D</name></proto>
            <param group="TextureTarget"><ptype>GLenum</ptype> <name>target</name></param>
            <param group="CheckedInt32"><ptype>GLint</ptype> <name>level</name></param>
            <param group="TextureComponentCount"><ptype>GLint</ptype> <name>internalformat</name></param>
            <param><ptype>GLsizei</ptype> <name>width</name></param>
            <param><ptype>GLsizei</ptype> <name>height</name></param>
            <param><ptype>GLsizei</ptype> <name>depth</name></param>
            <param group="CheckedInt32"><ptype>GLint</ptype> <name>border</name></param>
            <param group="PixelFormat"><ptype>GLenum</ptype> <name>format</name></param>
            <param group="PixelType"><ptype>GLenum</ptype> <name>type</name></param>
            <param len="COMPSIZE(format,type,width,height,depth)">const void *<name>pixels</name></param>
            <glx opcode="4114" type="render" />
            <glx comment="PBO protocol" name="glTexImage3DPBO" opcode="330" type="render" />
        </command>
         */
    private static Function _glTexImage3D ;
    public static void glTexImage3D (int target_ , int level_ , int internalformat_ , int width_ , int height_ , int depth_ , int border_ , int format_ , int type_ , byte[] pixels_ ) {
        if( _glTexImage3D == null )
            _glTexImage3D = loadFunction("glTexImage3D");
        _glTexImage3D.invoke(Void.class, new Object[]{target_,level_,internalformat_,width_,height_,depth_,border_,format_,type_,pixels_ });
    }
    /* <command>
            <proto>void <name>glTexImage3DMultisample</name></proto>
            <param><ptype>GLenum</ptype> <name>target</name></param>
            <param><ptype>GLsizei</ptype> <name>samples</name></param>
            <param><ptype>GLenum</ptype> <name>internalformat</name></param>
            <param><ptype>GLsizei</ptype> <name>width</name></param>
            <param><ptype>GLsizei</ptype> <name>height</name></param>
            <param><ptype>GLsizei</ptype> <name>depth</name></param>
            <param group="Boolean"><ptype>GLboolean</ptype> <name>fixedsamplelocations</name></param>
        </command>
         */
    private static Function _glTexImage3DMultisample ;
    public static void glTexImage3DMultisample (int target_ , int samples_ , int internalformat_ , int width_ , int height_ , int depth_ , boolean fixedsamplelocations_ ) {
        if( _glTexImage3DMultisample == null )
            _glTexImage3DMultisample = loadFunction("glTexImage3DMultisample");
        _glTexImage3DMultisample.invoke(Void.class, new Object[]{target_,samples_,internalformat_,width_,height_,depth_,(fixedsamplelocations_ ? (byte)1:(byte)0) });
    }
    /* <command>
            <proto>void <name>glTexParameterIiv</name></proto>
            <param group="TextureTarget"><ptype>GLenum</ptype> <name>target</name></param>
            <param group="TextureParameterName"><ptype>GLenum</ptype> <name>pname</name></param>
            <param len="COMPSIZE(pname)">const <ptype>GLint</ptype> *<name>params</name></param>
            <glx opcode="346" type="render" />
        </command>
         */
    private static Function _glTexParameterIiv ;
    public static void glTexParameterIiv (int target_ , int pname_ , int[] params_ ) {
        if( _glTexParameterIiv == null )
            _glTexParameterIiv = loadFunction("glTexParameterIiv");
        _glTexParameterIiv.invoke(Void.class, new Object[]{target_,pname_,params_ });
    }
    /* <command>
            <proto>void <name>glTexParameterIuiv</name></proto>
            <param group="TextureTarget"><ptype>GLenum</ptype> <name>target</name></param>
            <param group="TextureParameterName"><ptype>GLenum</ptype> <name>pname</name></param>
            <param len="COMPSIZE(pname)">const <ptype>GLuint</ptype> *<name>params</name></param>
            <glx opcode="347" type="render" />
        </command>
         */
    private static Function _glTexParameterIuiv ;
    public static void glTexParameterIuiv (int target_ , int pname_ , int[] params_ ) {
        if( _glTexParameterIuiv == null )
            _glTexParameterIuiv = loadFunction("glTexParameterIuiv");
        _glTexParameterIuiv.invoke(Void.class, new Object[]{target_,pname_,params_ });
    }
    /* <command>
            <proto>void <name>glTexParameterf</name></proto>
            <param group="TextureTarget"><ptype>GLenum</ptype> <name>target</name></param>
            <param group="TextureParameterName"><ptype>GLenum</ptype> <name>pname</name></param>
            <param group="CheckedFloat32"><ptype>GLfloat</ptype> <name>param</name></param>
            <glx opcode="105" type="render" />
        </command>
         */
    private static Function _glTexParameterf ;
    public static void glTexParameterf (int target_ , int pname_ , float param_ ) {
        if( _glTexParameterf == null )
            _glTexParameterf = loadFunction("glTexParameterf");
        _glTexParameterf.invoke(Void.class, new Object[]{target_,pname_,param_ });
    }
    /* <command>
            <proto>void <name>glTexParameterfv</name></proto>
            <param group="TextureTarget"><ptype>GLenum</ptype> <name>target</name></param>
            <param group="TextureParameterName"><ptype>GLenum</ptype> <name>pname</name></param>
            <param group="CheckedFloat32" len="COMPSIZE(pname)">const <ptype>GLfloat</ptype> *<name>params</name></param>
            <glx opcode="106" type="render" />
        </command>
         */
    private static Function _glTexParameterfv ;
    public static void glTexParameterfv (int target_ , int pname_ , float[] params_ ) {
        if( _glTexParameterfv == null )
            _glTexParameterfv = loadFunction("glTexParameterfv");
        _glTexParameterfv.invoke(Void.class, new Object[]{target_,pname_,params_ });
    }
    /* <command>
            <proto>void <name>glTexParameteri</name></proto>
            <param group="TextureTarget"><ptype>GLenum</ptype> <name>target</name></param>
            <param group="TextureParameterName"><ptype>GLenum</ptype> <name>pname</name></param>
            <param group="CheckedInt32"><ptype>GLint</ptype> <name>param</name></param>
            <glx opcode="107" type="render" />
        </command>
         */
    private static Function _glTexParameteri ;
    public static void glTexParameteri (int target_ , int pname_ , int param_ ) {
        if( _glTexParameteri == null )
            _glTexParameteri = loadFunction("glTexParameteri");
        _glTexParameteri.invoke(Void.class, new Object[]{target_,pname_,param_ });
    }
    /* <command>
            <proto>void <name>glTexParameteriv</name></proto>
            <param group="TextureTarget"><ptype>GLenum</ptype> <name>target</name></param>
            <param group="TextureParameterName"><ptype>GLenum</ptype> <name>pname</name></param>
            <param group="CheckedInt32" len="COMPSIZE(pname)">const <ptype>GLint</ptype> *<name>params</name></param>
            <glx opcode="108" type="render" />
        </command>
         */
    private static Function _glTexParameteriv ;
    public static void glTexParameteriv (int target_ , int pname_ , int[] params_ ) {
        if( _glTexParameteriv == null )
            _glTexParameteriv = loadFunction("glTexParameteriv");
        _glTexParameteriv.invoke(Void.class, new Object[]{target_,pname_,params_ });
    }
    /* <command>
            <proto>void <name>glTexStorage1D</name></proto>
            <param><ptype>GLenum</ptype> <name>target</name></param>
            <param><ptype>GLsizei</ptype> <name>levels</name></param>
            <param><ptype>GLenum</ptype> <name>internalformat</name></param>
            <param><ptype>GLsizei</ptype> <name>width</name></param>
        </command>
         */
    private static Function _glTexStorage1D ;
    public static void glTexStorage1D (int target_ , int levels_ , int internalformat_ , int width_ ) {
        if( _glTexStorage1D == null )
            _glTexStorage1D = loadFunction("glTexStorage1D");
        _glTexStorage1D.invoke(Void.class, new Object[]{target_,levels_,internalformat_,width_ });
    }
    /* <command>
            <proto>void <name>glTexStorage2D</name></proto>
            <param><ptype>GLenum</ptype> <name>target</name></param>
            <param><ptype>GLsizei</ptype> <name>levels</name></param>
            <param><ptype>GLenum</ptype> <name>internalformat</name></param>
            <param><ptype>GLsizei</ptype> <name>width</name></param>
            <param><ptype>GLsizei</ptype> <name>height</name></param>
        </command>
         */
    private static Function _glTexStorage2D ;
    public static void glTexStorage2D (int target_ , int levels_ , int internalformat_ , int width_ , int height_ ) {
        if( _glTexStorage2D == null )
            _glTexStorage2D = loadFunction("glTexStorage2D");
        _glTexStorage2D.invoke(Void.class, new Object[]{target_,levels_,internalformat_,width_,height_ });
    }
    /* <command>
            <proto>void <name>glTexStorage2DMultisample</name></proto>
            <param><ptype>GLenum</ptype> <name>target</name></param>
            <param><ptype>GLsizei</ptype> <name>samples</name></param>
            <param><ptype>GLenum</ptype> <name>internalformat</name></param>
            <param><ptype>GLsizei</ptype> <name>width</name></param>
            <param><ptype>GLsizei</ptype> <name>height</name></param>
            <param group="Boolean"><ptype>GLboolean</ptype> <name>fixedsamplelocations</name></param>
        </command>
         */
    private static Function _glTexStorage2DMultisample ;
    public static void glTexStorage2DMultisample (int target_ , int samples_ , int internalformat_ , int width_ , int height_ , boolean fixedsamplelocations_ ) {
        if( _glTexStorage2DMultisample == null )
            _glTexStorage2DMultisample = loadFunction("glTexStorage2DMultisample");
        _glTexStorage2DMultisample.invoke(Void.class, new Object[]{target_,samples_,internalformat_,width_,height_,(fixedsamplelocations_ ? (byte)1:(byte)0) });
    }
    /* <command>
            <proto>void <name>glTexStorage3D</name></proto>
            <param><ptype>GLenum</ptype> <name>target</name></param>
            <param><ptype>GLsizei</ptype> <name>levels</name></param>
            <param><ptype>GLenum</ptype> <name>internalformat</name></param>
            <param><ptype>GLsizei</ptype> <name>width</name></param>
            <param><ptype>GLsizei</ptype> <name>height</name></param>
            <param><ptype>GLsizei</ptype> <name>depth</name></param>
        </command>
         */
    private static Function _glTexStorage3D ;
    public static void glTexStorage3D (int target_ , int levels_ , int internalformat_ , int width_ , int height_ , int depth_ ) {
        if( _glTexStorage3D == null )
            _glTexStorage3D = loadFunction("glTexStorage3D");
        _glTexStorage3D.invoke(Void.class, new Object[]{target_,levels_,internalformat_,width_,height_,depth_ });
    }
    /* <command>
            <proto>void <name>glTexStorage3DMultisample</name></proto>
            <param><ptype>GLenum</ptype> <name>target</name></param>
            <param><ptype>GLsizei</ptype> <name>samples</name></param>
            <param><ptype>GLenum</ptype> <name>internalformat</name></param>
            <param><ptype>GLsizei</ptype> <name>width</name></param>
            <param><ptype>GLsizei</ptype> <name>height</name></param>
            <param><ptype>GLsizei</ptype> <name>depth</name></param>
            <param group="Boolean"><ptype>GLboolean</ptype> <name>fixedsamplelocations</name></param>
        </command>
         */
    private static Function _glTexStorage3DMultisample ;
    public static void glTexStorage3DMultisample (int target_ , int samples_ , int internalformat_ , int width_ , int height_ , int depth_ , boolean fixedsamplelocations_ ) {
        if( _glTexStorage3DMultisample == null )
            _glTexStorage3DMultisample = loadFunction("glTexStorage3DMultisample");
        _glTexStorage3DMultisample.invoke(Void.class, new Object[]{target_,samples_,internalformat_,width_,height_,depth_,(fixedsamplelocations_ ? (byte)1:(byte)0) });
    }
    /* <command>
            <proto>void <name>glTexSubImage1D</name></proto>
            <param group="TextureTarget"><ptype>GLenum</ptype> <name>target</name></param>
            <param group="CheckedInt32"><ptype>GLint</ptype> <name>level</name></param>
            <param group="CheckedInt32"><ptype>GLint</ptype> <name>xoffset</name></param>
            <param><ptype>GLsizei</ptype> <name>width</name></param>
            <param group="PixelFormat"><ptype>GLenum</ptype> <name>format</name></param>
            <param group="PixelType"><ptype>GLenum</ptype> <name>type</name></param>
            <param len="COMPSIZE(format,type,width)">const void *<name>pixels</name></param>
            <glx opcode="4099" type="render" />
            <glx comment="PBO protocol" name="glTexSubImage1DPBO" opcode="331" type="render" />
        </command>
         */
    private static Function _glTexSubImage1D ;
    public static void glTexSubImage1D (int target_ , int level_ , int xoffset_ , int width_ , int format_ , int type_ , byte[] pixels_ ) {
        if( _glTexSubImage1D == null )
            _glTexSubImage1D = loadFunction("glTexSubImage1D");
        _glTexSubImage1D.invoke(Void.class, new Object[]{target_,level_,xoffset_,width_,format_,type_,pixels_ });
    }
    /* <command>
            <proto>void <name>glTexSubImage2D</name></proto>
            <param group="TextureTarget"><ptype>GLenum</ptype> <name>target</name></param>
            <param group="CheckedInt32"><ptype>GLint</ptype> <name>level</name></param>
            <param group="CheckedInt32"><ptype>GLint</ptype> <name>xoffset</name></param>
            <param group="CheckedInt32"><ptype>GLint</ptype> <name>yoffset</name></param>
            <param><ptype>GLsizei</ptype> <name>width</name></param>
            <param><ptype>GLsizei</ptype> <name>height</name></param>
            <param group="PixelFormat"><ptype>GLenum</ptype> <name>format</name></param>
            <param group="PixelType"><ptype>GLenum</ptype> <name>type</name></param>
            <param len="COMPSIZE(format,type,width,height)">const void *<name>pixels</name></param>
            <glx opcode="4100" type="render" />
            <glx comment="PBO protocol" name="glTexSubImage2DPBO" opcode="332" type="render" />
        </command>
         */
    private static Function _glTexSubImage2D ;
    public static void glTexSubImage2D (int target_ , int level_ , int xoffset_ , int yoffset_ , int width_ , int height_ , int format_ , int type_ , byte[] pixels_ ) {
        if( _glTexSubImage2D == null )
            _glTexSubImage2D = loadFunction("glTexSubImage2D");
        _glTexSubImage2D.invoke(Void.class, new Object[]{target_,level_,xoffset_,yoffset_,width_,height_,format_,type_,pixels_ });
    }
    /* <command>
            <proto>void <name>glTexSubImage3D</name></proto>
            <param group="TextureTarget"><ptype>GLenum</ptype> <name>target</name></param>
            <param group="CheckedInt32"><ptype>GLint</ptype> <name>level</name></param>
            <param group="CheckedInt32"><ptype>GLint</ptype> <name>xoffset</name></param>
            <param group="CheckedInt32"><ptype>GLint</ptype> <name>yoffset</name></param>
            <param group="CheckedInt32"><ptype>GLint</ptype> <name>zoffset</name></param>
            <param><ptype>GLsizei</ptype> <name>width</name></param>
            <param><ptype>GLsizei</ptype> <name>height</name></param>
            <param><ptype>GLsizei</ptype> <name>depth</name></param>
            <param group="PixelFormat"><ptype>GLenum</ptype> <name>format</name></param>
            <param group="PixelType"><ptype>GLenum</ptype> <name>type</name></param>
            <param len="COMPSIZE(format,type,width,height,depth)">const void *<name>pixels</name></param>
            <glx opcode="4115" type="render" />
            <glx comment="PBO protocol" name="glTexSubImage3DPBO" opcode="333" type="render" />
        </command>
         */
    private static Function _glTexSubImage3D ;
    public static void glTexSubImage3D (int target_ , int level_ , int xoffset_ , int yoffset_ , int zoffset_ , int width_ , int height_ , int depth_ , int format_ , int type_ , byte[] pixels_ ) {
        if( _glTexSubImage3D == null )
            _glTexSubImage3D = loadFunction("glTexSubImage3D");
        _glTexSubImage3D.invoke(Void.class, new Object[]{target_,level_,xoffset_,yoffset_,zoffset_,width_,height_,depth_,format_,type_,pixels_ });
    }
    /* <command>
            <proto>void <name>glTextureBarrier</name></proto>
        </command>
         */
    private static Function _glTextureBarrier ;
    public static void glTextureBarrier () {
        if( _glTextureBarrier == null )
            _glTextureBarrier = loadFunction("glTextureBarrier");
        _glTextureBarrier.invoke(Void.class, new Object[]{ });
    }
    /* <command>
            <proto>void <name>glTextureBuffer</name></proto>
            <param><ptype>GLuint</ptype> <name>texture</name></param>
            <param><ptype>GLenum</ptype> <name>internalformat</name></param>
            <param><ptype>GLuint</ptype> <name>buffer</name></param>
        </command>
         */
    private static Function _glTextureBuffer ;
    public static void glTextureBuffer (int texture_ , int internalformat_ , int buffer_ ) {
        if( _glTextureBuffer == null )
            _glTextureBuffer = loadFunction("glTextureBuffer");
        _glTextureBuffer.invoke(Void.class, new Object[]{texture_,internalformat_,buffer_ });
    }
    /* <command>
            <proto>void <name>glTextureBufferRange</name></proto>
            <param><ptype>GLuint</ptype> <name>texture</name></param>
            <param><ptype>GLenum</ptype> <name>internalformat</name></param>
            <param><ptype>GLuint</ptype> <name>buffer</name></param>
            <param><ptype>GLintptr</ptype> <name>offset</name></param>
            <param group="BufferSize"><ptype>GLsizeiptr</ptype> <name>size</name></param>
        </command>
         */
    private static Function _glTextureBufferRange ;
    public static void glTextureBufferRange (int texture_ , int internalformat_ , int buffer_ , long offset_ , long size_ ) {
        if( _glTextureBufferRange == null )
            _glTextureBufferRange = loadFunction("glTextureBufferRange");
        _glTextureBufferRange.invoke(Void.class, new Object[]{texture_,internalformat_,buffer_,offset_,size_ });
    }
    /* <command>
            <proto>void <name>glTextureParameterIiv</name></proto>
            <param><ptype>GLuint</ptype> <name>texture</name></param>
            <param><ptype>GLenum</ptype> <name>pname</name></param>
            <param>const <ptype>GLint</ptype> *<name>params</name></param>
        </command>
         */
    private static Function _glTextureParameterIiv ;
    public static void glTextureParameterIiv (int texture_ , int pname_ , int[] params_ ) {
        if( _glTextureParameterIiv == null )
            _glTextureParameterIiv = loadFunction("glTextureParameterIiv");
        _glTextureParameterIiv.invoke(Void.class, new Object[]{texture_,pname_,params_ });
    }
    /* <command>
            <proto>void <name>glTextureParameterIuiv</name></proto>
            <param><ptype>GLuint</ptype> <name>texture</name></param>
            <param><ptype>GLenum</ptype> <name>pname</name></param>
            <param>const <ptype>GLuint</ptype> *<name>params</name></param>
        </command>
         */
    private static Function _glTextureParameterIuiv ;
    public static void glTextureParameterIuiv (int texture_ , int pname_ , int[] params_ ) {
        if( _glTextureParameterIuiv == null )
            _glTextureParameterIuiv = loadFunction("glTextureParameterIuiv");
        _glTextureParameterIuiv.invoke(Void.class, new Object[]{texture_,pname_,params_ });
    }
    /* <command>
            <proto>void <name>glTextureParameterf</name></proto>
            <param><ptype>GLuint</ptype> <name>texture</name></param>
            <param><ptype>GLenum</ptype> <name>pname</name></param>
            <param><ptype>GLfloat</ptype> <name>param</name></param>
        </command>
         */
    private static Function _glTextureParameterf ;
    public static void glTextureParameterf (int texture_ , int pname_ , float param_ ) {
        if( _glTextureParameterf == null )
            _glTextureParameterf = loadFunction("glTextureParameterf");
        _glTextureParameterf.invoke(Void.class, new Object[]{texture_,pname_,param_ });
    }
    /* <command>
            <proto>void <name>glTextureParameterfv</name></proto>
            <param><ptype>GLuint</ptype> <name>texture</name></param>
            <param><ptype>GLenum</ptype> <name>pname</name></param>
            <param>const <ptype>GLfloat</ptype> *<name>param</name></param>
        </command>
         */
    private static Function _glTextureParameterfv ;
    public static void glTextureParameterfv (int texture_ , int pname_ , float[] param_ ) {
        if( _glTextureParameterfv == null )
            _glTextureParameterfv = loadFunction("glTextureParameterfv");
        _glTextureParameterfv.invoke(Void.class, new Object[]{texture_,pname_,param_ });
    }
    /* <command>
            <proto>void <name>glTextureParameteri</name></proto>
            <param><ptype>GLuint</ptype> <name>texture</name></param>
            <param><ptype>GLenum</ptype> <name>pname</name></param>
            <param><ptype>GLint</ptype> <name>param</name></param>
        </command>
         */
    private static Function _glTextureParameteri ;
    public static void glTextureParameteri (int texture_ , int pname_ , int param_ ) {
        if( _glTextureParameteri == null )
            _glTextureParameteri = loadFunction("glTextureParameteri");
        _glTextureParameteri.invoke(Void.class, new Object[]{texture_,pname_,param_ });
    }
    /* <command>
            <proto>void <name>glTextureParameteriv</name></proto>
            <param><ptype>GLuint</ptype> <name>texture</name></param>
            <param><ptype>GLenum</ptype> <name>pname</name></param>
            <param>const <ptype>GLint</ptype> *<name>param</name></param>
        </command>
         */
    private static Function _glTextureParameteriv ;
    public static void glTextureParameteriv (int texture_ , int pname_ , int[] param_ ) {
        if( _glTextureParameteriv == null )
            _glTextureParameteriv = loadFunction("glTextureParameteriv");
        _glTextureParameteriv.invoke(Void.class, new Object[]{texture_,pname_,param_ });
    }
    /* <command>
            <proto>void <name>glTextureStorage1D</name></proto>
            <param><ptype>GLuint</ptype> <name>texture</name></param>
            <param><ptype>GLsizei</ptype> <name>levels</name></param>
            <param><ptype>GLenum</ptype> <name>internalformat</name></param>
            <param><ptype>GLsizei</ptype> <name>width</name></param>
        </command>
         */
    private static Function _glTextureStorage1D ;
    public static void glTextureStorage1D (int texture_ , int levels_ , int internalformat_ , int width_ ) {
        if( _glTextureStorage1D == null )
            _glTextureStorage1D = loadFunction("glTextureStorage1D");
        _glTextureStorage1D.invoke(Void.class, new Object[]{texture_,levels_,internalformat_,width_ });
    }
    /* <command>
            <proto>void <name>glTextureStorage2D</name></proto>
            <param><ptype>GLuint</ptype> <name>texture</name></param>
            <param><ptype>GLsizei</ptype> <name>levels</name></param>
            <param><ptype>GLenum</ptype> <name>internalformat</name></param>
            <param><ptype>GLsizei</ptype> <name>width</name></param>
            <param><ptype>GLsizei</ptype> <name>height</name></param>
        </command>
         */
    private static Function _glTextureStorage2D ;
    public static void glTextureStorage2D (int texture_ , int levels_ , int internalformat_ , int width_ , int height_ ) {
        if( _glTextureStorage2D == null )
            _glTextureStorage2D = loadFunction("glTextureStorage2D");
        _glTextureStorage2D.invoke(Void.class, new Object[]{texture_,levels_,internalformat_,width_,height_ });
    }
    /* <command>
            <proto>void <name>glTextureStorage2DMultisample</name></proto>
            <param><ptype>GLuint</ptype> <name>texture</name></param>
            <param><ptype>GLsizei</ptype> <name>samples</name></param>
            <param><ptype>GLenum</ptype> <name>internalformat</name></param>
            <param><ptype>GLsizei</ptype> <name>width</name></param>
            <param><ptype>GLsizei</ptype> <name>height</name></param>
            <param><ptype>GLboolean</ptype> <name>fixedsamplelocations</name></param>
        </command>
         */
    private static Function _glTextureStorage2DMultisample ;
    public static void glTextureStorage2DMultisample (int texture_ , int samples_ , int internalformat_ , int width_ , int height_ , boolean fixedsamplelocations_ ) {
        if( _glTextureStorage2DMultisample == null )
            _glTextureStorage2DMultisample = loadFunction("glTextureStorage2DMultisample");
        _glTextureStorage2DMultisample.invoke(Void.class, new Object[]{texture_,samples_,internalformat_,width_,height_,(fixedsamplelocations_ ? (byte)1:(byte)0) });
    }
    /* <command>
            <proto>void <name>glTextureStorage3D</name></proto>
            <param><ptype>GLuint</ptype> <name>texture</name></param>
            <param><ptype>GLsizei</ptype> <name>levels</name></param>
            <param><ptype>GLenum</ptype> <name>internalformat</name></param>
            <param><ptype>GLsizei</ptype> <name>width</name></param>
            <param><ptype>GLsizei</ptype> <name>height</name></param>
            <param><ptype>GLsizei</ptype> <name>depth</name></param>
        </command>
         */
    private static Function _glTextureStorage3D ;
    public static void glTextureStorage3D (int texture_ , int levels_ , int internalformat_ , int width_ , int height_ , int depth_ ) {
        if( _glTextureStorage3D == null )
            _glTextureStorage3D = loadFunction("glTextureStorage3D");
        _glTextureStorage3D.invoke(Void.class, new Object[]{texture_,levels_,internalformat_,width_,height_,depth_ });
    }
    /* <command>
            <proto>void <name>glTextureStorage3DMultisample</name></proto>
            <param><ptype>GLuint</ptype> <name>texture</name></param>
            <param><ptype>GLsizei</ptype> <name>samples</name></param>
            <param><ptype>GLenum</ptype> <name>internalformat</name></param>
            <param><ptype>GLsizei</ptype> <name>width</name></param>
            <param><ptype>GLsizei</ptype> <name>height</name></param>
            <param><ptype>GLsizei</ptype> <name>depth</name></param>
            <param><ptype>GLboolean</ptype> <name>fixedsamplelocations</name></param>
        </command>
         */
    private static Function _glTextureStorage3DMultisample ;
    public static void glTextureStorage3DMultisample (int texture_ , int samples_ , int internalformat_ , int width_ , int height_ , int depth_ , boolean fixedsamplelocations_ ) {
        if( _glTextureStorage3DMultisample == null )
            _glTextureStorage3DMultisample = loadFunction("glTextureStorage3DMultisample");
        _glTextureStorage3DMultisample.invoke(Void.class, new Object[]{texture_,samples_,internalformat_,width_,height_,depth_,(fixedsamplelocations_ ? (byte)1:(byte)0) });
    }
    /* <command>
            <proto>void <name>glTextureSubImage1D</name></proto>
            <param><ptype>GLuint</ptype> <name>texture</name></param>
            <param><ptype>GLint</ptype> <name>level</name></param>
            <param><ptype>GLint</ptype> <name>xoffset</name></param>
            <param><ptype>GLsizei</ptype> <name>width</name></param>
            <param><ptype>GLenum</ptype> <name>format</name></param>
            <param><ptype>GLenum</ptype> <name>type</name></param>
            <param>const void *<name>pixels</name></param>
        </command>
         */
    private static Function _glTextureSubImage1D ;
    public static void glTextureSubImage1D (int texture_ , int level_ , int xoffset_ , int width_ , int format_ , int type_ , byte[] pixels_ ) {
        if( _glTextureSubImage1D == null )
            _glTextureSubImage1D = loadFunction("glTextureSubImage1D");
        _glTextureSubImage1D.invoke(Void.class, new Object[]{texture_,level_,xoffset_,width_,format_,type_,pixels_ });
    }
    /* <command>
            <proto>void <name>glTextureSubImage2D</name></proto>
            <param><ptype>GLuint</ptype> <name>texture</name></param>
            <param><ptype>GLint</ptype> <name>level</name></param>
            <param><ptype>GLint</ptype> <name>xoffset</name></param>
            <param><ptype>GLint</ptype> <name>yoffset</name></param>
            <param><ptype>GLsizei</ptype> <name>width</name></param>
            <param><ptype>GLsizei</ptype> <name>height</name></param>
            <param><ptype>GLenum</ptype> <name>format</name></param>
            <param><ptype>GLenum</ptype> <name>type</name></param>
            <param>const void *<name>pixels</name></param>
        </command>
         */
    private static Function _glTextureSubImage2D ;
    public static void glTextureSubImage2D (int texture_ , int level_ , int xoffset_ , int yoffset_ , int width_ , int height_ , int format_ , int type_ , byte[] pixels_ ) {
        if( _glTextureSubImage2D == null )
            _glTextureSubImage2D = loadFunction("glTextureSubImage2D");
        _glTextureSubImage2D.invoke(Void.class, new Object[]{texture_,level_,xoffset_,yoffset_,width_,height_,format_,type_,pixels_ });
    }
    /* <command>
            <proto>void <name>glTextureSubImage3D</name></proto>
            <param><ptype>GLuint</ptype> <name>texture</name></param>
            <param><ptype>GLint</ptype> <name>level</name></param>
            <param><ptype>GLint</ptype> <name>xoffset</name></param>
            <param><ptype>GLint</ptype> <name>yoffset</name></param>
            <param><ptype>GLint</ptype> <name>zoffset</name></param>
            <param><ptype>GLsizei</ptype> <name>width</name></param>
            <param><ptype>GLsizei</ptype> <name>height</name></param>
            <param><ptype>GLsizei</ptype> <name>depth</name></param>
            <param><ptype>GLenum</ptype> <name>format</name></param>
            <param><ptype>GLenum</ptype> <name>type</name></param>
            <param>const void *<name>pixels</name></param>
        </command>
         */
    private static Function _glTextureSubImage3D ;
    public static void glTextureSubImage3D (int texture_ , int level_ , int xoffset_ , int yoffset_ , int zoffset_ , int width_ , int height_ , int depth_ , int format_ , int type_ , byte[] pixels_ ) {
        if( _glTextureSubImage3D == null )
            _glTextureSubImage3D = loadFunction("glTextureSubImage3D");
        _glTextureSubImage3D.invoke(Void.class, new Object[]{texture_,level_,xoffset_,yoffset_,zoffset_,width_,height_,depth_,format_,type_,pixels_ });
    }
    /* <command>
            <proto>void <name>glTextureView</name></proto>
            <param><ptype>GLuint</ptype> <name>texture</name></param>
            <param><ptype>GLenum</ptype> <name>target</name></param>
            <param><ptype>GLuint</ptype> <name>origtexture</name></param>
            <param><ptype>GLenum</ptype> <name>internalformat</name></param>
            <param><ptype>GLuint</ptype> <name>minlevel</name></param>
            <param><ptype>GLuint</ptype> <name>numlevels</name></param>
            <param><ptype>GLuint</ptype> <name>minlayer</name></param>
            <param><ptype>GLuint</ptype> <name>numlayers</name></param>
        </command>
         */
    private static Function _glTextureView ;
    public static void glTextureView (int texture_ , int target_ , int origtexture_ , int internalformat_ , int minlevel_ , int numlevels_ , int minlayer_ , int numlayers_ ) {
        if( _glTextureView == null )
            _glTextureView = loadFunction("glTextureView");
        _glTextureView.invoke(Void.class, new Object[]{texture_,target_,origtexture_,internalformat_,minlevel_,numlevels_,minlayer_,numlayers_ });
    }
    /* <command>
            <proto>void <name>glTransformFeedbackBufferBase</name></proto>
            <param><ptype>GLuint</ptype> <name>xfb</name></param>
            <param><ptype>GLuint</ptype> <name>index</name></param>
            <param><ptype>GLuint</ptype> <name>buffer</name></param>
        </command>
         */
    private static Function _glTransformFeedbackBufferBase ;
    public static void glTransformFeedbackBufferBase (int xfb_ , int index_ , int buffer_ ) {
        if( _glTransformFeedbackBufferBase == null )
            _glTransformFeedbackBufferBase = loadFunction("glTransformFeedbackBufferBase");
        _glTransformFeedbackBufferBase.invoke(Void.class, new Object[]{xfb_,index_,buffer_ });
    }
    /* <command>
            <proto>void <name>glTransformFeedbackBufferRange</name></proto>
            <param><ptype>GLuint</ptype> <name>xfb</name></param>
            <param><ptype>GLuint</ptype> <name>index</name></param>
            <param><ptype>GLuint</ptype> <name>buffer</name></param>
            <param><ptype>GLintptr</ptype> <name>offset</name></param>
            <param group="BufferSize"><ptype>GLsizeiptr</ptype> <name>size</name></param>
        </command>
         */
    private static Function _glTransformFeedbackBufferRange ;
    public static void glTransformFeedbackBufferRange (int xfb_ , int index_ , int buffer_ , long offset_ , long size_ ) {
        if( _glTransformFeedbackBufferRange == null )
            _glTransformFeedbackBufferRange = loadFunction("glTransformFeedbackBufferRange");
        _glTransformFeedbackBufferRange.invoke(Void.class, new Object[]{xfb_,index_,buffer_,offset_,size_ });
    }
    /* <command>
            <proto>void <name>glTransformFeedbackVaryings</name></proto>
            <param><ptype>GLuint</ptype> <name>program</name></param>
            <param><ptype>GLsizei</ptype> <name>count</name></param>
            <param len="count">const <ptype>GLchar</ptype> *const*<name>varyings</name></param>
            <param><ptype>GLenum</ptype> <name>bufferMode</name></param>
        </command>
         */
    private static Function _glTransformFeedbackVaryings ;
    public static void glTransformFeedbackVaryings (int program_ , int count_ , byte[] varyings_ , int bufferMode_ ) {
        if( _glTransformFeedbackVaryings == null )
            _glTransformFeedbackVaryings = loadFunction("glTransformFeedbackVaryings");
        _glTransformFeedbackVaryings.invoke(Void.class, new Object[]{program_,count_,varyings_,bufferMode_ });
    }
    /* <command>
            <proto>void <name>glUniform1d</name></proto>
            <param><ptype>GLint</ptype> <name>location</name></param>
            <param><ptype>GLdouble</ptype> <name>x</name></param>
        </command>
         */
    private static Function _glUniform1d ;
    public static void glUniform1d (int location_ , double x_ ) {
        if( _glUniform1d == null )
            _glUniform1d = loadFunction("glUniform1d");
        _glUniform1d.invoke(Void.class, new Object[]{location_,x_ });
    }
    /* <command>
            <proto>void <name>glUniform1dv</name></proto>
            <param><ptype>GLint</ptype> <name>location</name></param>
            <param><ptype>GLsizei</ptype> <name>count</name></param>
            <param len="count*1">const <ptype>GLdouble</ptype> *<name>value</name></param>
        </command>
         */
    private static Function _glUniform1dv ;
    public static void glUniform1dv (int location_ , int count_ , double[] value_ ) {
        if( _glUniform1dv == null )
            _glUniform1dv = loadFunction("glUniform1dv");
        _glUniform1dv.invoke(Void.class, new Object[]{location_,count_,value_ });
    }
    /* <command>
            <proto>void <name>glUniform1f</name></proto>
            <param><ptype>GLint</ptype> <name>location</name></param>
            <param><ptype>GLfloat</ptype> <name>v0</name></param>
        </command>
         */
    private static Function _glUniform1f ;
    public static void glUniform1f (int location_ , float v0_ ) {
        if( _glUniform1f == null )
            _glUniform1f = loadFunction("glUniform1f");
        _glUniform1f.invoke(Void.class, new Object[]{location_,v0_ });
    }
    /* <command>
            <proto>void <name>glUniform1fv</name></proto>
            <param><ptype>GLint</ptype> <name>location</name></param>
            <param><ptype>GLsizei</ptype> <name>count</name></param>
            <param len="count*1">const <ptype>GLfloat</ptype> *<name>value</name></param>
        </command>
         */
    private static Function _glUniform1fv ;
    public static void glUniform1fv (int location_ , int count_ , float[] value_ ) {
        if( _glUniform1fv == null )
            _glUniform1fv = loadFunction("glUniform1fv");
        _glUniform1fv.invoke(Void.class, new Object[]{location_,count_,value_ });
    }
    /* <command>
            <proto>void <name>glUniform1i</name></proto>
            <param><ptype>GLint</ptype> <name>location</name></param>
            <param><ptype>GLint</ptype> <name>v0</name></param>
        </command>
         */
    private static Function _glUniform1i ;
    public static void glUniform1i (int location_ , int v0_ ) {
        if( _glUniform1i == null )
            _glUniform1i = loadFunction("glUniform1i");
        _glUniform1i.invoke(Void.class, new Object[]{location_,v0_ });
    }
    /* <command>
            <proto>void <name>glUniform1iv</name></proto>
            <param><ptype>GLint</ptype> <name>location</name></param>
            <param><ptype>GLsizei</ptype> <name>count</name></param>
            <param len="count*1">const <ptype>GLint</ptype> *<name>value</name></param>
        </command>
         */
    private static Function _glUniform1iv ;
    public static void glUniform1iv (int location_ , int count_ , int[] value_ ) {
        if( _glUniform1iv == null )
            _glUniform1iv = loadFunction("glUniform1iv");
        _glUniform1iv.invoke(Void.class, new Object[]{location_,count_,value_ });
    }
    /* <command>
            <proto>void <name>glUniform1ui</name></proto>
            <param><ptype>GLint</ptype> <name>location</name></param>
            <param><ptype>GLuint</ptype> <name>v0</name></param>
        </command>
         */
    private static Function _glUniform1ui ;
    public static void glUniform1ui (int location_ , int v0_ ) {
        if( _glUniform1ui == null )
            _glUniform1ui = loadFunction("glUniform1ui");
        _glUniform1ui.invoke(Void.class, new Object[]{location_,v0_ });
    }
    /* <command>
            <proto>void <name>glUniform1uiv</name></proto>
            <param><ptype>GLint</ptype> <name>location</name></param>
            <param><ptype>GLsizei</ptype> <name>count</name></param>
            <param len="count*1">const <ptype>GLuint</ptype> *<name>value</name></param>
        </command>
         */
    private static Function _glUniform1uiv ;
    public static void glUniform1uiv (int location_ , int count_ , int[] value_ ) {
        if( _glUniform1uiv == null )
            _glUniform1uiv = loadFunction("glUniform1uiv");
        _glUniform1uiv.invoke(Void.class, new Object[]{location_,count_,value_ });
    }
    /* <command>
            <proto>void <name>glUniform2d</name></proto>
            <param><ptype>GLint</ptype> <name>location</name></param>
            <param><ptype>GLdouble</ptype> <name>x</name></param>
            <param><ptype>GLdouble</ptype> <name>y</name></param>
        </command>
         */
    private static Function _glUniform2d ;
    public static void glUniform2d (int location_ , double x_ , double y_ ) {
        if( _glUniform2d == null )
            _glUniform2d = loadFunction("glUniform2d");
        _glUniform2d.invoke(Void.class, new Object[]{location_,x_,y_ });
    }
    /* <command>
            <proto>void <name>glUniform2dv</name></proto>
            <param><ptype>GLint</ptype> <name>location</name></param>
            <param><ptype>GLsizei</ptype> <name>count</name></param>
            <param len="count*2">const <ptype>GLdouble</ptype> *<name>value</name></param>
        </command>
         */
    private static Function _glUniform2dv ;
    public static void glUniform2dv (int location_ , int count_ , double[] value_ ) {
        if( _glUniform2dv == null )
            _glUniform2dv = loadFunction("glUniform2dv");
        _glUniform2dv.invoke(Void.class, new Object[]{location_,count_,value_ });
    }
    /* <command>
            <proto>void <name>glUniform2f</name></proto>
            <param><ptype>GLint</ptype> <name>location</name></param>
            <param><ptype>GLfloat</ptype> <name>v0</name></param>
            <param><ptype>GLfloat</ptype> <name>v1</name></param>
        </command>
         */
    private static Function _glUniform2f ;
    public static void glUniform2f (int location_ , float v0_ , float v1_ ) {
        if( _glUniform2f == null )
            _glUniform2f = loadFunction("glUniform2f");
        _glUniform2f.invoke(Void.class, new Object[]{location_,v0_,v1_ });
    }
    /* <command>
            <proto>void <name>glUniform2fv</name></proto>
            <param><ptype>GLint</ptype> <name>location</name></param>
            <param><ptype>GLsizei</ptype> <name>count</name></param>
            <param len="count*2">const <ptype>GLfloat</ptype> *<name>value</name></param>
        </command>
         */
    private static Function _glUniform2fv ;
    public static void glUniform2fv (int location_ , int count_ , float[] value_ ) {
        if( _glUniform2fv == null )
            _glUniform2fv = loadFunction("glUniform2fv");
        _glUniform2fv.invoke(Void.class, new Object[]{location_,count_,value_ });
    }
    /* <command>
            <proto>void <name>glUniform2i</name></proto>
            <param><ptype>GLint</ptype> <name>location</name></param>
            <param><ptype>GLint</ptype> <name>v0</name></param>
            <param><ptype>GLint</ptype> <name>v1</name></param>
        </command>
         */
    private static Function _glUniform2i ;
    public static void glUniform2i (int location_ , int v0_ , int v1_ ) {
        if( _glUniform2i == null )
            _glUniform2i = loadFunction("glUniform2i");
        _glUniform2i.invoke(Void.class, new Object[]{location_,v0_,v1_ });
    }
    /* <command>
            <proto>void <name>glUniform2iv</name></proto>
            <param><ptype>GLint</ptype> <name>location</name></param>
            <param><ptype>GLsizei</ptype> <name>count</name></param>
            <param len="count*2">const <ptype>GLint</ptype> *<name>value</name></param>
        </command>
         */
    private static Function _glUniform2iv ;
    public static void glUniform2iv (int location_ , int count_ , int[] value_ ) {
        if( _glUniform2iv == null )
            _glUniform2iv = loadFunction("glUniform2iv");
        _glUniform2iv.invoke(Void.class, new Object[]{location_,count_,value_ });
    }
    /* <command>
            <proto>void <name>glUniform2ui</name></proto>
            <param><ptype>GLint</ptype> <name>location</name></param>
            <param><ptype>GLuint</ptype> <name>v0</name></param>
            <param><ptype>GLuint</ptype> <name>v1</name></param>
        </command>
         */
    private static Function _glUniform2ui ;
    public static void glUniform2ui (int location_ , int v0_ , int v1_ ) {
        if( _glUniform2ui == null )
            _glUniform2ui = loadFunction("glUniform2ui");
        _glUniform2ui.invoke(Void.class, new Object[]{location_,v0_,v1_ });
    }
    /* <command>
            <proto>void <name>glUniform2uiv</name></proto>
            <param><ptype>GLint</ptype> <name>location</name></param>
            <param><ptype>GLsizei</ptype> <name>count</name></param>
            <param len="count*2">const <ptype>GLuint</ptype> *<name>value</name></param>
        </command>
         */
    private static Function _glUniform2uiv ;
    public static void glUniform2uiv (int location_ , int count_ , int[] value_ ) {
        if( _glUniform2uiv == null )
            _glUniform2uiv = loadFunction("glUniform2uiv");
        _glUniform2uiv.invoke(Void.class, new Object[]{location_,count_,value_ });
    }
    /* <command>
            <proto>void <name>glUniform3d</name></proto>
            <param><ptype>GLint</ptype> <name>location</name></param>
            <param><ptype>GLdouble</ptype> <name>x</name></param>
            <param><ptype>GLdouble</ptype> <name>y</name></param>
            <param><ptype>GLdouble</ptype> <name>z</name></param>
        </command>
         */
    private static Function _glUniform3d ;
    public static void glUniform3d (int location_ , double x_ , double y_ , double z_ ) {
        if( _glUniform3d == null )
            _glUniform3d = loadFunction("glUniform3d");
        _glUniform3d.invoke(Void.class, new Object[]{location_,x_,y_,z_ });
    }
    /* <command>
            <proto>void <name>glUniform3dv</name></proto>
            <param><ptype>GLint</ptype> <name>location</name></param>
            <param><ptype>GLsizei</ptype> <name>count</name></param>
            <param len="count*3">const <ptype>GLdouble</ptype> *<name>value</name></param>
        </command>
         */
    private static Function _glUniform3dv ;
    public static void glUniform3dv (int location_ , int count_ , double[] value_ ) {
        if( _glUniform3dv == null )
            _glUniform3dv = loadFunction("glUniform3dv");
        _glUniform3dv.invoke(Void.class, new Object[]{location_,count_,value_ });
    }
    /* <command>
            <proto>void <name>glUniform3f</name></proto>
            <param><ptype>GLint</ptype> <name>location</name></param>
            <param><ptype>GLfloat</ptype> <name>v0</name></param>
            <param><ptype>GLfloat</ptype> <name>v1</name></param>
            <param><ptype>GLfloat</ptype> <name>v2</name></param>
        </command>
         */
    private static Function _glUniform3f ;
    public static void glUniform3f (int location_ , float v0_ , float v1_ , float v2_ ) {
        if( _glUniform3f == null )
            _glUniform3f = loadFunction("glUniform3f");
        _glUniform3f.invoke(Void.class, new Object[]{location_,v0_,v1_,v2_ });
    }
    /* <command>
            <proto>void <name>glUniform3fv</name></proto>
            <param><ptype>GLint</ptype> <name>location</name></param>
            <param><ptype>GLsizei</ptype> <name>count</name></param>
            <param len="count*3">const <ptype>GLfloat</ptype> *<name>value</name></param>
        </command>
         */
    private static Function _glUniform3fv ;
    public static void glUniform3fv (int location_ , int count_ , float[] value_ ) {
        if( _glUniform3fv == null )
            _glUniform3fv = loadFunction("glUniform3fv");
        _glUniform3fv.invoke(Void.class, new Object[]{location_,count_,value_ });
    }
    /* <command>
            <proto>void <name>glUniform3i</name></proto>
            <param><ptype>GLint</ptype> <name>location</name></param>
            <param><ptype>GLint</ptype> <name>v0</name></param>
            <param><ptype>GLint</ptype> <name>v1</name></param>
            <param><ptype>GLint</ptype> <name>v2</name></param>
        </command>
         */
    private static Function _glUniform3i ;
    public static void glUniform3i (int location_ , int v0_ , int v1_ , int v2_ ) {
        if( _glUniform3i == null )
            _glUniform3i = loadFunction("glUniform3i");
        _glUniform3i.invoke(Void.class, new Object[]{location_,v0_,v1_,v2_ });
    }
    /* <command>
            <proto>void <name>glUniform3iv</name></proto>
            <param><ptype>GLint</ptype> <name>location</name></param>
            <param><ptype>GLsizei</ptype> <name>count</name></param>
            <param len="count*3">const <ptype>GLint</ptype> *<name>value</name></param>
        </command>
         */
    private static Function _glUniform3iv ;
    public static void glUniform3iv (int location_ , int count_ , int[] value_ ) {
        if( _glUniform3iv == null )
            _glUniform3iv = loadFunction("glUniform3iv");
        _glUniform3iv.invoke(Void.class, new Object[]{location_,count_,value_ });
    }
    /* <command>
            <proto>void <name>glUniform3ui</name></proto>
            <param><ptype>GLint</ptype> <name>location</name></param>
            <param><ptype>GLuint</ptype> <name>v0</name></param>
            <param><ptype>GLuint</ptype> <name>v1</name></param>
            <param><ptype>GLuint</ptype> <name>v2</name></param>
        </command>
         */
    private static Function _glUniform3ui ;
    public static void glUniform3ui (int location_ , int v0_ , int v1_ , int v2_ ) {
        if( _glUniform3ui == null )
            _glUniform3ui = loadFunction("glUniform3ui");
        _glUniform3ui.invoke(Void.class, new Object[]{location_,v0_,v1_,v2_ });
    }
    /* <command>
            <proto>void <name>glUniform3uiv</name></proto>
            <param><ptype>GLint</ptype> <name>location</name></param>
            <param><ptype>GLsizei</ptype> <name>count</name></param>
            <param len="count*3">const <ptype>GLuint</ptype> *<name>value</name></param>
        </command>
         */
    private static Function _glUniform3uiv ;
    public static void glUniform3uiv (int location_ , int count_ , int[] value_ ) {
        if( _glUniform3uiv == null )
            _glUniform3uiv = loadFunction("glUniform3uiv");
        _glUniform3uiv.invoke(Void.class, new Object[]{location_,count_,value_ });
    }
    /* <command>
            <proto>void <name>glUniform4d</name></proto>
            <param><ptype>GLint</ptype> <name>location</name></param>
            <param><ptype>GLdouble</ptype> <name>x</name></param>
            <param><ptype>GLdouble</ptype> <name>y</name></param>
            <param><ptype>GLdouble</ptype> <name>z</name></param>
            <param><ptype>GLdouble</ptype> <name>w</name></param>
        </command>
         */
    private static Function _glUniform4d ;
    public static void glUniform4d (int location_ , double x_ , double y_ , double z_ , double w_ ) {
        if( _glUniform4d == null )
            _glUniform4d = loadFunction("glUniform4d");
        _glUniform4d.invoke(Void.class, new Object[]{location_,x_,y_,z_,w_ });
    }
    /* <command>
            <proto>void <name>glUniform4dv</name></proto>
            <param><ptype>GLint</ptype> <name>location</name></param>
            <param><ptype>GLsizei</ptype> <name>count</name></param>
            <param len="count*4">const <ptype>GLdouble</ptype> *<name>value</name></param>
        </command>
         */
    private static Function _glUniform4dv ;
    public static void glUniform4dv (int location_ , int count_ , double[] value_ ) {
        if( _glUniform4dv == null )
            _glUniform4dv = loadFunction("glUniform4dv");
        _glUniform4dv.invoke(Void.class, new Object[]{location_,count_,value_ });
    }
    /* <command>
            <proto>void <name>glUniform4f</name></proto>
            <param><ptype>GLint</ptype> <name>location</name></param>
            <param><ptype>GLfloat</ptype> <name>v0</name></param>
            <param><ptype>GLfloat</ptype> <name>v1</name></param>
            <param><ptype>GLfloat</ptype> <name>v2</name></param>
            <param><ptype>GLfloat</ptype> <name>v3</name></param>
        </command>
         */
    private static Function _glUniform4f ;
    public static void glUniform4f (int location_ , float v0_ , float v1_ , float v2_ , float v3_ ) {
        if( _glUniform4f == null )
            _glUniform4f = loadFunction("glUniform4f");
        _glUniform4f.invoke(Void.class, new Object[]{location_,v0_,v1_,v2_,v3_ });
    }
    /* <command>
            <proto>void <name>glUniform4fv</name></proto>
            <param><ptype>GLint</ptype> <name>location</name></param>
            <param><ptype>GLsizei</ptype> <name>count</name></param>
            <param len="count*4">const <ptype>GLfloat</ptype> *<name>value</name></param>
        </command>
         */
    private static Function _glUniform4fv ;
    public static void glUniform4fv (int location_ , int count_ , float[] value_ ) {
        if( _glUniform4fv == null )
            _glUniform4fv = loadFunction("glUniform4fv");
        _glUniform4fv.invoke(Void.class, new Object[]{location_,count_,value_ });
    }
    /* <command>
            <proto>void <name>glUniform4i</name></proto>
            <param><ptype>GLint</ptype> <name>location</name></param>
            <param><ptype>GLint</ptype> <name>v0</name></param>
            <param><ptype>GLint</ptype> <name>v1</name></param>
            <param><ptype>GLint</ptype> <name>v2</name></param>
            <param><ptype>GLint</ptype> <name>v3</name></param>
        </command>
         */
    private static Function _glUniform4i ;
    public static void glUniform4i (int location_ , int v0_ , int v1_ , int v2_ , int v3_ ) {
        if( _glUniform4i == null )
            _glUniform4i = loadFunction("glUniform4i");
        _glUniform4i.invoke(Void.class, new Object[]{location_,v0_,v1_,v2_,v3_ });
    }
    /* <command>
            <proto>void <name>glUniform4iv</name></proto>
            <param><ptype>GLint</ptype> <name>location</name></param>
            <param><ptype>GLsizei</ptype> <name>count</name></param>
            <param len="count*4">const <ptype>GLint</ptype> *<name>value</name></param>
        </command>
         */
    private static Function _glUniform4iv ;
    public static void glUniform4iv (int location_ , int count_ , int[] value_ ) {
        if( _glUniform4iv == null )
            _glUniform4iv = loadFunction("glUniform4iv");
        _glUniform4iv.invoke(Void.class, new Object[]{location_,count_,value_ });
    }
    /* <command>
            <proto>void <name>glUniform4ui</name></proto>
            <param><ptype>GLint</ptype> <name>location</name></param>
            <param><ptype>GLuint</ptype> <name>v0</name></param>
            <param><ptype>GLuint</ptype> <name>v1</name></param>
            <param><ptype>GLuint</ptype> <name>v2</name></param>
            <param><ptype>GLuint</ptype> <name>v3</name></param>
        </command>
         */
    private static Function _glUniform4ui ;
    public static void glUniform4ui (int location_ , int v0_ , int v1_ , int v2_ , int v3_ ) {
        if( _glUniform4ui == null )
            _glUniform4ui = loadFunction("glUniform4ui");
        _glUniform4ui.invoke(Void.class, new Object[]{location_,v0_,v1_,v2_,v3_ });
    }
    /* <command>
            <proto>void <name>glUniform4uiv</name></proto>
            <param><ptype>GLint</ptype> <name>location</name></param>
            <param><ptype>GLsizei</ptype> <name>count</name></param>
            <param len="count*4">const <ptype>GLuint</ptype> *<name>value</name></param>
        </command>
         */
    private static Function _glUniform4uiv ;
    public static void glUniform4uiv (int location_ , int count_ , int[] value_ ) {
        if( _glUniform4uiv == null )
            _glUniform4uiv = loadFunction("glUniform4uiv");
        _glUniform4uiv.invoke(Void.class, new Object[]{location_,count_,value_ });
    }
    /* <command>
            <proto>void <name>glUniformBlockBinding</name></proto>
            <param><ptype>GLuint</ptype> <name>program</name></param>
            <param><ptype>GLuint</ptype> <name>uniformBlockIndex</name></param>
            <param><ptype>GLuint</ptype> <name>uniformBlockBinding</name></param>
        </command>
         */
    private static Function _glUniformBlockBinding ;
    public static void glUniformBlockBinding (int program_ , int uniformBlockIndex_ , int uniformBlockBinding_ ) {
        if( _glUniformBlockBinding == null )
            _glUniformBlockBinding = loadFunction("glUniformBlockBinding");
        _glUniformBlockBinding.invoke(Void.class, new Object[]{program_,uniformBlockIndex_,uniformBlockBinding_ });
    }
    /* <command>
            <proto>void <name>glUniformMatrix2dv</name></proto>
            <param><ptype>GLint</ptype> <name>location</name></param>
            <param><ptype>GLsizei</ptype> <name>count</name></param>
            <param group="Boolean"><ptype>GLboolean</ptype> <name>transpose</name></param>
            <param len="count*4">const <ptype>GLdouble</ptype> *<name>value</name></param>
        </command>
         */
    private static Function _glUniformMatrix2dv ;
    public static void glUniformMatrix2dv (int location_ , int count_ , boolean transpose_ , double[] value_ ) {
        if( _glUniformMatrix2dv == null )
            _glUniformMatrix2dv = loadFunction("glUniformMatrix2dv");
        _glUniformMatrix2dv.invoke(Void.class, new Object[]{location_,count_,(transpose_ ? (byte)1:(byte)0),value_ });
    }
    /* <command>
            <proto>void <name>glUniformMatrix2fv</name></proto>
            <param><ptype>GLint</ptype> <name>location</name></param>
            <param><ptype>GLsizei</ptype> <name>count</name></param>
            <param group="Boolean"><ptype>GLboolean</ptype> <name>transpose</name></param>
            <param len="count*4">const <ptype>GLfloat</ptype> *<name>value</name></param>
        </command>
         */
    private static Function _glUniformMatrix2fv ;
    public static void glUniformMatrix2fv (int location_ , int count_ , boolean transpose_ , float[] value_ ) {
        if( _glUniformMatrix2fv == null )
            _glUniformMatrix2fv = loadFunction("glUniformMatrix2fv");
        _glUniformMatrix2fv.invoke(Void.class, new Object[]{location_,count_,(transpose_ ? (byte)1:(byte)0),value_ });
    }
    /* <command>
            <proto>void <name>glUniformMatrix2x3dv</name></proto>
            <param><ptype>GLint</ptype> <name>location</name></param>
            <param><ptype>GLsizei</ptype> <name>count</name></param>
            <param group="Boolean"><ptype>GLboolean</ptype> <name>transpose</name></param>
            <param len="count*6">const <ptype>GLdouble</ptype> *<name>value</name></param>
        </command>
         */
    private static Function _glUniformMatrix2x3dv ;
    public static void glUniformMatrix2x3dv (int location_ , int count_ , boolean transpose_ , double[] value_ ) {
        if( _glUniformMatrix2x3dv == null )
            _glUniformMatrix2x3dv = loadFunction("glUniformMatrix2x3dv");
        _glUniformMatrix2x3dv.invoke(Void.class, new Object[]{location_,count_,(transpose_ ? (byte)1:(byte)0),value_ });
    }
    /* <command>
            <proto>void <name>glUniformMatrix2x3fv</name></proto>
            <param><ptype>GLint</ptype> <name>location</name></param>
            <param><ptype>GLsizei</ptype> <name>count</name></param>
            <param group="Boolean"><ptype>GLboolean</ptype> <name>transpose</name></param>
            <param len="count*6">const <ptype>GLfloat</ptype> *<name>value</name></param>
            <glx opcode="305" type="render" />
        </command>
         */
    private static Function _glUniformMatrix2x3fv ;
    public static void glUniformMatrix2x3fv (int location_ , int count_ , boolean transpose_ , float[] value_ ) {
        if( _glUniformMatrix2x3fv == null )
            _glUniformMatrix2x3fv = loadFunction("glUniformMatrix2x3fv");
        _glUniformMatrix2x3fv.invoke(Void.class, new Object[]{location_,count_,(transpose_ ? (byte)1:(byte)0),value_ });
    }
    /* <command>
            <proto>void <name>glUniformMatrix2x4dv</name></proto>
            <param><ptype>GLint</ptype> <name>location</name></param>
            <param><ptype>GLsizei</ptype> <name>count</name></param>
            <param group="Boolean"><ptype>GLboolean</ptype> <name>transpose</name></param>
            <param len="count*8">const <ptype>GLdouble</ptype> *<name>value</name></param>
        </command>
         */
    private static Function _glUniformMatrix2x4dv ;
    public static void glUniformMatrix2x4dv (int location_ , int count_ , boolean transpose_ , double[] value_ ) {
        if( _glUniformMatrix2x4dv == null )
            _glUniformMatrix2x4dv = loadFunction("glUniformMatrix2x4dv");
        _glUniformMatrix2x4dv.invoke(Void.class, new Object[]{location_,count_,(transpose_ ? (byte)1:(byte)0),value_ });
    }
    /* <command>
            <proto>void <name>glUniformMatrix2x4fv</name></proto>
            <param><ptype>GLint</ptype> <name>location</name></param>
            <param><ptype>GLsizei</ptype> <name>count</name></param>
            <param group="Boolean"><ptype>GLboolean</ptype> <name>transpose</name></param>
            <param len="count*8">const <ptype>GLfloat</ptype> *<name>value</name></param>
            <glx opcode="307" type="render" />
        </command>
         */
    private static Function _glUniformMatrix2x4fv ;
    public static void glUniformMatrix2x4fv (int location_ , int count_ , boolean transpose_ , float[] value_ ) {
        if( _glUniformMatrix2x4fv == null )
            _glUniformMatrix2x4fv = loadFunction("glUniformMatrix2x4fv");
        _glUniformMatrix2x4fv.invoke(Void.class, new Object[]{location_,count_,(transpose_ ? (byte)1:(byte)0),value_ });
    }
    /* <command>
            <proto>void <name>glUniformMatrix3dv</name></proto>
            <param><ptype>GLint</ptype> <name>location</name></param>
            <param><ptype>GLsizei</ptype> <name>count</name></param>
            <param group="Boolean"><ptype>GLboolean</ptype> <name>transpose</name></param>
            <param len="count*9">const <ptype>GLdouble</ptype> *<name>value</name></param>
        </command>
         */
    private static Function _glUniformMatrix3dv ;
    public static void glUniformMatrix3dv (int location_ , int count_ , boolean transpose_ , double[] value_ ) {
        if( _glUniformMatrix3dv == null )
            _glUniformMatrix3dv = loadFunction("glUniformMatrix3dv");
        _glUniformMatrix3dv.invoke(Void.class, new Object[]{location_,count_,(transpose_ ? (byte)1:(byte)0),value_ });
    }
    /* <command>
            <proto>void <name>glUniformMatrix3fv</name></proto>
            <param><ptype>GLint</ptype> <name>location</name></param>
            <param><ptype>GLsizei</ptype> <name>count</name></param>
            <param group="Boolean"><ptype>GLboolean</ptype> <name>transpose</name></param>
            <param len="count*9">const <ptype>GLfloat</ptype> *<name>value</name></param>
        </command>
         */
    private static Function _glUniformMatrix3fv ;
    public static void glUniformMatrix3fv (int location_ , int count_ , boolean transpose_ , float[] value_ ) {
        if( _glUniformMatrix3fv == null )
            _glUniformMatrix3fv = loadFunction("glUniformMatrix3fv");
        _glUniformMatrix3fv.invoke(Void.class, new Object[]{location_,count_,(transpose_ ? (byte)1:(byte)0),value_ });
    }
    /* <command>
            <proto>void <name>glUniformMatrix3x2dv</name></proto>
            <param><ptype>GLint</ptype> <name>location</name></param>
            <param><ptype>GLsizei</ptype> <name>count</name></param>
            <param group="Boolean"><ptype>GLboolean</ptype> <name>transpose</name></param>
            <param len="count*6">const <ptype>GLdouble</ptype> *<name>value</name></param>
        </command>
         */
    private static Function _glUniformMatrix3x2dv ;
    public static void glUniformMatrix3x2dv (int location_ , int count_ , boolean transpose_ , double[] value_ ) {
        if( _glUniformMatrix3x2dv == null )
            _glUniformMatrix3x2dv = loadFunction("glUniformMatrix3x2dv");
        _glUniformMatrix3x2dv.invoke(Void.class, new Object[]{location_,count_,(transpose_ ? (byte)1:(byte)0),value_ });
    }
    /* <command>
            <proto>void <name>glUniformMatrix3x2fv</name></proto>
            <param><ptype>GLint</ptype> <name>location</name></param>
            <param><ptype>GLsizei</ptype> <name>count</name></param>
            <param group="Boolean"><ptype>GLboolean</ptype> <name>transpose</name></param>
            <param len="count*6">const <ptype>GLfloat</ptype> *<name>value</name></param>
            <glx opcode="306" type="render" />
        </command>
         */
    private static Function _glUniformMatrix3x2fv ;
    public static void glUniformMatrix3x2fv (int location_ , int count_ , boolean transpose_ , float[] value_ ) {
        if( _glUniformMatrix3x2fv == null )
            _glUniformMatrix3x2fv = loadFunction("glUniformMatrix3x2fv");
        _glUniformMatrix3x2fv.invoke(Void.class, new Object[]{location_,count_,(transpose_ ? (byte)1:(byte)0),value_ });
    }
    /* <command>
            <proto>void <name>glUniformMatrix3x4dv</name></proto>
            <param><ptype>GLint</ptype> <name>location</name></param>
            <param><ptype>GLsizei</ptype> <name>count</name></param>
            <param group="Boolean"><ptype>GLboolean</ptype> <name>transpose</name></param>
            <param len="count*12">const <ptype>GLdouble</ptype> *<name>value</name></param>
        </command>
         */
    private static Function _glUniformMatrix3x4dv ;
    public static void glUniformMatrix3x4dv (int location_ , int count_ , boolean transpose_ , double[] value_ ) {
        if( _glUniformMatrix3x4dv == null )
            _glUniformMatrix3x4dv = loadFunction("glUniformMatrix3x4dv");
        _glUniformMatrix3x4dv.invoke(Void.class, new Object[]{location_,count_,(transpose_ ? (byte)1:(byte)0),value_ });
    }
    /* <command>
            <proto>void <name>glUniformMatrix3x4fv</name></proto>
            <param><ptype>GLint</ptype> <name>location</name></param>
            <param><ptype>GLsizei</ptype> <name>count</name></param>
            <param group="Boolean"><ptype>GLboolean</ptype> <name>transpose</name></param>
            <param len="count*12">const <ptype>GLfloat</ptype> *<name>value</name></param>
            <glx opcode="309" type="render" />
        </command>
         */
    private static Function _glUniformMatrix3x4fv ;
    public static void glUniformMatrix3x4fv (int location_ , int count_ , boolean transpose_ , float[] value_ ) {
        if( _glUniformMatrix3x4fv == null )
            _glUniformMatrix3x4fv = loadFunction("glUniformMatrix3x4fv");
        _glUniformMatrix3x4fv.invoke(Void.class, new Object[]{location_,count_,(transpose_ ? (byte)1:(byte)0),value_ });
    }
    /* <command>
            <proto>void <name>glUniformMatrix4dv</name></proto>
            <param><ptype>GLint</ptype> <name>location</name></param>
            <param><ptype>GLsizei</ptype> <name>count</name></param>
            <param group="Boolean"><ptype>GLboolean</ptype> <name>transpose</name></param>
            <param len="count*16">const <ptype>GLdouble</ptype> *<name>value</name></param>
        </command>
         */
    private static Function _glUniformMatrix4dv ;
    public static void glUniformMatrix4dv (int location_ , int count_ , boolean transpose_ , double[] value_ ) {
        if( _glUniformMatrix4dv == null )
            _glUniformMatrix4dv = loadFunction("glUniformMatrix4dv");
        _glUniformMatrix4dv.invoke(Void.class, new Object[]{location_,count_,(transpose_ ? (byte)1:(byte)0),value_ });
    }
    /* <command>
            <proto>void <name>glUniformMatrix4fv</name></proto>
            <param><ptype>GLint</ptype> <name>location</name></param>
            <param><ptype>GLsizei</ptype> <name>count</name></param>
            <param group="Boolean"><ptype>GLboolean</ptype> <name>transpose</name></param>
            <param len="count*16">const <ptype>GLfloat</ptype> *<name>value</name></param>
        </command>
         */
    private static Function _glUniformMatrix4fv ;
    public static void glUniformMatrix4fv (int location_ , int count_ , boolean transpose_ , float[] value_ ) {
        if( _glUniformMatrix4fv == null )
            _glUniformMatrix4fv = loadFunction("glUniformMatrix4fv");
        _glUniformMatrix4fv.invoke(Void.class, new Object[]{location_,count_,(transpose_ ? (byte)1:(byte)0),value_ });
    }
    /* <command>
            <proto>void <name>glUniformMatrix4x2dv</name></proto>
            <param><ptype>GLint</ptype> <name>location</name></param>
            <param><ptype>GLsizei</ptype> <name>count</name></param>
            <param group="Boolean"><ptype>GLboolean</ptype> <name>transpose</name></param>
            <param len="count*8">const <ptype>GLdouble</ptype> *<name>value</name></param>
        </command>
         */
    private static Function _glUniformMatrix4x2dv ;
    public static void glUniformMatrix4x2dv (int location_ , int count_ , boolean transpose_ , double[] value_ ) {
        if( _glUniformMatrix4x2dv == null )
            _glUniformMatrix4x2dv = loadFunction("glUniformMatrix4x2dv");
        _glUniformMatrix4x2dv.invoke(Void.class, new Object[]{location_,count_,(transpose_ ? (byte)1:(byte)0),value_ });
    }
    /* <command>
            <proto>void <name>glUniformMatrix4x2fv</name></proto>
            <param><ptype>GLint</ptype> <name>location</name></param>
            <param><ptype>GLsizei</ptype> <name>count</name></param>
            <param group="Boolean"><ptype>GLboolean</ptype> <name>transpose</name></param>
            <param len="count*8">const <ptype>GLfloat</ptype> *<name>value</name></param>
            <glx opcode="308" type="render" />
        </command>
         */
    private static Function _glUniformMatrix4x2fv ;
    public static void glUniformMatrix4x2fv (int location_ , int count_ , boolean transpose_ , float[] value_ ) {
        if( _glUniformMatrix4x2fv == null )
            _glUniformMatrix4x2fv = loadFunction("glUniformMatrix4x2fv");
        _glUniformMatrix4x2fv.invoke(Void.class, new Object[]{location_,count_,(transpose_ ? (byte)1:(byte)0),value_ });
    }
    /* <command>
            <proto>void <name>glUniformMatrix4x3dv</name></proto>
            <param><ptype>GLint</ptype> <name>location</name></param>
            <param><ptype>GLsizei</ptype> <name>count</name></param>
            <param group="Boolean"><ptype>GLboolean</ptype> <name>transpose</name></param>
            <param len="count*12">const <ptype>GLdouble</ptype> *<name>value</name></param>
        </command>
         */
    private static Function _glUniformMatrix4x3dv ;
    public static void glUniformMatrix4x3dv (int location_ , int count_ , boolean transpose_ , double[] value_ ) {
        if( _glUniformMatrix4x3dv == null )
            _glUniformMatrix4x3dv = loadFunction("glUniformMatrix4x3dv");
        _glUniformMatrix4x3dv.invoke(Void.class, new Object[]{location_,count_,(transpose_ ? (byte)1:(byte)0),value_ });
    }
    /* <command>
            <proto>void <name>glUniformMatrix4x3fv</name></proto>
            <param><ptype>GLint</ptype> <name>location</name></param>
            <param><ptype>GLsizei</ptype> <name>count</name></param>
            <param group="Boolean"><ptype>GLboolean</ptype> <name>transpose</name></param>
            <param len="count*12">const <ptype>GLfloat</ptype> *<name>value</name></param>
            <glx opcode="310" type="render" />
        </command>
         */
    private static Function _glUniformMatrix4x3fv ;
    public static void glUniformMatrix4x3fv (int location_ , int count_ , boolean transpose_ , float[] value_ ) {
        if( _glUniformMatrix4x3fv == null )
            _glUniformMatrix4x3fv = loadFunction("glUniformMatrix4x3fv");
        _glUniformMatrix4x3fv.invoke(Void.class, new Object[]{location_,count_,(transpose_ ? (byte)1:(byte)0),value_ });
    }
    /* <command>
            <proto>void <name>glUniformSubroutinesuiv</name></proto>
            <param><ptype>GLenum</ptype> <name>shadertype</name></param>
            <param><ptype>GLsizei</ptype> <name>count</name></param>
            <param len="count">const <ptype>GLuint</ptype> *<name>indices</name></param>
        </command>
         */
    private static Function _glUniformSubroutinesuiv ;
    public static void glUniformSubroutinesuiv (int shadertype_ , int count_ , int[] indices_ ) {
        if( _glUniformSubroutinesuiv == null )
            _glUniformSubroutinesuiv = loadFunction("glUniformSubroutinesuiv");
        _glUniformSubroutinesuiv.invoke(Void.class, new Object[]{shadertype_,count_,indices_ });
    }
    /* <command>
            <proto group="Boolean"><ptype>GLboolean</ptype> <name>glUnmapBuffer</name></proto>
            <param group="BufferTargetARB"><ptype>GLenum</ptype> <name>target</name></param>
        </command>
         */
    private static Function _glUnmapBuffer ;
    public static boolean glUnmapBuffer (int target_ ) {
        if( _glUnmapBuffer == null )
            _glUnmapBuffer = loadFunction("glUnmapBuffer");
        byte rv= ( Byte )_glUnmapBuffer.invoke(Byte.class, new Object[]{target_ });
        return (rv!=0);
    }
    /* <command>
            <proto><ptype>GLboolean</ptype> <name>glUnmapNamedBuffer</name></proto>
            <param><ptype>GLuint</ptype> <name>buffer</name></param>
        </command>
         */
    private static Function _glUnmapNamedBuffer ;
    public static boolean glUnmapNamedBuffer (int buffer_ ) {
        if( _glUnmapNamedBuffer == null )
            _glUnmapNamedBuffer = loadFunction("glUnmapNamedBuffer");
        byte rv= ( Byte )_glUnmapNamedBuffer.invoke(Byte.class, new Object[]{buffer_ });
        return (rv!=0);
    }
    /* <command>
            <proto>void <name>glUseProgram</name></proto>
            <param><ptype>GLuint</ptype> <name>program</name></param>
        </command>
         */
    private static Function _glUseProgram ;
    public static void glUseProgram (int program_ ) {
        if( _glUseProgram == null )
            _glUseProgram = loadFunction("glUseProgram");
        _glUseProgram.invoke(Void.class, new Object[]{program_ });
    }
    /* <command>
            <proto>void <name>glUseProgramStages</name></proto>
            <param><ptype>GLuint</ptype> <name>pipeline</name></param>
            <param><ptype>GLbitfield</ptype> <name>stages</name></param>
            <param><ptype>GLuint</ptype> <name>program</name></param>
        </command>
         */
    private static Function _glUseProgramStages ;
    public static void glUseProgramStages (int pipeline_ , int stages_ , int program_ ) {
        if( _glUseProgramStages == null )
            _glUseProgramStages = loadFunction("glUseProgramStages");
        _glUseProgramStages.invoke(Void.class, new Object[]{pipeline_,stages_,program_ });
    }
    /* <command>
            <proto>void <name>glValidateProgram</name></proto>
            <param><ptype>GLuint</ptype> <name>program</name></param>
        </command>
         */
    private static Function _glValidateProgram ;
    public static void glValidateProgram (int program_ ) {
        if( _glValidateProgram == null )
            _glValidateProgram = loadFunction("glValidateProgram");
        _glValidateProgram.invoke(Void.class, new Object[]{program_ });
    }
    /* <command>
            <proto>void <name>glValidateProgramPipeline</name></proto>
            <param><ptype>GLuint</ptype> <name>pipeline</name></param>
        </command>
         */
    private static Function _glValidateProgramPipeline ;
    public static void glValidateProgramPipeline (int pipeline_ ) {
        if( _glValidateProgramPipeline == null )
            _glValidateProgramPipeline = loadFunction("glValidateProgramPipeline");
        _glValidateProgramPipeline.invoke(Void.class, new Object[]{pipeline_ });
    }
    /* <command>
            <proto>void <name>glVertexArrayAttribBinding</name></proto>
            <param><ptype>GLuint</ptype> <name>vaobj</name></param>
            <param><ptype>GLuint</ptype> <name>attribindex</name></param>
            <param><ptype>GLuint</ptype> <name>bindingindex</name></param>
        </command>
         */
    private static Function _glVertexArrayAttribBinding ;
    public static void glVertexArrayAttribBinding (int vaobj_ , int attribindex_ , int bindingindex_ ) {
        if( _glVertexArrayAttribBinding == null )
            _glVertexArrayAttribBinding = loadFunction("glVertexArrayAttribBinding");
        _glVertexArrayAttribBinding.invoke(Void.class, new Object[]{vaobj_,attribindex_,bindingindex_ });
    }
    /* <command>
            <proto>void <name>glVertexArrayAttribFormat</name></proto>
            <param><ptype>GLuint</ptype> <name>vaobj</name></param>
            <param><ptype>GLuint</ptype> <name>attribindex</name></param>
            <param><ptype>GLint</ptype> <name>size</name></param>
            <param><ptype>GLenum</ptype> <name>type</name></param>
            <param><ptype>GLboolean</ptype> <name>normalized</name></param>
            <param><ptype>GLuint</ptype> <name>relativeoffset</name></param>
        </command>
         */
    private static Function _glVertexArrayAttribFormat ;
    public static void glVertexArrayAttribFormat (int vaobj_ , int attribindex_ , int size_ , int type_ , boolean normalized_ , int relativeoffset_ ) {
        if( _glVertexArrayAttribFormat == null )
            _glVertexArrayAttribFormat = loadFunction("glVertexArrayAttribFormat");
        _glVertexArrayAttribFormat.invoke(Void.class, new Object[]{vaobj_,attribindex_,size_,type_,(normalized_ ? (byte)1:(byte)0),relativeoffset_ });
    }
    /* <command>
            <proto>void <name>glVertexArrayAttribIFormat</name></proto>
            <param><ptype>GLuint</ptype> <name>vaobj</name></param>
            <param><ptype>GLuint</ptype> <name>attribindex</name></param>
            <param><ptype>GLint</ptype> <name>size</name></param>
            <param><ptype>GLenum</ptype> <name>type</name></param>
            <param><ptype>GLuint</ptype> <name>relativeoffset</name></param>
        </command>
         */
    private static Function _glVertexArrayAttribIFormat ;
    public static void glVertexArrayAttribIFormat (int vaobj_ , int attribindex_ , int size_ , int type_ , int relativeoffset_ ) {
        if( _glVertexArrayAttribIFormat == null )
            _glVertexArrayAttribIFormat = loadFunction("glVertexArrayAttribIFormat");
        _glVertexArrayAttribIFormat.invoke(Void.class, new Object[]{vaobj_,attribindex_,size_,type_,relativeoffset_ });
    }
    /* <command>
            <proto>void <name>glVertexArrayAttribLFormat</name></proto>
            <param><ptype>GLuint</ptype> <name>vaobj</name></param>
            <param><ptype>GLuint</ptype> <name>attribindex</name></param>
            <param><ptype>GLint</ptype> <name>size</name></param>
            <param><ptype>GLenum</ptype> <name>type</name></param>
            <param><ptype>GLuint</ptype> <name>relativeoffset</name></param>
        </command>
         */
    private static Function _glVertexArrayAttribLFormat ;
    public static void glVertexArrayAttribLFormat (int vaobj_ , int attribindex_ , int size_ , int type_ , int relativeoffset_ ) {
        if( _glVertexArrayAttribLFormat == null )
            _glVertexArrayAttribLFormat = loadFunction("glVertexArrayAttribLFormat");
        _glVertexArrayAttribLFormat.invoke(Void.class, new Object[]{vaobj_,attribindex_,size_,type_,relativeoffset_ });
    }
    /* <command>
            <proto>void <name>glVertexArrayBindingDivisor</name></proto>
            <param><ptype>GLuint</ptype> <name>vaobj</name></param>
            <param><ptype>GLuint</ptype> <name>bindingindex</name></param>
            <param><ptype>GLuint</ptype> <name>divisor</name></param>
        </command>
         */
    private static Function _glVertexArrayBindingDivisor ;
    public static void glVertexArrayBindingDivisor (int vaobj_ , int bindingindex_ , int divisor_ ) {
        if( _glVertexArrayBindingDivisor == null )
            _glVertexArrayBindingDivisor = loadFunction("glVertexArrayBindingDivisor");
        _glVertexArrayBindingDivisor.invoke(Void.class, new Object[]{vaobj_,bindingindex_,divisor_ });
    }
    /* <command>
            <proto>void <name>glVertexArrayElementBuffer</name></proto>
            <param><ptype>GLuint</ptype> <name>vaobj</name></param>
            <param><ptype>GLuint</ptype> <name>buffer</name></param>
        </command>
         */
    private static Function _glVertexArrayElementBuffer ;
    public static void glVertexArrayElementBuffer (int vaobj_ , int buffer_ ) {
        if( _glVertexArrayElementBuffer == null )
            _glVertexArrayElementBuffer = loadFunction("glVertexArrayElementBuffer");
        _glVertexArrayElementBuffer.invoke(Void.class, new Object[]{vaobj_,buffer_ });
    }
    /* <command>
            <proto>void <name>glVertexArrayVertexBuffer</name></proto>
            <param><ptype>GLuint</ptype> <name>vaobj</name></param>
            <param><ptype>GLuint</ptype> <name>bindingindex</name></param>
            <param><ptype>GLuint</ptype> <name>buffer</name></param>
            <param><ptype>GLintptr</ptype> <name>offset</name></param>
            <param><ptype>GLsizei</ptype> <name>stride</name></param>
        </command>
         */
    private static Function _glVertexArrayVertexBuffer ;
    public static void glVertexArrayVertexBuffer (int vaobj_ , int bindingindex_ , int buffer_ , long offset_ , int stride_ ) {
        if( _glVertexArrayVertexBuffer == null )
            _glVertexArrayVertexBuffer = loadFunction("glVertexArrayVertexBuffer");
        _glVertexArrayVertexBuffer.invoke(Void.class, new Object[]{vaobj_,bindingindex_,buffer_,offset_,stride_ });
    }
    /* <command>
            <proto>void <name>glVertexArrayVertexBuffers</name></proto>
            <param><ptype>GLuint</ptype> <name>vaobj</name></param>
            <param><ptype>GLuint</ptype> <name>first</name></param>
            <param><ptype>GLsizei</ptype> <name>count</name></param>
            <param>const <ptype>GLuint</ptype> *<name>buffers</name></param>
            <param>const <ptype>GLintptr</ptype> *<name>offsets</name></param>
            <param>const <ptype>GLsizei</ptype> *<name>strides</name></param>
        </command>
         */
    private static Function _glVertexArrayVertexBuffers ;
    public static void glVertexArrayVertexBuffers (int vaobj_ , int first_ , int count_ , int[] buffers_ , long[] offsets_ , int[] strides_ ) {
        if( _glVertexArrayVertexBuffers == null )
            _glVertexArrayVertexBuffers = loadFunction("glVertexArrayVertexBuffers");
        _glVertexArrayVertexBuffers.invoke(Void.class, new Object[]{vaobj_,first_,count_,buffers_,offsets_,strides_ });
    }
    /* <command>
            <proto>void <name>glVertexAttrib1d</name></proto>
            <param><ptype>GLuint</ptype> <name>index</name></param>
            <param><ptype>GLdouble</ptype> <name>x</name></param>
            <vecequiv name="glVertexAttrib1dv" />
        </command>
         */
    private static Function _glVertexAttrib1d ;
    public static void glVertexAttrib1d (int index_ , double x_ ) {
        if( _glVertexAttrib1d == null )
            _glVertexAttrib1d = loadFunction("glVertexAttrib1d");
        _glVertexAttrib1d.invoke(Void.class, new Object[]{index_,x_ });
    }
    /* <command>
            <proto>void <name>glVertexAttrib1dv</name></proto>
            <param><ptype>GLuint</ptype> <name>index</name></param>
            <param len="1">const <ptype>GLdouble</ptype> *<name>v</name></param>
            <glx opcode="4197" type="render" />
        </command>
         */
    private static Function _glVertexAttrib1dv ;
    public static void glVertexAttrib1dv (int index_ , double[] v_ ) {
        if( _glVertexAttrib1dv == null )
            _glVertexAttrib1dv = loadFunction("glVertexAttrib1dv");
        _glVertexAttrib1dv.invoke(Void.class, new Object[]{index_,v_ });
    }
    /* <command>
            <proto>void <name>glVertexAttrib1f</name></proto>
            <param><ptype>GLuint</ptype> <name>index</name></param>
            <param><ptype>GLfloat</ptype> <name>x</name></param>
            <vecequiv name="glVertexAttrib1fv" />
        </command>
         */
    private static Function _glVertexAttrib1f ;
    public static void glVertexAttrib1f (int index_ , float x_ ) {
        if( _glVertexAttrib1f == null )
            _glVertexAttrib1f = loadFunction("glVertexAttrib1f");
        _glVertexAttrib1f.invoke(Void.class, new Object[]{index_,x_ });
    }
    /* <command>
            <proto>void <name>glVertexAttrib1fv</name></proto>
            <param><ptype>GLuint</ptype> <name>index</name></param>
            <param len="1">const <ptype>GLfloat</ptype> *<name>v</name></param>
            <glx opcode="4193" type="render" />
        </command>
         */
    private static Function _glVertexAttrib1fv ;
    public static void glVertexAttrib1fv (int index_ , float[] v_ ) {
        if( _glVertexAttrib1fv == null )
            _glVertexAttrib1fv = loadFunction("glVertexAttrib1fv");
        _glVertexAttrib1fv.invoke(Void.class, new Object[]{index_,v_ });
    }
    /* <command>
            <proto>void <name>glVertexAttrib1s</name></proto>
            <param><ptype>GLuint</ptype> <name>index</name></param>
            <param><ptype>GLshort</ptype> <name>x</name></param>
            <vecequiv name="glVertexAttrib1sv" />
        </command>
         */
    private static Function _glVertexAttrib1s ;
    public static void glVertexAttrib1s (int index_ , short x_ ) {
        if( _glVertexAttrib1s == null )
            _glVertexAttrib1s = loadFunction("glVertexAttrib1s");
        _glVertexAttrib1s.invoke(Void.class, new Object[]{index_,x_ });
    }
    /* <command>
            <proto>void <name>glVertexAttrib1sv</name></proto>
            <param><ptype>GLuint</ptype> <name>index</name></param>
            <param len="1">const <ptype>GLshort</ptype> *<name>v</name></param>
            <glx opcode="4189" type="render" />
        </command>
         */
    private static Function _glVertexAttrib1sv ;
    public static void glVertexAttrib1sv (int index_ , short[] v_ ) {
        if( _glVertexAttrib1sv == null )
            _glVertexAttrib1sv = loadFunction("glVertexAttrib1sv");
        _glVertexAttrib1sv.invoke(Void.class, new Object[]{index_,v_ });
    }
    /* <command>
            <proto>void <name>glVertexAttrib2d</name></proto>
            <param><ptype>GLuint</ptype> <name>index</name></param>
            <param><ptype>GLdouble</ptype> <name>x</name></param>
            <param><ptype>GLdouble</ptype> <name>y</name></param>
            <vecequiv name="glVertexAttrib2dv" />
        </command>
         */
    private static Function _glVertexAttrib2d ;
    public static void glVertexAttrib2d (int index_ , double x_ , double y_ ) {
        if( _glVertexAttrib2d == null )
            _glVertexAttrib2d = loadFunction("glVertexAttrib2d");
        _glVertexAttrib2d.invoke(Void.class, new Object[]{index_,x_,y_ });
    }
    /* <command>
            <proto>void <name>glVertexAttrib2dv</name></proto>
            <param><ptype>GLuint</ptype> <name>index</name></param>
            <param len="2">const <ptype>GLdouble</ptype> *<name>v</name></param>
            <glx opcode="4198" type="render" />
        </command>
         */
    private static Function _glVertexAttrib2dv ;
    public static void glVertexAttrib2dv (int index_ , double[] v_ ) {
        if( _glVertexAttrib2dv == null )
            _glVertexAttrib2dv = loadFunction("glVertexAttrib2dv");
        _glVertexAttrib2dv.invoke(Void.class, new Object[]{index_,v_ });
    }
    /* <command>
            <proto>void <name>glVertexAttrib2f</name></proto>
            <param><ptype>GLuint</ptype> <name>index</name></param>
            <param><ptype>GLfloat</ptype> <name>x</name></param>
            <param><ptype>GLfloat</ptype> <name>y</name></param>
            <vecequiv name="glVertexAttrib2fv" />
        </command>
         */
    private static Function _glVertexAttrib2f ;
    public static void glVertexAttrib2f (int index_ , float x_ , float y_ ) {
        if( _glVertexAttrib2f == null )
            _glVertexAttrib2f = loadFunction("glVertexAttrib2f");
        _glVertexAttrib2f.invoke(Void.class, new Object[]{index_,x_,y_ });
    }
    /* <command>
            <proto>void <name>glVertexAttrib2fv</name></proto>
            <param><ptype>GLuint</ptype> <name>index</name></param>
            <param len="2">const <ptype>GLfloat</ptype> *<name>v</name></param>
            <glx opcode="4194" type="render" />
        </command>
         */
    private static Function _glVertexAttrib2fv ;
    public static void glVertexAttrib2fv (int index_ , float[] v_ ) {
        if( _glVertexAttrib2fv == null )
            _glVertexAttrib2fv = loadFunction("glVertexAttrib2fv");
        _glVertexAttrib2fv.invoke(Void.class, new Object[]{index_,v_ });
    }
    /* <command>
            <proto>void <name>glVertexAttrib2s</name></proto>
            <param><ptype>GLuint</ptype> <name>index</name></param>
            <param><ptype>GLshort</ptype> <name>x</name></param>
            <param><ptype>GLshort</ptype> <name>y</name></param>
            <vecequiv name="glVertexAttrib2sv" />
        </command>
         */
    private static Function _glVertexAttrib2s ;
    public static void glVertexAttrib2s (int index_ , short x_ , short y_ ) {
        if( _glVertexAttrib2s == null )
            _glVertexAttrib2s = loadFunction("glVertexAttrib2s");
        _glVertexAttrib2s.invoke(Void.class, new Object[]{index_,x_,y_ });
    }
    /* <command>
            <proto>void <name>glVertexAttrib2sv</name></proto>
            <param><ptype>GLuint</ptype> <name>index</name></param>
            <param len="2">const <ptype>GLshort</ptype> *<name>v</name></param>
            <glx opcode="4190" type="render" />
        </command>
         */
    private static Function _glVertexAttrib2sv ;
    public static void glVertexAttrib2sv (int index_ , short[] v_ ) {
        if( _glVertexAttrib2sv == null )
            _glVertexAttrib2sv = loadFunction("glVertexAttrib2sv");
        _glVertexAttrib2sv.invoke(Void.class, new Object[]{index_,v_ });
    }
    /* <command>
            <proto>void <name>glVertexAttrib3d</name></proto>
            <param><ptype>GLuint</ptype> <name>index</name></param>
            <param><ptype>GLdouble</ptype> <name>x</name></param>
            <param><ptype>GLdouble</ptype> <name>y</name></param>
            <param><ptype>GLdouble</ptype> <name>z</name></param>
            <vecequiv name="glVertexAttrib3dv" />
        </command>
         */
    private static Function _glVertexAttrib3d ;
    public static void glVertexAttrib3d (int index_ , double x_ , double y_ , double z_ ) {
        if( _glVertexAttrib3d == null )
            _glVertexAttrib3d = loadFunction("glVertexAttrib3d");
        _glVertexAttrib3d.invoke(Void.class, new Object[]{index_,x_,y_,z_ });
    }
    /* <command>
            <proto>void <name>glVertexAttrib3dv</name></proto>
            <param><ptype>GLuint</ptype> <name>index</name></param>
            <param len="3">const <ptype>GLdouble</ptype> *<name>v</name></param>
            <glx opcode="4199" type="render" />
        </command>
         */
    private static Function _glVertexAttrib3dv ;
    public static void glVertexAttrib3dv (int index_ , double[] v_ ) {
        if( _glVertexAttrib3dv == null )
            _glVertexAttrib3dv = loadFunction("glVertexAttrib3dv");
        _glVertexAttrib3dv.invoke(Void.class, new Object[]{index_,v_ });
    }
    /* <command>
            <proto>void <name>glVertexAttrib3f</name></proto>
            <param><ptype>GLuint</ptype> <name>index</name></param>
            <param><ptype>GLfloat</ptype> <name>x</name></param>
            <param><ptype>GLfloat</ptype> <name>y</name></param>
            <param><ptype>GLfloat</ptype> <name>z</name></param>
            <vecequiv name="glVertexAttrib3fv" />
        </command>
         */
    private static Function _glVertexAttrib3f ;
    public static void glVertexAttrib3f (int index_ , float x_ , float y_ , float z_ ) {
        if( _glVertexAttrib3f == null )
            _glVertexAttrib3f = loadFunction("glVertexAttrib3f");
        _glVertexAttrib3f.invoke(Void.class, new Object[]{index_,x_,y_,z_ });
    }
    /* <command>
            <proto>void <name>glVertexAttrib3fv</name></proto>
            <param><ptype>GLuint</ptype> <name>index</name></param>
            <param len="3">const <ptype>GLfloat</ptype> *<name>v</name></param>
            <glx opcode="4195" type="render" />
        </command>
         */
    private static Function _glVertexAttrib3fv ;
    public static void glVertexAttrib3fv (int index_ , float[] v_ ) {
        if( _glVertexAttrib3fv == null )
            _glVertexAttrib3fv = loadFunction("glVertexAttrib3fv");
        _glVertexAttrib3fv.invoke(Void.class, new Object[]{index_,v_ });
    }
    /* <command>
            <proto>void <name>glVertexAttrib3s</name></proto>
            <param><ptype>GLuint</ptype> <name>index</name></param>
            <param><ptype>GLshort</ptype> <name>x</name></param>
            <param><ptype>GLshort</ptype> <name>y</name></param>
            <param><ptype>GLshort</ptype> <name>z</name></param>
            <vecequiv name="glVertexAttrib3sv" />
        </command>
         */
    private static Function _glVertexAttrib3s ;
    public static void glVertexAttrib3s (int index_ , short x_ , short y_ , short z_ ) {
        if( _glVertexAttrib3s == null )
            _glVertexAttrib3s = loadFunction("glVertexAttrib3s");
        _glVertexAttrib3s.invoke(Void.class, new Object[]{index_,x_,y_,z_ });
    }
    /* <command>
            <proto>void <name>glVertexAttrib3sv</name></proto>
            <param><ptype>GLuint</ptype> <name>index</name></param>
            <param len="3">const <ptype>GLshort</ptype> *<name>v</name></param>
            <glx opcode="4191" type="render" />
        </command>
         */
    private static Function _glVertexAttrib3sv ;
    public static void glVertexAttrib3sv (int index_ , short[] v_ ) {
        if( _glVertexAttrib3sv == null )
            _glVertexAttrib3sv = loadFunction("glVertexAttrib3sv");
        _glVertexAttrib3sv.invoke(Void.class, new Object[]{index_,v_ });
    }
    /* <command>
            <proto>void <name>glVertexAttrib4Nbv</name></proto>
            <param><ptype>GLuint</ptype> <name>index</name></param>
            <param len="4">const <ptype>GLbyte</ptype> *<name>v</name></param>
        </command>
         */
    private static Function _glVertexAttrib4Nbv ;
    public static void glVertexAttrib4Nbv (int index_ , byte[] v_ ) {
        if( _glVertexAttrib4Nbv == null )
            _glVertexAttrib4Nbv = loadFunction("glVertexAttrib4Nbv");
        _glVertexAttrib4Nbv.invoke(Void.class, new Object[]{index_,v_ });
    }
    /* <command>
            <proto>void <name>glVertexAttrib4Niv</name></proto>
            <param><ptype>GLuint</ptype> <name>index</name></param>
            <param len="4">const <ptype>GLint</ptype> *<name>v</name></param>
        </command>
         */
    private static Function _glVertexAttrib4Niv ;
    public static void glVertexAttrib4Niv (int index_ , int[] v_ ) {
        if( _glVertexAttrib4Niv == null )
            _glVertexAttrib4Niv = loadFunction("glVertexAttrib4Niv");
        _glVertexAttrib4Niv.invoke(Void.class, new Object[]{index_,v_ });
    }
    /* <command>
            <proto>void <name>glVertexAttrib4Nsv</name></proto>
            <param><ptype>GLuint</ptype> <name>index</name></param>
            <param len="4">const <ptype>GLshort</ptype> *<name>v</name></param>
        </command>
         */
    private static Function _glVertexAttrib4Nsv ;
    public static void glVertexAttrib4Nsv (int index_ , short[] v_ ) {
        if( _glVertexAttrib4Nsv == null )
            _glVertexAttrib4Nsv = loadFunction("glVertexAttrib4Nsv");
        _glVertexAttrib4Nsv.invoke(Void.class, new Object[]{index_,v_ });
    }
    /* <command>
            <proto>void <name>glVertexAttrib4Nub</name></proto>
            <param><ptype>GLuint</ptype> <name>index</name></param>
            <param><ptype>GLubyte</ptype> <name>x</name></param>
            <param><ptype>GLubyte</ptype> <name>y</name></param>
            <param><ptype>GLubyte</ptype> <name>z</name></param>
            <param><ptype>GLubyte</ptype> <name>w</name></param>
        </command>
         */
    private static Function _glVertexAttrib4Nub ;
    public static void glVertexAttrib4Nub (int index_ , byte x_ , byte y_ , byte z_ , byte w_ ) {
        if( _glVertexAttrib4Nub == null )
            _glVertexAttrib4Nub = loadFunction("glVertexAttrib4Nub");
        _glVertexAttrib4Nub.invoke(Void.class, new Object[]{index_,x_,y_,z_,w_ });
    }
    /* <command>
            <proto>void <name>glVertexAttrib4Nubv</name></proto>
            <param><ptype>GLuint</ptype> <name>index</name></param>
            <param len="4">const <ptype>GLubyte</ptype> *<name>v</name></param>
            <glx opcode="4201" type="render" />
        </command>
         */
    private static Function _glVertexAttrib4Nubv ;
    public static void glVertexAttrib4Nubv (int index_ , byte[] v_ ) {
        if( _glVertexAttrib4Nubv == null )
            _glVertexAttrib4Nubv = loadFunction("glVertexAttrib4Nubv");
        _glVertexAttrib4Nubv.invoke(Void.class, new Object[]{index_,v_ });
    }
    /* <command>
            <proto>void <name>glVertexAttrib4Nuiv</name></proto>
            <param><ptype>GLuint</ptype> <name>index</name></param>
            <param len="4">const <ptype>GLuint</ptype> *<name>v</name></param>
        </command>
         */
    private static Function _glVertexAttrib4Nuiv ;
    public static void glVertexAttrib4Nuiv (int index_ , int[] v_ ) {
        if( _glVertexAttrib4Nuiv == null )
            _glVertexAttrib4Nuiv = loadFunction("glVertexAttrib4Nuiv");
        _glVertexAttrib4Nuiv.invoke(Void.class, new Object[]{index_,v_ });
    }
    /* <command>
            <proto>void <name>glVertexAttrib4Nusv</name></proto>
            <param><ptype>GLuint</ptype> <name>index</name></param>
            <param len="4">const <ptype>GLushort</ptype> *<name>v</name></param>
        </command>
         */
    private static Function _glVertexAttrib4Nusv ;
    public static void glVertexAttrib4Nusv (int index_ , short[] v_ ) {
        if( _glVertexAttrib4Nusv == null )
            _glVertexAttrib4Nusv = loadFunction("glVertexAttrib4Nusv");
        _glVertexAttrib4Nusv.invoke(Void.class, new Object[]{index_,v_ });
    }
    /* <command>
            <proto>void <name>glVertexAttrib4bv</name></proto>
            <param><ptype>GLuint</ptype> <name>index</name></param>
            <param len="4">const <ptype>GLbyte</ptype> *<name>v</name></param>
        </command>
         */
    private static Function _glVertexAttrib4bv ;
    public static void glVertexAttrib4bv (int index_ , byte[] v_ ) {
        if( _glVertexAttrib4bv == null )
            _glVertexAttrib4bv = loadFunction("glVertexAttrib4bv");
        _glVertexAttrib4bv.invoke(Void.class, new Object[]{index_,v_ });
    }
    /* <command>
            <proto>void <name>glVertexAttrib4d</name></proto>
            <param><ptype>GLuint</ptype> <name>index</name></param>
            <param><ptype>GLdouble</ptype> <name>x</name></param>
            <param><ptype>GLdouble</ptype> <name>y</name></param>
            <param><ptype>GLdouble</ptype> <name>z</name></param>
            <param><ptype>GLdouble</ptype> <name>w</name></param>
            <vecequiv name="glVertexAttrib4dv" />
        </command>
         */
    private static Function _glVertexAttrib4d ;
    public static void glVertexAttrib4d (int index_ , double x_ , double y_ , double z_ , double w_ ) {
        if( _glVertexAttrib4d == null )
            _glVertexAttrib4d = loadFunction("glVertexAttrib4d");
        _glVertexAttrib4d.invoke(Void.class, new Object[]{index_,x_,y_,z_,w_ });
    }
    /* <command>
            <proto>void <name>glVertexAttrib4dv</name></proto>
            <param><ptype>GLuint</ptype> <name>index</name></param>
            <param len="4">const <ptype>GLdouble</ptype> *<name>v</name></param>
            <glx opcode="4200" type="render" />
        </command>
         */
    private static Function _glVertexAttrib4dv ;
    public static void glVertexAttrib4dv (int index_ , double[] v_ ) {
        if( _glVertexAttrib4dv == null )
            _glVertexAttrib4dv = loadFunction("glVertexAttrib4dv");
        _glVertexAttrib4dv.invoke(Void.class, new Object[]{index_,v_ });
    }
    /* <command>
            <proto>void <name>glVertexAttrib4f</name></proto>
            <param><ptype>GLuint</ptype> <name>index</name></param>
            <param><ptype>GLfloat</ptype> <name>x</name></param>
            <param><ptype>GLfloat</ptype> <name>y</name></param>
            <param><ptype>GLfloat</ptype> <name>z</name></param>
            <param><ptype>GLfloat</ptype> <name>w</name></param>
            <vecequiv name="glVertexAttrib4fv" />
        </command>
         */
    private static Function _glVertexAttrib4f ;
    public static void glVertexAttrib4f (int index_ , float x_ , float y_ , float z_ , float w_ ) {
        if( _glVertexAttrib4f == null )
            _glVertexAttrib4f = loadFunction("glVertexAttrib4f");
        _glVertexAttrib4f.invoke(Void.class, new Object[]{index_,x_,y_,z_,w_ });
    }
    /* <command>
            <proto>void <name>glVertexAttrib4fv</name></proto>
            <param><ptype>GLuint</ptype> <name>index</name></param>
            <param len="4">const <ptype>GLfloat</ptype> *<name>v</name></param>
            <glx opcode="4196" type="render" />
        </command>
         */
    private static Function _glVertexAttrib4fv ;
    public static void glVertexAttrib4fv (int index_ , float[] v_ ) {
        if( _glVertexAttrib4fv == null )
            _glVertexAttrib4fv = loadFunction("glVertexAttrib4fv");
        _glVertexAttrib4fv.invoke(Void.class, new Object[]{index_,v_ });
    }
    /* <command>
            <proto>void <name>glVertexAttrib4iv</name></proto>
            <param><ptype>GLuint</ptype> <name>index</name></param>
            <param len="4">const <ptype>GLint</ptype> *<name>v</name></param>
        </command>
         */
    private static Function _glVertexAttrib4iv ;
    public static void glVertexAttrib4iv (int index_ , int[] v_ ) {
        if( _glVertexAttrib4iv == null )
            _glVertexAttrib4iv = loadFunction("glVertexAttrib4iv");
        _glVertexAttrib4iv.invoke(Void.class, new Object[]{index_,v_ });
    }
    /* <command>
            <proto>void <name>glVertexAttrib4s</name></proto>
            <param><ptype>GLuint</ptype> <name>index</name></param>
            <param><ptype>GLshort</ptype> <name>x</name></param>
            <param><ptype>GLshort</ptype> <name>y</name></param>
            <param><ptype>GLshort</ptype> <name>z</name></param>
            <param><ptype>GLshort</ptype> <name>w</name></param>
            <vecequiv name="glVertexAttrib4sv" />
        </command>
         */
    private static Function _glVertexAttrib4s ;
    public static void glVertexAttrib4s (int index_ , short x_ , short y_ , short z_ , short w_ ) {
        if( _glVertexAttrib4s == null )
            _glVertexAttrib4s = loadFunction("glVertexAttrib4s");
        _glVertexAttrib4s.invoke(Void.class, new Object[]{index_,x_,y_,z_,w_ });
    }
    /* <command>
            <proto>void <name>glVertexAttrib4sv</name></proto>
            <param><ptype>GLuint</ptype> <name>index</name></param>
            <param len="4">const <ptype>GLshort</ptype> *<name>v</name></param>
            <glx opcode="4192" type="render" />
        </command>
         */
    private static Function _glVertexAttrib4sv ;
    public static void glVertexAttrib4sv (int index_ , short[] v_ ) {
        if( _glVertexAttrib4sv == null )
            _glVertexAttrib4sv = loadFunction("glVertexAttrib4sv");
        _glVertexAttrib4sv.invoke(Void.class, new Object[]{index_,v_ });
    }
    /* <command>
            <proto>void <name>glVertexAttrib4ubv</name></proto>
            <param><ptype>GLuint</ptype> <name>index</name></param>
            <param len="4">const <ptype>GLubyte</ptype> *<name>v</name></param>
        </command>
         */
    private static Function _glVertexAttrib4ubv ;
    public static void glVertexAttrib4ubv (int index_ , byte[] v_ ) {
        if( _glVertexAttrib4ubv == null )
            _glVertexAttrib4ubv = loadFunction("glVertexAttrib4ubv");
        _glVertexAttrib4ubv.invoke(Void.class, new Object[]{index_,v_ });
    }
    /* <command>
            <proto>void <name>glVertexAttrib4uiv</name></proto>
            <param><ptype>GLuint</ptype> <name>index</name></param>
            <param len="4">const <ptype>GLuint</ptype> *<name>v</name></param>
        </command>
         */
    private static Function _glVertexAttrib4uiv ;
    public static void glVertexAttrib4uiv (int index_ , int[] v_ ) {
        if( _glVertexAttrib4uiv == null )
            _glVertexAttrib4uiv = loadFunction("glVertexAttrib4uiv");
        _glVertexAttrib4uiv.invoke(Void.class, new Object[]{index_,v_ });
    }
    /* <command>
            <proto>void <name>glVertexAttrib4usv</name></proto>
            <param><ptype>GLuint</ptype> <name>index</name></param>
            <param len="4">const <ptype>GLushort</ptype> *<name>v</name></param>
        </command>
         */
    private static Function _glVertexAttrib4usv ;
    public static void glVertexAttrib4usv (int index_ , short[] v_ ) {
        if( _glVertexAttrib4usv == null )
            _glVertexAttrib4usv = loadFunction("glVertexAttrib4usv");
        _glVertexAttrib4usv.invoke(Void.class, new Object[]{index_,v_ });
    }
    /* <command>
            <proto>void <name>glVertexAttribBinding</name></proto>
            <param><ptype>GLuint</ptype> <name>attribindex</name></param>
            <param><ptype>GLuint</ptype> <name>bindingindex</name></param>
        </command>
         */
    private static Function _glVertexAttribBinding ;
    public static void glVertexAttribBinding (int attribindex_ , int bindingindex_ ) {
        if( _glVertexAttribBinding == null )
            _glVertexAttribBinding = loadFunction("glVertexAttribBinding");
        _glVertexAttribBinding.invoke(Void.class, new Object[]{attribindex_,bindingindex_ });
    }
    /* <command>
            <proto>void <name>glVertexAttribDivisor</name></proto>
            <param><ptype>GLuint</ptype> <name>index</name></param>
            <param><ptype>GLuint</ptype> <name>divisor</name></param>
        </command>
         */
    private static Function _glVertexAttribDivisor ;
    public static void glVertexAttribDivisor (int index_ , int divisor_ ) {
        if( _glVertexAttribDivisor == null )
            _glVertexAttribDivisor = loadFunction("glVertexAttribDivisor");
        _glVertexAttribDivisor.invoke(Void.class, new Object[]{index_,divisor_ });
    }
    /* <command>
            <proto>void <name>glVertexAttribFormat</name></proto>
            <param><ptype>GLuint</ptype> <name>attribindex</name></param>
            <param><ptype>GLint</ptype> <name>size</name></param>
            <param><ptype>GLenum</ptype> <name>type</name></param>
            <param group="Boolean"><ptype>GLboolean</ptype> <name>normalized</name></param>
            <param><ptype>GLuint</ptype> <name>relativeoffset</name></param>
        </command>
         */
    private static Function _glVertexAttribFormat ;
    public static void glVertexAttribFormat (int attribindex_ , int size_ , int type_ , boolean normalized_ , int relativeoffset_ ) {
        if( _glVertexAttribFormat == null )
            _glVertexAttribFormat = loadFunction("glVertexAttribFormat");
        _glVertexAttribFormat.invoke(Void.class, new Object[]{attribindex_,size_,type_,(normalized_ ? (byte)1:(byte)0),relativeoffset_ });
    }
    /* <command>
            <proto>void <name>glVertexAttribI1i</name></proto>
            <param><ptype>GLuint</ptype> <name>index</name></param>
            <param><ptype>GLint</ptype> <name>x</name></param>
            <vecequiv name="glVertexAttribI1iv" />
        </command>
         */
    private static Function _glVertexAttribI1i ;
    public static void glVertexAttribI1i (int index_ , int x_ ) {
        if( _glVertexAttribI1i == null )
            _glVertexAttribI1i = loadFunction("glVertexAttribI1i");
        _glVertexAttribI1i.invoke(Void.class, new Object[]{index_,x_ });
    }
    /* <command>
            <proto>void <name>glVertexAttribI1iv</name></proto>
            <param><ptype>GLuint</ptype> <name>index</name></param>
            <param len="1">const <ptype>GLint</ptype> *<name>v</name></param>
        </command>
         */
    private static Function _glVertexAttribI1iv ;
    public static void glVertexAttribI1iv (int index_ , int[] v_ ) {
        if( _glVertexAttribI1iv == null )
            _glVertexAttribI1iv = loadFunction("glVertexAttribI1iv");
        _glVertexAttribI1iv.invoke(Void.class, new Object[]{index_,v_ });
    }
    /* <command>
            <proto>void <name>glVertexAttribI1ui</name></proto>
            <param><ptype>GLuint</ptype> <name>index</name></param>
            <param><ptype>GLuint</ptype> <name>x</name></param>
            <vecequiv name="glVertexAttribI1uiv" />
        </command>
         */
    private static Function _glVertexAttribI1ui ;
    public static void glVertexAttribI1ui (int index_ , int x_ ) {
        if( _glVertexAttribI1ui == null )
            _glVertexAttribI1ui = loadFunction("glVertexAttribI1ui");
        _glVertexAttribI1ui.invoke(Void.class, new Object[]{index_,x_ });
    }
    /* <command>
            <proto>void <name>glVertexAttribI1uiv</name></proto>
            <param><ptype>GLuint</ptype> <name>index</name></param>
            <param len="1">const <ptype>GLuint</ptype> *<name>v</name></param>
        </command>
         */
    private static Function _glVertexAttribI1uiv ;
    public static void glVertexAttribI1uiv (int index_ , int[] v_ ) {
        if( _glVertexAttribI1uiv == null )
            _glVertexAttribI1uiv = loadFunction("glVertexAttribI1uiv");
        _glVertexAttribI1uiv.invoke(Void.class, new Object[]{index_,v_ });
    }
    /* <command>
            <proto>void <name>glVertexAttribI2i</name></proto>
            <param><ptype>GLuint</ptype> <name>index</name></param>
            <param><ptype>GLint</ptype> <name>x</name></param>
            <param><ptype>GLint</ptype> <name>y</name></param>
            <vecequiv name="glVertexAttribI2iv" />
        </command>
         */
    private static Function _glVertexAttribI2i ;
    public static void glVertexAttribI2i (int index_ , int x_ , int y_ ) {
        if( _glVertexAttribI2i == null )
            _glVertexAttribI2i = loadFunction("glVertexAttribI2i");
        _glVertexAttribI2i.invoke(Void.class, new Object[]{index_,x_,y_ });
    }
    /* <command>
            <proto>void <name>glVertexAttribI2iv</name></proto>
            <param><ptype>GLuint</ptype> <name>index</name></param>
            <param len="2">const <ptype>GLint</ptype> *<name>v</name></param>
        </command>
         */
    private static Function _glVertexAttribI2iv ;
    public static void glVertexAttribI2iv (int index_ , int[] v_ ) {
        if( _glVertexAttribI2iv == null )
            _glVertexAttribI2iv = loadFunction("glVertexAttribI2iv");
        _glVertexAttribI2iv.invoke(Void.class, new Object[]{index_,v_ });
    }
    /* <command>
            <proto>void <name>glVertexAttribI2ui</name></proto>
            <param><ptype>GLuint</ptype> <name>index</name></param>
            <param><ptype>GLuint</ptype> <name>x</name></param>
            <param><ptype>GLuint</ptype> <name>y</name></param>
            <vecequiv name="glVertexAttribI2uiv" />
        </command>
         */
    private static Function _glVertexAttribI2ui ;
    public static void glVertexAttribI2ui (int index_ , int x_ , int y_ ) {
        if( _glVertexAttribI2ui == null )
            _glVertexAttribI2ui = loadFunction("glVertexAttribI2ui");
        _glVertexAttribI2ui.invoke(Void.class, new Object[]{index_,x_,y_ });
    }
    /* <command>
            <proto>void <name>glVertexAttribI2uiv</name></proto>
            <param><ptype>GLuint</ptype> <name>index</name></param>
            <param len="2">const <ptype>GLuint</ptype> *<name>v</name></param>
        </command>
         */
    private static Function _glVertexAttribI2uiv ;
    public static void glVertexAttribI2uiv (int index_ , int[] v_ ) {
        if( _glVertexAttribI2uiv == null )
            _glVertexAttribI2uiv = loadFunction("glVertexAttribI2uiv");
        _glVertexAttribI2uiv.invoke(Void.class, new Object[]{index_,v_ });
    }
    /* <command>
            <proto>void <name>glVertexAttribI3i</name></proto>
            <param><ptype>GLuint</ptype> <name>index</name></param>
            <param><ptype>GLint</ptype> <name>x</name></param>
            <param><ptype>GLint</ptype> <name>y</name></param>
            <param><ptype>GLint</ptype> <name>z</name></param>
            <vecequiv name="glVertexAttribI3iv" />
        </command>
         */
    private static Function _glVertexAttribI3i ;
    public static void glVertexAttribI3i (int index_ , int x_ , int y_ , int z_ ) {
        if( _glVertexAttribI3i == null )
            _glVertexAttribI3i = loadFunction("glVertexAttribI3i");
        _glVertexAttribI3i.invoke(Void.class, new Object[]{index_,x_,y_,z_ });
    }
    /* <command>
            <proto>void <name>glVertexAttribI3iv</name></proto>
            <param><ptype>GLuint</ptype> <name>index</name></param>
            <param len="3">const <ptype>GLint</ptype> *<name>v</name></param>
        </command>
         */
    private static Function _glVertexAttribI3iv ;
    public static void glVertexAttribI3iv (int index_ , int[] v_ ) {
        if( _glVertexAttribI3iv == null )
            _glVertexAttribI3iv = loadFunction("glVertexAttribI3iv");
        _glVertexAttribI3iv.invoke(Void.class, new Object[]{index_,v_ });
    }
    /* <command>
            <proto>void <name>glVertexAttribI3ui</name></proto>
            <param><ptype>GLuint</ptype> <name>index</name></param>
            <param><ptype>GLuint</ptype> <name>x</name></param>
            <param><ptype>GLuint</ptype> <name>y</name></param>
            <param><ptype>GLuint</ptype> <name>z</name></param>
            <vecequiv name="glVertexAttribI3uiv" />
        </command>
         */
    private static Function _glVertexAttribI3ui ;
    public static void glVertexAttribI3ui (int index_ , int x_ , int y_ , int z_ ) {
        if( _glVertexAttribI3ui == null )
            _glVertexAttribI3ui = loadFunction("glVertexAttribI3ui");
        _glVertexAttribI3ui.invoke(Void.class, new Object[]{index_,x_,y_,z_ });
    }
    /* <command>
            <proto>void <name>glVertexAttribI3uiv</name></proto>
            <param><ptype>GLuint</ptype> <name>index</name></param>
            <param len="3">const <ptype>GLuint</ptype> *<name>v</name></param>
        </command>
         */
    private static Function _glVertexAttribI3uiv ;
    public static void glVertexAttribI3uiv (int index_ , int[] v_ ) {
        if( _glVertexAttribI3uiv == null )
            _glVertexAttribI3uiv = loadFunction("glVertexAttribI3uiv");
        _glVertexAttribI3uiv.invoke(Void.class, new Object[]{index_,v_ });
    }
    /* <command>
            <proto>void <name>glVertexAttribI4bv</name></proto>
            <param><ptype>GLuint</ptype> <name>index</name></param>
            <param len="4">const <ptype>GLbyte</ptype> *<name>v</name></param>
        </command>
         */
    private static Function _glVertexAttribI4bv ;
    public static void glVertexAttribI4bv (int index_ , byte[] v_ ) {
        if( _glVertexAttribI4bv == null )
            _glVertexAttribI4bv = loadFunction("glVertexAttribI4bv");
        _glVertexAttribI4bv.invoke(Void.class, new Object[]{index_,v_ });
    }
    /* <command>
            <proto>void <name>glVertexAttribI4i</name></proto>
            <param><ptype>GLuint</ptype> <name>index</name></param>
            <param><ptype>GLint</ptype> <name>x</name></param>
            <param><ptype>GLint</ptype> <name>y</name></param>
            <param><ptype>GLint</ptype> <name>z</name></param>
            <param><ptype>GLint</ptype> <name>w</name></param>
            <vecequiv name="glVertexAttribI4iv" />
        </command>
         */
    private static Function _glVertexAttribI4i ;
    public static void glVertexAttribI4i (int index_ , int x_ , int y_ , int z_ , int w_ ) {
        if( _glVertexAttribI4i == null )
            _glVertexAttribI4i = loadFunction("glVertexAttribI4i");
        _glVertexAttribI4i.invoke(Void.class, new Object[]{index_,x_,y_,z_,w_ });
    }
    /* <command>
            <proto>void <name>glVertexAttribI4iv</name></proto>
            <param><ptype>GLuint</ptype> <name>index</name></param>
            <param len="4">const <ptype>GLint</ptype> *<name>v</name></param>
        </command>
         */
    private static Function _glVertexAttribI4iv ;
    public static void glVertexAttribI4iv (int index_ , int[] v_ ) {
        if( _glVertexAttribI4iv == null )
            _glVertexAttribI4iv = loadFunction("glVertexAttribI4iv");
        _glVertexAttribI4iv.invoke(Void.class, new Object[]{index_,v_ });
    }
    /* <command>
            <proto>void <name>glVertexAttribI4sv</name></proto>
            <param><ptype>GLuint</ptype> <name>index</name></param>
            <param len="4">const <ptype>GLshort</ptype> *<name>v</name></param>
        </command>
         */
    private static Function _glVertexAttribI4sv ;
    public static void glVertexAttribI4sv (int index_ , short[] v_ ) {
        if( _glVertexAttribI4sv == null )
            _glVertexAttribI4sv = loadFunction("glVertexAttribI4sv");
        _glVertexAttribI4sv.invoke(Void.class, new Object[]{index_,v_ });
    }
    /* <command>
            <proto>void <name>glVertexAttribI4ubv</name></proto>
            <param><ptype>GLuint</ptype> <name>index</name></param>
            <param len="4">const <ptype>GLubyte</ptype> *<name>v</name></param>
        </command>
         */
    private static Function _glVertexAttribI4ubv ;
    public static void glVertexAttribI4ubv (int index_ , byte[] v_ ) {
        if( _glVertexAttribI4ubv == null )
            _glVertexAttribI4ubv = loadFunction("glVertexAttribI4ubv");
        _glVertexAttribI4ubv.invoke(Void.class, new Object[]{index_,v_ });
    }
    /* <command>
            <proto>void <name>glVertexAttribI4ui</name></proto>
            <param><ptype>GLuint</ptype> <name>index</name></param>
            <param><ptype>GLuint</ptype> <name>x</name></param>
            <param><ptype>GLuint</ptype> <name>y</name></param>
            <param><ptype>GLuint</ptype> <name>z</name></param>
            <param><ptype>GLuint</ptype> <name>w</name></param>
            <vecequiv name="glVertexAttribI4uiv" />
        </command>
         */
    private static Function _glVertexAttribI4ui ;
    public static void glVertexAttribI4ui (int index_ , int x_ , int y_ , int z_ , int w_ ) {
        if( _glVertexAttribI4ui == null )
            _glVertexAttribI4ui = loadFunction("glVertexAttribI4ui");
        _glVertexAttribI4ui.invoke(Void.class, new Object[]{index_,x_,y_,z_,w_ });
    }
    /* <command>
            <proto>void <name>glVertexAttribI4uiv</name></proto>
            <param><ptype>GLuint</ptype> <name>index</name></param>
            <param len="4">const <ptype>GLuint</ptype> *<name>v</name></param>
        </command>
         */
    private static Function _glVertexAttribI4uiv ;
    public static void glVertexAttribI4uiv (int index_ , int[] v_ ) {
        if( _glVertexAttribI4uiv == null )
            _glVertexAttribI4uiv = loadFunction("glVertexAttribI4uiv");
        _glVertexAttribI4uiv.invoke(Void.class, new Object[]{index_,v_ });
    }
    /* <command>
            <proto>void <name>glVertexAttribI4usv</name></proto>
            <param><ptype>GLuint</ptype> <name>index</name></param>
            <param len="4">const <ptype>GLushort</ptype> *<name>v</name></param>
        </command>
         */
    private static Function _glVertexAttribI4usv ;
    public static void glVertexAttribI4usv (int index_ , short[] v_ ) {
        if( _glVertexAttribI4usv == null )
            _glVertexAttribI4usv = loadFunction("glVertexAttribI4usv");
        _glVertexAttribI4usv.invoke(Void.class, new Object[]{index_,v_ });
    }
    /* <command>
            <proto>void <name>glVertexAttribIFormat</name></proto>
            <param><ptype>GLuint</ptype> <name>attribindex</name></param>
            <param><ptype>GLint</ptype> <name>size</name></param>
            <param><ptype>GLenum</ptype> <name>type</name></param>
            <param><ptype>GLuint</ptype> <name>relativeoffset</name></param>
        </command>
         */
    private static Function _glVertexAttribIFormat ;
    public static void glVertexAttribIFormat (int attribindex_ , int size_ , int type_ , int relativeoffset_ ) {
        if( _glVertexAttribIFormat == null )
            _glVertexAttribIFormat = loadFunction("glVertexAttribIFormat");
        _glVertexAttribIFormat.invoke(Void.class, new Object[]{attribindex_,size_,type_,relativeoffset_ });
    }
    /* <command>
            <proto>void <name>glVertexAttribIPointer</name></proto>
            <param><ptype>GLuint</ptype> <name>index</name></param>
            <param><ptype>GLint</ptype> <name>size</name></param>
            <param group="VertexAttribEnum"><ptype>GLenum</ptype> <name>type</name></param>
            <param><ptype>GLsizei</ptype> <name>stride</name></param>
            <param len="COMPSIZE(size,type,stride)">const void *<name>pointer</name></param>
        </command>
         */
    private static Function _glVertexAttribIPointer ;
    public static void glVertexAttribIPointer (int index_ , int size_ , int type_ , int stride_ , byte[] pointer_ ) {
        if( _glVertexAttribIPointer == null )
            _glVertexAttribIPointer = loadFunction("glVertexAttribIPointer");
        _glVertexAttribIPointer.invoke(Void.class, new Object[]{index_,size_,type_,stride_,pointer_ });
    }
    /* <command>
            <proto>void <name>glVertexAttribL1d</name></proto>
            <param><ptype>GLuint</ptype> <name>index</name></param>
            <param><ptype>GLdouble</ptype> <name>x</name></param>
        </command>
         */
    private static Function _glVertexAttribL1d ;
    public static void glVertexAttribL1d (int index_ , double x_ ) {
        if( _glVertexAttribL1d == null )
            _glVertexAttribL1d = loadFunction("glVertexAttribL1d");
        _glVertexAttribL1d.invoke(Void.class, new Object[]{index_,x_ });
    }
    /* <command>
            <proto>void <name>glVertexAttribL1dv</name></proto>
            <param><ptype>GLuint</ptype> <name>index</name></param>
            <param len="1">const <ptype>GLdouble</ptype> *<name>v</name></param>
        </command>
         */
    private static Function _glVertexAttribL1dv ;
    public static void glVertexAttribL1dv (int index_ , double[] v_ ) {
        if( _glVertexAttribL1dv == null )
            _glVertexAttribL1dv = loadFunction("glVertexAttribL1dv");
        _glVertexAttribL1dv.invoke(Void.class, new Object[]{index_,v_ });
    }
    /* <command>
            <proto>void <name>glVertexAttribL2d</name></proto>
            <param><ptype>GLuint</ptype> <name>index</name></param>
            <param><ptype>GLdouble</ptype> <name>x</name></param>
            <param><ptype>GLdouble</ptype> <name>y</name></param>
        </command>
         */
    private static Function _glVertexAttribL2d ;
    public static void glVertexAttribL2d (int index_ , double x_ , double y_ ) {
        if( _glVertexAttribL2d == null )
            _glVertexAttribL2d = loadFunction("glVertexAttribL2d");
        _glVertexAttribL2d.invoke(Void.class, new Object[]{index_,x_,y_ });
    }
    /* <command>
            <proto>void <name>glVertexAttribL2dv</name></proto>
            <param><ptype>GLuint</ptype> <name>index</name></param>
            <param len="2">const <ptype>GLdouble</ptype> *<name>v</name></param>
        </command>
         */
    private static Function _glVertexAttribL2dv ;
    public static void glVertexAttribL2dv (int index_ , double[] v_ ) {
        if( _glVertexAttribL2dv == null )
            _glVertexAttribL2dv = loadFunction("glVertexAttribL2dv");
        _glVertexAttribL2dv.invoke(Void.class, new Object[]{index_,v_ });
    }
    /* <command>
            <proto>void <name>glVertexAttribL3d</name></proto>
            <param><ptype>GLuint</ptype> <name>index</name></param>
            <param><ptype>GLdouble</ptype> <name>x</name></param>
            <param><ptype>GLdouble</ptype> <name>y</name></param>
            <param><ptype>GLdouble</ptype> <name>z</name></param>
        </command>
         */
    private static Function _glVertexAttribL3d ;
    public static void glVertexAttribL3d (int index_ , double x_ , double y_ , double z_ ) {
        if( _glVertexAttribL3d == null )
            _glVertexAttribL3d = loadFunction("glVertexAttribL3d");
        _glVertexAttribL3d.invoke(Void.class, new Object[]{index_,x_,y_,z_ });
    }
    /* <command>
            <proto>void <name>glVertexAttribL3dv</name></proto>
            <param><ptype>GLuint</ptype> <name>index</name></param>
            <param len="3">const <ptype>GLdouble</ptype> *<name>v</name></param>
        </command>
         */
    private static Function _glVertexAttribL3dv ;
    public static void glVertexAttribL3dv (int index_ , double[] v_ ) {
        if( _glVertexAttribL3dv == null )
            _glVertexAttribL3dv = loadFunction("glVertexAttribL3dv");
        _glVertexAttribL3dv.invoke(Void.class, new Object[]{index_,v_ });
    }
    /* <command>
            <proto>void <name>glVertexAttribL4d</name></proto>
            <param><ptype>GLuint</ptype> <name>index</name></param>
            <param><ptype>GLdouble</ptype> <name>x</name></param>
            <param><ptype>GLdouble</ptype> <name>y</name></param>
            <param><ptype>GLdouble</ptype> <name>z</name></param>
            <param><ptype>GLdouble</ptype> <name>w</name></param>
        </command>
         */
    private static Function _glVertexAttribL4d ;
    public static void glVertexAttribL4d (int index_ , double x_ , double y_ , double z_ , double w_ ) {
        if( _glVertexAttribL4d == null )
            _glVertexAttribL4d = loadFunction("glVertexAttribL4d");
        _glVertexAttribL4d.invoke(Void.class, new Object[]{index_,x_,y_,z_,w_ });
    }
    /* <command>
            <proto>void <name>glVertexAttribL4dv</name></proto>
            <param><ptype>GLuint</ptype> <name>index</name></param>
            <param len="4">const <ptype>GLdouble</ptype> *<name>v</name></param>
        </command>
         */
    private static Function _glVertexAttribL4dv ;
    public static void glVertexAttribL4dv (int index_ , double[] v_ ) {
        if( _glVertexAttribL4dv == null )
            _glVertexAttribL4dv = loadFunction("glVertexAttribL4dv");
        _glVertexAttribL4dv.invoke(Void.class, new Object[]{index_,v_ });
    }
    /* <command>
            <proto>void <name>glVertexAttribLFormat</name></proto>
            <param><ptype>GLuint</ptype> <name>attribindex</name></param>
            <param><ptype>GLint</ptype> <name>size</name></param>
            <param><ptype>GLenum</ptype> <name>type</name></param>
            <param><ptype>GLuint</ptype> <name>relativeoffset</name></param>
        </command>
         */
    private static Function _glVertexAttribLFormat ;
    public static void glVertexAttribLFormat (int attribindex_ , int size_ , int type_ , int relativeoffset_ ) {
        if( _glVertexAttribLFormat == null )
            _glVertexAttribLFormat = loadFunction("glVertexAttribLFormat");
        _glVertexAttribLFormat.invoke(Void.class, new Object[]{attribindex_,size_,type_,relativeoffset_ });
    }
    /* <command>
            <proto>void <name>glVertexAttribLPointer</name></proto>
            <param><ptype>GLuint</ptype> <name>index</name></param>
            <param><ptype>GLint</ptype> <name>size</name></param>
            <param><ptype>GLenum</ptype> <name>type</name></param>
            <param><ptype>GLsizei</ptype> <name>stride</name></param>
            <param len="size">const void *<name>pointer</name></param>
        </command>
         */
    private static Function _glVertexAttribLPointer ;
    public static void glVertexAttribLPointer (int index_ , int size_ , int type_ , int stride_ , byte[] pointer_ ) {
        if( _glVertexAttribLPointer == null )
            _glVertexAttribLPointer = loadFunction("glVertexAttribLPointer");
        _glVertexAttribLPointer.invoke(Void.class, new Object[]{index_,size_,type_,stride_,pointer_ });
    }
    /* <command>
            <proto>void <name>glVertexAttribP1ui</name></proto>
            <param><ptype>GLuint</ptype> <name>index</name></param>
            <param><ptype>GLenum</ptype> <name>type</name></param>
            <param group="Boolean"><ptype>GLboolean</ptype> <name>normalized</name></param>
            <param><ptype>GLuint</ptype> <name>value</name></param>
        </command>
         */
    private static Function _glVertexAttribP1ui ;
    public static void glVertexAttribP1ui (int index_ , int type_ , boolean normalized_ , int value_ ) {
        if( _glVertexAttribP1ui == null )
            _glVertexAttribP1ui = loadFunction("glVertexAttribP1ui");
        _glVertexAttribP1ui.invoke(Void.class, new Object[]{index_,type_,(normalized_ ? (byte)1:(byte)0),value_ });
    }
    /* <command>
            <proto>void <name>glVertexAttribP1uiv</name></proto>
            <param><ptype>GLuint</ptype> <name>index</name></param>
            <param><ptype>GLenum</ptype> <name>type</name></param>
            <param group="Boolean"><ptype>GLboolean</ptype> <name>normalized</name></param>
            <param len="1">const <ptype>GLuint</ptype> *<name>value</name></param>
        </command>
         */
    private static Function _glVertexAttribP1uiv ;
    public static void glVertexAttribP1uiv (int index_ , int type_ , boolean normalized_ , int[] value_ ) {
        if( _glVertexAttribP1uiv == null )
            _glVertexAttribP1uiv = loadFunction("glVertexAttribP1uiv");
        _glVertexAttribP1uiv.invoke(Void.class, new Object[]{index_,type_,(normalized_ ? (byte)1:(byte)0),value_ });
    }
    /* <command>
            <proto>void <name>glVertexAttribP2ui</name></proto>
            <param><ptype>GLuint</ptype> <name>index</name></param>
            <param><ptype>GLenum</ptype> <name>type</name></param>
            <param group="Boolean"><ptype>GLboolean</ptype> <name>normalized</name></param>
            <param><ptype>GLuint</ptype> <name>value</name></param>
        </command>
         */
    private static Function _glVertexAttribP2ui ;
    public static void glVertexAttribP2ui (int index_ , int type_ , boolean normalized_ , int value_ ) {
        if( _glVertexAttribP2ui == null )
            _glVertexAttribP2ui = loadFunction("glVertexAttribP2ui");
        _glVertexAttribP2ui.invoke(Void.class, new Object[]{index_,type_,(normalized_ ? (byte)1:(byte)0),value_ });
    }
    /* <command>
            <proto>void <name>glVertexAttribP2uiv</name></proto>
            <param><ptype>GLuint</ptype> <name>index</name></param>
            <param><ptype>GLenum</ptype> <name>type</name></param>
            <param group="Boolean"><ptype>GLboolean</ptype> <name>normalized</name></param>
            <param len="1">const <ptype>GLuint</ptype> *<name>value</name></param>
        </command>
         */
    private static Function _glVertexAttribP2uiv ;
    public static void glVertexAttribP2uiv (int index_ , int type_ , boolean normalized_ , int[] value_ ) {
        if( _glVertexAttribP2uiv == null )
            _glVertexAttribP2uiv = loadFunction("glVertexAttribP2uiv");
        _glVertexAttribP2uiv.invoke(Void.class, new Object[]{index_,type_,(normalized_ ? (byte)1:(byte)0),value_ });
    }
    /* <command>
            <proto>void <name>glVertexAttribP3ui</name></proto>
            <param><ptype>GLuint</ptype> <name>index</name></param>
            <param><ptype>GLenum</ptype> <name>type</name></param>
            <param group="Boolean"><ptype>GLboolean</ptype> <name>normalized</name></param>
            <param><ptype>GLuint</ptype> <name>value</name></param>
        </command>
         */
    private static Function _glVertexAttribP3ui ;
    public static void glVertexAttribP3ui (int index_ , int type_ , boolean normalized_ , int value_ ) {
        if( _glVertexAttribP3ui == null )
            _glVertexAttribP3ui = loadFunction("glVertexAttribP3ui");
        _glVertexAttribP3ui.invoke(Void.class, new Object[]{index_,type_,(normalized_ ? (byte)1:(byte)0),value_ });
    }
    /* <command>
            <proto>void <name>glVertexAttribP3uiv</name></proto>
            <param><ptype>GLuint</ptype> <name>index</name></param>
            <param><ptype>GLenum</ptype> <name>type</name></param>
            <param group="Boolean"><ptype>GLboolean</ptype> <name>normalized</name></param>
            <param len="1">const <ptype>GLuint</ptype> *<name>value</name></param>
        </command>
         */
    private static Function _glVertexAttribP3uiv ;
    public static void glVertexAttribP3uiv (int index_ , int type_ , boolean normalized_ , int[] value_ ) {
        if( _glVertexAttribP3uiv == null )
            _glVertexAttribP3uiv = loadFunction("glVertexAttribP3uiv");
        _glVertexAttribP3uiv.invoke(Void.class, new Object[]{index_,type_,(normalized_ ? (byte)1:(byte)0),value_ });
    }
    /* <command>
            <proto>void <name>glVertexAttribP4ui</name></proto>
            <param><ptype>GLuint</ptype> <name>index</name></param>
            <param><ptype>GLenum</ptype> <name>type</name></param>
            <param group="Boolean"><ptype>GLboolean</ptype> <name>normalized</name></param>
            <param><ptype>GLuint</ptype> <name>value</name></param>
        </command>
         */
    private static Function _glVertexAttribP4ui ;
    public static void glVertexAttribP4ui (int index_ , int type_ , boolean normalized_ , int value_ ) {
        if( _glVertexAttribP4ui == null )
            _glVertexAttribP4ui = loadFunction("glVertexAttribP4ui");
        _glVertexAttribP4ui.invoke(Void.class, new Object[]{index_,type_,(normalized_ ? (byte)1:(byte)0),value_ });
    }
    /* <command>
            <proto>void <name>glVertexAttribP4uiv</name></proto>
            <param><ptype>GLuint</ptype> <name>index</name></param>
            <param><ptype>GLenum</ptype> <name>type</name></param>
            <param group="Boolean"><ptype>GLboolean</ptype> <name>normalized</name></param>
            <param len="1">const <ptype>GLuint</ptype> *<name>value</name></param>
        </command>
         */
    private static Function _glVertexAttribP4uiv ;
    public static void glVertexAttribP4uiv (int index_ , int type_ , boolean normalized_ , int[] value_ ) {
        if( _glVertexAttribP4uiv == null )
            _glVertexAttribP4uiv = loadFunction("glVertexAttribP4uiv");
        _glVertexAttribP4uiv.invoke(Void.class, new Object[]{index_,type_,(normalized_ ? (byte)1:(byte)0),value_ });
    }
    /* <command>
            <proto>void <name>glVertexAttribPointer</name></proto>
            <param><ptype>GLuint</ptype> <name>index</name></param>
            <param><ptype>GLint</ptype> <name>size</name></param>
            <param group="VertexAttribPointerType"><ptype>GLenum</ptype> <name>type</name></param>
            <param group="Boolean"><ptype>GLboolean</ptype> <name>normalized</name></param>
            <param><ptype>GLsizei</ptype> <name>stride</name></param>
            <param len="COMPSIZE(size,type,stride)">const void *<name>pointer</name></param>
        </command>
         */
    private static Function _glVertexAttribPointer ;
    public static void glVertexAttribPointer (int index_ , int size_ , int type_ , boolean normalized_ , int stride_ , byte[] pointer_ ) {
        if( _glVertexAttribPointer == null )
            _glVertexAttribPointer = loadFunction("glVertexAttribPointer");
        _glVertexAttribPointer.invoke(Void.class, new Object[]{index_,size_,type_,(normalized_ ? (byte)1:(byte)0),stride_,pointer_ });
    }
    /* <command>
            <proto>void <name>glVertexBindingDivisor</name></proto>
            <param><ptype>GLuint</ptype> <name>bindingindex</name></param>
            <param><ptype>GLuint</ptype> <name>divisor</name></param>
        </command>
         */
    private static Function _glVertexBindingDivisor ;
    public static void glVertexBindingDivisor (int bindingindex_ , int divisor_ ) {
        if( _glVertexBindingDivisor == null )
            _glVertexBindingDivisor = loadFunction("glVertexBindingDivisor");
        _glVertexBindingDivisor.invoke(Void.class, new Object[]{bindingindex_,divisor_ });
    }
    /* <command>
            <proto>void <name>glViewport</name></proto>
            <param group="WinCoord"><ptype>GLint</ptype> <name>x</name></param>
            <param group="WinCoord"><ptype>GLint</ptype> <name>y</name></param>
            <param><ptype>GLsizei</ptype> <name>width</name></param>
            <param><ptype>GLsizei</ptype> <name>height</name></param>
            <glx opcode="191" type="render" />
        </command>
         */
    private static Function _glViewport ;
    public static void glViewport (int x_ , int y_ , int width_ , int height_ ) {
        if( _glViewport == null )
            _glViewport = loadFunction("glViewport");
        _glViewport.invoke(Void.class, new Object[]{x_,y_,width_,height_ });
    }
    /* <command>
            <proto>void <name>glViewportArrayv</name></proto>
            <param><ptype>GLuint</ptype> <name>first</name></param>
            <param><ptype>GLsizei</ptype> <name>count</name></param>
            <param len="COMPSIZE(count)">const <ptype>GLfloat</ptype> *<name>v</name></param>
        </command>
         */
    private static Function _glViewportArrayv ;
    public static void glViewportArrayv (int first_ , int count_ , float[] v_ ) {
        if( _glViewportArrayv == null )
            _glViewportArrayv = loadFunction("glViewportArrayv");
        _glViewportArrayv.invoke(Void.class, new Object[]{first_,count_,v_ });
    }
    /* <command>
            <proto>void <name>glViewportIndexedf</name></proto>
            <param><ptype>GLuint</ptype> <name>index</name></param>
            <param><ptype>GLfloat</ptype> <name>x</name></param>
            <param><ptype>GLfloat</ptype> <name>y</name></param>
            <param><ptype>GLfloat</ptype> <name>w</name></param>
            <param><ptype>GLfloat</ptype> <name>h</name></param>
        </command>
         */
    private static Function _glViewportIndexedf ;
    public static void glViewportIndexedf (int index_ , float x_ , float y_ , float w_ , float h_ ) {
        if( _glViewportIndexedf == null )
            _glViewportIndexedf = loadFunction("glViewportIndexedf");
        _glViewportIndexedf.invoke(Void.class, new Object[]{index_,x_,y_,w_,h_ });
    }
    /* <command>
            <proto>void <name>glViewportIndexedfv</name></proto>
            <param><ptype>GLuint</ptype> <name>index</name></param>
            <param len="4">const <ptype>GLfloat</ptype> *<name>v</name></param>
        </command>
         */
    private static Function _glViewportIndexedfv ;
    public static void glViewportIndexedfv (int index_ , float[] v_ ) {
        if( _glViewportIndexedfv == null )
            _glViewportIndexedfv = loadFunction("glViewportIndexedfv");
        _glViewportIndexedfv.invoke(Void.class, new Object[]{index_,v_ });
    }
    /* <command>
            <proto>void <name>glWaitSync</name></proto>
            <param group="sync"><ptype>GLsync</ptype> <name>sync</name></param>
            <param><ptype>GLbitfield</ptype> <name>flags</name></param>
            <param><ptype>GLuint64</ptype> <name>timeout</name></param>
        </command>
         */
    private static Function _glWaitSync ;
    public static void glWaitSync (Pointer sync_ , int flags_ , long timeout_ ) {
        if( _glWaitSync == null )
            _glWaitSync = loadFunction("glWaitSync");
        _glWaitSync.invoke(Void.class, new Object[]{sync_,flags_,timeout_ });
    }
}
