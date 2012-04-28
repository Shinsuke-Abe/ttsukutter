package models.manageappidea

import models.specs._
import scala.collection.mutable._
import anorm._
import anorm.SqlParser._
import play.api.db._
import play.api.Play.current

case class ApplicationIdea(
                            ideaId: Option[Long] = None,
                            ideamanId: Long,
                            description: String,
                            issues: ListBuffer[ApplicationIssue] = ListBuffer.empty) {
  ApplicationIdeaSpec isSatisfiedBy(this) andThen()
  
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
  val applicationIdea = {
    get[Long]("IDEA_ID") ~
    get[Long]("IDEAMAN_ID") ~
    get[String]("IDEA_DESCRIPTION") map {
      case ideaId ~ ideamanId ~ ideaDescription =>
        ApplicationIdea(Some(ideaId), ideamanId, ideaDescription)
    }
  }
  
  def create(appIdea: ApplicationIdea) {
    DB.withConnection { implicit c =>
      SQL("""
    	insert into T_APPLICATION_IDEA (
    		IDEA_ID,
      		IDEAMAN_ID,
      		IDEA_DESCRIPTION)
      	values (
      		{ideaId},
      		{ideamanId},
      		{ideaDescription})""")
      	.on(
      	    'ideaId -> appIdea.ideaId.get,
      	    'ideamanId -> appIdea.ideamanId,
      	    'ideaDescription -> appIdea.description)
      	.executeUpdate()
    }
  }
  
  def findById(id: Long) = {
    (DB.withConnection {implicit c =>
      SQL("""
          select * from T_APPLICATION_IDEA
          where IDEA_ID = {ideaId}""")
      .on('ideaId -> id)
      .as(applicationIdea *)
    }).head
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

  def update(appIdea: ApplicationIdea) {
    // TODO UPDATE文の発行
  }
}

object ApplicationIdeaSpec extends TTSpecification[ApplicationIdea] {
  override def isSatisfiedBy(target: ApplicationIdea) = {
    StringNotNothingSpec("Description", target.description) and
    	StringLengthSpec(maxLength = 140, name = "Description", target = target.description)
  }
}

object ApplicationIssueSpec extends TTSpecification[ApplicationIssue] {
  override def isSatisfiedBy(target: ApplicationIssue) = {
    StringNotNothingSpec("Description", target.description) and
      StringLengthSpec(maxLength = 140, name = "Description", target = target.description) and
      StringNotNothingSpec("URL", target.url) and
      StringLengthSpec(maxLength = 1024, name = "URL", target = target.url)
  }
}