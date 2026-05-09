package auth.service.admin;

import auth.dto.ResponseDto;
import auth.dto.admin.AdminLoginReqDto;
import auth.enums.ResponseCode;
import auth.jwt.JwtProvider;
import auth.repository.AdminRepository;
import auth.service.RedisService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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


        return new ResponseDto(ResponseCode.SUCCESS);
    }

}
