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
    var n = (Math.random() * Integer.MAX_VALUE).asInstanceOf[Int]
    for (i <- 1 until n)
    {
      Insurances.createInsurance(i, (Math.random() * 5).asInstanceOf[Int], new Date((Math.random * Long.MAX_VALUE).asInstanceOf[Long]))
    }

    // 利用者テーブルへのデータの挿入
    val c = Calendar.getInstance()
    val today = new Date(System.currentTimeMillis());
    for (i <- 1 until n)
    {
      c.set(Calendar.YEAR, (2015 - Math.random() * 100).asInstanceOf[Int])
      c.set(Calendar.MONTH, (i % 12) + 1)
      c.set(Calendar.DATE, (i % c.getActualMaximum(Calendar.DATE)) + 1)
      Aged.createAged("N", "N", ((Math.random()) * i % 100).asInstanceOf[Int], "女性", c.getTime(), "A", Integer.toString(i), Integer.toString(i), i, i, null)
    }
  }
}
