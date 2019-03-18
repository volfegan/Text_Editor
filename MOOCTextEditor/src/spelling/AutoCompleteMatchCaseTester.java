/**
 * 
 */
package spelling;

import static org.junit.Assert.*;

import java.util.LinkedList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

/**
 * @author Volfegan [DanieL L Lacerda]
 *
 */
public class AutoCompleteMatchCaseTester {

	private String dictFile = "data/words.small.txt";

    AutoCompleteMatchCase emptyDict;
    AutoCompleteMatchCase smallDict;
	
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception 
	{
		emptyDict = new AutoCompleteMatchCase();
		smallDict = new AutoCompleteMatchCase();

		smallDict.addWord("Hello");
		smallDict.addWord("HElLo");
		smallDict.addWord("help");
		smallDict.addWord("he");
		smallDict.addWord("hem");
		smallDict.addWord("hot");
		smallDict.addWord("hey");
		smallDict.addWord("a");
		smallDict.addWord("subsequent");
	}

	
	/** Test if the size method is working correctly.
	 */
	@Test
	public void testSize()
	{
		assertEquals("Testing size for empty dict", 0, emptyDict.size());
		assertEquals("Testing size for small dict", 8, smallDict.size());
	}
	
	/** Test the isWord method */
	@Test
	public void testIsWord()
	{
		assertEquals("Testing isWord on empty: Hello", false, emptyDict.isWord("Hello"));
		assertEquals("Testing isWord on small: Hello", true, smallDict.isWord("Hello"));
		
		assertEquals("Testing isWord on small: hello", false, smallDict.isWord("hello"));

		assertEquals("Testing isWord on small: hellow", false, smallDict.isWord("hellow"));
		
		assertEquals("Testing isWord on empty: empty string", false, emptyDict.isWord(""));
		assertEquals("Testing isWord on small: empty string", false, smallDict.isWord(""));
		
		assertEquals("Testing isWord on small: no", false, smallDict.isWord("no"));
		
		assertEquals("Testing isWord on small: subsequent", true, smallDict.isWord("subsequent"));
		
		
	}
	
	/** Test the addWord method */
	@Test
	public void testAddWord() {
		
		
		assertEquals("Asserting hellow is not in empty dict", false, emptyDict.isWord("hellow"));
		assertEquals("Asserting hellow is not in small dict", false, smallDict.isWord("hellow"));
		
		emptyDict.addWord("hellow");
		smallDict.addWord("hellow");

		assertEquals("Asserting hellow is in empty dict", true, emptyDict.isWord("hellow"));
		assertEquals("Asserting hellow is in small dict", true, smallDict.isWord("hellow"));

		assertEquals("Asserting xyzabc is not in empty dict", false, emptyDict.isWord("xyzabc"));
		assertEquals("Asserting xyzabc is not in small dict", false, smallDict.isWord("xyzabc"));

		
		emptyDict.addWord("XYZAbC");
		smallDict.addWord("XYZAbC");
        assertEquals("Asserting xyzabc is in empty dict", false, emptyDict.isWord("xyzabc"));
        assertEquals("Asserting xyzabc is in small dict", false, smallDict.isWord("xyzabc"));
        assertEquals("Asserting XYZAbC is in empty dict", true, emptyDict.isWord("XYZAbC"));
        assertEquals("Asserting XYZAbC is in small dict", true, smallDict.isWord("XYZAbC"));
		
		
		assertEquals("Testing isWord on empty: empty string", false, emptyDict.isWord(""));
		assertEquals("Testing isWord on small: empty string", false, smallDict.isWord(""));
		
		assertEquals("Testing isWord on small: no", false, smallDict.isWord("no"));
		
		assertEquals("Testing isWord on small: subsequent", true, smallDict.isWord("subsequent"));
		
		
	}
	
	@Test
	public void testPredictCompletions()
	{
		List<String> completions;
		completions = smallDict.predictCompletions("", 0);
		assertEquals(0, completions.size());
		
		completions = smallDict.predictCompletions("",  4);
		assertEquals(4, completions.size());
		assertTrue(completions.contains("a"));
		assertTrue(completions.contains("he"));
		boolean twoOfThree = completions.contains("hey") && completions.contains("hot") ||
				             completions.contains("hey") && completions.contains("hem") ||
				             completions.contains("hot") && completions.contains("hem");
		assertTrue(twoOfThree);
		
		completions = smallDict.predictCompletions("he", 2);
		boolean allIn = completions.contains("he") && 
				(completions.contains("hem") || completions.contains("hey"));
		assertEquals(2, completions.size());
		assertTrue(allIn);
		
		completions = smallDict.predictCompletions("hel", 10);
		assertEquals(1, completions.size());
		allIn = completions.contains("help");
		assertTrue(allIn);

        completions = smallDict.predictCompletions("He", 10);
        assertEquals(1, completions.size());
        allIn = completions.contains("Hello");
        assertTrue(allIn);
	
		completions = smallDict.predictCompletions("x", 5);
		assertEquals(0, completions.size());
	}
	
	
	
	
}
