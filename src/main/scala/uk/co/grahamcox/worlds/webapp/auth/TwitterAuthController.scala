package uk.co.grahamcox.worlds.webapp.auth

import java.util.UUID

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.{ResponseBody, RequestParam, RequestMapping}
import org.springframework.web.servlet.support.ServletUriComponentsBuilder
import uk.co.grahamcox.worlds.auth.twitter.TwitterAuthentication
/**
 * Controller to support authentication against the Twitter service
 * @param authenticator The mechanism by which we authenticate against Twitter
 */
@Controller
@RequestMapping(Array("/auth/twitter"))
class TwitterAuthController(authenticator: TwitterAuthentication) {

  /**
   * Attempt to authenticate the user
   */
  @RequestMapping(params = Array("!oauth_token", "!oauth_verifier"))
  def preAuth = {
    val sessionId = UUID.randomUUID().toString

    val callbackUrl = ServletUriComponentsBuilder.fromCurrentContextPath()
      .path("/auth/twitter")
      .queryParam("session", sessionId)
      .build()

    val redirectUrl = authenticator.start(sessionId, Option(callbackUrl.toUri))

    s"redirect:${redirectUrl}"
  }

  /**
   * Handle the response from twitter after authenticating the user
   * @param token the oauth token
   * @param verifier the oauth verifier value
   * @param sessionId the Session ID to use
   */
  @RequestMapping(params = Array("oauth_token", "oauth_verifier"))
  @ResponseBody
  def postAuth(@RequestParam("oauth_token") token: String,
               @RequestParam("oauth_verifier") verifier: String,
               @RequestParam("session") sessionId: String) = {

    authenticator.authenticate(sessionId, token, verifier).toArray
  }
}
