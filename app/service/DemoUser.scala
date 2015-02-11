package service

import securesocial.core._
import securesocial.core.providers.{ UsernamePasswordProvider, MailToken }
import scala.concurrent.Future
import securesocial.core.services.{ UserService, SaveMode }

case class DemoUser(main: BasicProfile, identities: List[BasicProfile])
