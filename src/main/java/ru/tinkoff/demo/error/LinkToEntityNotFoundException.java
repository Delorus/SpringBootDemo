package ru.tinkoff.demo.error;

public class LinkToEntityNotFoundException extends Exception {

    public LinkToEntityNotFoundException(String message) {
        super(message);
    }

    public LinkToEntityNotFoundException(String entityType, Object id) {
        super(String.format(EntityNotFoundException.MESSAGE_TEMPLATE, entityType, id));
    }
}
