package com.qunar.spark.diff.internal.impl.regular

import com.qunar.spark.diff.api.scala.DiffTracer
import com.qunar.spark.diff.base.compare.regular.Differ
import com.qunar.spark.diff.base.regular.elements.unit.UnitElement
import com.qunar.spark.diff.base.regular.elements.Element
import com.qunar.spark.diff.base.regular.elements.composite.{ArrayElement, CompositeElement}
import com.qunar.spark.diff.base.sort.Sorter

import scala.collection.mutable
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
  protected final def isElementDifferent(targetLeft: Element, targetRight: Element): Boolean = {
    /* 初始化 */
    val stackLeft: mutable.Stack[Element] = mutable.Stack(targetLeft)
    val stackRight: mutable.Stack[Element] = mutable.Stack(targetRight)
    // 当前处理的指针,指向处理的对象
    var pointerLeft = stackLeft.top
    var pointerRight = stackRight.top

    /* diff过程 */

    while (stackLeft.nonEmpty && stackRight.nonEmpty) {
      pointerLeft = stackLeft.top
      pointerRight = stackRight.top
      stackLeft.pop
      stackRight.pop
      (pointerLeft, pointerRight) match {
        // 对ArrayElement作单独处理
        case pointers: (ArrayElement, ArrayElement) => //todo

        // 对其他CompositeElement的通用处理
        case pointers: (CompositeElement, CompositeElement) =>
          val leftSorted = innerSorter.sort(pointers._1.childrenElements())
          val rightSorted = innerSorter.sort(pointers._2.childrenElements())
          if (!isDifferentInternal(leftSorted, rightSorted)) {
            stackLeft.pushAll(leftSorted)
            stackRight.pushAll(rightSorted)
          } else {
            true
          }

        // 如果是两个UnitElement作空处理,因为在上一个case中已经比较过,所以能走到这里肯定是相同的
        case pointers: (UnitElement[_], UnitElement[_]) =>

        // 匹配至其他情况,肯定是不同的,一律返回true
        case _ => true
      }
    }
    // 跳出while循环,如果两者相同,理论上应该两个stack都为空
    // 只要有一个不为空的stack,就是different的
    if (stackLeft.nonEmpty || stackRight.nonEmpty) true else false
  }

  /**
    * 对两个[[Element]]序列从前到后依次判断对应元素是否相同
    */
  private def isDifferentInternal(left: Seq[Element], right: Seq[Element]): Boolean = {
    for (elemLeft <- left; elemRight <- right) {
      if (!innerDiffer.compare(elemLeft, elemRight)) {
        true
      }
    }
    false
  }

}
