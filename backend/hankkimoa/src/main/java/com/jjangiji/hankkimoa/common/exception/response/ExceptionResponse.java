package com.jjangiji.hankkimoa.common.exception.response;

public record ExceptionResponse(String httpMethod, String path, String exceptionCode, String message) {
}
