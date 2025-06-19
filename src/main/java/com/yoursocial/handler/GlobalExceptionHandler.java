package com.yoursocial.handler;


import com.yoursocial.util.CommonUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@RequiredArgsConstructor
@Slf4j
public class GlobalExceptionHandler {

    private final CommonUtil util;


    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleException(Exception e) {
        log.error("Unhandled exception :{}", e.getMessage());
        return util.createErrorResponseMessage("An unexpected error occurred. Please try again later!", HttpStatus.INTERNAL_SERVER_ERROR);
    }


}
