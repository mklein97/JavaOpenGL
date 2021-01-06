package etec2101;

import java.awt.Point;
import java.util.ArrayList;
import org.newdawn.slick.Color;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.Graphics;

public class Quadtree {

    QuadtreeNode mRoot;

    public Quadtree(int w, int h, int max) {
        mRoot = new QuadtreeNode(new Rectangle(0, 0, w, h), null, max);
        mRoot.mRoot = mRoot;
    }

    public void add(Bouncer b) {
        mRoot.add(b);
    }

    public String toString() {
        if (mRoot == null) {
            return "<empty>";
        } else {
            return mRoot.generateString();
        }
    }

    public void draw(Graphics g, boolean mp, int mx, int my) {
        mRoot.draw(g, mp, mx, my);
    }

    public void update() {
        mRoot.update();
        mRoot.prune();
    }

    public class QuadtreeNode {

        QuadtreeNode nw, ne, sw, se, parent;
        int max;
        Rectangle rect;
        ArrayList<Bouncer> bList;
        QuadtreeNode mRoot;

        public QuadtreeNode(Rectangle r, QuadtreeNode parent, int max) {
            this.parent = parent;
            this.rect = r;
            this.nw = null;
            this.ne = null;
            this.sw = null;
            this.se = null;
            bList = new ArrayList();
            this.max = max;
            if (parent != null) {
                this.mRoot = parent.mRoot;
            }
        }

        protected void add(Bouncer b) {
            addRecursive(b);
        }

        private void addRecursive(Bouncer b) {
            if (b.rect.getX() >= rect.getX() && b.rect.getX() < rect.getX() + rect.getWidth() && b.rect.getY() >= rect.getY() && b.rect.getY() < rect.getY() + rect.getHeight()) {
                if (bList.size() < this.max && nw == null) {
                    bList.add(b);
                } else {
                    if (nw == null) {
                        nw = new QuadtreeNode(new Rectangle(rect.getX(), rect.getY(), rect.getWidth() / 2, rect.getHeight() / 2), this, max);
                        ne = new QuadtreeNode(new Rectangle(rect.getX() + rect.getWidth() / 2, rect.getY(), rect.getWidth() / 2, rect.getHeight() / 2), this, max);
                        sw = new QuadtreeNode(new Rectangle(rect.getX(), rect.getY() + rect.getHeight() / 2, rect.getWidth() / 2, rect.getHeight() / 2), this, max);
                        se = new QuadtreeNode(new Rectangle(rect.getX() + rect.getWidth() / 2, rect.getY() + rect.getHeight() / 2, rect.getWidth() / 2, rect.getHeight() / 2), this, max);
                    }
                    nw.addRecursive(b);
                    ne.addRecursive(b);
                    sw.addRecursive(b);
                    se.addRecursive(b);
                    for (Bouncer bo : bList) {
                        nw.addRecursive(bo);
                        ne.addRecursive(bo);
                        sw.addRecursive(bo);
                        se.addRecursive(bo);
                    }
                    bList.clear();
                }
            }
        }

        protected boolean contains(Bouncer b) {
            return calcContains(this, b);
        }

        protected boolean calcContains(QuadtreeNode qn, Bouncer b) {
            if (bList != null && bList.contains(b)) {
                return true;
            } else {
                if (nw.contains(b)) {
                    return true;
                }
                if (ne.contains(b)) {
                    return true;
                }
                if (sw.contains(b)) {
                    return true;
                }
                if (se.contains(b)) {
                    return true;
                }
            }
            return false;
        }

        protected String generateString() {
            String s = "";

            s = calcString(this, s, 0, 0);

            return s;
        }

        protected String calcString(QuadtreeNode qn, String s, int level, int pos) {
//            if (!qn.bList.isEmpty()) {
            for (int i = 0; i < level; i++) {
                s += "    ";
            }
            if (pos == 1) {
                s += "(NW) ";
            }
            if (pos == 2) {
                s += "(NE) ";
            }
            if (pos == 3) {
                s += "(SW) ";
            }
            if (pos == 4) {
                s += "(SE) ";
            }
            for (Bouncer b : qn.bList) {
                s += ("(" + b.rect.getX() + ", " + b.rect.getY() + ") ");
            }
            s += "\n";
            //}
            if (qn.nw != null) {
                s = calcString(qn.nw, s, level + 1, 1);
            }
            if (qn.ne != null) {
                s = calcString(qn.ne, s, level + 1, 2);
            }
            if (qn.sw != null) {
                s = calcString(qn.sw, s, level + 1, 3);
            }
            if (qn.se != null) {
                s = calcString(qn.se, s, level + 1, 4);
            }
            return s;
        }

        protected void draw(Graphics g, boolean mp, int mx, int my) {
            g.setColor(new Color(61, 124, 47));
            g.draw(this.rect);
            g.setColor(new Color(0, 0, 255));
            for (Bouncer b : bList) {
                b.draw(g, mp, mx, my, this.overlaps(b));
            }

            if (nw != null) {
                nw.draw(g, mp, mx, my);
            }
            if (ne != null) {
                ne.draw(g, mp, mx, my);
            }
            if (sw != null) {
                sw.draw(g, mp, mx, my);
            }
            if (se != null) {
                se.draw(g, mp, mx, my);
            }
        }

        protected boolean overlaps(Bouncer b) {
            if (b.doesoverlap) {
                return true;
            }

            return mRoot.calcOverlaps(b);
        }

        protected boolean calcOverlaps(Bouncer b) {
            if (!b.overlaps(this.rect)) {
                return false;
            }

            for (Bouncer bo : bList) {
                if (bo != b) {
                    if (b.overlaps(bo.rect)) {
                        bo.doesoverlap = true;
                        return true;
                    }
                }
            }
            if (nw != null) {
                if (nw.calcOverlaps(b)) {
                    return true;
                }
                if (ne.calcOverlaps(b)) {
                    return true;
                }
                if (sw.calcOverlaps(b)) {
                    return true;
                }
                if (se.calcOverlaps(b)) {
                    return true;
                }
            }
            return false;
        }

        protected void update() {
            ArrayList<Bouncer> dList = new ArrayList();
            for (Bouncer b : bList) {
                b.update();

                if (!this.containsB(b)) {
                    mRoot.add(b);
                    dList.add(b);
                }
            }

            for (Bouncer b : dList) {
                bList.remove(b);
            }

            if (nw != null) {
                nw.update();
                ne.update();
                sw.update();
                se.update();
            }
        }

        protected boolean isEmpty() {
            if (bList.isEmpty() && nw == null) {
                return true;
            }
            if (bList.isEmpty() && nw.isEmpty() && ne.isEmpty() && sw.isEmpty() && se.isEmpty()) {
                return true;
            }
            return false;
        }

        protected void prune() {
            if (nw != null) {
                nw.prune();
                ne.prune();
                sw.prune();
                se.prune();

                if (nw.isEmpty() && ne.isEmpty() && sw.isEmpty() && se.isEmpty()) {
                    nw = null;
                    ne = null;
                    sw = null;
                    se = null;
                }
            }
        }

        protected boolean containsB(Bouncer b) {
            float minx = rect.getX();
            float maxx = rect.getX() + rect.getWidth();
            float miny = rect.getY();
            float maxy = rect.getY() + rect.getHeight();
            return b.rect.getX() >= minx && b.rect.getX() <= maxx
                    && b.rect.getY() >= miny && b.rect.getY() <= maxy;
        }

    }
}
