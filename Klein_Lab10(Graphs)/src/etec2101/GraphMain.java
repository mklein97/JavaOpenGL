package etec2101;

import etec2101.Graph.GraphIterator;

/**
 *
 * @author Matt
 */
public class GraphMain {
    static Graph g;

    public static void main(String[] args) {
        g = new Graph();
        g.addNode("Q");
        g.addNode("R");
        g.addNode("S");
        g.addNode("T");
        g.addNode("U");
        g.addNode("V");
        
        g.addEdge(new Edge("mole", false), "Q", "T");
        g.addEdge(new Edge("toad", false), "T", "R");
        g.addEdge(new Edge("mouse", true), "R", "V");
        g.addEdge(new Edge("dog", true), "R", "U");
        g.addEdge(new Edge("cow", false), "R", "S");
        g.addEdge(new Edge("cat", true), "S", "S");
        g.addEdge(new Edge("rat", false), "S", "Q");
        
        System.out.println("Printing graph nodes");
        GraphIterator graphit = g.iterator();
        while (graphit.hasNext())
            System.out.println(graphit.next());
        
        g.removeNode("S");
        System.out.println("Printing graph after removing node S");
        graphit = g.iterator();
        while (graphit.hasNext())
            System.out.println(graphit.next());
        
        System.out.println("Printing number of neighbors for node R");
        System.out.println(g.numNeighbors("R"));
    }
    
}
