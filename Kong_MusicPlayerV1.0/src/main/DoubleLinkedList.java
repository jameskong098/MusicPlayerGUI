package main;

public class DoubleLinkedList {
	Node cur;
	Node head;
	Node tail;
	int size;
	String songName;
		
	public DoubleLinkedList() {
		cur = null;
		head = null;
		tail = null;
	}
	
	public void insert(Node cur) {
		if (head == null) {
			head = cur;
			tail = cur;
		}
		else {
			tail.next = cur;
			cur.prev = tail;
			tail = cur;
		}
		size += 1;
	}
	
	public void insert(String x) {
		cur = new Node(x);
		if (head == null) {
			head = cur;
			tail = cur;
		}
		else {
			tail.next = cur;
			cur.prev = tail;
			tail = cur;
		}
		size += 1;
	}
	
	public void insert(String x, String y) {
		cur = new Node(x,y);
		if (head == null) {
			head = cur;
			tail = cur;
		}
		else {
			tail.next = cur;
			cur.prev = tail;
			tail = cur;
		}
		size += 1;
	}
	
	public boolean remove(Node cur) throws Exception {
		if (head == null) {
			throw new Exception("Error: Double linked list is empty!");
		}
		else if (cur == head && size == 1) {
			head = null;
			tail = null;
		}
		else if (cur == head && cur.next != null) {
			head = cur.next;
			head.prev = null;
		}
		else if (cur == tail) {
			tail = cur.prev;
			tail.next = null;
		}
		else if (cur.next != null && cur.prev != null){
			cur.prev.next = cur.next;
			cur.next.prev = cur.prev;
		}
		size -= 1;
		return true;
	}
	
	public String toString() {
		cur = head;
		String temp = "";
		while (cur != null) {
			temp += cur.data;
			cur = cur.next;
		}
		if (temp == "" && cur == null) {
			temp += "[Empty]";
		}
		return temp;
	}
}
