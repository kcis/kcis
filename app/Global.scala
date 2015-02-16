import play.api._
import models._

object Global extends GlobalSettings {
  override def onStart(app: Application) {
    if (Accounts.findAll.isEmpty) {
      Seq(
        Account("100001", "100001", "1")
      ) foreach Accounts.create
    }
  }
}
