package models.insurance
import scala.slick.driver.MySQLDriver.simple._
import models.aged._

class Insurances(tag: Tag) extends Table[(Int, Byte, Date, Date)](tag, "INSURANCE") {
  def id = column[Int]("INSURANCE_ID", O.PrimaryKey)
  def nursingCareLevel = column[Byte]("LEVEL")
  def started = column[Date]("STARTED")
  def expired = column[Date]("EXPIRED")
  def agedId = column[Int]("AGED_ID")
  def * = (id, nursingCareLevel, started, expired)
  def aged = foreignKey("AGED_FK", agedId, aged)(_.id)
}
val insurances = TableQuery[Insrances]
