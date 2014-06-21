package uk.co.grahamcox.worlds.webapp.oauth

import java.time.Clock

/**
 * Representation of an actual signature
 * @param value the value of the signature
 * @param method the signature method
 */
case class Signature(val value: String,
                     val method: String = "HMAC-SHA1")

/**
 * Class to acutally sign a request
 * @param conusmerKey the consumer key to use
 * @param nonceGenerator the nonce generator to use
 * @param clock the clock to use
 */
class Signer(conusmerKey: ConsumerKey,
             nonceGenerator: NonceGenerator,
             clock: Clock)