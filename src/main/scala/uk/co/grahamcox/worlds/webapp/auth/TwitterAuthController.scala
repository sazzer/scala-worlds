package uk.co.grahamcox.worlds.webapp.auth

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
    val callbackUrl = ServletUriComponentsBuilder.fromCurrentContextPath().path("/auth/twitter").build()
    val redirectUrl = authenticator.start(Option(callbackUrl.toUri))

    s"redirect:${redirectUrl}"
  }

  /**
   * Handle the response from twitter after authenticating the user
   * @param token the oauth token
   * @param verifier the oauth verifier value
   */
  @RequestMapping(params = Array("oauth_token", "oauth_verifier"))
  @ResponseBody
  def postAuth(@RequestParam("oauth_token") token: String,
               @RequestParam("oauth_verifier") verifier: String) = {
    Array(token, verifier)
  }
}
