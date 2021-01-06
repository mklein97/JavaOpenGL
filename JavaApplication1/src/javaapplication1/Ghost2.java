package javaapplication1;

/**
 *
 * @author Matt
 */
import math3d.vec3;
import static math3d.functions.translation;
import java.nio.file.Paths;
import static math3d.functions.randrange;
import static math3d.functions.scaling;

public class Ghost2 extends GameObject{   
    static Mesh ghostmesh;
    static vec3 point;
    boolean needsdeleted;
    float alpha;
    Ghost2(vec3 pos, vec3 vel, vec3 g){
        super(pos,vel,g,-1);
        needsdeleted = false;
        alpha = 1.0f;
        point = new vec3(randrange(-25,25),0,randrange(-25,25));
        if (ghostmesh == null)
            ghostmesh = new Mesh(Paths.get("assets","ghost2.obj.mesh"));
    }
    void draw(){
        //set uniforms (worldMatrix)
        Program.current.setUniform("scaleMatrix", scaling(new vec3(1,1,1)));
        Program.current.setUniform("worldMatrix", lcs.getMatrix());
        ghostmesh.draw();
    }
    void resetPoint(){
        point = new vec3(randrange(-25,25),0,randrange(-25,25));
    }
    void update(int elapsed, float turn, vec3 p){
        lcs.turnTowards(point, turn);
        vec3 v = lcs.facing.mul(elapsed * 0.005).xyz();
        v.y = 0;
        lcs.position = lcs.position.add(v);
        if (lcs.position.x <= point.x + 5 && lcs.position.x <= point.x - 5 && lcs.position.z <= point.z + 5 && lcs.position.z >= point.z - 5)
            resetPoint();
        if (lcs.position.x <= p.x+2 && lcs.position.x >= p.x-2 && lcs.position.z <= p.z+2 && lcs.position.z >= p.z-2)
            needsdeleted = true;
    }
    boolean isAlive(){
        return(lcs.position.y > -4000);
    }
}
