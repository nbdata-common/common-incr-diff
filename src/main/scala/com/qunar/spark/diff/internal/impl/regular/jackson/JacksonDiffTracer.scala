package com.qunar.spark.diff.internal.impl.regular.jackson

import com.fasterxml.jackson.databind.JsonNode
import com.qunar.spark.diff.base.ReAssignableArrayBuffer
import com.qunar.spark.diff.base.compare.regular.Differ
import com.qunar.spark.diff.base.regular.elements.{CompositeElement, Element, UnitElement}
import com.qunar.spark.diff.base.sort.Sorter
import com.qunar.spark.diff.internal.impl.regular.{AnnotatedElement, RegularDiffTracer}
import com.qunar.spark.diff.internal.impl.regular.jackson.element.JacksonElementDriver
import com.qunar.spark.diff.internal.impl.regular.jackson.json.JsonMapper

import scala.collection.mutable
import scala.reflect.ClassTag

/**
  * 适用于Jackson的incr-diff实现类
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
    * 重写DiffTracer中的public方法
    */
  override def isDifferent(target1: T, target2: T): Boolean = {
    val node1 = beanToJsonNode(target1)
    val node2 = beanToJsonNode(target2)
    isDifferentInternal(node1, node2, annotatedWrap _)
  }

  /**
    * 对外开放的第二种api:  以JsonNode为入参作diff比较
    *
    * NOTICE: 当外部直接调用此api时,将不会开启注解增强功能
    * 若想使用注解增强,请使用:[[com.qunar.spark.diff.api.scala.DiffTracer.isDifferent]]
    */
  def isDifferent(target1: JsonNode, target2: JsonNode): Boolean = {
    isDifferentInternal(target1, target2, direct _)
  }


  /* inner implement */

  /**
    * 将java bean转为JsonNode
    */
  private def beanToJsonNode(target: T): JsonNode = {
    JsonMapper.readTree(JsonMapper.writeValueAsString(target))
  }

  /**
    * 内部的isDifferent处理
    */
  private def isDifferentInternal(target1: JsonNode, target2: JsonNode, wrapElement: (Element) => Element = direct): Boolean = {
    val node1 = jsonNodeToElement(target1, wrapElement)
    val node2 = jsonNodeToElement(target2, wrapElement)
    isDifferent(node1, node2)
  }

  /**
    * JacksonDiffTracer的核心逻辑: 将JsonNode转换为Element
    * 这里使用显式堆栈的递归方式创建Element的层次关系,以提高程序执行的效率,映射到JsonNode的层次关系
    * <p/>
    * 堆栈的填充内容类型是(JsonNode, Element),其代表的意义是:
    * 某个JsonNode所对应的Element
    */
  private def jsonNodeToElement(node: JsonNode, wrapElement: (Element) => Element): Element = {
    // 最终要返回的目标Element
    val result: Element = wrapElement(JacksonElementDriver.makeElement(node))
    // 初始化
    val stack: mutable.Stack[(JsonNode, Element)] = mutable.Stack((node, result))

    /* 以下为显式堆栈处理过程 */

    // 当前处理的指针,指向处理的对象
    var pointer = stack.top
    // 孩子Elements列表(初始化size = 32)
    val childrenElements = ReAssignableArrayBuffer[Element](32)

    while (stack.nonEmpty) {
      pointer = stack.top
      stack.pop
      // 栈顶元组的第二项:Element
      val topElement = pointer._2
      topElement match {
        case topElement: CompositeElement =>
          // entries的类型:(String, JsonNode)
          val entries = JacksonElementDriver.childrenNodesWithName(pointer._1)
          childrenElements.reset()
          // todo 预扩容
          // preProbableExpansion(childrenElements, entries.iterator)
          // 提取element
          for (entry <- entries) {
            val element = wrapElement(JacksonElementDriver.makeElement(entry._2, entry._1))
            stack.push((entry._2, element))
            childrenElements += element
          }
          // 设置孩子Elements
          topElement.setChildrenElements(childrenElements.compactCopy)

        case _ => // 其他情况作空处理
      }
    }

    // 返回目标
    result
  }

  /**
    * 预先处理可能的扩容,确保一次到位,避免重复GC
    */
  private def preProbableExpansion(childrenElements: ReAssignableArrayBuffer[Element], expectData: Iterator[_]) = {
    var childrenNum = 0
    while (expectData.hasNext) {
      childrenNum += 1
    }
    childrenElements.preProbableExpansion(childrenNum)
  }


  /* element wrapper functions */

  /**
    * 直接将元素作directly透传
    */
  private def direct(element: Element): Element = element

  /**
    * 将元素包装为注解感知的元素
    */
  private def annotatedWrap(element: Element): Element = AnnotatedElement(element, clazz)

}
