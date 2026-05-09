package auth.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class RedisService {

    private final StringRedisTemplate redisTemplate;

    public void saveRefreshToken(String userCode, String token) {

        redisTemplate.opsForValue()
                .set(
                        "auth:user:" + userCode + ":refresh",
                        token,
                        Duration.ofDays(7)
                );
    }

    public String getRefreshToken(String userCode) {

        return redisTemplate.opsForValue()
                .get("auth:user:" + userCode + ":refresh");
    }

    public void deleteRefreshToken(String userCode) {

        redisTemplate.delete(
                "auth:user:" + userCode + ":refresh"
        );
    }

}
