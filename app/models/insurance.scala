package models
import scala.slick.driver.MySQLDriver.simple._

class Insurances(tag: Tag) extends Table[(Int, Byte, Date, Date)](tag, "insrances") {
  def id = column[Int]("id", O.PrimaryKey)
  def nursingCareLevel = column[Byte]("level")
  def started = column[Date]("started")
  def expired = column[Date]("expired")
  def agedId = column[Int]("aged_id")
  def * = (id, nursingCareLevel, started, expired)
  def aged = foreignKey("aged_fk", agedId, aged)(_.id)
}
val insurances = TableQuery[Insrances]
