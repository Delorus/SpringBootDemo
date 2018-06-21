package ru.tinkoff.demo.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import ru.tinkoff.demo.entity.ModelEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ModelRepository extends JpaRepository<ModelEntity,Long> {

    @EntityGraph("ModelFull")
    Optional<ModelEntity> findFullById(Long id);
}