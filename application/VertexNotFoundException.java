package application;

/**
 * Checked exception thrown if the vertex being removed 
 * does not exist in the graph
 */
@SuppressWarnings("serial")
public class VertexNotFoundException extends Exception {
    private Object key;
    
    public VertexNotFoundException() {
        this.key = null;
    }
    
    public VertexNotFoundException(Object key) {
        this.key = key;
    }
    
    public Object getKey() {
        return this.key;
    }
}
