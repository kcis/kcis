package models
import scala.slick.driver.MySQLDriver.simple._
import scala.slick.ast.ColumnOption.NotNull
import java.sql.Date

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
}
