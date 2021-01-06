/** Written at SSU Spring 2017 */
package math3d;

/** A class for a 3 dimensional vector */
public class vec3  implements Cloneable,Comparable< vec3 >{
    /** The x data field */
    public float x = 0.0f ;
    /** The y data field */
    public float y = 0.0f ;
    /** The z data field */
    public float z = 0.0f ;
    /** Default constructor */
    public vec3 (){}
    public vec3 ( double x_, double y_, double z_ ){
        this.x = (float) x_;
        this.y = (float) y_;
        this.z = (float) z_;
    }
    /** Construct a vec3 from the given values. */
    public vec3 ( vec3  xyz_){
        x = xyz_.x ;
        y = xyz_.y ;
        z = xyz_.z ;
    }
    /** Construct a vec3 from the given values. */
    public vec3 ( vec2  xy_, double z_){
        x = xy_.x ;
        y = xy_.y ;
        z = (float) z_;
    }
    public String toString(){
        return "[" +  x+" "+y+" "+z +"]";
    }
    /**Get one of the fields.
        @param i Field to get (may be one of  [0, 1, 2] )
        @return The value
     */
    public float get(int i){
        switch(i){
            case( 0 ): return x ;
            case( 1 ): return y ;
            case( 2 ): return z ;
            default: throw new RuntimeException("Bad field number");
        }
    }
    /**Set one of the fields.
        @param i Field to set (may be one of  [0, 1, 2] )
        @param v The value
     */
    public void set(int i, double v){
        switch(i){
            case( 0 ):  x =(float)v; return;
            case( 1 ):  y =(float)v; return;
            case( 2 ):  z =(float)v; return;
            default: throw new RuntimeException("Bad field number");
        }
    }
    public Object clone(){
        return new vec3 ( x,y,z );
    }
    public float[] tofloats(){
        return new float[]{ (float) x,(float) y,(float) z};
    }
    /**Compute the dot product of this vec3 with another vec3 .*/
    public float dot( vec3  o){
        return this.x * o.x+this.y * o.y+this.z * o.z ;
    }
    /**Compute the sum of this vec3 with another vec3 .*/
    public vec3 add( vec3  o){
        return new vec3 ( this.x + o.x,this.y + o.y,this.z + o.z );
    }
    /**Compute the difference between this vec3 and another vec3 .*/
    public vec3 sub( vec3  o){
        return new vec3 ( this.x - o.x,this.y - o.y,this.z - o.z );
    }
    /**Compute the componentwise product of this vec3 and another vec3 .*/
    public vec3 mul( vec3  o){
        return new vec3 ( this.x * o.x,this.y * o.y,this.z * o.z );
    }
    /**Compute the product of this vec3 and a scalar.*/
    public vec3 mul( double f){
        return new vec3 ( this.x * f,this.y * f,this.z * f );
    }
    /** Return the negation of this vec3 */
    public vec3 neg(){
        return this.mul(-1.0f);
    }
    /**Compute the vector-matrix product.*/
    public vec3 mul( mat3 o){
        vec3  R = new vec3 ();
        R.x = this.x * o.get(0,0)+this.y * o.get(1,0)+this.z * o.get(2,0) ;
        R.y = this.x * o.get(0,1)+this.y * o.get(1,1)+this.z * o.get(2,1) ;
        R.z = this.x * o.get(0,2)+this.y * o.get(1,2)+this.z * o.get(2,2) ;
        return R;
    }
    public boolean equals( vec3 o){
        if( o == null ) return false;
        if( this.x != o.x) return false;
        if( this.y != o.y) return false;
        if( this.z != o.z) return false;
        return true;
     }
    public int compareTo( vec3  o){
        if( o == null ) return 1;
        if( this.x < o.x) return -1;
        if( this.x > o.x) return  1;
        if( this.y < o.y) return -1;
        if( this.y > o.y) return  1;
        if( this.z < o.z) return -1;
        if( this.z > o.z) return  1;
        return 0;
    }
    public int hashCode(){
        int v=0;
        v ^= Float.floatToIntBits( x );
        v ^= Float.floatToIntBits( y );
        v ^= Float.floatToIntBits( z );
        return v;
    }
    public vec2 xx (){
        return new vec2( x,x );
    }
    public vec3 xxx (){
        return new vec3( x,x,x );
    }
    public vec3 xxy (){
        return new vec3( x,x,y );
    }
    public vec3 xxz (){
        return new vec3( x,x,z );
    }
    public vec2 xy (){
        return new vec2( x,y );
    }
    public vec3 xyx (){
        return new vec3( x,y,x );
    }
    public vec3 xyy (){
        return new vec3( x,y,y );
    }
    public vec3 xyz (){
        return new vec3( x,y,z );
    }
    public vec2 xz (){
        return new vec2( x,z );
    }
    public vec3 xzx (){
        return new vec3( x,z,x );
    }
    public vec3 xzy (){
        return new vec3( x,z,y );
    }
    public vec3 xzz (){
        return new vec3( x,z,z );
    }
    public vec2 yx (){
        return new vec2( y,x );
    }
    public vec3 yxx (){
        return new vec3( y,x,x );
    }
    public vec3 yxy (){
        return new vec3( y,x,y );
    }
    public vec3 yxz (){
        return new vec3( y,x,z );
    }
    public vec2 yy (){
        return new vec2( y,y );
    }
    public vec3 yyx (){
        return new vec3( y,y,x );
    }
    public vec3 yyy (){
        return new vec3( y,y,y );
    }
    public vec3 yyz (){
        return new vec3( y,y,z );
    }
    public vec2 yz (){
        return new vec2( y,z );
    }
    public vec3 yzx (){
        return new vec3( y,z,x );
    }
    public vec3 yzy (){
        return new vec3( y,z,y );
    }
    public vec3 yzz (){
        return new vec3( y,z,z );
    }
    public vec2 zx (){
        return new vec2( z,x );
    }
    public vec3 zxx (){
        return new vec3( z,x,x );
    }
    public vec3 zxy (){
        return new vec3( z,x,y );
    }
    public vec3 zxz (){
        return new vec3( z,x,z );
    }
    public vec2 zy (){
        return new vec2( z,y );
    }
    public vec3 zyx (){
        return new vec3( z,y,x );
    }
    public vec3 zyy (){
        return new vec3( z,y,y );
    }
    public vec3 zyz (){
        return new vec3( z,y,z );
    }
    public vec2 zz (){
        return new vec2( z,z );
    }
    public vec3 zzx (){
        return new vec3( z,z,x );
    }
    public vec3 zzy (){
        return new vec3( z,z,y );
    }
    public vec3 zzz (){
        return new vec3( z,z,z );
    }
}
