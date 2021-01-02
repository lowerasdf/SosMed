package application;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Scanner;
import java.util.Set;
import java.util.Stack;

/**
 * Filename:        Network.java
 * ATEAM Number:    69
 * Project:         Team Project - Social Network
 * 
 * This class contains the implementation for a social network that stores 
 * Person users and their friendships. Functionalities include adding and
 * removing users and friendships, loading and saving the network from/to a
 * file, finding the mutual friends between two users, finding the number of 
 * connected components in the network, and finding the shortest friendship 
 * path between two users.
 */
public class Network {

    private Graph graph; // To store the graph of users and friendships for this network
    private List<String> log; // To store the history of changes to the graph
    private String centralUser; // The name of the central user of the network
    
    /**
     * Initializes the network
     */
    public Network() {
        this.graph = new Graph();
        this.log = new ArrayList<String>();
    }
    
    /**
     * @return A List of all the users in the network
     */
    public List<Person> getAllUsers(){
        return this.graph.getAllVertices();
    }
    
    /**
     * Adds a new user to the network
     * 
     * @param name Name of the user to be added
     * @throws DuplicateKeyException if the user is already in the network
     */
    public void addUser(String name) throws DuplicateKeyException {
        this.graph.addVertex(name);

        // Executed if no exceptions thrown
        this.log.add("a " + name);
    }
    
    /**
     * Removes a user from the network
     * 
     * @param name Name of the user to be removed
     * @throws VertexNotFoundException if the user is not found in the network
     */
    public void removeUser(String name) throws VertexNotFoundException {
        this.graph.removeVertex(name);

        // Executed if no exceptions thrown
        this.log.add("r " + name);
    }
    
    /**
     * Adds a friendship to the network between two users with names name1 and 
     * name2. If any of those users do not exist, they are added to the network
     * first.
     * 
     * @param name1 Name of the first user in the friendship
     * @param name2 Name of the second user in the friendship
     * @throws DuplicateEdgeException if the friendship is already in the network
     */
    public void addFriend(String name1, String name2)
            throws DuplicateEdgeException {
        this.graph.addEdge(name1, name2);

        // Executed if no exceptions thrown
        this.log.add("a " + name1 + " " + name2);
    }
    
    /**
     * Removes a friendship to the network between two users with names name1 
     * and name2.
     * 
     * @param name1 Name of the first user in the friendship
     * @param name2 Name of the second user in the friendship
     * @throws VertexNotFoundException if either or both of the users in the 
     *         friendship do not exist in the network
     * @throws EdgeNotFoundException if the friendship to be removed is not
     *         found in the network
     */
    public void removeFriend(String name1, String name2)
            throws VertexNotFoundException, EdgeNotFoundException {
        this.graph.removeEdge(name1, name2);

        // Executed if no exceptions thrown
        this.log.add("r " + name1 + " " + name2);
    }
    
    /**
     * Removes all the users and their friendships from the network and clears
     * the log
     * 
     */
    public void clearNetwork() {
        this.graph.clearGraph();
        this.log = new ArrayList<String>();
    }
    
    /**
     * Returns a List of all the friends of a user given the name of the user
     * 
     * @param name Name of the user to return friend list for
     * @return a List of all the friends of the user
     * @throws VertexNotFoundException if the user does not exist in the network
     */
    public List<Person> getFriends(String name) throws VertexNotFoundException {
        List<Person> friendList = new LinkedList<>(); // To store friend list
        
        // Iterates through the person's friend list and adds each user in
        // the friend list to friendList
        for (Person friend : this.graph.getVertex(name).getFriendList()) {
            if (friend == null) {
                break;
            }
            
            friendList.add(friend);
        }
        
        return friendList;
    }
    
    /**
     * Changes the central user and updates the log file whenever a new central 
     * user is being set for the network
     * @throws VertexNotFoundException 
     */
    public void setCentralUser(String name) throws VertexNotFoundException {
        this.graph.getVertex(name); // To check if a user with the given name exists
        this.centralUser = name;
        this.log.add("s " + name);
    }
    
