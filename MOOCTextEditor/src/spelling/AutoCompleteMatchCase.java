package spelling;

import java.util.List;
import java.util.Queue;
import java.util.Arrays;
import java.util.Set;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;

/**
 * An trie data structure that implements the Dictionary and the AutoComplete ADT
 * @author Volfegan [DanieL L Lacerda]
 *
 */
public class AutoCompleteMatchCase implements  Dictionary, AutoComplete {

    private TrieNode root;
    private int size;


    public AutoCompleteMatchCase() {
		root = new TrieNode();
	}


	/** Insert a word into the trie.
	 * This method will check if the string fits in one of three categories:
	 * string is all lower case.
	 * string has the 1st letter upper case.
	 * string is all upper case.
	 *
	 * This method adds a word by creating and linking the necessary trie nodes
	 * into the trie. It should appropriately use existing nodes in the trie,
	 * only creating new nodes when necessary.
	 * E.g. If the word "no" is already in the trie,
	 * then adding the word "now" would add only one additional node
	 * (for the 'w').
	 * With the new rules, a word like 'hELlo' will be added as lower case hello,
	 * but Hello, will be added as 1st node H and the rest of nodes lower case.
	 * Adding HELLO will be added as all nodes uppercase.
	 *
	 * @return true if the word was successfully added or false if it already exists
	 * in the dictionary.
	 */
	@Override
	public boolean addWord(String word) {
		if(word.equals("") || word.equals(null) ) return false;

		String word_case_match = checkLetterCase_of(word); //"all_uppercase" | "all_lowercase" | "1st_letter_uppercase"

		TrieNode currNode = root;

		for (Character c : word.toUpperCase().toCharArray()) {
			if (word_case_match.equals("all_uppercase")) c = Character.toUpperCase(c);
			if (word_case_match.equals("all_lowercase")) c = Character.toLowerCase(c);
			if (word_case_match.equals("1st_letter_uppercase")) word_case_match = "all_lowercase";
			//above will make the 1st letter be uppercase, but after it will be converted to lowercase

			//check whether the node have the letter, and if not, insert it
			TrieNode childNode = currNode.getChild(c);
			if (childNode != null) {
				currNode = childNode;
			} else {
				currNode = currNode.insert(c);
			}
		}
		//after the for loop is completed check if the trieNode (currNode = End node) already contains the word
		if (currNode.endsWord()) return false;
		//otherwise, put the word on it
		currNode.setEndsWord(true);
		size++;
		//System.out.println(currNode.getText());
		return true;
	}

	/**
	 * Return the number of words in the dictionary. This is NOT necessarily the same
	 * as the number of TrieNodes in the trie.
	 */
	public int size() {
		return size;
	}


	/** Returns whether the string is a word in the trie, using the algorithm
	 * described in the videos for this week. */
	@Override
	public boolean isWord(String s) {
		//we need to construct and format the word to one of the three possible formats
		String word_case_match = checkLetterCase_of(s); //"all_uppercase" | "all_lowercase" | "1st_letter_uppercase"
		StringBuilder word = new StringBuilder(); // it will be compared at the end

		TrieNode currNode = root;

		for (Character c : s.toCharArray()) {
			if (word_case_match.equals("all_uppercase")) c = Character.toUpperCase(c);
			if (word_case_match.equals("all_lowercase")) c = Character.toLowerCase(c);
			if (word_case_match.equals("1st_letter_uppercase")) word_case_match = "all_lowercase";
			//above will make the 1st letter be uppercase, but after it will be converted to lowercase
			word.append(c.toString()); //construct the word as per format

			//check whether the node have the letters of the searched word
			TrieNode childNode  = currNode.getChild(c);
			if (childNode  != null) {
				currNode = childNode;
			} else {
				// reached the end of the tree
				return false;
			}
		}
		//after the for loop is completed check if the trieNode (currNode = End node) contains the word or not
		//System.out.println(currNode.getText());
		return currNode.endsWord() && currNode.getText().equals(word.toString());
	}

	/**
     * Return a list, in order of increasing (non-decreasing) word length,
     * containing the numCompletions shortest legal completions
     * of the prefix string. All legal completions must be valid words in the
     * dictionary. If the prefix itself is a valid word, it is included
     * in the list of returned words.
     *
     * The list of completions must contain
     * all of the shortest completions, but when there are ties, it may break
     * them in any order. For example, if there the prefix string is "ste" and
     * only the words "step", "stem", "stew", "steer" and "steep" are in the
     * dictionary, when the user asks for 4 completions, the list must include
     * "step", "stem" and "stew", but may include either the word
     * "steer" or "steep".
     *
     * If this string prefix is not in the trie, it returns an empty list.
     *
     * @param prefix The text to use at the word stem
     * @param numCompletions The maximum number of predictions desired.
     * @return A list containing the up to numCompletions best predictions (case match)
     */@Override
     public List<String> predictCompletions(String prefix, int numCompletions) {
		//we need to format the prefix word to one of the three possible formats
		String word_case_match = checkLetterCase_of(prefix); //"all_uppercase" | "all_lowercase" | "1st_letter_uppercase"

		// Create a list of completions to return (initially empty)
		List<String> completions = new LinkedList<>();
		// 1. Find the stem in the trie.  If the stem does not appear in the trie, return an empty list
		TrieNode stemNode = root;
		for(char c : prefix.toCharArray()) {
			if (word_case_match.equals("all_uppercase")) c = Character.toUpperCase(c);
			if (word_case_match.equals("all_lowercase")) c = Character.toLowerCase(c);
			if (word_case_match.equals("1st_letter_uppercase")) word_case_match = "all_lowercase";
			//above will make the 1st letter be uppercase, but after it will be converted to lowercase

			TrieNode childNode = stemNode.getChild(c);
			if (childNode == null) {
				return completions; //still empty
			}
			stemNode = childNode;
		}
		// 2. Once the stem is found, perform a breadth first search (level order traversal) to generate completions
		//    Create a queue (LinkedList) and add the node that completes the stem to the end of the list.
		Queue<TrieNode> queue = new LinkedList<>();
		queue.add(stemNode);

		//    While the queue is not empty and you don't have enough completions:
		while ((!queue.isEmpty()) && (completions.size() < numCompletions)) {
			//       remove the first Node from the queue
			TrieNode node = queue.remove();
			//       If it is a word, add it to the completions list
			if (node.endsWord()) {
				completions.add(node.getText());
			}
			//       Add all of its child nodes to the end of the queue
			for (Character c : node.getValidNextCharacters()) {
				queue.add(node.getChild(c));
			}
		}
		// Return the list of completions
         return completions;
     }

