//Based on assimp, which has these copyrights:
/*
---------------------------------------------------------------------------
Open Asset Import Library (assimp)
---------------------------------------------------------------------------

Copyright (c) 2006-2011, assimp team

All rights reserved.

Redistribution and use of this software in source and binary forms, 
with or without modification, are permitted provided that the following 
conditions are met:

* Redistributions of source code must retain the above
copyright notice, this list of conditions and the
following disclaimer.

* Redistributions in binary form must reproduce the above
copyright notice, this list of conditions and the
following disclaimer in the documentation and/or other
materials provided with the distribution.

* Neither the name of the assimp team, nor the names of its
contributors may be used to endorse or promote products
derived from this software without specific prior
written permission of the assimp team.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS 
"AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT 
LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT 
OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT 
LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY 
THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT 
(INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE 
OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
---------------------------------------------------------------------------
---------------------------------------------------------------------------
Open Asset Import Library (assimp)
---------------------------------------------------------------------------

Copyright (c) 2006-2012, assimp team

All rights reserved.

Redistribution and use of this software in source and binary forms, 
with or without modification, are permitted provided that the following 
conditions are met:

* Redistributions of source code must retain the above
  copyright notice, this list of conditions and the
  following disclaimer.

* Redistributions in binary form must reproduce the above
  copyright notice, this list of conditions and the
  following disclaimer in the documentation and/or other
  materials provided with the distribution.

* Neither the name of the assimp team, nor the names of its
  contributors may be used to endorse or promote products
  derived from this software without specific prior
  written permission of the assimp team.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS 
"AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT 
LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT 
OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT 
LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY 
THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT 
(INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE 
OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
---------------------------------------------------------------------------
Open Asset Import Library (assimp)
----------------------------------------------------------------------

Copyright (c) 2006-2012, assimp team
All rights reserved.

Redistribution and use of this software in source and binary forms, 
with or without modification, are permitted provided that the 
following conditions are met:

* Redistributions of source code must retain the above
  copyright notice, this list of conditions and the
  following disclaimer.

* Redistributions in binary form must reproduce the above
  copyright notice, this list of conditions and the
  following disclaimer in the documentation and/or other
  materials provided with the distribution.

* Neither the name of the assimp team, nor the names of its
  contributors may be used to endorse or promote products
  derived from this software without specific prior
  written permission of the assimp team.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS 
"AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT 
LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT 
OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT 
LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY 
THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT 
(INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE 
OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

----------------------------------------------------------------------*/

package assimp;
import com.sun.jna.*;
public class AI {
	public static final int aiProcess_CalcTangentSpace = 0x1;
	public static final int aiProcess_JoinIdenticalVertices = 0x2;
	public static final int aiProcess_MakeLeftHanded = 0x4;
	public static final int aiProcess_Triangulate = 0x8;
	public static final int aiProcess_RemoveComponent = 0x10;
	public static final int aiProcess_GenNormals = 0x20;
	public static final int aiProcess_GenSmoothNormals = 0x40;
	public static final int aiProcess_SplitLargeMeshes = 0x80;
	public static final int aiProcess_PreTransformVertices = 0x100;
	public static final int aiProcess_LimitBoneWeights = 0x200;
	public static final int aiProcess_ValidateDataStructure = 0x400;
	public static final int aiProcess_ImproveCacheLocality = 0x800;
	public static final int aiProcess_RemoveRedundantMaterials = 0x1000;
	public static final int aiProcess_FixInfacingNormals = 0x2000;
	public static final int aiProcess_SortByPType = 0x8000;
	public static final int aiProcess_FindDegenerates = 0x10000;
	public static final int aiProcess_FindInvalidData = 0x20000;
	public static final int aiProcess_GenUVCoords = 0x40000;
	public static final int aiProcess_TransformUVCoords = 0x80000;
	public static final int aiProcess_FindInstances = 0x100000;
	public static final int aiProcess_OptimizeMeshes  = 0x200000; 
	public static final int aiProcess_OptimizeGraph  = 0x400000; 
	public static final int aiProcess_FlipUVs = 0x800000; 
	public static final int aiProcess_FlipWindingOrder  = 0x1000000;
	public static final int aiProcess_SplitByBoneCount  = 0x2000000;
	public static final int aiProcess_Debone  = 0x4000000;
	// aiProcess_GenEntityMeshes = 0x100000,
	// aiProcess_OptimizeAnimations = 0x200000
	// aiProcess_FixTexturePaths = 0x200000
    
    public static final int aiPrimitiveType_POINT       = 0x1;
    public static final int aiPrimitiveType_LINE        = 0x2;
    public static final int aiPrimitiveType_TRIANGLE    = 0x4;
    public static final int aiPrimitiveType_POLYGON     = 0x8;

