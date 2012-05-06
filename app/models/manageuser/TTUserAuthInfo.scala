package models.manageuser

import models.specs._

abstract class TTUserAuthInfo() {
  def getAuthInfoTuple:(Int, String, String)
}

case class OAuthInfo(accessToken: String, tokenSecret: String) extends TTUserAuthInfo {
  override def getAuthInfoTuple = (1, accessToken, tokenSecret)
}

case class RegularAuthInfo(mailAddress: String, password: String) extends TTUserAuthInfo {
  override def getAuthInfoTuple = (2, mailAddress, password)
}

object RegularAuthInfoSpec extends TTSpecification[RegularAuthInfo] {
  override def isSatisfiedBy(target: RegularAuthInfo) = {
    StringNotNothingSpec("EMail Address", target.mailAddress) and
    StringNotNothingSpec("Password", target.password)
  }
}

object OAuthInfoSpec extends TTSpecification[OAuthInfo] {
  override def isSatisfiedBy(target: OAuthInfo) = {
    StringNotNothingSpec("Access Token", target.accessToken) and
    StringNotNothingSpec("Token Secret", target.tokenSecret)
  }
}