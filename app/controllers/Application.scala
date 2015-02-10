package controllers

import jp.t2v.lab.play2.auth.AuthConfig
import play.api._
import play.api.mvc._

import scala.concurrent.{Future, ExecutionContext}
import scala.reflect._

trait AuthConfigImpl extends AuthConfig {
  /**
   * ユーザを識別するIDの型です。String や Int や Long などが使われるでしょう。
   */
  type Id = String
  /**
   * あなたのアプリケーションで認証するユーザを表す型です。
   * User型やAccount型など、アプリケーションに応じて設定してください。
   */
  type User = Account
  /**
   * 認可(権限チェック)を行う際に、アクション毎に設定するオブジェクトの型です。
   * このサンプルでは例として以下のような trait を使用しています。
   *
   * sealed trait Role
   * case object Administrator extends Role
   * case object NormalUser extends Role
   */
  type Authority = Role
  /**
   * CacheからユーザIDを取り出すための ClassTag です。
   * 基本的にはこの例と同じ記述をして下さい。
   */
  val idTag: ClassTag[Id] = classTag[Id]
  /**
   * セッションタイムアウトの時間(秒)です。
   */
  val sessionTimeoutInSeconds: Int = 3600
  /**
   * ユーザIDからUserブジェクトを取得するアルゴリズムを指定します。
   * 任意の処理を記述してください。
   */
  def resolveUser(id: Id)(implicit ctx: ExecutionContext): Future[Option[User]] = Account.findByIdAsync(id)
  /**
   * ログインが成功した際に遷移する先を指定します。
   */
  def loginSucceeded(request: RequestHeader)(implicit ctx: ExecutionContext): Future[Result] =
    Future.successful(Redirect(routes.Message.main))
  /**
   * ログアウトが成功した際に遷移する先を指定します。
   */
  def logoutSucceeded(request: RequestHeader)(implicit ctx: ExecutionContext): Future[Result] =
    Future.successful(Redirect(routes.Application.login))
  /**
   * 認証が失敗した場合に遷移する先を指定します。
   */
  def authenticationFailed(request: RequestHeader)(implicit ctx: ExecutionContext): Future[Result] =
    Future.successful(Redirect(routes.Application.login))
  /**
   * 認可(権限チェック)が失敗した場合に遷移する先を指定します。
   */
  def authorizationFailed(request: RequestHeader)(implicit ctx: ExecutionContext): Future[Result] = 
    Future.successful(Forbidden("no permission"))
  /**
   * 権限チェックのアルゴリズムを指定します。
   * 任意の処理を記述してください。
   */
  def authorize(user: User, authority: Authority)(implicit ctx: ExecutionContext): Future[Boolean] = Future.successful {
    (user.role, authority) match {
      case (Administrator, _) => true
      case (NormalUser, NormalUser) => true
      case _ => false
    }
  }
  /**
   * SessionID Cookieにsecureオプションを指定するか否かの設定です。
   * デフォルトでは利便性のために false になっていますが、
   * 実際のアプリケーションでは true にすることを強く推奨します。
   */
  override lazy val cookieSecureOption: Boolean = play.api.Play.isProd(play.api.Play.current)
  /**
   * SessionID Cookieの有効期限を、ブラウザが閉じられるまでにするか否かの設定です。
   * デフォルトは false です。
   */
  override lazy val isTransientCookie: Boolean = false
}

object Application extends Controller with LoginLogout with AuthConfigImpl {

  def index = Action {
    Ok(views.html.index("Your new application is ready."))
  }

}
