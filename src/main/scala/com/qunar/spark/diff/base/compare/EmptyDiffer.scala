package com.qunar.spark.diff.base.compare

import com.qunar.spark.diff.base.regular.elements.UnitElement

/**
  * 空比较器
  * 如果说使用装饰器模式串起来的比较器链是一个链表,
  * 那么空比较器就是链表最末端的null指针
  * 此类不允许被继承
  */
final class EmptyDiffer extends Differ(this) {

  override def isDifferent[T <: Comparable[T]](element1: UnitElement[T], element2: UnitElement[T]): Boolean = {

    false
  }

  private def ckeckTypeConsistent[T](element1: UnitElement[T], element2: UnitElement[T]): Boolean = {
    false
  }

}
