package com.qunar.spark.diff.base.compare.regular.composite

import javax.validation.constraints.NotNull

import com.qunar.spark.diff.base.compare.regular.AbstractDiffer
import com.qunar.spark.diff.base.regular.elements.Element
import com.qunar.spark.diff.base.regular.elements.composite.CompositeElement

/**
  * 递归结构中,针对[[CompositeElement]]的diff比较器的行为抽象
  */
abstract class CompositeDiffer(@NotNull private val decoratedDiffer: AbstractDiffer) extends AbstractDiffer(decoratedDiffer) {

  private[composite] def this() = this(AbstractDiffer.defaultEmptyDiffer)

  /**
    * 用于比较两个[[CompositeElement]]是否相同
    */
  protected def compareCompositeElement(element1: CompositeElement, element2: CompositeElement): Boolean

  /**
    * 实现父类方法,用于比较两个[[CompositeElement]]是否相同
    * 当传入的两个[[Element]]有一个或以上不是[[CompositeElement]]时返回false
    */
  override protected def compareInternal(element1: Element, element2: Element): Boolean = {
    (element1, element2) match {
      case elements: (CompositeElement, CompositeElement) => compareCompositeElement(elements._1, elements._2)
      case _ => false
    }
  }

}
