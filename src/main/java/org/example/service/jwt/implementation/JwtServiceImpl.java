package org.example.service.jwt.implementation;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import javax.crypto.SecretKey;
import lombok.RequiredArgsConstructor;
import org.example.service.jwt.JwtService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JwtServiceImpl implements JwtService {

  private static final long EXPIRATION_TIME = Duration
      .ofMinutes(30)
      .toSeconds();
  @Value("${jwt.secret}")
  private String secretKey;

  private Key getSigningKey() {
    return Keys.hmacShaKeyFor(secretKey.getBytes());
  }

  @Override
  public String generateToken(String employeeId, String sessionId) {
    Instant now = Instant.now();

    return Jwts
        .builder()
        .subject(employeeId)
        .claim("sid", sessionId)
        .issuedAt(Date.from(now))
        .expiration(
            Date.from(now.plusSeconds(EXPIRATION_TIME))
        )
        .signWith(getSigningKey())
        .compact();
  }

  @Override
  public String extractUsernameFromToken(String token) {
    return extractClaims(token).getSubject();
  }

  @Override
  public String extractSessionIdFromToken(String token) {
    return extractClaims(token).get("sid", String.class);
  }

  private Claims extractClaims(String token) {
    return Jwts.parser()
        .verifyWith((SecretKey) getSigningKey())
        .build()
        .parseSignedClaims(token)
        .getPayload();
  }
}
