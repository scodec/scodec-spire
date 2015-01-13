package scodec
package interop
package spire

import bits.BitVector

import org.scalacheck.Arbitrary
import Arbitrary.arbitrary
import _root_.spire.math._
import _root_.spire.implicits._

class UnsignedTest extends CodecSuite {

  def sameResults[U: Arbitrary, A](cu: Codec[U], ca: Codec[A])(toA: U => A) = {
    forAll { (x: U) => cu.encode(x) shouldBe ca.encode(toA(x)) }
    forAll { (x: BitVector) => cu.decode(x).map { _ map toA } shouldBe ca.decode(x) }
  }

  implicit val arbitraryUInt: Arbitrary[UInt] = Arbitrary(arbitrary[Int] map UInt.apply)
  implicit val arbitraryULong: Arbitrary[ULong] = Arbitrary(arbitrary[Long] map ULong.apply)

  "the spire unsigned integer codecs" should {
    "compute same results as equivalent core codecs" in {
      for (size <- 1 to 31) {
        sameResults(suint(size), codecs.uint(size))(_.toInt)
        sameResults(suintL(size), codecs.uintL(size))(_.toInt)
      }
      sameResults(suint(32), codecs.uint32)(_.toLong)
      sameResults(suintL(32), codecs.uint32L)(_.toLong)

      for (size <- 1 to 63) {
        sameResults(sulong(size), codecs.ulong(size))(_.toLong)
        sameResults(sulongL(size), codecs.ulongL(size))(_.toLong)
      }
      sameResults(sulong(64), codecs.int64)(_.toLong)
      sameResults(suint64, codecs.int64)(_.toLong)
    }
  }
}
