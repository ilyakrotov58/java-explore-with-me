package ru.practicum.exceptions;

public class UserIsNotEventOwnerException extends RuntimeException {
    public UserIsNotEventOwnerException(String message) {
        super(message);
    }
}
