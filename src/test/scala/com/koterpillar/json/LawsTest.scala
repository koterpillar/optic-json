package com.koterpillar.json

import cats.Eq
import cats.implicits._
import cats.kernel.laws._
import cats.kernel.laws.discipline._
import io.circe.Json
import org.scalacheck.Arbitrary
import org.scalacheck.Prop._
import org.scalatest.FunSuiteLike
import org.typelevel.discipline.Laws
import org.typelevel.discipline.scalatest.Discipline
import utils._

import scala.util.{Failure, Success, Try}

trait SchemaLaws[T] {
  implicit def schema: Schema[T]

  def encodedIsValid(value: T): IsEq[Either[String, Unit]] = {
    val schemaJson = schema.toEveritSchema
    val encoded: Json = schema.codec.reverseGet(value)
    val result = Try(schemaJson.validate(encoded.toOrgJson)) match {
      case Success(()) => Right(())
      case Failure(exc) => Left(exc.toString)
    }
    result <-> Right(())
  }
}

object SchemaLaws {
  def apply[T](implementation: Schema[T]): SchemaLaws[T] = new SchemaLaws[T] {
    override implicit def schema: Schema[T] = implementation
  }

}

trait SchemaTests[T] extends Laws {
  def laws: SchemaLaws[T]

  def algebra(implicit arb: Arbitrary[T],
              eqFOptEmail: Eq[T]) =
    new SimpleRuleSet(
      name = "Schema",
      "encoding corresponds to schema" -> forAll(laws.encodedIsValid _)
    )
}

object SchemaTests {

  def apply[T: Schema](): SchemaTests[T] = new SchemaTests[T] {
    override val laws: SchemaLaws[T] = SchemaLaws[T](implicitly[Schema[T]])
  }

}

class InstancesSchemaTests extends Discipline with FunSuiteLike {
  checkAll("Int", SchemaTests[Int]().algebra)
  checkAll("String", SchemaTests[String]().algebra)
  checkAll("Boolean", SchemaTests[Boolean]().algebra)
}
