package uk.co.grahamcox.worlds.webapp

import java.util.Properties

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.{ResponseBody, RequestMapping}

/**
 * Controller to get debug information
 */
@Controller
@RequestMapping(Array("/api/debug"))
class DebugController(private val buildInfo: Properties) {
  /**
   * Get the debug information for the webapp
   */
  @RequestMapping(Array("/info"))
  @ResponseBody
  def info = {
    buildInfo
  }
}
