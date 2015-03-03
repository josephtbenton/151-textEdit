package editor.model;

import static org.junit.Assert.*;

import org.junit.Test;

public class DoubleTextNodeTest {

	DoubleTextNode node1 = new DoubleTextNode('a');
	DoubleTextNode node2 = new DoubleTextNode('b');
	DoubleTextNode node3 = new DoubleTextNode('c');
	DoubleTextNode node4 = new DoubleTextNode('d');
	
	@Test
	public void testChars() {
		assertEquals('a', node1.getChar());
		assertEquals('b', node2.getChar());
		assertEquals('c', node3.getChar());
		assertEquals('d', node4.getChar());
	}
	
	@Test
	public void addAfterTest() {
		atStart();
		
		node1.addAfter(node2);
		oneTwo();
		
		node2.addAfter(node3);
		oneTwoThree();
		
		node2.addAfter(node4);
		andFour();
	}
	
	void oneTwo() {
		assertEquals(node1, node2.getPrev());
		assertEquals(node2, node1.getNext());
		assertEquals(null, node1.getPrev());
		assertEquals(null, node2.getNext());
	}

	@Test
	public void addBeforeTest() {
		atStart();
		
		node3.addBefore(node2);
		twoThree();
		
		node2.addBefore(node1);
		oneTwoThree();
		
		node3.addBefore(node4);
		andFour();
	}
	
	@Test
	public void removeBeforeTest() {
		addBeforeTest();
		node3.removeBefore();
		oneTwoThree();
		
		node1.removeBefore();
		oneTwoThree();
		
		node2.removeBefore();
		twoThree();
	}
	
	@Test
	public void removeAfterTest() {
		addAfterTest();
		node2.removeAfter();
		oneTwoThree();
		
		node3.removeAfter();
		oneTwoThree();
		
		node2.removeAfter();
		oneTwo();
	}
	
	void atStart() {
		assertEquals(null, node1.getPrev());
		assertEquals(null, node1.getNext());
	}
	
	void twoThree() {
		assertEquals(node2, node3.getPrev());
		assertEquals(node3, node2.getNext());
		assertEquals(null, node2.getPrev());
		assertEquals(null, node3.getNext());
	}
	
	void oneTwoThree() {
		assertEquals(node3, node2.getNext());
		assertEquals(node1, node2.getPrev());
		assertEquals(node2, node1.getNext());
		assertEquals(node2, node3.getPrev());
		assertEquals(node3, node1.getNext().getNext());
		assertEquals(node1, node3.getPrev().getPrev());
	}
	
	void andFour() {
		assertEquals(node4, node2.getNext());
		assertEquals(node2, node4.getPrev());
		assertEquals(node3, node4.getNext());
		assertEquals(node4, node3.getPrev());
	}
	
	boolean checkEqual(DoubleTextNode i1, DoubleTextNode i2) {
		while (i1 != null && i2 != null) {
			if (i1.getChar() != i2.getChar()) {return false;}
			i1 = i1.getNext();
			i2 = i2.getNext();
		}
		return i1 == null && i2 == null;
	}
	
	void printList(DoubleTextNode node) {
		for (DoubleTextNode n = node; n != null; n = n.getNext()) {
			System.out.println(n.getChar());
		}
		if (node == null) {System.out.println("null!");}
	}
	
	@Test
	public void testCopyAfter() {
		addAfterTest();
		DoubleTextNode copy = node1.copyAfter();
		assertTrue(checkEqual(node1, copy));
		copy.removeAfter();
		assertFalse(checkEqual(node1, copy));
	}
	
	@Test
	public void testCopyBefore() {
		addAfterTest();
		DoubleTextNode copy = node3.copyBefore();
		while (copy.getPrev() != null) {copy = copy.getPrev();}
		assertTrue(checkEqual(node1, copy));
		copy.removeAfter();
		assertFalse(checkEqual(node1, copy));
	}
	
	@Test
	public void testMergeAfter() {
		addAfterTest();
		DoubleTextNode mergee = new DoubleTextNode('e');
		mergee.addAfter(new DoubleTextNode('f'));
		node3.mergeAfter(mergee);
		assertEquals(mergee, node3.getNext());
		assertEquals(node3, mergee.getPrev());
		assertEquals(mergee.getNext(), node3.getNext().getNext());
	}
	
	@Test
	public void testMergeBefore() {
		addAfterTest();
		DoubleTextNode mergee = new DoubleTextNode('e');
		mergee.addAfter(new DoubleTextNode('f'));
		mergee = mergee.getNext();
		node1.mergeBefore(mergee);
		assertEquals(mergee, node1.getPrev());
		assertEquals(node1, mergee.getNext());
		assertEquals(mergee.getPrev(), node1.getPrev().getPrev());
	}
}