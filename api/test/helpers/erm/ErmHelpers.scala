package helpers.erm

import io.flow.event.relation.mapper.ErmQueue
import io.flow.test.utils.FlowPlaySpec
import org.scalatest.concurrent.Eventually.{eventually, timeout}
import org.scalatest.time.{Seconds, Span}

trait ErmHelpers {
  self: FlowPlaySpec =>

  def ermQueue: ErmQueue = app.injector.instanceOf[ErmQueue]

  def eventuallyInNSeconds[T](n: Long = 3)(f: => T): T = {
    eventually(timeout(Span(n, Seconds))) {
      f
    }
  }

}
