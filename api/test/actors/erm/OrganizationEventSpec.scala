package actors.erm

import io.flow.organization.event.v0.mock.Factories
import io.flow.organization.event.v0.models.OrganizationEvent
import io.flow.organization.event.v0.models.json._
import io.flow.test.utils.FlowPlaySpec
import org.scalatest.Assertion

class OrganizationEventSpec extends FlowPlaySpec
  with helpers.erm.ErmHelpers
  with helpers.erm.OrganizationHelpers
{

  private[this] lazy val producer = ermQueue.producer[OrganizationEvent]()

  private[this] def testEvents(
    findByPrimaryKey: => Option[Any],
    upserted: OrganizationEvent,
    deleted: OrganizationEvent,
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
