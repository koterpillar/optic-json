package com.koterpillar.json

import io.circe._
import io.circe.syntax._
import org.scalatest.{FreeSpec, Matchers}


class SyntaxTest extends FreeSpec with Matchers {
  "Object schema" - {
    "should be defined with map-like syntax" in {
      case class Sample(myint: Int, mystring: String, myboolean: Boolean)

      // TODO this is reversed
      val schema: Schema[Sample] = Object("myboolean", implicitly[Schema[Boolean]])("mystring", implicitly[Schema[String]])("myint", implicitly[Schema[Int]]).iso(fromGeneric[Sample])

      schema.codec.getOption(Json.obj("myint" -> 2.asJson, "mystring" -> "hello".asJson, "myboolean" -> true.asJson)) should be(Some(Sample(2, "hello", true)))
    }
  }
}
