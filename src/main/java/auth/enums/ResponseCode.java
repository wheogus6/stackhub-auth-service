package auth.enums;

import lombok.Getter;

@Getter
public enum ResponseCode {

    NOT_EXIST_ID("M001", "존재하지 않는 아이디 입니다."),
    PASSWORD_NOT_MATCH("M002", "비밀번호가 일치하지 않습니다."),
    DUPLICATE_ID("M003", "이미 사용 중인 아이디 입니다."),
    INVALID_TOKEN("T001", "유효하지 않은 토큰입니다."),
    EXPIRED_REFRESH_TOKEN("T002", "만료된 리프레시 토큰입니다."),
    TOKEN_NOT_FOUND("T003", "토큰을 찾을 수 없습니다."),
    SUCCESS("00", "성공");

    private final String code;
    private final String message;

    ResponseCode(String code, String message) {
        this.code = code;
        this.message = message;
    }
}
