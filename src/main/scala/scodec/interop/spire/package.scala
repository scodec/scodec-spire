package scodec
package interop

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
      def check(a: A) =
        if (interval contains a) Attempt.successful(a)
        else Attempt.failure(Err(s"$a not contained by $interval"))
      codec.exmap[A](check, check)
    }
  }

  val subyte: Codec[UByte] = codecs.byte.toUByte.withToString("8-bit unsigned byte")

  val sushort8: Codec[UShort] = codecs.ushort8.toUShort
  val sushort16: Codec[UShort] = codecs.short16.toUShort.withToString("16-bit unsigned short")
  def sushort(bits: Int): Codec[UShort] = {
    require(bits > 0 && bits <= 16)
    if (bits < 16) codecs.ushort(bits).toUShort.withToString(s"$bits-bit unsigned short")
    else sushort16
  }

  val sushort16L: Codec[UShort] = codecs.short16L.toUShort.withToString("16-bit unsigned short")
  def sushortL(bits: Int): Codec[UShort] = {
    require(bits > 0 && bits <= 16)
    if (bits < 16) codecs.ushortL(bits).toUShort.withToString(s"$bits-bit unsigned short")
    else sushort16L
  }

  val suint8: Codec[UInt] = codecs.uint8.toUInt
  val suint16: Codec[UInt] = codecs.uint16.toUInt
  val suint24: Codec[UInt] = codecs.uint24.toUInt
  val suint32: Codec[UInt] = codecs.int32.toUInt.withToString("32-bit unsigned integer")
  val suint64: Codec[ULong] = codecs.int64.toULong.withToString("64-bit unsigned integer")
  def suint(bits: Int): Codec[UInt] = {
    require(bits > 0 && bits <= 32)
    if (bits < 32) codecs.uint(bits).toUInt
    else suint32
  }
  def sulong(bits: Int): Codec[ULong] = {
    require(bits > 0 && bits <= 64)
    if (bits < 64) codecs.ulong(bits).toULong
    else suint64
  }

  val suint8L: Codec[UInt] = codecs.uint8L.toUInt
  val suint16L: Codec[UInt] = codecs.uint16L.toUInt
  val suint24L: Codec[UInt] = codecs.uint24L.toUInt
  val suint32L: Codec[UInt] = codecs.int32L.toUInt.withToString("32-bit unsigned integer")
  val suint64L: Codec[ULong] = codecs.int64L.toULong.withToString("64-bit unsigned integer")
  def suintL(bits: Int): Codec[UInt] = {
    require(bits > 0 && bits <= 32)
    if (bits < 32) codecs.uintL(bits).toUInt
    else suint32L
  }
  def sulongL(bits: Int): Codec[ULong] = {
    require(bits > 0 && bits <= 64)
    if (bits < 64) codecs.ulongL(bits).toULong
    else suint64L
  }
}
