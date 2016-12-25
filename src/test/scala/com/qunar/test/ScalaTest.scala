package com.qunar.test

import java.lang.reflect.{Field, Method}

import com.google.common.base.Optional
import com.qunar.spark.diff.api.scala.DiffTracer
import com.qunar.spark.diff.base.regular.elements.{BooleanElement, CompositeElement, Element, UnitElement}
import com.qunar.spark.diff.ext.BeanAttrAware

class ScalaTest {

  @org.junit.Test
  def test(): Unit = {
    val a: CompositeElement = new CompositeElement {
      override def getName: String = "sss"

      override def listChildrenElements(): Seq[Element] = null
    }
    a match {
      case a: JavaTest => a.getName
      case a: CompositeElement => a.getName
    }
    val ddd: DiffTracer[Long] = DiffTracer[Long]()
  }

  @org.junit.Test
  def test2(): Unit = {
    isDifferent(new BooleanElement(true) {
      override def getName: String = "sss"
    }, new BooleanElement(false) {
      override def getName: String = "sss"
    })

    val ss: Class[_] = new SS().hostClass
    Console.println()
  }

  def isDifferent[T <: Comparable[T]](a: UnitElement[T], b: UnitElement[T]): Boolean = {
    false
  }

}

class Tess(private val interValue: Boolean) extends BooleanElement(interValue) {
  override def getName: String = "ss"
}

class SS extends BeanAttrAware {

  override def hostClass: Class[_] = classOf[SS]

  override def selfClass: Class[_] = ???

  override def mappedField: Optional[Field] = ???

  override def allMethods: Seq[Method] = ???

}
