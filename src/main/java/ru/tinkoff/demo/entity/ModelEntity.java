package ru.tinkoff.demo.entity;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedEntityGraph;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "model")
@NamedEntityGraph(name = "ModelFull", includeAllAttributes = true)
@Getter
@Setter
public class ModelEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ModelIdGenerator")
    @SequenceGenerator(name = "ModelIdGenerator", sequenceName = "model_seq_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private MarkEntity mark;

    private String name;

    @Override
    public String toString() {
        return "Model - id=" + this.id + " name=" + this.name;
    }
}