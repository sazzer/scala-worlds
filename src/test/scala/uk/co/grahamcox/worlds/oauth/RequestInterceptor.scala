package uk.co.grahamcox.worlds.oauth

import java.net.URI
import java.time.{ZoneId, Instant, Clock}

import org.scalatest.{FunSpec, ShouldMatchers}
import org.springframework.http.{HttpStatus, HttpRequest, HttpMethod}
import org.springframework.http.client.{ClientHttpResponse, ClientHttpRequestExecution}
import org.springframework.mock.http.client.{MockClientHttpResponse, MockClientHttpRequest}
import org.springframework.web.client.RestTemplate
import org.springframework.web.util.UriComponentsBuilder
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

      val request = new MockClientHttpRequest(HttpMethod.POST,
        UriComponentsBuilder.fromHttpUrl("https://api.twitter.com/1/statuses/update.json")
          .queryParam("include_entities", "true")
          .queryParam("status", "Hello%20Ladies%20%2b%20Gentlemen%2c%20a%20signed%20OAuth%20request%21")
          .build(true)
          .toUri())
      val response = new MockClientHttpResponse(new Array[Byte](0), HttpStatus.OK)

      it("Should work") {
        val result = requestInterceptor.intercept(request, null, new ClientHttpRequestExecution {
          override def execute(p1: HttpRequest, p2: Array[Byte]): ClientHttpResponse = {
            val authorization = p1.getHeaders.getFirst("Authorization")
            authorization should be(a[String])
            authorization should be(
              Seq("""OAuth oauth_consumer_key="xvz1evFS4wEEPTGEFPHBog"""",
                """oauth_nonce="kYjzVBB8Y0ZFabxSWbWovY3uYSQ2pTgmZeNu2VS4cg"""",
                """oauth_signature="tnnArxj06cWHq44gCs1OSKk%2FjLY%3D"""",
                """oauth_signature_method="HMAC-SHA1"""",
                """oauth_timestamp="1318622958"""",
                """oauth_token="370773112-GmHxMAgYyLbNEtIKZeRNFsMKPR9EyMZeS9weJAEb"""",
                """oauth_version="1.0"""").mkString(", "))

            response
          }
        })

        result should be(response)
      }
    }
  }
}