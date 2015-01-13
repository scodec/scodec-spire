package scodec
package interop
package spire

import scala.collection.GenTraversable

import org.scalacheck.{ Arbitrary, Gen }
import org.scalatest.{Matchers, WordSpec}
import org.scalatest.prop.GeneratorDrivenPropertyChecks

import scodec.bits.BitVector

abstract class CodecSuite extends WordSpec with Matchers with GeneratorDrivenPropertyChecks {

  protected def roundtrip[A: Codec](a: A) {
    roundtrip(Codec[A], a)
  }

  protected def roundtrip[A](codec: Codec[A], a: A) {
    val encoded = codec.encode(a)
    val Attempt.Successful(DecodeResult(decoded, remainder)) = codec.decode(encoded.toOption.get)
    remainder shouldEqual BitVector.empty
    decoded shouldEqual a
  }

  protected def roundtripAll[A](codec: Codec[A], as: GenTraversable[A]) {
    as foreach { a => roundtrip(codec, a) }
  }

  implicit def arbitraryBitVector = Arbitrary(for {
    bytes <- Gen.listOf(Arbitrary.arbitrary[Byte])
    mod <- Gen.choose(0, 7)
  } yield BitVector(bytes).drop(mod))
}
