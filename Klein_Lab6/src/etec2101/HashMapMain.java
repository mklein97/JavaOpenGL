package etec2101;

/**
 *
 * @author Matt
 */
public class HashMapMain {

    public static void main(String[] args) {
        BucketHashMap bMap = new BucketHashMap(5);
        bMap.put("Tobyn", 1);
        bMap.put("Troy", 2);
        bMap.put("Mikey", -1);
        bMap.put("Graham", 3);
        bMap.put("Tony", 4);
        System.out.println(bMap.toString());
        System.out.println("testing get method for key 'Tobyn':");
        System.out.println(bMap.get("Tobyn").toString());
        System.out.println("testing containsKey method for 'Troy'");
        System.out.println(bMap.containsKey("Troy"));
        System.out.println("testing containsValue for value '-1':");
        System.out.println(bMap.containsValue(-1));
        System.out.println("testing size method:");
        System.out.println(bMap.size());
        System.out.println("testing remove method for key 'Tobyn':");
        System.out.println(bMap.remove("Tobyn"));
        System.out.println(bMap.toString());
        System.out.println("testing size method");
        System.out.println(bMap.size());
        System.out.println("testing get_load_factor");
        System.out.println(bMap.get_load_factor());
    }
    
}
