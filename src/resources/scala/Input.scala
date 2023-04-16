import scala.collection.mutable.ArrayBuffer

trait Animal {
  def sound: String
  def makeSound(): Unit = println(sound)
}

class Dog(val name: String) extends Animal {
  override val sound: String = "Woof!"
}

class Cat(val name: String) extends Animal {
  override val sound: String = "Meow!"
}

abstract class AnimalShelter[A <: Animal] {
  private val animals = new ArrayBuffer[A]()

  def adopt(): Option[A] = {
    if (animals.nonEmpty) {
      val animal = animals.head
      animals.remove(0)
      Some(animal)
    } else {
      None
    }
  }

  def intake(animal: A): Unit = {
    animals += animal
  }

  def numberOfAnimals: Int = animals.size
}

class DogShelter extends AnimalShelter[Dog]
class CatShelter extends AnimalShelter[Cat]

object Main {
  def main(args: Array[String]): Unit = {
    val dogShelter = new DogShelter
    val catShelter = new CatShelter

    dogShelter.intake(new Dog("Max"))
    dogShelter.intake(new Dog("Buddy"))
    catShelter.intake(new Cat("Milo"))
    catShelter.intake(new Cat("Oliver"))


    val adoptedDog = dogShelter.adopt()
    val adoptedCat = catShelter.adopt()

  }
}
