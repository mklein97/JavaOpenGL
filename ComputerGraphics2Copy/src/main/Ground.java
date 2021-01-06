/*
 */
package main;

import static math3d.functions.mul;
import static math3d.functions.sub;
import static math3d.functions.scaling;
import static math3d.functions.translation;
import math3d.mat4;
import math3d.vec3;

/**
 *
 * @author jhudson
 */
public class Ground extends GameObject {
    static Mesh mesh;
    mat4 M ;
    Ground(){
        super(new vec3(0,0,0), new vec3(0,0,0), new vec3(0,0,0),-1);
        if(mesh == null)
            mesh = new Mesh("assets/ground.obj.mesh");
        M = mul(scaling(50,50,50),translation(sub(lcs.position, new vec3(170,0,170))));
    
    }
    
    @Override
    void hit(Type t){
        //being hit by anything has no effect
    }
    
    void draw(){
        Program.current.setUniform("worldMatrix", M);
        mesh.draw();
    }

    @Override
    Mesh getMesh() {
        return mesh;
    }
}
