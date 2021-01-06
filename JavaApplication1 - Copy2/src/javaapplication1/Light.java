package javaapplication1;

import math3d.vec3;

/**
 *
 * @author Matt
 */
public class Light {
    vec3 position;
    vec3 color;
    float positional;
    float A0, A1, A2;
    
    Light(){
        position = new vec3(90,60, 10);
        color = new vec3(1,1,1);
        A0 = 1;
        A1 = 1;
        A2 = 1;
        positional = 0;
    }
}
