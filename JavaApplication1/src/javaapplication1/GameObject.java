package javaapplication1;

import math3d.vec3;
import static math3d.functions.*;
import static javaapplication1.Simple.prog;

/**
 *
 * @author jhudson
 */
public abstract class GameObject {

    vec3 position;
    vec3 velocity;
    vec3 gravity;
    int life_left;
    Mesh mesh;  //needs to be set by subclass.
    LocalCoordinateSystem lcs;

    GameObject(vec3 p, vec3 v, vec3 g, int maxlife) {
        lcs = new LocalCoordinateSystem(p);
        velocity = v;
        gravity = g;
        life_left = maxlife;
    }

    void update(int elapsed) {
        if (velocity != null) {
            lcs.position = add(lcs.position, mul(elapsed, velocity));
        }
        if (velocity != null && gravity != null) {
            velocity = add(velocity, mul(elapsed, gravity));
        }
        if (life_left > 0) {
            life_left -= elapsed;
            if (life_left < 0) {
                life_left = 0;
            }
        }
    }

    boolean isAlive() {
        return life_left != 0;
    }

    void draw() {
        if (mesh != null) {
            prog.setUniform("worldMatrix", lcs.getMatrix());
            mesh.draw();
        }
    }
}