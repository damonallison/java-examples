package com.damonallison.classes;

public class CloneableTest implements Cloneable {

    private final String value;

    public CloneableTest(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof CloneableTest)) {
            return false;
        }
        return this.hashCode() == obj.hashCode();
    }

    @Override
    public Object clone() {
        return new CloneableTest(this.getValue());
    }

    @Override
    public String toString() {
        return String.format("CloneableTest: %s", this.getValue());
    }
}
