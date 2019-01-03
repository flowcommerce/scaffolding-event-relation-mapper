package helpers

import org.scalatest.concurrent.Eventually.{eventually, timeout}
import org.scalatest.time.{Seconds, Span}

trait EventuallyHelpers {

  def eventuallyInNSeconds[T](n: Long = 3)(f: => T): T = {
    eventually(timeout(Span(n, Seconds))) {
      f
    }
  }


}