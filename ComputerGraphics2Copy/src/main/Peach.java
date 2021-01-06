/*
 */
package main;

import java.nio.file.Paths;
import math3d.vec3;

/**
 *
 * @author jhudson
 */
public class Peach extends GameObject {
    static Mesh mesh;
    Peach(vec3 p , vec3 v, vec3 g) {
        super(p,v,g,5000);
        if( mesh == null )
            mesh = new Mesh(Paths.get("assets","peach.obj.mesh").toString());
    }

    @Override
    Mesh getMesh() {
        return mesh;
    }
}