 	// For debugging
 	public void printTree() {
 		printNode(root);
 	}

 	/** Do a pre-order traversal from this node down */
 	public void printNode(TrieNode curr) {
 		if (curr == null)
 			return;

 		System.out.println(curr.getText());

 		TrieNode next = null;
 		for (Character c : curr.getValidNextCharacters()) {
 			next = curr.getChild(c);
 			printNode(next);
 		}
 	}


	/** Returns the string "all_uppercase", "all_lowercase", "1st_letter_uppercase" after analysing the word
	 * HELLO = all_uppercase; hello = all_lowercase; Hello = 1st_letter_uppercase; hELlo = all_lowercase; HElLo = 1st_letter_uppercase
	 * */
	public static String checkLetterCase_of(String word){
 		//letterCaseMode array will have 0 (lower case) or 1 (upper case) for each letter of the word
		//Ex.: word = [0,0,0,0]; WORD = [1,1,1,1]; Word = [1,0,0,0]
 		int[] letterCaseMode = new int[word.length()];

		for(int i = 0; i < word.length(); i++) {
			char letter =  word.charAt(i);
			//construct letterCaseMode
			if (Character.isLowerCase(letter)) letterCaseMode[i] = 0;
			else letterCaseMode[i] = 1;

			//if the 1st letter is lower case, the word is automatically considered all lowercase
			if (i == 0 && (Character.isLowerCase(letter))) return "all_lowercase";
		}

		for (int i=0; i< word.length(); i++) {
			//any lowercase letter after the 1st uppercase
			if (letterCaseMode[i] == 0) return "1st_letter_uppercase";
		}
		return "all_uppercase";
	}

	// debugging tests (uncomment System.out.println on functions for more info)
	public static void main(String[] args) {
		AutoCompleteMatchCase dict = new AutoCompleteMatchCase();

		System.out.println("AI " + checkLetterCase_of("AI"));
		System.out.println("air " + checkLetterCase_of("air"));
		System.out.println("hELlo " + checkLetterCase_of("hELlo"));
		System.out.println("Hello " + checkLetterCase_of("Hello"));
		System.out.println("HElLo " + checkLetterCase_of("HElLo"));

 		dict.addWord("AI");
 		dict.addWord("air");
 		dict.addWord("hELlo");
		dict.addWord("HElLo");

 		dict.printTree();

 		System.out.println("Size:" + dict.size());
		System.out.println("in the dictionary: AI, air, hELlo");
 		System.out.println();
 		System.out.println("Test # air is in the dictionary: " + dict.isWord("air"));
 		System.out.println("Test # AIR is in the dictionary: " + dict.isWord("AIR"));
 		System.out.println("Test # Air is in the dictionary: " + dict.isWord("Air"));
		System.out.println("Test # hELlo is in the dictionary: " + dict.isWord("hELlo") + " (it will be spelled as hello)");
		System.out.println("Test # hello is in the dictionary: " + dict.isWord("hello"));
		System.out.println("Test # Hello is in the dictionary: " + dict.isWord("Hello"));
		System.out.println("Test # HELLO is in the dictionary: " + dict.isWord("HELLO"));
		System.out.println();


		AutoCompleteMatchCase smallDict = new AutoCompleteMatchCase();
		List<String> completions;
		smallDict.addWord("Hello");
		smallDict.addWord("HElLo");
		smallDict.addWord("help");
		smallDict.addWord("he");
		smallDict.addWord("hem");
		smallDict.addWord("hot");
		smallDict.addWord("hey");
		smallDict.addWord("Heck");
		smallDict.addWord("HER");
		smallDict.addWord("subsequent");
		System.out.println("in the smallDict: Hello, HElLo, help, he, hem, hot, hey, Heck, HER, subsequent");
		completions = smallDict.predictCompletions("he", 10);
		System.out.println("Test # predictCompletions(\"he\", 10): " +  Arrays.toString(completions.toArray()) );
		completions = smallDict.predictCompletions("hel", 10);
		System.out.println("Test # predictCompletions(\"hel\", 10): " +  Arrays.toString(completions.toArray()) );
		completions = smallDict.predictCompletions("He", 10);
		System.out.println("Test # predictCompletions(\"He\", 10): " +  Arrays.toString(completions.toArray()) );
	}
}