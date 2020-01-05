package helpers.erm

import io.flow.event.relation.mapper.ErmQueue
import io.flow.test.utils.FlowPlaySpec

trait ErmHelpers {
  self: FlowPlaySpec =>

  def ermQueue: ErmQueue = app.injector.instanceOf[ErmQueue]

}
