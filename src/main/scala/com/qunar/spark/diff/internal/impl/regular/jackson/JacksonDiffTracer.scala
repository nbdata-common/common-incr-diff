package com.qunar.spark.diff.internal.impl.regular.jackson

import com.fasterxml.jackson.databind.JsonNode
import com.qunar.spark.diff.base.compare.regular.Differ
import com.qunar.spark.diff.base.regular.elements.Element
import com.qunar.spark.diff.base.sort.Sorter
import com.qunar.spark.diff.internal.impl.regular.{AnnotatedElement, RegularDiffTracer}
import com.qunar.spark.diff.internal.impl.regular.jackson.element.JacksonNumericElement
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


  /* main api and its dependencies */

  /**
    * 重写DiffTracer中的public方法
    */
  override def isDifferent(target1: T, target2: T): Boolean = {
    val node1 = beanToJsonNode(target1)
    val node2 = beanToJsonNode(target2)
    isDifferentInternal(node1, node2, annotatedWrap _)
  }

  /**
    * 将java bean转为JsonNode
    */
  private def beanToJsonNode(target: T): JsonNode = {
    JsonMapper.readTree(JsonMapper.writeValueAsString(target))
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

  private def isDifferentInternal(target1: JsonNode, target2: JsonNode, wrapElement: (Element) => Element = direct): Boolean = {
    val node1 = jsonNodeToElement(target1, wrapElement)
    val node2 = jsonNodeToElement(target2, wrapElement)
    isDifferent(node1, node2)
  }

  /**
    * JacksonDiffTracer的核心逻辑: 将JsonNode转换为Element
    * 这里使用显式堆栈的递归方式创建Element的层次关系,以提高程序执行的效率,映射到JsonNode的层次关系
    */
  private def jsonNodeToElement(node: JsonNode, wrapElement: (Element) => Element): Element = {
    // 最终要返回的目标Element
    val result: Element = wrapElement(null)
    val stack: mutable.Stack[(JsonNode, Element)] = mutable.Stack[(JsonNode, Element)]((node, result))
    while (stack.nonEmpty) {

    }
    result
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
