package edu.iastate.cs228.hw4;

import java.util.Arrays;

import java.util.Scanner;
import java.io.FileNotFoundException;
import java.io.File;

/**
 * @author Mitchell Kerr
 *
 *         An entry tree class Add Javadoc comments to each method
 */
@SuppressWarnings("unused")
public class EntryTree<K, V> {
    /**
     * dummy root node made public for grading
     */
    protected Node root; // Represents the root of the node
    protected K tempKey; // Represents the key of the value returned by search()
    protected Node tempNode; // represents the node at the value returned by
    							// search()
    protected boolean prefixNotEqualToKeyarr; // Boolean that is false if search() method
    								// could not search entire entry
    protected Node recursor; // Stores Node obtained by recursive showTree
    							// helper method

    /**
     * prefixlen is the largest index such that the keys in the subarray keyarr
     * from index 0 to index prefixlen - 1 are, respectively, with the nodes on
     * the path from the root to a node. prefixlen is computed by a private
     * method shared by both search() and prefix() to avoid duplication of code.
     */
    protected int prefixlen;

    protected class Node implements EntryNode<K, V> {
	protected Node child; // link to the first child node
	protected Node parent; // link to the parent node
	protected Node prev; // link to the previous sibling
	protected Node next; // link to the next sibling
	protected K key; // the key for this node
	protected V value; // the value at this node

	public Node(K aKey, V aValue) {
	    key = aKey;
	    value = aValue;
	    child = null;
	    parent = null;
	    prev = null;
	    next = null;
	}

	@Override
	public EntryNode<K, V> parent() {
	    if (parent == null) {
		return null;
	    }
	    return this.parent;

	}

	@Override
	public EntryNode<K, V> child() {
	    if (child == null) {

		return null;
	    }
	    return this.child;
	}

	@Override
	public EntryNode<K, V> next() {
	    if (next == null) {
		return null;
	    }

	    return this.next;
	}

	@Override
	public EntryNode<K, V> prev() {
	    if (prev == null) {
		return null;
	    }

	    return this.prev;
	}

	@Override
	public K key() {
	    if (key == null) {
		return null;
	    }

	    return this.key;
	}

	@Override
	public V value() {
	    return this.value;

	}
    }

    public EntryTree() {
	root = new Node(null, null);
    }

    /**
     * Returns the value of the entry with a specified key sequence, or null if
     * this tree contains no entry with the key sequence.
     * 
     * @param keyarr
     * @return temmp.value - The value of the key at the highest index of keyarr
     *         that exists under the root
     */
    public V search(K[] keyarr) {
	if (isNull(keyarr)) { // Checks that the array is not null
	    return null;
	}
	if (throwNullException(keyarr)) { // Checks that the array is not null
										// anywhere
	    throw new NullPointerException();
	}

	prefixNotEqualToKeyarr = false;
	int tracker = 0; // To track how many times the root's children were
			 		// checked
	Node temp = root;
	for (int i = 0; i < keyarr.length; i++) {
	    Node rootChild = temp.child;
	    while (rootChild != null) {
		if (Check(rootChild, keyarr[i])) { // Call to helper method at
						   					// bottom of code, true if
											// both keys equal
		    temp = rootChild;
		    tracker++;
		}
		rootChild = rootChild.next;
	    }
	}

	if (tracker != keyarr.length) {
	    prefixNotEqualToKeyarr = true; // Indicated the entire array does not exist under
			      						// the root
	}
	tempNode = temp; // sets tempNode to current value
	tempKey = temp.key; // sets tempKey to tempNode's key
	return temp.value;
    }

