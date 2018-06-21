package ru.tinkoff.demo.service;

import java.util.List;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import ru.tinkoff.demo.dto.CityChangeRequest;
import ru.tinkoff.demo.entity.CityEntity;
import ru.tinkoff.demo.error.EntityNotFoundException;
import ru.tinkoff.demo.mapper.CityMapper;
import ru.tinkoff.demo.model.City;
import ru.tinkoff.demo.repository.CityRepository;

@Service
public class CityService {

    private final CityRepository cityRepository;
    private final CityMapper cityMapper;

    public CityService(CityRepository cityRepository, CityMapper cityMapper) {
        this.cityRepository = cityRepository;
        this.cityMapper = cityMapper;
    }

    public List<City> getAll() {

        return cityMapper.mapEntitiesToModels(cityRepository.findAll());
    }

    public City createCity(CityChangeRequest request) {
        final CityEntity cityEntity = cityMapper.changeRequestToEntity(request);

        return cityMapper.entityToModel(cityRepository.save(cityEntity));
    }

    public City updateCity(Long id, CityChangeRequest changeRequest) throws EntityNotFoundException {
        cityRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("City", id));

        final CityEntity cityEntity = cityMapper.changeRequestToEntity(changeRequest);
        cityEntity.setId(id);

        return cityMapper.entityToModel(cityRepository.saveAndFlush(cityEntity));
    }

    public void deleteCity(Long id) throws EntityNotFoundException {
        try {
            cityRepository.deleteById(id);
        } catch (EmptyResultDataAccessException ex) {
            throw new EntityNotFoundException("City", id);
        }
    }

    public City getCity(Long id) throws EntityNotFoundException {
        final CityEntity cityEntity = cityRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("City", id));

        return cityMapper.entityToModel(cityEntity);
    }

    public City getCityByName(String name) {
        final CityEntity cityEntity = cityRepository.findByNameStartsWithIgnoreCase(name);

        return cityMapper.entityToModel(cityEntity);
    }
}
