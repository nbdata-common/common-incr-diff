package com.qunar.spark.diff.internal.impl.regular

import java.lang.reflect.Field

import com.qunar.spark.diff.base.regular.elements.Element
import com.qunar.spark.diff.base.regular.elements.composite.{ArrayElement, CompositeElement}
import com.qunar.spark.diff.base.regular.elements.ext.api.{ExtArrayElement, ExtCompositeElement, ExtUnitElement}
import com.qunar.spark.diff.base.regular.elements.unit.{BooleanElement, NumericElement, TextElement, UnitElement}
import com.qunar.spark.diff.ext.AnnotationAware

/**
  * ''[功能拓展]'' 递归结构中,拥有注解感知能力的元素
  *
  * field            这里在构造器中直接引入field,以简化类的结构,然后将具体的构造过程交给伴生对象的工厂方法
  * decoratedElement 被装饰的元素,这里设置为可变型的,是为了能够复用实例,节约内存空间
  *
  * @note field, decoratedElement 这种类似的设计在[[com.qunar.spark.diff.internal]]包的其他类中也有体现:
  * @see [[com.qunar.spark.diff.internal.impl.regular.jackson.element]]
  */
trait AnnotatedElement extends AnnotationAware {

  protected var innerDecoratedElement: Element

  protected val innerField: Field

  def setDecoratedElement(newElement: Element): Unit = {
    this.innerDecoratedElement = newElement
  }

  override def mappedField: Option[Field] = Option(innerField)

}

/**
  * 以下三个类为[[AnnotatedElement]]的三种实现类
  * 之所以要定义三个内容大致相同的实现类,是考虑到scala的语法限制:不能继承两个或两个以上的class/abstract class
  * 为了能够区分不同类型的[[AnnotatedElement]],此处不得不作如下处理.
  */

/**
  * 适用于[[UnitElement]]的注解装饰拓展类
  */
final class AnnotatedUnitElement[T <: Comparable[T]](private var decoratedElement: UnitElement[T],
                                                     private val field: Field) extends ExtUnitElement(decoratedElement) with AnnotatedElement {

  override protected var innerDecoratedElement: Element = decoratedElement
  override protected val innerField: Field = field

}

/**
  * 适用于[[CompositeElement]]的注解装饰拓展类
  */
final class AnnotatedCompositeElement(private var decoratedElement: CompositeElement,
                                      private val field: Field) extends ExtCompositeElement(decoratedElement) with AnnotatedElement {

  override protected var innerDecoratedElement: Element = decoratedElement
  override protected val innerField: Field = field

}

/**
  * 适用于[[ArrayElement]]的注解装饰拓展类
  */
final class AnnotatedArrayElement(private var decoratedElement: ArrayElement,
                                  private val field: Field) extends ExtArrayElement(decoratedElement) with AnnotatedElement {

  override protected var innerDecoratedElement: Element = decoratedElement
  override protected val innerField: Field = field

}


object AnnotatedElement {

  /**
    * 给定 [[Element]] 与 宿主对象 以构造拥有注解感知能力的元素
    *
    * @param host 宿主对象
    */
  def apply(decoratedElement: Element, host: Any): Element = {
    val hostClass = host.getClass
    AnnotatedElement(decoratedElement, hostClass)
  }

  /**
    * 功能同上
    *
    * @param hostClass 宿主类的Class对象
    */
  def apply(decoratedElement: Element, hostClass: Class[_]): Element = {
    val name = decoratedElement.getName
    try {
      val field = hostClass.getField(name)
      decoratedElement match {
        case decoratedElement: TextElement => new AnnotatedUnitElement(decoratedElement, field)
        case decoratedElement: NumericElement => new AnnotatedUnitElement(decoratedElement, field)
        case decoratedElement: BooleanElement => new AnnotatedUnitElement(decoratedElement, field)
        case decoratedElement: ArrayElement => new AnnotatedArrayElement(decoratedElement, field)
        case decoratedElement: CompositeElement => new AnnotatedCompositeElement(decoratedElement, field)
      }
    } catch {
      // 出现异常则返回原element
      case e: NoSuchFieldException => decoratedElement
      case e: SecurityException => decoratedElement
    }
  }

}
