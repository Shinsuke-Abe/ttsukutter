package models.specs

class StringLengthSpec(minLength: Int = 0, maxLength: Int = Int.MaxValue, name: String) extends TTSpecification[String] {
	override def isSatisfiedBy(target: String) = {
	  if (target.length < minLength) SpecificateResult(false, name + "'s length can not be under " + minLength)
	  else if (target.length > maxLength) SpecificateResult(false, name + "'s length can not be over " + maxLength)
	  else SpecificateSuccess
	}
}

object StringLengthSpec {
  def apply(minLength: Int = 0, maxLength: Int = Int.MaxValue, name: String, target: String) = {
    (new StringLengthSpec(minLength, maxLength, name)).isSatisfiedBy(target)
  }
}