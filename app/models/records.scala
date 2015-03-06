package models
import scala.slick.driver.MySQLDriver.simple._

class Records(tag: Tag) extends Table[Nothing](tag, "RECORDS") {
}
