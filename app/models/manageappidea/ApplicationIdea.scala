package models.manageappidea

import models.specs._
import anorm._
import anorm.SqlParser._
import play.api.db._
import play.api.Play.current
import utils.exceptions.TTsukutterAppException

// domain layer
case class ApplicationIdea(
                            ideaId: Option[Long] = None,
                            ideamanId: Long,
                            description: String,
                            var issues: List[ApplicationIssue] = List.empty) {
  ApplicationIdeaSpec isSatisfiedBy(this) andThen()
  
  private def issueExecute(issue: ApplicationIssue, executor: ApplicationIssue => Unit) {
    if (ApplicationIdea.exists(ideaId.get) == false)
      throw new TTsukutterAppException("Issue can not add App Idea not exists. ideaId:" + ideaId.get)
    
    ApplicationIssueSpec isSatisfiedBy(issue) andThen {
      executor(issue)
      issues = ApplicationIssue.findByIdeaId(ideaId.get)
    }
  }

  def addIssue(issue: ApplicationIssue) {
    val addIssue = ApplicationIssue(Some(ApplicationIssue.nextSeqNo(ideaId.get)), issue.description, issue.url, issue.craftmanId)
    issueExecute(addIssue, ApplicationIssue.create(ideaId.get, _))
  }

  def updateIssue(issue: ApplicationIssue) {
    issueExecute(issue, ApplicationIssue.update(ideaId.get, _))
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

// infrastructure layer
object ApplicationIdea {
  val applicationIdea = {
    get[Long]("IDEA_ID") ~
    get[Long]("IDEAMAN_ID") ~
    get[String]("IDEA_DESCRIPTION") map {
      case ideaId ~ ideamanId ~ ideaDescription =>
        ApplicationIdea(Some(ideaId), ideamanId, ideaDescription)
    }
  }
  
  val findByIdQuery = SQL("""
             select * from T_APPLICATION_IDEA
             where IDEA_ID = {ideaId}""")
  
  def create(appIdea: ApplicationIdea) {
    DB.withConnection { implicit c =>
      if (exists(appIdea.ideaId.get))
        throw new TTsukutterAppException("This idea ID is exists:" + appIdea.ideaId.get)
      
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
      findByIdQuery
      .on('ideaId -> id)
      .as(applicationIdea *)
    }).head
  }
  
  def exists(id: Long) = {
    (DB.withConnection {implicit c =>
      findByIdQuery
      .on('ideaId -> id)
      .as(applicationIdea *).length > 0
      })
  }
}
