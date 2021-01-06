package assimp;
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
import com.sun.jna.*;
import java.util.*;
public class aiMesh extends Structure {
    public static class ByValue extends aiMesh implements Structure.ByValue{}
    public static class ByReference extends aiMesh implements Structure.ByReference{}
    public int mPrimitiveTypes;  //int mPrimitiveTypes
    public int mNumVertices;  //int mNumVertices
    public int mNumFaces;  //int mNumFaces
    public aiVector3D.ByReference mVertices;  //C_STRUCT aiVector3D* mVertices
    public aiVector3D.ByReference mNormals;  //C_STRUCT aiVector3D* mNormals
    public aiVector3D.ByReference mTangents;  //C_STRUCT aiVector3D* mTangents
    public aiVector3D.ByReference mBitangents;  //C_STRUCT aiVector3D* mBitangents
    public aiColor4D.ByReference[] mColors = new aiColor4D.ByReference[8]; //C_STRUCT aiColor4D* mColors[8]
    public aiVector3D.ByReference[] mTextureCoords = new aiVector3D.ByReference[8]; //C_STRUCT aiVector3D* mTextureCoords[8]
    public int[] mNumUVComponents = new int[8]; //int mNumUVComponents[8]
    public aiFace.ByReference mFaces;  //C_STRUCT aiFace* mFaces
    public int mNumBones;  //int mNumBones
    public Pointer mBones;  //C_STRUCT aiBone** mBones
    public int mMaterialIndex;  //int mMaterialIndex
    public aiString.ByValue mName;  //C_STRUCT aiString mName
    public int mNumAnimMeshes;  //int mNumAnimMeshes
    public Pointer mAnimMeshes;  //C_STRUCT aiAnimMesh** mAnimMeshes
    public List<String> getFieldOrder(){
        return Arrays.asList(new String[]{"mPrimitiveTypes","mNumVertices","mNumFaces","mVertices","mNormals","mTangents","mBitangents","mColors","mTextureCoords","mNumUVComponents","mFaces","mNumBones","mBones","mMaterialIndex","mName","mNumAnimMeshes","mAnimMeshes"});
    }

    private aiVector3D[] mVertices_;
    public aiVector3D[] getVertices(){
        if(mVertices_ == null )
            mVertices_ = (aiVector3D[]) this.mVertices.toArray(this.mNumVertices);
        return mVertices_;
    }
    private aiVector3D[] mNormals_;
    public aiVector3D[] getNormals(){
        if(mNormals_ == null )
            mNormals_ = (aiVector3D[]) this.mNormals.toArray(this.mNumVertices);
        return mNormals_;
    }
    private aiVector3D[] mTangents_;
    public aiVector3D[] getTangents(){
        if(mTangents_==null)
            mTangents_ = (aiVector3D[]) this.mTangents.toArray(this.mNumVertices);
        return mTangents_;
    }
    private aiVector3D[] mBitangents_;
    public aiVector3D[] getBitangents(){
        if(mBitangents_ == null )
            mBitangents_ = (aiVector3D[]) this.mBitangents.toArray(this.mNumVertices);
        return mBitangents_;
    }
    private aiVector3D[][] mTextureCoords_ = new aiVector3D[16][];
    public aiVector3D[] getTextureCoords(int k){
        if( this.mTextureCoords[k] == null )
            return null;
        if( mTextureCoords_[k] == null )
            mTextureCoords_[k] = (aiVector3D[])this.mTextureCoords[k].toArray(this.mNumVertices);
        return mTextureCoords_[k];
    }
    private aiFace[] mFaces_;
    public aiFace[] getFaces(){
        if( mFaces_ == null )
            mFaces_ = (aiFace[]) this.mFaces.toArray(this.mNumFaces);
        return mFaces_;
    }
    private aiBone[] mBones_;
    public aiBone[] getBones(){
        if( mBones_ == null ){
            Pointer[] BA = this.mBones.getPointerArray(0,this.mNumBones);
            aiBone[] ret = new aiBone[this.mNumBones];
            for(int i=0;i<BA.length;++i){
                ret[i] = (aiBone) Structure.newInstance(aiBone.class,BA[i]);
                ret[i].read();
            }
            mBones_ = ret;
        }
        return mBones_;
    }
    
}
