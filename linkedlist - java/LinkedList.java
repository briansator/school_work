package data_structures;

import java.util.Iterator;
import java.util.NoSuchElementException;


public class LinkedList<E> implements ListI<E>{
	private int counter;
	
	class iteratorHelper<E> implements Iterator<E>{
		Node<E> iterPointer;
		public iteratorHelper(){
			iterPointer = (Node<E>) head;
		}
		
		public boolean hasNext(){
			return iterPointer != null;
		}
		
		public E next(){
			if(!hasNext())
				throw new NoSuchElementException();
			E tmp = iterPointer.data;
			iterPointer = iterPointer.next;
			return tmp;
		}
		
		public void remove(){
			throw new UnsupportedOperationException();
		}
		
	}
	private class Node<E>{
		E data;
		Node<E> next;
		
		public Node(E data){
			this.data = data;
			next = null;
		}
	}

	
	private Node<E> head;
	
	public LinkedList(){
		head = null;
		counter = 0;
	}
/**
 * inserts data into the linked list
 * @param data to add to the list
 */
public void insert(E data){
	Node<E> newNode = new Node<E>(data);
	Node<E> previous = null, current = head;
	while(current != null && ((Comparable<E>)data).compareTo(current.data) > 0){
		previous = current;
		current = current.next;
	}
	if(previous == null){
		newNode.next = head;
		head = newNode;
	}else{
		previous.next = newNode;
		newNode.next = current;
	}
	
}
/**
 * Adds the Object obj to the beginning of the list
 * @param obj the object to add
 */
public void addFirst(E obj){
	Node<E> newNode = new Node<E>(obj);
	if(isEmpty()){
		head = newNode;
	}else{
	newNode.next = head;
	head = newNode;
	
	}
	counter++;
}
/**
 * Adds the Object obj to the end of the list
 * @param obj the object to add
 */
public void addLast(E obj){
	if(head == null){
		head = (Node<E>) obj;
	}
	Node<E> tmp = head;
	while(tmp != null){
		tmp = tmp.next;
	}
	tmp.next = (Node<E>) obj;
}
/**
 * Removes the first Object in the list and returns it.
 * @return the first object in the list or null if the list is empty.
 */
public E removeFirst(){
	if(head == null){
		return null;
	}
	Node<E> current = head;
	head = head.next;
	counter--;
	return current.data;
}
/**
 * Removes the last Object in the list and returns it.
 * @return the last object in the list or null if the list is empty.
 */
public E removeLast(){
	if(head == null){
		return null;
	}
	if(head.next == null){
		return removeFirst();
	}
	Node<E> current = head;
	Node<E> previous = null;
	while(current.next != null){
		previous = current;
		current = current.next;
	}
	previous.next = null;
	counter--;
	return current.data;
}
/**
 * Returns the first Object in the list, but does not remove it.
 * @return the first object in the list or null if the list is empty.
 */
public E peekFirst(){
	if(head == null){
		return null;
	}
	return head.data;
}
/**
 * Returns the last Object in the list, but does not remove it.
 * @return the last object in the list or null if the list is empty.
 */
public E peekLast(){
	if(head == null){
		return null;
	}
	if(head.next == null){
		return peekFirst();
	}
	Node<E> current = head;
	while(current.next != null){
		current = current.next;
	}
	return current.data;
}
/**
 * The list is returned to an empty state.
 */
public void makeEmpty(){
	head = null;
}
/**
 * Is the list empty?
 * @return true if the list is empty, otherwise false
 */
public boolean isEmpty(){
	if(head==null){
		return true;
	}
	return false;
}
/**
 * Is the list full? 
 * @return true if the list is full, otherwise false
 */
public boolean isFull(){
	return false;
}
/**
 * What is the size of the list
 * @return size of the list
 */
public int size(){
	return counter;
}
/**
 * Check to see if an object is in the list
 * @param obj  the object to look for
 * @return whether the list contains the object
 */
public boolean contains(E data){
	Node<E> tmp = head;
	while(tmp != null){
		if(((Comparable<E>)data).compareTo(tmp.data) == 0){
			return true;
		}
			tmp = tmp.next;
		
	}
	return false;
}
/** 
 * Returns an Iterator of the values in the list, presented in
 * the same order as the list.
 */
public Iterator<E> iterator(){
	return new iteratorHelper();
}
}