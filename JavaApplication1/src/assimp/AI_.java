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
public interface AI_ extends Library {
    AI_ INSTANCE = (AI_)Native.loadLibrary("assimp",AI_.class);
    
    Pointer aiGetLegalString();
    Pointer aiGetErrorString();
    aiScene aiImportFileExWithProperties(byte[] pBuffer, int flags, Pointer pfs, aiPropertyStore props);
    
    long aiGetExportFormatCount();
    aiExportFormatDesc aiGetExportFormatDescription(long pIndex);
    void aiCopyScene(aiScene pIn,Pointer pOut);
    void aiFreeScene(aiScene pIn);
    int aiExportScene(aiScene pScene,byte[] pFormatId,byte[] pFileName,int pPreprocessing);
    aiExportDataBlob aiExportSceneToBlob(aiScene pScene,byte[] pFormatId,int pPreprocessing);
    void aiReleaseExportBlob(aiExportDataBlob pData);
    aiScene aiImportFile(byte[] pFile,int pFlags);
    aiScene aiImportFileFromMemory(byte[] pBuffer,int pLength,int pFlags,byte[] pHint);
    aiScene aiImportFileFromMemoryWithProperties(byte[] pBuffer,int pLength,int pFlags,byte[] pHint,aiPropertyStore pProps);
    aiScene aiApplyPostProcessing(aiScene pScene,int pFlags);
    aiLogStream.ByValue aiGetPredefinedLogStream(int pStreams,byte[] file);
    void aiAttachLogStream(aiLogStream stream);
    void aiEnableVerboseLogging(int d);
    int aiDetachLogStream(aiLogStream stream);
    void aiDetachAllLogStreams();
    void aiReleaseImport(aiScene pScene);
    int aiIsExtensionSupported(byte[] szExtension);
    void aiGetExtensionList(aiString szOut);
    void aiGetMemoryRequirements(aiScene pIn,aiMemoryInfo in);
    aiPropertyStore aiCreatePropertyStore();
    void aiReleasePropertyStore(aiPropertyStore p);
    void aiSetImportPropertyInteger(aiPropertyStore store,byte[] szName,int value);
    void aiSetImportPropertyFloat(aiPropertyStore store,byte[] szName,float value);
    void aiSetImportPropertyString(aiPropertyStore store,byte[] szName,aiString st);
    void aiSetImportPropertyMatrix(aiPropertyStore store,byte[] szName,aiMatrix4x4 mat);
    void aiCreateQuaternionFromMatrix(aiQuaternion quat,aiMatrix3x3 mat);
    void aiDecomposeMatrix(aiMatrix4x4 mat,aiVector3D scaling,aiQuaternion rotation,aiVector3D position);
    void aiTransposeMatrix4(aiMatrix4x4 mat);
    void aiTransposeMatrix3(aiMatrix3x3 mat);
    void aiTransformVecByMatrix3(aiVector3D vec,aiMatrix3x3 mat);
    void aiTransformVecByMatrix4(aiVector3D vec,aiMatrix4x4 mat);
    void aiMultiplyMatrix4(aiMatrix4x4 dst,aiMatrix4x4 src);
    void aiMultiplyMatrix3(aiMatrix3x3 dst,aiMatrix3x3 src);
    void aiIdentityMatrix3(aiMatrix3x3 mat);
    void aiIdentityMatrix4(aiMatrix4x4 mat);
    int aiGetMaterialProperty(aiMaterial pMat,byte[] pKey,int type,int index,Pointer pPropOut);
    int aiGetMaterialFloatArray(aiMaterial pMat,byte[] pKey,int type,int index,float[] pOut,int[] pMax);
    int aiGetMaterialIntegerArray(aiMaterial pMat,byte[] pKey,int type,int index,int[] pOut,int[] pMax);
    int aiGetMaterialColor(aiMaterial pMat,byte[] pKey,int type,int index,aiColor4D pOut);
    int aiGetMaterialUVTransform(aiMaterial pMat,byte[] pKey,int type,int index,aiUVTransform pOut);
    int aiGetMaterialString(aiMaterial pMat,byte[] pKey,int type,int index,aiString pOut);
    int aiGetMaterialTextureCount(aiMaterial pMat,int type);
    int aiGetMaterialTexture(aiMaterial mat,int type,int index,aiString path,int[] mapping,int[] uvindex,float[] blend,int[] op,int[] mapmode,int[] flags);
    int aiGetVersionMinor();
    int aiGetVersionMajor();
    int aiGetVersionRevision();
    int aiGetCompileFlags();

}
