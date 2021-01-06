package main;

import static math3d.functions.add;
import static math3d.functions.mul;
import math3d.mat4;
import math3d.vec3;
import math3d.vec4;

/**
 *
 * @author jhudson
 */
public abstract class GameObject {
    enum State {
        ALIVE, DYING, DEAD
    }
    enum Type{
        PLAYER, ENEMY, WEAPON, POWERUP
    }
    
    vec3 velocity;  //null = doesn't move
    vec3 gravity;   //null = doesn't accelerate
    int lifeleft;   //negative = infinite
    LocalCoordinateSystem lcs;
    State state = State.ALIVE;
    static final int FADEOUT_TIME = 500;
    boolean isJumping = false;
    
    GameObject(vec3 p , vec3 v, vec3 g, int life  ) {
        lcs = new LocalCoordinateSystem(p);
        velocity = v;
        gravity = g;
        lifeleft = life;
    }
    
    void update(int elapsed){
        if( velocity != null )
            lcs.position = add(lcs.position, mul(elapsed,velocity));
        if( velocity != null && gravity != null )
            velocity = add(velocity, mul(elapsed,gravity));
        if(lifeleft > 0 ){
            lifeleft -= elapsed;
            if(lifeleft <= 0 ){
                lifeleft=0;
                state = State.DEAD;
            }
        }
    }
    
    void hit(Type colliderType){
        if( state == State.ALIVE ){
            state = State.DYING;
            lifeleft = FADEOUT_TIME;    
        }
    }
    
    void draw(){
        Mesh m = getMesh();
        if( m != null ){
            Program.current.setUniform("worldMatrix", lcs.getMatrix() );
            if( state == State.DYING ){
                Program.current.setUniform("alphaFactor",lifeleft*1.0f/FADEOUT_TIME);
            }
            else{
                Program.current.setUniform("alphaFactor",1.0f);
            }
            m.draw();
        }
    }

    abstract Mesh getMesh();
    
    Sphere getBoundingSphere(){
        Mesh m = getMesh();
        if( state != State.ALIVE || m == null){
            return new Sphere(new vec3(0,0,0), 0);
        }
        
        Sphere s = m.boundingSphere;
        mat4 M = lcs.getMatrix();
        vec4 c = new vec4(s.center,1.0);
        return new Sphere(mul(c,M).xyz(),s.radius);
    }
    
    void walk(float amt){
        lcs.walk(amt);
    }
    void strafe(float dx){
        lcs.strafe(dx);
    }
    void strafeY(float dx){
        lcs.strafeY(dx);
    }
    void turn(float amt){
        lcs.turn(amt);
    }
    void tilt(float amt){
        lcs.tilt(amt);
    }
    void turnY(float amt){
        lcs.turnY(amt);
    }
    void jump(){
        this.isJumping = true;
    }
}
