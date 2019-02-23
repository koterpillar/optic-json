package com.koterpillar.json

import monocle.Iso
import shapeless._

import scala.language.implicitConversions

trait Syntax {
  def product[H, T <: HList](head: Field[H], tail: Object[T]): Object[H :: T] =
    ConsObject(head, tail)

  def field[A: Schema](name: String) = Field(name, implicitly[Schema[A]])

  implicit def toSchemaOps[A](schema: Schema[A]): SchemaOps[A] =
    new SchemaOps[A](schema)

  // From https://github.com/julien-truffaut/Monocle/blob/master/generic/shared/src/main/scala/monocle/generic/Generic.scala

  def toGeneric[S](implicit S: Generic[S]): Iso[S, S.Repr] =
    Iso[S, S.Repr](S.to(_))(S.from(_))

  def fromGeneric[S](implicit S: Generic[S]): Iso[S.Repr, S] =
    Iso[S.Repr, S](S.from(_))(S.to(_))
}
