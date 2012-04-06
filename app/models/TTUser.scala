package models

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
                   profile: String,
                   authInfo: TTUserAuthInfo,
                   userSiteList: ListBuffer[TTUserSite] = ListBuffer.empty,
                   favoriteList: ListBuffer[TTUserFavorite] = ListBuffer.empty) {
  def add {

  }

  def update {

  }
}

case class TTUserFavorite(ideaId: Int, seqNo: Int)

case class TTUserSite(dispNo: Int, url: String)

case class TTUserAuthInfo(authType: Int,
                          password: Option[String] = None,
                          accessToken: Option[String] = None,
                          tokenSecret: Option[String] = None)

object TTUserFactory {
  def get(userId: Long) = {
    //new TTUser
  }

  def create(
              name: String,
              profile: String,
              authInfo: TTUserAuthInfo,
              userSiteList: ListBuffer[TTUserSite] = ListBuffer.empty,
              favoriteList: ListBuffer[TTUserFavorite] = ListBuffer.empty) = {
    //new TTUser
  }
}