package main;

import math3d.vec3;

/**
 *
 * @author jhudson
 */
public class JackOLantern  extends GameObject {
    static Mesh m;
    public JackOLantern(vec3 p) {
        super(p, null,null,-1);
        if(m==null)
            m = new Mesh("assets/jackolantern.obj.mesh");
    }
    
    void update(int elapsed){
        super.update(elapsed);
        lcs.lookAtNoY(Main.world.player.lcs.position);
    }

    @Override
    Mesh getMesh() {
        return m;
    }

}
