package models.specs

/**
 * Created with IntelliJ IDEA.
 * User: mao
 * Date: 12/04/12
 * Time: 1:16
 * To change this template use File | Settings | File Templates.
 */

class MailAddressFormatSpec extends TTSpecification[String] {
  override def isSatisfiedBy(target: String) = {
    SpecificateSuccess
  }
}

object MailAddressFormatSpec {
  def apply(target: String) = {
    (new MailAddressFormatSpec).isSatisfiedBy(target)
  }
}