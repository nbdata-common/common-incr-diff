package com.qunar.spark.diff.internal.impl.regular

import java.lang.reflect.Field

import com.qunar.spark.diff.base.regular.elements.UnitElement
import com.qunar.spark.diff.base.regular.elements.ext.api.ExtUnitElement
import com.qunar.spark.diff.ext.AnnotationAware

/**
  * [功能拓展] 递归结构中,带有注解感知能力的原子元素
  */
class AnnotatedUnitElement[T](private val decoratedElement: UnitElement[T]) extends ExtUnitElement(decoratedElement)
  with AnnotationAware {

  override def mappedField: Field = {
    null
  }

}
