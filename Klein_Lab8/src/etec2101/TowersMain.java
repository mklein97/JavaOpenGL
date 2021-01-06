package etec2101;
import java.util.ArrayList;

/**
 *
 * @author Matt
 */
public class TowersMain {
   
    public static void main(String[] args) {
        int counter = 1;
        int n = 20;
        ArrayList<Integer> list = hanoiSolver(n);
        ArrayList<Integer> p1 = new ArrayList();
        ArrayList<Integer> p2 = new ArrayList();
        ArrayList<Integer> p3 = new ArrayList();
        
        for (int i = n; i > 0; i--){
            p1.add(i);
        }
        
        System.out.println("+------------------");
        System.out.println("| step 0");
        System.out.println("+------------------");
        System.out.println("|" + mString(p1));
        System.out.println("+------------------");
        
        for (Integer i: list){
            System.out.println("+------------------");
            System.out.println("| step " + counter);
            System.out.println("+------------------");
            if (p1.contains(i)){
                p1.remove(i);
                boolean b = true;
                if (!p2.isEmpty() && p2.get(p2.size()-1) < i)
                    b = false;
                if (!p2.contains(i) && b)
                    p2.add(i);
                else 
                    p3.add(i);
            }
            else if (p2.contains(i)){
                p2.remove(i);
                boolean b = true;
                if (!p3.isEmpty() && p3.get(p3.size()-1) < i)
                    b = false;
                if (!p3.contains(i) && b)
                    p3.add(i);
                else 
                    p1.add(i);
            }
            else if (p3.contains(i)){
                p3.remove(i);
                boolean b = true;
                if (!p1.isEmpty() && p1.get(p1.size()-1) < i)
                    b = false;
                if (!p1.contains(i) && b)
                    p1.add(i);
                else
                    p2.add(i);
            }
            
            System.out.println("|" + mString(p1));
            System.out.println("|" + mString(p2));
            System.out.println("|" + mString(p3));
            System.out.println("+------------------");
            counter++;
        }
    }
    
    static ArrayList<Integer> hanoiSolver(int n){
        ArrayList<Integer> list = new ArrayList();
        ArrayList<Integer> rList = null;
        if (n > 1){
           rList = hanoiSolver(n-1);
           list.addAll(rList);
        }
        
        list.add(n);
        if (n > 1){
           list.addAll(rList);
        }
        return list;
    }
    
    static String mString (ArrayList<Integer> l){
        String s = "";
        for (Integer i: l){
            s += i;
            s += " ";
        }
        return s;
    }
    
}
