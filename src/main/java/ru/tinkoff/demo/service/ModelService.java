package ru.tinkoff.demo.service;

import java.util.List;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import ru.tinkoff.demo.dto.ModelChangeRequest;
import ru.tinkoff.demo.entity.ModelEntity;
import ru.tinkoff.demo.error.EntityNotFoundException;
import ru.tinkoff.demo.mapper.ModelsMapper;
import ru.tinkoff.demo.model.Model;
import ru.tinkoff.demo.repository.ModelRepository;

@Service
public class ModelService {

    private final ModelRepository modelRepository;
    private final ModelsMapper modelsMapper;

    public ModelService(ModelRepository modelRepository, ModelsMapper modelsMapper) {
        this.modelRepository = modelRepository;
        this.modelsMapper = modelsMapper;
    }

    public List<Model> getAll() {
        return modelsMapper.mapEntitiesToModels(modelRepository.findAll());
    }

    public Model createModel(ModelChangeRequest changeRequest) {
        final ModelEntity modelEntity = modelsMapper.changeRequestToEntity(changeRequest);

        return modelsMapper.entityToModel(modelRepository.save(modelEntity));
    }

    public Model updateModel(Long id, ModelChangeRequest changeRequest) throws EntityNotFoundException {
        modelRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Model", id));

        final ModelEntity modelEntity = modelsMapper.changeRequestToEntity(changeRequest);
        modelEntity.setId(id);

        return modelsMapper.entityToModel(modelRepository.saveAndFlush(modelEntity));
    }

    public void deleteModel(Long id) throws EntityNotFoundException {
        try {
            modelRepository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new EntityNotFoundException("Model", id);
        }
    }

    public Model getModel(Long id) throws EntityNotFoundException {
        final ModelEntity modelEntity = modelRepository.findFullById(id)
            .orElseThrow(() -> new EntityNotFoundException("Model", id));

        return modelsMapper.entityToFullModel(modelEntity);
    }
}
