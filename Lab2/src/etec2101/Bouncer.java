package etec2101;

import java.awt.Point;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Rectangle;
import java.lang.Math.*;
import etec2101.QuadtreeMain;

/**
 *
 * @author Matt
 */
public class Bouncer {
    Rectangle rect;
    boolean doesoverlap;
    float dir, speed;
    
    public Bouncer(Point p, int size, float dir, float speed) {
        rect = new Rectangle(p.x, p.y, size, size);
        doesoverlap = false;
        this.dir = dir;
        this.speed = speed;
    }
    
//    Bouncer(Bouncer b) {
//        this.rect = new Rectangle(b.rect.getX(), b.rect.getY(), b.rect.getWidth(), b.rect.getHeight());
//    }
//    
    public void draw(Graphics g, boolean mp, int mx, int my, boolean overlaps) {
        if (this.contains(new Point(mx, my)) && mp)
            g.setColor(new Color(238, 255, 0));
        else {
            if (overlaps)
                g.setColor(new Color(255, 0, 0));
            else
                g.setColor(new Color(0, 0, 255));
        }
            
        g.fillRect(rect.getX(), rect.getY(), rect.getWidth(), rect.getHeight());
    }
    
    public void update() {
        float dx = speed * (float) (Math.cos(Math.toRadians(dir)));
        float dy = speed * (float) (-1 * Math.sin(Math.toRadians(dir)));
        rect.setX(rect.getX() + dx);
        rect.setY(rect.getY() + dy);

        if (rect.getX() < 0) {
            rect.setX(0);
            dx *= -1;
        }
        if (rect.getX() > QuadtreeMain.appgc.getWidth() - rect.getWidth()) {
            rect.setX(QuadtreeMain.appgc.getWidth() - rect.getWidth());
            dx *= -1;
        }
        if (rect.getY() < 0) {
            rect.setY(0);
            dy *= -1;
        }
        if (rect.getY() > QuadtreeMain.appgc.getHeight() - rect.getHeight()) {
            rect.setY(QuadtreeMain.appgc.getHeight() - rect.getHeight());
            dy *= -1;
        }

        dir = (float) Math.toDegrees(Math.atan2(-dy, dx));
        speed = (float) Math.sqrt(Math.pow(dx, 2) + Math.pow(dy, 2));
        doesoverlap = false;
    }
    
    public boolean contains(Point p) {
        float minx = rect.getX();
        float maxx = rect.getX() + rect.getWidth();
        float miny = rect.getY();
        float maxy = rect.getY() + rect.getHeight();
        return p.x >= minx && p.x <= maxx
                && p.y >= miny && p.y <= maxy;
    }

    public boolean overlaps(Rectangle a) {
        // separating axis-theorem...
        float minAx = a.getX();
        float maxAx = a.getX() + a.getWidth();
        float minAy = a.getY();
        float maxAy = a.getY() + a.getHeight();
        float minBx = rect.getX();
        float maxBx = rect.getX() + rect.getWidth();
        float minBy = rect.getY();
        float maxBy = rect.getY() + rect.getHeight();
        return !(minBx > maxAx || maxBx < minAx
                || minBy > maxAy || maxBy < minAy);
    }
}
