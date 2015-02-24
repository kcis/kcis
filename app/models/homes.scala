package models
import scala.slick.driver.MySQLDriver.simple._
import scala.slick.ast.ColumnOption.NotNull

class Homes(tag: Tag) extends Table[(Int, String, String, String)](tag, "HOMES") {
  def id = column[Int]("ID", O.PrimaryKey, O.AutoInc)
  def corporationName = column[String]("CORPORATION_NAME", NotNull)
  def officeName = column[String]("OFFICE_NAME", NotNull)
  def careType = column[String]("CARE_TYPE", NotNull)
  def * = (id, corporationName, officeName, careType)

  def indexHomes = index("HOMES_INDEX", id)
}

object Homes {
  val homes = TableQuery[Homes]
}
