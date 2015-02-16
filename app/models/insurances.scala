package models
import scala.slick.driver.MySQLDriver.simple._
import scala.slick.ast.ColumnOption.NotNull
import java.sql.Date

class Insurances(tag: Tag) extends Table[(Int, Byte, Date, Date)](tag, "insurances") {
  def id = column[Int]("id", O.PrimaryKey)
  def nursingCareLevel = column[Byte]("level", NotNull)
  def started = column[Date]("started", NotNull)
  def expired = column[Date]("expired", NotNull)
  def agedId = column[Int]("agedId", NotNull)
  def * = (id, nursingCareLevel, started, expired, agedId)

  def aged = foreignKey("agedFK", agedId, aged)(_.id)
  def indexInsurance = index("indexInsurance", id)
}

val insurances = TableQuery[Insurances]
