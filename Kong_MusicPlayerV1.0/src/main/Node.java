/**
 * James Kong
 * jameskong098@gmail.com
 * December 21, 2021
 * Music Player GUI Project
 * Node Class: Modified node class to include two data fields for Doubly Linked List
 * Known Bugs: No known bugs
 */

package main;

public class Node {
	Node next;
	Node prev;
	String data;
	String data2;
	
	public Node() {
		next = null;
		prev = null;
		data = null;
		data2 = null;
	}
	
	public Node(String c) {
		next = null;
		prev = null;
		data = c;
	}
	
	public Node(String c, String y) {
		next = null;
		prev = null;
		data = c;
		data2 = y;
	}
}
