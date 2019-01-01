package com.koterpillar

import io.circe.Json

package object json extends AllInstances {
  def schema[A: Schema]: Json = implicitly[Schema[A]].schema
}
