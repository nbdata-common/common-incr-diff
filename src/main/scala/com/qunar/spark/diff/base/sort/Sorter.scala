package com.qunar.spark.diff.base.sort

/**
  * 字段名排序器的行为抽象
  */
trait Sorter {

  /**
    * 对指定的target序列排序并返回有序的序列结果
    */
  def sort[E <: Comparable[E]](target: Seq[E]): Seq[E]

}

object Sorter {

  def apply(): Sorter = new DoubleArrayTrieSorter

}
