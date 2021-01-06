package javaapplication1;

/**
 *
 * @author Matt
 */
import math3d.vec3;
import static math3d.functions.translation;
import java.nio.file.Paths;
import static math3d.functions.scaling;

public class FairyRing extends GameObject{   
    static Mesh fmesh;
    FairyRing(vec3 pos, vec3 vel, vec3 g){
        super(pos,vel,g,-1);
        
        if (fmesh == null)
            fmesh = new Mesh(Paths.get("assets","fairyring.obj.mesh"));
    }
    void update(vec3 fpos){
        lcs.lookAtNoY(fpos);
    }
    
    void draw(){
        //set uniforms (worldMatrix)
        Program.current.setUniform("scaleMatrix", scaling(new vec3(2,2,2)));
        Program.current.setUniform("worldMatrix", lcs.getMatrix());
        fmesh.draw();
    }
    boolean isAlive(){
        return(lcs.position.y > -4000);
    }
}
