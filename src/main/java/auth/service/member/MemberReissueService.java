package auth.service.member;

import auth.dto.ReissueReqDto;
import auth.dto.ResponseDto;
import auth.dto.member.MemberLoginResDto;
import auth.enums.ResponseCode;
import auth.jwt.JwtProvider;
import auth.service.common.RedisService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MemberReissueService {

    private final String USER_TYPE = "member";

    private final JwtProvider jwtProvider;
    private final RedisService redisService;

    public ResponseDto memberReissue(ReissueReqDto reqDto) {

        String refreshToken = reqDto.getRefreshToken();

        if (!jwtProvider.validateToken(refreshToken)) {
            return new ResponseDto(ResponseCode.EXPIRED_REFRESH_TOKEN);
        }

        String memberId   = jwtProvider.getId(refreshToken);
        String memberCode = jwtProvider.getCode(refreshToken);

        String storedToken = redisService.getRefreshToken(USER_TYPE, memberCode);
        if (storedToken == null || !storedToken.equals(refreshToken)) {
            return new ResponseDto(ResponseCode.TOKEN_NOT_FOUND);
        }

        // 새 세션 토큰으로 access / refresh 모두 재발급
        String newSessionToken = UUID.randomUUID().toString();

        String newAccessToken = jwtProvider.generateAccessToken(
                memberId,
                memberCode,
                newSessionToken
        );

        String newRefreshToken = jwtProvider.generateRefreshToken(
                memberId,
                memberCode,
                newSessionToken
        );

        redisService.saveRefreshToken(USER_TYPE, memberCode, newRefreshToken);

        MemberLoginResDto resDto = MemberLoginResDto.builder()
                .memberCode(memberCode)
                .accessToken(newAccessToken)
                .refreshToken(newRefreshToken)
                .build();

        return new ResponseDto(ResponseCode.SUCCESS, resDto);
    }

}
