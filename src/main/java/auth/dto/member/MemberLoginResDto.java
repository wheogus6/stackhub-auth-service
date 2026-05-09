package auth.dto.member;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberLoginResDto {

    private String memberCode;
    private String token;

}