    public static final int  aiProcess_ConvertToLeftHanded = 
        aiProcess_MakeLeftHanded     | 
        aiProcess_FlipUVs            | 
        aiProcess_FlipWindingOrder;

    public static final int  aiProcessPreset_TargetRealtime_Fast = 
        aiProcess_CalcTangentSpace		|  
        aiProcess_GenNormals			|  
        aiProcess_JoinIdenticalVertices |  
        aiProcess_Triangulate			|  
        aiProcess_GenUVCoords           |  
        aiProcess_SortByPType;


    public static final int  aiProcessPreset_TargetRealtime_Quality = 
        aiProcess_CalcTangentSpace				|  
        aiProcess_GenSmoothNormals				|  
        aiProcess_JoinIdenticalVertices			|  
        aiProcess_ImproveCacheLocality			|  
        aiProcess_LimitBoneWeights				|  
        aiProcess_RemoveRedundantMaterials      |  
        aiProcess_SplitLargeMeshes				|  
        aiProcess_Triangulate					|  
        aiProcess_GenUVCoords                   |  
        aiProcess_SortByPType                   |  
        aiProcess_FindDegenerates               |  
        aiProcess_FindInvalidData ;

    public static final int  aiProcessPreset_TargetRealtime_MaxQuality = 
        aiProcessPreset_TargetRealtime_Quality   |  
        aiProcess_FindInstances                  |  
        aiProcess_ValidateDataStructure          |  
        aiProcess_OptimizeMeshes      ;             


    public static final byte[] AI_MATKEY_NAME = "?mat.name\0".getBytes();
    public static final byte[] AI_MATKEY_TWOSIDED = "$mat.twosided\0".getBytes();
    public static final byte[] AI_MATKEY_SHADING_MODEL = "$mat.shadingm\0".getBytes();
    public static final byte[] AI_MATKEY_ENABLE_WIREFRAME = "$mat.wireframe\0".getBytes();
    public static final byte[] AI_MATKEY_BLEND_FUNC = "$mat.blend\0".getBytes();
    public static final byte[] AI_MATKEY_OPACITY = "$mat.opacity\0".getBytes();
    public static final byte[] AI_MATKEY_BUMPSCALING = "$mat.bumpscaling\0".getBytes();
    public static final byte[] AI_MATKEY_SHININESS = "$mat.shininess\0".getBytes();
    public static final byte[] AI_MATKEY_REFLECTIVITY = "$mat.reflectivity\0".getBytes();
    public static final byte[] AI_MATKEY_SHININESS_STRENGTH = "$mat.shinpercent\0".getBytes();
    public static final byte[] AI_MATKEY_REFRACTI = "$mat.refracti\0".getBytes();
    public static final byte[] AI_MATKEY_COLOR_DIFFUSE = "$clr.diffuse\0".getBytes();
    public static final byte[] AI_MATKEY_COLOR_AMBIENT = "$clr.ambient\0".getBytes();
    public static final byte[] AI_MATKEY_COLOR_SPECULAR = "$clr.specular\0".getBytes();
    public static final byte[] AI_MATKEY_COLOR_EMISSIVE = "$clr.emissive\0".getBytes();
    public static final byte[] AI_MATKEY_COLOR_TRANSPARENT = "$clr.transparent\0".getBytes();
    public static final byte[] AI_MATKEY_COLOR_REFLECTIVE = "$clr.reflective\0".getBytes();
    public static final byte[] AI_MATKEY_GLOBAL_BACKGROUND_IMAGE = "?bg.global\0".getBytes();
    
    public static final byte[]  _AI_MATKEY_TEXTURE_BASE   =              "$tex.file\0".getBytes();
    public static final byte[]  _AI_MATKEY_UVWSRC_BASE    =              "$tex.uvwsrc\0".getBytes();
    public static final byte[]  _AI_MATKEY_TEXOP_BASE     =              "$tex.op\0".getBytes();
    public static final byte[]  _AI_MATKEY_MAPPING_BASE   =              "$tex.mapping\0".getBytes();
    public static final byte[]  _AI_MATKEY_TEXBLEND_BASE  =              "$tex.blend\0".getBytes();
    public static final byte[]  _AI_MATKEY_MAPPINGMODE_U_BASE =  "$tex.mapmodeu\0".getBytes();
    public static final byte[]  _AI_MATKEY_MAPPINGMODE_V_BASE =  "$tex.mapmodev\0".getBytes();
    public static final byte[]  _AI_MATKEY_TEXMAP_AXIS_BASE   =          "$tex.mapaxis\0".getBytes();
    public static final byte[]  _AI_MATKEY_UVTRANSFORM_BASE   =          "$tex.uvtrafo\0".getBytes();
    public static final byte[]  _AI_MATKEY_TEXFLAGS_BASE      =          "$tex.flags\0".getBytes();
    
