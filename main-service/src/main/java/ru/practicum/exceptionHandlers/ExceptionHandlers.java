package ru.practicum.exceptionHandlers;

import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.exceptions.NotFoundException;
import ru.practicum.exceptions.ConflictDataException;

@RestControllerAdvice
@Slf4j
public class ExceptionHandlers {

    private final Gson gson = new Gson();

    @ExceptionHandler
    public ResponseEntity<String> handleSameDataChangingException(final ConflictDataException e) {
        log.error("409 {}", e.getLocalizedMessage());
        return new ResponseEntity<>(gson.toJson(e.getLocalizedMessage()), HttpStatus.CONFLICT);
    }

    @ExceptionHandler
    public ResponseEntity<String> handleNotFoundException(final NotFoundException e) {
        log.error("404 {}", e.getLocalizedMessage());
        return new ResponseEntity<>(gson.toJson(e.getLocalizedMessage()), HttpStatus.NOT_FOUND);
    }
}
