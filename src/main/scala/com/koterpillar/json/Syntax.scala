package com.koterpillar.json

trait Syntax {
  def product[H, T](head: Field[H], tail: Object[T]): Object[(H, T)] = ConsObject(head, tail)

  def field[A: Schema](name: String) = Field(name, implicitly[Schema[A]])
}
