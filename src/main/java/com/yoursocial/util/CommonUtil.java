package com.yoursocial.util;

import com.yoursocial.handler.GenericResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class CommonUtil {

    public ResponseEntity<?> createBuildResponse(Object data, HttpStatus httpStatusCode) {

        GenericResponse response = GenericResponse.builder()
                .responseStatusCode(httpStatusCode)
                .status("success")
                .message("fetching data")
                .data(data)
                .build();

        return response.create();
    }

    public ResponseEntity<?> createBuildResponseMessage(String message, HttpStatus httpStatusCode) {
        GenericResponse response = GenericResponse.builder()
                .responseStatusCode(httpStatusCode)
                .status("success")
                .message(message)
                .build();
        return response.create();
    }

    public ResponseEntity<?> createErrorResponse(Object data, HttpStatus httpStatusCode) {
        GenericResponse response = GenericResponse.builder()
                .responseStatusCode(httpStatusCode)
                .status("failed!")
                .message("something went wrong!")
                .data(data)
                .build();
        return response.create();
    }

    public ResponseEntity<?> createErrorResponseMessage(String message, HttpStatus httpStatusCode) {
        GenericResponse response = GenericResponse.builder()
                .responseStatusCode(httpStatusCode)
                .status("success")
                .message(message)
                .build();
        return response.create();
    }

}
