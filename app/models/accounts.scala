package models
import scala.slick.driver.MySQLDriver.simple._
import scala.slick.ast.ColumnOption.NotNull

case class Account(userName: String, password: String, stationId: Int, roleId: Int)

class Accounts(tag: Tag) extends Table[(Int, Account)](tag, "ACCOUNTS")
{
  def id = column[Int]("ID", O.PrimaryKey, O.AutoInc)
  def userName = column[String]("USER_NAME", NotNull)
  def password = column[String]("PASSWORD", NotNull)
  def stationId = column[Int]("STATION_ID", NotNull)
  def roleId = column[Int]("ROLE_ID", NotNull)
  def * = (userName, password, stationId, roleId) <> (Account.tupled, Account.unapply)

  def station = foreignKey("HOME_FK", stationId, Homes.homes)(_.id, onUpdate = ForeignKeyAction.Cascade, onDelete = ForeignKeyAction.Restrict)
  def role = foreignKey("ROLE_FK", roleId, Roles.roles)(_.id, onUpdate = ForeignKeyAction.Cascade, onDelete = ForeignKeyAction.Restrict)
}

object Accounts
{
  private val accounts = TableQuery[Accounts]

  // アカウントが新規登録される際に、ID か パスワード に重複が見つからないかを見る。1行でも結果が返れば登録済みである。
  def checkDuplication(userName: String, password: String)(implicit session: Session) = accounts.filter(a => a.userName === userName || a.password === password).length.run == 0

  // checkDuplication() と似ているが、こちらはアカウント認証時にIDとPasswordの組が一致するかを見る。
  def matchAccount(userName: String, password: String)(implicit session: Session) = accounts.filter(a => a.userName === userName && a.password === password).length.run == 1

  def getRoleId(userName: String)(implicit session: Session) = accounts.filter(a => a.userName === userName).map(a => a.roleId).run

  def deleteAccount(id: Int)(implicit session: Session) = accounts.filter(a => a.id === id).delete

  def createAccount(account: Account)(implicit session: Session) = accounts.insert(account)

  def updateAccount(id: Int, account: Account)(implicit session: Session) = accounts.filter(a => a.id === id).update(account)
}
