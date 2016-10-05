package seedu.address.model.task;

/*
 * Represents an immutable description for a Task.
 */
public class Description {
	private final String content;
	
	public Description() {
		this.content = "";
	}
	
	public Description(String content) {
		this.content = content;
	}

	public String getContent() {
		return content;
	}
	
	@Override
	public String toString() {
		return getContent();
	}
}
