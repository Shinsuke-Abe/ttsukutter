package models.specs

import models.manageuser.TTUser

/**
 * Created with IntelliJ IDEA.
 * User: mao
 * Date: 12/04/12
 * Time: 1:09
 * To change this template use File | Settings | File Templates.
 */

class UserExistsSpec extends TTSpecification[Long] {
  override def isSatisfiedBy(target: Long) = {
    if (TTUser.exists(target)) SpecificateSuccess
    else SpecificateResult(false, "User is not found. UserId:" + target)
  }
}

object UserExistsSpec {
  def apply(target: Long) = {
    (new UserExistsSpec).isSatisfiedBy(target)
  }
}
