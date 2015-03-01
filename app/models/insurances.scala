package models
import java.sql.Date
import java.util.Calendar
import scala.slick.driver.MySQLDriver.simple._
import scala.slick.ast.ColumnOption.NotNull

case class Insurance(id: Int, nursingCareLevel: Byte, started: Date, expired: Date)

class Insurances(tag: Tag) extends Table[Insurance](tag, "INSURANCES") {
  def id = column[Int]("ID", O.PrimaryKey)
  def nursingCareLevel = column[Byte]("LEVEL", NotNull)
  def started = column[Date]("STARTED", NotNull)
  def expired = column[Date]("EXPIRES", NotNull)
  def * = (id, nursingCareLevel, started, expired) <> (Insurance.tupled, Insurance.unapply)

  def insuranceIndex = index("INSURANCE_INDEX", id)
}

object Insurances {
  private val insurances = TableQuery[Insurances]

  private def checkNextTerm(started: Date): Date
  {
    /*
     * 介護保険の更新期限は、
     * 初回: 認定された日から半年後の月末まで
     * 2回目以降: 前回の更新が適用され始めた日から1年後の月末まで
     */
    val expired = Calendar.getInstance()
    expired.setTime(started)
    val today = Calendar.getInstance()
    today.setTime(new Date(System.currentTimeMillis()))

    if (today.get(Calendar.YEAR) == started.get(Calendar.YEAR) && today.get(Calendar.MONTH) - started.get(Calendar.MONTH) < 6)
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
    else throw new IllegalArgumentException("ID は 1 以上 2147483647 以下にしてください。")
  }

  // createInsurance() および updateInsurance() 用のチェッカ
  private def doAfterChecking(insurance: Insurance, f: => Int)
  {
    val today = new Date(System.currentTimeMillis())

    if (insurance.id <= 0) throw new IllegalArgumentException("ID は 1 以上にしてください。")
    else if (insurance.nursingCareLevel < 0 || insurance.nursingCareLevel > 5) throw new IllegalArgumentException("要介護度は 1 から 5 までです。")
    else if (insurance.started.after(today)) throw new IllegalArgumentException("今日現在、まだ介護保険を認定されていない方を入居させることはできません。")
    else f
  }

  // 利用者テーブルとの結合用(private化して隠蔽するため、他のテーブルが条件として使う場合に参照できない問題をこれで解決する)
  def getTableQuery = insurances

  def createInsurance(insurance: Insurance)(implicit session: Session)
  {
    doAfterChecking
    (
      insurance,
      {
        val expired = checkNextTerm(insurance.started)
        insurances.insert(Insurance(insurance.id, insurance.nursingCareLevel, insurance.started, expired))
      }
    )
  }

  def updateInsurance(insurance: Insurance)(implicit session: Session)
  {
    doAfterChecking
    (
      insurance,
      {
        val expired = checkNextTerm(started)
        insurances.filter(i => i.id === id).update(Insurance(insurance.id, insurance.nursingCareLevel, insurance.started, expired))
      }
    )
  }

  def deleteInsurance(id: Int)(implicit session: Session) = doAfterChecking(id, {insurance.filter(i => i.id === id).delete})
}
