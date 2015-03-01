package controllers
import play.api.db.slick._
import play.api._
import play.api.mvc._

object SessionManager
{
  def isAuthorized(implicit session: Session): Bool = !(session.get("homeId").isEmpty && session.get("userId") && session.get("roleId").isEmpty)

  def createLoginSession(homeId: Int, userId: Int, roleId: Int)(implicit session: Session) = session + ("homeId", Integer.toString(homeId)) + ("userId", Integer.toString(userId)) + ("roleId", Integer.toString(roleId))

  def deleteLoginSession(implicit session: Session) = session - ("homeId") - ("userId") - ("roleId")
  // まだログインしていない状態でログイン以外の画面を開いた時に、ログイン画面に飛ばしつつも元々開こうとしていた画面を覚えておくためのもの

  def redirectFrom(uri: String)(implicit session: Session) = session + ("redirectFrom", uri)

  def authorized(implicit session: Session)
  {
    val redirectFrom = session.get("redirectFrom")
    session - ("redirectFrom")
    redirectFrom
  }

  def getHomeId(implicit session: Session) = session.get("homeId")
}
