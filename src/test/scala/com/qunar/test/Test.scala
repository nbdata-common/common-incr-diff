package com.qunar.test

import com.qunar.spark.base.regular.elems.{NodeElement, UnitElement}

class Test {

  @org.junit.Test
  def test(): Unit = {
    val a: NodeElement = new NodeElement {
      override def value[@specialized(Char, Int, Boolean, Byte, Long, Float, Double) T]: T = {
        null.asInstanceOf[T]
      }

      override def name: String = "sss"
    }
    a match {
      case a: Test => a.name
      case a: NodeElement => a.name
      case a: UnitElement => a.name
    }
  }


}
