package auth.dto.admin;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdminLoginReqDto {

    private String memberId;
    private String password;

}
