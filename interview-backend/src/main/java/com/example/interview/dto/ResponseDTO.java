package com.example.interview.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "统一响应数据传输对象")
public class ResponseDTO<T> {

    @Schema(description = "响应状态码", example = "200")
    private Integer code;

    @Schema(description = "响应消息", example = "success")
    private String message;

    @Schema(description = "响应数据")
    private T data;

    @Schema(description = "数据总数", example = "100")
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