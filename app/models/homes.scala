package models
import scala.slick.driver.MySQLDriver.simple._
import scala.slick.ast.ColumnOption.NotNull

class Homes(tag: Tag) extends Table[(Int, String, String)](tag, "homes") {
  def id = column[Int]("id", O.PrimaryKey, O.AutoInc)
  def corporationName = column[String]("corporationName", O.Length(255, false), NotNull)
  def officeName = column[String]("officeName", O.Length(255, false), NotNull)
  def careType = column[String]("careType", O.Length(255, false), NotNull)
  def * = (id, corporationName, officeName, careType)

  def indexHomes = index("indexHomes", id)
}

val homes = TableQuery[Homes]
