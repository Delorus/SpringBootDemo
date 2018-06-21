package ru.tinkoff.demo.model;

import lombok.Data;

@Data
public class Transport {

    private Long id;
    private String govnumber;
    private Model model;
    private Person person;
}
