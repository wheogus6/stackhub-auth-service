package auth.enums;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ResponseCode {

    NOT_FOUND_MEMBER("M001", "존재하지 않는 아이디 입니다."),
    PASSWORD_NOT_MATCH("M002", "비밀번호가 일치하지 않습니다."),
    SUCCESS("00", "성공");

    private final String code;
    private final String message;

    ResponseCode(String code, String message) {
        this.code = code;
        this.message = message;
    }
}
