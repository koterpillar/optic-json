package com.koterpillar.json

import io.circe.Json
import monocle.{Iso, Prism}

class SchemaOps[A](value: Schema[A]) {
  def iso[B](iso: Iso[A, B]): Schema[B] = new Schema[B] {
    override def schema: Json = value.schema

    override def codec: Prism[Json, B] = value.codec composeIso iso
  }
}
