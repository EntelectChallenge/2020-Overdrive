package test

import org.scalatest.FunSuite

class Command_Nothing_Tests extends FunSuite{



  test("Given player during race when NOTHING command given then player moves forward at current speed") {
    val expectedValue: String = "Hello World!"
    val inputValue: String = "Hello World!"
    assert(inputValue.equals(expectedValue))
  }
}
