package com.qunar.spark.diff.base.regular.elements.composite

/**
  * [[CompositeElement]]下的特殊子类型:数组类型
  * 该类型的特点:容器内所有的内容都是同一种类型,
  * 即[[com.qunar.spark.diff.base.regular.elements.unit.UnitElement]]或[[CompositeElement]]类型,
  * 但不包括[[ArrayElement]]本身,否则就会变成''高维数组'',那样的处理会增加很大难度与复杂度,故暂不考虑
  */
abstract class ArrayElement extends CompositeElement {

}
