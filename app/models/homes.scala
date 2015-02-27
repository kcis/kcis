package models
import scala.slick.driver.MySQLDriver.simple._
import scala.slick.ast.ColumnOption.NotNull

case class Home(corporationName: String, officeName: String, careType: String)

class Homes(tag: Tag) extends Table[(Int, Home)](tag, "HOMES") {
  def id = column[Int]("ID", O.PrimaryKey, O.AutoInc)
  def corporationName = column[String]("CORPORATION_NAME", NotNull)
  def officeName = column[String]("OFFICE_NAME", NotNull)
  def careType = column[String]("CARE_TYPE", NotNull)
  def * = (corporationName, officeName, careType) <> (Home.tupled, Home.unapply)

  def indexHomes = index("HOMES_INDEX", id)
}

object Homes {
  val homes = TableQuery[Homes]

  def createHomes(home: Home)(implicit session: Session) = homes.insert(home)

  def updateHomes(id: Int, home: Home)(implicit session: Session) = homes.filter(h => h.id === id).update(home)

  def deleteHomes(id: Int)(implicit session: Session) = homes.filter(h => h.id === id).delete
}
