package uk.co.grahamcox.worlds.oauth

import java.net.URI
import java.time.Instant

import org.scalatest.{FunSpec, ShouldMatchers}

/**
 * Tests for the Signer
 */
class SignerSpec extends FunSpec with ShouldMatchers {
  describe("Signer") {
    describe("When using the example from https://dev.twitter.com/docs/auth/creating-signature") {
      val signer = new Signer(ConsumerKey("xvz1evFS4wEEPTGEFPHBog", "kAcSOqF21Fu85e7zjz7ZN2U4ZRhfV3WpwPAoE3Z7kBw"))
      val request = OAuthRequestDetails(
        request = RequestDetails(
          method = "POST",
          url = new URI("https://api.twitter.com/1/statuses/update.json"),
          params = Map(
            "include_entities" -> "true",
            "status" -> "Hello Ladies + Gentlemen, a signed OAuth request!"
          )
        ),
        nonce = Nonce("kYjzVBB8Y0ZFabxSWbWovY3uYSQ2pTgmZeNu2VS4cg"),
        timestamp = Instant.ofEpochSecond(1318622958),
        accessToken = Some(AccessToken("370773112-GmHxMAgYyLbNEtIKZeRNFsMKPR9EyMZeS9weJAEb",
          "LswwdoUaIvS8ltyTt5jkRh4J50vUPVVHtR2YPi5kE"))
      )

      val signature = signer.sign(request)

      it("Should be a valid Signature") {
        signature should be(a[Signature])
      }
      it("Should have a method of HMAC-SHA1") {
        signature.method should be("HMAC-SHA1")
      }
      it("Should have the correct value") {
        signature.value should be("tnnArxj06cWHq44gCs1OSKk/jLY=")
      }
    }
  }
}
