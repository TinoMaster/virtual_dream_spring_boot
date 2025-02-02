package com.tinomaster.virtualdream.virtualdream.dtos.response;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ResponseType<T> {
	private ResponseType() {
    }

    public static <T> ResponseEntity<ResponseBody<T>> ok(String message, T data) {
        return new ResponseEntity<>(ResponseBody.<T>builder()
                .status(HttpStatus.OK.value())
                .message(message)
                .data(data)
                .build(), HttpStatus.OK
        );
    }
    
    public static ResponseEntity<ResponseBody<Object>> ok(String message) {
        return new ResponseEntity<>(ResponseBody.builder()
                .status(HttpStatus.OK.value())
                .message(message)
                .build(), HttpStatus.OK
        );
    }

    public static ResponseEntity<ResponseBody<Object>> noContent() {
        return new ResponseEntity<>(ResponseBody.builder()
                .status(HttpStatus.NO_CONTENT.value())
                .build(), HttpStatus.NO_CONTENT
        );
    }

    public static <T>  ResponseEntity<ResponseBody<T>> badRequest(String message, T data) {
        return new ResponseEntity<>(ResponseBody.<T>builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .message(message)
                .data(data)
                .build(), HttpStatus.BAD_REQUEST
        );
    }

    public static ResponseEntity<ResponseBody<Object>> notFound(String message, Object data) {
        return new ResponseEntity<>(ResponseBody.builder()
                .status(HttpStatus.NOT_FOUND.value())
                .message(message)
                .data(data)
                .build(), HttpStatus.NOT_FOUND
        );
    }

    public static ResponseEntity<ResponseBody<Object>> internalServerError(String message, Object data) {
        return new ResponseEntity<>(ResponseBody.builder()
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .message(message)
                .data(data)
                .build(), HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

    public static ResponseEntity<ResponseBody<Object>> internalServerError() {
        return new ResponseEntity<>(ResponseBody.builder()
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .message(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase())
                .build(), HttpStatus.INTERNAL_SERVER_ERROR
        );
    }
}
