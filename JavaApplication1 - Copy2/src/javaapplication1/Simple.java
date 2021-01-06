package javaapplication1;

import com.sun.jna.Pointer;
import static etgg.SDL.*;
import static etgg.GL.*;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.TreeSet;
import static math3d.functions.translation;
import math3d.vec3;
import java.util.concurrent.ThreadLocalRandom;

/**
 *
 * @author jhudson
 */
public class Simple {

    static SDL_Window win;
    static TreeSet<Integer> keysdown = new TreeSet<>();
    static float tx, ty, texSlice;
    static Program prog;
    static Peach p;
    static Tree tree1, tree2, tree3, tree4;
    static Ground ground;
    static Ghost1 ghost1;
    static Ghost2 ghost2;
    static Pumpkin pump1, pump2, pump3;
    static ArrayList<GameObject> objects = new ArrayList<>();
    public static Camera cam = new Camera();
    static Light[] lights = new Light[1];
    public static vec3 ghost1pos = new vec3(0,0,0);

    static void initGL() {
        SDL_Init(SDL_INIT_VIDEO);
        SDL_GL_SetAttribute(SDL_GL_CONTEXT_PROFILE_MASK, SDL_GL_CONTEXT_PROFILE_CORE);
        SDL_GL_SetAttribute(SDL_GL_DEPTH_SIZE, 24);
        SDL_GL_SetAttribute(SDL_GL_STENCIL_SIZE, 8);
        SDL_GL_SetAttribute(SDL_GL_CONTEXT_MAJOR_VERSION, 3);
        SDL_GL_SetAttribute(SDL_GL_CONTEXT_MINOR_VERSION, 3);
        SDL_GL_SetAttribute(SDL_GL_CONTEXT_FLAGS, SDL_GL_CONTEXT_DEBUG_FLAG);
        win = SDL_CreateWindow("ETGG", 20, 20, 512, 512, SDL_WINDOW_OPENGL);
        SDL_GL_CreateContext(win);

        glDebugMessageCallback(
                (int source, int type, int id, int severity, int length, String message, Object param)
                -> {
            System.out.println(message);
        },
                null
        );

        glDebugMessageControl(GL_DONT_CARE, GL_DONT_CARE, GL_DONT_CARE, 0, null, true);
        glEnable(GL_DEBUG_OUTPUT_SYNCHRONOUS);
        glEnable(GL_DEBUG_OUTPUT);
        glEnable(GL_DEPTH_TEST);
        glDepthFunc(GL_LEQUAL);
    }

    static void handleEvents() {
        SDL_Event ev = new SDL_Event();
        while (true) {
            if (0 == SDL_PollEvent(ev)) {
                break;
            }
            if (ev.type == SDL_QUIT) {
                System.exit(0);
            } else if (ev.type == SDL_KEYDOWN) {
                SDL_KeyboardEvent k = (SDL_KeyboardEvent) ev.readField("key");
                keysdown.add(k.keysym.sym);
            } else if (ev.type == SDL_KEYUP) {
                SDL_KeyboardEvent k = (SDL_KeyboardEvent) ev.readField("key");
                keysdown.remove(k.keysym.sym);
            } else if (ev.type == SDL_MOUSEMOTION) {
                SDL_MouseMotionEvent m = (SDL_MouseMotionEvent) ev.readField("motion");
                SDL_SetRelativeMouseMode(1);
                cam.turn(m.xrel * -.009f);
            } else if (ev.type == SDL_MOUSEBUTTONDOWN) {
                SDL_MouseButtonEvent b = (SDL_MouseButtonEvent) ev.readField("button");
                objects.add(new Peach(cam.eye, cam.w.mul(-0.09f), new vec3(0, -.0005, 0)));
            }
        }
    }
    
    
    static void update(int elapsed) {

        if (keysdown.contains(SDLK_w)) {
            cam.walk(elapsed * 0.01f);
        }

        if (keysdown.contains(SDLK_s)) {
            cam.walk(elapsed * -0.01f);
        }

        if (keysdown.contains(SDLK_a)) {
            cam.strafe(elapsed * -0.01f, elapsed * 0);
        }

        if (keysdown.contains(SDLK_d)) {
            cam.strafe(elapsed * 0.01f, elapsed * 0);
        }

        if (keysdown.contains(SDLK_0)) {
            System.exit(0);
        }
        
        ghost1.update(elapsed, .0015f);
        ghost2.update(elapsed, .0015f);
        
        texSlice += elapsed * 0.1f;
        if( texSlice > 100 )
            texSlice -= 100;

        for (int i = 0; i < objects.size(); ++i) {
            objects.get(i).update(elapsed);
            if (!objects.get(i).isAlive()) {
                GameObject tmp = objects.remove(i);
                if (i == objects.size() - 1) {
                    objects.set(i, tmp);
                    i--;
                }
            }
        }
    }

