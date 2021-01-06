package etec2101;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class LinkedList <E>
{
    /**
     * This Node class is meant to be used internally.  It represents one link in a LinkedList and contains
 pointers to the previous and following node in the linked list.  It also contains a data element (of type E)
 [sometimes called the "payload"].  By making this class protected, we ensure that outside of the LinkedList
 class, this class isn't even visible.
     */
    protected class Node
    {
        /**
         * The data value at this position in the linked list.
         */
        E mData;

        /**
         * The previous node in the linked list (or null if this is the first node)
         */
        Node mPrev;

        /**
         * The next node in the linked list (or null if this is the last node)
         */
        Node mNext;

        /**
         * The constructor for the node class
         * @param value: The data value to store in this node.
         */
        public Node(E value)
        {
            mData = value;
            mPrev = mNext = null;
        }
    }

    /**
     * A reference to the beginning (head) node of the linked list.  Will be null if the list is empty.
     */
    protected Node mStart;

    /**
     * A reference to the end (tail) node of the linked list.  Will be null if this list is empty.
     */
    protected Node mEnd;

    /**
     * The number of nodes currently being stored in the linked list.
     */
    protected int mSize;

    /**
     * The LinkedList constructor.
     */
    public LinkedList()
    {
        mSize = 0;
        mStart = mEnd = null;
    }


    /**
     * Adds a new value (and creates a node) at the end of the linked list.
     * @param value
     */
    public void addLast(E value)
    {
        Node n = new Node(val);
        if (mEnd == null)  // mStart is also null, mSize is 0
        {
            // Case I: list is empty
            mStart = mEnd = n;
        }
        else
        {
            // Case II: list is NOT empty
            mEnd.mNext = n;     // Blue line (on board)
            n.mPrev = mEnd;     // Red line (on board)
            mEnd = n;           // Do this last!
        }
        mSize++;
    }


    /**
     * Adds a new value (and creates a node) at the beginning of the linked list.
     * @param value
     */
    public void addFirst(E value)
    {
        // FINISH ME!
    }
    
    
    /**
     * Inserts a new node at the given position in the linked List, "shoving" every other node after forward
     * @param position
     * @param value
     */
    public void add(int position, E value) throws IndexOutOfBoundsException
    {
        if (position < 0 || position > mSize)   // Covers case 5 -- fast-fail!
            throw new IndexOutOfBoundsException("Invalid index: " + position);
        
        if (position == 0 || mSize == 0)
            addFirst(value);                    // Covers case 1 & 2
        else if (position == mSize)
            addLast(value);                     // Covers case 3
        else
        {
            // Everything else falls under case 4  -- we're somewhere in the middle.
            // ... Create the new node (this is done in addFirst and addLast too...
            Node newNode = new Node(value);
            
            // ... traverse to the correct spot.
            Node cur = mStart;
            int curPos = 0;
            while (curPos != position && cur != null)
            {
                cur = cur.mNext;
                curPos++;
            }

            // ... re-route the pointers
            newNode.mNext = cur;
            newNode.mPrev = cur.mPrev;

            cur.mPrev.mNext = newNode;
            cur.mPrev = newNode;
            
            // We now have one more node.  Note: I put it in the else b/c
            // addFirst and addLast do it themselves -- we don't want to do 
            // it twice!
            mSize++;
        }  
    }
    
    
    /**
     * Removes all elements from the linked list.
     */
    public void clear()
    {
        // FINISH ME!
    }
    
    
    /**
     * Finds the first occurrence of val and returns the "index" it would be
     * at if this were an array.
     * @param val: The value to find.
     * @return the index of the first occurrence of val or -1 if it isn't found.
     */
    public int indexOf(E val)
    {
        // FINISH ME!
    }
    
    


    /**
     * The indexing-like operator for our linked list.  
     * @param position: The position.  Must be in the range 0...size-1
     * @return: The value at that position.
     * @throws IndexOutOfBoundsException
     */
    public E get(int position) throws IndexOutOfBoundsException
    {
        // FINISH ME!
    }




    /**
     * Removes an element from the linked list.
     * @param index: The index of the object we wish to remove (exception is
     *                   thrown if this is an invalid index)
     */
    public void remove(int index) throws IndexOutOfBoundsException
    {
        // FINISH ME!
    }
    
    
    
    /**
     * @return: The length of the list.
     */
    public int size()
    {
        // FINISH ME!
    }
    
    
    
    /**
     * Copies all data in the linked list to an array of Objects (where the
     * type of each will actually be E).
     * @return: the new array.
     */
    public Object[] toArray()
    {
        // FINISH ME!
    }
    


    /**
     * @return: A formatted string representation of the contents of this linked list
     */
    @Override
    public String toString()
    {
        // FINISH ME!
    }


    
    /**
     * @return: A forward-advancing iterator
     */
    public LinkedListIterator iterator()
    {
        // FINISH ME!
    }
    
    
    
    /**
     * This class implements the Iterator interface as is used to traverse (either forward or backwards) through
     * the attached LinkedList.  These iterators are meant to be created by calling the iterator or riterator
     * methods of the LinkedList class.
     */
    public class LinkedListIterator implements Iterator<E>
    {
        // FINISH ME!
    }
}
