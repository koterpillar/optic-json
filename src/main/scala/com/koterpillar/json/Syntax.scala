package com.koterpillar.json

import shapeless._

import scala.language.implicitConversions

trait Syntax {
  def product[H, T <: HList](head: Field[H], tail: Object[T]): Object[H :: T] = ConsObject(head, tail)

  def field[A: Schema](name: String) = Field(name, implicitly[Schema[A]])

  def emptyObject: Object[HNil] = NullObject

  implicit def toSchemaOps[A](schema: Schema[A]): SchemaOps[A] = new SchemaOps[A](schema)
}
