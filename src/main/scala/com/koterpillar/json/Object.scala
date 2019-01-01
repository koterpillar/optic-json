package com.koterpillar.json

import io.circe.Json
import io.circe.syntax._

trait Field_ {
  def name: String
  def schema: Schema[_]
}

case class Field[A](name: String, schema: Schema[A]) extends Field_

trait Object[A] extends Schema[A] {
  def fields: List[Field_]

  override def schema: Json = Json.obj(
    "type" -> "object".asJson,
    "fields" -> Json.obj((for { field <- fields} yield { field.name -> field.schema.schema }): _*),
    "required" -> fields.map(_.name).asJson,
  )
}

case object NullObject extends Object[Unit] {
  override def fields: List[Field_] = Nil
}

case class ConsObject[H, T](head: Field[H], tail: Object[T]) extends Object[(H, T)] {
  override def fields: List[Field_] = head :: tail.fields
}
