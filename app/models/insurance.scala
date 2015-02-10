package models.insurance
import scala.slick.driver.MySQLDriver.simple._
import models.aged._

class Insurance(tag: Tag) extends Table[(Int, Byte, Date, Date)](tag, "INSURANCE") {
  def insuranceId = column[Int]("INSURANCE_ID", O.PrimaryKey)
  def nursingCareLevel = column[Byte]("LEVEL")
  def started = column[Date]("STARTED")
  def expired = column[Date]("EXPIRED")
  def * = (id, nursingCareLevel, started, expired)
  def agedId = foreignKey("AGED_FK", insuranceId, aged)(_.id)
}
val insurances = TableQuery[Insrance]
