package auth.service.admin;

import auth.dto.ResponseDto;
import auth.dto.admin.AdminLoginReqDto;
import auth.dto.admin.AdminLoginResDto;
import auth.entity.Admin;
import auth.enums.ResponseCode;
import auth.jwt.JwtProvider;
import auth.repository.AdminRepository;
import auth.service.RedisService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class AdminLoginService {

    private final String USER_TYPE = "admin";

    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;
    private final RedisService redisService;

    public ResponseDto adminLogin(AdminLoginReqDto reqDto) {

        Admin admin = adminRepository.findByAdminId(reqDto.getAdminId());

        if (admin == null) return new ResponseDto(ResponseCode.NOT_EXIST_ID);

        if (!passwordEncoder.matches(reqDto.getPassword(), admin.getPassword())) return new ResponseDto(ResponseCode.PASSWORD_NOT_MATCH);

        AdminLoginResDto resDto = createLoginResponse(admin);

        return new ResponseDto(ResponseCode.SUCCESS, resDto);
    }


    private AdminLoginResDto createLoginResponse(Admin admin) {
        String sessionToken = UUID.randomUUID().toString();

        String accessToken = jwtProvider.generateAccessToken(
                admin.getAdminId(),
                admin.getAdminCode(),
                sessionToken
        );

        String refreshToken = jwtProvider.generateRefreshToken(
                admin.getAdminId(),
                admin.getAdminCode(),
                sessionToken
        );

        // 레디스에 refreshToken 저장
        redisService.saveRefreshToken(
                USER_TYPE,
                admin.getAdminCode(),
                refreshToken
        );

        return AdminLoginResDto.builder()
                .adminCode(admin.getAdminCode())
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }
}
