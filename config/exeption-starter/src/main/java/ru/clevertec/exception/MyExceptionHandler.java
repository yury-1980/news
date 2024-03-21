package ru.clevertec.exception;

import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class MyExceptionHandler {

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<MessageResponseException> handlerException(EntityNotFoundException entityNotFoundException) {
        MessageResponseException messageResponseException = new MessageResponseException();
        messageResponseException.setInfo(entityNotFoundException.getMessage());
        log.error(entityNotFoundException.getMessage());

        return new ResponseEntity<>(messageResponseException, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(EntityAlreadyExistsException.class)
    public ResponseEntity<MessageResponseException> handlerException(EntityAlreadyExistsException entityAlreadyExistsException) {
        MessageResponseException messageResponseException = new MessageResponseException();
        messageResponseException.setInfo(entityAlreadyExistsException.getMessage());
        log.error(entityAlreadyExistsException.getMessage());

        return new ResponseEntity<>(messageResponseException, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(WrongDataException.class)
    public ResponseEntity<MessageResponseException> handlerException(WrongDataException wrongDataException) {
        MessageResponseException messageResponseException = new MessageResponseException();
        messageResponseException.setInfo(wrongDataException.getMessage());
        log.error(wrongDataException.getMessage());

        return new ResponseEntity<>(messageResponseException, HttpStatus.PRECONDITION_FAILED);
    }

    @ExceptionHandler
    public ResponseEntity<MessageResponseException> handlerException(Exception exception) {
        MessageResponseException messageResponseException = new MessageResponseException();
        messageResponseException.setInfo(exception.getMessage());
        log.error(exception.getMessage());

        return new ResponseEntity<>(messageResponseException, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ValidationErrorResponse onMethodArgumentNotValidException(
            MethodArgumentNotValidException e
                                                                    ) {
        final List<Violation> violations = e.getBindingResult().getFieldErrors().stream()
                                            .map(error -> new Violation(error.getField(), error.getDefaultMessage()))
                                            .collect(Collectors.toList());
        return new ValidationErrorResponse(violations);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ValidationErrorResponse onConstraintValidationException(
            ConstraintViolationException e
                                                                  ) {
        final List<Violation> violations = e.getConstraintViolations().stream()
                                            .map(
                                                    violation -> new Violation(
                                                            violation.getPropertyPath().toString(),
                                                            violation.getMessage()
                                                    )
                                                )
                                            .collect(Collectors.toList());
        return new ValidationErrorResponse(violations);
    }
}
