package klein_lab7;

/**
 *
 * @author Matt
 */
public class Edge<E> {

    E value;
    boolean bidirectional;

    public Edge(E value, boolean bidirectional) {
        this.value = value;
        this.bidirectional = bidirectional;
    }

}
