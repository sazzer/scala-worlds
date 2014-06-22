package uk.co.grahamcox.worlds.webapp.oauth

import java.net.{URLEncoder, URI}
import java.time.Instant
import java.util.Base64
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

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

/*
 * Class to acutally sign a request
 * @param consumerKey the consumer key to use
 */
class Signer(consumerKey: ConsumerKey) extends Logging {
  def sign(request: OAuthRequestDetails): Signature = {
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
      case Some(accessToken: AccessToken) => Map("oauth_token" -> accessToken.key)
      case _ => Map()
    })
    val queryParams = request.request.params
    val paramsMap = oauthParams ++ queryParams

    val paramsString = paramsMap.toSeq
      .map(param => (percentEncode(param._1), percentEncode(param._2)))     // Percent Encode the params
      .sortWith((a, b) => a._1 < b._1)                                      // Sort the params
      .map(param => param._1 + "=" + param._2)                              // Combine into <key>=<value>
      .mkString("&")                                                        // Combine with "&" separators
    log.debug(s"Params string: ${paramsString}")

    val signingString = Seq(request.request.method,
      request.request.url.toString,
      paramsString)
      .map(e => percentEncode(e))                                           // Percent Encode (Again)
      .mkString("&")                                                        // Combine with "&" separators
    log.debug(s"Signing String: ${signingString}")

    val signingKey = percentEncode(consumerKey.secret) +
      "&" +
      percentEncode(request.accessToken.map(_.secret).getOrElse(""))
    log.debug(s"Signing Key: ${signingKey}")

    Signature(hash(signingString, signingKey))
  }

  /**
   * Percent encode the string
   * @param str The string to encide
   * @return the percent encoded string
   */
  private def percentEncode(str: String) = {
    val dontEncode = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz-._~"

    (str.map {
      c => c match {
        case c if dontEncode.contains(c) => c
        case c => "%" + c.toInt.toHexString.toUpperCase
      }
    }).mkString("")
  }

  /**
   * Generate the HMAC-SHA1 hash of the given string with the given key
   * @param signingString the string to hash
   * @param signingKey the key to hash with
   * @return the hash
   */
  private def hash(signingString: String, signingKey: String) = {
    val key = new SecretKeySpec(signingKey.getBytes("UTF-8"), "HmacSHA1")
    val mac = Mac.getInstance("HmacSHA1")
    mac.init(key)

    val rawHash = mac.doFinal(signingString.getBytes("UTF-8"))

    val hash = Base64.getEncoder.encodeToString(rawHash)
    log.debug(s"Hash: ${hash}")
    hash
  }
}