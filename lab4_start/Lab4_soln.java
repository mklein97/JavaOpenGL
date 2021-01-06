package lab4_jw;		// Change this to have YOUR initials!

import etec2101.*;
import java.util.Arrays;
import java.util.Iterator;

public class Lab4_soln 
{
    public static void main(String[] args) 
    {
        LinkedList<String> LL = new LinkedList<String>();
        
        
        LL.addLast("Bob");
        LL.addFirst("Sue");
        LL.addLast("Tom");
        System.out.println("LL = " + LL);
        
        LL.add(0, "Abe");
        LL.add(4, "Xavier");
        LL.add(2, "Carl");
        System.out.println("LL = " + LL);
        
        System.out.println("LL.indexOf(\"Carl\") = " + LL.indexOf("Carl"));
        System.out.println("LL.indexOf(\"Dude\") = " + LL.indexOf("Dude"));
        
        System.out.println("LL[0] = " + LL.get(0));
        System.out.println("LL[3] = " + LL.get(3));
        //System.out.println("LL[99]" + LL.get(99));
        
        LL.remove(LL.indexOf("Carl"));
        System.out.println("LL = " + LL);
        System.out.println("LL.size() = " + LL.size());
        
        Object[] arr = LL.toArray();
        System.out.println("Array copy of LL = " + Arrays.toString(arr));
        
        Iterator<String> it = LL.iterator();
        while (it.hasNext())
        {
            String temp = it.next();
            System.out.println(temp);
        }
            
        
        LL.clear();
        System.out.println("LL = " + LL);
        System.out.println("LL.size() = " + LL.size());
    }
    
}
