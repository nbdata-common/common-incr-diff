package com.qunar.spark.diff.internal.impl.regular

import java.lang.reflect.Field

import com.qunar.spark.diff.base.regular.elements.Element
import com.qunar.spark.diff.base.regular.elements.ext.api.ExtElement
import com.qunar.spark.diff.ext.AnnotationAware

/**
  * [功能拓展] 递归结构中,拥有注解感知能力的元素
  *
  * @param field            这里在构造器中直接引入field,以简化类的结构,然后将具体的构造过程交给伴生对象的工厂方法
  * @param decoratedElement 被装饰的元素,这里设置为可变型的,是为了能够复用实例,节约内存空间
  *                         <p/>
  * @note [[field]],[[decoratedElement]]这种类似的设计在[[com.qunar.spark.diff.internal]]包的其他类中也有体现:
  * @see [[com.qunar.spark.diff.internal.impl.regular.jackson.element]]
  */
class AnnotatedElement private(private var decoratedElement: Element,
                               private val field: Field) extends ExtElement(decoratedElement)
  with AnnotationAware {

  def setDecoratedElement(newElement: Element): Unit = {
    this.decoratedElement = newElement
  }

  override def mappedField: Option[Field] = Option(field)

}

object AnnotatedElement {

  /**
    * 给定Element与宿主对象构造拥有注解感知能力的元素
    *
    * @param host 宿主对象
    */
  def apply(decoratedElement: Element, host: Any): AnnotatedElement = {
    val hostClass = host.getClass
    AnnotatedElement(decoratedElement, hostClass)
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
