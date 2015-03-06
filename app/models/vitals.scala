package models
import scala.slick.driver.MySQLDriver.simple._

class Vitals(tag: Tag) extends Table[Nothing](tag, "VITALS"){
}
