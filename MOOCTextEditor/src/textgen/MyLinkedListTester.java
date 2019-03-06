/**
 * 
 */
package textgen;

import java.util.LinkedList;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;


/**
 * @author UC San Diego MOOC team & Volfegan
 *
 */
public class MyLinkedListTester {

	private static final int LONG_LIST_LENGTH =10; 

	MyLinkedList<String> shortList;
	MyLinkedList<Integer> emptyList;
	MyLinkedList<Integer> longerList;
	MyLinkedList<Integer> list1;
	
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		// Feel free to use these lists, or add your own
        for (String s : shortList = new MyLinkedList<String>()) {

        }
		shortList.add("A");
		shortList.add("B");
		emptyList = new MyLinkedList<Integer>();
		longerList = new MyLinkedList<Integer>();
		for (int i = 0; i < LONG_LIST_LENGTH; i++) {
			longerList.add(i);
		}
		list1 = new MyLinkedList<Integer>();
		list1.add(65);
		list1.add(21);
		list1.add(42);
		
	}

	
	/** Test if the get method is working correctly.
	 */
	/*You should not need to add much to this method.
	 * We provide it as an example of a thorough test. */
	@Test
	public void testGet() {
		//test empty list, get should throw an exception
		try {
			emptyList.get(0);
			fail("Check out of bounds");
		} catch (IndexOutOfBoundsException e) { }
		
		// test short list, first contents, then out of bounds
		assertEquals("Check first", "A", shortList.get(0));
		assertEquals("Check second", "B", shortList.get(1));
		
		try {
			shortList.get(-1);
			fail("Check out of bounds");
		} catch (IndexOutOfBoundsException e) { }
		try {
			shortList.get(2);
			fail("Check out of bounds");
		} catch (IndexOutOfBoundsException e) { }

		// test longer list contents
		for(int i = 0; i<LONG_LIST_LENGTH; i++ ) {
			assertEquals("Check "+i+ " element", (Integer)i, longerList.get(i));
		}
		
		// test off the end of the longer array
		try {
			longerList.get(-1);
			fail("Check out of bounds");
		} catch (IndexOutOfBoundsException e) { }
		try {
			longerList.get(LONG_LIST_LENGTH);
			fail("Check out of bounds");
		} catch (IndexOutOfBoundsException e) { }
		
	}
	
	
	/** Test removing an element from the list.
	 * We've included the example from the concept challenge.
	 * You will want to add more tests.  */
	@Test
	public void testRemove() {
		int a = list1.remove(0);
		assertEquals("Remove: check a is correct ", 65, a);
		assertEquals("Remove: check element 0 is correct ", (Integer)21, list1.get(0));
		assertEquals("Remove: check size is correct ", 2, list1.size());
		//change getNode(int index) method to public for this test
		//assertEquals("Check if sentinel head of list is the pointer of prev node of element that replaced removed el.", list1.head, list1.getNode(0).prev);


        //check index out of bounds
        try {
            longerList.remove(-1);
            fail("Check out of bounds");
        } catch (IndexOutOfBoundsException e) { }
        try {
            longerList.remove(LONG_LIST_LENGTH);
            fail("Check out of bounds");
        } catch (IndexOutOfBoundsException e) { }
        try {
            emptyList.remove(0);
            fail("Check remove Empty List");
        } catch (IndexOutOfBoundsException e) { }


    }
	
	/** Test adding an element into the end of the list, specifically
	 *  public boolean add(E element)
	 * */
	@Test
	public void testAddEnd() {

        //test for null pointer, add should throw an exception
        try {
            emptyList.add(null);
            fail("Null Pointer Exception!");
        } catch (NullPointerException e) { }

        // test short list
        boolean b = shortList.add("C");
        assertEquals("Add: check if add returns true", true, b);
        assertEquals("Add: Check first", "A", shortList.get(0));
        assertEquals("Add: Check second", "B", shortList.get(1));
        assertEquals("Add: Check third", "C", shortList.get(2));
        assertEquals("Add: check size is correct ", 3, shortList.size());
		
	}

	
	/** Test the size of the list */
	@Test
	public void testSize() {
        assertEquals("size: check emptyList", 0, emptyList.size());
        assertEquals("size: check shortList", 2, shortList.size());
        assertEquals("size: check longerList", LONG_LIST_LENGTH, longerList.size());
        assertEquals("size: check list1", 3, list1.size());
	}

	
	
	/** Test adding an element into the list at a specified index,
	 * specifically:
	 * public void add(int index, E element)
	 * */
	@Test
	public void testAddAtIndex() {
        //test for null pointer, add should throw an exception
        try {
            list1.add(1, null);
            fail("Null Pointer Exception!");
        } catch (NullPointerException e) { }

        // check index out of bounds
        try {
            shortList.add(-1, "w");
            fail("Check out of bounds");
        } catch (IndexOutOfBoundsException e) { }
        try {
            shortList.add(3, "w");
            fail("Check out of bounds");
        } catch (IndexOutOfBoundsException e) { }

        // test short list
        shortList.add(1, "C");
        assertEquals("addAtIndex: Check first", "A", shortList.get(0));
        assertEquals("addAtIndex: Check second", "C", shortList.get(1));
        assertEquals("addAtIndex: Check third", "B", shortList.get(2));
        assertEquals("addAtIndex: check size is correct ", 3, shortList.size());

        // test off the end of the longer array
        longerList.add(LONG_LIST_LENGTH, LONG_LIST_LENGTH-1);
        assertEquals(Integer.valueOf(LONG_LIST_LENGTH-1), longerList.get(LONG_LIST_LENGTH-1));
        assertEquals(LONG_LIST_LENGTH+1, longerList.size());
    }
	
	/** Test setting an element in the list */
	@Test
	public void testSet() {
        //test for null pointer, set should throw an exception
        try {
            list1.set(1, null);
            fail("Null Pointer Exception!");
        } catch (NullPointerException e) { }

        // check index out of bounds
        try {
            shortList.set(-1, "w");
            fail("Check out of bounds");
        } catch (IndexOutOfBoundsException e) { }
        try {
            shortList.set(shortList.size(), "C");
            fail("Check out of bounds");
        } catch (IndexOutOfBoundsException e) { }
        try {
            emptyList.set(0, -1);
            fail("Check out of bounds");
        } catch (IndexOutOfBoundsException e) { }

        for ( int i = 0; i < LONG_LIST_LENGTH; i++) {
            assertEquals("Check set " + i + " element", (Integer) i, longerList.set(i, -i));
            assertEquals("Check get " + i + " element", Integer.valueOf(-i), longerList.get(i));
        }

        assertEquals("A", shortList.set(0, "33"));
        assertEquals("33", shortList.get(0));
    }

    @Test
    public void testToString(){
        assertEquals("[A, B]", shortList.toString());
        assertEquals("[65, 21, 42]", list1.toString());
    }
	
}
