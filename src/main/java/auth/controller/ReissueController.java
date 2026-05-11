package auth.controller;

import auth.dto.ReissueReqDto;
import auth.dto.ResponseDto;
import auth.service.admin.AdminReissueService;
import auth.service.member.MemberReissueService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/reissue")
@RequiredArgsConstructor
public class ReissueController {

    private final MemberReissueService memberReissueService;
    private final AdminReissueService adminReissueService;

    @PostMapping("/memberReissue")
    public ResponseDto memberReissue(@RequestBody ReissueReqDto reqDto) {
        return memberReissueService.memberReissue(reqDto);
    }

    @PostMapping("/adminReissue")
    public ResponseDto adminReissue(@RequestBody ReissueReqDto reqDto) {
        return adminReissueService.adminReissue(reqDto);
    }

}
