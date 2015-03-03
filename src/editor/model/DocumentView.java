package editor.model;

public interface DocumentView {
	public void insert(char c);
	
	public void removeAfter();
	public void removeBefore();
	public void removeAll();
	
	public boolean atStart();
	public boolean atEnd();
	public boolean isEmpty();
	
	public void goRight();
	public void goLeft();
	
	public char peekAfter();
	public char peekBefore();
	
	public DocumentView copyPosition();
	public DocumentView copyContent();
	
	public boolean isConsistent();
	
	default public void replaceWith(String src) {
		removeAll();
		for (int i = 0; i < src.length(); i++) {
			insert(src.charAt(i));
		}
	}
	
	default public String getAllChars() {
		StringBuilder result = new StringBuilder();
		DocumentView cursor = copyPosition();
		while (!cursor.atStart()) {
			cursor.goLeft();
		}
		while (!cursor.atEnd()) {
			result.append(cursor.peekAfter());
			cursor.goRight();
		}
		return result.toString();
	}
	
	default public String getPreviousLine() {
		StringBuilder result = new StringBuilder();
		for (DocumentView cursor = copyPosition(); 
			 !cursor.atStart() && cursor.peekBefore() != '\n'; 
			 cursor.goLeft()) {
			result.append(cursor.peekBefore());
		}
		return result.reverse().toString();
	}
}
