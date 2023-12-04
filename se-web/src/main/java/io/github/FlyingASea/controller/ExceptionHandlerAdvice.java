package io.github.FlyingASea.controller;

import com.fasterxml.jackson.core.JsonParseException;
import io.github.FlyingASea.result.ApiException;
import io.github.FlyingASea.result.Errors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MultipartException;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class ExceptionHandlerAdvice {

    @InitBinder
    public void initBinder(WebDataBinder binder) {
    }

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<Map<String, Object>> handleException(ApiException e) {
        HttpStatus status = HttpStatus.valueOf(e.getHttpCode());
        Map<String, Object> map = new HashMap<>();
        map.put("errorCode", e.getCode());
        map.put("errorMessage", e.getMessage());
        map.put("reasonPhrase", status.getReasonPhrase());
        return ResponseEntity.status(status).body(map);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleException(Exception e) {
        Errors errorType;
        String additionalMessage = null;
        if (e instanceof HttpMediaTypeNotSupportedException)
            errorType = Errors.UNSUPPORTED_MEDIA_TYPE;
        else if (e instanceof HttpRequestMethodNotSupportedException)
            errorType = Errors.UNSUPPORTED_METHOD;
        else if (e instanceof MultipartException)
            errorType = Errors.NOT_A_MULTIPART_REQUEST;
        else if (e instanceof HttpMessageNotReadableException notReadableException) {
            Throwable cause = notReadableException.getCause();
            errorType = Errors.INVALID_DATA_FORMAT;
            if (cause instanceof JsonParseException jsonParseException)
                additionalMessage = jsonParseException.getOriginalMessage();
        } else
            errorType = Errors.UNKNOWN_ERROR;
        if (additionalMessage == null)
            additionalMessage = errorType.getErrMsg();
        HttpStatus status = HttpStatus.valueOf(errorType.getHttpCode());
        Map<String, Object> map = new HashMap<>();
        map.put("errorCode", errorType.getErrCode());
        map.put("errorMessage", additionalMessage);
        map.put("reasonPhrase", status.getReasonPhrase());
        if (errorType == Errors.UNKNOWN_ERROR) {
            map.put("serverExceptionName", e.getClass().getSimpleName());
            log.error("An unknown error occurred.", e);
        }
        return ResponseEntity.status(status).body(map);
    }
}

