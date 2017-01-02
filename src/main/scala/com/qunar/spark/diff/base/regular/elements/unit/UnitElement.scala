package com.qunar.spark.diff.base.regular.elements.unit

import com.google.common.base.Preconditions
import com.qunar.spark.diff.base.regular.elements.Element
import com.qunar.spark.diff.base.regular.elements.unit.UnitElementType.UnitElementType

/**
  * 在递归结构中,表征一个原子元素(即单元节点,不可继续拆分)
  * 单元节点必须满足:其唯一映射到一个T类型的value,该value已实现[[Comparable[T] ]]
  */
trait UnitElement[T <: Comparable[T]] extends Element {

  // 获取value
  def value: T

  // 重置value
  def setValue(newValue: T): Unit

  override def compareTo(elem: Element): Int = {
    elem match {
      case elem: UnitElement[T] => if (super.compareTo(elem) != 0) 1 else if (value.compareTo(elem.value) == 0) 0 else 1
      // 对于UnitElement,只要比较对象类型不同或类型参数不相同,则认为不同,默认返回1
      case _ => 1
    }
  }

}

object UnitElement {

  /**
    * 获得[[UnitElement]]的实际枚举类型(Numeric,Text或Boolean)
    *
    * @return [[UnitElement]]的枚举类型[[UnitElementType]]
    */
  def actualType(element: UnitElement[_]): UnitElementType = {
    Preconditions.checkNotNull[Element](element)
    element match {
      case element: NumericElement => UnitElementType.NUMERIC_ELEMENT
      case element: TextElement => UnitElementType.TEXT_ELEMENT
      case element: BooleanElement => UnitElementType.BOOLEAN_ELEMENT
    }
  }

}

/**
  * [[UnitElement]]的枚举类型(Numeric,Text或Boolean)
  */
object UnitElementType extends Enumeration {

  type UnitElementType = Value

  val NUMERIC_ELEMENT, TEXT_ELEMENT, BOOLEAN_ELEMENT = Value

}
