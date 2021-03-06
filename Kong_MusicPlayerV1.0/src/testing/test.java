/**
 * James Kong
 * jameskong098@gmail.com
 * December 21, 2021
 * Music Player GUI Project
 * test Class: Junit tests to test Queue ADT and doubly linked list ADT
 * Known Bugs: Queue Underflow/Doubly Linked List exception thrown
 */

package testing;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import main.DoubleLinkedList;
import main.Node;
import main.Queue;

class test {

	@Test
	void testQueue() throws Exception {
		Queue Q = new Queue();
		Q.enqueue("Finish javascript homework");
		Q.enqueue("Ride bicycle around town");
		Q.enqueue("Play some ranked valorant games");
		assertEquals("Finish javascript homework", Q.front.data);
		assertEquals("Finish javascript homework", Q.dequeueString());
		assertEquals(2, Q.L.size);
		assertEquals("Ride bicycle around town", Q.front.data);
		assertEquals("Ride bicycle around town", Q.dequeueString());
		assertEquals(1, Q.L.size);
		assertEquals("Play some ranked valorant games", Q.front.data);
		assertEquals("Play some ranked valorant games", Q.dequeueString());
		assertEquals(null, Q.front);
		assertEquals(0, Q.L.size);
		Q.enqueue("Clean room");
		assertEquals(1, Q.L.size);
		assertEquals("Clean room", Q.front.data);
		assertEquals("Clean room", Q.dequeueString());
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
