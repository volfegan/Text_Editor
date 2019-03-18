package textgen;

import java.util.AbstractList;


/** A class that implements a doubly linked list
 *
 * @author UC San Diego Intermediate Programming MOOC team & Volfegan [Daniel L Lacerda]
 *
 * @param <E> The type of the elements stored in the list
 */
public class MyLinkedList<E> extends AbstractList<E> {
	LLNode<E> head;
	LLNode<E> tail;
	int size;

	/** Create a new empty LinkedList */
	public MyLinkedList() {
		size = 0;
		head = new LLNode<E>(null);
		tail = new LLNode<E>(null, head);
        /*
        The tail node will be constructed with all the next, prev pointers and also update the head.next
        and should result as bellow
		*/
		//tail = new LLNode<E>(null);
		//head.next = tail;
		//tail.prev = head;
	}

    /**
     * Get the LLNode<E> element at position index (helper method)
     *
     * @param index of the element
     * @return The LLNode<E> element
     */
	private LLNode<E> getNode(int index) {
        // This will get the tail node (ex.: empty list), so the bounds need to be checked outside of this method.
        LLNode<E> node = head.next;
        int countIndex = 0;
        while(node != tail && countIndex < index) {
            node = node.next;
			countIndex++;
        }
        return node;
    }

	/**
	 * Appends an element to the end of the list
	 * @param element The element to add
	 */
	public boolean add(E element ) {
        this.add(this.size(), element);
        return true;
	}

	/**
	 * Add an element to the list at the specified index
	 * @param index where the element should be added
	 * @param element The element to add
	 */
	public void add(int index, E element ) {
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException("MyLinkedList add => Index = "+index+" is invalid. List size = "+size);
        }
        if (element == null) {
            throw new NullPointerException("MyLinkedList: Null elements not allowed in List.");
        }
        //just like the example show for Singly Linked List [Concept Challenge Implementing linked lists, a first attempt]
        //and [Support Adding to a Linked List]
        LLNode<E> currentNode = getNode(index);
        //get the previous Node of currentNode and insert LLNode<E> element using the constructor. All pointer will be handle by it
        new LLNode<E>(element, currentNode.prev);
        size++;
	}


    /** Get the element at position index
     * @throws IndexOutOfBoundsException if the index is out of bounds. */
    public E get(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("MyLinkedList get => Index = "+index+" is invalid. List size = "+size);
        }
        return getNode(index).data;
    }

    /**
     * Set an index position in the list to a new element
     * @param index The index of the element to change
     * @param element The new element
     * @return The element that was replaced
     * @throws IndexOutOfBoundsException if the index is out of bounds.
     */
    public E set(int index, E element) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("MyLinkedList set => Index = "+index+" is invalid. List size = "+size);
        }
        if (element == null) {
            throw new NullPointerException("MyLinkedList: Null elements not allowed in List.");
        }
        LLNode<E> currentNode = getNode(index);
        E tempData = currentNode.data;
		currentNode.data = element;
        return tempData;
    }

	/** Remove a node at the specified index and return its data element.
	 * @param index The index of the element to remove
	 * @return The data element removed
	 * @throws IndexOutOfBoundsException If index is outside the bounds of the list
	 *
	 */
	public E remove(int index) {
		if (index < 0 || index >= size) {
			throw new IndexOutOfBoundsException("MyLinkedList remove => Index = "+index+" is invalid. List size = "+size);
		}
		LLNode<E> currentNode = getNode(index);
		// fix next elements previous pointer
		currentNode.next.prev = currentNode.prev;

		// fix prev elements next pointer
		currentNode.prev.next = currentNode.next;

		size--;
		return currentNode.data;
	}

	/** Return the size of the list */
	public int size() {
		return size;
	}

	@Override
	public String toString() {
		StringBuilder s = new StringBuilder();
		s.append("[");

		int index = 0;

		if (this.size == 0) s.append(" ");

		for (E element : this) {
			if (index != size - 1) {
				s.append(element.toString() + ", ");
				index++;
			} else {
				s.append(element.toString());
			}
		}
		s.append("]");

		return s.toString();
	}

	//simple test to see if linked list constructor works
	public static void main(String[] args) {
		MyLinkedList list = new MyLinkedList<Integer>();
		list.add(65);
		list.add(21);
		list.add(42);
		System.out.println(list.toString());
		//test sentinel
		System.out.println("Sentinel head of list (list.getNode(0).prev) = " + list.getNode(0).prev); //must be null
		System.out.println("Sentinel tail of list (list.getNode(2).next) = " + list.getNode(2).next); //must be null
		System.out.println("Node = 21 prev (list.getNode(1).prev) = " + list.getNode(1).prev);
		System.out.println("Node = 21 next (list.getNode(1).next) = " + list.getNode(1).next);

		MyLinkedList emptyList = new MyLinkedList<Integer>();
		System.out.println(emptyList.toString());
		System.out.println("Sentinal head of list (list.getNode(0).prev) = " + list.getNode(0).prev); //must be null
		System.out.println("Sentinal tail of list (list.getNode(0).next) = " + list.getNode(2).next); //must be null
	}

} //end of MyLinkedList class

//Link Listed Node
class LLNode<E> {
	public LLNode<E> prev;
	public LLNode<E> next;
	E data;

	public LLNode(E e) {
		this.data = e;
		this.prev = null;
		this.next = null;
	}

	public LLNode(E e, LLNode<E> prevNode) {
		this.data = e;
		//if the previous node is not head sentinel create pointers
		if (prevNode != null) {
			this.next = prevNode.next;
			//if the next node is not tail sentinel, point the nextNode.prev to this current node
			if (this.next != null) {
				(this.next).prev = this;
			}
			prevNode.next = this;
			this.prev = prevNode;
		}
	}

	public String toString() {
		if (data == null) return "null";
		return this.data.toString();
	}
}
