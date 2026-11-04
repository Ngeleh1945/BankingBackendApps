package org.example.service.jwt.implementation;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.time.Duration;
import java.time.Instant;
import javax.crypto.SecretKey;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.service.jwt.JwtService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class JwtServiceImpl implements JwtService {

  private static final long EXPIRATION_TIME = Duration.ofMinutes(30).toSeconds();
  @Value("${jwt.secret}")
  private String secretKey;
  private Key getSigningKey() {
    return Keys.hmacShaKeyFor(secretKey.getBytes());
  }

  @Override
  public String generateToken(String employeeId) {
    Instant now = Instant.now();

    long issuedAt = now.getEpochSecond();
    long expiration = now.plusSeconds(EXPIRATION_TIME).getEpochSecond();

    return Jwts
        .builder()
        .subject(employeeId)
        .claim("iat", issuedAt)
        .claim("exp", expiration)
        .signWith(getSigningKey())
        .compact();
  }

  @Override
  public String extractUsernameFromToken(String token) {
    Claims claims = Jwts
        .parser()
        .verifyWith((SecretKey) getSigningKey())
        .build()
        .parseSignedClaims(token)
        .getPayload();

    return claims.getSubject();
  }

}
