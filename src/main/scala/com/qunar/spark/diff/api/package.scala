package com.qunar.spark.diff

import com.qunar.spark.diff.api.enums.DifferType

/**
  * <span class="badge" style="float: right;">MAIN API COMPONENT</span>
  * [[com.qunar.spark.diff.api]]包是common-incr-diff项目对外开放的的public api
  * <p/>
  * 开发者可以使用该包内的[[com.qunar.spark.diff.api.scala.DiffTracer]]''(针对scala项目)''
  * 或[[com.qunar.spark.diff.api.java.JavaDiffTracer]]''(针对java项目)''
  * 实现bean的diff逻辑.
  */
package object api {

  type UnitDifferType = DifferType.UnitDifferType

  type CompositeDifferType = DifferType.CompositeDifferType

}
