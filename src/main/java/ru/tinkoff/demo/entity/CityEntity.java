package ru.tinkoff.demo.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "city")
@Getter
@Setter
public class CityEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CityIdGenerator")
    @SequenceGenerator(name = "CityIdGenerator", sequenceName = "city_seq_id")
    private Long id;

    private String name;

    @Override
    public String toString() {
        return "City - id=" + this.id + " name=" + this.name;
    }

}