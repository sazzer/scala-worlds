package uk.co.grahamcox.worlds.webapp.oauth

/**
 * Some utilities that make life easier
 */
object Utils {

  /**
   * Percent encode the string
   * @param str The string to encide
   * @return the percent encoded string
   */
  def percentEncode(str: String) = {
    val dontEncode = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz-._~"

    str.map {
      c => c match {
        case c if dontEncode.contains(c) => c
        case c => "%" + c.toInt.toHexString.toUpperCase
      }
    } mkString ""
  }

}
