package assign09;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


/**
 * The testing class for the HashTable class. 
 * 
 * @author Kira Halls and Nandini Goel
 *
 */
class HashTableTest {
	private HashTable<Integer, Integer> emptyIntTable, smallIntTable, mediumIntTable;
	private HashTable<String, String> emptyStringTable, smallStringTable, mediumStringTable;
	private ArrayList<String> smallTableKeys, smallTableValues;
	
	@BeforeEach 
	void setUp() {
		emptyIntTable = new HashTable<Integer, Integer>();
		
		smallIntTable = new HashTable<Integer, Integer>();
		for (int i = 0; i < 10; i++) {
			smallIntTable.put(i * 2, i);
		}
		
		mediumIntTable = new HashTable<Integer, Integer>();
		for (int i = 0; i < 25; i++) {
			mediumIntTable.put(i * 2, i);
		}
		
		emptyStringTable = new HashTable<String, String>();
		
		smallStringTable = new HashTable<String, String>();
		smallStringTable.put("car", "ferrari");
		smallStringTable.put("snack", "chips");
		smallStringTable.put("drink", "water");
		smallStringTable.put("fruit", "banana");
		smallStringTable.put("vegetable", "carrot");
		smallStringTable.put("soup", "tomato");
		smallStringTable.put("dog", "maltese");
		smallStringTable.put("fish", "goldfish");
		smallStringTable.put("cat", "siamese");
		smallStringTable.put("flower", "rose");
		smallTableKeys = new ArrayList<String>();
		smallTableKeys.add("car");
		smallTableKeys.add("snack");
		smallTableKeys.add("drink");
		smallTableKeys.add("fruit");
		smallTableKeys.add("vegetable");
		smallTableKeys.add("soup");
		smallTableKeys.add("dog");
		smallTableKeys.add("fish");
		smallTableKeys.add("cat");
		smallTableKeys.add("flower");
		smallTableValues = new ArrayList<String>();
		smallTableValues.add("ferrari");
		smallTableValues.add("chips");
		smallTableValues.add("water");
		smallTableValues.add("banana");
		smallTableValues.add("carrot");
		smallTableValues.add("tomato");
		smallTableValues.add("maltese");
		smallTableValues.add("goldfish");
		smallTableValues.add("siamese");
		smallTableValues.add("rose");
		
		mediumStringTable = new HashTable<String, String>();
		for(int i = 0; i < 26; i++) {
			String letter = (char)('A' + i % 26) + "";
			String letter2 = (char)('a' + (i) % 26) + "";
			String key = letter + letter2;
			mediumStringTable.put(key, key + "test");
		}
	}
	
	/** Begin Integer HashMap Tests **/
	
	@Test
	void testClearEmpty() {
		assertEquals(0, emptyIntTable.size());
		emptyIntTable.clear();
		assertEquals(0, emptyIntTable.size());
		assertTrue(emptyIntTable.isEmpty());
	}
	
	@Test
	void testClearSmall() {
		assertEquals(10, smallIntTable.size());
		smallIntTable.clear();
		assertEquals(0, smallIntTable.size());
		assertTrue(smallIntTable.isEmpty());
	}
	
	@Test
	void testClearMedium() {
		assertEquals(25, mediumIntTable.size());
		mediumIntTable.clear();
		assertEquals(0, mediumIntTable.size());
		assertTrue(mediumIntTable.isEmpty());
	}
	
	@Test
	void testContainsKeyEmpty() {
		assertFalse(emptyIntTable.containsKey(6));
	}
	
	@Test
	void testContainsKeySmall() {
		for (int i = 0; i < 10; i++) {
			assertTrue(smallIntTable.containsKey(i * 2));
		}
		assertFalse(smallIntTable.containsKey(45));
	}
	
	@Test
	void testContainsKeyMedium() {
		for (int i = 0; i < 25; i++) {
			assertTrue(mediumIntTable.containsKey(i * 2));
		}
		assertFalse(mediumIntTable.containsKey(45));
	}
	
	@Test
	void testContainsValueEmpty() {
		assertFalse(emptyIntTable.containsValue(6));
	}
	
	@Test
	void testContainsValueSmall() {
		for (int i = 0; i < 10; i++) {
			assertTrue(smallIntTable.containsValue(i));
		}
		assertFalse(smallIntTable.containsValue(45));
	}
	
	@Test
	void testContainsValueMedium() {
		for (int i = 0; i < 25; i++) {
			assertTrue(mediumIntTable.containsValue(i));
		}
		assertFalse(mediumIntTable.containsValue(45));
	}
	
