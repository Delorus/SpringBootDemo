package ru.tinkoff.demo.error;

public class FieldsConstraintsViolationException extends Exception {

    public FieldsConstraintsViolationException(String message) {
        super(message);
    }
}