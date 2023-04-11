package ru.practicum.exceptions;

public class WrongDateTimeFormatException extends RuntimeException {
    public WrongDateTimeFormatException(String message) {
        super(message);
    }
}
