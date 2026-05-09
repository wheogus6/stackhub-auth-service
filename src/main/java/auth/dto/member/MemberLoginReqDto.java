package auth.dto.member;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberLoginReqDto {

    private String memberId;
    private String password;

}
