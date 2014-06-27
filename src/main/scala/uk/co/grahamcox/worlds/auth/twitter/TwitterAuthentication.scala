package uk.co.grahamcox.worlds.auth.twitter

import java.net.URI

import org.springframework.web.client.RestTemplate
import org.springframework.web.util.UriComponentsBuilder
import uk.co.grahamcox.worlds.{SessionId, Sessions}
import uk.co.grahamcox.worlds.oauth.{AccessToken, RequestInterceptor, Authorizor}
import scala.collection.JavaConversions._

/**
 * Representation of an authenticated twitter user
 * @param username the username
 * @param userId the user ID
 * @param accessToken the access token
 */
case class AuthenticatedUser(username: String, userId: String, accessToken: AccessToken)

/**
 * Class to handle the details of authentication against Twitter
 * @param authorizor The Authorizor to use
 */
class TwitterAuthentication(sessions: Sessions,
                            authorizor: Authorizor,
                            requestTokenUrl: URI,
                            accessTokenUrl: URI,
                            authenticateUrl: URI) {

  /**
   * Start the authentication process
   * @param sessionId The Session ID for the session
   * @return a URL to redirect the user to
   */
  def start(sessionId: SessionId,
            callback: Option[URI]) = {
    val restTemplate: RestTemplate = new RestTemplate()
    restTemplate.setInterceptors(Seq(new RequestInterceptor(authorizor = authorizor,
      accessToken = None,
      callback = callback)))

    val requestToken = restTemplate.postForEntity(requestTokenUrl, null, classOf[String])

    val requestTokenValues = Map(requestToken.getBody.split("&")
      .map(p => p.split("=", 2))
      .map(p => (p(0), p(1))) : _*)

    sessions(sessionId)("requestToken") = AccessToken(
        key = requestTokenValues("oauth_token"),
        secret = requestTokenValues("oauth_token_secret"))

    UriComponentsBuilder.fromUri(authenticateUrl)
      .queryParam("oauth_token", requestTokenValues("oauth_token"))
      .build()    
  }

  /**
   * Handle the authentication response from Twitter
   * @param sessionId the Session ID for the session
   * @param token the OAuth Token that is being authenticated
   * @param verifier the OAuth verification value
   */
  def authenticate(sessionId: SessionId, token: String, verifier: String) = {
    val restTemplate: RestTemplate = new RestTemplate()
    restTemplate.setInterceptors(Seq(new RequestInterceptor(authorizor = authorizor,
      accessToken = sessions(sessionId).remove[AccessToken]("requestToken"))))

    val accessTokenUrl = UriComponentsBuilder.fromUri(this.accessTokenUrl)
      .queryParam("oauth_verifier", verifier)
      .build()
    val accessToken = restTemplate.postForEntity(accessTokenUrl.toUri, null, classOf[String])

    val accessTokenValues = Map(accessToken.getBody.split("&")
      .map(p => p.split("=", 2))
      .map(p => (p(0), p(1))) : _*)

    AuthenticatedUser(username = accessTokenValues("screen_name"),
      userId = accessTokenValues("user_id"),
      accessToken = AccessToken(key = accessTokenValues("oauth_token"),
        secret = accessTokenValues("oauth_token_secret")
      )
    )
  }
}
