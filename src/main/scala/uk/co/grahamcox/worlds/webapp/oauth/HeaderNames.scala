package uk.co.grahamcox.worlds.webapp.oauth

/**
 * Base class for a header name
 * @param value the value of the header name
 */
abstract class HeaderName(val value: String) {
  /**
   * Get the value of the header name
   * @return the header name
   */
  override def toString = value
}

/**
 * Collection of the header names
 */
object HeaderNames {
  object CALLBACK extends HeaderName("oauth_callback")
  object CONSUMER_KEY extends HeaderName("oauth_consumer_key")
  object NONCE extends HeaderName("oauth_nonce")
  object TIMESTAMP extends HeaderName("oauth_timestamp")
  object TOKEN extends HeaderName("oauth_token")
  object SIGNATURE extends HeaderName("oauth_signature")
  object SIGNATURE_METHOD extends HeaderName("oauth_signature_method")
  object VERSION extends HeaderName("oauth_version")
}