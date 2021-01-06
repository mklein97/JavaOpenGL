package javaapplication1;

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

public class Camera {
    mat4 viewMatrix;
    mat4 projMatrix;
    vec3 eye;
    vec3 u,v,w;
    float hither= 0.1f;
    float yon = 500.0f;
    float fov_v= 45.0f;
    float aspect_ratio = 1.0f;
    Camera(){
        eye = new vec3(0,0,0);
        u = new vec3(1,0,0);
        v = new vec3(0,1,0);
        w = new vec3(0,0,1);
        compute_view_matrix();
        compute_proj_matrix();
        
    }
    void compute_proj_matrix(){
        float dv = (float) (1.0/Math.tan(fov_v/180.0f *Math.PI));
        projMatrix = new mat4(1/(Math.tan(dv)),0,0,0,
                0,1/(Math.tan(dv)),0,0,
                0,0,1+((2*yon)/(hither-yon)),-1,
                0,0,((2*hither*yon)/(hither-yon)),0);
    }
    void compute_view_matrix(){
        viewMatrix = mul(translation(eye.neg()), new mat4(u.x,v.x,w.x,0,u.y,v.y,w.y,0,u.z,v.z,w.z,0,0,0,0,1));
    }
    void turn(double amt){
        mat4 M= axisRotation(v,amt);
        u = mul(new vec4(u,0),M).xyz();
        w = mul(new vec4(w,0),M).xyz();
        compute_view_matrix();
        
    }
    void walk(double amt){
        eye = add(mul(-amt,w),eye);
        compute_view_matrix();
    }
    void strafe(double dx, double dy){
        eye = add(mul(dx,u), mul(dy,v), eye);
        compute_view_matrix();
    }
    void lookAt(vec3 e, vec3 coi, vec3 up){
        eye = new vec3(e.x,e.y,e.z);
        w = normalize(sub(eye,coi));
        u = normalize(cross(up,w));
        v = normalize(cross(w,u));
        compute_view_matrix();
        
    }
    void draw(){
        Program.current.setUniform("projMatrix", projMatrix);
        Program.current.setUniform("viewMatrix", viewMatrix);
        Program.current.setUniform("eyePos", eye);
    }
}
