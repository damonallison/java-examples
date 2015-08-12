# Java #

## TODO ##

* Maven integration

## Tools
* Javadoc - how to generate / view. What is the syntax? Add examples to sample project.
* Exceptions (1.8 | multiple exception handling)
* Executor service - runnables / callables - objects.
* Serialization / reflection.
* java.util.Concurrent.
* Classes : questions and exercises:
https://docs.oracle.com/javase/tutorial/java/javaOO/QandE/nested-questions.html

* Review annotations.

* Interfaces and Inheritance.
* Generics
* Finish "Learning the Java Language"
* "Essential Java Classes"

* Java Collections Framework (java.util...)



#### Java ####

Java API Engineers:

* Josh Bloch
* Neil Gafter
* Arthur van Hoff


## Questions

* Language : What interface do collections need to implement to be "enhanded for" compatible?
```
for (int x : coll) { }
```

* Tooling :
  * eclipse / intellij (open source?)
  * Unit testing framework(s).
  * Functional programming patterns (Functional Reactive Programming).
  * "State of the art" in Java. The community driving it, google / oracle involvement,
    open source involvement.

* Initializer blocks seem like a hack (copied into all constructors). It would
  be better to just chain constructor calls.

* Java 8 Annotations can be applied to *uses* of type ("Type Annotations") - what
  are they and how to create a new

## Likes

* Strong type checking.
* Generics.
* Memory management.

## Dislikes

* Slow moving. Lambda expressions / generics multiple years after Microsoft.
* Instance initializer blocks. I'd rather see a designated initializer used
  rather than in initializer blocks. Static initializer blocks are ok and
  could be useful.

## Java System Environment

/Library/Java/JavaVirtualMachines/jdk1.8.0_45.jdk
/System/Library/Java/JavaVirtualMachines/jdk1.6.0.jdk (this must be mac os x's location).


## Eclipse Environment

* Theme : Preferences -> General -> Appearance

* Enable "content assist" (autocomplete) for every character:
Preferences -> Java -> Editor -> Content Assist - set to:
```
._abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ
```

* Assertions (per project):
  Run -> Run Configurations -> Arguments Tab ->
  VM Arguments -> Add -enableassertions

* Global Assertions:
  Preferences -> Java / Installed JREs -> JRE -> Default VM Arguments -> Edit" -> Add -enableassertions

* Emacs keys:
  Preferences -> General -> Keys -> Scheme -> Emacs

* Auto-format on save:
  Preferences -> Java -> Editor -> Save Actions ->
  Perform the selected actions on save -> Format source code

* Custom shortcut keys:
  Preferences -> General -> Keys

## Language

* short (16) / int (32) / long (64) / float (32) / double (64)
* switch works with string, primitive types, enums, and a few special
  classes that wrap primitives.

## Annotations

* @Deprecated
* @Override
* @SuppressWarnings({"deprecation, unchecked"})
* @SafeVarargs - asserts the code does not perform unsafe operations on varargs.
* @FunctionalInterface - indicates the type should be treated as a functional interface.

## Collections

* Map : key-value mapping (NSDictionary).
* HashMap : hashtable backed map - order not guaranteed.
* TreeMap : tree backed map - order guaranteed.


java.util.Hashtable
  * non-null key/values only
  * Objects used in the hashtable must implement hashCode and equals
  * loadFactor is a measure of how full the hash table is allowed to get before
    increasing capacity.



## Generics

Generic method example. Notice the type declaration comes before the return
type.

```

public static <T extends Comparable<T>> int countGreaterThan(T[] array, T elem) {
	int count = 0;
	for (T e : array) {
		if (e.compareTo(elem) > 0) {
			++count;
		}
	}
	return count;
}

```

## Java Language Changes

### 1.7

* In 1.7+, you can switch on String.

### 1.8

* Lambdas
* FunctionalInterface : java.util.function
* Repeated annotations.
* Annotations can be applied when *using* types `new @Interned MyObject(); `.
