package auth.controller;

import auth.dto.ResponseDto;
import auth.dto.member.MemberJoinReqDto;
import auth.service.member.MemberJoinService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/join")
@RequiredArgsConstructor
public class JoinController {

    private final MemberJoinService memberJoinService;

    @PostMapping("/memberJoin")
    public ResponseDto memberJoin(@RequestBody MemberJoinReqDto reqDto) {
        return memberJoinService.memberJoin(reqDto);
    }

}
