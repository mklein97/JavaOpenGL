package main;

import static etgg.SDL.*;
import static etgg.GL.*;
import java.util.TreeSet;
import java.util.ArrayList;
import java.util.Collections;
import static math3d.functions.add;
import static math3d.functions.mul;
import math3d.vec2;
import math3d.vec3;
import math3d.vec4;

/**
 *
 * @author jhudson
 */
public class Main {
    static final float FOG_NEAR = 50.0f;
    static final float FOG_FAR = 100.0f;
    static final float FOG_DELTA = FOG_FAR - FOG_NEAR;
    static final int WIN_WIDTH = 1024, WIN_HEIGHT = 1024;
    static final vec2 SCREEN_SIZE_FACTOR = new vec2(1.0f / WIN_WIDTH,
            1.0f / WIN_HEIGHT);
    static boolean paused = false;
    static SDL_Window win;
    static World world;
    static TreeSet<Integer> keys = new TreeSet<>();
    static Program deferredPass1Program, deferredPass2Program;
    static Program overlayProgram;
    static Program skyProgramPass1;
    static Program textProgram;
    static Program fboProgram;
    static Program shadowProg;
    static Square square;
    static Camera camera = new Camera();
    static boolean mouseButtonDown = false;
    static boolean currentWeapon = true;
    static float bulletCharge = 0;
    static boolean needBullet = false;
    static boolean recording = false;
    static Texture2DArray pauseText;
    static Texture2DArray xhair;
    static Texture2DArray drumstickIcon, drumstickIconDark;
    static Texture2DArray peachIcon, peachIconDark;
    static Texture2DArray heart;
    static Texture2DArray yellow;
    static Texture2DArray ramp;
    static DataTexture2DArray noiseP;
    static TextureCube skytexture;
    static Text fpsText, ghostNumText;
    static int frames = 0;
    static double startTime = 0;
    static double fps;
    static Mesh cube;
    static Framebuffer2D fbo, gbuffer, shadowbuffer;
    static UnitSquare usq;
    static ArrayList<Light> lights = new ArrayList();
    
    static void processInput() {
        SDL_Event ev = new SDL_Event();

        while (true) {
            if (0 == SDL_PollEvent(ev)) {
                break;
            }
            if (ev.type == SDL_QUIT)
                System.exit(0);
             else if (ev.type == SDL_KEYDOWN) {
                SDL_KeyboardEvent k = (SDL_KeyboardEvent) ev.readField("key");
                keys.add(k.keysym.sym);
                if (k.keysym.sym == SDLK_F11 && SDL_GetWindowFlags(win) != SDL_WINDOW_FULLSCREEN) 
                    SDL_SetWindowFullscreen(win, SDL_WINDOW_FULLSCREEN);
                if (k.keysym.sym == SDLK_F12) 
                    SDL_SetWindowFullscreen(win, 0);
                if (k.keysym.sym == 27) {
                    //escape
                    SDL_SetRelativeMouseMode(0);
                    paused = !paused;
                    //gbuffer.dump("tmp");
                }
                if (k.keysym.sym == SDLK_SPACE)
                    world.player.jump();
                
            } else if (ev.type == SDL_KEYUP) {
                SDL_KeyboardEvent k = (SDL_KeyboardEvent) ev.readField("key");
                keys.remove(k.keysym.sym);
            } else if (ev.type == SDL_MOUSEMOTION) {
                SDL_MouseMotionEvent m = (SDL_MouseMotionEvent) ev.readField(
                        "motion");
                if (!paused) 
                    world.player.turn(m.xrel * -0.01f);
                    world.player.turnY(m.yrel * -0.01f);
            } else if (ev.type == SDL_MOUSEBUTTONDOWN) {
                     SDL_MouseButtonEvent b = (SDL_MouseButtonEvent) ev.readField("button");
                    if(b.button == SDL_BUTTON_LEFT)
                        mouseButtonDown = true;
//                    if(b.button == SDL_BUTTON_MIDDLE)
//                        mouseButtonDown = true;
                    if(b.button == SDL_BUTTON_RIGHT)
                       currentWeapon = !currentWeapon;
                
            } else if (ev.type == SDL_MOUSEBUTTONUP) {
                if (mouseButtonDown)
                    needBullet = true;
                mouseButtonDown = false;
            }
        }
    }

