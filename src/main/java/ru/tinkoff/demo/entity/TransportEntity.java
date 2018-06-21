package ru.tinkoff.demo.entity;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedAttributeNode;
import javax.persistence.NamedEntityGraph;
import javax.persistence.NamedEntityGraphs;
import javax.persistence.NamedSubgraph;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "transport")
@NamedEntityGraphs({
    @NamedEntityGraph(
        name = "TransportFull",
        attributeNodes = {
            @NamedAttributeNode(
                value = "person",
                subgraph = "person.city"
            ),
            @NamedAttributeNode(
                value = "model",
                subgraph = "model.mark"
            )
        },
        subgraphs = {
            @NamedSubgraph(
                name = "person.city",
                attributeNodes = @NamedAttributeNode("city")
            ),
            @NamedSubgraph(
                name = "model.mark",
                attributeNodes = @NamedAttributeNode("mark")
            )
        }
    ),
    @NamedEntityGraph(
        name = "TransportWithPerson",
        attributeNodes = @NamedAttributeNode(
            value = "person",
            subgraph = "person.city"
        ),
        subgraphs = @NamedSubgraph(
            name = "person.city",
            attributeNodes = @NamedAttributeNode("city")
        )
    )
})
@Getter
@Setter
public class TransportEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "TransportIdGenerator")
    @SequenceGenerator(name = "TransportIdGenerator", sequenceName = "transport_seq_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private PersonEntity person;

    @ManyToOne(fetch = FetchType.LAZY)
    private ModelEntity model;

    private String govnumber;

    @Override
    public String toString() {
        return "Transport - id=" + this.id + " govnomer=" + this.govnumber;
    }
}