	@Test
	void testEntriesEmpty() {
		ArrayList<Integer> expectedArrayList = new ArrayList<Integer>();
		assertEquals(expectedArrayList.toString(), emptyIntTable.entries().toString());
	}
	
	@Test
	void testEntriesSmall() {
		for(int i = 0; i < 10; i++) {
			assertTrue(smallIntTable.entries().contains(new MapEntry<Integer, Integer> (i*2, i)));
		}
	}
	
	@Test
	void testEntriesMedium() {
		for(int i = 0; i < 25; i++) {
			assertTrue(mediumIntTable.entries().contains(new MapEntry<Integer, Integer> (i*2, i)));
		}
	}
	
	@Test
	void testGetEmpty() {
		for (int i = 0; i < 10; i++) {
			assertNull(emptyIntTable.get(i * 2));
		}
	}
	
	@Test
	void testGetSmall() {
		for (int i = 0; i < 10; i++) {
			assertEquals(i , smallIntTable.get(i * 2));
		}
	}
	
	@Test
	void testGetMedium() {
		for (int i = 0; i < 25; i++) {
			assertEquals(i , mediumIntTable.get(i * 2));
		}
	}
	
	@Test
	void testIsEmptyEmpty() {
		assertTrue(emptyIntTable.isEmpty());
		emptyIntTable.put(1, 1);
		assertFalse(emptyIntTable.isEmpty());
	}
	
	@Test
	void testIsEmptySmall() {
		assertFalse(smallIntTable.isEmpty());
		smallIntTable.clear();
		assertTrue(smallIntTable.isEmpty());
	}
	
	@Test
	void testIsEmptyMedium() {
		assertFalse(mediumIntTable.isEmpty());
		mediumIntTable.clear();
		assertTrue(mediumIntTable.isEmpty());
	}
	
	@Test
	void testPutOnEmpty() {
		assertFalse(emptyIntTable.containsKey(243));
		emptyIntTable.put(243, 7);
		assertTrue(emptyIntTable.entries().contains(new MapEntry<Integer, Integer> (243, 7)));
		assertEquals(1, emptyIntTable.size());
	}
	
	@Test
	void testPutOnSmall() {
		assertFalse(smallIntTable.containsKey(42));
		smallIntTable.put(42, 44);
		assertTrue(smallIntTable.entries().contains(new MapEntry<Integer, Integer> (42, 44)));
		
		for(int i = 10; i < 20; i++) {
			smallIntTable.put(i*2, i);
			assertTrue(smallIntTable.entries().contains(new MapEntry<Integer, Integer> (i*2, i)));
		}
		assertEquals(21, smallIntTable.size());
	}
	
	@Test
	void testPutOnMedium() {
		assertFalse(mediumIntTable.containsKey(112));
		mediumIntTable.put(112, 88);
		assertTrue(mediumIntTable.entries().contains(new MapEntry<Integer, Integer> (112, 88)));
		
		for(int i = 25; i < 50; i++) {
			mediumIntTable.put(i*2, i);
			assertTrue(mediumIntTable.entries().contains(new MapEntry<Integer, Integer> (i*2, i)));
		}
		assertEquals(51, mediumIntTable.size());
	}
	
	@Test
	void testRemoveOnEmpty() {
		assertNull(emptyIntTable.remove(999));
		assertEquals(0, emptyIntTable.size());
	}
	
	@Test
	void testRemoveOnSmall() {
		assertTrue(smallIntTable.containsKey(18));
		assertEquals(9, smallIntTable.remove(18));
		assertFalse(smallIntTable.entries().contains(new MapEntry<Integer, Integer> (18, 9)));
		assertNull(smallIntTable.remove(18));
		
		for(int i = 0; i < 9; i++) {
			assertEquals(i, smallIntTable.remove(i*2));
			assertFalse(smallIntTable.entries().contains(new MapEntry<Integer, Integer> (i*2, i)));
		}
		assertEquals(0, smallIntTable.size());
	}
	
	@Test
	void testRemoveOnMedium() {
		assertTrue(mediumIntTable.containsKey(48));
		assertEquals(24, mediumIntTable.remove(48));
		assertFalse(mediumIntTable.entries().contains(new MapEntry<Integer, Integer> (48, 24)));
		assertNull(mediumIntTable.remove(48));
		
		for(int i = 0; i < 24; i++) {
			assertEquals(i, mediumIntTable.remove(i*2));
			assertFalse(mediumIntTable.entries().contains(new MapEntry<Integer, Integer> (i*2, i)));
		}
		assertEquals(0, mediumIntTable.size());
	}
	
