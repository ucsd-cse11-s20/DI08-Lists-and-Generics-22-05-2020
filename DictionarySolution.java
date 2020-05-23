import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;

import tester.*;

class Dictionary<K, V> {
	List<K> keys;
	List<V> values;

	Dictionary() {
		this.keys = new ArrayList<>();
		this.values = new ArrayList<>();
	}

	void put(K key, V value) {
		/* 
		Solution to Problem 3:
			this.keys.add(key);
			this.values.add(value);
		*/

		for (int i = 0; i < keys.size(); i++) {
			if (this.keys.get(i).equals(key)) {
				this.values.set(i, value);
				return;
			}
		}

		// New Key -> Add it to the end of the list
		this.keys.add(key);
		this.values.add(value);
	}

	V get(K key) {
		for (int i = 0; i < keys.size(); i++) {
			if (this.keys.get(i).equals(key)) {
				return this.values.get(i);
			}
		}
		return null;
	}
}

class ExampleDictionaries {

	/*
		This method accepts two List<>s named keys and values and a key, 
		and returns the value corresponding to the key. If the key does
		not exist, it should return null. This method is generic over
		two types, a type K for keys, and a type V for values.
	*/
	<K, V> V lookup(List<K> keys, List<V> values, K key) {
		for (int i = 0; i < keys.size(); i += 1) {		
			if (keys.get(i).equals(key)) {
				return values.get(i);
			}
		}
		return null;
	}

	void testLookup(Tester t) {
		List<Integer> intKeys = Arrays.asList(1, 2, 3);
		List<String> strKeys = Arrays.asList("a", "b", "c");
		
		List<Integer> intValues = Arrays.asList(4, 5, 6);
		List<String> strValues = Arrays.asList("ucsd", "cse", "11");

		t.checkExpect(this.lookup(intKeys, intValues, 1), 4);
		t.checkExpect(this.lookup(strKeys, intValues, "b"), 5);

		t.checkExpect(this.lookup(intKeys, strValues, 3), "11");
		t.checkExpect(this.lookup(strKeys, strValues, "a"), "ucsd");

		t.checkExpect(this.lookup(intKeys, intValues, 5), null);
		t.checkExpect(this.lookup(strKeys, intValues, "abc"), null);
	}

	void testDictionary(Tester t) {
		// Test the empty dictionary
		Dictionary<String, Integer> dict = new Dictionary<>();
		t.checkExpect(dict.keys.size(), 0);
		t.checkExpect(dict.values.size(), 0);

		// Add some values
		dict.put("a", 1);
		dict.put("b", 2);
		dict.put("c", 3);

		t.checkExpect(dict.get("a"), 1);
		t.checkExpect(dict.get("b"), 2);
		t.checkExpect(dict.get("c"), 3);
		t.checkExpect(dict.get("d"), null);	

		// Update an existing value
		dict.put("a", 4);
		t.checkExpect(dict.get("a"), 4);

		// Make sure this wasn't added as a new key
		t.checkExpect(dict.keys.size(), 3);

		// Make sure the other values are unchanged
		t.checkExpect(dict.get("b"), 2);
		t.checkExpect(dict.get("c"), 3);
		t.checkExpect(dict.get("d"), null);

		// Make sure adding a new value after
		// the update still works
		dict.put("d", 5);
		t.checkExpect(dict.get("a"), 4);
		t.checkExpect(dict.get("b"), 2);
		t.checkExpect(dict.get("c"), 3);
		t.checkExpect(dict.get("d"), 5);
		t.checkExpect(dict.get("e"), null);
	}

}
