package auth.dto.member;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberJoinReqDto {

    private String memberId;
    private String memberName;
    private String password;

}
