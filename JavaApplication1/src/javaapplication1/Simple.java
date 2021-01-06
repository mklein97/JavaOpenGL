package javaapplication1;

import com.sun.jna.Pointer;
import static etgg.SDL.*;
import static etgg.GL.*;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.TreeSet;
import static math3d.functions.translation;
import math3d.vec3;
import java.util.concurrent.ThreadLocalRandom;

/**
 *
 * @author Matt Klein
 */
public class Simple {

    static SDL_Window win;
    static TreeSet<Integer> keysdown = new TreeSet<>();
    static int health = 5;
    static float tx, ty, texslice;
    static float alpha = 1.0f;
    static Program prog, prog2, progG, skyboxProg;
    static Peach p;
    static Tree tree1, tree2, tree3, tree4;
    static Ground ground;
    static Ghost1 ghost1;
    static Ghost2 ghost2;
    static Pumpkin pump1, pump2, pump3;
    static FairyRing fring1, fring2;
    static float timer = 4000;
    static ArrayList<GameObject> objects = new ArrayList();
    static ArrayList<Ghost1> ghost1s = new ArrayList();
    static ArrayList<Ghost2> ghost2s = new ArrayList();
    static ArrayList<Ghost1> deletedghost1s = new ArrayList();
    static ArrayList<Ghost2> deletedghost2s = new ArrayList();
    static ArrayList<Pumpkin> pumpkins = new ArrayList();
    public static Camera cam = new Camera();
    static Light[] lights = new Light[1];
    static Mesh unitCube;
    static TextureCube skyTexture;

