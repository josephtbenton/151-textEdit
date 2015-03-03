package editor.model;

class DoubleTextNode {
	private char c;
	private DoubleTextNode next, prev;

	DoubleTextNode(char c) {
		this.c = c;
		this.next = null;
		this.prev = null;
	}

	DoubleTextNode getNext() {return next;}
	DoubleTextNode getPrev() {return prev;}
	char getChar() {return c;}

	void addAfter(DoubleTextNode other) {
		if (this.next == null) {
			this.next = other;
			other.prev = this;
		} else {
			DoubleTextNode neighbor = this.next;
			other.next = neighbor;
			other.prev = this;
			this.next = other;
			neighbor.prev = other;
		}
	}

	void addBefore(DoubleTextNode other) {
		if (this.prev == null) {
			this.prev = other;
			other.next = this;
		} else {
			DoubleTextNode neighbor = this.prev;
			other.prev = neighbor;
			other.next = this;
			this.prev = other;
			neighbor.next = other;
		}
	}

	void removeAfter() {
		if (this.next != null) {
			DoubleTextNode victim = this.next;
			if (victim.next == null) {
				this.next = null;
			} else {
				this.next = victim.next;
				victim.next.prev = this;
			}
		}
	}

	void removeBefore() {
		if (this.prev != null) {
			DoubleTextNode victim = this.prev;
			if (victim.prev == null) {
				this.prev = null;
			} else {
				this.prev = victim.prev;
				victim.prev.next = this;
			}
		}
	}

	void mergeAfter(DoubleTextNode other) {
		if (this.next == null) {
			other.prev = this;
			this.next = other;
		} else {
			DoubleTextNode end = other;
			while (end.next != null) {
				end = end.next;
			}
			this.next.prev = end;
			end.next = this.next;
			this.next = other;
			other.prev = this;
		}
	}

	void mergeBefore(DoubleTextNode other) {
		if (this.prev == null) {
			other.next = this;
			this.prev = other;
		} else {
			DoubleTextNode end = other;
			while (end.prev != null) {
				end = end.next;
			}
			this.prev.next = end;
			end.prev = this.prev;
			this.prev = other;
			other.next = this;
		}
	}

	public DoubleTextNode copyBefore() {
		DoubleTextNode res = new DoubleTextNode(this.c);
		if (this.prev == null) {
			return res;
		}
		else {
			DoubleTextNode copy = this.prev.copyBefore();
			copy.next = res;
			res.prev = copy;
			return res;
		}
	}

	public DoubleTextNode copyAfter() {
		DoubleTextNode res = new DoubleTextNode(this.c);
		if (this.next == null) {
			return res;
		}
		else {
			DoubleTextNode copy = this.next.copyAfter();
			copy.prev = res;
			res.next = copy;
			return res;
		}
	}
}