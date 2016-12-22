package com.qunar.test

import java.lang.reflect.Field

import com.qunar.spark.diff.api.scala.DiffTracer
import com.qunar.spark.diff.base.BeanAttrAware
import com.qunar.spark.diff.base.regular.elements.{BooleanElement, CompositeElement, Element}

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

  @org.junit.Test
  def test2(): Unit = {
    val ss: Class[_] = new SS().hostClass
    Console.println()
  }

}

class Tess(private val interValue: Boolean) extends BooleanElement(interValue) {
  override def name: String = "ss"
}

class SS extends BeanAttrAware {

  override def hostClass[_]: Class[_] = {
    this.getClass
  }

  override def selfClass[_]: Class[_] = ???

  override def mappedField: Field = ???

}