package main;

import static math3d.functions.mul;
import static math3d.functions.scaling;
import static math3d.functions.translation;
import math3d.vec3;
import math3d.mat4;

/**
 *
 * @author jhudson
 */
public class Statue  extends GameObject {
    static Mesh m;
    mat4 M;
    public Statue(vec3 p) {
        super(p, null,null,-1);
        if(m==null)
            m = new Mesh("assets/statue.obj.mesh");
        M = mul(scaling(0.009f,0.009f,0.009f),translation(lcs.position));
    }
    
    void update(int elapsed){
        super.update(elapsed);
        lcs.lookAtNoY(Main.world.player.lcs.position);
    }
    
    void draw() {
        Program.current.setUniform("worldMatrix", M);
        Program.current.setUniform("noiseOn", 1);
        m.draw();
        Program.current.setUniform("noiseOn", 0);
    }

    @Override
    Mesh getMesh() {
        return m;
    }

}
