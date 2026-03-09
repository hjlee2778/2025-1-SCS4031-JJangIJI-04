package com.jjangiji.hankkimoa.common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ExceptionCode {

    // 전체
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "예상치 못한 서버에러가 발생했습니다"),
    INVALID_PARAMETER(HttpStatus.BAD_REQUEST, "잘못된 인자입니다."),

    // 지출
    EXPENSE_NOT_FOUND(HttpStatus.BAD_REQUEST, "지출이 존재하지 않습니다."),
    EXPENSE_DATE_NOT_SAME(HttpStatus.INTERNAL_SERVER_ERROR, "지출일이 일치하지 않습니다."),
    EXPENSE_DATE_INVALID(HttpStatus.BAD_REQUEST, "지출일은 미래일 수 없습니다."),
    RATING_INVALID_FORMAT(HttpStatus.BAD_REQUEST, "평점은 0 ~ 5점 사이값이어야 합니다."),
    MEMO_INVALID_LENGTH(HttpStatus.BAD_REQUEST, "메모는 100자를 초과할 수 없습니다."),
    EMOJI_ALREADY_EXIST(HttpStatus.BAD_REQUEST, "이모지가 이미 존재합니다."),
    EMOJI_NOT_FOUND(HttpStatus.BAD_REQUEST, "이모지가 존재하지 않습니다"),

    // 지출 목표 금액
    EXPENSE_SAVING_GOAL_NOT_FOUND(HttpStatus.BAD_REQUEST, "지출 목표 금액이 존재하지 않습니다."),
    EXPENSE_SAVING_GOAL_ALREADY_EXIST(HttpStatus.BAD_REQUEST, "지출 목표 금액이 이미 존재합니다."),
    EXPENSE_SAVING_GOAL_NOT_OWNED(HttpStatus.BAD_REQUEST, "사용자의 지출 목표 금액이 아닙니다."),

    // 식당
    RESTAURANT_NOT_FOUND(HttpStatus.BAD_REQUEST, "식당이 존재하지 않습니다."),
    CATEGORY_NOT_FOUND(HttpStatus.BAD_REQUEST, "카테고리가 존재하지 않습니다."),
    RESTAURANT_DAYOFWEEK_INTERNAL_EXCEPTION(HttpStatus.INTERNAL_SERVER_ERROR, "식당 요일을 변환할 수 없습니다."),
    RECOMMEND_SERVER_INTERNAL_EXCEPTION(HttpStatus.INTERNAL_SERVER_ERROR, "추천 서버와 통신하는 과정 중 예상치 못한 예외가 발생했습니다."),
    RECOMMENDATION_FEEDBACK_INVALID_FORMAT(HttpStatus.BAD_REQUEST, "피드백 형태가 올바르지 않습니다."),

    // 돈
    MONEY_NEGATIVE(HttpStatus.BAD_REQUEST, "금액은 음수값을 가질 수 없습니다."),

    // 날짜
    DATE_RANGE_INVALID(HttpStatus.BAD_REQUEST, "시작일이 종료일보다 앞설 수 없습니다."),

    // 유저
    USER_NOT_FOUND(HttpStatus.BAD_REQUEST, "유저가 존재하지 않습니다."),
    OAUTH_REDIRECT_URI_MISMATCH(HttpStatus.BAD_REQUEST, "일치하는 Redirect URI가 존재하지 않습니다."),
    OAUTH_TOKEN_INTERNAL_EXCEPTION(HttpStatus.INTERNAL_SERVER_ERROR, "카카오 서버와 통신하는 과정 중 예상치 못한 예외가 발생했습니다."),
    AUTHENTICATION_ACCESS_TOKEN_EMPTY(HttpStatus.UNAUTHORIZED,
            "액세스 토큰이 존재하지 않습니다. 액세스 토큰을 발급해주세요."),
    AUTHENTICATION_REFRESH_TOKEN_EMPTY(HttpStatus.UNAUTHORIZED,
            "리프레시 토큰이 존재하지 않습니다. 다시 로그인해주세요."),
    AUTHENTICATION_TOKEN_EMPTY(HttpStatus.UNAUTHORIZED, "로그인이 필요한 사용자입니다."),
    AUTHENTICATION_TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "토큰이 만료되었습니다."),
    AUTHENTICATION_TOKEN_INVALID(HttpStatus.UNAUTHORIZED, "토큰 정보가 올바르지 않습니다."),
    AUTHENTICATION_TOKEN_USER_MISMATCH(HttpStatus.UNAUTHORIZED, "엑세스 토큰과 리프레시 토큰의 소유자가 다릅니다."),
    AUTHENTICATION_TOKEN_TYPE_MISMATCH(HttpStatus.BAD_REQUEST, "토큰 타입이 올바르지 않습니다."),

    //북마크
    BOOKMARK_EXISTS(HttpStatus.BAD_REQUEST, "즐겨찾기에 이미 추가된 식당입니다."),
    BOOKMARK_NOT_FOUND(HttpStatus.BAD_REQUEST, "즐겨찾기를 찾을 수 없습니다."),
    ;
  
    private final HttpStatus httpStatus;
    private final String message;
}
