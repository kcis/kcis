package models

import scala.slick.driver.MySQLDriver.simple._

class Homes(tag: Tag) extends Table[(Int, String, String)](tag, "homes") {
  def id = column[Int]("id", O.PrimaryKey, O.AutoInc)
  def corporationName = column[String]("corporationName")
  def officeName = column[String]("officeName")
  def careType = column[String]("careType")
  def * = (id, corporationName, officeName, careType)

  def indexHomes = index("indexHomes", id)
}
val homes = TableQuery[Homes]
