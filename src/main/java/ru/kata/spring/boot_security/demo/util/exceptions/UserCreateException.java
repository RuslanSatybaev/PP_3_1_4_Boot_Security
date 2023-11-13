package ru.kata.spring.boot_security.demo.util.exceptions;

public class UserCreateException extends RuntimeException {
    public UserCreateException(String message) {
        super(message);
    }
}
