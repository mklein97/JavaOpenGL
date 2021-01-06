package etec2101;

import java.util.Iterator;
import java.util.LinkedList;

/**
 *
 * @author Matt
 */
public class BucketHashMap extends HashMap {

    protected LinkedList<Pair>[] mTable;
    protected int size;

    public BucketHashMap(int length) {
        this.mTable = new LinkedList[length];
        this.size = 0;
    }

    @Override
    public void put(Object key, Object value) throws UnsupportedOperationException {
        //grow if true
        if (size >= mTable.length * 0.7) {
            int newLength = mTable.length * 2;
            int newSize = 0;
            LinkedList<Pair>[] newTable = new LinkedList[newLength];
            for (LinkedList L : mTable) {
                if (L != null) {
                    for (Object p : L) {
                        int index = ((Pair) p).mKey.hashCode() % newLength;
                        if (newTable[index] == null) {
                            newTable[index] = new LinkedList();
                        }
                        newTable[index].addLast((Pair) p);
                        newSize++;
                    }
                }
            }
            mTable = newTable;
            size = newSize;

        }
        int index = key.hashCode() % mTable.length;

        if (mTable[index] == null) {
            size++;
            mTable[index] = new LinkedList();
        }

        mTable[index].addLast(new Pair(key, value));

    }

    @Override
    public Object get(Object key) {
        int index = key.hashCode() % mTable.length;
        if (!mTable[index].isEmpty()) {
            for (Pair p : mTable[index]) {
                if (p.mKey == key) {
                    return p.mValue;
                }
            }
            return null;
        }
        return null;
    }

    @Override
    public boolean containsKey(Object key) {
        int index = key.hashCode() % mTable.length;
        if (!mTable[index].isEmpty()) {
            for (Pair p : mTable[index]) {
                if (p.mKey == key) {
                    return true;
                }
            }
            return false;
        }
        return false;
    }

    @Override
    public boolean containsValue(Object value) {
        for (LinkedList L : mTable) {
            for (Object p : L) {
                if (((Pair) p).mValue == value) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public boolean remove(Object key) {
        int index = key.hashCode() % mTable.length;
        if (mTable[index] != null) {
            for (Pair p : mTable[index]) {
                if (p.mKey == key) {
                    mTable[index].remove(p);
                    if (mTable[index].isEmpty()){
                      size--;
                      mTable[index] = null;
                    }
                    return true;
                }
                
            }
            return false;
        }
        return false;
    }

    @Override
    public int size() {
        int counter = 0;
        for (LinkedList L : mTable) {
            if (L != null)
                counter += L.size();
        }
        return counter;
    }

    @Override
    public float get_load_factor() {
        return ((float) size) / mTable.length;
    }

    @Override
    public Iterator key_iterator() {
        Iterator I = new HashKeyIterator(mTable);
        return I;
    }

    @Override
    public Iterator value_iterator() {
        Iterator I = new HashValueIterator(mTable);
        return I;
    }

    @Override
    public String toString() {
        String s = "{";
        Iterator K = key_iterator();
        Iterator V = value_iterator();
        while (K.hasNext()) {
            Object k = K.next();
            Object v = V.next();
            s += k.toString();
            s += ":";
            s += v.toString();
            if (K.hasNext())
                s += ", ";
        }
        s += "}";
        return s;
    }

    public class HashKeyIterator implements Iterator {

        // Make any attributess you want here (probably a reference to the
        // owner ArrayList and an integer??)
        protected LinkedList[] mOwner;
        private int listIndex;
        private int arrayIndex;

        public HashKeyIterator(LinkedList[] listData) {
            this.mOwner = listData;
            listIndex = 0;
            arrayIndex = 0;
            for (LinkedList L : mOwner) {
                if (!L.isEmpty()) {
                    break;
                }
                arrayIndex++;
            }
        }

        /**
         * Note: This can be safely called any number of times.
         *
         * @return : true if there is more data to be iterated through
         */
        @Override
        public boolean hasNext() {
            if (listIndex < mOwner[arrayIndex].size()) {
                return true;
            } else {
                for (int i = arrayIndex + 1; i < mOwner.length; i++) {
                    if (mOwner[i] != null) {
                        return true;
                    }
                }
                return false;
            }
        }

        /**
         * Return the "current" value *and* advances the iteration. The user is
         * supposed to call hasNext at least once before calling next.
         *
         * @return
         */
        @Override
        public Object next() {
            if (listIndex < mOwner[arrayIndex].size()) {
                Object o = mOwner[arrayIndex].get(listIndex);
                listIndex++;
                return ((Pair)o).mKey;
            } else {
                for (int i = arrayIndex + 1; i < mOwner.length; i++) {
                    if (mOwner[i] != null) {
                        arrayIndex = i;
                        listIndex = 1;
                        return ((Pair)(mOwner[i].getFirst())).mKey;
                    }
                }
                return null;
            }
        }
    }

    public class HashValueIterator implements Iterator {

        // Make any attributess you want here (probably a reference to the
        // owner ArrayList and an integer??)
        protected LinkedList[] mOwner;
        private int listIndex;
        private int arrayIndex;

        public HashValueIterator(LinkedList[] listData) {
            this.mOwner = listData;
            listIndex = 0;
            arrayIndex = 0;
            for (LinkedList L : mOwner) {
                if (!L.isEmpty()) {
                    break;
                }
                arrayIndex++;
            }
        }

        /**
         * Note: This can be safely called any number of times.
         *
         * @return : true if there is more data to be iterated through
         */
        @Override
        public boolean hasNext() {
            if (listIndex < mOwner[arrayIndex].size()) {
                return true;
            } else {
                for (int i = arrayIndex + 1; i < mOwner.length; i++) {
                    if (!mOwner[i].isEmpty()) {
                        return true;
                    }
                }
                return false;
            }
        }

        /**
         * Return the "current" value *and* advances the iteration. The user is
         * supposed to call hasNext at least once before calling next.
         *
         * @return
         */
        @Override
        public Object next() {
            if (listIndex < mOwner[arrayIndex].size()) {
                Object o = mOwner[arrayIndex].get(listIndex);
                listIndex++;
                return ((Pair)o).mValue;
            } else {
                for (int i = arrayIndex + 1; i < mOwner.length; i++) {
                    if (mOwner[i] != null) {
                        arrayIndex = i;
                        listIndex = 1;
                        return ((Pair) mOwner[i].getFirst()).mValue;
                    }
                }
                return null;
            }
        }
    }

}