    public static final byte[]  AI_MATKEY_TEXTURE_BASE   =              "$tex.file\0".getBytes();
    public static final byte[]  AI_MATKEY_UVWSRC_BASE    =              "$tex.uvwsrc\0".getBytes();
    public static final byte[]  AI_MATKEY_TEXOP_BASE     =              "$tex.op\0".getBytes();
    public static final byte[]  AI_MATKEY_MAPPING_BASE   =              "$tex.mapping\0".getBytes();
    public static final byte[]  AI_MATKEY_TEXBLEND_BASE  =              "$tex.blend\0".getBytes();
    public static final byte[]  AI_MATKEY_MAPPINGMODE_U_BASE =  "$tex.mapmodeu\0".getBytes();
    public static final byte[]  AI_MATKEY_MAPPINGMODE_V_BASE =  "$tex.mapmodev\0".getBytes();
    public static final byte[]  AI_MATKEY_TEXMAP_AXIS_BASE   =          "$tex.mapaxis\0".getBytes();
    public static final byte[]  AI_MATKEY_UVTRANSFORM_BASE   =          "$tex.uvtrafo\0".getBytes();
    public static final byte[]  AI_MATKEY_TEXFLAGS_BASE      =          "$tex.flags\0".getBytes();


    public static final int aiPTI_Float   = 0x1;
    public static final int aiPTI_String  = 0x3;
    public static final int aiPTI_Integer = 0x4;
    public static final int aiPTI_Buffer  = 0x5;

    public static final int 	aiTextureOp_Multiply = 0x0;
    public static final int 	aiTextureOp_Add = 0x1;
    public static final int 	aiTextureOp_Subtract = 0x2;
    public static final int 	aiTextureOp_Divide = 0x3;
    public static final int 	aiTextureOp_SmoothAdd = 0x4;
    public static final int 	aiTextureOp_SignedAdd = 0x5;
    public static final int     aiTextureMapMode_Wrap = 0x0;
    public static final int     aiTextureMapMode_Clamp = 0x1;
    public static final int     aiTextureMapMode_Decal = 0x3;
    public static final int     aiTextureMapMode_Mirror = 0x2;
    public static final int     aiTextureMapping_UV = 0x0;
    public static final int     aiTextureMapping_SPHERE = 0x1;
    public static final int     aiTextureMapping_CYLINDER = 0x2;
    public static final int     aiTextureMapping_BOX = 0x3;
    public static final int     aiTextureMapping_PLANE = 0x4;
    public static final int     aiTextureMapping_OTHER = 0x5;
    public static final int 	aiTextureType_NONE = 0x0;
    public static final int     aiTextureType_DIFFUSE = 0x1;
    public static final int     aiTextureType_SPECULAR = 0x2;
    public static final int     aiTextureType_AMBIENT = 0x3;
    public static final int     aiTextureType_EMISSIVE = 0x4;
    public static final int     aiTextureType_HEIGHT = 0x5;
    public static final int     aiTextureType_NORMALS = 0x6;
    public static final int     aiTextureType_SHININESS = 0x7;
    public static final int     aiTextureType_OPACITY = 0x8;
    public static final int     aiTextureType_DISPLACEMENT = 0x9;
    public static final int     aiTextureType_LIGHTMAP = 0xA;
    public static final int     aiTextureType_REFLECTION = 0xB;
    public static final int     aiShadingMode_Flat = 0x1;
    public static final int     aiShadingMode_Gouraud =	0x2;
    public static final int     aiShadingMode_Phong = 0x3;
    public static final int     aiShadingMode_Blinn	= 0x4;
    public static final int     aiShadingMode_Toon = 0x5;
    public static final int     aiShadingMode_OrenNayar = 0x6;
    public static final int     aiShadingMode_Minnaert = 0x7;
    public static final int     aiShadingMode_CookTorrance = 0x8;
    public static final int     aiShadingMode_NoShading = 0x9;
    public static final int     aiShadingMode_Fresnel = 0xa;
    public static final int 	aiTextureFlags_Invert = 0x1;
    public static final int 	aiTextureFlags_UseAlpha = 0x2;
    public static final int 	aiTextureFlags_IgnoreAlpha = 0x4;
    public static final int 	aiBlendMode_Default = 0x0;
    public static final int 	aiBlendMode_Additive = 0x1;

