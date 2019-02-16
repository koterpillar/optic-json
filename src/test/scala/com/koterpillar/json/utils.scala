package com.koterpillar.json

import io.circe.Json
import org.everit.json.schema.loader.SchemaLoader
import org.everit.json.schema.{Schema => EveritSchema}
import org.json._

object utils {
  implicit class JsonOps(value: Json) {
    def toOrgJson: AnyRef = new JSONObject(s"""{"key":${value.noSpaces}}""").get("key")
  }

  implicit class SchemaOps[A](value: Schema[A]) {
    def toEveritSchema: EveritSchema = SchemaLoader.load(value.schema.toOrgJson.asInstanceOf[JSONObject])
  }
}
