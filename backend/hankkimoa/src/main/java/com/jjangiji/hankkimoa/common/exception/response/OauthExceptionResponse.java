package com.jjangiji.hankkimoa.common.exception.response;

public record OauthExceptionResponse(String error, String error_description, String error_code) {
}
