package application;

import java.util.ArrayList;
import java.util.List;

/**
 * Filename:        Graph.java
 * ATEAM Number:    69
 * Project:         Team Project - Social Network
 * 
 * This class contains the implementation for an undirected and unweighted 
 * graph of Person objects. The graph represents the social network of all
 * the Person objects.
 */
public class Graph implements GraphADT <String, Person> {
    
    private Person[] userList; // Person array of the graph's vertices (users)
    private int size; // number of edges in the graph
    private int order; // number of vertices in the graph
    
    // Initial user list array length, can be resized
    private static final int INITIAL_USER_LIST_LENGTH = 100;
    
    /**
     * Constructs a new empty graph
     */
    public Graph() {
        this.userList = new Person[INITIAL_USER_LIST_LENGTH];
        this.size = 0;
        this.order = 0;
    }
    
    @Override
    public void addVertex(String key) throws DuplicateKeyException {
        // Pre-condition: Key cannot be null. Input validation in GUI.
        
        int index; // To store index to insert Person in userList if no duplicates
                   // are found
        
        // Iterates through userList
        for (index = 0; index < this.userList.length; ++index) {
            // If unused portion of userList is reached
            if (this.userList[index] == null) {
                break;
            }
            
            // Duplicate found, throw Exception
            if (this.userList[index].getName().equals(key)) {
                throw new DuplicateKeyException();
            }
        }
        
        // Adds a Person with name key at userList[index]
        this.userList = add(this.userList, new Person(key), index);
        
        this.order++; // Order is increased if user added successfully
    }

    @Override
    public void removeVertex(String key) throws VertexNotFoundException {
        // Pre-condition: Key cannot be null. Input validation in GUI.
        
        int edgeDeleteCount = 0; // to keep track of number of deleted edges
        
        int index = -1; // remain as -1 of user not found
        
        // Iterates through userList to search for user
        for (int i = 0; i < this.userList.length; ++i) {
            Person user = userList[i];
            
            // If unused portion of userList is reached
            if (user == null) {
                break;
            }
            
            // If a match is found
            if (user.getName().equals(key)) {
                index = i; // index updated to index of matching user
                continue; // to delete edges
            }
            
            // Delete edges to user to be removed, if there are any, for all users
            // Friend list of current vertex
            Person[] friends = user.getFriendList();
            
            // Search for edges to user to be removed
            for (int j = 0; j < friends.length; ++j) {
                // If unused portion of friend list is reached
                if (friends[j] == null) {
                    break;
                }
                
                // If an edge is found, increments edgeDeleteCount
                if (friends[j].getName().equals(key)) {
                    user.setFriendList(remove(friends, j));
                    ++edgeDeleteCount;
                    break;
                }
            }
        }
        
        // If user with matching name is not found, throw exception
        // Delete user at index along with its friend list if found
        if (index == -1) {
            throw new VertexNotFoundException(key);
        } else {            
            this.userList = remove(this.userList, index);
            this.order--;
            this.size -= edgeDeleteCount;
        }
        
    }

