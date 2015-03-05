package models
import scala.slick.driver.MySQLDriver.simple._
import scala.slick.ast.ColumnOption.NotNull

case class Role(role: String, authority: Byte)

class Roles(tag: Tag) extends Table[(Int, Role)](tag, "ROLES")
{
  def id = column[Int]("ID", O.PrimaryKey, O.AutoInc)
  def role = column[String]("ROLE", NotNull)
  def authority = column[Byte]("AUTHORITY", NotNull)

  def * = (role, authority) <> (Role.tupled, Role.unapply)
}

object Roles
{
  private val roles = TableQuery[Roles]

  def getAuthority(id: Int)(implicit session: Session) = roles.filter(r => r.id === id).map(r => r.authority).run

  def createRoles(role: Roll)(implicit session: Session) = roles.insert(role)
  def updateRoles(id: Int, role: Roll)(implicit session: Session) = roles.filter(r => r.id === id).update(role)
  def deleteRoles(id: Int)(implicit session: Session) = roles.filter(r => r.id === id).delete
}
