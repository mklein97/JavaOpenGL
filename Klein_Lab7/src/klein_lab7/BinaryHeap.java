package klein_lab7;

import java.util.Iterator;
import java.util.ArrayList;

/**
 *
 * @author Matt
 */
public class BinaryHeap {

    protected boolean maxHeap;
    protected ArrayList<Float> array;
    protected int size;

    public BinaryHeap(boolean maxHeap) {
        this.maxHeap = maxHeap;
        this.array = new ArrayList(1);
        this.array.add(new Float(0.0));
        this.size = 0;
    }

    public void push(Float val) {
            size++;
            this.array.add(size, val);
            if (this.size > 1)
                reheapifyUp();
    }

    public Float pop() {
        swap(1, this.size - 1);
        Float val = this.array.get(this.size - 1);
        this.array.remove(this.size - 1);
        size--;
        reheapifyDown();
        return val;
    }

    public Float peek() {
        return this.array.get(1);
    }

//    public Iterator iterator() {
//
//    }


    public void reheapifyUp() {
        int index = this.size;
        int parent = index >> 1;
        if (maxHeap){
            while (parent >= 1 && (this.array.get(index) > this.array.get(parent))) {
                swap(index, parent);
                index = parent;
                parent = index >> 1;
            }
        }
        else {
            while (parent >= 1 && (this.array.get(index) < this.array.get(parent))) {
                swap(index, parent);
                index = parent;
                parent = index >> 1;
            }
        }
    }

    public void reheapifyDown() {
        int index = 1;
        while ((index << 1) <= this.size) {
            int leftChild = index << 1;
            int largerChild = -1;
            if (maxHeap){
                if (this.array.get(index) < this.array.get(leftChild)) {
                    largerChild = leftChild;
                }
            }
            else {
                if (this.array.get(index) > this.array.get(leftChild)) {
                    largerChild = leftChild;
                }
            }

            int rightChild = (index << 1) + 1;
            if (maxHeap){
                if (rightChild <= this.size) {
                    if (this.array.get(index) < this.array.get(rightChild)) {
                        largerChild = rightChild;
                    }
                }
            }
            else {
                if (rightChild <= this.size) {
                    if (this.array.get(index) > this.array.get(rightChild)) {
                        largerChild = rightChild;
                    }
                }
            }
            
            if (largerChild > 0) {
                int child = largerChild;
                swap(index, child);
                index = child;
            }
            else{
                break;
            }
        }
    }

    public void swap(int i1, int i2) {
        Float obj = this.array.get(i1);
        this.array.set(i1, this.array.get(i2));
        this.array.set(i2, obj);
    }
    
    public HeapIterator iterator(){
        return new HeapIterator(this);
    }
    
    public class HeapIterator implements Iterator<Float>
    {
        protected BinaryHeap mOwner;
        private int index;
                
        public HeapIterator(BinaryHeap heapData)
        {
            this.mOwner = heapData;
            index = 1;
            
        }
        
        /**
         * Note: This can be safely called any number of times.
         * @return : true if there is more data to be iterated through
         */
        @Override
        public boolean hasNext()
        {
           return index <= mOwner.size;
        }
        
        /**
         * Return the "current" value *and* advances the iteration.  The user
         * is supposed to call hasNext at least once before calling next.
         * @return 
         */
        @Override
        public Float next()
        {
          Float currentValue = mOwner.array.get(index);            
          index++;
          return currentValue;
        }
    }
}
