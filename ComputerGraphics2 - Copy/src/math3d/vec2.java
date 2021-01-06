/** Written at SSU Spring 2017 */
package math3d;

/** A class for a 2 dimensional vector */
public class vec2  implements Cloneable,Comparable< vec2 >{
    /** The x data field */
    public float x = 0.0f ;
    /** The y data field */
    public float y = 0.0f ;
    /** Default constructor */
    public vec2 (){}
    public vec2 ( double x_, double y_ ){
        this.x = (float) x_;
        this.y = (float) y_;
    }
    /** Construct a vec2 from the given values. */
    public vec2 ( vec2  xy_){
        x = xy_.x ;
        y = xy_.y ;
    }
    public String toString(){
        return "[" +  x+" "+y +"]";
    }
    /**Get one of the fields.
        @param i Field to get (may be one of  [0, 1] )
        @return The value
     */
    public float get(int i){
        switch(i){
            case( 0 ): return x ;
            case( 1 ): return y ;
            default: throw new RuntimeException("Bad field number");
        }
    }
    /**Set one of the fields.
        @param i Field to set (may be one of  [0, 1] )
        @param v The value
     */
    public void set(int i, double v){
        switch(i){
            case( 0 ):  x =(float)v; return;
            case( 1 ):  y =(float)v; return;
            default: throw new RuntimeException("Bad field number");
        }
    }
    public Object clone(){
        return new vec2 ( x,y );
    }
    public float[] tofloats(){
        return new float[]{ (float) x,(float) y};
    }
    /**Compute the dot product of this vec2 with another vec2 .*/
    public float dot( vec2  o){
        return this.x * o.x+this.y * o.y ;
    }
    /**Compute the sum of this vec2 with another vec2 .*/
    public vec2 add( vec2  o){
        return new vec2 ( this.x + o.x,this.y + o.y );
    }
    /**Compute the difference between this vec2 and another vec2 .*/
    public vec2 sub( vec2  o){
        return new vec2 ( this.x - o.x,this.y - o.y );
    }
    /**Compute the componentwise product of this vec2 and another vec2 .*/
    public vec2 mul( vec2  o){
        return new vec2 ( this.x * o.x,this.y * o.y );
    }
    /**Compute the product of this vec2 and a scalar.*/
    public vec2 mul( double f){
        return new vec2 ( this.x * f,this.y * f );
    }
    /** Return the negation of this vec2 */
    public vec2 neg(){
        return this.mul(-1.0f);
    }
    /**Compute the vector-matrix product.*/
    public vec2 mul( mat2 o){
        vec2  R = new vec2 ();
        R.x = this.x * o.get(0,0)+this.y * o.get(1,0) ;
        R.y = this.x * o.get(0,1)+this.y * o.get(1,1) ;
        return R;
    }
    public boolean equals( vec2 o){
        if( o == null ) return false;
        if( this.x != o.x) return false;
        if( this.y != o.y) return false;
        return true;
     }
    public int compareTo( vec2  o){
        if( o == null ) return 1;
        if( this.x < o.x) return -1;
        if( this.x > o.x) return  1;
        if( this.y < o.y) return -1;
        if( this.y > o.y) return  1;
        return 0;
    }
    public int hashCode(){
        int v=0;
        v ^= Float.floatToIntBits( x );
        v ^= Float.floatToIntBits( y );
        return v;
    }
    public vec2 xx (){
        return new vec2( x,x );
    }
    public vec2 xy (){
        return new vec2( x,y );
    }
    public vec2 yx (){
        return new vec2( y,x );
    }
    public vec2 yy (){
        return new vec2( y,y );
    }
}
