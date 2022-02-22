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
