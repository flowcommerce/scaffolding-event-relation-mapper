package helpers

import db.organization.OrganizationsDao
import io.flow.common.v0.mock.Factories
import io.flow.common.v0.models.{Organization, OrganizationDefaults, OrganizationReference}
import io.flow.test.utils.FlowPlaySpec

trait OrganizationHelpers {
  this: FlowPlaySpec =>

  def organizationsDao: OrganizationsDao = init[db.organization.OrganizationsDao]

  def createOrganization(
    organization: Organization = makeOrganization()
  ): Organization = {
    organizationsDao.upsertByPrimaryKey(organization)
    organizationsDao.findByPrimaryKey(organization.id).get.model
  }

  def makeOrganization(
    id: String = createTestId(),
    parentId: Option[String] = None
  ): Organization = {
    Factories.makeOrganization().copy(
      id = id,
      parent = parentId.map(OrganizationReference),
      defaults = Some(
        OrganizationDefaults(
          baseCurrency = "USD",
          country = "USA",
          locale = "en-US",
          language = "en",
          timezone = "America/New_York"
        )
      )
    )
  }

}