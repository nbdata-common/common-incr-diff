package com.qunar.spark.diff.base.compare

import com.qunar.spark.diff.base.regular.elements.UnitElement

/**
  * 带有注解拦截的diff比较器
  */
abstract class AnnotationInterceptDiffer(private val decorateDiffer: Differ) extends Differ(decorateDiffer) {

  /**
    * 判断当前注解是否对指定元素生效
    */
  protected def isAnnotationEffective[T](element: UnitElement[T]): Boolean

  protected def isDifferentUnderAnnotation[T](element1: UnitElement[T], element2: UnitElement[T]): Boolean

  /**
    * 注解拦截diff比较器的AnnotationInterceptDiffer#isDifferent方法的标准实现
    * 此方法不允许被重写,子类的isDifferent都必须遵从此方法的逻辑:
    * 1.被装饰的differ的diff判断  2.注解是否是有效的  3.在注解规则下两个对象是否不同
    */
  override final def isDifferent[T <: Comparable[T]](element1: UnitElement[T], element2: UnitElement[T]): Boolean = {
    decorateDiffer.isDifferent(element1, element2) || (isAnnotationEffective(element1) && isDifferentUnderAnnotation(element1, element2))
  }

}
