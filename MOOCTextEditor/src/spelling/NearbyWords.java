/**
 * 
 */
package spelling;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;


/**
 * @author UC San Diego Intermediate MOOC team and Volfegan [Daniel L Lacerda]
 *
 */
public class NearbyWords implements SpellingSuggest {
	// THRESHOLD to determine how many words to look through when looking
	// for spelling suggestions (stops prohibitively long searching)
	// For use in the Optional Optimization in Part 2.
	private static final int THRESHOLD = 1000;

	Dictionary dict;

	public NearbyWords (Dictionary dict) {
	    this.dict = dict;
	}

	/** Return the list of Strings that are one modification away
	 * from the input string.  
	 * @param s The original String
	 * @param wordsOnly controls whether to return only words or any String
	 * @return list of Strings which are nearby the original string
	 */
	public List<String> distanceOne(String s, boolean wordsOnly )  {
		   List<String> retList = new ArrayList<String>();
		   insertions(s, retList, wordsOnly);
		   substitution(s, retList, wordsOnly);
		   deletions(s, retList, wordsOnly);
		   return retList;
	}

	
	/** Add to the currentList Strings that are one character mutation away
	 * from the input string.  
	 * @param s The original String
	 * @param currentList is the list of words to append modified words 
	 * @param wordsOnly controls whether to return only words or any String
	 * @return
	 */
	public void substitution(String s, List<String> currentList, boolean wordsOnly) {
		// for each letter in the s and for all possible replacement characters
		for(int index = 0; index < s.length(); index++){
			for(int charCode = (int)'a'; charCode <= (int)'z'; charCode++) {
				// use StringBuffer for an easy interface to permuting the letters in the String
                StringBuffer sb = new StringBuffer(s);
                //System.out.println("Substitution (index["+index+"], '"+sb.charAt(index)+"'=>'"+(char)charCode+"'): " + sb.toString());
                sb.setCharAt(index, (char)charCode);

				// if the item isn't in the list, isn't the original string, and
				// (if wordsOnly is true) is a real word, add to the list
				if(!currentList.contains(sb.toString()) &&
                        (!wordsOnly||dict.isWord(sb.toString())) && !s.equals(sb.toString())) {
                    //System.out.println("Substitution granted (index["+index+"], '"+(char)charCode+"'): " + sb.toString());
					currentList.add(sb.toString());
				}
			}
		}
	}
	
	/** Add to the currentList Strings that are one character insertion away
	 * from the input string.  
	 * @param s The original String
	 * @param currentList is the list of words to append modified words 
	 * @param wordsOnly controls whether to return only words or any String
	 * @return
	 */
	public void insertions(String s, List<String> currentList, boolean wordsOnly ) {
        // for each letter in the s and for all possible replacement characters
        for(int index = 0; index <= s.length(); index++){
            for(int charCode = (int)'a'; charCode <= (int)'z'; charCode++) {
                StringBuffer sb = new StringBuffer(s);
                sb.insert(index, (char)charCode);
                //System.out.println("Inserting (index["+index+"], '"+(char)charCode+"'): " + sb.toString());
                if(!currentList.contains(sb.toString()) && (!wordsOnly||dict.isWord(sb.toString()))) {
                    //System.out.println("Insertion granted (index["+index+"], '"+(char)charCode+"'): " + sb.toString());
                    currentList.add(sb.toString());
                }
            }
        }
	}

	/** Add to the currentList Strings that are one character deletion away
	 * from the input string.  
	 * @param s The original String
	 * @param currentList is the list of words to append modified words 
	 * @param wordsOnly controls whether to return only words or any String
	 * @return
	 */
	public void deletions(String s, List<String> currentList, boolean wordsOnly ) {
        // for each letter in the s and for all possible replacement characters
        for(int index = 0; index < s.length(); index++){
            StringBuffer sb = new StringBuffer(s);
            //System.out.println("Deleting (index["+index+"]= '"+sb.charAt(index)+"'): " + sb.toString());
            sb.deleteCharAt(index);
            if(!currentList.contains(sb.toString()) && (!wordsOnly||dict.isWord(sb.toString()))
                    && !s.equals(sb.toString())) {
                //System.out.println("Deleted granted (index["+index+"]='?': " + sb.toString());
                currentList.add(sb.toString());
            }

        }
	}

	/** Add to the currentList Strings that are one character deletion away
	 * from the input string.  
	 * @param word The misspelled word
	 * @param numSuggestions is the maximum number of suggestions to return 
	 * @return the list of spelling suggestions
	 */
	@Override
	public List<String> suggestions(String word, int numSuggestions) {
		// initial variables
		List<String> queue = new LinkedList<String>();     // Create a queue to hold words to explore
		HashSet<String> visited = new HashSet<String>();   // to avoid exploring the same string x times
		List<String> retList = new LinkedList<String>();   // words to return

		// insert first node(initial word)
		queue.add(word);
		visited.add(word);

        // while not enough words generated and queue is not empty
        int limit = 0; //and limit to stop any possible infinite exploration
        while(retList.size() <= numSuggestions && !queue.isEmpty() && limit <= THRESHOLD) {
            //remove the word from the start of the queue and assign to curr
            String curr = queue.remove(0);
            //get a list of neighbors (strings one mutation away from curr)
            List<String> neighbors = distanceOne(curr, false);
            //for each n in the list of neighbors
            for(String n : neighbors) {
                //if n is not visited
                if (!visited.contains(n)) {
                    //add n to the visited set
                    visited.add(n);
                    //add n to the back of the queue for further exploration
                    queue.add(n);
                    //if n is a word in the dictionary, add n to the suggestion list of words
                    if (retList.size() < numSuggestions && dict.isWord(n)) {
                        retList.add(n);
                    }
                }
            }
            limit++;
        }
		return retList;
	}	

	public static void main(String[] args) {
	   // basic testing code to get started
	   String word = "i";
	   // Pass NearbyWords any Dictionary implementation you prefer
	   Dictionary d = new DictionaryHashSet();
	   DictionaryLoader.loadDictionary(d, "data/dict.txt");
	   NearbyWords w = new NearbyWords(d);
	   List<String> l = w.distanceOne(word, true);
	   System.out.println("One away word Strings for for \""+word+"\" are:");
	   System.out.println(l+"\n");

       word = "tailo";
	   List<String> suggest = w.suggestions(word, 10);
	   System.out.println("Spelling Suggestions for \""+word+"\":");
	   System.out.println(suggest);
       word = "kangaro";
       suggest = w.suggestions(word, 10);
       System.out.println("Spelling Suggestions for \""+word+"\":");
       System.out.println(suggest);
   }

}
