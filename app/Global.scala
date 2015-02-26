import java.util.Date
import java.util.Calendar
import models._
import play.api._

object Global extends GlobalSettings
{
  val application = new DefaultApplication(new File("."), this.getClass.getClassloader, None, Play.Mode.Test)
  override def onStart(app: Application)
  {
    // 介護施設テーブルへのデータの挿入
    Homes.createHomes("医療福祉法人KCIS", "KCIS", "通称介護")
    Homes.createHomes("株式会社センティリオン", "介護ホームセンティリオン", "グループホーム")

    // 介護保険テーブルへのデータの挿入
    for (i <- 1 until 25)
    {
      Insurances.createInsurance(i, ((Math.random() * 5) + 1).asInstanceOf[Int], new Date((Math.random() * Long/MAX_VALUE).asInstanceOf[Long]))
    }

    // 利用者テーブルへのデータの挿入
    Aged.createAged()
  }
}
