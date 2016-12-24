package com.qunar.spark.diff.base.sort

/**
  * 字段名排序器的行为抽象
  */
trait Sorter {

  /**
    * 对指定的target序列排序并返回有序的序列结果
    */
  def sort[T <: Comparable[T]](target: Seq[T]): Seq[T]

}

object Sorter {

  def apply(): Sorter = new DoubleArrayTrieSorter

}
