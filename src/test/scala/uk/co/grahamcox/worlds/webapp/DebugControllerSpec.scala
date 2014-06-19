package uk.co.grahamcox.worlds.webapp

import java.time.format.DateTimeFormatter

import org.scalatest.{FunSpec, Matchers}
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.{TestContextManager, ActiveProfiles, ContextConfiguration}
import org.springframework.test.context.web.WebAppConfiguration
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext

/**
 * Tests for the Debug Controller
 */
@WebAppConfiguration
@ContextConfiguration(classes = Array(classOf[TestContext]))
@ActiveProfiles(Array("test", "dev"))
class DebugControllerSpec extends FunSpec with Matchers {
  /** The Web App Context */
  @Autowired
  private[this] var webApplicationContext: WebApplicationContext = null

  // This is needed to bootstrap Spring
  new TestContextManager(this.getClass()).prepareTestInstance(this)

  describe("/api/debug") {
    val mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build()

    describe("/info") {
      val result = mockMvc.perform(MockMvcRequestBuilders.get("/api/debug/info"))

      it("Should return success") {
        result.andExpect(MockMvcResultMatchers.status().isOk)
      }
      it("Should return JSON") {
        result.andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith("application/json"))
      }
      it("Should return build.time") {
        result.andExpect(MockMvcResultMatchers.jsonPath("$.[build.time]").value("19 June 2014, 12:52:52 +01:00"))
      }
      it("Should return project.name") {
        result.andExpect(MockMvcResultMatchers.jsonPath("$.[project.name]").value("Multiworld Online"))
      }
      it("Should return project.version") {
        result.andExpect(MockMvcResultMatchers.jsonPath("$.[project.version]").value("1.0-SNAPSHOT"))
      }
      it("Should return git.revision") {
        result.andExpect(MockMvcResultMatchers.jsonPath("$.[git.revision]").value("4e5f3e24bd95b54c6f47a9b0e5fdcc70b4169fb2"))
      }
    }

    describe("/now") {
      val result = mockMvc.perform(MockMvcRequestBuilders.get("/api/debug/now"))

      it("Should return success") {
        result.andExpect(MockMvcResultMatchers.status().isOk)
      }
      it("Should return text") {
        result.andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith("text/plain"))
      }
      it("Should be a valid time") {
        val content = result.andReturn().getResponse.getContentAsString
        DateTimeFormatter.ISO_DATE_TIME.parse(content)
      }
    }
  }
}
