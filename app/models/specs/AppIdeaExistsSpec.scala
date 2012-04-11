package models.specs

import models.manageappidea.ApplicationIdea

/**
 * Created with IntelliJ IDEA.
 * User: mao
 * Date: 12/04/12
 * Time: 1:19
 * To change this template use File | Settings | File Templates.
 */

class AppIdeaExistsSpec extends TTSpecification[ApplicationIdea] {
  override def isSatisfiedBy(target: ApplicationIdea) = {
    SpecificateSuccess
  }
}

object AppIdeaExistsSpec {
  def apply(target: ApplicationIdea) = {
    (new AppIdeaExistsSpec).isSatisfiedBy(target)
  }
}
