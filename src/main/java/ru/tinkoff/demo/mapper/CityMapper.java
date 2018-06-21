package ru.tinkoff.demo.mapper;

import java.util.List;
import org.mapstruct.Mapper;
import ru.tinkoff.demo.dto.CityChangeRequest;
import ru.tinkoff.demo.entity.CityEntity;
import ru.tinkoff.demo.model.City;

@Mapper(componentModel="spring")
public interface CityMapper {

    List<City> mapEntitiesToModels(List<CityEntity> cityEntities);

    City entityToModel(CityEntity cityEntity);

    CityEntity modelToEntity(City city);

    CityEntity changeRequestToEntity(CityChangeRequest changeRequest);
}
