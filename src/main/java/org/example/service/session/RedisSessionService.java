package org.example.service.session;

/**
 * Service interface for managing employee authentication sessions.
 */
public interface RedisSessionService {

  /**
   * Saves or replaces the active session token for a user.
   *
   * @param username the username identifying the session
   * @param token the authentication token to store
   */
  boolean createSession(String username, String token);

  /**
   *
   * @param username
   * @param sessionId
   * @return
   */
  boolean validateAndRefreshSession(String username, String sessionId);

  /**
   * Deletes the active session for a user.
   *
   * @param username the username whose session will be deleted
   */
  void deleteSession(String username);
}