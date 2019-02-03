package com.koterpillar.json
import io.circe._
import io.circe.syntax._
import monocle.Prism

trait AllInstances {
  def simpleType(typeValue: String): Json = Json.obj("type" -> typeValue.asJson)
  def number: Json = simpleType("number")
  def string: Json = simpleType("string")
  def boolean: Json = simpleType("boolean")

  implicit val intSchema: Schema[Int] = new Schema[Int] {
    override def schema: Json = number

    override def codec: Prism[Json, Int] = Prism[Json, Int] {
      obj => obj.asNumber.flatMap(_.toInt)
    }(Json.fromInt)
  }

  implicit val stringSchema: Schema[String] = new Schema[String] {
    override def schema: Json = string

    override def codec: Prism[Json, String] = Prism[Json, String](_.asString)(Json.fromString)
  }

  implicit val booleanSchema: Schema[Boolean] = new Schema[Boolean] {
    override def schema: Json = boolean

    override def codec: Prism[Json, Boolean] = Prism[Json, Boolean](_.asBoolean)(Json.fromBoolean)
  }
}
