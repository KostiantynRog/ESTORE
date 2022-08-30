package com.rog.EShop.exceptions;

//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.ExceptionHandler;
//import org.springframework.web.bind.annotation.RestControllerAdvice;
//import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
//
//@RestControllerAdvice
//
//public class RestApiExceptionHandler extends ResponseEntityExceptionHandler {
//    Logger logger = LoggerFactory.getLogger(RestApiExceptionHandler.class);
//    @ExceptionHandler
//    public ResponseEntity<AppError> handleNotFoundException(NotFoundException ex) {
//        logger.error(ex.getMessage());
//        return new ResponseEntity<>(new AppError(HttpStatus.NOT_FOUND.value(), ex.getMessage()), HttpStatus.NOT_FOUND);
//    }
//}
