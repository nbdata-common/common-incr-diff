package com.qunar.spark.diff.internal.impl.regular

import com.qunar.spark.diff.api.scala.DiffTracer
import com.qunar.spark.diff.base.compare.regular.Differ
import com.qunar.spark.diff.base.regular.elements.Element
import com.qunar.spark.diff.base.sort.Sorter

import scala.reflect.ClassTag

/**
  * 递归结构的DiffTracer通用抽象类
  */
abstract class RegularDiffTracer[T: ClassTag] extends DiffTracer[T] {

  // 用于diff的比较器,由子类负责实例化
  protected val innerDiffer: Differ

  // 用于diff的字段排序器,由子类负责实例化
  protected val innerSorter: Sorter

  /**
    * 统一递归结构的diff方法
    * <p/>
    * [[Element]]类型的入参作为递归结构的抽象基础类型,
    * 使该方法可以在统一的high level层面上作逻辑处理,屏蔽了底层数据源的类型差异化.
    * 所有递归结构的DiffTracer的继承者们只需要实现从泛型入参[[T]]到[[Element]]及其子类的转换
    * 即可间接实现diff功能.
    */
  protected def isDifferent(target1: Element, target2: Element): Boolean = {

    false
  }

}
