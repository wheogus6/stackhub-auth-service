package auth.service.admin;

import auth.dto.ReissueReqDto;
import auth.dto.ResponseDto;
import auth.dto.admin.AdminLoginResDto;
import auth.enums.ResponseCode;
import auth.jwt.JwtProvider;
import auth.service.common.RedisService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AdminReissueService {

    private final String USER_TYPE = "admin";

    private final JwtProvider jwtProvider;
    private final RedisService redisService;

    public ResponseDto adminReissue(ReissueReqDto reqDto) {

        String refreshToken = reqDto.getRefreshToken();

        if (!jwtProvider.validateToken(refreshToken)) {
            return new ResponseDto(ResponseCode.EXPIRED_REFRESH_TOKEN);
        }

        String adminId   = jwtProvider.getId(refreshToken);
        String adminCode = jwtProvider.getCode(refreshToken);

        String storedToken = redisService.getRefreshToken(USER_TYPE, adminCode);
        if (storedToken == null || !storedToken.equals(refreshToken)) {
            return new ResponseDto(ResponseCode.TOKEN_NOT_FOUND);
        }

        // 새 세션 토큰으로 access / refresh 모두 재발급
        String newSessionToken = UUID.randomUUID().toString();

        String newAccessToken = jwtProvider.generateAccessToken(
                adminId,
                adminCode,
                newSessionToken
        );

        String newRefreshToken = jwtProvider.generateRefreshToken(
                adminId,
                adminCode,
                newSessionToken
        );

        redisService.saveRefreshToken(USER_TYPE, adminCode, newRefreshToken);

        AdminLoginResDto resDto = AdminLoginResDto.builder()
                .adminCode(adminCode)
                .accessToken(newAccessToken)
                .refreshToken(newRefreshToken)
                .build();

        return new ResponseDto(ResponseCode.SUCCESS, resDto);
    }

}
