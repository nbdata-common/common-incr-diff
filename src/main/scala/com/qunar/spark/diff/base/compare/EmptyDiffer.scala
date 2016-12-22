package com.qunar.spark.diff.base.compare

import com.qunar.spark.diff.base.regular.elements.UnitElementType.UnitElementType
import com.qunar.spark.diff.base.regular.elements.UnitElement

/**
  * 空比较器
  * 如果说使用装饰器模式串起来的比较器链是一个链表,
  * 那么空比较器就是链表最末端的null指针
  * 此类不允许被继承
  */
final class EmptyDiffer extends Differ(new EmptyDiffer) {

  override def isDifferent[T <: Comparable[T]](element1: UnitElement[T], element2: UnitElement[T]): Boolean = {
    checkTypeConsistent(element1, element2)
  }

  /**
    * 检查传入的两个参数是否拥有相同的实际类型
    * 如果不一致,则抛出IllegalArgumentException
    * 否则返回false,以确保比较器链正常向下游执行
    */
  private def checkTypeConsistent[T <: Comparable[T]](element1: UnitElement[T], element2: UnitElement[T]): Boolean = {
    val elemType1: UnitElementType = UnitElement.actualType(element1)
    val elemType2: UnitElementType = UnitElement.actualType(element2)
    if (elemType1 != elemType2) {
      throw new IllegalArgumentException("element1 and element2 don't share the same actual type")
    }

    false
  }

}
