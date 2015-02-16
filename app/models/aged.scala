package models
import scala.slick.driver.MySQLDriver.simple._
import scala.slick.ast.ColumnOption.NotNull
import java.sql.Date

class Aged(tag: Tag) extends Table[(Int, String, String, Byte, String, Date, String, String, String, Int, Int, Date)](tag, "AGED")
{
  def id = column[Int]("ID", O.PrimaryKey)
  def name = column[String]("NAME", O.Length(255, false), NotNull)
  def kana = column[String]("KANA", O.Length(255, false), NotNull)
  def age = column[Byte]("AGE", NotNull)
  def sex = column[String]("SEX", O.Length(4, false), NotNull)
  def birthed = column[Date]("BIRTHED", NotNull)
  def address = column[String]("ADDRESS", O.Length(65535, false), NotNull)
  def postal = column[String]("POSTAL", O.Length(7, false), NotNull)
  def phone = column[String]("PHONNE", O.Length(13, false), NotNull)
  def insuranceId = column[Int]("INSURANCE_ID", NotNull)
  def homeId = column[Int]("HOME_ID", NotNull)
  def left = column[Date]("LEFT")
  def * = (id, name, kana, age, sex, birthed, address, postal, phone, insuranceId, homeId, left)

  def insurance = foreignKey("INSURANCE_FK", insuranceId, Insurances.insurances)(_.id, onUpdate = ForeignKeyAction.Cascade, onDelete = ForeignKeyAction.Restrict)
  def home = foreignKey("HOME_FK", homeId, Homes.homes)(_.id, onUpdate = ForeignKeyAction.Cascade, onDelete = ForeignKeyAction.Restrict)
  def agedIndex = index("AGED_INDEX", id)
}

object Aged
{
  val db = Database.forURL("jdbc:h2:mem:play", user = "sa", driver = "org.h2.Driver")
  val aged = TableQuery[Aged]
  def getAgedForIndex = db.withSession
  {
    implicit session =>
    (aged leftJoin Insurances.insurances on (_.insuranceId === _.id))
    .map
    {
      case (a, i) => (a.id, a.name, a.kana, a.age, a.sex, a.birthed, i.expired.?)
    }
    .run
  }
}
