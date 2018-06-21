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
@Table(name = "mark")
@Getter
@Setter
public class MarkEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "MarkIdGenerator")
    @SequenceGenerator(name = "MarkIdGenerator", sequenceName = "mark_seq_id")
    private Long id;

    private String name;

    @Override
    public String toString() {
        return "Mark - id=" + this.id + " name=" + this.name;
    }
}