    /**
     * The method returns an array of type K[] with the longest prefix of the
     * key sequence specified in keyarr such that the keys in the prefix label
     * the nodes on the path from the root to a node. The length of the returned
     * array is the length of the longest prefix.
     * 
     * @param keyarr
     * @return toReturn - a key array representing the longest branch of keys
     *         corresponding to keyarr
     */
    public K[] prefix(K[] keyarr) {
	if (isNull(keyarr)) { // Checks that the array is not null
	    return null;
	}
	if (throwNullException(keyarr)) { // Checks that the array is not null
					  					// anywhere
	    throw new NullPointerException();
	}

	K[] toUse = Arrays.copyOf(keyarr, keyarr.length); // Used to analyze
														// array
	Arrays.fill(toUse, null); // Fill with null so to see where keys end
	int index = 0;
	for (int i = 1; i < keyarr.length + 1; i++) { // Check all given keys
													// for prefix
	    K[] copy = Arrays.copyOf(keyarr, i); // Creates copy as long as
						 						// whatever index is being
	    										// checked
	    V value = search(copy); // to update tempKey
	    if (prefixNotEqualToKeyarr) {
		toUse[index] = null;
	    } else {
		toUse[index] = tempKey; // updates toUse , fills null if there
								// is no key there
		index++;
	    }
	}

	int newSize = 0;
	for (int i = 0; i < toUse.length; i++) { // Finds how many non-null keys
											// are in new array
	    if (toUse[i] != null) {
		newSize++;
	    }
	}

	K[] toReturn = Arrays.copyOf(keyarr, newSize); // Create copy of keyarr
													// the same size as how
													// many keys were found
	prefixlen = newSize;
	return toReturn;

    }

