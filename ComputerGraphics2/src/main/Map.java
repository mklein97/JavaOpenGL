package main;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import static math3d.functions.mul;
import static math3d.functions.scaling;
import static math3d.functions.translation;
import math3d.mat4;
import math3d.vec3;

/**
 *
 * @author jhudson
 */
public class Map extends GameObject {
    static Mesh mesh;
    int mapSizeX, mapSizeY;
    mat4 M;
    vec3 playerStart;
    ArrayList<vec3> spawnerLocations = new ArrayList<>();
    ArrayList<vec3> pumpkinLocations = new ArrayList<>();
    ArrayList<vec3> treeLocations = new ArrayList<>();
    
    Map(){
        super(new vec3(0,0,0), new vec3(0,0,0), new vec3(0,0,0),-1);
        if(mesh == null)
            mesh = new Mesh("assets/map.obj.mesh");
        
        float scaleAmount=9;
        
        M = mul(scaling(scaleAmount,scaleAmount,scaleAmount),translation(lcs.position));
    
        FileInputStream fin;
        try{
            fin = new FileInputStream("assets/map.tmx");
        }catch(IOException e){
            throw new RuntimeException(e);
        }
        Scanner in = new Scanner(fin);
        in.useDelimiter("<END OF FILE>");
        String wholeThing = in.next();

        
        //<map version="1.0" orientation="orthogonal" renderorder="right-down" width="25" height="25" tilewidth="32" tileheight="32" backgroundcolor="#aaaaff" nextobjectid="4">
        Pattern p = Pattern.compile("<map [^>]* width=\"([0-9]+)\"\\s+height=\"([0-9]+)\"\\s+tilewidth=\"([0-9]+)\"\\s+tileheight=\"([0-9]+)\"");
        Matcher m = p.matcher(wholeThing);
        if( !m.find() )
            throw new RuntimeException("Did not find <map> pattern");
        
        //number of tiles horizontally
        int tilesX = Integer.parseInt(m.group(1));
        int tilesY = Integer.parseInt(m.group(2));
        int tileW = Integer.parseInt(m.group(3));
        int tileH = Integer.parseInt(m.group(4));
        
        //size of the entire map
        mapSizeX = tilesX * tileW;
        mapSizeY = tilesY * tileH;
        
        //<object id="3" gid="65" x="33" y="147" width="32" height="32"/>
        p = Pattern.compile("<object [^>]*gid=\"([0-9]+)\"\\s+x=\"([-0-9.]+)\"\\s+y=\"([-0-9.]+)\"");
        m = p.matcher(wholeThing);
        while(m.find()){
            int gid = Integer.parseInt(m.group(1));
            double x = Double.parseDouble(m.group(2));
            double y = Double.parseDouble(m.group(3));
            
            //x,y is tiled's coordinates for the lower left corner
            //of the object. We want to use the center of the object.
            //We know that tileW units of pixel (tiled) space = 1 unit of world space
            //before we scale it and similarly for tileH
            double tx = x/tileW;
            double ty = y/tileH;

            tx += 0.5;
            ty -= 0.5;
            
            //must scale up since ground was scaled too
            tx *= scaleAmount;
            ty *= scaleAmount;

            switch(gid){
                case 61:
                    //player position
                    playerStart = new vec3(tx,4,ty);
                    break;
                case 62:
                    spawnerLocations.add(new vec3(tx,2,ty));
                    break;
                case 63:
                    pumpkinLocations.add(new vec3(tx,1,ty));
                    break;
                case 64:
                    treeLocations.add(new vec3(tx,0,ty));
                    break;
                default:
                    throw new RuntimeException("Unknown object gid: "+gid);
            }
        }
    }
    
    @Override
    void hit(Type t){
        //being hit by anything has no effect
    }
    
    @Override
    void draw(){
        Program.current.setUniform("worldMatrix", M);
        mesh.draw();
    }

    @Override
    Mesh getMesh() {
        return mesh;
    }
}