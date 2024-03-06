package ru.clevertec.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionHandler {

    @org.springframework.web.bind.annotation.ExceptionHandler
    @ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "No such Object")
    public void handlerException(EntityNotFoundException entityNotFoundException) {
        MessageResponseException messageResponseException = new MessageResponseException();
        messageResponseException.setInfo(entityNotFoundException.getMessage());
    }

    @org.springframework.web.bind.annotation.ExceptionHandler
    @ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Already exists Object")
    public void handlerException(EntityAlreadyExistsException entityAlreadyExistsException) {
        MessageResponseException messageResponseException = new MessageResponseException();
        messageResponseException.setInfo(entityAlreadyExistsException.getMessage());
    }

    @org.springframework.web.bind.annotation.ExceptionHandler
    @ResponseStatus(value = HttpStatus.PRECONDITION_FAILED, reason = "Wrong data")
    public void handlerException(WrongDataException wrongDataException) {
        MessageResponseException messageResponseException = new MessageResponseException();
        messageResponseException.setInfo(wrongDataException.getMessage());
    }

    @org.springframework.web.bind.annotation.ExceptionHandler
    public ResponseEntity<MessageResponseException> handlerException(Exception exception) {
        MessageResponseException messageResponseException = new MessageResponseException();
        messageResponseException.setInfo(exception.getMessage());

        return new ResponseEntity<>(messageResponseException, HttpStatus.BAD_REQUEST);
    }


}
