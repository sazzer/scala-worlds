package uk.co.grahamcox.worlds.oauth

import java.net.URI

/**
 * Details of the HTTP Request we are signing
 * @param method the HTTP Method
 * @param url the URL
 * @param params the Querystring and Payload parameters
 */
case class RequestDetails(val method: String,
                          val url: URI,
                          val params: Map[String, String])
