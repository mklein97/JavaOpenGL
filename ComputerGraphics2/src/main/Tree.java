/*
 */
package main;

import java.nio.file.Paths;
import math3d.vec3;

/**
 *
 * @author jhudson
 */
public class Tree extends GameObject{
    static Mesh mesh;
    public Tree(vec3 p) {
        super(p,new vec3(0,0,0), new vec3(0,0,0), -1);
        if( mesh == null )
            mesh = new Mesh(Paths.get("assets","tree2.obj.mesh").toString());
    }

    @Override
    Mesh getMesh() {
        return mesh;
    }
        
}
