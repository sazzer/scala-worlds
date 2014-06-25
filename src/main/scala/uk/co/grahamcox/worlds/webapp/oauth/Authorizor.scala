package uk.co.grahamcox.worlds.webapp.oauth

import java.net.URI
import java.time.Clock

/**
 * Mechanism to generate the authorization header for a request
 * @param consumerKey The consumer key to use
 * @param signer The mechanism to generate a Signature
 * @param nonceGenerator The mechanism to generate a Nonce
 * @param clock The clock to get the current time from
 */
class Authorizor(consumerKey: ConsumerKey,
                 signer: Signer,
                 nonceGenerator: NonceGenerator,
                 clock: Clock) {
  import Utils._

  /**
   * Generate the value for the Authorization header
   * @param request The request details
   * @param accessToken The optional access token to use
   * @param callback The optional Callback value to use. Only makes sense for certain requests
   * @return the Authorization header
   */
  def authorize(request: RequestDetails,
                accessToken: Option[AccessToken],
                callback: Option[URI] = None): String = {
    val nonce = nonceGenerator.generate
    val instant = clock.instant()
    val signature = signer.sign(OAuthRequestDetails(
      request = request,
      nonce = nonce,
      timestamp = instant,
      accessToken = accessToken,
      callback = callback
      )
    )

    val oauthParts: Map[HeaderName, String] = Map(
      HeaderNames.CONSUMER_KEY -> consumerKey.key,
      HeaderNames.NONCE -> nonce.value,
      HeaderNames.SIGNATURE -> signature.value,
      HeaderNames.SIGNATURE_METHOD -> signature.method,
      HeaderNames.TIMESTAMP -> instant.getEpochSecond.toString,
      HeaderNames.VERSION -> "1.0") ++
      callback.map(cb => Map(HeaderNames.CALLBACK -> cb.toString)).getOrElse(Map()) ++
      accessToken.map(at => Map(HeaderNames.TOKEN -> at.key)).getOrElse(Map())

    oauthParts.toSeq
      .map(v => (v._1.toString, percentEncode(v._2)))
      .sortWith((a, b) => a._1 < b._1)                                      // Sort the params
      .map(param => param._1 + "=\"" + param._2 + "\"")                     // Combine into <key>=<value>
      .mkString("OAuth ", ", ", "")
  }
}
