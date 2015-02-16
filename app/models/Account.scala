package models

import play.api.db.slick.Config.driver.simple._

case class Account(station_id: String, member_id: String, password: String)

class Accounts(tag: Tag) extends Table[Account](tag, "ACCOUNT") {
  def station_id = column[String]("STATION_ID", O.PrimaryKey)
  def member_id = column[String]("MEMBER_ID", O.PrimaryKey)
  def password = column[String]("PASSWORD")
  def * = (station_id, member_id, password) <> (Account.tupled, Account.unapply _)
}

object Accounts {
  val account = TableQuery[Accounts]

  def authenticate(sid: String, mid: String, password: String): Option[Account] = {
    findById((sid, mid)).filter { account => password.equals(account.password) }
  }

  def findById(smid: (String, String)): Option[Account] = None // xxx

  def findAll(): Seq[Account] = Seq.empty // xxx

  def create(account: Account) {
    // xxx
  }
}
