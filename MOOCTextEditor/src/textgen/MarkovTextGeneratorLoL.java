package textgen;

import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/** 
 * An implementation of the MTG interface that uses a list of lists.
 * @author UC San Diego Intermediate Programming MOOC team & Volfegan
 */
public class MarkovTextGeneratorLoL implements MarkovTextGenerator {

	// The list of words with their next words
	private List<ListNode> wordList; 
	
	// The starting "word"
	private String starter;
	
	// The random number generator
	private Random rnGenerator;
	
	public MarkovTextGeneratorLoL(Random generator) {
		wordList = new LinkedList<ListNode>();
		starter = "";
		rnGenerator = generator;
	}

	/** Train the generator by adding the sourceText */
	@Override
	public void train(String sourceText) {

        List<String> words = getWords(sourceText);
        if (words.isEmpty()) {
            System.out.println("Empty text");
            return;
        };
        //set "starter" to be the first word in the text
        starter = words.get(0);

        //set "prevWord" to be starter
        String prevWord = starter;

        /*for each word "w" in the source text starting at the second word
		check to see if "prevWord" is already a node in the list*/
        int count = 0;
        for (String w : words) {
            count++;
            if (count < 2) continue; //checking if above 2nd word
            if (wordList_contains(prevWord)) {
                //if "prevWord" is a node in the list add "w" as a nextWord to the "prevWord" node
                ListNode node = getNode(prevWord);
                node.addNextWord(w);
                //System.out.println("\n"+count+". debugging node; "+node+"\n");
            } else {
                //add a node to the list with "prevWord" as the node's word
                ListNode node = new ListNode(prevWord);
                //add "w" as a nextWord to the "prevWord" node
                node.addNextWord(w);
                wordList.add(node);
                //System.out.println(""+count+". debugging node; "+node+"");
            }
            //set "prevWord" = "w"
            prevWord = w;
        }
        //add starter to be a next word for the last word in the source text
        ListNode node = getNode(prevWord);
        if (node == null) {
            node = new ListNode(prevWord);
        }
        node.addNextWord(starter);
        if (!wordList_contains(prevWord)) wordList.add(node);
        //System.out.println("Last word; "+node+"\n");
	}
	/*
    An example of the train function should work as below.
    The wordList content if trained on the string:
    "hi there hi Leo" (Notice the starter points to "hi")

                               wordList
    -------------------------------------------------------------
    |  | -------------- | | -------------- | | -------------- | |
    |  | |     hi     | | | |    there   | | | |    Leo     | | |
    |  | -------------- | | -------------- | | -------------- | |
    |  |     (WORD)     | |     (WORD)     | |     (WORD)     | |
    |  |                | |                | |                | |
    |  | -------------- | | -------------- | | -------------- | |
    |  | |[there, Leo]| | | |     hi     | | | |     hi     | | |
    |  | -------------- | | -------------- | | -------------- | |
    |  |   (nextWORD)   | |   (nextWORD)   | |   (nextWORD)   | |
    |  |                | |                | |                | |
    |   ----------------   ----------------   ----------------  |
    |       ListNode           ListNode           ListNode      |
    |-----------------------------------------------------------|
    will result:

    hi: there->Leo->
    there: hi->
    Leo: hi->
    */

	/** 
	 * Generate the number of words requested.
	 */
	@Override
	public String generateText(int numWords) {
        if(numWords == 0 || this.wordList.size() == 0 ){
            return "";
        }
        //set "currWord" to be the starter word
        String currWord = starter;
        //set "output" to be ""
        StringBuilder output = new StringBuilder();
        //add "currWord" to output
        output.append(currWord);
        //while you need more words
        int countGeneratedWords = 1;
        while (numWords > countGeneratedWords){
            //separate the words with space
            output.append(" ");

            //find the "node" corresponding to "currWord" in the list
            ListNode node = getNode(currWord);
            //select a random word "w" from the "wordList" for "node"
            String w = node.getRandomNextWord(rnGenerator);
            //add "w" to the "output"
            output.append(w);
            //set "currWord" to be "w"
            currWord = w;
            //increment number of words added to the output list
            countGeneratedWords++;
        }
        return output.toString();
	}
	/*
	An example for generateText trained on "hi there hi Leo" to generate 4 words:
	Possible outputs:

	hi Leo hi Leo
    hi there hi there
    hi there hi Leo
    hi Leo hi there
	 */

	/** Retrain the generator from scratch on the source text */
	@Override
	public void retrain(String sourceText) {
        wordList.clear();
        train(sourceText);
	}

