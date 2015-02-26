package models
import scala.slick.driver.MySQLDriver.simple._
import scala.slick.ast.ColumnOption.NotNull
import java.sql.Date
import java.util.Calendar

case class Aged(id: Int, name: String, kana: String, age: Byte, sex: String, birthed: Date, address: String, postal: String, phone: String, insuranceId: Int, homeId: Int, left: Date)

class Ageds(tag: Tag) extends Table[Aged](tag, "AGEDS")
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
  def * = (id, name, kana, age, sex, birthed, address, postal, phone, insuranceId, homeId, left) <> (Aged.tupled, Aged.unapply)

  def insurance = foreignKey("INSURANCE_FK", insuranceId, Insurances.insurances)(_.id, onUpdate = ForeignKeyAction.Cascade, onDelete = ForeignKeyAction.Restrict)
  def home = foreignKey("HOME_FK", homeId, Homes.homes)(_.id, onUpdate = ForeignKeyAction.Cascade, onDelete = ForeignKeyAction.Restrict)
  def agedIndex = index("AGED_INDEX", id)
}

object Ageds
{
  val ageds = TableQuery[Ageds]

  private def doAfterChecking(aged: Aged, f: => T)
  {
    val tmp = Calendar.getInstance()
    tmp.setTime(aged.birthed)
    val today = Calendar.getInstance()
    today.setTime(new Date(System.currentTimeMillis()))

    if (aged.age <= 0) throw new IllegalArgumentException("年齢には自然数を入力してください。")
    else if (!(aged.sex.equals("男性") || aged.sex.equals("女性"))) throw new IllegalArgumentException("性別は男性か女性のいずれか1つです。")
    else if (today.get(Calendar.YEAR) - tmp.get(Calendar.YEAR) > Byte.MAX_BALUE) throw new IllegalArgumentException("生年月日は今から127年前までの範囲で入力してください。")
    else f
  }

  def getAgedForIndex(implicit session: Session) = (ageds leftJoin Insurances.insurances on (_.insuranceId === _.id)).map{case (a, i) => (a.id, a.name, a.kana, a.age, a.sex, a.birthed, i.expired.?)}.run
  
  def createAged(aged: Aged)(implicit session: Session) = ageds.map(a => a.name, a.kana, a.age, a.sex, a.birthed, a.address, a.postal, a.phone, a.insuranceId, a.homeId, a.left).insert(aged.name, aged.kana, aged.age, aged.sex, aged.birthed, aged.address, aged.postal, aged.phone, aged.insuranceId, aged.homeId, aged.left)
}
