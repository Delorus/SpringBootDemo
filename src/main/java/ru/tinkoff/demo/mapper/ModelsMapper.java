package ru.tinkoff.demo.mapper;

import java.util.List;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import ru.tinkoff.demo.dto.ModelChangeRequest;
import ru.tinkoff.demo.entity.ModelEntity;
import ru.tinkoff.demo.model.Model;

@Mapper(componentModel = "spring")

public interface ModelsMapper {

    @IterableMapping(qualifiedByName = "entityToSimpleModel")
    List<Model> mapEntitiesToModels(List<ModelEntity> modelEntities);

    @Named("entityToSimpleModel")
    @Mapping(target = "mark.name", ignore = true)
    Model entityToModel(ModelEntity modelEntity);

    Model entityToFullModel(ModelEntity modelEntity);

    ModelEntity modelToEntity(Model model);

    ModelEntity changeRequestToEntity(ModelChangeRequest changeRequest);

}
