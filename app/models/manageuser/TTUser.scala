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
import utils.exceptions.TTsukutterAppException
import anorm._
import anorm.SqlParser._
import play.api.db._
import play.api.Play.current

// domain layer
case class TTUser(
                   id: Option[Long] = None,
                   name: String,
                   profile: Option[String] = None,
                   authInfo: TTUserAuthInfo,
                   var userSiteList: ListBuffer[TTUserSite] = ListBuffer.empty,
                   var favoriteList: ListBuffer[TTUserFavorite] = ListBuffer.empty) {
  authInfo match {
	  case regularAuth: RegularAuthInfo => RegularAuthInfoSpec isSatisfiedBy(regularAuth) andThen()
	  case oAuthInfo: OAuthInfo => OAuthInfoSpec isSatisfiedBy(oAuthInfo) andThen()
	}
  
  def addFavoriteIdea(ideaId: Long) {
    AppIdeaExistsSpec(ideaId) andThen {
      if (favoriteList.exists(_.ideaId == ideaId)) throw new TTsukutterAppException("Added application idea: " + ideaId)
      favoriteList += new TTUserFavorite(dispNo = favoriteList.length + 1, ideaId = ideaId)
    }
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
    StringNotNothingSpec("User Site", url) andThen{
    	userSiteList += new TTUserSite(dispNo = userSiteList.length + 1, url = url)
    }
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

// infrastructure layer
object TTUser {
  val ttuser = {
    get[Long]("USER_ID") ~
    get[String]("USER_NAME") ~
    get[String]("USER_PROFILE") ~
    get[Int]("AUTH_TYPE") ~
    get[String]("AUTH_INFO_1") ~
    get[String]("AUTH_INFO_2") map {
      case userId ~ userName ~ userProfile ~ authType ~ authInfo1 ~ authInfo2 =>
        // とりあえず通常認証はOAuth以外とする
        if (authType == 1) TTUser(Some(userId), userName, Some(userProfile), OAuthInfo(authInfo1, authInfo2))
        else TTUser(Some(userId), userName, Some(userProfile), RegularAuthInfo(authInfo1, authInfo2))
    }
  }
  
  val findByKeyQuery = SQL("""
        select * from M_TT_USER
        where USER_ID = {userId}""")
  
  val findByAuthQuery = SQL("""
        select * from M_TT_USER
        where
          AUTH_TYPE = {authType} and
          AUTH_INFO_1 = {authInfo1} and
          AUTH_INFO_2 = {authInfo2}""")
  
  def create(ttuser: TTUser) {
    if (exists(ttuser.authInfo))
      throw new TTsukutterAppException("Auth info for insert is duplicate. AuthInfo1:" + ttuser.authInfo.getAuthInfoTuple._2)
    val (authType, authInfo1, authInfo2) = ttuser.authInfo.getAuthInfoTuple
    DB.withConnection {implicit c =>
      SQL("""
          insert into M_TT_USER(
            USER_NAME,
            USER_PROFILE,
            AUTH_TYPE,
            AUTH_INFO_1,
            AUTH_INFO_2)
          values (
            {userName},
            {userProfile},
            {authType},
            {authInfo1},
            {authInfo2}
          )""")
      .on(
          'userName -> ttuser.name,
          'userProfile -> ttuser.profile.get,
          'authType -> authType,
          'authInfo1 -> authInfo1,
          'authInfo2 -> authInfo2)
      .executeUpdate()
    }
  }
  
  def update(ttuser: TTUser) {
    if (exists(ttuser.id.get) == false)
      throw new TTsukutterAppException("User for update is not found. UserId:" + ttuser.id.get)
    val (authType, authInfo1, authInfo2) = ttuser.authInfo.getAuthInfoTuple
    DB.withConnection{implicit c =>
      SQL("""
          update M_TT_USER set
            USER_NAME = {userName},
            USER_PROFILE = {userProfile},
            AUTH_TYPE = {authType},
            AUTH_INFO_1 = {authInfo1},
            AUTH_INFO_2 = {authInfo2}
          where
            USER_ID = {userId}""")
      .on(
          'userName -> ttuser.name,
          'userProfile -> ttuser.profile.get,
          'authType -> authType,
          'authInfo1 -> authInfo1,
          'authInfo2 -> authInfo2,
          'userId -> ttuser.id.get)
      .executeUpdate()
    }
  }
  
  def findById(userId: Long) = {
    DB.withConnection{implicit c =>
      findByKeyQuery
      .on('userId -> userId)
      .as(ttuser *)
    }.head
  }
  
  def findByAuthInfo(authInfo: TTUserAuthInfo) = {
    val (authType, authInfo1, authInfo2) = authInfo.getAuthInfoTuple
    DB.withConnection{implicit c =>
      findByAuthQuery
      .on(
          'authType -> authType,
          'authInfo1 -> authInfo1,
          'authInfo2 -> authInfo2)
      .as(ttuser *)
    }.head
  }
  
  def exists(userId: Long) = {
    DB.withConnection{implicit c =>
      findByKeyQuery
      .on('userId -> userId)
      .as(ttuser *)
    }.size > 0
  }
  
  def exists(authInfo: TTUserAuthInfo) = {
    val (authType, authInfo1, authInfo2) = authInfo.getAuthInfoTuple
    DB.withConnection{implicit c =>
      findByAuthQuery
      .on(
          'authType -> authType,
          'authInfo1 -> authInfo1,
          'authInfo2 -> authInfo2)
      .as(ttuser *)
    }.size > 0
  }
}


