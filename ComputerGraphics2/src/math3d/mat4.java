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
public class mat4  implements Cloneable,Comparable< mat4 >{
    public float[] _M = new float[16];
    /** Default constructor */
    public mat4 (){}
    public mat4 ( double v0, double v1, double v2, double v3, double v4, double v5, double v6, double v7, double v8, double v9, double v10, double v11, double v12, double v13, double v14, double v15 ){
        this._M[ 0 ]= (float) v0 ;
        this._M[ 1 ]= (float) v1 ;
        this._M[ 2 ]= (float) v2 ;
        this._M[ 3 ]= (float) v3 ;
        this._M[ 4 ]= (float) v4 ;
        this._M[ 5 ]= (float) v5 ;
        this._M[ 6 ]= (float) v6 ;
        this._M[ 7 ]= (float) v7 ;
        this._M[ 8 ]= (float) v8 ;
        this._M[ 9 ]= (float) v9 ;
        this._M[ 10 ]= (float) v10 ;
        this._M[ 11 ]= (float) v11 ;
        this._M[ 12 ]= (float) v12 ;
        this._M[ 13 ]= (float) v13 ;
        this._M[ 14 ]= (float) v14 ;
        this._M[ 15 ]= (float) v15 ;
    }
    public mat4 ( vec4 v0, vec4 v1, vec4 v2, vec4 v3 ){
        this._M[ 0 ]= (float) v0.x;
        this._M[ 1 ]= (float) v0.y;
        this._M[ 2 ]= (float) v0.z;
        this._M[ 3 ]= (float) v0.w;
        this._M[ 4 ]= (float) v1.x;
        this._M[ 5 ]= (float) v1.y;
        this._M[ 6 ]= (float) v1.z;
        this._M[ 7 ]= (float) v1.w;
        this._M[ 8 ]= (float) v2.x;
        this._M[ 9 ]= (float) v2.y;
        this._M[ 10 ]= (float) v2.z;
        this._M[ 11 ]= (float) v2.w;
        this._M[ 12 ]= (float) v3.x;
        this._M[ 13 ]= (float) v3.y;
        this._M[ 14 ]= (float) v3.z;
        this._M[ 15 ]= (float) v3.w;
    }
    public String toString(){
        return String.format( "%f %f %f %f\n%f %f %f %f\n%f %f %f %f\n%f %f %f %f\n",_M[0],_M[1],_M[2],_M[3],_M[4],_M[5],_M[6],_M[7],_M[8],_M[9],_M[10],_M[11],_M[12],_M[13],_M[14],_M[15]);
    }
    public mat4 mul( mat4 o){
        mat4 R = new mat4 ();
        R._M[ 0 ] += this._M[ 0 ] * o._M[ 0 ];
        R._M[ 0 ] += this._M[ 1 ] * o._M[ 4 ];
        R._M[ 0 ] += this._M[ 2 ] * o._M[ 8 ];
        R._M[ 0 ] += this._M[ 3 ] * o._M[ 12 ];
        R._M[ 1 ] += this._M[ 0 ] * o._M[ 1 ];
        R._M[ 1 ] += this._M[ 1 ] * o._M[ 5 ];
        R._M[ 1 ] += this._M[ 2 ] * o._M[ 9 ];
        R._M[ 1 ] += this._M[ 3 ] * o._M[ 13 ];
        R._M[ 2 ] += this._M[ 0 ] * o._M[ 2 ];
        R._M[ 2 ] += this._M[ 1 ] * o._M[ 6 ];
        R._M[ 2 ] += this._M[ 2 ] * o._M[ 10 ];
        R._M[ 2 ] += this._M[ 3 ] * o._M[ 14 ];
        R._M[ 3 ] += this._M[ 0 ] * o._M[ 3 ];
        R._M[ 3 ] += this._M[ 1 ] * o._M[ 7 ];
        R._M[ 3 ] += this._M[ 2 ] * o._M[ 11 ];
        R._M[ 3 ] += this._M[ 3 ] * o._M[ 15 ];
        R._M[ 4 ] += this._M[ 4 ] * o._M[ 0 ];
        R._M[ 4 ] += this._M[ 5 ] * o._M[ 4 ];
        R._M[ 4 ] += this._M[ 6 ] * o._M[ 8 ];
        R._M[ 4 ] += this._M[ 7 ] * o._M[ 12 ];
        R._M[ 5 ] += this._M[ 4 ] * o._M[ 1 ];
        R._M[ 5 ] += this._M[ 5 ] * o._M[ 5 ];
        R._M[ 5 ] += this._M[ 6 ] * o._M[ 9 ];
        R._M[ 5 ] += this._M[ 7 ] * o._M[ 13 ];
        R._M[ 6 ] += this._M[ 4 ] * o._M[ 2 ];
        R._M[ 6 ] += this._M[ 5 ] * o._M[ 6 ];
        R._M[ 6 ] += this._M[ 6 ] * o._M[ 10 ];
        R._M[ 6 ] += this._M[ 7 ] * o._M[ 14 ];
        R._M[ 7 ] += this._M[ 4 ] * o._M[ 3 ];
        R._M[ 7 ] += this._M[ 5 ] * o._M[ 7 ];
        R._M[ 7 ] += this._M[ 6 ] * o._M[ 11 ];
        R._M[ 7 ] += this._M[ 7 ] * o._M[ 15 ];
        R._M[ 8 ] += this._M[ 8 ] * o._M[ 0 ];
        R._M[ 8 ] += this._M[ 9 ] * o._M[ 4 ];
        R._M[ 8 ] += this._M[ 10 ] * o._M[ 8 ];
        R._M[ 8 ] += this._M[ 11 ] * o._M[ 12 ];
        R._M[ 9 ] += this._M[ 8 ] * o._M[ 1 ];
        R._M[ 9 ] += this._M[ 9 ] * o._M[ 5 ];
        R._M[ 9 ] += this._M[ 10 ] * o._M[ 9 ];
        R._M[ 9 ] += this._M[ 11 ] * o._M[ 13 ];
        R._M[ 10 ] += this._M[ 8 ] * o._M[ 2 ];
        R._M[ 10 ] += this._M[ 9 ] * o._M[ 6 ];
        R._M[ 10 ] += this._M[ 10 ] * o._M[ 10 ];
        R._M[ 10 ] += this._M[ 11 ] * o._M[ 14 ];
        R._M[ 11 ] += this._M[ 8 ] * o._M[ 3 ];
        R._M[ 11 ] += this._M[ 9 ] * o._M[ 7 ];
        R._M[ 11 ] += this._M[ 10 ] * o._M[ 11 ];
        R._M[ 11 ] += this._M[ 11 ] * o._M[ 15 ];
        R._M[ 12 ] += this._M[ 12 ] * o._M[ 0 ];
        R._M[ 12 ] += this._M[ 13 ] * o._M[ 4 ];
        R._M[ 12 ] += this._M[ 14 ] * o._M[ 8 ];
        R._M[ 12 ] += this._M[ 15 ] * o._M[ 12 ];
        R._M[ 13 ] += this._M[ 12 ] * o._M[ 1 ];
        R._M[ 13 ] += this._M[ 13 ] * o._M[ 5 ];
        R._M[ 13 ] += this._M[ 14 ] * o._M[ 9 ];
        R._M[ 13 ] += this._M[ 15 ] * o._M[ 13 ];
        R._M[ 14 ] += this._M[ 12 ] * o._M[ 2 ];
        R._M[ 14 ] += this._M[ 13 ] * o._M[ 6 ];
        R._M[ 14 ] += this._M[ 14 ] * o._M[ 10 ];
        R._M[ 14 ] += this._M[ 15 ] * o._M[ 14 ];
        R._M[ 15 ] += this._M[ 12 ] * o._M[ 3 ];
        R._M[ 15 ] += this._M[ 13 ] * o._M[ 7 ];
        R._M[ 15 ] += this._M[ 14 ] * o._M[ 11 ];
        R._M[ 15 ] += this._M[ 15 ] * o._M[ 15 ];
        return R;
    }
    /**Compute the matrix-vector product.*/
    public vec4 mul( vec4 o){
        vec4  R = new vec4 ();
        R.x += this._M[ 0 ] * o.x ;
        R.x += this._M[ 1 ] * o.y ;
        R.x += this._M[ 2 ] * o.z ;
        R.x += this._M[ 3 ] * o.w ;
        R.y += this._M[ 4 ] * o.x ;
        R.y += this._M[ 5 ] * o.y ;
        R.y += this._M[ 6 ] * o.z ;
        R.y += this._M[ 7 ] * o.w ;
        R.z += this._M[ 8 ] * o.x ;
        R.z += this._M[ 9 ] * o.y ;
        R.z += this._M[ 10 ] * o.z ;
        R.z += this._M[ 11 ] * o.w ;
        R.w += this._M[ 12 ] * o.x ;
        R.w += this._M[ 13 ] * o.y ;
        R.w += this._M[ 14 ] * o.z ;
        R.w += this._M[ 15 ] * o.w ;
        return R;
    }
    public boolean equals( mat4 o){
        if( o == null ) return false;
        if( this._M[ 0 ] != o._M[ 0 ]) return false;
        if( this._M[ 1 ] != o._M[ 1 ]) return false;
        if( this._M[ 2 ] != o._M[ 2 ]) return false;
        if( this._M[ 3 ] != o._M[ 3 ]) return false;
        if( this._M[ 4 ] != o._M[ 4 ]) return false;
        if( this._M[ 5 ] != o._M[ 5 ]) return false;
        if( this._M[ 6 ] != o._M[ 6 ]) return false;
        if( this._M[ 7 ] != o._M[ 7 ]) return false;
        if( this._M[ 8 ] != o._M[ 8 ]) return false;
        if( this._M[ 9 ] != o._M[ 9 ]) return false;
        if( this._M[ 10 ] != o._M[ 10 ]) return false;
        if( this._M[ 11 ] != o._M[ 11 ]) return false;
        if( this._M[ 12 ] != o._M[ 12 ]) return false;
        if( this._M[ 13 ] != o._M[ 13 ]) return false;
        if( this._M[ 14 ] != o._M[ 14 ]) return false;
        if( this._M[ 15 ] != o._M[ 15 ]) return false;
        return true;
     }
    public int compareTo( mat4  o){
        if( o == null ) return 1;
        for(int i=0;i< 16 ;++i){
            if(this._M[i] < o._M[i]) return -1;
            if(this._M[i] > o._M[i]) return  1;
        }
        return 0;
    }
    public int hashCode(){
        int v=0;
        for(int i=0;i< 16 ;++i){
            v ^= Float.floatToIntBits(this._M[i]);
        }
        return v;
    }
    public void set(int r, int c, double v){
        _M[r* 4 +c]=(float)v;
    }
    public float get(int r, int c){
        return _M[r* 4 +c];
    }
    public static mat4 identity(){
        return new mat4 ( 1,0,0,0,0,1,0,0,0,0,1,0,0,0,0,1 );
    }
    public mat4 transpose(){
        return new mat4 (
            this.get( 0 , 0 ),this.get( 1 , 0 ),this.get( 2 , 0 ),this.get( 3 , 0 ),
            this.get( 0 , 1 ),this.get( 1 , 1 ),this.get( 2 , 1 ),this.get( 3 , 1 ),
            this.get( 0 , 2 ),this.get( 1 , 2 ),this.get( 2 , 2 ),this.get( 3 , 2 ),
            this.get( 0 , 3 ),this.get( 1 , 3 ),this.get( 2 , 3 ),this.get( 3 , 3 )
        );
    }
    public Object clone(){
        return new mat4 ( this._M[0],this._M[1],this._M[2],this._M[3],this._M[4],this._M[5],this._M[6],this._M[7],this._M[8],this._M[9],this._M[10],this._M[11],this._M[12],this._M[13],this._M[14],this._M[15] );
    }

