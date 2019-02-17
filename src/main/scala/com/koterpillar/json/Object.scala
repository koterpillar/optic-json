package com.koterpillar.json

import io.circe.{Json, JsonObject}
import io.circe.syntax._
import monocle.Prism
import shapeless._

trait Field_ {
  def name: String
  def schema: Schema[_]
}

case class Field[A](name: String, schema: Schema[A]) extends Field_

sealed trait Object[A] extends Schema[A] {
  def fields: List[Field_]

  override def schema: Json = Json.obj(
    "type" -> "object".asJson,
    "fields" -> Json.obj((for { field <- fields } yield {
      field.name -> field.schema.schema
    }): _*),
    "required" -> fields.map(_.name).asJson,
  )

  def objectCodec: Prism[JsonObject, A]

  override def codec: Prism[Json, A] =
    Object.jsonObject.composePrism(objectCodec)
}

object Object {
  def jsonObject: Prism[Json, JsonObject] =
    Prism[Json, JsonObject](_.asObject)(Json.fromJsonObject)
}

case object NullObject extends Object[HNil] {
  override def fields: List[Field_] = Nil

  override def objectCodec: Prism[JsonObject, HNil] =
    Prism[JsonObject, HNil] { obj =>
      if (obj.toMap == Map.empty) Some(HNil) else None
    }(_ => JsonObject.empty)
}

final case class ConsObject[H, T <: HList](head: Field[H], tail: Object[T])
    extends Object[H :: T] {
  override def fields: List[Field_] = head :: tail.fields

  override def objectCodec: Prism[JsonObject, H :: T] = {
    def getOption(json: JsonObject): Option[H :: T] =
      for {
        headJson  <- json(head.name)
        headValue <- head.schema.codec.getOption(headJson)
        tailJson = json.remove(head.name)
        tailValue <- tail.objectCodec.getOption(tailJson)
      } yield headValue :: tailValue

    def reverseGet(value: H :: T): JsonObject = {
      val headValue :: tailValue = value
      val result                 = tail.objectCodec.reverseGet(tailValue)
      result.add(head.name, head.schema.codec.reverseGet(headValue))
    }

    Prism(getOption)(reverseGet)
  }
}
