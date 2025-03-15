package com.java.person.handler;

import com.java.person.dto.response.AppResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ResponseHandler {
    public static ResponseEntity<Object> generateResponse(String message, HttpStatus status, Object responseObj) {
        AppResponse response = AppResponse.builder()
                .status(status.value())
                .message(message)
                .data(responseObj)
                .build();

        return new ResponseEntity<>(response, status);
    }
}
