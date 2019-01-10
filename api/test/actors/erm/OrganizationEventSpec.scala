package actors.erm

import io.flow.organization.event.v0.mock.Factories
import io.flow.organization.event.v0.models.OrganizationEvent
import io.flow.organization.event.v0.models.json._
import io.flow.test.utils.FlowPlaySpec
import org.scalatest.Assertion

import scala.concurrent.ExecutionContext

class OrganizationEventSpec extends FlowPlaySpec
  with helpers.erm.ErmHelpers
  with helpers.erm.OrganizationHelpers
{

  private[this] implicit val ec = ExecutionContext.global
  private[this] lazy val producer = ermQueue.producer[OrganizationEvent]()

  private[this] def testEvents[U <: OrganizationEvent, V <: OrganizationEvent](
    findByPrimaryKey: => Option[Any],
    upserted: U,
    deleted: V
  ): Assertion = {
    producer.publish(upserted)
    eventuallyInNSeconds() {
      findByPrimaryKey.get
    }

    producer.publish(deleted)
    eventuallyInNSeconds() {
      findByPrimaryKey.isEmpty must be(true)
    }
  }

  "organization" in {
    val org = makeOrganization()
    testEvents(
      findByPrimaryKey = { organizationsDao.findByPrimaryKey(org.id) },
      upserted = Factories.makeOrganizationUpsertedV2().copy(organization = org),
      deleted = Factories.makeOrganizationDeletedV2().copy(organization = org)
    )
  }

}
