package db.erm.organization

import io.flow.organization.event.v0.models.OrganizationUpsertedV2
import io.flow.common.v0.models._
import io.flow.common.v0.models.json._
import io.flow.event.relation.mapper.dao.{DaoArguments, Dao}
import javax.inject.Inject

class OrganizationsDao @Inject()(args: DaoArguments) extends Dao[OrganizationUpsertedV2, Organization](args)