    static void draw() {
        //draw frame
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        cam.draw();
        Program.current.setUniform("lightPos", new vec3(50,90,20));
        Program.current.setUniform("texSlice", texSlice);

        //Program.current.setUniform("p", 1.2f);
        //Program.current.setUniform("objColor", new vec3(0.976, 0.439, 0.203));

        for (GameObject obj : objects) {
            obj.draw();
        }

        tree1.draw();
        tree4.draw();
        tree3.draw();
        tree2.draw();
        
        ground.draw();
        
        ghost1.draw();
        ghost2.draw();
        
        pump1.lcs.turnTowards(cam.eye, 360.0f);
        pump2.lcs.turnTowards(cam.eye, 360.0f);
        pump3.lcs.turnTowards(cam.eye, 360.0f);
        pump1.draw();
        pump2.draw();
        pump3.draw();

        SDL_GL_SwapWindow(win);
    }

    public static void main(String[] args) {

        initGL();

        prog = new Program("prog1.vs", "prog1.fs");
        
        objects.add(new Peach(new vec3(1000,1000,1000), new vec3(0,0,0), new vec3(0,0,0)));
        
        tree1 = new Tree(new vec3(0,-5,-10), new vec3(0,0,0), new vec3(0,0,0));
        tree2 = new Tree(new vec3(10,-5,0), new vec3(0,0,0), new vec3(0,0,0));
        tree3 = new Tree(new vec3(-10,-5,0), new vec3(0,0,0), new vec3(0,0,0));
        tree4 = new Tree(new vec3(0,-5,10), new vec3(0,0,0), new vec3(0,0,0));

        ground = new Ground(new vec3(0,-5,0), new vec3(0,0,0), new vec3(0,0,0));
        
        ghost1 = new Ghost1(new vec3(7,-5,10), new vec3(0,0,0), new vec3(0,0,0));
        ghost2 = new Ghost2(new vec3(0,-5,0), new vec3(0,0,0), new vec3(0,0,0));
        
        pump1 = new Pumpkin(new vec3(4,-2,0), new vec3(0,0,0), new vec3(0,0,0));
        pump2 = new Pumpkin(new vec3(-3,-5,9), new vec3(0,0,0), new vec3(0,0,0));
        pump3 = new Pumpkin(new vec3(0,-5,-1), new vec3(0,0,0), new vec3(0,0,0));
        
        prog.use();

        lights[0] = new Light();
        
        glClearColor(0.074f, 0.184f, 0.545f, 1.0f);

        long last = System.nanoTime();
        int accumulated_update_time = 0;
        final int UPDATE_QUANTUM = 5;
        while (true) {
            handleEvents();
            long now = System.nanoTime();
            int elapsed = (int) ((now - last));
            last = now;
            accumulated_update_time += elapsed;
            while (accumulated_update_time >= (UPDATE_QUANTUM * 1000000)) {
                update(UPDATE_QUANTUM);
                accumulated_update_time -= (UPDATE_QUANTUM * 1000000);
            }
            draw();
        }
    }
}