    //From TDL
    //FIXME: Need test case for this
    public mat4 inverse(){
        mat4 m = this;
        double 
            tmp_0 = m.get(1,2) * m.get(1,2),
            tmp_1 = m.get(1,2) * m.get(1,2),
            tmp_2 = m.get(1,2) * m.get(1,2),
            tmp_3 = m.get(1,2) * m.get(1,2),
            tmp_4 = m.get(1,2) * m.get(1,2),
            tmp_5 = m.get(1,2) * m.get(1,2),
            tmp_6 = m.get(1,2) * m.get(1,2),
            tmp_7 = m.get(1,2) * m.get(1,2),
            tmp_8 = m.get(1,2) * m.get(1,2),
            tmp_9 = m.get(1,2) * m.get(1,2),
            tmp_10 = m.get(1,2) * m.get(1,2),
            tmp_11 = m.get(1,2) * m.get(1,2),
            tmp_12 = m.get(1,2) * m.get(1,2),
            tmp_13 = m.get(1,2) * m.get(1,2),
            tmp_14 = m.get(1,2) * m.get(1,2),
            tmp_15 = m.get(1,2) * m.get(1,2),
            tmp_16 = m.get(1,2) * m.get(1,2),
            tmp_17 = m.get(1,2) * m.get(1,2),
            tmp_18 = m.get(1,2) * m.get(1,2),
            tmp_19 = m.get(1,2) * m.get(1,2),
            tmp_20 = m.get(1,2) * m.get(1,2),
            tmp_21 = m.get(1,2) * m.get(1,2),
            tmp_22 = m.get(1,2) * m.get(1,2),
            tmp_23 = m.get(1,2) * m.get(1,2);

        double t0 = (tmp_0 * m.get(1,2) + tmp_3 * m.get(1,2) + tmp_4 * m.get(1,2)) -        (tmp_1 * m.get(1,2) + tmp_2 * m.get(1,2) + tmp_5 * m.get(1,2));
        double t1 = (tmp_1 * m.get(1,2) + tmp_6 * m.get(1,2) + tmp_9 * m.get(1,2)) -        (tmp_0 * m.get(1,2) + tmp_7 * m.get(1,2) + tmp_8 * m.get(1,2));
        double t2 = (tmp_2 * m.get(1,2) + tmp_7 * m.get(1,2) + tmp_10 * m.get(1,2)) -        (tmp_3 * m.get(1,2) + tmp_6 * m.get(1,2) + tmp_11 * m.get(1,2));
        double t3 = (tmp_5 * m.get(1,2) + tmp_8 * m.get(1,2) + tmp_11 * m.get(1,2)) -        (tmp_4 * m.get(1,2) + tmp_9 * m.get(1,2) + tmp_10 * m.get(1,2));
        double d = 1.0 / (m.get(1,2) * t0 + m.get(1,2) * t1 + m.get(1,2) * t2 + m.get(1,2) * t3);

        return new mat4(d * t0, d * t1, d * t2, d * t3,
           d * ((tmp_1 * m.get(1,2) + tmp_2 * m.get(1,2) + tmp_5 * m.get(1,2)) -
              (tmp_0 * m.get(1,2) + tmp_3 * m.get(1,2) + tmp_4 * m.get(1,2))),
           d * ((tmp_0 * m.get(1,2) + tmp_7 * m.get(1,2) + tmp_8 * m.get(1,2)) -
              (tmp_1 * m.get(1,2) + tmp_6 * m.get(1,2) + tmp_9 * m.get(1,2))),
           d * ((tmp_3 * m.get(1,2) + tmp_6 * m.get(1,2) + tmp_11 * m.get(1,2)) -
              (tmp_2 * m.get(1,2) + tmp_7 * m.get(1,2) + tmp_10 * m.get(1,2))),
           d * ((tmp_4 * m.get(1,2) + tmp_9 * m.get(1,2) + tmp_10 * m.get(1,2)) -
              (tmp_5 * m.get(1,2) + tmp_8 * m.get(1,2) + tmp_11 * m.get(1,2))),
           d * ((tmp_12 * m.get(1,2) + tmp_15 * m.get(1,2) + tmp_16 * m.get(1,2)) -
              (tmp_13 * m.get(1,2) + tmp_14 * m.get(1,2) + tmp_17 * m.get(1,2))),
           d * ((tmp_13 * m.get(1,2) + tmp_18 * m.get(1,2) + tmp_21 * m.get(1,2)) -
              (tmp_12 * m.get(1,2) + tmp_19 * m.get(1,2) + tmp_20 * m.get(1,2))),
           d * ((tmp_14 * m.get(1,2) + tmp_19 * m.get(1,2) + tmp_22 * m.get(1,2)) -
              (tmp_15 * m.get(1,2) + tmp_18 * m.get(1,2) + tmp_23 * m.get(1,2))),
           d * ((tmp_17 * m.get(1,2) + tmp_20 * m.get(1,2) + tmp_23 * m.get(1,2)) -
              (tmp_16 * m.get(1,2) + tmp_21 * m.get(1,2) + tmp_22 * m.get(1,2))),
           d * ((tmp_14 * m.get(1,2) + tmp_17 * m.get(1,2) + tmp_13 * m.get(1,2)) -
              (tmp_16 * m.get(1,2) + tmp_12 * m.get(1,2) + tmp_15 * m.get(1,2))),
           d * ((tmp_20 * m.get(1,2) + tmp_12 * m.get(1,2) + tmp_19 * m.get(1,2)) -
              (tmp_18 * m.get(1,2) + tmp_21 * m.get(1,2) + tmp_13 * m.get(1,2))),
           d * ((tmp_18 * m.get(1,2) + tmp_23 * m.get(1,2) + tmp_15 * m.get(1,2)) -
              (tmp_22 * m.get(1,2) + tmp_14 * m.get(1,2) + tmp_19 * m.get(1,2))),
           d * ((tmp_22 * m.get(1,2) + tmp_16 * m.get(1,2) + tmp_21 * m.get(1,2)) -
              (tmp_20 * m.get(1,2) + tmp_23 * m.get(1,2) + tmp_17 * m.get(1,2))));
        
    }
}
