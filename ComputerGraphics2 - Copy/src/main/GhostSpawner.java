package main;

import java.util.ArrayList;
import java.util.Random;
import static math3d.functions.mul;
import static math3d.functions.scaling;
import math3d.vec3;
import math3d.vec2;

/**
 *
 * @author jhudson
 */
public class GhostSpawner extends GameObject {
    int timeToNextSpawn=0;
    static Random R = new Random();
    static BillboardGroup M;
    static Mesh m2;
    Texture2DArray rings = new ImageTexture2DArray("assets/rings.zip");
    float frameNumber;
    float fasterCounter;
    GhostSpawner(vec3 p, vec2 size) {
        super(p,null,null,-1);
        if(M == null )
            M = new BillboardGroup(rings, 20);
        M.addBillboard(p, size);
    }

    @Override
    void update(int elapsed) {
        timeToNextSpawn -= elapsed;
        if( timeToNextSpawn <= 0 ){
            timeToNextSpawn = R.nextInt(10000);
            float x = 0; 
            float z = -5; 
            float y = 3.0f;
            Main.world.addEnemy(new Ghost( this.lcs.position) );
        }
        frameNumber = (float)(frameNumber + 0.01*elapsed);
        if( frameNumber > 100.0 )
            frameNumber -= 100.0;
    }

    @Override
    void draw(){
        Program.current.setUniform("worldMatrix",
                mul(scaling(2,2,2),lcs.getMatrix()));
        Program.current.setUniform("texFrameNumber", frameNumber);
        M.draw();
        Program.current.setUniform("texFrameNumber",0);
    }
    
    @Override
    Sphere getBoundingSphere(){
        //does not participate in collisions
        return null;
    }
    
    @Override
    Mesh getMesh() {
        return m2;
    }
    
    
}
