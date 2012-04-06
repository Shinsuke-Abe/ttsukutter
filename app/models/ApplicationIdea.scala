package models

/**
 * Created by IntelliJ IDEA.
 * User: mao
 * Date: 12/03/29
 * Time: 0:22
 * To change this template use File | Settings | File Templates.
 */

import scala.collection.mutable.ListBuffer

/**
 * アプリネタクラス
 * @param ideaId ネタID
 * @param ideamanId ネタ主ID
 * @param description ネタ概要
 * @param issues 公開アプリ一覧
 */
case class ApplicationIdea(
                            ideaId: Option[Long] = None,
                            ideamanId: Long,
                            description: String,
                            issues: ListBuffer[ApplicationIssue] = ListBuffer.empty) {
  def addIssue(issue: ApplicationIssue) {
    ApplicationIssueSpec.isSatisfiedBy(issue) match {
      case SpecificateSuccess => issues += issue
      case notSatisfied => throw new ApplicationIssueSpecificateException(notSatisfied.message)
    }
  }

  def updateIssue(issue: ApplicationIssue) {
    issues.update(issues.findIndexOf(target => target.issueId == issue.issueId), issue)
  }
}

/**
 * アプリネタクラス
 * @param issueId 公開アプリId
 * @param description 概要
 * @param url URL
 * @param craftmanId 概要
 */
case class ApplicationIssue(
                             issueId: Option[Long] = None,
                             description: String,
                             url: String,
                             craftmanId: Long) {
}

/**
 * アプリネタクラスのチェック仕様
 */
object ApplicationIssueSpec extends TTSpecification[ApplicationIssue] {
  override def isSatisfiedBy(target: ApplicationIssue) = {
    StringNotNothingSpec("Description", target.description) and
      StringNotNothingSpec("URL", target.url)
  }
}

/**
 * アプリネタクラスのチェック例外
 * @param s メッセージ
 */
class ApplicationIssueSpecificateException(s: String = null) extends Exception(s: String)

/**
 * アプリネタファクトリ
 */
object ApplicationIdea {

  def get(id: Long) = {
    // TODO データベースからの取得
    //new ApplicationIdea
  }
}

/**
 * アプリネタリポジトリ
 */
object ApplicationIdeaRepository {
  def createdList(ideamanId: Option[Long] = None, craftmanId: Option[Long] = None) = {
    // TODO データベースからの取得
    List()
  }

  def notCreatedList(ideamanId: Option[Long] = None) = {
    // TODO データベースからの取得
    List()
  }

  def newlyArrivedList() = {
    // TODO データベースからの取得
    List()
  }

  def findByText(word: String) = {
    // TODO 検索条件の分割
    // TODO データベースからの取得
    List()
  }

  def add(appIdea: ApplicationIdea) {
    // TODO INSERT文の発行
  }

  def update(appIdea: ApplicationIdea) {
    // TODO UPDATE文の発行
  }
}
