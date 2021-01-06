package main;

import static math3d.functions.add;
import static math3d.functions.axisRotation;
import static math3d.functions.cross;
import static math3d.functions.mul;
import static math3d.functions.normalize;
import static math3d.functions.radians;
import static math3d.functions.sub;
import static math3d.functions.translation;
import math3d.mat4;
import math3d.vec3;
import math3d.vec4;

/**
 *
 * @author jhudson
 */
public class Camera {
    
    float fov_v = 45.0f;
    float aspect_ratio = 1.0f;
    mat4 projMatrix;
    mat4 viewMatrix;
    float hither = 0.1f;
    float yon = 300.0f;
    float viewPlaneDistance_h;  
    float viewPlaneDistance_v;
    vec3 eye = new vec3(0,0,0);
    vec3 U = new vec3(1,0,0);
    vec3 V = new vec3(0,1,0);
    vec3 W = new vec3(0,0,1);
    
    public Camera(){
        updateProjMatrix();
        updateViewMatrix();
    }
    
    public void updateProjMatrix(){
        float fov_h = aspect_ratio * fov_v;
        viewPlaneDistance_h = (float) (1.0f / Math.tan(radians(fov_h)));
        viewPlaneDistance_v = (float) (1.0f / Math.tan(radians(fov_v)));
        projMatrix = new mat4(
                viewPlaneDistance_h,0,0,0,
                0,viewPlaneDistance_v,0,0,
                0,0,1+(2*yon)/(hither-yon),-1,
                0,0,(2*hither*yon)/(hither-yon),0);
    }
    
    public void updateViewMatrix(){
        viewMatrix = mul(
                translation(eye.neg()),
                new mat4(U.x,V.x,W.x,0,
                        U.y,V.y,W.y,0,
                        U.z,V.z,W.z,0,
                        0,0,0,1));
    }
        
    public void draw(){
        Program.current.setUniform("projMatrix",projMatrix);
        Program.current.setUniform("viewMatrix",viewMatrix);
        Program.current.setUniform("eyePos",eye);
        Program.current.setUniform("hitherYon", new vec4(hither, yon, yon - hither, 1 / (yon - hither)));
    }
    
    public void lookAt(vec3 e, vec3 c, vec3 up ){
        eye = e.xyz();
        W = normalize(sub(e,c));
        U = normalize(cross(up,W));
        V = normalize(cross(W,U));
        updateViewMatrix();
    }
    public void setPositionFacingAndUp(vec3 pos, vec3 facing, vec3 up){
        lookAt( pos, add(pos,facing), up);
    }
    
    public void walk(double amt){
        eye = add(eye,mul(amt,W.neg()));
        updateViewMatrix();
    }
    
    public void turn(double amt){
        mat4 M = axisRotation(V,amt);
        W = mul( new vec4(W,0), M).xyz();
        U = mul( new vec4(U,0), M).xyz();
        updateViewMatrix();
    }
    
    public void strafe(double amt){
        eye = add(eye,mul(amt,U));
        updateViewMatrix();
    }
}
