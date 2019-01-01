package com.koterpillar.json

import org.scalatest.{FreeSpec, Matchers}

class SchemaTest extends FreeSpec with Matchers {
  "Schema" - {
    "should be defined for basic types" in {
      assert(schema[Int].noSpaces == """{"type":"number"}""")
      assert(schema[String].noSpaces == """{"type":"string"}""")
      assert(schema[Boolean].noSpaces == """{"type":"boolean"}""")
    }
  }
}
