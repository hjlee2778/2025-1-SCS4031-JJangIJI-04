package com.jjangiji.hankkimoa.common.handler;

import com.jjangiji.hankkimoa.common.exception.ExceptionCode;
import com.jjangiji.hankkimoa.common.exception.response.ExceptionResponse;
import com.jjangiji.hankkimoa.common.exception.HankkiMoaException;
import com.jjangiji.hankkimoa.common.exception.OauthException;
import com.jjangiji.hankkimoa.common.exception.response.OauthExceptionResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(HankkiMoaException.class)
    public ResponseEntity<ExceptionResponse> handleHankkiMoaException(HankkiMoaException exception,
                                                                      HttpServletRequest request) {
        ExceptionResponse response = new ExceptionResponse(
                request.getMethod(),
                request.getRequestURI(),
                exception.getExceptionCode(),
                exception.getMessage());

        return ResponseEntity.status(exception.getHttpStatusCode())
                .body(response);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ExceptionResponse> handleRuntimeException(HttpServletRequest request, RuntimeException exception) {
        log.error("INTERNAL SERVER ERROR : ", exception);
        ExceptionResponse response = new ExceptionResponse(
                request.getMethod(),
                request.getRequestURI(),
                ExceptionCode.INTERNAL_SERVER_ERROR.name(),
                ExceptionCode.INTERNAL_SERVER_ERROR.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(response);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionResponse> handleMethodArgumentNotValidException(
            MethodArgumentNotValidException exception,
            HttpServletRequest request) {
        FieldError fieldError = exception.getFieldError();
        String exceptionMessage = ExceptionCode.INVALID_PARAMETER.getMessage();
        if (fieldError != null) {
            exceptionMessage = fieldError.getDefaultMessage();
        }

        ExceptionResponse response = new ExceptionResponse(
                request.getMethod(),
                request.getRequestURI(),
                ExceptionCode.INVALID_PARAMETER.name(),
                exceptionMessage);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(response);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ExceptionResponse> handleConstraintViolation(
            ConstraintViolationException exception,
            HttpServletRequest request) {
        String exceptionMessage = exception.getConstraintViolations()
                .stream()
                .findFirst()
                .get().getMessage();

        ExceptionResponse response = new ExceptionResponse(
                request.getMethod(),
                request.getRequestURI(),
                ExceptionCode.INVALID_PARAMETER.name(),
                exceptionMessage
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(response);
    }

    @ExceptionHandler(OauthException.class)
    public ResponseEntity<OauthExceptionResponse> handleOauthException(OauthException exception) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(exception.getResponse());
    }
}
