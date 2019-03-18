/**
 * 
 */
package spelling;

//import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * WPTree implements WordPath by dynamically creating a tree of words during a Breadth First
 * Search of Nearby words to create a path between two words. 
 * 
 * @author UC San Diego Intermediate MOOC team & Volfegan [Daniel L L]
 *
 */
public class WPTree implements WordPath {
    // THRESHOLD number of words to look through for spelling suggestions (stops prohibitively long searching)
    private static final int THRESHOLD = 1000000;

    // this is the root node of the WPTree
	private WPTreeNode root;
	// used to search for nearby Words
	private NearbyWords nw; 
	
	// This constructor is used by the Text Editor Application
	// You'll need to create your own NearbyWords object here.
	public WPTree () {
		this.root = null;
        Dictionary d = new DictionaryHashSet();
        DictionaryLoader.loadDictionary(d, "data/dict.txt");
        this.nw = new NearbyWords(d);
	}
	
	//This constructor will be used by the grader code
	public WPTree (NearbyWords nw) {
		this.root = null;
		this.nw = nw;
	}

    /** Return a path from word1 to word2 through dictionary words with
     *  the restriction that each step in the path can involve only a
     *  single character mutation
     * @param word1 The first word
     * @param word2 The second word
     * @return list of Strings which are the path from word1 to word2
     *         including word1 and word2 or null if no path is found
     */
	public List<String> findPath(String word1, String word2) {

	    //check if word2 is valid word in the dictionary
        if(!nw.dict.isWord(word2)) {
            return null;
        }
        //Create a queue of WPTreeNodes to hold words to explore
        Queue<WPTreeNode> queue =  new LinkedList<>();
        //Create a visited set to avoid looking at the same word repeatedly
        HashSet<String> visited = new HashSet<>();
        //Set the root (parent node = null) to be a WPTreeNode containing word1
        root = new WPTreeNode(word1, null);
        //Add the initial word to visited
        visited.add(word1);
        //Add root to the queue
        queue.add(root);

        // while queue has elements and we have not found word2
        while(!queue.isEmpty() && visited.size() < THRESHOLD) {
            //remove the node from the start of the queue and assign to curr
            WPTreeNode curr = queue.remove();
            //get a list of real word neighbors (one mutation from curr's word)
            List<String> neighbors = nw.distanceOne(curr.getWord(), true);
            //for each n in the list of neighbors
            for(String n: neighbors) {
                //if n is not visited
                if(!visited.contains(n)) {
                    WPTreeNode node = curr.addChild(n); //add n as a child of curr
                    visited.add(n); //add n to the visited set
                    queue.add(node); //add the node for n to the back of the queue
                    //printQueue(queue);
                    if(n.equals(word2))
                        return node.buildPathToRoot();
                }
            }
        }
        // return null as no path exists
        return null;
	}
	// Method to print a list of WPTreeNodes (useful for debugging)
	private String printQueue(Queue<WPTreeNode> list) {

        //System.out.println(Arrays.toString(list.toArray()));

		String ret = "[ ";
		
		for (WPTreeNode w : list) {
			ret+= w.getWord()+", ";
		}
		ret+= "]";
        System.out.println(ret);
		return ret;
	}


    public static void main(String[] args) {
        // basic testing code
        String word1;
        String word2;
        List<String> path;
        Dictionary dict = new DictionaryHashSet();
        DictionaryLoader.loadDictionary(dict, "data/grader_dict.txt");
        System.out.println("Dictionary size = "+dict.size());
        WPTree tree = new WPTree(new NearbyWords(dict));

        word1 = "pool";
        word2 = "spoon";
        path = tree.findPath(word1, word2);
        if (path == null) {path = new LinkedList<>(); path.add("null");}
        System.out.println("\""+word1+"\"=>\""+word2+"\" path = " +  Arrays.toString(path.toArray()));

        word1 = "stools";
        word2 = "moon";
        path = tree.findPath(word1, word2);
        if (path == null) {path = new LinkedList<>(); path.add("null");}
        System.out.println("\""+word1+"\"=>\""+word2+"\" path = " +  Arrays.toString(path.toArray()));

        word1 = "foal";
        word2 = "needless"; //impossible path
        path = tree.findPath(word1, word2);
        if (path == null) {path = new LinkedList<>(); path.add("null");}
        System.out.println("\""+word1+"\"=>\""+word2+"\" path = " +  Arrays.toString(path.toArray()));

        word1 = "needle";
        word2 = "kitten"; //word not in grader_dict.txt
        path = tree.findPath(word1, word2);
        if (path == null) {path = new LinkedList<>(); path.add("null");}
        System.out.println("\""+word1+"\"=>\""+word2+"\" path = " +  Arrays.toString(path.toArray()));


        dict = new DictionaryHashSet();
        DictionaryLoader.loadDictionary(dict, "data/dict.txt");
        System.out.println("Dictionary size = "+dict.size());
        tree = new WPTree(new NearbyWords(dict));
        word1 = "the";
        word2 = "kangaroo"; //impossible path
        path = tree.findPath(word1, word2);
        if (path == null) {path = new LinkedList<>(); path.add("null");}
        System.out.println("\""+word1+"\"=>\""+word2+"\" path = " +  Arrays.toString(path.toArray()));
    }


	
}

/* Tree Node in a WordPath Tree. This is a standard tree with each
 * node having any number of possible children.  Each node should only
 * contain a word in the dictionary and the relationship between nodes is
 * that a child is one character mutation (deletion, insertion, or
 * substitution) away from its parent
*/
class WPTreeNode {
    
    private String word;
    private List<WPTreeNode> children;
    private WPTreeNode parent;
    
    /** Construct a node with the word w and the parent p
     *  (pass a null parent to construct the root)  
	 * @param w The new node's word
	 * @param p The new node's parent
	 */
    public WPTreeNode(String w, WPTreeNode p) {
        this.word = w;
        this.parent = p;
        this.children = new LinkedList<WPTreeNode>();
    }
    
    /** Add a child of a node containing the String s
     *  precondition: The word is not already a child of this node
     * @param s The child node's word
	 * @return The new WPTreeNode
	 */
    public WPTreeNode addChild(String s){
        WPTreeNode child = new WPTreeNode(s, this);
        this.children.add(child);
        return child;
    }
    
    /** Get the list of children of the calling object
     *  (pass a null parent to construct the root)
	 * @return List of WPTreeNode children
	 */
    public List<WPTreeNode> getChildren() {
        return this.children;
    }
   
    /** Allows you to build a path from the root node to the calling object
     * @return The list of strings starting at the root and 
     *         ending at the calling object
	 */
    public List<String> buildPathToRoot() {
        WPTreeNode curr = this;
        List<String> path = new LinkedList<String>();
        while(curr != null) {
            path.add(0,curr.getWord());
            curr = curr.parent; 
        }
        return path;
    }
    
    /** Get the word for the calling object
     *
	 * @return Getter for calling object's word
	 */
    public String getWord() {
        return this.word;
    }
    
    /** toString method
    *
	 * @return The string representation of a WPTreeNode
	 */
    public String toString() {
        String ret = "Word: "+word+", parent = ";
        if(this.parent == null) {
           ret+="null.\n";
        }
        else {
           ret += this.parent.getWord()+"\n";
        }
        ret+="[ ";
        for(WPTreeNode curr: children) {
            ret+=curr.getWord() + ", ";
        }
        ret+=(" ]\n");
        return ret;
    }

}