    /**
     * @return The name of the central user of the network
     */
    public String getCentralUser(String name) {
        return this.centralUser;
    }
    
    /**
     * @return The command log of the network
     */
    public List<String> getLog() {
        return this.log;
    }
    
    /**
     * Parses a log input file into commands for the network and executes those
     * commands. If there is a syntax error in any of those commands, an
     * exception is thrown and the graph remains unchanged.
     * 
     * @param inputFile The File object containing the log file
     * @return the name of the latest central user set in the log file, null if none
     * @throws FileNotFoundException if the file cannot be found
     * @throws InvalidSyntaxException if the file contains a line with invalid
     *         syntax, or if an exception is thrown when executing a valid
     *         addUser, addFriend, removeUser, removeFriend, or setCentralUser
     *         command
     */
    public String parse(File inputFile) throws FileNotFoundException, InvalidSyntaxException {
        
        // Scanner object to read the input file
        Scanner scnr = null;
        
        // To store the graph before parsing in case a line with invalid syntax is encountered
        Graph tempGraph = this.graph; 

        // To store central user set in log file, remains as null if no 
        // central user is set in log file
        String centralUser = null;
        
        try {
            scnr = new Scanner(inputFile);
            
            int lineNum = 0; // to keep track of the line number in the file
            
            // While there are lines in the file
            while (scnr.hasNextLine()) {
                
                ++lineNum; // increase line number
                
                // Reads the next command line and stores it as a list of Strings
                String commandLine = scnr.nextLine();
                String trimmedCommand = commandLine.trim();
                
                // Ignore lines of only whitespace
                if (trimmedCommand.length() == 0) {
                    continue;
                }
                
                String[] command = trimmedCommand.split(" ");
                
                try {
                    switch (command[0]) {
                        case "a":
                        case "A":
                            if (command.length == 2) {
                                // Add user
                                addUser(command[1]);
                            } else if (command.length == 3) {
                                // Add friend
                                addFriend(command[1], command[2]);
                            } else {
                                // Invalid syntax, reset graph and throw exception
                                this.graph = tempGraph;
                                throw new InvalidSyntaxException(lineNum);
                            }
                            break;
                        case "r":
                        case "R":
                            if (command.length == 2) {
                                // Remove user
                                removeUser(command[1]);
                            } else if (command.length == 3) {
                                // Remove friend
                                removeFriend(command[1], command[2]);
                            } else {
                                // Invalid syntax, reset graph and throw exception
                                this.graph = tempGraph;
                                throw new InvalidSyntaxException(lineNum);
                            }
                            break;
                        case "s":
                        case "S":
                            if (command.length == 2) {
                                // Set central user
                                centralUser = command[1];
                                setCentralUser(centralUser);
                            } else {
                                // Invalid syntax, reset graph and throw exception
                                this.graph = tempGraph;
                                throw new InvalidSyntaxException(lineNum);
                            }
                            break;
                        default:
                            // Invalid syntax, reset graph and throw exception
                            this.graph = tempGraph;
                            throw new InvalidSyntaxException(lineNum);
                    }
                } catch (DuplicateKeyException | DuplicateEdgeException | 
                        VertexNotFoundException | EdgeNotFoundException e) {
                    // Adding or removing error, reset graph and throw exception
                    this.graph = tempGraph;
                    throw new InvalidSyntaxException(lineNum);
                }
            }
            
            return centralUser; // The name of the central user if set by log file
            
        } finally {
            scnr.close();
        }
    }
    
