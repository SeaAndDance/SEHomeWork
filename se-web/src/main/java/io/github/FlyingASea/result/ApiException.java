package io.github.FlyingASea.result;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ApiException extends RuntimeException {

    private final int code;
    private final int httpCode;
    private final String message;

    public ApiException(Errors resultEnum) {
        this(resultEnum.getErrCode(), resultEnum.getHttpCode(), resultEnum.getErrMsg());
    }

    public ApiException(Errors resultEnum, String message) {
        this(resultEnum.getErrCode(), resultEnum.getHttpCode(), message);
    }
}