package scodec
package interop
package spire

import scalaz.\/.left
import _root_.spire.implicits._
import _root_.spire.math.Interval

import codecs.int32

class BoundedTest extends CodecSuite {

  "the bounded combinator" should {
    "roundtrip values that lie within an interval" in {
      forAll { (a: Int, b: Int, c: Int) =>
        val sorted = List(a, b, c).sorted
        val interval = Interval(sorted.head, sorted.last)
        roundtrip(int32.bounded(interval), sorted.tail.head)
      }
    }

    "return an error when encoding values outside the interval" in {
      forAll { (a: Int, b: Int, c: Int) =>
        val sorted = List(a, b, c).sorted
        if (sorted.head != sorted.tail.head) {
          val interval = Interval(sorted.tail.head, sorted.last)
          int32.bounded(interval).encode(sorted.head) shouldBe left(Err(s"${sorted.head} not contained by $interval"))
        }
      }
    }

    "return an error when decoding values outside the interval" in {
      forAll { (a: Int, b: Int) =>
        val sorted = List(a, b).sorted
        val interval = Interval(sorted.head, sorted.last)
        if (sorted.last < Int.MaxValue) {
          val bits = int32.encodeValid(sorted.last + 1)
          int32.bounded(interval).decode(bits) shouldBe left(Err(s"${sorted.last + 1} not contained by $interval"))
        }
      }
    }
  }
}
