package com.qunar.spark.diff.base.compare.regular

import javax.validation.constraints.NotNull

import com.google.common.base.Preconditions
import com.qunar.spark.diff.base.regular.elements.Element

/**
  * 完全抽象的diff行为(囊括Unit与Composite)
  *
  * @param decoratedDiffer 被装饰的前置比较器,不允许为null,否则构造失败,异常抛出
  */
abstract class AbstractDiffer(@NotNull private val decoratedDiffer: AbstractDiffer) {

  Preconditions.checkNotNull(decoratedDiffer)

  /**
    * 比较行为
    *
    * @throws java.lang.IllegalArgumentException 详见[[com.qunar.spark.diff.base.compare.regular.unit.UnitDiffer]]
    * @return 是否相同
    */
  @throws(classOf[IllegalArgumentException])
  final def compare(element1: Element, element2: Element): Boolean = {
    decoratedDiffer.compare(element1, element2) || compareInternal(element1, element2)
  }

  protected def compareInternal(element1: Element, element2: Element): Boolean

}