    @Override
    public void addEdge(String key1, String key2)
            throws DuplicateEdgeException {
        // Pre-condition: Keys cannot be null. Input validation in GUI.
        
        // Makes sure user1 (user with key1) exists
        int index1; // Stores index of user1 if it exists or index to 
                    // add the user at if it does not
        
        boolean found = false; // to keep track of whether user1 exists
        
        // Searches for user1
        for (index1 = 0; index1 < this.userList.length; ++index1) {
            // Unused portion of userList reached
            if (this.userList[index1] == null) {
                break;
            }
            
            // user1 found
            if (this.userList[index1].getName().equals(key1)) {
                found = true;
                break;
            }
        }
        
        // Add user1 to graph if not found
        if (!found) {
            this.userList = add(this.userList, new Person(key1), index1);
            this.order++;
        }
        
        Person user1 = userList[index1]; // reference to user1's vertex
        
        // Makes sure user2 (user with key2) exists       
        int index2; // Stores index of user2 if it exists or index to add user2
                    // at if it does not
        
        found = false; // to keep track of whether user2 exists
        
        // Searches for user2
        for (index2 = 0; index2 < this.userList.length; ++index2) {
            // Unused portion of userList reached
            if (this.userList[index2] == null) {
                break;
            }
            
            // user2 found
            if (this.userList[index2].getName().equals(key2)) {
                found = true;
                break;
            }
        }
        
        // Add user2 to graph if not found
        if (!found) {
            this.userList = add(this.userList, new Person(key2), index2);
            this.order++;
        }
        
        Person user2 = userList[index2]; // reference to user2's vertex
        
        // To see if an edge between user1 to user2 already exists and add it
        // if it doesn't
        Person[] user1Friends = user1.getFriendList();
        
        int user1Index; // index to add edge to in user1's friend list 
                        // if it doesn't already exist
        boolean edgeFound = false; // to keep track of whether an edge is found
        
        // Searches for edge
        // If an edge exists between user1 and user2, both users' friend lists 
        // will contain the other user. If no edge exists, both users' friend
        // list will not have the other user.
        for (user1Index = 0; user1Index < user1Friends.length; ++user1Index) {
            // Unused portion of friend list reached
            if (user1Friends[user1Index] == null) {
                break;
            }
            
            // If edge is found
            if (user1Friends[user1Index].getName().equals(key2)) {
                edgeFound = true;
                break;
            }
        }
        
        // Adds edge if it doesn't already exist
        if (!edgeFound) {
            // Reference to user2's friend list
            Person[] user2Friends = user2.getFriendList();
            
            int user2Index; // index to add edge to in user2's friend list
            
            for (user2Index = 0; user2Index < user2Friends.length; ++user2Index) {
                // Unused portion of friend list reached
                if (user2Friends[user2Index] == null) {
                    break;
                }
            }
            
            user1.setFriendList(add(user1Friends, user2, user1Index));
            user2.setFriendList(add(user2Friends, user1, user2Index));
            this.size++;
            
        } else {
            throw new DuplicateEdgeException();
        }
    }

    @Override
    public void removeEdge(String key1, String key2)
            throws VertexNotFoundException, EdgeNotFoundException {
        // Pre-condition: Keys cannot be null. Input validation in GUI.
        
        int index1 = -1; // Remains as -1 if user1 is not found
        int index2 = -1; // Remains as -1 if user2 is not found
        
        // Iterates through userList to search for both vertices
        for (int i = 0; i < this.userList.length; ++i) {
            // Unused portion of userList reached
            if (this.userList[i] == null) {
                break;
            }
            
            // If user1 found
            if (this.userList[i].getName().equals(key1)) {
                index1 = i;
                
                // Stop searching if user2 is also found
                if (index2 != -1) {
                    break;
                }
            
            // If user2 found
            } else if (this.userList[i].getName().equals(key2)) {
                index2 = i;
                
                // Stop searching if user1 is also found
                if (index1 != -1) {
                    break;
                }
            }
        }
        
        // If either user does not exist, throw an appropriate VertexNotFoundException
        if (index1 == -1) {
            // If both users don't exist
            if (index2 == -1) {
                throw new VertexNotFoundException();
            }
            
            // If only user1 does not exist
            throw new VertexNotFoundException(key1);
            
        } else if (index2 == -1) {
            // If only user2 does not exist
            throw new VertexNotFoundException(key2);
            
        } else {
            // If both users exist, check for existence of edge between them
            int edge1Index = -1; // Remains as -1 if edge is not found
            
            // Friend list of user1
            Person[] user1Friends = this.userList[index1].getFriendList();
            
            // Searches user1's friend list for edge to user2
            // If it exists, there must also be an edge from user2 to user1
            for (int i = 0; i < user1Friends.length; ++i) {
                // Unused portion of friend list reached
                if (user1Friends[i] == null) {
                    break;
                }
                
                // If edge found
                if (user1Friends[i].getName().equals(key2)) {
                    edge1Index = i;
                    break;
                }
            }
            
            // If the edge exists, remove it from both users' friend lists
            if (edge1Index != -1) {
                int edge2Index; // To store index to remove from in user2's
                                // friend list
                
                Person[] user2Friends = this.userList[index2].getFriendList();
                
                // Searches user2's friend list for index of edge to user1
                for (edge2Index = 0; edge2Index < user2Friends.length; ++edge2Index) {   
                    // Unused portion of friend list reached
                    if (user2Friends[edge2Index] == null) {
                        break;
                    }
                    
                    // If edge found
                    if (user2Friends[edge2Index].getName().equals(key2)) {
                        break;
                    }
                }
                
                this.userList[index1].setFriendList(remove(user1Friends, edge1Index));
                this.userList[index2].setFriendList(remove(user2Friends, edge2Index));
                this.size--; // decrease size
            
            // If no edge exist between user1 and user2
            } else {
                throw new EdgeNotFoundException();
            }
        }        
    }

