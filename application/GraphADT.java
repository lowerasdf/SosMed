package application;

import java.util.List;

/**
 * Filename:        GraphADT.java
 * ATEAM Number:    69
 * Project:         Team Project - Social Network
 * Author(s):       Daniel Khong Ang Toong  
 * 
 * This interface outlines the necessary methods for an undirected graph with
 * unweighted edges
 * 
 * @param T Generic variable for the object type to become the vertex
 * @param K Generic variable for the object type of the key to the vertices
 */
public interface GraphADT<K, T> {
    /**
     * Adds a new vertex to the graph given the key
     *
     * If the graph already contains a vertex with the same key, a
     * DuplicateKeyException is thrown and the graph is unchanged.
     * 
     * @param key The key of the vertex to be added
     * @throws DuplicateKeyException if the graph already contains a vertex 
     *         with the same key
     */
    public void addVertex(K key) throws DuplicateKeyException;
    
    /**
     * Remove a vertex and all associated edges from the graph given
     * the key
     * 
     * If the graph does not contain the vertex, a 
     * VertexNotFoundException is thrown with the non-existent key
     * set as the exception's key and the graph is unchanged.
     *  
     * @param key The key of the vertex to be removed
     * @throws VertexNotFoundException if the graph does not contain a vertex
     *         with the matching key
     */
    public void removeVertex(K key) throws VertexNotFoundException;
    
    /**
     * Adds an unweighted, undirected edge between two vertices
     * to the graph given the vertices' keys
     * 
     * If either vertex does not exist, vertex is added and then
     * the edge is created.
     *
     * If the edge already exists in the graph, a DuplicateEdgeException is 
     * thrown and the graph is unchanged.
     *  
     * @param key1 The key of the first vertex
     * @param key2 The key of the second vertex
     * @throws DuplicateEdgeException if the graph already contains the edge
     */
    public void addEdge(K key1, K key2) throws DuplicateEdgeException;

    
    /**
     * Remove the edge between two vertices from the graph given the
     * vertices' keys
     * 
     * If either vertex does not exist, a VertexNotFoundException is thrown
     * with the with the non-existent key set as the exception's key.
     * If both vertices do not exist, the exception is thrown with a null key. 
     * In both cases, the graph is unchanged.
     * 
     * If an edge between the vertices does not exist, an EdgeNotFoundException
     * is thrown and the graph is unchanged.
     *  
     * @param key1 The key of the first vertex
     * @param key2 The key of the second vertex
     * @throws VertexNotFoundException if the graph does not contain either
     *         vertex
     * @throws EdgeNotFoundException if the graph does not contain the edge to
     *         be removed
     */
    public void removeEdge(K key1, K key2) throws VertexNotFoundException, 
        EdgeNotFoundException;
    
    /**
     * Clears all vertices and their associated edges from the graph
     */
    public void clearGraph();
    
    /**
     * Returns the reference to a vertex in the graph given its key
     * 
     * If a vertex with the matching key is not found, a 
     * VertexNotFoundException is thrown with the non-existent key set
     * as the exception's key.
     * 
     * @param key The key of the vertex to search for
     * @return the vertex in the graph with the matching key
     * @throws VertexNotFoundException if the graph does not contain a vertex
     *         with the matching key
     */
    public T getVertex(K key) throws VertexNotFoundException;
    
    /**
     * Returns a List that contains all the vertices in the graph
     * 
     * @return a List<T> that contains all the vertices in the graph
     */
    public List<T> getAllVertices();
    

    /**
     * Returns the number of edges in this graph
     * 
     * @return number of edges in the graph
     */
    public int size();
    
    
    /**
     * Returns the number of vertices in this graph
     * 
     * @return number of vertices in graph
     */
    public int order();

}