    static int time_to_next_spawn = 0;
    static final float maxBulletCharge = 1.0f;

    static void update(int elapsed_ms) {

        if (paused)
            return;
        if (!paused && SDL_GetRelativeMouseMode() == 0)
            SDL_SetRelativeMouseMode(1);

        if (mouseButtonDown) {
            bulletCharge += 0.001 * elapsed_ms;
            if (bulletCharge > maxBulletCharge)
                bulletCharge = maxBulletCharge;
        } else {
            bulletCharge -= 0.01 * elapsed_ms;
            if (bulletCharge < 0)
                bulletCharge = 0;
        }
        
        if (world.player.isJumping) {
            world.player.lcs.position.y += 0.02 * elapsed_ms;
            if (world.player.lcs.position.y > 10)
                world.player.isJumping = false;
        }
            if (!world.player.isJumping && world.player.lcs.position.y > 4) {
                world.player.lcs.position.y -= 0.03 * elapsed_ms;
                if (world.player.lcs.position.y < 4)
                    world.player.lcs.position.y = 4;
        }

        if (needBullet) {
            vec3 bulletStart = add(world.player.lcs.position,
                    mul(1.5f, world.player.lcs.facing.xyz()),
                    mul(-1.0f, world.player.lcs.up.xyz()));
            bulletStart.y += 0.6;
            vec3 bulletVelocity = mul(bulletCharge * 0.2f, world.player.lcs.facing.xyz());
            vec3 bulletGravity = new vec3(0, -0.00001, 0);
            if (currentWeapon) {
                Peach peach = new Peach(bulletStart, bulletVelocity, bulletGravity);
                world.addWeapon(peach); 
            }
            if (!currentWeapon) {
                Drumstick drum = new Drumstick(bulletStart, bulletVelocity, bulletGravity);
                world.addWeapon(drum);
            }
            needBullet = false;
        }

        if (keys.contains(SDLK_a))
            world.player.strafe(0.01f * elapsed_ms);
        if (keys.contains(SDLK_d))
            world.player.strafe(-0.01f * elapsed_ms);
        if (keys.contains(SDLK_w))
            world.player.walk(0.01f * elapsed_ms);
        if (keys.contains(SDLK_s))
            world.player.walk(-0.01f * elapsed_ms);

        //for debugging
        if (keys.contains(SDLK_i))
            world.player.strafeY(0.01f * elapsed_ms);
        if (keys.contains(SDLK_k))
            world.player.strafeY(-0.01f * elapsed_ms);
        if (keys.contains(SDLK_u))
            world.player.tilt(-0.001f * elapsed_ms);
        if (keys.contains(SDLK_j))
            world.player.tilt(0.001f * elapsed_ms);

        world.update(elapsed_ms);

        world.checkCollisions();

        camera.setPositionFacingAndUp(
                world.player.lcs.position,
                world.player.lcs.facing.xyz(),
                world.player.lcs.up.xyz()
        );
        fpsText.setText("FPS: " + (int)fps);
        ghostNumText.setText("Ghosts Killed: " + world.player.ghostsKilled / 100);
    }

