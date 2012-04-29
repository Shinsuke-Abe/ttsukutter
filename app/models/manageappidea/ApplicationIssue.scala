package models.manageappidea

import models.specs._
import scala.collection.mutable._
import anorm._
import anorm.SqlParser._
import play.api.db._
import play.api.Play.current
import utils.exceptions.TTsukutterAppException

// domain layer
case class ApplicationIssue(
                             seqNo: Option[Int] = None,
                             description: String,
                             url: String,
                             craftmanId: Int) {
}

object ApplicationIssueSpec extends TTSpecification[ApplicationIssue] {
  override def isSatisfiedBy(target: ApplicationIssue) = {
    StringNotNothingSpec("Description", target.description) and
    StringLengthSpec(maxLength = 140, name = "Description", target = target.description) and
    StringNotNothingSpec("URL", target.url) and
    StringLengthSpec(maxLength = 1024, name = "URL", target = target.url)
  }
}

// infrastructure layer
object ApplicationIssue {
  val applicationIssue = {
    get[Long]("IDEA_ID") ~
    get[Int]("SEQ_NO") ~
    get[String]("ISSUE_DESCLIPTION") ~
    get[String]("ISSUE_URL") ~
    get[Int]("CRAFTMAN_ID") map {
      case ideaId ~ seqNo ~ description ~ url ~ craftmanId =>
        ApplicationIssue(Some(seqNo), description, url, craftmanId)
    }
  }
  
  def findByIdeaId(ideaId: Long) = {
    DB.withConnection { implicit c =>
      SQL("""
          select * from T_APPLICATION_ISSUE
          where IDEA_ID = {ideaId}
          """)
      .on('ideaId -> ideaId)
      .as(applicationIssue *)
    }
  }
  
  def create(ideaId: Long, addIssue: ApplicationIssue) {
    DB.withConnection{implicit c =>
      if (exists(ideaId, addIssue.seqNo.get))
        throw new TTsukutterAppException("This Issue is exists. IDEA_ID:%d SEQ_NO:%d".format(ideaId, addIssue.seqNo.get))
      
      SQL("""
          insert into T_APPLICATION_ISSUE (
    		  IDEA_ID,
    		  SEQ_NO,
    		  ISSUE_DESCLIPTION,
    		  ISSUE_URL,
    		  CRAFTMAN_ID
          ) values (
    		  {ideaId},
    		  {seqNo},
    		  {description},
    		  {url},
    		  {craftmanId}
          )
          """)
      .on(
          'ideaId -> ideaId,
          'seqNo -> addIssue.seqNo.get,
          'description -> addIssue.description,
          'url -> addIssue.url,
          'craftmanId -> addIssue.craftmanId)
      .executeUpdate()
    }
  }
  
  def update(ideaId: Long, updateIssue: ApplicationIssue) {
    DB.withConnection{ implicit c =>
      if (exists(ideaId, updateIssue.seqNo.get) == false)
        throw new TTsukutterAppException("Not exists Issue can not update. IDEA_ID:%d SEQ_NO:%d".format(ideaId, updateIssue.seqNo.get))
      
      SQL("""
          update T_APPLICATION_ISSUE
          set
    		  ISSUE_DESCLIPTION = {description},
    		  ISSUE_URL = {url},
    		  CRAFTMAN_ID = {craftmanId}
          where
    		  IDEA_ID = {ideaId} and SEQ_NO = {seqNo}
          """)
      .on(
          'description -> updateIssue.description,
          'url -> updateIssue.url,
          'craftmanId -> updateIssue.craftmanId,
          'ideaId -> ideaId,
          'seqNo -> updateIssue.seqNo.get)
      .executeUpdate()
    }
  }
  
  def nextSeqNo(ideaId: Long) = {
    DB.withConnection{implicit c =>
      val issues = findByIdeaId(ideaId)
      if (issues.isEmpty) 1
      else (issues.reduce((z, n) => if (z.seqNo.get > n.seqNo.get) z else n).seqNo.get + 1)
    }
  }
  
  private def exists(ideaId: Long, seqNo: Int) = {
    DB.withConnection( implicit c =>
      SQL("""select * from T_APPLICATION_ISSUE
          where IDEA_ID = {ideaId} and SEQ_NO = {seqNo}
          """)
      .on('ideaId -> ideaId, 'seqNo -> seqNo)
      .as(applicationIssue *).length > 0)
  }
}