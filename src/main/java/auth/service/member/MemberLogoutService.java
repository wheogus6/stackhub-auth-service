package auth.service.member;

import auth.dto.ResponseDto;
import auth.enums.ResponseCode;
import auth.jwt.JwtProvider;
import auth.service.common.RedisService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberLogoutService {

    private final String USER_TYPE = "member";

    private final JwtProvider jwtProvider;
    private final RedisService redisService;

    public ResponseDto memberLogout(String accessToken) {

        if (!jwtProvider.validateToken(accessToken)) {
            return new ResponseDto(ResponseCode.INVALID_TOKEN);
        }

        String memberCode = jwtProvider.getCode(accessToken);

        String storedToken = redisService.getRefreshToken(USER_TYPE, memberCode);
        if (storedToken == null) {
            return new ResponseDto(ResponseCode.TOKEN_NOT_FOUND);
        }

        redisService.deleteRefreshToken(USER_TYPE, memberCode);

        return new ResponseDto(ResponseCode.SUCCESS);
    }

}
