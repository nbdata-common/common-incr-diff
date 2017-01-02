package com.qunar.spark.diff.internal.impl.regular.jackson

import com.fasterxml.jackson.databind.JsonNode
import com.qunar.spark.diff.api.enums.DifferType
import com.qunar.spark.diff.base.ReAssignableArrayBuffer
import com.qunar.spark.diff.base.compare.regular.Differ
import com.qunar.spark.diff.base.regular.elements.unit.UnitElement
import com.qunar.spark.diff.base.regular.elements.Element
import com.qunar.spark.diff.base.regular.elements.composite.{ArrayElement, CompositeElement}
import com.qunar.spark.diff.base.sort.Sorter
import com.qunar.spark.diff.internal.impl.regular.{AnnotatedElement, RegularDiffTracer}
import com.qunar.spark.diff.internal.impl.regular.jackson.element.JacksonElementDriver
import com.qunar.spark.diff.internal.impl.regular.jackson.json.JsonMapper

import scala.collection.mutable
import scala.reflect.ClassTag

/**
  * 适用于Jackson的[[com.qunar.spark.diff.api.scala.DiffTracer]]实现类[[JacksonDiffTracer]]
  */
private[diff] final class JacksonDiffTracer[T: ClassTag](val differ: Differ, val sorter: Sorter) extends RegularDiffTracer[T] {

  /**
    * 辅助构造器:以默认的比较器和字段排序器初始化
    */
  def this() = this(Differ(), Sorter())

  override protected val innerDiffer: Differ = differ

  override protected val innerSorter: Sorter = sorter

  // 传入的bean所对应的Class
  private val clazz = getClassT

  private def getClassT(implicit ct: ClassTag[T]): Class[_] = {
    ct.runtimeClass
  }


  /* main api */

  /**
    * 实现[[com.qunar.spark.diff.api.scala.DiffTracer]]中的public方法
    * <p/>
    * NOTICE: 传给[[isDifferentInternal]]方法的第三个参数ElementWrapper,这里使用了
    * [[adaptiveWrapper]],主要是为了根据装配的[[Differ]]灵活地控制包装的深度,以确保性能
    */
  override def isDifferent(targetLeft: T, targetRight: T): Boolean = {
    val nodeLeft = beanToJsonNode(targetLeft)
    val nodeRight = beanToJsonNode(targetRight)
    isDifferentInternal(nodeLeft, nodeRight, adaptiveWrapper)
  }

  /**
    * 对外开放的第二类api:  以[[JsonNode]]为入参作diff比较
    *
    * NOTICE: 当外部直接调用此api时,将不会开启注解增强功能
    * 若想使用注解增强,请使用:[[com.qunar.spark.diff.api.scala.DiffTracer.isDifferent]]
    */
  def isDifferent(targetLeft: JsonNode, targetRight: JsonNode): Boolean = {
    isDifferentInternal(targetLeft, targetRight, direct _)
  }


  /* inner implement */

  /**
    * 将java bean转为[[JsonNode]]
    */
  private def beanToJsonNode(target: T): JsonNode = {
    JsonMapper.readTree(JsonMapper.writeValueAsString(target))
  }

  /**
    * 内部的isDifferent处理
    *
    * @param wrapElement 这是一个对[[Element]]的包装函数,用于扩展[[Element]]的功能,以对应
    *                    [[com.qunar.spark.diff.base.compare.regular.AbstractDiffer]]的
    *                    继承者们的各种diff规则.
    */
  private def isDifferentInternal(target1: JsonNode, target2: JsonNode, wrapElement: (Element) => Element = direct): Boolean = {
    val node1 = jsonNodeToElement(target1, wrapElement)
    val node2 = jsonNodeToElement(target2, wrapElement)
    isElementDifferent(node1, node2)
  }

  /**
    * [[JacksonDiffTracer]]的核心逻辑: 将[[JsonNode]]转换为[[Element]]
    * 这里使用显式堆栈的递归方式创建[[Element]]的层次关系,以提高程序执行的效率,映射到[[JsonNode]]的层次关系
    * <p/>
    * 堆栈的填充内容类型是([[JsonNode]], [[Element]]),其代表的意义是:
    * 某个[[JsonNode]]所对应的[[Element]]
    */
  private def jsonNodeToElement(node: JsonNode, wrapElement: (Element) => Element): Element = {
    /* 初始化 */
    // 最终要返回的目标Element
    val result: Element = wrapElement(JacksonElementDriver.makeElement(node))
    // 初始化
    val stack: mutable.Stack[(JsonNode, Element)] = mutable.Stack((node, result))
    // 当前处理的指针,指向处理的对象
    var pointer = stack.top
    // 孩子Elements列表(初始化size = 32)
    val childrenElements = ReAssignableArrayBuffer[Element](32)

    /* 以下为显式堆栈处理过程 */

    while (stack.nonEmpty) {
      pointer = stack.top
      stack.pop
      // 栈顶元组的第二项:Element
      val topElement = pointer._2
      topElement match {
        // 映射到CompositeElement的子类型ArrayElement的处理
        case topElement: ArrayElement => //todo

        // 映射到除了ArrayElement的其他CompositeElement类型的处理
        case topElement: CompositeElement =>
          // entries的类型:(String, JsonNode)
          val entries = JacksonElementDriver.childrenNodesWithName(pointer._1)
          childrenElements.reset()
          // 提取element
          for (entry <- entries) {
            val element = wrapElement(JacksonElementDriver.makeElement(entry._2, entry._1))
            stack.push((entry._2, element))
            childrenElements += element
          }
          // 设置孩子Elements
          topElement.setChildrenElements(childrenElements.compactCopy)

        // 映射到UnitElement类型
        case topElement: UnitElement[_] => // 其他情况作空处理
      }
    }

    // 返回目标
    result
  }


  /* element wrapper functions */

  /**
    * 适应性地对[[Element]]包装
    * 根据传入的[[Differ]]适应性地选择最轻量化的包装函数,以提升diff性能
    */
  private val adaptiveWrapper: (Element) => Element = {
    val annotatedDifferType = Seq(DifferType.UNIT_DIFF_IGNORE, DifferType.UNIT_DIFF_RANGE, DifferType.COMPOSITE_DIFF_IGNORE)
    if (innerDiffer.differChainSnapshot.forall(differType => {
      if (!annotatedDifferType.contains(differType)) false else true
    }))
      direct
    else
      annotatedWrap
  }

  /**
    * 直接将元素作directly透传
    */
  private def direct(element: Element): Element = element

  /**
    * 将元素包装为注解感知([[com.qunar.spark.diff.ext.AnnotationAware]])的元素
    */
  private def annotatedWrap(element: Element): Element = AnnotatedElement(element, clazz)

}
