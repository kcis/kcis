package models
import scala.slick.driver.MySQLDriver.simple._
import scala.slick.ast.ColumnOption.NotNull
import java.sql.Date
import java.util.Calendar

case class Aged(name: String, kana: String, age: Byte, sex: String, birthed: Date, address: String, postal: String, phone: String, insuranceId: Int, homeId: Int, left: Date)

class Ageds(tag: Tag) extends Table[(Int, Aged)](tag, "AGEDS")
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
  def * = (name, kana, age, sex, birthed, address, postal, phone, insuranceId, homeId, left) <> (Aged.tupled, Aged.unapply)

  def insurance = foreignKey("INSURANCE_FK", insuranceId, Insurances.insurances)(_.id, onUpdate = ForeignKeyAction.Cascade, onDelete = ForeignKeyAction.Restrict)
  def home = foreignKey("HOME_FK", homeId, Homes.homes)(_.id, onUpdate = ForeignKeyAction.Cascade, onDelete = ForeignKeyAction.Restrict)
  def agedIndex = index("AGED_INDEX", id)
}

object Ageds
{
  val ageds = TableQuery[Ageds]

  private def doAfterChecking(aged: Aged, f: => T)
  {
    val birthed = Calendar.getInstance()
    birthed.setTime(aged.birthed)

    val today = Calendar.getInstance()
    today.setTime(new Date(System.currentTimeMillis()))

    // Slick のテーブル定義は、広範なデータベースに共通する以上のオプションは提供できない。
    // なので、負数を認めてはいけない場合、このようにして外側から検査する必要がある。
    if (aged.age < 0) throw new IllegalArgumentException("年齢には0以上127以下の数字を入力してください。")
    else if (aged.birthed.after(today.getTime())) throw new IllegalArgumentException("生年月日は現在よりも前の日付で入力してください。")
    else if (aged.age != (today.get(Calendar.YEAR) - birthed.get(Calendar.YEAR))) throw new IllegalArgumentException("年齢と生年月日が一致していません。")
    else f
  }

  // 利用者一覧に使うデータはこれ一つでまとめて表示できる
  def getAgedForIndex(implicit session: Session) = (ageds leftJoin Insurances.insurances on (_.insuranceId === _.id)).map{case (a, i) => (a.id, a.name, a.kana, a.age, a.sex, a.birthed, i.expired.?)}.run
  
  def createAged(aged: Aged)(implicit session: Session) = doAfterChecking(aged, {ageds.insert(aged)})

  def updateAged(id: Int, aged: Aged)(implicit session: Session) = doAfterChecking(aged, {ageds.filter(a => a.id === id).update(aged)})

  def deleteAged(id: Int)(implicit session: Session) = ageds.filter(a => a.id === id).delete
}
