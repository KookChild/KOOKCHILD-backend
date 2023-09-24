package com.service.kookchild.global.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class KookChildException extends RuntimeException{

    private final ExceptionStatus status;
}
