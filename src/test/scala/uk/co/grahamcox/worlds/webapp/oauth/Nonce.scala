package uk.co.grahamcox.worlds.webapp.oauth

import org.scalatest.{ShouldMatchers, FunSpec}

/**
 * Tests for the UUID Nonce Generator
 */
class UuidNonceGeneratorSpec extends FunSpec with ShouldMatchers {
  describe("UuidNonceGenerator") {
    val generator = new UuidNonceGenerator
    describe("When generating a Nonce") {
      val nonce = generator.generate

      it("Should be a valid Nonce") {
        nonce should be(a[Nonce])
      }

      it("Should have a valid UUID as the value") {
        val value = nonce.value
        value should fullyMatch regex("""[a-fA-F0-9]{8}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{12}""")
      }
    }
  }
}
