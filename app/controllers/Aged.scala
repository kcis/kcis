package controllers
import play.api.mvc._
import models._

object Aged extends Controller {
  def index = Action { request =>
    val result =
      (aged leftjoin insurances on (_.insuranceId === _.id))
      .filter(a => a.homeId === request.sessiong.get("homeId"))
      .map
      {
        case (a, i) => (a.id, a.name, a.kana, a.age, a.sex, a.birthed, i.expired.?)
      }
      .run
    Ok(views.html.Ageds.index(request.session, result))
  }
}
