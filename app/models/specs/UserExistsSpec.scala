package models.specs

import models.manageuser.TTUser

/**
 * Created with IntelliJ IDEA.
 * User: mao
 * Date: 12/04/12
 * Time: 1:09
 * To change this template use File | Settings | File Templates.
 */

class UserExistsSpec extends TTSpecification[TTUser] {
  override def isSatisfiedBy(target: TTUser) = {
    SpecificateSuccess
  }
}

object UserExistsSpec {
  def apply(target: TTUser) = {
    (new UserExistsSpec).isSatisfiedBy(target)
  }
}
