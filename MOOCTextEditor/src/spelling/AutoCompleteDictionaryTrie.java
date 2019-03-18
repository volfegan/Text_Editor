package spelling;

import java.util.List;
import java.util.Queue;
import java.util.Set;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;

/** 
 * An trie data structure that implements the Dictionary and the AutoComplete ADT
 * @author Volfegan [DanieL L Lacerda]
 *
 */
public class AutoCompleteDictionaryTrie implements  Dictionary, AutoComplete {

    private TrieNode root;
    private int size;
    

    public AutoCompleteDictionaryTrie() {
		root = new TrieNode();
	}
	
	
	/** Insert a word into the trie.
	 * For the basic part of the assignment (part 2), you should convert the 
	 * string to all lower case before you insert it. 
	 * 
	 * This method adds a word by creating and linking the necessary trie nodes 
	 * into the trie, as described outlined in the videos for this week. It 
	 * should appropriately use existing nodes in the trie, only creating new 
	 * nodes when necessary. E.g. If the word "no" is already in the trie, 
	 * then adding the word "now" would add only one additional node 
	 * (for the 'w').
	 * 
	 * @return true if the word was successfully added or false if it already exists
	 * in the dictionary.
	 */
	@Override
	public boolean addWord(String word) {
		if(word.equals("") || word.equals(null) ) return false;

		TrieNode currNode = root;

		for (Character c : word.toLowerCase().toCharArray()) {
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
		TrieNode currNode = root;

		for (Character c : s.toLowerCase().toCharArray()) {
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
		return currNode.endsWord() && currNode.getText().equals(s.toLowerCase());
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
     * @return A list containing the up to numCompletions best predictions
     */@Override
     public List<String> predictCompletions(String prefix, int numCompletions) {
		// Create a list of completions to return (initially empty)
		List<String> completions = new LinkedList<>();
		// 1. Find the stem in the trie.  If the stem does not appear in the trie, return an empty list
		TrieNode stemNode = root;
		for(char c : prefix.toLowerCase().toCharArray()) {
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
	// debugging tests [uncomment System.out.println on functions]
	public static void main(String[] args) {
		AutoCompleteDictionaryTrie smallDict = new AutoCompleteDictionaryTrie();

		smallDict.addWord("Hello");
		smallDict.addWord("a");
		smallDict.addWord("subsequent");

		smallDict.isWord("gel");

 		smallDict.printTree();
 		System.out.println("Size:" + smallDict.size());
 		System.out.println();
 		System.out.println("Test # Hello is in the dictionary: " + smallDict.isWord("Hello"));
 		System.out.println("Test # a is in the dictionary: " + smallDict.isWord("A"));
 		System.out.println("Test # subsequent is in the dictionary: " + smallDict.isWord("subsequent"));
	}
}