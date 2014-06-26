package uk.co.grahamcox.worlds.oauth

import java.util.UUID

/**
 * Wrapper around the Nonce value to use
 */
case class Nonce(val value: String)

/**
 * Generator to generate a Nonce to use
 */
trait NonceGenerator {
  /**
   * Actually generate a Nonce
   * @return the nonce to use
   */
  def generate: Nonce
}

/**
 * Nonce generator that will generate a Nonce from a newly generated UUID
 */
class UuidNonceGenerator extends NonceGenerator {
  /**
   * {@inheritdoc}
   */
  override def generate = Nonce(UUID.randomUUID().toString)
}