    // Can be helpful for debugging
    @Override
    public String toString() {
        String toReturn = "";
        for (ListNode n : wordList) {
            toReturn += n.toString();
        }
        return toReturn;
    }

	// Helper methods section

    /** Returns a LinkedList of words from the source text string
     * Totally based on getTokens(getTokens(String pattern) method from Document.java
     * @param sourceText The text string
     * @return A LinkedList of tokens (words) from the source text
     */
    private List<String> getWords(String sourceText){
        List<String> words = new LinkedList<>();
        //Punctuation counts, so it's okay to end up with the same word punctuated and not punctuated
        //if we need only words without punctuation, pattern = "[0-9]+|[a-zA-Z]+"
        Pattern tokSplitter = Pattern.compile("[^ ]+");
        Matcher m = tokSplitter.matcher(sourceText);
        while (m.find()){
            words.add(m.group());
        }
        return words;
    }

    /** Check if List<ListNode> wordList contains the String word
     * @param word to be checked
     * @return boolean
     */
    private boolean wordList_contains(String word){
        for (ListNode node : wordList) {
            if (node.getWord().equals(word)) {
                return true;
            }
        }
        return false;
    }

    /** Returns the ListNode node that contains the String word
     * @param word parameter of the node
     * @return the ListNode node or null
     */
    private ListNode getNode(String word){
        for (ListNode node : wordList) {
            if (node.getWord().equals(word)) {
                return node;
            }
        }
        return null;
    }
	
	/**
	 * This is a minimal set of tests.  Note that it can be difficult
	 * to test methods/classes with randomized behavior.   
	 * @param args
	 */
	public static void main(String[] args) {
		// feed the generator a fixed random value for repeatable behavior
		MarkovTextGeneratorLoL gen = new MarkovTextGeneratorLoL(new Random(42));

        String textString = "hi there hi Leo";
		System.out.println(textString);
		gen.train(textString);
		System.out.println(gen);
        System.out.println(gen.generateText(4)+"\n");

        String textString1 = "Hello.  Hello there.  This is a test.  Hello there.  Hello Bob.  Test again.";
        System.out.println(textString1);
        gen.retrain(textString1);
        System.out.println(gen);
        System.out.println(gen.generateText(20)+"\n");

		String textString2 = "You say yes, I say no, "+
				"You say stop, and I say go, go, go, "+
				"Oh no. You say goodbye and I say hello, hello, hello, "+
				"I don't know why you say goodbye, I say hello, hello, hello, "+
				"I don't know why you say goodbye, I say hello. "+
				"I say high, you say low, "+
				"You say why, and I say I don't know. "+
				"Oh no. "+
				"You say goodbye and I say hello, hello, hello. "+
				"I don't know why you say goodbye, I say hello, hello, hello, "+
				"I don't know why you say goodbye, I say hello. "+
				"Why, why, why, why, why, why, "+
				"Do you say goodbye. "+
				"Oh no. "+
				"You say goodbye and I say hello, hello, hello. "+
				"I don't know why you say goodbye, I say hello, hello, hello, "+
				"I don't know why you say goodbye, I say hello. "+
				"You say yes, I say no, "+
				"You say stop and I say go, go, go. "+
				"Oh, oh no. "+
				"You say goodbye and I say hello, hello, hello. "+
				"I don't know why you say goodbye, I say hello, hello, hello, "+
				"I don't know why you say goodbye, I say hello, hello, hello, "+
				"I don't know why you say goodbye, I say hello, hello, hello,";
		System.out.println(textString2);
		gen.retrain(textString2);
		System.out.println(gen);
		System.out.println(gen.generateText(20));
	}

}

/** Links a word to the next words in the list 
 * You should use this class in your implementation. */
class ListNode {
    // The word that is linking to the next words
	private String word;
	
	// The next words that could follow it
	private List<String> nextWords;
	
	ListNode(String word) {
		this.word = word;
		nextWords = new LinkedList<String>();
	}
	
	public String getWord() {
	    return word;
	}

	public void addNextWord(String nextWord) {
	    nextWords.add(nextWord);
	}
	
	public String getRandomNextWord(Random generator) {
	    // The random number generator should be passed from the MarkovTextGeneratorLoL class
        int index = generator.nextInt(nextWords.size());
        return nextWords.get(index);
	}

	public String toString() {
		String toReturn = word + ": ";
		for (String s : nextWords) {
			toReturn += s + "->";
		}
		toReturn += "\n";
		return toReturn;
	}
	
}


