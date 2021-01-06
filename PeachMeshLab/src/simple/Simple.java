package simple;

import static etgg.SDL.*;
import static etgg.GL.*;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.TreeSet;
import static math3d.functions.translation;

/**
 *
 * @author jhudson
 */
public class Simple {

    static SDL_Window win;
    static TreeSet<Integer> keysdown = new TreeSet<>();
    static float tx, ty;
    static Mesh foo;
    static Program prog;
    static ArrayList<GameObject> objects = new ArrayList<>();
    
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
                //SDL_MouseMotionEvent m = (SDL_MouseMotionEvent)ev.readField("motion");
                //use m.x and m.y
            } else if (ev.type == SDL_MOUSEBUTTONDOWN) {
                SDL_MouseButtonEvent b = (SDL_MouseButtonEvent) ev.readField("button");
                //objects.add( new Peach(...));
            }
        }
    }

    static void update(int elapsed) {
        if (keysdown.contains(SDLK_SPACE)) {
            tx += 0.0001f * elapsed;
        }
        ty += 0.01f;
        
        int x=0;
        
        for(int i=0;i<objects.size();++i){
            objects.get(i).update(elapsed);
            if( !objects.get(i).isAlive()){
                GameObject tmp = objects.remove(objects.size()-1);
                if( i != objects.size()-1 ){
                    objects.set(i,tmp);
                    i--;
                }
            }
        }
    }

    static void draw() {
        //draw frame
        glClear(GL_COLOR_BUFFER_BIT);

        Program.current.setUniform("foo", 1.2f);

        Program.current.setUniform(
                "worldMatrix",
                translation(tx, 0, 0));

        foo.draw();

        Program.current.setUniform(
                "worldMatrix",
                translation(-0.3f, ty, 0));
        foo.draw();

        for(GameObject obj : objects)
            obj.draw();
        
        SDL_GL_SwapWindow(win);
    }

    public static void main(String[] args) {

        initGL();

        //foo = new Pyramid();
        foo = new Mesh(Paths.get("assets","smiley.obj.mesh"));
        
        prog = new Program("prog1.vs", "prog1.fs");

        prog.use();

        glClearColor(0.2f, 0.4f, 0.6f, 1.0f);

        long accumulated_update_time = 0;
        final int UPDATE_QUANTUM_MS = 5;
        final long UPDATE_QUANTUM_NS = UPDATE_QUANTUM_MS * 1000000;
        long last,now,elapsed;
        last = System.nanoTime();
        while (true) {
            handleEvents();
            now = System.nanoTime();
            elapsed = now-last;
            last=now;
            accumulated_update_time += elapsed;
            while( accumulated_update_time >= UPDATE_QUANTUM_NS){
                update(UPDATE_QUANTUM_MS);
                accumulated_update_time -= UPDATE_QUANTUM_NS;
            }
            draw();
        }
    }
}
