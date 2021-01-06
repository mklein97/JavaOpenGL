//package etec2101;
//
//import java.util.Iterator;
//
///**
// *
// * @author Matt
// */
//public class ProbingHashMap extends HashMap {
//    
//    protected Pair[] mTable;
//    protected int size;
//
//    public ProbingHashMap(int size){
//        this.mTable = new Pair[size];
//        this.size = size;
//    }
//    
//    @Override
//    public void put(Object key, Object value) throws UnsupportedOperationException {
//        //throw new UnsupportedOperationException("Not supported yet.");
//        //hash key
//        int index = key.hashCode() % size;
// 
//        //check for valid index
//        if (mTable[index] == null)
//            mTable[index] = new Pair(key, value);
//        
//        //if not start probing 
//        else{
//            
//            
//            for (int i = 0; i < mTable.length; i++){
//                int j = (i + index) % size;
//            }
//        }
//    }
//
//    @Override
//    public Object get(Object key) {
//       
//    }
//
//    @Override
//    public boolean containsKey(Object key) {
//        
//    }
//
//    @Override
//    public boolean containsValue(Object value) {
//       
//    }
//
//    @Override
//    public boolean remove(Object key) {
//       
//    }
//
//    @Override
//    public int size() {
//        
//    }
//
//    @Override
//    public float get_load_factor() {
//       
//    }
//
//    @Override
//    public Iterator key_iterator() {
//  
//    }
//
//    @Override
//    public Iterator value_iterator() {
//      
//    }
//
//    @Override
//    public String toString() {
//      
//    }
//    
//}