    @Override
    public void clearGraph() {
        // Same code as the constructor of this class
        this.userList = new Person[INITIAL_USER_LIST_LENGTH];
        this.size = 0;
        this.order = 0;
    }

    @Override
    public Person getVertex(String key) throws VertexNotFoundException {
        // Pre-condition: Key cannot be null. Input validation in GUI.
        
        int userIndex = -1; // Remains as -1 if user is not in graph
        
        // Searches userList for user
        for (int i = 0; i < this.userList.length; ++i) {
            // If unused portion of userList is reached
            if (this.userList[i] == null) {
                break;
            }
            
            // If user found
            if (this.userList[i].getName().equals(key)) {
                userIndex = i;
                break;
            }
        }
        
        // If user exists
        if (userIndex != -1) {
            return this.userList[userIndex];
        }
        
        throw new VertexNotFoundException(key); // if user does not exist
    }

    @Override
    public List<Person> getAllVertices() {
        List<Person> users = new ArrayList<Person>();
        
        for (int i = 0; i < this.order; ++i) {
            users.add(this.userList[i]);
        }
        
        return users;
    }

    @Override
    public int size() {
        return this.size;
    }

    @Override
    public int order() {
        return this.order;
    }
    
    /**
     * Private helper method to add a Person into an array (either
     * userList or a Person's friendList)
     * 
     * @param array Array to add Person to
     * @param vertex Person to be added
     * @param index Index to add Person at
     * @return The modified array
     */
    private Person[] add(Person[] array, Person vertex, int index) {
        // Calls the resize() helper method to expand array if array is full
        if (index >= array.length) {
            array = resize(array);
        }
        
        array[index] = vertex;
        
        return array;
    }
    
    /**
     * Private helper method to resize a full array
     * 
     * @param array Array to be resized
     * @return The resized array
     */
    private Person[] resize(Person[] array) {
        Person[] newArray = new Person[array.length*2];
        
        // Copy elements into new array
        for (int i = 0; i < array.length; ++i) {
            newArray[i] = array[i];
        }
        
        return newArray;
    }
    
    /**
     * Private helper method to remove a Person from an array (either
     * userList or a Person's friendList)
     * 
     * @param array Array to remove Person from
     * @param index Index to remove from
     * @return The modified array
     */
    private Person[] remove(Person[] array, int index) {
        // Shifts all the array elements to the right of index left
        for (int i = index; i < array.length; ++i) {
            // If end of array is reached, simply set it to null
            if (i == (array.length - 1)) {
                array[i] = null;
                
            } else {
                // Unused portion of array reached
                if (array[i] == null) {
                    break;
                }
                
                // Left shift
                array[i] = array[i+1];
            }
        }
        
        return array;
    }
}
