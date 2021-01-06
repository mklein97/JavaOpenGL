package javaapplication1;

/**
 *
 * @author Matt
 */
import math3d.vec3;
import static math3d.functions.translation;
import java.nio.file.Paths;
import static math3d.functions.scaling;

public class Pumpkin extends GameObject{   
    static Mesh pumpmesh;
    boolean needsdeleted;
    Pumpkin(vec3 pos, vec3 vel, vec3 g){
        super(pos,vel,g,-1);
        needsdeleted = false;
        if (pumpmesh == null)
            pumpmesh = new Mesh(Paths.get("assets","jackolantern.obj.mesh"));
    }
    void draw(){
        //set uniforms (worldMatrix)
        Program.current.setUniform("scaleMatrix", scaling(new vec3(1,1,1)));
        Program.current.setUniform("worldMatrix", lcs.getMatrix());
        pumpmesh.draw();
    }
    boolean isAlive(){
        return(lcs.position.y > -4000);
    }
    
    void update(vec3 p) {
        if (lcs.position.x <= p.x+2 && lcs.position.x >= p.x-2 && lcs.position.z <= p.z+2 && lcs.position.z >= p.z-2)
            needsdeleted = true;
    }
}
