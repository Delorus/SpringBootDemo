package ru.tinkoff.demo.repository;

import ru.tinkoff.demo.entity.CityEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CityRepository extends JpaRepository<CityEntity,Long> {

    CityEntity findByNameStartsWithIgnoreCase(String name);
}