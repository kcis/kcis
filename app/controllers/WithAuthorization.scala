package controllers
import play.api.mvc._

object WithAuthorization extends ActionBuilder[Request]
{
  override def invokeBlock[A](request: Request[A], block: (Request[A]) => Future[Result]) : Future[Result] = if (SessionManager.isAuthorized()) block(request) else Redirect("/login", 302).withSession(SessionManager.redirectFrom(request.uri))
}
