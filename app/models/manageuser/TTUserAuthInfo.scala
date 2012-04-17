package models.manageuser

import models.specs._

abstract class TTUserAuthInfo()

case class OAuthInfo(accessToken: String, tokenSecret: String) extends TTUserAuthInfo

case class RegularAuthInfo(mailAddress: String, passrowd: String) extends TTUserAuthInfo

object RegularAuthInfoSpec extends TTSpecification[RegularAuthInfo] {
  override def isSatisfiedBy(target: RegularAuthInfo) = {
    StringNotNothingSpec("EMail Address", target.mailAddress) and
    StringNotNothingSpec("Password", target.passrowd)
  }
}

object OAuthInfoSpec extends TTSpecification[OAuthInfo] {
  override def isSatisfiedBy(target: OAuthInfo) = {
    StringNotNothingSpec("Access Token", target.accessToken) and
    StringNotNothingSpec("Token Secret", target.tokenSecret)
  }
}