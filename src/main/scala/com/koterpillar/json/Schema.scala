package com.koterpillar.json

import io.circe._
import monocle.Prism

trait Schema[A] {
  def schema: Json

  def codec: Prism[Json, A]
}
