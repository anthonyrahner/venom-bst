package venom;

import java.util.ArrayList;

/**
 * The Venom class represents a binary search tree of SymbioteHost objects.
 * Venom is a sentient alien symbiote with a liquid-like form that requires a
 * host to bond with for its survival. The host is granted superhuman abilities
 * and the symbiote gains a degree of autonomy. The Venom class contains methods
 * that will help venom find the most compatible host. You are Venom.
 * 
 * @author Ayla Muminovic
 * @author Shane Haughton
 * @author Elian D. Deogracia-Brito
 */
public class Venom {
    private SymbioteHost root;

    /**
     * DO NOT EDIT THIS METHOD
     * 
     * Default constructor.
     */
    public Venom() {
        root = null;
    }

    /**
     * This method is provided to you
     * Creates an array of SymbioteHost objects from a file. The file should
     * contain the number of people on the first line, followed by the name,
     * compatibility, stability, and whether they have antibodies on each
     * subsequent line.
     * 
     * @param filename the name of the file
     * @return an array of SymbioteHosts (should not contain children)
     */
    public SymbioteHost[] createSymbioteHosts(String filename) {
        // DO NOT EDIT THIS METHOD
        StdIn.setFile(filename);
        int numOfPeople = StdIn.readInt();
        SymbioteHost[] people = new SymbioteHost[numOfPeople];
        for (int i = 0; i < numOfPeople; i++) {
            StdIn.readLine();
            String name = StdIn.readLine();
            int compatibility = StdIn.readInt();
            int stability = StdIn.readInt();
            boolean hasAntibodies = StdIn.readBoolean();
            SymbioteHost newPerson = new SymbioteHost(name, compatibility, stability, hasAntibodies, null, null);
            people[i] = newPerson;
        }
        return people;
    }

    /**
     * Inserts a SymbioteHost object into the binary search tree.
     * 
     * @param symbioteHost the SymbioteHost object to insert
     */
    public void insertSymbioteHost(SymbioteHost symbioteHost) {
        // WRITE YOUR CODE HERE
        root = insert(root, symbioteHost);
        
    }

    private SymbioteHost insert(SymbioteHost root, SymbioteHost newHost) {
        if (root == null) { //base case
            return newHost; 
        }

        int cmp = newHost.getName().compareTo(root.getName()); //compares new host name to root name
        if (cmp < 0) root.setLeft(insert(root.getLeft(), newHost)); //less than, goes left
        else if (cmp > 0) root.setRight(insert(root.getRight(), newHost)); //greater than, goes right
        else { //duplicate name, update with new information
            root.setSymbioteCompatibility(newHost.getSymbioteCompatibility());
            root.setMentalStability(newHost.getMentalStability());
            root.setHasAntibodies(newHost.hasAntibodies());
        }

        return root;

    }
    /**
     * Builds a binary search tree from an array of SymbioteHost objects.
     * 
     * @param filename filename to read
     */
    public void buildTree(String filename) {
        // WRITE YOUR CODE HERE
        SymbioteHost[] people = createSymbioteHosts(filename);
        for (int i = 0; i < people.length; i++) {
            insertSymbioteHost(people[i]);
        }
        
    }

    /**
     * Finds the most compatible host in the tree. The most compatible host
     * is the one with the highest suitability.
     * PREorder traversal is used to traverse the tree. The host with the highest suitability
     * is returned. If the tree is empty, null is returned.
     * 
     * USE the calculateSuitability method on a SymbioteHost instance to get
     * a host's suitability.
     * 
     * @return the most compatible SymbioteHost object
     */
    public SymbioteHost findMostSuitable() {
        int[] highestScore = {0}; //track highest score in array so it updates across each call
        return findMostSuitable(root, null, highestScore);
    }

    private SymbioteHost findMostSuitable(SymbioteHost current, SymbioteHost highestHost, int[] highestScore) {
        if (current == null) return highestHost; //base case

        int currentScore = current.calculateSuitability(); //find suitability of current host
        if (currentScore > highestScore[0]) { //if higher than previously highest score
            highestHost = current; //update both the host object and the tracker int
            highestScore[0] = currentScore;
        }

        //preorder traversal
        highestHost = findMostSuitable(current.getLeft(), highestHost, highestScore);
        highestHost = findMostSuitable(current.getRight(), highestHost, highestScore);

        return highestHost;
    }

    /**
     * Finds all hosts in the tree that have antibodies. INorder traversal is used to
     * traverse the tree. The hosts that have antibodies are added to an
     * ArrayList. If the tree is empty, null is returned.
     * 
     * @return an ArrayList of SymbioteHost objects that have antibodies
     */
    public ArrayList<SymbioteHost> findHostsWithAntibodies() {
        ArrayList<SymbioteHost> hostsWithAntibodies = new ArrayList<>(); //declare array list
        findHostsWithAntibodies(root, hostsWithAntibodies);
        return hostsWithAntibodies;
    }

