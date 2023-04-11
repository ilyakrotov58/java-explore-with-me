package ru.practicum.excpetionHandlers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.exceptions.WrongDateTimeFormatException;

@RestControllerAdvice
@Slf4j
public class ExceptionHandlers {

    @ExceptionHandler
    public ResponseEntity<String> handleWrongDateTimeFormatException(final WrongDateTimeFormatException e) {
        log.error("400 {}", e.getLocalizedMessage());
        return new ResponseEntity<>(e.getLocalizedMessage(), HttpStatus.BAD_REQUEST);
    }
}
