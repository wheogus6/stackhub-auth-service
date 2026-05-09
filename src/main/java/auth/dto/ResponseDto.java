package auth.dto;

import auth.enums.ResponseCode;
import lombok.*;
import org.springframework.http.ResponseEntity;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ResponseDto {

    private String code;
    private String message;
    private Object data;
    private String accessDomain;


    public ResponseDto(ResponseCode responseCode) {
        this.code = responseCode.getCode();
        this.message = responseCode.getMessage();
    }

    public ResponseDto(ResponseCode responseCode, Object data) {
        this.code = responseCode.getCode();
        this.message = responseCode.getMessage();
        this.data = data;
    }

    public void setResponseCode(ResponseCode responseCode) {
        this.code = responseCode.getCode();
        this.message = responseCode.getMessage();
    }

    public static ResponseEntity<ResponseDto> dataResponse(ResponseDto responseDto) {
        return ResponseEntity.ok(responseDto);
    }

    public static ResponseEntity<ResponseDto> codeResponse(ResponseCode code) {
        return ResponseEntity.ok(
                ResponseDto
                        .builder()
                        .code(code.getCode())
                        .message(code.getMessage())
                        .build());
    }

}
