package ru.tinkoff.demo.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import ru.tinkoff.demo.entity.PersonEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PersonRepository extends JpaRepository<PersonEntity,Long> {

    List<PersonEntity> findAllByOrderByLastName();

    @EntityGraph("PersonFull")
    List<PersonEntity>
    findAllByFirstNameStartsWithIgnoreCaseAndLastNameStartsWithIgnoreCaseAndParentNameStartsWithIgnoreCase(
        String firstName, String lastName, String parentName);

    @EntityGraph("PersonFull")
    Optional<PersonEntity> findFullById(Long id);
}