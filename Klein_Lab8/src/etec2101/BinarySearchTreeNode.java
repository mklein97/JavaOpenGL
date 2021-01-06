package etec2101;

import java.util.ArrayList;

/**
 *
 * @author Matt
 */
class BinarySearchTreeNode<E extends Comparable> {

    E payload;
    BinarySearchTreeNode left;
    BinarySearchTreeNode right;
    BinarySearchTreeNode parent;

    protected BinarySearchTreeNode(E val, BinarySearchTreeNode parent) {
        this.parent = parent;
        this.payload = val;
        this.left = null;
        this.right = null;
    }

    protected void add(E val) {
        addRecursive(val, this);
    }

    protected void addRecursive(E val, BinarySearchTreeNode parent) {
        if (val == this.payload)
                  return;
            else if (val.compareTo(this.payload) < 0) {
                  if (left == null) {
                        left = new BinarySearchTreeNode(val, this);
                        return;
                  } else{
                        left.addRecursive(val, this);
                        return;
                  }
            } else if (val.compareTo(this.payload) > 0) {
                  if (right == null) {
                        right = new BinarySearchTreeNode(val, this);
                        return;
                  } else {
                        right.addRecursive(val, this);
                        return;
                  }
            }
            return;
    }

    protected int size() {
        int counter = 0;
        return calcSize(this, counter);
    }

    protected int calcSize(BinarySearchTreeNode bstn, int n) {
        if (bstn.payload != null) {
            n++;
        }
        if (bstn.left != null) {
            n = calcSize(bstn.left, n);
        }
        if (bstn.right != null) {
            n = calcSize(bstn.right, n);
        }
        return n;
    }

    protected boolean contains(E val) {
        return calcContains(this, val);
    }

    protected boolean calcContains(BinarySearchTreeNode bstn, E val) {
        if (bstn.payload == val) {
            return true;
        }
        if (val.compareTo(bstn.payload) < 0 && bstn.left != null) {
            if (calcContains(bstn.left, val)) {
                return true;
            }
        }
        if (val.compareTo(bstn.payload) > 0 && bstn.right != null) {
            if (calcContains(bstn.right, val)) {
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

    protected String calcString(BinarySearchTreeNode bstn, String s, int level, int pos) {
        if (bstn.payload != null) {
            for (int i = 0; i < level; i++) {
                s += "    ";
            }
            if (pos == 1) {
                s += "(left) ";
            }
            if (pos == 2) {
                s += "(right) ";
            }
            s += bstn.payload;
            s += "\n";

        }
        if (bstn.left != null) {
            s = calcString(bstn.left, s, level + 1, 1);
        }
        if (bstn.right != null) {
            s = calcString(bstn.right, s, level + 1, 2);
        }
        return s;
    }
    
    public void remove(E val) {
        if (val.compareTo(this.payload) < 0) {
            if (left != null) {
                left.remove(val);
                return;
            } else {
                return;
            }
        } else if (val.compareTo(this.payload) > 0) {
            if (right != null) {
                right.remove(val);
                return;
            } else {
                return;
            }
        } else {
            if (left != null && right != null) {
                this.payload = (E) right.min();
                right.remove(this.payload);
            } else if (parent != null && parent.left != null && parent.left == this) {
                if (this.left != null) {
                    parent.left = this.left;
                } else {
                    parent.left = this.right;
                }
            } else if (parent != null && parent.right != null && parent.right == this) {
                if (this.left != null) {
                    parent.right = this.left;
                } else {
                    parent.right = this.right;
                }
            }
            return;
        }
    }
    
    public E min() {
        if (left == null) {
            return this.payload;
        } else {
            return (E)left.min();
        }
    }

    public void splitAdd(BinarySearchTree t, ArrayList<E> list) {
        if (list == null || list.isEmpty()) {
            return;
        }

        t.add(list.get(list.size() / 2));

        if (list.size() == 1) {
            return;
        }

        ArrayList<E> leftHalf = new ArrayList();
        leftHalf.addAll(list.subList(0, (list.size() / 2)));
        splitAdd(t, leftHalf);

        if (list.size() > 2) {
            ArrayList<E> rightHalf = new ArrayList();
            rightHalf.addAll(list.subList((list.size() / 2) + 1, list.size()));
            splitAdd(t, rightHalf);
        }
    }

}
