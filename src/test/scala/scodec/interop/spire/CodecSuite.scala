package scodec
package interop
package spire

import scala.collection.GenTraversable

import org.scalacheck.{ Arbitrary, Gen }
import org.scalatest.{ Assertion, Matchers, WordSpec }
import org.scalatest.prop.GeneratorDrivenPropertyChecks

import shapeless.Lazy

import scodec.bits.BitVector

abstract class CodecSuite extends WordSpec with Matchers with GeneratorDrivenPropertyChecks {

  protected def roundtrip[A](a: A)(implicit c: Lazy[Codec[A]]): Assertion = {
    roundtrip(Codec[A], a)
  }

  protected def roundtrip[A](codec: Codec[A], a: A): Assertion = {
    val encoded = codec.encode(a)
    val Attempt.Successful(DecodeResult(decoded, remainder)) = codec.decode(encoded.toOption.get)
    remainder shouldEqual BitVector.empty
    decoded shouldEqual a
  }

  protected def roundtripAll[A](codec: Codec[A], as: GenTraversable[A]): Unit = {
    as foreach { a => roundtrip(codec, a) }
  }

  implicit def arbitraryBitVector = Arbitrary(for {
    bytes <- Gen.listOf(Arbitrary.arbitrary[Byte])
    mod <- Gen.choose(0, 7)
  } yield BitVector(bytes).drop(mod.toLong))
}
