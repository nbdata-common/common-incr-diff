package com.qunar.spark.diff.base.regular.elements

import com.google.common.base.Preconditions
import com.qunar.spark.diff.base.regular.elements.UnitElementType.UnitElementType

/**
  * 在规则(递归)结构中,表征一个原子元素(即单元节点,不可继续拆分)
  * 单元节点必须满足:其唯一映射到一个T类型的value,该value已实现Comparable[T]
  */
trait UnitElement[T <: Comparable[T]] extends Element {

  // 获取value
  def value: T

  // 重置value
  def setValue(newValue: T): Unit

  override def compareTo(elem: Element): Int = {
    elem match {
      case elem: UnitElement[T] =>
        if (value.compareTo(elem.value) == -1) {
          -1
        } else if (value.compareTo(elem.value) == 1) {
          1
        } else {
          0
        }

      case _ =>
        super.compareTo(elem)
    }
  }

}

object UnitElement {

  /**
    * 获得UnitElement的实际枚举类型(Numeric,Text或Boolean)
    *
    * @return UnitElement的枚举类型[[UnitElementType]]
    */
  def actualType[T <: Comparable[T]](element: UnitElement[T]): UnitElementType = {
    Preconditions.checkNotNull(element)
    element match {
      case element: NumericElement => UnitElementType.NUMERIC_ELEMENT
      case element: TextElement => UnitElementType.TEXT_ELEMENT
      case element: BooleanElement => UnitElementType.BOOLEAN_ELEMENT
    }
  }

}

/**
  * UnitElement的枚举类型(Numeric,Text或Boolean)
  */
object UnitElementType extends Enumeration {

  type UnitElementType = Value

  val NUMERIC_ELEMENT, TEXT_ELEMENT, BOOLEAN_ELEMENT = Value

}
