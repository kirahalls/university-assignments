package assign02;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.GregorianCalendar;
import java.util.Scanner;

/**
 * This class represents a library, which is a collection of library books.
 * 
 * @author Erin Parker and Braden Campbell and Kira Halls
 * @version September 2, 2020
 */
public class LibraryGeneric<Type> {

	private ArrayList<LibraryBookGeneric<Type>> libraryGeneric;

	/**
	 * Creates an empty library.
	 */
	public LibraryGeneric() {
		libraryGeneric = new ArrayList<LibraryBookGeneric<Type>>();
	}

	/**
	 * Adds to the library the book, specified by the given ISBN, author, and
	 * title. Assumes there is no possibility of duplicate library books.
	 * 
	 * @param isbn - ISBN of the book to be added
	 * @param author - author of the book to be added
	 * @param title - title of the book to be added
	 */
	public void add(long isbn, String author, String title) {
		libraryGeneric.add(new LibraryBookGeneric<Type>(isbn, author, title));
	}

	/**
	 * Add the list of library books to the library. Assumes there is no
	 * possibility of duplicate library books.
	 * 
	 * @param list - list of library books to be added
	 */
	public void addAll(ArrayList<LibraryBookGeneric<Type>> list) {
		libraryGeneric.addAll(list);
	}

	/**
	 * Adds the the library the books specified by the input file. Assumes the
	 * input files specifies one book per line with ISBN, author, and title
	 * separated by tabs.
	 * 
	 * If file does not exist or format is violated, prints an error message and
	 * does not change the library.
	 * 
	 * @param filename
	 */
	public void addAll(String filename) {
		ArrayList<LibraryBookGeneric<Type>> toBeAdded = new ArrayList<LibraryBookGeneric<Type>>();

		try {
			Scanner fileIn = new Scanner(new File(filename));
			int lineNum = 1;

			while(fileIn.hasNextLine()) {
				String line = fileIn.nextLine();

				Scanner lineIn = new Scanner(line);
				lineIn.useDelimiter("\\t");

				if(!lineIn.hasNextLong()) {
					lineIn.close();
					throw new ParseException("ISBN", lineNum);
				}
				long isbn = lineIn.nextLong();

				if(!lineIn.hasNext()) {
					lineIn.close();
					throw new ParseException("Author", lineNum);
				}
				String author = lineIn.next();

				if(!lineIn.hasNext()) {
					lineIn.close();
					throw new ParseException("Title", lineNum);
				}
				String title = lineIn.next();

				toBeAdded.add(new LibraryBookGeneric<Type>(isbn, author, title));

				lineNum++;
				lineIn.close();
			}
			fileIn.close();
		}
		catch(FileNotFoundException e) {
			System.err.println(e.getMessage() + " Nothing added to the library.");
			return;
		}
		catch(ParseException e) {
			System.err.println(e.getLocalizedMessage() + " formatted incorrectly at line " + e.getErrorOffset()
					+ ". Nothing added to the library.");
			return;
		}

		libraryGeneric.addAll(toBeAdded);
	}

	/**
	 * Returns the holder of the library book with the specified ISBN.
	 * 
	 * If no book with the specified ISBN is in the library, returns null.
	 * 
	 * @param isbn - ISBN of the book to be looked up
	 */
	public Type lookup(long isbn) {
		for (LibraryBookGeneric<Type> book : this.libraryGeneric) {
			if (book.getIsbn() == isbn) {
				return book.getHolder();
			}
		}
		return null;
	}

	/**
	 * Returns the list of library books checked out to the specified holder.
	 * 
	 * If the specified holder has no books checked out, returns an empty list.
	 * 
	 * @param holder - holder whose checked out books are returned
	 */
	public ArrayList<LibraryBookGeneric<Type>> lookup(Type holder) {
		ArrayList<LibraryBookGeneric<Type>> currentHolds = new ArrayList<LibraryBookGeneric<Type>>();
		for (LibraryBookGeneric<Type> book : this.libraryGeneric) {
			if (book.getHolder() == holder) {
				currentHolds.add(book);
			}
		}
		return currentHolds;
	}

	/**
	 * Sets the holder and due date of the library book with the specified ISBN.
	 * 
	 * If no book with the specified ISBN is in the library, returns false.
	 * 
	 * If the book with the specified ISBN is already checked out, returns
	 * false.
	 * 
	 * Otherwise, returns true.
	 * 
	 * @param isbn - ISBN of the library book to be checked out
	 * @param holder - new holder of the library book
	 * @param month - month of the new due date of the library book
	 * @param day - day of the new due date of the library book
	 * @param year - year of the new due date of the library book
	 * 
	 */
	public boolean checkout(long isbn, Type holder, int month, int day, int year) {
		GregorianCalendar dueDate = new GregorianCalendar(year, month, day);
		for (LibraryBookGeneric<Type> book : this.libraryGeneric) {
			if (book.getIsbn() == isbn) {
				if (book.isCheckedOut()) {
					return false;
				}
				book.checkOut(holder, dueDate);
				return true;
			}
		}
		return false;
	}

	/**
	 * Unsets the holder and due date of the library book.
	 * 
	 * If no book with the specified ISBN is in the library, returns false.
	 * 
	 * If the book with the specified ISBN is already checked in, returns false.
	 * 
	 * Otherwise, returns true.
	 * 
	 * @param isbn - ISBN of the library book to be checked in
	 */
	public boolean checkin(long isbn) {
		for (LibraryBookGeneric<Type> book : this.libraryGeneric) {
			if (book.getIsbn() == isbn) {
				if (!(book.isCheckedOut())) { //if the book is NOT checked out (already in library) return false
					return false;
				}
				book.checkIn();
				return true;
			}
		}
		return false;
	}

