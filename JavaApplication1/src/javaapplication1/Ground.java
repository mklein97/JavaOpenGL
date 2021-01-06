package javaapplication1;

/**
 *
 * @author Matt
 */
import math3d.vec3;
import static math3d.functions.translation;
import java.nio.file.Paths;
import static math3d.functions.scaling;

public class Ground extends GameObject{
    //Sphere sph;
    static Mesh groundmesh;
    Ground(vec3 pos, vec3 vel, vec3 g){
        super(pos,vel,g,-1);
        //sph = new Sphere();
        if (groundmesh == null)
            groundmesh = new Mesh(Paths.get("assets","ground.obj.mesh"));
    }
    void draw(){
        //set uniforms (worldMatrix)
        Program.current.setUniform("scaleMatrix", scaling(new vec3(100,100,100)));
        Program.current.setUniform("worldMatrix", lcs.getMatrix());
        groundmesh.draw();
    }
    boolean isAlive(){
        return(lcs.position.y > -4000);
    }
}
