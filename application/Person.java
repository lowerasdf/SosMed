package application;

/**
 * Filename:        Person.java
 * ATEAM Number:    69
 * Project:         Team Project - Social Network
 * Author(s):       Daniel Khong Ang Toong  
 * 
 * This class models a Person object used as a vertex in the graph.
 * Each Person has a String name and an List<Person> friend list to store
 * all its friends.
 */
public class Person {
    
    private String name; // Name of Person
    private Person[] friendList; // Person array of the Person's friends
    
    // Initial friend list array length, can be resized
    private static final int INITIAL_FRIEND_LIST_LENGTH = 10;
    
    /**
     * Constructs a Person and initializes the name
     */
    public Person(String name) {
        this.name = name;
        this.friendList = new Person[INITIAL_FRIEND_LIST_LENGTH];
    }
    
    /**
     * Getter method for this person's name
     */
    public String getName() {
        return this.name;
    }
    
    /**
     * Setter method for this person's name
     */
    public void setName(String newName) {
        this.name = newName;
    }
    
    /**
     * Getter method for this person's friend list
     */
    public Person[] getFriendList() {
        return this.friendList;
    }
    
    /**
     * Setter method for this person's friend list
     */
    public void setFriendList(Person[] newList) {
        this.friendList = newList;
    }
    
    @Override
    public String toString() {
        return this.name;
    }
}