	/**
	 * Unsets the holder and due date for all library books checked out be the
	 * specified holder.
	 * 
	 * If no books with the specified holder are in the library, returns false;
	 * 
	 * Otherwise, returns true.
	 * 
	 * @param holder - holder of the library books to be checked in
	 */
	public boolean checkin(Type holder) {
		ArrayList<LibraryBookGeneric<Type>> currentHolds = this.lookup(holder);
		if (currentHolds.isEmpty()) {
			return false;
		}
		for (LibraryBookGeneric<Type> book : currentHolds) {
			book.checkIn();
		}
		return true;
	}
	
	/**
	 * Returns the list of library books, sorted by ISBN (smallest ISBN first).
	 */
	public ArrayList<LibraryBookGeneric<Type>> getInventoryList() {
		ArrayList<LibraryBookGeneric<Type>> libraryCopy = new ArrayList<LibraryBookGeneric<Type>>();
		libraryCopy.addAll(libraryGeneric);

		OrderByIsbn comparator = new OrderByIsbn();

		sort(libraryCopy, comparator);

		return libraryCopy;
	}

	/**
	 * Returns the list of library books whose due date is older than the input
	 * date. The list is sorted by date (oldest first).
	 *
	 * If no library books are overdue, returns an empty list.
	 */
	public ArrayList<LibraryBookGeneric<Type>> getOverdueList(int month, int day, int year) {
		ArrayList<LibraryBookGeneric<Type>> overDueBooks = new ArrayList<LibraryBookGeneric<Type>>();
		ArrayList<LibraryBookGeneric<Type>> libraryCopy = this.getInventoryList();
		GregorianCalendar dueDate = new GregorianCalendar(year, month, day);
		for (LibraryBookGeneric<Type> book : libraryCopy) {
			if (book.getDueDate() != null) {
				if (book.getDueDate().compareTo(dueDate) < 0) {
					overDueBooks.add(book);
				}
			}
		}
		OrderByDueDate comparator = new OrderByDueDate();
		sort(overDueBooks, comparator);
		
		return overDueBooks;
	}

	/**
	 * Returns the list of library books, sorted by title
	 */
	public ArrayList<LibraryBookGeneric<Type>> getOrderedByTitle() {
		ArrayList<LibraryBookGeneric<Type>> orderedBooks = this.getInventoryList();
		OrderByTitle comparator = new OrderByTitle();
		sort(orderedBooks, comparator);
		
		return orderedBooks;
	}

	/**
	 * Performs a SELECTION SORT on the input ArrayList. 
	 * 
	 * 1. Finds the smallest item in the list. 
	 * 2. Swaps the smallest item with the first item in the list. 
	 * 3. Reconsiders the list be the remaining unsorted portion (second item to Nth item) and 
	 *    repeats steps 1, 2, and 3.
	 */
	private static <ListType> void sort(ArrayList<ListType> list, Comparator<ListType> c) {
		for(int i = 0; i < list.size() - 1; i++) {
			int j, minIndex;
			for(j = i + 1, minIndex = i; j < list.size(); j++)
				if(c.compare(list.get(j), list.get(minIndex)) < 0)
					minIndex = j;
			ListType temp = list.get(i);
			list.set(i, list.get(minIndex));
			list.set(minIndex, temp);
		}
	}

	/**
	 * Comparator that defines an ordering among library books using the ISBN.
	 */
	protected class OrderByIsbn implements Comparator<LibraryBookGeneric<Type>> {

		/**
		 * Returns a negative value if lhs is smaller than rhs. 
		 * Returns a positive value if lhs is larger than rhs. 
		 * Returns 0 if lhs and rhs are equal.
		 */
		public int compare(LibraryBookGeneric<Type> lhs, LibraryBookGeneric<Type> rhs) {
			return lhs.getIsbn() > rhs.getIsbn() ? 1 : (lhs.getIsbn() < rhs.getIsbn() ? -1 : 0);
		}
	}
	
	/**
	 * Comparator that defines an ordering among library books using the due
	 * date.
	 */
	protected class OrderByDueDate implements Comparator<LibraryBookGeneric<Type>> {
		
		/**
		 * Returns a negative value if book1 due date is before book2 due date
		 * Returns a positive value if book1 due date is after book2 due date
		 * Returns 0 if book1 due date and book2 due date are equal
		 */
		public int compare(LibraryBookGeneric<Type> book1 , LibraryBookGeneric<Type> book2) {
			return book1.getDueDate().compareTo(book2.getDueDate()) > 0 ? 1 : book1.getDueDate().compareTo(book2.getDueDate()) < 0 ? -1 : 0;
		}
		
	}
	
	/**
	 * Comparator that defines an ordering among library books using the book title
	 */
	protected class OrderByTitle implements Comparator<LibraryBookGeneric<Type>> {
		
		/**
		 * Returns a negative value if book1 title is before book2 title
		 * Returns a postive value if book1 title is after book2 title
		 * Returns a 0 if book1 is the same as book2 title
		 */
		public int compare(LibraryBookGeneric<Type> book1, LibraryBookGeneric<Type> book2) {
			return book1.getTitle().compareTo(book2.getTitle()) > 0 ? 1 : book1.getTitle().compareTo(book2.getTitle()) < 0 ? -1 : 0;
		}
	}
}