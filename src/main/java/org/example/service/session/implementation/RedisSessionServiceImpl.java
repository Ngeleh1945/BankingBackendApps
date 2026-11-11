package org.example.service.session.implementation;

import java.time.Duration;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.service.session.RedisSessionService;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

/**
 * Service implementation for managing employee authentication sessions.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class RedisSessionServiceImpl implements RedisSessionService {
  private final StringRedisTemplate redisTemplate;
  private static final String SESSION_PREFIX = "session:";
  private static final Duration SESSION_TTL = Duration.ofMinutes(30);

  private String key(String username) {
    if (username == null) {
      throw new IllegalArgumentException("Username cannot be blank");
    }

    return SESSION_PREFIX + username;
  }

  @Override
  public boolean createSession(String username, String token) {
    Boolean created = redisTemplate
        .opsForValue()
        .setIfAbsent(
            key(username),
            token,
            SESSION_TTL
        );

    return Boolean.TRUE.equals(created);
  }

  @Override
  public boolean validateAndRefreshSession(String username, String sessionId) {
    String sessionKey = key(username);

    String storedSessionId =
        redisTemplate.opsForValue().get(sessionKey);

    if (storedSessionId == null || !storedSessionId.equals(sessionId)) {
      return false;
    }
    redisTemplate.expire(sessionKey, SESSION_TTL);

    return true;
  }

  @Override
  public void deleteSession(String username) {
    redisTemplate.delete(SESSION_PREFIX + username);
  }
}
