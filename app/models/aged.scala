package models
import scala.slick.driver.MySQLDriver.simple._
import models.insurance
import java.util.Date

class Aged(tag: Tag) extends Table[(Int, String, String, Byte, Char, Date, Int)](tag, "AGED") {
  def id = column[Int]("AGED_ID", O.PrimaryKey)
  def name = column[String]("NAME")
  def kana = column[String]("KANA")
  def age = column[Byte]("AGE")
  def sex = column[Char]("SEX")
  def birthed = column[Date]("BIRTHED")
  def insuranceId = column[Int]("INSURANCE_ID")
  def * = (id, name, kana, age, sex, birthed, insuranceId)
  def insurance = foreignKey("INSURANCE_FK", insuranceId, insrances)(_.id)
}
val aged = TableQuery[Aged]
