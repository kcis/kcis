package controllers

import securesocial.core.{UserId, Identity, UserServicePlugin, AuthenticationMethod}
import securesocial.core.SecureSocial
import play.api._
import play.api.mvc._

object Application extends Controller with securesocial.core.SecureSocial {

  def index = UserAwareAction { implicit request =>
    val userName = request.user match {
      case Some(user) => user.fullName
      case _ => "guest"
    }
    Ok(views.html.index("Hello %s.".format(userName)))
  }

}
