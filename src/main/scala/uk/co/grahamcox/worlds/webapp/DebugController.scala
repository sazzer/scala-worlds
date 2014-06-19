package uk.co.grahamcox.worlds.webapp

import java.time.format.DateTimeFormatter
import java.time.{ZonedDateTime, Clock}
import java.util.Properties

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.{ResponseBody, RequestMapping}

/**
 * Controller to get debug information
 * @param buildInfo The build information
 * @param clock The clock
 */
@Controller
@RequestMapping(Array("/api/debug"))
class DebugController(private val buildInfo: Properties,
                       private val clock: Clock) {
  /**
   * Get the debug information for the webapp
   * @return the build information
   */
  @RequestMapping(Array("/info"))
  @ResponseBody
  def info = {
    buildInfo
  }

  /**
   * Get the current time according to the server
   * @return the current time
   */
  @RequestMapping(Array("/now"))
  @ResponseBody
  def now = {
    ZonedDateTime.now(clock).format(DateTimeFormatter.ISO_DATE_TIME)
  }
}
