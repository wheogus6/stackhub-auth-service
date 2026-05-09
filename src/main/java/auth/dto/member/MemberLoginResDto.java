package auth.dto.member;


import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberLoginResDto {

    private String memberCode;
    private String accessToken;
    private String refreshToken;

}
