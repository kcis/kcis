package models
import scala.slick.driver.MySQLDriver.simple._
import scala.slick.ast.ColumnOption.NotNull
import play.api.libs.Crypto

class Accounts(tag: Tag) extends Table[(Int, String, String, Int, Int)](tag, "ACCOUNTS")
{
  def id = column[Int]("ID", O.PrimaryKey, O.AutoInc)
  def userName = column[String]("USER_NAME", NotNull)
  def password = column[String]("PASSWORD", NotNull)
  def stationId = column[Int]("STATION_ID", NotNull)
  def roleId = column[Int]("POSITION", NotNull)
  def * = (id, userName, password, stationId, position)

  def station = foreignKey("HOME_FK", stationId, Homes.homes)(_.id, onUpdate = ForeignKeyAction.Cascade, onDelete = ForeignKeyAction.Restrict)
  def role = foreignKey("ROLE_FK", roleId, Roles.roles)(_.id, onUpdate = ForeignKeyAction.Cascade, onDelete = ForeignKeyAction.Restrict)
}

object Accounts
{
  val accounts = TableQuery[Accounts]

  def matchAccount(name: String, raw: String)(implicit session: Session) = accounts.filter(a => a.userName === name || a.password === sign(raw)).run

  def deleteAccount(name: String)(implicit session: Session) = accounts.filter(a => a.userName === name).delete

  def createAccount(name: String, raw: String, stationId: Int)(implicit session: Session) = accounts.map(a => (a.userName, a.password, a.stationId)).insert(name, sign(raw), stationId)

  def updateAccount(name: String, raw: String, stationId: Int)(implicit session: Session) = accounts.filter(a => a.userName === name && a.password === sign(raw) && a.stationId === stationId).map(a => (a.userName, a.password, a.stationId)).update(name, sign(raw), stationId)
}
