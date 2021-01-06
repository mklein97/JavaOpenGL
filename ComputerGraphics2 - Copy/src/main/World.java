/*
 */
package main;

import java.util.ArrayList;

/**
 *
 * @author jhudson
 */
public class World {
    Player player;
    ArrayList<GameObject> powerUps = new ArrayList<>();
    ArrayList<GameObject> enemies = new ArrayList<>();
    ArrayList<GameObject> weapons = new ArrayList<>();
    ArrayList<GameObject> npcs = new ArrayList<>();
    Map map;
    
    void setPlayer(Player p){
        player=p;
    }

    void addPowerup(GameObject o){
        powerUps.add(o);
    }
    void addNpc(GameObject o){
        npcs.add(o);
    }
    
    void addEnemy(GameObject o){
        enemies.add(o);
    }
    
    void addWeapon(GameObject o){
        weapons.add(o);
    }
    
    void setMap(Map m){
        map = m;
    }
    
    void checkCollisions(){
        
        Sphere playerSphere = player.getBoundingSphere();
        
        //check player-enemy collisions
        for (int i = 0; i < enemies.size(); ++i) {
            GameObject oi = enemies.get(i);
            if (oi.state == GameObject.State.ALIVE ){
                Sphere si = oi.getBoundingSphere();
                if (playerSphere.intersects(si)) {
                    player.hit(GameObject.Type.ENEMY);
                    oi.hit(GameObject.Type.PLAYER);
                }
            }
        }
        
        //check player-powerup collisions
        for(int i=0;i<powerUps.size();++i){
            GameObject oi = powerUps.get(i);
            if( oi.state == GameObject.State.ALIVE ){
                Sphere si = oi.getBoundingSphere();
                if( playerSphere.intersects(si) && player.health < 10){
                    player.hit(GameObject.Type.POWERUP);
                    oi.hit(GameObject.Type.PLAYER);
                }
            }
        }

        //check weapon-enemy collisions
        for(int j=0;j<enemies.size();++j){
            GameObject oj = enemies.get(j);
            Sphere sj = oj.getBoundingSphere();
            for(int i=0;i<weapons.size();++i){
                GameObject oi = weapons.get(i);
                Sphere si = oi.getBoundingSphere();
                if( si.intersects(sj)){
                    oj.hit(GameObject.Type.WEAPON);
                    oi.hit(GameObject.Type.ENEMY);
                    player.ghostsKilled += 1;
                }
            }
        }
    }
    
    void draw(){
        player.draw();
        for(GameObject o : powerUps )
            o.draw();
        for(GameObject o : enemies )
            o.draw();
        for(GameObject o : weapons )
            o.draw();
        for(GameObject o : npcs )
            o.draw();
        map.draw();
    }
        
    void update(int elapsed_ms){
        updateAll(powerUps,elapsed_ms);
        updateAll(enemies,elapsed_ms);
        updateAll(weapons,elapsed_ms);
        updateAll(npcs,elapsed_ms);
        player.update(elapsed_ms);
    }
    
    void updateAll(ArrayList<GameObject> A,int elapsed_ms){
        for(int i=0;i<A.size();++i){
            GameObject o = A.get(i);
            o.update(elapsed_ms);
            if( o.state == GameObject.State.DEAD ){
                GameObject X = A.remove(A.size()-1);
                if (i < A.size() - 1) {
                    A.set(i, X);
                    i--;    //need to make sure we don't skip any objects
                }
            }
        }
    }
}
