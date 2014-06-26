package uk.co.grahamcox.worlds.auth.twitter

import java.net.URI

import org.springframework.web.client.RestTemplate
import org.springframework.web.util.UriComponentsBuilder
import uk.co.grahamcox.worlds.oauth.{RequestInterceptor, Authorizor}
import scala.collection.JavaConversions._

/**
 * Class to handle the details of authentication against Twitter
 * @param authorizor The Authorizor to use
 */
class TwitterAuthentication(authorizor: Authorizor,
                           requestTokenUrl: URI, 
                           authenticateUrl: URI) {
  /** The rest template to use for the Twitter API requests */
  private val restTemplate: RestTemplate = new RestTemplate()

  restTemplate.setInterceptors(Seq(new RequestInterceptor(authorizor = authorizor, accessToken = None)))

  /**
   * Start the authentication process
   * @return a URL to redirect the user to
   */
  def start = {
    val requestToken = restTemplate.postForEntity(requestTokenUrl, null, classOf[String])

    val requestTokenValues = Map(requestToken.getBody.split("&")
      .map(p => p.split("=", 2))
      .map(p => (p(0), p(1))) : _*)

    UriComponentsBuilder.fromUri(authenticateUrl)
      .queryParam("oauth_token", requestTokenValues("oauth_token"))
      .build()    
  }
}
