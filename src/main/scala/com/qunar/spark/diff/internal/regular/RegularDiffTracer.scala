package com.qunar.spark.diff.internal.regular

import com.qunar.spark.diff.api.scala.DiffTracer
import com.qunar.spark.diff.base.regular.elements.CompositeElement

/**
  * 规则(递归)结构的通用抽象类
  */
abstract class RegularDiffTracer[T] extends DiffTracer[T] {

  /**
    * 统一递归结构的diff方法
    * <p/>
    * NodeElement类型的入参作为递归结构的抽象复合类型,
    * 使该方法可以在统一的high level上作逻辑处理.
    * 所有递归结构的DiffTracer的继承者只需要实现从泛型入参T到CompositeElement的转换即可
    */
  protected def isDifferent(target1: CompositeElement, target2: CompositeElement): Boolean = {
    false
  }

}
