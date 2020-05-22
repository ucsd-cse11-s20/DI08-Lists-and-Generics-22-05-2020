# Generics and Lists
This week, we learned about a very important concept in Java (and Computer Science in general!) called "Parametric Polymorphism", which is a fancy word that simply means using the same code to work with different data types. (Okay, it's a bit more complicated than that, but it's a good enough definition for our purposes). So for discussion, we are going to get some practice writing Polymorphic code (also called "Generic" in Java) and see how we can use it to do more with less code. But as usual, before that here's the review:

## Review

### Generic Methods
We've already seen how parameters in methods let us run the same code for multiple values of the same type. Generics methods in Java allow us to do the same thing, but for types as well! To define a generic method, we start the method header by writing the name of the type parameter in `<>` brackets. The type parameter's name is our choice, although the convention is to use `T`, `U`, etc. For example, we saw in lecture that we can write a generic method that counts the elements of an array that match some condition:

```java
<T> int count(T[] ts, Checker<T> c) {
    int count = 0;
    for (T t: ts) {
        if (c.check(t)) {
            count += 1;
        }
    }
    return count;
}
```

Notice that we start the method header with a name in `<>` brackets (in this case, we picked the name `T`), which allows us to use `T` as a _type name_ in the rest of this method.

We can call generic methods just like regular methods in java, and Java knows which actual types our generic types correspond to in each method call. However, keep in mind that each generic type must correspond to _the same type_ everywhere in our method call. For instance here:

```java
int longTweetCount = count(
    new Tweet[] { t1, t2, t3, t4}, // T[]        -> TextTweet[]
    new LongTweet()                // Checker<T> -> LongTweet (implements Checker<TextTweet>)
    );              
```

type `T` corresponds to `TextTweet` in both parameters.

### Generic Interfaces and Classes
Java also allows us to define generic interfaces and classes. To define these, we again use the `<>` notation, but write it in front of the name of the interface or class instead. Like with methods, this allows us to use that type parameter as a type name in the body of the class/interface definition:

```java
interface Checker<T> {
    boolean check(T t); // We can use T here.
}
```

One notable difference between generic methods and classes is that when using generic classes/interfaces, Java won't figure out the actual types for us, and we need to provide them manually:

```java
class LongTweet implements Checker<TextTweet> { // We need to provide the TextTweet type here ...
    public boolean check(TextTweet t) {
        return t.content.length() > 20;
    }
}

/* ... */

Checker<TextTweet> longChecker = new LongTweet(); // and here!
```

One exception to this is when directly instantiating a generic class, we can skip the types between the `<>` in the constructor call. Note that we still need to provide it in the type of the field/variable, and we still need to include the empty `<>`:

```java
class Pair<TypeOne, TypeTwo> { /* ... */ }

/* ... */

Pair<String, String> pair = new Pair<>("Hello,", "CSE11");
```

### Boxes: Integer, Boolean, Double

A limitation of generics in Java is that all parametrized types must be _reference types_, and we can't use _primitive_ types (`int`, `boolean`, `double`) in generics. To alleviate this, Java provides the classes `Integer`, `Boolean` and `Double` that are simple wrappers/boxes around the primitive types, letting us use primitives with generics (among other nice things):

```java
class Positive implements Checker<Integer> {
    public boolean check(Integer i) {
        return i > 0;
    }
}
```

These are classes just like the other classes we have seen. However, being built into Java, Java knows how to convert between them and their primitive types. This feature (sometimes called "Autoboxing") allows us to use the reference types when necessary, while still treating them as primitives:

```java
Integer one = 1;         // We can use new Integer(1) instead, but we don't need to!
Boolean notTrue = false; // Same here
Double two = 2.0;        // and here.

if (notTrue) {           // We can use Booleans directly.
    one += 1;            // We can add/subtract/compare/etc. Integers, just like ints.
} else {
    two = 1 - two;       // And the same with Doubles.
}
```