	/** Begin String HashMap Tests **/
	
	@Test
	void testClearEmptyString() {
		assertEquals(0, emptyStringTable.size());
		emptyStringTable.clear();
		assertEquals(0, emptyStringTable.size());
		assertTrue(emptyStringTable.isEmpty());
	}
	
	@Test
	void testClearSmallString() {
		assertEquals(10, smallStringTable.size());
		smallStringTable.clear();
		assertEquals(0, smallStringTable.size());
		assertTrue(smallStringTable.isEmpty());
	}
	
	@Test
	void testClearMediumString() {
		assertEquals(26, mediumStringTable.size());
		mediumStringTable.clear();
		assertEquals(0, mediumStringTable.size());
		assertTrue(mediumStringTable.isEmpty());
	}
	
	@Test
	void testContainsKeyEmptyString() {
		assertFalse(emptyStringTable.containsKey("apple"));
	}
	
	@Test
	void testContainsKeySmallString() {
		for (String key : smallTableKeys) {
			assertTrue(smallStringTable.containsKey(key));
		}
		assertFalse(smallStringTable.containsKey("apple"));
	}
	
	@Test
	void testContainsKeyMediumString() {
		for(int i = 0; i < 26; i++) {
			String letter = (char)('A' + i % 26) + "";
			String letter2 = (char)('a' + (i) % 26) + "";
			String key = letter + letter2;
			assertTrue(mediumStringTable.containsKey(key));
		}
		assertFalse(mediumStringTable.containsKey("apple"));	
	}
	
	@Test
	void testContainsValueEmptyString() {
		assertFalse(emptyStringTable.containsValue("apple"));
	}
	
	@Test
	void testContainsValueSmallString() {
		for (String value : smallTableValues) {
			assertTrue(smallStringTable.containsValue(value));
		}
		assertFalse(smallStringTable.containsValue("apple"));
	}
	
	@Test
	void testContainsValueMediumString() {
		for(int i = 0; i < 26; i++) {
			String letter = (char)('A' + i % 26) + "";
			String letter2 = (char)('a' + (i) % 26) + "";
			String key = letter + letter2;
			assertTrue(mediumStringTable.containsValue(key + "test"));
		}
		assertFalse(mediumStringTable.containsValue("apple"));
	}
	
	@Test
	void testEntriesEmptyString() {
		ArrayList<String> expectedArrayList = new ArrayList<String>();
		assertEquals(expectedArrayList.toString(), emptyStringTable.entries().toString());
	}
	
	@Test
	void testEntriesSmallString() {
		ArrayList<MapEntry<String, String>> expectedList = new ArrayList<MapEntry<String, String>>();
		expectedList.add(new MapEntry<String, String> ("car", "ferrari"));
		expectedList.add(new MapEntry<String, String> ("snack", "chips"));
		expectedList.add(new MapEntry<String, String> ("drink", "water"));
		expectedList.add(new MapEntry<String, String> ("fruit", "banana"));
		expectedList.add(new MapEntry<String, String> ("vegetable", "carrot"));
		expectedList.add(new MapEntry<String, String> ("soup", "tomato"));
		expectedList.add(new MapEntry<String, String> ("dog", "maltese"));
		expectedList.add(new MapEntry<String, String> ("fish", "goldfish"));
		expectedList.add(new MapEntry<String, String> ("cat", "siamese"));
		expectedList.add(new MapEntry<String, String> ("flower", "rose"));
		for (MapEntry<String, String> entry : expectedList) {
			assertTrue(smallStringTable.entries().contains(entry));
		}
	}
	
	@Test
	void testEntriesMediumString() {
		ArrayList<MapEntry<String, String>> expectedList = new ArrayList<MapEntry<String, String>>();
		for(int i = 0; i < 26; i++) {
			String letter = (char)('A' + i % 26) + "";
			String letter2 = (char)('a' + (i) % 26) + "";
			String key = letter + letter2;
			expectedList.add(new MapEntry<String, String> (key, key + "test"));
		}
		for (MapEntry<String, String> entry : expectedList) {
			assertTrue(mediumStringTable.entries().contains(entry));
		}
	}
	
	@Test
	void testGetEmptyString() {
		assertNull(emptyStringTable.get("apple"));
	}
	
