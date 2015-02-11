package controllers

import jp.t2v.lab.play2.auth._
import models._
import play.api._
import play.api.data._
import play.api.data.Forms._
import play.api.mvc._
import play.api.mvc.Results._

import scala.concurrent.{Future, ExecutionContext}
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import scala.reflect._

trait AuthConfigImpl extends AuthConfig {
  type Id = Int
  type User = Account
  type Authority = Role
  val idTag: ClassTag[Id] = classTag[Id]
  val sessionTimeoutInSeconds: Int = 3600

  def resolveUser(id: Id) = Account.findById(id)
  def loginSucceeded(request: RequestHeader) = Redirect(routes.Application.index)
  def logoutSucceeded(request: RequestHeader) = Redirect(routes.Application.login)
  def authenticationFailed(request: RequestHeader) = Redirect(routes.Application.login)
  def authorizationFailed(request: RequestHeader) = Forbidden("no permission")
  def authorize(user: User, authority: Authority) = (user.permission, authority) match {
    case (Administrator, _) => true
    case (NormalUser, NormalUser) => true
    case _ => false
  }
}

object Application extends Controller with LoginLogout with AuthConfigImpl {

  def index = Action {
    Ok(views.html.index("Your new application is ready."))
  }

  val loginForm = Form {
    mapping("email" -> email, "password" -> text)(Account.authenticate)(_.map(u => (u.email, "")))
      .verifying("Invalid email or password", result => result.isDefined)
  }

  def login = Action { implicit request =>
    Ok(views.html.login(loginForm))
  }

  def logout = Action.async { implicit request =>
    gotoLogoutSucceeded.map(_.flashing(
      "success" -> "You've been logged out"
    ))
  }

  def authenticate = Action.async { implicit request =>
    loginForm.bindFromRequest.fold(
      formWithErrors => Future.successful(BadRequest(views.html.login(formWithErrors))),
      user           => gotoLoginSucceeded(user.get.id)
    )
  }
}
