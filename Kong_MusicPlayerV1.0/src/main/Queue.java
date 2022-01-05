/**
 * James Kong
 * jameskong098@gmail.com
 * December 21, 2021
 * Music Player GUI Project
 * Queue Class: Queue ADT Data Structure made using a doubly linked list
 * Known Bugs: Queue Underflow exception thrown
 */

package main;

public class Queue {
	DoubleLinkedList L;
	Node front;
	
	/**
	 * Queue constructor, initializes fields
	 */
	
	public Queue() {
		L = new DoubleLinkedList();
		front = null;
	}
	
	/**
	 * enqueue: adds an element to front of queue
	 * @param data (element to be added)
	 */
	
	public void enqueue(String data) {
		if (front == null) {
			L.insert(data);
			front = L.head;
		}
		else {
			L.insert(data);
		}
	}
	
	/**
	 * enqueue(Node cur) adds a node to front of queue
	 * @param cur
	 */

	public void enqueue(Node cur) {
		if (front == null) {
			L.insert(cur);
			front = L.head;
		}
		else {
			L.insert(cur);
		}
	}
	
	/**
	 * dequeue: removes an element from the front of queue
	 * @return T (element removed is returned
	 * @throws Exception 
	 */
	
	public String dequeueString() throws Exception {
		if (L.size - 1 < 0) {
			throw new Exception("Queue underflow");
		}
		String temp = L.head.data;
		L.remove(L.head);
		front = L.head;
		return temp;
	}
	
	public Node dequeue() throws Exception {
		if (L.size - 1 < 0) {
			throw new Exception("Queue underflow");
		}
		Node temp = new Node(L.head.data, L.head.data2);
		L.remove(L.head);
		front = L.head;
		return temp;
	}
	
	/**
	 * returns the front element of the queue without removing it
	 * @return the front node element of the queue is returned
	 */
	
	public Node peek() {
		return front;
	}
	
	/**
	 * getSize: getter that returns the size of the queue
	 * @return L.size (size of linked list)
	 */
	
	public int getSize() {
		return L.size;
	}
	
	/**
	 * front: returns the data of the front element
	 * @return T (front returned)
	 */
	
	public String front() {
		return front.data;
	}
	
}