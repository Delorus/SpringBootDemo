package ru.tinkoff.demo.repository;

import ru.tinkoff.demo.entity.MarkEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MarkRepository extends JpaRepository<MarkEntity,Long> {
	
}