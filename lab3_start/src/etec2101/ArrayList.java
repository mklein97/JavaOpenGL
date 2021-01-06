package etec2101;

import java.lang.reflect.Array;
import java.util.Iterator;

/**
 * A generic container which is internally implemented as an array.
 * @author jwitherell
 * @param <E> 
 */
public class ArrayList<E>
{
    /**
     * Used when creating iterators through the iterator method to indicate
     * the direction of traversal.
     */
    public enum Direction { FWD, REV };
    
    /**
     * The array of data.  Java won't allow arrays of type E, so we must use
     * the base-class of all types (Object).  Since data can only be added via
     * our add method, we can be sure all elements of mData are actually of type
     * E.
     */
    protected Object[] mData;
    
    /**
     * The number of USED spots in mData (make sure there are no "holes" in 
     * mData so this would also indicate the position of the next spot to add
     * an item.  Note: this is not the same as mData.length (the number of 
     * ALLOCATED spots)
     */
    protected int mSize;
    
    /**
     * When we run out of space in mData, the array needs to be re-allocated to
     * be this much bigger than it currently is.  
     */
    protected static int sIncrementSize = 10;       // Allocate the array
                            // to initially be this size.  If we run out of
                            // room, re-allocate to be this much bigger
    
    /**
     * Initialize the array to have capacity of sIncrementSize.
     */
    public ArrayList()
    {
        mData = new Object[sIncrementSize]; // all null's
        mSize = 0;
    }
    
    /**
     * Set the increment size for ALL ArrayList's.  The choice of making this
     * a static method was totally arbitrary (just an excuse to show static
     * attributes and methods)
     * @param new_size 
     */
    public static void set_increment_size(int new_size)
    {
        sIncrementSize = new_size;
    }
    
    
    /**
     * Adds a new element to the end of the list.
     * @param new_val 
     */
    public void add(E new_val)
    {
        // Add new_val to the end of the array (if room) --        
        if (mSize < mData.length){
            mData[mSize] = new_val;
            mSize++;
        }
        // expand mData as necessary.   
        else{
            Object[] temparray = new Object[mSize + sIncrementSize];
            System.arraycopy(mData, 0, temparray, 0, mSize);
            mData = temparray;
            
            mData[mSize] = new_val;
            mSize++;
        }        
    }
    
    /**
     * Gets the number of data items in the list.
     * @return 
     */
    public int getSize()
    {
        return mSize;
    }
    
    
    
    /**
     * Resets the list (to its initial empty state), removing any existing data.
     */
    public void clear()
    {
        Object[] tempArray = new Object[sIncrementSize];
        mData = tempArray;
        mSize = 0;
    }
    
    
    /**
     * Finds the first occurrence of val in the list or -1 if that item isn't 
     * found.
     * @param val
     * @return 
     */
    public int indexOf(E val)
    {
        for (int i = 0; i < mSize; i++) 
            if (mData[i] == val) return i;
        
        return -1;
    }
    
    
    /**
     * Returns true if there is at least one occurrence of val.
     * @param val
     * @return 
     */
    public boolean contains(E val)
    {
        // FINISH ME: Use the indexOf method to do the real work (this body
        //   should be ~1 line of code.
        return indexOf(val) != -1;
    }
    
    
    /**
     * Gets the element at the given position.  If Java allowed overloading
     * of [], this function would be called as my_list[index]
     * @param index
     * @return
     * @throws IndexOutOfBoundsException if ...
     */
    public E get(int index) throws IndexOutOfBoundsException
    {
        if (index < 0 || index > mSize)
            throw new IndexOutOfBoundsException("Your index is out of bounds");
        
        return (E)mData[index];
    }
    
    
    
    /**
     * Replaces the value at position index with new_val.  Similar to
     * my_list[index] = new_val;
     * @param index
     * @param new_val
     * @throws IndexOutOfBoundsException 
     */
    public void set(int index, E new_val) throws IndexOutOfBoundsException
    {
        if (index < 0 || index > mSize)
            throw new IndexOutOfBoundsException("Your index is out of bounds");
        
        mData[index] = new_val;
    }
    
    
    
    /**
     * Removes the data at the given index by "collpasing" the data that comes
     * after it. 
     * Example: mData is ["a", "b", "c", "d", "e", null, null, null, null, null]
     *    we remove index 2
     * I want mData to be ["a", "b", "d", "e", null, null, null, null, null, null]
     * (I wouldn't re-allocate the array size)
     * @param index 
     */
    public void remove(int index)
    {
        mData[index] = null;
        
        for(int i = index; i < mSize-1; i++){
            if(mData[i+1] != null && mData[i] == null){
                mData[i] = mData[i+1]; 
                mData[i+1] = null;
            }
        }
        mSize--;
    }
    
    
    
    /**
     * Returns a new ArrayList that contains copies of all elements in this
     * list from position start_index (inclusive), up to, but not including
     * end_index (exclusive)
     * @param start_index
     * @param end_index
     * @return 
     */
    public ArrayList subList(int start_index, int end_index)
    {
        ArrayList temp = new ArrayList();
        // FINISH ME: Populate temp
        for (int i = 0; i < mSize; i++){
            if (i >= start_index && i <= end_index){
                temp.add(mData[i]);
            }
        }
        return temp;
    }
    
  
    /**
     * Creates a new Iterator (actually an ArrayListIterator) that is capable
     * of walking through all values in this ArrayList.
     * @param d
     * @return 
     */
    public ArrayListIterator iterator(Direction d)
    {
        // Construct and return a new ArrayListIterator.
        return new ArrayListIterator(this, d);
        // FINISH-ME
    }
    
    
    // A nested class.  Can be assigned to Iterator<E> or Iterator objects
    public class ArrayListIterator implements Iterator<E>
    {
        // Make any attributess you want here (probably a reference to the
        // owner ArrayList and an integer??)
        protected ArrayList mOwner;
        protected Direction direction; 
        private int index;
                
        public ArrayListIterator(ArrayList listData, Direction direction)
        {
            this.mOwner = listData;
            this.direction = direction;
            if(direction == Direction.FWD){
                index = 0;
            }
            else{
                index = listData.getSize();
            }
        }
        
        /**
         * Note: This can be safely called any number of times.
         * @return : true if there is more data to be iterated through
         */
        @Override
        public boolean hasNext()
        {
            if(direction == Direction.FWD){
                return index < mOwner.mSize;
            }
            return index > -1;
        }
        
        /**
         * Return the "current" value *and* advances the iteration.  The user
         * is supposed to call hasNext at least once before calling next.
         * @return 
         */
        @Override
        public E next()
        {
            if(direction == Direction.FWD){
                E currentValue = (E)mOwner.mData[index];            
                index++;
                return currentValue;
            }
            E currentValue = (E)mOwner.mData[index]; 
            index--;
            return currentValue;
        }
    }      
}
