package com.qunar.test

import com.qunar.spark.base.regular.elems.NodeElement
import com.qunar.spark.diff.api.scala.DiffTracer
import com.qunar.spark.diff.base.regular.elems.{NodeElement, UnitElement}

class ScalaTest {

  @org.junit.Test
  def test(): Unit = {
    val a: NodeElement = new NodeElement {
      override def value[@specialized(Char, Int, Boolean, Byte, Long, Float, Double) T]: T = {
        null.asInstanceOf[T]
      }

      override def name: String = "sss"

      override def sortElement(): Seq[UnitElement] = null
    }
    a match {
      case a: JavaTest => a.name
      case a: NodeElement => a.name
      case a: UnitElement => a.name
    }
    val ddd: DiffTracer[Long] = DiffTracer[Long]()
  }


}
