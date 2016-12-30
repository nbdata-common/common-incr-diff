package com.qunar.spark.diff.base.compare.regular.composite

import com.qunar.spark.diff.base.regular.elements.CompositeElement

/**
  * 针对[[CompositeElement]]的空比较器
  * 如果说使用装饰器模式串起来的比较器链是一个链表,那么空比较器就是链表最末端的null指针
  *
  * NOTICE: 此类不允许被继承
  */
final class EmptyCompositeDiffer extends CompositeDiffer {

  /**
    * 对于CompositeElement来说,空比较器不存在任何逻辑,直接返回false即可
    */
  override protected def compareCompositeElement(element1: CompositeElement, element2: CompositeElement): Boolean = false

}
