package models.specs

import models.manageappidea.ApplicationIdea

/**
 * Created with IntelliJ IDEA.
 * User: mao
 * Date: 12/04/12
 * Time: 1:19
 * To change this template use File | Settings | File Templates.
 */

class AppIdeaExistsSpec extends TTSpecification[Long] {
  override def isSatisfiedBy(target: Long) = {
    if (ApplicationIdea.exists(target)) SpecificateSuccess
    else SpecificateResult(false, "This AppIdea is not found. IdeaId:" + target)
  }
}

object AppIdeaExistsSpec {
  def apply(target: Long) = {
    (new AppIdeaExistsSpec).isSatisfiedBy(target)
  }
}
