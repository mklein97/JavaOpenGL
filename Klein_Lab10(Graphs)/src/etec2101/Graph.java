package etec2101;

import java.util.HashMap;
import java.util.Iterator;

public class Graph<N, E> {

    protected HashMap<N, HashMap<N, E>> mGraph;
    
    public Graph() {
        mGraph = new HashMap();
    }

    public void addNode(N value) {
        mGraph.put(value, new HashMap<N, E>());

    }

    public void removeNode(N value) {
        mGraph.remove(value);
    }

    public void addEdge(E value, N data1, N data2) {
        mGraph.get(data1).put(data2, value);
    }

    public void removeEdge(N data1, N data2) {
        mGraph.get(data1).remove(data2);
    }

    public void removeEdge2(N node, E edge) {
        N tempKey = null;
        for (HashMap.Entry<N, E> entry : mGraph.get(node).entrySet()) {
            N key = entry.getKey();
            E value = entry.getValue();
            if (value == edge) {
                tempKey = key;
            }
        }
        if (tempKey != null) {
            mGraph.get(node).remove(tempKey);
        }
    }

    public int numNeighbors(N value) {
        return mGraph.get(value).size();
    }
    
    public GraphIterator iterator() {
        return new GraphIterator(this);
    }

    public class GraphIterator implements Iterator<N> {

        protected Graph mOwner;
        private int index;

        public GraphIterator(Graph graphData) {
            this.mOwner = graphData;
            index = 0;

        }

        /**
         * Note: This can be safely called any number of times.
         *
         * @return : true if there is more data to be iterated through
         */
        @Override
        public boolean hasNext() {
            return index < mOwner.mGraph.size();
        }

        /**
         * Return the "current" value *and* advances the iteration. The user is
         * supposed to call hasNext at least once before calling next.
         *
         * @return
         */
        @Override
        public N next() {
            int counter = 0;
            N key = null;
            java.util.Set<HashMap.Entry<N, HashMap<N, E>>> set = mOwner.mGraph.entrySet();
            for (HashMap.Entry<N, HashMap<N, E>> entry : set) {
                key = entry.getKey();
                if(counter == index)
                    break;
                counter++;
            }
            index++;
            return key;
        }
    }
}
