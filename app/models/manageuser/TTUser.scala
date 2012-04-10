package models.manageuser

/**
 * Created by IntelliJ IDEA.
 * User: mao
 * Date: 12/03/29
 * Time: 0:40
 * To change this template use File | Settings | File Templates.
 */

import scala.collection.mutable._

case class TTUser(
                   id: Option[Long] = None,
                   name: String,
                   profile: Option[String] = None,
                   authInfo: TTUserAuthInfo,
                   userSiteList: ListBuffer[TTUserSite] = ListBuffer.empty,
                   favoriteList: ListBuffer[TTUserFavorite] = ListBuffer.empty) {
  def addFavoriteIdea(ideaId: Long) {
    favoriteList += new TTUserFavorite(ideaId, favoriteList.length + 1)
  }

  def addUserSite(url: String) {
    userSiteList += new TTUserSite(userSiteList.length + 1, url)
  }
}

case class TTUserFavorite(ideaId: Long, seqNo: Int)

case class TTUserSite(dispNo: Int, url: String)

case class TTUserAuthInfo(authType: Int,
                          password: Option[String] = None,
                          accessToken: Option[String] = None,
                          tokenSecret: Option[String] = None)

object TTUser {
  def get(userId: Long) = {
    //new TTUser
  }
}