If, however, you require the primitive value and Java doesn't convert it for you, each class has a `*value()` method that returns its primitive value:

```java
int primitiveOne = one.intValue();
boolean primitiveNotTrue = notTrue.booleanValue();
double primitiveTwo = two.doubleValue();
```

### Lists and ArrayLists
Java comes with a variety of really useful generic classes that represent different ways of collecting data. This section will focus on the `List` interface, and a common implementation of that interface called the `ArrayList`. You can find the details of these and other available collections in the [Java Collections Framework](https://docs.oracle.com/en/java/javase/13/docs/api/java.base/java/util/package-summary.html#JavaCollectionsFramework).

Previously, we used regular arrays to store lists of data, read them, and modify them. `ArrayList`s are a generic type in Java that allows us to do all the same things, with slightly different syntax:

| Array                                  | ArrayList                                         |
| -----------------------------          | ------------------------------------------------- |
| `int[] numArray = {1, 2, 3};`          | `List<Integer> numList = Arrays.asList(1, 2, 3);` |
| `int one = numArray[0];`               | `int anotherOne = numList.get(0);`                |
| `numArray[1] = one * 2;`               | `numList.add(1, anotherOne * 2);`                 |
| `for (int num: numArray) { /* ... */}` | `for (Integer num: numList) { /* ... */}`         |

The key distinction between arrays and `ArrayList`s is that `ArrayList`s are resizable, so we can `add` and `remove` values from them!

```java
List<String> messages = Arrays.asList("Hello", "CSE11");

// Appends "on your assignments!" to the end of messages
messages.add("on your assignments!"); 

// Inserts "Good luck" at index 2, shifting "on your assignments!" to the right.
messages.add(2, "Good luck");

// Removes "Hello" from the list, and returns it.
String hello = messages.remove(0);
```

### Overloading
A smaller Java feature that we saw this week was the ability to _overload_ methods, which means providing multiple implementations of the same method, differentiated by the parameters they accept. Note that this only works if the different method have a different number of parameters, or parameters of different types (or both!), but not if they only have different return types. However, if they _do_ have different parameters, they can return different types as well:

```java
int add(int a, int b) { /* ... */}
double add(double a, double b) { /* ... */}
```

One place we have already seen overloaded methods is in the `ArrayList` above! Notice that the `add` method is overloaded to optionally accept an index, without which it just inserts the element at the end of the array. (Bonus problem: If you look up these methods in [the documentation](https://docs.oracle.com/en/java/javase/13/docs/api/java.base/java/util/ArrayList.html), you'll see that one of them returns a `boolean` while the other's return type is `void`. Can you find out why?)

A common use for overloading methods is to provide default parameters. For instance, the following method is overloaded to use a default length of `5`:

```java
/*
    This method returns a new ArrayList containing the Strings in `strs` that
    are longer than `length`.
*/
static ArrayList<String> longStrings(ArrayList<String> strs, int length) { 
    /* See lecture 15 for the implementation! */
}

/*
    Same as `longStrings` above, but takes Strings longer than `5` to be long.
*/
static ArrayList<String> longStrings(ArrayList<String> strs) { 
    return longStrings(strs, 5);
}
```

When calling an overloaded method, Java knows which one to call based on the arguments we use to call the method.

### Exceptions
Exceptions are Java's way of handling errors that can happen when we run our programs. When programming, we have often been making assumptions about our input ("the array is not empty", "the argument can be read as an integer", etc.) where our code had no way of handling those erroneous/exceptional cases. You may have run into exceptions as a result of incorrect/buggy code as well. Some of these exceptions include:

1. `ArrayOutOfBoundsException`: Happens when we try to get the element at an index of an array that doesn't exist
2. `NullPointerException`: Happens when we try to read a field of, or call a method on a `null` value.
3. `ArithmeticException`: Happens when we try to run an invalid arithmetic operation (e.g. divide by zero)

This week, we saw how to _throw_ exceptions ourselves, which allows us to be more specific about the nature of the error (by throwing an exception of a particular type) and provide a custom, more descriptive, error message. To do so, we simply use the keyword `throw` followed by an instance of a class extending the `Throwable` class:

```java
static Integer max(ArrayList<Integer> elements) {
    if (elements.size() == 0) {
        throw new IllegalArgumentException("max got an empty list");
    }

    /* ... */
}
```

The `IllegalArgumentException` class (which extends the `Throwable` class) is thrown when the input to a method/program/etc. is not valid for that method/program/etc. Since `max` is only defined/meaningful for non-empty lists, we throw an instance of it (by calling the constructor of that exception using `new`) to handle this case.

The `throw` keyword:

1. Exits out of each method call in reverse order that they were called, until it eventually exits the program.
2. Collects information about each method's stack frame as it exists them.
3. Prints out this information, called the "Stack Trace", as well as the message of the exception itself to the terminal.

The Stack Trace always starts with information about the exception itself (it's type, and the message), followed by the stack trace, which let us know the complete chain of method calls that resulted in the exception being thrown. For example, the following output:

```log
Exception in thread "main" java.lang.IllegalArgumentException: max got an empty list
    at ExceptionsExamples.max(ExceptionsExamples.java:14)
    at ExceptionsExamples.main(ExceptionsExamples.java:31)
```

tells us that the `main` method called the `max` method one line 31 in `ExceptionsExamples.java`, and the `max` method threw an `IllegalArgumentException` on line `14` with the message: `"max got an empty list"`.

## Problems: You say Dictionary, I say Map

### Problem 1:
Recall the `lookup` method from [PA5](https://ucsd-cse11-s20.github.io/pa5/#array-methods). This method accepts two arrays, a `String` array named `keys` and an `int` array named `values`, and a String parameter `key` that it looks up in the "keys" to return the corresponding "value".

In file named `Dictionary.java`, write a class named `DictionaryExamples` and in it define a method named `lookup` that accepts two `List`s named `keys` and `values` and a `key`, and returns the value corresponding to the key. If the key does not exist, it should return `null`. This method should be generic over _two_ types, a type `K` for `keys`, and a type `V` for `values`.

Write at least 4 tests for this method, using `ArrayList`s of all possible combinations of `String` and `Integer` types.

### Problem 2:
In the file `Dictionary.java`, write a new class `Dictionary` which is generic over two types `K` and `V`. Add two fields named `keys` and `values` of types `List<K>` and `List<V>`, and a constructor that accepts _no_ parameters, but initializes both fields to empty `ArrayList`s.

Write a test that creates an instance of a `Dictionary` from `String`s to `Integer`s, and checks that both fields are empty.

### Problem 3:
Write a method `put` in `Dictionary` that accepts two parameters of type `K` and `V`, and inserts them in the dictionary. For now, you can assume that all keys in the dictionary are unique.

Then write another method named `get` that accepts a parameter of type `K` and returns the matching value if it exists in the dictionary. It should return `null` if no such key exists.

Write at least 4 interesting tests for these methods.

### Problem 4:
Modify the `put` method from Problem 3 to account for duplicate keys. If the given key already exists in the dictionary, it should _update_ the corresponding value rather than adding a duplicate key.

Write at least 6 tests to make sure that:

1. Values are correctly updated
2. Duplicate keys cannot be added to the dictionary using the `put` method.
3. Updating a value using `put` does not change the value of other keys in the dictionary.

Are there other possible bugs we should test for?

### Problem 5: [Extra problem for the curious]
Using the `Pair` class that we defined in lecture, rewrite the `Dictionary` class to use a single list of pairs, instead of two separate lists.

### Problem 6: [Bonus problem for the adventurous]
The `Dictionary` above implements some of the basic methods for the `Map` interface in Java. Look up the definition of the `Map` interface [here](https://docs.oracle.com/en/java/javase/13/docs/api/java.base/java/util/Map.html), pick 3 other interesting methods from `Map` and implement them for the `Dictionary` class we defined.