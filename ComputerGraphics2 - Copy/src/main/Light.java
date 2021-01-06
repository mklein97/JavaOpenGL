package main;

import math3d.vec2;
import math3d.vec3;
import main.Camera;

/**
 *
 * @author mattk
 */
public class Light {
    vec3 position;
    float angle;
    Camera camera;
    
    public Light(vec3 p, float a, Camera c) {
        position = p;
        angle = a;
        camera = c;
    }
}