    /**
     * The method locates the node P corresponding to the longest prefix of the
     * key sequence specified in keyarr such that the keys in the prefix label
     * the nodes on the path from the root to the node. If the length of the
     * prefix is equal to the length of keyarr, then the method places aValue at
     * the node P and returns true. Otherwise, the method creates a new path of
     * nodes (starting at a node S) labeled by the corresponding suffix for the
     * prefix, connects the prefix path and suffix path together by making the
     * node S a child of the node P, and returns true.
     * 
     * @param keyarr
     * @param aValue
     * @return true if the key array and value were added to the tree or already
     * exist in the tree, false otherwise
     */
    public boolean add(K[] keyarr, V aValue) {
	if (isNull(keyarr) || aValue == null) { // Checks that the array is not
											// null and that aValue has a
											// value
	    return false;
	}
	if (throwNullException(keyarr)) { // Checks that the array is not null
										// anywhere
	    throw new NullPointerException();
	}
	K[] pre = prefix(keyarr); // Finds prefix of argument
	V preVal = search(pre); // Used to update the temp instance variables
	if (pre.length == keyarr.length) { // If entire array already exists,
										// updates the final key's value and
										// returns true
	    tempNode.value = aValue;
	    return true;
	}
	int suffixSize = keyarr.length - pre.length; // size of keys left in
						     					// argument after prefix
	K[] suffix = Arrays.copyOf(keyarr, suffixSize);
	Arrays.fill(suffix, null);
	int index = 0;
	for (int i = keyarr.length - suffixSize; i < keyarr.length; i++) { // Sets
																	// suffix
																	// to
																	// correct
									   								// values
	    suffix[index] = keyarr[i];
	    index++;
	}

	search(pre); // Updates temp instance variables
	Node temp = new Node(null, null);

	if (tempNode.key == null && tempNode.value == null) { // If suffix is
															// entirety of
															// argument,
															// parent node
															// is root
	    temp = root;
	} else { // If part of argument already exists, parent node is highest
		 		// value of prefix
	    temp = tempNode;
	}

	if (pre.length > 0 && suffix.length > 0) { // if there is both a prefix
						   						// and a suffix this means
												// part of the argument
												// array exists but not the
												// whole thing

	    if (tempNode.child == null) { // if suffix will be added to a leaf
					  					// node
		for (int y = 0; y < suffix.length - 1; y++) { // Only loop to
							      						// node before
														// last so as to
							      						// add value to
							      						// last node
		    temp.child = new Node(suffix[y], null);
		    temp.child.child = null;
		    temp.child.parent = temp;
		    temp.child.next = null;
		    temp.child.prev = null;
		    temp = temp.child;
		}
		temp.child = new Node(suffix[suffixSize - 1], aValue); // sets
																// value
																// to
								       							// last
								       							// node
		temp.child.parent = temp;
		temp.child.next = null;
		temp.child.prev = null;
		while (temp.parent != null) { // iterates up to temp's total
					      			// parent
		    temp = temp.parent;
		}
		tempNode = temp;

	    } else { // if suffix will be added to inner node as a next value

		Node aTemp = tempNode.child; // add next value to child of
					     			// highest prefix node
		while (aTemp.next != null) { // Iterate until there are no more
					     			// next nodes
		    aTemp = aTemp.next;
		}
		aTemp.next = new Node(suffix[0], null); // Loop differently this
												// time for some reason
												// idk
		aTemp.next.parent = tempNode;
		aTemp.next.next = null;
		aTemp.next.prev = aTemp;
		aTemp = aTemp.next;

		for (int y = 1; y < suffix.length - 1; y++) { // Loop starting
							      						// at second
							      						// suffix value
		    aTemp.child = new Node(suffix[y], null);
		    aTemp.child.child = null;
		    aTemp.child.parent = aTemp;
		    aTemp.child.next = null;
		    aTemp.child.prev = null;
		    aTemp = aTemp.child;
		}
		if (suffixSize > 1) { // if suffix size is greater than 1, set
				      			// last node to correct value
		    aTemp.child = new Node(suffix[suffixSize - 1], aValue);
		    aTemp.child.parent = aTemp;
		    aTemp.child.next = null;
		    aTemp.child.prev = null;
		} else {
		    aTemp.value = aValue; // if suffix size is 1, set current
					  				// node to correct value
		}

		while (aTemp.parent != null) { // Iterates up to node's total
										// parent
		    aTemp = aTemp.parent;
		}
		tempNode = aTemp;

	    }

	}

	if (pre.length == 0 && suffix.length > 0) { // If argument array does
												// not exist at all under
												// root

	    for (int y = 0; y < suffix.length - 1; y++) { // Set branch under
							  							// root
		temp.child = new Node(suffix[y], null);
		temp.child.child = null;
		temp.child.parent = temp;
		temp.child.next = null;
		temp.child.prev = null;
		temp = temp.child;
	    }

	    temp.child = new Node(suffix[suffixSize - 1], aValue); // Set last
	    														// node to
	    														// correct
	    														// value and
	    														// key
	    temp.child.parent = temp;
	    temp.child.next = null;
	    temp.child.prev = null;
	    while (temp.parent != null) { // iterate up to node's total parent
		temp = temp.parent;
	    }
	    root = temp;

	}

	return true; // If add was successful
    }

    
    
    
    
    /**
     * Removes the entry for a key sequence from this tree and returns its value
     * if it is present. Otherwise, it makes no change to the tree and returns
     * null.
     * 
     * @param keyarr
     * @return theValue - the value at the node of the last index of keyarr
     */
    public V remove(K[] keyarr) {
	if (isNull(keyarr)) { // Checks that the array is not null
	    return null;
	}
	if (throwNullException(keyarr)) { // Checks that the array is not null
					  // anywhere
	    throw new NullPointerException();
	}
	if (prefix(keyarr).length != keyarr.length) { // Returns null if the key
						      						// array argument does
													// not exist under the
													// root
	    return null;
	}

	V theValue = search(keyarr); // updates temp instance variables
	tempNode.value = null; // sets value of last node in keyarr to null

	for (int i = keyarr.length; i > 1; i--) { // iterate as long as keyarr
	    if (tempNode.child == null && tempNode.parent != null && tempNode.next == null) { // Only keep iterating if tempNode's child and next are null
	    																					//and it has a parent
		tempNode = tempNode.parent;
		tempNode.child = null;
	

	    } 
	  
	    
	    
	    else {
	    	  if(tempNode.next != null){	//If the second branch needs to become the first branch
	  	    	Node tempNext = tempNode.next;
	  	    	tempNode = tempNode.parent;
	  	    	tempNode.child = null;
	  	    	tempNode.child = tempNext;
	  	    	tempNode.child.prev = null;
	  	    }
			break;
		    }
	}
	

	return theValue;
    }

