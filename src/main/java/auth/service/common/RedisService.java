package auth.service.common;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class RedisService {

    private final StringRedisTemplate redisTemplate;

    public void saveRefreshToken(
            String userType,
            String userCode,
            String token
    ) {

        redisTemplate.opsForValue()
                .set(
                        createRefreshTokenKey(userType, userCode),
                        token,
                        Duration.ofDays(7)
                );
    }

    public String getRefreshToken(
            String userType,
            String userCode
    ) {

        return redisTemplate.opsForValue()
                .get(
                        createRefreshTokenKey(userType, userCode)
                );
    }

    public void deleteRefreshToken(
            String userType,
            String userCode
    ) {

        redisTemplate.delete(
                createRefreshTokenKey(userType, userCode)
        );
    }

    private String createRefreshTokenKey(
            String userType,
            String userCode
    ) {

        return "auth:" + userType + ":" + userCode + ":refresh";
    }

}
