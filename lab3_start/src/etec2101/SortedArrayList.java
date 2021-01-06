package etec2101;

public class SortedArrayList<E extends Comparable> extends ArrayList<E>
{
    // Internally, we'll need to sort items
    // in the array list.  Problem: a < b doesn't work!
    // There is a work-around: the Comparable interface
    // (if a class implements this interface, they
    // must provide a compareTo method)
    @Override
    public void add(E new_val)
    {
        // Grow the array if necessary
        // FINISH-ME (Some bonus points for BinarySearch)
        // Note: There's a bug in this code...
        // Add new_val to the end of the array (if room) --        
        if (mSize < mData.length){
            mData[mSize] = new_val;
            mSize++;
        }
        
        
        // Start from beginning, find where this new value should
        // go.
        for (int i = 0; i < mSize - 1; i++)
        {
            // Every object of type E has a compareTo method
            // a.compareTo(b) will return
            //   a negative int if a < b
            //   0 if a == b
            //   a positive int if a > b
            
            // We want to determine if new_val goes between
            // element i and i+1.
            if (new_val.compareTo(mData[i]) >= 0 && 
                new_val.compareTo(mData[i+1]) <= 0)
            {
                // Make room for it.  Probably right??
                System.arraycopy(mData, i+1, mData, i+2, mSize + i);
                mData[i+1] = new_val;
            }
        }
        //Example: mData is ["d", "c", "a"]
    }
    
    
   // @Override
//    public int indexOf(E val)
//    {
//    }
}
