package models
import scala.slick.driver.MySQLDriver.simple._
import scala.slick.ast.ColumnOption.NotNull
import java.sql.Date

class Aged(tag: Tag) extends Table[(Int, String, String, Byte, String, Date, String, String, String, Int, Int, Date)](tag, "AGED")
{
  def id = column[Int]("ID", O.PrimaryKey, O.AutoInc)
  def name = column[String]("NAME", NotNull)
  def kana = column[String]("KANA", NotNull)
  def age = column[Byte]("AGE", NotNull)
  def sex = column[String]("SEX", NotNull)
  def birthed = column[Date]("BIRTHED", NotNull)
  def address = column[String]("ADDRESS", NotNull)
  def postal = column[String]("POSTAL", NotNull)
  def phone = column[String]("PHONNE", NotNull)
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
  val aged = TableQuery[Aged]

  def getAgedForIndex(implicit session: Session) =
    (aged leftJoin Insurances.insurances on (_.insuranceId === _.id))
    .map
    {
      case (a, i) => (a.id, a.name, a.kana, a.age, a.sex, a.birthed, i.expired.?)
    }
    .run
  
  def createAged(name: String, kana: String, age: Byte, sex: String, birthed: Date, address: String, postal: String, phone: String, insuranceId: Int, homeId: Int, left: Date) = aged.map(a => a.name, a.kana, a.age, a.sex, a.birthed, a.address, a.postal, a.phone, a.insuranceId, a.homeId, a.left).insert(name, kana, age, sex, birthed, address, postal, phone, insuranceId, homeId, left)
}
