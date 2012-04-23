package models.manageappidea

import models.specs._
import scala.collection.mutable._

case class ApplicationIdea(
                            ideaId: Option[Long] = None,
                            ideamanId: Long,
                            description: String,
                            issues: ListBuffer[ApplicationIssue] = ListBuffer.empty) {
  private def issueExecute(issue: ApplicationIssue, executor: ApplicationIssue => Unit) {
    ApplicationIssueSpec isSatisfiedBy(issue) andThen executor(issue)
  }

  def addIssue(issue: ApplicationIssue) {
    var addIssue = ApplicationIssue(Some(issues.length + 1), issue.description, issue.url, issue.craftmanId)
    issueExecute(issue, {_ => issues += addIssue})
  }

  def updateIssue(issue: ApplicationIssue) {
    issueExecute(
      issue,
      {newValue => issues.update(issues.findIndexOf(target => target.seqNo == newValue.seqNo), newValue)}
    )
  }
}

case class ApplicationIssue(
                             seqNo: Option[Int] = None,
                             description: String,
                             url: String,
                             craftmanId: Int) {
}

object ApplicationIdea {

  def get(id: Long) = {
    // TODO データベースからの取得
    //new ApplicationIdea
  }
}

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

object ApplicationIssueSpec extends TTSpecification[ApplicationIssue] {
  override def isSatisfiedBy(target: ApplicationIssue) = {
    StringNotNothingSpec("Description", target.description) and
      StringNotNothingSpec("URL", target.url)
  }
}

class ApplicationIssueSpecificateException(s: String = null) extends Exception(s: String)