    static void initGL() {
        SDL_Init(SDL_INIT_VIDEO);
        SDL_GL_SetAttribute(SDL_GL_CONTEXT_PROFILE_MASK,
                SDL_GL_CONTEXT_PROFILE_CORE);
        SDL_GL_SetAttribute(SDL_GL_DEPTH_SIZE, 24);
        SDL_GL_SetAttribute(SDL_GL_STENCIL_SIZE, 8);
        SDL_GL_SetAttribute(SDL_GL_CONTEXT_MAJOR_VERSION, 3);
        SDL_GL_SetAttribute(SDL_GL_CONTEXT_MINOR_VERSION, 3);
        SDL_GL_SetAttribute(SDL_GL_CONTEXT_FLAGS, SDL_GL_CONTEXT_DEBUG_FLAG);
        SDL_GL_SetAttribute(SDL_GL_MULTISAMPLEBUFFERS, 1);
        SDL_GL_SetAttribute(SDL_GL_MULTISAMPLESAMPLES, 4);

        win = SDL_CreateWindow("Spooky Ghosties", 20, 20, WIN_WIDTH, WIN_HEIGHT,
                SDL_WINDOW_OPENGL | SDL_WINDOW_RESIZABLE);
        SDL_GL_CreateContext(win);

        glDebugMessageCallback(
                (int source, int type, int id, int severity, int length, String message, Object param)
                -> {
            switch (id) {
                case 131204:
                case 131185:
                    return;
                default:
                    System.out.println(id + ": " + message);
            }
        }, null
        );

        glDebugMessageControl(GL_DONT_CARE, GL_DONT_CARE, GL_DONT_CARE, 0, null,
                true);
        glEnable(GL_DEBUG_OUTPUT_SYNCHRONOUS);
        glEnable(GL_DEBUG_OUTPUT);
        glEnable(GL_MULTISAMPLE);
        glEnable(GL_DEPTH_TEST);
        //glEnable(GL_CULL_FACE);
        glDepthFunc(GL_LEQUAL);
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glClearColor(0, 0, 0, 1);
        SDL_SetRelativeMouseMode(1);
    }

