package models.specs

trait TTSpecification[A] {
  def isSatisfiedBy(target: A): SpecificateResult
}

class TTSpecificateException(s: String) extends Exception(s: String)

case class SpecificateResult(result: Boolean, message: String) {
  def and(other: => SpecificateResult) = {
    if (this.result == false) this
    else if (other.result == false) other
    else SpecificateSuccess
  }

  def or(other: => SpecificateResult) = {
    if (this.result && other.result) SpecificateSuccess
    else SpecificateResult(false, this.message + ":" + other.message)
  }

  def not = {
    SpecificateResult(!this.result, this.message)
  }
  
  def andThen(f: => Unit) {
    if (result) f
    else throw new TTSpecificateException(message)
  }
}

object SpecificateSuccess extends SpecificateResult(true, "")