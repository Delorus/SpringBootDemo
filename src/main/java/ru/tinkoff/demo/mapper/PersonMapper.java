package ru.tinkoff.demo.mapper;

import java.util.List;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.Named;
import ru.tinkoff.demo.dto.PersonChangeRequest;
import ru.tinkoff.demo.entity.PersonEntity;
import ru.tinkoff.demo.entity.TransportEntity;
import ru.tinkoff.demo.model.Person;
import ru.tinkoff.demo.model.Transport;

@Mapper(componentModel = "spring")
public interface PersonMapper {

    @IterableMapping(qualifiedByName = "entityToSimpleModel")
    List<Person> mapEntitiesToModels(List<PersonEntity> PersonEntities);

    @Named("entityToSimpleModel")
    @Mappings({
        @Mapping(target = "city.name", ignore = true),
        @Mapping(target = "transports", ignore = true)
    })
    Person entityToModel(PersonEntity personEntity);

    List<Person> mapEntitiesToFullModels(List<PersonEntity> PersonEntities);

    Person entityToFullModel(PersonEntity personEntity);

    PersonEntity changeRequestToEntity(PersonChangeRequest changeRequest);

    @Mappings({
        @Mapping(target = "model.mark", ignore = true),
        @Mapping(target = "person.firstName", ignore = true),
        @Mapping(target = "person.lastName", ignore = true),
        @Mapping(target = "person.parentName", ignore = true),
        @Mapping(target = "person.transports", ignore = true)
    })
    Transport transportEntityToTransport(TransportEntity transportEntity);
}