    public static void draw() {
        glBlendFunc(GL_ONE, GL_ZERO);
        deferredPass1Program.use();
        gbuffer.bind(true);
        camera.draw();
        world.draw();
        skyProgramPass1.use();
        camera.draw();
        Program.current.setUniform("textures", skytexture);
        cube.draw();
        gbuffer.unbind();
        
        glBlendFunc(GL_ONE, GL_ONE);
        deferredPass2Program.use();
        boolean firstpass = true;
        for (Light L : lights) {
            shadowbuffer.bind(true);
            shadowProg.use();
            glBlendFunc(GL_ONE, GL_ZERO);
            L.cam.draw();
            world.draw();
            shadowbuffer.unbind();
            glBlendFunc(GL_ONE, GL_ONE);
            fbo.bind(firstpass);
            firstpass = false;
            deferredPass2Program.use();
            Program.current.setUniform("shadowBuffer", shadowbuffer.texture);
            camera.draw();
            Program.current.setUniform("lightPosition", new vec4(L.cam.eye, 1.0));
            //System.out.println(L.position);
            Program.current.setUniform("lightColor", L.color);
            Program.current.setUniform("lightHitherYon", new vec4(L.cam.hither, L.cam.yon, L.cam.yon - L.cam.hither, 
                    1 / (L.cam.yon - L.cam.hither)));
            Program.current.setUniform("lightProjMatrix", L.cam.projMatrix);
            Program.current.setUniform("lightViewMatrix", L.cam.viewMatrix);
            Program.current.setUniform("bias", 0.001f);
            Program.current.setUniform("tex", gbuffer.texture);
            Program.current.setUniform("fogNear", FOG_NEAR);
            Program.current.setUniform("fogDelta", FOG_DELTA);
            Program.current.setUniform("fogColor", new vec3(0, 0, 0));
            Program.current.setUniform("noiseP", noiseP);
            Program.current.setUniform("ramp", ramp);
            Program.current.setUniform("noiseScale", 0.04);
            
            usq.draw();
            shadowbuffer.texture.unbind();
            fbo.unbind();
        }
        
        //fbo.unbind();
        gbuffer.texture.unbind();
        
        //gbuffer.dump("tmp");
        gbuffer.blur(25, 3, 0, 4);
        
        fboProgram.use();
        fbo.bind(false);
        Program.current.setUniform("tex", gbuffer.texture);
        usq.draw();
        
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        
        if (world.player.bloodFactor != 0) {
            fbo.blur((int) world.player.bloodFactor - 1, 0, 0, 1);
        }
        
        textProgram.use();
        Program.current.setUniform("screenSizeFactor", SCREEN_SIZE_FACTOR);
        fpsText.draw();
        ghostNumText.draw();

        overlayProgram.use();
        Program.current.setUniform("screenSizeFactor", SCREEN_SIZE_FACTOR);
        
        //draw paused text if paused
        if (paused) {
            Program.current.setUniform("textures", pauseText);
            Program.current.setUniform("alphaFactor", 1);
            Program.current.setUniform("topLeft", new vec2((WIN_WIDTH / 2) - 70, (WIN_HEIGHT / 2) - 20));
            Program.current.setUniform("size", new vec2(140,85));
            square.draw();
        }
        
        //draw crosshair
        Program.current.setUniform("textures", xhair);
        Program.current.setUniform("alphaFactor", 1);
        Program.current.setUniform("topLeft", new vec2((WIN_WIDTH / 2) - 5, (WIN_HEIGHT / 2) - 5));
        Program.current.setUniform("size", new vec2(5, 5));
        square.draw();
        
        //draw peach icon
        if (currentWeapon)
            Program.current.setUniform("textures", peachIcon);
        else Program.current.setUniform("textures", peachIconDark);
        Program.current.setUniform("alphaFactor", 1);
        Program.current.setUniform("topLeft", new vec2(5, WIN_HEIGHT - 115));
        Program.current.setUniform("size", new vec2(42, 42));
        square.draw();
        
        //draw drumstick icon
        if (!currentWeapon)
            Program.current.setUniform("textures", drumstickIcon);
        else Program.current.setUniform("textures", drumstickIconDark);
        Program.current.setUniform("alphaFactor", 1);
        Program.current.setUniform("topLeft", new vec2(45, WIN_HEIGHT - 115));
        Program.current.setUniform("size", new vec2(42, 42));
        square.draw();

        //draw bullet power bar
        Program.current.setUniform("textures", yellow);
        Program.current.setUniform("alphaFactor", 1);
        Program.current.setUniform("topLeft", new vec2(0, 10));
        Program.current.setUniform("size", new vec2(
                WIN_WIDTH * bulletCharge * 1.0 / maxBulletCharge, 10));
        square.draw();

        //draw player health indicator
        Program.current.setUniform("textures", heart);
        Program.current.setUniform("alphaFactor", 1.0);
        Program.current.setUniform("size", new vec2(32, 32));

        for (int i = 0; i < world.player.health; ++i) {
            Program.current.setUniform("topLeft", new vec2(i * 40, WIN_HEIGHT - 32));
            square.draw();
        }
        fbo.unbind();
        
        fboProgram.use();
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        Program.current.setUniform("tex", fbo.texture);
        usq.draw();

        fbo.texture.unbind();
        gbuffer.texture.unbind();
        SDL_GL_SwapWindow(win);
    }

    public static void loadShaders() {
        //prog = new Program("prog1.vs", null, "prog1.fs");
        overlayProgram = new Program("overlay.vs", null, "overlay.fs");
        skyProgramPass1 = new Program("skyprog.vs", null, "skypass1.fs");
        textProgram = new Program("text.vs", null, "text.fs");
        fboProgram = new Program("fboprog.vs", null, "fboprog.fs");
        deferredPass1Program = new Program("pass1.vs", null, "pass1.fs");
        deferredPass2Program = new Program("pass2.vs", null, "pass2.fs");
        shadowProg = new Program("shadow.vs", null, "shadow.fs");
    }

