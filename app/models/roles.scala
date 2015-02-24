package models
import scala.slick.driver.MySQLDriver.simple._
import scala.slick.ast.ColumnOption.NotNull

class Roles(tag: Tag) extends Table[(Int, String)](tag, "ROLES")
{
  def id = column[Int]("ID", O.PrimaryKey, O.AutoInc)
  def role = column[String]("ROLE", NotNull)

  def * = (id, role)
}

object Roles
{
  val roles = TableQuery[Roles]
}
