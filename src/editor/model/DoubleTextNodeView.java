package editor.model;

import java.util.ConcurrentModificationException;

public class DoubleTextNodeView implements DocumentView {
	private DoubleTextNode after, before;

	@Override
	public boolean isConsistent() {
		if (after == null && before.getNext() == null) {
			return true;
		}
		if (after.getPrev() == null && before == null) {
			return true;
		}
		if (isEmpty()) return true;
		if (after.getPrev() == before.getNext()) {
			return true;
		}
		return false;
		
	}

	public void assertConsistent() {
		if (!isConsistent()) {
			throw new ConcurrentModificationException();
		}
	}
	public DoubleTextNodeView() {
		this.after = null;
		this.before = null;
	}

	@Override
	public boolean atStart() {
		return before == null;
	}

	@Override
	public boolean atEnd() {
		return after == null;
	}

	@Override
	public boolean isEmpty() {
		return before == null && after == null;
	}

	@Override
	public char peekAfter() {
		if (!atEnd()){
			return after.getChar();
		}
		return '\0';
	}

	@Override
	public char peekBefore() {
		if (!atStart()){
			return before.getChar();
		}
		return '\0';
	}

	public void insert(char c) {
		if (atStart()) {
			before = new DoubleTextNode(c);
		} else {
			before.addAfter(new DoubleTextNode(c));
			before = before.getNext();
		}
		
	}

	@Override
	public void goLeft() {
		if (!atStart()) {
			before = before.getPrev();
			after = before;
		}
	}

	@Override
	public void goRight() {
		if (!atEnd()) {
			after = after.getNext();
			before = after;
		}
	}

	public void removeAfter() {
		
	}

	public void removeBefore() {
		if (!atStart()) {
			if (atEnd()) before = null;
		}
	}

	@Override
	public DoubleTextNodeView copyPosition() {
		DoubleTextNodeView cursor = new DoubleTextNodeView();
		return cursor;
	}

	@Override
	public DoubleTextNodeView copyContent() {
		return null;
	}

	@Override
	public void removeAll() {
		before = null;
		after = null;
	}
}
