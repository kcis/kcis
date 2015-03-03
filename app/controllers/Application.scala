package controllers
import play.api._
import play.api.data._
import play.api.data.Forms._
import play.api.mvc._

case class LoginParameter(stationId: Int, memberId: String, password: String)

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
      mapping
      (
        "stationId" -> text,
        "memberId" -> text,
        "password" -> text
      )(LoginParameter.apply)(LoginParameter.unapply)
    )
    val loginParameter = authForm.bindFormRequest.get
    
  }
}
