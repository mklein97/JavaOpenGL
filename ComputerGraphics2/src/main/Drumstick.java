package main;

import static math3d.functions.axisRotation;
import static math3d.functions.cross;
import static math3d.functions.length;
import static math3d.functions.mul;
import static math3d.functions.normalize;
import math3d.vec3;

/**
 *
 * @author jhudson
 */
public class Drumstick extends GameObject {
    static Mesh M;
    float rotAngle;
    vec3 rotAxis;
    float rotSpeed;
    
    Drumstick(vec3 p, vec3 v, vec3 g){
        super(p,v,g,4000);
        if( M==null)
            M=new Mesh("assets/drumstick.obj.mesh");
        float le = length(v);
        rotSpeed = le;
        v = mul(1.0/le,v);
        rotAxis = normalize(cross(new vec3(0,1,0),v));
    }
    
    @Override
    public void update(int elapsed){
        super.update(elapsed);
        rotAngle += rotSpeed*Math.PI;
        while( rotAngle > 2.0*Math.PI )
            rotAngle -= 2.0*Math.PI;
    }

    @Override
    Mesh getMesh() {
        return M;
    }
    
    void draw(){
        Program.current.setUniform("worldMatrix",
                mul(
                        axisRotation(rotAxis,rotAngle), 
                        lcs.getMatrix() 
                )
        );
        M.draw();
    }
    

}
