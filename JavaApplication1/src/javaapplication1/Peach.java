package javaapplication1;

import math3d.vec3;
import static math3d.functions.translation;
import java.nio.file.Paths;
import static math3d.functions.scaling;

/**
 *
 * @author jhudson
 */
public class Peach extends GameObject{
    static Mesh peachmesh;
    Peach(vec3 pos, vec3 vel, vec3 g){
        super(pos,vel,g,-1);
     
        if (peachmesh == null)
            peachmesh = new Mesh(Paths.get("assets","peach.obj.mesh"));
    }
    void draw(){
        //set uniforms (worldMatrix)
        Program.current.setUniform("scaleMatrix", scaling(new vec3(1,1,1)));
        Program.current.setUniform("worldMatrix", lcs.getMatrix());
        peachmesh.draw();
    }
    boolean isAlive(){
        return(lcs.position.y > -4000);
    }
}