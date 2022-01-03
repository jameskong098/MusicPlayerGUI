package main;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class test {

	@Test
	void testQueue() throws Exception {
		Queue Q = new Queue();
		Q.enqueue("Finish javascript homework");
		Q.enqueue("Ride bicycle around town");
		Q.enqueue("Play some ranked valorant games");
		assertEquals("Finish javascript homework", Q.front.data);
		assertEquals("Finish javascript homework", Q.dequeue());
		assertEquals(2, Q.L.size);
		assertEquals("Ride bicycle around town", Q.front.data);
		assertEquals("Ride bicycle around town", Q.dequeue());
		assertEquals(1, Q.L.size);
		assertEquals("Play some ranked valorant games", Q.front.data);
		assertEquals("Play some ranked valorant games", Q.dequeue());
		assertEquals(null, Q.front);
		assertEquals(0, Q.L.size);
		Q.enqueue("Clean room");
		assertEquals(1, Q.L.size);
		assertEquals("Clean room", Q.front.data);
		assertEquals("Clean room", Q.dequeue());
		assertEquals(null, Q.front);
	}
	
	@Test
	void testDoubleLinkedList() throws Exception {
		DoubleLinkedList L = new DoubleLinkedList();
		Node cur = new Node();
		L.insert("A");
		cur = L.head;
		L.insert("B");
		cur = cur.next;
		L.remove(cur);
		L.insert("B");
		L.insert("C");
		L.insert("D");
		cur = L.head;
		cur = cur.next.next.next;
		L.remove(cur);
		L.insert("E");
		L.insert("F");
		L.insert("G");
		assertEquals("ABCEFG", L.toString());
		cur = L.tail;
		L.remove(cur);
		cur = L.tail;
		L.remove(cur);
		cur = L.head;
		L.remove(cur);
		assertEquals("BCE", L.toString());
		cur = L.head;
		L.remove(cur);
		assertEquals("CE", L.toString());
	}

}
