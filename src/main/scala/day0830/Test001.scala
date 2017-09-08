package day0830

object Test001 {
  def main(args: Array[String]): Unit = {
    printf("hello mac \n")
    val a = 3
    val b = 4
    val c = a * b
    printf(c.toString + "\n")

    println(sum_pf(10,100))

    val vl = List(1,2,3,4,5,6,7,8,9)

    println(sum_count_1(vl))

    println(sqrt(2))
  }

  def pf(v : Int) : Int = v * v

  def sum_pf(v1 :Int, v2 : Int) : Int = pf(v1) + pf(v2)

  def sum_count(vlist : List[Int]) : Int = {
    var total = 0
    var index = 0

    while (index < vlist.size) {
      total += vlist(index)
      index += 1
    }

    total
  }

  def sum_count_1(vlist : List[Int]) : Int = vlist.sum

  def sqrt(x : Double) : Double = {
    def sqrtItrt(guess : Double): Double =
      if (goodGuess(guess))
        guess
      else
        sqrtItrt((guess + x / guess)/2)

    def goodGuess(guess: Double) : Boolean =
      abs(guess * guess - x) < 0.0001

    def abs(x: Double) =
      if (x < 0) -x else x

    sqrtItrt(1)
  }


}
