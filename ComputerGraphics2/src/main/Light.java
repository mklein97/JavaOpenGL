package main;

import math3d.vec3;
import math3d.vec4;

/**
 *
 * @author mattk
 */
public class Light {
    vec3 color;
    float angle;
    Camera cam;
    
    public Light(vec3 c, float a, Camera cam) {
        angle = a;
        this.cam = cam;
        color = c;
    }
}