    public static final int aiDefaultLogStream_FILE = 0x1;
    public static final int aiDefaultLogStream_STDOUT = 0x2;
    public static final int aiDefaultLogStream_STDERR = 0x4;
    public static final int aiDefaultLogStream_DEBUGGER = 0x8;

    public static final int aiComponent_NORMALS = 0x2;
    public static final int aiComponent_TANGENTS_AND_BITANGENTS = 0x4;
    public static final int aiComponent_COLORS = 0x8;
    public static final int aiComponent_TEXCOORDS = 0x10;
    public static final int aiComponent_BONEWEIGHTS = 0x20;
    public static final int aiComponent_ANIMATIONS = 0x40;
    public static final int aiComponent_TEXTURES = 0x80;
    public static final int aiComponent_LIGHTS = 0x100;
    public static final int aiComponent_CAMERAS = 0x200;
    public static final int aiComponent_MESHES = 0x400;
    public static final int aiComponent_MATERIALS = 0x800;
    public static final int  AI_SLM_DEFAULT_MAX_TRIANGLES= 1000000;
    public static final int  AI_SLM_DEFAULT_MAX_VERTICES= 1000000;
    public static final int  AI_LMW_MAX_WEIGHTS =0x4;
    public static final float  AI_DEBONE_THRESHOLD =1.0f;
    public static final int  PP_ICL_PTCACHE_SIZE =12;
    public static final int  AI_UVTRAFO_SCALING= 0x1;
    public static final int  AI_UVTRAFO_ROTATION =0x2;
    public static final int  AI_UVTRAFO_TRANSLATION =0x4;
    public static final int  AI_UVTRAFO_ALL =(AI_UVTRAFO_SCALING | AI_UVTRAFO_ROTATION | AI_UVTRAFO_TRANSLATION);
    public static final int  aiComponent_COLORSn(int n) { return (1  << (n+20 )); }
    public static final int  aiComponent_TEXCOORDSn(int n){ return (1  << (n+25 )); }
    public static final byte[]  AI_CONFIG_PP_RVC_FLAGS = "PP_RVC_FLAGS\0".getBytes();
    public static final byte[]  AI_CONFIG_PP_SBP_REMOVE = "PP_SBP_REMOVE\0".getBytes();
    public static final byte[]  AI_CONFIG_PP_FID_ANIM_ACCURACY = "PP_FID_ANIM_ACCURACY\0".getBytes();
    public static final byte[]  AI_CONFIG_PP_TUV_EVALUATE = "PP_TUV_EVALUATE\0".getBytes();
    public static final byte[]  AI_CONFIG_FAVOUR_SPEED = "FAVOUR_SPEED\0".getBytes();
    public static final byte[]  AI_CONFIG_IMPORT_FBX_READ_ALL_GEOMETRY_LAYERS = "IMPORT_FBX_READ_ALL_GEOMETRY_LAYERS\0".getBytes();
    public static final byte[]  AI_CONFIG_IMPORT_FBX_READ_ALL_MATERIALS = "IMPORT_FBX_READ_ALL_MATERIALS\0".getBytes();
    public static final byte[]  AI_CONFIG_IMPORT_FBX_READ_MATERIALS = "IMPORT_FBX_READ_MATERIALS\0".getBytes();
    public static final byte[]  AI_CONFIG_IMPORT_FBX_READ_CAMERAS = "IMPORT_FBX_READ_CAMERAS\0".getBytes();
    public static final byte[]  AI_CONFIG_IMPORT_FBX_READ_LIGHTS = "IMPORT_FBX_READ_LIGHTS\0".getBytes();
    public static final byte[]  AI_CONFIG_IMPORT_FBX_READ_ANIMATIONS = "IMPORT_FBX_READ_ANIMATIONS\0".getBytes();
    public static final byte[]  AI_CONFIG_IMPORT_FBX_STRICT_MODE = "IMPORT_FBX_STRICT_MODE\0".getBytes();
    public static final byte[]  AI_CONFIG_IMPORT_FBX_PRESERVE_PIVOTS = "IMPORT_FBX_PRESERVE_PIVOTS\0".getBytes();
    public static final byte[]  AI_CONFIG_IMPORT_FBX_OPTIMIZE_EMPTY_ANIMATION_CURVES = "IMPORT_FBX_OPTIMIZE_EMPTY_ANIMATION_CURVES\0".getBytes();
    public static final byte[]  AI_CONFIG_IMPORT_GLOBAL_KEYFRAME = "IMPORT_GLOBAL_KEYFRAME\0".getBytes();
    public static final byte[]  AI_CONFIG_IMPORT_MD3_KEYFRAME = "IMPORT_MD3_KEYFRAME\0".getBytes();
    public static final byte[]  AI_CONFIG_IMPORT_MD2_KEYFRAME = "IMPORT_MD2_KEYFRAME\0".getBytes();
    public static final byte[]  AI_CONFIG_IMPORT_MDL_KEYFRAME = "IMPORT_MDL_KEYFRAME\0".getBytes();
    public static final byte[]  AI_CONFIG_IMPORT_MDC_KEYFRAME = "IMPORT_MDC_KEYFRAME\0".getBytes();
    public static final byte[]  AI_CONFIG_IMPORT_SMD_KEYFRAME = "IMPORT_SMD_KEYFRAME\0".getBytes();
    public static final byte[]  AI_CONFIG_IMPORT_UNREAL_KEYFRAME = "IMPORT_UNREAL_KEYFRAME\0".getBytes();
    public static final byte[]  AI_CONFIG_IMPORT_AC_SEPARATE_BFCULL = "IMPORT_AC_SEPARATE_BFCULL\0".getBytes();
    public static final byte[]  AI_CONFIG_IMPORT_AC_EVAL_SUBDIVISION = "IMPORT_AC_EVAL_SUBDIVISION\0".getBytes();
    public static final byte[]  AI_CONFIG_IMPORT_UNREAL_HANDLE_FLAGS = "UNREAL_HANDLE_FLAGS\0".getBytes();
    public static final byte[]  AI_CONFIG_IMPORT_TER_MAKE_UVS = "IMPORT_TER_MAKE_UVS\0".getBytes();
    public static final byte[]  AI_CONFIG_IMPORT_ASE_RECONSTRUCT_NORMALS = "IMPORT_ASE_RECONSTRUCT_NORMALS\0".getBytes();
    public static final byte[]  AI_CONFIG_IMPORT_MD3_HANDLE_MULTIPART = "IMPORT_MD3_HANDLE_MULTIPART\0".getBytes();
    public static final byte[]  AI_CONFIG_IMPORT_MD3_SKIN_NAME = "IMPORT_MD3_SKIN_NAME\0".getBytes();
    public static final byte[]  AI_CONFIG_IMPORT_MD3_SHADER_SRC = "IMPORT_MD3_SHADER_SRC\0".getBytes();
    public static final byte[]  AI_CONFIG_IMPORT_LWO_ONE_LAYER_ONLY = "IMPORT_LWO_ONE_LAYER_ONLY\0".getBytes();
    public static final byte[]  AI_CONFIG_IMPORT_MD5_NO_ANIM_AUTOLOAD = "IMPORT_MD5_NO_ANIM_AUTOLOAD\0".getBytes();
    public static final byte[]  AI_CONFIG_IMPORT_LWS_ANIM_START = "IMPORT_LWS_ANIM_START\0".getBytes();
    public static final byte[]  AI_CONFIG_IMPORT_LWS_ANIM_END = "IMPORT_LWS_ANIM_END\0".getBytes();
    public static final byte[]  AI_CONFIG_IMPORT_IRR_ANIM_FPS = "IMPORT_IRR_ANIM_FPS\0".getBytes();
    public static final byte[]  AI_CONFIG_IMPORT_OGRE_MATERIAL_FILE = "IMPORT_OGRE_MATERIAL_FILE\0".getBytes();
    public static final byte[]  AI_CONFIG_IMPORT_OGRE_TEXTURETYPE_FROM_FILENAME = "IMPORT_OGRE_TEXTURETYPE_FROM_FILENAME\0".getBytes();
    public static final byte[]  AI_CONFIG_IMPORT_IFC_SKIP_SPACE_REPRESENTATIONS = "IMPORT_IFC_SKIP_SPACE_REPRESENTATIONS\0".getBytes();
    public static final byte[]  AI_CONFIG_IMPORT_IFC_SKIP_CURVE_REPRESENTATIONS = "IMPORT_IFC_SKIP_CURVE_REPRESENTATIONS\0".getBytes();
    public static final byte[]  AI_CONFIG_IMPORT_IFC_CUSTOM_TRIANGULATION = "IMPORT_IFC_CUSTOM_TRIANGULATION\0".getBytes();
    public static final byte[]  AI_CONFIG_IMPORT_COLLADA_IGNORE_UP_DIRECTION = "IMPORT_COLLADA_IGNORE_UP_DIRECTION\0".getBytes();
    public static final byte[]  AI_CONFIG_PP_DB_ALL_OR_NONE = "PP_DB_ALL_OR_NONE\0".getBytes();
    public static final byte[]  AI_CONFIG_PP_ICL_PTCACHE_SIZE = "PP_ICL_PTCACHE_SIZE\0".getBytes();
    public static final byte[]  AI_CONFIG_PP_DB_THRESHOLD = "PP_DB_THRESHOLD\0".getBytes();
    public static final byte[]  AI_CONFIG_PP_LBW_MAX_WEIGHTS = "PP_LBW_MAX_WEIGHTS\0".getBytes();
    public static final byte[]  AI_CONFIG_PP_SLM_VERTEX_LIMIT = "PP_SLM_VERTEX_LIMIT\0".getBytes();
    public static final byte[]  AI_CONFIG_GLOB_MEASURE_TIME = "GLOB_MEASURE_TIME\0".getBytes();
    public static final byte[]  AI_CONFIG_IMPORT_NO_SKELETON_MESHES = "IMPORT_NO_SKELETON_MESHES\0".getBytes();
    public static final byte[]  AI_CONFIG_PP_SBBC_MAX_BONES = "PP_SBBC_MAX_BONES\0".getBytes();
    public static final byte[]  AI_CONFIG_PP_CT_MAX_SMOOTHING_ANGLE = "PP_CT_MAX_SMOOTHING_ANGLE\0".getBytes();
    public static final byte[]  AI_CONFIG_PP_CT_TEXTURE_CHANNEL_INDEX = "PP_CT_TEXTURE_CHANNEL_INDEX\0".getBytes();
    public static final byte[]  AI_CONFIG_PP_GSN_MAX_SMOOTHING_ANGLE = "PP_GSN_MAX_SMOOTHING_ANGLE\0".getBytes();
    public static final byte[]  AI_CONFIG_IMPORT_MDL_COLORMAP = "IMPORT_MDL_COLORMAP\0".getBytes();
    public static final byte[]  AI_CONFIG_PP_RRM_EXCLUDE_LIST = "PP_RRM_EXCLUDE_LIST\0".getBytes();
    public static final byte[]  AI_CONFIG_PP_PTV_KEEP_HIERARCHY = "PP_PTV_KEEP_HIERARCHY\0".getBytes();
    public static final byte[]  AI_CONFIG_PP_PTV_NORMALIZE = "PP_PTV_NORMALIZE\0".getBytes();
    public static final byte[]  AI_CONFIG_PP_PTV_ADD_ROOT_TRANSFORMATION = "PP_PTV_ADD_ROOT_TRANSFORMATION\0".getBytes();
    public static final byte[]  AI_CONFIG_PP_PTV_ROOT_TRANSFORMATION = "PP_PTV_ROOT_TRANSFORMATION\0".getBytes();
    public static final byte[]  AI_CONFIG_PP_FD_REMOVE = "PP_FD_REMOVE\0".getBytes();
    public static final byte[]  AI_CONFIG_PP_OG_EXCLUDE_LIST = "PP_OG_EXCLUDE_LIST\0".getBytes();
    public static final byte[]  AI_CONFIG_PP_SLM_TRIANGLE_LIMIT = "PP_SLM_TRIANGLE_LIMIT\0".getBytes();