    /**
     * Saves the log of the network to a file with a user defined name and at a
     * user defined location in their file system.
     * 
     * @param logFileName Name of the path to the save file to save the log
     */
    public void save(String logFileName) throws IOException {       
        FileWriter filewriter = null;
        
        try {
            filewriter = new FileWriter(logFileName);
            
            for (int i = 0; i < this.log.size(); ++i) {  // loop to write all commands to file
                filewriter.write(log.get(i) + "\n");
            }
            
        } catch (IOException e) {
            throw e;
        } finally { // exception handling
            if (filewriter != null) {
                filewriter.close();
            }
        }
    }
    
    /**
     * Returns a list of the mutual friends between 2 users given the users'
     * names.
     * 
     * @param name1 Name of user 1
     * @param name2 Name of user 2
     * @return a List of mutual friends between user 1 and user 2
     * @throws VertexNotFoundException when either or both users are not in the
     *         network
     */
    public List<String> mutualConnections(String name1, String name2)
            throws VertexNotFoundException {

        Person[] user1Friends; // To store user1's friend list

        try {
            user1Friends = this.graph.getVertex(name1).getFriendList();
        } catch (VertexNotFoundException e) {
            try {
                this.graph.getVertex(name2);
            } catch (VertexNotFoundException f) { // If both users do not exist
                throw new VertexNotFoundException();
            }

            throw new VertexNotFoundException(name1); // Only user 1 does not
                                                      // exist
        }

        // To store user 2's friendlist
        Person[] user2Friends = this.graph.getVertex(name2).getFriendList();

        List<String> mutuals = new ArrayList<String>();

        for (int i = 0; i < user1Friends.length; ++i) {
            // Unused portion of friend list reached
            if (user1Friends[i] == null) {
                break;
            }

            for (int j = 0; j < user2Friends.length; ++j) {
                // Unused portion of friend list reached
                if (user2Friends[j] == null) {
                    break;
                }

                if (user2Friends[j].getName()
                        .equals(user1Friends[i].getName())) {
                    mutuals.add(user1Friends[i].getName());
                    break;
                }
            }
        }

        return mutuals;
    }
    
    /**
     * Returns the number of connected components in this network.
     * 
     * @return the number of connected components in this network
     */
    public int numConnectedComponents() {
        // To keep track of which users are explored
        List<Person> allUsers = this.graph.getAllVertices();
        boolean[] visited = new boolean[allUsers.size()]; // Initially all false
        int remainingUsers = allUsers.size(); // Unvisited users
        int connectedCompCount = 0; // Keep track of how many DFS occured

        // While there are still unexplored users in the network
        while (remainingUsers > 0) {
            int visitIndex; // To store index of unvisited user

            for (visitIndex = 0; visitIndex < visited.length; ++visitIndex) {
                // Unvisited user found
                if (!visited[visitIndex]) {
                    connectedCompCount++; // Unexplored users indicate new
                                          // connected component
                    break;
                }
            }

            // Depth first search is used to explore a connected component

            // Stack to be used in DFS
            Stack<Person> visitStack = new Stack<Person>();

            // Add a vertex to the stack and mark it as visited, decrement
            // remainingUsers
            visitStack.push(allUsers.get(visitIndex));
            visited[visitIndex] = true;
            remainingUsers--;

            while (!visitStack.isEmpty()) {
                Person current = visitStack.peek();
                Person[] currFriendList = current.getFriendList();

                int unvisited = -1; // Remains as -1 if all friends of a user
                                    // are visited
                for (int i = 0; i < currFriendList.length; ++i) {
                    if (currFriendList[i] == null) {
                        break;
                    }

                    // Unvisited friend found
                    if (!visited[allUsers.indexOf(currFriendList[i])]) {
                        unvisited = i;
                        break;
                    }
                }

                // No unvisited friend
                if (unvisited == -1) {
                    visitStack.pop();
                } else {
                    // Push unvisited user to stack and mark as visited
                    // Decrement remainingUsers
                    visitStack.push(currFriendList[unvisited]);
                    visited[allUsers.indexOf(currFriendList[unvisited])] = true;
                    remainingUsers--;
                }
            }
        }

        return connectedCompCount;
    }

