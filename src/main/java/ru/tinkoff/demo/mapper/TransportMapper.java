package ru.tinkoff.demo.mapper;

import java.util.List;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.Named;
import ru.tinkoff.demo.dto.TransportChangeRequest;
import ru.tinkoff.demo.dto.TransportCreateRequest;
import ru.tinkoff.demo.entity.TransportEntity;
import ru.tinkoff.demo.model.Transport;

@Mapper(componentModel = "spring")
public interface TransportMapper {

    @IterableMapping(qualifiedByName = "entityToSimpleModel")
    List<Transport> mapEntitiesToModels(List<TransportEntity> transportEntities);

    @Named("entityToSimpleModel")
    @Mappings({
        @Mapping(target = "model.mark", ignore = true),
        @Mapping(target = "model.name", ignore = true),
        @Mapping(target = "person.city", ignore = true),
        @Mapping(target = "person.firstName", ignore = true),
        @Mapping(target = "person.lastName", ignore = true),
        @Mapping(target = "person.parentName", ignore = true),
        @Mapping(target = "person.transports", ignore = true)
    })
    Transport entityToModel(TransportEntity transportEntity);

    @Mapping(target = "person.transports", ignore = true)
    Transport entityToFullModel(TransportEntity transportEntity);

    TransportEntity createRequestToEntity(TransportCreateRequest createRequest);

    TransportEntity changeRequestToEntity(TransportChangeRequest changeRequest);
}