	@Test
	void testGetSmallString() {
		ArrayList<MapEntry<String, String>> expectedList = new ArrayList<MapEntry<String, String>>();
		expectedList.add(new MapEntry<String, String> ("car", "ferrari"));
		expectedList.add(new MapEntry<String, String> ("snack", "chips"));
		expectedList.add(new MapEntry<String, String> ("drink", "water"));
		expectedList.add(new MapEntry<String, String> ("fruit", "banana"));
		expectedList.add(new MapEntry<String, String> ("vegetable", "carrot"));
		expectedList.add(new MapEntry<String, String> ("soup", "tomato"));
		expectedList.add(new MapEntry<String, String> ("dog", "maltese"));
		expectedList.add(new MapEntry<String, String> ("fish", "goldfish"));
		expectedList.add(new MapEntry<String, String> ("cat", "siamese"));
		expectedList.add(new MapEntry<String, String> ("flower", "rose"));
		for (MapEntry<String, String> entry : expectedList) {
			assertEquals(entry.getValue(),smallStringTable.get(entry.getKey()));
		}
	}
	
	@Test
	void testGetMediumString() {
		ArrayList<MapEntry<String, String>> expectedList = new ArrayList<MapEntry<String, String>>();
		for(int i = 0; i < 26; i++) {
			String letter = (char)('A' + i % 26) + "";
			String letter2 = (char)('a' + (i) % 26) + "";
			String key = letter + letter2;
			expectedList.add(new MapEntry<String, String> (key, key + "test"));
		}
		for (MapEntry<String, String> entry : expectedList) {
			assertEquals(entry.getValue(), mediumStringTable.get(entry.getKey()));
		}
	}
	
	@Test
	void testIsEmptyEmptyString() {
		assertTrue(emptyStringTable.isEmpty());
		emptyStringTable.put("apple", "is a fruit");
		assertFalse(emptyStringTable.isEmpty());
	}
	
	@Test
	void testIsEmptySmallString() {
		assertFalse(smallStringTable.isEmpty());
		smallStringTable.clear();
		assertTrue(smallStringTable.isEmpty());
	}
	
	@Test
	void testIsEmptyMediumString() {
		assertFalse(mediumStringTable.isEmpty());
		mediumStringTable.clear();
		assertTrue(mediumStringTable.isEmpty());
	}
	
	@Test
	void testPutOnEmptyString() {
		assertFalse(emptyStringTable.containsKey("apple"));
		emptyStringTable.put("apple", "is a fruit");
		assertTrue(emptyStringTable.entries().contains(new MapEntry<String, String> ("apple", "is a fruit")));
		assertEquals(1, emptyStringTable.size());
	}
	
	@Test
	void testPutOnSmallString() {
		assertFalse(smallStringTable.containsKey("jar"));
		smallStringTable.put("jar", "jam");
		assertTrue(smallStringTable.entries().contains(new MapEntry<String, String> ("jar", "jam")));
		assertEquals(11, smallStringTable.size());
	}
	
	@Test
	void testPutOnMediumString() {
		assertFalse(mediumStringTable.containsKey("Af"));
		mediumStringTable.put("Af", "yes");
		assertTrue(mediumStringTable.entries().contains(new MapEntry<String, String> ("Af", "yes")));
		assertEquals(27, mediumStringTable.size());
	}
	
	@Test
	void testRemoveOnEmptyString() {
		assertNull(emptyStringTable.remove("word"));
		assertEquals(0, emptyStringTable.size());
	}
	
	@Test
	void testRemoveOnSmallString() {
		assertTrue(smallStringTable.containsKey("flower"));
		assertEquals("rose", smallStringTable.remove("flower"));
		assertFalse(smallStringTable.entries().contains(new MapEntry<String, String> ("flower", "rose")));
		assertNull(smallStringTable.remove("flower"));
	}
	
	@Test
	void testRemoveOnMediumString() {

		for(int i = 0; i < 26; i++) {
			String letter = (char)('A' + i % 26) + "";
			String letter2 = (char)('a' + (i) % 26) + "";
			String key = letter + letter2;
			assertTrue(mediumStringTable.entries().contains(new MapEntry<String, String> (key, key + "test")));
			assertEquals(key + "test", mediumStringTable.remove(key));
			assertFalse(mediumStringTable.entries().contains(new MapEntry<String, String> (key, key + "test")));
		}
		assertEquals(0, mediumStringTable.size());
	}
}
