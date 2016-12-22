package com.qunar.spark.diff.base.regular.elements

import com.google.common.base.Preconditions
import com.qunar.spark.diff.base.regular.elements.UnitElementType.UnitElementType

/**
  * 在规则(递归)结构中,表征一个原子元素(即单元节点,不可继续拆分)
  * 单元节点必须满足:其唯一映射到一个T类型的value,该value已实现Comparable[T]
  */
trait UnitElement[T <: Comparable[T]] extends Element {

  def value: T

}

object UnitElement {

  def actualType[T](element: UnitElement[T]): UnitElementType = {
    Preconditions.checkNotNull(element)
    element match {
      case element: NumericElement => UnitElementType.NUMERIC_ELEMENT
      case element: TextElement => UnitElementType.TEXT_ELEMENT
      case element: BooleanElement => UnitElementType.BOOLEAN_ELEMENT
    }
  }

}

object UnitElementType extends Enumeration {

  type UnitElementType = Value

  val NUMERIC_ELEMENT, TEXT_ELEMENT, BOOLEAN_ELEMENT = Value

}
