package com.damonallison.classes;

/**
 * A simple generic class that holds a type.
 *
 * The term "generic class" can be misleading. It's simply a class that has one
 * or more generic type parameters.
 */
public class Pair<K, V> {
	private K key;
	private V value;

	public Pair(K key, V value) {
		this.key = key;
		this.value = value;
	}

	public K getKey() {
		return this.key;
	}

	public V getValue() {
		return this.value;
	}

}
