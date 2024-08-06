package assign09;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * This class represents a hash table using separate chaining to solve collisions. Provides methods to add, remove, clear, etc.
 * 
 * @author Kira Halls and Nandini Goel
 *
 * @param <K> The Key value for the HashTable
 * @param <V> The Value for the associated key in the Hashtable
 */
public class HashTable<K, V> implements Map<K, V> {
	private ArrayList<LinkedList<MapEntry<K, V>>> table;
	private int capacity = 10;
	private int size = 0;
	
	/**
	 * Constructor to create a new Hashtable. Assigns a capacity of 10 and creates a new table for use.
	 */
	public HashTable() {
		table = new ArrayList<LinkedList<MapEntry<K, V>>>();
		for(int i = 0; i < capacity; i++)
		   table.add(new LinkedList<MapEntry<K, V>>());
	}

	/**
	 * Removes all mappings from this map.
	 * 
	 * O(table length)
	 */
	@Override
	public void clear() {
		for (LinkedList<MapEntry<K, V>> list : table) {
			list.clear();
		}
		size = 0;
		capacity = 10;
	}

	/**
	 * Determines whether this map contains the specified key.
	 * 
	 * O(1)
	 * 
	 * @param key
	 * @return true if this map contains the key, false otherwise
	 */
	@Override
	public boolean containsKey(K key) {
		Integer h = key.hashCode();
		if (h == Integer.MIN_VALUE) {
			h++;
		}
		int index = Math.abs(h) % capacity;
		if (index > size) {
			return false;
		}
		for (MapEntry<K, V> entry : table.get(index)) {
			if (entry.getKey().equals(key)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Determines whether this map contains the specified value.
	 * 
	 * O(table length)
	 * 
	 * @param value
	 * @return true if this map contains one or more keys to the specified value,
	 *         false otherwise
	 */
	@Override
	public boolean containsValue(V value) {
		for (LinkedList<MapEntry<K, V>> list : table) {
			if (!(list.isEmpty())) {
				for (MapEntry<K, V> entry : list) {
					if (entry.getValue().equals(value)) {
						return true;
					}
				}
			}	
		}
		return false;
	}

	/**
	 * Returns a List view of the mappings contained in this map, where the ordering of 
	 * mapping in the list is insignificant.
	 * 
	 * O(table length)
	 * 
	 * @return a List object containing all mapping (i.e., entries) in this map
	 */
	@Override
	public List<MapEntry<K, V>> entries() {
		List<MapEntry<K, V>> list = new ArrayList<MapEntry<K, V>>();
		for (LinkedList<MapEntry<K, V>> linkedList : table) {
			if (!(linkedList.isEmpty())) {
				list.addAll(linkedList);
			}	
		}
		return list;
	}

	/**
	 * Gets the value to which the specified key is mapped.
	 * 
	 * O(1)
	 * 
	 * @param key
	 * @return the value to which the specified key is mapped, or null if this map
	 *         contains no mapping for the key
	 */
	@Override
	public V get(K key) {
		Integer h = key.hashCode();
		if (h == Integer.MIN_VALUE) {
			h++;
		}
		int index = Math.abs(h) % capacity;
		for (MapEntry<K, V> entry : table.get(index)) {
			if (entry.getKey().equals(key)) {
				return entry.getValue();
			}
		}
		return null;
	}

	/**
	 * Determines whether this map contains any mappings.
	 * 
	 * O(1)
	 * 
	 * @return true if this map contains no mappings, false otherwise
	 */
	@Override
	public boolean isEmpty() {
		for (LinkedList<MapEntry<K, V>> list : table) {
			if (!list.isEmpty()) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Associates the specified value with the specified key in this map.
	 * (I.e., if the key already exists in this map, resets the value; 
	 * otherwise adds the specified key-value pair.)
	 * 
	 * O(1)
	 * 
	 * @param key
	 * @param value
	 * @return the previous value associated with key, or null if there was no
	 *         mapping for key
	 */
	@Override
	public V put(K key, V value) {
		Integer h = key.hashCode();
		if (h == Integer.MIN_VALUE) {
			h++;
		}
		int index = Math.abs(h) % capacity;
		for (MapEntry<K, V> entry : table.get(index)) {
			if (entry.getKey().equals(key)) {
				V prevValue = entry.getValue();
				entry.setValue(value);
				return prevValue;
			}
		}
		
		// rehash if necessary
		if (this.size != 0) {
			double loadFactor = index / size;
			if(loadFactor >= 10.0) {
				rehash();
			}
		}
		table.get(index).add(new MapEntry<K, V> (key, value));
		size++;
		return null;
	}
	
	/**
	 * This private helper method rehashes a specified hashtable if its load factor exceeds 10
	 */
	private void rehash() {
		this.capacity = capacity * 2;
		ArrayList<LinkedList<MapEntry<K, V>>> doubledTable = new ArrayList<>(capacity);
		for(int i = 0; i < capacity; i++) {
			doubledTable.add(new LinkedList<MapEntry<K, V>> ());
		}
		List<MapEntry<K, V>> entries = entries();
		table = doubledTable;
	
		for(MapEntry<K, V> entry : entries) {
			table.get(entry.getKey().hashCode()).add(new MapEntry<K, V> (entry.getKey(), entry.getValue()));
		}
	}

	/**
	 * Removes the mapping for a key from this map if it is present.
	 * 
	 * O(1)
	 *
	 * @param key
	 * @return the previous value associated with key, or null if there was no
	 *         mapping for key
	 */
	@Override
	public V remove(K key) {
		Integer h = key.hashCode();
		if (h == Integer.MIN_VALUE) {
			h++;
		}
		int index = Math.abs(h) % capacity;
		for (MapEntry<K, V> entry : table.get(index)) {
			if (entry.getKey().equals(key)) {
				V prevValue = entry.getValue();
				table.get(index).remove(entry);
				size--;
				entry.setValue(null);
				return prevValue;
			}
		}
		return null;
	}

	/**
	 * Determines the number of mappings in this map.
	 * 
	 * O(1)
	 * 
	 * @return the number of mappings in this map
	 */
	@Override
	public int size() {
		return size;
	}
}
