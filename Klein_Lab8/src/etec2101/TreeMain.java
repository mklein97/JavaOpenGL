package etec2101;

import java.util.Iterator;
import etec2101.BinarySearchTree.TraversalType;

/**
 *
 * @author Matt
 */
public class TreeMain {
    
    
    public static void main(String[] args){
        BinarySearchTree b = new BinarySearchTree();
        System.out.println("Adding 5, 1, 10, 15, 4 to tree, respectively");
        b.add(5);
        b.add(1);
        b.add(10);
        b.add(15);
        b.add(4);
        System.out.println("Testing toString:");
        System.out.println(b.toString());
        System.out.println("Testing size:");
        System.out.println(b.size());
        System.out.println("Testing if tree contains 1:");
        System.out.println(b.contains(1));
        System.out.println("Testing if tree contains 7:");
        System.out.println(b.contains(7));
        b.remove(4);
        System.out.println("Testing if tree contains 4 after removing it:");
        System.out.println(b.contains(4));
        
        System.out.println("Testing iterator:");
        Iterator i = b.iterator(TraversalType.IN_ORDER);
        while (i.hasNext()){
            System.out.println(i.next());
        }
        
        BinarySearchTree b2 = new BinarySearchTree();
        b2.add(6);
        b2.add(5);
        b2.add(4);
        b2.add(3);
        b2.add(2);
        b2.add(1);
        System.out.println("Printing unbalanced tree:");
        System.out.println(b2.toString());
        System.out.println("Testing rebalance:");
        b2.rebalance();
        System.out.println(b2.toString());
    }
    
}
