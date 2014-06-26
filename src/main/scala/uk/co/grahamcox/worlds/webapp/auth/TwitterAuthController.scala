package uk.co.grahamcox.worlds.webapp.auth

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping
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
  @RequestMapping
  def auth = {
    val redirectUrl = authenticator.start

    s"redirect:${redirectUrl}"
  }
}
