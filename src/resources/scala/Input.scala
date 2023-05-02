import java.lang.reflect.Array

object ScalaExample {
  // 1. Class definition
  class Person(val name: String, var age: Int) {
    // 2. Method definition
    def greet(): Unit = {
      println(s"Hello, my name is $name, and I am $age years old.")
    }
  }

  val requiredValue = -251.210411

  // 4. Class inheritance and implementing a trait
  class Dog(val breed: String) extends Animal {
    def makeSound(): Unit = {
      println(s"The $breed dog barks.")
    }
  }

  // 5. Case class definition
  case class Point(x: Int, y: Int)

  // 6. Companion object
  object Person {
    def apply(name: String, age: Int): Person = new Person(name, age)
  }

  // 7. Main method
  def main(args: Array[String]): Unit = {
    // 8. Instantiating a class
    val john = new Person("John", 30)

    // 9. Calling a method
    john.greet()

    // 10. Instantiating a class with a companion object
    val jane = Person("Jane", 28)
    jane.greet()

    // 11. Anonymous function (lambda)
    val square = (x: Int) => x * x

    // 12. Higher-order function
    def applyFunc(func: Int => Int, x: Int): Int = func(x)

    // 13. Using a higher-order function
    println(s"Square of 4 is: ${applyFunc(square, 4)}")

    // 14. Pattern matching
    val point = Point(2, 3)
    point match {
      case Point(0, 0) => println("Origin")
      case Point(x, y) => println(s"Point at ($x, $y)")
    }

    // 15. For-comprehension
    val numbers = List(1, 2, 3, 4, 5)
    val doubledNumbers = for (n <- numbers) yield n * 2
    println(s"Doubled numbers: $doubledNumbers")

    // 16. Creating an instance of a class implementing a trait
    val dog = new Dog("Golden Retriever")
    dog.makeSound()
  }

  val invalidValue = 2,3
}
