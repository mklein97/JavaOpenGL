
package main;

import static math3d.functions.add;
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
            
    static final double PI2 = Math.PI/2;
    static final double PI32 = 3.0*Math.PI/2;
    static final double TWOPI = Math.PI*2;
    
    float turnTowards(vec3 p, float maxRotate){
        //vector from location to p
        vec3 f = sub(p,position.xyz());
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
        
        if( angle != 0 ){
            mat4 R = axisRotation( new vec3(0,1,0), angle);
            right = mul(right,R);
            up = mul(up,R);
            facing = mul(facing,R);
        }
        return (float)angle;
    }
    
    void setPosition(vec3 v){
        position = v;
    }
    
    void lookAt(vec3 pos){
        facing = new vec4(normalize(sub(pos,position)),0);
        right = normalize(cross(up,facing));
        up = normalize(cross(facing,right));
        
    }
    void lookAtNoY(vec3 pos){
        facing = new vec4(sub(pos,position),0);
        facing.y = 0;
        facing = normalize(facing);
        right = normalize(cross(up,facing));
        up = normalize(cross(facing,right));
    }

    void walk(float amt){
        position = add( position, mul(amt, new vec3(facing.x, 0, facing.z)));
    }
    void turn(float amt){
        mat4 M = axisRotation(up,amt);
        facing = mul(facing,M);
        right = mul(right,M);
    }
    void tilt(float amt){
        mat4 M = axisRotation(right,amt);
        facing = mul(facing,M);
        up = mul(up,M);
    }
    void strafe(float dx){
        position = add(position, mul(dx, new vec3(right.x, 0, right.z)));
    }
    void strafeY(float dx){
        position = add(position, mul(dx,up.xyz()));
    }
    void turnY(float amt) {
        facing.y += amt;
        if (facing.y > 3.4f)
            facing.y = 3.4f;
        if (facing.y < -3.4f)
            facing.y = -3.4f;
    }
}
