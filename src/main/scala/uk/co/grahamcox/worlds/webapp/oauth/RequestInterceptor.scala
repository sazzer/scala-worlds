package uk.co.grahamcox.worlds.webapp.oauth

import java.net.URI

import collection.JavaConversions._
import org.eintr.loglady.Logging
import org.springframework.http.HttpRequest
import org.springframework.http.client.{ClientHttpResponse, ClientHttpRequestExecution, ClientHttpRequestInterceptor}
import org.springframework.web.util.{UriComponentsBuilder, UriComponents}

/**
 * Spring RestTemplate interceptor to do the OAuth magic
 * @param authorizor The authorizor to use
 * @param accessToken The optional access token to use
 * @param callback The optional Callback value to use. Only makes sense for certain requests
 */
class RequestInterceptor(authorizor: Authorizor,
                         accessToken: Option[AccessToken],
                         callback: Option[URI] = None) extends ClientHttpRequestInterceptor with Logging {
  /**
   * Authorize the given request
   * @param request the request to authorize
   * @param body the body of the request
   * @param execution the execution chain
   * @return the response
   */
  override def intercept(request: HttpRequest,
                         body: Array[Byte],
                         execution: ClientHttpRequestExecution): ClientHttpResponse = {

    val requestDetails = RequestDetails(
      method = request.getMethod.toString,
      url = UriComponentsBuilder.fromUri(request.getURI).replaceQuery("").build().toUri,
      params = UriComponentsBuilder.fromUri(request.getURI).build().getQueryParams.toSingleValueMap.toMap
    )

    log.debug(s"Authorizing request to ${requestDetails}")
    val authorization = authorizor.authorize(requestDetails, accessToken, callback)
    request.getHeaders.add("Authorization", authorization)

    execution.execute(request, body)
  }
}
