package auth.controller;

import auth.dto.ResponseDto;
import auth.service.admin.AdminLogoutService;
import auth.service.member.MemberLogoutService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/logout")
@RequiredArgsConstructor
public class LogoutController {

    private final MemberLogoutService memberLogoutService;
    private final AdminLogoutService adminLogoutService;

    @PostMapping("/memberLogout")
    public ResponseDto memberLogout(
            @RequestHeader("Authorization") String accessToken
    ) {
        return memberLogoutService.memberLogout(accessToken);
    }

    @PostMapping("/adminLogout")
    public ResponseDto adminLogout(
            @RequestHeader("Authorization") String accessToken
    ) {
        return adminLogoutService.adminLogout(accessToken);
    }

}
