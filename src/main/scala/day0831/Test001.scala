package day0831

object Test001 {
  def main(args: Array[String]): Unit = {
    val fisheagle = new fishEagle
    val flyable : flayable = fisheagle
    flyable.fly
    fisheagle.walk(1000)

    val swimable : swingable = fisheagle
    swimable.swim


    val str = "+abc"

    println(str.replace(str.substring(0,1),"0"))
    println(str)
  }
}

abstract class animal {
  def walk (speed : Int)

  def breathe () = println("animal breathe")

}

trait flayable {
  def hasFeather = true
  def fly
}

trait swingable {
  def swim
}

class fishEagle extends animal with flayable with swingable {
  def walk(speed:Int) = println("fish eagle walk with speed " + speed)
  def swim() = println("fish eagle swim fast")
  def fly() = println("fish eagle fly fast")
}