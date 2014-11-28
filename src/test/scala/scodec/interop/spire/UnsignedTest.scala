package scodec
package interop
package spire

import bits.BitVector

import org.scalacheck.Arbitrary
import Arbitrary.arbitrary
import scalaz.\/.left
import _root_.spire.math._
import _root_.spire.implicits._

class UnsignedTest extends CodecSuite {

  def sameResults[U: Arbitrary, A](cu: Codec[U], ca: Codec[A])(toA: U => A) = {
    forAll { (x: U) => cu.encode(x) shouldBe ca.encode(toA(x)) }
    forAll { (x: BitVector) => cu.decode(x).map { case (rem, u) => (rem, toA(u)) } shouldBe ca.decode(x) }
  }

  implicit val arbitraryUInt: Arbitrary[UInt] = Arbitrary(arbitrary[Int] map UInt.apply)
  implicit val arbitraryULong: Arbitrary[ULong] = Arbitrary(arbitrary[Long] map ULong.apply)

  "the spire unsigned integer codecs" should {
    "compute same results as equivalent core codecs" in {
      for (size <- 1 to 31) {
        sameResults(uint(size), codecs.uint(size))(_.toInt)
        sameResults(uintL(size), codecs.uintL(size))(_.toInt)
      }
      sameResults(uint(32), codecs.uint32)(_.toLong)
      sameResults(uintL(32), codecs.uint32L)(_.toLong)

      for (size <- 1 to 63) {
        sameResults(ulong(size), codecs.ulong(size))(_.toLong)
        sameResults(ulongL(size), codecs.ulongL(size))(_.toLong)
      }
      sameResults(ulong(64), codecs.int64)(_.toLong)
      sameResults(uint64, codecs.int64)(_.toLong)
    }
  }
}
