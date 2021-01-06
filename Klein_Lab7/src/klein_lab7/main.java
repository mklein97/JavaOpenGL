package klein_lab7;

import klein_lab7.BinaryHeap.HeapIterator;

/**
 *
 * @author Matt
 */
public class main {

    public static void main(String[] args) {
        // Max if true and min if false
        System.out.println("Testing max queue:");
        BinaryHeap heap = new BinaryHeap(true);
        heap.push(new Float(5.5));
        heap.push(new Float(9.3));
        heap.push(new Float(0.5));
        heap.push(new Float(1.9));
        heap.push(new Float(11.2));
        
        System.out.println("Testing iterator for max queue");
        HeapIterator i = heap.iterator();
        while (i.hasNext()){
            System.out.println(i.next());
        }
        
        System.out.println("Testing pop for max queue");
        System.out.println(heap.pop());
        System.out.println(heap.pop());
        System.out.println(heap.pop());
        System.out.println(heap.pop());
        System.out.println(heap.pop());
        
        System.out.println("Testing min queue:");
        BinaryHeap heap2 = new BinaryHeap(false);
        heap2.push(new Float(5.5));
        heap2.push(new Float(9.3));
        heap2.push(new Float(0.5));
        heap2.push(new Float(1.9));
        heap2.push(new Float(11.2));
        
        System.out.println(heap2.pop());
        System.out.println(heap2.pop());
        System.out.println(heap2.pop());
        System.out.println(heap2.pop());
        System.out.println(heap2.pop());
        
        
    }
    
}
