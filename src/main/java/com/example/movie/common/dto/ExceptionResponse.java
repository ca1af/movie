package com.example.movie.common.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class ExceptionResponse {

    private int status;
    private String error;
    private String message;


    /**
     * 생성자
     *
     * @param message 오류 메세지
     * @param status  오류 상태 코드
     */
    @Builder
    public ExceptionResponse(int status, String error, String message) {
        this.status = status;
        this.error = error;
        this.message = message;
    }
}
