package models

import play.api.db.slick.Config.driver.simple._

case class Account(id: Int, email: String, password: String)

class Accounts(tag: Tag) extends Table[Account](tag, "ACCOUNT") {
  def id = column[Int]("ID", O.PrimaryKey)
  def email = column[String]("EMAIL")
  def password = column[String]("PASSWORD")
  def * = (id, email, password) <> (Account.tupled, Account.unapply _)
}

object Accounts {
  val account = TableQuery[Accounts]

  def authenticate(email: String, password: String): Option[Account] = {
    findByEmail(email).filter { account => password.equals(account.password) }
  }

  def findByEmail(email: String): Option[Account] = None // xxx
  def findById(id: Int): Option[Account] = None // xxx
}
