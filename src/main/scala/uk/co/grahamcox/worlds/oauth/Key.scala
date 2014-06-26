package uk.co.grahamcox.worlds.oauth

/**
 * Representation of the Consumer Key and Secret
 * @param key the key
 * @param secret the secret
 */
case class ConsumerKey(val key: String, val secret: String)

/**
 * Representation of a Request Token Key and Secret
 * @param key the key
 * @param secret the secret
 */
case class RequestToken(val key: String, val secret: String)

/**
 * Representation of an Access Token Key and Secret
 * @param key the key
 * @param secret the secret
 */
case class AccessToken(val key: String, val secret: String)

