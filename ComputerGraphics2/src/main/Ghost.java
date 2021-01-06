package main;

import java.util.Random;
import static math3d.functions.add;
import static math3d.functions.length;
import static math3d.functions.mul;
import static math3d.functions.randrange;
import static math3d.functions.sub;
import math3d.vec3;

/**
 *
 * @author jhudson
 */
public class Ghost extends GameObject{
    static Mesh[] meshes;
    int whichMesh;
    static Random R = new Random();
    vec3 destination;
    float sinepos;

    Ghost(vec3 pos){
        super(pos,null,null,-1);
        destination = pos.xyz();    //make a local copy
        //destination = add(pos,new vec3(5,0,0));
        
        if( meshes == null ){
            String[] x = new String[]{ "ghost1.obj.mesh","ghost2.obj.mesh"};
            meshes = new Mesh[x.length];
            for(int i=0;i<x.length;++i){
                meshes[i] = new Mesh("assets/"+x[i]);
            }
        }
        whichMesh = R.nextInt(2);
    }
    
    float debug;
    
    @Override
    void update(int elapsed){

        if( lifeleft >= 0 ){
            lifeleft -= elapsed;
            if( lifeleft <= 0 ){
                lifeleft=0;
                state = State.DEAD;
            }
        }
        
        if( state != State.ALIVE )
            return;
        
        //see if we need to turn our coordinate system
        float turnAmt = lcs.turnTowards(destination,elapsed*0.01f);
        if( turnAmt > 0.01 || turnAmt < -0.01 )
            return;
        
        //vec3 v = sub(destination,lcs.position);
        float le = length(sub(lcs.position.xz(),destination.xz()));
        
        float amtToMove = elapsed * 0.01f;
        float amtToMove2 = elapsed * 0.02f;
        if(le < amtToMove || le < amtToMove2) {
            //lcs.position = destination;
            //choose a new destination
            destination = new vec3(
                    randrange(lcs.position.x-10, lcs.position.x+10),
                    0,
                    randrange(lcs.position.z-10, lcs.position.z+10));
        }
        else {
            //amtToMove <= distance from position to destination
            vec3 v = mul(amtToMove,lcs.facing).xyz();
            vec3 v2 = mul(amtToMove2,lcs.facing).xyz();
            v.y = 0; v2.y = 0;   //ignore y in these calculations
            //v = normalize(v);
            if (whichMesh == 1)
                lcs.position = add(lcs.position, v);
            else
                lcs.position = add(lcs.position, v2);
        }
         
        sinepos += 0.04*elapsed;
        while(sinepos > 2.0 * Math.PI )
            sinepos -= 2.0*Math.PI;
        
        lcs.position.y = (float)Math.sin(sinepos);
        
    }
    
    @Override
    Mesh getMesh() {
        return meshes[whichMesh];
    }
  
}
