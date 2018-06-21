package ru.tinkoff.demo.error;

public class TransportWithAlreadyExistingGovnumberException extends Exception {

    static final String MESSAGE_TEMPLATE = "Transport with govnomer=%s is already exist";

    public TransportWithAlreadyExistingGovnumberException(String govnumber) {
        super(String.format(MESSAGE_TEMPLATE, govnumber));
    }
}
