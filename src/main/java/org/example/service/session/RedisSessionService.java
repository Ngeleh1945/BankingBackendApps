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
   * Validates an active user session against the stored session ID in Redis
   * and refreshes its expiration time (TTL) if valid.
   *
   * @param username  the username associated with the session
   * @param sessionId the unique session identifier to validate
   * @return {@code true} if the session is valid and successfully refreshed;
   *         {@code false} otherwise
   */
  boolean validateAndRefreshSession(String username, String sessionId);

  /**
   * Checks whether the specified user currently has an active session stored in Redis.
   *
   * @param username the username to check for an active session
   * @return {@code true} if an active session exists for the user;
   *         {@code false} otherwise
   */
  boolean hasActiveSession(String username);

  /**
   * Deletes the active session for a user.
   *
   * @param username the username whose session will be deleted
   */
  void deleteSession(String username);
}