package application;

/**
 * Implementation of Djikstra sort
 * 
 * @author Jason Sutanto (jsutanto2@wisc.edu)
 */
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Set;

public class djikstra extends Graph {
  Graph network = new Graph();

  /**
   * This method performs a djikstra sort and returns the shortest path of a graph starting from
   * source
   * 
   * 
   * @param source specifies which node to start sorting
   * @return a list containing shortest path of a graph starting from source
   */
  public List<String> djikstraSort(String source, String destination) {
    // shortest path
    ArrayList<String> shortestPath = new ArrayList<String>();
    // list of all users
    ArrayList<Person> users = (ArrayList<Person>) network.getAllVertices();
    // predecessor of users in graph
    ArrayList<Person> pred = new ArrayList<Person>();
    // total weight of all graph
    int[] totalWeight = new int[network.order()];
    // check if vertex is visited
    Set<Person> visited = new HashSet<Person>();
    // start vertex
    Person sourceVertex = null;
    // destination vertex
    Person destinationVertex = null;
    // get Index of start
    int startIndex = 0;
    // destination index
    int endIndex = 0;
    // find start vertex
    for (int i = 0; i < network.size(); ++i) {
      if (users.get(i).getName().equals(source)) {
        sourceVertex = users.get(i);
        startIndex = i;
      } else if (users.get(i).getName().equals(destination)) {
        endIndex = 0;
        destinationVertex = users.get(i);
      }
    }

    // set all nodes total weight to infinity
    for (int i = 0; i < totalWeight.length; ++i) {
      totalWeight[i] = Integer.MAX_VALUE;
    }
    // set sources totalWeight to 0
    totalWeight[startIndex] = 0;
    // create a priority queue
    PriorityQueue<Person> pq = new PriorityQueue<Person>();
    pq.add(sourceVertex);

    while (!pq.isEmpty()) {
      int index = 0;
      int newDistance = totalWeight[startIndex];

      Person c = pq.remove();

      // check neighbors distances
      for (int i = 0; i < c.getFriendList().length; ++i) {
        Person s = c.getFriendList()[i];
        // get index of neighbors
        for (int j = 0; j < users.size(); ++i) {
          if (users.get(j).equals(s)) {
            index = i;
            break;
          }
        }
        // check if neighbor is visited
        if (!visited.contains(s)) {
          // increment distance from source which is one
          newDistance += 1;
          // update total weight if less then distance from start to current node
          if (newDistance < totalWeight[index]) {
            totalWeight[index] = newDistance;
          }
          // update successors predecessor to c
          pred.add(index, c);
          // insert sucessor to priority queue
          pq.add(s);
        }
      }
    }
    // add nodes from predecessor list to shortestPath list start from destination
    shortestPath.add(destinationVertex.getName());
    int nextPred = endIndex;
    while (pred.get(nextPred) != null) {
      shortestPath.add(pred.get(nextPred).getName());
      nextPred = users.indexOf(pred.get(nextPred));
    }
    // revers shortestPath
    Collections.reverse(shortestPath);

    return shortestPath;
  }
}
