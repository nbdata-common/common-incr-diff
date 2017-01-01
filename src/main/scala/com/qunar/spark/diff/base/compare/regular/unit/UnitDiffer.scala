package com.qunar.spark.diff.base.compare.regular.unit

import javax.validation.constraints.NotNull

import com.qunar.spark.diff.base.compare.regular.AbstractDiffer
import com.qunar.spark.diff.base.regular.elements.unit.{BooleanElement, NumericElement, TextElement, UnitElement}
import com.qunar.spark.diff.base.regular.elements.Element

/**
  * 递归结构中,针对[[UnitElement]]的diff比较器的行为抽象
  */
abstract class UnitDiffer(@NotNull private val decoratedDiffer: AbstractDiffer) extends AbstractDiffer(decoratedDiffer) {

  private[unit] def this() = this(AbstractDiffer.defaultEmptyDiffer)

  /**
    * 用于比较两个[[UnitElement]]是否相同
    * 适用于此方法的类型包括:
    * [[NumericElement]]
    * [[TextElement]]
    * [[BooleanElement]]
    *
    * @throws IllegalArgumentException 当传入的两个[[UnitElement]]的实际类型不同时抛出此异常
    */
  @throws(classOf[IllegalArgumentException])
  protected def compareUnitElement(element1: UnitElement[_], element2: UnitElement[_]): Boolean

  /**
    * 实现父类方法,用于比较两个[[UnitElement]]是否相同
    * 当传入的两个[[Element]]有一个或以上不是[[UnitElement]]时返回false
    */
  override protected def compareInternal(element1: Element, element2: Element): Boolean = {
    (element1, element2) match {
      case elements: (UnitElement[_], UnitElement[_]) => compareUnitElement(elements._1, elements._2)
      case _ => false
    }
  }

}
