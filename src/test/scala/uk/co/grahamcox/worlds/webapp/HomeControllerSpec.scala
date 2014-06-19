package uk.co.grahamcox.worlds.webapp

import org.scalatest.{FunSpec, Matchers}
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.web.WebAppConfiguration
import org.springframework.test.context.{ActiveProfiles, ContextConfiguration, TestContextManager}
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext

/**
 * Tests for the Home Controller
 */
@WebAppConfiguration
@ContextConfiguration(classes = Array(classOf[TestContext]))
@ActiveProfiles(Array("test", "dev"))
class HomeControllerSpec extends FunSpec with Matchers {
  /** The Web App Context */
  @Autowired
  private[this] var webApplicationContext: WebApplicationContext = null

  // This is needed to bootstrap Spring
  new TestContextManager(this.getClass()).prepareTestInstance(this)

  describe("GET /") {
    val mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build()
    val result = mockMvc.perform(MockMvcRequestBuilders.get("/"))
    it("Should return success") {
      result.andExpect(MockMvcResultMatchers.status().isOk)
    }
  }
}
