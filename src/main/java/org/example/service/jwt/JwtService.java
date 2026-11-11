package org.example.service.jwt;

public interface JwtService {
  /**
   * Generates a JWT token for the authenticated employee.
   *
   * @param username the username for which the token is generated
   * @return the generated JWT token string
   */
  String generateToken(String username, String sessionId);

  /**
   * Extracts the username from a given JWT token.
   *
   * @param token the JWT token string
   * @return the username extracted from the token claims
   */
  String extractUsernameFromToken(String token);
  String extractSessionIdFromToken(String token);
}
