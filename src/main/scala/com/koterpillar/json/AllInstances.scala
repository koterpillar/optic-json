package com.koterpillar.json
import io.circe.Json

trait AllInstances {
  def simpleType(typeValue: String): Json = Json.obj("type" -> Json.fromString(typeValue))
  def number: Json = simpleType("number")
  def string: Json = simpleType("string")
  def boolean: Json = simpleType("boolean")

  implicit val intSchema: Schema[Int] = new Schema[Int] {
    override def schema: Json = number
  }

  implicit val stringSchema: Schema[String] = new Schema[String] {
    override def schema: Json = string
  }

  implicit val booleanSchema: Schema[Boolean] = new Schema[Boolean] {
    override def schema: Json = boolean
  }
}
