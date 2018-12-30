package com.koterpillar.json

import io.circe._

trait Schema[A] {
  def schema: Json
}
