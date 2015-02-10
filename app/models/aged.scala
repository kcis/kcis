package models
import scala.slick.driver.MySQLDriver.simple._
import models.insurance._
import java.sql.Date

class Aged(tag: Tag) extends Table[(Int, String, String, Byte, Char, Date, Int)](tag, "aged") {
  def id = column[Int]("aged_id", O.PrimaryKey)
  def name = column[String]("name")
  def kana = column[String]("kana")
  def age = column[Byte]("age")
  def sex = column[Char]("sex")
  def birthed = column[Date]("birthed")
  def address = column[String]("address")
  def postal = column[String]("postal")
  def phone = column[String]("phone")
  def insuranceId = column[Int]("insrance_id")
  def homeId = column[Int]("home_id")
  def left = column[Date]("left")
  def * = (id, name, kana, age, sex, birthed, insuranceId)
  def insurance = foreignKey("insrance_fk", insuranceId, insrances)(_.id)
}
val aged = TableQuery[Aged]
