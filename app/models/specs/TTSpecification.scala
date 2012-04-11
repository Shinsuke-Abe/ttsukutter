package models.specs

trait TTSpecification[A] {
  def isSatisfiedBy(target: A): SpecificateResult
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