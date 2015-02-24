package test
import models._
import java.sql.Date
import java.util.Calendar
import org.spec2.mutable._
import play.api.db.slick.DB
import play.api.test._
import play.api.test.Helpers._

class ModelSpec extends Specification
{
  "The table of insurances" should
  {
    "Datetime validation" in
    {
      running(FakeApplication(additionalConfiguration = inMemoryDataBbase()))
      {
        var starts = Calendar.getInstance().setTime(Date(System.currentTimeMillis()))
        var expires = Calendar.getInstance()
        DB.withSession
        {
          // 正常系
          Homes.homes.map(h => (h.corporationName, h.officeName, h.careType)).insert("test1", "test1_1", "通所介護(デイサービス)")
          Insurances.insurances.map(i => (i.id, i.nursingCareLevel, i.started, i.expired)).insert(1, 1, starts.getTime(), expires.getTime())
        }
      }
    }
  }
}
