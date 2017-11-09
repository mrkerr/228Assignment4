package edu.iastate.cs228.hw4;

import static org.junit.Assert.*;

import java.util.Arrays;


import org.junit.Before;
import org.junit.Test;

/*
 * @author Mitchell Kerr
 */

public class EntryTreeTests {
	
	EntryTree<Character, String> newTree;
	Character[] charArrGood;
	Character[] charArrBad;
	String stringGood;
	String stringBad;
	Character key;
	
	
	/*
	 * Initializes variables
	 */
	@Before 
	public void initialize() {
	    
	newTree = new EntryTree<>();
	charArrGood = new Character[4];
	charArrGood[0] = 'e';
	charArrGood[1] = 'd';
	charArrGood[2] = 'i';
	charArrGood[3] = 't';
	
	charArrBad = new Character[4];
	charArrBad[0] = 'e';
	charArrBad[1] = 'd';
	charArrBad[2] = null;
	charArrBad[3] = 't';
	
	
	stringGood = "value";
	stringBad = null;
	
	key = 'k';
}
	

	/*
	 * Tests NullPointerExceptions in EntryTree methods
	 * Thrown if any part of argument is null
	 */
	
	@Test(expected=NullPointerException.class)
	public void testNullPointerException() {
		newTree.add(charArrBad, stringGood);
	    newTree.search(charArrBad);
	    newTree.remove(charArrBad);
	    newTree.prefix(charArrBad);
	}
	
	
	/*
	 * Tests Node class methods
	 */
	@Test
	public void testNodeMethods(){
		EntryTree<Character,String>.Node node = newTree.new Node(key, stringGood);	//Create new Node
		
		assertEquals(key, node.key());	//Test that key works correctly
		assertEquals(stringGood, node.value());	//Test that value works correctly
		
		//Test that all attributes initialize to null
		assertNull(node.child());
		assertNull(node.parent());
		assertNull(node.next());
		assertNull(node.prev());

		//Test child() method
		EntryTree<Character,String>.Node nodeChild = newTree.new Node('c', "child");
		node.child = nodeChild;
		assertEquals(nodeChild, node.child());
		
		//Test parent() method
		EntryTree<Character,String>.Node nodeParent = newTree.new Node('p', "parent");
		node.parent = nodeParent;
		assertEquals(nodeParent, node.parent());
		
		//Test next() method
		EntryTree<Character,String>.Node nodeNext = newTree.new Node('n', "next");
		node.next = nodeNext;
		assertEquals(nodeNext, node.next());
		
		//Test prev() method
		EntryTree<Character,String>.Node nodePrev = newTree.new Node('r', "prev");
		node.prev = nodePrev;
		assertEquals(nodePrev, node.prev());
		
	}
	
	/*
	 * Tests the add() method
	 */
	@Test
	public void testAddMethod() {
		
		assertTrue(newTree.add(charArrGood, stringGood));	//Returns true if proper arguments
		assertFalse(newTree.add(charArrGood, stringBad));	//Returns false if there are any nulls in arguments

	}
	
	/*
	 * Tests the prefix() method
	 */
	@Test
	public void testPrefixMethod(){
		newTree.add(charArrGood, stringGood);
		Character[] prefixCharArr = new Character[6];
		prefixCharArr[0] = 'e';
		prefixCharArr[1] = 'd';
		prefixCharArr[2] = 'i';
		prefixCharArr[3] = 't';
		prefixCharArr[4] = 'i';
		prefixCharArr[5] = 'o';
		assertTrue(Arrays.equals(charArrGood, newTree.prefix(prefixCharArr)));
		
		//Make sure prefix only search children starting at root
		Character[] prefixCharArrTwo = {'d', 'i', 't'};
		assertFalse(Arrays.equals(charArrGood, newTree.prefix(prefixCharArrTwo)));
		
	}
	
	/*
	 * Tests search() method
	 */
	@Test
	public void testSearchMethod(){
		newTree.add(charArrGood, stringGood);
		assertEquals(stringGood, newTree.search(charArrGood));
	}
	
	/*
	 * Tests remove() method
	 */
	@Test
	public void testRemoveMethod(){
		newTree.add(charArrGood, stringGood);
		assertEquals(stringGood, newTree.remove(charArrGood));
	}
	
	/*
	 * Test that nodes update correctly after various calls to EntryTree methods
	 */
	@Test
	public void addMultipleObjectsAndCallNodeMethods(){
		EntryTree<Character, String> newTreeTwo = new EntryTree<>();
		
		newTreeTwo.add(charArrGood, stringGood); //Mitch you're a dumb fuck
		Character[] forCheckingOne = {'e', 'd', 'l', 'x'};
		String stringOne = "valueOne";
		
		newTreeTwo.add(forCheckingOne, stringOne);
		assertEquals(stringOne, newTreeTwo.root.child.child.child.next.child.value);
		
		Character[] forCheckingTwo = {'e', 'd', 'i', 't', 'i', 'o', 'n'};
		String stringTwo = "valueTwo";
		newTreeTwo.add(forCheckingTwo, stringTwo);
		assertEquals(stringTwo, newTreeTwo.root.child.child.child.child.child.child.child.value);

		
	
		
		Character[] forCheckingThree = {'e', 'd', 'l', 'x', 'o'};
		String stringThree = "valueThree";
		newTreeTwo.add(forCheckingThree, stringThree);
		assertEquals(stringThree, newTreeTwo.root.child.child.child.next.child.child.value);

		
		//Check other methods
		assertEquals(stringTwo, newTreeTwo.remove(forCheckingTwo));
		assertEquals(stringOne, newTreeTwo.search(forCheckingOne));
		Character[] forCheckingFour = {'e', 'd'};
		Character[] forCheckingFive = {'e', 'd', 'z'};
		assertTrue(Arrays.equals(forCheckingFour, newTreeTwo.prefix(forCheckingFive)));
		
		Character[] forCheckingSix = {'e', 'd', 'l', 'p'};
		String stringFour = "valueFour";
		newTreeTwo.add(forCheckingSix, stringFour);
		assertEquals(stringFour, newTreeTwo.root.child.child.child.child.next.value);
		
		//Check that if you remove the first branch, the next branch becomes the first branch
		EntryTree<Character, String> newTreeThree = new EntryTree<>();
		Character[] forCheckingSeven = {'e', 'd', 'i', 't', 'i', 'o', 'n'};
		String stringFive = "valueFive";
		Character[] forCheckingEight = {'e', 'd', 'i', 't', 'o', 'r'};
		String stringSix = "valueSix";
		newTreeThree.add(forCheckingSeven, stringFive);
		newTreeThree.add(forCheckingEight, stringSix);
		newTreeThree.remove(forCheckingSeven);
		assertEquals(stringSix, newTreeThree.root.child.child.child.child.child.child.value);
		
		
	}

}
