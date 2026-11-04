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
   * Checks whether a token matches the user's active session.
   *
   * @param username the username identifying the session
   * @param token the token to validate
   * @return true if the token matches an active session
   */
  boolean isSessionValid(String username, String token);

  boolean validateAndRefreshSession(String username, String token);

  /**
   * Deletes the active session for a user.
   *
   * @param username the username whose session will be deleted
   */
  void deleteSession(String username);
}