package etec2101;

import java.util.Iterator;

public abstract class HashMap<K, V> 
{
    /**
     * A simple class for storing the key-value pair.  Initially, I thought
     * we could just do a table of type V.  But...once the add operation is
     * done, we would have lost the key information.  Thus, this class is
     * probably necessary.
     */
    protected class Pair
    {
        protected K mKey;
        protected V mValue;
        public Pair(K k, V v)
        {
            mKey = k;
            mValue = v;
        }
    }
    
    
    
    /**
     * Associates the given value with the given key.  Note: this is the 
     * HashMap's equivalent to an "add" operation.  But...it also can be
     * used to replace the value currently associated with the given key.
     * Make sure to "grow" the internal container if we are over our
     * load_factor.
     * @param key
     * @param value 
     */
    public abstract void put(K key, V value) throws UnsupportedOperationException;
    
    /**
     * Gets the value associated with the given key, or null if that key is
     * not found.  If you need to distinguish between a null-value and a non-
     * existent key, you should use containsKey.
     * @param key
     * @return 
     */
    public abstract V get(K key);
    
    /**
     * @param key
     * @return true if the given key is in this HashMap, false if not. 
     */
    public abstract boolean containsKey(K key);
    
    /**
     * @param value
     * @return true if the given value is in this HashMap, false if not.  Note:
     * this is a slow operation.  Use containsKey if possible.
     */
    public abstract boolean containsValue(V value);
    
    /**
     * Removes the key-value pair identified by the given key from the 
     * HashMap.
     * @param key 
     * @return true if the item was removed (and found), false if not.
     */
    public abstract boolean remove(K key);
    
    /**
     * @return The number of key-value pairs in the HashMap. 
     */
    public abstract int size();
    
    
    /**
     * @return the current load factor (used-slots / capacity)
     */
    public abstract float get_load_factor();
    
    /**
     * @return an iterator which yields all keys in an unspecified order. 
     */
    public abstract Iterator<K> key_iterator();
    
    /**
     * @return an iterator which yields all values in an unspecified order. 
     */
    public abstract Iterator<V> value_iterator();
    
    /**
     * @return a String of the form '{key1:value1, key2:value2, ...}
     * (in an unspecified order)
     */
    @Override
    public abstract String toString();
}