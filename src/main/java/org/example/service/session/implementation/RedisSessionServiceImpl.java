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
  private static final String SESSION_PREFIX = "session";
  private static final Duration SESSION_TTL = Duration.ofMinutes(30);

  private String key(String username) {
    if (username == null) {
      log.error("username kosong");
      throw new IllegalArgumentException("Username tidak boleh kosong");
    }

    return SESSION_PREFIX + username;
  }

  @Override
  public boolean createSession(String username, String token) {
    String sessionKey = key(username);
    Boolean created = redisTemplate
        .opsForValue()
        .setIfAbsent(
            key(username),
            token,
            SESSION_TTL
        );

    log.info("Redis session key : {}", sessionKey);
    log.info("Redis setIfAbsent result: {}", created);

    log.info("Redis connection factory: {}",
        redisTemplate.getConnectionFactory()
            .getClass().getName());

    log.info("Redis database: {}",
        redisTemplate.getConnectionFactory()
            .getConnection()
            .serverCommands()
            .info("server"));

    if (Boolean.FALSE.equals(created)){
      String storedToken = redisTemplate.opsForValue().get(sessionKey);

      log.warn("Key sudah ada: {}", storedToken !=null);
    }
    return Boolean.TRUE.equals(created);
  }

  @Override
  public boolean isSessionValid(String username, String token) {
    String storedToken = redisTemplate.opsForValue().get(key(username));

    return storedToken != null && storedToken.equals(token);
  }

  @Override
  public boolean validateAndRefreshSession(String username, String token) {
    String key = key(username);

    String storedToken = redisTemplate.opsForValue().get(key);

    if (storedToken == null || !storedToken.equals(token)) {
      return false;
    }

    redisTemplate.expire(key, SESSION_TTL);

    return true;
  }

  @Override
  public void deleteSession(String username) {
    redisTemplate.delete(SESSION_PREFIX + username);
  }
}
