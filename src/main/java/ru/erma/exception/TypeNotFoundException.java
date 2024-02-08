package ru.erma.exception;

public class TypeNotFoundException extends RuntimeException{
    public TypeNotFoundException(String message) {
        super(message);
    }
}
