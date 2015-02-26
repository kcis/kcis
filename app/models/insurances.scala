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

  private def checkNextTerm(started: Date): Date
  {
    /*
     * 介護保険の更新期限は、
     * 初回: 認定された日から半年後の月末まで
     * 2回目以降: 前回の更新が適用され始めた日から1年後の月末まで
     */
    val expired = Calendar.getInstance()
    tmp.setTime(started)
    val today = Calendar.getInstance()
    today.setTime(new Date(System.currentTimeMillis()))

    if (today.get(Calendar.YEAR) == expired.get(Calendar.YEAR) && today.get(Calendar.MONTH) - expired.get(Calendar.MONTH) < 6)
    {
      expired.add(Calendar.MONTH, 6)
      expired.set(Calendar.DATE, expired.getActualMaximum(Calendar.DATE))
      return expired.getTime()
    }
    else
    {
      expired.add(Calendar.YEAR, 1)
      expired.set(Calendar.DATE, expired.getActualMaximum(Calendar.DATE))
      return expired.getTime()
    }
  }

  // deleteInsurance() 用のチェッカ
  private def doAfterChecking(id: Int, f: => Int)
  {
    if (id > 0) f
    else throw new IllegalArgumentException("ID は 1 から Intの最大値(約21億) の範囲でなければなりません。")
  }

  // createInsurance() および updateInsurance() 用のチェッカ
  private def doAfterChecking(id: Int, nursingCareLevel: Byte, f: => Int)
  {
    if (id > 0 && nursingCareLevel > 0 && nursingCareLevel <= 5) f
    else
    {
      if (id <= 0) throw new IllegalArgumentException("IDは 1 から Intの最大値(約21億) の範囲でなければなりません。")
      else throw new IllegalArgumentException("要介護度は 1 から 5 の範囲でなければなりません。")
    }
  }

  def createInsurance(id: Int, nursingCareLevel: Byte, started: Date)(implicit session: Session)
  {
    doAfterChecking
    (
      id,
      nursingCareLevel,
      {
        val expired = checkNextTerm(started)
        insurances.map(i => (i.id, i.nursingCareLevel, i.started, i.expired)).insert(id, nursingCareLevel, started, expired)
      }
    )
  }

  def updateInsurance(id: Int, nursingCareLevel: Byte, started: Date)(implicit session: Session)
  {
    doAfterChecking
    (
      id,
      nursingCareLevel,
      {
        val expired = checkNextTerm(started)
        insurances.filter(i => i.id === id).map(i => (i.nursingCareLevel, i.started, i.expired)).update(nursingCareLevel, started, expired)
      }
    )
  }

  def deleteInsurance(id: Int)(implicit session: Session) = doAfterChecking(id, {insurance.filter(i => i.id === id).delete})
}
