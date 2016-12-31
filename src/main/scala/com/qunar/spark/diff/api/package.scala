package com.qunar.spark.diff

import com.qunar.spark.diff.api.enums.DifferType

/**
  * <span class="badge" style="float: right;">ALPHA COMPONENT</span>
  * [[com.qunar.spark.diff.api]]包是对外开放的common-incr-diff的public api
  */
package object api {

  type UnitDifferType = DifferType.UnitDifferType

  type CompositeDifferType = DifferType.CompositeDifferType

}
