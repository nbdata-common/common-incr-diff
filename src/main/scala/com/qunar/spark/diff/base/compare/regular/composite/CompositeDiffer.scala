package com.qunar.spark.diff.base.compare.regular.composite

import javax.validation.constraints.NotNull

import com.qunar.spark.diff.base.compare.regular.AbstractDiffer
import com.qunar.spark.diff.base.regular.elements.{CompositeElement, Element}

/**
  * 递归结构中,针对CompositeElement的diff比较器的行为抽象
  *
  * @param decoratedDiffer 被装饰的前置比较器,不允许为null,否则构造失败,异常抛出
  */
abstract class CompositeDiffer(@NotNull private val decoratedDiffer: CompositeDiffer) extends AbstractDiffer(decoratedDiffer) {

  protected def compareCompositeElement(element1: CompositeElement, element2: CompositeElement): Boolean

  override protected def compareInternal(element1: Element, element2: Element): Boolean = {
    (element1, element2) match {
      case elements: (CompositeElement, CompositeElement) => compareCompositeElement(elements._1, elements._2)
      case _ => false
    }
  }

}