    static void initGL() {
        SDL_Init(SDL_INIT_VIDEO);
        SDL_GL_SetAttribute(SDL_GL_CONTEXT_PROFILE_MASK, SDL_GL_CONTEXT_PROFILE_CORE);
        SDL_GL_SetAttribute(SDL_GL_DEPTH_SIZE, 24);
        SDL_GL_SetAttribute(SDL_GL_STENCIL_SIZE, 8);
        SDL_GL_SetAttribute(SDL_GL_CONTEXT_MAJOR_VERSION, 3);
        SDL_GL_SetAttribute(SDL_GL_CONTEXT_MINOR_VERSION, 3);
        SDL_GL_SetAttribute(SDL_GL_CONTEXT_FLAGS, SDL_GL_CONTEXT_DEBUG_FLAG);
        win = SDL_CreateWindow("ETGG", 20, 20, 1280, 720, SDL_WINDOW_OPENGL);
        SDL_GL_CreateContext(win);

        glDebugMessageCallback(
                (int source, int type, int id, int severity, int length, String message, Object param)
                -> {
            if (id == 131204) {
                return;
            }
            System.out.println(message);
            System.out.println(id);
        },
                null
        );

        glDebugMessageControl(GL_DONT_CARE, GL_DONT_CARE, GL_DONT_CARE, 0, null, true);
        glEnable(GL_DEBUG_OUTPUT_SYNCHRONOUS);
        glEnable(GL_DEBUG_OUTPUT);
        glEnable(GL_DEPTH_TEST);
        glEnable(GL_BLEND);
        glDepthFunc(GL_LEQUAL);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
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

        texslice += elapsed * 0.1f;
        if (texslice > 100) {
            texslice -= 100;
        }

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

        timer -= elapsed;
        if (timer < 0) {
            ghost1s.add(new Ghost1(new vec3(14, -5, 10), new vec3(0, 0, 0), new vec3(0, 0, 0)));
            ghost2s.add(new Ghost2(new vec3(20, -5, 6), new vec3(0, 0, 0), new vec3(0, 0, 0)));
            timer = 4000;
        }

        Iterator<Ghost1> ghostit = ghost1s.iterator();
        while (ghostit.hasNext()) {
            Ghost1 cur = ghostit.next();
            if (cur.needsdeleted) {
                deletedghost1s.add(cur);
                ghostit.remove();
                health -= 1;
                System.out.println("Health: " + health);
            }
            for (GameObject p : objects) {
                if (cur.lcs.position.x <= p.lcs.position.x + 1 && cur.lcs.position.x >= p.lcs.position.x - 1 && cur.lcs.position.y <= p.lcs.position.y && cur.lcs.position.y >= p.lcs.position.y - 20 && cur.lcs.position.z <= p.lcs.position.z + 1 && cur.lcs.position.z >= p.lcs.position.z - 1) {
                    deletedghost1s.add(cur);
                    ghostit.remove();
                }
            }
        }
        
        Iterator<Ghost1> dGhostit = deletedghost1s.iterator();
        while (dGhostit.hasNext()){
            Ghost1 cur = dGhostit.next();
            if (cur.needsdeleted && cur.alpha > 0.0f)
                cur.needsdeleted = false;
            if (cur.alpha <= 0.0f) {
                cur.needsdeleted = true;
            }
            if (cur.needsdeleted)
                dGhostit.remove();
        }

        Iterator<Ghost2> ghostit2 = ghost2s.iterator();
        while (ghostit2.hasNext()) {
            Ghost2 cur = ghostit2.next();
            if (cur.needsdeleted) {
                deletedghost2s.add(cur);
                ghostit2.remove();
                health -= 1;
                System.out.println("Health: " + health);
            }
            for (GameObject p : objects) {
                if (cur.lcs.position.x <= p.lcs.position.x + 1 && cur.lcs.position.x >= p.lcs.position.x - 1 && cur.lcs.position.y <= p.lcs.position.y && cur.lcs.position.y >= p.lcs.position.y - 20 && cur.lcs.position.z <= p.lcs.position.z + 1 && cur.lcs.position.z >= p.lcs.position.z - 1) {
                    deletedghost2s.add(cur);
                    ghostit2.remove();
                }
            }
        }
        
        Iterator<Ghost2> dGhostit2 = deletedghost2s.iterator();
        while (dGhostit2.hasNext()){
            Ghost2 cur = dGhostit2.next();
            if (cur.needsdeleted && cur.alpha > 0.0f)
                cur.needsdeleted = false;
            if (cur.alpha <= 0.0f) {
                cur.needsdeleted = true;
            }
            if (cur.needsdeleted)
                dGhostit2.remove();
        }

        Iterator<Pumpkin> pumpit = pumpkins.iterator();
        while (pumpit.hasNext()) {
            Pumpkin cur = pumpit.next();
            if (cur.needsdeleted) {
                pumpit.remove();
                health += 1;
            }
        }

        for (Ghost1 g1 : ghost1s) {
            if (!g1.needsdeleted) 
                g1.update(elapsed, .15f, cam.eye);
        }
        for (Ghost2 g2 : ghost2s) {
            if (!g2.needsdeleted) 
                g2.update(elapsed, .15f, cam.eye);
        }
        for (Pumpkin p : pumpkins) {
            p.update(cam.eye);
            p.lcs.turnTowards(cam.eye, 360.0f);
        }
       
        for (Ghost1 g1 : deletedghost1s) {
            g1.alpha -= elapsed * 0.001f;
            if (g1.alpha <= 0.0f) {
                g1.alpha = 0.0f;
            }

            Program.current.setUniform("alpha", g1.alpha);
        }
        
        for (Ghost2 g2 : deletedghost2s) {
            g2.alpha -= elapsed * 0.001f;
            if (g2.alpha <= 0.0f) {
                g2.alpha = 0.0f;
            }

            Program.current.setUniform("alpha", g2.alpha);
        }
        
        if (health <= 0) {
            System.out.println("You died!");
            System.exit(0);
        }

        fring1.update(cam.eye);
        fring2.update(cam.eye);
    }

