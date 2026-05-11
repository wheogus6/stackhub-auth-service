package auth.service.member;

import auth.dto.ResponseDto;
import auth.dto.member.MemberJoinReqDto;
import auth.entity.Member;
import auth.enums.ResponseCode;
import auth.repository.MemberRepository;
import auth.service.common.CreateCodeService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MemberJoinService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final CreateCodeService createCodeService;
    @Transactional
    public ResponseDto memberJoin(MemberJoinReqDto reqDto) {

        // 아이디 중복 체크
        Member existing = memberRepository.findByMemberId(reqDto.getMemberId());
        if (existing != null) return new ResponseDto(ResponseCode.DUPLICATE_ID);

        Member member = Member.builder()
                .memberCode(createCodeService.createCodeByCodeId("MEMBER"))
                .memberId(reqDto.getMemberId())
                .memberName(reqDto.getMemberName())
                .password(passwordEncoder.encode(reqDto.getPassword()))
                .status("ACTIVE")
                .regDate(LocalDateTime.now())
                .build();

        memberRepository.save(member);

        return new ResponseDto(ResponseCode.SUCCESS);
    }

}
