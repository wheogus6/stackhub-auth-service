package auth.controller;

import auth.dto.ResponseDto;
import auth.dto.admin.AdminLoginReqDto;
import auth.dto.member.MemberLoginReqDto;
import auth.service.admin.AdminLoginService;
import auth.service.member.MemberLoginService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth/login")
@RequiredArgsConstructor
public class LoginController {

    private final MemberLoginService memberLoginService;
    private final AdminLoginService adminLoginService;

    @GetMapping("/memberLogin")
    public ResponseDto memberLogin(@RequestBody MemberLoginReqDto reqDto) {
        return memberLoginService.memberLogin(reqDto);
    }

    @GetMapping("/adminLogin")
    public ResponseDto adminLogin(@RequestBody AdminLoginReqDto reqDto) {
        return adminLoginService.adminLogin(reqDto);
    }


}
