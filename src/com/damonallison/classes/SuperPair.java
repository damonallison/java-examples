package com.damonallison.classes;

/**
 * Shows creating a subtype of a generic class.
 *
 * Note that {@link SuperPair} binds V to Comparable<V>. This restricts the types that can be used with {@link SuperPair}
 * to be more restrictive than {@link Pair}. This is allowed.
 */
public class SuperPair<K, V extends Comparable<V>> extends Pair<K, V> {

	public SuperPair(K key, V value) {
		super(key, value);
	}

	@Override
	public String toString() {
		return String.format("SuperPair[" + this.getKey().toString() + "," + this.getValue().toString());
	}

}
