/** Written at SSU Spring 2017 */
package math3d;

/*
# some of these functions (individually noted) are based on code from TDL
# The TDL copyright is as follows:
# 
#  Copyright 2009, Google Inc.
#  All rights reserved.
# 
#  Redistribution and use in source and binary forms, with or without
#  modification, are permitted provided that the following conditions are
#  met:
# 
#      *  Redistributions of source code must retain the above copyright
#  notice, this list of conditions and the following disclaimer.
#      *  Redistributions in binary form must reproduce the above
#  copyright notice, this list of conditions and the following disclaimer
#  in the documentation and/or other materials provided with the
#  distribution.
#      *  Neither the name of Google Inc. nor the names of its
#  contributors may be used to endorse or promote products derived from
#  this software without specific prior written permission.
# 
#  THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
#  "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
#  LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
#  A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
#  OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
#  SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
#  LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
#  DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
#  THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
#  (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
#  OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
#
*/
public class mat2  implements Cloneable,Comparable< mat2 >{
    public float[] _M = new float[4];
    /** Default constructor */
    public mat2 (){}
    public mat2 ( double v0, double v1, double v2, double v3 ){
        this._M[ 0 ]= (float) v0 ;
        this._M[ 1 ]= (float) v1 ;
        this._M[ 2 ]= (float) v2 ;
        this._M[ 3 ]= (float) v3 ;
    }
    public mat2 ( vec2 v0, vec2 v1 ){
        this._M[ 0 ]= (float) v0.x;
        this._M[ 1 ]= (float) v0.y;
        this._M[ 2 ]= (float) v1.x;
        this._M[ 3 ]= (float) v1.y;
    }
    public String toString(){
        return String.format( "%f %f\n%f %f\n",_M[0],_M[1],_M[2],_M[3]);
    }
    public mat2 mul( mat2 o){
        mat2 R = new mat2 ();
        R._M[ 0 ] += this._M[ 0 ] * o._M[ 0 ];
        R._M[ 0 ] += this._M[ 1 ] * o._M[ 2 ];
        R._M[ 1 ] += this._M[ 0 ] * o._M[ 1 ];
        R._M[ 1 ] += this._M[ 1 ] * o._M[ 3 ];
        R._M[ 2 ] += this._M[ 2 ] * o._M[ 0 ];
        R._M[ 2 ] += this._M[ 3 ] * o._M[ 2 ];
        R._M[ 3 ] += this._M[ 2 ] * o._M[ 1 ];
        R._M[ 3 ] += this._M[ 3 ] * o._M[ 3 ];
        return R;
    }
    /**Compute the matrix-vector product.*/
    public vec2 mul( vec2 o){
        vec2  R = new vec2 ();
        R.x += this._M[ 0 ] * o.x ;
        R.x += this._M[ 1 ] * o.y ;
        R.y += this._M[ 4 ] * o.x ;
        R.y += this._M[ 5 ] * o.y ;
        return R;
    }
    public boolean equals( mat2 o){
        if( o == null ) return false;
        if( this._M[ 0 ] != o._M[ 0 ]) return false;
        if( this._M[ 1 ] != o._M[ 1 ]) return false;
        if( this._M[ 2 ] != o._M[ 2 ]) return false;
        if( this._M[ 3 ] != o._M[ 3 ]) return false;
        return true;
     }
    public int compareTo( mat2  o){
        if( o == null ) return 1;
        for(int i=0;i< 4 ;++i){
            if(this._M[i] < o._M[i]) return -1;
            if(this._M[i] > o._M[i]) return  1;
        }
        return 0;
    }
    public int hashCode(){
        int v=0;
        for(int i=0;i< 4 ;++i){
            v ^= Float.floatToIntBits(this._M[i]);
        }
        return v;
    }
    public void set(int r, int c, double v){
        _M[r* 2 +c]=(float)v;
    }
    public float get(int r, int c){
        return _M[r* 2 +c];
    }
    public static mat2 identity(){
        return new mat2 ( 1,0,0,1 );
    }
    public mat2 transpose(){
        return new mat2 (
            this.get( 0 , 0 ),this.get( 1 , 0 ),
            this.get( 0 , 1 ),this.get( 1 , 1 )
        );
    }
    public Object clone(){
        return new mat2 ( this._M[0],this._M[1],this._M[2],this._M[3] );
    }
}
