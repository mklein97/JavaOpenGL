package main;

import static math3d.functions.mul;
import math3d.mat4;
import math3d.vec3;
import math3d.vec4;

/**
 *
 * @author jhudson
 */
public class Player extends GameObject{
    float bloodFactor = 30.0f;
    int health=10;
    int timeBeforeCanTakeDamage=0;
    int ghostsKilled = 0;
    
    public Player(vec3 pos, vec3 coi){
        super(pos,null,null,-1);
        lcs.turnTowards(coi, 9999);
    }
    
    @Override
    Sphere getBoundingSphere(){
        mat4 M = lcs.getMatrix();
        vec4 c = new vec4(0,0,0,1.0);
        return new Sphere(mul(c,M).xyz(),3);
    }
    
    @Override
   void hit(Type colliderType){
        if( colliderType == Type.POWERUP ){
            health++;
        }
        else if( colliderType == Type.ENEMY ){
            if( timeBeforeCanTakeDamage == 0 ){
                bloodFactor = 30.0f;
                health--;
                if(health<0)
                    health=0;
                timeBeforeCanTakeDamage = 500;
            }
        }
   }
    
    @Override
    void update(int elapsed_ms){
        super.update(elapsed_ms);
        timeBeforeCanTakeDamage -= elapsed_ms;
        if( timeBeforeCanTakeDamage < 0 )
            timeBeforeCanTakeDamage = 0;
        bloodFactor -= 0.03f * elapsed_ms;
        if (bloodFactor < 0) 
            bloodFactor = 0;
    }
    
 
    
    @Override
    Mesh getMesh() {
        return null;
    }
    
}
