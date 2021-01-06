package etec2101;

// NOTE: I Want you to make the BinarySearchTreeNode class external to this one

import java.util.Iterator;
import java.util.ArrayList;

//       (not nested as we usually do).  Still...ensure that anyone outside this
//       etec2101 package can't be aware that there is such a class.

// NOTE2: Don't change anything in this class unless we discuss it and approve
//        it in class.

public class BinarySearchTree<E extends Comparable>
{
    BinarySearchTreeNode mRoot;
    
    public enum TraversalType { IN_ORDER, PRE_ORDER, POST_ORDER };
    
    public BinarySearchTree()
    {
        mRoot = null;
    }
    
    public void add(E val)
    {
        if (mRoot == null)
            mRoot = new BinarySearchTreeNode(val, null);	// Note: the null indicates there
						// is no parent.  If you don't need this, you can
					               // delete it.
        else
            mRoot.add(val);       		// Note: If this value already exists,
                                        //   it should go down the "right" (as
                                        //   in not-left) branch.
    }
    
    
    public int size()
    {
        if (mRoot == null)
            return 0;
        else
            return mRoot.size();        // Yes...I know it would be more efficient
                                        //   to store the size here, but this is
                                        //   an interesting challenge.
    }
    
    
    public boolean contains(E val)
    {
        if (mRoot == null)
            return false;
        else
            return mRoot.contains(val); // Make sure to only check those areas
                                        // of the tree that we need to!
    }
    
    
    public String toString()
    {
        if (mRoot == null)
            return "<empty>";
        else
            return mRoot.generateString();    // You can decide what, if 
                                                         // anything, to pass here.
                                                 
        //  If the tree looks like this:
        //              M
        //        D              Q
        //      A   F                R
        //  I want the string return by this function to look like:
        //  M
        //     (left) D
        //         (left) A
        //         (right) F
        //     (right) Q
        //         (right) R
    }
    
	
	public void rebalance()
    {
        // Write the code here.  Suggested algorithm: collect all data from the
        // tree into one place (maybe an ArrayList?)  You could do this with the
        // iterator (if it's done), or better yet, write a utility function in the node
        // class that does this (if doing the easy version of the iterator, you'd
        // likely have to do this anyway).
        // Then...pick the middle element as the root, and recursively break
        // the list into two halves (everything before the middle element and 
        // everything after -- recursively apply the algorithm on the two halves).
        
        // NOTE: This is a relatively slow operation: O(n log n) at best.
        
        // FINISH ME!
        ArrayList<E> list = new ArrayList();
        Iterator i = this.iterator(TraversalType.IN_ORDER);
        while (i.hasNext()){
            list.add((E)i.next());
        }
        BinarySearchTree temp = new BinarySearchTree();
        BinarySearchTreeNode tempNode = new BinarySearchTreeNode(0, null);
        tempNode.splitAdd(temp, list);
        this.mRoot = temp.mRoot;
    }
	
    
    public Iterator<E> iterator(TraversalType t)
    {
        // You'll build this class.  I'd like you to create this class outside
        // of the BinarySearchTree (and BinarySearchTreeNode) classes.  The 
        // user (outside of this package) *should* be aware of this new class.
        // The *easy* approach is to calculate (recursively) all data we will
        // explore and store it in a container (e.g. ArrayList).  The harder 
        // (but better) approach is to calculate the data as needed.
        return new BinarySearchTreeIterator(mRoot, t);
    }
    
    
    // This one is bonus -- comment this out if not attempting the bonus.
    public void remove(E val)
    {
        if (mRoot == null)
            return;         // Nothing to do!
        else
        {
            // There are different ways to tackle this problem.  You *can*
            // handle the case where the root is the "target" here, but also
            // write a recursive version for when the root is not the target
            // (that's what I'm showing on the next line)
            if (mRoot.payload == val) {
                BinarySearchTreeNode temp = new BinarySearchTreeNode(0, null);
                temp.left = mRoot;
                mRoot.remove(val);
                mRoot = temp.left;
                return;
            } else {
                mRoot.remove(val);
                return;
            }
        }
    }
}
