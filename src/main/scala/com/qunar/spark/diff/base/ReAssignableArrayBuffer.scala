package com.qunar.spark.diff.base

import com.google.common.collect.Iterators

import scala.collection.mutable.ArrayBuffer
import scala.collection.JavaConversions._

/**
  * 可以被新数组重新赋值的GC友好的可变数组
  */
class ReAssignableArrayBuffer[A](initSize: Int) extends Seq[A] {

  // 内部维护一个ArrayBuffer
  private val innerArray = new ArrayBuffer[A](initSize)

  /**
    * 以下三个成员,受了[[java.nio.Buffer]]的启发,并在其上作了针对性的改造
    * 三者关系: position <= limit <= capacity
    */
  private var position = 0
  private var limit = 0
  private var capacity = initSize

  /**
    * 重置状态,表明数组原有内部信息已被清空
    */
  def reset(): Unit = {
    position = 0
  }

  /**
    * 追加一个元素
    */
  def +=(elem: A): this.type = {
    // 不需要扩容
    if (position < capacity) {
      innerArray.update(position, elem)
    }
    // 需要扩容
    else {
      innerArray += elem
      capacity = innerArray.length
    }
    position += 1

    this
  }



  /* 实现Seq[A]的方法 */

  override def iterator: Iterator[A] = {
    limit = position
    Iterators.limit[A](innerArray.iterator, limit)
  }

  override def length: Int = position

  override def apply(idx: Int): A = innerArray(idx)

}

object ReAssignableArrayBuffer extends {

  def apply[A](initSize: Int): ReAssignableArrayBuffer[A] = new ReAssignableArrayBuffer(initSize)

}
