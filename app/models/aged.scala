package models
import scala.slick.driver.MySQLDriver.simple._
import scala.slick.ast.ColumnOption.NotNull
import java.sql.Date

class Aged(tag: Tag) extends Table[(Int, String, String, Byte, Char, Date, String, String, String, Int, Int, Date)](tag, "aged") {
  def id = column[Int]("id", O.PrimaryKey)
  def name = column[String]("name", O.Length(255, false), NotNull)
  def kana = column[String]("kana", O.Length(255, false), NotNull)
  def age = column[Byte]("age", NotNull)
  def sex = column[String]("sex", O.Length(4, false), NotNull)
  def birthed = column[Date]("birthed", NotNull)
  def address = column[String]("address", O.Length(65535, false), NotNull)
  def postal = column[String]("postal", O.Length(7, false), NotNull)
  def phone = column[String]("phone", O.Length(13, false), NotNull)
  def insuranceId = column[Int]("insuranceId", NotNull)
  def homeId = column[Int]("homeId", NotNull)
  def left = column[Date]("left")
  def * = (id, name, kana, age, sex, birthed, address, postal, phone, insuranceId, homeId, left)

  def insurance = foreignKey("insuranceFK", insuranceId, insurances)(_.id)
  def home = foreignKey("homeFK", homeId, homes)(_.id)
  def agedIndex = index("agedIndex", id)
}

object aged extends TableQuery[Aged] {
  //override def leftJoin[E2, U2, D[_]](q2: Query[E2, U2, D]): BaseJoinQuery[E, E2, E.TableElementType, U2, Seq] = super.leftJoin(q2)
  def getAgedForIndex = (this leftJoin insurances on (_.insuranceId === _.id)).map { case (a, i) => (a.id, a.name, a.kana, a.age, a.sex, a.birthed, i.expired.?) }.run
}
