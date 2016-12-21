package com.qunar.spark.diff.base.regular.elements

/**
  * 在规则(递归)结构中,表征一个原子元素(即单元节点,不可继续拆分)
  * 单元节点必须满足:其唯一映射到一个T类型的value,该value已实现Comparable[T]
  */
trait UnitElement[T <: Comparable[T]] extends Element {

  def value: T

}
