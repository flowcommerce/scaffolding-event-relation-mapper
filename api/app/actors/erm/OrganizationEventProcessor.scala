package actors.erm

import akka.actor.ActorSystem
import io.flow.event.relation.mapper.{ErmQueue, EventProcessor, EventRelationMapperProcessor}
import io.flow.organization.event.v0.models._
import io.flow.organization.event.v0.models.json._
import javax.inject.{Inject, Singleton}

@Singleton
class OrganizationEventProcessor @Inject()(
  override val queue: ErmQueue,
  override val system: ActorSystem,
  eventRelationMapper: EventProcessor
) extends EventRelationMapperProcessor[OrganizationEvent](queue, system, eventRelationMapper) {

  override def accepts = {
    case _: OrganizationUpsertedV2 => true
    case _: OrganizationDeletedV2 => true
    case _ => false
  }

}