package com.qunar.spark.diff.internal.impl.regular

import java.lang.reflect.Field

import com.google.common.base.Optional
import com.qunar.spark.diff.base.regular.elements.Element
import com.qunar.spark.diff.base.regular.elements.ext.api.ExtElement
import com.qunar.spark.diff.ext.AnnotationAware

/**
  * [功能拓展] 递归结构中,拥有注解感知能力的元素
  *
  * @param field 这里在构造器中直接引入field,以简化类的结构,然后将具体的构造过程交给伴生对象的工厂方法
  */
class AnnotatedElement private(private val decoratedElement: Element,
                               private val field: Field) extends ExtElement(decoratedElement)
  with AnnotationAware {

  //todo 改成scala的Option
  override def mappedField: Optional[Field] = Optional.fromNullable(field)

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
    val name = decoratedElement.getName
    try {
      val field = hostClass.getField(name)
      new AnnotatedElement(decoratedElement, field)
    } catch {
      case e: NoSuchFieldException => new AnnotatedElement(decoratedElement, null)
    }
  }

}
