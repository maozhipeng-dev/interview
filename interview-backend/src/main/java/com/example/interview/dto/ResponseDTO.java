package com.example.interview.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResponseDTO<T> {

    private Integer code;
    private String message;
    private T data;
    private Long total;

    public static <T> ResponseDTO<T> success(T data) {
        return new ResponseDTO<>(200, "success", data, null);
    }

    public static <T> ResponseDTO<T> success(T data, Long total) {
        return new ResponseDTO<>(200, "success", data, total);
    }

    public static <T> ResponseDTO<T> error(String message) {
        return new ResponseDTO<>(500, message, null, null);
    }

    public static <T> ResponseDTO<T> notFound(String message) {
        return new ResponseDTO<>(404, message, null, null);
    }

    public static <T> ResponseDTO<T> badRequest(String message) {
        return new ResponseDTO<>(400, message, null, null);
    }
}