    static void draw() {
        //draw frame
        prog.use();
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        cam.draw();
        for (int i = 0; i < lights.length; ++i) {
            Program.current.setUniform("lights[" + i + "].position", lights[i].position);
            Program.current.setUniform("lights[" + i + "].color", lights[i].color);
            Program.current.setUniform("lights[" + i + "].positional", 0);
            Program.current.setUniform("lights[" + i + "].A0", lights[i].A0);
            Program.current.setUniform("lights[" + i + "].A1", lights[i].A1);
            Program.current.setUniform("lights[" + i + "].A2", lights[i].A2);
        }

        //Program.current.setUniform("p", 1.2f);
        //Program.current.setUniform("objColor", new vec3(0.976, 0.439, 0.203));
        for (GameObject obj : objects) {
            obj.draw();
        }

        for (Pumpkin p : pumpkins) {
            p.draw();
        }

        for (Ghost1 g1 : ghost1s) {
            g1.draw();
        }
        for (Ghost2 g1 : ghost2s) {
            g1.draw();
        }

        tree1.draw();
        tree4.draw();
        tree3.draw();
        tree2.draw();

        ground.draw();
        
        skyboxProg.use();
        cam.draw();
        Program.current.setUniform("tex", skyTexture);
        unitCube.draw();

        prog2.use();
        Program.current.setUniform("texSlice", texslice);
        cam.draw();

        fring1.draw();
        fring2.draw();

        progG.use();
        cam.draw();
        if (deletedghost1s.size() > 0) {
            for (int i = 0; i < lights.length; ++i) {
                Program.current.setUniform("lights[" + i + "].position", lights[i].position);
                Program.current.setUniform("lights[" + i + "].color", lights[i].color);
                Program.current.setUniform("lights[" + i + "].positional", 0);
                Program.current.setUniform("lights[" + i + "].A0", lights[i].A0);
                Program.current.setUniform("lights[" + i + "].A1", lights[i].A1);
                Program.current.setUniform("lights[" + i + "].A2", lights[i].A2);
            }

            for (Ghost1 g1 : deletedghost1s) {
                Program.current.setUniform("alpha", g1.alpha);
                g1.draw();
            }
          
        }
        
        if (deletedghost2s.size() > 0) {
            for (int i = 0; i < lights.length; ++i) {
                Program.current.setUniform("lights[" + i + "].position", lights[i].position);
                Program.current.setUniform("lights[" + i + "].color", lights[i].color);
                Program.current.setUniform("lights[" + i + "].positional", 0);
                Program.current.setUniform("lights[" + i + "].A0", lights[i].A0);
                Program.current.setUniform("lights[" + i + "].A1", lights[i].A1);
                Program.current.setUniform("lights[" + i + "].A2", lights[i].A2);
            }

            for (Ghost2 g2 : deletedghost2s) {       
                Program.current.setUniform("alpha", g2.alpha);
                g2.draw();
            }
          
        }
        
        

        SDL_GL_SwapWindow(win);
    }

    public static void main(String[] args) {

        initGL();

        unitCube = new Mesh(Paths.get("assets", "cube.obj.mesh"));
        skyTexture = new ImageTextureCube(
                "assets/0001.png.jpg",
                "assets/0002.png.jpg",
                "assets/0004.png.jpg",
                "assets/0003.png.jpg",
                "assets/0005.png.jpg",
                "assets/0006.png.jpg"
        );
        prog = new Program("prog1.vs", "prog1.fs");
        prog2 = new Program("prog1.vs", "prog2.fs");
        progG = new Program("prog1.vs", "progG.fs");
        skyboxProg = new Program("skyprog.vs", "skyprog.fs");

        objects.add(new Peach(new vec3(1000, 1000, 1000), new vec3(0, 0, 0), new vec3(0, 0, 0)));
        ghost1s.add(new Ghost1(new vec3(14, -5, 10), new vec3(0, 0, 0), new vec3(0, 0, 0)));
        ghost2s.add(new Ghost2(new vec3(20, -5, 6), new vec3(0, 0, 0), new vec3(0, 0, 0)));

        tree1 = new Tree(new vec3(0, -5, -10), new vec3(0, 0, 0), new vec3(0, 0, 0));
        tree2 = new Tree(new vec3(10, -5, 0), new vec3(0, 0, 0), new vec3(0, 0, 0));
        tree3 = new Tree(new vec3(-10, -5, 0), new vec3(0, 0, 0), new vec3(0, 0, 0));
        tree4 = new Tree(new vec3(0, -5, 10), new vec3(0, 0, 0), new vec3(0, 0, 0));

        ground = new Ground(new vec3(0, -5, 0), new vec3(0, 0, 0), new vec3(0, 0, 0));

        pumpkins.add(new Pumpkin(new vec3(4, -2, 0), new vec3(0, 0, 0), new vec3(0, 0, 0)));
        pumpkins.add(new Pumpkin(new vec3(-3, -5, 9), new vec3(0, 0, 0), new vec3(0, 0, 0)));
        pumpkins.add(new Pumpkin(new vec3(0, -5, -1), new vec3(0, 0, 0), new vec3(0, 0, 0)));

        fring1 = new FairyRing(new vec3(14, 0, 10), new vec3(0, 0, 0), new vec3(0, 0, 0));
        fring2 = new FairyRing(new vec3(20, 0, 6), new vec3(0, 0, 0), new vec3(0, 0, 0));

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
