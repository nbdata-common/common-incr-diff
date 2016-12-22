package com.qunar.test

import com.qunar.spark.diff.api.scala.DiffTracer
import com.qunar.spark.diff.base.regular.elements.{BooleanElement, CompositeElement, Element, UnitElement}

class ScalaTest {

  @org.junit.Test
  def test(): Unit = {
    val a: CompositeElement = new CompositeElement {
      override def name: String = "sss"

      override def sortElement(): Seq[Element] = null
    }
    a match {
      case a: JavaTest => a.name
      case a: CompositeElement => a.name
    }
    val ddd: DiffTracer[Long] = DiffTracer[Long]()
  }

  def isDifferent[T](a: UnitElement[T], b: UnitElement[T]): Boolean = {
    false
  }


}

class Tess(private val interValue: Boolean) extends BooleanElement(interValue) {
  override def name: String = "ss"
}