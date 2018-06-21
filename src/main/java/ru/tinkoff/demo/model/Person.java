package ru.tinkoff.demo.model;

import java.util.List;
import lombok.Data;

@Data
public class Person {

    private Long id;
    private String firstName;
    private String lastName;
    private String parentName;
    private List<Transport> transports;
    private City city;
}
