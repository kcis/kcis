package controllers
import play.api.db.slick._
import play.api.mvc._
import models._

object Aged extends Controller {
  def index = DBAction { implicit request =>
    Ok(views.html.Aged.index(request.session, aged.getAgedForIndex))
  }
}
