package main;

import static math3d.functions.dot;
import static math3d.functions.sub;
import math3d.vec3;


/**
 *
 * @author jhudson
 */
public class Sphere {

    vec3 center;
    float radius;

    Sphere(vec3 c, float r) {
        center = c.xyz();
        radius = r;
    }

    boolean intersects(Sphere s2){
        vec3 v = sub(center,s2.center);
        float d2 = dot(v,v);
        float r2 = radius + s2.radius;
        r2*=r2;
        return d2 <= r2;
    }
}
