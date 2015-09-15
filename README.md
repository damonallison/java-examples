# README #

This project is a collection of examples and tests I wrote while researching
Java.

## Trail : Learning the Java Language ##

Start here :
https://docs.oracle.com/javase/tutorial/essential/io/file.html

# Links / Reading History #

I read thru the following sources / tutorials when creating this code:

* [The Java Tutorials](https://docs.oracle.com/javase/tutorial/index.html)

The Java Tutorials is a quick, high level introduction to Java written by Sun (Oracle). The tutorials are called "trails".

## Trails Read ##

### [Trail : Getting Started](https://docs.oracle.com/javase/tutorial/getStarted/index.html) ###

Very simple intro into running your first application.

### [Trail : Learning the Java Programming Language](https://docs.oracle.com/javase/tutorial/java/TOC.html) ###

This tutorial covered the basics of all aspects of the language, including:
  * Introduction to OOP.
  * Java the language : types, control flow, classes, generics, interfaces.
  * Usage of numbers / strings.

### [Trail : Essential Java Classes](https://docs.oracle.com/javase/tutorial/essential/index.html) ###

This tutorial covered the basics of the most frequently used java classes.
  * Exceptions
  * I/O
  * Concurrency
  * The Platform Environment : the java infrastructure.
  * Regular Expressions

#### Books ####

[The Java Programming Language](http://www.amazon.com/The-Java-Programming-Language-Edition/dp/0321349806)
Arnold, Gosling (original java authors)

[Java Concurrency in Practice](http://www.amazon.com/Java-Concurrency-Practice-Brian-Goetz/dp/0321349601)
Written by Doug Lea. Doug oversaw the concurrency team at Sun.

[Effective Java, 2nd Edition](http://www.amazon.com/Effective-Java-Edition-Joshua-Bloch/dp/0321356683)
Written by Josh Block, responsible for Java API development at Sun.


#### Todo ####

* Concurrency

* Java Serialization : what is written during serialization? Is there a
  specification for the java serialization format?

* Are their any java books written by the compiler and/or runtime teams at Google?
* Why is implementing a correct version of "compareTo" impossible with java inheritance?
* String interpolation - is printf the best we can do? Is there a better
  replacement for Code42's log formatter token replacer?

* Is it more common to use primitives for performance or objects for ubitquity?
* Advanced I/O : netty or pipeline based I/O. How does Netty's pipeline compare to concatenating streams into a pipeline?
* How does try-with-resources work behind the scenes? Does the compiler insert `.Close` into a generated `finally` block?


###### Language ######

* Inline Annotations (Java 8) - @NonNull - do they have any runtime effect or are they for compile / static code/tool analysis only?
* New Java 8 Features (streams, parallel sorts)

* Lambda Expressions.

* Enum types (holding variable state)
	* Write an example of an enum singleton.

* Serialization.
* Reflection.

* Streams and Functional Programming : flatMap()
* Streams using I/O (file) as a data source.
* Streams : what are spliterators?


###### Tools ######

* Jar files : how to create them, what they can contain, their relation to the class path, etc.
* Javadoc : examples of the syntax, relationship to HTML.
* Analytics tools : cyclomatic complexity, unit test code coverage, LOC, etc.
* Log4J.
* junit


###### Libraries ######

* Apache commons.
* Iterator : implement a custom iterator.
* Guava : anything by Google.

#### Questions ####
* Hotspot : what optimizations does the HotSpot JVM provide?
* Performance : how does Java perform in real world usage when compared to compiled languages like C, Swift, or Go?
* Are all the packaging tools (Java Web Start, Java Plug-In) and UI tools (Swing, JavaFX, and Java2D) obsolete?
  Why would I **ever** want to write a UI in java? All java should be server side or backend, cross platform code
  wherever necessary.

## Notes ##

* Covariant return type : The return type is allowed to vary in the same direction as the subclass.

```
    /**
     *  Assume the following type hierarchy : Number -> ImaginaryNumber -> Object.
     *
     *  Covariant return type says that the return type can be ImaginaryNumber or
     * a sublcass of ImaginaryNumber (i.e., Number)
     */
    public ImaginaryNumber genNextNumber(int i) {
        return new Number(++i);
    }
```

#### Nested Classes ####
* Nested class : the term used to described a class that is defined within another class. There are many classifications of nested classes.

    * Inner class : Non-static nested class. Has access to the parent's state.
    * Static nested class : Static nested class.  Does *not* have access to the parent's state. Think of a static
    nested class as a normal class that is nested within another class for packaging purposes only.
    * Local class : A class defined within a method.
    * Anonymous class : a local class that does not have a name. Used heavily in callbacks. Watch out for memory
    cycles within anonymous classes!

* Local (and anonymous) classes capture instance variables. They can only capture final (or effectively final) instance
 variables. The compiler enforces this, which prevents any runtime errors.

* Inner classes and local classes cannot define any static methods (only static constants).

* Anonymous classes are simply unnamed local classes. Anonymous classes are more concise than declaring local classes.

#### Lambda Expressions ####

* Lambda expressions can be used when the interface being implemented contains only one method.

* Lambda expressions are simply syntactic sugar to simplify the creation of single-method anonymous classes.

#### Generics ####

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

#### Streams ####

* Streams have sources, intermediate operations, and terminators.
* Intermediate operations query the stream. The stream **should not** be modified while a stream is being queried.
* Parameters or objects used in an intermediate opration should be immutable. They should not change during the execution of the stream pipeline.


## Eclipse

* Theme : Preferences -> General -> Appearance

* Assertions (per project):
  Run -> Run Configurations -> Arguments Tab ->
  VM Arguments -> Add -enableassertions

* Global Assertions:
  Preferences -> Java / Installed JREs -> JRE -> Default VM Arguments -> Edit" -> Add -enableassertions

* Auto-format on save:
  Preferences -> Java -> Editor -> Save Actions ->
  Perform the selected actions on save -> Format source code

* Custom shortcut keys (emacs keys):
  Preferences -> General -> Keys
