package auth.service.admin;

import auth.dto.ResponseDto;
import auth.enums.ResponseCode;
import auth.jwt.JwtProvider;
import auth.service.common.RedisService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminLogoutService {

    private final String USER_TYPE = "admin";

    private final JwtProvider jwtProvider;
    private final RedisService redisService;

    public ResponseDto adminLogout(String accessToken) {

        if (!jwtProvider.validateToken(accessToken)) {
            return new ResponseDto(ResponseCode.INVALID_TOKEN);
        }

        String adminCode = jwtProvider.getCode(accessToken);

        String storedToken = redisService.getRefreshToken(USER_TYPE, adminCode);
        if (storedToken == null) {
            return new ResponseDto(ResponseCode.TOKEN_NOT_FOUND);
        }

        redisService.deleteRefreshToken(USER_TYPE, adminCode);

        return new ResponseDto(ResponseCode.SUCCESS);
    }

}
