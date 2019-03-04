package textgen;

import java.util.AbstractList;
import java.util.Objects;


/** A class that implements a doubly linked list
 *
 * @author UC San Diego Intermediate Programming MOOC team
 *
 * @param <E> The type of the elements stored in the list
 */
public class MyLinkedList<E> extends AbstractList<E> {
	LLNode<E> head;
	LLNode<E> tail;
	int size;

	/** Create a new empty LinkedList */
	public MyLinkedList() {
		// create nodes
		head = new LLNode<E>(null);
		tail = new LLNode<E>(null, head);
		/*
		The tail node will be constructed with all the next, prev pointers and also update the head.next
		and should result as bellow
		*/
		//tail = new LLNode<E>(null);
		//head.next = tail;
		//tail.prev = head;

		size = 0;

	}

	/**
	 * Appends an element to the end of the list
	 * @param element The element to add
	 */
	public boolean add(E element) {
		this.add(this.size(), element);
		return true;
	}

	/**
	 * Get the element at position index
	 *
	 * @throws IndexOutOfBoundsException
	 *             if the index is out of bounds.
	 */
	public E get(int index) {
		if (index < 0 || index >= size) {
			throw new IndexOutOfBoundsException("MyLinkedList get => Index = "+index+" is invalid. List size = "+size);
		}
		return getNthNode(index).data;
	}

        // This will get the tail node (ex.: empty list), so the bounds need to be checked outside of this method.
	private LLNode<E> getNthNode(int index) {
		LLNode<E> node = head.next;
		while (--index >= 0) {
			node = node.next;
		}

		return node;
	}

	/**
	 * Add an element to the list at the specified index
	 *
	 * @param index where the element should be added
	 * @param element
	 *            The element to add
	 */
	public void add(int index, E element) {
		Objects.requireNonNull(element);

		if (index < 0 || index > size) {
			throw new IndexOutOfBoundsException("MyLinkedList add => Index = "+index+" is invalid. List size = "+size);
		}
		LLNode<E> nthNode = getNthNode(index);
		new LLNode<E>(element, nthNode.prev);
		++size;
	}

	/** Return the size of the list */
	public int size() {
		return size;
	}

	/**
	 * Remove a node at the specified index and return its data element.
	 *
	 * @param index
	 *            The index of the element to remove
	 * @return The data element removed
	 * @throws IndexOutOfBoundsException
	 *             If index is outside the bounds of the list
	 *
	 */
	public E remove(int index) {

		if (index < 0 || index >= size) {
			throw new IndexOutOfBoundsException("MyLinkedList remove => Index = "+index+" is invalid. List size = "+size);
		}
		LLNode<E> nthNode = getNthNode(index);
		nthNode.next.prev = nthNode.prev;
		nthNode.prev.next = nthNode.next;
		--size;
		return nthNode.data;
	}

	/**
	 * Set an index position in the list to a new element
	 *
	 * @param index
	 *            The index of the element to change
	 * @param element
	 *            The new element
	 * @return The element that was replaced
	 * @throws IndexOutOfBoundsException
	 *             if the index is out of bounds.
	 */
	public E set(int index, E element) {
		Objects.requireNonNull(element);
		if (index < 0 || index >= size) {
			throw new IndexOutOfBoundsException("MyLinkedList set => Index = "+index+" is invalid. List size = "+size);
		}
		LLNode<E> nthNode = getNthNode(index);
		E previousValue = nthNode.data;
		nthNode.data = element;
		return previousValue;
	}

	@Override
	public String toString() {
		StringBuilder stringRepresentation = new StringBuilder();
		stringRepresentation.append("[");

		int index = 0;

		for (E element : this) {
			if (index++ != size - 1) {
				stringRepresentation.append(element.toString() + ", ");
			} else {
				stringRepresentation.append(element.toString());
			}
		}

		stringRepresentation.append("]");

		return stringRepresentation.toString();
	}

	//simple test to see if linked list constructor works
	public static void main(String[] args) {
		MyLinkedList list = new MyLinkedList<Integer>();
		list.add(65);
		list.add(21);
		list.add(42);
		System.out.println(list.toString());

		System.out.println("Sentinel head of list (list.getNode(0).prev) = " + list.getNthNode(0).prev); //must be null
		System.out.println("Sentinel tail of list (list.getNode(2).next) = " + list.getNthNode(2).next); //must be null
		System.out.println("Node = 21 prev (list.getNode(1).prev) = " + list.getNthNode(1).prev);
		System.out.println("Node = 21 next (list.getNode(1).next) = " + list.getNthNode(1).next);

		MyLinkedList emptyList = new MyLinkedList<Integer>();
		System.out.println(emptyList.toString());
		System.out.println("Sentinal head of list (list.getNode(0).prev) = " + list.getNthNode(0).prev); //must be null
		System.out.println("Sentinal tail of list (list.getNode(0).next) = " + list.getNthNode(2).next); //must be null
	}
}

class LLNode<E> {
	LLNode<E> prev;
	LLNode<E> next;
	E data;

	public LLNode(E e) {
		this.data = e;
		this.prev = null;
		this.next = null;
	}

	public LLNode(E e, LLNode<E> prevNode) {
		this.data = e;
		//if the previous node is not head sentinel create pointers, else they will be automatically null
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