    /**
     * The method prints the tree on the console in the output format shown in
     * an example output file.
     */
    public void showTree() {
/*
	Node temp = root;
	String theDents = "      ";	//starting spaces
	String spaces = "  ";	//spaces to add
	System.out.println(root.key + "->" + root.value);
	*/
	/*
	while (temp.child != null) {
	    System.out.println(theDents + temp.child.key + "->" + temp.child.value);
	    temp = temp.child;
	    theDents = theDents + spaces;
	}
	*/
    System.out.println(root.key + "->" + root.value);
    if(root.child != null){
	showTreeRecursor(root.child,"      ");
    }
	
	//showTreeRecursor(temp, "      ", " ");
	
	
    }

    public void showTreeRecursor(Node n, String Dents) {
    	String theDents = Dents;
    	String theOtherDents = Dents;
    	String spaces = "  ";	//spaces to add
    	System.out.println(theDents + n.key + "->" + n.value);
		theDents = theDents+spaces;
		Node temp = n;
		Node tempTwo = new Node(null, null);
    	if(n.next != null){
    		System.out.println(theOtherDents + n.next.key + "->" + n.next.value);
    		theOtherDents = theOtherDents+spaces;
    		tempTwo = n;
    		n = n.next.child;
    		
    		while (n.child != null) {
    		    System.out.println(theOtherDents + temp.child.key + "->" + temp.child.value);
    		    n = n.child;
    		    theOtherDents = theOtherDents + spaces;
    		}
    		
    		if(tempTwo.next.child != null){
    			showTreeRecursor(tempTwo.next.child, theDents);
    		}
    		
    	}
    	if(temp.child != null){
    		showTreeRecursor(temp.child, theDents);
    	}
    	
    	/*
    	String theDents = realBlanks;
    	String spaces = "  ";
    	
	if (n.next == null && n.parent != null) {
		theDents = blanks;
		blanks = blanks + spaces;
	    showTreeRecursor(n.parent , blanks, theDents);
	    
	}
	
	Node helperOne = new Node(null, null);
	int index = 0;
	if(n.next != null){
		n = n.next;
	while (n != null) {
	    System.out.println(theDents + n.key + "->" + n.value);
	    helperOne = n;
	    n = n.child;
		theDents = theDents + spaces;
		index++;
		

	    
	}
	
	for(int i = 0; i<index-1; i++){
		helperOne = helperOne.parent;
	}
	if(helperOne.next != null){
		showTreeRecursor(helperOne, blanks, theDents);
	}
	
	
	}
	*/
	return;
    }

    

    public void showTreeHelper(Node n) {
	Node temp = n;
	String theDents = "      ";
	String spaces = "  ";
	while (temp != null) {
	    System.out.println(theDents + temp.child.key + "->" + temp.child.value);
	    temp = temp.child;
	    theDents = theDents + spaces;
	}

    }

    //Helper method to check that argument has value and length
    public boolean isNull(K[] keyarr) {
	if (keyarr == null || keyarr.length == 0) {
	    return true;
	}
	return false;
    }

    //Helper method to check that argument is non-null everywhere
    public boolean throwNullException(K[] keyarr) {
	for (int i = 0; i < keyarr.length; i++) {
	    if (keyarr[i] == null) {
		return true;
	    }

	}
	return false;

    }

    //Helper method to check if a node's key equals a given key
    public boolean Check(Node n, K key) {
	if (n.key == key) {
	    return true;
	}
	return false;
    }

}
