package assign09;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * This is the test class for the StudentBadHash class
 * 
 * @author Kira Halls and Nandini Goel
 *
 */
class StudentBadHashTest {
	StudentBadHash alan, ada, edsger, grace;
	HashTable<StudentBadHash, Double> studentGpaTable;
	
	@BeforeEach
	void setUp() {
		alan = new StudentBadHash(1019999, "Alan", "Turing");
		ada = new StudentBadHash(1004203, "Ada", "Lovelace");
		edsger = new StudentBadHash(1010661, "Edsger", "Dijkstra");
		grace = new StudentBadHash(1019941, "Grace", "Hopper");
		
		//also serves as test to see if we can add values using the hashcode
		studentGpaTable = new HashTable<StudentBadHash, Double>();
		studentGpaTable.put(alan, 3.2);
		studentGpaTable.put(ada, 3.5);
		studentGpaTable.put(edsger, 3.8);
		studentGpaTable.put(grace, 4.0);
	}

	@Test
	void testHashOutput() {
		assertEquals(6, alan.hashCode());
		assertEquals(8, ada.hashCode());
		assertEquals(8, edsger.hashCode());
		assertEquals(6, grace.hashCode());
	}
	
	@Test
	void testGet() {
		assertEquals(3.2, studentGpaTable.get(alan));
		assertEquals(3.5, studentGpaTable.get(ada));
		assertEquals(3.8, studentGpaTable.get(edsger));
		assertEquals(4.0, studentGpaTable.get(grace));
	}

}
