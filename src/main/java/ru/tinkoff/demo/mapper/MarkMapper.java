package ru.tinkoff.demo.mapper;

import java.util.List;
import org.mapstruct.Mapper;
import ru.tinkoff.demo.dto.MarkChangeRequest;
import ru.tinkoff.demo.entity.MarkEntity;
import ru.tinkoff.demo.model.Mark;

@Mapper(componentModel = "spring")
public interface MarkMapper {

    List<Mark> mapEntitiesToModels(List<MarkEntity> markEntities);

    Mark entityToModel(MarkEntity markEntity);

    MarkEntity modelToEntity(Mark mark);

    MarkEntity changeRequestToEntity(MarkChangeRequest changeRequest);
}