    static void loadTextures() {
        yellow = new ImageTexture2DArray("assets/yellow.png");
        heart = new ImageTexture2DArray("assets/heart.svg.png");
        pauseText = new ImageTexture2DArray("assets/paused.png");
        xhair = new ImageTexture2DArray("assets/crosshair.png");
        peachIcon = new ImageTexture2DArray("assets/peachicon.png");
        peachIconDark = new ImageTexture2DArray("assets/peachicondark.png");
        drumstickIcon = new ImageTexture2DArray("assets/drumstickicon.png");
        drumstickIconDark = new ImageTexture2DArray("assets/drumstickicondark.png");
        ramp = new ImageTexture2DArray("assets/ramp.png");
        fpsText = new Text("assets/font.zip", "FPS: ", WIN_WIDTH - 65, WIN_HEIGHT - 20);
        ghostNumText = new Text("assets/ghostfont.zip", "Ghosts:", 5, WIN_HEIGHT - 70);
        skytexture = new ImageCubeTexture(
                "assets/0001.jpg",
                "assets/0002.jpg",
                "assets/0003.jpg",
                "assets/0004.jpg",
                "assets/0005.jpg",
                "assets/0006.jpg"
        );
    }

    public static void main(String[] args) {
        initGL();

        loadShaders();
        loadTextures();
        
        fbo = new Framebuffer2D(WIN_WIDTH, WIN_HEIGHT, GL_RGBA32F, 5);
        gbuffer = new Framebuffer2D(WIN_WIDTH, WIN_HEIGHT, GL_RGBA32F, 5);
        shadowbuffer = new Framebuffer2D(WIN_WIDTH*8, WIN_HEIGHT*8, GL_RGBA32F, 1);
        cube = new Mesh("assets/cube.obj.mesh");
        square = new Square();

        world = new World();
        world.setMap(new Map());
        Player p = new Player(world.map.playerStart, new vec3(0, 0, -1));
        world.setPlayer(p);

        world.map.pumpkinLocations.forEach((v) -> { 
            world.addPowerup(new JackOLantern(v));
        });
        
        world.map.spawnerLocations.forEach((v) -> { 
            world.addNpc(new GhostSpawner(v, new vec2(2, 2)));
        });
        
        world.map.treeLocations.forEach((v) -> { 
            world.addNpc(new Tree(v));
        });
        world.addNpc(new Statue(new vec3(46,0,46)));
        usq = new UnitSquare();
        
        //Create Lights
        Camera cam1 = new Camera();
        cam1.lookAt(new vec3(-10, 40, -15), new vec3(20, 20, 10), new vec3(0, 1, 0));
        cam1.updateProjMatrix();
        lights.add(new Light(new vec3(1,1,1), 90.0f, cam1));
        
        //noise
        ArrayList<Float> tmp = new ArrayList<>();
        int sigma = 256;
        for (int i = 0; i < sigma; ++i) {
            tmp.add((float) i);
        }
        Collections.shuffle(tmp);
        for (int i = 0; i < sigma; ++i) {
            tmp.add(tmp.get(i));
        }
        float[] P = new float[tmp.size()];
        for (int i = 0; i < tmp.size(); ++i) {
            P[i] = tmp.get(i);
        }
        noiseP = new DataTexture2DArray(P.length, 1, 1, P);
        
        //preload meshes to avoid delays
        new Peach(new vec3(0, 0, 0), new vec3(0, 0, 0), new vec3(0, 0, 0));
        new Drumstick(new vec3(0, 0, 0), new vec3(1, 1, 1), new vec3(0,0,0));
        
        //milliseconds
        int UPDATE_QUANTUM_MS = 5;
        long UPDATE_QUANTUM_NS = UPDATE_QUANTUM_MS * 1000000;

        long last = System.nanoTime();
        //nanoseconds
        long accumulated_time_ns = 0;

        while (true) {
            processInput();
            long now = System.nanoTime();
            long elapsed = now - last;
            last = now;
            accumulated_time_ns += elapsed;
            while (accumulated_time_ns >= UPDATE_QUANTUM_NS) {
                update(UPDATE_QUANTUM_MS);
                accumulated_time_ns -= UPDATE_QUANTUM_NS;
            }
            draw();
            ++frames;
            if (frames == 10) {
                long nnow = System.currentTimeMillis();
                fps = frames * 1000.0 / (nnow - startTime);
                frames = 0;
                startTime = nnow;
            }
            SDL_Delay(10);
        }
    }
}
