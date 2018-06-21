package ru.tinkoff.demo.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.tinkoff.demo.entity.TransportEntity;

public interface TransportRepository extends JpaRepository<TransportEntity, Long> {

    @EntityGraph("TransportFull")
    Optional<TransportEntity> findByGovnumberIgnoreCase(String govnumber);

    @EntityGraph("TransportFull")
    Optional<TransportEntity> findFullById(Long id);

    List<TransportEntity> findAllByOrderByGovnumber();

    List<TransportEntity> findAllByPersonIdOrderByGovnumber(Long personId);
}