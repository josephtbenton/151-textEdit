package editor.model;

import static org.junit.Assert.*;

import org.junit.Test;

abstract public class DocumentViewTest {
	
	abstract DocumentView makeView();
	
	DocumentView c = makeView();
	DocumentView d;
	
	@Test
	public void emptyTest() {
		assertTrue(c.isEmpty());
		assertTrue(c.atStart());
		assertTrue(c.atEnd());
	}
	
	@Test
	public void consistentTest1() {
		assertTrue(c.isConsistent());
	}
	
	@Test
	public void insertTest1() {
		c.insert('a');
		assertTrue(c.isConsistent());
		assertFalse(c.isEmpty());
		assertEquals('a', c.peekBefore());
		assertTrue(c.atEnd());
		assertFalse(c.atStart());
	}

	@Test
	public void insertTest2() {
		insertTest1();
		c.insert('b');
		assertTrue(c.isConsistent());
		assertEquals('b', c.peekBefore());
		assertTrue(c.atEnd());
	}
	
	@Test
	public void leftTest() {
		insertTest1();
		c.goLeft();
		assertTrue(c.atStart());
		assertEquals('a', c.peekAfter());
		assertFalse(c.atEnd());
	}
	
	@Test
	public void rightTest() {
		leftTest();
		c.goRight();
		assertTrue(c.atEnd());
		assertEquals('a', c.peekBefore());
		assertFalse(c.atStart());
	}
	
	@Test
	public void backupTest() {
		insertTest2();
		c.goLeft();
		assertTrue(c.isConsistent());
		assertEquals('a', c.peekBefore());
		assertEquals('b', c.peekAfter());
		assertFalse(c.atEnd());
		assertFalse(c.atStart());
	}
	
	@Test
	public void keepBackingUp() {
		backupTest();
		c.goLeft();
		backedUp();
		c.goLeft();
		backedUp();
	}
	
	void backedUp() {
		assertTrue(c.isConsistent());
		assertFalse(c.atEnd());
		assertTrue(c.atStart());
		assertFalse(c.isEmpty());
		assertEquals('a', c.peekAfter());
	}
	
	@Test
	public void forwarding() {
		backupTest();
		c.goRight();
		forwarded();
		c.goRight();
		forwarded();
	}
	
	void forwarded() {
		assertTrue(c.isConsistent());
		assertTrue(c.atEnd());
		assertFalse(c.atStart());
		assertEquals('b', c.peekBefore());
	}

	@Test
	public void inBetweenTest() {
		backupTest();
		c.insert('c');
		assertTrue(c.isConsistent());
		assertEquals('c', c.peekBefore());
		assertEquals('b', c.peekAfter());
	}
	
	@Test
	public void testDelete() {
		backupTest();
		c.removeAfter();
		afterDelete();
	}
	
	void afterDelete() {
		assertTrue(c.isConsistent());
		assertTrue(c.atEnd());
		assertEquals('a', c.peekBefore());
	}
	
	@Test
	public void testEmptyDelete() {
		testDelete();
		c.removeAfter();
		afterDelete();
	}
	
	@Test
	public void testBackspace() {
		backupTest();
		c.removeBefore();
		afterBackspace();
	}
	
	void afterBackspace() {
		assertTrue(c.isConsistent());
		assertTrue(c.atStart());
		assertEquals('b', c.peekAfter());
	}
	
	@Test
	public void testEmptyBackspace() {
		testBackspace();
		c.removeBefore();
		afterBackspace();
	}
	
	@Test 
	public void testBackspaceEnd() {
		insertTest2();
		c.removeBefore();
		assertTrue(c.isConsistent());
		assertEquals('a', c.peekBefore());
		assertTrue(c.atEnd());
		assertFalse(c.atStart());
	}
	
	@Test
	public void testDeleteStart() {
		insertTest2();
		while (!c.atStart()) {
			char prev = c.peekBefore();
			c.goLeft();
			assertEquals(prev, c.peekAfter());
		}
		c.removeAfter();
		assertTrue(c.isConsistent());
		assertTrue(c.atStart());
		assertFalse(c.atEnd());
		assertEquals('b', c.peekAfter());
	}
	
	@Test
	public void deleteToNothing() {
		insertTest1();
		c.goLeft();
		assertTrue(c.isConsistent());
		c.removeAfter();
		atNothing();
	}
	
	void atNothing() {
		assertTrue(c.isConsistent());
		assertTrue(c.atStart());
		assertTrue(c.atEnd());
	}
	
	@Test
	public void backspaceToNothing() {
		insertTest1();
		c.removeBefore();
		atNothing();
	}
	
	@Test
	public void testCopyContent() {
		biggerTestLineStart();
		d = c.copyContent();
		assertTrue(c.isConsistent());
		checkCopy();
	}
	
	@Test
	public void testCopyContentFromEnd() {
		biggerTest();
		d = c.copyContent();
		checkCopy();
	}
	
	@Test
	public void testCopyContentFromStart() {
		biggerTest();
		while (!c.atStart()) {c.goLeft();}
		d = c.copyContent();
		checkCopy();
	}
	
	void checkCopy() {
		if (!c.atStart()) assertEquals(c.peekBefore(), d.peekBefore());
		if (!c.atEnd()) assertEquals(c.peekAfter(), d.peekAfter());		
		assertEquals(c.getAllChars(), d.getAllChars());
	}
	
	@Test
	public void testCopyContentInsertion() {
		testCopyContent();
		d.insert('*');
		assertTrue(c.isConsistent());
		assertTrue(d.isConsistent());
		assertNotEquals(c.peekBefore(), d.peekBefore());
		assertEquals(c.peekAfter(), d.peekAfter());
	}
	
	@Test
	public void testCopyPositionSetup() {
		biggerTestLineStart();
		d = c.copyPosition();
		checkCopy();
	}
	
	final String bigger = "This is a longer test.\nWe're in the second line!\nEncountering the third line of the test?";
	final String replacement = "Replaced";
	
	@Test
	public void biggerTest() {
		c.replaceWith(bigger);
		assertTrue(c.atEnd());
		assertEquals(bigger, c.getAllChars());
	}
	
	@Test
	public void previousLineTest() {
		biggerTest();
		assertEquals(bigger.substring(bigger.lastIndexOf('\n') + 1), c.getPreviousLine());
		for (int i = 0; i < 4; i++) {
			c.goLeft();
		}
		assertEquals(bigger.substring(bigger.lastIndexOf('\n') + 1, bigger.length() - 4), c.getPreviousLine());
		while (c.peekBefore() != '.') {
			c.goLeft();
		}
		assertEquals(bigger.substring(0, bigger.indexOf('.') + 1), c.getPreviousLine());
	}
	
	public void biggerTestLineStart() {
		biggerTest();
		while (c.peekBefore() != '\n') {
			c.goLeft();
		}
	}
	
	@Test
	public void removeAllTest() {
		biggerTest();
		c.removeAll();
		allGone();
	}
	
	public void allGone() {
		assertTrue(c.atStart());
		assertTrue(c.atEnd());
		assertTrue(c.isEmpty());
		assertEquals("", c.getAllChars());
	}
	
	@Test
	public void simpleRemoveAllTest() {
		insertTest2();
		c.removeAll();
		assertTrue(c.atStart());
		assertTrue(c.atEnd());
		assertTrue(c.isEmpty());
	}
	
	@Test
	public void replaceTest() {
		biggerTest();
		c.replaceWith(replacement);
		assertEquals(replacement, c.getAllChars());
		assertTrue(c.atEnd());
		assertFalse(c.atStart());
	}
}
