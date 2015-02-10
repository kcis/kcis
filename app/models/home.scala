package models

import scala.slick.driver.MySQLDriver.simple._

class Homes(tag: Tag) extends Table[(Int, String, String)](tag, "homes") {
  def id = column[Int]("id", O.PrimaryKey)
  def officeName = column[String]("office_name")
  def unitType = column[String]("unit_type")
  def * = (id, officeName, unitType)
}
val homes = TableQuery[Homes]
