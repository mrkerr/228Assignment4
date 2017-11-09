package edu.iastate.cs228.hw4;

import java.util.ArrayList;

import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.StringTokenizer;
import java.io.FileNotFoundException;
import java.io.File;

/**
 * @author Mitchell Kerr
 * 
 *         An application class
 */
@SuppressWarnings("unused")
public class Dictionary {
	
	
	public static void main(String[] args){
		EntryTree<Character, String> newTree = new EntryTree<>();
		// TODO
			try{
			Scanner sc = new Scanner(new File ("C:/Users/Mitch/Desktop/228 Supplemental Notes/Project4RealInputs.txt"));	//creates new scanner for the indicated file. Needs to be args[0]
			List<String> lines = new ArrayList<String>();		//Creates new string array list
			while (sc.hasNextLine()) {
			  lines.add(sc.nextLine());			//adds every line from file to new array list
			}
			sc.close();

			String[] arr = lines.toArray(new String[0]);		//Converts the array list to an array
			
			
			int index = 0;
			for(int k = 0; k < arr.length; k++){
				String[] splitPre = arr[k].split(" ");	//Used to find what command is
				
			if(splitPre[0].equals("add")){	
			
			
			StringTokenizer st = new StringTokenizer(arr[index]);	//Removes all whitespace from string
			st.nextToken();
			String s = st.nextToken();
			Character[] charArr = new Character[s.length()];
			
			
			
			for(int j = 0; j<s.length(); j++){	//Turns string into Character array
				charArr[j] = s.charAt(j);
				
			}
				String ss = st.nextToken();
				boolean isAdded = newTree.add(charArr, ss);	//Call to add method
				System.out.println("Command: add " + s + " " + ss);
				System.out.println("Result from an add: " + isAdded);
				System.out.print("\n");
				
			
		
			
			}
			if(splitPre[0].equals("search")|| splitPre[0].equals("prefix")|| splitPre[0].equals("remove")){	//All of these command lines will only contain 
																											//two words
				
					StringTokenizer st = new StringTokenizer(arr[k]);	//Removes all whitespace
					st.nextToken();
					String s = st.nextToken();
					Character[] charArr = new Character[s.length()];
					
					
					
					for(int j = 0; j<s.length(); j++){	//Creates Character array 
						charArr[j] = s.charAt(j);
						
					}
					if(splitPre[0].equals("search")){	
						String searchedValue = newTree.search(charArr);	//Call to search method
						System.out.println("Command: search " + s);
						System.out.println("Result from a search: " + searchedValue);
						System.out.print("\n");
					}
					if(splitPre[0].equals("prefix")){
						Character[] prefix = newTree.prefix(charArr);	//Call to prefix method
						String toPrint = "";
						for(int p = 0; p<prefix.length; p++){	//Prints string
							toPrint = toPrint + prefix[p];
						}
						System.out.println("Command: prefix " + s);
						System.out.println("Result from a prefix: " + toPrint);
						System.out.print("\n");
					}
					if(splitPre[0].equals("remove")){
						String removed = newTree.remove(charArr);	//Call to remove method
						System.out.println("Command: remove " + s);
						System.out.println("Result from a remove: " + removed);
						System.out.print("\n");
					}
					
					
			
			

	}
			if(splitPre[0].equals("showTree")){
				System.out.println("Command: showTree");
				System.out.print("\n");
				System.out.println("Result from a showTree:");
				newTree.showTree();	//Call to showTree method
				System.out.print("\n");
				}
			index++;
}
}
			catch(FileNotFoundException e){	//Catches file not found exception
				System.out.println("FileNotFoundException");
			}
			
	}
}
	
