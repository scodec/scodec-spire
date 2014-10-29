package scodec
package interop

import scalaz.\/
import \/.{ left, right }
import _root_.spire.math.{ Interval, UByte, UInt, UShort, ULong }

/**
 * Integrates Spire with scodec.
 */
package object spire {

  /** Provides enrichments for `Codec[A]` related to Spire. */
  implicit class SpireCodecEnrichment[A](val codec: Codec[A]) extends AnyVal {

    def toUByte(implicit ev: A =:= Byte): Codec[UByte] = codec.xmap[UByte](b => UByte(b), _.toByte.asInstanceOf[A]).withToString(codec.toString)
    def toUShort(implicit ev: A =:= Short): Codec[UShort] = codec.xmap[UShort](s => UShort(s), _.toShort.asInstanceOf[A]).withToString(codec.toString)
    def toUInt(implicit ev: A =:= Int): Codec[UInt] = codec.xmap[UInt](i => UInt(i), _.toInt.asInstanceOf[A]).withToString(codec.toString)
    def toULong(implicit ev: A =:= Long): Codec[ULong] = codec.xmap[ULong](l => ULong(l), _.toLong.asInstanceOf[A]).withToString(codec.toString)

    /**
     * Returns a codec that validates encoded and decoded values are contained
     * by the specified interval. Values outside the interval result in errors
     * being returned from encode/decode.
     *
     * @param interval interval for which to bound values
     */
    def bounded(interval: Interval[A]): Codec[A] = {
      def check(a: A): Err \/ A =
        if (interval contains a) right(a)
        else left(Err(s"$a not contained by $interval"))
      codec.exmap[A](check, check)
    }
  }

  val ubyte: Codec[UByte] = codecs.byte.toUByte.withToString("8-bit unsigned byte")

  val ushort8: Codec[UShort] = codecs.ushort8.toUShort
  val ushort16: Codec[UShort] = codecs.short16.toUShort.withToString("16-bit unsigned short")
  def ushort(bits: Int): Codec[UShort] = {
    require(bits > 0 && bits <= 16)
    if (bits < 16) codecs.ushort(bits).toUShort.withToString(s"$bits-bit unsigned short")
    else ushort16
  }

  val ushort16L: Codec[UShort] = codecs.short16L.toUShort.withToString("16-bit unsigned short")
  def ushortL(bits: Int): Codec[UShort] = {
    require(bits > 0 && bits <= 16)
    if (bits < 16) codecs.ushortL(bits).toUShort.withToString(s"$bits-bit unsigned short")
    else ushort16L
  }

  val uint8: Codec[UInt] = codecs.uint8.toUInt
  val uint16: Codec[UInt] = codecs.uint16.toUInt
  val uint24: Codec[UInt] = codecs.uint24.toUInt
  val uint32: Codec[UInt] = codecs.int32.toUInt.withToString("32-bit unsigned integer")
  def uint(bits: Int): Codec[UInt] = {
    require(bits > 0 && bits <= 32)
    if (bits < 32) codecs.uint(bits).toUInt
    else uint32
  }

  val uint8L: Codec[UInt] = codecs.uint8L.toUInt
  val uint16L: Codec[UInt] = codecs.uint16L.toUInt
  val uint24L: Codec[UInt] = codecs.uint24L.toUInt
  val uint32L: Codec[UInt] = codecs.int32L.toUInt.withToString("32-bit unsigned integer")
  def uintL(bits: Int): Codec[UInt] = {
    require(bits > 0 && bits <= 32)
    if (bits < 32) codecs.uintL(bits).toUInt
    else uint32L
  }
}
