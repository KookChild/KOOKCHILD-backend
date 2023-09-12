package com.service.kookchild.global.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ExceptionStatus {

    BAD_REQUEST(HttpStatus.BAD_REQUEST, "잘못된 요청입니다."),
    NOT_EXIST_USER_EMAIL(HttpStatus.BAD_REQUEST, "존재하지 않는 유저의 이메일입니다.");

    private final HttpStatus httpStatus;
    private final String message;
}
