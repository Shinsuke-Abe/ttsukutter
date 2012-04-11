package models.specs

/**
 * Created with IntelliJ IDEA.
 * User: mao
 * Date: 12/04/12
 * Time: 1:13
 * To change this template use File | Settings | File Templates.
 */

class URLFormatSpec extends TTSpecification[String] {
  override def isSatisfiedBy(target: String) = {
    SpecificateSuccess
  }
}

object URLFormatSpec {
  def apply(target: String) = {
    (new URLFormatSpec).isSatisfiedBy(target)
  }
}