    /**
     * This method performs a Djikstra sort and returns the shortest path of a 
     * graph starting from source
     * 
     * 
     * @param source specifies which node to start sorting
     * @return a list containing shortest path of a graph starting from source
     * @throws VertexNotFoundException 
     */
    public List<String> djikstraSort(String source, String destination) throws VertexNotFoundException {
        // Inner class to be used in priority queue
        class Pair implements Comparable<Pair> {
            private Person user;
            private int weight;
            
            private Pair(Person user, int weight) {
                this.user = user;
                this.weight = weight;
            }

            @Override
            public int compareTo(Pair otherPair) {
                return (this.weight - otherPair.weight);
            }
        }
        
        // List to store the shortest path
        List<String> shortestPath = new ArrayList<String>();
        // List of all users in the network
        List<Person> users = graph.getAllVertices();
        // Predecessor list of users in network, initialized to all null
        Person[] pred = new Person[users.size()];
        // Pair array to store total weight to each user in the network
        Pair[] totalWeight = new Pair[users.size()];
        // To store visited users
        // All users are unvisited initially
        Set<Person> visited = new HashSet<Person>();
      
        Person sourceVertex; // to store user at start of search
        try {
            
            sourceVertex = this.graph.getVertex(source);
            
        } catch (VertexNotFoundException e) {
            try {
                this.graph.getVertex(destination);
                
                // Only source user does not exist in network
                throw e;
                
            } catch (VertexNotFoundException f) {
                // Both users do not exist in network
                throw new VertexNotFoundException(null);
            }
        }
        
        // To store user at destination of search
        // Throws exception if no user with the same name found
        Person destinationVertex = this.graph.getVertex(destination);
        
        // Set all Pairs' total weight to infinity
        for (int i = 0; i < totalWeight.length; ++i) {
            totalWeight[i] = new Pair(users.get(i), Integer.MAX_VALUE);
        }
        
        // Set source's totalWeight to 0
        totalWeight[users.indexOf(sourceVertex)].weight = 0;
        
        // Create a minimum priority queue for Dijkstra
        PriorityQueue<Pair> pq = new PriorityQueue<Pair>();
        pq.add(totalWeight[users.indexOf(sourceVertex)]);

        while (!pq.isEmpty()) {
            Pair currentPair = pq.remove();
            Person current = currentPair.user;
            int currWeight = currentPair.weight;
            
            if (visited.contains(current)) {
                continue;
            } else {
                visited.add(current);
            }
            
            Person[] friends = current.getFriendList(); // current user's friends
            
            // check neighbors distances
            for (int i = 0; i < friends.length; ++i) {
                // Unused portion of friend list reached
                if (friends[i] == null) {
                    break;
                }
                
                Person successor = friends[i];
                
                // Skip successor if visited
                if (visited.contains(successor)) {
                    continue;
                }
                
                // Get user index pair of successor
                Pair succPair = totalWeight[users.indexOf(successor)];
                
                int succWeight = succPair.weight;
                
                // Checks if successor's total path weight can be reduced 
                // if using currWeight + 1
                if (succWeight > (currWeight + 1)) {
                    succPair.weight = currWeight + 1;
                    pred[users.indexOf(successor)] = current;
                    pq.add(succPair);
                }
            }
        }
        
        // If predecessor of destinationVertex is still null, there is no path to it
        if (pred[users.indexOf(destinationVertex)] != null) {            
            // add nodes from predecessor list to shortestPath list start from destination
            shortestPath.add(destinationVertex.getName());
            int nextPred = users.indexOf(destinationVertex);
            while (pred[nextPred] != null) {
                shortestPath.add(pred[nextPred].getName());
                nextPred = users.indexOf(pred[nextPred]);
            }
          
            // reverse shortestPath
            Collections.reverse(shortestPath);

        }

        return shortestPath; // Will be empty if there is no path
    }
}
