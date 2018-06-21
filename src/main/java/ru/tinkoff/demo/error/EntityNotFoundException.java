package ru.tinkoff.demo.error;

public class EntityNotFoundException extends Exception {

    static final String MESSAGE_TEMPLATE = "%s with id=%s is not found.";

    public EntityNotFoundException(String message) {
        super(message);
    }

    public EntityNotFoundException(String entityType, Long id) {
        super(String.format(MESSAGE_TEMPLATE, entityType, id));
    }
}
