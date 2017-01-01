package com.qunar.spark.diff.api.enums

import com.qunar.spark.diff.base.regular.elements.composite.CompositeElement
import com.qunar.spark.diff.base.regular.elements.unit.UnitElement

import scala.language.implicitConversions

/**
  * [[com.qunar.spark.diff.base.compare.regular.Differ]]的枚举类型(比较规则的类别)
  * 使用此枚举类以定制化构造自己的[[com.qunar.spark.diff.base.compare.regular.Differ]]比较器链
  * <p/>
  * NOTICE: 以下的各个类型均不出现空比较器([[com.qunar.spark.diff.base.compare.regular.unit.EmptyUnitDiffer]]以及
  * [[com.qunar.spark.diff.base.compare.regular.AbstractDiffer.defaultEmptyDiffer]])
  * 因为空比较器在比较器链中必须出现而且必须处于头节点的位置,在比较器链的构造工厂中此逻辑已经被固化
  */
object DifferType extends Enumeration {

  type DifferType = Value

  /**
    * 适用于[[UnitElement]]的[[com.qunar.spark.diff.base.compare.regular.Differ]]类型
    */
  sealed class UnitDifferType extends Val

  // 忽略UnitElement字段
  val UNIT_DIFF_IGNORE = new UnitDifferType
  // 允许UnitElement字段在指定范围内差异
  val UNIT_DIFF_RANGE = new UnitDifferType
  // UnitElement默认的比较:即绝对相等
  val UNIT_DEFAULT = new UnitDifferType

  implicit def convertToUnit(v: Value): UnitDifferType = v.asInstanceOf[UnitDifferType]

  /**
    * 适用于[[CompositeElement]]的[[com.qunar.spark.diff.base.compare.regular.Differ]]类型
    */
  sealed class CompositeDifferType extends Val

  // 忽略CompositeElement字段
  val COMPOSITE_DIFF_IGNORE = new CompositeDifferType
  // 强制CompositeElement字段的容器内元素有序
  val COMPOSITE_SORT = new CompositeDifferType
  // CompositeElement默认的比较:即name相同
  val COMPOSITE_DEFAULT = new CompositeDifferType

  implicit def convertToComposite(v: Value): CompositeDifferType = v.asInstanceOf[CompositeDifferType]

}
