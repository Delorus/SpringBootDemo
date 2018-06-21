package ru.tinkoff.demo.error;

public class EntityAlreadyExistException extends Exception {

    static final String MESSAGE_TEMPLATE = "%s with id=%s is already exist.";

    public EntityAlreadyExistException(String message) {
        super(message);
    }

    public EntityAlreadyExistException(String entityType, Long id) {
        super(String.format(MESSAGE_TEMPLATE, entityType, id));
    }
}
