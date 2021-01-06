/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simple;

import math3d.vec3;
import static math3d.functions.*;
/**
 *
 * @author jhudson
 */
public abstract class GameObject {
    vec3 position;
    vec3 velocity;
    vec3 gravity;
    int life_left;
    GameObject( vec3 p, vec3 v, vec3 g, int maxlife ){
        position = p;
        velocity = v;
        gravity = g;
        life_left=maxlife;
    }
    void update(int elapsed){
        //position = position + elapsed * velocity;
        //velocity = velocity + elapsed * gravity;
        position = add( position, mul(elapsed,velocity));
        velocity = add( velocity, mul(elapsed,gravity));
        if( life_left > 0 ){
            life_left -= elapsed;
            if(life_left<0)
                life_left=0;
        }
    }
    boolean isAlive(){
        return life_left != 0;
    }
    abstract void draw();
}