    private void findHostsWithAntibodies(SymbioteHost current, ArrayList<SymbioteHost> hostsWithAntibodies) {
        if (current == null) return; //base case
        
        //inorder traversal
        findHostsWithAntibodies(current.getLeft(), hostsWithAntibodies);

        if (current.hasAntibodies()) {
            hostsWithAntibodies.add(current);
        }

        findHostsWithAntibodies(current.getRight(), hostsWithAntibodies);
    }

    /**
     * Finds all hosts in the tree that have a suitability between the given
     * range. The range is inclusive. Level order traversal is used to traverse the tree. The
     * hosts that fall within the range are added to an ArrayList. If the tree
     * is empty, null is returned.
     * 
     * @param minSuitability the minimum suitability
     * @param maxSuitability the maximum suitability
     * @return an ArrayList of SymbioteHost objects that fall within the range
     */
    public ArrayList<SymbioteHost> findHostsWithinSuitabilityRange(int minSuitability, int maxSuitability) {
        ArrayList<SymbioteHost> undesiredRange = new ArrayList<>();
        Queue<SymbioteHost> queue = new Queue<>(); //initialize queue

        queue.enqueue(root); //add BST's root to queue

        //level order traversal
        while(!queue.isEmpty()) {
            SymbioteHost current = queue.dequeue(); //dequeue a node
            //check if host is in range
            int currentScore = current.calculateSuitability();
            if (currentScore >= minSuitability && currentScore <= maxSuitability) {
                undesiredRange.add(current);
            }

            //enqueue children of current
            if (current.getLeft() != null) {
                queue.enqueue(current.getLeft());
            }
            if (current.getRight() != null) {
                queue.enqueue(current.getRight());
            }

        }
        
        
        return undesiredRange;
    }

    /**
     * Deletes a node from the binary search tree with the given name.
     * If the node is not found, nothing happens.
     * 
     * @param name the name of the SymbioteHost object to delete
     */
    public void deleteSymbioteHost(String name) {
        root = deleteSymbioteHost(root, name);
    }

    private SymbioteHost deleteSymbioteHost(SymbioteHost current, String name){
        if (current == null) return null; //base case

        int cmp = name.compareTo(current.getName()); //search through tree to find node
        if (cmp < 0) {
            current.setLeft(deleteSymbioteHost(current.getLeft(), name));
        }
        else if (cmp > 0) {
            current.setRight(deleteSymbioteHost(current.getRight(), name));
        }
        else { //node is found, implement cases of hibbard deletion
            //node has no children:
            if (current.getLeft() == null && current.getRight() == null) {
                return null;
            }

            //node has one child:
            if (current.getLeft() == null) {
                return current.getRight();
            }
            if (current.getRight() == null) {
                return current.getLeft();
            }

            //node has 2 children:
            //call helper method to find inorder successor, starts at right subtree of current
            SymbioteHost successor = inOrderSuccessor(current.getRight());
            current.setRight(deleteSymbioteHost(current.getRight(), successor.getName()));

        }

        return current;
    }

    private SymbioteHost inOrderSuccessor(SymbioteHost current) { //helper for deletion
        while (current.getLeft() != null) {
            current = current.getLeft(); //leftmost node in right subtree
        }
        return current;
    }

    /**
     * Challenge - worth zero points
     *
     * Heroes have arrived to defeat you! You must clean up the tree to
     * optimize your chances of survival. You must remove hosts with a
     * suitability between 0 and 100 and hosts that have antibodies.
     * 
     * Cleans up the tree by removing nodes with a suitability of 0 to 100
     * and nodes that have antibodies (IN THAT ORDER).
     */
    public void cleanupTree() {
        // WRITE YOUR CODE HERE
    }

    /**
     * Gets the root of the tree.
     * 
     * @return the root of the tree
     */
    public SymbioteHost getRoot() {
        return root;
    }

    /**
     * Prints out the tree.
     */
    public void printTree() {
        if (root == null) {
            return;
        }

        // Modify no. of '\t' based on depth of node
        printTree(root, 0, false, false);
    }

    private void printTree(SymbioteHost root, int depth, boolean isRight, boolean isLeft) {
        System.out.print("\t".repeat(depth));

        if (isRight) {
            System.out.print("|-R- ");
        } else if (isLeft) {
            System.out.print("|-L- ");
        } else {
            System.out.print("+--- ");
        }

        if (root == null) {
            System.out.println("null");
            return;
        }

        System.out.println(root);

        if (root.getLeft() == null && root.getRight() == null) {
            return;
        }

        printTree(root.getLeft(), depth + 1, false, true);
        printTree(root.getRight(), depth + 1, true, false);
    }
}
