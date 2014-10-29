package scodec
package interop

import scalaz.\/
import \/.{ left, right }
import _root_.spire.math.Interval

/**
 * Integrates Spire with scodec.
 */
package object spire {

  /** Provides enrichments for `Codec[A]` related to Spire. */
  implicit class SpireCodecEnrichment[A](val codec: Codec[A]) extends AnyVal {

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

}
