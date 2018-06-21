package ru.tinkoff.demo.service;

import java.util.List;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import ru.tinkoff.demo.dto.MarkChangeRequest;
import ru.tinkoff.demo.entity.MarkEntity;
import ru.tinkoff.demo.error.EntityNotFoundException;
import ru.tinkoff.demo.mapper.MarkMapper;
import ru.tinkoff.demo.model.Mark;
import ru.tinkoff.demo.repository.MarkRepository;

@Service
public class MarkService {

    private final MarkRepository markRepository;
    private final MarkMapper markMapper;

    public MarkService(MarkRepository markRepository, MarkMapper markMapper) {
        this.markRepository = markRepository;
        this.markMapper = markMapper;
    }

    public List<Mark> getAll() {
        return markMapper.mapEntitiesToModels(markRepository.findAll());
    }

    public Mark createMark(MarkChangeRequest changeRequest) {
        final MarkEntity markEntity = markMapper.changeRequestToEntity(changeRequest);

        return markMapper.entityToModel(markRepository.save(markEntity));
    }

    public Mark updateMark(Long id, MarkChangeRequest changeRequest) throws EntityNotFoundException {
        markRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Mark", id));

        final MarkEntity markEntity = markMapper.changeRequestToEntity(changeRequest);
        markEntity.setId(id);

        return markMapper.entityToModel(markRepository.saveAndFlush(markEntity));
    }

    public Mark getMark(Long id) throws EntityNotFoundException {
        final MarkEntity markEntity = markRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Mark", id));

        return markMapper.entityToModel(markEntity);
    }

    public void deleteMark(Long id) throws EntityNotFoundException {
        try {
            markRepository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new EntityNotFoundException("Mark", id);
        }
    }
}
