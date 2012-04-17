package models.manageuser

/**
 * Created by IntelliJ IDEA.
 * User: mao
 * Date: 12/03/29
 * Time: 0:40
 * To change this template use File | Settings | File Templates.
 */

import scala.collection.mutable._
import models.specs._

case class TTUser(
                   id: Option[Long] = None,
                   name: String,
                   profile: Option[String] = None,
                   authInfo: TTUserAuthInfo,
                   var userSiteList: ListBuffer[TTUserSite] = ListBuffer.empty,
                   var favoriteList: ListBuffer[TTUserFavorite] = ListBuffer.empty) {
  authInfo match {
	  case regularAuth: RegularAuthInfo =>{
	    RegularAuthInfoSpec.isSatisfiedBy(regularAuth) match {
	      case SpecificateSuccess =>
	      case notSatisfied => throw new RegularAuthInfoSpecificateException(notSatisfied.message)
	    }
	  }
	  case oAuthInfo: OAuthInfo => {
	    OAuthInfoSpec.isSatisfiedBy(oAuthInfo) match {
	      case SpecificateSuccess =>
	      case notSatisfied => throw new OAuthInfoSpecificateException(notSatisfied.message)
	    }
	  }
	}
  
  def addFavoriteIdea(ideaId: Long) {
    favoriteList += new TTUserFavorite(dispNo = favoriteList.length + 1, ideaId = ideaId)
  }

  def removeFavoriteIdea(ideaId: Long) {
    favoriteList.remove(favoriteList.findIndexOf(_.ideaId == ideaId))
    renumberFavoriteList
  }

  def changeDispNoFavoriteIdea(ideaId: Long, destDispNo: Int) {
    val newIndex = calculateNewIndex(destDispNo, favoriteList.length)
    favoriteList.find(_.ideaId == ideaId) match {
      case Some(moveFavorite) => {
        favoriteList = favoriteList.filter(_.ideaId != ideaId)
        favoriteList.insert(newIndex, moveFavorite)
        renumberFavoriteList
      }
      case _ =>
    }
  }

  private def renumberFavoriteList {
    favoriteList = favoriteList.zipWithIndex.map(x => new TTUserFavorite(dispNo = x._2 + 1, ideaId = x._1.ideaId))
  }

  def addUserSite(url: String) {
    userSiteList += new TTUserSite(dispNo = userSiteList.length + 1, url = url)
  }

  def removeUserSite(url: String) {
    userSiteList.remove(userSiteList.findIndexOf(_.url == url))
    renumberUserSiteList
  }

  def changeDispNoUserSite(url: String, destDispNo: Int) {
    val newIndex = calculateNewIndex(destDispNo, userSiteList.length)
    userSiteList.find(_.url == url) match {
      case Some(moveUserSite) => {
        userSiteList = userSiteList.filter(_.url != url)
        userSiteList.insert(newIndex, moveUserSite)
        renumberUserSiteList
      }
      case _ =>
    }
  }

  private def renumberUserSiteList {
    userSiteList = userSiteList.zipWithIndex.map(x => new TTUserSite(dispNo = x._2 + 1, url = x._1.url))
  }

  private def calculateNewIndex(destNo: Int, listSize: Int) = {
    if (destNo < 1) 0
    else if (destNo > listSize) listSize - 1
    else destNo - 1
  }
}

case class TTUserFavorite(id: Option[Int] = None, dispNo: Int, ideaId: Long)

case class TTUserSite(id: Option[Int] = None, dispNo: Int, url: String)

abstract class TTUserAuthInfo()

case class OAuthInfo(accessToken: String, tokenSecret: String) extends TTUserAuthInfo

case class RegularAuthInfo(mailAddress: String, passrowd: String) extends TTUserAuthInfo

object TTUser {
  def get(userId: Long) = {
    //new TTUser
  }
}

object RegularAuthInfoSpec extends TTSpecification[RegularAuthInfo] {
  override def isSatisfiedBy(target: RegularAuthInfo) = {
    StringNotNothingSpec("EMail Address", target.mailAddress) and
    StringNotNothingSpec("Password", target.passrowd)
  }
}

object OAuthInfoSpec extends TTSpecification[OAuthInfo] {
  override def isSatisfiedBy(target: OAuthInfo) = {
    StringNotNothingSpec("Access Token", target.accessToken)
  }
}

class RegularAuthInfoSpecificateException(s: String = null) extends Exception(s: String)
class OAuthInfoSpecificateException(s: String = null) extends Exception(s: String)