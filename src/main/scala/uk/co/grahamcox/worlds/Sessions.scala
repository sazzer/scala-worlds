package uk.co.grahamcox.worlds

/**
 * A Session ID
 * @param id the Session ID
 */
case class SessionId(id: String)

class Session {
  /** The actual data in this session */
  private val sessionData = scala.collection.mutable.Map[String, Any]()
  /**
   * Get the given value from the session
   * @param name the name of the session
   * @tparam T the type of value to get
   * @return the value
   */
  def apply[T](name: String): Option[T] = {
    sessionData.get(name).map(_.asInstanceOf[T])
  }

  /**
   * Set the given value into the session
   * @param name the name of the value
   * @param value the value itself
   */
  def update(name: String, value: Any) {
    sessionData(name) = value
  }

  /**
   * Remove the given value from the session
   * @param name the name of the session
   */
  def remove(name: String) {
    sessionData.remove(name)
  }
}

/**
 * Mechanism to store session details
 */
class Sessions {
  /** The actual sessions */
  private val sessions = scala.collection.mutable.Map[SessionId, Session]()

  /**
   * Get the session with the given ID
   * @param id the ID of the session
   * @return the session
   */
  def apply(id: SessionId): Session = sessions.getOrElseUpdate(id, new Session)
}
