package ru.tinkoff.demo.entity;

import java.util.List;
import javax.persistence.Column;
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
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "person")
@NamedEntityGraphs({
    @NamedEntityGraph(
        name = "PersonFull",
        attributeNodes =
            {
                @NamedAttributeNode(
                    value = "transports",
                    subgraph = "transports.model"
                ),
                @NamedAttributeNode(
                    value = "city"
                )
            },
        subgraphs = {
            @NamedSubgraph(
                name = "transports.model",
                attributeNodes = @NamedAttributeNode("model")
            )
        }
    )
})
@Getter
@Setter
public class PersonEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PersonIdGenerator")
    @SequenceGenerator(name = "PersonIdGenerator", sequenceName = "person_seq_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private CityEntity city;

    @Column(name = "firstname")
    private String firstName;

    @Column(name = "lastname")
    private String lastName;

    @Column(name = "parentname")
    private String parentName;

    @OneToMany(mappedBy = "person", fetch = FetchType.LAZY)
    private List<TransportEntity> transports;

    @Override
    public String toString() {
        return "Person - id=" + this.id + " firstname=" + this.firstName + " lastname="
            + this.lastName + "parentname=" + this.parentName;
    }
}