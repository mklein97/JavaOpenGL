package etec2101;

import etec2101.Quadtree.QuadtreeNode;
import java.awt.Point;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.KeyListener;
import org.newdawn.slick.MouseListener;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;
import java.util.ArrayList;
import java.lang.Math;

public class QuadtreeMain extends BasicGame implements KeyListener, MouseListener {

    Image mImage;
    static Quadtree q;
    static boolean mousePressed;
    static int mouseX, mouseY, bSize;
    static public AppGameContainer appgc;

    public QuadtreeMain(String gamename) {
        super(gamename);
    }

    @Override
    public void init(GameContainer gc) throws SlickException {
        mImage = new Image("pieces.png");
        //gc.setShowFPS(// Optional;
        
        //second parameter is speed
        generateBouncers(100, 0.2f);

    }

    @Override
    public void update(GameContainer gc, int i) throws SlickException {
      q.update();
    }

    @Override
    public void keyPressed(int k, char c) {
        if (k == Input.KEY_ESCAPE) {
            System.exit(0);
        }
    }
    
    @Override
    public void mousePressed(int button, int x, int y) {
        mouseX = x;
        mouseY = y;
        mousePressed = true;
    }
    
    @Override
    public void mouseReleased(int button, int x, int y) {
        mousePressed = false;
    }

    @Override
    public void render(GameContainer gc, Graphics g) throws SlickException {
//        g.setColor(new Color(61, 124, 47));
        q.draw(g, mousePressed, mouseX, mouseY);
    }

    public static void main(String[] args) {
        try {           
            q = new Quadtree(1280, 720, 4);
            bSize = 15;

            System.out.println(q.toString());

            appgc = new AppGameContainer(new QuadtreeMain("Quadtree Solution"));
            appgc.setDisplayMode(1280, 720, false);
            appgc.start();
        } catch (SlickException ex) {
            Logger.getLogger(QuadtreeMain.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void generateBouncers(int num, float speed) {
    for (int i = 0; i <= num; i++) {
        int randomPosX = (int) (15 + Math.random() * (1260 - 15));
        int randomPosY = (int) (2 + Math.random() * (700 - 2));
        int randomDir = (int) (2 + Math.random() * (360 - 2));
        q.add(new Bouncer(new Point(randomPosX, randomPosY), bSize, randomDir, speed));
    }
}
    
}
