package uk.co.grahamcox.worlds.webapp.oauth

import java.net.URI
import java.time.Instant

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
 * @param conusmerKey the consumer key to use
 */
class Signer(conusmerKey: ConsumerKey) {
  def sign(request: OAuthRequestDetails): Signature = {
    Signature("tnnArxj06cWHq44gCs1OSKk/jLY=")
  }
}