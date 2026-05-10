package auth.service.member;


import auth.dto.ResponseDto;
import auth.dto.member.MemberLoginReqDto;
import auth.dto.member.MemberLoginResDto;
import auth.entity.Member;
import auth.enums.ResponseCode;
import auth.jwt.JwtProvider;
import auth.repository.MemberRepository;
import auth.service.RedisService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MemberLoginService {

    private final String USER_TYPE = "member";

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;
    private final RedisService redisService;

    public ResponseDto memberLogin(MemberLoginReqDto reqDto) {

        Member member = memberRepository.findByMemberId(reqDto.getMemberId());

        if (member == null) return new ResponseDto(ResponseCode.NOT_EXIST_ID);

        if (!passwordEncoder.matches(reqDto.getPassword(), member.getPassword())) return new ResponseDto(ResponseCode.PASSWORD_NOT_MATCH);

        MemberLoginResDto resDto = createLoginResponse(member);

        return new ResponseDto(ResponseCode.SUCCESS, resDto);
    }


    private MemberLoginResDto createLoginResponse(Member member) {
        String sessionToken = UUID.randomUUID().toString();

        String accessToken = jwtProvider.generateAccessToken(
                member.getMemberId(),
                member.getMemberCode(),
                sessionToken
        );

        String refreshToken = jwtProvider.generateRefreshToken(
                member.getMemberId(),
                member.getMemberCode(),
                sessionToken
        );

        // 레디스에 refreshToken 저장
        redisService.saveRefreshToken(
                USER_TYPE,
                member.getMemberCode(),
                refreshToken
        );

//        member.setPassword(
//                passwordEncoder.encode(reqDto.getPassword())
//        );

        return MemberLoginResDto.builder()
                .memberCode(member.getMemberCode())
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }


}
