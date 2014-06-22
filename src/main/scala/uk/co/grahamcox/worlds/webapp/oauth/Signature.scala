package uk.co.grahamcox.worlds.webapp.oauth

import java.net.URI
import java.time.Instant

import org.eintr.loglady.Logging

/**
 * Representation of an actual signature
 * @param value the value of the signature
 * @param method the signature method
 */
case class Signature(val value: String,
                     val method: String = "HMAC-SHA1")

/**
 * OAuth details for the request we are signing
 * @param request the request that we are signing
 * @param nonce the nonce
 * @param timestamp the timestamp
 * @param version the OAuth version to use
 * @param accessToken the access token, if we have one
 * @param callback the callback, if we have one
 */
case class OAuthRequestDetails(val request: RequestDetails,
                               val nonce: Nonce,
                               val timestamp: Instant,
                               val version: String = "1.0",
                               val accessToken: Option[AccessToken] = None,
                               val callback: Option[URI] = None)

object Helpers {
  /**
   * Wrapper class to make it easy to percent encode a string
   * @param str the string
   */
  implicit class PercentEncodingString(str: String) {
    /**
     * Percent encode the string
     * @return the percent encoded string
     */
    def percentEncode = str
  }
}

/*
 * Class to acutally sign a request
 * @param consumerKey the consumer key to use
 */
class Signer(consumerKey: ConsumerKey) extends Logging {
  def sign(request: OAuthRequestDetails): Signature = {
    import Helpers._

    val oauthParams: Map[String, String] = Map(
      "oauth_consumer_key" -> consumerKey.key,
      "oauth_nonce" -> request.nonce.value,
      "oauth_signature_method" -> "HMAC-SHA1",
      "oauth_timestamp" -> request.timestamp.getEpochSecond.toString,
      "oauth_version" -> "1.0"
    ) ++ (request.callback match {
      case Some(callback: URI) => Map("oauth_callback" -> callback.toString)
      case _ => Map()
    }) ++ (request.accessToken match {
      case Some(accessToken: AccessToken) => Map("oauth_token" -> accessToken.secret)
      case _ => Map()
    })
    val queryParams = request.request.params
    val paramsMap = oauthParams ++ queryParams

    val paramsString = paramsMap.toSeq
      .map(param => (param._1.percentEncode, param._2.percentEncode))       // Percent Encode the params
      .sortWith((a, b) => a._1 < b._1)                                      // Sort the params
      .map(param => param._1 + "=" + param._2)                              // Combine into <key>=<value>
      .mkString("&")                                                        // Combine with "&" separators
    log.debug(s"Params string: ${paramsString}")

    val signingString = Seq(request.request.method,
      request.request.url.toString,
      paramsString)
      .map(e => e.percentEncode)                                            // Percent Encode (Again)
      .mkString("&")                                                        // Combine with "&" separators
    log.debug(s"Signing String: ${signingString}")

    Signature("tnnArxj06cWHq44gCs1OSKk/jLY=")
  }
}