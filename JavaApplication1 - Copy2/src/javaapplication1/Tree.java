package javaapplication1;

/**
 *
 * @author Matt
 */
import math3d.vec3;
import static math3d.functions.translation;
import java.nio.file.Paths;
import static math3d.functions.scaling;

public class Tree extends GameObject{   
    static Mesh treemesh;
    Tree(vec3 pos, vec3 vel, vec3 g){
        super(pos,vel,g,-1);
        
        if (treemesh == null)
            treemesh = new Mesh(Paths.get("assets","tree2.obj.mesh"));
    }
    void draw(){
        //set uniforms (worldMatrix)
        Program.current.setUniform("scaleMatrix", scaling(new vec3(1,1,1)));
        Program.current.setUniform("worldMatrix", lcs.getMatrix());
        treemesh.draw();
    }
    boolean isAlive(){
        return(lcs.position.y > -4000);
    }
}
