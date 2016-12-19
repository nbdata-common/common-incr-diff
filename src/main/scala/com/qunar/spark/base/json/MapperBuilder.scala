package com.qunar.spark.base.json

import MapperBuilder._
import com.fasterxml.jackson.annotation.JsonInclude.Include
import com.fasterxml.jackson.core.{JsonGenerator, JsonParser}
import com.fasterxml.jackson.databind.{DeserializationFeature, MapperFeature, ObjectMapper, SerializationFeature}
import com.google.common.cache.{CacheBuilder, CacheLoader, LoadingCache}
import com.qunar.spark.base.json.JsonFeature.JsonFeatureValue

/**
  * 定制ObjectMapper的工厂
  * copied from qunar.common.api by @miao.yang and customize it
  */
class MapperBuilder {

  private var buildFeature = defaultFeatures

  def enable(jf: JsonFeatureValue): MapperBuilder = {
    buildFeature = jf.enable(buildFeature)
    this
  }

  def disable(jf: JsonFeatureValue): MapperBuilder = {
    buildFeature = jf.disable(buildFeature)
    this
  }

  def configure(jf: JsonFeatureValue, state: Boolean): MapperBuilder = {
    if (state) enable(jf) else disable(jf)
    this
  }

  def configure(enabled: Array[JsonFeatureValue], disabled: Array[JsonFeatureValue]): MapperBuilder = {
    for (jf <- enabled) {
      enable(jf)
    }
    for (jf <- disabled) {
      disable(jf)
    }
    this
  }

  def build(features: Long): ObjectMapper = buildMapper(features)

  def build: ObjectMapper = build(buildFeature)

  /**
    * 构造默认的ObjectMapper
    */
  def buildDefault: ObjectMapper = build(defaultFeatures)

}

object MapperBuilder {

  def create: MapperBuilder = new MapperBuilder

  /**
    * 工厂生成指定模板的ObjectMapper
    */
  def apply(feature: Long, isDefault: Boolean = false): ObjectMapper = buildMapperInternal(feature)

  // 默认的JsonFeature配置模板
  private val defaultFeatures: Long = JsonFeature.defaults

  private val cache: LoadingCache[Long, ObjectMapper] = CacheBuilder.newBuilder()
    .maximumSize(100)
    .build(new CacheLoader[Long, ObjectMapper]() {
      override def load(key: Long): ObjectMapper = buildMapperInternal(key)
    })

  private def buildMapperInternal(features: Long): ObjectMapper = {
    val mapper = new ObjectMapper
    for (jf <- JsonFeature.values) {
      configure(mapper, jf.getFeature, jf.isEnabled(features))
    }
    mapper
  }

  private def configure(mapper: ObjectMapper, feature: AnyRef, state: Boolean) {
    feature match {
      case SerializationFeature => mapper.configure(feature.asInstanceOf[SerializationFeature], state)
      case DeserializationFeature => mapper.configure(feature.asInstanceOf[DeserializationFeature], state)
      case JsonParser.Feature => mapper.configure(feature.asInstanceOf[JsonParser.Feature], state)
      case JsonGenerator.Feature => mapper.configure(feature.asInstanceOf[JsonGenerator.Feature], state)
      case MapperFeature => mapper.configure(feature.asInstanceOf[MapperFeature], state)
      case Include => if (state) mapper.setSerializationInclusion(feature.asInstanceOf[Include])
    }
  }

  private def buildMapper(features: Long): ObjectMapper = {
    cache.get(features)
  }

}
