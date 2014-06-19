package uk.co.grahamcox.worlds.webapp

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping

/**
 * Controller for rendering the home page
 */
@Controller
@RequestMapping(Array("/"))
class HomeController {
  /**
   * Get the home page
   */
  @RequestMapping(Array("/"))
  def home = "home"
}
