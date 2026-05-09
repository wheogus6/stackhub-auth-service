package auth.service.member;


import auth.dto.ResponseDto;
import auth.dto.member.MemberLoginReqDto;
import auth.enums.ResponseCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberLoginService {

    public ResponseDto memberLogin(MemberLoginReqDto reqDto) {
        return new ResponseDto(ResponseCode.SUCCESS);
    }

}
