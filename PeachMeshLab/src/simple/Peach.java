/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simple;

import math3d.vec3;

/**
 *
 * @author jhudson
 */
public class Peach extends GameObject{
    Sphere sph;
    Peach(vec3 pos, vec3 vel, vec3 g){
        super(pos,vel,g,-1);
        sph = new Sphere();
    }
    void draw(){
        //set uniforms (worldMatrix)
        sph.draw();
    }
    boolean isAlive(){
        return( position.y < -4 );
    }
}
