package javaapplication1;

import static math3d.functions.axisRotation;
import static math3d.functions.cross;
import static math3d.functions.mul;
import static math3d.functions.normalize;
import static math3d.functions.sub;
import static math3d.functions.translation;
import math3d.mat4;
import math3d.vec3;
import math3d.vec4;

/**
 *
 * @author jhudson
 */
class LocalCoordinateSystem {
    vec4 right,up,facing;
    vec3 position;
    
    public LocalCoordinateSystem(vec3 p) {
        position = p.xyz();
        right = new vec4(1,0,0,0);
        up = new vec4(0,1,0,0);
        facing = new vec4(0,0,1,0);
    }
    public mat4 getMatrix(){
        return mul(
            new mat4(
                right.x, right.y, right.z, 0,
                up.x, up.y, up.z, 0,
                facing.x, facing.y, facing.z,0,
                0,0,0,1),
        translation(position));
    }
    
//    //assumes angle is between 0 ... +/- pi
//    int quadrant(double angle){
//        if( angle >= 0.5*Math.PI )
//            return 2;
//        if( angle >=0 )
//            return 1;
//        else if( angle < -0.5*Math.PI )
//            return 3;
//        else
//            return 4;
//    }
            
    static final double PI2 = Math.PI/2;
    static final double PI32 = 3.0*Math.PI/2;
    static final double TWOPI = Math.PI*2;
    
    float turnTowards(vec3 p, float maxRotate){
        //vector from location to p
        vec3 f = sub(p,position);
        double want = Math.atan2(f.x,f.z);
        double have = Math.atan2(facing.x,facing.z);
        if(want < 0 )
            want += TWOPI;
        if( have < 0 )
            have += TWOPI;
        double angle = want-have;
        if( angle > Math.PI )
            angle -= TWOPI;
        if( angle < -Math.PI )
            angle += TWOPI;
        
                    
        if( angle > 0 && angle > maxRotate){
            angle = maxRotate;
        }
        else if( angle < 0 && angle < -maxRotate){
            angle = -maxRotate;
        }
        
        //System.out.println("wantAngle="+angle1+" currAngle="+angle2+" delta="+angle);
        if( angle != 0 ){
            mat4 R = axisRotation( new vec3(0,1,0), angle);
            right = mul(right,R);
            up = mul(up,R);
            facing = mul(facing,R);
        }
        return (float)angle;
    }
    
    void lookAt(vec3 pos){
        facing = new vec4(normalize(sub(pos,this.position)),0.0);
        right = normalize(cross(up,facing));
        
    }
    void lookAtNoY(vec3 pos){
        facing = new vec4(normalize(sub(pos,this.position)),0.0);
        facing.y = 0;
        right = normalize(cross(up,facing));
        
    }

        
}
