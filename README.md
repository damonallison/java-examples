# README #

This project is a collection of java sources and tests that I wrote while attempting to learn Java. 

## BOOKMARK -- START HERE (Nested Classes) ##

https://docs.oracle.com/javase/tutorial/java/javaOO/nested.html

#### Books ####

[Java Concurrency in Practice](http://www.amazon.com/Java-Concurrency-Practice-Brian-Goetz/dp/0321349601)
Written by Doug Lea. Doug oversaw the concurrency team at Sun.

[Effective Java, 2nd Edition](http://www.amazon.com/Effective-Java-Edition-Joshua-Bloch/dp/0321356683)
Written by Josh Block, responsible for Java API development at Sun.


#### Todo ####

* Jar files : how to create them, what they can contain, their relation to the class path, etc.
* Javadoc : examples of the syntax, relationship to HTML.
* Inline Annotations (Java 8) - @NonNull - do they have any runtime effect or are they for compile / static code/tool analysis only?
* Guava : anything by Google.
* Analytics tools : cyclomatic complexity, unit test code coverage, LOC, etc. 
* New Java 8 Features (streams, parallel sorts, lambda expressions)
* Enum types (holding variable state)
	* Write an example of an enum singleton. 
	
#### Questions ####

* Hotspot : what optimizations does the HotSpot JVM provide?
* Performance : how does Java perform in real world usage when compared to compiled languages like C, Swift, or Go? 
* Are all the packaging tools (Java Web Start, Java Plug-In) and UI tools (Swing, JavaFX, and Java2D) obsolete?
  Why would I **ever** want to write a UI in java? All java should be server side or backend, cross platform code
  wherever necessary.


# Links / Reading History #

I read thru the following sources / tutorials when creating this code:

* [The Java Tutorials](https://docs.oracle.com/javase/tutorial/index.html) The Java Tutorials is a quick, high level
 introduction to Java written by Sun (Oracle). I read through "Trails Covering the Basics".

## Trail : Learning the Java Language ##

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



