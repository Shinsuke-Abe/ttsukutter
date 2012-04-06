package models

/**
 * Created with IntelliJ IDEA.
 * User: mao
 * Date: 12/04/06
 * Time: 10:53
 * To change this template use File | Settings | File Templates.
 */

trait TTSpecification[A] {
  def isSatisfiedBy(target: A): SpecificateResult
}

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

case class SpecificateResult(result: Boolean, message: String) {
  def and(other: SpecificateResult) = {
    if (this.result == false) this
    else if (other.result == false) other
    else SpecificateSuccess
  }

  def or(other: SpecificateResult) = {
    if (this.result && other.result) SpecificateSuccess
    else SpecificateResult(false, this.message + ":" + other.message)
  }

  def not = {
    SpecificateResult(!this.result, this.message)
  }
}

object SpecificateSuccess extends SpecificateResult(true, "")