    public static int aiGetMaterialFloat(aiMaterial pMat,
        byte[] pKey, int type, int index, float[] pOut){
        return aiGetMaterialFloatArray(pMat, pKey, type, index, pOut, null);
    }
    public static int aiGetMaterialInteger(aiMaterial pMat,
        byte[] pKey, int type, int index, int[] pOut){
        return aiGetMaterialIntegerArray(pMat, pKey, type, index, pOut, null);
    }
    
    public static aiScene aiImportFile(String fname, int flags){
        return aiImportFile( (fname+"\0").getBytes(), flags);
    }
    
    public static aiScene aiImportFileExWithProperties( String fname, int flags, 
        Pointer pfs, aiPropertyStore props){
        return AI_.INSTANCE.aiImportFileExWithProperties( (fname+"\0").getBytes(), flags, pfs, props );
    }
    
    public static String aiGetMaterialString(aiMaterial M, byte[] key ){
        aiString stir = new aiString();
        int rv = aiGetMaterialString(M, key, 0, 0, stir);
        if(rv<0)
            return null;
        stir.read();
        String stirs = stir.toString();
        return stirs;
    }
        
    public static String aiGetMaterialString(aiMaterial M, byte[] key, int type, int idx){
        aiString stir = new aiString();
        int rv = aiGetMaterialString(M, key, type, idx, stir);
        if(rv<0)
            return null;
        stir.read();
        String stirs = stir.toString();
        return stirs;
    }
    
    
    public static aiLogStream aiGetPredefinedLogStream(int which, String file){
        return aiGetPredefinedLogStream(which,(file+"\0").getBytes());
    }
    
