package ru.clevertec.exeption;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExeptionHandler {

    @ExceptionHandler
    @ResponseStatus(value=HttpStatus.NOT_FOUND, reason="No such Object")
    public void hendlerExeption(EntityNotFoundExeption entityNotFoundExeption) {
        MessageResponse messageResponse = new MessageResponse();
        messageResponse.setInfo(entityNotFoundExeption.getMessage());
    }

    @ExceptionHandler
    public ResponseEntity<MessageResponse> hendlerExeption(Exception exception) {
        MessageResponse messageResponse = new MessageResponse();
        messageResponse.setInfo(exception.getMessage());

        return new ResponseEntity<>(messageResponse, HttpStatus.BAD_REQUEST);
    }


}
