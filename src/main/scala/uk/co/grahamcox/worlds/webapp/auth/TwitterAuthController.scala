package uk.co.grahamcox.worlds.webapp.auth

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.{ResponseBody, RequestMapping}
import org.springframework.web.client.RestTemplate
import org.springframework.web.util.UriComponentsBuilder
import uk.co.grahamcox.worlds.webapp.oauth.{RequestInterceptor, Authorizor}
import scala.collection.JavaConversions._
/**
 * Controller to support authentication against the Twitter service
 * @param authorizor The Authorizor to use
 */
@Controller
@RequestMapping(Array("/auth/twitter"))
class TwitterAuthController(authorizor: Authorizor) {
  /** The rest template to use for the Twitter API requests */
  private val restTemplate: RestTemplate = new RestTemplate()

  restTemplate.setInterceptors(Seq(new RequestInterceptor(authorizor = authorizor, accessToken = None)))

  /**
   * Attempt to authenticate the user
   */
  @RequestMapping
  def auth = {
    val requestToken = restTemplate.postForEntity("https://api.twitter.com/oauth/request_token", null, classOf[String])

    val requestTokenValues = Map(requestToken.getBody.split("&")
      .map(p => p.split("=", 2))
      .map(p => (p(0), p(1))) : _*)

    val redirectUrl = UriComponentsBuilder.fromHttpUrl("https://api.twitter.com/oauth/authenticate")
      .queryParam("oauth_token", requestTokenValues("oauth_token"))
      .build()

    s"redirect:${redirectUrl}"
  }
}
