package uk.co.grahamcox.worlds.oauth

import java.net.URI
import java.time.{ZoneId, Instant, Clock}

import org.scalatest.{FunSpec, ShouldMatchers}

/**
 * Spec for the Authorizor
 */
class AuthorizorSpec extends FunSpec with ShouldMatchers {
  describe("Authorizor") {
    describe("When using the example from https://dev.twitter.com/docs/auth/creating-signature") {
      val consumerKey = ConsumerKey("xvz1evFS4wEEPTGEFPHBog", "kAcSOqF21Fu85e7zjz7ZN2U4ZRhfV3WpwPAoE3Z7kBw")
      val signer = new Signer(consumerKey)
      val clock = Clock.fixed(Instant.ofEpochSecond(1318622958), ZoneId.of("UTC"))
      val nonceGenerator = new NonceGenerator {
        override def generate: Nonce = Nonce("kYjzVBB8Y0ZFabxSWbWovY3uYSQ2pTgmZeNu2VS4cg")
      }
      val authorizor = new Authorizor(consumerKey,
        signer,
        nonceGenerator,
        clock)

      val request = RequestDetails(
        method = "POST",
        url = new URI("https://api.twitter.com/1/statuses/update.json"),
        params = Map(
          "include_entities" -> "true",
          "status" -> "Hello Ladies + Gentlemen, a signed OAuth request!"
        )
      )

      val authorization = authorizor.authorize(request,
        Some(AccessToken("370773112-GmHxMAgYyLbNEtIKZeRNFsMKPR9EyMZeS9weJAEb",
          "LswwdoUaIvS8ltyTt5jkRh4J50vUPVVHtR2YPi5kE"))
        )
      it("should be a String") {
        authorization should be(a[String])
      }
      it("should be correct") {
        authorization should be(
          Seq("""OAuth oauth_consumer_key="xvz1evFS4wEEPTGEFPHBog"""",
              """oauth_nonce="kYjzVBB8Y0ZFabxSWbWovY3uYSQ2pTgmZeNu2VS4cg"""",
              """oauth_signature="tnnArxj06cWHq44gCs1OSKk%2FjLY%3D"""",
              """oauth_signature_method="HMAC-SHA1"""",
              """oauth_timestamp="1318622958"""",
              """oauth_token="370773112-GmHxMAgYyLbNEtIKZeRNFsMKPR9EyMZeS9weJAEb"""",
              """oauth_version="1.0"""").mkString(", "))
      }
    }
  }
}