    public static String aiGetLegalString(){
        Pointer p = AI_.INSTANCE.aiGetLegalString();
        return pointerToString(p);
    }
    public static String aiGetErrorString(){
        Pointer p = AI_.INSTANCE.aiGetErrorString();
        return pointerToString(p);
    }
    
    private static String pointerToString(Pointer p){
        String s="";
        for(int i=0; ; i++){
            byte b = p.getByte(i);
            if(b==0)
                return s;
            s += (char)b;
        }
    }
    
    
    public static long aiGetExportFormatCount(){
        return AI_.INSTANCE.aiGetExportFormatCount();
    }
    public static aiExportFormatDesc aiGetExportFormatDescription(long pIndex){
        return AI_.INSTANCE.aiGetExportFormatDescription(pIndex);
    }
    public static void aiCopyScene(aiScene pIn,Pointer pOut){
        AI_.INSTANCE.aiCopyScene(pIn,pOut);
    }
    public static void aiFreeScene(aiScene pIn){
        AI_.INSTANCE.aiFreeScene(pIn);
    }
    public static int aiExportScene(aiScene pScene,byte[] pFormatId,byte[] pFileName,int pPreprocessing){
        return AI_.INSTANCE.aiExportScene(pScene,pFormatId,pFileName,pPreprocessing);
    }
    public static aiExportDataBlob aiExportSceneToBlob(aiScene pScene,byte[] pFormatId,int pPreprocessing){
        return AI_.INSTANCE.aiExportSceneToBlob(pScene,pFormatId,pPreprocessing);
    }
    public static void aiReleaseExportBlob(aiExportDataBlob pData){
        AI_.INSTANCE.aiReleaseExportBlob(pData);
    }
    public static aiScene aiImportFile(byte[] pFile,int pFlags){
        return AI_.INSTANCE.aiImportFile(pFile,pFlags);
    }
    public static aiScene aiImportFileFromMemory(byte[] pBuffer,int pLength,int pFlags,byte[] pHint){
        return AI_.INSTANCE.aiImportFileFromMemory(pBuffer,pLength,pFlags,pHint);
    }
    public static aiScene aiImportFileFromMemoryWithProperties(byte[] pBuffer,int pLength,int pFlags,byte[] pHint,aiPropertyStore pProps){
        return AI_.INSTANCE.aiImportFileFromMemoryWithProperties(pBuffer,pLength,pFlags,pHint,pProps);
    }
    public static aiScene aiApplyPostProcessing(aiScene pScene,int pFlags){
        return AI_.INSTANCE.aiApplyPostProcessing(pScene,pFlags);
    }
    public static aiLogStream.ByValue aiGetPredefinedLogStream(int pStreams,byte[] file){
        return AI_.INSTANCE.aiGetPredefinedLogStream(pStreams,file);
    }
    public static void aiAttachLogStream(aiLogStream stream){
        AI_.INSTANCE.aiAttachLogStream(stream);
    }
    public static void aiEnableVerboseLogging(int d){
        AI_.INSTANCE.aiEnableVerboseLogging(d);
    }
    public static int aiDetachLogStream(aiLogStream stream){
        return AI_.INSTANCE.aiDetachLogStream(stream);
    }
    public static void aiDetachAllLogStreams(){
        AI_.INSTANCE.aiDetachAllLogStreams();
    }
    public static void aiReleaseImport(aiScene pScene){
        AI_.INSTANCE.aiReleaseImport(pScene);
    }
    public static int aiIsExtensionSupported(byte[] szExtension){
        return AI_.INSTANCE.aiIsExtensionSupported(szExtension);
    }
    public static void aiGetExtensionList(aiString szOut){
        AI_.INSTANCE.aiGetExtensionList(szOut);
    }
    public static void aiGetMemoryRequirements(aiScene pIn,aiMemoryInfo in){
        AI_.INSTANCE.aiGetMemoryRequirements(pIn,in);
    }
    public static aiPropertyStore aiCreatePropertyStore(){
        return AI_.INSTANCE.aiCreatePropertyStore();
    }
    public static void aiReleasePropertyStore(aiPropertyStore p){
        AI_.INSTANCE.aiReleasePropertyStore(p);
    }
    public static void aiSetImportPropertyInteger(aiPropertyStore store,byte[] szName,int value){
        AI_.INSTANCE.aiSetImportPropertyInteger(store,szName,value);
    }
    public static void aiSetImportPropertyFloat(aiPropertyStore store,byte[] szName,float value){
        AI_.INSTANCE.aiSetImportPropertyFloat(store,szName,value);
    }
    public static void aiSetImportPropertyString(aiPropertyStore store,byte[] szName,aiString st){
        AI_.INSTANCE.aiSetImportPropertyString(store,szName,st);
    }
    public static void aiSetImportPropertyMatrix(aiPropertyStore store,byte[] szName,aiMatrix4x4 mat){
        AI_.INSTANCE.aiSetImportPropertyMatrix(store,szName,mat);
    }
    public static void aiCreateQuaternionFromMatrix(aiQuaternion quat,aiMatrix3x3 mat){
        AI_.INSTANCE.aiCreateQuaternionFromMatrix(quat,mat);
    }
    public static void aiDecomposeMatrix(aiMatrix4x4 mat,aiVector3D scaling,aiQuaternion rotation,aiVector3D position){
        AI_.INSTANCE.aiDecomposeMatrix(mat,scaling,rotation,position);
    }
    public static void aiTransposeMatrix4(aiMatrix4x4 mat){
        AI_.INSTANCE.aiTransposeMatrix4(mat);
    }
    public static void aiTransposeMatrix3(aiMatrix3x3 mat){
        AI_.INSTANCE.aiTransposeMatrix3(mat);
    }
    public static void aiTransformVecByMatrix3(aiVector3D vec,aiMatrix3x3 mat){
        AI_.INSTANCE.aiTransformVecByMatrix3(vec,mat);
    }
    public static void aiTransformVecByMatrix4(aiVector3D vec,aiMatrix4x4 mat){
        AI_.INSTANCE.aiTransformVecByMatrix4(vec,mat);
    }
    public static void aiMultiplyMatrix4(aiMatrix4x4 dst,aiMatrix4x4 src){
        AI_.INSTANCE.aiMultiplyMatrix4(dst,src);
    }
    public static void aiMultiplyMatrix3(aiMatrix3x3 dst,aiMatrix3x3 src){
        AI_.INSTANCE.aiMultiplyMatrix3(dst,src);
    }
    public static void aiIdentityMatrix3(aiMatrix3x3 mat){
        AI_.INSTANCE.aiIdentityMatrix3(mat);
    }
    public static void aiIdentityMatrix4(aiMatrix4x4 mat){
        AI_.INSTANCE.aiIdentityMatrix4(mat);
    }
    public static int aiGetMaterialProperty(aiMaterial pMat,byte[] pKey,int type,int index,Pointer pPropOut){
        return AI_.INSTANCE.aiGetMaterialProperty(pMat,pKey,type,index,pPropOut);
    }
    public static int aiGetMaterialFloatArray(aiMaterial pMat,byte[] pKey,int type,int index,float[] pOut,int[] pMax){
        return AI_.INSTANCE.aiGetMaterialFloatArray(pMat,pKey,type,index,pOut,pMax);
    }
    public static int aiGetMaterialIntegerArray(aiMaterial pMat,byte[] pKey,int type,int index,int[] pOut,int[] pMax){
        return AI_.INSTANCE.aiGetMaterialIntegerArray(pMat,pKey,type,index,pOut,pMax);
    }
    public static int aiGetMaterialColor(aiMaterial pMat,byte[] pKey,int type,int index,aiColor4D pOut){
        return AI_.INSTANCE.aiGetMaterialColor(pMat,pKey,type,index,pOut);
    }
    public static int aiGetMaterialUVTransform(aiMaterial pMat,byte[] pKey,int type,int index,aiUVTransform pOut){
        return AI_.INSTANCE.aiGetMaterialUVTransform(pMat,pKey,type,index,pOut);
    }
    public static int aiGetMaterialString(aiMaterial pMat,byte[] pKey,int type,int index,aiString pOut){
        return AI_.INSTANCE.aiGetMaterialString(pMat,pKey,type,index,pOut);
    }
    public static int aiGetMaterialTextureCount(aiMaterial pMat,int type){
        return AI_.INSTANCE.aiGetMaterialTextureCount(pMat,type);
    }
    public static int aiGetMaterialTexture(aiMaterial mat,int type,int index,aiString path,int[] mapping,int[] uvindex,float[] blend,int[] op,int[] mapmode,int[] flags){
        return AI_.INSTANCE.aiGetMaterialTexture(mat,type,index,path,mapping,uvindex,blend,op,mapmode,flags);
    }
    public static int aiGetVersionMinor(){
        return AI_.INSTANCE.aiGetVersionMinor();
    }
    public static int aiGetVersionMajor(){
        return AI_.INSTANCE.aiGetVersionMajor();
    }
    public static int aiGetVersionRevision(){
        return AI_.INSTANCE.aiGetVersionRevision();
    }
    public static int aiGetCompileFlags(){
        return AI_.INSTANCE.aiGetCompileFlags();
    }

}
