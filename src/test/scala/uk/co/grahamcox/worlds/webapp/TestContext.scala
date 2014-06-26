package uk.co.grahamcox.worlds.webapp

import org.springframework.context.annotation.{ImportResource, Configuration}

@Configuration
@ImportResource(Array(
  "classpath:/uk/co/grahamcox/worlds/test-context.xml",
  "classpath:/uk/co/grahamcox/worlds/webapp/core/config/context.xml",
  "classpath:/uk/co/grahamcox/worlds/webapp/webapp/config/context.xml"
))
class TestContext {

}
