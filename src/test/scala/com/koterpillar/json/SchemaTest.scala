package com.koterpillar.json

import io.circe.Json
import io.circe.syntax._
import org.scalatest.{FreeSpec, Matchers}

class SchemaTest extends FreeSpec with Matchers {
  "Schema" - {
    "should be defined for basic types" in {
      schema[Int] should be(Json.obj("type" -> "number".asJson))
      schema[String] should be(Json.obj("type" -> "string".asJson))
      schema[Boolean] should be(Json.obj("type" -> "boolean".asJson))
    }

    "should be accumulated for object types" in {
      val latlonSchema = product(field[Int]("latitude"), product(field[Int]("longitude"), NullObject))
      latlonSchema.schema should be(Json.obj(
        "type" -> "object".asJson,
        "fields" -> Json.obj(
          "latitude" -> Json.obj("type" -> "number".asJson),
          "longitude" -> Json.obj("type" -> "number".asJson),
        ),
        "required" -> List("latitude", "longitude").asJson,
      ))
    }
  }
}
