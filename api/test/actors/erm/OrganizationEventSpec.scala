package actors.erm

import helpers.{ErmHelpers, EventuallyHelpers, OrganizationHelpers}
import io.flow.organization.event.v0.models._
import io.flow.organization.event.v0.models.json._
import io.flow.test.utils.FlowPlaySpec
import io.flow.organization.event.v0.mock.{Factories => EventFactories}

class OrganizationEventSpec extends FlowPlaySpec
  with ErmHelpers
  with OrganizationHelpers
  with EventuallyHelpers
{

  private[this] lazy val organizationEventPublisher = ermQueue.producer[OrganizationEvent]()

  "organization events" in {
    val org = makeOrganization()
    
    organizationEventPublisher.publish(
      EventFactories.makeOrganizationUpsertedV2().copy(organization = org)
    )

    val fetched = eventuallyInNSeconds(10) {
      organizationsDao.findByPrimaryKey(org.id).get
    }.model
    fetched.name must be(fetched.name)
    fetched.environment must be(fetched.environment)
    fetched.defaults.get.baseCurrency must equal("USD")
    fetched.defaults.get.country must equal("USA")
    fetched.defaults.get.locale must equal("en-US")
    fetched.defaults.get.language must equal("en")
    fetched.defaults.get.timezone must equal("America/New_York")
    fetched.parent must be(None)

    organizationEventPublisher.publish(
      EventFactories.makeOrganizationDeletedV2().copy(
        organization = org
      )
    )

    eventuallyInNSeconds(10) {
      organizationsDao.findByPrimaryKey(org.id) must be(empty)
    }
  }
}
