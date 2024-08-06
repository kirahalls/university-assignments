package assign02;

import java.util.GregorianCalendar;

public class LibraryBookGeneric<Type> extends Book{
	private Type holder; 
	private GregorianCalendar dueDate;
	
	public LibraryBookGeneric(long isbn, String author, String title) {
		super(isbn, author, title);
	}
	
	/**
	 * Get holder of checked out book
	 * @return - Holder of checked out book
	 */
	public Type getHolder() {
		return this.holder;
	}
	
	/**
	 * Get due date of checked out book
	 * @return - Due date of checked out book
	 */
	public GregorianCalendar getDueDate() {
		return this.dueDate;
	}

	/**
	 * Checking in book and setting holder and due date back to null
	 */
	public void checkIn() {
		this.holder = null;
		this.dueDate = null;
	}
	
	/**
	 * Setting holder and due date for a book being checked out
	 * @param holder - person checking out book
	 * @param dueDate - Date book is due
	 */
	public void checkOut(Type holder, GregorianCalendar dueDate) {
		this.holder = holder;
		this.dueDate = dueDate;
	}
	
	/**
	 * Checking if the book is checked out
	 * @return - true if book is checked out, false if not
	 */
	public boolean isCheckedOut() {
		if (this.holder == null) {
			return false;
		}
		return true;
	}

}
