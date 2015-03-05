package controllers
import models.Accounts._
import models.Roles._
import play.api._
import play.api.data._
import play.api.data.Forms._
import play.api.libs.Crypto._
import play.api.mvc._

object Application extends Controller
{

  def index = WithAuthorization
  {
    Redirect("/aged", 302)
  }

  def login = Action
  {
    if (SessionManager.isAuthorized) NotModified else Ok(views.html.login)
  }

  def auth = Action
  {
    val authForm: Form[LoginParameter] = Form
    (
      tuple
      (
        "homeId" -> number(min = 1),
        "userName" -> text,
        "password" -> text
      )
    )
    val (homeId, userName, password) = authForm.bindFormRequest.get
    if (checkDuplication(userName, sign(password)))
    {
      val roleId = getRoleId(userName)
      val authority = getAuthority(roleId)
      SessionManager.createLoginSession(homeId, userName, roleId)
      Redirect(SessionManager.authorized(), 302)
    }
    
  }
}
