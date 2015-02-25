package models
import java.sql.Date
import java.util.Calendar
import scala.slick.driver.MySQLDriver.simple._
import scala.slick.ast.ColumnOption.NotNull

class Insurances(tag: Tag) extends Table[(Int, Byte, Date, Date, Int)](tag, "INSURANCES") {
  def id = column[Int]("ID", O.PrimaryKey)
  def nursingCareLevel = column[Byte]("LEVEL", NotNull)
  def started = column[Date]("STARTED", NotNull)
  def expired = column[Date]("EXPIRES", NotNull)
  def * = (id, nursingCareLevel, started, expired, agedId)

  def insuranceIndex = index("INSURANCE_INDEX", id)
}

object Insurances {
  val insurances = TableQuery[Insurances]
  def createInsurance(id: Int, nursingCareLevel: Byte, started: Date)(implicit session: Session)
  {
    /*
     * 介護保険の適用期限は登録(更新)から半年後の末日である。
     * また、保険の更新を行うために必要な市役所は、遅くとも午後6時には閉まる。
     */
    if (id > 0 && id <= Integer.MAX_VALUE)
    {
      val expired = Calendar.getInstance()
      expired.setTime(started)
      expired.add(Calendar.MONTH, 6)
      expired.set(Calendar.DATE, expired.getActualMaximum(Calendar.DATE))
      expired.set(Calendar.HOUR_OF_DAY, 18)
      insurances.map(i => (i.id, i.nursingCareLevel, i.started, i.expired)).insert(id, nursingCareLevel, started, expired.getTime())
    }
    else throw new IllegalArgumentException()
  }
  def updateInsurance(id: Int, nursingCareLevel: Byte, started: Date)(implicit session: Session)
  {
    val expired = Calendar.getInstance()
    expired.setTime(started)
    expired.add(Calendar.MONTH, 6)
    expired.set(Calendar.DATE, expired.getActualMaximum(Calendar.DATE))
    expired.set(Calendar.HOUR_OF_DAY, 18)
    insurances.filter(i => i.id === id).map(i => (i.nursingCareLevel, i.started, i.expired)).update(nursingCareLevel, started, expired.getTime())
  }
  def deleteInsurance(id: Int)(implicit session: Session)
  {
    insurances.filter(i => i.id === id).delete
  }
}
