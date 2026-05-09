package auth.dto.admin;


import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminLoginResDto {

    private String adminCode;
    private String accessToken;
    private String refreshToken;

}
