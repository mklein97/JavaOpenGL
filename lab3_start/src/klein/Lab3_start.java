/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package klein;

import etec2101.*;
import java.util.Iterator;
//import java.util.ArrayList;

/**
 *
 * @author jwitherell, klein
 */
public class Lab3_start
{

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args)
    {
        ArrayList.set_increment_size(2);
        
        ArrayList<String> x = new ArrayList<String>();
        x.add("abe");
        x.add("bob");
        x.add("charles");
        x.add("doug");
        x.add("eric");
        
        
    
        ArrayList<String> y = x.subList(1, 3);
        System.out.println("The items in the sublistlist (indices 1-3) are: ");
        Iterator h = y.iterator(ArrayList.Direction.FWD);
        while (h.hasNext())
        {
            String s = (String)h.next();
            System.out.println(s);
        }
        
        // Test our iterator, walk through all values.
        System.out.println("The items in the arraylist are: ");
        Iterator i = x.iterator(ArrayList.Direction.FWD);
        while (i.hasNext())
        {
            String s = (String)i.next();
            System.out.println(s);
        }
        System.out.println("What indexOf returns when 'bob' is passed in is: " + x.indexOf("bob"));
        
        x.remove(1);
        System.out.println("The items in the arraylist after removing index 1 (bob) and using a reversed iterator are: ");
        Iterator j = x.iterator(ArrayList.Direction.REV);
        while (j.hasNext())
        {
            String s = (String)j.next();
            System.out.println(s);
        }
        
        x.clear();
        System.out.println("The items in the arraylist after clearing it are: ");
        Iterator k = x.iterator(ArrayList.Direction.FWD);
        while (k.hasNext())
        {
            String s = (String)k.next();
            System.out.println(s);
        }
        
        
        SortedArrayList<String> z = new SortedArrayList<>();
        z.add("something6");        
        z.add("something2");
        z.add("something4");
        z.add("something4");
        z.add("something1");

//         //Note: Integers implement the Comparable interface
//        Integer b = new Integer(15);
//        Integer a = new Integer(14);
//        // DON'T ===> if (a < b)
//        if (a.compareTo(b) < 0)
//        {
//            System.out.println("a is less than b");
//        }
    }
    
}
