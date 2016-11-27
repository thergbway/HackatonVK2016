package com.ruber.controller.support;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class GlobalExceptionControllerAdvice {
    private static final Logger log = LoggerFactory.getLogger("EXCEPTION_CONTROLLER");

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public String handleUnauthorisedBackendExceptions(Exception ex, HttpServletResponse response) {
        log.error("Exception!", ex);

        response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        return ex.getMessage();
    }
}
