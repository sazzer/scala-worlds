package uk.co.grahamcox.worlds.webapp.oauth

import java.time.{ZoneId, Instant, Clock}

import org.scalatest.{FunSpec, ShouldMatchers}
import org.springframework.web.client.RestTemplate
import collection.JavaConversions._

/**
 * Spec for the Request Interceptor
 */
class RequestInterceptorSpec extends FunSpec with ShouldMatchers {
  describe("RequestInterceptor") {
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

      val requestInterceptor = new RequestInterceptor(authorizor,
        Some(AccessToken("370773112-GmHxMAgYyLbNEtIKZeRNFsMKPR9EyMZeS9weJAEb",
          "LswwdoUaIvS8ltyTt5jkRh4J50vUPVVHtR2YPi5kE"))
        )

      val restTemplate = new RestTemplate()
      restTemplate.setInterceptors(Seq(requestInterceptor))

      restTemplate.postForEntity("https://api.twitter.com/1/statuses/update.json?include_entities=true&status=Hello Ladies + Gentlemen, a signed OAuth request!",
        null,
        classOf[String])

    }
  }
}