package com.damonallison.utils;

import java.util.ArrayList;

import com.google.common.base.Predicate;

public class CoolList<T extends Number> extends ArrayList<T> {

	private static final long serialVersionUID = -1612761500577055734L;

	public CoolList(int initialCapacity) {
		super(initialCapacity);
	}

	/**
	 * Is this defined in a library somewhere? Like on ArrayList?
	 * 
	 * @param func
	 * @return
	 */

	public CoolList<T> filter(Predicate<T> pred) {
		if (pred == null) {
			return null;
		}
		CoolList<T> ret = new CoolList<T>(this.size());
		for (T elt : this) {
			if (pred.apply(elt)) {
				ret.add(elt);
			}
		}
		return ret;
	}
}
