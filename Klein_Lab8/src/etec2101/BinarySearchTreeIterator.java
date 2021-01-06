package etec2101;

import java.util.Iterator;
import java.util.ArrayList;
import etec2101.BinarySearchTree;
import etec2101.BinarySearchTree.TraversalType;

/**
 *
 * @author Matt
 */
public class BinarySearchTreeIterator<E> implements Iterator<E> {

    protected BinarySearchTreeNode mOwner;
    private int index;
    protected ArrayList<E> list;

    public BinarySearchTreeIterator(BinarySearchTreeNode tData, TraversalType t) {
        this.mOwner = tData;
        index = 0;
        this.list = new ArrayList();
        calcList(tData, t);
    }

    /**
     * Note: This can be safely called any number of times.
     *
     * @return : true if there is more data to be iterated through
     */
    @Override
    public boolean hasNext() {
        return index < list.size();
    }

    /**
     * Return the "current" value *and* advances the iteration. The user is
     * supposed to call hasNext at least once before calling next.
     *
     * @return
     */
    @Override
    public E next() {
        E currentValue = list.get(index);
        index++;
        return currentValue;
    }

    public void calcList(BinarySearchTreeNode bstn, TraversalType t) {
        if (t == TraversalType.PRE_ORDER) {
            if (bstn.payload != null) {
                list.add((E) bstn.payload);
            }
            if (bstn.left != null) {
                calcList(bstn.left, t);
            }
            if (bstn.right != null) {
                calcList(bstn.right, t);
            }
        }
        if (t == TraversalType.POST_ORDER) {
            if (bstn.left != null) {
                calcList(bstn.left, t);
            }
            if (bstn.right != null) {
                calcList(bstn.right, t);
            }
            if (bstn.payload != null) {
                list.add((E) bstn.payload);
            }
        }
        if (t == TraversalType.IN_ORDER) {
            if (bstn.left != null) {
                calcList(bstn.left, t);
            }
            if (bstn.payload != null) {
                list.add((E) bstn.payload);
            }
            if (bstn.right != null) {
                calcList(bstn.right, t);
            }
        }
    }
}
