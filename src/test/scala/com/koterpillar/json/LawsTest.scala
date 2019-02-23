package com.koterpillar.json

import cats._
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
import shapeless._
import org.scalacheck.ScalacheckShapeless._

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

  def roundTrip(value: T): IsEq[Option[T]] = {
    val encoded: Json = schema.codec.reverseGet(value)
    val decoded: Option[T] = schema.codec.getOption(encoded)

    decoded <-> Some(value)
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
              eq: Eq[T]) =
    new SimpleRuleSet(
      name = "Schema",
      "encoding corresponds to schema" -> forAll(laws.encodedIsValid _),
      "decoding round-trips encoding" -> forAll(laws.roundTrip _),
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

  type SampleGeneric = Int :: String :: Boolean :: HNil
  implicit val sampleGenericEq: Eq[SampleGeneric] =
    (a: SampleGeneric, b: SampleGeneric) =>
      Eq.eqv(a.head, b.head) &&
        Eq.eqv(a.tail.head, b.tail.head) &&
        Eq.eqv(a.tail.tail.head, b.tail.tail.head)
  implicit val sampleGenericSchema: Schema[SampleGeneric] =
    product(field("myint"), product(field("mystring"), product(field("mybool"), emptyObject)))
  checkAll("SampleGeneric", SchemaTests[SampleGeneric]().algebra)

  case class Sample(myint: Int, mystring: String, myboolean: Boolean)
  implicit val sampleEq: Eq[Sample] = Eq.fromUniversalEquals[Sample]
  implicit val sampleSchema: Schema[Sample] = sampleGenericSchema.iso(fromGeneric[Sample])
  checkAll("Sample", SchemaTests[Sample]().algebra)
}
