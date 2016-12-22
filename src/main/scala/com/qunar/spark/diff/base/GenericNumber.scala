package com.qunar.spark.diff.base

import java.lang.{Byte, Double, Float, Long, Short}
import javax.validation.constraints.NotNull

import com.google.common.base.Preconditions
import com.qunar.spark.diff.base.NumberType.NumberType

/**
  * 数值类型的泛化与统一(实现Comparable[GenericNumber]接口以适配UnitElement的入参)
  * 统一Int,Long,Double,Float,Byte,Short等类型
  *
  * @param value 需要被包装的数值,不允许为null,否则构造失败,异常抛出
  */
class GenericNumber(@NotNull private val value: Number) extends Comparable[GenericNumber] {

  Preconditions.checkNotNull(value)

  /**
    * 匹配该数值的实际类型(Int,Long,Double,Float,Byte,Short等)
    */
  def numberType: NumberType = {
    value match {
      case value: Integer => NumberType.INT
      case value: Long => NumberType.LONG
      case value: Double => NumberType.DOUBLE
      case value: Float => NumberType.FLOAT
      case value: Byte => NumberType.BYTE
      case value: Short => NumberType.SHORT
    }
  }

  /* 以下是数值类型转换 */

  def intValue(): Integer = value.intValue()

  def longValue(): Long = value.longValue()

  def doubleValue(): Double = value.doubleValue()

  def floatValue(): Float = value.floatValue()

  def byteValue(): Byte = value.byteValue()

  def shortValue(): Short = value.shortValue()

  /**
    * 实现Comparable接口的比较方法compareTo
    * Notice:对于diff的功能需求,这里只实现两个待比较对象是否相等,并不比较大小
    *
    * @return 是否相等,相等:0   不相等:1
    */
  override def compareTo(anotherNumber: GenericNumber): Int = {
    if (anotherNumber == this) {
      0
    } else if (anotherNumber.numberType != this.numberType) {
      1
    } else {
      0 //todo
    }
  }

}

object NumberType extends Enumeration {

  type NumberType = Value

  val INT, LONG, DOUBLE, FLOAT, BYTE, SHORT = Value

}
