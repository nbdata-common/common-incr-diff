package com.qunar.spark.diff.internal.impl.regular

import java.lang.reflect.Field

import com.qunar.spark.diff.base.regular.elements.Element
import com.qunar.spark.diff.base.regular.elements.ext.api.ExtElement
import com.qunar.spark.diff.ext.AnnotationAware

/**
  * [功能拓展] 递归结构中,拥有注解感知能力的元素
  */
class AnnotatedElement private(private val decoratedElement: Element,
                               private val field: Field) extends ExtElement(decoratedElement)
  with AnnotationAware {

  override def mappedField: Field = {
    field
  }

}

object AnnotatedElement {

  /**
    * 给定Element与宿主对象构造拥有注解感知能力的元素
    *
    * @param host 宿主对象
    */
  def apply(decoratedElement: Element, host: Any): AnnotatedElement = {
    val hostClass = host.getClass
    apply(decoratedElement, hostClass)
  }

  /**
    * 功能同上
    *
    * @param hostClass 宿主类的Class对象
    */
  def apply(decoratedElement: Element, hostClass: Class[_]): AnnotatedElement = {
    val name = decoratedElement.name
    try {
      val field = hostClass.getField(name)
      new AnnotatedElement(decoratedElement, field)
    } catch {
      case e: NoSuchFieldException => new AnnotatedElement(decoratedElement, null)
    }
  }

}
