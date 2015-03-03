package editor.model;

import static org.junit.Assert.*;

import java.util.ConcurrentModificationException;

import org.junit.Test;


public class DoubleTextNodeViewTest extends DocumentViewTest {
	
	@Override
	DocumentView makeView() {
		return new DoubleTextNodeView();
	}

	@Test(expected=ConcurrentModificationException.class)
	public void testCopyPosition1() {
		testCopyPositionSetup();
		d.insert('*');
		assertTrue(d.isConsistent());
		assertFalse(c.isConsistent());
		c.goLeft();
	}
	
	@Test(expected=ConcurrentModificationException.class) 
	public void testCopyPosition2() {
		testCopyPositionSetup();
		d.removeAfter();
		assertTrue(d.isConsistent());
		assertFalse(c.isConsistent());
		c.goRight();
	}
}
