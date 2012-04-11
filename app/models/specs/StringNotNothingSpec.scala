package models.specs

/**
 * Created with IntelliJ IDEA.
 * User: mao
 * Date: 12/04/12
 * Time: 1:07
 * To change this template use File | Settings | File Templates.
 */

class StringNotNothingSpec(name: String) extends TTSpecification[String] {
  override def isSatisfiedBy(target: String) = {
    target match {
      case "" => SpecificateResult(false, name + " is nothing")
      case null => SpecificateResult(false, name + " is null")
      case _ => SpecificateSuccess
    }
  }
}

object StringNotNothingSpec {
  def apply(name: String, target: String) = {
    (new StringNotNothingSpec(name)).isSatisfiedBy(target)
  }
}
